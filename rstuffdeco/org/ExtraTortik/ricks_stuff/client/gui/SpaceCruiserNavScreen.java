/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.AbstractWidget
 *  net.minecraft.client.gui.components.EditBox
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.sounds.SoundEvent
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton$Style
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetGuiUtils
 *  org.ExtraTortik.ricks_stuff.client.gui.SpaceCruiserNavScreen$ParsedCoords
 *  org.ExtraTortik.ricks_stuff.client.gui.SpaceCruiserNavScreen$ViewMode
 *  org.ExtraTortik.ricks_stuff.entity.CruiserWaypoint
 *  org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SAbortWarpPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SCruiserDeleteLocationPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SCruiserSaveLocationPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SInitiateWarpPacket
 *  org.ExtraTortik.ricks_stuff.portal.BlenderDimensionHelper
 *  org.ExtraTortik.ricks_stuff.portal.FishDimensionHelper
 *  org.ExtraTortik.ricks_stuff.portal.PortalPlacementHelper
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetButton;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetGuiUtils;
import org.ExtraTortik.ricks_stuff.client.gui.SpaceCruiserNavScreen;
import org.ExtraTortik.ricks_stuff.entity.CruiserWaypoint;
import org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.C2SAbortWarpPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SCruiserDeleteLocationPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SCruiserSaveLocationPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SInitiateWarpPacket;
import org.ExtraTortik.ricks_stuff.portal.BlenderDimensionHelper;
import org.ExtraTortik.ricks_stuff.portal.FishDimensionHelper;
import org.ExtraTortik.ricks_stuff.portal.PortalPlacementHelper;

public class SpaceCruiserNavScreen
extends Screen {
    private final SpaceCruiserEntity ship;
    private ViewMode currentView = ViewMode.NAVIGATION;
    private final List<String> availableDimensions = new ArrayList<String>();
    private int selectedDimIndex = 0;
    private String selectedDimension = "minecraft:overworld";
    private EditBox coordsInput;
    private GadgetButton btnDimPrev;
    private GadgetButton btnDimDisplay;
    private GadgetButton btnDimNext;
    private GadgetButton btnCurrentPos;
    private GadgetButton btnQuickSave;
    private EditBox searchBox;
    private GadgetButton btnScrollUp;
    private GadgetButton btnScrollDown;
    private int scrollOffset = 0;
    private static final int ITEMS_PER_PAGE = 3;
    private final List<CruiserWaypoint> filteredWaypoints = new ArrayList<CruiserWaypoint>();
    private final List<AbstractWidget> dynamicRowWidgets = new ArrayList<AbstractWidget>();
    private boolean isNamingWaypoint = false;
    private boolean savingCurrentPos = true;
    private EditBox nameInputBox;
    private GadgetButton btnConfirmSave;
    private GadgetButton btnCancelSave;
    private GadgetButton btnEngage;
    private GadgetButton btnToggleView;
    private GadgetButton btnAbort;
    private String statusMessage = "";
    private int statusColor = -12720539;

    public SpaceCruiserNavScreen(SpaceCruiserEntity ship) {
        super((Component)Component.literal((String)"Space Cruiser Navigation Computer"));
        this.ship = ship;
        if (ship == null) return;
        String shipTargetDim = ship.getTargetDimension();
        if (shipTargetDim != null && !shipTargetDim.isEmpty()) {
            this.selectedDimension = shipTargetDim;
        } else {
            if (ship.level() == null) return;
            this.selectedDimension = ship.level().dimension().location().toString();
        }
    }

    public SpaceCruiserEntity getShip() {
        return this.ship;
    }

    protected void init() {
        int initZ;
        super.init();
        this.populateDimensions();
        int panelWidth = 320;
        int panelHeight = 224;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;
        int termX = left + 8;
        int termY = top + 26;
        int initX = this.ship != null ? (int)Math.round(this.ship.getX()) : 0;
        int initY = this.ship != null ? (int)Math.round(this.ship.getY()) : 100;
        int n = initZ = this.ship != null ? (int)Math.round(this.ship.getZ()) : 0;
        if (this.ship != null && (this.ship.getTargetX() != 0.0 || this.ship.getTargetY() != 0.0 || this.ship.getTargetZ() != 0.0)) {
            initX = (int)Math.round(this.ship.getTargetX());
            initY = (int)Math.round(this.ship.getTargetY());
            initZ = (int)Math.round(this.ship.getTargetZ());
        }
        String initialCoordsText = initX + " " + initY + " " + initZ;
        int dimRowY = termY + 42;
        this.btnDimPrev = GadgetButton.gadget((Component)Component.literal((String)"<"), b -> this.cycleDimension(-1)).bounds(termX + 6, dimRowY, 18, 18).style(GadgetButton.Style.NORMAL).build();
        this.addRenderableWidget((GuiEventListener)this.btnDimPrev);
        this.btnDimDisplay = GadgetButton.gadget((Component)this.getDimensionDisplayComponent(), b -> this.cycleDimension(1)).bounds(termX + 26, dimRowY, 252, 18).style(GadgetButton.Style.NORMAL).build();
        this.addRenderableWidget((GuiEventListener)this.btnDimDisplay);
        this.btnDimNext = GadgetButton.gadget((Component)Component.literal((String)">"), b -> this.cycleDimension(1)).bounds(termX + 280, dimRowY, 18, 18).style(GadgetButton.Style.NORMAL).build();
        this.addRenderableWidget((GuiEventListener)this.btnDimNext);
        this.coordsInput = new EditBox(this.font, termX + 32, termY + 74, 240, 18, (Component)Component.literal((String)"Target Coordinates"));
        this.coordsInput.setValue(initialCoordsText);
        this.coordsInput.setHint((Component)Component.literal((String)"X Y Z (e.g. 100 64 -200)").withStyle(ChatFormatting.DARK_GRAY));
        this.coordsInput.setMaxLength(64);
        this.coordsInput.setTextColor(-12720539);
        this.coordsInput.setResponder(s -> {
            this.statusMessage = "";
        });
        this.addRenderableWidget((GuiEventListener)this.coordsInput);
        int actionBtnY = termY + 96;
        this.btnCurrentPos = GadgetButton.gadget((Component)Component.literal((String)"CURRENT POS"), b -> this.lockCurrentShipPosition()).bounds(termX + 64, actionBtnY, 84, 18).style(GadgetButton.Style.NORMAL).build();
        this.addRenderableWidget((GuiEventListener)this.btnCurrentPos);
        this.btnQuickSave = GadgetButton.gadget((Component)Component.literal((String)"SAVE POS"), b -> this.startSavingWaypoint(true)).bounds(termX + 156, actionBtnY, 84, 18).style(GadgetButton.Style.NORMAL).build();
        this.addRenderableWidget((GuiEventListener)this.btnQuickSave);
        int wpTopY = termY + 38;
        this.searchBox = new EditBox(this.font, termX + 6, wpTopY, 244, 16, (Component)Component.literal((String)"Search"));
        this.searchBox.setHint((Component)Component.literal((String)"Search waypoints...").withStyle(ChatFormatting.DARK_GRAY));
        this.searchBox.setResponder(s -> {
            this.filterWaypoints();
            this.rebuildWaypointRows();
        });
        this.addRenderableWidget((GuiEventListener)this.searchBox);
        this.btnScrollUp = GadgetButton.gadget((Component)Component.literal((String)"^"), b -> this.scrollWaypoints(-1)).bounds(termX + 254, wpTopY, 20, 16).style(GadgetButton.Style.NORMAL).build();
        this.addRenderableWidget((GuiEventListener)this.btnScrollUp);
        this.btnScrollDown = GadgetButton.gadget((Component)Component.literal((String)"v"), b -> this.scrollWaypoints(1)).bounds(termX + 276, wpTopY, 20, 16).style(GadgetButton.Style.NORMAL).build();
        this.addRenderableWidget((GuiEventListener)this.btnScrollDown);
        int saveRowY = termY + 130;
        this.nameInputBox = new EditBox(this.font, termX + 6, saveRowY, 172, 18, (Component)Component.literal((String)"Waypoint Name"));
        this.nameInputBox.setHint((Component)Component.literal((String)"Waypoint Name...").withStyle(ChatFormatting.DARK_GRAY));
        this.nameInputBox.setMaxLength(32);
        this.nameInputBox.setTextColor(-12720539);
        this.addRenderableWidget((GuiEventListener)this.nameInputBox);
        this.btnConfirmSave = GadgetButton.gadget((Component)Component.literal((String)"SAVE"), b -> this.confirmSaveWaypoint()).bounds(termX + 182, saveRowY, 56, 18).style(GadgetButton.Style.PRIMARY).build();
        this.addRenderableWidget((GuiEventListener)this.btnConfirmSave);
        this.btnCancelSave = GadgetButton.gadget((Component)Component.literal((String)"CANCEL"), b -> this.cancelSaveWaypoint()).bounds(termX + 242, saveRowY, 56, 18).style(GadgetButton.Style.NORMAL).build();
        this.addRenderableWidget((GuiEventListener)this.btnCancelSave);
        int actionRowY = top + 188;
        this.btnEngage = GadgetButton.gadget((Component)Component.literal((String)"ENGAGE HYPERDRIVE"), b -> this.onEngagePressed()).bounds(left + 10, actionRowY, 142, 24).style(GadgetButton.Style.PRIMARY).build();
        this.addRenderableWidget((GuiEventListener)this.btnEngage);
        this.btnToggleView = GadgetButton.gadget((Component)Component.literal((String)"WAYPOINTS"), b -> this.toggleViewMode()).bounds(left + 156, actionRowY, 86, 24).style(GadgetButton.Style.NORMAL).build();
        this.addRenderableWidget((GuiEventListener)this.btnToggleView);
        boolean warping = this.ship != null && this.ship.isWarpActive();
        MutableComponent abortText = warping ? Component.literal((String)"ABORT") : Component.literal((String)"CLOSE");
        GadgetButton.Style abortStyle = warping ? GadgetButton.Style.DANGER : GadgetButton.Style.NORMAL;
        this.btnAbort = GadgetButton.gadget((Component)abortText, b -> this.onAbortPressed()).bounds(left + 246, actionRowY, 64, 24).style(abortStyle).build();
        this.addRenderableWidget((GuiEventListener)this.btnAbort);
        this.filterWaypoints();
        this.updateWidgetVisibility();
    }

    private void updateWidgetVisibility() {
        boolean isNav = this.currentView == ViewMode.NAVIGATION;
        this.btnDimPrev.visible = isNav && !this.isNamingWaypoint;
        this.btnDimDisplay.visible = isNav && !this.isNamingWaypoint;
        this.btnDimNext.visible = isNav && !this.isNamingWaypoint;
        this.coordsInput.visible = isNav && !this.isNamingWaypoint;
        this.btnCurrentPos.visible = isNav && !this.isNamingWaypoint;
        this.btnQuickSave.visible = isNav && !this.isNamingWaypoint;
        this.searchBox.visible = !isNav && !this.isNamingWaypoint;
        this.btnScrollUp.visible = !isNav && !this.isNamingWaypoint && this.scrollOffset > 0;
        this.btnScrollDown.visible = !isNav && !this.isNamingWaypoint && this.scrollOffset + 3 < this.filteredWaypoints.size();
        this.nameInputBox.visible = this.isNamingWaypoint;
        this.btnConfirmSave.visible = this.isNamingWaypoint;
        this.btnCancelSave.visible = this.isNamingWaypoint;
        if (this.isNamingWaypoint) {
            this.setFocused((GuiEventListener)this.nameInputBox);
        } else if (isNav) {
            this.setFocused((GuiEventListener)this.coordsInput);
        }
        if (this.btnToggleView != null) {
            this.btnToggleView.setMessage((Component)(isNav ? Component.literal((String)"WAYPOINTS") : Component.literal((String)"NAV COMPUTER")));
        }
        this.rebuildWaypointRows();
    }

    private void toggleViewMode() {
        this.playDialSound();
        this.isNamingWaypoint = false;
        this.currentView = this.currentView == ViewMode.NAVIGATION ? ViewMode.WAYPOINTS : ViewMode.NAVIGATION;
        this.statusMessage = "";
        this.filterWaypoints();
        this.updateWidgetVisibility();
    }

    public void refreshWaypoints() {
        this.filterWaypoints();
        this.rebuildWaypointRows();
    }

    private void filterWaypoints() {
        this.filteredWaypoints.clear();
        List waypoints = this.ship != null ? this.ship.getSavedWaypoints() : List.of();
        String query = this.searchBox != null ? this.searchBox.getValue().trim().toLowerCase() : "";
        Iterator iterator = waypoints.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                int maxScroll = Math.max(0, this.filteredWaypoints.size() - 3);
                if (this.scrollOffset <= maxScroll) return;
                this.scrollOffset = maxScroll;
                return;
            }
            CruiserWaypoint wp = (CruiserWaypoint)iterator.next();
            if (!query.isEmpty() && !wp.name().toLowerCase().contains(query) && !wp.dimension().toLowerCase().contains(query)) continue;
            this.filteredWaypoints.add(wp);
        }
    }

    private void scrollWaypoints(int delta) {
        int maxScroll = Math.max(0, this.filteredWaypoints.size() - 3);
        int newOffset = Math.max(0, Math.min(maxScroll, this.scrollOffset + delta));
        if (newOffset == this.scrollOffset) return;
        this.scrollOffset = newOffset;
        this.playDialSound();
        this.updateWidgetVisibility();
    }

    private void rebuildWaypointRows() {
        int index;
        for (AbstractWidget w : this.dynamicRowWidgets) {
            this.removeWidget((GuiEventListener)w);
        }
        this.dynamicRowWidgets.clear();
        if (this.currentView != ViewMode.WAYPOINTS) return;
        if (this.isNamingWaypoint) {
            return;
        }
        int panelWidth = 320;
        int panelHeight = 224;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;
        int termX = left + 8;
        int termY = top + 26;
        int rowStartY = termY + 58;
        int rowHeight = 23;
        int count = Math.min(3, this.filteredWaypoints.size() - this.scrollOffset);
        for (int i = 0; i < count && (index = this.scrollOffset + i) < this.filteredWaypoints.size(); ++i) {
            CruiserWaypoint wp = this.filteredWaypoints.get(index);
            int rowY = rowStartY + i * (rowHeight + 2);
            GadgetButton btnLoad = GadgetButton.gadget((Component)Component.literal((String)"LOAD"), b -> this.loadWaypointIntoNav(wp)).bounds(termX + 208, rowY + 2, 46, 18).style(GadgetButton.Style.PRIMARY).build();
            this.dynamicRowWidgets.add((AbstractWidget)btnLoad);
            this.addRenderableWidget((GuiEventListener)btnLoad);
            GadgetButton btnDel = GadgetButton.gadget((Component)Component.literal((String)"DEL"), b -> this.deleteWaypoint(wp)).bounds(termX + 258, rowY + 2, 38, 18).style(GadgetButton.Style.DANGER).build();
            this.dynamicRowWidgets.add((AbstractWidget)btnDel);
            this.addRenderableWidget((GuiEventListener)btnDel);
        }
        int addRowY = termY + 132;
        GadgetButton btnAddCurrent = GadgetButton.gadget((Component)Component.literal((String)"SAVE SHIP POS"), b -> this.startSavingWaypoint(true)).bounds(termX + 6, addRowY, 142, 18).style(GadgetButton.Style.NORMAL).build();
        this.dynamicRowWidgets.add((AbstractWidget)btnAddCurrent);
        this.addRenderableWidget((GuiEventListener)btnAddCurrent);
        GadgetButton btnAddTarget = GadgetButton.gadget((Component)Component.literal((String)"SAVE TARGET POS"), b -> this.startSavingWaypoint(false)).bounds(termX + 152, addRowY, 144, 18).style(GadgetButton.Style.NORMAL).build();
        this.dynamicRowWidgets.add((AbstractWidget)btnAddTarget);
        this.addRenderableWidget((GuiEventListener)btnAddTarget);
    }

    private void loadWaypointIntoNav(CruiserWaypoint wp) {
        this.playDialSound();
        this.selectedDimension = wp.dimension();
        int idx = this.availableDimensions.indexOf(wp.dimension());
        if (idx >= 0) {
            this.selectedDimIndex = idx;
        } else {
            this.availableDimensions.add(wp.dimension());
            this.selectedDimIndex = this.availableDimensions.indexOf(wp.dimension());
        }
        this.updateDimensionDisplayButton();
        if (this.coordsInput != null) {
            this.coordsInput.setValue((int)Math.round(wp.x()) + " " + (int)Math.round(wp.y()) + " " + (int)Math.round(wp.z()));
        }
        this.statusMessage = "\u00a7aWAYPOINT LOADED: " + wp.name();
        this.statusColor = -12720539;
        this.currentView = ViewMode.NAVIGATION;
        this.updateWidgetVisibility();
    }

    private void deleteWaypoint(CruiserWaypoint wp) {
        this.playPressSound();
        if (this.ship == null) return;
        ModMessages.sendToServer((CustomPacketPayload)new C2SCruiserDeleteLocationPacket(this.ship.getId(), wp.id()));
        this.ship.removeSavedWaypoint(wp.id());
        this.statusMessage = "\u00a7aWAYPOINT REMOVED";
        this.statusColor = -12720539;
        this.filterWaypoints();
        this.rebuildWaypointRows();
    }

    private void startSavingWaypoint(boolean fromCurrentShipPos) {
        Object defaultName;
        this.playDialSound();
        this.savingCurrentPos = fromCurrentShipPos;
        this.isNamingWaypoint = true;
        if (fromCurrentShipPos) {
            int curX = this.ship != null ? (int)Math.round(this.ship.getX()) : 0;
            int curY = this.ship != null ? (int)Math.round(this.ship.getY()) : 0;
            int curZ = this.ship != null ? (int)Math.round(this.ship.getZ()) : 0;
            defaultName = "Beacon (" + curX + ", " + curY + ", " + curZ + ")";
        } else {
            ParsedCoords coords = this.parseEnteredCoordinates();
            defaultName = coords.valid ? "Waypoint (" + (int)coords.x + ", " + (int)coords.y + ", " + (int)coords.z + ")" : "Sector Marker";
        }
        this.nameInputBox.setValue((String)defaultName);
        this.statusMessage = "\u00a7eENTER WAYPOINT IDENTIFIER";
        this.statusColor = -19674;
        this.updateWidgetVisibility();
    }

    private void confirmSaveWaypoint() {
        String saveDim;
        double saveZ;
        double saveY;
        double saveX;
        if (this.ship == null) {
            return;
        }
        Object name = this.nameInputBox.getValue().trim();
        if (((String)name).isEmpty()) {
            name = "Waypoint #" + (this.ship.getSavedWaypoints().size() + 1);
        }
        if (this.savingCurrentPos) {
            saveX = Math.round(this.ship.getX());
            saveY = Math.round(this.ship.getY());
            saveZ = Math.round(this.ship.getZ());
            saveDim = this.ship.level() != null ? this.ship.level().dimension().location().toString() : "minecraft:overworld";
        } else {
            ParsedCoords coords = this.parseEnteredCoordinates();
            if (!coords.valid) {
                this.statusMessage = "\u00a7c" + coords.error;
                this.statusColor = -43691;
                this.playMalfunctionSound();
                return;
            }
            saveX = coords.x;
            saveY = coords.y;
            saveZ = coords.z;
            saveDim = this.selectedDimension;
        }
        this.playPressSound();
        CruiserWaypoint newWaypoint = new CruiserWaypoint((String)name, saveX, saveY, saveZ, saveDim);
        this.ship.addSavedWaypoint(newWaypoint);
        ModMessages.sendToServer((CustomPacketPayload)new C2SCruiserSaveLocationPacket(this.ship.getId(), (String)name, saveX, saveY, saveZ, saveDim));
        this.statusMessage = "\u00a7aWAYPOINT SAVED: " + (String)name;
        this.statusColor = -12720539;
        this.isNamingWaypoint = false;
        this.filterWaypoints();
        this.updateWidgetVisibility();
    }

    private void cancelSaveWaypoint() {
        this.playDialSound();
        this.isNamingWaypoint = false;
        this.statusMessage = "";
        this.updateWidgetVisibility();
    }

    private void lockCurrentShipPosition() {
        String currentDim;
        this.playDialSound();
        if (this.ship == null) return;
        int curX = (int)Math.round(this.ship.getX());
        int curY = (int)Math.round(this.ship.getY());
        int curZ = (int)Math.round(this.ship.getZ());
        this.coordsInput.setValue(curX + " " + curY + " " + curZ);
        this.selectedDimension = currentDim = this.ship.level().dimension().location().toString();
        int idx = this.availableDimensions.indexOf(currentDim);
        if (idx >= 0) {
            this.selectedDimIndex = idx;
        } else {
            this.availableDimensions.add(currentDim);
            this.selectedDimIndex = this.availableDimensions.indexOf(currentDim);
        }
        this.updateDimensionDisplayButton();
        this.statusMessage = "\u00a7aGPS LOCK: CURRENT SHIP VECTOR";
        this.statusColor = -12720539;
    }

    private void populateDimensions() {
        this.availableDimensions.clear();
        Minecraft mc = Minecraft.getInstance();
        if (mc.getConnection() != null) {
            Set levels = mc.getConnection().levels();
            for (ResourceKey key : levels) {
                String dimId = key.location().toString();
                if (this.availableDimensions.contains(dimId)) continue;
                this.availableDimensions.add(dimId);
            }
        }
        if (!this.availableDimensions.contains("minecraft:overworld")) {
            this.availableDimensions.add("minecraft:overworld");
        }
        if (!this.availableDimensions.contains("minecraft:the_nether")) {
            this.availableDimensions.add("minecraft:the_nether");
        }
        if (!this.availableDimensions.contains("minecraft:the_end")) {
            this.availableDimensions.add("minecraft:the_end");
        }
        this.availableDimensions.sort((a, b) -> {
            int rankB;
            int rankA = this.getDimensionRank((String)a);
            if (rankA == (rankB = this.getDimensionRank((String)b))) return a.compareToIgnoreCase((String)b);
            return Integer.compare(rankA, rankB);
        });
        this.selectedDimIndex = this.availableDimensions.indexOf(this.selectedDimension);
        if (this.selectedDimIndex >= 0) return;
        this.selectedDimIndex = 0;
        this.selectedDimension = this.availableDimensions.get(0);
    }

    private int getDimensionRank(String dim) {
        if (dim.equals("minecraft:overworld")) {
            return 0;
        }
        if (dim.equals("minecraft:the_nether")) return 1;
        if (dim.equals("the_nether")) return 1;
        if (dim.equals("nether")) {
            return 1;
        }
        if (dim.equals("minecraft:the_end")) return 2;
        if (dim.equals("the_end")) return 2;
        if (!dim.equals("end")) return 10;
        return 2;
    }

    private void cycleDimension(int direction) {
        if (this.availableDimensions.isEmpty()) {
            return;
        }
        this.selectedDimIndex = (this.selectedDimIndex + direction + this.availableDimensions.size()) % this.availableDimensions.size();
        this.selectedDimension = this.availableDimensions.get(this.selectedDimIndex);
        this.updateDimensionDisplayButton();
        this.playDialSound();
    }

    private Component getDimensionDisplayComponent() {
        String formatted = PortalPlacementHelper.formatDimensionName((String)this.selectedDimension);
        String count = " (" + (this.selectedDimIndex + 1) + "/" + this.availableDimensions.size() + ")";
        int maxNameWidth = 236 - this.font.width(count);
        String displayName = this.font.width(formatted) <= maxNameWidth ? formatted : this.font.plainSubstrByWidth(formatted, Math.max(10, maxNameWidth - this.font.width("..."))) + "...";
        return Component.literal((String)(displayName + count));
    }

    private void updateDimensionDisplayButton() {
        if (this.btnDimDisplay == null) return;
        this.btnDimDisplay.setMessage(this.getDimensionDisplayComponent());
    }

    private ParsedCoords parseEnteredCoordinates() {
        if (this.coordsInput == null) {
            return new ParsedCoords(0.0, 0.0, 0.0, false, "COORDINATES FIELD MISSING");
        }
        String raw = this.coordsInput.getValue().trim();
        if (raw.isEmpty()) {
            return new ParsedCoords(0.0, 0.0, 0.0, false, "ENTER TARGET X Y Z");
        }
        if (raw.startsWith("(") && raw.endsWith(")") && raw.length() > 2) {
            raw = raw.substring(1, raw.length() - 1).trim();
        } else if (raw.startsWith("[") && raw.endsWith("]") && raw.length() > 2) {
            raw = raw.substring(1, raw.length() - 1).trim();
        }
        String clean = raw.replaceAll("[,;/\t]+", " ").trim();
        String[] parts = clean.split("\\s+");
        if (parts.length < 3) {
            return new ParsedCoords(0.0, 0.0, 0.0, false, "ENTER X Y Z SEPARATED BY SPACE");
        }
        double shipX = this.ship != null ? this.ship.getX() : 0.0;
        double shipY = this.ship != null ? this.ship.getY() : 100.0;
        double shipZ = this.ship != null ? this.ship.getZ() : 0.0;
        try {
            double x = this.parseCoordComponent(parts[0], shipX);
            double y = this.parseCoordComponent(parts[1], shipY);
            double z = this.parseCoordComponent(parts[2], shipZ);
            if (y < -64.0) return new ParsedCoords(0.0, 0.0, 0.0, false, "Y MUST BE BETWEEN -64 AND 320");
            if (!(y > 320.0)) return new ParsedCoords(x, y, z, true, null);
            return new ParsedCoords(0.0, 0.0, 0.0, false, "Y MUST BE BETWEEN -64 AND 320");
        }
        catch (NumberFormatException e) {
            return new ParsedCoords(0.0, 0.0, 0.0, false, "INVALID NUMERIC COORDINATES");
        }
    }

    private double parseCoordComponent(String token, double origin) throws NumberFormatException {
        if (!token.startsWith("~")) return Double.parseDouble(token);
        String offsetStr = token.substring(1).trim();
        if (!offsetStr.isEmpty()) return origin + Double.parseDouble(offsetStr);
        return origin;
    }

    private void onEngagePressed() {
        ParsedCoords coords = this.parseEnteredCoordinates();
        if (!coords.valid) {
            this.statusMessage = "\u00a7c" + coords.error;
            this.statusColor = -43691;
            this.playMalfunctionSound();
            return;
        }
        if (this.ship == null || !this.ship.isAlive()) {
            this.statusMessage = "\u00a7cNO SHIP TELEMETRY LINK";
            this.statusColor = -43691;
            this.playMalfunctionSound();
            return;
        }
        if (!this.ship.isEngineOn()) {
            this.statusMessage = "CRITICAL: ENGINE OFFLINE";
            this.statusColor = -43691;
            this.playMalfunctionSound();
            return;
        }
        if (this.ship.isBroken()) {
            this.statusMessage = "\u00a7cCRITICAL: HULL REPAIRS REQUIRED";
            this.statusColor = -43691;
            this.playMalfunctionSound();
            return;
        }
        if (this.ship.isCrashing()) {
            this.statusMessage = "\u00a7cCRITICAL: ENGINE ENGULFED IN FLAMES";
            this.statusColor = -43691;
            this.playMalfunctionSound();
            return;
        }
        if (this.ship.getEnergy() < 25.0f) {
            this.statusMessage = "\u00a7cINSUFFICIENT ENERGY (MIN 25% REQUIRED)";
            this.statusColor = -43691;
            this.playMalfunctionSound();
            return;
        }
        this.playPressSound();
        ModMessages.sendToServer((CustomPacketPayload)new C2SInitiateWarpPacket(coords.x, coords.y, coords.z, this.selectedDimension));
        this.onClose();
    }

    private void onAbortPressed() {
        if (this.ship != null && this.ship.isWarpActive()) {
            ModMessages.sendToServer((CustomPacketPayload)new C2SAbortWarpPacket());
        }
        this.playPressSound();
        this.onClose();
    }

    private void playPressSound() {
        if (this.minecraft == null) return;
        if (this.minecraft.player == null) return;
        this.minecraft.player.playSound((SoundEvent)ModSounds.UI_PRESS.get(), 1.0f, 1.0f);
    }

    private void playDialSound() {
        if (this.minecraft == null) return;
        if (this.minecraft.player == null) return;
        this.minecraft.player.playSound((SoundEvent)ModSounds.UI_DIAL.get(), 1.0f, 1.0f);
    }

    private void playMalfunctionSound() {
        if (this.minecraft == null) return;
        if (this.minecraft.player == null) return;
        this.minecraft.player.playSound((SoundEvent)ModSounds.GUN_MALFUNCTION.get(), 1.0f, 1.0f);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 257 || keyCode == 335) {
            if (this.isNamingWaypoint) {
                this.confirmSaveWaypoint();
                return true;
            }
            if (this.currentView == ViewMode.NAVIGATION) {
                this.onEngagePressed();
                return true;
            }
        }
        if (keyCode != 256) return super.keyPressed(keyCode, scanCode, modifiers);
        if (!this.isNamingWaypoint) return super.keyPressed(keyCode, scanCode, modifiers);
        this.cancelSaveWaypoint();
        return true;
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (this.currentView != ViewMode.WAYPOINTS) return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        if (this.isNamingWaypoint) return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        this.scrollWaypoints(scrollY > 0.0 ? -1 : 1);
        return true;
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int engineColor;
        String engineStatus;
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        int panelWidth = 320;
        int panelHeight = 224;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;
        GadgetGuiUtils.renderChassis((GuiGraphics)guiGraphics, (int)left, (int)top, (int)panelWidth, (int)panelHeight);
        GadgetGuiUtils.renderHeader((GuiGraphics)guiGraphics, (int)(left + 4), (int)(top + 4), (int)(panelWidth - 8), (int)20);
        guiGraphics.drawString(this.font, "SPACE CRUISER // MULTIVERSE NAV COMPUTER", left + 10, top + 8, -12720539, false);
        GadgetGuiUtils.renderLed((GuiGraphics)guiGraphics, (int)(left + panelWidth - 14), (int)(top + 10), (int)-12720539);
        int termX = left + 8;
        int termY = top + 26;
        int termW = 304;
        int termH = 154;
        GadgetGuiUtils.renderTerminalScreen((GuiGraphics)guiGraphics, (int)termX, (int)termY, (int)termW, (int)termH);
        for (int sy = termY + 1; sy < termY + termH - 1; sy += 2) {
            guiGraphics.fill(termX + 1, sy, termX + termW - 1, sy + 1, 0x1A000000);
        }
        long time = System.currentTimeMillis();
        int sweepY = termY + 1 + (int)(time / 25L % (long)(termH - 2));
        guiGraphics.fill(termX + 1, sweepY, termX + termW - 1, sweepY + 1, 238937701);
        int curX = this.ship != null ? (int)Math.round(this.ship.getX()) : 0;
        int curY = this.ship != null ? (int)Math.round(this.ship.getY()) : 0;
        int curZ = this.ship != null ? (int)Math.round(this.ship.getZ()) : 0;
        String rawCurDim = this.ship != null && this.ship.level() != null ? this.ship.level().dimension().location().toString() : "minecraft:overworld";
        String formattedCurDim = PortalPlacementHelper.formatDimensionName((String)rawCurDim);
        guiGraphics.drawString(this.font, "POS: " + curX + ", " + curY + ", " + curZ, termX + 6, termY + 5, -12720539, false);
        int dimLabelW = this.font.width("DIM: ");
        int maxDimValW = 120;
        String displayCurDim = this.font.width(formattedCurDim) <= maxDimValW ? formattedCurDim : this.font.plainSubstrByWidth(formattedCurDim, Math.max(10, maxDimValW - this.font.width("..."))) + "...";
        guiGraphics.drawString(this.font, "DIM: ", termX + 150, termY + 5, -7696228, false);
        guiGraphics.drawString(this.font, displayCurDim, termX + 150 + dimLabelW, termY + 5, -19674, false);
        if (this.ship == null || !this.ship.isAlive()) {
            engineStatus = "NO LINK";
            engineColor = -48060;
        } else if (this.ship.isBroken()) {
            engineStatus = "BROKEN";
            engineColor = -48060;
        } else if (this.ship.isCrashing()) {
            engineStatus = "CRASHING";
            engineColor = -48060;
        } else if (this.ship.isWarpActive()) {
            engineStatus = "WARP CHARGING";
            engineColor = -32513;
        } else if (this.ship.isEngineOn()) {
            engineStatus = "ONLINE";
            engineColor = -12720539;
        } else {
            engineStatus = "STANDBY";
            engineColor = -7696228;
        }
        guiGraphics.drawString(this.font, "ENG: " + engineStatus, termX + 6, termY + 16, engineColor, false);
        float energy = this.ship != null ? this.ship.getEnergy() : 0.0f;
        int energyPct = Math.round(energy / 100.0f * 100.0f);
        guiGraphics.drawString(this.font, "PWR:", termX + 150, termY + 16, -13183000, false);
        int barX = termX + 178;
        int barY = termY + 16;
        int barW = 68;
        int barH = 7;
        guiGraphics.fill(barX, barY, barX + barW, barY + barH, -16247796);
        guiGraphics.renderOutline(barX, barY, barW, barH, -15454181);
        int fillW = (int)Math.round((double)(barW - 2) * ((double)Math.max(0, Math.min(100, energyPct)) / 100.0));
        if (fillW > 0) {
            int energyBarColor = energyPct > 25 ? -13638053 : (energyPct > 10 ? -19674 : -48060);
            guiGraphics.fill(barX + 1, barY + 1, barX + 1 + fillW, barY + barH - 1, energyBarColor);
        }
        guiGraphics.drawString(this.font, energyPct + "%", barX + barW + 4, barY - 1, energyPct > 25 ? -12720539 : -48060, false);
        guiGraphics.fill(termX + 4, termY + 27, termX + termW - 4, termY + 28, -15783148);
        if (this.currentView == ViewMode.NAVIGATION && !this.isNamingWaypoint) {
            this.renderNavigationView(guiGraphics, termX, termY, termW, top);
        } else if (this.currentView == ViewMode.WAYPOINTS && !this.isNamingWaypoint) {
            this.renderWaypointsView(guiGraphics, termX, termY, termW);
        } else if (this.isNamingWaypoint) {
            this.renderSaveDialogView(guiGraphics, termX, termY, termW);
        }
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void renderNavigationView(GuiGraphics guiGraphics, int termX, int termY, int termW, int top) {
        guiGraphics.drawString(this.font, "TARGET DIMENSION", termX + 6, termY + 31, -13183000, false);
        guiGraphics.drawCenteredString(this.font, "TARGET JUMP VECTORS", termX + termW / 2, termY + 63, -13183000);
        int statusY = termY + 124;
        if (!this.statusMessage.isEmpty()) {
            guiGraphics.drawCenteredString(this.font, this.statusMessage, termX + termW / 2, statusY, this.statusColor);
        } else if (this.ship != null && this.ship.getEnergy() < 25.0f) {
            guiGraphics.drawCenteredString(this.font, "\u00a7c[!] WARP CORE UNDERCHARGED (MIN 25% REQUIRED) [!]", termX + termW / 2, statusY, -43691);
        } else {
            if (this.ship == null) return;
            if (!this.ship.isWarpActive()) return;
            guiGraphics.drawCenteredString(this.font, "\u00a7d>>> WARP SEQUENCE ENGAGED <<<", termX + termW / 2, statusY, -32513);
        }
    }

    private void renderWaypointsView(GuiGraphics guiGraphics, int termX, int termY, int termW) {
        int totalWaypoints = this.ship != null ? this.ship.getSavedWaypoints().size() : 0;
        String header = "CRUISER WAYPOINTS (" + totalWaypoints + "/30)";
        guiGraphics.drawString(this.font, header, termX + 6, termY + 28, -12720539, false);
        if (this.filteredWaypoints.isEmpty()) {
            guiGraphics.drawCenteredString(this.font, "\u00a78NO WAYPOINTS SAVED IN SHIP COMPUTER", termX + termW / 2, termY + 84, -10063748);
            guiGraphics.drawCenteredString(this.font, "\u00a77Click 'SAVE SHIP POS' below to create a waypoint", termX + termW / 2, termY + 98, -11838896);
        } else {
            int rowStartY = termY + 58;
            int rowHeight = 23;
            int count = Math.min(3, this.filteredWaypoints.size() - this.scrollOffset);
            int i = 0;
            while (i < count) {
                int index = this.scrollOffset + i;
                if (index >= this.filteredWaypoints.size()) return;
                CruiserWaypoint wp = this.filteredWaypoints.get(index);
                int rowY = rowStartY + i * (rowHeight + 2);
                guiGraphics.fill(termX + 6, rowY, termX + termW - 6, rowY + rowHeight, -16246771);
                guiGraphics.renderOutline(termX + 6, rowY, termW - 12, rowHeight, -15454181);
                this.renderDimensionBadge(guiGraphics, termX + 8, rowY + 3, wp.dimension());
                int maxNameW = 145;
                String wpName = this.font.width(wp.name()) <= maxNameW ? wp.name() : this.font.plainSubstrByWidth(wp.name(), Math.max(10, maxNameW - this.font.width("..."))) + "...";
                guiGraphics.drawString(this.font, wpName, termX + 48, rowY + 3, -12720539, false);
                String coordsStr = (int)Math.round(wp.x()) + ", " + (int)Math.round(wp.y()) + ", " + (int)Math.round(wp.z());
                guiGraphics.drawString(this.font, coordsStr, termX + 48, rowY + 13, -7696228, false);
                ++i;
            }
            return;
        }
    }

    private void renderSaveDialogView(GuiGraphics guiGraphics, int termX, int termY, int termW) {
        guiGraphics.drawCenteredString(this.font, "\u00a7eSAVE WAYPOINT TO NAV COMPUTER", termX + termW / 2, termY + 40, -19674);
        String desc = this.savingCurrentPos ? "Target: Current Ship Coordinates & Dimension" : "Target: Entered Jump Vectors & Dimension";
        guiGraphics.drawCenteredString(this.font, "\u00a77" + desc, termX + termW / 2, termY + 56, -7696228);
        if (this.savingCurrentPos) {
            int curX = this.ship != null ? (int)Math.round(this.ship.getX()) : 0;
            int curY = this.ship != null ? (int)Math.round(this.ship.getY()) : 0;
            int curZ = this.ship != null ? (int)Math.round(this.ship.getZ()) : 0;
            String rawDim = this.ship != null && this.ship.level() != null ? this.ship.level().dimension().location().toString() : "minecraft:overworld";
            String curDim = PortalPlacementHelper.formatDimensionName((String)rawDim);
            int maxDimW = 100;
            String displayDim = this.font.width(curDim) <= maxDimW ? curDim : this.font.plainSubstrByWidth(curDim, maxDimW - this.font.width("...")) + "...";
            guiGraphics.drawCenteredString(this.font, "\u00a7a" + curX + ", " + curY + ", " + curZ + " [" + displayDim + "]", termX + termW / 2, termY + 70, -12720539);
        } else {
            String displayDim;
            ParsedCoords c = this.parseEnteredCoordinates();
            String dim = PortalPlacementHelper.formatDimensionName((String)this.selectedDimension);
            int maxDimW = 100;
            String string = displayDim = this.font.width(dim) <= maxDimW ? dim : this.font.plainSubstrByWidth(dim, maxDimW - this.font.width("...")) + "...";
            if (c.valid) {
                guiGraphics.drawCenteredString(this.font, "\u00a7a" + (int)c.x + ", " + (int)c.y + ", " + (int)c.z + " [" + displayDim + "]", termX + termW / 2, termY + 70, -12720539);
            }
        }
        guiGraphics.drawString(this.font, "ENTER WAYPOINT NAME:", termX + 6, termY + 118, -13183000, false);
    }

    private void renderDimensionBadge(GuiGraphics guiGraphics, int x, int y, String dimension) {
        int textColor;
        int borderColor;
        String badge;
        String lower = dimension.toLowerCase();
        if (lower.contains("overworld")) {
            badge = "[OVER]";
            borderColor = -13638053;
            textColor = -13638053;
        } else if (lower.contains("nether")) {
            badge = "[NETH]";
            borderColor = -48060;
            textColor = -48060;
        } else if (lower.contains("end")) {
            badge = "[ END]";
            borderColor = -5618433;
            textColor = -5618433;
        } else if (BlenderDimensionHelper.isBlenderDimension((String)dimension)) {
            badge = "[BLND]";
            borderColor = -19674;
            textColor = -19674;
        } else if (FishDimensionHelper.isFishDimension((String)dimension)) {
            badge = "[FISH]";
            borderColor = -16718337;
            textColor = -16718337;
        } else if (lower.contains("infinity")) {
            badge = "[ INF]";
            borderColor = -13183000;
            textColor = -13183000;
        } else {
            badge = "[ DIM]";
            borderColor = -13183000;
            textColor = -13183000;
        }
        guiGraphics.fill(x, y, x + 36, y + 17, -16445432);
        guiGraphics.renderOutline(x, y, 36, 17, borderColor);
        guiGraphics.drawString(this.font, badge, x + 2, y + 5, textColor, false);
    }

    public boolean isPauseScreen() {
        return false;
    }
}

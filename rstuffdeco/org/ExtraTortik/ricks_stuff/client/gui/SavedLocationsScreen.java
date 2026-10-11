/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.AbstractWidget
 *  net.minecraft.client.gui.components.EditBox
 *  net.minecraft.client.gui.components.Renderable
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.item.ItemStack
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton$Style
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetGuiUtils
 *  org.ExtraTortik.ricks_stuff.client.gui.SavedLocationsScreen$LocationEntry
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.UpgradeType
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SDeleteLocationPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SSaveLocationPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SSetCoordinatesPacket
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetButton;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetGuiUtils;
import org.ExtraTortik.ricks_stuff.client.gui.SavedLocationsScreen;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.ExtraTortik.ricks_stuff.item.UpgradeType;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.C2SDeleteLocationPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SSaveLocationPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SSetCoordinatesPacket;

public class SavedLocationsScreen
extends Screen {
    private final Screen parent;
    private final CompoundTag gunNbt;
    private final int slotIndex;
    private ItemStack gunStack;
    private EditBox searchBox;
    private EditBox nameInputBox;
    private boolean isAddingLocation = false;
    private int scrollOffset = 0;
    private static final int ITEMS_PER_PAGE = 4;
    private final List<AbstractWidget> dynamicWidgets = new ArrayList<AbstractWidget>();
    private final List<LocationEntry> allLocations = new ArrayList<LocationEntry>();
    private final List<LocationEntry> filteredLocations = new ArrayList<LocationEntry>();

    public SavedLocationsScreen(Screen parent, CompoundTag gunNbt, int slotIndex) {
        super((Component)Component.literal((String)"Saved Locations"));
        this.parent = parent;
        this.gunNbt = gunNbt != null ? gunNbt : new CompoundTag();
        this.slotIndex = slotIndex;
    }

    private boolean hasDimUpgrade() {
        return this.gunStack != null && PortalGunItem.hasUpgrade((ItemStack)this.gunStack, (UpgradeType)UpgradeType.DIMENSIONAL) || PortalGunItem.hasUpgrade((CompoundTag)this.gunNbt, (UpgradeType)UpgradeType.DIMENSIONAL);
    }

    private void loadLocationsFromNbt() {
        this.allLocations.clear();
        if (this.gunNbt.contains("SavedLocations", 9)) {
            ListTag list = this.gunNbt.getList("SavedLocations", 10);
            for (int i = 0; i < list.size(); ++i) {
                CompoundTag tag = list.getCompound(i);
                String name = tag.getString("Name");
                double x = tag.getDouble("X");
                double y = tag.getDouble("Y");
                double z = tag.getDouble("Z");
                String dim = tag.getString("Dimension");
                this.allLocations.add(new LocationEntry(i, name, x, y, z, dim));
            }
        }
        this.filterLocations();
    }

    private void filterLocations() {
        this.filteredLocations.clear();
        String query = this.searchBox != null ? this.searchBox.getValue().trim().toLowerCase() : "";
        Iterator<LocationEntry> iterator = this.allLocations.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                this.scrollOffset = 0;
                return;
            }
            LocationEntry loc = iterator.next();
            if (!query.isEmpty() && !loc.name.toLowerCase().contains(query) && !loc.dimension.toLowerCase().contains(query)) continue;
            this.filteredLocations.add(loc);
        }
    }

    protected void init() {
        boolean hasCard;
        super.init();
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (this.slotIndex >= 0 && this.slotIndex < player.getInventory().getContainerSize()) {
                this.gunStack = player.getInventory().getItem(this.slotIndex);
            }
            if (this.gunStack == null || !SavedLocationsScreen.isGun(this.gunStack)) {
                if (SavedLocationsScreen.isGun(player.getMainHandItem())) {
                    this.gunStack = player.getMainHandItem();
                } else if (SavedLocationsScreen.isGun(player.getOffhandItem())) {
                    this.gunStack = player.getOffhandItem();
                }
            }
        }
        this.loadLocationsFromNbt();
        int panelWidth = 300;
        int panelHeight = 220;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;
        boolean bl = hasCard = this.gunStack != null && PortalGunItem.hasMemoryCard((ItemStack)this.gunStack) || this.gunNbt.getBoolean("HasMemoryCard");
        if (hasCard) {
            this.searchBox = new EditBox(this.font, left + 12, top + 34, panelWidth - 24, 16, (Component)Component.literal((String)"Search"));
            this.searchBox.setHint((Component)Component.literal((String)"Search locations...").withStyle(ChatFormatting.DARK_GRAY));
            this.searchBox.setResponder(s -> {
                this.filterLocations();
                this.refreshRows();
            });
            this.addRenderableWidget((GuiEventListener)this.searchBox);
            this.nameInputBox = new EditBox(this.font, left + 12, top + 162, 150, 16, (Component)Component.literal((String)"Name"));
            this.nameInputBox.setHint((Component)Component.literal((String)"Location Name...").withStyle(ChatFormatting.DARK_GRAY));
            this.nameInputBox.setVisible(false);
            this.addRenderableWidget((GuiEventListener)this.nameInputBox);
        }
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"Back"), b -> {
            this.playClickSound();
            Minecraft.getInstance().setScreen(this.parent);
        }).bounds(left + panelWidth - 100 - 12, top + 185, 100, 20).build());
        if (!hasCard) return;
        this.refreshRows();
    }

    private <T extends AbstractWidget> T addDynamicWidget(T widget) {
        this.dynamicWidgets.add(widget);
        return (T)((AbstractWidget)this.addRenderableWidget((GuiEventListener)widget));
    }

    private void refreshRows() {
        Iterator<AbstractWidget> iterator = this.dynamicWidgets.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                this.dynamicWidgets.clear();
                int panelWidth = 300;
                int panelHeight = 220;
                int left = (this.width - panelWidth) / 2;
                int top = (this.height - panelHeight) / 2;
                this.rebuildRowWidgets(left, top, panelWidth);
                return;
            }
            AbstractWidget w = iterator.next();
            this.removeWidget((GuiEventListener)w);
        }
    }

    private void rebuildRowWidgets(int left, int top, int panelWidth) {
        int index;
        int listTop = top + 54;
        int rowHeight = 24;
        int count = Math.min(4, this.filteredLocations.size() - this.scrollOffset);
        for (int i = 0; i < count && (index = this.scrollOffset + i) < this.filteredLocations.size(); ++i) {
            LocationEntry entry = this.filteredLocations.get(index);
            int rowY = listTop + i * (rowHeight + 2);
            this.addDynamicWidget(GadgetButton.gadget((Component)Component.literal((String)"Select"), b -> {
                boolean isCrossDim;
                LocalPlayer player = Minecraft.getInstance().player;
                String currentDim = player != null ? player.level().dimension().location().toString() : "";
                boolean bl = isCrossDim = !entry.dimension.equalsIgnoreCase(currentDim);
                if (!this.hasDimUpgrade() && isCrossDim) {
                    if (player == null) return;
                    player.playSound((SoundEvent)ModSounds.GUN_MALFUNCTION.get(), 1.0f, 1.0f);
                    return;
                }
                this.playClickSound();
                ModMessages.sendToServer((CustomPacketPayload)new C2SSetCoordinatesPacket(entry.x, entry.y, entry.z, entry.dimension));
                if (player == null) return;
                player.playSound((SoundEvent)ModSounds.UI_DIAL.get(), 1.0f, 1.0f);
            }).bounds(left + panelWidth - 95, rowY + 2, 45, 18).style(GadgetButton.Style.PRIMARY).build());
            this.addDynamicWidget(GadgetButton.gadget((Component)Component.literal((String)"Del"), b -> {
                ListTag list;
                this.playClickSound();
                ModMessages.sendToServer((CustomPacketPayload)new C2SDeleteLocationPacket(entry.originalIndex));
                if (this.gunNbt.contains("SavedLocations", 9) && entry.originalIndex < (list = this.gunNbt.getList("SavedLocations", 10)).size()) {
                    list.remove(entry.originalIndex);
                }
                this.loadLocationsFromNbt();
                this.refreshRows();
            }).bounds(left + panelWidth - 45, rowY + 2, 32, 18).style(GadgetButton.Style.DANGER).build());
        }
        if (this.scrollOffset > 0) {
            this.addDynamicWidget(GadgetButton.gadget((Component)Component.literal((String)"\u25b2"), b -> {
                this.scrollOffset = Math.max(0, this.scrollOffset - 1);
                this.refreshRows();
            }).bounds(left + panelWidth - 16, top + 34, 14, 16).build());
        }
        if (this.scrollOffset + 4 < this.filteredLocations.size()) {
            this.addDynamicWidget(GadgetButton.gadget((Component)Component.literal((String)"\u25bc"), b -> {
                ++this.scrollOffset;
                this.refreshRows();
            }).bounds(left + panelWidth - 16, listTop + 104 - 10, 14, 16).build());
        }
        if (!this.isAddingLocation) {
            this.addDynamicWidget(GadgetButton.gadget((Component)Component.literal((String)"Save Current Pos"), b -> {
                this.playClickSound();
                this.isAddingLocation = true;
                this.nameInputBox.setValue("");
                this.nameInputBox.setVisible(true);
                this.refreshRows();
            }).bounds(left + 12, top + 185, 140, 20).build());
        } else {
            this.addDynamicWidget(GadgetButton.gadget((Component)Component.literal((String)"Save"), b -> {
                this.playClickSound();
                LocalPlayer player = Minecraft.getInstance().player;
                if (player == null) return;
                Object name = this.nameInputBox.getValue().trim();
                if (((String)name).isEmpty()) {
                    name = "Location #" + (this.allLocations.size() + 1);
                }
                double x = player.getX();
                double y = player.getY();
                double z = player.getZ();
                String dim = player.level().dimension().location().toString();
                ModMessages.sendToServer((CustomPacketPayload)new C2SSaveLocationPacket((String)name, x, y, z, dim));
                ListTag list = this.gunNbt.getList("SavedLocations", 10);
                CompoundTag newEntry = new CompoundTag();
                newEntry.putString("Name", (String)name);
                newEntry.putDouble("X", x);
                newEntry.putDouble("Y", y);
                newEntry.putDouble("Z", z);
                newEntry.putString("Dimension", dim);
                list.add((Object)newEntry);
                this.gunNbt.put("SavedLocations", (Tag)list);
                this.isAddingLocation = false;
                this.nameInputBox.setVisible(false);
                this.loadLocationsFromNbt();
                this.refreshRows();
            }).bounds(left + 168, top + 160, 54, 20).style(GadgetButton.Style.PRIMARY).build());
            this.addDynamicWidget(GadgetButton.gadget((Component)Component.literal((String)"Cancel"), b -> {
                this.playClickSound();
                this.isAddingLocation = false;
                this.nameInputBox.setVisible(false);
                this.refreshRows();
            }).bounds(left + 226, top + 160, 62, 20).build());
        }
    }

    private void playClickSound() {
        if (Minecraft.getInstance().player == null) return;
        Minecraft.getInstance().player.playSound((SoundEvent)ModSounds.UI_PRESS.get(), 1.0f, 1.0f);
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (scrollY > 0.0 && this.scrollOffset > 0) {
            --this.scrollOffset;
            this.refreshRows();
            return true;
        }
        if (!(scrollY < 0.0)) return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        if (this.scrollOffset + 4 >= this.filteredLocations.size()) return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        ++this.scrollOffset;
        this.refreshRows();
        return true;
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        boolean hasCard;
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        int panelWidth = 300;
        int panelHeight = 220;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;
        GadgetGuiUtils.renderChassis((GuiGraphics)guiGraphics, (int)left, (int)top, (int)panelWidth, (int)panelHeight);
        GadgetGuiUtils.renderHeader((GuiGraphics)guiGraphics, (int)(left + 4), (int)(top + 4), (int)(panelWidth - 8), (int)24);
        guiGraphics.drawString(this.font, "SAVED DESTINATIONS", left + 12, top + 8, -12720539, false);
        String totalText = "Total: " + this.allLocations.size();
        int totalW = this.font.width(totalText);
        guiGraphics.drawString(this.font, totalText, left + panelWidth - totalW - 12, top + 8, -13183000, false);
        int listTop = top + 54;
        GadgetGuiUtils.renderTerminalScreen((GuiGraphics)guiGraphics, (int)(left + 10), (int)listTop, (int)(panelWidth - 20), (int)104);
        boolean bl = hasCard = this.gunStack != null && PortalGunItem.hasMemoryCard((ItemStack)this.gunStack) || this.gunNbt.getBoolean("HasMemoryCard");
        if (!hasCard) {
            guiGraphics.drawCenteredString(this.font, "\u00a7c[!] NO MEMORY CARD DETECTED", left + panelWidth / 2, listTop + 25, -43691);
            guiGraphics.drawCenteredString(this.font, "Insert a Quantum Memory Card into the gun", left + panelWidth / 2, listTop + 45, -7696228);
            guiGraphics.drawCenteredString(this.font, "to store and manage destination coordinates.", left + panelWidth / 2, listTop + 58, -7696228);
        } else if (this.filteredLocations.isEmpty()) {
            guiGraphics.drawCenteredString(this.font, "No saved locations found.", left + panelWidth / 2, listTop + 35, -7696228);
        } else {
            LocalPlayer player = Minecraft.getInstance().player;
            String currentDim = player != null ? player.level().dimension().location().toString() : "";
            boolean hasDim = this.hasDimUpgrade();
            int count = Math.min(4, this.filteredLocations.size() - this.scrollOffset);
            for (int i = 0; i < count; ++i) {
                boolean isCrossDim;
                int index = this.scrollOffset + i;
                LocationEntry entry = this.filteredLocations.get(index);
                int rowY = listTop + i * 26;
                guiGraphics.fill(left + 12, rowY + 1, left + panelWidth - 12, rowY + 25, 403383314);
                guiGraphics.fill(left + 12, rowY + 1, left + 14, rowY + 25, this.getDimColor(entry.dimension));
                String dimBadge = this.formatDimensionBadge(entry.dimension);
                guiGraphics.drawString(this.font, dimBadge, left + 18, rowY + 4, this.getDimColor(entry.dimension), false);
                String trimmedName = entry.name.length() > 16 ? entry.name.substring(0, 15) + "\u2026" : entry.name;
                guiGraphics.drawString(this.font, trimmedName, left + 65, rowY + 4, -1, false);
                String coords = "X:" + (int)entry.x + " Y:" + (int)entry.y + " Z:" + (int)entry.z;
                guiGraphics.drawString(this.font, coords, left + 18, rowY + 14, -7696228, false);
                boolean bl2 = isCrossDim = !entry.dimension.equalsIgnoreCase(currentDim);
                if (hasDim || !isCrossDim) continue;
                guiGraphics.drawString(this.font, "\u00a7c[Dim Card Req]\u00a7r", left + panelWidth - 170, rowY + 14, -43691, false);
            }
        }
        Iterator iterator = this.renderables.iterator();
        while (iterator.hasNext()) {
            Renderable renderable = (Renderable)iterator.next();
            renderable.render(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    private int getDimColor(String dim) {
        if (dim == null) return -16711834;
        if (dim.isEmpty()) {
            return -16711834;
        }
        String lower = dim.toLowerCase();
        if (lower.contains("nether")) {
            return -48060;
        }
        if (lower.contains("end")) {
            return -43521;
        }
        if (lower.contains("overworld")) {
            return -16711834;
        }
        if (!lower.contains("blender")) return -16718337;
        return -19674;
    }

    private String formatDimensionBadge(String dim) {
        if (dim == null) return "[OVER]";
        if (dim.isEmpty()) {
            return "[OVER]";
        }
        String lower = dim.toLowerCase();
        if (lower.contains("nether")) {
            return "[NETH]";
        }
        if (lower.contains("end")) {
            return "[ END]";
        }
        if (lower.contains("overworld")) {
            return "[OVER]";
        }
        if (lower.contains("blender")) {
            return "[BLND]";
        }
        String path = lower.contains(":") ? lower.substring(lower.indexOf(":") + 1) : lower;
        String badge = path.replace("_", "").toUpperCase();
        if (badge.length() <= 4) return "[" + String.format("%-4s", badge) + "]";
        badge = badge.substring(0, 4);
        return "[" + String.format("%-4s", badge) + "]";
    }

    private static boolean isGun(ItemStack s) {
        return s != null && !s.isEmpty() && (s.getItem() instanceof PortalGunItem || s.getItem() instanceof DischargedPortalGunItem);
    }

    public boolean isPauseScreen() {
        return false;
    }
}

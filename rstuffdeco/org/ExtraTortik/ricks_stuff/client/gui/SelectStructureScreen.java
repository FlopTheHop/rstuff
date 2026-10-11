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
 *  net.minecraft.core.Registry
 *  net.minecraft.core.RegistryAccess
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.item.ItemStack
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton$Style
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetGuiUtils
 *  org.ExtraTortik.ricks_stuff.client.gui.SelectStructureScreen$StructureEntry
 *  org.ExtraTortik.ricks_stuff.client.gui.SetCoordinatesScreen
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.UpgradeType
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.C2STargetStructurePacket
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetButton;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetGuiUtils;
import org.ExtraTortik.ricks_stuff.client.gui.SelectStructureScreen;
import org.ExtraTortik.ricks_stuff.client.gui.SetCoordinatesScreen;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.ExtraTortik.ricks_stuff.item.UpgradeType;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.C2STargetStructurePacket;

public class SelectStructureScreen
extends Screen {
    private final Screen parent;
    private final CompoundTag gunNbt;
    private final int slotIndex;
    private ItemStack gunStack;
    private final List<String> availableDimensions = new ArrayList<String>();
    private int selectedDimIndex = 0;
    private String selectedDimension = "minecraft:overworld";
    private EditBox dimensionField;
    private EditBox searchBox;
    private final List<AbstractWidget> dynamicWidgets = new ArrayList<AbstractWidget>();
    private final List<StructureEntry> allDiscoveredStructures = new ArrayList<StructureEntry>();
    private final List<StructureEntry> filteredStructures = new ArrayList<StructureEntry>();
    private int scrollOffset = 0;
    private static final int ITEMS_PER_PAGE = 4;
    private String statusMessage = "";
    private int statusColor = 0xFF5555;

    public SelectStructureScreen(Screen parent, CompoundTag gunNbt, int slotIndex) {
        super((Component)Component.literal((String)"Target Structure Scanner"));
        this.parent = parent;
        this.gunNbt = gunNbt != null ? gunNbt : new CompoundTag();
        this.slotIndex = slotIndex;
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

    private void discoverAllStructures() {
        Optional opt;
        RegistryAccess registryAccess;
        this.allDiscoveredStructures.clear();
        HashSet<String> addedIds = new HashSet<String>();
        Minecraft mc = Minecraft.getInstance();
        Object object = mc.level != null ? mc.level.registryAccess() : (registryAccess = mc.getConnection() != null ? mc.getConnection().registryAccess() : null);
        if (registryAccess != null && (opt = registryAccess.registry(Registries.STRUCTURE)).isPresent()) {
            Registry reg = (Registry)opt.get();
            for (Map.Entry entry : reg.entrySet()) {
                ResourceLocation id = ((ResourceKey)entry.getKey()).location();
                String idStr = id.toString();
                if (addedIds.contains(idStr)) continue;
                String dim = this.inferStructureDimension(id);
                String displayName = SelectStructureScreen.formatStructureName(idStr);
                String badge = this.formatDimensionBadge(dim);
                int badgeColor = this.getDimColor(dim);
                this.allDiscoveredStructures.add(new StructureEntry(idStr, displayName, dim, badge, badgeColor));
                addedIds.add(idStr);
            }
        }
        this.addFallbackIfMissing(addedIds, "minecraft:village_plains", "Village (Plains)", "minecraft:overworld");
        this.addFallbackIfMissing(addedIds, "minecraft:village_desert", "Village (Desert)", "minecraft:overworld");
        this.addFallbackIfMissing(addedIds, "minecraft:village_savanna", "Village (Savanna)", "minecraft:overworld");
        this.addFallbackIfMissing(addedIds, "minecraft:village_snowy", "Village (Snowy)", "minecraft:overworld");
        this.addFallbackIfMissing(addedIds, "minecraft:village_taiga", "Village (Taiga)", "minecraft:overworld");
        this.addFallbackIfMissing(addedIds, "minecraft:pillager_outpost", "Pillager Outpost", "minecraft:overworld");
        this.addFallbackIfMissing(addedIds, "minecraft:ancient_city", "Ancient City", "minecraft:overworld");
        this.addFallbackIfMissing(addedIds, "minecraft:mansion", "Woodland Mansion", "minecraft:overworld");
        this.addFallbackIfMissing(addedIds, "minecraft:stronghold", "Stronghold", "minecraft:overworld");
        this.addFallbackIfMissing(addedIds, "minecraft:monument", "Ocean Monument", "minecraft:overworld");
        this.addFallbackIfMissing(addedIds, "minecraft:mineshaft", "Mineshaft", "minecraft:overworld");
        this.addFallbackIfMissing(addedIds, "minecraft:desert_pyramid", "Desert Pyramid", "minecraft:overworld");
        this.addFallbackIfMissing(addedIds, "minecraft:jungle_pyramid", "Jungle Temple", "minecraft:overworld");
        this.addFallbackIfMissing(addedIds, "minecraft:swamp_hut", "Witch Hut", "minecraft:overworld");
        this.addFallbackIfMissing(addedIds, "minecraft:trail_ruins", "Trail Ruins", "minecraft:overworld");
        this.addFallbackIfMissing(addedIds, "minecraft:fortress", "Nether Fortress", "minecraft:the_nether");
        this.addFallbackIfMissing(addedIds, "minecraft:bastion_remnant", "Bastion Remnant", "minecraft:the_nether");
        this.addFallbackIfMissing(addedIds, "minecraft:nether_fossil", "Nether Fossil", "minecraft:the_nether");
        this.addFallbackIfMissing(addedIds, "minecraft:end_city", "End City", "minecraft:the_end");
        this.allDiscoveredStructures.sort(Comparator.comparing(StructureEntry::displayName));
    }

    private void addFallbackIfMissing(Set<String> added, String id, String name, String dim) {
        if (added.contains(id)) return;
        String badge = this.formatDimensionBadge(dim);
        int badgeColor = this.getDimColor(dim);
        this.allDiscoveredStructures.add(new StructureEntry(id, name, dim, badge, badgeColor));
        added.add(id);
    }

    private String inferStructureDimension(ResourceLocation id) {
        String availDim;
        String path = id.getPath().toLowerCase();
        String namespace = id.getNamespace().toLowerCase();
        if (namespace.equals("minecraft")) {
            if (path.contains("fortress")) return "minecraft:the_nether";
            if (path.contains("bastion")) return "minecraft:the_nether";
            if (path.contains("nether")) {
                return "minecraft:the_nether";
            }
            if (path.contains("end_city")) return "minecraft:the_end";
            if (!path.contains("endcity")) return "minecraft:overworld";
            return "minecraft:the_end";
        }
        if (namespace.equals("the_nether")) return "minecraft:the_nether";
        if (namespace.contains("nether")) {
            return "minecraft:the_nether";
        }
        if (namespace.equals("the_end")) return "minecraft:the_end";
        if (namespace.contains("end")) {
            return "minecraft:the_end";
        }
        Iterator<String> iterator = this.availableDimensions.iterator();
        do {
            if (!iterator.hasNext()) return namespace + ":" + namespace;
            availDim = iterator.next();
            if (availDim.toLowerCase().startsWith(namespace + ":")) return availDim;
        } while (!availDim.equalsIgnoreCase(namespace));
        return availDim;
    }

    private static String formatStructureName(String structureId) {
        String path = structureId.contains(":") ? structureId.substring(structureId.indexOf(":") + 1) : structureId;
        String[] parts = path.replace('_', ' ').replace('/', ' ').split("\\s+");
        StringBuilder sb = new StringBuilder();
        String[] stringArray = parts;
        int n = stringArray.length;
        int n2 = 0;
        while (n2 < n) {
            String part = stringArray[n2];
            if (!part.isEmpty()) {
                if (sb.length() > 0) {
                    sb.append(" ");
                }
                sb.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1).toLowerCase());
            }
            ++n2;
        }
        return sb.toString();
    }

    private boolean hasDimUpgrade() {
        return this.gunStack != null && PortalGunItem.hasUpgrade((ItemStack)this.gunStack, (UpgradeType)UpgradeType.DIMENSIONAL) || PortalGunItem.hasUpgrade((CompoundTag)this.gunNbt, (UpgradeType)UpgradeType.DIMENSIONAL);
    }

    private void filterStructures() {
        this.filteredStructures.clear();
        String query = this.searchBox != null ? this.searchBox.getValue().trim().toLowerCase() : "";
        String curDim = this.selectedDimension.trim().toLowerCase();
        for (StructureEntry entry : this.allDiscoveredStructures) {
            String dimNs;
            String structNs;
            boolean matchesDim = false;
            String entryDim = entry.primaryDimension().toLowerCase();
            if (curDim.equals("minecraft:overworld")) {
                matchesDim = entryDim.equals("minecraft:overworld") || entryDim.equals("overworld");
            } else if (curDim.equals("minecraft:the_nether") || curDim.equals("the_nether") || curDim.equals("nether")) {
                matchesDim = entryDim.equals("minecraft:the_nether") || entryDim.equals("the_nether") || entryDim.equals("nether");
            } else if (curDim.equals("minecraft:the_end") || curDim.equals("the_end") || curDim.equals("end")) {
                matchesDim = entryDim.equals("minecraft:the_end") || entryDim.equals("the_end") || entryDim.equals("end");
            } else if (entryDim.equals(curDim) || entryDim.endsWith(":" + curDim) || curDim.endsWith(":" + entryDim)) {
                matchesDim = true;
            } else if (entry.id().contains(":") && curDim.contains(":") && (structNs = entry.id().substring(0, entry.id().indexOf(58))).equalsIgnoreCase(dimNs = curDim.substring(0, curDim.indexOf(58))) && !entryDim.equals("minecraft:overworld") && !entryDim.equals("minecraft:the_nether") && !entryDim.equals("minecraft:the_end")) {
                matchesDim = true;
            }
            if (!matchesDim || !query.isEmpty() && !entry.displayName().toLowerCase().contains(query) && !entry.id().toLowerCase().contains(query)) continue;
            this.filteredStructures.add(entry);
        }
        if (this.filteredStructures.isEmpty() && !query.isEmpty()) {
            for (StructureEntry entry : this.allDiscoveredStructures) {
                if (!entry.displayName().toLowerCase().contains(query) && !entry.id().toLowerCase().contains(query)) continue;
                this.filteredStructures.add(entry);
            }
        }
        this.scrollOffset = 0;
    }

    protected void init() {
        super.init();
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (this.slotIndex >= 0 && this.slotIndex < player.getInventory().getContainerSize()) {
                this.gunStack = player.getInventory().getItem(this.slotIndex);
            }
            if (this.gunStack == null || !SelectStructureScreen.isGun(this.gunStack)) {
                if (SelectStructureScreen.isGun(player.getMainHandItem())) {
                    this.gunStack = player.getMainHandItem();
                } else if (SelectStructureScreen.isGun(player.getOffhandItem())) {
                    this.gunStack = player.getOffhandItem();
                }
            }
            if (this.selectedDimension.equals("minecraft:overworld")) {
                this.selectedDimension = player.level().dimension().location().toString();
            }
        }
        this.populateDimensions();
        this.discoverAllStructures();
        int panelWidth = 300;
        int panelHeight = 230;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;
        int dimNavY = top + 30;
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"\u25c0"), b -> {
            if (this.hasDimUpgrade()) {
                this.playClickSound();
                this.cycleDimension(-1);
                return;
            }
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.playSound((SoundEvent)ModSounds.GUN_MALFUNCTION.get(), 1.0f, 1.0f);
            }
            this.statusMessage = "Dimensional Card required!";
            this.statusColor = -43691;
        }).bounds(left + 12, dimNavY, 18, 16).build());
        this.dimensionField = new EditBox(this.font, left + 33, dimNavY, 234, 16, (Component)Component.literal((String)"Dimension ID"));
        this.dimensionField.setMaxLength(64);
        this.dimensionField.setValue(this.selectedDimension);
        this.dimensionField.setResponder(val -> {
            String trimmed = val.trim();
            if (!this.hasDimUpgrade() && Minecraft.getInstance().player != null) {
                String currentDim = Minecraft.getInstance().player.level().dimension().location().toString();
                if (!trimmed.isEmpty() && !trimmed.equalsIgnoreCase(currentDim)) {
                    this.statusMessage = "Dimensional Card required!";
                    this.statusColor = -43691;
                }
            }
            this.selectedDimension = trimmed.isEmpty() ? "minecraft:overworld" : trimmed;
            int idx = this.availableDimensions.indexOf(this.selectedDimension);
            if (idx >= 0) {
                this.selectedDimIndex = idx;
            }
            this.filterStructures();
            this.refreshRows();
        });
        this.addRenderableWidget((GuiEventListener)this.dimensionField);
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"\u25b6"), b -> {
            if (this.hasDimUpgrade()) {
                this.playClickSound();
                this.cycleDimension(1);
                return;
            }
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.playSound((SoundEvent)ModSounds.GUN_MALFUNCTION.get(), 1.0f, 1.0f);
            }
            this.statusMessage = "Dimensional Card required!";
            this.statusColor = -43691;
        }).bounds(left + 270, dimNavY, 18, 16).build());
        String prevSearch = this.searchBox != null ? this.searchBox.getValue() : "";
        this.searchBox = new EditBox(this.font, left + 12, top + 49, panelWidth - 24, 16, (Component)Component.literal((String)"Search"));
        this.searchBox.setHint((Component)Component.literal((String)"Search structure by name / id...").withStyle(ChatFormatting.DARK_GRAY));
        this.searchBox.setValue(prevSearch);
        this.searchBox.setResponder(s -> {
            this.filterStructures();
            this.refreshRows();
        });
        this.addRenderableWidget((GuiEventListener)this.searchBox);
        this.filterStructures();
        this.refreshRows();
    }

    private void cycleDimension(int direction) {
        if (this.availableDimensions.isEmpty()) {
            return;
        }
        this.selectedDimIndex = (this.selectedDimIndex + direction + this.availableDimensions.size()) % this.availableDimensions.size();
        this.selectedDimension = this.availableDimensions.get(this.selectedDimIndex);
        if (this.dimensionField != null) {
            this.dimensionField.setValue(this.selectedDimension);
        }
        this.filterStructures();
        this.refreshRows();
    }

    private <T extends AbstractWidget> T addDynamicWidget(T widget) {
        this.dynamicWidgets.add(widget);
        return (T)((AbstractWidget)this.addRenderableWidget((GuiEventListener)widget));
    }

    private void refreshRows() {
        int index;
        this.setFocused(null);
        for (AbstractWidget w : this.dynamicWidgets) {
            this.removeWidget((GuiEventListener)w);
        }
        this.dynamicWidgets.clear();
        int panelWidth = 300;
        int panelHeight = 230;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;
        int listTop = top + 68;
        int rowHeight = 26;
        LocalPlayer player = Minecraft.getInstance().player;
        String currentDim = player != null ? player.level().dimension().location().toString() : "";
        boolean hasDim = this.hasDimUpgrade();
        int count = Math.min(4, this.filteredStructures.size() - this.scrollOffset);
        for (int i = 0; i < count && (index = this.scrollOffset + i) < this.filteredStructures.size(); ++i) {
            StructureEntry entry = this.filteredStructures.get(index);
            int rowY = listTop + i * rowHeight;
            boolean crossDim = !this.selectedDimension.equalsIgnoreCase(currentDim);
            boolean dimLocked = crossDim && !hasDim;
            MutableComponent btnLabel = dimLocked ? Component.literal((String)"\u00a78Scan\u00a7r") : Component.literal((String)"Scan");
            this.addDynamicWidget(GadgetButton.gadget((Component)btnLabel, b -> {
                if (dimLocked) {
                    this.statusMessage = "Dimensional Matrix Card required!";
                    this.statusColor = -43691;
                    if (Minecraft.getInstance().player == null) return;
                    Minecraft.getInstance().player.playSound((SoundEvent)ModSounds.GUN_MALFUNCTION.get(), 1.0f, 1.0f);
                    return;
                }
                this.playClickSound();
                Screen patt0$temp = this.parent;
                if (patt0$temp instanceof SetCoordinatesScreen) {
                    SetCoordinatesScreen setCoords = (SetCoordinatesScreen)patt0$temp;
                    setCoords.startScanning();
                }
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().player.playSound((SoundEvent)ModSounds.UI_DIAL.get(), 1.0f, 1.0f);
                }
                ModMessages.sendToServer((CustomPacketPayload)new C2STargetStructurePacket(entry.id(), this.selectedDimension));
                Minecraft.getInstance().setScreen(this.parent);
            }).bounds(left + panelWidth - 76, rowY + 3, 62, 20).style(dimLocked ? GadgetButton.Style.NORMAL : GadgetButton.Style.PRIMARY).build());
        }
        if (this.scrollOffset > 0) {
            this.addDynamicWidget(GadgetButton.gadget((Component)Component.literal((String)"\u25b2"), b -> {
                this.scrollOffset = Math.max(0, this.scrollOffset - 1);
                this.refreshRows();
            }).bounds(left + panelWidth - 14, listTop + 2, 10, 14).build());
        }
        if (this.scrollOffset + 4 < this.filteredStructures.size()) {
            this.addDynamicWidget(GadgetButton.gadget((Component)Component.literal((String)"\u25bc"), b -> {
                ++this.scrollOffset;
                this.refreshRows();
            }).bounds(left + panelWidth - 14, listTop + 4 * rowHeight - 16, 10, 14).build());
        }
        this.addDynamicWidget(GadgetButton.gadget((Component)Component.literal((String)"Back"), b -> {
            this.playClickSound();
            Minecraft.getInstance().setScreen(this.parent);
        }).bounds(left + (panelWidth - 100) / 2, top + panelHeight - 24, 100, 18).build());
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
        if (this.scrollOffset + 4 >= this.filteredStructures.size()) return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        ++this.scrollOffset;
        this.refreshRows();
        return true;
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        int panelWidth = 300;
        int panelHeight = 230;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;
        GadgetGuiUtils.renderChassis((GuiGraphics)guiGraphics, (int)left, (int)top, (int)panelWidth, (int)panelHeight);
        GadgetGuiUtils.renderHeader((GuiGraphics)guiGraphics, (int)(left + 4), (int)(top + 4), (int)(panelWidth - 8), (int)22);
        guiGraphics.drawString(this.font, "STRUCTURE RADAR SCANNER", left + 12, top + 7, -12720539, false);
        String totalText = "Found: " + this.filteredStructures.size();
        int totalW = this.font.width(totalText);
        guiGraphics.drawString(this.font, totalText, left + panelWidth - totalW - 12, top + 7, -13183000, false);
        int listTop = top + 68;
        int rowHeight = 26;
        int listHeight = 4 * rowHeight;
        GadgetGuiUtils.renderTerminalScreen((GuiGraphics)guiGraphics, (int)(left + 10), (int)listTop, (int)(panelWidth - 20), (int)listHeight);
        if (this.filteredStructures.isEmpty()) {
            guiGraphics.drawCenteredString(this.font, "No structures matched query.", left + panelWidth / 2, listTop + 40, -7696228);
        } else {
            int index;
            int count = Math.min(4, this.filteredStructures.size() - this.scrollOffset);
            for (int i = 0; i < count && (index = this.scrollOffset + i) < this.filteredStructures.size(); ++i) {
                StructureEntry entry = this.filteredStructures.get(index);
                int rowY = listTop + i * rowHeight;
                int bg = i % 2 == 0 ? 537601042 : 269165586;
                guiGraphics.fill(left + 12, rowY + 1, left + panelWidth - 12, rowY + rowHeight - 1, bg);
                guiGraphics.fill(left + 12, rowY + 1, left + 14, rowY + rowHeight - 1, entry.badgeColor());
                String dimBadge = entry.dimBadge();
                guiGraphics.drawString(this.font, dimBadge, left + 18, rowY + 4, entry.badgeColor(), false);
                Object name = entry.displayName();
                int maxNameW = panelWidth - 170;
                if (this.font.width((String)name) > maxNameW) {
                    name = this.font.plainSubstrByWidth((String)name, maxNameW - 6) + "\u2026";
                }
                guiGraphics.drawString(this.font, (String)name, left + 62, rowY + 4, -1, false);
                Object idStr = entry.id();
                int maxIdW = panelWidth - 170;
                if (this.font.width((String)idStr) > maxIdW) {
                    idStr = this.font.plainSubstrByWidth((String)idStr, maxIdW - 6) + "\u2026";
                }
                guiGraphics.drawString(this.font, (String)idStr, left + 18, rowY + 14, -7696228, false);
            }
        }
        if (!this.statusMessage.isEmpty()) {
            Object displayMsg = this.statusMessage;
            int maxMsgW = panelWidth - 24;
            if (this.font.width((String)displayMsg) > maxMsgW) {
                displayMsg = this.font.plainSubstrByWidth((String)displayMsg, maxMsgW - 6) + "\u2026";
            }
            guiGraphics.drawCenteredString(this.font, (String)displayMsg, left + panelWidth / 2, top + panelHeight - 38, this.statusColor);
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
        if (lower.contains("twilight")) {
            return -7842305;
        }
        if (!lower.contains("aether")) return -19674;
        return -16718337;
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

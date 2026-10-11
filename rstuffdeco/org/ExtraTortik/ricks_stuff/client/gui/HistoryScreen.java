/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Renderable
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.component.CustomData
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton$Style
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetGuiUtils
 *  org.ExtraTortik.ricks_stuff.client.gui.HistoryScreen$HistoryEntry
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SSaveLocationPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SSetCoordinatesPacket
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetButton;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetGuiUtils;
import org.ExtraTortik.ricks_stuff.client.gui.HistoryScreen;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.C2SSaveLocationPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SSetCoordinatesPacket;

public class HistoryScreen
extends Screen {
    private final Screen parent;
    private final CompoundTag gunNbt;
    private final int slotIndex;
    private int scrollOffset = 0;
    private static final int ITEMS_PER_PAGE = 4;
    private final List<HistoryEntry> historyList = new ArrayList<HistoryEntry>();

    public HistoryScreen(Screen parent, CompoundTag gunNbt, int slotIndex) {
        super((Component)Component.literal((String)"Teleportation History"));
        this.parent = parent;
        this.gunNbt = gunNbt != null ? gunNbt : new CompoundTag();
        this.slotIndex = slotIndex;
        this.loadHistoryFromNbt();
    }

    private void loadHistoryFromNbt() {
        this.historyList.clear();
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            ItemStack stack = ItemStack.EMPTY;
            if (this.slotIndex >= 0 && this.slotIndex < player.getInventory().getContainerSize()) {
                stack = player.getInventory().getItem(this.slotIndex);
            }
            if (stack.isEmpty() || !(stack.getItem() instanceof PortalGunItem) && !(stack.getItem() instanceof DischargedPortalGunItem)) {
                if (player.getMainHandItem().getItem() instanceof PortalGunItem || player.getMainHandItem().getItem() instanceof DischargedPortalGunItem) {
                    stack = player.getMainHandItem();
                } else if (player.getOffhandItem().getItem() instanceof PortalGunItem || player.getOffhandItem().getItem() instanceof DischargedPortalGunItem) {
                    stack = player.getOffhandItem();
                }
            }
            if (!stack.isEmpty()) {
                CompoundTag tag = ((CustomData)stack.getOrDefault(DataComponents.CUSTOM_DATA, (Object)CustomData.EMPTY)).copyTag();
                this.gunNbt.merge(tag);
            }
        }
        if (!this.gunNbt.contains("HistoryLocations", 9)) return;
        ListTag list = this.gunNbt.getList("HistoryLocations", 10);
        int i = list.size() - 1;
        while (i >= 0) {
            CompoundTag tag = list.getCompound(i);
            Object name = tag.getString("Name");
            if (((String)name).isEmpty()) {
                name = "Log #" + (i + 1);
            }
            double x = tag.getDouble("X");
            double y = tag.getDouble("Y");
            double z = tag.getDouble("Z");
            String dim = tag.getString("Dimension");
            if (dim.isEmpty()) {
                dim = "minecraft:overworld";
            }
            this.historyList.add(new HistoryEntry(i, (String)name, x, y, z, dim));
            --i;
        }
    }

    protected void rebuildWidgets() {
        this.clearWidgets();
        this.init();
    }

    protected void init() {
        super.init();
        int panelWidth = 300;
        int panelHeight = 220;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;
        int listTop = top + 42;
        int rowHeight = 26;
        boolean hasCard = this.gunNbt.getBoolean("HasMemoryCard");
        if (hasCard) {
            int index;
            int count = Math.min(4, this.historyList.size() - this.scrollOffset);
            for (int i = 0; i < count && (index = this.scrollOffset + i) < this.historyList.size(); ++i) {
                HistoryEntry entry = this.historyList.get(index);
                int rowY = listTop + i * rowHeight;
                this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"Select"), b -> {
                    this.playClickSound();
                    ModMessages.sendToServer((CustomPacketPayload)new C2SSetCoordinatesPacket(entry.x, entry.y, entry.z, entry.dimension));
                    if (Minecraft.getInstance().player == null) return;
                    Minecraft.getInstance().player.playSound((SoundEvent)ModSounds.UI_DIAL.get(), 1.0f, 1.0f);
                }).bounds(left + panelWidth - 95, rowY + 2, 45, 18).style(GadgetButton.Style.PRIMARY).build());
                this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"Save"), b -> {
                    this.playClickSound();
                    ModMessages.sendToServer((CustomPacketPayload)new C2SSaveLocationPacket(entry.name, entry.x, entry.y, entry.z, entry.dimension));
                    b.setMessage((Component)Component.literal((String)"Saved"));
                }).bounds(left + panelWidth - 48, rowY + 2, 38, 18).build());
            }
            if (this.scrollOffset > 0) {
                this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"\u25b2"), b -> {
                    this.scrollOffset = Math.max(0, this.scrollOffset - 1);
                    this.rebuildWidgets();
                }).bounds(left + panelWidth - 16, top + 42, 14, 16).build());
            }
            if (this.scrollOffset + 4 < this.historyList.size()) {
                this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"\u25bc"), b -> {
                    ++this.scrollOffset;
                    this.rebuildWidgets();
                }).bounds(left + panelWidth - 16, listTop + 104 - 16, 14, 16).build());
            }
        }
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"Back"), b -> {
            this.playClickSound();
            Minecraft.getInstance().setScreen(this.parent);
        }).bounds(left + (panelWidth - 100) / 2, top + 185, 100, 20).build());
    }

    private void playClickSound() {
        if (Minecraft.getInstance().player == null) return;
        Minecraft.getInstance().player.playSound((SoundEvent)ModSounds.UI_PRESS.get(), 1.0f, 1.0f);
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (scrollY > 0.0 && this.scrollOffset > 0) {
            --this.scrollOffset;
            this.rebuildWidgets();
            return true;
        }
        if (!(scrollY < 0.0)) return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        if (this.scrollOffset + 4 >= this.historyList.size()) return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        ++this.scrollOffset;
        this.rebuildWidgets();
        return true;
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        int panelWidth = 300;
        int panelHeight = 220;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;
        GadgetGuiUtils.renderChassis((GuiGraphics)guiGraphics, (int)left, (int)top, (int)panelWidth, (int)panelHeight);
        GadgetGuiUtils.renderHeader((GuiGraphics)guiGraphics, (int)(left + 4), (int)(top + 4), (int)(panelWidth - 8), (int)24);
        guiGraphics.drawString(this.font, "TELEPORTATION HISTORY", left + 12, top + 8, -12720539, false);
        String logsText = "Logs: " + this.historyList.size();
        int logsW = this.font.width(logsText);
        guiGraphics.drawString(this.font, logsText, left + panelWidth - logsW - 12, top + 8, -13183000, false);
        int listTop = top + 42;
        GadgetGuiUtils.renderTerminalScreen((GuiGraphics)guiGraphics, (int)(left + 10), (int)listTop, (int)(panelWidth - 20), (int)106);
        boolean hasCard = this.gunNbt.getBoolean("HasMemoryCard");
        if (!hasCard) {
            guiGraphics.drawCenteredString(this.font, "\u00a7c[!] NO MEMORY CARD DETECTED", left + panelWidth / 2, listTop + 25, -43691);
            guiGraphics.drawCenteredString(this.font, "Insert a Quantum Memory Card into the gun to track trips.", left + panelWidth / 2, listTop + 48, -7696228);
        } else if (this.historyList.isEmpty()) {
            guiGraphics.drawCenteredString(this.font, "No teleportation history recorded.", left + panelWidth / 2, listTop + 40, -7696228);
        } else {
            int count = Math.min(4, this.historyList.size() - this.scrollOffset);
            for (int i = 0; i < count; ++i) {
                int index = this.scrollOffset + i;
                HistoryEntry entry = this.historyList.get(index);
                int rowY = listTop + i * 26 + 1;
                guiGraphics.fill(left + 12, rowY + 1, left + panelWidth - 12, rowY + 25, 403383314);
                guiGraphics.fill(left + 12, rowY + 1, left + 14, rowY + 25, this.getDimColor(entry.dimension));
                String dimBadge = this.formatDimensionBadge(entry.dimension);
                guiGraphics.drawString(this.font, dimBadge, left + 18, rowY + 4, this.getDimColor(entry.dimension), false);
                String name = entry.name.length() > 16 ? entry.name.substring(0, 15) + "\u2026" : entry.name;
                guiGraphics.drawString(this.font, name, left + 65, rowY + 4, -1, false);
                String coords = "X:" + (int)entry.x + " Y:" + (int)entry.y + " Z:" + (int)entry.z;
                guiGraphics.drawString(this.font, coords, left + 18, rowY + 14, -7696228, false);
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

    public boolean isPauseScreen() {
        return false;
    }
}

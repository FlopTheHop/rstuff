/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Renderable
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.sounds.SoundEvent
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetGuiUtils
 *  org.ExtraTortik.ricks_stuff.client.gui.SelectModeScreen$1
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SSelectModePacket
 *  org.ExtraTortik.ricks_stuff.portal.PortalMode
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetButton;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetGuiUtils;
import org.ExtraTortik.ricks_stuff.client.gui.SelectModeScreen;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.C2SSelectModePacket;
import org.ExtraTortik.ricks_stuff.portal.PortalMode;

public class SelectModeScreen
extends Screen {
    private final Screen parent;
    private final CompoundTag gunNbt;
    private final int slotIndex;
    private PortalMode currentMode = PortalMode.FIFO;

    public SelectModeScreen(Screen parent, CompoundTag gunNbt, int slotIndex) {
        super((Component)Component.literal((String)"Select Mode"));
        this.parent = parent;
        this.gunNbt = gunNbt != null ? gunNbt : new CompoundTag();
        this.slotIndex = slotIndex;
        if (!this.gunNbt.contains("Mode")) return;
        this.currentMode = PortalMode.fromString((String)this.gunNbt.getString("Mode"));
    }

    protected void init() {
        super.init();
        int panelWidth = 300;
        int panelHeight = 230;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;
        int btnWidth = 135;
        int btnHeight = 20;
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"Cycle (FIFO)"), b -> this.selectMode(PortalMode.FIFO)).bounds(left + 12, top + 36, btnWidth, btnHeight).build());
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"Override (LIFO)"), b -> this.selectMode(PortalMode.LIFO)).bounds(left + 153, top + 36, btnWidth, btnHeight).build());
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"Multi-Pairs"), b -> this.selectMode(PortalMode.MULTI_PAIR)).bounds(left + 12, top + 62, btnWidth, btnHeight).build());
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"Base Hub"), b -> this.selectMode(PortalMode.ROOT)).bounds(left + 153, top + 62, btnWidth, btnHeight).build());
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"Back"), b -> {
            this.playClickSound();
            Minecraft.getInstance().setScreen(this.parent);
        }).bounds(left + (panelWidth - 100) / 2, top + 195, 100, 20).build());
    }

    private void selectMode(PortalMode mode) {
        this.currentMode = mode;
        this.gunNbt.putString("Mode", mode.getName());
        ModMessages.sendToServer((CustomPacketPayload)new C2SSelectModePacket(mode.getName()));
        this.playClickSound();
    }

    private void playClickSound() {
        if (Minecraft.getInstance().player == null) return;
        Minecraft.getInstance().player.playSound((SoundEvent)ModSounds.UI_PRESS.get(), 1.0f, 1.0f);
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        int panelWidth = 300;
        int panelHeight = 230;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;
        GadgetGuiUtils.renderChassis((GuiGraphics)guiGraphics, (int)left, (int)top, (int)panelWidth, (int)panelHeight);
        GadgetGuiUtils.renderHeader((GuiGraphics)guiGraphics, (int)(left + 4), (int)(top + 4), (int)(panelWidth - 8), (int)24);
        guiGraphics.drawString(this.font, "PROTOCOL CONFIGURATION", left + 12, top + 8, -12720539, false);
        String modeBadge = "[" + this.currentMode.getDisplayName().toUpperCase() + "]";
        int badgeW = this.font.width(modeBadge);
        guiGraphics.drawString(this.font, modeBadge, left + panelWidth - badgeW - 12, top + 8, -13183000, false);
        int activeX = -1;
        int activeY = -1;
        int activeW = 135;
        int activeH = 20;
        switch (1.$SwitchMap$org$ExtraTortik$ricks_stuff$portal$PortalMode[this.currentMode.ordinal()]) {
            case 1: {
                activeX = left + 12;
                activeY = top + 36;
                break;
            }
            case 2: {
                activeX = left + 153;
                activeY = top + 36;
                break;
            }
            case 3: {
                activeX = left + 12;
                activeY = top + 62;
                break;
            }
            case 4: {
                activeX = left + 153;
                activeY = top + 62;
                break;
            }
        }
        if (activeX > 0) {
            guiGraphics.renderOutline(activeX - 1, activeY - 1, activeW + 2, activeH + 2, -12720539);
        }
        int descTop = top + 90;
        int descH = 98;
        GadgetGuiUtils.renderTerminalScreen((GuiGraphics)guiGraphics, (int)(left + 12), (int)descTop, (int)(panelWidth - 24), (int)descH);
        this.renderModeDescription(guiGraphics, left + 18, descTop + 8);
        Iterator iterator = this.renderables.iterator();
        while (iterator.hasNext()) {
            Renderable renderable = (Renderable)iterator.next();
            renderable.render(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    private void renderModeDescription(GuiGraphics guiGraphics, int x, int y) {
        switch (1.$SwitchMap$org$ExtraTortik$ricks_stuff$portal$PortalMode[this.currentMode.ordinal()]) {
            case 1: {
                guiGraphics.drawString(this.font, "\u00a7eCycle Mode:", x, y, 0xFFFFFF);
                guiGraphics.drawString(this.font, "\u00a77Maintains a 2-portal link.", x, y + 12, 0xAAAAAA);
                guiGraphics.drawString(this.font, "\u00a77Opening a 3rd portal closes the \u00a7coldest\u00a77 portal.", x, y + 24, 0xAAAAAA);
                guiGraphics.drawString(this.font, "\u00a77Best for continuous exploration and travel.", x, y + 36, 0xAAAAAA);
                break;
            }
            case 2: {
                guiGraphics.drawString(this.font, "\u00a7eOverride Mode:", x, y, 0xFFFFFF);
                guiGraphics.drawString(this.font, "\u00a77Maintains a 2-portal link.", x, y + 12, 0xAAAAAA);
                guiGraphics.drawString(this.font, "\u00a77Opening a 3rd portal replaces the \u00a7cnewest\u00a77 portal,", x, y + 24, 0xAAAAAA);
                guiGraphics.drawString(this.font, "\u00a77keeping the 1st portal as a fixed entrance.", x, y + 36, 0xAAAAAA);
                break;
            }
            case 3: {
                guiGraphics.drawString(this.font, "\u00a7bMulti-Pairs Mode:", x, y, 0xFFFFFF);
                guiGraphics.drawString(this.font, "\u00a77Allows up to 10 active portals (5 separate pairs).", x, y + 12, 0xAAAAAA);
                guiGraphics.drawString(this.font, "\u00a77Each pair links together: (1 \u2194 2), (3 \u2194 4), etc.", x, y + 24, 0xAAAAAA);
                guiGraphics.drawString(this.font, "\u00a77Great for connecting multiple rooms and bases.", x, y + 36, 0xAAAAAA);
                break;
            }
            case 4: {
                guiGraphics.drawString(this.font, "\u00a7dBase Hub Mode:", x, y, 0xFFFFFF);
                guiGraphics.drawString(this.font, "\u00a77The 1st portal becomes the central \u00a7bBASE HUB\u00a77.", x, y + 12, 0xAAAAAA);
                guiGraphics.drawString(this.font, "\u00a77All other portals take you back to the Hub.", x, y + 24, 0xAAAAAA);
                guiGraphics.drawString(this.font, "\u00a77Entering Hub returns you to your latest portal.", x, y + 36, 0xAAAAAA);
                break;
            }
            default: {
                guiGraphics.drawString(this.font, "\u00a77Select a portal protocol above.", x, y, 0xAAAAAA);
            }
        }
    }

    public boolean isPauseScreen() {
        return false;
    }
}

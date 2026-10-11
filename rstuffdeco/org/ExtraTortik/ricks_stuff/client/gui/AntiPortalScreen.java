/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  org.ExtraTortik.ricks_stuff.client.gui.AntiPortalMenu
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton$Style
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SUpdateAntiPortalConfigPacket
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.ExtraTortik.ricks_stuff.client.gui.AntiPortalMenu;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetButton;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.C2SUpdateAntiPortalConfigPacket;

public class AntiPortalScreen
extends AbstractContainerScreen<AntiPortalMenu> {
    private Button btnDec10;
    private Button btnDec1;
    private Button btnInc1;
    private Button btnInc10;
    private Button btnPower;

    public AntiPortalScreen(AntiPortalMenu menu, Inventory playerInventory, Component title) {
        super((AbstractContainerMenu)menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 176;
        this.inventoryLabelY = 82;
        this.inventoryLabelX = 8;
    }

    protected void init() {
        super.init();
        int left = this.leftPos;
        int top = this.topPos;
        this.btnDec10 = GadgetButton.gadget((Component)Component.literal((String)"-10"), b -> this.adjustRadius(-10)).bounds(left + 10, top + 36, 26, 16).style(GadgetButton.Style.NORMAL).build();
        this.btnDec1 = GadgetButton.gadget((Component)Component.literal((String)"-1"), b -> this.adjustRadius(-1)).bounds(left + 38, top + 36, 22, 16).style(GadgetButton.Style.NORMAL).build();
        this.btnInc1 = GadgetButton.gadget((Component)Component.literal((String)"+1"), b -> this.adjustRadius(1)).bounds(left + 116, top + 36, 22, 16).style(GadgetButton.Style.NORMAL).build();
        this.btnInc10 = GadgetButton.gadget((Component)Component.literal((String)"+10"), b -> this.adjustRadius(10)).bounds(left + 140, top + 36, 26, 16).style(GadgetButton.Style.NORMAL).build();
        this.addRenderableWidget((GuiEventListener)this.btnDec10);
        this.addRenderableWidget((GuiEventListener)this.btnDec1);
        this.addRenderableWidget((GuiEventListener)this.btnInc1);
        this.addRenderableWidget((GuiEventListener)this.btnInc10);
        this.btnPower = GadgetButton.gadget((Component)this.getPowerButtonText(((AntiPortalMenu)this.menu).isEnabled()), b -> this.togglePower()).bounds(left + 10, top + 56, 156, 18).style(((AntiPortalMenu)this.menu).isEnabled() ? GadgetButton.Style.PRIMARY : GadgetButton.Style.DANGER).build();
        this.addRenderableWidget((GuiEventListener)this.btnPower);
    }

    private void adjustRadius(int delta) {
        int current;
        int newR;
        if (this.minecraft != null && this.minecraft.player != null) {
            this.minecraft.player.playSound((SoundEvent)ModSounds.UI_PRESS.get(), 1.0f, 1.0f);
        }
        if ((newR = Math.max(8, Math.min(64, (current = ((AntiPortalMenu)this.menu).getRadius()) + delta))) == current) return;
        ((AntiPortalMenu)this.menu).setRadius(newR);
        this.sendConfigUpdate(newR, ((AntiPortalMenu)this.menu).isEnabled());
    }

    private void togglePower() {
        if (this.minecraft != null && this.minecraft.player != null) {
            this.minecraft.player.playSound((SoundEvent)ModSounds.UI_PRESS.get(), 1.0f, 1.0f);
        }
        boolean newEnabled = !((AntiPortalMenu)this.menu).isEnabled();
        ((AntiPortalMenu)this.menu).setEnabled(newEnabled);
        this.updatePowerButton(newEnabled);
        this.sendConfigUpdate(((AntiPortalMenu)this.menu).getRadius(), newEnabled);
    }

    private void updatePowerButton(boolean enabled) {
        if (this.btnPower == null) return;
        this.btnPower.setMessage(this.getPowerButtonText(enabled));
        ((GadgetButton)this.btnPower).setStyle(enabled ? GadgetButton.Style.PRIMARY : GadgetButton.Style.DANGER);
    }

    private Component getPowerButtonText(boolean enabled) {
        return enabled ? Component.literal((String)"POWER: ON") : Component.literal((String)"POWER: OFF");
    }

    private void sendConfigUpdate(int radius, boolean enabled) {
        ModMessages.sendToServer((CustomPacketPayload)new C2SUpdateAntiPortalConfigPacket(((AntiPortalMenu)this.menu).getBlockPos(), radius, enabled));
    }

    protected void containerTick() {
        super.containerTick();
        if (this.btnPower == null) return;
        this.updatePowerButton(((AntiPortalMenu)this.menu).isEnabled());
    }

    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        int left = this.leftPos;
        int top = this.topPos;
        graphics.fill(left, top, left + this.imageWidth, top + this.imageHeight, -15591648);
        graphics.fill(left + 2, top + 2, left + this.imageWidth - 2, top + this.imageHeight - 2, -15196118);
        graphics.fill(left, top, left + this.imageWidth, top + 2, -16718337);
        graphics.fill(left, top + this.imageHeight - 2, left + this.imageWidth, top + this.imageHeight, -16742230);
        graphics.fill(left, top, left + 2, top + this.imageHeight, -16733492);
        graphics.fill(left + this.imageWidth - 2, top, left + this.imageWidth, top + this.imageHeight, -16733492);
        boolean isActive = ((AntiPortalMenu)this.menu).isActive();
        int statusBg = isActive ? -16112098 : -14021102;
        int statusBorder = isActive ? -16711783 : -52412;
        graphics.fill(left + 10, top + 18, left + 166, top + 32, statusBg);
        graphics.fill(left + 10, top + 18, left + 166, top + 19, statusBorder);
        graphics.fill(left + 10, top + 31, left + 166, top + 32, statusBorder);
        graphics.fill(left + 10, top + 18, left + 11, top + 32, statusBorder);
        graphics.fill(left + 165, top + 18, left + 166, top + 32, statusBorder);
        String statusText = isActive ? "\u25cf SHIELD ACTIVE" : "\u25cb SHIELD OFFLINE";
        int statusColor = isActive ? 65484 : 0xFF6677;
        graphics.drawString(this.font, statusText, left + 16, top + 21, statusColor, false);
        graphics.fill(left + 62, top + 36, left + 114, top + 52, -16249582);
        graphics.fill(left + 62, top + 36, left + 114, top + 37, -16718337);
        graphics.fill(left + 62, top + 51, left + 114, top + 52, -16718337);
        graphics.fill(left + 62, top + 36, left + 63, top + 52, -16718337);
        graphics.fill(left + 113, top + 36, left + 114, top + 52, -16718337);
        String rStr = String.valueOf(((AntiPortalMenu)this.menu).getRadius()) + "m";
        int rWidth = this.font.width(rStr);
        graphics.drawString(this.font, rStr, left + 88 - rWidth / 2, top + 40, 65535, false);
        graphics.fill(left + 8, top + 78, left + 168, top + 79, -16742230);
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int slotX = left + 8 + col * 18;
                int slotY = top + 94 + row * 18;
                this.renderTechSlot(graphics, slotX - 1, slotY - 1);
            }
        }
        int col = 0;
        while (col < 9) {
            int slotX = left + 8 + col * 18;
            int slotY = top + 152;
            this.renderTechSlot(graphics, slotX - 1, slotY - 1);
            ++col;
        }
    }

    private void renderTechSlot(GuiGraphics graphics, int x, int y) {
        graphics.fill(x, y, x + 18, y + 18, -15986666);
        graphics.fill(x, y, x + 18, y + 1, -14800842);
        graphics.fill(x, y, x + 1, y + 18, -14800842);
        graphics.fill(x, y + 17, x + 18, y + 18, -14010294);
        graphics.fill(x + 17, y, x + 18, y + 18, -14010294);
    }

    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, (Component)Component.literal((String)"ANTI-PORTAL GENERATOR"), 8, 5, 58879, false);
        graphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 10531008, false);
    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }
}

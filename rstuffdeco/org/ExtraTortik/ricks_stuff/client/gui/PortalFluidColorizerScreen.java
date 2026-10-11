/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.component.CustomData
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton$Style
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetGuiUtils
 *  org.ExtraTortik.ricks_stuff.client.gui.PortalFluidColorizerMenu
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SColorizeFluidPacket
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetButton;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetGuiUtils;
import org.ExtraTortik.ricks_stuff.client.gui.PortalFluidColorizerMenu;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.C2SColorizeFluidPacket;

public class PortalFluidColorizerScreen
extends AbstractContainerScreen<PortalFluidColorizerMenu> {
    private static final int PICKER_SIZE = 72;
    private static final int SAMPLE_STEP = 2;
    private static final int SAMPLE_GRID = 36;
    private static final int[][] SPECTRUM_CACHE = new int[36][36];
    private GadgetButton colorizeButton;
    private GadgetButton channel1Button;
    private GadgetButton channel2Button;
    private int selectedFluidColorRgb = 2356545;
    private int selectedCoreColorRgb = 2356545;
    private int activeChannel = 1;
    private int cursorX = 25;
    private int cursorY = 30;
    private boolean isDraggingPicker = false;
    private ItemStack lastInputStack = ItemStack.EMPTY;

    public PortalFluidColorizerScreen(PortalFluidColorizerMenu menu, Inventory playerInventory, Component title) {
        super((AbstractContainerMenu)menu, playerInventory, title);
        this.imageWidth = 196;
        this.imageHeight = 224;
        this.inventoryLabelX = 18;
        this.inventoryLabelY = 130;
    }

    protected void init() {
        super.init();
        int left = this.leftPos;
        int top = this.topPos;
        this.readInputColors();
        this.setCursorFromRgb(this.activeChannel == 1 ? this.selectedFluidColorRgb : this.selectedCoreColorRgb);
        this.channel1Button = GadgetButton.gadget((Component)Component.literal((String)"1"), b -> this.setChannel(1)).bounds(left + 98, top + 46, 38, 16).style(this.activeChannel == 1 ? GadgetButton.Style.PRIMARY : GadgetButton.Style.NORMAL).build();
        this.channel2Button = GadgetButton.gadget((Component)Component.literal((String)"2"), b -> this.setChannel(2)).bounds(left + 140, top + 46, 38, 16).style(this.activeChannel == 2 ? GadgetButton.Style.PRIMARY : GadgetButton.Style.NORMAL).build();
        this.colorizeButton = GadgetButton.gadget((Component)Component.translatable((String)"gui.ricks_stuff.portal_fluid_colorizer.colorize"), b -> this.handleColorizeClicked()).bounds(left + 98, top + 96, 80, 20).style(GadgetButton.Style.PRIMARY).build();
        this.addRenderableWidget((GuiEventListener)this.channel1Button);
        this.addRenderableWidget((GuiEventListener)this.channel2Button);
        this.addRenderableWidget((GuiEventListener)this.colorizeButton);
        this.updateButtonState();
    }

    private void setChannel(int channel) {
        if (this.minecraft != null && this.minecraft.player != null) {
            this.minecraft.player.playSound((SoundEvent)ModSounds.UI_PRESS.get(), 1.0f, 1.2f);
        }
        this.activeChannel = channel;
        if (this.channel1Button != null) {
            this.channel1Button.setStyle(channel == 1 ? GadgetButton.Style.PRIMARY : GadgetButton.Style.NORMAL);
        }
        if (this.channel2Button != null) {
            this.channel2Button.setStyle(channel == 2 ? GadgetButton.Style.PRIMARY : GadgetButton.Style.NORMAL);
        }
        this.setCursorFromRgb(channel == 1 ? this.selectedFluidColorRgb : this.selectedCoreColorRgb);
    }

    private void readInputColors() {
        CustomData customData;
        if (((PortalFluidColorizerMenu)this.menu).slots.size() <= 0) return;
        ItemStack input = ((PortalFluidColorizerMenu)this.menu).getSlot(0).getItem();
        if (!input.isEmpty() && (customData = (CustomData)input.get(DataComponents.CUSTOM_DATA)) != null) {
            CompoundTag tag = customData.copyTag();
            if (tag.contains("FluidColor")) {
                this.selectedFluidColorRgb = tag.getInt("FluidColor");
            }
            this.selectedCoreColorRgb = tag.contains("CoreColor") ? tag.getInt("CoreColor") : this.selectedFluidColorRgb;
        }
        this.lastInputStack = input.copy();
    }

    private void handleColorizeClicked() {
        if (this.minecraft != null && this.minecraft.player != null) {
            this.minecraft.player.playSound((SoundEvent)ModSounds.UI_PRESS.get(), 1.0f, 1.0f);
        }
        if (((PortalFluidColorizerMenu)this.menu).getBlockPos() == null) return;
        ModMessages.sendToServer((CustomPacketPayload)new C2SColorizeFluidPacket(((PortalFluidColorizerMenu)this.menu).getBlockPos(), this.selectedFluidColorRgb, this.selectedCoreColorRgb));
    }

    protected void containerTick() {
        ItemStack input;
        super.containerTick();
        if (((PortalFluidColorizerMenu)this.menu).slots.size() > 0 && !ItemStack.matches((ItemStack)(input = ((PortalFluidColorizerMenu)this.menu).getSlot(0).getItem()), (ItemStack)this.lastInputStack)) {
            this.readInputColors();
            this.setCursorFromRgb(this.activeChannel == 1 ? this.selectedFluidColorRgb : this.selectedCoreColorRgb);
        }
        this.updateButtonState();
    }

    private void updateButtonState() {
        boolean ready = ((PortalFluidColorizerMenu)this.menu).isReadyToColorize();
        if (this.colorizeButton != null) {
            this.colorizeButton.visible = ready;
            this.colorizeButton.active = ready;
        }
        if (this.channel1Button != null) {
            this.channel1Button.visible = ready;
            this.channel1Button.active = ready;
        }
        if (this.channel2Button == null) return;
        this.channel2Button.visible = ready;
        this.channel2Button.active = ready;
    }

    private void setCursorFromRgb(int rgb) {
        float[] hsb = Color.RGBtoHSB(rgb >> 16 & 0xFF, rgb >> 8 & 0xFF, rgb & 0xFF, null);
        float h = hsb[0];
        float s = hsb[1];
        float v = hsb[2];
        this.cursorX = Math.max(0, Math.min(71, Math.round(h * 71.0f)));
        this.cursorY = v >= 0.95f ? Math.max(0, Math.min(35, Math.round(s * 35.0f))) : Math.max(36, Math.min(71, 36 + Math.round((1.0f - v) * 35.0f)));
    }

    private static int getColorAt(int relX, int relY) {
        float val;
        float sat;
        float hue = Math.max(0.0f, Math.min(1.0f, (float)relX / 71.0f));
        if (relY < 36) {
            sat = Math.max(0.05f, Math.min(1.0f, (float)relY / 35.0f));
            val = 1.0f;
        } else {
            sat = 1.0f;
            val = Math.max(0.0f, Math.min(1.0f, 1.0f - (float)(relY - 35) / 36.0f));
        }
        return Color.HSBtoRGB(hue, sat, val) & 0xFFFFFF;
    }

    private void updateColorFromMouse(double mouseX, double mouseY) {
        int pickerX = this.leftPos + 18;
        int pickerY = this.topPos + 48;
        int relX = (int)Math.max(0.0, Math.min(71.0, mouseX - (double)pickerX));
        int relY = (int)Math.max(0.0, Math.min(71.0, mouseY - (double)pickerY));
        this.cursorX = relX;
        this.cursorY = relY;
        int color = PortalFluidColorizerScreen.getColorAt(relX, relY);
        if (this.activeChannel == 1) {
            this.selectedFluidColorRgb = color;
        } else {
            this.selectedCoreColorRgb = color;
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return super.mouseClicked(mouseX, mouseY, button);
        if (!((PortalFluidColorizerMenu)this.menu).isReadyToColorize()) return super.mouseClicked(mouseX, mouseY, button);
        int pickerX = this.leftPos + 18;
        int pickerY = this.topPos + 48;
        if (mouseX >= (double)pickerX && mouseX < (double)(pickerX + 72) && mouseY >= (double)pickerY && mouseY < (double)(pickerY + 72)) {
            this.isDraggingPicker = true;
            this.updateColorFromMouse(mouseX, mouseY);
            return true;
        }
        int swatchY = this.topPos + 69;
        if (!(mouseY >= (double)swatchY)) return super.mouseClicked(mouseX, mouseY, button);
        if (!(mouseY <= (double)(swatchY + 18))) return super.mouseClicked(mouseX, mouseY, button);
        int s1X = this.leftPos + 103;
        int s2X = this.leftPos + 121;
        if (mouseX >= (double)s1X && mouseX <= (double)(s1X + 14)) {
            this.setChannel(1);
            return true;
        }
        if (!(mouseX >= (double)s2X)) return super.mouseClicked(mouseX, mouseY, button);
        if (!(mouseX <= (double)(s2X + 14))) return super.mouseClicked(mouseX, mouseY, button);
        this.setChannel(2);
        return true;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button != 0) return super.mouseReleased(mouseX, mouseY, button);
        this.isDraggingPicker = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button != 0) return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        if (!this.isDraggingPicker) return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        if (!((PortalFluidColorizerMenu)this.menu).isReadyToColorize()) return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        this.updateColorFromMouse(mouseX, mouseY);
        return true;
    }

    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        int left = this.leftPos;
        int top = this.topPos;
        GadgetGuiUtils.renderChassis((GuiGraphics)guiGraphics, (int)left, (int)top, (int)this.imageWidth, (int)this.imageHeight);
        GadgetGuiUtils.renderHeader((GuiGraphics)guiGraphics, (int)(left + 14), (int)(top + 5), (int)168, (int)12);
        boolean hasFluid = ((PortalFluidColorizerMenu)this.menu).slots.size() > 0 && !((PortalFluidColorizerMenu)this.menu).getSlot(0).getItem().isEmpty();
        boolean hasDye1 = ((PortalFluidColorizerMenu)this.menu).slots.size() > 1 && !((PortalFluidColorizerMenu)this.menu).getSlot(1).getItem().isEmpty();
        boolean hasDye2 = ((PortalFluidColorizerMenu)this.menu).slots.size() > 2 && !((PortalFluidColorizerMenu)this.menu).getSlot(2).getItem().isEmpty();
        boolean hasDye3 = ((PortalFluidColorizerMenu)this.menu).slots.size() > 3 && !((PortalFluidColorizerMenu)this.menu).getSlot(3).getItem().isEmpty();
        boolean hasOutput = ((PortalFluidColorizerMenu)this.menu).slots.size() > 4 && !((PortalFluidColorizerMenu)this.menu).getSlot(4).getItem().isEmpty();
        this.drawBeveledSlot(guiGraphics, left + 25, top + 21, -14002888);
        this.drawBeveledSlot(guiGraphics, left + 55, top + 21, -11194320);
        this.drawBeveledSlot(guiGraphics, left + 75, top + 21, -13609680);
        this.drawBeveledSlot(guiGraphics, left + 95, top + 21, -13619115);
        this.drawBeveledSlot(guiGraphics, left + 151, top + 21, -14137256);
        if (!hasFluid) {
            guiGraphics.drawCenteredString(this.font, "\u00a78F", left + 34, top + 26, 0x446655);
        }
        if (!hasDye1) {
            guiGraphics.drawCenteredString(this.font, "\u00a78D1", left + 64, top + 26, 0x664444);
        }
        if (!hasDye2) {
            guiGraphics.drawCenteredString(this.font, "\u00a78D2", left + 84, top + 26, 0x446644);
        }
        if (!hasDye3) {
            guiGraphics.drawCenteredString(this.font, "\u00a78D3", left + 104, top + 26, 0x444466);
        }
        if (!hasOutput) {
            guiGraphics.drawCenteredString(this.font, "\u00a78OUT", left + 160, top + 26, 0x445566);
        }
        boolean isReady = ((PortalFluidColorizerMenu)this.menu).isReadyToColorize();
        int tubeY = top + 29;
        guiGraphics.fill(left + 116, tubeY, left + 148, tubeY + 4, -15724010);
        guiGraphics.renderOutline(left + 116, tubeY, 32, 4, -14407892);
        int conduitColor = isReady ? -12720539 : -14012360;
        guiGraphics.fill(left + 117, tubeY + 1, left + 147, tubeY + 3, conduitColor);
        guiGraphics.drawString(this.font, "\u25ba", left + 130, top + 27, isReady ? -12720539 : -12236462, false);
        if (!isReady) {
            GadgetGuiUtils.renderTerminalScreen((GuiGraphics)guiGraphics, (int)(left + 14), (int)(top + 46), (int)168, (int)76);
            int crtCenterX = left + 14 + 84;
            guiGraphics.drawCenteredString(this.font, "\u00a78[SPECTRAL SYNTHESIZER]", crtCenterX, top + 55, 0x888888);
            guiGraphics.drawCenteredString(this.font, "\u00a7eInsert Portal Fluid", crtCenterX, top + 70, 0xFFFF55);
            guiGraphics.drawCenteredString(this.font, "\u00a77and 3 Dyes to unlock", crtCenterX, top + 84, 0xAAAAAA);
            guiGraphics.drawCenteredString(this.font, "\u00a73Full RGB Spectrum", crtCenterX, top + 98, 0x55FFFF);
        } else {
            int pickerX = left + 18;
            int pickerY = top + 48;
            guiGraphics.renderOutline(left + 16, top + 46, 76, 76, -13289150);
            guiGraphics.renderOutline(left + 17, top + 47, 74, 74, -16118770);
            for (int bx = 0; bx < 36; ++bx) {
                int px = pickerX + bx * 2;
                for (int by = 0; by < 36; ++by) {
                    int py = pickerY + by * 2;
                    guiGraphics.fill(px, py, px + 2, py + 2, SPECTRUM_CACHE[bx][by]);
                }
            }
            int cx = pickerX + this.cursorX;
            int cy = pickerY + this.cursorY;
            guiGraphics.renderOutline(cx - 3, cy - 3, 7, 7, -16777216);
            guiGraphics.renderOutline(cx - 2, cy - 2, 5, 5, -1);
            guiGraphics.fill(cx, cy, cx + 1, cy + 1, -16777216);
            GadgetGuiUtils.renderTerminalScreen((GuiGraphics)guiGraphics, (int)(left + 98), (int)(top + 65), (int)80, (int)28);
            int s1X = left + 103;
            int sY = top + 69;
            int sW = 14;
            int sH = 18;
            guiGraphics.fill(s1X - 1, sY - 1, s1X + sW + 1, sY + sH + 1, -15592425);
            guiGraphics.fill(s1X, sY, s1X + sW, sY + sH, 0xFF000000 | this.selectedFluidColorRgb);
            guiGraphics.renderOutline(s1X, sY, sW, sH, this.activeChannel == 1 ? -11141121 : 0x44FFFFFF);
            guiGraphics.drawCenteredString(this.font, "1", s1X + sW / 2, sY + 5, this.activeChannel == 1 ? -1 : -1426063361);
            int s2X = left + 121;
            guiGraphics.fill(s2X - 1, sY - 1, s2X + sW + 1, sY + sH + 1, -15592425);
            guiGraphics.fill(s2X, sY, s2X + sW, sY + sH, 0xFF000000 | this.selectedCoreColorRgb);
            guiGraphics.renderOutline(s2X, sY, sW, sH, this.activeChannel == 2 ? -11141121 : 0x44FFFFFF);
            guiGraphics.drawCenteredString(this.font, "2", s2X + sW / 2, sY + 5, this.activeChannel == 2 ? -1 : -1426063361);
            int textX = left + 140;
            guiGraphics.drawString(this.font, this.activeChannel == 1 ? "FLUID" : "CORE", textX, top + 69, -7696228, false);
            String hexStr = String.format("#%06X", this.activeChannel == 1 ? this.selectedFluidColorRgb : this.selectedCoreColorRgb);
            guiGraphics.drawString(this.font, hexStr, textX, top + 79, -12720539, false);
        }
        guiGraphics.fill(left + 14, top + 126, left + 182, top + 127, -13289150);
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.drawInventorySlot(guiGraphics, left + 17 + col * 18, top + 141 + row * 18);
            }
        }
        int col = 0;
        while (col < 9) {
            this.drawInventorySlot(guiGraphics, left + 17 + col * 18, top + 199);
            ++col;
        }
    }

    private void drawBeveledSlot(GuiGraphics guiGraphics, int x, int y, int outlineColor) {
        guiGraphics.fill(x, y, x + 18, y + 18, -15724010);
        guiGraphics.fill(x, y, x + 18, y + 1, -16118770);
        guiGraphics.fill(x, y, x + 1, y + 18, -16118770);
        guiGraphics.fill(x, y + 17, x + 18, y + 18, -14012358);
        guiGraphics.fill(x + 17, y, x + 18, y + 18, -14012358);
        if (outlineColor == 0) return;
        guiGraphics.renderOutline(x - 1, y - 1, 20, 20, outlineColor);
    }

    private void drawInventorySlot(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.fill(x, y, x + 18, y + 18, -15592424);
        guiGraphics.fill(x, y, x + 18, y + 1, -16184819);
        guiGraphics.fill(x, y, x + 1, y + 18, -16184819);
        guiGraphics.fill(x, y + 17, x + 18, y + 18, -14276046);
        guiGraphics.fill(x + 17, y, x + 18, y + 18, -14276046);
    }

    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 18, 7, -12720539, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, -7696228, false);
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        if (!((PortalFluidColorizerMenu)this.menu).isReadyToColorize()) return;
        int sY = this.topPos + 69;
        int s1X = this.leftPos + 103;
        int s2X = this.leftPos + 121;
        if (mouseY >= sY && mouseY <= sY + 18) {
            if (mouseX >= s1X && mouseX <= s1X + 14) {
                guiGraphics.renderTooltip(this.font, (Component)Component.translatable((String)"gui.ricks_stuff.portal_fluid_colorizer.channel_1").append(String.format(" (#%06X)", this.selectedFluidColorRgb)), mouseX, mouseY);
            } else if (mouseX >= s2X && mouseX <= s2X + 14) {
                guiGraphics.renderTooltip(this.font, (Component)Component.translatable((String)"gui.ricks_stuff.portal_fluid_colorizer.channel_2").append(String.format(" (#%06X)", this.selectedCoreColorRgb)), mouseX, mouseY);
            }
        }
        if (mouseY < this.topPos + 46) return;
        if (mouseY > this.topPos + 62) return;
        if (mouseX >= this.leftPos + 98 && mouseX <= this.leftPos + 136) {
            guiGraphics.renderTooltip(this.font, (Component)Component.translatable((String)"gui.ricks_stuff.portal_fluid_colorizer.channel_1"), mouseX, mouseY);
        } else {
            if (mouseX < this.leftPos + 140) return;
            if (mouseX > this.leftPos + 178) return;
            guiGraphics.renderTooltip(this.font, (Component)Component.translatable((String)"gui.ricks_stuff.portal_fluid_colorizer.channel_2"), mouseX, mouseY);
        }
    }

    static {
        int bx = 0;
        while (bx < 36) {
            int rx = bx * 2;
            for (int by = 0; by < 36; ++by) {
                int ry = by * 2;
                PortalFluidColorizerScreen.SPECTRUM_CACHE[bx][by] = 0xFF000000 | PortalFluidColorizerScreen.getColorAt(rx, ry);
            }
            ++bx;
        }
    }
}

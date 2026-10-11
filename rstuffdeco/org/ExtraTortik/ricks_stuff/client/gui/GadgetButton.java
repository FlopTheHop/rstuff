/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.Button$OnPress
 *  net.minecraft.network.chat.Component
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton$Builder
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton$Style
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetButton;

public class GadgetButton
extends Button {
    private Style style = Style.NORMAL;

    public GadgetButton(int x, int y, int width, int height, Component message, Button.OnPress onPress, Style style) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.style = style != null ? style : Style.NORMAL;
    }

    public GadgetButton(int x, int y, int width, int height, Component message, Button.OnPress onPress) {
        this(x, y, width, height, message, onPress, Style.NORMAL);
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public Style getStyle() {
        return this.style;
    }

    public static Builder gadget(Component message, Button.OnPress onPress) {
        return new Builder(message, onPress);
    }

    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int textColor;
        int ledColor;
        int border;
        int bevelDark;
        int bevelLight;
        int bg;
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;
        boolean hovered = this.isHoveredOrFocused();
        int x = this.getX();
        int y = this.getY();
        int w = this.getWidth();
        int h = this.getHeight();
        if (!this.active) {
            bg = -15197667;
            bevelLight = -14407892;
            bevelDark = -15789805;
            border = -16119027;
            ledColor = -14144720;
            textColor = -11183258;
        } else if (hovered) {
            switch (this.style.ordinal()) {
                case 1: {
                    bg = -14534616;
                    bevelLight = -13278913;
                    bevelDark = -15457255;
                    border = -12720539;
                    ledColor = -12189840;
                    textColor = -7995484;
                    break;
                }
                case 2: {
                    bg = -13098974;
                    bevelLight = -10407884;
                    bevelDark = -14413035;
                    border = -48060;
                    ledColor = -47803;
                    textColor = -24930;
                    break;
                }
                default: {
                    bg = -13683908;
                    bevelLight = -11248532;
                    bevelDark = -15197408;
                    border = -13183000;
                    ledColor = -13183000;
                    textColor = -1;
                    break;
                }
            }
        } else {
            switch (this.style.ordinal()) {
                case 1: {
                    bg = -14865374;
                    bevelLight = -13742792;
                    bevelDark = -15655915;
                    border = -15195108;
                    ledColor = -13912755;
                    textColor = -11868557;
                    break;
                }
                case 2: {
                    bg = -13886434;
                    bevelLight = -11719632;
                    bevelDark = -14937837;
                    border = -14412264;
                    ledColor = -4904149;
                    textColor = -1682091;
                    break;
                }
                default: {
                    bg = -14539477;
                    bevelLight = -12301995;
                    bevelDark = -15526632;
                    border = -15921648;
                    ledColor = -12038566;
                    textColor = -2959649;
                }
            }
        }
        guiGraphics.fill(x, y, x + w, y + h, border);
        guiGraphics.fill(x + 1, y + 1, x + w - 1, y + h - 1, bg);
        guiGraphics.fill(x + 1, y + 1, x + w - 1, y + 2, bevelLight);
        guiGraphics.fill(x + 1, y + 1, x + 2, y + h - 1, bevelLight);
        guiGraphics.fill(x + 1, y + h - 2, x + w - 1, y + h - 1, bevelDark);
        guiGraphics.fill(x + w - 2, y + 1, x + w - 1, y + h - 1, bevelDark);
        if (h >= 14 && w >= 30) {
            int ledX = x + 4;
            int ledY = y + (h - 4) / 2;
            guiGraphics.fill(ledX - 1, ledY - 1, ledX + 4, ledY + 4, -15921648);
            guiGraphics.fill(ledX, ledY, ledX + 3, ledY + 3, ledColor);
            if (hovered && this.active) {
                guiGraphics.fill(ledX + 1, ledY + 1, ledX + 2, ledY + 2, -1);
            }
        }
        int textOffsetLeft = h >= 14 && w >= 45 ? 4 : 0;
        int textX = x + (w + textOffsetLeft) / 2;
        int textY = y + (h - 8) / 2;
        guiGraphics.drawCenteredString(font, this.getMessage(), textX, textY, textColor);
    }
}

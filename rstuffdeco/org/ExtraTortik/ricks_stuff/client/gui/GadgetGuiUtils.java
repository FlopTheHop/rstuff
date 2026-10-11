/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class GadgetGuiUtils {
    public static final int COLOR_CHASSIS_BG = -14407892;
    public static final int COLOR_CHASSIS_LIGHT = -12236462;
    public static final int COLOR_CHASSIS_DARK = -15592425;
    public static final int COLOR_SHADOW = -16250614;
    public static final int COLOR_HEADER_BG = -15065822;
    public static final int COLOR_HEADER_BORDER = -13289150;
    public static final int COLOR_CRT_BG = -16379127;
    public static final int COLOR_CRT_BORDER = -15785453;
    public static final int COLOR_CRT_SHEEN = -16047086;
    public static final int COLOR_TEXT_PHOSPHOR = -12720539;
    public static final int COLOR_TEXT_AMBER = -19674;
    public static final int COLOR_TEXT_CYAN = -13183000;
    public static final int COLOR_TEXT_MUTED = -7696228;
    public static final int COLOR_FLUID_GREEN_MID = -13638053;
    public static final int COLOR_FLUID_GREEN_TOP = -7995484;
    public static final int COLOR_FLUID_GREEN_BOT = -15098311;

    public static void renderChassis(GuiGraphics g, int x, int y, int width, int height) {
        g.fill(x - 2, y - 2, x + width + 2, y + height + 2, -16250614);
        g.fill(x, y, x + width, y + height, -14407892);
        g.fill(x, y, x + width, y + 2, -12236462);
        g.fill(x, y, x + 2, y + height, -12236462);
        g.fill(x, y + height - 2, x + width, y + height, -15592425);
        g.fill(x + width - 2, y, x + width, y + height, -15592425);
        GadgetGuiUtils.renderRivet(g, x + 4, y + 4);
        GadgetGuiUtils.renderRivet(g, x + width - 7, y + 4);
        GadgetGuiUtils.renderRivet(g, x + 4, y + height - 7);
        GadgetGuiUtils.renderRivet(g, x + width - 7, y + height - 7);
    }

    public static void renderHeader(GuiGraphics g, int x, int y, int width, int height) {
        g.fill(x - 1, y - 1, x + width + 1, y + height + 1, -15592425);
        g.fill(x, y, x + width, y + height, -15065822);
        g.fill(x, y + height - 1, x + width, y + height, -13289150);
        g.fill(x, y, x + width, y + 1, -12236462);
    }

    public static void renderTerminalScreen(GuiGraphics g, int x, int y, int width, int height) {
        g.fill(x - 1, y - 1, x + width + 1, y + height + 1, -15592425);
        g.fill(x, y, x + width, y + height, -16379127);
        g.renderOutline(x, y, width, height, -15785453);
        g.fill(x + 1, y + 1, x + width - 1, y + 2, -16047086);
    }

    public static void renderFluidTube(GuiGraphics g, Font font, int x, int y, int width, int height, int charge, boolean showText) {
        g.fill(x - 2, y - 1, x, y + height + 1, -12630964);
        g.fill(x + width, y - 1, x + width + 2, y + height + 1, -12630964);
        g.fill(x, y, x + width, y + height, -15919344);
        g.renderOutline(x, y, width, height, -14930914);
        int fillW = (int)Math.round((double)(width - 2) * ((double)Math.max(0, Math.min(100, charge)) / 100.0));
        if (fillW > 0) {
            int fluidMid;
            int fluidBase;
            int n = charge > 25 ? -15098311 : (fluidBase = charge > 10 ? -5076992 : -6415079);
            int n2 = charge > 25 ? -13638053 : (fluidMid = charge > 10 ? -13800 : -1691863);
            int fluidTop = charge > 25 ? -7995484 : (charge > 10 ? -6027 : -31358);
            g.fill(x + 1, y + 1, x + 1 + fillW, y + height - 1, fluidMid);
            g.fill(x + 1, y + height - 3, x + 1 + fillW, y + height - 1, fluidBase);
            g.fill(x + 1, y + 1, x + 1 + fillW, y + 2, fluidTop);
            if (fillW < width - 2) {
                g.fill(x + fillW, y + 1, x + 1 + fillW, y + height - 1, fluidTop);
            }
        }
        for (int p = 25; p <= 75; p += 25) {
            int gx = x + 1 + (int)((double)(width - 2) * ((double)p / 100.0));
            g.fill(gx, y + 1, gx + 1, y + 4, 0x55FFFFFF);
            g.fill(gx, y + height - 4, gx + 1, y + height - 1, 0x55000000);
        }
        g.fill(x + 2, y + 1, x + width - 2, y + 2, 0x33FFFFFF);
        if (!showText) return;
        if (font == null) return;
        int textX = x + width + 6;
        int textColor = charge > 25 ? -12720539 : (charge > 10 ? -19674 : -48060);
        g.drawString(font, charge + "%", textX, y + (height - 8) / 2, textColor, true);
    }

    public static void renderRivet(GuiGraphics g, int rx, int ry) {
        g.fill(rx, ry, rx + 3, ry + 3, -15592425);
        g.fill(rx + 1, ry + 1, rx + 2, ry + 2, -10722450);
    }

    public static void renderLed(GuiGraphics g, int x, int y, int color) {
        g.fill(x, y, x + 4, y + 4, -15724012);
        g.fill(x + 1, y + 1, x + 3, y + 3, color);
        g.fill(x + 1, y + 1, x + 2, y + 2, -1);
    }
}

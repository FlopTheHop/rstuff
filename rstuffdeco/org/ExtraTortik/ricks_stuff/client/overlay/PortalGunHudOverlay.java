/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.LayeredDraw$Layer
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.ExtraTortik.ricks_stuff.portal.PortalMode
 */
package org.ExtraTortik.ricks_stuff.client.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.ExtraTortik.ricks_stuff.portal.PortalMode;

public class PortalGunHudOverlay {
    public static final LayeredDraw.Layer HUD_OVERLAY = (guiGraphics, deltaTracker) -> {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;
        if (mc.options.hideGui) {
            return;
        }
        ItemStack stack = PortalGunHudOverlay.getHeldGun(player);
        if (stack == null) return;
        Item patt0$temp = stack.getItem();
        if (!(patt0$temp instanceof PortalGunItem)) return;
        PortalGunItem gunItem = (PortalGunItem)patt0$temp;
        Font font = mc.font;
        int charge = PortalGunItem.getCharge((ItemStack)stack);
        PortalMode mode = PortalGunItem.getMode((ItemStack)stack);
        boolean isInfinite = PortalGunItem.hasInfinityBulb((ItemStack)stack);
        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();
        if (!PortalGunItem.isChargeBar((ItemStack)stack)) return;
        PortalGunHudOverlay.renderChargeBar(guiGraphics, font, width, height, stack, gunItem, charge, mode, isInfinite);
    };

    private static void renderChargeBar(GuiGraphics guiGraphics, Font font, int width, int height, ItemStack gunStack, PortalGunItem gunItem, int charge, PortalMode mode, boolean isInfinite) {
        int fillW;
        int ledColor;
        boolean scaled;
        int x;
        int boxW = 120;
        int boxH = 34;
        String anchor = Config.hudAnchor != null ? Config.hudAnchor.trim().toUpperCase() : "BOTTOM_LEFT";
        int offX = Config.hudOffsetX;
        int offY = Config.hudOffsetY;
        int y = switch (anchor) {
            case "TOP_LEFT" -> {
                x = offX;
                yield offY;
            }
            case "TOP_RIGHT" -> {
                x = width - boxW - offX;
                yield offY;
            }
            case "BOTTOM_RIGHT" -> {
                x = width - boxW - offX;
                yield height - boxH - offY;
            }
            case "BOTTOM_LEFT" -> {
                x = offX;
                yield height - boxH - offY;
            }
            default -> {
                x = offX;
                yield height - boxH - offY;
            }
        };
        float scale = (float)Config.hudScale;
        boolean bl = scaled = Math.abs(scale - 1.0f) > 0.01f;
        if (scaled) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate((float)x, (float)y, 0.0f);
            guiGraphics.pose().scale(scale, scale, 1.0f);
            guiGraphics.pose().translate((float)(-x), (float)(-y), 0.0f);
        }
        guiGraphics.fill(x - 1, y - 1, x + boxW + 1, y + boxH + 1, -16250614);
        guiGraphics.fill(x, y, x + boxW, y + boxH, -14013134);
        guiGraphics.fill(x, y, x + boxW, y + 1, -11907497);
        guiGraphics.fill(x, y, x + 1, y + boxH, -11907497);
        guiGraphics.fill(x, y + boxH - 1, x + boxW, y + boxH, -15460840);
        guiGraphics.fill(x + boxW - 1, y, x + boxW, y + boxH, -15460840);
        PortalGunHudOverlay.renderRivet(guiGraphics, x + 2, y + 2);
        PortalGunHudOverlay.renderRivet(guiGraphics, x + boxW - 5, y + 2);
        PortalGunHudOverlay.renderRivet(guiGraphics, x + 2, y + boxH - 5);
        PortalGunHudOverlay.renderRivet(guiGraphics, x + boxW - 5, y + boxH - 5);
        int screenX = x + 8;
        int screenY = y + 4;
        int screenW = boxW - 16;
        int screenH = 11;
        guiGraphics.fill(screenX - 1, screenY - 1, screenX + screenW + 1, screenY + screenH + 1, -15724012);
        guiGraphics.fill(screenX, screenY, screenX + screenW, screenY + screenH, -16379127);
        guiGraphics.fill(screenX, screenY, screenX + screenW, screenY + 1, -16046573);
        String modeName = mode == PortalMode.LIFO ? "OVERRIDE" : (mode == PortalMode.FIFO ? "CYCLE" : mode.getDisplayName().toUpperCase());
        String modeText = "MODE: " + modeName;
        guiGraphics.drawString(font, modeText, screenX + 3, screenY + 2, -13048482, false);
        if (isInfinite) {
            float pulse = 0.7f + 0.3f * (float)Math.sin((double)System.currentTimeMillis() * 0.006);
            int g = (int)(255.0f * pulse);
            ledColor = 0xFF300000 | g << 8 | 0xFF;
        } else {
            ledColor = charge > 20 ? -16711834 : (charge > 0 ? -17664 : -56798);
        }
        guiGraphics.fill(screenX + screenW - 6, screenY + 3, screenX + screenW - 2, screenY + 7, ledColor);
        guiGraphics.fill(screenX + screenW - 5, screenY + 4, screenX + screenW - 3, screenY + 6, -1);
        int tubeX = x + 8;
        int tubeY = y + 18;
        int tubeW = 74;
        int tubeH = 10;
        guiGraphics.fill(tubeX - 2, tubeY - 1, tubeX, tubeY + tubeH + 1, -12630964);
        guiGraphics.fill(tubeX + tubeW, tubeY - 1, tubeX + tubeW + 2, tubeY + tubeH + 1, -12630964);
        guiGraphics.fill(tubeX, tubeY, tubeX + tubeW, tubeY + tubeH, -15919344);
        guiGraphics.renderOutline(tubeX, tubeY, tubeW, tubeH, -14996962);
        int n = fillW = isInfinite ? tubeW - 2 : (int)Math.round((double)(tubeW - 2) * ((double)Math.max(0, Math.min(100, charge)) / 100.0));
        if (fillW > 0) {
            int fluidTop;
            int fluidMid;
            int fluidBase;
            boolean isCustom;
            int fluidRgb = PortalGunItem.getFluidColor((ItemStack)gunStack);
            boolean bl2 = isCustom = fluidRgb != 2356545;
            if (isCustom) {
                int r = fluidRgb >> 16 & 0xFF;
                int g = fluidRgb >> 8 & 0xFF;
                int b = fluidRgb & 0xFF;
                fluidBase = 0xFF000000 | (int)((float)r * 0.6f) << 16 | (int)((float)g * 0.6f) << 8 | (int)((float)b * 0.6f);
                fluidMid = 0xFF000000 | r << 16 | g << 8 | b;
                fluidTop = 0xFF000000 | Math.min(255, (int)((float)r * 0.65f + 89.25f)) << 16 | Math.min(255, (int)((float)g * 0.65f + 89.25f)) << 8 | Math.min(255, (int)((float)b * 0.65f + 89.25f));
            } else if (isInfinite) {
                fluidBase = -15758792;
                fluidMid = -13240978;
                fluidTop = -4653102;
            } else {
                int n2 = charge > 25 ? -15098311 : (fluidBase = charge > 10 ? -5076992 : -6415079);
                int n3 = charge > 25 ? -13638053 : (fluidMid = charge > 10 ? -13800 : -1691863);
                fluidTop = charge > 25 ? -7995484 : (charge > 10 ? -6027 : -31358);
            }
            guiGraphics.fill(tubeX + 1, tubeY + 1, tubeX + 1 + fillW, tubeY + tubeH - 1, fluidMid);
            guiGraphics.fill(tubeX + 1, tubeY + tubeH - 3, tubeX + 1 + fillW, tubeY + tubeH - 1, fluidBase);
            guiGraphics.fill(tubeX + 1, tubeY + 1, tubeX + 1 + fillW, tubeY + 2, fluidTop);
            if (!isInfinite && fillW < tubeW - 2) {
                guiGraphics.fill(tubeX + fillW, tubeY + 1, tubeX + 1 + fillW, tubeY + tubeH - 1, fluidTop);
            }
        }
        for (int p = 25; p <= 75; p += 25) {
            int gx = tubeX + 1 + (int)((double)(tubeW - 2) * ((double)p / 100.0));
            guiGraphics.fill(gx, tubeY + 1, gx + 1, tubeY + 4, 0x55FFFFFF);
            guiGraphics.fill(gx, tubeY + tubeH - 4, gx + 1, tubeY + tubeH - 1, 0x55000000);
        }
        guiGraphics.fill(tubeX + 2, tubeY + 1, tubeX + tubeW - 2, tubeY + 2, 0x33FFFFFF);
        int textX = tubeX + tubeW + 5;
        if (isInfinite) {
            guiGraphics.drawString(font, "\u221e", textX + 3, tubeY + 1, -12517499, true);
        } else {
            int textColor = charge > 25 ? -13048482 : (charge > 10 ? -13278 : -48060);
            guiGraphics.drawString(font, charge + "%", textX, tubeY + 1, textColor, true);
        }
        if (!scaled) return;
        guiGraphics.pose().popPose();
    }

    private static void renderRivet(GuiGraphics guiGraphics, int rx, int ry) {
        guiGraphics.fill(rx, ry, rx + 3, ry + 3, -15460840);
        guiGraphics.fill(rx + 1, ry + 1, rx + 2, ry + 2, -10854294);
    }

    private static ItemStack getHeldGun(LocalPlayer player) {
        if (player.getMainHandItem().getItem() instanceof PortalGunItem) {
            return player.getMainHandItem();
        }
        if (!(player.getOffhandItem().getItem() instanceof PortalGunItem)) return null;
        return player.getOffhandItem();
    }
}

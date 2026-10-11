/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.gui.LayeredDraw$Layer
 */
package org.ExtraTortik.ricks_stuff.client.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.LayeredDraw;

public class PortalFadeOverlay {
    public static final int COLOR_WHITE_FLASH = 99;
    private static long durationMs = 600L;
    private static long startTime = -1L;
    private static int currentColor = 0;
    public static final LayeredDraw.Layer OVERLAY = (guiGraphics, deltaTracker) -> {
        int g;
        int r;
        if (startTime < 0L) {
            return;
        }
        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed >= durationMs) {
            startTime = -1L;
            return;
        }
        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();
        float progress = (float)elapsed / (float)durationMs;
        float alpha = currentColor == 99 ? (progress < 0.65f ? 1.0f : 1.0f - (progress - 0.65f) / 0.35f) : (progress < 0.45f ? progress / 0.45f * 0.75f : (1.0f - (progress - 0.45f) / 0.55f) * 0.75f);
        int alphaInt = (int)(Math.max(0.0f, Math.min(1.0f, alpha)) * 255.0f);
        if (alphaInt <= 0) {
            return;
        }
        int b = switch (currentColor) {
            case 99 -> {
                r = 250;
                g = 255;
                yield 250;
            }
            case 1 -> {
                r = 20;
                g = 130;
                yield 255;
            }
            case 2 -> {
                r = 255;
                g = 225;
                yield 30;
            }
            default -> {
                r = 40;
                g = 240;
                yield 70;
            }
        };
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        int argb = alphaInt << 24 | r << 16 | g << 8 | b;
        guiGraphics.fill(0, 0, width, height, argb);
        RenderSystem.disableBlend();
    };

    public static void trigger(int color) {
        PortalFadeOverlay.trigger(color, 600L);
    }

    public static void trigger(int color, long duration) {
        startTime = System.currentTimeMillis();
        currentColor = color;
        durationMs = duration > 0L ? duration : 600L;
    }
}

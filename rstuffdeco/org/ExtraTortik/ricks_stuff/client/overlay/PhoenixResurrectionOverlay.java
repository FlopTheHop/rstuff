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

public class PhoenixResurrectionOverlay {
    private static long startTime = -1L;
    private static long durationMs = 2400L;
    private static int activeCatalyst = 0;
    private static boolean corrupted = false;
    public static final LayeredDraw.Layer OVERLAY = (guiGraphics, deltaTracker) -> {
        float dripTime;
        float dropFade;
        int dropAlpha;
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
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        if (elapsed < 400L) {
            float flashAlpha = 1.0f - (float)elapsed / 400.0f;
            int flashR = 180;
            int flashG = 255;
            int flashB = 200;
            if (activeCatalyst == 1) {
                flashR = 120;
                flashG = 240;
                flashB = 255;
            } else if (activeCatalyst == 2 || activeCatalyst == 3) {
                flashR = 255;
                flashG = 230;
                flashB = 100;
            } else if (corrupted) {
                flashR = 140;
                flashG = 160;
                flashB = 60;
            }
            int alphaInt = (int)(flashAlpha * 0.85f * 255.0f);
            int flashColor = alphaInt << 24 | flashR << 16 | flashG << 8 | flashB;
            guiGraphics.fill(0, 0, width, height, flashColor);
        }
        float vigAlpha = progress < 0.25f ? progress / 0.25f : 1.0f - (progress - 0.25f) / 0.75f;
        int vigR = 15;
        int vigG = 120;
        int vigB = 40;
        if (activeCatalyst == 1) {
            vigR = 10;
            vigG = 100;
            vigB = 140;
        } else if (activeCatalyst == 2 || activeCatalyst == 3) {
            vigR = 140;
            vigG = 100;
            vigB = 10;
        } else if (corrupted) {
            vigR = 80;
            vigG = 80;
            vigB = 20;
        }
        int vigAlphaInt = (int)(vigAlpha * 0.55f * 255.0f);
        if (vigAlphaInt > 0) {
            int vigBorder = Math.min(width, height) / 4;
            int vigColor = vigAlphaInt << 24 | vigR << 16 | vigG << 8 | vigB;
            guiGraphics.fillGradient(0, 0, width, vigBorder, vigColor, 0);
            guiGraphics.fillGradient(0, height - vigBorder, width, height, 0, vigColor);
            guiGraphics.fillGradient(0, 0, vigBorder, height, vigColor, 0);
            guiGraphics.fillGradient(width - vigBorder, 0, width, height, 0, vigColor);
        }
        if (elapsed > 200L && elapsed < 2000L && (dropAlpha = (int)((dropFade = (dripTime = (float)(elapsed - 200L) / 1800.0f) < 0.6f ? 1.0f : 1.0f - (dripTime - 0.6f) / 0.4f) * 0.75f * 255.0f)) > 0) {
            int dropCount = 10;
            for (int i = 0; i < dropCount; ++i) {
                int seed = i * 37 + 13;
                float startX = (float)(seed * 73 % 100) / 100.0f * (float)width;
                float speed = 0.4f + (float)(seed % 10) / 10.0f * 0.6f;
                float dropY = dripTime * speed * (float)height * 0.85f;
                int dropLen = 8 + seed % 14;
                int dropW = 2 + seed % 3;
                int dropCol = dropAlpha << 24 | vigR << 16 | vigG << 8 | vigB;
                int trailCol = dropAlpha / 3 << 24 | vigR << 16 | vigG << 8 | vigB;
                guiGraphics.fill((int)startX, (int)(dropY - (float)dropLen), (int)startX + dropW, (int)dropY, trailCol);
                guiGraphics.fill((int)startX - 1, (int)dropY, (int)startX + dropW + 1, (int)dropY + 3, dropCol);
            }
        }
        RenderSystem.disableBlend();
    };

    public static void trigger(long duration, int catalyst, boolean isCorrupted) {
        startTime = System.currentTimeMillis();
        durationMs = duration > 0L ? duration : 2400L;
        activeCatalyst = catalyst;
        corrupted = isCorrupted;
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.gui.LayeredDraw$Layer
 *  net.minecraft.util.Mth
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 */
package org.ExtraTortik.ricks_stuff.client.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Random;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(value=Dist.CLIENT)
public class OmegaScreenDistortionOverlay {
    private static long flashStartTime = -1L;
    private static long flashDurationMs = 1200L;
    private static float flashIntensity = 1.0f;
    private static long glitchStartTime = -1L;
    private static long glitchDurationMs = 1500L;
    private static float glitchIntensity = 1.0f;
    private static float trauma = 0.0f;
    private static int traumaTicksRemaining = 0;
    private static final Random RANDOM = new Random();
    public static final LayeredDraw.Layer OVERLAY = (guiGraphics, deltaTracker) -> {
        boolean hasFlash;
        long now = System.currentTimeMillis();
        boolean bl = hasFlash = flashStartTime > 0L && now - flashStartTime < flashDurationMs;
        if (!hasFlash && trauma <= 0.01f) {
            return;
        }
        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        if (hasFlash) {
            float alpha;
            long elapsed = now - flashStartTime;
            float progress = (float)elapsed / (float)flashDurationMs;
            if (progress < 0.2f) {
                alpha = flashIntensity;
            } else {
                float decayProg = (progress - 0.2f) / 0.8f;
                alpha = flashIntensity * (float)Math.pow(1.0f - decayProg, 2.5);
            }
            int alphaInt = (int)(Mth.clamp((float)alpha, (float)0.0f, (float)1.0f) * 255.0f);
            if (alphaInt > 0) {
                int flashColor = alphaInt << 24 | 0xF50000 | 0xFF00 | 0xFA;
                guiGraphics.fill(0, 0, width, height, flashColor);
            }
        }
        if (!(trauma > 0.01f)) return;
        float shakeAlpha = Mth.clamp((float)(trauma * 0.45f), (float)0.0f, (float)0.65f);
        int a = (int)(shakeAlpha * 255.0f);
        if (a <= 0) return;
        int vigColor = a << 24 | 0x390000 | 0xFF00 | 0x14;
        guiGraphics.fill(0, 0, width, 4, vigColor);
        guiGraphics.fill(0, height - 4, width, height, vigColor);
        guiGraphics.fill(0, 0, 4, height, vigColor);
        guiGraphics.fill(width - 4, 0, width, height, vigColor);
    };

    public static void triggerFlash(float intensity, long durationMs) {
        flashStartTime = System.currentTimeMillis();
        flashDurationMs = durationMs > 0L ? durationMs : 1200L;
        flashIntensity = Mth.clamp((float)intensity, (float)0.0f, (float)1.0f);
    }

    public static void triggerGlitch(float intensity, long durationMs) {
        glitchStartTime = System.currentTimeMillis();
        glitchDurationMs = durationMs > 0L ? durationMs : 1500L;
        glitchIntensity = Mth.clamp((float)intensity, (float)0.0f, (float)1.0f);
    }

    public static void triggerShake(float addedTrauma, int durationTicks) {
    }

    public static void triggerErasureSequence() {
        OmegaScreenDistortionOverlay.triggerFlash(1.0f, 1600L);
    }

    public static void clientTick() {
        trauma = 0.0f;
        traumaTicksRemaining = 0;
    }

    public static float getTrauma() {
        return 0.0f;
    }

    public static float getShakeAngle(float seed, float scale) {
        return 0.0f;
    }
}

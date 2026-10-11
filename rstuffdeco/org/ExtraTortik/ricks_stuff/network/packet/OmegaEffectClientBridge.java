/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Mth
 *  org.ExtraTortik.ricks_stuff.client.overlay.OmegaScreenDistortionOverlay
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.util.Mth;
import org.ExtraTortik.ricks_stuff.client.overlay.OmegaScreenDistortionOverlay;

public class OmegaEffectClientBridge {
    public static float flashIntensity = 0.0f;
    public static int flashRemainingTicks = 0;
    public static int flashTotalTicks = 0;
    public static float shakeTrauma = 0.0f;
    public static int shakeRemainingTicks = 0;
    public static int shakeTotalTicks = 0;

    public static void triggerClientEffect(int effectType, double ox, double oy, double oz, float fIntensity, int fDuration, float sIntensity, int sDuration) {
        if (effectType == 0 || effectType == 2) {
            flashIntensity = Math.max(flashIntensity, fIntensity);
            flashRemainingTicks = Math.max(flashRemainingTicks, fDuration);
            flashTotalTicks = Math.max(flashTotalTicks, fDuration);
        }
        if (effectType == 1 || effectType == 2) {
            shakeTrauma = Math.max(shakeTrauma, sIntensity);
            shakeRemainingTicks = Math.max(shakeRemainingTicks, sDuration);
            shakeTotalTicks = Math.max(shakeTotalTicks, sDuration);
        }
        if (effectType == 2) {
            OmegaScreenDistortionOverlay.triggerErasureSequence();
        } else {
            if (effectType == 0) {
                OmegaScreenDistortionOverlay.triggerFlash((float)fIntensity, (long)((long)fDuration * 50L));
            }
            if (effectType != 1) return;
            OmegaScreenDistortionOverlay.triggerShake((float)sIntensity, (int)sDuration);
            OmegaScreenDistortionOverlay.triggerGlitch((float)sIntensity, (long)((long)sDuration * 50L));
        }
    }

    public static void clientTick() {
        if (flashRemainingTicks > 0 && --flashRemainingTicks <= 0) {
            flashIntensity = 0.0f;
            flashTotalTicks = 0;
        }
        if (shakeRemainingTicks <= 0) return;
        if (--shakeRemainingTicks > 0) return;
        shakeTrauma = 0.0f;
        shakeTotalTicks = 0;
    }

    public static float getFlashAlpha(float partialTicks) {
        if (flashTotalTicks <= 0) return 0.0f;
        if (flashRemainingTicks <= 0) {
            return 0.0f;
        }
        float progress = ((float)flashRemainingTicks - partialTicks) / (float)flashTotalTicks;
        return Mth.clamp((float)(progress * flashIntensity), (float)0.0f, (float)1.0f);
    }

    public static float getShakeIntensity(float partialTicks) {
        if (shakeTotalTicks <= 0) return 0.0f;
        if (shakeRemainingTicks <= 0) {
            return 0.0f;
        }
        float progress = ((float)shakeRemainingTicks - partialTicks) / (float)shakeTotalTicks;
        return Mth.clamp((float)(progress * shakeTrauma), (float)0.0f, (float)1.0f);
    }
}

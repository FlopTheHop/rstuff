/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.LayeredDraw$Layer
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.ExtraTortik.ricks_stuff.client.BioNeuralImplantManager
 *  org.ExtraTortik.ricks_stuff.client.BioNeuralImplantManager$State
 *  org.ExtraTortik.ricks_stuff.client.gui.RadialRenderUtils
 *  org.joml.Matrix4f
 */
package org.ExtraTortik.ricks_stuff.client.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.ExtraTortik.ricks_stuff.client.BioNeuralImplantManager;
import org.ExtraTortik.ricks_stuff.client.gui.RadialRenderUtils;
import org.joml.Matrix4f;

@OnlyIn(value=Dist.CLIENT)
public class BioNeuralImplantOverlay {
    private static final float R_INNER = 7.5f;
    private static final float R_OUTER = 14.0f;
    public static final LayeredDraw.Layer HUD_OVERLAY = (guiGraphics, deltaTracker) -> {
        BioNeuralImplantManager.State state = BioNeuralImplantManager.getState();
        if (state == BioNeuralImplantManager.State.IDLE) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui) {
            return;
        }
        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();
        float cx = (float)width / 2.0f;
        float cy = (float)height / 2.0f;
        Matrix4f matrix = guiGraphics.pose().last().pose();
        if (state == BioNeuralImplantManager.State.FILLING) {
            float progress = BioNeuralImplantManager.getProgress();
            RadialRenderUtils.drawRing((Matrix4f)matrix, (float)cx, (float)cy, (float)7.5f, (float)14.0f, (int)1712328728, (int)32);
            RadialRenderUtils.drawRingOutline((Matrix4f)matrix, (float)cx, (float)cy, (float)7.5f, (float)14.0f, (int)-2011159243, (float)1.0f, (int)32);
            if (!(progress > 0.01f)) return;
            float startAngle = -1.5707964f;
            float endAngle = (float)(-1.5707963267948966 + Math.PI * 2 * (double)Math.min(1.0f, progress));
            int blueColor = -16718337;
            RadialRenderUtils.drawSector((Matrix4f)matrix, (float)cx, (float)cy, (float)7.5f, (float)14.0f, (float)startAngle, (float)endAngle, (int)blueColor, (int)32);
            RadialRenderUtils.drawSectorOutline((Matrix4f)matrix, (float)cx, (float)cy, (float)7.5f, (float)14.0f, (float)startAngle, (float)endAngle, (int)-8326913, (float)1.2f, (int)32);
        } else {
            if (state != BioNeuralImplantManager.State.FAILED) return;
            float failAlpha = BioNeuralImplantManager.getFailAlpha();
            if (!(failAlpha > 0.01f)) return;
            int a = Math.max(0, Math.min(255, (int)(failAlpha * 240.0f)));
            int redFill = a << 24 | 0xFF1A00;
            int redOutline = a << 24 | 0xFF5500;
            RadialRenderUtils.drawRing((Matrix4f)matrix, (float)cx, (float)cy, (float)7.5f, (float)14.0f, (int)redFill, (int)32);
            RadialRenderUtils.drawRingOutline((Matrix4f)matrix, (float)cx, (float)cy, (float)7.5f, (float)14.0f, (int)redOutline, (float)1.2f, (int)32);
        }
    };
}

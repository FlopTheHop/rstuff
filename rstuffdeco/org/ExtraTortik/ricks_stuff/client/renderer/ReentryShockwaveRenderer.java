/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.util.Mth
 *  org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity
 *  org.joml.Matrix4f
 */
package org.ExtraTortik.ricks_stuff.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;
import org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity;
import org.joml.Matrix4f;

public class ReentryShockwaveRenderer {
    private static final int RINGS = 16;
    private static final int SECTORS = 24;
    private static final float APEX_Z = 3.85f;
    private static final float LENGTH = 6.4f;
    private static final float RADIUS_X = 4.85f;
    private static final float RADIUS_Y = 2.05f;

    public static void renderShockwave(SpaceCruiserEntity ship, PoseStack poseStack, VertexConsumer consumer, float partialTicks, float intensity) {
        if (intensity <= 0.02f) {
            return;
        }
        poseStack.pushPose();
        Matrix4f pose = poseStack.last().pose();
        float time = ((float)ship.tickCount + partialTicks) * 0.45f;
        int r = 0;
        while (true) {
            if (r >= 16) {
                poseStack.popPose();
                return;
            }
            float u0 = (float)r / 16.0f;
            float u1 = (float)(r + 1) / 16.0f;
            float z0 = 3.85f - (float)Math.pow(u0, 1.32) * 6.4f;
            float z1 = 3.85f - (float)Math.pow(u1, 1.32) * 6.4f;
            float rx0 = 4.85f * (float)Math.sqrt(u0);
            float ry0 = 2.05f * (float)Math.sqrt(u0);
            float rx1 = 4.85f * (float)Math.sqrt(u1);
            float ry1 = 2.05f * (float)Math.sqrt(u1);
            float[] c0 = ReentryShockwaveRenderer.getPlasmaColor(u0, intensity);
            float[] c1 = ReentryShockwaveRenderer.getPlasmaColor(u1, intensity);
            for (int s = 0; s < 24; ++s) {
                float a0 = (float)((double)(s * 2) * Math.PI / 24.0);
                float a1 = (float)((double)((s + 1) * 2) * Math.PI / 24.0);
                float j00 = 1.0f + 0.035f * (float)Math.sin(6.0f * a0 + time * 3.0f);
                float j01 = 1.0f + 0.035f * (float)Math.sin(6.0f * a1 + time * 3.0f);
                float j10 = 1.0f + 0.035f * (float)Math.sin(6.0f * a0 + time * 3.0f);
                float j11 = 1.0f + 0.035f * (float)Math.sin(6.0f * a1 + time * 3.0f);
                float x00 = (float)Math.cos(a0) * rx0 * j00;
                float y00 = (float)Math.sin(a0) * ry0 * j00 + 0.55f;
                float x01 = (float)Math.cos(a1) * rx0 * j01;
                float y01 = (float)Math.sin(a1) * ry0 * j01 + 0.55f;
                float x10 = (float)Math.cos(a0) * rx1 * j10;
                float y10 = (float)Math.sin(a0) * ry1 * j10 + 0.55f;
                float x11 = (float)Math.cos(a1) * rx1 * j11;
                float y11 = (float)Math.sin(a1) * ry1 * j11 + 0.55f;
                consumer.addVertex(pose, x00, y00, z0).setColor(c0[0], c0[1], c0[2], c0[3]);
                consumer.addVertex(pose, x01, y01, z0).setColor(c0[0], c0[1], c0[2], c0[3]);
                consumer.addVertex(pose, x11, y11, z1).setColor(c1[0], c1[1], c1[2], c1[3]);
                consumer.addVertex(pose, x10, y10, z1).setColor(c1[0], c1[1], c1[2], c1[3]);
            }
            ++r;
        }
    }

    private static float[] getPlasmaColor(float u, float intensity) {
        float a;
        float b;
        float g;
        float r;
        if (u < 0.22f) {
            r = 1.0f;
            g = 0.96f - u * 0.45f;
            b = 0.75f - u * 2.2f;
            a = (0.92f - u * 0.8f) * intensity;
        } else if (u < 0.68f) {
            float f = (u - 0.22f) / 0.46f;
            r = 1.0f;
            g = Mth.lerp((float)f, (float)0.86f, (float)0.38f);
            b = Mth.lerp((float)f, (float)0.22f, (float)0.02f);
            a = (0.75f - f * 0.32f) * intensity;
        } else {
            float f = (u - 0.68f) / 0.32f;
            r = Mth.lerp((float)f, (float)1.0f, (float)0.45f);
            g = Mth.lerp((float)f, (float)0.38f, (float)0.05f);
            b = 0.0f;
            a = 0.42f * (1.0f - f) * intensity;
        }
        return new float[]{r, g, b, a};
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.util.Mth
 *  org.ExtraTortik.ricks_stuff.client.renderer.PortalRenderTypes
 *  org.ExtraTortik.ricks_stuff.client.renderer.WarpDebrisRenderer
 *  org.ExtraTortik.ricks_stuff.client.renderer.WarpTunnelRenderer$TunnelRenderTypes
 *  org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity
 *  org.ExtraTortik.ricks_stuff.entity.WarpState
 *  org.joml.Matrix4f
 */
package org.ExtraTortik.ricks_stuff.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import org.ExtraTortik.ricks_stuff.client.renderer.PortalRenderTypes;
import org.ExtraTortik.ricks_stuff.client.renderer.WarpDebrisRenderer;
import org.ExtraTortik.ricks_stuff.client.renderer.WarpTunnelRenderer;
import org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity;
import org.ExtraTortik.ricks_stuff.entity.WarpState;
import org.joml.Matrix4f;

public class WarpTunnelRenderer {
    private static final float OPAQUE_SHELL_RADIUS = 75.0f;
    private static final float OPAQUE_SHELL_LENGTH = 350.0f;
    private static final int OPAQUE_SHELL_SECTORS = 48;
    private static final int OPAQUE_SHELL_LONGITUDINAL = 24;
    private static final float OPAQUE_SHELL_START_Z = -50.0f;
    private static final float MIDDLE_RADIUS = 60.0f;
    private static final float MIDDLE_LENGTH = 320.0f;
    private static final int MIDDLE_SECTORS = 40;
    private static final int MIDDLE_LONGITUDINAL = 24;
    private static final float MIDDLE_START_Z = -40.0f;
    private static final float INNER_RADIUS = 48.0f;
    private static final float INNER_LENGTH = 300.0f;
    private static final int INNER_SECTORS = 36;
    private static final int INNER_LONGITUDINAL = 24;
    private static final float INNER_START_Z = -30.0f;
    private static final float[][] COLOR_PALETTE = new float[][]{{0.0f, 1.0f, 0.933f}, {1.0f, 0.0f, 0.667f}, {0.224f, 1.0f, 0.078f}, {1.0f, 1.0f, 0.0f}, {0.478f, 0.0f, 1.0f}};

    public static void renderWarpTunnel(PoseStack poseStack, MultiBufferSource buffer, SpaceCruiserEntity ship, float partialTicks) {
        WarpState warpState = ship.getWarpStateEnum();
        if (!warpState.isWarping() && warpState != WarpState.TUNNEL_TRANSIT) {
            return;
        }
        int warpTimer = ship.getWarpTimer();
        float currentTicks = (float)warpTimer + partialTicks;
        float alpha = 0.0f;
        if (warpState == WarpState.TUNNEL_TRANSIT) {
            int duration = WarpState.TUNNEL_TRANSIT.getDurationTicks();
            float fadeIn = Math.min(1.0f, currentTicks / 8.0f);
            float fadeOut = Math.min(1.0f, ((float)duration - currentTicks) / 12.0f);
            alpha = Math.max(0.0f, Math.min(fadeIn, fadeOut));
        } else if (warpState == WarpState.ENTERING) {
            if (currentTicks >= 12.0f) {
                alpha = (currentTicks - 12.0f) / 8.0f;
            }
        } else if (warpState == WarpState.EXITING && currentTicks <= 15.0f) {
            alpha = 1.0f - currentTicks / 15.0f;
        }
        alpha = Mth.clamp((float)alpha, (float)0.0f, (float)1.0f);
        if (alpha <= 0.005f) {
            return;
        }
        float totalTime = (float)ship.tickCount + partialTicks;
        poseStack.pushPose();
        VertexConsumer opaqueConsumer = buffer.getBuffer(PortalRenderTypes.WARP_TUNNEL_OPAQUE);
        WarpTunnelRenderer.renderOpaqueShell(poseStack, opaqueConsumer, totalTime, 75.0f, -50.0f, 350.0f, 48, 24);
        VertexConsumer wallConsumer = buffer.getBuffer(TunnelRenderTypes.TUNNEL_WALLS);
        WarpTunnelRenderer.renderTunnelCylinder(poseStack, wallConsumer, totalTime, alpha * 0.75f, 60.0f, -40.0f, 320.0f, 40, 24, -totalTime * 22.0f, 2.8f, 1.8f, 1.1f, false);
        WarpTunnelRenderer.renderTunnelCylinder(poseStack, wallConsumer, totalTime, alpha * 0.92f, 48.0f, -30.0f, 300.0f, 36, 24, totalTime * 34.0f, 4.2f, 2.4f, 1.6f, true);
        WarpDebrisRenderer.renderDebris((PoseStack)poseStack, (MultiBufferSource)buffer, (SpaceCruiserEntity)ship, (float)partialTicks, (float)alpha);
        VertexConsumer speedLineConsumer = buffer.getBuffer(TunnelRenderTypes.TUNNEL_SPEEDLINES);
        WarpTunnelRenderer.renderSpeedLines(poseStack, speedLineConsumer, totalTime, alpha);
        poseStack.popPose();
    }

    private static void renderOpaqueShell(PoseStack poseStack, VertexConsumer consumer, float time, float radius, float startZ, float length, int sectors, int longitudinal) {
        poseStack.pushPose();
        Matrix4f matrix = poseStack.last().pose();
        float baseR = 0.007843f;
        float baseG = 0.0f;
        float baseB = 0.039216f;
        float alpha = 1.0f;
        for (int l = 0; l < longitudinal; ++l) {
            float v0 = (float)l / (float)longitudinal;
            float v1 = (float)(l + 1) / (float)longitudinal;
            float z0 = startZ + v0 * length;
            float z1 = startZ + v1 * length;
            for (int s = 0; s < sectors; ++s) {
                float u0 = (float)s / (float)sectors;
                float u1 = (float)(s + 1) / (float)sectors;
                float theta0 = (float)((double)u0 * Math.PI * 2.0);
                float theta1 = (float)((double)u1 * Math.PI * 2.0);
                float cosT0 = (float)Math.cos(theta0);
                float sinT0 = (float)Math.sin(theta0);
                float cosT1 = (float)Math.cos(theta1);
                float sinT1 = (float)Math.sin(theta1);
                float tint0 = 1.0f + 0.12f * (float)Math.sin((double)u0 * 8.0 + (double)v0 * 4.0 + (double)(time * 0.04f));
                float tint1 = 1.0f + 0.12f * (float)Math.sin((double)u1 * 8.0 + (double)v1 * 4.0 + (double)(time * 0.04f));
                float r0 = baseR * tint0;
                float b0 = baseB * tint0;
                float r1 = baseR * tint1;
                float b1 = baseB * tint1;
                consumer.addVertex(matrix, cosT0 * radius, sinT0 * radius, z0).setColor(r0, baseG, b0, alpha);
                consumer.addVertex(matrix, cosT1 * radius, sinT1 * radius, z0).setColor(r1, baseG, b1, alpha);
                consumer.addVertex(matrix, cosT1 * radius, sinT1 * radius, z1).setColor(r1, baseG, b1, alpha);
                consumer.addVertex(matrix, cosT0 * radius, sinT0 * radius, z1).setColor(r0, baseG, b0, alpha);
            }
        }
        float zBack = startZ;
        for (int s = 0; s < sectors; ++s) {
            float u0 = (float)s / (float)sectors;
            float u1 = (float)(s + 1) / (float)sectors;
            float theta0 = (float)((double)u0 * Math.PI * 2.0);
            float theta1 = (float)((double)u1 * Math.PI * 2.0);
            float cosT0 = (float)Math.cos(theta0);
            float sinT0 = (float)Math.sin(theta0);
            float cosT1 = (float)Math.cos(theta1);
            float sinT1 = (float)Math.sin(theta1);
            consumer.addVertex(matrix, 0.0f, 0.0f, zBack).setColor(baseR, baseG, baseB, alpha);
            consumer.addVertex(matrix, cosT0 * radius, sinT0 * radius, zBack).setColor(baseR, baseG, baseB, alpha);
            consumer.addVertex(matrix, cosT1 * radius, sinT1 * radius, zBack).setColor(baseR, baseG, baseB, alpha);
            consumer.addVertex(matrix, 0.0f, 0.0f, zBack).setColor(baseR, baseG, baseB, alpha);
        }
        float zFront = startZ + length;
        int s = 0;
        while (true) {
            if (s >= sectors) {
                poseStack.popPose();
                return;
            }
            float u0 = (float)s / (float)sectors;
            float u1 = (float)(s + 1) / (float)sectors;
            float theta0 = (float)((double)u0 * Math.PI * 2.0);
            float theta1 = (float)((double)u1 * Math.PI * 2.0);
            float cosT0 = (float)Math.cos(theta0);
            float sinT0 = (float)Math.sin(theta0);
            float cosT1 = (float)Math.cos(theta1);
            float sinT1 = (float)Math.sin(theta1);
            consumer.addVertex(matrix, 0.0f, 0.0f, zFront).setColor(baseR, baseG, baseB, alpha);
            consumer.addVertex(matrix, cosT1 * radius, sinT1 * radius, zFront).setColor(baseR, baseG, baseB, alpha);
            consumer.addVertex(matrix, cosT0 * radius, sinT0 * radius, zFront).setColor(baseR, baseG, baseB, alpha);
            consumer.addVertex(matrix, 0.0f, 0.0f, zFront).setColor(baseR, baseG, baseB, alpha);
            ++s;
        }
    }

    private static void renderTunnelCylinder(PoseStack poseStack, VertexConsumer consumer, float time, float alpha, float baseRadius, float startZ, float length, int sectors, int longitudinal, float rotationDegrees, float spiralTurns, float waveFrequency, float velocity, boolean isInner) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotationDegrees));
        Matrix4f matrix = poseStack.last().pose();
        int l = 0;
        while (true) {
            if (l >= longitudinal) {
                poseStack.popPose();
                return;
            }
            float v0 = (float)l / (float)longitudinal;
            float v1 = (float)(l + 1) / (float)longitudinal;
            float z0 = startZ + v0 * length;
            float z1 = startZ + v1 * length;
            float rad0 = baseRadius * (1.0f + 0.02f * (float)Math.sin((double)(v0 * waveFrequency) * Math.PI * 2.0 + (double)(time * 0.4f)));
            float rad1 = baseRadius * (1.0f + 0.02f * (float)Math.sin((double)(v1 * waveFrequency) * Math.PI * 2.0 + (double)(time * 0.4f)));
            float edgeFade0 = Math.min(1.0f, Math.min(v0 * 4.0f, (1.0f - v0) * 4.0f));
            float edgeFade1 = Math.min(1.0f, Math.min(v1 * 4.0f, (1.0f - v1) * 4.0f));
            for (int s = 0; s < sectors; ++s) {
                float u0 = (float)s / (float)sectors;
                float u1 = (float)(s + 1) / (float)sectors;
                float theta0 = (float)((double)u0 * Math.PI * 2.0);
                float theta1 = (float)((double)u1 * Math.PI * 2.0);
                float cosT0 = (float)Math.cos(theta0);
                float sinT0 = (float)Math.sin(theta0);
                float cosT1 = (float)Math.cos(theta1);
                float sinT1 = (float)Math.sin(theta1);
                float phase00 = u0 * spiralTurns + v0 * waveFrequency - time * 0.16f * velocity;
                float phase01 = u1 * spiralTurns + v0 * waveFrequency - time * 0.16f * velocity;
                float phase10 = u0 * spiralTurns + v1 * waveFrequency - time * 0.16f * velocity;
                float phase11 = u1 * spiralTurns + v1 * waveFrequency - time * 0.16f * velocity;
                float[] c00 = WarpTunnelRenderer.getPaletteColor(phase00, alpha * edgeFade0);
                float[] c01 = WarpTunnelRenderer.getPaletteColor(phase01, alpha * edgeFade0);
                float[] c10 = WarpTunnelRenderer.getPaletteColor(phase10, alpha * edgeFade1);
                float[] c11 = WarpTunnelRenderer.getPaletteColor(phase11, alpha * edgeFade1);
                consumer.addVertex(matrix, cosT0 * rad0, sinT0 * rad0, z0).setColor(c00[0], c00[1], c00[2], c00[3]);
                consumer.addVertex(matrix, cosT1 * rad0, sinT1 * rad0, z0).setColor(c01[0], c01[1], c01[2], c01[3]);
                consumer.addVertex(matrix, cosT1 * rad1, sinT1 * rad1, z1).setColor(c11[0], c11[1], c11[2], c11[3]);
                consumer.addVertex(matrix, cosT0 * rad1, sinT0 * rad1, z1).setColor(c10[0], c10[1], c10[2], c10[3]);
            }
            ++l;
        }
    }

    private static void renderSpeedLines(PoseStack poseStack, VertexConsumer consumer, float time, float alpha) {
        Matrix4f matrix = poseStack.last().pose();
        int numLines = 80;
        float speed = 7.5f;
        int i = 0;
        while (i < numLines) {
            float baseAngle = (float)((double)i * 2.0 * Math.PI / (double)numLines) + (i % 3 == 0 ? 0.12f : -0.09f);
            float radFrac = (float)(i % 9) / 8.0f;
            float radius = 14.0f + radFrac * 33.0f + (i % 2 == 0 ? 1.0f : -1.0f) * 1.5f;
            float streakCycle = 22.0f + (float)(i % 5) * 3.0f;
            float streakTime = time * speed + (float)i * 14.3f;
            float progress = streakTime % streakCycle / streakCycle;
            if (progress < 0.0f) {
                progress += 1.0f;
            }
            float streakLength = 24.0f + (float)(i % 6) * 8.0f;
            float zHead = 270.0f - progress * 305.0f;
            float zTail = zHead + streakLength;
            float width = 0.45f + (float)(i % 4) * 0.25f;
            float cosA = (float)Math.cos(baseAngle);
            float sinA = (float)Math.sin(baseAngle);
            float tanX = -sinA * width;
            float tanY = cosA * width;
            float[] color = COLOR_PALETTE[i % COLOR_PALETTE.length];
            float intensity = (0.8f + 0.2f * (float)Math.sin(time * 4.0f + (float)i)) * alpha;
            float r = color[0];
            float g = color[1];
            float b = color[2];
            consumer.addVertex(matrix, cosA * radius - tanX, sinA * radius - tanY, zHead).setColor(1.0f, 1.0f, 1.0f, intensity * 0.98f);
            consumer.addVertex(matrix, cosA * radius + tanX, sinA * radius + tanY, zHead).setColor(1.0f, 1.0f, 1.0f, intensity * 0.98f);
            consumer.addVertex(matrix, cosA * radius + tanX, sinA * radius + tanY, zTail).setColor(r, g, b, 0.0f);
            consumer.addVertex(matrix, cosA * radius - tanX, sinA * radius - tanY, zTail).setColor(r, g, b, 0.0f);
            ++i;
        }
    }

    private static float[] getPaletteColor(float phase, float alpha) {
        float p = phase % 1.0f;
        if (p < 0.0f) {
            p += 1.0f;
        }
        int numColors = COLOR_PALETTE.length;
        float scaled = p * (float)numColors;
        int idx0 = (int)Math.floor(scaled) % numColors;
        int idx1 = (idx0 + 1) % numColors;
        float frac = scaled - (float)Math.floor(scaled);
        float smoothFrac = 0.5f - 0.5f * (float)Math.cos((double)frac * Math.PI);
        float[] c0 = COLOR_PALETTE[idx0];
        float[] c1 = COLOR_PALETTE[idx1];
        float r = Mth.lerp((float)smoothFrac, (float)c0[0], (float)c1[0]);
        float g = Mth.lerp((float)smoothFrac, (float)c0[1], (float)c1[1]);
        float b = Mth.lerp((float)smoothFrac, (float)c0[2], (float)c1[2]);
        return new float[]{r, g, b, alpha};
    }
}

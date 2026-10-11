/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.util.Mth
 *  net.minecraft.world.phys.Vec3
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity
 *  org.joml.Matrix4f
 */
package org.ExtraTortik.ricks_stuff.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.Locale;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity;
import org.joml.Matrix4f;

public class ThrusterFlameRenderer {
    private static final int CROSS_PLANES = 6;
    private static final int LENGTH_SEGMENTS = 8;
    private static final float NOZZLE_RADIUS = 0.42f;

    public static void renderThrusterJets(SpaceCruiserEntity ship, PoseStack poseStack, VertexConsumer consumer, float partialTicks) {
        if (!ship.isEngineOn()) {
            return;
        }
        float flameIntensity = ship.getInterpolatedFlameIntensity(partialTicks);
        float boostIntensity = ship.getInterpolatedBoostIntensity(partialTicks);
        float reverseFlame = ship.getInterpolatedReverseFlame(partialTicks);
        float steerDiff = ship.getInterpolatedSteerDiff(partialTicks);
        if (flameIntensity <= 0.01f && reverseFlame <= 0.01f) {
            return;
        }
        ThrusterFlameRenderer.renderSingleJet(ship, poseStack, consumer, partialTicks, SpaceCruiserEntity.THRUSTER_LEFT, false, flameIntensity, boostIntensity, reverseFlame, steerDiff);
        ThrusterFlameRenderer.renderSingleJet(ship, poseStack, consumer, partialTicks, SpaceCruiserEntity.THRUSTER_RIGHT, true, flameIntensity, boostIntensity, reverseFlame, steerDiff);
    }

    private static void renderSingleJet(SpaceCruiserEntity ship, PoseStack poseStack, VertexConsumer consumer, float partialTicks, Vec3 origin, boolean right, float flameIntensity, float boostIntensity, float reverseFlame, float steerDiff) {
        float thrust = flameIntensity + (right ? -1.0f : 1.0f) * steerDiff;
        thrust = Math.max(0.04f, thrust);
        poseStack.pushPose();
        poseStack.translate(origin.x, origin.y, origin.z);
        if (Math.abs(steerDiff) > 0.02f) {
            float deflYaw = Mth.clamp((float)(-steerDiff * 14.0f), (float)-7.0f, (float)7.0f);
            poseStack.mulPose(Axis.YP.rotationDegrees(deflYaw));
        }
        Matrix4f pose = poseStack.last().pose();
        float time = (float)ship.tickCount + partialTicks;
        int seed = ship.getId() * 31 + (right ? 17 : 3);
        if (reverseFlame > 0.04f) {
            ThrusterFlameRenderer.renderReverseThrust(pose, consumer, time, reverseFlame, seed);
        }
        if (flameIntensity > 0.02f) {
            String quality;
            float flameLength;
            if (thrust <= 0.12f) {
                flameLength = 0.14f + thrust * 0.6f;
            } else {
                float activeProgress = thrust - 0.12f;
                flameLength = 0.21f + activeProgress * 1.6f;
                if (thrust > 0.8f) {
                    flameLength += (float)Math.sin(time * 0.8f) * 0.15f * Math.min(1.5f, thrust - 0.8f);
                }
            }
            ThrusterFlameRenderer.renderNozzleCorona(pose, consumer, time, thrust);
            ThrusterFlameRenderer.renderPlasmaCore(pose, consumer, time, flameLength, thrust);
            ThrusterFlameRenderer.renderCrossPlanes(pose, consumer, time, flameLength, thrust, seed);
            float diamondAlpha = Mth.clamp((float)((thrust - 0.7f) / 0.55f), (float)0.0f, (float)1.0f);
            if (diamondAlpha > 0.01f) {
                ThrusterFlameRenderer.renderShockDiamonds(pose, consumer, time, flameLength, diamondAlpha);
            }
            String string = quality = Config.thrusterEffectsQuality != null ? Config.thrusterEffectsQuality.toUpperCase(Locale.ROOT) : "HIGH";
            if (!"LOW".equals(quality) && boostIntensity > 0.02f) {
                ThrusterFlameRenderer.renderElectricArcs(pose, consumer, time, flameLength, thrust, seed, boostIntensity);
            }
        }
        poseStack.popPose();
    }

    private static void renderNozzleCorona(Matrix4f pose, VertexConsumer consumer, float time, float thrust) {
        int discSegments = 12;
        float pulse = 1.0f + 0.06f * (float)Math.sin(time * 0.6f);
        float radiusFactor = thrust <= 0.12f ? 0.75f : 0.75f + Math.min(1.0f, (thrust - 0.12f) / 0.8f) * 0.4f;
        float radius = 0.42f * radiusFactor * pulse;
        float alphaCenter = Mth.clamp((float)(0.25f + thrust * 0.65f), (float)0.0f, (float)0.95f);
        float centerR = 1.0f;
        float centerG = 1.0f;
        float centerB = 1.0f;
        float edgeR = 0.0f;
        float edgeG = 0.85f;
        float edgeB = 1.0f;
        int i = 0;
        while (i < discSegments) {
            float a1 = (float)((double)(i * 2) * Math.PI / (double)discSegments);
            float a2 = (float)((double)((i + 1) * 2) * Math.PI / (double)discSegments);
            float x1 = (float)Math.cos(a1) * radius;
            float y1 = (float)Math.sin(a1) * radius;
            float x2 = (float)Math.cos(a2) * radius;
            float y2 = (float)Math.sin(a2) * radius;
            consumer.addVertex(pose, 0.0f, 0.0f, -0.01f).setColor(centerR, centerG, centerB, alphaCenter);
            consumer.addVertex(pose, x1, y1, -0.01f).setColor(edgeR, edgeG, edgeB, 0.0f);
            consumer.addVertex(pose, x2, y2, -0.01f).setColor(edgeR, edgeG, edgeB, 0.0f);
            consumer.addVertex(pose, 0.0f, 0.0f, -0.01f).setColor(centerR, centerG, centerB, alphaCenter);
            ++i;
        }
    }

    private static void renderPlasmaCore(Matrix4f pose, VertexConsumer consumer, float time, float flameLength, float thrust) {
        int coreSlices = 8;
        int coreSegs = 3;
        float coreLength = flameLength * (0.3f + 0.04f * (float)Math.sin(time * 0.9f));
        float baseCoreRadius = 0.42f * (0.35f + Math.min(1.2f, thrust) * 0.4f);
        float alphaMultiplier = Mth.clamp((float)(0.4f + thrust * 0.55f), (float)0.0f, (float)1.0f);
        int s = 0;
        while (s < coreSegs) {
            float u0 = (float)s / (float)coreSegs;
            float u1 = (float)(s + 1) / (float)coreSegs;
            float z0 = -u0 * coreLength;
            float z1 = -u1 * coreLength;
            float r0 = baseCoreRadius * (1.0f - u0 * 0.85f);
            float r1 = baseCoreRadius * (1.0f - u1 * 0.85f);
            float a0 = Mth.clamp((float)((1.0f - u0 * 0.8f) * alphaMultiplier), (float)0.0f, (float)1.0f);
            float a1 = Mth.clamp((float)((1.0f - u1 * 0.8f) * alphaMultiplier), (float)0.0f, (float)1.0f);
            float rCol0 = Mth.lerp((float)u0, (float)1.0f, (float)0.5f);
            float gCol0 = Mth.lerp((float)u0, (float)1.0f, (float)0.95f);
            float bCol0 = 1.0f;
            float rCol1 = Mth.lerp((float)u1, (float)1.0f, (float)0.5f);
            float gCol1 = Mth.lerp((float)u1, (float)1.0f, (float)0.95f);
            float bCol1 = 1.0f;
            for (int i = 0; i < coreSlices; ++i) {
                float aStart = (float)((double)(i * 2) * Math.PI / (double)coreSlices);
                float aEnd = (float)((double)((i + 1) * 2) * Math.PI / (double)coreSlices);
                float cosA = (float)Math.cos(aStart);
                float sinA = (float)Math.sin(aStart);
                float cosB = (float)Math.cos(aEnd);
                float sinB = (float)Math.sin(aEnd);
                consumer.addVertex(pose, cosA * r0, sinA * r0, z0).setColor(rCol0, gCol0, bCol0, a0);
                consumer.addVertex(pose, cosB * r0, sinB * r0, z0).setColor(rCol0, gCol0, bCol0, a0);
                consumer.addVertex(pose, cosB * r1, sinB * r1, z1).setColor(rCol1, gCol1, bCol1, a1);
                consumer.addVertex(pose, cosA * r1, sinA * r1, z1).setColor(rCol1, gCol1, bCol1, a1);
            }
            ++s;
        }
    }

    private static void renderCrossPlanes(Matrix4f pose, VertexConsumer consumer, float time, float flameLength, float thrust, int seed) {
        String quality;
        float baseWidth = 0.42f * (0.35f + Math.min(1.5f, thrust) * 0.55f);
        String string = quality = Config.thrusterEffectsQuality != null ? Config.thrusterEffectsQuality.toUpperCase(Locale.ROOT) : "HIGH";
        int planes = "LOW".equals(quality) ? 2 : ("MEDIUM".equals(quality) ? 3 : 6);
        int p = 0;
        while (p < planes) {
            float planeAngle = (float)((double)p * Math.PI / (double)planes);
            float cos = (float)Math.cos(planeAngle);
            float sin = (float)Math.sin(planeAngle);
            float planePhase = (float)p * 1.15f + (float)seed * 0.3f;
            for (int s = 0; s < 8; ++s) {
                float u0 = (float)s / 8.0f;
                float u1 = (float)(s + 1) / 8.0f;
                float z0 = -u0 * flameLength;
                float z1 = -u1 * flameLength;
                float w0 = ThrusterFlameRenderer.getFlameWidth(u0, baseWidth, time, planePhase, thrust);
                float w1 = ThrusterFlameRenderer.getFlameWidth(u1, baseWidth, time, planePhase, thrust);
                float r0 = ThrusterFlameRenderer.getFlameRed(u0);
                float g0 = ThrusterFlameRenderer.getFlameGreen(u0);
                float b0 = ThrusterFlameRenderer.getFlameBlue(u0);
                float a0 = ThrusterFlameRenderer.getFlameAlpha(u0, thrust);
                float r1 = ThrusterFlameRenderer.getFlameRed(u1);
                float g1 = ThrusterFlameRenderer.getFlameGreen(u1);
                float b1 = ThrusterFlameRenderer.getFlameBlue(u1);
                float a1 = ThrusterFlameRenderer.getFlameAlpha(u1, thrust);
                consumer.addVertex(pose, -cos * w0, -sin * w0, z0).setColor(r0, g0, b0, a0);
                consumer.addVertex(pose, cos * w0, sin * w0, z0).setColor(r0, g0, b0, a0);
                consumer.addVertex(pose, cos * w1, sin * w1, z1).setColor(r1, g1, b1, a1);
                consumer.addVertex(pose, -cos * w1, -sin * w1, z1).setColor(r1, g1, b1, a1);
            }
            ++p;
        }
    }

    private static float getFlameWidth(float u, float baseWidth, float time, float phase, float thrust) {
        float envelope = (1.0f - u) * (1.0f + 0.38f * (float)Math.sin((double)u * Math.PI));
        float waveScale = thrust <= 0.12f ? 0.06f : 0.12f + Math.min(1.0f, thrust) * 0.15f;
        float wave = (float)Math.sin(time * 0.95f - u * 8.5f + phase) * waveScale;
        float jitterScale = thrust <= 0.12f ? 0.03f : 0.12f;
        float jitter = (float)Math.sin(time * 2.1f + u * 16.0f + phase * 2.0f) * jitterScale;
        return Math.max(0.01f, baseWidth * envelope * (1.0f + wave + jitter));
    }

    private static float getFlameRed(float u) {
        if (u < 0.2f) {
            return Mth.lerp((float)(u / 0.2f), (float)1.0f, (float)0.05f);
        }
        if (!(u < 0.6f)) return 0.01f;
        return Mth.lerp((float)((u - 0.2f) / 0.4f), (float)0.05f, (float)0.02f);
    }

    private static float getFlameGreen(float u) {
        if (u < 0.2f) {
            return Mth.lerp((float)(u / 0.2f), (float)1.0f, (float)0.92f);
        }
        if (!(u < 0.6f)) return Mth.lerp((float)((u - 0.6f) / 0.4f), (float)0.45f, (float)0.1f);
        return Mth.lerp((float)((u - 0.2f) / 0.4f), (float)0.92f, (float)0.45f);
    }

    private static float getFlameBlue(float u) {
        if (u < 0.2f) {
            return 1.0f;
        }
        if (!(u < 0.6f)) return Mth.lerp((float)((u - 0.6f) / 0.4f), (float)0.98f, (float)0.65f);
        return Mth.lerp((float)((u - 0.2f) / 0.4f), (float)1.0f, (float)0.98f);
    }

    private static float getFlameAlpha(float u, float thrust) {
        float baseAlpha = u < 0.15f ? 0.85f : (u < 0.65f ? Mth.lerp((float)((u - 0.15f) / 0.5f), (float)0.85f, (float)0.45f) : Mth.lerp((float)((u - 0.65f) / 0.35f), (float)0.45f, (float)0.0f));
        float thrustScale = thrust <= 0.12f ? 0.35f : 0.45f + Math.min(1.5f, thrust) * 0.4f;
        return Mth.clamp((float)(baseAlpha * thrustScale), (float)0.0f, (float)1.0f);
    }

    private static void renderShockDiamonds(Matrix4f pose, VertexConsumer consumer, float time, float flameLength, float alphaMultiplier) {
        int diamondCount = flameLength > 2.8f ? 3 : 2;
        float diamondSize = 0.189f;
        int d = 0;
        while (d < diamondCount) {
            float frac = (float)(d + 1) * 0.24f;
            float zCenter = -frac * flameLength + (float)Math.sin(time * 0.7f + (float)d) * 0.06f;
            float pulse = 1.0f + 0.15f * (float)Math.sin(time * 1.2f + (float)d * 2.0f);
            float dLen = diamondSize * 1.4f * pulse;
            float dWidth = diamondSize * 0.85f * pulse;
            float alpha = 0.85f * alphaMultiplier;
            consumer.addVertex(pose, 0.0f, 0.0f, zCenter + dLen).setColor(1.0f, 1.0f, 1.0f, 0.0f);
            consumer.addVertex(pose, -dWidth, 0.0f, zCenter).setColor(0.4f, 0.95f, 1.0f, alpha);
            consumer.addVertex(pose, 0.0f, 0.0f, zCenter - dLen).setColor(0.0f, 0.6f, 1.0f, 0.0f);
            consumer.addVertex(pose, dWidth, 0.0f, zCenter).setColor(0.4f, 0.95f, 1.0f, alpha);
            consumer.addVertex(pose, 0.0f, 0.0f, zCenter + dLen).setColor(1.0f, 1.0f, 1.0f, 0.0f);
            consumer.addVertex(pose, 0.0f, -dWidth, zCenter).setColor(0.4f, 0.95f, 1.0f, alpha);
            consumer.addVertex(pose, 0.0f, 0.0f, zCenter - dLen).setColor(0.0f, 0.6f, 1.0f, 0.0f);
            consumer.addVertex(pose, 0.0f, dWidth, zCenter).setColor(0.4f, 0.95f, 1.0f, alpha);
            ++d;
        }
    }

    private static void renderElectricArcs(Matrix4f pose, VertexConsumer consumer, float time, float flameLength, float thrust, int seed, float alphaMultiplier) {
        int arcCount = thrust > 1.4f ? 5 : (thrust > 0.6f ? 4 : 2);
        int tickFrame = (int)(time * 0.45f);
        int a = 0;
        while (a < arcCount) {
            int arcSeed = seed + a * 1013 + tickFrame * 9973;
            float startAngle = ThrusterFlameRenderer.hash(arcSeed, 1) * ((float)Math.PI * 2);
            float startDist = 0.42f * (0.8f + ThrusterFlameRenderer.hash(arcSeed, 2) * 0.3f);
            float startZ = -0.05f - ThrusterFlameRenderer.hash(arcSeed, 3) * 0.2f;
            float curX = (float)Math.cos(startAngle) * startDist;
            float curY = (float)Math.sin(startAngle) * startDist;
            float curZ = startZ;
            int segments = 5;
            float segLen = flameLength * 0.85f / (float)segments;
            float arcWidth = (0.025f + Math.min(1.5f, thrust) * 0.015f) * alphaMultiplier;
            for (int s = 0; s < segments; ++s) {
                int segSeed = arcSeed + s * 37;
                float nextZ = curZ - segLen * (0.8f + ThrusterFlameRenderer.hash(segSeed, 4) * 0.4f);
                float radialSpread = (0.2f + Math.min(1.5f, thrust) * 0.25f) * (1.0f + (float)s / (float)segments);
                float nextX = curX + (ThrusterFlameRenderer.hash(segSeed, 5) - 0.5f) * radialSpread;
                float nextY = curY + (ThrusterFlameRenderer.hash(segSeed, 6) - 0.5f) * radialSpread;
                float segProgress = (float)s / (float)segments;
                float alpha = (1.0f - segProgress * 0.75f) * alphaMultiplier * 0.9f;
                float r = Mth.lerp((float)segProgress, (float)0.9f, (float)0.1f);
                float g = Mth.lerp((float)segProgress, (float)0.98f, (float)0.7f);
                float b = 1.0f;
                consumer.addVertex(pose, curX - arcWidth, curY, curZ).setColor(r, g, b, alpha);
                consumer.addVertex(pose, curX + arcWidth, curY, curZ).setColor(r, g, b, alpha);
                consumer.addVertex(pose, nextX + arcWidth, nextY, nextZ).setColor(r, g, b, alpha * 0.7f);
                consumer.addVertex(pose, nextX - arcWidth, nextY, nextZ).setColor(r, g, b, alpha * 0.7f);
                consumer.addVertex(pose, curX, curY - arcWidth, curZ).setColor(r, g, b, alpha);
                consumer.addVertex(pose, curX, curY + arcWidth, curZ).setColor(r, g, b, alpha);
                consumer.addVertex(pose, nextX, nextY + arcWidth, nextZ).setColor(r, g, b, alpha * 0.7f);
                consumer.addVertex(pose, nextX, nextY - arcWidth, nextZ).setColor(r, g, b, alpha * 0.7f);
                curX = nextX;
                curY = nextY;
                curZ = nextZ;
            }
            ++a;
        }
    }

    private static void renderReverseThrust(Matrix4f pose, VertexConsumer consumer, float time, float brakePower, int seed) {
        int slices = 12;
        float dishLength = 0.5f + brakePower * 0.35f;
        float dishRadius = 0.42f * (1.1f + brakePower * 0.7f);
        float pulse = 1.0f + 0.1f * (float)Math.sin(time * 1.5f);
        dishRadius *= pulse;
        float alpha = Mth.clamp((float)(brakePower * 0.9f), (float)0.0f, (float)0.95f);
        int i = 0;
        while (i < slices) {
            float a1 = (float)((double)(i * 2) * Math.PI / (double)slices);
            float a2 = (float)((double)((i + 1) * 2) * Math.PI / (double)slices);
            float x1_start = (float)Math.cos(a1) * 0.42f * 0.6f;
            float y1_start = (float)Math.sin(a1) * 0.42f * 0.6f;
            float x2_start = (float)Math.cos(a2) * 0.42f * 0.6f;
            float y2_start = (float)Math.sin(a2) * 0.42f * 0.6f;
            float x1_end = (float)Math.cos(a1) * dishRadius;
            float y1_end = (float)Math.sin(a1) * dishRadius;
            float x2_end = (float)Math.cos(a2) * dishRadius;
            float y2_end = (float)Math.sin(a2) * dishRadius;
            consumer.addVertex(pose, x1_start, y1_start, -0.02f).setColor(1.0f, 1.0f, 1.0f, alpha);
            consumer.addVertex(pose, x2_start, y2_start, -0.02f).setColor(1.0f, 1.0f, 1.0f, alpha);
            consumer.addVertex(pose, x2_end, y2_end, -dishLength).setColor(0.0f, 0.7f, 1.0f, 0.0f);
            consumer.addVertex(pose, x1_end, y1_end, -dishLength).setColor(0.0f, 0.7f, 1.0f, 0.0f);
            ++i;
        }
    }

    private static float hash(int seed, int index) {
        int n = seed * 374761393 + index * 668265263;
        n = (n ^ n >> 13) * 1274126177;
        return (float)((double)(n & Integer.MAX_VALUE) / 2.147483647E9);
    }
}

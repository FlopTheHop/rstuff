/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.util.Mth
 *  net.minecraft.world.phys.Vec3
 *  org.ExtraTortik.ricks_stuff.client.renderer.PortalRenderTypes
 *  org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity
 *  org.joml.Matrix4f
 */
package org.ExtraTortik.ricks_stuff.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.ExtraTortik.ricks_stuff.client.renderer.PortalRenderTypes;
import org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity;
import org.joml.Matrix4f;

public class CatastrophicFireRenderer {
    private static final int THRUSTER_PLANES = 8;
    private static final int THRUSTER_SEGMENTS = 7;
    private static final float THRUSTER_BASE_RADIUS = 0.42f;
    private static final int DECK_PLANES = 4;
    private static final int DECK_SEGMENTS = 5;

    public static void renderEngineFire(SpaceCruiserEntity ship, PoseStack poseStack, MultiBufferSource buffer, float partialTicks, float intensity) {
        if (intensity <= 0.05f) {
            return;
        }
        float time = (float)ship.tickCount + partialTicks;
        Vec3 worldMotion = ship.getDeltaMovement();
        float yaw = Mth.rotLerp((float)partialTicks, (float)ship.yRotO, (float)ship.getYRot());
        float pitch = Mth.rotLerp((float)partialTicks, (float)ship.xRotO, (float)ship.getXRot());
        float roll = ship.getInterpolatedRoll(partialTicks);
        Vec3 localWind = CatastrophicFireRenderer.toLocalWind(worldMotion, yaw, pitch, roll);
        VertexConsumer outerConsumer = buffer.getBuffer(PortalRenderTypes.CATASTROPHIC_FIRE_OUTER);
        if (ship.isCrashing()) {
            CatastrophicFireRenderer.renderThrusterFlamesOuter(poseStack, outerConsumer, SpaceCruiserEntity.THRUSTER_LEFT, time, intensity, localWind, 101, false);
            CatastrophicFireRenderer.renderThrusterFlamesOuter(poseStack, outerConsumer, SpaceCruiserEntity.THRUSTER_RIGHT, time, intensity, localWind, 202, false);
        }
        CatastrophicFireRenderer.renderRearDeckFireOuter(poseStack, outerConsumer, SpaceCruiserEntity.REAR_HOOD_DECK, time, intensity, localWind, ship.isBroken());
        VertexConsumer coreConsumer = buffer.getBuffer(PortalRenderTypes.CATASTROPHIC_FIRE_CORE);
        if (ship.isCrashing()) {
            CatastrophicFireRenderer.renderThrusterFlamesCore(poseStack, coreConsumer, SpaceCruiserEntity.THRUSTER_LEFT, time, intensity, localWind, 101, false);
            CatastrophicFireRenderer.renderThrusterFlamesCore(poseStack, coreConsumer, SpaceCruiserEntity.THRUSTER_RIGHT, time, intensity, localWind, 202, false);
        }
        CatastrophicFireRenderer.renderRearDeckFireCore(poseStack, coreConsumer, SpaceCruiserEntity.REAR_HOOD_DECK, time, intensity, localWind, ship.isBroken());
    }

    public static void renderWarpThrusterFlames(SpaceCruiserEntity ship, PoseStack poseStack, MultiBufferSource buffer, float partialTicks, float intensity) {
        if (intensity <= 0.02f) {
            return;
        }
        float time = (float)ship.tickCount + partialTicks;
        Vec3 worldMotion = ship.getDeltaMovement();
        float yaw = Mth.rotLerp((float)partialTicks, (float)ship.yRotO, (float)ship.getYRot());
        float pitch = Mth.rotLerp((float)partialTicks, (float)ship.xRotO, (float)ship.getXRot());
        float roll = ship.getInterpolatedRoll(partialTicks);
        Vec3 localWind = CatastrophicFireRenderer.toLocalWind(worldMotion, yaw, pitch, roll);
        VertexConsumer outerConsumer = buffer.getBuffer(PortalRenderTypes.CATASTROPHIC_FIRE_OUTER);
        CatastrophicFireRenderer.renderThrusterFlamesOuter(poseStack, outerConsumer, SpaceCruiserEntity.THRUSTER_LEFT, time, intensity, localWind, 101, true);
        CatastrophicFireRenderer.renderThrusterFlamesOuter(poseStack, outerConsumer, SpaceCruiserEntity.THRUSTER_RIGHT, time, intensity, localWind, 202, true);
        VertexConsumer coreConsumer = buffer.getBuffer(PortalRenderTypes.CATASTROPHIC_FIRE_CORE);
        CatastrophicFireRenderer.renderThrusterFlamesCore(poseStack, coreConsumer, SpaceCruiserEntity.THRUSTER_LEFT, time, intensity, localWind, 101, true);
        CatastrophicFireRenderer.renderThrusterFlamesCore(poseStack, coreConsumer, SpaceCruiserEntity.THRUSTER_RIGHT, time, intensity, localWind, 202, true);
    }

    private static void renderThrusterFlamesOuter(PoseStack poseStack, VertexConsumer outerConsumer, Vec3 origin, float time, float intensity, Vec3 wind, int seed, boolean isWarp) {
        poseStack.pushPose();
        poseStack.translate(origin.x, origin.y, origin.z);
        Matrix4f pose = poseStack.last().pose();
        float baseLength = (1.65f + 0.95f * intensity) * (1.0f + 0.1f * (float)Math.sin(time * 0.65f + (float)seed));
        float windSpeed = (float)wind.length();
        if (windSpeed > 0.2f) {
            baseLength += Math.min(1.2f, (windSpeed - 0.2f) * 0.7f);
        }
        float windX = Mth.clamp((float)((float)wind.x * 0.25f), (float)-0.6f, (float)0.6f);
        float windY = Mth.clamp((float)((float)wind.y * 0.25f), (float)-0.6f, (float)0.6f);
        float windZ = Mth.clamp((float)((float)wind.z * 0.25f), (float)-1.0f, (float)0.0f);
        int p = 0;
        while (true) {
            if (p >= 8) {
                poseStack.popPose();
                return;
            }
            float planeAngle = (float)((double)p * Math.PI / 8.0);
            float cosP = (float)Math.cos(planeAngle);
            float sinP = (float)Math.sin(planeAngle);
            float planePhase = (float)p * 0.75f + (float)seed * 0.2f;
            for (int s = 0; s < 7; ++s) {
                float b1Out;
                float g1Out;
                float r1Out;
                float b0Out;
                float g0Out;
                float r0Out;
                float u0 = (float)s / 7.0f;
                float u1 = (float)(s + 1) / 7.0f;
                float env0 = u0 < 0.25f ? 0.85f + 0.4f * (u0 / 0.25f) : 1.25f * (1.0f - (u0 - 0.25f) / 0.75f);
                float env1 = u1 < 0.25f ? 0.85f + 0.4f * (u1 / 0.25f) : 1.25f * (1.0f - (u1 - 0.25f) / 0.75f);
                float wave0 = 1.0f + 0.16f * (float)Math.sin(time * 1.5f - u0 * 3.5f + planePhase);
                float wave1 = 1.0f + 0.16f * (float)Math.sin(time * 1.5f - u1 * 3.5f + planePhase);
                float w0Outer = 0.42f * env0 * wave0;
                float w1Outer = 0.42f * env1 * wave1;
                float z0 = -u0 * baseLength + windZ * (float)Math.pow(u0, 1.25);
                float z1 = -u1 * baseLength + windZ * (float)Math.pow(u1, 1.25);
                float cx0 = windX * (float)Math.pow(u0, 1.25);
                float cy0 = windY * (float)Math.pow(u0, 1.25);
                float cx1 = windX * (float)Math.pow(u1, 1.25);
                float cy1 = windY * (float)Math.pow(u1, 1.25);
                float a0Out = (1.0f - u0 * 0.85f) * 0.88f * intensity;
                float a1Out = (1.0f - u1 * 0.85f) * 0.88f * intensity;
                if (isWarp) {
                    r0Out = Mth.lerp((float)u0, (float)0.15f, (float)0.01f);
                    g0Out = Mth.lerp((float)u0, (float)0.85f, (float)0.25f);
                    b0Out = Mth.lerp((float)u0, (float)1.0f, (float)0.95f);
                    r1Out = Mth.lerp((float)u1, (float)0.15f, (float)0.01f);
                    g1Out = Mth.lerp((float)u1, (float)0.85f, (float)0.25f);
                    b1Out = Mth.lerp((float)u1, (float)1.0f, (float)0.95f);
                } else {
                    r0Out = Mth.lerp((float)u0, (float)1.0f, (float)0.75f);
                    g0Out = Mth.lerp((float)u0, (float)0.52f, (float)0.05f);
                    b0Out = 0.01f;
                    r1Out = Mth.lerp((float)u1, (float)1.0f, (float)0.75f);
                    g1Out = Mth.lerp((float)u1, (float)0.52f, (float)0.05f);
                    b1Out = 0.01f;
                }
                outerConsumer.addVertex(pose, cx0 - cosP * w0Outer, cy0 - sinP * w0Outer, z0).setColor(r0Out, g0Out, b0Out, a0Out);
                outerConsumer.addVertex(pose, cx0 + cosP * w0Outer, cy0 + sinP * w0Outer, z0).setColor(r0Out, g0Out, b0Out, a0Out);
                outerConsumer.addVertex(pose, cx1 + cosP * w1Outer, cy1 + sinP * w1Outer, z1).setColor(r1Out, g1Out, b1Out, a1Out);
                outerConsumer.addVertex(pose, cx1 - cosP * w1Outer, cy1 - sinP * w1Outer, z1).setColor(r1Out, g1Out, b1Out, a1Out);
            }
            ++p;
        }
    }

    private static void renderThrusterFlamesCore(PoseStack poseStack, VertexConsumer coreConsumer, Vec3 origin, float time, float intensity, Vec3 wind, int seed, boolean isWarp) {
        poseStack.pushPose();
        poseStack.translate(origin.x, origin.y, origin.z);
        Matrix4f pose = poseStack.last().pose();
        float baseLength = (1.65f + 0.95f * intensity) * (1.0f + 0.1f * (float)Math.sin(time * 0.65f + (float)seed));
        float windSpeed = (float)wind.length();
        if (windSpeed > 0.2f) {
            baseLength += Math.min(1.2f, (windSpeed - 0.2f) * 0.7f);
        }
        float windX = Mth.clamp((float)((float)wind.x * 0.25f), (float)-0.6f, (float)0.6f);
        float windY = Mth.clamp((float)((float)wind.y * 0.25f), (float)-0.6f, (float)0.6f);
        float windZ = Mth.clamp((float)((float)wind.z * 0.25f), (float)-1.0f, (float)0.0f);
        int p = 0;
        while (true) {
            if (p >= 8) {
                poseStack.popPose();
                return;
            }
            float planeAngle = (float)((double)p * Math.PI / 8.0);
            float cosP = (float)Math.cos(planeAngle);
            float sinP = (float)Math.sin(planeAngle);
            float planePhase = (float)p * 0.75f + (float)seed * 0.2f;
            for (int s = 0; s < 7; ++s) {
                float b1Core;
                float g1Core;
                float r1Core;
                float b0Core;
                float g0Core;
                float r0Core;
                float u0 = (float)s / 7.0f;
                float u1 = (float)(s + 1) / 7.0f;
                if (u0 >= 0.7f) continue;
                float env0 = u0 < 0.25f ? 0.85f + 0.4f * (u0 / 0.25f) : 1.25f * (1.0f - (u0 - 0.25f) / 0.75f);
                float env1 = u1 < 0.25f ? 0.85f + 0.4f * (u1 / 0.25f) : 1.25f * (1.0f - (u1 - 0.25f) / 0.75f);
                float wave0 = 1.0f + 0.16f * (float)Math.sin(time * 1.5f - u0 * 3.5f + planePhase);
                float wave1 = 1.0f + 0.16f * (float)Math.sin(time * 1.5f - u1 * 3.5f + planePhase);
                float w0Outer = 0.42f * env0 * wave0;
                float w1Outer = 0.42f * env1 * wave1;
                float z0 = -u0 * baseLength + windZ * (float)Math.pow(u0, 1.25);
                float z1 = -u1 * baseLength + windZ * (float)Math.pow(u1, 1.25);
                float cx0 = windX * (float)Math.pow(u0, 1.25);
                float cy0 = windY * (float)Math.pow(u0, 1.25);
                float cx1 = windX * (float)Math.pow(u1, 1.25);
                float cy1 = windY * (float)Math.pow(u1, 1.25);
                float coreU0 = u0 / 0.7f;
                float coreU1 = u1 / 0.7f;
                float w0Core = w0Outer * 0.52f;
                float w1Core = w1Outer * 0.52f;
                float a0Core = (1.0f - coreU0 * 0.9f) * 0.95f * intensity;
                float a1Core = (1.0f - coreU1 * 0.9f) * 0.95f * intensity;
                if (isWarp) {
                    r0Core = Mth.lerp((float)coreU0, (float)0.85f, (float)0.15f);
                    g0Core = Mth.lerp((float)coreU0, (float)0.98f, (float)0.75f);
                    b0Core = 1.0f;
                    r1Core = Mth.lerp((float)coreU1, (float)0.85f, (float)0.15f);
                    g1Core = Mth.lerp((float)coreU1, (float)0.98f, (float)0.75f);
                    b1Core = 1.0f;
                } else {
                    r0Core = 1.0f;
                    g0Core = Mth.lerp((float)coreU0, (float)1.0f, (float)0.65f);
                    b0Core = Mth.lerp((float)coreU0, (float)0.78f, (float)0.0f);
                    r1Core = 1.0f;
                    g1Core = Mth.lerp((float)coreU1, (float)1.0f, (float)0.65f);
                    b1Core = Mth.lerp((float)coreU1, (float)0.78f, (float)0.0f);
                }
                coreConsumer.addVertex(pose, cx0 - cosP * w0Core, cy0 - sinP * w0Core, z0 + 0.02f).setColor(r0Core, g0Core, b0Core, a0Core);
                coreConsumer.addVertex(pose, cx0 + cosP * w0Core, cy0 + sinP * w0Core, z0 + 0.02f).setColor(r0Core, g0Core, b0Core, a0Core);
                coreConsumer.addVertex(pose, cx1 + cosP * w1Core, cy1 + sinP * w1Core, z1 + 0.02f).setColor(r1Core, g1Core, b1Core, a1Core);
                coreConsumer.addVertex(pose, cx1 - cosP * w1Core, cy1 - sinP * w1Core, z1 + 0.02f).setColor(r1Core, g1Core, b1Core, a1Core);
            }
            ++p;
        }
    }

    private static void renderRearDeckFireOuter(PoseStack poseStack, VertexConsumer outerConsumer, Vec3 origin, float time, float intensity, Vec3 wind, boolean isLanded) {
        poseStack.pushPose();
        poseStack.translate(origin.x, origin.y, origin.z);
        Matrix4f pose = poseStack.last().pose();
        float windX = isLanded ? 0.0f : Mth.clamp((float)((float)wind.x * 0.2f), (float)-0.4f, (float)0.4f);
        float windZ = isLanded ? 0.0f : Mth.clamp((float)((float)wind.z * 0.25f), (float)-0.6f, (float)0.0f);
        float heightMult = isLanded ? 0.6f : 1.0f;
        CatastrophicFireRenderer.renderDeckBreachOuter(pose, outerConsumer, 0.0f, 0.0f, 0.0f, 0.35f, 0.95f * heightMult, time, intensity, windX, windZ, 11);
        CatastrophicFireRenderer.renderDeckBreachOuter(pose, outerConsumer, -0.65f, -0.02f, 0.22f, 0.25f, 0.75f * heightMult, time, intensity, windX, windZ, 22);
        CatastrophicFireRenderer.renderDeckBreachOuter(pose, outerConsumer, 0.65f, -0.02f, 0.22f, 0.25f, 0.75f * heightMult, time, intensity, windX, windZ, 33);
        poseStack.popPose();
    }

    private static void renderRearDeckFireCore(PoseStack poseStack, VertexConsumer coreConsumer, Vec3 origin, float time, float intensity, Vec3 wind, boolean isLanded) {
        poseStack.pushPose();
        poseStack.translate(origin.x, origin.y, origin.z);
        Matrix4f pose = poseStack.last().pose();
        float windX = isLanded ? 0.0f : Mth.clamp((float)((float)wind.x * 0.2f), (float)-0.4f, (float)0.4f);
        float windZ = isLanded ? 0.0f : Mth.clamp((float)((float)wind.z * 0.25f), (float)-0.6f, (float)0.0f);
        float heightMult = isLanded ? 0.6f : 1.0f;
        CatastrophicFireRenderer.renderDeckBreachCore(pose, coreConsumer, 0.0f, 0.0f, 0.0f, 0.35f, 0.95f * heightMult, time, intensity, windX, windZ, 11);
        CatastrophicFireRenderer.renderDeckBreachCore(pose, coreConsumer, -0.65f, -0.02f, 0.22f, 0.25f, 0.75f * heightMult, time, intensity, windX, windZ, 22);
        CatastrophicFireRenderer.renderDeckBreachCore(pose, coreConsumer, 0.65f, -0.02f, 0.22f, 0.25f, 0.75f * heightMult, time, intensity, windX, windZ, 33);
        poseStack.popPose();
    }

    private static void renderDeckBreachOuter(Matrix4f pose, VertexConsumer outerConsumer, float offsetX, float offsetY, float offsetZ, float radius, float baseHeight, float time, float intensity, float windX, float windZ, int seed) {
        float dynamicHeight = baseHeight * (0.8f + 0.25f * intensity) * (1.0f + 0.12f * (float)Math.sin(time * 0.85f + (float)seed));
        int p = 0;
        while (p < 4) {
            float planeAngle = (float)((double)p * Math.PI / 4.0);
            float cosP = (float)Math.cos(planeAngle);
            float sinP = (float)Math.sin(planeAngle);
            float planePhase = (float)p * 0.8f + (float)seed * 0.25f;
            for (int s = 0; s < 5; ++s) {
                float u0 = (float)s / 5.0f;
                float u1 = (float)(s + 1) / 5.0f;
                float wave0 = 1.0f + 0.14f * (float)Math.sin(time * 1.4f - u0 * 3.0f + planePhase);
                float wave1 = 1.0f + 0.14f * (float)Math.sin(time * 1.4f - u1 * 3.0f + planePhase);
                float w0 = radius * (1.0f - u0 * 0.75f) * wave0;
                float w1 = radius * (1.0f - u1 * 0.75f) * wave1;
                float y0 = offsetY + u0 * dynamicHeight;
                float y1 = offsetY + u1 * dynamicHeight;
                float cx0 = offsetX + windX * (float)Math.pow(u0, 1.25);
                float cz0 = offsetZ + windZ * (float)Math.pow(u0, 1.25);
                float cx1 = offsetX + windX * (float)Math.pow(u1, 1.25);
                float cz1 = offsetZ + windZ * (float)Math.pow(u1, 1.25);
                float a0Out = (1.0f - u0 * 0.85f) * 0.85f * intensity;
                float a1Out = (1.0f - u1 * 0.85f) * 0.85f * intensity;
                float r0Out = Mth.lerp((float)u0, (float)1.0f, (float)0.8f);
                float g0Out = Mth.lerp((float)u0, (float)0.48f, (float)0.05f);
                float b0Out = 0.01f;
                float r1Out = Mth.lerp((float)u1, (float)1.0f, (float)0.8f);
                float g1Out = Mth.lerp((float)u1, (float)0.48f, (float)0.05f);
                float b1Out = 0.01f;
                outerConsumer.addVertex(pose, cx0 - cosP * w0, y0, cz0 - sinP * w0).setColor(r0Out, g0Out, b0Out, a0Out);
                outerConsumer.addVertex(pose, cx0 + cosP * w0, y0, cz0 + sinP * w0).setColor(r0Out, g0Out, b0Out, a0Out);
                outerConsumer.addVertex(pose, cx1 + cosP * w1, y1, cz1 + sinP * w1).setColor(r1Out, g1Out, b1Out, a1Out);
                outerConsumer.addVertex(pose, cx1 - cosP * w1, y1, cz1 - sinP * w1).setColor(r1Out, g1Out, b1Out, a1Out);
            }
            ++p;
        }
    }

    private static void renderDeckBreachCore(Matrix4f pose, VertexConsumer coreConsumer, float offsetX, float offsetY, float offsetZ, float radius, float baseHeight, float time, float intensity, float windX, float windZ, int seed) {
        float dynamicHeight = baseHeight * (0.8f + 0.25f * intensity) * (1.0f + 0.12f * (float)Math.sin(time * 0.85f + (float)seed));
        int p = 0;
        while (p < 4) {
            float planeAngle = (float)((double)p * Math.PI / 4.0);
            float cosP = (float)Math.cos(planeAngle);
            float sinP = (float)Math.sin(planeAngle);
            float planePhase = (float)p * 0.8f + (float)seed * 0.25f;
            for (int s = 0; s < 5; ++s) {
                float u0 = (float)s / 5.0f;
                float u1 = (float)(s + 1) / 5.0f;
                float wave0 = 1.0f + 0.14f * (float)Math.sin(time * 1.4f - u0 * 3.0f + planePhase);
                float wave1 = 1.0f + 0.14f * (float)Math.sin(time * 1.4f - u1 * 3.0f + planePhase);
                float w0 = radius * (1.0f - u0 * 0.75f) * wave0;
                float w1 = radius * (1.0f - u1 * 0.75f) * wave1;
                float y0 = offsetY + u0 * dynamicHeight;
                float y1 = offsetY + u1 * dynamicHeight;
                float cx0 = offsetX + windX * (float)Math.pow(u0, 1.25);
                float cz0 = offsetZ + windZ * (float)Math.pow(u0, 1.25);
                float cx1 = offsetX + windX * (float)Math.pow(u1, 1.25);
                float cz1 = offsetZ + windZ * (float)Math.pow(u1, 1.25);
                float w0Core = w0 * 0.52f;
                float w1Core = w1 * 0.52f;
                float a0Core = (1.0f - u0 * 0.95f) * 0.95f * intensity;
                float a1Core = (1.0f - u1 * 0.95f) * 0.95f * intensity;
                float r0Core = 1.0f;
                float g0Core = Mth.lerp((float)u0, (float)1.0f, (float)0.65f);
                float b0Core = Mth.lerp((float)u0, (float)0.78f, (float)0.0f);
                float r1Core = 1.0f;
                float g1Core = Mth.lerp((float)u1, (float)1.0f, (float)0.65f);
                float b1Core = Mth.lerp((float)u1, (float)0.78f, (float)0.0f);
                coreConsumer.addVertex(pose, cx0 - cosP * w0Core, y0 + 0.01f, cz0 - sinP * w0Core).setColor(r0Core, g0Core, b0Core, a0Core);
                coreConsumer.addVertex(pose, cx0 + cosP * w0Core, y0 + 0.01f, cz0 + sinP * w0Core).setColor(r0Core, g0Core, b0Core, a0Core);
                coreConsumer.addVertex(pose, cx1 + cosP * w1Core, y1 + 0.01f, cz1 + sinP * w1Core).setColor(r1Core, g1Core, b1Core, a1Core);
                coreConsumer.addVertex(pose, cx1 - cosP * w1Core, y1 + 0.01f, cz1 - sinP * w1Core).setColor(r1Core, g1Core, b1Core, a1Core);
            }
            ++p;
        }
    }

    private static Vec3 toLocalWind(Vec3 worldMotion, float yaw, float pitch, float roll) {
        double vx = -worldMotion.x;
        double vy = -worldMotion.y;
        double vz = -worldMotion.z;
        float yawRad = (float)Math.toRadians(yaw);
        float cosY = (float)Math.cos(yawRad);
        float sinY = (float)Math.sin(yawRad);
        double x1 = vx * (double)cosY + vz * (double)sinY;
        double y1 = vy;
        double z1 = -vx * (double)sinY + vz * (double)cosY;
        float pitchRad = (float)Math.toRadians(-pitch);
        float cosP = (float)Math.cos(pitchRad);
        float sinP = (float)Math.sin(pitchRad);
        double x2 = x1;
        double y2 = y1 * (double)cosP - z1 * (double)sinP;
        double z2 = y1 * (double)sinP + z1 * (double)cosP;
        float rollRad = (float)Math.toRadians(-roll);
        float cosR = (float)Math.cos(rollRad);
        float sinR = (float)Math.sin(rollRad);
        double x3 = x2 * (double)cosR - y2 * (double)sinR;
        double y3 = x2 * (double)sinR + y2 * (double)cosR;
        double z3 = z2;
        return new Vec3(x3, y3, z3);
    }
}

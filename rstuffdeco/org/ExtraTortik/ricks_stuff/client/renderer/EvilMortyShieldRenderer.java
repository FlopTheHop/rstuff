/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.Camera
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent$Stage
 *  org.ExtraTortik.ricks_stuff.client.renderer.EvilMortyShieldClientManager
 *  org.ExtraTortik.ricks_stuff.client.renderer.EvilMortyShieldClientManager$ShieldInstance
 *  org.ExtraTortik.ricks_stuff.client.renderer.EvilMortyShieldClientManager$SparkInstance
 *  org.ExtraTortik.ricks_stuff.client.renderer.PortalRenderTypes
 *  org.joml.Matrix4f
 *  org.joml.Quaternionf
 *  org.joml.Quaternionfc
 *  org.joml.Vector3f
 */
package org.ExtraTortik.ricks_stuff.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.ExtraTortik.ricks_stuff.client.renderer.EvilMortyShieldClientManager;
import org.ExtraTortik.ricks_stuff.client.renderer.PortalRenderTypes;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;

@OnlyIn(value=Dist.CLIENT)
@EventBusSubscriber(modid="ricks_stuff", value={Dist.CLIENT})
public class EvilMortyShieldRenderer {
    private static final float RADIUS_X = 1.35f;
    private static final float RADIUS_Y = 1.45f;
    private static final float RADIUS_Z = 1.35f;
    private static final int LAT_BANDS = 24;
    private static final int LON_BANDS = 36;

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }
        List shields = EvilMortyShieldClientManager.getActiveShields();
        List sparks = EvilMortyShieldClientManager.getActiveSparks();
        if (shields.isEmpty() && sparks.isEmpty()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        if (mc.player == null) {
            return;
        }
        PoseStack poseStack = event.getPoseStack();
        if (poseStack == null) {
            return;
        }
        Camera camera = event.getCamera();
        Vec3 camPos = camera.getPosition();
        float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);
        float gameTime = (float)mc.level.getGameTime() + partialTick;
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        boolean renderedShields = false;
        boolean renderedSparks = false;
        if (!shields.isEmpty()) {
            VertexConsumer domeConsumer = bufferSource.getBuffer(PortalRenderTypes.FORCEFIELD_DOME);
            for (EvilMortyShieldClientManager.ShieldInstance shield : shields) {
                Entity entity = mc.level.getEntity(shield.getEntityId());
                if (entity == null || !entity.isAlive()) continue;
                double px = Mth.lerp((double)partialTick, (double)entity.xOld, (double)entity.getX());
                double py = Mth.lerp((double)partialTick, (double)entity.yOld, (double)entity.getY());
                double pz = Mth.lerp((double)partialTick, (double)entity.zOld, (double)entity.getZ());
                Vec3 center = new Vec3(px, py + (double)entity.getBbHeight() * 0.5, pz);
                float currentAge = (float)shield.getAge() + partialTick;
                float t = Mth.clamp((float)(currentAge / (float)shield.getMaxAge()), (float)0.0f, (float)1.0f);
                poseStack.pushPose();
                poseStack.translate(center.x - camPos.x, center.y - camPos.y, center.z - camPos.z);
                Matrix4f matrix = poseStack.last().pose();
                EvilMortyShieldRenderer.renderShieldDomeBase(matrix, domeConsumer, center, camPos, shield.getImpactDir(), t, gameTime, shield.isBreak());
                poseStack.popPose();
                renderedShields = true;
            }
            if (renderedShields) {
                bufferSource.endBatch(PortalRenderTypes.FORCEFIELD_DOME);
            }
            VertexConsumer additiveConsumer = bufferSource.getBuffer(PortalRenderTypes.FORCEFIELD_ADDITIVE);
            for (EvilMortyShieldClientManager.ShieldInstance shield : shields) {
                Entity entity = mc.level.getEntity(shield.getEntityId());
                if (entity == null || !entity.isAlive()) continue;
                double px = Mth.lerp((double)partialTick, (double)entity.xOld, (double)entity.getX());
                double py = Mth.lerp((double)partialTick, (double)entity.yOld, (double)entity.getY());
                double pz = Mth.lerp((double)partialTick, (double)entity.zOld, (double)entity.getZ());
                Vec3 center = new Vec3(px, py + (double)entity.getBbHeight() * 0.5, pz);
                float currentAge = (float)shield.getAge() + partialTick;
                float t = Mth.clamp((float)(currentAge / (float)shield.getMaxAge()), (float)0.0f, (float)1.0f);
                poseStack.pushPose();
                poseStack.translate(center.x - camPos.x, center.y - camPos.y, center.z - camPos.z);
                Matrix4f matrix = poseStack.last().pose();
                EvilMortyShieldRenderer.renderShieldDomeAdditive(matrix, additiveConsumer, center, camPos, shield.getImpactDir(), t, gameTime, shield.isBreak());
                poseStack.popPose();
            }
            renderedSparks = true;
        }
        if (!sparks.isEmpty()) {
            VertexConsumer additiveConsumer = bufferSource.getBuffer(PortalRenderTypes.FORCEFIELD_ADDITIVE);
            poseStack.pushPose();
            Matrix4f sparkPose = poseStack.last().pose();
            EvilMortyShieldRenderer.renderSparks(sparkPose, additiveConsumer, sparks, camPos, camera, partialTick);
            poseStack.popPose();
            renderedSparks = true;
        }
        if (!renderedShields) {
            if (!renderedSparks) return;
        }
        bufferSource.endBatch(PortalRenderTypes.FORCEFIELD_ADDITIVE);
    }

    private static void renderShieldDomeBase(Matrix4f pose, VertexConsumer domeConsumer, Vec3 worldCenter, Vec3 camPos, Vec3 impactDir, float t, float gameTime, boolean isBreak) {
        Vec3 toCam;
        double camDist;
        float uFront = t <= 0.35f ? t / 0.35f : 1.0f;
        float fade = 1.0f;
        if (t > 0.65f) {
            fade = Math.max(0.0f, 1.0f - (t - 0.65f) / 0.35f);
        }
        Vec3 camViewDir = (camDist = (toCam = camPos.subtract(worldCenter)).length()) > 1.0E-4 ? toCam.scale(1.0 / camDist) : new Vec3(0.0, 1.0, 0.0);
        Vec3 normImpactDir = impactDir == null || impactDir.lengthSqr() < 1.0E-4 ? new Vec3(0.0, 0.0, 1.0) : impactDir.normalize();
        float dx = (float)normImpactDir.x;
        float dy = (float)normImpactDir.y;
        float dz = (float)normImpactDir.z;
        int lat = 0;
        while (lat < 24) {
            float theta0 = (float)((double)lat * Math.PI / 24.0);
            float theta1 = (float)((double)(lat + 1) * Math.PI / 24.0);
            float sinTheta0 = Mth.sin((float)theta0);
            float cosTheta0 = Mth.cos((float)theta0);
            float sinTheta1 = Mth.sin((float)theta1);
            float cosTheta1 = Mth.cos((float)theta1);
            for (int lon = 0; lon < 36; ++lon) {
                float b;
                float g;
                float r;
                float noise;
                float phi0 = (float)((double)(lon * 2) * Math.PI / 36.0);
                float phi1 = (float)((double)((lon + 1) * 2) * Math.PI / 36.0);
                float sinPhi0 = Mth.sin((float)phi0);
                float cosPhi0 = Mth.cos((float)phi0);
                float sinPhi1 = Mth.sin((float)phi1);
                float cosPhi1 = Mth.cos((float)phi1);
                float n00x = sinTheta0 * cosPhi0;
                float n00y = cosTheta0;
                float n00z = sinTheta0 * sinPhi0;
                float n01x = sinTheta0 * cosPhi1;
                float n01y = cosTheta0;
                float n01z = sinTheta0 * sinPhi1;
                float n11x = sinTheta1 * cosPhi1;
                float n11y = cosTheta1;
                float n11z = sinTheta1 * sinPhi1;
                float n10x = sinTheta1 * cosPhi0;
                float n10y = cosTheta1;
                float n10z = sinTheta1 * sinPhi0;
                float x00 = n00x * 1.35f;
                float y00 = n00y * 1.45f;
                float z00 = n00z * 1.35f;
                float x01 = n01x * 1.35f;
                float y01 = n01y * 1.45f;
                float z01 = n01z * 1.35f;
                float x11 = n11x * 1.35f;
                float y11 = n11y * 1.45f;
                float z11 = n11z * 1.35f;
                float x10 = n10x * 1.35f;
                float y10 = n10y * 1.45f;
                float z10 = n10z * 1.35f;
                float avgNx = 0.25f * (n00x + n01x + n11x + n10x);
                float avgNy = 0.25f * (n00y + n01y + n11y + n10y);
                float avgNz = 0.25f * (n00z + n01z + n11z + n10z);
                float nLen = Mth.sqrt((float)(avgNx * avgNx + avgNy * avgNy + avgNz * avgNz));
                if (nLen > 1.0E-4f) {
                    avgNx /= nLen;
                    avgNy /= nLen;
                    avgNz /= nLen;
                }
                float dotImpact = avgNx * dx + avgNy * dy + avgNz * dz;
                float angle = (float)Math.acos(dotImpact = Mth.clamp((float)dotImpact, (float)-1.0f, (float)1.0f));
                float u = angle / (float)Math.PI;
                float uEffective = u + (noise = (float)Math.sin(avgNx * 12.0f + avgNy * 14.0f + gameTime * 0.2f) * 0.025f);
                if (uEffective > uFront + 0.05f) continue;
                float distToFront = Math.abs(uEffective - uFront);
                float edgeFlash = 0.0f;
                if (t <= 0.4f && distToFront < 0.1f) {
                    edgeFlash = 1.0f - distToFront / 0.1f;
                    if (t > 0.35f) {
                        edgeFlash *= 1.0f - (t - 0.35f) / 0.05f;
                    }
                }
                float impactFlash = 0.0f;
                if (t <= 0.3f && u < 0.35f) {
                    impactFlash = (1.0f - t / 0.3f) * (1.0f - u / 0.35f);
                }
                float originGlow = 0.0f;
                if (t <= 0.35f && u < 0.32f) {
                    float uFactor = 1.0f - u / 0.32f;
                    float tFactor = 1.0f - t / 0.35f;
                    originGlow = uFactor * uFactor * tFactor;
                }
                float dotCam = (float)Math.abs((double)avgNx * camViewDir.x + (double)avgNy * camViewDir.y + (double)avgNz * camViewDir.z);
                dotCam = Math.min(1.0f, dotCam);
                float fresnel = (float)Math.pow(1.0f - dotCam, 2.2);
                float p1 = Mth.sin((float)(avgNx * 4.2f + avgNy * 5.0f + gameTime * 0.18f));
                float p2 = Mth.cos((float)(avgNz * 4.5f - avgNy * 3.8f + gameTime * 0.15f));
                float p3 = Mth.sin((float)((avgNx + avgNz) * 5.5f + gameTime * 0.22f));
                float plasmaVal = (p1 * p2 + p3) * 0.5f + 0.5f;
                if (isBreak) {
                    r = 1.0f;
                    g = 0.12f + 0.25f * (edgeFlash + impactFlash);
                    b = 0.12f;
                } else {
                    r = 0.08f + 0.35f * (edgeFlash + impactFlash) + 0.55f * originGlow;
                    g = 0.82f + 0.18f * (edgeFlash + impactFlash) + 0.18f * originGlow;
                    b = 1.0f;
                }
                float alpha = (0.1f + 0.42f * fresnel + 0.12f * plasmaVal + 0.55f * edgeFlash + 0.4f * impactFlash + 0.55f * originGlow) * fade;
                alpha = Mth.clamp((float)alpha, (float)0.0f, (float)0.95f);
                domeConsumer.addVertex(pose, x00, y00, z00).setColor(r, g, b, alpha);
                domeConsumer.addVertex(pose, x01, y01, z01).setColor(r, g, b, alpha);
                domeConsumer.addVertex(pose, x11, y11, z11).setColor(r, g, b, alpha);
                domeConsumer.addVertex(pose, x10, y10, z10).setColor(r, g, b, alpha);
            }
            ++lat;
        }
    }

    private static void renderShieldDomeAdditive(Matrix4f pose, VertexConsumer additiveConsumer, Vec3 worldCenter, Vec3 camPos, Vec3 impactDir, float t, float gameTime, boolean isBreak) {
        Vec3 toCam;
        double camDist;
        float uFront = t <= 0.35f ? t / 0.35f : 1.0f;
        float fade = 1.0f;
        if (t > 0.65f) {
            fade = Math.max(0.0f, 1.0f - (t - 0.65f) / 0.35f);
        }
        Vec3 camViewDir = (camDist = (toCam = camPos.subtract(worldCenter)).length()) > 1.0E-4 ? toCam.scale(1.0 / camDist) : new Vec3(0.0, 1.0, 0.0);
        Vec3 normImpactDir = impactDir == null || impactDir.lengthSqr() < 1.0E-4 ? new Vec3(0.0, 0.0, 1.0) : impactDir.normalize();
        float dx = (float)normImpactDir.x;
        float dy = (float)normImpactDir.y;
        float dz = (float)normImpactDir.z;
        int lat = 0;
        while (true) {
            if (lat >= 24) {
                if (!(t <= 0.35f)) return;
                EvilMortyShieldRenderer.renderImpactStarburst(pose, additiveConsumer, normImpactDir, t, gameTime, fade, isBreak);
                return;
            }
            float theta0 = (float)((double)lat * Math.PI / 24.0);
            float theta1 = (float)((double)(lat + 1) * Math.PI / 24.0);
            float sinTheta0 = Mth.sin((float)theta0);
            float cosTheta0 = Mth.cos((float)theta0);
            float sinTheta1 = Mth.sin((float)theta1);
            float cosTheta1 = Mth.cos((float)theta1);
            for (int lon = 0; lon < 36; ++lon) {
                float addB;
                float addG;
                float addR;
                float p3;
                float p2;
                float noise;
                float phi0 = (float)((double)(lon * 2) * Math.PI / 36.0);
                float phi1 = (float)((double)((lon + 1) * 2) * Math.PI / 36.0);
                float sinPhi0 = Mth.sin((float)phi0);
                float cosPhi0 = Mth.cos((float)phi0);
                float sinPhi1 = Mth.sin((float)phi1);
                float cosPhi1 = Mth.cos((float)phi1);
                float n00x = sinTheta0 * cosPhi0;
                float n00y = cosTheta0;
                float n00z = sinTheta0 * sinPhi0;
                float n01x = sinTheta0 * cosPhi1;
                float n01y = cosTheta0;
                float n01z = sinTheta0 * sinPhi1;
                float n11x = sinTheta1 * cosPhi1;
                float n11y = cosTheta1;
                float n11z = sinTheta1 * sinPhi1;
                float n10x = sinTheta1 * cosPhi0;
                float n10y = cosTheta1;
                float n10z = sinTheta1 * sinPhi0;
                float x00 = n00x * 1.35f;
                float y00 = n00y * 1.45f;
                float z00 = n00z * 1.35f;
                float x01 = n01x * 1.35f;
                float y01 = n01y * 1.45f;
                float z01 = n01z * 1.35f;
                float x11 = n11x * 1.35f;
                float y11 = n11y * 1.45f;
                float z11 = n11z * 1.35f;
                float x10 = n10x * 1.35f;
                float y10 = n10y * 1.45f;
                float z10 = n10z * 1.35f;
                float avgNx = 0.25f * (n00x + n01x + n11x + n10x);
                float avgNy = 0.25f * (n00y + n01y + n11y + n10y);
                float avgNz = 0.25f * (n00z + n01z + n11z + n10z);
                float nLen = Mth.sqrt((float)(avgNx * avgNx + avgNy * avgNy + avgNz * avgNz));
                if (nLen > 1.0E-4f) {
                    avgNx /= nLen;
                    avgNy /= nLen;
                    avgNz /= nLen;
                }
                float dotImpact = avgNx * dx + avgNy * dy + avgNz * dz;
                float angle = (float)Math.acos(dotImpact = Mth.clamp((float)dotImpact, (float)-1.0f, (float)1.0f));
                float u = angle / (float)Math.PI;
                float uEffective = u + (noise = (float)Math.sin(avgNx * 12.0f + avgNy * 14.0f + gameTime * 0.2f) * 0.025f);
                if (uEffective > uFront + 0.05f) continue;
                float distToFront = Math.abs(uEffective - uFront);
                float edgeFlash = 0.0f;
                if (t <= 0.4f && distToFront < 0.1f) {
                    edgeFlash = 1.0f - distToFront / 0.1f;
                    if (t > 0.35f) {
                        edgeFlash *= 1.0f - (t - 0.35f) / 0.05f;
                    }
                }
                float impactFlash = 0.0f;
                if (t <= 0.3f && u < 0.35f) {
                    impactFlash = (1.0f - t / 0.3f) * (1.0f - u / 0.35f);
                }
                float originGlow = 0.0f;
                if (t <= 0.35f && u < 0.32f) {
                    float uFactor = 1.0f - u / 0.32f;
                    float tFactor = 1.0f - t / 0.35f;
                    originGlow = uFactor * uFactor * tFactor;
                }
                float dotCam = (float)Math.abs((double)avgNx * camViewDir.x + (double)avgNy * camViewDir.y + (double)avgNz * camViewDir.z);
                dotCam = Math.min(1.0f, dotCam);
                float fresnel = (float)Math.pow(1.0f - dotCam, 2.2);
                float p1 = Mth.sin((float)(avgNx * 4.2f + avgNy * 5.0f + gameTime * 0.18f));
                float plasmaVal = (p1 * (p2 = Mth.cos((float)(avgNz * 4.5f - avgNy * 3.8f + gameTime * 0.15f))) + (p3 = Mth.sin((float)((avgNx + avgNz) * 5.5f + gameTime * 0.22f)))) * 0.5f + 0.5f;
                if (!(plasmaVal > 0.58f || edgeFlash > 0.12f || impactFlash > 0.1f || originGlow > 0.05f) && !(fresnel > 0.72f)) continue;
                if (isBreak) {
                    addR = 1.0f;
                    addG = 0.2f + 0.3f * edgeFlash;
                    addB = 0.1f;
                } else {
                    addR = 0.2f + 0.8f * (edgeFlash + impactFlash) + 0.8f * originGlow;
                    addG = 0.88f + 0.12f * (edgeFlash + impactFlash) + 0.12f * originGlow;
                    addB = 1.0f;
                }
                float addAlpha = ((plasmaVal - 0.58f) * 1.6f + edgeFlash * 1.2f + impactFlash * 1.5f + originGlow * 1.8f + (fresnel - 0.72f) * 0.9f) * fade;
                addAlpha = Mth.clamp((float)addAlpha, (float)0.0f, (float)1.0f);
                additiveConsumer.addVertex(pose, x00, y00, z00).setColor(addR, addG, addB, addAlpha);
                additiveConsumer.addVertex(pose, x01, y01, z01).setColor(addR, addG, addB, addAlpha);
                additiveConsumer.addVertex(pose, x11, y11, z11).setColor(addR, addG, addB, addAlpha);
                additiveConsumer.addVertex(pose, x10, y10, z10).setColor(addR, addG, addB, addAlpha);
            }
            ++lat;
        }
    }

    private static void renderImpactStarburst(Matrix4f pose, VertexConsumer consumer, Vec3 dir, float t, float gameTime, float fade, boolean isBreak) {
        Vec3 d = dir == null || dir.lengthSqr() < 1.0E-4 ? new Vec3(0.0, 0.0, 1.0) : dir.normalize();
        Vec3 up = Math.abs(d.y) < 0.9 ? new Vec3(0.0, 1.0, 0.0) : new Vec3(1.0, 0.0, 0.0);
        Vec3 t1 = up.cross(d).normalize();
        Vec3 t2 = d.cross(t1).normalize();
        float cx = (float)d.x * 1.35f;
        float cy = (float)d.y * 1.45f;
        float cz = (float)d.z * 1.35f;
        float burstFade = (1.0f - t / 0.35f) * fade;
        int rayCount = 8;
        int i = 0;
        while (i < rayCount) {
            float baseAngle = (float)((double)(i * 2) * Math.PI / (double)rayCount) + gameTime * 0.15f;
            float rayLen = (0.28f + 0.22f * Mth.sin((float)((float)i * 3.5f + gameTime * 0.4f))) * burstFade;
            float halfWidth = 0.035f * burstFade;
            float cosA = Mth.cos((float)baseAngle);
            float sinA = Mth.sin((float)baseAngle);
            float rx = (float)(t1.x * (double)cosA + t2.x * (double)sinA);
            float ry = (float)(t1.y * (double)cosA + t2.y * (double)sinA);
            float rz = (float)(t1.z * (double)cosA + t2.z * (double)sinA);
            float perpX = (float)(-t1.x * (double)sinA + t2.x * (double)cosA) * halfWidth;
            float perpY = (float)(-t1.y * (double)sinA + t2.y * (double)cosA) * halfWidth;
            float perpZ = (float)(-t1.z * (double)sinA + t2.z * (double)cosA) * halfWidth;
            float lift = 0.03f;
            float bx = cx + (float)d.x * lift;
            float by = cy + (float)d.y * lift;
            float bz = cz + (float)d.z * lift;
            float tipX = bx + rx * rayLen;
            float tipY = by + ry * rayLen;
            float tipZ = bz + rz * rayLen;
            float r = isBreak ? 1.0f : 0.7f;
            float g = isBreak ? 0.25f : 0.96f;
            float bVal = isBreak ? 0.1f : 1.0f;
            float a = 0.85f * burstFade;
            float tipR = 1.0f;
            float tipG = isBreak ? 0.6f : 1.0f;
            float tipB = isBreak ? 0.2f : 1.0f;
            consumer.addVertex(pose, bx - perpX, by - perpY, bz - perpZ).setColor(r, g, bVal, a);
            consumer.addVertex(pose, bx + perpX, by + perpY, bz + perpZ).setColor(r, g, bVal, a);
            consumer.addVertex(pose, tipX + perpX * 0.2f, tipY + perpY * 0.2f, tipZ + perpZ * 0.2f).setColor(tipR, tipG, tipB, a * 0.4f);
            consumer.addVertex(pose, tipX - perpX * 0.2f, tipY - perpY * 0.2f, tipZ - perpZ * 0.2f).setColor(tipR, tipG, tipB, a * 0.4f);
            ++i;
        }
    }

    private static void renderSparks(Matrix4f pose, VertexConsumer consumer, List<EvilMortyShieldClientManager.SparkInstance> sparks, Vec3 camPos, Camera camera, float partialTick) {
        Quaternionf camRot = camera.rotation();
        Vector3f camRight = new Vector3f(1.0f, 0.0f, 0.0f).rotate((Quaternionfc)camRot);
        Vector3f camUp = new Vector3f(0.0f, 1.0f, 0.0f).rotate((Quaternionfc)camRot);
        Iterator<EvilMortyShieldClientManager.SparkInstance> iterator = sparks.iterator();
        while (iterator.hasNext()) {
            Vec3 headRel;
            float headB;
            EvilMortyShieldClientManager.SparkInstance spark = iterator.next();
            double hx = spark.getX(partialTick);
            double hy = spark.getY(partialTick);
            double hz = spark.getZ(partialTick);
            Vec3 head = new Vec3(hx, hy, hz);
            List history = spark.getTrailHistory();
            ArrayList<Vec3> points = new ArrayList<Vec3>(history.size() + 1);
            points.add(head);
            points.addAll(history);
            int numPoints = points.size();
            int n = numPoints - 1;
            float fSpark = (float)Math.max(0.0, 1.0 - ((double)spark.getAge() + (double)partialTick) / (double)spark.getMaxAge());
            if (fSpark <= 0.0f) continue;
            boolean isBreakSpark = spark.isBreak();
            float headR = isBreakSpark ? 1.0f : 0.85f;
            float headG = isBreakSpark ? 0.3f : 0.98f;
            float f = headB = isBreakSpark ? 0.1f : 1.0f;
            if (n < 1) {
                headRel = head.subtract(camPos);
                EvilMortyShieldRenderer.renderSparkPoint(pose, consumer, headRel, camRight, camUp, 0.04f, headR, headG, headB, fSpark);
                continue;
            }
            for (int i = 0; i < n; ++i) {
                Vec3 ref;
                Vec3 p1;
                Vec3 p0 = (Vec3)points.get(i);
                Vec3 s = p0.subtract(p1 = (Vec3)points.get(i + 1));
                double sLenSqr = s.lengthSqr();
                if (sLenSqr < 1.0E-8) continue;
                Vec3 mid = p0.add(p1).scale(0.5);
                Vec3 c = mid.subtract(camPos);
                Vec3 w = s.cross(c);
                double wLen = w.length();
                if (wLen < 1.0E-6 && (wLen = (w = s.cross(ref = Math.abs(s.y) < 0.95 ? new Vec3(0.0, 1.0, 0.0) : new Vec3(1.0, 0.0, 0.0))).length()) < 1.0E-6) {
                    w = new Vec3(0.0, 1.0, 0.0);
                    wLen = 1.0;
                }
                w = w.scale(1.0 / wLen);
                double wi = Math.max(0.005, 0.04 * (1.0 - (double)i / (double)n));
                double wNext = Math.max(0.002, 0.04 * (1.0 - (double)(i + 1) / (double)n));
                float ai = (float)((1.0 - (double)i / (double)n) * (double)fSpark);
                float aNext = (float)((1.0 - (double)(i + 1) / (double)n) * (double)fSpark);
                float[] colI = EvilMortyShieldRenderer.getTrailColor((double)i / (double)n, isBreakSpark);
                float[] colNext = EvilMortyShieldRenderer.getTrailColor((double)(i + 1) / (double)n, isBreakSpark);
                double offXi = w.x * wi;
                double offYi = w.y * wi;
                double offZi = w.z * wi;
                double offXnext = w.x * wNext;
                double offYnext = w.y * wNext;
                double offZnext = w.z * wNext;
                float x0 = (float)(p0.x - camPos.x + offXi);
                float y0 = (float)(p0.y - camPos.y + offYi);
                float z0 = (float)(p0.z - camPos.z + offZi);
                float x1 = (float)(p0.x - camPos.x - offXi);
                float y1 = (float)(p0.y - camPos.y - offYi);
                float z1 = (float)(p0.z - camPos.z - offZi);
                float x2 = (float)(p1.x - camPos.x - offXnext);
                float y2 = (float)(p1.y - camPos.y - offYnext);
                float z2 = (float)(p1.z - camPos.z - offZnext);
                float x3 = (float)(p1.x - camPos.x + offXnext);
                float y3 = (float)(p1.y - camPos.y + offYnext);
                float z3 = (float)(p1.z - camPos.z + offZnext);
                consumer.addVertex(pose, x0, y0, z0).setColor(colI[0], colI[1], colI[2], ai);
                consumer.addVertex(pose, x1, y1, z1).setColor(colI[0], colI[1], colI[2], ai);
                consumer.addVertex(pose, x2, y2, z2).setColor(colNext[0], colNext[1], colNext[2], aNext);
                consumer.addVertex(pose, x3, y3, z3).setColor(colNext[0], colNext[1], colNext[2], aNext);
            }
            headRel = head.subtract(camPos);
            EvilMortyShieldRenderer.renderSparkPoint(pose, consumer, headRel, camRight, camUp, 0.04f, headR, headG, headB, fSpark);
        }
    }

    private static void renderSparkPoint(Matrix4f pose, VertexConsumer consumer, Vec3 posRel, Vector3f camRight, Vector3f camUp, float radius, float r, float g, float b, float a) {
        if (a <= 0.001f) {
            return;
        }
        float rx = camRight.x() * radius;
        float ry = camRight.y() * radius;
        float rz = camRight.z() * radius;
        float ux = camUp.x() * radius;
        float uy = camUp.y() * radius;
        float uz = camUp.z() * radius;
        float px = (float)posRel.x;
        float py = (float)posRel.y;
        float pz = (float)posRel.z;
        consumer.addVertex(pose, px - rx - ux, py - ry - uy, pz - rz - uz).setColor(r, g, b, a);
        consumer.addVertex(pose, px + rx - ux, py + ry - uy, pz + rz - uz).setColor(r, g, b, a);
        consumer.addVertex(pose, px + rx + ux, py + ry + uy, pz + rz + uz).setColor(r, g, b, a);
        consumer.addVertex(pose, px - rx + ux, py - ry + uy, pz - rz + uz).setColor(r, g, b, a);
    }

    private static float[] getTrailColor(double t) {
        return EvilMortyShieldRenderer.getTrailColor(t, false);
    }

    private static float[] getTrailColor(double t, boolean isBreak) {
        float b;
        float g;
        float r;
        if (isBreak) {
            float b2;
            float g2;
            float r2;
            if (t <= 0.5) {
                float f = (float)(t / 0.5);
                r2 = 1.0f;
                g2 = Mth.lerp((float)f, (float)0.45f, (float)0.25f);
                b2 = Mth.lerp((float)f, (float)0.15f, (float)0.05f);
            } else {
                float f = (float)((t - 0.5) / 0.5);
                r2 = Mth.lerp((float)f, (float)1.0f, (float)0.8f);
                g2 = Mth.lerp((float)f, (float)0.25f, (float)0.08f);
                b2 = Mth.lerp((float)f, (float)0.05f, (float)0.01f);
            }
            return new float[]{r2, g2, b2};
        }
        if (t <= 0.5) {
            float f = (float)(t / 0.5);
            r = Mth.lerp((float)f, (float)0.85f, (float)0.05f);
            g = Mth.lerp((float)f, (float)0.98f, (float)0.85f);
            b = 1.0f;
        } else {
            float f = (float)((t - 0.5) / 0.5);
            r = Mth.lerp((float)f, (float)0.05f, (float)0.0f);
            g = Mth.lerp((float)f, (float)0.85f, (float)0.6f);
            b = 1.0f;
        }
        return new float[]{r, g, b};
    }
}

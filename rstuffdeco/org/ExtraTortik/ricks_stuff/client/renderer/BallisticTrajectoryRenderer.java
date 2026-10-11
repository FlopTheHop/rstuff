/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.Camera
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent$Stage
 *  org.ExtraTortik.ricks_stuff.client.renderer.BallisticTrajectoryRenderer$1
 *  org.ExtraTortik.ricks_stuff.client.renderer.PortalRenderTypes
 *  org.ExtraTortik.ricks_stuff.combat.BallisticPredictionEngine
 *  org.ExtraTortik.ricks_stuff.combat.BallisticPredictionEngine$ThreatLevel
 *  org.ExtraTortik.ricks_stuff.combat.BallisticPredictionEngine$Trajectory
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.joml.Matrix4f
 */
package org.ExtraTortik.ricks_stuff.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.ExtraTortik.ricks_stuff.client.renderer.BallisticTrajectoryRenderer;
import org.ExtraTortik.ricks_stuff.client.renderer.PortalRenderTypes;
import org.ExtraTortik.ricks_stuff.combat.BallisticPredictionEngine;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.joml.Matrix4f;

@OnlyIn(value=Dist.CLIENT)
@EventBusSubscriber(modid="ricks_stuff", value={Dist.CLIENT})
public class BallisticTrajectoryRenderer {
    private static int warningAudioCooldown = 0;

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (mc.level == null) {
            return;
        }
        List trajectories = BallisticPredictionEngine.getActiveTrajectories();
        if (trajectories.isEmpty()) {
            return;
        }
        PoseStack poseStack = event.getPoseStack();
        if (poseStack == null) {
            return;
        }
        Camera camera = event.getCamera();
        Vec3 camPos = camera.getPosition();
        if (BallisticPredictionEngine.hasCriticalThreat() && warningAudioCooldown <= 0) {
            mc.player.playSound((SoundEvent)ModSounds.UI_DIAL.get(), 0.5f, 1.8f);
            warningAudioCooldown = 25;
        }
        if (warningAudioCooldown > 0) {
            --warningAudioCooldown;
        }
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        VertexConsumer consumer = bufferSource.getBuffer(PortalRenderTypes.TRAJECTORY_LINE);
        poseStack.pushPose();
        Matrix4f matrix = poseStack.last().pose();
        float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);
        float gameTime = ((float)mc.level.getGameTime() + partialTick) * 0.2f;
        Iterator iterator = trajectories.iterator();
        while (true) {
            int a;
            int b;
            int g;
            int r;
            if (!iterator.hasNext()) {
                poseStack.popPose();
                bufferSource.endBatch(PortalRenderTypes.TRAJECTORY_LINE);
                return;
            }
            BallisticPredictionEngine.Trajectory trajectory = (BallisticPredictionEngine.Trajectory)iterator.next();
            List points = trajectory.getPoints();
            if (points.size() < 2) continue;
            float ribbonWidth = switch (1.$SwitchMap$org$ExtraTortik$ricks_stuff$combat$BallisticPredictionEngine$ThreatLevel[trajectory.getThreatLevel().ordinal()]) {
                case 1 -> {
                    r = 255;
                    g = 26;
                    b = 0;
                    a = 240;
                    yield 0.08f;
                }
                case 2 -> {
                    r = 255;
                    g = 136;
                    b = 0;
                    a = 200;
                    yield 0.06f;
                }
                default -> {
                    r = 255;
                    g = 247;
                    b = 133;
                    a = 140;
                    yield 0.04f;
                }
            };
            for (int i = 0; i < points.size() - 1; ++i) {
                if ((i + (int)gameTime) % 3 == 0 && trajectory.getThreatLevel() != BallisticPredictionEngine.ThreatLevel.CRITICAL) continue;
                Vec3 p0 = (Vec3)points.get(i);
                Vec3 p1 = (Vec3)points.get(i + 1);
                BallisticTrajectoryRenderer.renderCameraFacingSegment(matrix, consumer, camPos, p0, p1, ribbonWidth, r, g, b, a);
            }
            Vec3 impact = trajectory.getImpactPoint();
            if (impact == null) continue;
            BallisticTrajectoryRenderer.renderImpactMarker(matrix, consumer, camPos, impact, ribbonWidth * 4.0f, r, g, b, a);
        }
    }

    private static void renderCameraFacingSegment(Matrix4f mat, VertexConsumer consumer, Vec3 camPos, Vec3 p0, Vec3 p1, float halfWidth, int r, int g, int b, int a) {
        double dy = p1.y - p0.y;
        double midZ = (p0.z + p1.z) * 0.5;
        double toCamZ = camPos.z - midZ;
        double dz = p1.z - p0.z;
        double midY = (p0.y + p1.y) * 0.5;
        double toCamY = camPos.y - midY;
        double px = dy * toCamZ - dz * toCamY;
        double midX = (p0.x + p1.x) * 0.5;
        double toCamX = camPos.x - midX;
        double dx = p1.x - p0.x;
        double py = dz * toCamX - dx * toCamZ;
        double pz = dx * toCamY - dy * toCamX;
        double len = Math.sqrt(px * px + py * py + pz * pz);
        if (len < 1.0E-5) {
            return;
        }
        px = px / len * (double)halfWidth;
        py = py / len * (double)halfWidth;
        pz = pz / len * (double)halfWidth;
        float x0A = (float)(p0.x - camPos.x - px);
        float y0A = (float)(p0.y - camPos.y - py);
        float z0A = (float)(p0.z - camPos.z - pz);
        float x0B = (float)(p0.x - camPos.x + px);
        float y0B = (float)(p0.y - camPos.y + py);
        float z0B = (float)(p0.z - camPos.z + pz);
        float x1B = (float)(p1.x - camPos.x + px);
        float y1B = (float)(p1.y - camPos.y + py);
        float z1B = (float)(p1.z - camPos.z + pz);
        float x1A = (float)(p1.x - camPos.x - px);
        float y1A = (float)(p1.y - camPos.y - py);
        float z1A = (float)(p1.z - camPos.z - pz);
        consumer.addVertex(mat, x0A, y0A, z0A).setColor(r, g, b, a);
        consumer.addVertex(mat, x0B, y0B, z0B).setColor(r, g, b, a);
        consumer.addVertex(mat, x1B, y1B, z1B).setColor(r, g, b, a);
        consumer.addVertex(mat, x1A, y1A, z1A).setColor(r, g, b, a);
    }

    private static void renderImpactMarker(Matrix4f mat, VertexConsumer consumer, Vec3 camPos, Vec3 impact, float radius, int r, int g, int b, int a) {
        float cx = (float)(impact.x - camPos.x);
        float cy = (float)(impact.y - camPos.y + 0.05);
        float cz = (float)(impact.z - camPos.z);
        consumer.addVertex(mat, cx - radius, cy, cz).setColor(r, g, b, a);
        consumer.addVertex(mat, cx, cy, cz - radius).setColor(r, g, b, a);
        consumer.addVertex(mat, cx + radius, cy, cz).setColor(r, g, b, a);
        consumer.addVertex(mat, cx, cy, cz + radius).setColor(r, g, b, a);
    }
}

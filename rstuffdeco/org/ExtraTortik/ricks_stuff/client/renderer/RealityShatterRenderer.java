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
 *  org.ExtraTortik.ricks_stuff.client.renderer.RealityShatterRenderer$MicroSparkData
 *  org.ExtraTortik.ricks_stuff.client.renderer.RealityShatterRenderer$ShardData
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
import org.ExtraTortik.ricks_stuff.client.renderer.RealityShatterRenderer;
import org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity;
import org.ExtraTortik.ricks_stuff.entity.WarpState;
import org.joml.Matrix4f;

public class RealityShatterRenderer {
    private static final int SHARD_COUNT = 44;
    private static final int MICRO_SPARK_COUNT = 28;
    private static final float DISC_FORWARD_Z = 6.0f;
    private static final float DISC_CENTER_Y = 1.28f;
    private static final ShardData[] SHARDS = new ShardData[44];
    private static final MicroSparkData[] MICRO_SPARKS = new MicroSparkData[28];

    private static void initShards() {
        int i = 0;
        while (i < 44) {
            float edgeB;
            float edgeG;
            float edgeR;
            int edgeColorType;
            float thickness;
            float scale;
            float fwdSpeed;
            float speed;
            float angle;
            float r;
            if (i < 12) {
                r = 0.35f + (float)i / 12.0f * 1.1f;
                angle = (float)i / 12.0f * ((float)Math.PI * 2) + (float)i * 0.618f;
                speed = 2.7f + (float)(i % 3) * 0.45f;
                fwdSpeed = 0.9f - (float)(i % 4) * 0.35f;
                scale = 0.48f + (float)(i % 3) * 0.12f;
                thickness = 0.045f;
                edgeColorType = i % 3;
            } else if (i < 28) {
                int idx = i - 12;
                r = 1.45f + (float)idx / 16.0f * 1.4f;
                angle = (float)idx / 16.0f * ((float)Math.PI * 2) + (float)idx * 0.43f;
                speed = 1.95f + (float)(idx % 4) * 0.35f;
                fwdSpeed = 0.45f - (float)(idx % 3) * 0.3f;
                scale = 0.78f + (float)(idx % 4) * 0.16f;
                thickness = 0.065f;
                edgeColorType = (i + 1) % 3;
            } else {
                int idx = i - 28;
                r = 2.9f + (float)idx / 16.0f * 1.8f;
                angle = (float)idx / 16.0f * ((float)Math.PI * 2) + (float)idx * 0.88f;
                speed = 1.35f + (float)(idx % 4) * 0.25f;
                fwdSpeed = 0.15f - (float)(idx % 2) * 0.2f;
                scale = 1.15f + (float)(idx % 4) * 0.22f;
                thickness = 0.085f;
                edgeColorType = (i + 2) % 3;
            }
            float x0 = r * (float)Math.cos(angle);
            float y0 = 1.28f + r * (float)Math.sin(angle) * 0.85f;
            float z0 = 6.0f + (float)Math.sin((double)i * 2.8) * 0.22f;
            float dx = x0;
            float dy = y0 - 1.28f;
            float dist = (float)Math.sqrt(dx * dx + dy * dy);
            float nx = dist > 0.001f ? dx / dist : 1.0f;
            float ny = dist > 0.001f ? dy / dist : 0.0f;
            float seedX = (float)Math.sin((double)i * 1.7) * 1.25f;
            float seedY = (float)Math.cos((double)i * 2.3) * 1.3f;
            float seedZ = (float)Math.sin((double)i * 3.1 + 0.5) * 1.15f;
            int vertCount = 3 + i % 3;
            float[][] verts2D = new float[vertCount][2];
            for (int v = 0; v < vertCount; ++v) {
                float va = (float)v / (float)vertCount * ((float)Math.PI * 2) + (float)v * 0.32f * (i % 2 == 0 ? 1.0f : -1.0f);
                float vr = scale * (0.65f + 0.35f * (float)Math.sin((double)v * 2.1 + (double)i));
                verts2D[v][0] = vr * (float)Math.cos(va);
                verts2D[v][1] = vr * (float)Math.sin(va);
            }
            float faceR = 1.0f;
            float faceG = 0.98f;
            float faceB = 0.68f;
            if (edgeColorType == 1) {
                faceR = 0.9f;
                faceG = 0.98f;
                faceB = 1.0f;
            } else if (edgeColorType == 2) {
                faceR = 1.0f;
                faceG = 0.92f;
                faceB = 0.98f;
            }
            if (edgeColorType == 0) {
                edgeR = 1.0f;
                edgeG = 0.9f;
                edgeB = 0.05f;
            } else if (edgeColorType == 1) {
                edgeR = 0.0f;
                edgeG = 0.94f;
                edgeB = 1.0f;
            } else {
                edgeR = 1.0f;
                edgeG = 0.05f;
                edgeB = 0.65f;
            }
            float edgeWidth = 0.055f + (float)(i % 3) * 0.015f;
            RealityShatterRenderer.SHARDS[i] = new ShardData(x0, y0, z0, nx, ny, speed, fwdSpeed, seedX, seedY, seedZ, verts2D, thickness, faceR, faceG, faceB, edgeR, edgeG, edgeB, edgeWidth);
            ++i;
        }
    }

    private static void initMicroSparks() {
        int i = 0;
        while (i < 28) {
            float bCol;
            float gCol;
            float rCol;
            float angle = (float)i / 28.0f * ((float)Math.PI * 2) + (float)i * 0.73f;
            float r = 0.5f + (float)Math.sin((double)i * 1.9) * 2.2f;
            float speed = 2.2f + (float)(i % 5) * 0.6f;
            float fwd = 0.5f - (float)(i % 3) * 0.4f;
            float x0 = r * (float)Math.cos(angle);
            float y0 = 1.28f + r * (float)Math.sin(angle) * 0.85f;
            float z0 = 6.0f + (float)Math.cos((double)i * 3.3) * 0.3f;
            float dx = x0;
            float dy = y0 - 1.28f;
            float dist = (float)Math.sqrt(dx * dx + dy * dy);
            float nx = dist > 0.001f ? dx / dist : 1.0f;
            float ny = dist > 0.001f ? dy / dist : 0.0f;
            float size = 0.08f + (float)(i % 4) * 0.04f;
            int type = i % 3;
            if (type == 0) {
                rCol = 1.0f;
                gCol = 0.95f;
                bCol = 0.2f;
            } else if (type == 1) {
                rCol = 0.2f;
                gCol = 1.0f;
                bCol = 1.0f;
            } else {
                rCol = 1.0f;
                gCol = 0.2f;
                bCol = 0.9f;
            }
            RealityShatterRenderer.MICRO_SPARKS[i] = new MicroSparkData(x0, y0, z0, nx, ny, speed, fwd, (float)Math.sin((double)i * 2.1) * 2.5f, (float)Math.cos((double)i * 1.7) * 2.5f, size, rCol, gCol, bCol);
            ++i;
        }
    }

    public static void renderRealityShatter(PoseStack poseStack, MultiBufferSource buffer, SpaceCruiserEntity ship, float partialTicks) {
        ShardData shard;
        int i;
        int maxDuration;
        if (ship.getWarpStateEnum() != WarpState.EXITING) {
            return;
        }
        int warpTimer = ship.getWarpTimer();
        float t = (float)warpTimer + partialTicks;
        float progress = Mth.clamp((float)(t / (float)(maxDuration = Math.max(1, WarpState.EXITING.getDurationTicks()))), (float)0.0f, (float)1.0f);
        if (progress >= 1.0f) {
            return;
        }
        float faceAlpha = 0.85f * (1.0f - progress) * (1.0f - progress * 0.45f);
        float edgeAlpha = Math.max(0.0f, 1.0f - progress * 0.82f);
        if (faceAlpha > 0.01f) {
            VertexConsumer glassConsumer = buffer.getBuffer(PortalRenderTypes.REALITY_GLASS_SHARD_TRANSLUCENT);
            for (i = 0; i < 44; ++i) {
                shard = SHARDS[i];
                RealityShatterRenderer.renderSingleShardFaces(poseStack, glassConsumer, shard, t, progress, faceAlpha);
            }
        }
        VertexConsumer edgeConsumer = buffer.getBuffer(PortalRenderTypes.REALITY_GLASS_SHARD);
        if (t < 16.0f) {
            RealityShatterRenderer.renderCenterShockwaveBurst(poseStack, edgeConsumer, t, progress);
        }
        if (edgeAlpha > 0.01f) {
            for (i = 0; i < 44; ++i) {
                shard = SHARDS[i];
                RealityShatterRenderer.renderSingleShardEdges(poseStack, edgeConsumer, shard, t, progress, edgeAlpha);
            }
        }
        RealityShatterRenderer.renderMicroSparks(poseStack, edgeConsumer, t, progress, edgeAlpha);
    }

    private static void renderSingleShardFaces(PoseStack poseStack, VertexConsumer glassConsumer, ShardData shard, float t, float progress, float faceAlpha) {
        if (faceAlpha <= 0.01f) {
            return;
        }
        float radDist = shard.radialSpeed * (t * 0.24f + t * t * 0.014f);
        float fwdDist = shard.forwardSpeed * t * 0.12f - t * 0.08f;
        float curX = shard.initX + shard.radDirX * radDist;
        float curY = shard.initY + shard.radDirY * radDist;
        float curZ = shard.initZ + fwdDist;
        float pitchRot = shard.seedX * t * 25.0f;
        float yawRot = shard.seedY * t * 25.0f;
        float rollRot = shard.seedZ * t * 25.0f;
        poseStack.pushPose();
        poseStack.translate(curX, curY, curZ);
        poseStack.mulPose(Axis.XP.rotationDegrees(pitchRot));
        poseStack.mulPose(Axis.YP.rotationDegrees(yawRot));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rollRot));
        Matrix4f pose = poseStack.last().pose();
        float h = shard.thickness * 0.5f;
        float[][] verts = shard.verts2D;
        int nVerts = verts.length;
        float r = shard.faceR;
        float g = shard.faceG;
        float b = shard.faceB;
        float a = faceAlpha;
        RealityShatterRenderer.renderPolygonFace(pose, glassConsumer, verts, nVerts, h, r, g, b, a, false);
        RealityShatterRenderer.renderPolygonFace(pose, glassConsumer, verts, nVerts, -h, r, g, b, a, true);
        int j = 0;
        while (true) {
            if (j >= nVerts) {
                poseStack.popPose();
                return;
            }
            int next = (j + 1) % nVerts;
            float x0 = verts[j][0];
            float y0 = verts[j][1];
            float x1 = verts[next][0];
            float y1 = verts[next][1];
            glassConsumer.addVertex(pose, x0, y0, h).setColor(r, g, b, a);
            glassConsumer.addVertex(pose, x1, y1, h).setColor(r, g, b, a);
            glassConsumer.addVertex(pose, x1, y1, -h).setColor(r, g, b, a);
            glassConsumer.addVertex(pose, x0, y0, -h).setColor(r, g, b, a);
            ++j;
        }
    }

    private static void renderSingleShardEdges(PoseStack poseStack, VertexConsumer edgeConsumer, ShardData shard, float t, float progress, float edgeAlpha) {
        if (edgeAlpha <= 0.01f) {
            return;
        }
        float radDist = shard.radialSpeed * (t * 0.24f + t * t * 0.014f);
        float fwdDist = shard.forwardSpeed * t * 0.12f - t * 0.08f;
        float curX = shard.initX + shard.radDirX * radDist;
        float curY = shard.initY + shard.radDirY * radDist;
        float curZ = shard.initZ + fwdDist;
        float pitchRot = shard.seedX * t * 25.0f;
        float yawRot = shard.seedY * t * 25.0f;
        float rollRot = shard.seedZ * t * 25.0f;
        poseStack.pushPose();
        poseStack.translate(curX, curY, curZ);
        poseStack.mulPose(Axis.XP.rotationDegrees(pitchRot));
        poseStack.mulPose(Axis.YP.rotationDegrees(yawRot));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rollRot));
        Matrix4f pose = poseStack.last().pose();
        float h = shard.thickness * 0.5f;
        float[][] verts = shard.verts2D;
        int nVerts = verts.length;
        float er = shard.edgeR;
        float eg = shard.edgeG;
        float eb = shard.edgeB;
        float ea = edgeAlpha;
        float w = shard.edgeWidth;
        RealityShatterRenderer.renderGlowingPerimeterRibbons(pose, edgeConsumer, verts, nVerts, h, w, er, eg, eb, ea);
        RealityShatterRenderer.renderGlowingPerimeterRibbons(pose, edgeConsumer, verts, nVerts, -h, w, er, eg, eb, ea);
        RealityShatterRenderer.renderWhiteCoreEdges(pose, edgeConsumer, verts, nVerts, h, w * 0.45f, ea);
        RealityShatterRenderer.renderWhiteCoreEdges(pose, edgeConsumer, verts, nVerts, -h, w * 0.45f, ea);
        poseStack.popPose();
    }

    private static void renderPolygonFace(Matrix4f pose, VertexConsumer consumer, float[][] verts, int nVerts, float zOffset, float r, float g, float b, float a, boolean reverse) {
        if (nVerts == 3) {
            int i0 = reverse ? 2 : 0;
            int i1 = 1;
            int i2 = reverse ? 0 : 2;
            consumer.addVertex(pose, verts[i0][0], verts[i0][1], zOffset).setColor(r, g, b, a);
            consumer.addVertex(pose, verts[i1][0], verts[i1][1], zOffset).setColor(r, g, b, a);
            consumer.addVertex(pose, verts[i2][0], verts[i2][1], zOffset).setColor(r, g, b, a);
            consumer.addVertex(pose, verts[i2][0], verts[i2][1], zOffset).setColor(r, g, b, a);
        } else if (nVerts == 4) {
            int i0 = reverse ? 3 : 0;
            int i1 = reverse ? 2 : 1;
            int i2 = reverse ? 1 : 2;
            int i3 = reverse ? 0 : 3;
            consumer.addVertex(pose, verts[i0][0], verts[i0][1], zOffset).setColor(r, g, b, a);
            consumer.addVertex(pose, verts[i1][0], verts[i1][1], zOffset).setColor(r, g, b, a);
            consumer.addVertex(pose, verts[i2][0], verts[i2][1], zOffset).setColor(r, g, b, a);
            consumer.addVertex(pose, verts[i3][0], verts[i3][1], zOffset).setColor(r, g, b, a);
        } else if (!reverse) {
            consumer.addVertex(pose, verts[0][0], verts[0][1], zOffset).setColor(r, g, b, a);
            consumer.addVertex(pose, verts[1][0], verts[1][1], zOffset).setColor(r, g, b, a);
            consumer.addVertex(pose, verts[2][0], verts[2][1], zOffset).setColor(r, g, b, a);
            consumer.addVertex(pose, verts[3][0], verts[3][1], zOffset).setColor(r, g, b, a);
            consumer.addVertex(pose, verts[0][0], verts[0][1], zOffset).setColor(r, g, b, a);
            consumer.addVertex(pose, verts[3][0], verts[3][1], zOffset).setColor(r, g, b, a);
            consumer.addVertex(pose, verts[4][0], verts[4][1], zOffset).setColor(r, g, b, a);
            consumer.addVertex(pose, verts[4][0], verts[4][1], zOffset).setColor(r, g, b, a);
        } else {
            consumer.addVertex(pose, verts[3][0], verts[3][1], zOffset).setColor(r, g, b, a);
            consumer.addVertex(pose, verts[2][0], verts[2][1], zOffset).setColor(r, g, b, a);
            consumer.addVertex(pose, verts[1][0], verts[1][1], zOffset).setColor(r, g, b, a);
            consumer.addVertex(pose, verts[0][0], verts[0][1], zOffset).setColor(r, g, b, a);
            consumer.addVertex(pose, verts[4][0], verts[4][1], zOffset).setColor(r, g, b, a);
            consumer.addVertex(pose, verts[4][0], verts[4][1], zOffset).setColor(r, g, b, a);
            consumer.addVertex(pose, verts[3][0], verts[3][1], zOffset).setColor(r, g, b, a);
            consumer.addVertex(pose, verts[0][0], verts[0][1], zOffset).setColor(r, g, b, a);
        }
    }

    private static void renderGlowingPerimeterRibbons(Matrix4f pose, VertexConsumer consumer, float[][] verts, int nVerts, float zOffset, float width, float r, float g, float b, float a) {
        int j = 0;
        while (j < nVerts) {
            int next = (j + 1) % nVerts;
            float xb = verts[next][0];
            float xa = verts[j][0];
            float dx = xb - xa;
            float yb = verts[next][1];
            float ya = verts[j][1];
            float dy = yb - ya;
            float len = (float)Math.sqrt(dx * dx + dy * dy);
            if (!(len < 1.0E-4f)) {
                float mx = (xa + xb) * 0.5f;
                float nx = dy / len;
                float my = (ya + yb) * 0.5f;
                float ny = -dx / len;
                if (mx * nx + my * ny < 0.0f) {
                    nx = -nx;
                    ny = -ny;
                }
                consumer.addVertex(pose, xa, ya, zOffset).setColor(r, g, b, a);
                consumer.addVertex(pose, xb, yb, zOffset).setColor(r, g, b, a);
                consumer.addVertex(pose, xb + nx * width, yb + ny * width, zOffset).setColor(r, g, b, a * 0.25f);
                consumer.addVertex(pose, xa + nx * width, ya + ny * width, zOffset).setColor(r, g, b, a * 0.25f);
            }
            ++j;
        }
    }

    private static void renderWhiteCoreEdges(Matrix4f pose, VertexConsumer consumer, float[][] verts, int nVerts, float zOffset, float width, float a) {
        int j = 0;
        while (j < nVerts) {
            int next = (j + 1) % nVerts;
            float xb = verts[next][0];
            float xa = verts[j][0];
            float dx = xb - xa;
            float yb = verts[next][1];
            float ya = verts[j][1];
            float dy = yb - ya;
            float len = (float)Math.sqrt(dx * dx + dy * dy);
            if (!(len < 1.0E-4f)) {
                float nx = dy / len;
                float ny = -dx / len;
                consumer.addVertex(pose, xa - nx * width * 0.5f, ya - ny * width * 0.5f, zOffset).setColor(1.0f, 1.0f, 1.0f, a);
                consumer.addVertex(pose, xb - nx * width * 0.5f, yb - ny * width * 0.5f, zOffset).setColor(1.0f, 1.0f, 1.0f, a);
                consumer.addVertex(pose, xb + nx * width * 0.5f, yb + ny * width * 0.5f, zOffset).setColor(1.0f, 1.0f, 1.0f, a);
                consumer.addVertex(pose, xa + nx * width * 0.5f, ya + ny * width * 0.5f, zOffset).setColor(1.0f, 1.0f, 1.0f, a);
            }
            ++j;
        }
    }

    private static void renderCenterShockwaveBurst(PoseStack poseStack, VertexConsumer consumer, float t, float progress) {
        float burstAlpha = Math.max(0.0f, 1.0f - t / 15.0f);
        if (burstAlpha <= 0.01f) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.0f, 1.28f, 6.0f);
        Matrix4f pose = poseStack.last().pose();
        int ringSegments = 24;
        float ringRadius = 0.6f + t * 0.85f;
        float ringThickness = 0.28f + t * 0.05f;
        float rInner = Math.max(0.0f, ringRadius - ringThickness * 0.5f);
        float rOuter = ringRadius + ringThickness * 0.5f;
        for (int s = 0; s < ringSegments; ++s) {
            float a0 = (float)s / (float)ringSegments * ((float)Math.PI * 2);
            float a1 = (float)(s + 1) / (float)ringSegments * ((float)Math.PI * 2);
            float cos0 = (float)Math.cos(a0);
            float sin0 = (float)Math.sin(a0);
            float cos1 = (float)Math.cos(a1);
            float sin1 = (float)Math.sin(a1);
            float x0_in = rInner * cos0;
            float y0_in = rInner * sin0 * 0.85f;
            float x1_in = rInner * cos1;
            float y1_in = rInner * sin1 * 0.85f;
            float x0_out = rOuter * cos0;
            float y0_out = rOuter * sin0 * 0.85f;
            float x1_out = rOuter * cos1;
            float y1_out = rOuter * sin1 * 0.85f;
            float r = s % 2 == 0 ? 1.0f : 0.0f;
            float g = s % 2 == 0 ? 0.92f : 0.95f;
            float b = s % 2 == 0 ? 0.15f : 1.0f;
            consumer.addVertex(pose, x0_in, y0_in, 0.0f).setColor(1.0f, 1.0f, 1.0f, burstAlpha);
            consumer.addVertex(pose, x1_in, y1_in, 0.0f).setColor(1.0f, 1.0f, 1.0f, burstAlpha);
            consumer.addVertex(pose, x1_out, y1_out, 0.0f).setColor(r, g, b, burstAlpha * 0.4f);
            consumer.addVertex(pose, x0_out, y0_out, 0.0f).setColor(r, g, b, burstAlpha * 0.4f);
        }
        int rayCount = 12;
        int k = 0;
        while (true) {
            float rayG;
            float rayR;
            if (k >= rayCount) {
                poseStack.popPose();
                return;
            }
            float rayAngle = (float)k / (float)rayCount * ((float)Math.PI * 2) + (float)k * 0.25f;
            float rayLength = (1.5f + t * 0.9f) * (0.8f + (float)(k % 3) * 0.3f);
            float rayWidth = 0.08f;
            float cos = (float)Math.cos(rayAngle);
            float sin = (float)Math.sin(rayAngle);
            float px = -sin * rayWidth;
            float py = cos * rayWidth;
            float xEnd = cos * rayLength;
            float yEnd = sin * rayLength * 0.85f;
            float f = k % 3 == 0 ? 1.0f : (rayR = k % 3 == 1 ? 0.0f : 1.0f);
            float f2 = k % 3 == 0 ? 0.9f : (rayG = k % 3 == 1 ? 0.95f : 0.1f);
            float rayB = k % 3 == 0 ? 0.1f : (k % 3 == 1 ? 1.0f : 0.8f);
            consumer.addVertex(pose, -px, -py, 0.0f).setColor(1.0f, 1.0f, 1.0f, burstAlpha);
            consumer.addVertex(pose, px, py, 0.0f).setColor(1.0f, 1.0f, 1.0f, burstAlpha);
            consumer.addVertex(pose, xEnd + px * 0.2f, yEnd + py * 0.2f, 0.0f).setColor(rayR, rayG, rayB, 0.0f);
            consumer.addVertex(pose, xEnd - px * 0.2f, yEnd - py * 0.2f, 0.0f).setColor(rayR, rayG, rayB, 0.0f);
            ++k;
        }
    }

    private static void renderMicroSparks(PoseStack poseStack, VertexConsumer consumer, float t, float progress, float edgeAlpha) {
        if (edgeAlpha <= 0.01f) {
            return;
        }
        int i = 0;
        while (i < 28) {
            MicroSparkData spark = MICRO_SPARKS[i];
            float radDist = spark.speed * (t * 0.3f + t * t * 0.016f);
            float fwdDist = spark.fwdSpeed * t * 0.15f - t * 0.1f;
            float curX = spark.initX + spark.radDirX * radDist;
            float curY = spark.initY + spark.radDirY * radDist;
            float curZ = spark.initZ + fwdDist;
            float rotX = spark.rotSpeedX * t * 30.0f;
            float rotY = spark.rotSpeedY * t * 30.0f;
            poseStack.pushPose();
            poseStack.translate(curX, curY, curZ);
            poseStack.mulPose(Axis.XP.rotationDegrees(rotX));
            poseStack.mulPose(Axis.YP.rotationDegrees(rotY));
            Matrix4f pose = poseStack.last().pose();
            float s = spark.size;
            consumer.addVertex(pose, 0.0f, s, 0.0f).setColor(1.0f, 1.0f, 1.0f, edgeAlpha);
            consumer.addVertex(pose, s * 0.7f, 0.0f, 0.0f).setColor(spark.r, spark.g, spark.b, edgeAlpha * 0.6f);
            consumer.addVertex(pose, 0.0f, -s, 0.0f).setColor(1.0f, 1.0f, 1.0f, edgeAlpha);
            consumer.addVertex(pose, -s * 0.7f, 0.0f, 0.0f).setColor(spark.r, spark.g, spark.b, edgeAlpha * 0.6f);
            poseStack.popPose();
            ++i;
        }
    }

    static {
        RealityShatterRenderer.initShards();
        RealityShatterRenderer.initMicroSparks();
    }
}

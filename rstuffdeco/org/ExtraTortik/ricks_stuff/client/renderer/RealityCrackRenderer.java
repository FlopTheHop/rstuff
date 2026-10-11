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
 *  org.ExtraTortik.ricks_stuff.client.renderer.RealityCrackRenderer$BranchData
 *  org.ExtraTortik.ricks_stuff.client.renderer.RealityCrackRenderer$ShardData
 *  org.ExtraTortik.ricks_stuff.client.renderer.RealityCrackRenderer$SubBranchData
 *  org.ExtraTortik.ricks_stuff.client.renderer.RealityCrackRenderer$VacuumSparkData
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
import org.ExtraTortik.ricks_stuff.client.renderer.RealityCrackRenderer;
import org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity;
import org.ExtraTortik.ricks_stuff.entity.WarpState;
import org.joml.Matrix4f;

public class RealityCrackRenderer {
    private static final int PRIMARY_BRANCH_COUNT = 6;
    private static final int SEGMENTS_PER_BRANCH = 10;
    private static final int SHARD_COUNT = 36;
    private static final int APERTURE_VERTICES = 28;
    private static final int VACUUM_SPARK_COUNT = 28;
    private static final int TUNNEL_DEPTH_RINGS = 6;
    private static final float RIFT_FORWARD_Z = 28.0f;
    private static final float RIFT_CENTER_Y = 1.3f;
    private static final float MAX_RIFT_RADIUS = 9.5f;
    private static final float MAX_APERTURE_RADIUS = 5.2f;
    private static final float[] TUNNEL_RING_DEPTHS = new float[]{0.0f, 3.0f, 7.5f, 13.0f, 20.0f, 28.0f};
    private static final float[] TUNNEL_RING_RADIUS_SCALES = new float[]{1.0f, 0.98f, 1.1f, 1.25f, 1.45f, 1.7f};
    private static final ShardData[] BAKED_SHARDS = new ShardData[36];
    private static final BranchData[] BAKED_BRANCHES = new BranchData[6];
    private static final float[][] BAKED_APERTURE_CONTOUR = new float[28][2];
    private static final VacuumSparkData[] BAKED_VACUUM_SPARKS = new VacuumSparkData[28];
    private static final float[][][] SCRATCH_TUNNEL_RINGS = new float[6][28][3];
    private static final float[][] HYPERSPACE_PALETTE = new float[][]{{0.0f, 1.0f, 0.933f}, {1.0f, 0.0f, 0.667f}, {0.224f, 1.0f, 0.078f}, {1.0f, 1.0f, 0.0f}, {0.478f, 0.0f, 1.0f}};

    private static void initFractureTree() {
        int b = 0;
        while (b < 6) {
            float baseAngle = (float)b / 6.0f * ((float)Math.PI * 2) + RealityCrackRenderer.hashSigned(b, 101) * 0.28f;
            float[][] points = new float[11][2];
            float[] activationThresholds = new float[10];
            points[0][0] = 0.0f;
            points[0][1] = 0.0f;
            float curX = 0.0f;
            float curY = 0.0f;
            float angleAccum = baseAngle;
            float totalLength = 9.5f * (0.8f + RealityCrackRenderer.hash(b, 102) * 0.35f);
            float segLen = totalLength / 10.0f;
            SubBranchData[] subBranches = new SubBranchData[3];
            int subCount = 0;
            for (int s = 0; s < 10; ++s) {
                float segProg = (float)(s + 1) / 10.0f;
                activationThresholds[s] = (float)Math.pow(segProg, 1.35) * 0.92f;
                float turnAngle = RealityCrackRenderer.hashSigned(b * 31 + s, 201) * 0.42f;
                float nextX = curX + (float)Math.cos(angleAccum += turnAngle) * segLen;
                float nextY = curY + (float)Math.sin(angleAccum) * segLen;
                points[s + 1][0] = nextX;
                points[s + 1][1] = nextY;
                if ((s == 2 || s == 5 || s == 7) && subCount < subBranches.length) {
                    float forkSign = subCount % 2 == 0 ? 1.0f : -1.0f;
                    float forkAngle = angleAccum + forkSign * (float)Math.toRadians(36.0 + (double)RealityCrackRenderer.hash(b * 10 + s, 301) * 22.0);
                    int subSegCount = 4;
                    float[][] subPts = new float[subSegCount + 1][2];
                    subPts[0][0] = nextX;
                    subPts[0][1] = nextY;
                    float subCurX = nextX;
                    float subCurY = nextY;
                    float subAngle = forkAngle;
                    float subSegLen = segLen * 0.7f;
                    for (int ss = 0; ss < subSegCount; ++ss) {
                        float subNextX = subCurX + (float)Math.cos(subAngle += RealityCrackRenderer.hashSigned(b * 50 + s * 5 + ss, 401) * 0.35f) * subSegLen;
                        float subNextY = subCurY + (float)Math.sin(subAngle) * subSegLen;
                        subPts[ss + 1][0] = subNextX;
                        subPts[ss + 1][1] = subNextY;
                        subCurX = subNextX;
                        subCurY = subNextY;
                    }
                    float subThreshold = Math.min(0.98f, activationThresholds[s] + 0.08f);
                    subBranches[subCount++] = new SubBranchData(subPts, subThreshold);
                }
                curX = nextX;
                curY = nextY;
            }
            SubBranchData[] finalSubs = new SubBranchData[subCount];
            System.arraycopy(subBranches, 0, finalSubs, 0, subCount);
            RealityCrackRenderer.BAKED_BRANCHES[b] = new BranchData(points, activationThresholds, finalSubs);
            ++b;
        }
    }

    private static void initVoronoiShards() {
        int i = 0;
        while (i < 36) {
            float edgeB;
            float edgeG;
            float edgeR;
            int edgeColorType;
            float thickness;
            float fwdSpeed;
            float speed;
            float angle;
            float r;
            if (i < 12) {
                r = 0.35f + (float)i / 12.0f * 1.35f;
                angle = (float)i / 12.0f * ((float)Math.PI * 2) + (float)i * 0.48f;
                speed = 4.2f + (float)(i % 4) * 0.65f;
                fwdSpeed = (i % 2 == 0 ? 1.2f : -1.0f) * (0.9f + (float)(i % 3) * 0.4f);
                thickness = 0.065f;
                edgeColorType = i % 3;
            } else if (i < 24) {
                int idx = i - 12;
                r = 1.85f + (float)idx / 12.0f * 1.55f;
                angle = (float)idx / 12.0f * ((float)Math.PI * 2) + (float)idx * 0.35f;
                speed = 2.8f + (float)(idx % 3) * 0.5f;
                fwdSpeed = (idx % 2 == 0 ? 0.7f : -0.7f) * (0.6f + (float)(idx % 3) * 0.3f);
                thickness = 0.085f;
                edgeColorType = (i + 1) % 3;
            } else {
                int idx = i - 24;
                r = 3.45f + (float)idx / 12.0f * 1.65f;
                angle = (float)idx / 12.0f * ((float)Math.PI * 2) + (float)idx * 0.62f;
                speed = 1.7f + (float)(idx % 4) * 0.35f;
                fwdSpeed = (idx % 2 == 0 ? 0.4f : -0.4f) * 0.45f;
                thickness = 0.105f;
                edgeColorType = (i + 2) % 3;
            }
            float cx0 = r * (float)Math.cos(angle);
            float cy0 = r * (float)Math.sin(angle) * 0.92f;
            float cz0 = 0.0f;
            float dist = (float)Math.sqrt(cx0 * cx0 + cy0 * cy0);
            float nx = dist > 0.001f ? cx0 / dist : 1.0f;
            float ny = dist > 0.001f ? cy0 / dist : 0.0f;
            float seedX = (float)Math.sin((double)i * 1.8 + 0.3) * 1.6f;
            float seedY = (float)Math.cos((double)i * 2.5 + 0.8) * 1.55f;
            float seedZ = (float)Math.sin((double)i * 3.2 + 1.1) * 1.45f;
            int vertCount = 3 + i % 3;
            float[][] verts2D = new float[vertCount][2];
            float shardScale = 0.55f + r / 5.2f * 0.6f;
            for (int v = 0; v < vertCount; ++v) {
                float va = (float)v / (float)vertCount * ((float)Math.PI * 2) + (float)v * 0.28f * (i % 2 == 0 ? 1.0f : -1.0f);
                float vr = shardScale * (0.65f + 0.35f * (float)Math.sin((double)v * 2.4 + (double)i * 1.7));
                verts2D[v][0] = vr * (float)Math.cos(va);
                verts2D[v][1] = vr * (float)Math.sin(va);
            }
            float faceR = 0.82f;
            float faceG = 0.96f;
            float faceB = 1.0f;
            if (edgeColorType == 0) {
                edgeR = 0.0f;
                edgeG = 1.0f;
                edgeB = 0.93f;
            } else if (edgeColorType == 1) {
                edgeR = 0.22f;
                edgeG = 1.0f;
                edgeB = 0.08f;
            } else {
                edgeR = 1.0f;
                edgeG = 0.0f;
                edgeB = 0.67f;
            }
            float edgeWidth = 0.045f + thickness * 0.2f;
            RealityCrackRenderer.BAKED_SHARDS[i] = new ShardData(cx0, cy0, cz0, nx, ny, speed, fwdSpeed, seedX, seedY, seedZ, verts2D, thickness, faceR, faceG, faceB, edgeR, edgeG, edgeB, edgeWidth);
            ++i;
        }
    }

    private static void initApertureContour() {
        int i = 0;
        while (i < 28) {
            float angle = (float)((double)i * 2.0 * Math.PI / 28.0);
            float jaggedRadius = 5.2f * (0.86f + 0.14f * (float)Math.sin((double)i * 3.7 + 1.2) + 0.08f * (float)Math.cos((double)i * 7.1));
            RealityCrackRenderer.BAKED_APERTURE_CONTOUR[i][0] = (float)Math.cos(angle) * jaggedRadius;
            RealityCrackRenderer.BAKED_APERTURE_CONTOUR[i][1] = (float)Math.sin(angle) * jaggedRadius * 0.92f;
            ++i;
        }
    }

    private static void initVacuumSparks() {
        int i = 0;
        while (i < 28) {
            float angle = (float)i / 28.0f * ((float)Math.PI * 2) + (float)i * 0.618f;
            float startRadius = 4.0f + (float)(i % 6) * 1.1f;
            float z0 = ((float)(i % 4) - 1.5f) * 0.4f;
            float speed = 6.0f + (float)(i % 3) * 1.5f;
            float length = 0.85f + (float)(i % 4) * 0.25f;
            int colorType = i % 3;
            RealityCrackRenderer.BAKED_VACUUM_SPARKS[i] = new VacuumSparkData(angle, startRadius, z0, speed, length, colorType);
            ++i;
        }
    }

    public static void renderRealityCrack(PoseStack poseStack, MultiBufferSource buffer, SpaceCruiserEntity ship, float partialTicks) {
        float apertureEase;
        WarpState warpState = ship.getWarpStateEnum();
        if (warpState == WarpState.IDLE) {
            return;
        }
        int warpTimer = ship.getWarpTimer();
        float timer = (float)warpTimer + partialTicks;
        float crackProgress = 0.0f;
        float shatterProgress = 0.0f;
        float alpha = 1.0f;
        float riftZ = 28.0f;
        float enterProgress = 0.0f;
        if (warpState == WarpState.CHARGING) {
            float chargeDuration = Math.max(1, WarpState.CHARGING.getDurationTicks());
            if (timer < 25.0f) {
                return;
            }
            p = (timer - 25.0f) / (chargeDuration - 25.0f);
            crackProgress = (float)Math.pow(p, 2.2) * 0.4f;
            shatterProgress = 0.0f;
            alpha = p * 0.9f;
            riftZ = 28.0f;
        } else if (warpState == WarpState.RIFT_OPENING) {
            float openDuration = Math.max(1, WarpState.RIFT_OPENING.getDurationTicks());
            p = Math.min(1.0f, timer / openDuration);
            if (p <= 0.58f) {
                float crackP = p / 0.58f;
                float ease = 1.0f - (float)Math.pow(1.0f - crackP, 2.5);
                crackProgress = 0.4f + 0.6f * ease;
                shatterProgress = 0.0f;
                alpha = 1.0f;
            } else {
                crackProgress = 1.0f;
                shatterProgress = (p - 0.58f) / 0.42f;
                alpha = 1.0f;
            }
            riftZ = 28.0f;
        } else {
            if (warpState != WarpState.ENTERING) return;
            float enterDuration = Math.max(1, WarpState.ENTERING.getDurationTicks());
            enterProgress = Math.min(1.0f, timer / enterDuration);
            crackProgress = 1.0f;
            shatterProgress = 1.0f;
            float enterEase = (float)Math.pow(enterProgress, 1.5);
            riftZ = 28.0f - enterEase * 30.0f;
            if (enterProgress < 0.65f) {
                alpha = 1.0f;
            } else {
                float implodeP = (enterProgress - 0.65f) / 0.35f;
                alpha = Math.max(0.0f, 1.0f - (float)Math.pow(implodeP, 2.0));
            }
        }
        if (alpha <= 0.005f) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.0, (double)1.3f, (double)riftZ);
        Matrix4f pose = poseStack.last().pose();
        float time = ((float)ship.tickCount + partialTicks) * 0.45f;
        float apertureRadiusScale = apertureEase = shatterProgress > 0.0f ? 1.0f - (float)Math.pow(1.0f - shatterProgress, 3.0) : 0.0f;
        if (apertureRadiusScale > 0.05f) {
            RealityCrackRenderer.updateScratchTunnelRings(time, apertureRadiusScale);
        }
        if (apertureRadiusScale > 0.05f) {
            VertexConsumer tunnelConsumer = buffer.getBuffer(PortalRenderTypes.WARP_TUNNEL);
            RealityCrackRenderer.renderHyperspaceApertureTunnel(pose, tunnelConsumer, time, alpha);
        }
        VertexConsumer glowConsumer = buffer.getBuffer(PortalRenderTypes.REALITY_CRACK);
        if (apertureRadiusScale > 0.05f) {
            RealityCrackRenderer.renderApertureRim(pose, glowConsumer, time, alpha, false);
        }
        RealityCrackRenderer.renderPlanarFractureTree(pose, glowConsumer, time, crackProgress, alpha, apertureRadiusScale, false);
        if (crackProgress > 0.3f) {
            RealityCrackRenderer.renderFractureElectricalArcs(pose, glowConsumer, time, crackProgress, alpha, apertureRadiusScale, false);
        }
        if (warpState == WarpState.ENTERING && enterProgress >= 0.4f) {
            RealityCrackRenderer.renderVacuumSparks(pose, glowConsumer, time, enterProgress, alpha, false);
        }
        VertexConsumer coreConsumer = buffer.getBuffer(PortalRenderTypes.REALITY_CRACK_CORE);
        if (apertureRadiusScale > 0.05f) {
            RealityCrackRenderer.renderTunnelEndCap(pose, coreConsumer, time, alpha);
            RealityCrackRenderer.renderApertureRim(pose, coreConsumer, time, alpha, true);
        }
        RealityCrackRenderer.renderPlanarFractureTree(pose, coreConsumer, time, crackProgress, alpha, apertureRadiusScale, true);
        if (crackProgress > 0.3f) {
            RealityCrackRenderer.renderFractureElectricalArcs(pose, coreConsumer, time, crackProgress, alpha, apertureRadiusScale, true);
        }
        if (warpState == WarpState.ENTERING && enterProgress >= 0.4f) {
            RealityCrackRenderer.renderVacuumSparks(pose, coreConsumer, time, enterProgress, alpha, true);
        }
        if (crackProgress > 0.05f) {
            VertexConsumer shardFaceConsumer = buffer.getBuffer(PortalRenderTypes.REALITY_GLASS_SHARD_TRANSLUCENT);
            RealityCrackRenderer.renderPlanarAndShatteredShardFaces(poseStack, shardFaceConsumer, time, crackProgress, shatterProgress, enterProgress, alpha);
        }
        if (crackProgress > 0.05f) {
            VertexConsumer shardEdgeConsumer = buffer.getBuffer(PortalRenderTypes.REALITY_GLASS_SHARD);
            RealityCrackRenderer.renderPlanarAndShatteredShardEdges(poseStack, shardEdgeConsumer, time, crackProgress, shatterProgress, enterProgress, alpha);
        }
        poseStack.popPose();
    }

    private static void updateScratchTunnelRings(float time, float apertureScale) {
        int d = 0;
        while (d < 6) {
            float z = TUNNEL_RING_DEPTHS[d];
            float rScale = TUNNEL_RING_RADIUS_SCALES[d] * apertureScale;
            float twist = d == 0 ? 0.0f : time * (3.0f - (float)d * 0.35f) + (float)d * 0.4f;
            for (int s = 0; s < 28; ++s) {
                float baseX = BAKED_APERTURE_CONTOUR[s][0] * rScale;
                float baseY = BAKED_APERTURE_CONTOUR[s][1] * rScale;
                float cosT = (float)Math.cos(twist);
                float sinT = (float)Math.sin(twist);
                float pulse = 1.0f + 0.08f * (float)Math.sin((float)s * 0.8f + time * 3.0f + (float)d);
                float rotX = (baseX * cosT - baseY * sinT) * pulse;
                float rotY = (baseX * sinT + baseY * cosT) * pulse;
                RealityCrackRenderer.SCRATCH_TUNNEL_RINGS[d][s][0] = rotX;
                RealityCrackRenderer.SCRATCH_TUNNEL_RINGS[d][s][1] = rotY;
                RealityCrackRenderer.SCRATCH_TUNNEL_RINGS[d][s][2] = z;
            }
            ++d;
        }
    }

    private static void renderHyperspaceApertureTunnel(Matrix4f pose, VertexConsumer tunnelConsumer, float time, float alpha) {
        int d = 0;
        while (d < 5) {
            int nextD = d + 1;
            for (int s = 0; s < 28; ++s) {
                int nextS = (s + 1) % 28;
                int colIdxA = (s + d * 2 + (int)(time * 1.8f)) % HYPERSPACE_PALETTE.length;
                int colIdxB = (colIdxA + 1) % HYPERSPACE_PALETTE.length;
                float[] colA = HYPERSPACE_PALETTE[colIdxA];
                float[] colB = HYPERSPACE_PALETTE[colIdxB];
                float depthFade = 1.0f - (float)d / 6.0f * 0.3f;
                float quadAlpha = Math.min(1.0f, 0.95f * alpha * depthFade);
                tunnelConsumer.addVertex(pose, SCRATCH_TUNNEL_RINGS[d][s][0], SCRATCH_TUNNEL_RINGS[d][s][1], SCRATCH_TUNNEL_RINGS[d][s][2]).setColor(colA[0], colA[1], colA[2], quadAlpha);
                tunnelConsumer.addVertex(pose, SCRATCH_TUNNEL_RINGS[d][nextS][0], SCRATCH_TUNNEL_RINGS[d][nextS][1], SCRATCH_TUNNEL_RINGS[d][nextS][2]).setColor(colA[0], colA[1], colA[2], quadAlpha);
                tunnelConsumer.addVertex(pose, SCRATCH_TUNNEL_RINGS[nextD][nextS][0], SCRATCH_TUNNEL_RINGS[nextD][nextS][1], SCRATCH_TUNNEL_RINGS[nextD][nextS][2]).setColor(colB[0], colB[1], colB[2], quadAlpha);
                tunnelConsumer.addVertex(pose, SCRATCH_TUNNEL_RINGS[nextD][s][0], SCRATCH_TUNNEL_RINGS[nextD][s][1], SCRATCH_TUNNEL_RINGS[nextD][s][2]).setColor(colB[0], colB[1], colB[2], quadAlpha);
            }
            ++d;
        }
    }

    private static void renderTunnelEndCap(Matrix4f pose, VertexConsumer coreConsumer, float time, float alpha) {
        float endZ = TUNNEL_RING_DEPTHS[5] + 3.0f;
        float centerX = (float)Math.sin(time * 6.0f) * 0.2f;
        float centerY = (float)Math.cos(time * 7.0f) * 0.2f;
        int s = 0;
        while (s < 28) {
            int nextS = (s + 1) % 28;
            coreConsumer.addVertex(pose, SCRATCH_TUNNEL_RINGS[5][s][0], SCRATCH_TUNNEL_RINGS[5][s][1], SCRATCH_TUNNEL_RINGS[5][s][2]).setColor(0.224f, 1.0f, 0.078f, 0.95f * alpha);
            coreConsumer.addVertex(pose, SCRATCH_TUNNEL_RINGS[5][nextS][0], SCRATCH_TUNNEL_RINGS[5][nextS][1], SCRATCH_TUNNEL_RINGS[5][nextS][2]).setColor(0.0f, 1.0f, 0.933f, 0.95f * alpha);
            coreConsumer.addVertex(pose, centerX, centerY, endZ).setColor(1.0f, 1.0f, 1.0f, 0.98f * alpha);
            coreConsumer.addVertex(pose, centerX, centerY, endZ).setColor(1.0f, 1.0f, 1.0f, 0.98f * alpha);
            ++s;
        }
    }

    private static void renderApertureRim(Matrix4f pose, VertexConsumer consumer, float time, float alpha, boolean isCore) {
        int s = 0;
        while (s < 28) {
            int nextS = (s + 1) % 28;
            float x1 = SCRATCH_TUNNEL_RINGS[0][s][0];
            float y1 = SCRATCH_TUNNEL_RINGS[0][s][1];
            float x2 = SCRATCH_TUNNEL_RINGS[0][nextS][0];
            float y2 = SCRATCH_TUNNEL_RINGS[0][nextS][1];
            if (!isCore) {
                float shift = (float)Math.sin(time * 3.5f + (float)s * 0.5f) * 0.5f + 0.5f;
                float rOuter = Mth.lerp((float)shift, (float)0.22f, (float)1.0f);
                float gOuter = Mth.lerp((float)shift, (float)1.0f, (float)0.0f);
                float bOuter = Mth.lerp((float)shift, (float)0.08f, (float)0.67f);
                RealityCrackRenderer.render3DLine(pose, consumer, x1, y1, 0.0f, x2, y2, 0.0f, 0.12f, rOuter, gOuter, bOuter, 0.85f * alpha);
            } else {
                RealityCrackRenderer.render3DLine(pose, consumer, x1, y1, 0.0f, x2, y2, 0.0f, 0.035f, 0.95f, 1.0f, 1.0f, 0.98f * alpha);
            }
            ++s;
        }
    }

    private static void renderPlanarFractureTree(Matrix4f pose, VertexConsumer consumer, float time, float crackProgress, float alpha, float apertureScale, boolean isCore) {
        float apertureClipRadiusSq = 5.2f * apertureScale * (5.2f * apertureScale);
        int b = 0;
        block0: while (b < 6) {
            BranchData branch = BAKED_BRANCHES[b];
            float[][] pts = branch.points;
            float[] thresholds = branch.activationThresholds;
            for (int s = 0; s < 10 && !(crackProgress < thresholds[s]); ++s) {
                float x1 = pts[s][0];
                float y1 = pts[s][1];
                float x2 = pts[s + 1][0];
                float y2 = pts[s + 1][1];
                if (apertureScale > 0.05f) {
                    float distSq1 = x1 * x1 + y1 * y1;
                    float distSq2 = x2 * x2 + y2 * y2;
                    if (distSq1 < apertureClipRadiusSq && distSq2 < apertureClipRadiusSq) continue;
                }
                float segProg = (float)s / 10.0f;
                float segAlpha = Math.min(1.0f, (crackProgress - thresholds[s]) / 0.12f) * alpha;
                if (!isCore) {
                    float widthOuter = Mth.lerp((float)segProg, (float)0.14f, (float)0.025f);
                    float colorShift = (float)Math.sin(time * 2.5f + (float)b * 1.5f + segProg * 3.0f) * 0.5f + 0.5f;
                    float rOuter = Mth.lerp((float)colorShift, (float)0.22f, (float)1.0f);
                    float gOuter = Mth.lerp((float)colorShift, (float)1.0f, (float)0.0f);
                    float bOuter = Mth.lerp((float)colorShift, (float)0.08f, (float)0.67f);
                    RealityCrackRenderer.render3DLine(pose, consumer, x1, y1, 0.0f, x2, y2, 0.0f, widthOuter, rOuter, gOuter, bOuter, 0.8f * segAlpha);
                    continue;
                }
                float widthCore = Mth.lerp((float)segProg, (float)0.045f, (float)0.008f);
                RealityCrackRenderer.render3DLine(pose, consumer, x1, y1, 0.0f, x2, y2, 0.0f, widthCore, 0.95f, 1.0f, 1.0f, 0.98f * segAlpha);
            }
            SubBranchData[] subBranchDataArray = branch.subBranches;
            int n = subBranchDataArray.length;
            int n2 = 0;
            while (true) {
                block8: {
                    float[][] subPts;
                    float subAlpha;
                    block9: {
                        block7: {
                            if (n2 >= n) break block7;
                            SubBranchData sub = subBranchDataArray[n2];
                            if (crackProgress < sub.activationThreshold) break block8;
                            subAlpha = Math.min(1.0f, (crackProgress - sub.activationThreshold) / 0.1f) * alpha;
                            subPts = sub.points;
                            break block9;
                        }
                        ++b;
                        continue block0;
                    }
                    for (int ss = 0; ss < subPts.length - 1; ++ss) {
                        float sx1 = subPts[ss][0];
                        float sy1 = subPts[ss][1];
                        float sx2 = subPts[ss + 1][0];
                        float sy2 = subPts[ss + 1][1];
                        if (apertureScale > 0.05f && sx1 * sx1 + sy1 * sy1 < apertureClipRadiusSq && sx2 * sx2 + sy2 * sy2 < apertureClipRadiusSq) continue;
                        if (!isCore) {
                            float subWidthOuter = 0.075f * (1.0f - (float)ss / (float)subPts.length * 0.4f);
                            RealityCrackRenderer.render3DLine(pose, consumer, sx1, sy1, 0.0f, sx2, sy2, 0.0f, subWidthOuter, 0.0f, 1.0f, 0.93f, 0.75f * subAlpha);
                            continue;
                        }
                        float subWidthCore = 0.025f * (1.0f - (float)ss / (float)subPts.length * 0.4f);
                        RealityCrackRenderer.render3DLine(pose, consumer, sx1, sy1, 0.0f, sx2, sy2, 0.0f, subWidthCore, 0.95f, 1.0f, 1.0f, 0.95f * subAlpha);
                    }
                }
                ++n2;
            }
            break;
        }
        return;
    }

    private static void renderPlanarAndShatteredShardFaces(PoseStack poseStack, VertexConsumer glassConsumer, float time, float crackProgress, float shatterProgress, float enterProgress, float alpha) {
        if (crackProgress <= 0.05f) {
            return;
        }
        boolean isShattering = shatterProgress > 0.001f;
        float shatterEase = isShattering ? 1.0f - (float)Math.pow(1.0f - shatterProgress, 3.0) : 0.0f;
        float implodeScale = 1.0f;
        if (enterProgress >= 0.65f) {
            float implodeP = (enterProgress - 0.65f) / 0.35f;
            implodeScale = Math.max(0.0f, 1.0f - (float)Math.pow(implodeP, 2.5));
        }
        int i = 0;
        while (i < 36) {
            float rotZ;
            float rotY;
            float rotX;
            float z;
            float y;
            float x;
            ShardData shard = BAKED_SHARDS[i];
            if (!isShattering) {
                x = shard.x0;
                y = shard.y0;
                z = 0.0f;
                rotX = 0.0f;
                rotY = 0.0f;
                rotZ = 0.0f;
            } else {
                float burstDist = shard.speed * shatterEase * 2.3f;
                x = (shard.x0 + shard.nx * burstDist) * implodeScale;
                y = (shard.y0 + shard.ny * burstDist) * implodeScale;
                z = shard.fwdSpeed * shatterEase * 1.8f * implodeScale;
                float rotSpeed = 50.0f * (1.0f + shatterEase * 2.5f);
                rotX = time * rotSpeed * shard.seedX % 360.0f;
                rotY = time * rotSpeed * shard.seedY % 360.0f;
                rotZ = time * rotSpeed * shard.seedZ % 360.0f;
            }
            poseStack.pushPose();
            poseStack.translate(x, y, z);
            if (isShattering) {
                poseStack.mulPose(Axis.XP.rotationDegrees(rotX));
                poseStack.mulPose(Axis.YP.rotationDegrees(rotY));
                poseStack.mulPose(Axis.ZP.rotationDegrees(rotZ));
            }
            Matrix4f shardPose = poseStack.last().pose();
            float halfT = shard.thickness * 0.5f;
            int nVerts = shard.verts2D.length;
            float shardAlpha = (isShattering ? 0.6f * (1.0f - shatterEase * 0.35f) : 0.25f * crackProgress) * alpha * implodeScale;
            RealityCrackRenderer.renderPolygonFace(shardPose, glassConsumer, shard.verts2D, nVerts, halfT, shard.faceR, shard.faceG, shard.faceB, shardAlpha, false);
            RealityCrackRenderer.renderPolygonFace(shardPose, glassConsumer, shard.verts2D, nVerts, -halfT, shard.faceR, shard.faceG, shard.faceB, shardAlpha, true);
            for (int j = 0; j < nVerts; ++j) {
                int next = (j + 1) % nVerts;
                float x1 = shard.verts2D[j][0];
                float y1 = shard.verts2D[j][1];
                float x2 = shard.verts2D[next][0];
                float y2 = shard.verts2D[next][1];
                glassConsumer.addVertex(shardPose, x1, y1, halfT).setColor(shard.faceR, shard.faceG, shard.faceB, shardAlpha * 0.9f);
                glassConsumer.addVertex(shardPose, x2, y2, halfT).setColor(shard.faceR, shard.faceG, shard.faceB, shardAlpha * 0.9f);
                glassConsumer.addVertex(shardPose, x2, y2, -halfT).setColor(shard.faceR, shard.faceG, shard.faceB, shardAlpha * 0.9f);
                glassConsumer.addVertex(shardPose, x1, y1, -halfT).setColor(shard.faceR, shard.faceG, shard.faceB, shardAlpha * 0.9f);
            }
            poseStack.popPose();
            ++i;
        }
    }

    private static void renderPlanarAndShatteredShardEdges(PoseStack poseStack, VertexConsumer edgeConsumer, float time, float crackProgress, float shatterProgress, float enterProgress, float alpha) {
        if (crackProgress <= 0.05f) {
            return;
        }
        boolean isShattering = shatterProgress > 0.001f;
        float shatterEase = isShattering ? 1.0f - (float)Math.pow(1.0f - shatterProgress, 3.0) : 0.0f;
        float implodeScale = 1.0f;
        if (enterProgress >= 0.65f) {
            float implodeP = (enterProgress - 0.65f) / 0.35f;
            implodeScale = Math.max(0.0f, 1.0f - (float)Math.pow(implodeP, 2.5));
        }
        int i = 0;
        while (i < 36) {
            float rotZ;
            float rotY;
            float rotX;
            float z;
            float y;
            float x;
            ShardData shard = BAKED_SHARDS[i];
            if (!isShattering) {
                x = shard.x0;
                y = shard.y0;
                z = 0.0f;
                rotX = 0.0f;
                rotY = 0.0f;
                rotZ = 0.0f;
            } else {
                float burstDist = shard.speed * shatterEase * 2.3f;
                x = (shard.x0 + shard.nx * burstDist) * implodeScale;
                y = (shard.y0 + shard.ny * burstDist) * implodeScale;
                z = shard.fwdSpeed * shatterEase * 1.8f * implodeScale;
                float rotSpeed = 50.0f * (1.0f + shatterEase * 2.5f);
                rotX = time * rotSpeed * shard.seedX % 360.0f;
                rotY = time * rotSpeed * shard.seedY % 360.0f;
                rotZ = time * rotSpeed * shard.seedZ % 360.0f;
            }
            poseStack.pushPose();
            poseStack.translate(x, y, z);
            if (isShattering) {
                poseStack.mulPose(Axis.XP.rotationDegrees(rotX));
                poseStack.mulPose(Axis.YP.rotationDegrees(rotY));
                poseStack.mulPose(Axis.ZP.rotationDegrees(rotZ));
            }
            Matrix4f shardPose = poseStack.last().pose();
            float halfT = shard.thickness * 0.5f;
            int nVerts = shard.verts2D.length;
            float edgeAlpha = (isShattering ? 0.9f : 0.65f * crackProgress) * alpha * implodeScale;
            RealityCrackRenderer.renderGlowingPerimeterRibbons(shardPose, edgeConsumer, shard.verts2D, nVerts, halfT, shard.edgeWidth, shard.edgeR, shard.edgeG, shard.edgeB, edgeAlpha);
            RealityCrackRenderer.renderGlowingPerimeterRibbons(shardPose, edgeConsumer, shard.verts2D, nVerts, -halfT, shard.edgeWidth, shard.edgeR, shard.edgeG, shard.edgeB, edgeAlpha);
            RealityCrackRenderer.renderWhiteCoreEdges(shardPose, edgeConsumer, shard.verts2D, nVerts, halfT, shard.edgeWidth * 0.35f, 0.95f * edgeAlpha);
            RealityCrackRenderer.renderWhiteCoreEdges(shardPose, edgeConsumer, shard.verts2D, nVerts, -halfT, shard.edgeWidth * 0.35f, 0.95f * edgeAlpha);
            poseStack.popPose();
            ++i;
        }
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
                float nx = -dy / len * width;
                float ny = dx / len * width;
                consumer.addVertex(pose, xa, ya, zOffset).setColor(r, g, b, a);
                consumer.addVertex(pose, xb, yb, zOffset).setColor(r, g, b, a);
                consumer.addVertex(pose, xb + nx, yb + ny, zOffset).setColor(r, g, b, 0.0f);
                consumer.addVertex(pose, xa + nx, ya + ny, zOffset).setColor(r, g, b, 0.0f);
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
                float nx = -dy / len * width;
                float ny = dx / len * width;
                consumer.addVertex(pose, xa - nx, ya - ny, zOffset).setColor(1.0f, 1.0f, 1.0f, a);
                consumer.addVertex(pose, xb - nx, yb - ny, zOffset).setColor(1.0f, 1.0f, 1.0f, a);
                consumer.addVertex(pose, xb + nx, yb + ny, zOffset).setColor(1.0f, 1.0f, 1.0f, a);
                consumer.addVertex(pose, xa + nx, ya + ny, zOffset).setColor(1.0f, 1.0f, 1.0f, a);
            }
            ++j;
        }
    }

    private static void renderFractureElectricalArcs(Matrix4f pose, VertexConsumer consumer, float time, float crackProgress, float alpha, float apertureScale, boolean isCore) {
        int arcCount = 12;
        float apertureClipRadiusSq = 5.2f * apertureScale * (5.2f * apertureScale);
        int t = 0;
        while (t < arcCount) {
            float phase = (time * 5.0f + (float)t * 0.618f) % 1.0f;
            if (!(phase > 0.75f)) {
                float arcAlpha = (float)Math.sin((double)(phase / 0.75f) * Math.PI) * alpha * crackProgress;
                float angle = (float)((double)t * 2.0 * Math.PI / (double)arcCount) + (float)Math.sin(time * 3.0f + (float)t) * 0.3f;
                float r1 = 1.2f + RealityCrackRenderer.hash(t, 901) * 2.0f;
                float r2 = r1 + (1.5f + RealityCrackRenderer.hash(t, 902) * 2.2f);
                float x1 = (float)Math.cos(angle) * r1;
                float y1 = (float)Math.sin(angle) * r1;
                float angle2 = angle + RealityCrackRenderer.hashSigned(t, 904) * 0.5f;
                float x2 = (float)Math.cos(angle2) * r2;
                float y2 = (float)Math.sin(angle2) * r2;
                if (!(apertureScale > 0.05f && x1 * x1 + y1 * y1 < apertureClipRadiusSq && x2 * x2 + y2 * y2 < apertureClipRadiusSq)) {
                    float midX = (x1 + x2) * 0.5f + (float)Math.sin(time * 22.0f + (float)t) * 0.15f;
                    float midY = (y1 + y2) * 0.5f + (float)Math.cos(time * 22.0f + (float)t) * 0.15f;
                    if (!isCore) {
                        RealityCrackRenderer.render3DLine(pose, consumer, x1, y1, 0.0f, midX, midY, 0.0f, 0.09f, 0.22f, 1.0f, 0.08f, 0.75f * arcAlpha);
                        RealityCrackRenderer.render3DLine(pose, consumer, midX, midY, 0.0f, x2, y2, 0.0f, 0.08f, 1.0f, 0.0f, 0.67f, 0.65f * arcAlpha);
                    } else {
                        RealityCrackRenderer.render3DLine(pose, consumer, x1, y1, 0.0f, midX, midY, 0.0f, 0.03f, 0.95f, 1.0f, 1.0f, 0.98f * arcAlpha);
                        RealityCrackRenderer.render3DLine(pose, consumer, midX, midY, 0.0f, x2, y2, 0.0f, 0.025f, 0.95f, 1.0f, 1.0f, 0.98f * arcAlpha);
                    }
                }
            }
            ++t;
        }
    }

    private static void renderVacuumSparks(Matrix4f pose, VertexConsumer consumer, float time, float enterProgress, float alpha, boolean isCore) {
        float implodeProgress = (enterProgress - 0.4f) / 0.6f;
        int i = 0;
        while (i < 28) {
            VacuumSparkData spark = BAKED_VACUUM_SPARKS[i];
            float currentRadius = Math.max(0.0f, spark.startRadius * (1.0f - implodeProgress));
            float tailRadius = currentRadius + spark.length * (1.0f - implodeProgress * 0.5f);
            float x1 = (float)Math.cos(spark.angle) * currentRadius;
            float y1 = (float)Math.sin(spark.angle) * currentRadius;
            float z1 = spark.z0 * (1.0f - implodeProgress);
            float x2 = (float)Math.cos(spark.angle) * tailRadius;
            float y2 = (float)Math.sin(spark.angle) * tailRadius;
            float z2 = z1 + (float)Math.sin((double)i * 2.0) * 0.15f;
            if (!isCore) {
                float bGlow;
                float gGlow;
                float rGlow;
                if (spark.colorType == 0) {
                    rGlow = 0.0f;
                    gGlow = 1.0f;
                    bGlow = 0.93f;
                } else if (spark.colorType == 1) {
                    rGlow = 0.22f;
                    gGlow = 1.0f;
                    bGlow = 0.08f;
                } else {
                    rGlow = 1.0f;
                    gGlow = 0.0f;
                    bGlow = 0.67f;
                }
                RealityCrackRenderer.render3DLine(pose, consumer, x1, y1, z1, x2, y2, z2, 0.09f, rGlow, gGlow, bGlow, 0.85f * alpha);
            } else {
                RealityCrackRenderer.render3DLine(pose, consumer, x1, y1, z1, x2, y2, z2, 0.03f, 1.0f, 1.0f, 1.0f, 0.98f * alpha);
            }
            ++i;
        }
    }

    private static void render3DLine(Matrix4f pose, VertexConsumer consumer, float x1, float y1, float z1, float x2, float y2, float z2, float width, float r, float g, float b, float a) {
        float n1z;
        float n1y;
        float n1x;
        if (a <= 0.001f) return;
        if (width <= 1.0E-4f) {
            return;
        }
        float dx = x2 - x1;
        float dy = y2 - y1;
        float dz = z2 - z1;
        float lenSq = dx * dx + dy * dy + dz * dz;
        if (lenSq < 1.0E-6f) {
            return;
        }
        float len = (float)Math.sqrt(lenSq);
        float dirX = dx / len;
        float dirY = dy / len;
        float dirZ = dz / len;
        if (Math.abs(dirZ) < 0.95f) {
            nLen = (float)Math.sqrt(dirY * dirY + dirX * dirX);
            n1x = -dirY / nLen;
            n1y = dirX / nLen;
            n1z = 0.0f;
        } else {
            nLen = (float)Math.sqrt(dirZ * dirZ + dirY * dirY);
            n1x = 0.0f;
            n1y = -dirZ / nLen;
            n1z = dirY / nLen;
        }
        float n2x = dirY * n1z - dirZ * n1y;
        float n2y = dirZ * n1x - dirX * n1z;
        float n2z = dirX * n1y - dirY * n1x;
        float halfW = width * 0.5f;
        float ox1 = n1x * halfW;
        float oy1 = n1y * halfW;
        float oz1 = n1z * halfW;
        consumer.addVertex(pose, x1 - ox1, y1 - oy1, z1 - oz1).setColor(r, g, b, a);
        consumer.addVertex(pose, x2 - ox1, y2 - oy1, z2 - oz1).setColor(r, g, b, a);
        consumer.addVertex(pose, x2 + ox1, y2 + oy1, z2 + oz1).setColor(r, g, b, a);
        consumer.addVertex(pose, x1 + ox1, y1 + oy1, z1 + oz1).setColor(r, g, b, a);
        float ox2 = n2x * halfW;
        float oy2 = n2y * halfW;
        float oz2 = n2z * halfW;
        consumer.addVertex(pose, x1 - ox2, y1 - oy2, z1 - oz2).setColor(r, g, b, a);
        consumer.addVertex(pose, x2 - ox2, y2 - oy2, z2 - oz2).setColor(r, g, b, a);
        consumer.addVertex(pose, x2 + ox2, y2 + oy2, z2 + oz2).setColor(r, g, b, a);
        consumer.addVertex(pose, x1 + ox2, y1 + oy2, z1 + oz2).setColor(r, g, b, a);
    }

    private static float hash(int seed, int index) {
        int h = seed * 374761393 + index * 668265263;
        h = (h ^ h >> 13) * 1274126177;
        return (float)((h & Integer.MAX_VALUE) % 10000) / 10000.0f;
    }

    private static float hashSigned(int seed, int index) {
        return RealityCrackRenderer.hash(seed, index) * 2.0f - 1.0f;
    }

    static {
        RealityCrackRenderer.initFractureTree();
        RealityCrackRenderer.initVoronoiShards();
        RealityCrackRenderer.initApertureContour();
        RealityCrackRenderer.initVacuumSparks();
    }
}

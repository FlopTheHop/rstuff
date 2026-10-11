/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  java.lang.MatchException
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.util.Mth
 *  org.ExtraTortik.ricks_stuff.client.renderer.OmegaLightningRenderer$1
 *  org.ExtraTortik.ricks_stuff.client.renderer.OmegaRenderTypes
 *  org.ExtraTortik.ricks_stuff.client.util.FastNoise
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDevicePhase
 *  org.joml.Matrix4f
 *  org.joml.Vector3f
 *  org.joml.Vector3fc
 */
package org.ExtraTortik.ricks_stuff.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import org.ExtraTortik.ricks_stuff.client.renderer.OmegaLightningRenderer;
import org.ExtraTortik.ricks_stuff.client.renderer.OmegaRenderTypes;
import org.ExtraTortik.ricks_stuff.client.util.FastNoise;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDevicePhase;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class OmegaLightningRenderer {
    private static final int EMITTER_COUNT = 16;
    private static final float CAGE_RADIUS = 50.0f;
    private static final float SPHERE_SURFACE_RADIUS = 18.0f;
    private static final int BEAM_SEGS = 36;
    private static final int HELIX_SEGS = 72;
    private static final int HELIX_VERTS = 73;
    private static final float HELIX_TURNS = 5.5f;
    private static final float HELIX_MAX_RADIUS = 2.4f;
    private static final float HELIX_ROTATION_SPEED = 5.0f;
    private static final int CORE_SIDES = 6;
    private static final float[] CORE_COS;
    private static final float[] CORE_SIN;
    private static final int GLOW_SIDES = 6;
    private static final float[] GLOW_COS;
    private static final float[] GLOW_SIN;
    private static final float[] HELIX_PX;
    private static final float[] HELIX_PY;
    private static final float[] HELIX_PZ;
    private static final float[] HELIX_RX;
    private static final float[] HELIX_RY;
    private static final float[] HELIX_RZ;
    private static final float[] HELIX_TX;
    private static final float[] HELIX_TY;
    private static final float[] HELIX_TZ;
    private static final float[] HELIX_NX;
    private static final float[] HELIX_NY;
    private static final float[] HELIX_NZ;
    private static final float[] HELIX_BX;
    private static final float[] HELIX_BY;
    private static final float[] HELIX_BZ;
    private static final float[] HELIX_TAPER;
    private static final float[][] HELIX_RING_X;
    private static final float[][] HELIX_RING_Y;
    private static final float[][] HELIX_RING_Z;
    private static final Vector3f SCRATCH_AXIS_PT;
    private static final Vector3f[] EMITTERS;
    private static final Vector3f[] TARGETS;
    private static final Vector3f[] AXIS_DIR;
    private static final Vector3f[] AXIS_U;
    private static final Vector3f[] AXIS_V;
    private static final float[] BEAM_LENGTHS;
    private static final Vector3f[] SURFACE_NORMALS;
    private static final Vector3f BOTTOM_EMITTER;
    private static final Vector3f BOTTOM_TARGET;
    private static final Vector3f BOTTOM_NORMAL;
    private static final Vector3f BOTTOM_DIR;
    private static final Vector3f BOTTOM_U;
    private static final Vector3f BOTTOM_V;
    private static final float BOTTOM_LEN = 30.0f;
    private static final int RIPPLE_SIDES = 24;
    private static final int RIPPLE_VERTS = 25;
    private static final float SPHERE_RIPPLE_RADIUS = 18.06f;
    private static final float RIPPLE_MAX_THETA = 0.32f;
    private static final int RIPPLE_RINGS_PER_CONTACT = 2;
    private static final float[] RIPPLE_COS_PSI;
    private static final float[] RIPPLE_SIN_PSI;
    private static final float[] SCRATCH_IN_X;
    private static final float[] SCRATCH_IN_Y;
    private static final float[] SCRATCH_IN_Z;
    private static final float[] SCRATCH_CREST_X;
    private static final float[] SCRATCH_CREST_Y;
    private static final float[] SCRATCH_CREST_Z;
    private static final float[] SCRATCH_OUT_X;
    private static final float[] SCRATCH_OUT_Y;
    private static final float[] SCRATCH_OUT_Z;

    public static void renderConvergentLightning(OmegaDeviceEntity device, PoseStack poseStack, MultiBufferSource bufferSource, float partialTicks) {
        float intensity;
        if (device == null) {
            return;
        }
        OmegaDevicePhase phase = device.getPhase();
        if (phase == null) {
            phase = OmegaDevicePhase.IDLE;
        }
        if ((intensity = OmegaLightningRenderer.getPhaseIntensity(phase, device.getPhaseTicks(), partialTicks)) <= 0.01f) {
            return;
        }
        float overloadWeight = OmegaLightningRenderer.getOverloadWeight(phase, device.getPhaseTicks(), partialTicks);
        float erasureWeight = OmegaLightningRenderer.getErasureWeight(phase, device.getPhaseTicks(), partialTicks);
        float time = ((float)device.tickCount + partialTicks) * 0.065f;
        poseStack.pushPose();
        VertexConsumer solidConsumer = bufferSource.getBuffer(OmegaRenderTypes.PLASMA_SOLID_BODY);
        OmegaLightningRenderer.renderAllSolidBeams(poseStack, solidConsumer, intensity, time, erasureWeight);
        VertexConsumer glowConsumer = bufferSource.getBuffer(OmegaRenderTypes.PLASMA_ADDITIVE_GLOW);
        OmegaLightningRenderer.renderAllGlowBeamsAndFlares(poseStack, glowConsumer, intensity, overloadWeight, erasureWeight, time);
        poseStack.popPose();
    }

    private static void renderAllSolidBeams(PoseStack poseStack, VertexConsumer consumer, float intensity, float time, float erasureWeight) {
        Matrix4f mat = poseStack.last().pose();
        int i = 0;
        while (true) {
            if (i >= 16) {
                float bCoreWidth = (0.22f + 0.08f * erasureWeight) * intensity;
                OmegaLightningRenderer.renderBottomAxisCoreSolid(mat, consumer, time, bCoreWidth, erasureWeight);
                float bHelixWidth = (0.09f + 0.03f * erasureWeight) * intensity;
                OmegaLightningRenderer.renderBottomHelixStrandSolid(mat, consumer, time, 0.0f, bHelixWidth, erasureWeight);
                OmegaLightningRenderer.renderBottomHelixStrandSolid(mat, consumer, time, (float)Math.PI, bHelixWidth, erasureWeight);
                return;
            }
            Vector3f e = EMITTERS[i];
            Vector3f t = TARGETS[i];
            Vector3f dir = AXIS_DIR[i];
            Vector3f u = AXIS_U[i];
            Vector3f v = AXIS_V[i];
            float len = BEAM_LENGTHS[i];
            float coreHalfWidth = (0.18f + 0.06f * erasureWeight) * intensity;
            OmegaLightningRenderer.renderAxisCoreSolid(mat, consumer, e, t, dir, u, v, time, i, coreHalfWidth, erasureWeight);
            float helixHalfWidth = (0.075f + 0.025f * erasureWeight) * intensity;
            OmegaLightningRenderer.renderHelixStrandSolid(mat, consumer, e, t, dir, u, v, len, time, 0.0f, i, helixHalfWidth, erasureWeight);
            OmegaLightningRenderer.renderHelixStrandSolid(mat, consumer, e, t, dir, u, v, len, time, (float)Math.PI, i, helixHalfWidth, erasureWeight);
            ++i;
        }
    }

    private static void renderAllGlowBeamsAndFlares(PoseStack poseStack, VertexConsumer consumer, float intensity, float overloadWeight, float erasureWeight, float time) {
        Matrix4f mat = poseStack.last().pose();
        int i = 0;
        while (true) {
            if (i >= 16) {
                OmegaLightningRenderer.renderBottomLaserGlow(mat, consumer, intensity, overloadWeight, erasureWeight, time);
                return;
            }
            Vector3f e = EMITTERS[i];
            Vector3f t = TARGETS[i];
            Vector3f dir = AXIS_DIR[i];
            Vector3f u = AXIS_U[i];
            Vector3f v = AXIS_V[i];
            float len = BEAM_LENGTHS[i];
            OmegaLightningRenderer.renderAxisGlow(mat, consumer, e, t, dir, u, v, time, i, intensity, overloadWeight, erasureWeight);
            OmegaLightningRenderer.renderHelixStrandGlow(mat, consumer, e, t, dir, u, v, len, time, 0.0f, i, intensity, overloadWeight, erasureWeight);
            OmegaLightningRenderer.renderHelixStrandGlow(mat, consumer, e, t, dir, u, v, len, time, (float)Math.PI, i, intensity, overloadWeight, erasureWeight);
            OmegaLightningRenderer.renderTransversePlasmaArcs(mat, consumer, e, t, dir, u, v, len, time, i, intensity, overloadWeight, erasureWeight);
            OmegaLightningRenderer.renderImpactAndEmitterFlares(mat, consumer, e, t, dir, u, v, time, i, intensity, overloadWeight, erasureWeight);
            OmegaLightningRenderer.renderLaserSurfaceContactWaves(mat, consumer, i, time, intensity, overloadWeight, erasureWeight);
            ++i;
        }
    }

    private static void getBeamPoint(Vector3f out, Vector3f e, Vector3f t, Vector3f u, Vector3f v, float s, float time, int beamIdx, float erasureWeight, int k) {
        float basePx = e.x + (t.x - e.x) * s;
        float basePy = e.y + (t.y - e.y) * s;
        float basePz = e.z + (t.z - e.z) * s;
        float idleJx = 0.03f * Mth.sin((float)(s * 14.0f + time * 6.0f + (float)beamIdx));
        float idleJy = 0.03f * Mth.cos((float)(s * 14.0f - time * 5.5f + (float)beamIdx));
        if (erasureWeight <= 0.001f) {
            out.set(basePx + u.x * idleJx + v.x * idleJy, basePy + u.y * idleJx + v.y * idleJy, basePz + u.z * idleJx + v.z * idleJy);
            return;
        }
        float env = (float)Math.pow(Math.sin(Math.PI * (double)s), 0.75);
        float waveU = 3.6f * Mth.sin((float)(s * 12.566f - time * 12.0f + (float)beamIdx * 1.1f)) * Mth.cos((float)(s * 7.854f + time * 7.0f + (float)beamIdx * 0.5f));
        float waveV = 3.6f * Mth.cos((float)(s * 12.566f - time * 12.0f + (float)beamIdx * 1.1f)) * Mth.sin((float)(s * 9.425f - time * 9.0f + (float)beamIdx * 0.7f));
        int stepTime = (int)(time * 18.0f);
        float hash1 = (float)((k * 1337 + stepTime * 7919 + beamIdx * 3571 & Short.MAX_VALUE) % 1000) / 500.0f - 1.0f;
        float hash2 = (float)((k * 2411 + stepTime * 5653 + beamIdx * 8209 & Short.MAX_VALUE) % 1000) / 500.0f - 1.0f;
        float jitU = 1.1f * Mth.sin((float)(s * 24.0f + time * 32.0f)) + hash1 * 0.75f;
        float jitV = 1.1f * Mth.cos((float)(s * 22.0f - time * 30.0f)) + hash2 * 0.75f;
        float dispU = idleJx * (1.0f - erasureWeight) + (waveU + jitU) * env * erasureWeight;
        float dispV = idleJy * (1.0f - erasureWeight) + (waveV + jitV) * env * erasureWeight;
        out.set(basePx + u.x * dispU + v.x * dispV, basePy + u.y * dispU + v.y * dispV, basePz + u.z * dispU + v.z * dispV);
    }

    private static void renderAxisCoreSolid(Matrix4f mat, VertexConsumer consumer, Vector3f e, Vector3f t, Vector3f dir, Vector3f u, Vector3f v, float time, int beamIdx, float halfWidth, float erasureWeight) {
        Vector3f p0 = new Vector3f();
        Vector3f p1 = new Vector3f();
        int k = 0;
        while (k < 36) {
            float s0 = (float)k / 36.0f;
            float s1 = (float)(k + 1) / 36.0f;
            OmegaLightningRenderer.getBeamPoint(p0, e, t, u, v, s0, time, beamIdx, erasureWeight, k);
            OmegaLightningRenderer.getBeamPoint(p1, e, t, u, v, s1, time, beamIdx, erasureWeight, k + 1);
            float ux = u.x * halfWidth;
            float uy = u.y * halfWidth;
            float uz = u.z * halfWidth;
            consumer.addVertex(mat, p0.x - ux, p0.y - uy, p0.z - uz).setColor(255, 255, 255, 255);
            consumer.addVertex(mat, p0.x + ux, p0.y + uy, p0.z + uz).setColor(255, 255, 255, 255);
            consumer.addVertex(mat, p1.x + ux, p1.y + uy, p1.z + uz).setColor(255, 255, 255, 255);
            consumer.addVertex(mat, p1.x - ux, p1.y - uy, p1.z - uz).setColor(255, 255, 255, 255);
            float vx = v.x * halfWidth;
            float vy = v.y * halfWidth;
            float vz = v.z * halfWidth;
            consumer.addVertex(mat, p0.x - vx, p0.y - vy, p0.z - vz).setColor(255, 255, 255, 255);
            consumer.addVertex(mat, p0.x + vx, p0.y + vy, p0.z + vz).setColor(255, 255, 255, 255);
            consumer.addVertex(mat, p1.x + vx, p1.y + vy, p1.z + vz).setColor(255, 255, 255, 255);
            consumer.addVertex(mat, p1.x - vx, p1.y - vy, p1.z - vz).setColor(255, 255, 255, 255);
            ++k;
        }
    }

    private static void renderAxisGlow(Matrix4f mat, VertexConsumer consumer, Vector3f e, Vector3f t, Vector3f dir, Vector3f u, Vector3f v, float time, int beamIdx, float intensity, float overloadWeight, float erasureWeight) {
        float midHalfWidth = (0.45f + 0.25f * erasureWeight) * intensity;
        float outerHalfWidth = (0.95f + 0.65f * erasureWeight) * intensity;
        int aMid = (int)((165.0f + 50.0f * erasureWeight) * intensity);
        int aOut = (int)((75.0f + 40.0f * erasureWeight) * intensity);
        int stdMidR = (int)Mth.lerp((float)overloadWeight, (float)176.0f, (float)230.0f);
        int stdMidG = (int)Mth.lerp((float)overloadWeight, (float)245.0f, (float)175.0f);
        int stdMidB = 255;
        int stdOutR = (int)Mth.lerp((float)overloadWeight, (float)80.0f, (float)170.0f);
        int stdOutG = (int)Mth.lerp((float)overloadWeight, (float)210.0f, (float)65.0f);
        int stdOutB = 255;
        int midR = (int)Mth.lerp((float)erasureWeight, (float)stdMidR, (float)57.0f);
        int midG = (int)Mth.lerp((float)erasureWeight, (float)stdMidG, (float)255.0f);
        int midB = (int)Mth.lerp((float)erasureWeight, (float)stdMidB, (float)20.0f);
        int outR = (int)Mth.lerp((float)erasureWeight, (float)stdOutR, (float)118.0f);
        int outG = (int)Mth.lerp((float)erasureWeight, (float)stdOutG, (float)230.0f);
        int outB = (int)Mth.lerp((float)erasureWeight, (float)stdOutB, (float)18.0f);
        Vector3f p0 = new Vector3f();
        Vector3f p1 = new Vector3f();
        int k = 0;
        while (k < 36) {
            float s0 = (float)k / 36.0f;
            float s1 = (float)(k + 1) / 36.0f;
            OmegaLightningRenderer.getBeamPoint(p0, e, t, u, v, s0, time, beamIdx, erasureWeight, k);
            OmegaLightningRenderer.getBeamPoint(p1, e, t, u, v, s1, time, beamIdx, erasureWeight, k + 1);
            float mux = u.x * midHalfWidth;
            float muy = u.y * midHalfWidth;
            float muz = u.z * midHalfWidth;
            consumer.addVertex(mat, p0.x - mux, p0.y - muy, p0.z - muz).setColor(midR, midG, midB, aMid);
            consumer.addVertex(mat, p0.x + mux, p0.y + muy, p0.z + muz).setColor(midR, midG, midB, aMid);
            consumer.addVertex(mat, p1.x + mux, p1.y + muy, p1.z + muz).setColor(midR, midG, midB, aMid);
            consumer.addVertex(mat, p1.x - mux, p1.y - muy, p1.z - muz).setColor(midR, midG, midB, aMid);
            float oux = u.x * outerHalfWidth;
            float ouy = u.y * outerHalfWidth;
            float ouz = u.z * outerHalfWidth;
            consumer.addVertex(mat, p0.x - oux, p0.y - ouy, p0.z - ouz).setColor(outR, outG, outB, aOut);
            consumer.addVertex(mat, p0.x + oux, p0.y + ouy, p0.z + ouz).setColor(outR, outG, outB, aOut);
            consumer.addVertex(mat, p1.x + oux, p1.y + ouy, p1.z + ouz).setColor(outR, outG, outB, aOut);
            consumer.addVertex(mat, p1.x - oux, p1.y - ouy, p1.z - ouz).setColor(outR, outG, outB, aOut);
            ++k;
        }
    }

    private static void sampleHelixCenterline(Vector3f e, Vector3f t, Vector3f dir, Vector3f u, Vector3f v, float time, float phaseOffset, int beamIdx, float erasureWeight) {
        int k;
        float rotSpeed = 5.0f * (1.0f + 1.2f * erasureWeight);
        for (k = 0; k < 73; ++k) {
            float taper;
            float s = (float)k / 72.0f;
            OmegaLightningRenderer.HELIX_TAPER[k] = taper = (float)Math.pow(Math.sin(Math.PI * (double)s), 0.55);
            float env = 2.4f * taper;
            float theta = (float)(34.55751918948772 * (double)s) - time * rotSpeed + phaseOffset + (float)beamIdx * 0.4f;
            float cosT = Mth.cos((float)theta);
            float sinT = Mth.sin((float)theta);
            float rx = u.x * cosT + v.x * sinT;
            float ry = u.y * cosT + v.y * sinT;
            float rz = u.z * cosT + v.z * sinT;
            OmegaLightningRenderer.HELIX_RX[k] = rx;
            OmegaLightningRenderer.HELIX_RY[k] = ry;
            OmegaLightningRenderer.HELIX_RZ[k] = rz;
            OmegaLightningRenderer.getBeamPoint(SCRATCH_AXIS_PT, e, t, u, v, s, time, beamIdx, erasureWeight, (int)(s * 36.0f));
            OmegaLightningRenderer.HELIX_PX[k] = OmegaLightningRenderer.SCRATCH_AXIS_PT.x + rx * env;
            OmegaLightningRenderer.HELIX_PY[k] = OmegaLightningRenderer.SCRATCH_AXIS_PT.y + ry * env;
            OmegaLightningRenderer.HELIX_PZ[k] = OmegaLightningRenderer.SCRATCH_AXIS_PT.z + rz * env;
        }
        k = 0;
        while (k < 73) {
            float tz;
            float ty;
            float tx;
            if (k == 0) {
                tx = HELIX_PX[1] - HELIX_PX[0];
                ty = HELIX_PY[1] - HELIX_PY[0];
                tz = HELIX_PZ[1] - HELIX_PZ[0];
            } else if (k == 72) {
                tx = HELIX_PX[72] - HELIX_PX[71];
                ty = HELIX_PY[72] - HELIX_PY[71];
                tz = HELIX_PZ[72] - HELIX_PZ[71];
            } else {
                tx = HELIX_PX[k + 1] - HELIX_PX[k - 1];
                ty = HELIX_PY[k + 1] - HELIX_PY[k - 1];
                tz = HELIX_PZ[k + 1] - HELIX_PZ[k - 1];
            }
            float tLen = (float)Math.sqrt(tx * tx + ty * ty + tz * tz);
            if (tLen > 1.0E-6f) {
                tx /= tLen;
                ty /= tLen;
                tz /= tLen;
            } else {
                tx = dir.x;
                ty = dir.y;
                tz = dir.z;
            }
            OmegaLightningRenderer.HELIX_TX[k] = tx;
            OmegaLightningRenderer.HELIX_TY[k] = ty;
            OmegaLightningRenderer.HELIX_TZ[k] = tz;
            float rx = HELIX_RX[k];
            float ry = HELIX_RY[k];
            float rz = HELIX_RZ[k];
            float nx = ty * rz - tz * ry;
            float ny = tz * rx - tx * rz;
            float nz = tx * ry - ty * rx;
            float nLen = (float)Math.sqrt(nx * nx + ny * ny + nz * nz);
            if (nLen > 1.0E-5f) {
                nx /= nLen;
                ny /= nLen;
                nz /= nLen;
            } else {
                nx = ty * u.z - tz * u.y;
                ny = tz * u.x - tx * u.z;
                nz = tx * u.y - ty * u.x;
                nLen = (float)Math.sqrt(nx * nx + ny * ny + nz * nz);
                if (nLen > 1.0E-5f) {
                    nx /= nLen;
                    ny /= nLen;
                    nz /= nLen;
                } else {
                    nx = u.x;
                    ny = u.y;
                    nz = u.z;
                }
            }
            OmegaLightningRenderer.HELIX_NX[k] = nx;
            OmegaLightningRenderer.HELIX_NY[k] = ny;
            OmegaLightningRenderer.HELIX_NZ[k] = nz;
            float bx = ty * nz - tz * ny;
            float by = tz * nx - tx * nz;
            float bz = tx * ny - ty * nx;
            OmegaLightningRenderer.HELIX_BX[k] = bx;
            OmegaLightningRenderer.HELIX_BY[k] = by;
            OmegaLightningRenderer.HELIX_BZ[k] = bz;
            ++k;
        }
    }

    private static void buildTubeRings(int sides, float[] cosTable, float[] sinTable, float baseRadius) {
        int k = 0;
        while (k < 73) {
            float r = baseRadius * HELIX_TAPER[k];
            float px = HELIX_PX[k];
            float py = HELIX_PY[k];
            float pz = HELIX_PZ[k];
            float nx = HELIX_NX[k];
            float ny = HELIX_NY[k];
            float nz = HELIX_NZ[k];
            float bx = HELIX_BX[k];
            float by = HELIX_BY[k];
            float bz = HELIX_BZ[k];
            for (int m = 0; m < sides; ++m) {
                float cm = cosTable[m];
                float sm = sinTable[m];
                float ox = (nx * cm + bx * sm) * r;
                float oy = (ny * cm + by * sm) * r;
                float oz = (nz * cm + bz * sm) * r;
                OmegaLightningRenderer.HELIX_RING_X[k][m] = px + ox;
                OmegaLightningRenderer.HELIX_RING_Y[k][m] = py + oy;
                OmegaLightningRenderer.HELIX_RING_Z[k][m] = pz + oz;
            }
            ++k;
        }
    }

    private static void emitTubeQuads(Matrix4f mat, VertexConsumer consumer, int sides, int red, int green, int blue, int alpha) {
        int k = 0;
        while (k < 72) {
            int nextK = k + 1;
            for (int m = 0; m < sides; ++m) {
                int nextM = (m + 1) % sides;
                consumer.addVertex(mat, HELIX_RING_X[k][m], HELIX_RING_Y[k][m], HELIX_RING_Z[k][m]).setColor(red, green, blue, alpha);
                consumer.addVertex(mat, HELIX_RING_X[k][nextM], HELIX_RING_Y[k][nextM], HELIX_RING_Z[k][nextM]).setColor(red, green, blue, alpha);
                consumer.addVertex(mat, HELIX_RING_X[nextK][nextM], HELIX_RING_Y[nextK][nextM], HELIX_RING_Z[nextK][nextM]).setColor(red, green, blue, alpha);
                consumer.addVertex(mat, HELIX_RING_X[nextK][m], HELIX_RING_Y[nextK][m], HELIX_RING_Z[nextK][m]).setColor(red, green, blue, alpha);
            }
            ++k;
        }
    }

    private static void renderHelixStrandSolid(Matrix4f mat, VertexConsumer consumer, Vector3f e, Vector3f t, Vector3f dir, Vector3f u, Vector3f v, float len, float time, float phaseOffset, int beamIdx, float halfWidth, float erasureWeight) {
        OmegaLightningRenderer.sampleHelixCenterline(e, t, dir, u, v, time, phaseOffset, beamIdx, erasureWeight);
        OmegaLightningRenderer.buildTubeRings(6, CORE_COS, CORE_SIN, halfWidth);
        OmegaLightningRenderer.emitTubeQuads(mat, consumer, 6, 255, 255, 255, 255);
    }

    private static void renderHelixStrandGlow(Matrix4f mat, VertexConsumer consumer, Vector3f e, Vector3f t, Vector3f dir, Vector3f u, Vector3f v, float len, float time, float phaseOffset, int beamIdx, float intensity, float overloadWeight, float erasureWeight) {
        float glowRadius = (0.28f + 0.12f * erasureWeight) * intensity;
        int aGlow = (int)((155.0f + 50.0f * erasureWeight) * intensity);
        int stdMidR = (int)Mth.lerp((float)overloadWeight, (float)120.0f, (float)215.0f);
        int stdMidG = (int)Mth.lerp((float)overloadWeight, (float)235.0f, (float)175.0f);
        int stdMidB = 255;
        int midR = (int)Mth.lerp((float)erasureWeight, (float)stdMidR, (float)80.0f);
        int midG = (int)Mth.lerp((float)erasureWeight, (float)stdMidG, (float)255.0f);
        int midB = (int)Mth.lerp((float)erasureWeight, (float)stdMidB, (float)25.0f);
        OmegaLightningRenderer.sampleHelixCenterline(e, t, dir, u, v, time, phaseOffset, beamIdx, erasureWeight);
        OmegaLightningRenderer.buildTubeRings(6, GLOW_COS, GLOW_SIN, glowRadius);
        OmegaLightningRenderer.emitTubeQuads(mat, consumer, 6, midR, midG, midB, aGlow);
    }

    private static void renderTransversePlasmaArcs(Matrix4f mat, VertexConsumer consumer, Vector3f e, Vector3f t, Vector3f dir, Vector3f u, Vector3f v, float len, float time, int beamIdx, float intensity, float overloadWeight, float erasureWeight) {
        int arcCount = 6 + (int)(6.0f * erasureWeight);
        float arcWidth = (0.055f + 0.035f * erasureWeight) * intensity;
        int aArc = (int)((210.0f + 40.0f * erasureWeight) * intensity);
        int stdR = (int)Mth.lerp((float)overloadWeight, (float)230.0f, (float)240.0f);
        int stdG = (int)Mth.lerp((float)overloadWeight, (float)255.0f, (float)215.0f);
        int stdB = 255;
        int arcR = (int)Mth.lerp((float)erasureWeight, (float)stdR, (float)180.0f);
        int arcG = (int)Mth.lerp((float)erasureWeight, (float)stdG, (float)255.0f);
        int arcB = (int)Mth.lerp((float)erasureWeight, (float)stdB, (float)60.0f);
        float rotSpeed = 5.0f * (1.0f + 1.2f * erasureWeight);
        Vector3f axisPt = new Vector3f();
        int a = 0;
        while (a < arcCount) {
            float s = 0.15f + (float)a * (0.7f / (float)arcCount) + 0.05f * Mth.sin((float)(time * 5.0f + (float)a * 2.1f + (float)beamIdx));
            s = Mth.clamp((float)s, (float)0.1f, (float)0.9f);
            float env = 2.4f * (float)Math.pow(Math.sin(Math.PI * (double)s), 0.55);
            float thetaA = (float)(34.55751918948772 * (double)s) - time * rotSpeed + (float)beamIdx * 0.4f;
            float thetaB = thetaA + (float)Math.PI;
            OmegaLightningRenderer.getBeamPoint(axisPt, e, t, u, v, s, time, beamIdx, erasureWeight, (int)(s * 36.0f));
            float ax0 = axisPt.x + (u.x * Mth.cos((float)thetaA) + v.x * Mth.sin((float)thetaA)) * env;
            float ay0 = axisPt.y + (u.y * Mth.cos((float)thetaA) + v.y * Mth.sin((float)thetaA)) * env;
            float az0 = axisPt.z + (u.z * Mth.cos((float)thetaA) + v.z * Mth.sin((float)thetaA)) * env;
            float bx0 = axisPt.x + (u.x * Mth.cos((float)thetaB) + v.x * Mth.sin((float)thetaB)) * env;
            float by0 = axisPt.y + (u.y * Mth.cos((float)thetaB) + v.y * Mth.sin((float)thetaB)) * env;
            float bz0 = axisPt.z + (u.z * Mth.cos((float)thetaB) + v.z * Mth.sin((float)thetaB)) * env;
            float mx = (ax0 + bx0) * 0.5f + dir.x * 0.2f * Mth.sin((float)(time * 14.0f + (float)a));
            float my = (ay0 + by0) * 0.5f + dir.y * 0.2f * Mth.sin((float)(time * 14.0f + (float)a));
            float mz = (az0 + bz0) * 0.5f + dir.z * 0.2f * Mth.sin((float)(time * 14.0f + (float)a));
            float ux = u.x * arcWidth;
            float uy = u.y * arcWidth;
            float uz = u.z * arcWidth;
            consumer.addVertex(mat, ax0 - ux, ay0 - uy, az0 - uz).setColor(arcR, arcG, arcB, aArc);
            consumer.addVertex(mat, ax0 + ux, ay0 + uy, az0 + uz).setColor(arcR, arcG, arcB, aArc);
            consumer.addVertex(mat, mx + ux, my + uy, mz + uz).setColor(arcR, arcG, arcB, aArc);
            consumer.addVertex(mat, mx - ux, my - uy, mz - uz).setColor(arcR, arcG, arcB, aArc);
            consumer.addVertex(mat, mx - ux, my - uy, mz - uz).setColor(arcR, arcG, arcB, aArc);
            consumer.addVertex(mat, mx + ux, my + uy, mz + uz).setColor(arcR, arcG, arcB, aArc);
            consumer.addVertex(mat, bx0 + ux, by0 + uy, bz0 + uz).setColor(arcR, arcG, arcB, aArc);
            consumer.addVertex(mat, bx0 - ux, by0 - uy, bz0 - uz).setColor(arcR, arcG, arcB, aArc);
            ++a;
        }
    }

    private static void renderImpactAndEmitterFlares(Matrix4f mat, VertexConsumer consumer, Vector3f e, Vector3f t, Vector3f dir, Vector3f u, Vector3f v, float time, int beamIdx, float intensity, float overloadWeight, float erasureWeight) {
        float z2;
        float y2;
        float x2;
        float z1;
        float y1;
        float x1;
        float flareRadius = (2.8f + 1.2f * erasureWeight) * intensity;
        int flareSides = 12;
        int aFlare = (int)((190.0f + 50.0f * erasureWeight) * intensity);
        int stdRimR = (int)Mth.lerp((float)overloadWeight, (float)100.0f, (float)190.0f);
        int stdRimG = (int)Mth.lerp((float)overloadWeight, (float)225.0f, (float)140.0f);
        int stdRimB = 255;
        int rimR = (int)Mth.lerp((float)erasureWeight, (float)stdRimR, (float)60.0f);
        int rimG = (int)Mth.lerp((float)erasureWeight, (float)stdRimG, (float)255.0f);
        int rimB = (int)Mth.lerp((float)erasureWeight, (float)stdRimB, (float)20.0f);
        for (int k = 0; k < flareSides; ++k) {
            float a1 = (float)(Math.PI * 2 * (double)k / (double)flareSides);
            float a2 = (float)(Math.PI * 2 * (double)(k + 1) / (double)flareSides);
            float rPulse = flareRadius * (1.0f + 0.14f * Mth.sin((float)(time * 8.0f + (float)k * 1.5f + (float)beamIdx)));
            x1 = t.x + (u.x * Mth.cos((float)a1) + v.x * Mth.sin((float)a1)) * rPulse;
            y1 = t.y + (u.y * Mth.cos((float)a1) + v.y * Mth.sin((float)a1)) * rPulse;
            z1 = t.z + (u.z * Mth.cos((float)a1) + v.z * Mth.sin((float)a1)) * rPulse;
            x2 = t.x + (u.x * Mth.cos((float)a2) + v.x * Mth.sin((float)a2)) * rPulse;
            y2 = t.y + (u.y * Mth.cos((float)a2) + v.y * Mth.sin((float)a2)) * rPulse;
            z2 = t.z + (u.z * Mth.cos((float)a2) + v.z * Mth.sin((float)a2)) * rPulse;
            consumer.addVertex(mat, t.x, t.y, t.z).setColor(255, 255, 255, aFlare);
            consumer.addVertex(mat, x1, y1, z1).setColor(rimR, rimG, rimB, 0);
            consumer.addVertex(mat, x2, y2, z2).setColor(rimR, rimG, rimB, 0);
            consumer.addVertex(mat, t.x, t.y, t.z).setColor(255, 255, 255, aFlare);
        }
        float eFlareR = (1.8f + 0.8f * erasureWeight) * intensity;
        int k = 0;
        while (k < flareSides) {
            float a1 = (float)(Math.PI * 2 * (double)k / (double)flareSides);
            float a2 = (float)(Math.PI * 2 * (double)(k + 1) / (double)flareSides);
            x1 = e.x + (u.x * Mth.cos((float)a1) + v.x * Mth.sin((float)a1)) * eFlareR;
            y1 = e.y + (u.y * Mth.cos((float)a1) + v.y * Mth.sin((float)a1)) * eFlareR;
            z1 = e.z + (u.z * Mth.cos((float)a1) + v.z * Mth.sin((float)a1)) * eFlareR;
            x2 = e.x + (u.x * Mth.cos((float)a2) + v.x * Mth.sin((float)a2)) * eFlareR;
            y2 = e.y + (u.y * Mth.cos((float)a2) + v.y * Mth.sin((float)a2)) * eFlareR;
            z2 = e.z + (u.z * Mth.cos((float)a2) + v.z * Mth.sin((float)a2)) * eFlareR;
            consumer.addVertex(mat, e.x, e.y, e.z).setColor(255, 255, 255, aFlare);
            consumer.addVertex(mat, x1, y1, z1).setColor(rimR, rimG, rimB, 0);
            consumer.addVertex(mat, x2, y2, z2).setColor(rimR, rimG, rimB, 0);
            consumer.addVertex(mat, e.x, e.y, e.z).setColor(255, 255, 255, aFlare);
            ++k;
        }
    }

    private static void getBottomBeamPoint(Vector3f out, float s, float time, float erasureWeight, int k) {
        float basePx = OmegaLightningRenderer.BOTTOM_EMITTER.x;
        float basePy = OmegaLightningRenderer.BOTTOM_EMITTER.y + 30.0f * s;
        float basePz = OmegaLightningRenderer.BOTTOM_EMITTER.z;
        float idleJx = 0.035f * Mth.sin((float)(s * 14.0f + time * 6.0f + 20.0f));
        float idleJz = 0.035f * Mth.cos((float)(s * 14.0f - time * 5.5f + 20.0f));
        if (erasureWeight <= 0.001f) {
            out.set(basePx + idleJx, basePy, basePz + idleJz);
            return;
        }
        float env = (float)Math.pow(Math.sin(Math.PI * (double)s), 0.75);
        float waveX = 3.2f * Mth.sin((float)(s * 12.0f - time * 12.0f)) * Mth.cos((float)(s * 7.5f + time * 6.5f));
        float waveZ = 3.2f * Mth.cos((float)(s * 12.0f - time * 12.0f)) * Mth.sin((float)(s * 8.5f - time * 8.5f));
        int stepTime = (int)(time * 18.0f);
        float hash1 = (float)((k * 1337 + stepTime * 7919 + 71420 & Short.MAX_VALUE) % 1000) / 500.0f - 1.0f;
        float hash2 = (float)((k * 2411 + stepTime * 5653 + 164180 & Short.MAX_VALUE) % 1000) / 500.0f - 1.0f;
        float jitX = 1.0f * Mth.sin((float)(s * 24.0f + time * 30.0f)) + hash1 * 0.7f;
        float jitZ = 1.0f * Mth.cos((float)(s * 22.0f - time * 28.0f)) + hash2 * 0.7f;
        float dispX = idleJx * (1.0f - erasureWeight) + (waveX + jitX) * env * erasureWeight;
        float dispZ = idleJz * (1.0f - erasureWeight) + (waveZ + jitZ) * env * erasureWeight;
        out.set(basePx + dispX, basePy, basePz + dispZ);
    }

    private static void renderBottomAxisCoreSolid(Matrix4f mat, VertexConsumer consumer, float time, float halfWidth, float erasureWeight) {
        Vector3f p0 = new Vector3f();
        Vector3f p1 = new Vector3f();
        int k = 0;
        while (k < 36) {
            float s0 = (float)k / 36.0f;
            float s1 = (float)(k + 1) / 36.0f;
            OmegaLightningRenderer.getBottomBeamPoint(p0, s0, time, erasureWeight, k);
            OmegaLightningRenderer.getBottomBeamPoint(p1, s1, time, erasureWeight, k + 1);
            float ux = OmegaLightningRenderer.BOTTOM_U.x * halfWidth;
            consumer.addVertex(mat, p0.x - ux, p0.y, p0.z).setColor(255, 255, 255, 255);
            consumer.addVertex(mat, p0.x + ux, p0.y, p0.z).setColor(255, 255, 255, 255);
            consumer.addVertex(mat, p1.x + ux, p1.y, p1.z).setColor(255, 255, 255, 255);
            consumer.addVertex(mat, p1.x - ux, p1.y, p1.z).setColor(255, 255, 255, 255);
            float vz = OmegaLightningRenderer.BOTTOM_V.z * halfWidth;
            consumer.addVertex(mat, p0.x, p0.y, p0.z - vz).setColor(255, 255, 255, 255);
            consumer.addVertex(mat, p0.x, p0.y, p0.z + vz).setColor(255, 255, 255, 255);
            consumer.addVertex(mat, p1.x, p1.y, p1.z + vz).setColor(255, 255, 255, 255);
            consumer.addVertex(mat, p1.x, p1.y, p1.z - vz).setColor(255, 255, 255, 255);
            ++k;
        }
    }

    private static void renderBottomHelixStrandSolid(Matrix4f mat, VertexConsumer consumer, float time, float phaseOffset, float halfWidth, float erasureWeight) {
        OmegaLightningRenderer.renderHelixStrandSolid(mat, consumer, BOTTOM_EMITTER, BOTTOM_TARGET, BOTTOM_DIR, BOTTOM_U, BOTTOM_V, 30.0f, time, phaseOffset, 20, halfWidth, erasureWeight);
    }

    private static void renderBottomLaserGlow(Matrix4f mat, VertexConsumer consumer, float intensity, float overloadWeight, float erasureWeight, float time) {
        float midHalfWidth = (0.55f + 0.3f * erasureWeight) * intensity;
        float outerHalfWidth = (1.2f + 0.8f * erasureWeight) * intensity;
        int aMid = (int)((175.0f + 50.0f * erasureWeight) * intensity);
        int aOut = (int)((85.0f + 40.0f * erasureWeight) * intensity);
        int stdMidR = (int)Mth.lerp((float)overloadWeight, (float)176.0f, (float)230.0f);
        int stdMidG = (int)Mth.lerp((float)overloadWeight, (float)245.0f, (float)175.0f);
        int stdMidB = 255;
        int stdOutR = (int)Mth.lerp((float)overloadWeight, (float)80.0f, (float)170.0f);
        int stdOutG = (int)Mth.lerp((float)overloadWeight, (float)210.0f, (float)65.0f);
        int stdOutB = 255;
        int midR = (int)Mth.lerp((float)erasureWeight, (float)stdMidR, (float)57.0f);
        int midG = (int)Mth.lerp((float)erasureWeight, (float)stdMidG, (float)255.0f);
        int midB = (int)Mth.lerp((float)erasureWeight, (float)stdMidB, (float)20.0f);
        int outR = (int)Mth.lerp((float)erasureWeight, (float)stdOutR, (float)118.0f);
        int outG = (int)Mth.lerp((float)erasureWeight, (float)stdOutG, (float)230.0f);
        int outB = (int)Mth.lerp((float)erasureWeight, (float)stdOutB, (float)18.0f);
        Vector3f p0 = new Vector3f();
        Vector3f p1 = new Vector3f();
        for (int k = 0; k < 36; ++k) {
            float s0 = (float)k / 36.0f;
            float s1 = (float)(k + 1) / 36.0f;
            OmegaLightningRenderer.getBottomBeamPoint(p0, s0, time, erasureWeight, k);
            OmegaLightningRenderer.getBottomBeamPoint(p1, s1, time, erasureWeight, k + 1);
            float mux = OmegaLightningRenderer.BOTTOM_U.x * midHalfWidth;
            float muz = OmegaLightningRenderer.BOTTOM_V.z * midHalfWidth;
            consumer.addVertex(mat, p0.x - mux, p0.y, p0.z - muz).setColor(midR, midG, midB, aMid);
            consumer.addVertex(mat, p0.x + mux, p0.y, p0.z + muz).setColor(midR, midG, midB, aMid);
            consumer.addVertex(mat, p1.x + mux, p1.y, p1.z + muz).setColor(midR, midG, midB, aMid);
            consumer.addVertex(mat, p1.x - mux, p1.y, p1.z - muz).setColor(midR, midG, midB, aMid);
            float oux = OmegaLightningRenderer.BOTTOM_U.x * outerHalfWidth;
            float ouz = OmegaLightningRenderer.BOTTOM_V.z * outerHalfWidth;
            consumer.addVertex(mat, p0.x - oux, p0.y, p0.z - ouz).setColor(outR, outG, outB, aOut);
            consumer.addVertex(mat, p0.x + oux, p0.y, p0.z + ouz).setColor(outR, outG, outB, aOut);
            consumer.addVertex(mat, p1.x + oux, p1.y, p1.z + ouz).setColor(outR, outG, outB, aOut);
            consumer.addVertex(mat, p1.x - oux, p1.y, p1.z - ouz).setColor(outR, outG, outB, aOut);
        }
        OmegaLightningRenderer.renderHelixStrandGlow(mat, consumer, BOTTOM_EMITTER, BOTTOM_TARGET, BOTTOM_DIR, BOTTOM_U, BOTTOM_V, 30.0f, time, 0.0f, 20, intensity, overloadWeight, erasureWeight);
        OmegaLightningRenderer.renderHelixStrandGlow(mat, consumer, BOTTOM_EMITTER, BOTTOM_TARGET, BOTTOM_DIR, BOTTOM_U, BOTTOM_V, 30.0f, time, (float)Math.PI, 20, intensity, overloadWeight, erasureWeight);
        OmegaLightningRenderer.renderTransversePlasmaArcs(mat, consumer, BOTTOM_EMITTER, BOTTOM_TARGET, BOTTOM_DIR, BOTTOM_U, BOTTOM_V, 30.0f, time, 20, intensity, overloadWeight, erasureWeight);
        float flareRadius = (3.5f + 1.5f * erasureWeight) * intensity;
        int flareSides = 14;
        int aFlare = (int)((210.0f + 45.0f * erasureWeight) * intensity);
        int stdRimR = (int)Mth.lerp((float)overloadWeight, (float)100.0f, (float)190.0f);
        int stdRimG = (int)Mth.lerp((float)overloadWeight, (float)225.0f, (float)140.0f);
        int stdRimB = 255;
        int rimR = (int)Mth.lerp((float)erasureWeight, (float)stdRimR, (float)60.0f);
        int rimG = (int)Mth.lerp((float)erasureWeight, (float)stdRimG, (float)255.0f);
        int rimB = (int)Mth.lerp((float)erasureWeight, (float)stdRimB, (float)20.0f);
        int k = 0;
        while (true) {
            if (k >= flareSides) {
                OmegaLightningRenderer.renderBottomLaserSurfaceContactWaves(mat, consumer, time, intensity, overloadWeight, erasureWeight);
                return;
            }
            float a1 = (float)(Math.PI * 2 * (double)k / (double)flareSides);
            float a2 = (float)(Math.PI * 2 * (double)(k + 1) / (double)flareSides);
            float rPulse = flareRadius * (1.0f + 0.14f * Mth.sin((float)(time * 8.0f + (float)k * 1.5f)));
            float x1 = OmegaLightningRenderer.BOTTOM_TARGET.x + Mth.cos((float)a1) * rPulse;
            float y1 = OmegaLightningRenderer.BOTTOM_TARGET.y;
            float z1 = OmegaLightningRenderer.BOTTOM_TARGET.z + Mth.sin((float)a1) * rPulse;
            float x2 = OmegaLightningRenderer.BOTTOM_TARGET.x + Mth.cos((float)a2) * rPulse;
            float y2 = OmegaLightningRenderer.BOTTOM_TARGET.y;
            float z2 = OmegaLightningRenderer.BOTTOM_TARGET.z + Mth.sin((float)a2) * rPulse;
            consumer.addVertex(mat, OmegaLightningRenderer.BOTTOM_TARGET.x, OmegaLightningRenderer.BOTTOM_TARGET.y, OmegaLightningRenderer.BOTTOM_TARGET.z).setColor(255, 255, 255, aFlare);
            consumer.addVertex(mat, x1, y1, z1).setColor(rimR, rimG, rimB, 0);
            consumer.addVertex(mat, x2, y2, z2).setColor(rimR, rimG, rimB, 0);
            consumer.addVertex(mat, OmegaLightningRenderer.BOTTOM_TARGET.x, OmegaLightningRenderer.BOTTOM_TARGET.y, OmegaLightningRenderer.BOTTOM_TARGET.z).setColor(255, 255, 255, aFlare);
            ++k;
        }
    }

    private static float getPhaseIntensity(OmegaDevicePhase phase, int phaseTicks, float partialTicks) {
        return switch (1.$SwitchMap$org$ExtraTortik$ricks_stuff$entity$omega$OmegaDevicePhase[phase.ordinal()]) {
            default -> throw new MatchException(null, null);
            case 1 -> 0.85f;
            case 2 -> 1.45f;
            case 3 -> 1.0f;
            case 4 -> 1.25f;
            case 5 -> 1.6f;
            case 6 -> 2.4f;
            case 7 -> 0.6f;
        };
    }

    private static float getOverloadWeight(OmegaDevicePhase phase, int phaseTicks, float partialTicks) {
        float t = (float)phaseTicks + partialTicks;
        float linear = switch (1.$SwitchMap$org$ExtraTortik$ricks_stuff$entity$omega$OmegaDevicePhase[phase.ordinal()]) {
            case 2 -> Mth.clamp((float)(t / 35.0f), (float)0.0f, (float)1.0f);
            case 3 -> 0.25f;
            case 4 -> 0.65f;
            case 5 -> 1.0f;
            default -> 0.0f;
        };
        return linear * linear * (3.0f - 2.0f * linear);
    }

    private static float getErasureWeight(OmegaDevicePhase phase, int phaseTicks, float partialTicks) {
        float t = (float)phaseTicks + partialTicks;
        float linear = switch (1.$SwitchMap$org$ExtraTortik$ricks_stuff$entity$omega$OmegaDevicePhase[phase.ordinal()]) {
            case 6 -> 1.0f;
            case 7 -> Mth.clamp((float)(1.0f - t / 40.0f), (float)0.0f, (float)1.0f);
            default -> 0.0f;
        };
        return linear * linear * (3.0f - 2.0f * linear);
    }

    private static void renderSphericalRippleRing(Matrix4f mat, VertexConsumer consumer, Vector3f normal, Vector3f u, Vector3f v, float phase, float time, int contactIdx, int ringIdx, float intensity, float overloadWeight, float erasureWeight) {
        int k;
        float fade = Mth.clamp((float)(phase / 0.15f), (float)0.0f, (float)1.0f) * (1.0f - phase) * (1.0f - phase);
        if (fade <= 0.001f) {
            return;
        }
        float maxAlpha = (215.0f + 40.0f * erasureWeight) * intensity;
        int aCrest = (int)(maxAlpha * fade);
        if (aCrest <= 0) {
            return;
        }
        int stdCrestR = (int)Mth.lerp((float)overloadWeight, (float)220.0f, (float)245.0f);
        int stdCrestG = (int)Mth.lerp((float)overloadWeight, (float)250.0f, (float)210.0f);
        int stdCrestB = 255;
        int crestR = (int)Mth.lerp((float)erasureWeight, (float)stdCrestR, (float)170.0f);
        int crestG = (int)Mth.lerp((float)erasureWeight, (float)stdCrestG, (float)255.0f);
        int crestB = (int)Mth.lerp((float)erasureWeight, (float)stdCrestB, (float)90.0f);
        int stdRimR = (int)Mth.lerp((float)overloadWeight, (float)80.0f, (float)180.0f);
        int stdRimG = (int)Mth.lerp((float)overloadWeight, (float)220.0f, (float)130.0f);
        int stdRimB = 255;
        int rimR = (int)Mth.lerp((float)erasureWeight, (float)stdRimR, (float)57.0f);
        int rimG = (int)Mth.lerp((float)erasureWeight, (float)stdRimG, (float)255.0f);
        int rimB = (int)Mth.lerp((float)erasureWeight, (float)stdRimB, (float)20.0f);
        float thetaCrest = phase * 0.32f;
        float halfWidth = 0.028f + 0.02f * phase + 0.015f * erasureWeight;
        float thetaIn = Math.max(0.0f, thetaCrest - halfWidth);
        float thetaOut = thetaCrest + halfWidth;
        float cosIn = Mth.cos((float)thetaIn);
        float sinIn = Mth.sin((float)thetaIn);
        float cosCrest = Mth.cos((float)thetaCrest);
        float sinCrest = Mth.sin((float)thetaCrest);
        float cosOut = Mth.cos((float)thetaOut);
        float sinOut = Mth.sin((float)thetaOut);
        for (k = 0; k <= 24; ++k) {
            float cosPsi = RIPPLE_COS_PSI[k];
            float sinPsi = RIPPLE_SIN_PSI[k];
            float tx = u.x * cosPsi + v.x * sinPsi;
            float ty = u.y * cosPsi + v.y * sinPsi;
            float tz = u.z * cosPsi + v.z * sinPsi;
            float inDirX = normal.x * cosIn + tx * sinIn;
            float inDirY = normal.y * cosIn + ty * sinIn;
            float inDirZ = normal.z * cosIn + tz * sinIn;
            float crestDirX = normal.x * cosCrest + tx * sinCrest;
            float crestDirY = normal.y * cosCrest + ty * sinCrest;
            float crestDirZ = normal.z * cosCrest + tz * sinCrest;
            float outDirX = normal.x * cosOut + tx * sinOut;
            float outDirY = normal.y * cosOut + ty * sinOut;
            float outDirZ = normal.z * cosOut + tz * sinOut;
            float noise = FastNoise.simplex3D((float)(crestDirX * 10.0f + (float)contactIdx * 3.7f), (float)(crestDirY * 10.0f + (float)ringIdx * 5.3f), (float)(crestDirZ * 10.0f + time * 5.5f));
            float jitter = noise * (0.03f + 0.05f * erasureWeight) * intensity;
            float rEff = 18.06f + jitter;
            OmegaLightningRenderer.SCRATCH_IN_X[k] = inDirX * rEff;
            OmegaLightningRenderer.SCRATCH_IN_Y[k] = inDirY * rEff;
            OmegaLightningRenderer.SCRATCH_IN_Z[k] = inDirZ * rEff;
            OmegaLightningRenderer.SCRATCH_CREST_X[k] = crestDirX * rEff;
            OmegaLightningRenderer.SCRATCH_CREST_Y[k] = crestDirY * rEff;
            OmegaLightningRenderer.SCRATCH_CREST_Z[k] = crestDirZ * rEff;
            OmegaLightningRenderer.SCRATCH_OUT_X[k] = outDirX * rEff;
            OmegaLightningRenderer.SCRATCH_OUT_Y[k] = outDirY * rEff;
            OmegaLightningRenderer.SCRATCH_OUT_Z[k] = outDirZ * rEff;
        }
        k = 0;
        while (k < 24) {
            int next = k + 1;
            consumer.addVertex(mat, SCRATCH_IN_X[k], SCRATCH_IN_Y[k], SCRATCH_IN_Z[k]).setColor(rimR, rimG, rimB, 0);
            consumer.addVertex(mat, SCRATCH_IN_X[next], SCRATCH_IN_Y[next], SCRATCH_IN_Z[next]).setColor(rimR, rimG, rimB, 0);
            consumer.addVertex(mat, SCRATCH_CREST_X[next], SCRATCH_CREST_Y[next], SCRATCH_CREST_Z[next]).setColor(crestR, crestG, crestB, aCrest);
            consumer.addVertex(mat, SCRATCH_CREST_X[k], SCRATCH_CREST_Y[k], SCRATCH_CREST_Z[k]).setColor(crestR, crestG, crestB, aCrest);
            consumer.addVertex(mat, SCRATCH_CREST_X[k], SCRATCH_CREST_Y[k], SCRATCH_CREST_Z[k]).setColor(crestR, crestG, crestB, aCrest);
            consumer.addVertex(mat, SCRATCH_CREST_X[next], SCRATCH_CREST_Y[next], SCRATCH_CREST_Z[next]).setColor(crestR, crestG, crestB, aCrest);
            consumer.addVertex(mat, SCRATCH_OUT_X[next], SCRATCH_OUT_Y[next], SCRATCH_OUT_Z[next]).setColor(rimR, rimG, rimB, 0);
            consumer.addVertex(mat, SCRATCH_OUT_X[k], SCRATCH_OUT_Y[k], SCRATCH_OUT_Z[k]).setColor(rimR, rimG, rimB, 0);
            ++k;
        }
    }

    public static void renderLaserSurfaceContactWaves(Matrix4f mat, VertexConsumer consumer, int beamIdx, float time, float intensity, float overloadWeight, float erasureWeight) {
        if (beamIdx < 0) return;
        if (beamIdx >= 16) {
            return;
        }
        Vector3f normal = SURFACE_NORMALS[beamIdx];
        Vector3f u = AXIS_U[beamIdx];
        Vector3f v = AXIS_V[beamIdx];
        float waveSpeed = 0.85f * (1.0f + 0.5f * erasureWeight);
        int r = 0;
        while (r < 2) {
            float phaseOffset = (float)r / 2.0f;
            float phase = (time * waveSpeed + phaseOffset + (float)beamIdx * 0.187f) % 1.0f;
            if (phase < 0.0f) {
                phase += 1.0f;
            }
            OmegaLightningRenderer.renderSphericalRippleRing(mat, consumer, normal, u, v, phase, time, beamIdx, r, intensity, overloadWeight, erasureWeight);
            ++r;
        }
    }

    public static void renderLaserSurfaceContactWaves(Matrix4f mat, VertexConsumer consumer, float time, float intensity, float overloadWeight, float erasureWeight) {
        int i = 0;
        while (i < 16) {
            OmegaLightningRenderer.renderLaserSurfaceContactWaves(mat, consumer, i, time, intensity, overloadWeight, erasureWeight);
            ++i;
        }
    }

    public static void renderBottomLaserSurfaceContactWaves(Matrix4f mat, VertexConsumer consumer, float time, float intensity, float overloadWeight, float erasureWeight) {
        float waveSpeed = 0.85f * (1.0f + 0.5f * erasureWeight);
        int r = 0;
        while (r < 2) {
            float phaseOffset = (float)r / 2.0f;
            float phase = (time * waveSpeed + phaseOffset + 3.7400002f) % 1.0f;
            if (phase < 0.0f) {
                phase += 1.0f;
            }
            OmegaLightningRenderer.renderSphericalRippleRing(mat, consumer, BOTTOM_NORMAL, BOTTOM_U, BOTTOM_V, phase, time, 20, r, intensity, overloadWeight, erasureWeight);
            ++r;
        }
    }

    static {
        double phi;
        int m;
        CORE_COS = new float[6];
        CORE_SIN = new float[6];
        GLOW_COS = new float[6];
        GLOW_SIN = new float[6];
        HELIX_PX = new float[73];
        HELIX_PY = new float[73];
        HELIX_PZ = new float[73];
        HELIX_RX = new float[73];
        HELIX_RY = new float[73];
        HELIX_RZ = new float[73];
        HELIX_TX = new float[73];
        HELIX_TY = new float[73];
        HELIX_TZ = new float[73];
        HELIX_NX = new float[73];
        HELIX_NY = new float[73];
        HELIX_NZ = new float[73];
        HELIX_BX = new float[73];
        HELIX_BY = new float[73];
        HELIX_BZ = new float[73];
        HELIX_TAPER = new float[73];
        HELIX_RING_X = new float[73][8];
        HELIX_RING_Y = new float[73][8];
        HELIX_RING_Z = new float[73][8];
        SCRATCH_AXIS_PT = new Vector3f();
        EMITTERS = new Vector3f[16];
        TARGETS = new Vector3f[16];
        AXIS_DIR = new Vector3f[16];
        AXIS_U = new Vector3f[16];
        AXIS_V = new Vector3f[16];
        BEAM_LENGTHS = new float[16];
        SURFACE_NORMALS = new Vector3f[16];
        BOTTOM_EMITTER = new Vector3f(0.0f, -48.0f, 0.0f);
        BOTTOM_TARGET = new Vector3f(0.0f, -18.0f, 0.0f);
        BOTTOM_NORMAL = new Vector3f(0.0f, -1.0f, 0.0f);
        BOTTOM_DIR = new Vector3f(0.0f, 1.0f, 0.0f);
        BOTTOM_U = new Vector3f(1.0f, 0.0f, 0.0f);
        BOTTOM_V = new Vector3f(0.0f, 0.0f, 1.0f);
        RIPPLE_COS_PSI = new float[25];
        RIPPLE_SIN_PSI = new float[25];
        SCRATCH_IN_X = new float[25];
        SCRATCH_IN_Y = new float[25];
        SCRATCH_IN_Z = new float[25];
        SCRATCH_CREST_X = new float[25];
        SCRATCH_CREST_Y = new float[25];
        SCRATCH_CREST_Z = new float[25];
        SCRATCH_OUT_X = new float[25];
        SCRATCH_OUT_Y = new float[25];
        SCRATCH_OUT_Z = new float[25];
        for (m = 0; m < 6; ++m) {
            phi = (double)m * 2.0 * Math.PI / 6.0;
            OmegaLightningRenderer.CORE_COS[m] = (float)Math.cos(phi);
            OmegaLightningRenderer.CORE_SIN[m] = (float)Math.sin(phi);
        }
        for (m = 0; m < 6; ++m) {
            phi = (double)m * 2.0 * Math.PI / 6.0;
            OmegaLightningRenderer.GLOW_COS[m] = (float)Math.cos(phi);
            OmegaLightningRenderer.GLOW_SIN[m] = (float)Math.sin(phi);
        }
        for (int k = 0; k <= 24; ++k) {
            double psi = (double)k * 2.0 * Math.PI / 24.0;
            OmegaLightningRenderer.RIPPLE_COS_PSI[k] = (float)Math.cos(psi);
            OmegaLightningRenderer.RIPPLE_SIN_PSI[k] = (float)Math.sin(psi);
        }
        int i = 0;
        while (i < 16) {
            float len;
            double angle = (double)i * 2.0 * Math.PI / 16.0;
            double elevAngle = ((double)(i % 4) - 1.5) * 0.3141592653589793;
            float cosElev = (float)Math.cos(elevAngle);
            float sinElev = (float)Math.sin(elevAngle);
            float cosAng = (float)Math.cos(angle);
            float sinAng = (float)Math.sin(angle);
            float ex = cosAng * cosElev * 50.0f;
            float ey = sinElev * 35.0f;
            float ez = sinAng * cosElev * 50.0f;
            OmegaLightningRenderer.EMITTERS[i] = new Vector3f(ex, ey, ez);
            float eLen = (float)Math.sqrt(ex * ex + ey * ey + ez * ez);
            float tx = ex / eLen * 18.0f;
            float ty = ey / eLen * 18.0f;
            float tz = ez / eLen * 18.0f;
            OmegaLightningRenderer.TARGETS[i] = new Vector3f(tx, ty, tz);
            OmegaLightningRenderer.SURFACE_NORMALS[i] = new Vector3f(tx / 18.0f, ty / 18.0f, tz / 18.0f);
            float dx = tx - ex;
            float dy = ty - ey;
            float dz = tz - ez;
            OmegaLightningRenderer.BEAM_LENGTHS[i] = len = (float)Math.sqrt(dx * dx + dy * dy + dz * dz);
            OmegaLightningRenderer.AXIS_DIR[i] = new Vector3f(dx /= len, dy /= len, dz /= len);
            Vector3f up = Math.abs(dy) < 0.9f ? new Vector3f(0.0f, 1.0f, 0.0f) : new Vector3f(1.0f, 0.0f, 0.0f);
            Vector3f u = new Vector3f();
            AXIS_DIR[i].cross((Vector3fc)up, u);
            u.normalize();
            Vector3f v = new Vector3f();
            AXIS_DIR[i].cross((Vector3fc)u, v);
            v.normalize();
            OmegaLightningRenderer.AXIS_U[i] = u;
            OmegaLightningRenderer.AXIS_V[i] = v;
            ++i;
        }
    }
}

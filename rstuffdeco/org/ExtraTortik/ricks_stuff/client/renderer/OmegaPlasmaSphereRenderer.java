/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.util.Mth
 *  org.ExtraTortik.ricks_stuff.client.renderer.OmegaPlasmaSphereRenderer$1
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
import org.ExtraTortik.ricks_stuff.client.renderer.OmegaPlasmaSphereRenderer;
import org.ExtraTortik.ricks_stuff.client.renderer.OmegaRenderTypes;
import org.ExtraTortik.ricks_stuff.client.util.FastNoise;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDevicePhase;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class OmegaPlasmaSphereRenderer {
    public static final float BASE_CORE_RADIUS = 18.0f;
    private static final int MANTLE_STACKS = 28;
    private static final int MANTLE_SLICES = 42;
    private static final int CLOUD_STACKS = 20;
    private static final int CLOUD_SLICES = 32;
    private static final int CORONA_STACKS = 16;
    private static final int CORONA_SLICES = 24;
    private static final int NUM_NODES = 80;
    private static final float[] NODE_X = new float[80];
    private static final float[] NODE_Y = new float[80];
    private static final float[] NODE_Z = new float[80];
    private static final float[] NODE_PHASE = new float[80];
    private static final float[] NODE_TANGENT_U_X = new float[80];
    private static final float[] NODE_TANGENT_U_Y = new float[80];
    private static final float[] NODE_TANGENT_U_Z = new float[80];
    private static final float[] NODE_TANGENT_V_X = new float[80];
    private static final float[] NODE_TANGENT_V_Y = new float[80];
    private static final float[] NODE_TANGENT_V_Z = new float[80];
    private static final int MAX_EDGES = 300;
    private static final int[] EDGE_U = new int[300];
    private static final int[] EDGE_V = new int[300];
    private static final float[] EDGE_PHASE = new float[300];
    private static int TOTAL_EDGES = 0;
    private static final int EDGE_SEGS = 8;
    private static final int NUM_CRAWLERS = 32;
    private static final int CRAWLER_SEGS = 6;
    private static final int NUM_CLOUD_LOBES = 24;
    private static final Vector3f[] CLOUD_LOBE_DIRS = new Vector3f[24];
    private static final Vector3f[] CLOUD_LOBE_U = new Vector3f[24];
    private static final Vector3f[] CLOUD_LOBE_V = new Vector3f[24];
    private static final float[] CLOUD_LOBE_PHASES = new float[24];
    private static final int MAX_SCRATCH_SEGS = 16;
    private static final float[] SCRATCH_PX = new float[17];
    private static final float[] SCRATCH_PY = new float[17];
    private static final float[] SCRATCH_PZ = new float[17];
    private static final float[] SCRATCH_LEFT_X = new float[17];
    private static final float[] SCRATCH_LEFT_Y = new float[17];
    private static final float[] SCRATCH_LEFT_Z = new float[17];
    private static final float[] SCRATCH_RIGHT_X = new float[17];
    private static final float[] SCRATCH_RIGHT_Y = new float[17];
    private static final float[] SCRATCH_RIGHT_Z = new float[17];
    private static final float[] SCRATCH_MID_LEFT_X = new float[17];
    private static final float[] SCRATCH_MID_LEFT_Y = new float[17];
    private static final float[] SCRATCH_MID_LEFT_Z = new float[17];
    private static final float[] SCRATCH_MID_RIGHT_X = new float[17];
    private static final float[] SCRATCH_MID_RIGHT_Y = new float[17];
    private static final float[] SCRATCH_MID_RIGHT_Z = new float[17];
    private static final float[] SCRATCH_OUT_LEFT_X = new float[17];
    private static final float[] SCRATCH_OUT_LEFT_Y = new float[17];
    private static final float[] SCRATCH_OUT_LEFT_Z = new float[17];
    private static final float[] SCRATCH_OUT_RIGHT_X = new float[17];
    private static final float[] SCRATCH_OUT_RIGHT_Y = new float[17];
    private static final float[] SCRATCH_OUT_RIGHT_Z = new float[17];

    public static void renderPlasmaSphere(OmegaDeviceEntity device, PoseStack poseStack, MultiBufferSource bufferSource, float partialTicks, int packedLight, int packedOverlay) {
        float cloudAge;
        float energyScale;
        if (device == null) {
            return;
        }
        OmegaDevicePhase phase = device.getPhase();
        if (phase == null) {
            phase = OmegaDevicePhase.IDLE;
        }
        if ((energyScale = device.getCoreEnergyScale()) <= 0.01f) {
            energyScale = 1.0f;
        }
        float overloadFactor = OmegaPlasmaSphereRenderer.getOverloadFactor(phase, device.getPhaseTicks(), partialTicks);
        float erasureFactor = OmegaPlasmaSphereRenderer.getErasureFactor(phase, device.getPhaseTicks(), partialTicks);
        float time = ((float)device.tickCount + partialTicks) * 0.085f;
        float subtleBreathing = 1.0f + 0.003f * Mth.sin((float)(time * 0.75f));
        float sphereRadius = 18.0f * energyScale * subtleBreathing;
        poseStack.pushPose();
        VertexConsumer mantleConsumer = bufferSource.getBuffer(OmegaRenderTypes.PLASMA_SOLID_BODY);
        OmegaPlasmaSphereRenderer.renderSolidPlasmaMantle(poseStack, mantleConsumer, sphereRadius, time, overloadFactor, erasureFactor);
        float coreRadiusMultiplier = 0.3f + 0.14f * overloadFactor + 0.1f * erasureFactor;
        VertexConsumer cloudConsumer = bufferSource.getBuffer(OmegaRenderTypes.PLASMA_TRANSLUCENT_BODY);
        OmegaPlasmaSphereRenderer.renderCosmicNebulaCore(poseStack, cloudConsumer, sphereRadius * coreRadiusMultiplier, time, overloadFactor, erasureFactor);
        VertexConsumer solidLightningConsumer = bufferSource.getBuffer(OmegaRenderTypes.PLASMA_SOLID_BODY);
        OmegaPlasmaSphereRenderer.renderNoiseLightningSolidCores(poseStack, solidLightningConsumer, sphereRadius * 1.002f, time, overloadFactor, erasureFactor);
        VertexConsumer glowLightningConsumer = bufferSource.getBuffer(OmegaRenderTypes.PLASMA_ADDITIVE_GLOW);
        OmegaPlasmaSphereRenderer.renderNoiseLightningGlowSheaths(poseStack, glowLightningConsumer, sphereRadius * 1.003f, time, overloadFactor, erasureFactor);
        OmegaPlasmaSphereRenderer.renderLightningJunctionSparks(poseStack, glowLightningConsumer, sphereRadius * 1.004f, time, overloadFactor, erasureFactor);
        VertexConsumer coronaConsumer = bufferSource.getBuffer(OmegaRenderTypes.PLASMA_ADDITIVE_GLOW);
        OmegaPlasmaSphereRenderer.renderSubtleCoronaHalo(poseStack, coronaConsumer, sphereRadius, overloadFactor, erasureFactor);
        if (phase == OmegaDevicePhase.ERASING && (cloudAge = (float)device.getPhaseTicks() + partialTicks) <= 45.0f) {
            OmegaPlasmaSphereRenderer.renderExpandingGreenCloudBurst(poseStack, bufferSource, cloudAge);
        }
        poseStack.popPose();
    }

    private static void renderSolidPlasmaMantle(PoseStack poseStack, VertexConsumer consumer, float radius, float time, float overloadFactor, float erasureFactor) {
        Matrix4f mat = poseStack.last().pose();
        int j = 0;
        while (j < 28) {
            float theta1 = (float)(Math.PI * (double)j / 28.0);
            float theta2 = (float)(Math.PI * (double)(j + 1) / 28.0);
            float y1 = radius * (float)Math.cos(theta1);
            float y2 = radius * (float)Math.cos(theta2);
            float r1 = radius * (float)Math.sin(theta1);
            float r2 = radius * (float)Math.sin(theta2);
            float ny1 = (float)Math.cos(theta1);
            float ny2 = (float)Math.cos(theta2);
            float nr1 = (float)Math.sin(theta1);
            float nr2 = (float)Math.sin(theta2);
            for (int i = 0; i < 42; ++i) {
                float phi1 = (float)(Math.PI * 2 * (double)i / 42.0);
                float phi2 = (float)(Math.PI * 2 * (double)(i + 1) / 42.0);
                float cosPhi1 = (float)Math.cos(phi1);
                float sinPhi1 = (float)Math.sin(phi1);
                float cosPhi2 = (float)Math.cos(phi2);
                float sinPhi2 = (float)Math.sin(phi2);
                float x11 = r1 * cosPhi1;
                float z11 = r1 * sinPhi1;
                float x21 = r2 * cosPhi1;
                float z21 = r2 * sinPhi1;
                float x22 = r2 * cosPhi2;
                float z22 = r2 * sinPhi2;
                float x12 = r1 * cosPhi2;
                float z12 = r1 * sinPhi2;
                int col11 = OmegaPlasmaSphereRenderer.computeSolidPlasmaColor(nr1 * cosPhi1, ny1, nr1 * sinPhi1, time, overloadFactor, erasureFactor);
                int col21 = OmegaPlasmaSphereRenderer.computeSolidPlasmaColor(nr2 * cosPhi1, ny2, nr2 * sinPhi1, time, overloadFactor, erasureFactor);
                int col22 = OmegaPlasmaSphereRenderer.computeSolidPlasmaColor(nr2 * cosPhi2, ny2, nr2 * sinPhi2, time, overloadFactor, erasureFactor);
                int col12 = OmegaPlasmaSphereRenderer.computeSolidPlasmaColor(nr1 * cosPhi2, ny1, nr1 * sinPhi2, time, overloadFactor, erasureFactor);
                consumer.addVertex(mat, x11, y1, z11).setColor(col11 >> 16 & 0xFF, col11 >> 8 & 0xFF, col11 & 0xFF, col11 >> 24 & 0xFF);
                consumer.addVertex(mat, x21, y2, z21).setColor(col21 >> 16 & 0xFF, col21 >> 8 & 0xFF, col21 & 0xFF, col21 >> 24 & 0xFF);
                consumer.addVertex(mat, x22, y2, z22).setColor(col22 >> 16 & 0xFF, col22 >> 8 & 0xFF, col22 & 0xFF, col22 >> 24 & 0xFF);
                consumer.addVertex(mat, x12, y1, z12).setColor(col12 >> 16 & 0xFF, col12 >> 8 & 0xFF, col12 & 0xFF, col12 >> 24 & 0xFF);
            }
            ++j;
        }
    }

    private static int computeSolidPlasmaColor(float nx, float ny, float nz, float time, float overloadFactor, float erasureFactor) {
        float lightDot = Math.max(0.0f, nx * 0.35f + ny * 0.85f + nz * 0.38f);
        float diffuse = 0.82f + 0.18f * lightDot;
        float specular = (float)Math.pow(lightDot, 10.0) * (0.45f + 0.35f * overloadFactor);
        float fresnel = (float)Math.pow(1.0f - Math.min(1.0f, Math.abs(nz)), 2.2f);
        float veinNoise = FastNoise.ridged3D((float)(nx * 2.8f), (float)(ny * 2.8f + time * 0.35f), (float)(nz * 2.8f), (int)2, (float)2.0f, (float)0.5f);
        float cellularGlow = FastNoise.voronoiRidge3D((float)(nx * 3.5f - time * 0.25f), (float)(ny * 3.5f), (float)(nz * 3.5f + time * 0.2f), (float)0.85f);
        float convection = FastNoise.simplex3D((float)(nx * 4.5f + time * 0.5f), (float)(ny * 4.5f - time * 0.4f), (float)(nz * 4.5f));
        float energyVeins = veinNoise * 0.28f + cellularGlow * 0.22f + convection * 0.12f;
        float glowMod = 1.0f + energyVeins;
        float baseR = (48.0f + 120.0f * lightDot + 40.0f * overloadFactor + 60.0f * veinNoise) * glowMod;
        float baseG = (210.0f + 45.0f * lightDot + 30.0f * cellularGlow) * glowMod;
        float baseB = 255.0f;
        float standardR = Mth.clamp((float)(baseR * diffuse + specular * 255.0f + fresnel * 85.0f), (float)0.0f, (float)255.0f);
        float standardG = Mth.clamp((float)(baseG * diffuse + specular * 255.0f + fresnel * 37.0f), (float)0.0f, (float)255.0f);
        float standardB = 255.0f;
        float greenBaseR = (40.0f + 50.0f * lightDot + 45.0f * veinNoise) * glowMod;
        float greenBaseG = 255.0f;
        float greenBaseB = (18.0f + 35.0f * lightDot + 25.0f * cellularGlow) * glowMod;
        float greenR = Mth.clamp((float)(greenBaseR * diffuse + specular * 220.0f + fresnel * 70.0f), (float)0.0f, (float)255.0f);
        float greenG = Mth.clamp((float)(greenBaseG * diffuse + specular * 255.0f + fresnel * 40.0f), (float)0.0f, (float)255.0f);
        float greenB = Mth.clamp((float)(greenBaseB * diffuse + specular * 180.0f + fresnel * 30.0f), (float)0.0f, (float)255.0f);
        int r = (int)Mth.lerp((float)erasureFactor, (float)standardR, (float)greenR);
        int g = (int)Mth.lerp((float)erasureFactor, (float)standardG, (float)greenG);
        int b = (int)Mth.lerp((float)erasureFactor, (float)standardB, (float)greenB);
        int a = (int)Mth.clamp((float)(240.0f + 15.0f * fresnel), (float)235.0f, (float)255.0f);
        return a << 24 | r << 16 | g << 8 | b;
    }

    private static void renderCosmicNebulaCore(PoseStack poseStack, VertexConsumer consumer, float radius, float time, float overloadFactor, float erasureFactor) {
        Matrix4f mat = poseStack.last().pose();
        int j = 0;
        while (j < 20) {
            float theta1 = (float)(Math.PI * (double)j / 20.0);
            float theta2 = (float)(Math.PI * (double)(j + 1) / 20.0);
            float ny1 = (float)Math.cos(theta1);
            float ny2 = (float)Math.cos(theta2);
            float nr1 = (float)Math.sin(theta1);
            float nr2 = (float)Math.sin(theta2);
            for (int i = 0; i < 32; ++i) {
                float phi1 = (float)(Math.PI * 2 * (double)i / 32.0);
                float phi2 = (float)(Math.PI * 2 * (double)(i + 1) / 32.0);
                float cosPhi1 = (float)Math.cos(phi1);
                float sinPhi1 = (float)Math.sin(phi1);
                float cosPhi2 = (float)Math.cos(phi2);
                float sinPhi2 = (float)Math.sin(phi2);
                float nx11 = nr1 * cosPhi1;
                float nz11 = nr1 * sinPhi1;
                float nx21 = nr2 * cosPhi1;
                float nz21 = nr2 * sinPhi1;
                float nx22 = nr2 * cosPhi2;
                float nz22 = nr2 * sinPhi2;
                float nx12 = nr1 * cosPhi2;
                float nz12 = nr1 * sinPhi2;
                float r11 = OmegaPlasmaSphereRenderer.computeFluidRadius(radius, nx11, ny1, nz11, time);
                float r21 = OmegaPlasmaSphereRenderer.computeFluidRadius(radius, nx21, ny2, nz21, time);
                float r22 = OmegaPlasmaSphereRenderer.computeFluidRadius(radius, nx22, ny2, nz22, time);
                float r12 = OmegaPlasmaSphereRenderer.computeFluidRadius(radius, nx12, ny1, nz12, time);
                int col11 = OmegaPlasmaSphereRenderer.computeNebulaColor(nx11, ny1, nz11, time, overloadFactor, erasureFactor);
                int col21 = OmegaPlasmaSphereRenderer.computeNebulaColor(nx21, ny2, nz21, time, overloadFactor, erasureFactor);
                int col22 = OmegaPlasmaSphereRenderer.computeNebulaColor(nx22, ny2, nz22, time, overloadFactor, erasureFactor);
                int col12 = OmegaPlasmaSphereRenderer.computeNebulaColor(nx12, ny1, nz12, time, overloadFactor, erasureFactor);
                consumer.addVertex(mat, r11 * nx11, r11 * ny1, r11 * nz11).setColor(col11 >> 16 & 0xFF, col11 >> 8 & 0xFF, col11 & 0xFF, col11 >> 24 & 0xFF);
                consumer.addVertex(mat, r21 * nx21, r21 * ny2, r21 * nz21).setColor(col21 >> 16 & 0xFF, col21 >> 8 & 0xFF, col21 & 0xFF, col21 >> 24 & 0xFF);
                consumer.addVertex(mat, r22 * nx22, r22 * ny2, r22 * nz22).setColor(col22 >> 16 & 0xFF, col22 >> 8 & 0xFF, col22 & 0xFF, col22 >> 24 & 0xFF);
                consumer.addVertex(mat, r12 * nx12, r12 * ny1, r12 * nz12).setColor(col12 >> 16 & 0xFF, col12 >> 8 & 0xFF, col12 & 0xFF, col12 >> 24 & 0xFF);
            }
            ++j;
        }
    }

    private static float computeFluidRadius(float baseR, float nx, float ny, float nz, float time) {
        float h1 = Mth.sin((float)(2.2f * nx + 1.8f * ny + 1.2f * time));
        float h2 = Mth.cos((float)(2.0f * ny - 2.4f * nz - 1.0f * time));
        float h3 = Mth.sin((float)(2.8f * nz + 1.8f * nx + 1.5f * time));
        float disp = h1 * 0.16f + h2 * 0.14f + h3 * 0.1f;
        return baseR * (1.0f + disp);
    }

    private static int computeNebulaColor(float nx, float ny, float nz, float time, float overloadFactor, float erasureFactor) {
        float turbulence = 0.5f + 0.5f * Mth.sin((float)(2.2f * nx - 1.6f * ny + 2.0f * nz + time * 1.4f));
        float centerFalloff = Math.max(0.0f, nz);
        float idleR = 42.0f + 35.0f * turbulence;
        float idleG = 85.0f + 50.0f * turbulence;
        float idleB = 185.0f + 35.0f * turbulence;
        float activeR = 190.0f + 55.0f * centerFalloff + 10.0f * turbulence;
        float activeG = 85.0f + 100.0f * centerFalloff + 25.0f * turbulence;
        float activeB = 245.0f + 10.0f * centerFalloff;
        float stdR = Mth.lerp((float)overloadFactor, (float)idleR, (float)activeR);
        float stdG = Mth.lerp((float)overloadFactor, (float)idleG, (float)activeG);
        float stdB = Mth.lerp((float)overloadFactor, (float)idleB, (float)activeB);
        float greenR = 80.0f + 60.0f * centerFalloff + 30.0f * turbulence;
        float greenG = 240.0f + 15.0f * centerFalloff;
        float greenB = 20.0f + 30.0f * centerFalloff;
        int r = (int)Mth.lerp((float)erasureFactor, (float)stdR, (float)greenR);
        int g = (int)Mth.lerp((float)erasureFactor, (float)stdG, (float)greenG);
        int b = (int)Mth.lerp((float)erasureFactor, (float)stdB, (float)greenB);
        int a = (int)Mth.clamp((float)Mth.lerp((float)Math.max(overloadFactor, erasureFactor), (float)(75.0f + 55.0f * centerFalloff * (0.8f + 0.2f * turbulence)), (float)(120.0f + 100.0f * centerFalloff * (0.8f + 0.2f * turbulence))), (float)35.0f, (float)240.0f);
        return a << 24 | r << 16 | g << 8 | b;
    }

    private static void renderNoiseLightningSolidCores(PoseStack poseStack, VertexConsumer consumer, float radius, float time, float overloadFactor, float erasureFactor) {
        Matrix4f mat = poseStack.last().pose();
        float rotY = time * 14.0f;
        float rotX = 7.0f * Mth.sin((float)(time * 0.6f));
        float rotZ = 5.0f * Mth.cos((float)(time * 0.5f));
        float cosY = Mth.cos((float)(rotY * ((float)Math.PI / 180)));
        float sinY = Mth.sin((float)(rotY * ((float)Math.PI / 180)));
        float cosX = Mth.cos((float)(rotX * ((float)Math.PI / 180)));
        float sinX = Mth.sin((float)(rotX * ((float)Math.PI / 180)));
        float cosZ = Mth.cos((float)(rotZ * ((float)Math.PI / 180)));
        float sinZ = Mth.sin((float)(rotZ * ((float)Math.PI / 180)));
        float baseCoreHalfWidth = 0.016f + 0.007f * overloadFactor + 0.009f * erasureFactor;
        int e = 0;
        while (true) {
            int k;
            if (e >= TOTAL_EDGES) {
                OmegaPlasmaSphereRenderer.renderDynamicSurfaceSparksCore(mat, consumer, radius, time, overloadFactor, erasureFactor, cosX, sinX, cosY, sinY, cosZ, sinZ, baseCoreHalfWidth);
                return;
            }
            int u = EDGE_U[e];
            int v = EDGE_V[e];
            float phiE = EDGE_PHASE[e];
            float ux = NODE_X[u];
            float uy = NODE_Y[u];
            float uz = NODE_Z[u];
            float vx = NODE_X[v];
            float vy = NODE_Y[v];
            float vz = NODE_Z[v];
            float midX = (ux + vx) * 0.5f;
            float midY = (uy + vy) * 0.5f;
            float midZ = (uz + vz) * 0.5f;
            float edgeActivity = FastNoise.simplex3D((float)(midX * 2.5f + time * 2.0f), (float)(midY * 2.5f - time * 1.7f), (float)(midZ * 2.5f + phiE));
            float activity = Mth.clamp((float)(edgeActivity * 0.75f + 0.65f + 0.2f * overloadFactor + 0.25f * erasureFactor), (float)0.2f, (float)1.25f);
            float coreWidth = baseCoreHalfWidth * activity;
            int alpha = (int)Mth.clamp((float)(255.0f * activity), (float)160.0f, (float)255.0f);
            for (k = 0; k <= 8; ++k) {
                float t = (float)k / 8.0f;
                float p0x = (1.0f - t) * ux + t * vx;
                float p0y = (1.0f - t) * uy + t * vy;
                float p0z = (1.0f - t) * uz + t * vz;
                float p0Len = (float)Math.sqrt(p0x * p0x + p0y * p0y + p0z * p0z);
                p0x /= p0Len;
                p0y /= p0Len;
                p0z /= p0Len;
                float w = Mth.sin((float)(t * (float)Math.PI));
                float tz = vz - uz;
                float ty = vy - uy;
                float bx = p0y * tz - p0z * ty;
                float tx = vx - ux;
                float by = p0z * tx - p0x * tz;
                float bz = p0x * ty - p0y * tx;
                float bLen = (float)Math.sqrt(bx * bx + by * by + bz * bz);
                if (bLen > 1.0E-5f) {
                    bx /= bLen;
                    by /= bLen;
                    bz /= bLen;
                }
                float nx = p0x * 3.8f + time * 1.6f + phiE;
                float ny = p0y * 3.8f - time * 1.8f + phiE * 1.3f;
                float nz = p0z * 3.8f + time * 1.4f + phiE * 0.7f;
                float dispLat = FastNoise.fbm3D((float)nx, (float)ny, (float)nz, (int)2, (float)2.0f, (float)0.5f) * 0.12f * w;
                float jitter = FastNoise.simplex3D((float)(p0x * 12.0f + time * 4.0f), (float)(p0y * 12.0f - time * 4.5f), (float)(p0z * 12.0f + phiE * 5.0f)) * 0.035f * w;
                float dispOut = Math.abs(FastNoise.simplex3D((float)(nx + 13.5f), (float)(ny + 47.2f), (float)(nz + 91.8f))) * 0.04f * w;
                float px = p0x + bx * (dispLat + jitter) + p0x * dispOut;
                float py = p0y + by * (dispLat + jitter) + p0y * dispOut;
                float pz = p0z + bz * (dispLat + jitter) + p0z * dispOut;
                OmegaPlasmaSphereRenderer.SCRATCH_PX[k] = OmegaPlasmaSphereRenderer.rotateX(px, py, pz, cosX, sinX, cosY, sinY, cosZ, sinZ);
                OmegaPlasmaSphereRenderer.SCRATCH_PY[k] = OmegaPlasmaSphereRenderer.rotateY(px, py, pz, cosX, sinX, cosY, sinY, cosZ, sinZ);
                OmegaPlasmaSphereRenderer.SCRATCH_PZ[k] = OmegaPlasmaSphereRenderer.rotateZ(px, py, pz, cosX, sinX, cosY, sinY, cosZ, sinZ);
            }
            for (k = 0; k <= 8; ++k) {
                float rbz;
                float px;
                float rby;
                float pz;
                float py;
                float rbx;
                float rbLen;
                float segTz;
                float segTy;
                float segTx;
                if (k == 0) {
                    segTx = SCRATCH_PX[1] - SCRATCH_PX[0];
                    segTy = SCRATCH_PY[1] - SCRATCH_PY[0];
                    segTz = SCRATCH_PZ[1] - SCRATCH_PZ[0];
                } else if (k == 8) {
                    segTx = SCRATCH_PX[8] - SCRATCH_PX[7];
                    segTy = SCRATCH_PY[8] - SCRATCH_PY[7];
                    segTz = SCRATCH_PZ[8] - SCRATCH_PZ[7];
                } else {
                    segTx = SCRATCH_PX[k + 1] - SCRATCH_PX[k - 1];
                    segTy = SCRATCH_PY[k + 1] - SCRATCH_PY[k - 1];
                    segTz = SCRATCH_PZ[k + 1] - SCRATCH_PZ[k - 1];
                }
                float segTLen = (float)Math.sqrt(segTx * segTx + segTy * segTy + segTz * segTz);
                if (segTLen > 1.0E-5f) {
                    segTx /= segTLen;
                    segTy /= segTLen;
                    segTz /= segTLen;
                }
                if ((rbLen = (float)Math.sqrt((rbx = (py = SCRATCH_PY[k]) * segTz - (pz = SCRATCH_PZ[k]) * segTy) * rbx + (rby = pz * segTx - (px = SCRATCH_PX[k]) * segTz) * rby + (rbz = px * segTy - py * segTx) * rbz)) > 1.0E-5f) {
                    rbx /= rbLen;
                    rby /= rbLen;
                    rbz /= rbLen;
                }
                float cx = px * radius;
                float cy = py * radius;
                float cz = pz * radius;
                OmegaPlasmaSphereRenderer.SCRATCH_LEFT_X[k] = cx - rbx * coreWidth;
                OmegaPlasmaSphereRenderer.SCRATCH_LEFT_Y[k] = cy - rby * coreWidth;
                OmegaPlasmaSphereRenderer.SCRATCH_LEFT_Z[k] = cz - rbz * coreWidth;
                OmegaPlasmaSphereRenderer.SCRATCH_RIGHT_X[k] = cx + rbx * coreWidth;
                OmegaPlasmaSphereRenderer.SCRATCH_RIGHT_Y[k] = cy + rby * coreWidth;
                OmegaPlasmaSphereRenderer.SCRATCH_RIGHT_Z[k] = cz + rbz * coreWidth;
            }
            for (k = 0; k < 8; ++k) {
                consumer.addVertex(mat, SCRATCH_LEFT_X[k], SCRATCH_LEFT_Y[k], SCRATCH_LEFT_Z[k]).setColor(255, 255, 255, alpha);
                consumer.addVertex(mat, SCRATCH_RIGHT_X[k], SCRATCH_RIGHT_Y[k], SCRATCH_RIGHT_Z[k]).setColor(255, 255, 255, alpha);
                consumer.addVertex(mat, SCRATCH_RIGHT_X[k + 1], SCRATCH_RIGHT_Y[k + 1], SCRATCH_RIGHT_Z[k + 1]).setColor(255, 255, 255, alpha);
                consumer.addVertex(mat, SCRATCH_LEFT_X[k + 1], SCRATCH_LEFT_Y[k + 1], SCRATCH_LEFT_Z[k + 1]).setColor(255, 255, 255, alpha);
            }
            ++e;
        }
    }

    private static void renderNoiseLightningGlowSheaths(PoseStack poseStack, VertexConsumer consumer, float radius, float time, float overloadFactor, float erasureFactor) {
        Matrix4f mat = poseStack.last().pose();
        float rotY = time * 14.0f;
        float rotX = 7.0f * Mth.sin((float)(time * 0.6f));
        float rotZ = 5.0f * Mth.cos((float)(time * 0.5f));
        float cosY = Mth.cos((float)(rotY * ((float)Math.PI / 180)));
        float sinY = Mth.sin((float)(rotY * ((float)Math.PI / 180)));
        float cosX = Mth.cos((float)(rotX * ((float)Math.PI / 180)));
        float sinX = Mth.sin((float)(rotX * ((float)Math.PI / 180)));
        float cosZ = Mth.cos((float)(rotZ * ((float)Math.PI / 180)));
        float sinZ = Mth.sin((float)(rotZ * ((float)Math.PI / 180)));
        float baseMidHalfWidth = 0.055f + 0.025f * overloadFactor + 0.03f * erasureFactor;
        float baseOutHalfWidth = 0.14f + 0.06f * overloadFactor + 0.07f * erasureFactor;
        int stdMidR = (int)Mth.lerp((float)overloadFactor, (float)160.0f, (float)225.0f);
        int stdMidG = (int)Mth.lerp((float)overloadFactor, (float)240.0f, (float)175.0f);
        int stdMidB = 255;
        int stdOutR = (int)Mth.lerp((float)overloadFactor, (float)80.0f, (float)180.0f);
        int stdOutG = (int)Mth.lerp((float)overloadFactor, (float)200.0f, (float)100.0f);
        int stdOutB = 255;
        int midR = (int)Mth.lerp((float)erasureFactor, (float)stdMidR, (float)120.0f);
        int midG = (int)Mth.lerp((float)erasureFactor, (float)stdMidG, (float)255.0f);
        int midB = (int)Mth.lerp((float)erasureFactor, (float)stdMidB, (float)35.0f);
        int outR = (int)Mth.lerp((float)erasureFactor, (float)stdOutR, (float)50.0f);
        int outG = (int)Mth.lerp((float)erasureFactor, (float)stdOutG, (float)235.0f);
        int outB = (int)Mth.lerp((float)erasureFactor, (float)stdOutB, (float)15.0f);
        int e = 0;
        while (true) {
            int k;
            if (e >= TOTAL_EDGES) {
                OmegaPlasmaSphereRenderer.renderDynamicSurfaceSparksGlow(mat, consumer, radius, time, overloadFactor, erasureFactor, cosX, sinX, cosY, sinY, cosZ, sinZ, baseMidHalfWidth, baseOutHalfWidth, midR, midG, midB, outR, outG, outB);
                return;
            }
            int u = EDGE_U[e];
            int v = EDGE_V[e];
            float phiE = EDGE_PHASE[e];
            float ux = NODE_X[u];
            float uy = NODE_Y[u];
            float uz = NODE_Z[u];
            float vx = NODE_X[v];
            float vy = NODE_Y[v];
            float vz = NODE_Z[v];
            float midX = (ux + vx) * 0.5f;
            float midY = (uy + vy) * 0.5f;
            float midZ = (uz + vz) * 0.5f;
            float edgeActivity = FastNoise.simplex3D((float)(midX * 2.5f + time * 2.0f), (float)(midY * 2.5f - time * 1.7f), (float)(midZ * 2.5f + phiE));
            float activity = Mth.clamp((float)(edgeActivity * 0.75f + 0.65f + 0.2f * overloadFactor + 0.25f * erasureFactor), (float)0.2f, (float)1.25f);
            float midWidth = baseMidHalfWidth * activity;
            float outWidth = baseOutHalfWidth * activity;
            int midAlpha = (int)Mth.clamp((float)(175.0f * activity), (float)70.0f, (float)220.0f);
            int outAlpha = (int)Mth.clamp((float)(80.0f * activity), (float)30.0f, (float)110.0f);
            for (k = 0; k <= 8; ++k) {
                float t = (float)k / 8.0f;
                float p0x = (1.0f - t) * ux + t * vx;
                float p0y = (1.0f - t) * uy + t * vy;
                float p0z = (1.0f - t) * uz + t * vz;
                float p0Len = (float)Math.sqrt(p0x * p0x + p0y * p0y + p0z * p0z);
                p0x /= p0Len;
                p0y /= p0Len;
                p0z /= p0Len;
                float w = Mth.sin((float)(t * (float)Math.PI));
                float tz = vz - uz;
                float ty = vy - uy;
                float bx = p0y * tz - p0z * ty;
                float tx = vx - ux;
                float by = p0z * tx - p0x * tz;
                float bz = p0x * ty - p0y * tx;
                float bLen = (float)Math.sqrt(bx * bx + by * by + bz * bz);
                if (bLen > 1.0E-5f) {
                    bx /= bLen;
                    by /= bLen;
                    bz /= bLen;
                }
                float nx = p0x * 3.8f + time * 1.6f + phiE;
                float ny = p0y * 3.8f - time * 1.8f + phiE * 1.3f;
                float nz = p0z * 3.8f + time * 1.4f + phiE * 0.7f;
                float dispLat = FastNoise.fbm3D((float)nx, (float)ny, (float)nz, (int)2, (float)2.0f, (float)0.5f) * 0.12f * w;
                float jitter = FastNoise.simplex3D((float)(p0x * 12.0f + time * 4.0f), (float)(p0y * 12.0f - time * 4.5f), (float)(p0z * 12.0f + phiE * 5.0f)) * 0.035f * w;
                float dispOut = Math.abs(FastNoise.simplex3D((float)(nx + 13.5f), (float)(ny + 47.2f), (float)(nz + 91.8f))) * 0.04f * w;
                float px = p0x + bx * (dispLat + jitter) + p0x * dispOut;
                float py = p0y + by * (dispLat + jitter) + p0y * dispOut;
                float pz = p0z + bz * (dispLat + jitter) + p0z * dispOut;
                OmegaPlasmaSphereRenderer.SCRATCH_PX[k] = OmegaPlasmaSphereRenderer.rotateX(px, py, pz, cosX, sinX, cosY, sinY, cosZ, sinZ);
                OmegaPlasmaSphereRenderer.SCRATCH_PY[k] = OmegaPlasmaSphereRenderer.rotateY(px, py, pz, cosX, sinX, cosY, sinY, cosZ, sinZ);
                OmegaPlasmaSphereRenderer.SCRATCH_PZ[k] = OmegaPlasmaSphereRenderer.rotateZ(px, py, pz, cosX, sinX, cosY, sinY, cosZ, sinZ);
            }
            for (k = 0; k <= 8; ++k) {
                float rbz;
                float px;
                float rby;
                float pz;
                float py;
                float rbx;
                float rbLen;
                float segTz;
                float segTy;
                float segTx;
                if (k == 0) {
                    segTx = SCRATCH_PX[1] - SCRATCH_PX[0];
                    segTy = SCRATCH_PY[1] - SCRATCH_PY[0];
                    segTz = SCRATCH_PZ[1] - SCRATCH_PZ[0];
                } else if (k == 8) {
                    segTx = SCRATCH_PX[8] - SCRATCH_PX[7];
                    segTy = SCRATCH_PY[8] - SCRATCH_PY[7];
                    segTz = SCRATCH_PZ[8] - SCRATCH_PZ[7];
                } else {
                    segTx = SCRATCH_PX[k + 1] - SCRATCH_PX[k - 1];
                    segTy = SCRATCH_PY[k + 1] - SCRATCH_PY[k - 1];
                    segTz = SCRATCH_PZ[k + 1] - SCRATCH_PZ[k - 1];
                }
                float segTLen = (float)Math.sqrt(segTx * segTx + segTy * segTy + segTz * segTz);
                if (segTLen > 1.0E-5f) {
                    segTx /= segTLen;
                    segTy /= segTLen;
                    segTz /= segTLen;
                }
                if ((rbLen = (float)Math.sqrt((rbx = (py = SCRATCH_PY[k]) * segTz - (pz = SCRATCH_PZ[k]) * segTy) * rbx + (rby = pz * segTx - (px = SCRATCH_PX[k]) * segTz) * rby + (rbz = px * segTy - py * segTx) * rbz)) > 1.0E-5f) {
                    rbx /= rbLen;
                    rby /= rbLen;
                    rbz /= rbLen;
                }
                float cx = px * radius;
                float cy = py * radius;
                float cz = pz * radius;
                OmegaPlasmaSphereRenderer.SCRATCH_MID_LEFT_X[k] = cx - rbx * midWidth;
                OmegaPlasmaSphereRenderer.SCRATCH_MID_LEFT_Y[k] = cy - rby * midWidth;
                OmegaPlasmaSphereRenderer.SCRATCH_MID_LEFT_Z[k] = cz - rbz * midWidth;
                OmegaPlasmaSphereRenderer.SCRATCH_MID_RIGHT_X[k] = cx + rbx * midWidth;
                OmegaPlasmaSphereRenderer.SCRATCH_MID_RIGHT_Y[k] = cy + rby * midWidth;
                OmegaPlasmaSphereRenderer.SCRATCH_MID_RIGHT_Z[k] = cz + rbz * midWidth;
                OmegaPlasmaSphereRenderer.SCRATCH_OUT_LEFT_X[k] = cx - rbx * outWidth;
                OmegaPlasmaSphereRenderer.SCRATCH_OUT_LEFT_Y[k] = cy - rby * outWidth;
                OmegaPlasmaSphereRenderer.SCRATCH_OUT_LEFT_Z[k] = cz - rbz * outWidth;
                OmegaPlasmaSphereRenderer.SCRATCH_OUT_RIGHT_X[k] = cx + rbx * outWidth;
                OmegaPlasmaSphereRenderer.SCRATCH_OUT_RIGHT_Y[k] = cy + rby * outWidth;
                OmegaPlasmaSphereRenderer.SCRATCH_OUT_RIGHT_Z[k] = cz + rbz * outWidth;
            }
            for (k = 0; k < 8; ++k) {
                consumer.addVertex(mat, SCRATCH_MID_LEFT_X[k], SCRATCH_MID_LEFT_Y[k], SCRATCH_MID_LEFT_Z[k]).setColor(midR, midG, midB, midAlpha);
                consumer.addVertex(mat, SCRATCH_MID_RIGHT_X[k], SCRATCH_MID_RIGHT_Y[k], SCRATCH_MID_RIGHT_Z[k]).setColor(midR, midG, midB, midAlpha);
                consumer.addVertex(mat, SCRATCH_MID_RIGHT_X[k + 1], SCRATCH_MID_RIGHT_Y[k + 1], SCRATCH_MID_RIGHT_Z[k + 1]).setColor(midR, midG, midB, midAlpha);
                consumer.addVertex(mat, SCRATCH_MID_LEFT_X[k + 1], SCRATCH_MID_LEFT_Y[k + 1], SCRATCH_MID_LEFT_Z[k + 1]).setColor(midR, midG, midB, midAlpha);
                consumer.addVertex(mat, SCRATCH_OUT_LEFT_X[k], SCRATCH_OUT_LEFT_Y[k], SCRATCH_OUT_LEFT_Z[k]).setColor(outR, outG, outB, outAlpha);
                consumer.addVertex(mat, SCRATCH_OUT_RIGHT_X[k], SCRATCH_OUT_RIGHT_Y[k], SCRATCH_OUT_RIGHT_Z[k]).setColor(outR, outG, outB, outAlpha);
                consumer.addVertex(mat, SCRATCH_OUT_RIGHT_X[k + 1], SCRATCH_OUT_RIGHT_Y[k + 1], SCRATCH_OUT_RIGHT_Z[k + 1]).setColor(outR, outG, outB, outAlpha);
                consumer.addVertex(mat, SCRATCH_OUT_LEFT_X[k + 1], SCRATCH_OUT_LEFT_Y[k + 1], SCRATCH_OUT_LEFT_Z[k + 1]).setColor(outR, outG, outB, outAlpha);
            }
            ++e;
        }
    }

    private static void renderDynamicSurfaceSparksCore(Matrix4f mat, VertexConsumer consumer, float radius, float time, float overloadFactor, float erasureFactor, float cosX, float sinX, float cosY, float sinY, float cosZ, float sinZ, float baseCoreHalfWidth) {
        int c = 0;
        while (c < 32) {
            float crawlerTime = time * 2.2f + (float)c * 1.731f;
            float life = crawlerTime - (float)Math.floor(crawlerTime);
            float crawlerAlpha = Mth.sin((float)(life * (float)Math.PI));
            if (!(crawlerAlpha <= 0.05f)) {
                int s;
                int startNode = c * 7 % 80;
                float sx = NODE_X[startNode];
                float sy = NODE_Y[startNode];
                float sz = NODE_Z[startNode];
                float ux = NODE_TANGENT_U_X[startNode];
                float uy = NODE_TANGENT_U_Y[startNode];
                float uz = NODE_TANGENT_U_Z[startNode];
                float vx = NODE_TANGENT_V_X[startNode];
                float vy = NODE_TANGENT_V_Y[startNode];
                float vz = NODE_TANGENT_V_Z[startNode];
                float curlAngle = FastNoise.simplex3D((float)(sx * 3.0f + crawlerTime * 0.4f), (float)(sy * 3.0f), (float)(sz * 3.0f), (int)(c * 101)) * (float)Math.PI * 2.0f;
                float cosCurl = Mth.cos((float)curlAngle);
                float sinCurl = Mth.sin((float)curlAngle);
                float dx = cosCurl * ux + sinCurl * vx;
                float dy = cosCurl * uy + sinCurl * vy;
                float dz = cosCurl * uz + sinCurl * vz;
                float perpX = -sinCurl * ux + cosCurl * vx;
                float perpY = -sinCurl * uy + cosCurl * vy;
                float perpZ = -sinCurl * uz + cosCurl * vz;
                float coreWidth = baseCoreHalfWidth * 0.85f;
                int alpha = (int)(255.0f * crawlerAlpha);
                for (s = 0; s <= 6; ++s) {
                    float frac = (float)s / 6.0f;
                    float stepDist = frac * 0.5f;
                    float px = sx + dx * stepDist;
                    float py = sy + dy * stepDist;
                    float pz = sz + dz * stepDist;
                    float pLen = (float)Math.sqrt(px * px + py * py + pz * pz);
                    float jag = FastNoise.ridged3D((float)((px /= pLen) * 14.0f + crawlerTime * 3.0f), (float)((py /= pLen) * 14.0f), (float)((pz /= pLen) * 14.0f), (int)(c * 31337), (int)2, (float)2.0f, (float)0.5f) * 0.08f * (1.0f - frac * 0.25f);
                    OmegaPlasmaSphereRenderer.SCRATCH_PX[s] = OmegaPlasmaSphereRenderer.rotateX(px += perpX * jag, py += perpY * jag, pz += perpZ * jag, cosX, sinX, cosY, sinY, cosZ, sinZ);
                    OmegaPlasmaSphereRenderer.SCRATCH_PY[s] = OmegaPlasmaSphereRenderer.rotateY(px, py, pz, cosX, sinX, cosY, sinY, cosZ, sinZ);
                    OmegaPlasmaSphereRenderer.SCRATCH_PZ[s] = OmegaPlasmaSphereRenderer.rotateZ(px, py, pz, cosX, sinX, cosY, sinY, cosZ, sinZ);
                }
                for (s = 0; s <= 6; ++s) {
                    float rbz;
                    float px;
                    float rby;
                    float pz;
                    float py;
                    float rbx;
                    float rbLen;
                    float segTz;
                    float segTy;
                    float segTx;
                    if (s == 0) {
                        segTx = SCRATCH_PX[1] - SCRATCH_PX[0];
                        segTy = SCRATCH_PY[1] - SCRATCH_PY[0];
                        segTz = SCRATCH_PZ[1] - SCRATCH_PZ[0];
                    } else if (s == 6) {
                        segTx = SCRATCH_PX[6] - SCRATCH_PX[5];
                        segTy = SCRATCH_PY[6] - SCRATCH_PY[5];
                        segTz = SCRATCH_PZ[6] - SCRATCH_PZ[5];
                    } else {
                        segTx = SCRATCH_PX[s + 1] - SCRATCH_PX[s - 1];
                        segTy = SCRATCH_PY[s + 1] - SCRATCH_PY[s - 1];
                        segTz = SCRATCH_PZ[s + 1] - SCRATCH_PZ[s - 1];
                    }
                    float segTLen = (float)Math.sqrt(segTx * segTx + segTy * segTy + segTz * segTz);
                    if (segTLen > 1.0E-5f) {
                        segTx /= segTLen;
                        segTy /= segTLen;
                        segTz /= segTLen;
                    }
                    if ((rbLen = (float)Math.sqrt((rbx = (py = SCRATCH_PY[s]) * segTz - (pz = SCRATCH_PZ[s]) * segTy) * rbx + (rby = pz * segTx - (px = SCRATCH_PX[s]) * segTz) * rby + (rbz = px * segTy - py * segTx) * rbz)) > 1.0E-5f) {
                        rbx /= rbLen;
                        rby /= rbLen;
                        rbz /= rbLen;
                    }
                    float cx = px * radius;
                    float cy = py * radius;
                    float cz = pz * radius;
                    OmegaPlasmaSphereRenderer.SCRATCH_LEFT_X[s] = cx - rbx * coreWidth;
                    OmegaPlasmaSphereRenderer.SCRATCH_LEFT_Y[s] = cy - rby * coreWidth;
                    OmegaPlasmaSphereRenderer.SCRATCH_LEFT_Z[s] = cz - rbz * coreWidth;
                    OmegaPlasmaSphereRenderer.SCRATCH_RIGHT_X[s] = cx + rbx * coreWidth;
                    OmegaPlasmaSphereRenderer.SCRATCH_RIGHT_Y[s] = cy + rby * coreWidth;
                    OmegaPlasmaSphereRenderer.SCRATCH_RIGHT_Z[s] = cz + rbz * coreWidth;
                }
                for (s = 0; s < 6; ++s) {
                    consumer.addVertex(mat, SCRATCH_LEFT_X[s], SCRATCH_LEFT_Y[s], SCRATCH_LEFT_Z[s]).setColor(255, 255, 255, alpha);
                    consumer.addVertex(mat, SCRATCH_RIGHT_X[s], SCRATCH_RIGHT_Y[s], SCRATCH_RIGHT_Z[s]).setColor(255, 255, 255, alpha);
                    consumer.addVertex(mat, SCRATCH_RIGHT_X[s + 1], SCRATCH_RIGHT_Y[s + 1], SCRATCH_RIGHT_Z[s + 1]).setColor(255, 255, 255, alpha);
                    consumer.addVertex(mat, SCRATCH_LEFT_X[s + 1], SCRATCH_LEFT_Y[s + 1], SCRATCH_LEFT_Z[s + 1]).setColor(255, 255, 255, alpha);
                }
            }
            ++c;
        }
    }

    private static void renderDynamicSurfaceSparksGlow(Matrix4f mat, VertexConsumer consumer, float radius, float time, float overloadFactor, float erasureFactor, float cosX, float sinX, float cosY, float sinY, float cosZ, float sinZ, float baseMidHalfWidth, float baseOutHalfWidth, int midR, int midG, int midB, int outR, int outG, int outB) {
        int c = 0;
        while (c < 32) {
            float crawlerTime = time * 2.2f + (float)c * 1.731f;
            float life = crawlerTime - (float)Math.floor(crawlerTime);
            float crawlerAlpha = Mth.sin((float)(life * (float)Math.PI));
            if (!(crawlerAlpha <= 0.05f)) {
                int s;
                int startNode = c * 7 % 80;
                float sx = NODE_X[startNode];
                float sy = NODE_Y[startNode];
                float sz = NODE_Z[startNode];
                float ux = NODE_TANGENT_U_X[startNode];
                float uy = NODE_TANGENT_U_Y[startNode];
                float uz = NODE_TANGENT_U_Z[startNode];
                float vx = NODE_TANGENT_V_X[startNode];
                float vy = NODE_TANGENT_V_Y[startNode];
                float vz = NODE_TANGENT_V_Z[startNode];
                float curlAngle = FastNoise.simplex3D((float)(sx * 3.0f + crawlerTime * 0.4f), (float)(sy * 3.0f), (float)(sz * 3.0f), (int)(c * 101)) * (float)Math.PI * 2.0f;
                float cosCurl = Mth.cos((float)curlAngle);
                float sinCurl = Mth.sin((float)curlAngle);
                float dx = cosCurl * ux + sinCurl * vx;
                float dy = cosCurl * uy + sinCurl * vy;
                float dz = cosCurl * uz + sinCurl * vz;
                float perpX = -sinCurl * ux + cosCurl * vx;
                float perpY = -sinCurl * uy + cosCurl * vy;
                float perpZ = -sinCurl * uz + cosCurl * vz;
                float midWidth = baseMidHalfWidth * 0.85f;
                float outWidth = baseOutHalfWidth * 0.85f;
                int midAlpha = (int)(165.0f * crawlerAlpha);
                int outAlpha = (int)(75.0f * crawlerAlpha);
                for (s = 0; s <= 6; ++s) {
                    float frac = (float)s / 6.0f;
                    float stepDist = frac * 0.5f;
                    float px = sx + dx * stepDist;
                    float py = sy + dy * stepDist;
                    float pz = sz + dz * stepDist;
                    float pLen = (float)Math.sqrt(px * px + py * py + pz * pz);
                    float jag = FastNoise.ridged3D((float)((px /= pLen) * 14.0f + crawlerTime * 3.0f), (float)((py /= pLen) * 14.0f), (float)((pz /= pLen) * 14.0f), (int)(c * 31337), (int)2, (float)2.0f, (float)0.5f) * 0.08f * (1.0f - frac * 0.25f);
                    OmegaPlasmaSphereRenderer.SCRATCH_PX[s] = OmegaPlasmaSphereRenderer.rotateX(px += perpX * jag, py += perpY * jag, pz += perpZ * jag, cosX, sinX, cosY, sinY, cosZ, sinZ);
                    OmegaPlasmaSphereRenderer.SCRATCH_PY[s] = OmegaPlasmaSphereRenderer.rotateY(px, py, pz, cosX, sinX, cosY, sinY, cosZ, sinZ);
                    OmegaPlasmaSphereRenderer.SCRATCH_PZ[s] = OmegaPlasmaSphereRenderer.rotateZ(px, py, pz, cosX, sinX, cosY, sinY, cosZ, sinZ);
                }
                for (s = 0; s <= 6; ++s) {
                    float rbz;
                    float px;
                    float rby;
                    float pz;
                    float py;
                    float rbx;
                    float rbLen;
                    float segTz;
                    float segTy;
                    float segTx;
                    if (s == 0) {
                        segTx = SCRATCH_PX[1] - SCRATCH_PX[0];
                        segTy = SCRATCH_PY[1] - SCRATCH_PY[0];
                        segTz = SCRATCH_PZ[1] - SCRATCH_PZ[0];
                    } else if (s == 6) {
                        segTx = SCRATCH_PX[6] - SCRATCH_PX[5];
                        segTy = SCRATCH_PY[6] - SCRATCH_PY[5];
                        segTz = SCRATCH_PZ[6] - SCRATCH_PZ[5];
                    } else {
                        segTx = SCRATCH_PX[s + 1] - SCRATCH_PX[s - 1];
                        segTy = SCRATCH_PY[s + 1] - SCRATCH_PY[s - 1];
                        segTz = SCRATCH_PZ[s + 1] - SCRATCH_PZ[s - 1];
                    }
                    float segTLen = (float)Math.sqrt(segTx * segTx + segTy * segTy + segTz * segTz);
                    if (segTLen > 1.0E-5f) {
                        segTx /= segTLen;
                        segTy /= segTLen;
                        segTz /= segTLen;
                    }
                    if ((rbLen = (float)Math.sqrt((rbx = (py = SCRATCH_PY[s]) * segTz - (pz = SCRATCH_PZ[s]) * segTy) * rbx + (rby = pz * segTx - (px = SCRATCH_PX[s]) * segTz) * rby + (rbz = px * segTy - py * segTx) * rbz)) > 1.0E-5f) {
                        rbx /= rbLen;
                        rby /= rbLen;
                        rbz /= rbLen;
                    }
                    float cx = px * radius;
                    float cy = py * radius;
                    float cz = pz * radius;
                    OmegaPlasmaSphereRenderer.SCRATCH_MID_LEFT_X[s] = cx - rbx * midWidth;
                    OmegaPlasmaSphereRenderer.SCRATCH_MID_LEFT_Y[s] = cy - rby * midWidth;
                    OmegaPlasmaSphereRenderer.SCRATCH_MID_LEFT_Z[s] = cz - rbz * midWidth;
                    OmegaPlasmaSphereRenderer.SCRATCH_MID_RIGHT_X[s] = cx + rbx * midWidth;
                    OmegaPlasmaSphereRenderer.SCRATCH_MID_RIGHT_Y[s] = cy + rby * midWidth;
                    OmegaPlasmaSphereRenderer.SCRATCH_MID_RIGHT_Z[s] = cz + rbz * midWidth;
                    OmegaPlasmaSphereRenderer.SCRATCH_OUT_LEFT_X[s] = cx - rbx * outWidth;
                    OmegaPlasmaSphereRenderer.SCRATCH_OUT_LEFT_Y[s] = cy - rby * outWidth;
                    OmegaPlasmaSphereRenderer.SCRATCH_OUT_LEFT_Z[s] = cz - rbz * outWidth;
                    OmegaPlasmaSphereRenderer.SCRATCH_OUT_RIGHT_X[s] = cx + rbx * outWidth;
                    OmegaPlasmaSphereRenderer.SCRATCH_OUT_RIGHT_Y[s] = cy + rby * outWidth;
                    OmegaPlasmaSphereRenderer.SCRATCH_OUT_RIGHT_Z[s] = cz + rbz * outWidth;
                }
                for (s = 0; s < 6; ++s) {
                    consumer.addVertex(mat, SCRATCH_MID_LEFT_X[s], SCRATCH_MID_LEFT_Y[s], SCRATCH_MID_LEFT_Z[s]).setColor(midR, midG, midB, midAlpha);
                    consumer.addVertex(mat, SCRATCH_MID_RIGHT_X[s], SCRATCH_MID_RIGHT_Y[s], SCRATCH_MID_RIGHT_Z[s]).setColor(midR, midG, midB, midAlpha);
                    consumer.addVertex(mat, SCRATCH_MID_RIGHT_X[s + 1], SCRATCH_MID_RIGHT_Y[s + 1], SCRATCH_MID_RIGHT_Z[s + 1]).setColor(midR, midG, midB, midAlpha);
                    consumer.addVertex(mat, SCRATCH_MID_LEFT_X[s + 1], SCRATCH_MID_LEFT_Y[s + 1], SCRATCH_MID_LEFT_Z[s + 1]).setColor(midR, midG, midB, midAlpha);
                    consumer.addVertex(mat, SCRATCH_OUT_LEFT_X[s], SCRATCH_OUT_LEFT_Y[s], SCRATCH_OUT_LEFT_Z[s]).setColor(outR, outG, outB, outAlpha);
                    consumer.addVertex(mat, SCRATCH_OUT_RIGHT_X[s], SCRATCH_OUT_RIGHT_Y[s], SCRATCH_OUT_RIGHT_Z[s]).setColor(outR, outG, outB, outAlpha);
                    consumer.addVertex(mat, SCRATCH_OUT_RIGHT_X[s + 1], SCRATCH_OUT_RIGHT_Y[s + 1], SCRATCH_OUT_RIGHT_Z[s + 1]).setColor(outR, outG, outB, outAlpha);
                    consumer.addVertex(mat, SCRATCH_OUT_LEFT_X[s + 1], SCRATCH_OUT_LEFT_Y[s + 1], SCRATCH_OUT_LEFT_Z[s + 1]).setColor(outR, outG, outB, outAlpha);
                }
            }
            ++c;
        }
    }

    private static void renderLightningJunctionSparks(PoseStack poseStack, VertexConsumer consumer, float radius, float time, float overloadFactor, float erasureFactor) {
        Matrix4f mat = poseStack.last().pose();
        float rotY = time * 14.0f;
        float rotX = 7.0f * Mth.sin((float)(time * 0.6f));
        float rotZ = 5.0f * Mth.cos((float)(time * 0.5f));
        float cosY = Mth.cos((float)(rotY * ((float)Math.PI / 180)));
        float sinY = Mth.sin((float)(rotY * ((float)Math.PI / 180)));
        float cosX = Mth.cos((float)(rotX * ((float)Math.PI / 180)));
        float sinX = Mth.sin((float)(rotX * ((float)Math.PI / 180)));
        float cosZ = Mth.cos((float)(rotZ * ((float)Math.PI / 180)));
        float sinZ = Mth.sin((float)(rotZ * ((float)Math.PI / 180)));
        int stdR = (int)Mth.lerp((float)overloadFactor, (float)180.0f, (float)235.0f);
        int stdG = (int)Mth.lerp((float)overloadFactor, (float)245.0f, (float)190.0f);
        int stdB = 255;
        int tipR = (int)Mth.lerp((float)erasureFactor, (float)stdR, (float)130.0f);
        int tipG = (int)Mth.lerp((float)erasureFactor, (float)stdG, (float)255.0f);
        int tipB = (int)Mth.lerp((float)erasureFactor, (float)stdB, (float)40.0f);
        float baseStarSize = 0.26f + 0.1f * overloadFactor + 0.12f * erasureFactor;
        int i = 0;
        while (i < 80) {
            float nx = NODE_X[i];
            float ny = NODE_Y[i];
            float nz = NODE_Z[i];
            float phase = NODE_PHASE[i];
            float flicker = FastNoise.simplex3D((float)(nx * 5.0f), (float)(ny * 5.0f), (float)(nz * 5.0f + time * 6.5f), (int)((int)(phase * 100.0f)));
            float intensity = flicker * 0.65f + 0.35f + 0.2f * overloadFactor + 0.25f * erasureFactor;
            if (!(intensity <= 0.25f)) {
                float rnx = OmegaPlasmaSphereRenderer.rotateX(nx, ny, nz, cosX, sinX, cosY, sinY, cosZ, sinZ);
                float rny = OmegaPlasmaSphereRenderer.rotateY(nx, ny, nz, cosX, sinX, cosY, sinY, cosZ, sinZ);
                float rnz = OmegaPlasmaSphereRenderer.rotateZ(nx, ny, nz, cosX, sinX, cosY, sinY, cosZ, sinZ);
                float ux = NODE_TANGENT_U_X[i];
                float uy = NODE_TANGENT_U_Y[i];
                float uz = NODE_TANGENT_U_Z[i];
                float rux = OmegaPlasmaSphereRenderer.rotateX(ux, uy, uz, cosX, sinX, cosY, sinY, cosZ, sinZ);
                float ruy = OmegaPlasmaSphereRenderer.rotateY(ux, uy, uz, cosX, sinX, cosY, sinY, cosZ, sinZ);
                float ruz = OmegaPlasmaSphereRenderer.rotateZ(ux, uy, uz, cosX, sinX, cosY, sinY, cosZ, sinZ);
                float vx = NODE_TANGENT_V_X[i];
                float vy = NODE_TANGENT_V_Y[i];
                float vz = NODE_TANGENT_V_Z[i];
                float rvx = OmegaPlasmaSphereRenderer.rotateX(vx, vy, vz, cosX, sinX, cosY, sinY, cosZ, sinZ);
                float rvy = OmegaPlasmaSphereRenderer.rotateY(vx, vy, vz, cosX, sinX, cosY, sinY, cosZ, sinZ);
                float rvz = OmegaPlasmaSphereRenderer.rotateZ(vx, vy, vz, cosX, sinX, cosY, sinY, cosZ, sinZ);
                float starSize = baseStarSize * intensity * (0.85f + 0.3f * FastNoise.simplex3D((float)(time * 12.0f), (float)((float)i * 3.0f), (float)0.0f));
                float waistSize = starSize * 0.26f;
                float cx = rnx * radius;
                float cy = rny * radius;
                float cz = rnz * radius;
                float tipU1X = cx + rux * starSize;
                float tipU1Y = cy + ruy * starSize;
                float tipU1Z = cz + ruz * starSize;
                float tipU2X = cx - rux * starSize;
                float tipU2Y = cy - ruy * starSize;
                float tipU2Z = cz - ruz * starSize;
                float tipV1X = cx + rvx * starSize;
                float tipV1Y = cy + rvy * starSize;
                float tipV1Z = cz + rvz * starSize;
                float tipV2X = cx - rvx * starSize;
                float tipV2Y = cy - rvy * starSize;
                float tipV2Z = cz - rvz * starSize;
                float c1X = cx + (rux + rvx) * waistSize;
                float c1Y = cy + (ruy + rvy) * waistSize;
                float c1Z = cz + (ruz + rvz) * waistSize;
                float c2X = cx + (-rux + rvx) * waistSize;
                float c2Y = cy + (-ruy + rvy) * waistSize;
                float c2Z = cz + (-ruz + rvz) * waistSize;
                float c3X = cx + (-rux - rvx) * waistSize;
                float c3Y = cy + (-ruy - rvy) * waistSize;
                float c3Z = cz + (-ruz - rvz) * waistSize;
                float c4X = cx + (rux - rvx) * waistSize;
                float c4Y = cy + (ruy - rvy) * waistSize;
                float c4Z = cz + (ruz - rvz) * waistSize;
                int centerA = (int)Mth.clamp((float)(255.0f * intensity), (float)140.0f, (float)255.0f);
                int tipA = 0;
                int cornerA = (int)((float)centerA * 0.45f);
                consumer.addVertex(mat, cx, cy, cz).setColor(255, 255, 255, centerA);
                consumer.addVertex(mat, c4X, c4Y, c4Z).setColor(tipR, tipG, tipB, cornerA);
                consumer.addVertex(mat, tipU1X, tipU1Y, tipU1Z).setColor(tipR, tipG, tipB, tipA);
                consumer.addVertex(mat, c1X, c1Y, c1Z).setColor(tipR, tipG, tipB, cornerA);
                consumer.addVertex(mat, cx, cy, cz).setColor(255, 255, 255, centerA);
                consumer.addVertex(mat, c1X, c1Y, c1Z).setColor(tipR, tipG, tipB, cornerA);
                consumer.addVertex(mat, tipV1X, tipV1Y, tipV1Z).setColor(tipR, tipG, tipB, tipA);
                consumer.addVertex(mat, c2X, c2Y, c2Z).setColor(tipR, tipG, tipB, cornerA);
                consumer.addVertex(mat, cx, cy, cz).setColor(255, 255, 255, centerA);
                consumer.addVertex(mat, c2X, c2Y, c2Z).setColor(tipR, tipG, tipB, cornerA);
                consumer.addVertex(mat, tipU2X, tipU2Y, tipU2Z).setColor(tipR, tipG, tipB, tipA);
                consumer.addVertex(mat, c3X, c3Y, c3Z).setColor(tipR, tipG, tipB, cornerA);
                consumer.addVertex(mat, cx, cy, cz).setColor(255, 255, 255, centerA);
                consumer.addVertex(mat, c3X, c3Y, c3Z).setColor(tipR, tipG, tipB, cornerA);
                consumer.addVertex(mat, tipV2X, tipV2Y, tipV2Z).setColor(tipR, tipG, tipB, tipA);
                consumer.addVertex(mat, c4X, c4Y, c4Z).setColor(tipR, tipG, tipB, cornerA);
            }
            ++i;
        }
    }

    private static float rotateX(float x, float y, float z, float cosX, float sinX, float cosY, float sinY, float cosZ, float sinZ) {
        float x1 = cosY * x + sinY * z;
        float y1 = y;
        float z1 = -sinY * x + cosY * z;
        float x2 = x1;
        float y2 = cosX * y1 - sinX * z1;
        return cosZ * x2 - sinZ * y2;
    }

    private static float rotateY(float x, float y, float z, float cosX, float sinX, float cosY, float sinY, float cosZ, float sinZ) {
        float x1 = cosY * x + sinY * z;
        float y1 = y;
        float z1 = -sinY * x + cosY * z;
        float x2 = x1;
        float y2 = cosX * y1 - sinX * z1;
        return sinZ * x2 + cosZ * y2;
    }

    private static float rotateZ(float x, float y, float z, float cosX, float sinX, float cosY, float sinY, float cosZ, float sinZ) {
        float x1 = cosY * x + sinY * z;
        float y1 = y;
        float z1 = -sinY * x + cosY * z;
        return sinX * y1 + cosX * z1;
    }

    private static void renderSubtleCoronaHalo(PoseStack poseStack, VertexConsumer consumer, float baseRadius, float overloadFactor, float erasureFactor) {
        Matrix4f mat = poseStack.last().pose();
        float haloRadius = baseRadius * (1.05f + 0.03f * overloadFactor + 0.04f * erasureFactor);
        int maxAlpha = (int)(35.0f + 30.0f * overloadFactor + 40.0f * erasureFactor);
        int stdR = (int)Mth.lerp((float)overloadFactor, (float)79.0f, (float)160.0f);
        int stdG = (int)Mth.lerp((float)overloadFactor, (float)229.0f, (float)180.0f);
        int stdB = 255;
        int r = (int)Mth.lerp((float)erasureFactor, (float)stdR, (float)60.0f);
        int g = (int)Mth.lerp((float)erasureFactor, (float)stdG, (float)255.0f);
        int b = (int)Mth.lerp((float)erasureFactor, (float)stdB, (float)25.0f);
        int j = 0;
        while (j < 16) {
            float theta1 = (float)(Math.PI * (double)j / 16.0);
            float theta2 = (float)(Math.PI * (double)(j + 1) / 16.0);
            float y1 = haloRadius * (float)Math.cos(theta1);
            float y2 = haloRadius * (float)Math.cos(theta2);
            float r1 = haloRadius * (float)Math.sin(theta1);
            float r2 = haloRadius * (float)Math.sin(theta2);
            float ny1 = (float)Math.cos(theta1);
            float ny2 = (float)Math.cos(theta2);
            float nr1 = (float)Math.sin(theta1);
            float nr2 = (float)Math.sin(theta2);
            for (int i = 0; i < 24; ++i) {
                float phi1 = (float)(Math.PI * 2 * (double)i / 24.0);
                float phi2 = (float)(Math.PI * 2 * (double)(i + 1) / 24.0);
                float cosPhi1 = (float)Math.cos(phi1);
                float sinPhi1 = (float)Math.sin(phi1);
                float cosPhi2 = (float)Math.cos(phi2);
                float sinPhi2 = (float)Math.sin(phi2);
                float x11 = r1 * cosPhi1;
                float z11 = r1 * sinPhi1;
                float x21 = r2 * cosPhi1;
                float z21 = r2 * sinPhi1;
                float x22 = r2 * cosPhi2;
                float z22 = r2 * sinPhi2;
                float x12 = r1 * cosPhi2;
                float z12 = r1 * sinPhi2;
                float f11 = (float)Math.pow(1.0f - Math.min(1.0f, Math.abs(nr1 * sinPhi1)), 2.0);
                float f21 = (float)Math.pow(1.0f - Math.min(1.0f, Math.abs(nr2 * sinPhi1)), 2.0);
                float f22 = (float)Math.pow(1.0f - Math.min(1.0f, Math.abs(nr2 * sinPhi2)), 2.0);
                float f12 = (float)Math.pow(1.0f - Math.min(1.0f, Math.abs(nr1 * sinPhi2)), 2.0);
                consumer.addVertex(mat, x11, y1, z11).setColor(r, g, b, (int)((float)maxAlpha * f11));
                consumer.addVertex(mat, x21, y2, z21).setColor(r, g, b, (int)((float)maxAlpha * f21));
                consumer.addVertex(mat, x22, y2, z22).setColor(r, g, b, (int)((float)maxAlpha * f22));
                consumer.addVertex(mat, x12, y1, z12).setColor(r, g, b, (int)((float)maxAlpha * f12));
            }
            ++j;
        }
    }

    private static void renderExpandingGreenCloudBurst(PoseStack poseStack, MultiBufferSource bufferSource, float cloudAge) {
        float alpha;
        float expandProg = Math.min(1.0f, cloudAge / 30.0f);
        float eased = 1.0f - (float)Math.pow(1.0f - expandProg, 3.0);
        float cloudRadius = 2.0f + 28.0f * eased;
        if (cloudAge <= 18.0f) {
            alpha = 1.0f;
        } else {
            float fadeProg = (cloudAge - 18.0f) / 27.0f;
            alpha = Math.max(0.0f, 1.0f - (float)Math.pow(fadeProg, 1.6));
        }
        if (alpha <= 0.01f) {
            return;
        }
        Matrix4f mat = poseStack.last().pose();
        VertexConsumer cloudConsumer = bufferSource.getBuffer(OmegaRenderTypes.PLASMA_TRANSLUCENT_BODY);
        int outA = (int)(210.0f * alpha);
        int outR = 118;
        int outG = 230;
        int outB = 18;
        int lobeSegs = 14;
        for (int l = 0; l < 24; ++l) {
            Vector3f dir = CLOUD_LOBE_DIRS[l];
            Vector3f u = CLOUD_LOBE_U[l];
            Vector3f v = CLOUD_LOBE_V[l];
            float phase = CLOUD_LOBE_PHASES[l];
            float lobeDist = cloudRadius * (0.85f + 0.2f * Mth.sin((float)(phase + cloudAge * 0.2f)));
            float lx = dir.x * lobeDist;
            float ly = dir.y * lobeDist;
            float lz = dir.z * lobeDist;
            float lobeRadius = cloudRadius * 0.4f * (0.8f + (float)(l % 5) * 0.09f);
            for (int k = 0; k < lobeSegs; ++k) {
                float a1 = (float)(Math.PI * 2 * (double)k / (double)lobeSegs);
                float a2 = (float)(Math.PI * 2 * (double)(k + 1) / (double)lobeSegs);
                float p1x = lx + (u.x * Mth.cos((float)a1) + v.x * Mth.sin((float)a1)) * lobeRadius;
                float p1y = ly + (u.y * Mth.cos((float)a1) + v.y * Mth.sin((float)a1)) * lobeRadius;
                float p1z = lz + (u.z * Mth.cos((float)a1) + v.z * Mth.sin((float)a1)) * lobeRadius;
                float p2x = lx + (u.x * Mth.cos((float)a2) + v.x * Mth.sin((float)a2)) * lobeRadius;
                float p2y = ly + (u.y * Mth.cos((float)a2) + v.y * Mth.sin((float)a2)) * lobeRadius;
                float p2z = lz + (u.z * Mth.cos((float)a2) + v.z * Mth.sin((float)a2)) * lobeRadius;
                cloudConsumer.addVertex(mat, lx, ly, lz).setColor(outR, outG, outB, outA);
                cloudConsumer.addVertex(mat, p1x, p1y, p1z).setColor(outR, outG, outB, (int)((float)outA * 0.4f));
                cloudConsumer.addVertex(mat, p2x, p2y, p2z).setColor(outR, outG, outB, (int)((float)outA * 0.4f));
                cloudConsumer.addVertex(mat, lx, ly, lz).setColor(outR, outG, outB, outA);
            }
        }
        VertexConsumer glowConsumer = bufferSource.getBuffer(OmegaRenderTypes.PLASMA_ADDITIVE_GLOW);
        int inA = (int)(240.0f * alpha);
        int inR = 196;
        int inG = 255;
        int inB = 32;
        for (int l = 0; l < 24; ++l) {
            Vector3f dir = CLOUD_LOBE_DIRS[l];
            Vector3f u = CLOUD_LOBE_U[l];
            Vector3f v = CLOUD_LOBE_V[l];
            float phase = CLOUD_LOBE_PHASES[l];
            float lobeDist = cloudRadius * 0.65f * (0.85f + 0.15f * Mth.sin((float)(phase + cloudAge * 0.2f)));
            float lx = dir.x * lobeDist;
            float ly = dir.y * lobeDist;
            float lz = dir.z * lobeDist;
            float innerRadius = cloudRadius * 0.26f * (0.85f + (float)(l % 4) * 0.08f);
            for (int k = 0; k < lobeSegs; ++k) {
                float a1 = (float)(Math.PI * 2 * (double)k / (double)lobeSegs);
                float a2 = (float)(Math.PI * 2 * (double)(k + 1) / (double)lobeSegs);
                float p1x = lx + (u.x * Mth.cos((float)a1) + v.x * Mth.sin((float)a1)) * innerRadius;
                float p1y = ly + (u.y * Mth.cos((float)a1) + v.y * Mth.sin((float)a1)) * innerRadius;
                float p1z = lz + (u.z * Mth.cos((float)a1) + v.z * Mth.sin((float)a1)) * innerRadius;
                float p2x = lx + (u.x * Mth.cos((float)a2) + v.x * Mth.sin((float)a2)) * innerRadius;
                float p2y = ly + (u.y * Mth.cos((float)a2) + v.y * Mth.sin((float)a2)) * innerRadius;
                float p2z = lz + (u.z * Mth.cos((float)a2) + v.z * Mth.sin((float)a2)) * innerRadius;
                glowConsumer.addVertex(mat, lx, ly, lz).setColor(inR, inG, inB, inA);
                glowConsumer.addVertex(mat, p1x, p1y, p1z).setColor(inR, inG, inB, (int)((float)inA * 0.3f));
                glowConsumer.addVertex(mat, p2x, p2y, p2z).setColor(inR, inG, inB, (int)((float)inA * 0.3f));
                glowConsumer.addVertex(mat, lx, ly, lz).setColor(inR, inG, inB, inA);
            }
        }
        int spikeCount = 16;
        float spikeLen = cloudRadius * 1.25f;
        int s = 0;
        while (s < spikeCount) {
            double ang = (double)s * 2.0 * Math.PI / (double)spikeCount;
            double elev = (double)(s % 5 - 2) * 0.35;
            float cosElev = (float)Math.cos(elev);
            float sinElev = (float)Math.sin(elev);
            float dx = (float)Math.cos(ang) * cosElev;
            float dy = sinElev;
            float dz = (float)Math.sin(ang) * cosElev;
            int spikeSegs = 6;
            float prevX = 0.0f;
            float prevY = 0.0f;
            float prevZ = 0.0f;
            for (int seg = 1; seg <= spikeSegs; ++seg) {
                float frac = (float)seg / (float)spikeSegs;
                float dist = spikeLen * frac;
                float jx = 1.4f * Mth.sin((float)(frac * 12.0f + cloudAge * 0.8f + (float)s));
                float jy = 1.4f * Mth.cos((float)(frac * 10.0f - cloudAge * 0.7f + (float)s));
                float jz = 1.4f * Mth.sin((float)(frac * 14.0f + cloudAge * 0.9f + (float)s));
                float curX = dx * dist + jx;
                float curY = dy * dist + jy;
                float curZ = dz * dist + jz;
                float width = 0.35f * (1.0f - frac);
                glowConsumer.addVertex(mat, prevX - width, prevY, prevZ).setColor(255, 255, 255, (int)(230.0f * alpha));
                glowConsumer.addVertex(mat, prevX + width, prevY, prevZ).setColor(255, 255, 255, (int)(230.0f * alpha));
                glowConsumer.addVertex(mat, curX + width, curY, curZ).setColor(57, 255, 20, (int)(180.0f * alpha));
                glowConsumer.addVertex(mat, curX - width, curY, curZ).setColor(57, 255, 20, (int)(180.0f * alpha));
                prevX = curX;
                prevY = curY;
                prevZ = curZ;
            }
            ++s;
        }
    }

    private static float getOverloadFactor(OmegaDevicePhase phase, int phaseTicks, float partialTicks) {
        float t = (float)phaseTicks + partialTicks;
        float linear = switch (1.$SwitchMap$org$ExtraTortik$ricks_stuff$entity$omega$OmegaDevicePhase[phase.ordinal()]) {
            case 1 -> Mth.clamp((float)(t / 35.0f), (float)0.0f, (float)1.0f);
            case 2 -> 0.25f;
            case 3 -> 0.65f;
            case 4 -> 1.0f;
            default -> 0.0f;
        };
        return linear * linear * (3.0f - 2.0f * linear);
    }

    private static float getErasureFactor(OmegaDevicePhase phase, int phaseTicks, float partialTicks) {
        float t = (float)phaseTicks + partialTicks;
        float linear = switch (1.$SwitchMap$org$ExtraTortik$ricks_stuff$entity$omega$OmegaDevicePhase[phase.ordinal()]) {
            case 5 -> 1.0f;
            case 6 -> Mth.clamp((float)(1.0f - t / 40.0f), (float)0.0f, (float)1.0f);
            default -> 0.0f;
        };
        return linear * linear * (3.0f - 2.0f * linear);
    }

    static {
        float phi = (1.0f + (float)Math.sqrt(5.0)) * 0.5f;
        for (int i = 0; i < 80; ++i) {
            float vz;
            float vy;
            float vx;
            float vLen;
            float uz;
            float uy;
            float y = 1.0f - (float)i / 79.0f * 2.0f;
            float radiusAtY = (float)Math.sqrt(Math.max(0.0f, 1.0f - y * y));
            float theta = (float)Math.PI * 2 * (float)i / phi;
            float x = (float)Math.cos(theta) * radiusAtY;
            float z = (float)Math.sin(theta) * radiusAtY;
            OmegaPlasmaSphereRenderer.NODE_X[i] = x;
            OmegaPlasmaSphereRenderer.NODE_Y[i] = y;
            OmegaPlasmaSphereRenderer.NODE_Z[i] = z;
            OmegaPlasmaSphereRenderer.NODE_PHASE[i] = (float)((double)i * 2.399963229728);
            float upX = Math.abs(y) < 0.9f ? 0.0f : 1.0f;
            float upZ = 0.0f;
            float upY = Math.abs(y) < 0.9f ? 1.0f : 0.0f;
            float ux = y * upZ - z * upY;
            float uLen = (float)Math.sqrt(ux * ux + (uy = z * upX - x * upZ) * uy + (uz = x * upY - y * upX) * uz);
            if (uLen > 1.0E-5f) {
                ux /= uLen;
                uy /= uLen;
                uz /= uLen;
            }
            if ((vLen = (float)Math.sqrt((vx = y * uz - z * uy) * vx + (vy = z * ux - x * uz) * vy + (vz = x * uy - y * ux) * vz)) > 1.0E-5f) {
                vx /= vLen;
                vy /= vLen;
                vz /= vLen;
            }
            OmegaPlasmaSphereRenderer.NODE_TANGENT_U_X[i] = ux;
            OmegaPlasmaSphereRenderer.NODE_TANGENT_U_Y[i] = uy;
            OmegaPlasmaSphereRenderer.NODE_TANGENT_U_Z[i] = uz;
            OmegaPlasmaSphereRenderer.NODE_TANGENT_V_X[i] = vx;
            OmegaPlasmaSphereRenderer.NODE_TANGENT_V_Y[i] = vy;
            OmegaPlasmaSphereRenderer.NODE_TANGENT_V_Z[i] = vz;
        }
        int edgeCount = 0;
        float[] minDist = new float[5];
        int[] minIndex = new int[5];
        int i = 0;
        block1: while (true) {
            int k;
            if (i >= 80) break;
            for (k = 0; k < 5; ++k) {
                minDist[k] = Float.MAX_VALUE;
                minIndex[k] = -1;
            }
            int j = 0;
            while (true) {
                if (j >= 80) break;
                if (i != j) {
                    float dx = NODE_X[i] - NODE_X[j];
                    float dy = NODE_Y[i] - NODE_Y[j];
                    float dz = NODE_Z[i] - NODE_Z[j];
                    float d2 = dx * dx + dy * dy + dz * dz;
                    for (int k2 = 0; k2 < 5; ++k2) {
                        if (!(d2 < minDist[k2])) continue;
                        for (int m = 4; m > k2; --m) {
                            minDist[m] = minDist[m - 1];
                            minIndex[m] = minIndex[m - 1];
                        }
                        minDist[k2] = d2;
                        minIndex[k2] = j;
                        break;
                    }
                }
                ++j;
            }
            k = 0;
            while (true) {
                block14: {
                    boolean exists;
                    int v;
                    int u;
                    block15: {
                        block13: {
                            if (k >= 5) break block13;
                            int neighbor = minIndex[k];
                            if (neighbor < 0 || (u = Math.min(i, neighbor)) == (v = Math.max(i, neighbor))) break block14;
                            exists = false;
                            break block15;
                        }
                        ++i;
                        continue block1;
                    }
                    for (int e = 0; e < edgeCount; ++e) {
                        if (EDGE_U[e] != u || EDGE_V[e] != v) continue;
                        exists = true;
                        break;
                    }
                    if (!exists && edgeCount < 300) {
                        OmegaPlasmaSphereRenderer.EDGE_U[edgeCount] = u;
                        OmegaPlasmaSphereRenderer.EDGE_V[edgeCount] = v;
                        OmegaPlasmaSphereRenderer.EDGE_PHASE[edgeCount] = (float)((double)((u * 37 + v * 53) % 100) * 0.062831853);
                        ++edgeCount;
                    }
                }
                ++k;
            }
            break;
        }
        TOTAL_EDGES = edgeCount;
        i = 0;
        while (i < 24) {
            float y = 1.0f - (float)i / 23.0f * 2.0f;
            float radiusAtY = (float)Math.sqrt(Math.max(0.0f, 1.0f - y * y));
            float theta = (float)Math.PI * 2 * (float)i / phi;
            float x = (float)Math.cos(theta) * radiusAtY;
            float z = (float)Math.sin(theta) * radiusAtY;
            OmegaPlasmaSphereRenderer.CLOUD_LOBE_DIRS[i] = new Vector3f(x, y, z);
            OmegaPlasmaSphereRenderer.CLOUD_LOBE_PHASES[i] = (float)((double)i * 2.3999632297);
            Vector3f up = Math.abs(y) < 0.9f ? new Vector3f(0.0f, 1.0f, 0.0f) : new Vector3f(1.0f, 0.0f, 0.0f);
            Vector3f u = new Vector3f();
            CLOUD_LOBE_DIRS[i].cross((Vector3fc)up, u);
            u.normalize();
            Vector3f v = new Vector3f();
            CLOUD_LOBE_DIRS[i].cross((Vector3fc)u, v);
            v.normalize();
            OmegaPlasmaSphereRenderer.CLOUD_LOBE_U[i] = u;
            OmegaPlasmaSphereRenderer.CLOUD_LOBE_V[i] = v;
            ++i;
        }
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  java.lang.MatchException
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.util.Mth
 *  org.ExtraTortik.ricks_stuff.client.renderer.WarpDebrisRenderer$CelestialTheme
 *  org.ExtraTortik.ricks_stuff.client.renderer.WarpDebrisRenderer$DebrisRenderTypes
 *  org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity
 *  org.joml.Matrix4f
 */
package org.ExtraTortik.ricks_stuff.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import org.ExtraTortik.ricks_stuff.client.renderer.WarpDebrisRenderer;
import org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity;
import org.joml.Matrix4f;

/*
 * Exception performing whole class analysis ignored.
 */
public class WarpDebrisRenderer {
    private static final int ACTIVE_STREAM_SLOTS = 8;
    private static final float STREAM_START_Z = 250.0f;
    private static final float STREAM_END_Z = -50.0f;
    private static final float STREAM_SPAN_Z = 300.0f;
    private static final float CYCLE_DURATION_TICKS = 42.0f;
    private static final int LATITUDE_BANDS = 18;
    private static final int LONGITUDE_BANDS = 28;

    public static void renderDebris(PoseStack poseStack, MultiBufferSource buffer, SpaceCruiserEntity ship, float partialTicks) {
        WarpDebrisRenderer.renderDebris(poseStack, buffer, ship, partialTicks, 1.0f);
    }

    public static void renderDebris(PoseStack poseStack, MultiBufferSource buffer, SpaceCruiserEntity ship, float partialTicks, float masterAlpha) {
        if (masterAlpha <= 0.01f) {
            return;
        }
        float totalTime = (float)ship.tickCount + partialTicks;
        VertexConsumer bodyConsumer = buffer.getBuffer(DebrisRenderTypes.CELESTIAL_BODY);
        for (int slot = 0; slot < 8; ++slot) {
            WarpDebrisRenderer.renderStreamSlot(poseStack, bodyConsumer, slot, totalTime, masterAlpha, false);
        }
        VertexConsumer coronaConsumer = buffer.getBuffer(DebrisRenderTypes.ATMOSPHERE_CORONA);
        int slot = 0;
        while (slot < 8) {
            WarpDebrisRenderer.renderStreamSlot(poseStack, coronaConsumer, slot, totalTime, masterAlpha, true);
            ++slot;
        }
    }

    private static void renderStreamSlot(PoseStack poseStack, VertexConsumer consumer, int slot, float totalTime, float masterAlpha, boolean isCoronaPass) {
        float slotOffset = (float)slot * 5.25f;
        float effectiveTime = totalTime + slotOffset;
        int cycleIndex = (int)Math.floor(effectiveTime / 42.0f);
        float cycleProgress = effectiveTime % 42.0f / 42.0f;
        if (cycleProgress < 0.0f) {
            cycleProgress += 1.0f;
        }
        float currentZ = 250.0f - cycleProgress * 300.0f;
        float distanceAlpha = 1.0f;
        if (currentZ > 220.0f) {
            distanceAlpha = (250.0f - currentZ) / 30.0f;
        } else if (currentZ < -20.0f) {
            distanceAlpha = (currentZ - -50.0f) / 30.0f;
        }
        distanceAlpha = Mth.clamp((float)distanceAlpha, (float)0.0f, (float)1.0f) * masterAlpha;
        if (distanceAlpha <= 0.01f) {
            return;
        }
        int seed = WarpDebrisRenderer.hashSeed(slot, cycleIndex);
        CelestialTheme theme = CelestialTheme.fromId((int)seed);
        if (isCoronaPass && theme == CelestialTheme.ASTEROID_CLUSTER) {
            return;
        }
        float signX = WarpDebrisRenderer.hashFloat(seed, 1) > 0.5f ? 1.0f : -1.0f;
        float rawX = 22.0f + WarpDebrisRenderer.hashFloat(seed, 2) * 24.0f;
        float posX = signX * rawX;
        float signY = WarpDebrisRenderer.hashFloat(seed, 3) > 0.5f ? 1.0f : -1.0f;
        float rawY = 12.0f + WarpDebrisRenderer.hashFloat(seed, 4) * 20.0f;
        float posY = signY * rawY;
        float radius = theme == CelestialTheme.GAS_GIANT_RINGS ? 20.0f + WarpDebrisRenderer.hashFloat(seed, 5) * 8.0f : (theme == CelestialTheme.ASTEROID_CLUSTER ? 12.0f + WarpDebrisRenderer.hashFloat(seed, 5) * 6.0f : 14.0f + WarpDebrisRenderer.hashFloat(seed, 5) * 12.0f);
        poseStack.pushPose();
        poseStack.translate(posX, posY, currentZ);
        float tiltX = (WarpDebrisRenderer.hashFloat(seed, 6) - 0.5f) * 45.0f;
        float tiltZ = (WarpDebrisRenderer.hashFloat(seed, 7) - 0.5f) * 45.0f;
        float spinYaw = (totalTime * (1.2f + WarpDebrisRenderer.hashFloat(seed, 8) * 2.0f) + WarpDebrisRenderer.hashFloat(seed, 9) * 360.0f) % 360.0f;
        poseStack.mulPose(Axis.XP.rotationDegrees(tiltX));
        poseStack.mulPose(Axis.ZP.rotationDegrees(tiltZ));
        poseStack.mulPose(Axis.YP.rotationDegrees(spinYaw));
        if (!isCoronaPass) {
            if (theme == CelestialTheme.ASTEROID_CLUSTER) {
                WarpDebrisRenderer.renderAsteroidCluster(poseStack, consumer, radius, distanceAlpha, seed, totalTime);
            } else {
                WarpDebrisRenderer.renderPlanetSphere(poseStack, consumer, radius, theme, distanceAlpha, seed);
                if (theme == CelestialTheme.GAS_GIANT_RINGS) {
                    WarpDebrisRenderer.renderPlanetaryRings(poseStack, consumer, radius, distanceAlpha, seed);
                }
            }
        } else {
            WarpDebrisRenderer.renderAtmosphericCorona(poseStack, consumer, radius, theme, distanceAlpha);
        }
        poseStack.popPose();
    }

    private static void renderPlanetSphere(PoseStack poseStack, VertexConsumer consumer, float radius, CelestialTheme theme, float alpha, int seed) {
        Matrix4f matrix = poseStack.last().pose();
        float lightX = 0.45f;
        float lightY = 0.65f;
        float lightZ = 0.6f;
        float lightLen = Mth.sqrt((float)(lightX * lightX + lightY * lightY + lightZ * lightZ));
        lightX /= lightLen;
        lightY /= lightLen;
        lightZ /= lightLen;
        int lat = 0;
        while (lat < 18) {
            float phi0 = (float)Math.PI * ((float)lat / 18.0f - 0.5f);
            float phi1 = (float)Math.PI * ((float)(lat + 1) / 18.0f - 0.5f);
            float y0 = (float)Math.sin(phi0);
            float y1 = (float)Math.sin(phi1);
            float cosPhi0 = (float)Math.cos(phi0);
            float cosPhi1 = (float)Math.cos(phi1);
            for (int lon = 0; lon < 28; ++lon) {
                float theta0 = (float)((double)lon * 2.0 * Math.PI / 28.0);
                float theta1 = (float)((double)(lon + 1) * 2.0 * Math.PI / 28.0);
                float x00 = cosPhi0 * (float)Math.cos(theta0);
                float z00 = cosPhi0 * (float)Math.sin(theta0);
                float x01 = cosPhi0 * (float)Math.cos(theta1);
                float z01 = cosPhi0 * (float)Math.sin(theta1);
                float x10 = cosPhi1 * (float)Math.cos(theta0);
                float z10 = cosPhi1 * (float)Math.sin(theta0);
                float x11 = cosPhi1 * (float)Math.cos(theta1);
                float z11 = cosPhi1 * (float)Math.sin(theta1);
                float[] c00 = WarpDebrisRenderer.getPlanetSurfaceColor(theme, x00, y0, z00, lightX, lightY, lightZ, alpha, seed);
                float[] c01 = WarpDebrisRenderer.getPlanetSurfaceColor(theme, x01, y0, z01, lightX, lightY, lightZ, alpha, seed);
                float[] c11 = WarpDebrisRenderer.getPlanetSurfaceColor(theme, x11, y1, z11, lightX, lightY, lightZ, alpha, seed);
                float[] c10 = WarpDebrisRenderer.getPlanetSurfaceColor(theme, x10, y1, z10, lightX, lightY, lightZ, alpha, seed);
                consumer.addVertex(matrix, x00 * radius, y0 * radius, z00 * radius).setColor(c00[0], c00[1], c00[2], c00[3]);
                consumer.addVertex(matrix, x01 * radius, y0 * radius, z01 * radius).setColor(c01[0], c01[1], c01[2], c01[3]);
                consumer.addVertex(matrix, x11 * radius, y1 * radius, z11 * radius).setColor(c11[0], c11[1], c11[2], c11[3]);
                consumer.addVertex(matrix, x10 * radius, y1 * radius, z10 * radius).setColor(c10[0], c10[1], c10[2], c10[3]);
            }
            ++lat;
        }
    }

    private static float[] getPlanetSurfaceColor(CelestialTheme theme, float nx, float ny, float nz, float lx, float ly, float lz, float alpha, int seed) {
        float dot = nx * lx + ny * ly + nz * lz;
        float diffuse = Math.max(0.28f, dot * 0.72f + 0.35f);
        float lon = (float)Math.atan2(nz, nx);
        float lat = ny;
        float r = 1.0f;
        float g = 1.0f;
        float b = 1.0f;
        switch (theme.ordinal()) {
            case 0: {
                float landNoise = (float)(Math.sin(3.5 * (double)lon + 1.2) * Math.cos(2.8 * (double)lat) + 0.45 * Math.sin(7.0 * (double)lon - 4.0 * (double)lat));
                float cloudNoise = (float)(Math.sin(5.0 * (double)lon + 3.0 * (double)lat + 0.8) * Math.cos(4.0 * (double)lon - 2.0 * (double)lat));
                if (Math.abs(lat) > 0.82f) {
                    r = 0.92f;
                    g = 0.96f;
                    b = 1.0f;
                    break;
                }
                if (cloudNoise > 0.42f) {
                    r = 0.95f;
                    g = 0.98f;
                    b = 1.0f;
                    break;
                }
                if (landNoise > 0.15f) {
                    r = 0.16f;
                    g = 0.68f;
                    b = 0.28f;
                    break;
                }
                r = 0.06f;
                g = 0.32f;
                b = 0.88f;
                break;
            }
            case 1: {
                float rustNoise = (float)(Math.sin(4.0 * (double)lon) * Math.cos(3.0 * (double)lat) + 0.3 * Math.sin(8.0 * (double)lat));
                if (Math.abs(lat) > 0.88f) {
                    r = 0.96f;
                    g = 0.92f;
                    b = 0.94f;
                    break;
                }
                if (rustNoise > 0.2f) {
                    r = 0.88f;
                    g = 0.28f;
                    b = 0.12f;
                    break;
                }
                r = 0.96f;
                g = 0.52f;
                b = 0.22f;
                break;
            }
            case 2: {
                float band = (float)(Math.sin(lat * 14.0f) + (double)0.35f * Math.sin(lat * 28.0f + lon * 2.0f));
                float storm = (float)Math.sin(lon * 4.0f - lat * 6.0f);
                if (storm > 0.7f && Math.abs(lat + 0.25f) < 0.2f) {
                    r = 0.95f;
                    g = 0.35f;
                    b = 0.15f;
                    break;
                }
                if (band > 0.3f) {
                    r = 0.98f;
                    g = 0.82f;
                    b = 0.42f;
                    break;
                }
                if (band > -0.3f) {
                    r = 0.88f;
                    g = 0.62f;
                    b = 0.3f;
                    break;
                }
                r = 0.72f;
                g = 0.45f;
                b = 0.22f;
                break;
            }
            case 3: {
                float toxicNoise = (float)(Math.sin(5.0 * (double)lon) * Math.cos(4.0 * (double)lat) + 0.4 * Math.sin(9.0 * (double)lon + (double)lat * 3.0));
                if (toxicNoise > 0.2f) {
                    r = 0.224f;
                    g = 1.0f;
                    b = 0.078f;
                    break;
                }
                if (toxicNoise > -0.25f) {
                    r = 0.58f;
                    g = 0.92f;
                    b = 0.12f;
                    break;
                }
                r = 0.08f;
                g = 0.42f;
                b = 0.15f;
                break;
            }
            case 4: {
                float purpleNoise = (float)Math.sin(lat * 10.0f + lon * 2.0f);
                if (Math.abs(lat) > 0.8f) {
                    r = 0.0f;
                    g = 0.95f;
                    b = 1.0f;
                    break;
                }
                if (purpleNoise > 0.2f) {
                    r = 0.478f;
                    g = 0.0f;
                    b = 1.0f;
                    break;
                }
                r = 1.0f;
                g = 0.0f;
                b = 0.667f;
                break;
            }
            case 5: {
                float fissure = (float)(Math.sin(7.0 * (double)lon) * Math.cos(6.0 * (double)lat));
                if (Math.abs(fissure) < 0.18f) {
                    r = 1.0f;
                    g = 0.55f;
                    b = 0.08f;
                    diffuse = 1.15f;
                    break;
                }
                if (fissure > 0.25f) {
                    r = 0.14f;
                    g = 0.12f;
                    b = 0.12f;
                    break;
                }
                r = 0.32f;
                g = 0.15f;
                b = 0.1f;
                break;
            }
            case 6: {
                float crack = (float)(Math.sin(8.0 * (double)lon + (double)lat * 5.0) * Math.cos(6.0 * (double)lon - (double)lat * 7.0));
                if (Math.abs(crack) < 0.15f) {
                    r = 0.12f;
                    g = 0.65f;
                    b = 0.95f;
                    break;
                }
                r = 0.88f;
                g = 0.95f;
                b = 1.0f;
                break;
            }
            case 7: {
                r = 0.55f;
                g = 0.5f;
                b = 0.48f;
                break;
            }
        }
        return new float[]{r * diffuse, g * diffuse, b * diffuse, alpha};
    }

    private static void renderPlanetaryRings(PoseStack poseStack, VertexConsumer consumer, float planetRadius, float alpha, int seed) {
        Matrix4f matrix = poseStack.last().pose();
        int ringSectors = 36;
        float innerRadius = planetRadius * 1.4f;
        float outerRadius = planetRadius * 2.5f;
        int ringBands = 5;
        int b = 0;
        while (b < ringBands) {
            float r0 = innerRadius + (outerRadius - innerRadius) * ((float)b / (float)ringBands);
            float r1 = innerRadius + (outerRadius - innerRadius) * ((float)(b + 1) / (float)ringBands);
            float bandAlpha = b == 2 ? alpha * 0.25f : alpha * 0.85f;
            float red = 0.95f - (float)b * 0.08f;
            float green = 0.78f - (float)b * 0.1f;
            float blue = 0.42f - (float)b * 0.06f;
            for (int s = 0; s < ringSectors; ++s) {
                float a0 = (float)((double)s * 2.0 * Math.PI / (double)ringSectors);
                float a1 = (float)((double)(s + 1) * 2.0 * Math.PI / (double)ringSectors);
                float cosA0 = (float)Math.cos(a0);
                float sinA0 = (float)Math.sin(a0);
                float cosA1 = (float)Math.cos(a1);
                float sinA1 = (float)Math.sin(a1);
                consumer.addVertex(matrix, cosA0 * r0, 0.0f, sinA0 * r0).setColor(red, green, blue, bandAlpha);
                consumer.addVertex(matrix, cosA1 * r0, 0.0f, sinA1 * r0).setColor(red, green, blue, bandAlpha);
                consumer.addVertex(matrix, cosA1 * r1, 0.0f, sinA1 * r1).setColor(red, green, blue, bandAlpha);
                consumer.addVertex(matrix, cosA0 * r1, 0.0f, sinA0 * r1).setColor(red, green, blue, bandAlpha);
                consumer.addVertex(matrix, cosA0 * r1, 0.0f, sinA0 * r1).setColor(red, green, blue, bandAlpha);
                consumer.addVertex(matrix, cosA1 * r1, 0.0f, sinA1 * r1).setColor(red, green, blue, bandAlpha);
                consumer.addVertex(matrix, cosA1 * r0, 0.0f, sinA0 * r0).setColor(red, green, blue, bandAlpha);
                consumer.addVertex(matrix, cosA0 * r0, 0.0f, sinA0 * r0).setColor(red, green, blue, bandAlpha);
            }
            ++b;
        }
    }

    private static void renderAtmosphericCorona(PoseStack poseStack, VertexConsumer consumer, float radius, CelestialTheme theme, float alpha) {
        float[] fArray;
        Matrix4f matrix = poseStack.last().pose();
        float innerRadius = radius * 0.98f;
        float outerRadius = radius * 1.32f;
        int haloSegments = 36;
        switch (theme.ordinal()) {
            default: {
                throw new MatchException(null, null);
            }
            case 0: {
                float[] fArray2 = new float[3];
                fArray2[0] = 0.15f;
                fArray2[1] = 0.55f;
                fArray = fArray2;
                fArray2[2] = 1.0f;
                break;
            }
            case 1: {
                float[] fArray3 = new float[3];
                fArray3[0] = 0.95f;
                fArray3[1] = 0.4f;
                fArray = fArray3;
                fArray3[2] = 0.15f;
                break;
            }
            case 2: {
                float[] fArray4 = new float[3];
                fArray4[0] = 0.98f;
                fArray4[1] = 0.75f;
                fArray = fArray4;
                fArray4[2] = 0.35f;
                break;
            }
            case 3: {
                float[] fArray5 = new float[3];
                fArray5[0] = 0.224f;
                fArray5[1] = 1.0f;
                fArray = fArray5;
                fArray5[2] = 0.078f;
                break;
            }
            case 4: {
                float[] fArray6 = new float[3];
                fArray6[0] = 0.7f;
                fArray6[1] = 0.1f;
                fArray = fArray6;
                fArray6[2] = 1.0f;
                break;
            }
            case 5: {
                float[] fArray7 = new float[3];
                fArray7[0] = 1.0f;
                fArray7[1] = 0.4f;
                fArray = fArray7;
                fArray7[2] = 0.05f;
                break;
            }
            case 6: {
                float[] fArray8 = new float[3];
                fArray8[0] = 0.35f;
                fArray8[1] = 0.85f;
                fArray = fArray8;
                fArray8[2] = 1.0f;
                break;
            }
            case 7: {
                float[] fArray9 = new float[3];
                fArray9[0] = 0.6f;
                fArray9[1] = 0.55f;
                fArray = fArray9;
                fArray9[2] = 0.5f;
            }
        }
        float[] haloColor = fArray;
        int s = 0;
        while (s < haloSegments) {
            float a0 = (float)((double)s * 2.0 * Math.PI / (double)haloSegments);
            float a1 = (float)((double)(s + 1) * 2.0 * Math.PI / (double)haloSegments);
            float cosA0 = (float)Math.cos(a0);
            float sinA0 = (float)Math.sin(a0);
            float cosA1 = (float)Math.cos(a1);
            float sinA1 = (float)Math.sin(a1);
            consumer.addVertex(matrix, cosA0 * innerRadius, sinA0 * innerRadius, 0.0f).setColor(haloColor[0], haloColor[1], haloColor[2], alpha * 0.65f);
            consumer.addVertex(matrix, cosA1 * innerRadius, sinA1 * innerRadius, 0.0f).setColor(haloColor[0], haloColor[1], haloColor[2], alpha * 0.65f);
            consumer.addVertex(matrix, cosA1 * outerRadius, sinA1 * outerRadius, 0.0f).setColor(haloColor[0], haloColor[1], haloColor[2], 0.0f);
            consumer.addVertex(matrix, cosA0 * outerRadius, sinA0 * outerRadius, 0.0f).setColor(haloColor[0], haloColor[1], haloColor[2], 0.0f);
            ++s;
        }
    }

    private static void renderAsteroidCluster(PoseStack poseStack, VertexConsumer consumer, float baseRadius, float alpha, int seed, float time) {
        Matrix4f matrix = poseStack.last().pose();
        WarpDebrisRenderer.renderCraggyRock(consumer, matrix, 0.0f, 0.0f, 0.0f, baseRadius, alpha, seed);
        int i = 0;
        while (i < 4) {
            float subOrbit = time * (2.2f + (float)i * 0.9f) + (float)((double)i * Math.PI * 2.0 / 4.0);
            float subDist = baseRadius * (1.6f + (float)i * 0.35f);
            float subX = (float)Math.cos(subOrbit) * subDist;
            float subY = (float)Math.sin(subOrbit * 0.7f) * (subDist * 0.6f);
            float subZ = (float)Math.sin(subOrbit) * subDist;
            float subRadius = baseRadius * (0.2f + (float)i * 0.06f);
            WarpDebrisRenderer.renderCraggyRock(consumer, matrix, subX, subY, subZ, subRadius, alpha, seed + i * 31);
            ++i;
        }
    }

    private static void renderCraggyRock(VertexConsumer consumer, Matrix4f matrix, float cx, float cy, float cz, float radius, float alpha, int seed) {
        int lats = 10;
        int lons = 16;
        int lat = 0;
        while (lat < lats) {
            float phi0 = (float)Math.PI * ((float)lat / (float)lats - 0.5f);
            float phi1 = (float)Math.PI * ((float)(lat + 1) / (float)lats - 0.5f);
            float y0 = (float)Math.sin(phi0);
            float y1 = (float)Math.sin(phi1);
            float cos0 = (float)Math.cos(phi0);
            float cos1 = (float)Math.cos(phi1);
            for (int lon = 0; lon < lons; ++lon) {
                float t0 = (float)((double)lon * 2.0 * Math.PI / (double)lons);
                float t1 = (float)((double)(lon + 1) * 2.0 * Math.PI / (double)lons);
                float p00 = 1.0f + 0.28f * (float)Math.sin(3.0 * (double)t0 + 2.0 * (double)phi0 + (double)seed);
                float p01 = 1.0f + 0.28f * (float)Math.sin(3.0 * (double)t1 + 2.0 * (double)phi0 + (double)seed);
                float p10 = 1.0f + 0.28f * (float)Math.sin(3.0 * (double)t0 + 2.0 * (double)phi1 + (double)seed);
                float p11 = 1.0f + 0.28f * (float)Math.sin(3.0 * (double)t1 + 2.0 * (double)phi1 + (double)seed);
                float x00 = cos0 * (float)Math.cos(t0) * p00 * radius + cx;
                float y00 = y0 * p00 * radius + cy;
                float z00 = cos0 * (float)Math.sin(t0) * p00 * radius + cz;
                float x01 = cos0 * (float)Math.cos(t1) * p01 * radius + cx;
                float y01 = y0 * p01 * radius + cy;
                float z01 = cos0 * (float)Math.sin(t1) * p01 * radius + cz;
                float x11 = cos1 * (float)Math.cos(t1) * p11 * radius + cx;
                float y11 = y1 * p11 * radius + cy;
                float z11 = cos1 * (float)Math.sin(t1) * p11 * radius + cz;
                float x10 = cos1 * (float)Math.cos(t0) * p10 * radius + cx;
                float y10 = y1 * p10 * radius + cy;
                float z10 = cos1 * (float)Math.sin(t0) * p10 * radius + cz;
                float shade = 0.35f + 0.45f * ((float)lon / (float)lons);
                float r = 0.52f * shade;
                float g = 0.48f * shade;
                float b = 0.46f * shade;
                consumer.addVertex(matrix, x00, y00, z00).setColor(r, g, b, alpha);
                consumer.addVertex(matrix, x01, y01, z01).setColor(r, g, b, alpha);
                consumer.addVertex(matrix, x11, y11, z11).setColor(r, g, b, alpha);
                consumer.addVertex(matrix, x10, y10, z10).setColor(r, g, b, alpha);
            }
            ++lat;
        }
    }

    private static int hashSeed(int slot, int cycle) {
        int h = slot * 374761393 + cycle * 668265263;
        h = (h ^ h >>> 13) * 1274126177;
        return h ^ h >>> 16;
    }

    private static float hashFloat(int seed, int index) {
        int h = seed + index * 1013904223;
        h = (h ^ h >>> 15) * 73244475;
        h = (h ^ h >>> 15) * 73244475;
        h ^= h >>> 15;
        return (float)((h & Integer.MAX_VALUE) % 10000) / 10000.0f;
    }
}

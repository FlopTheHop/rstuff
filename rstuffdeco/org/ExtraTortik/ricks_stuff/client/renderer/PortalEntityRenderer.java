/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.PoseStack$Pose
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.Entity
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.client.portal.CustomPortalTextureManager
 *  org.ExtraTortik.ricks_stuff.client.renderer.PortalRenderTypes
 *  org.ExtraTortik.ricks_stuff.entity.PortalEntity
 */
package org.ExtraTortik.ricks_stuff.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.client.portal.CustomPortalTextureManager;
import org.ExtraTortik.ricks_stuff.client.renderer.PortalRenderTypes;
import org.ExtraTortik.ricks_stuff.entity.PortalEntity;

public class PortalEntityRenderer
extends EntityRenderer<PortalEntity> {
    private static final ResourceLocation PORTAL_GREEN = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/entity/portal/green_portal_animated_texture.png");
    private static final ResourceLocation PORTAL_BLUE = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/entity/portal/blue_portal_animated_texture.png");
    private static final ResourceLocation PORTAL_YELLOW = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/entity/portal/yellow_portal_animated_texture.png");
    private static final float[] BEDROCK_PASS_Z_KEYS = new float[]{0.0f, -2.0f, 2.0f, -1.0f, 1.0f, -0.5f, 0.5f, 0.0f};
    private static final float[] BEDROCK_PASS_ANGLE_KEYS = new float[]{0.0f, 10.0f, -10.0f, 5.0f, -5.0f, 2.5f, -2.5f, 0.0f};
    private static final float[] BEDROCK_PASS_LIFT_KEYS = new float[]{0.0f, 0.225f, 0.085f, 0.125f, 0.045f, 0.07f, 0.02f, 0.0f};

    public PortalEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public ResourceLocation getTextureLocation(PortalEntity entity) {
        if (entity.getColor() == 0 && CustomPortalTextureManager.hasCustomTexture()) {
            return CustomPortalTextureManager.getTextureLocation();
        }
        return switch (entity.getColor()) {
            case 1 -> PORTAL_BLUE;
            case 2 -> PORTAL_YELLOW;
            default -> PORTAL_GREEN;
        };
    }

    public void render(PortalEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        float baseScale = entity.getScale();
        int orientation = entity.getOrientation();
        int rotation = entity.getRotation();
        float animScale = 1.0f;
        float time = (float)entity.tickCount + partialTicks;
        if (entity.isClosing()) {
            int closingTimer = entity.getClientClosingTimer();
            float closeElapsed = closingTimer >= 0 ? 9.0f - ((float)closingTimer - partialTicks) : 9.0f;
            float closeProgress = Math.max(0.0f, Math.min(1.0f, closeElapsed / 9.0f));
            if (closeProgress >= 1.0f) {
                animScale = 0.0f;
            } else if (closeProgress < 0.18f) {
                animScale = 1.0f + closeProgress / 0.18f * 0.15f;
            } else {
                float decay = (closeProgress - 0.18f) / 0.82f;
                animScale = Math.max(0.0f, 1.15f * (1.0f - decay));
            }
        } else if (entity.tickCount < 14) {
            float openProgress = Math.min(1.0f, ((float)entity.tickCount + partialTicks) / 14.0f);
            animScale = (float)Math.sin((double)openProgress * Math.PI * 0.5);
            if (openProgress < 0.75f) {
                animScale *= 1.0f + 0.15f * (float)Math.sin((double)(openProgress / 0.75f) * Math.PI);
            }
        }
        float scale = baseScale * animScale;
        if (scale <= 0.001f) {
            poseStack.popPose();
            return;
        }
        if (orientation == 0) {
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0f - entity.getYRot()));
        } else if (orientation == 1) {
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));
            poseStack.mulPose(Axis.ZP.rotationDegrees((float)rotation * 90.0f));
        } else {
            poseStack.mulPose(Axis.XP.rotationDegrees(-90.0f));
            poseStack.mulPose(Axis.ZP.rotationDegrees((float)rotation * 90.0f));
        }
        float width = (orientation == 0 ? 0.95f : 0.95f) * scale;
        float height = (orientation == 0 ? 1.05f : 0.95f) * scale;
        int totalFrames = 40;
        float animSpeed = 0.8f * (float)Config.portalVortexAnimationSpeed;
        if (entity.getColor() == 0 && CustomPortalTextureManager.hasCustomTexture()) {
            totalFrames = CustomPortalTextureManager.getTotalFrames();
            animSpeed = CustomPortalTextureManager.getAnimSpeed() * (float)Config.portalVortexAnimationSpeed;
        }
        float v0 = 0.0f;
        float v1 = 1.0f;
        if (totalFrames > 1) {
            int frame = (int)(time * animSpeed) % totalFrames;
            v0 = (float)frame / (float)totalFrames;
            v1 = (float)(frame + 1) / (float)totalFrames;
        }
        int fullBright = 0xF000F0;
        VertexConsumer portalConsumer = buffer.getBuffer(PortalRenderTypes.portal((ResourceLocation)this.getTextureLocation(entity)));
        PoseStack.Pose portalPose = poseStack.last();
        int passTicks = entity.getClientPassTimer();
        float passProgress = 0.0f;
        if (passTicks > 0) {
            passProgress = Math.max(0.0f, Math.min(1.0f, (18.0f - ((float)passTicks - partialTicks)) / 18.0f));
        }
        this.renderVortexMesh(portalConsumer, portalPose, width, height, v0, v1, fullBright, passProgress, orientation);
        poseStack.popPose();
        super.render((Entity)entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    private void renderVortexMesh(VertexConsumer consumer, PoseStack.Pose poseEntry, float w, float h, float v0, float v1, int light, float passProgress, int orientation) {
        float normalSign = orientation == 0 ? -1.0f : 1.0f;
        float zDisplacement = 0.0f;
        float liftOffset = 0.0f;
        if (passProgress > 0.0f && passProgress <= 1.0f) {
            float unitScale = 2.0f * w / 54.0f;
            zDisplacement = PortalEntityRenderer.sampleCatmullRomSpline(BEDROCK_PASS_Z_KEYS, passProgress) * unitScale;
            liftOffset = PortalEntityRenderer.sampleCatmullRomSpline(BEDROCK_PASS_LIFT_KEYS, passProgress);
        }
        int GRID = 8;
        float totalZOffset = (zDisplacement + liftOffset) * normalSign;
        float lateralDecay = passProgress > 0.0f && passProgress <= 1.0f ? (float)Math.sin((double)passProgress * Math.PI * 2.5) * (1.0f - passProgress) : 0.0f;
        int row = 0;
        while (row < 8) {
            for (int col = 0; col < 8; ++col) {
                int[][] quadIndices;
                for (int[] pt : quadIndices = new int[][]{{col, row}, {col + 1, row}, {col + 1, row + 1}, {col, row + 1}}) {
                    int c = pt[0];
                    int r = pt[1];
                    float fx = (float)c / 8.0f;
                    float fy = (float)r / 8.0f;
                    float gx = fx * 2.0f - 1.0f;
                    float gy = fy * 2.0f - 1.0f;
                    float vx = -w + fx * (2.0f * w);
                    float vy = -h + fy * (2.0f * h);
                    float vz = 0.0f;
                    if (passProgress > 0.0f && passProgress <= 1.0f) {
                        float bell = (float)(Math.cos((double)gx * Math.PI * 0.5) * Math.cos((double)gy * Math.PI * 0.5));
                        vz = totalZOffset * bell;
                        float waveX = (float)Math.sin((double)gy * Math.PI) * 0.025f * lateralDecay * bell;
                        float waveY = (float)Math.sin((double)gx * Math.PI) * 0.025f * lateralDecay * bell;
                        vx += waveX;
                        vy += waveY;
                    }
                    float u = fx;
                    float v = v1 + fy * (v0 - v1);
                    consumer.addVertex(poseEntry, vx, vy, vz).setColor(255, 255, 255, 255).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(poseEntry, 0.0f, 0.0f, normalSign);
                }
            }
            ++row;
        }
    }

    private static float evalCatmullRom(float p0, float p1, float p2, float p3, float t) {
        return 0.5f * (2.0f * p1 + (-p0 + p2) * t + (2.0f * p0 - 5.0f * p1 + 4.0f * p2 - p3) * (t * t) + (-p0 + 3.0f * p1 - 3.0f * p2 + p3) * (t * t * t));
    }

    private static float sampleCatmullRomSpline(float[] keys, float progress) {
        if (progress <= 0.0f) {
            return keys[0];
        }
        if (progress >= 1.0f) {
            return keys[keys.length - 1];
        }
        int n = keys.length - 1;
        float scaled = progress * (float)n;
        int idx = (int)scaled;
        if (idx >= n) {
            return keys[n];
        }
        float t = scaled - (float)idx;
        float p0 = keys[Math.max(0, idx - 1)];
        float p1 = keys[idx];
        float p2 = keys[Math.min(n, idx + 1)];
        float p3 = keys[Math.min(n, idx + 2)];
        return PortalEntityRenderer.evalCatmullRom(p0, p1, p2, p3, t);
    }
}

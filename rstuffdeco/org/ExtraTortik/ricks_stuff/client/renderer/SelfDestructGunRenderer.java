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
 *  net.minecraft.client.renderer.entity.ItemRenderer
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.item.ItemDisplayContext
 *  net.minecraft.world.item.ItemStack
 *  org.ExtraTortik.ricks_stuff.client.renderer.PortalRenderTypes
 *  org.ExtraTortik.ricks_stuff.entity.SelfDestructGunEntity
 */
package org.ExtraTortik.ricks_stuff.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.ExtraTortik.ricks_stuff.client.renderer.PortalRenderTypes;
import org.ExtraTortik.ricks_stuff.entity.SelfDestructGunEntity;

public class SelfDestructGunRenderer
extends EntityRenderer<SelfDestructGunEntity> {
    private static final ResourceLocation SKY_BEAM_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/entity/self_destruct/sky_beam.png");
    private static final ResourceLocation GROUND_PORTAL_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/entity/self_destruct/ground_portal_disc.png");
    private static final ResourceLocation ORB_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/entity/self_destruct/energy_orb.png");
    private static final ResourceLocation ORB_SWIRL_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/entity/self_destruct/orb_swirl_ring.png");
    private static final ResourceLocation FUNNEL_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/entity/self_destruct/singularity_funnel.png");
    private static final ResourceLocation HYPER_BEAM_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/entity/self_destruct/hyper_beam.png");
    private final ItemRenderer itemRenderer;

    public SelfDestructGunRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.shadowRadius = 0.3f;
    }

    public void render(SelfDestructGunEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        float fuse = (float)entity.getFuse() + partialTicks;
        poseStack.pushPose();
        poseStack.translate(0.0, 0.08, 0.0);
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));
        poseStack.scale(0.85f, 0.85f, 0.85f);
        ItemStack gunStack = entity.getGunItem();
        this.itemRenderer.renderStatic(gunStack, ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), entity.getId());
        poseStack.popPose();
        if (fuse >= 105.0f && fuse < 185.0f) {
            float portalRadius;
            if (fuse < 125.0f) {
                portalRadius = (fuse - 105.0f) / 20.0f * 2.2f;
            } else if (fuse < 160.0f) {
                portalRadius = 2.2f + (float)Math.sin(fuse * 0.3f) * 0.15f;
            } else {
                float shrink = (fuse - 160.0f) / 25.0f;
                portalRadius = Math.max(0.05f, 2.2f * (1.0f - shrink));
            }
            this.renderGroundPortal(poseStack, buffer, portalRadius, fuse);
        }
        if (fuse >= 100.0f) {
            if (fuse < 185.0f) {
                float beamR = 0.18f + (float)Math.sin(fuse * 0.5f) * 0.04f;
                this.renderVerticalBeam(poseStack, buffer, SKY_BEAM_TEXTURE, beamR, 128.0f, fuse, 240);
            } else {
                float prog = (fuse - 185.0f) / 20.0f;
                float hyperRadius = 0.2f + (float)Math.pow(prog, 2.2) * 5.5f;
                this.renderVerticalBeam(poseStack, buffer, HYPER_BEAM_TEXTURE, hyperRadius, 256.0f, fuse, 255);
            }
        }
        if (fuse >= 115.0f && fuse < 185.0f) {
            float orbRadius;
            float orbY;
            if (fuse < 160.0f) {
                float prog = (fuse - 115.0f) / 45.0f;
                orbY = 0.5f + prog * 2.6f;
                orbRadius = fuse < 125.0f ? (fuse - 115.0f) / 10.0f * 2.2f : 2.2f + (float)Math.sin(fuse * 0.4f) * 0.1f;
                this.renderFeedingBeam(poseStack, buffer, 0.12, (double)orbY - (double)orbRadius * 0.6, 0.22f, fuse);
            } else {
                float t = (fuse - 160.0f) / 25.0f;
                orbY = 3.1f * (1.0f - t) + 0.15f * t;
                orbRadius = 2.2f * (1.0f - t) + 0.18f * t;
                this.renderFunnelCollapse(poseStack, buffer, t, fuse);
            }
            this.renderRisingOrb(poseStack, buffer, orbY, orbRadius, fuse);
        }
        if (fuse >= 180.0f && fuse < 188.0f) {
            this.renderWhiteCore(poseStack, buffer, 0.45f, fuse);
        }
        poseStack.popPose();
        super.render((Entity)entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    private void renderGroundPortal(PoseStack poseStack, MultiBufferSource buffer, float radius, float fuse) {
        poseStack.pushPose();
        poseStack.translate(0.0, 0.02, 0.0);
        VertexConsumer consumer = buffer.getBuffer(PortalRenderTypes.portal((ResourceLocation)GROUND_PORTAL_TEXTURE));
        PoseStack.Pose poseEntry = poseStack.last();
        int segments = 24;
        float angleOffset = fuse * 0.08f;
        int i = 0;
        while (true) {
            if (i >= segments) {
                poseStack.popPose();
                return;
            }
            float a1 = (float)((double)i * Math.PI * 2.0 / (double)segments) + angleOffset;
            float a2 = (float)((double)(i + 1) * Math.PI * 2.0 / (double)segments) + angleOffset;
            float x1 = (float)Math.cos(a1) * radius;
            float z1 = (float)Math.sin(a1) * radius;
            float x2 = (float)Math.cos(a2) * radius;
            float z2 = (float)Math.sin(a2) * radius;
            float u1 = 0.5f + (float)Math.cos(a1) * 0.5f;
            float v1 = 0.5f + (float)Math.sin(a1) * 0.5f;
            float u2 = 0.5f + (float)Math.cos(a2) * 0.5f;
            float v2 = 0.5f + (float)Math.sin(a2) * 0.5f;
            consumer.addVertex(poseEntry, 0.0f, 0.0f, 0.0f).setColor(255, 255, 255, 255).setUv(0.5f, 0.5f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(poseEntry, 0.0f, 1.0f, 0.0f);
            consumer.addVertex(poseEntry, x1, 0.0f, z1).setColor(255, 255, 255, 240).setUv(u1, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(poseEntry, 0.0f, 1.0f, 0.0f);
            consumer.addVertex(poseEntry, x2, 0.0f, z2).setColor(255, 255, 255, 240).setUv(u2, v2).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(poseEntry, 0.0f, 1.0f, 0.0f);
            consumer.addVertex(poseEntry, 0.0f, 0.0f, 0.0f).setColor(255, 255, 255, 255).setUv(0.5f, 0.5f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(poseEntry, 0.0f, 1.0f, 0.0f);
            ++i;
        }
    }

    private void renderVerticalBeam(PoseStack poseStack, MultiBufferSource buffer, ResourceLocation texture, float radius, float height, float fuse, int alpha) {
        poseStack.pushPose();
        VertexConsumer consumer = buffer.getBuffer(PortalRenderTypes.portal((ResourceLocation)texture));
        PoseStack.Pose poseEntry = poseStack.last();
        int sides = 8;
        float vOffset = -fuse * 0.25f;
        int i = 0;
        while (true) {
            if (i >= sides) {
                poseStack.popPose();
                return;
            }
            float a1 = (float)((double)i * Math.PI * 2.0 / (double)sides);
            float a2 = (float)((double)(i + 1) * Math.PI * 2.0 / (double)sides);
            float x1 = (float)Math.cos(a1) * radius;
            float z1 = (float)Math.sin(a1) * radius;
            float x2 = (float)Math.cos(a2) * radius;
            float z2 = (float)Math.sin(a2) * radius;
            float u1 = (float)i / (float)sides;
            float u2 = (float)(i + 1) / (float)sides;
            consumer.addVertex(poseEntry, x1, 0.0f, z1).setColor(255, 255, 255, alpha).setUv(u1, vOffset).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(poseEntry, 0.0f, 1.0f, 0.0f);
            consumer.addVertex(poseEntry, x2, 0.0f, z2).setColor(255, 255, 255, alpha).setUv(u2, vOffset).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(poseEntry, 0.0f, 1.0f, 0.0f);
            consumer.addVertex(poseEntry, x2, height, z2).setColor(255, 255, 255, alpha).setUv(u2, vOffset + 8.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(poseEntry, 0.0f, 1.0f, 0.0f);
            consumer.addVertex(poseEntry, x1, height, z1).setColor(255, 255, 255, alpha).setUv(u1, vOffset + 8.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(poseEntry, 0.0f, 1.0f, 0.0f);
            ++i;
        }
    }

    private void renderFeedingBeam(PoseStack poseStack, MultiBufferSource buffer, double startY, double endY, float radius, float fuse) {
        if (endY <= startY) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.0, startY, 0.0);
        VertexConsumer consumer = buffer.getBuffer(PortalRenderTypes.portal((ResourceLocation)SKY_BEAM_TEXTURE));
        PoseStack.Pose poseEntry = poseStack.last();
        float h = (float)(endY - startY);
        int sides = 8;
        float vOffset = -fuse * 0.3f;
        int i = 0;
        while (true) {
            if (i >= sides) {
                poseStack.popPose();
                return;
            }
            float a1 = (float)((double)i * Math.PI * 2.0 / (double)sides);
            float a2 = (float)((double)(i + 1) * Math.PI * 2.0 / (double)sides);
            float x1 = (float)Math.cos(a1) * radius;
            float z1 = (float)Math.sin(a1) * radius;
            float x2 = (float)Math.cos(a2) * radius;
            float z2 = (float)Math.sin(a2) * radius;
            float u1 = (float)i / (float)sides;
            float u2 = (float)(i + 1) / (float)sides;
            consumer.addVertex(poseEntry, x1, 0.0f, z1).setColor(255, 255, 255, 255).setUv(u1, vOffset).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(poseEntry, 0.0f, 1.0f, 0.0f);
            consumer.addVertex(poseEntry, x2, 0.0f, z2).setColor(255, 255, 255, 255).setUv(u2, vOffset).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(poseEntry, 0.0f, 1.0f, 0.0f);
            consumer.addVertex(poseEntry, x2, h, z2).setColor(255, 255, 255, 255).setUv(u2, vOffset + h * 2.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(poseEntry, 0.0f, 1.0f, 0.0f);
            consumer.addVertex(poseEntry, x1, h, z1).setColor(255, 255, 255, 255).setUv(u1, vOffset + h * 2.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(poseEntry, 0.0f, 1.0f, 0.0f);
            ++i;
        }
    }

    private void renderRisingOrb(PoseStack poseStack, MultiBufferSource buffer, float orbY, float radius, float fuse) {
        poseStack.pushPose();
        poseStack.translate(0.0, (double)orbY, 0.0);
        float rotation = fuse * 8.0f;
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        VertexConsumer coreConsumer = buffer.getBuffer(PortalRenderTypes.portal((ResourceLocation)HYPER_BEAM_TEXTURE));
        this.render3DSphere(poseStack, coreConsumer, radius * 0.65f, 12, 16, fuse * 0.04f, 255);
        VertexConsumer orbConsumer = buffer.getBuffer(PortalRenderTypes.portal((ResourceLocation)ORB_TEXTURE));
        this.render3DSphere(poseStack, orbConsumer, radius, 14, 20, -fuse * 0.02f, 240);
        VertexConsumer swirlConsumer = buffer.getBuffer(PortalRenderTypes.portal((ResourceLocation)ORB_SWIRL_TEXTURE));
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-rotation * 1.6f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(28.0f));
        PoseStack.Pose sPose = poseStack.last();
        float swirlR = radius * 1.3f;
        int sSides = 20;
        int i = 0;
        while (true) {
            if (i >= sSides) {
                poseStack.popPose();
                poseStack.popPose();
                return;
            }
            float a1 = (float)((double)i * Math.PI * 2.0 / (double)sSides);
            float a2 = (float)((double)(i + 1) * Math.PI * 2.0 / (double)sSides);
            float x1 = (float)Math.cos(a1) * swirlR;
            float z1 = (float)Math.sin(a1) * swirlR;
            float x2 = (float)Math.cos(a2) * swirlR;
            float z2 = (float)Math.sin(a2) * swirlR;
            float u1 = (float)i / (float)sSides;
            float u2 = (float)(i + 1) / (float)sSides;
            swirlConsumer.addVertex(sPose, x1, -0.22f, z1).setColor(255, 255, 255, 240).setUv(u1, 0.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(sPose, 0.0f, 1.0f, 0.0f);
            swirlConsumer.addVertex(sPose, x2, -0.22f, z2).setColor(255, 255, 255, 240).setUv(u2, 0.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(sPose, 0.0f, 1.0f, 0.0f);
            swirlConsumer.addVertex(sPose, x2, 0.22f, z2).setColor(255, 255, 255, 240).setUv(u2, 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(sPose, 0.0f, 1.0f, 0.0f);
            swirlConsumer.addVertex(sPose, x1, 0.22f, z1).setColor(255, 255, 255, 240).setUv(u1, 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(sPose, 0.0f, 1.0f, 0.0f);
            ++i;
        }
    }

    private void render3DSphere(PoseStack poseStack, VertexConsumer consumer, float radius, int stacks, int slices, float uOffset, int alpha) {
        PoseStack.Pose poseEntry = poseStack.last();
        int j = 0;
        while (j < stacks) {
            float theta1 = (float)(Math.PI * (double)j / (double)stacks);
            float theta2 = (float)(Math.PI * (double)(j + 1) / (double)stacks);
            float y1 = radius * (float)Math.cos(theta1);
            float y2 = radius * (float)Math.cos(theta2);
            float r1 = radius * (float)Math.sin(theta1);
            float r2 = radius * (float)Math.sin(theta2);
            float v1 = (float)j / (float)stacks;
            float v2 = (float)(j + 1) / (float)stacks;
            for (int i = 0; i < slices; ++i) {
                float phi1 = (float)(Math.PI * 2 * (double)i / (double)slices);
                float phi2 = (float)(Math.PI * 2 * (double)(i + 1) / (double)slices);
                float x11 = r1 * (float)Math.cos(phi1);
                float z11 = r1 * (float)Math.sin(phi1);
                float x21 = r2 * (float)Math.cos(phi1);
                float z21 = r2 * (float)Math.sin(phi1);
                float x22 = r2 * (float)Math.cos(phi2);
                float z22 = r2 * (float)Math.sin(phi2);
                float x12 = r1 * (float)Math.cos(phi2);
                float z12 = r1 * (float)Math.sin(phi2);
                float u1 = (float)i / (float)slices + uOffset;
                float u2 = (float)(i + 1) / (float)slices + uOffset;
                consumer.addVertex(poseEntry, x11, y1, z11).setColor(255, 255, 255, alpha).setUv(u1, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(poseEntry, x11 / radius, y1 / radius, z11 / radius);
                consumer.addVertex(poseEntry, x21, y2, z21).setColor(255, 255, 255, alpha).setUv(u1, v2).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(poseEntry, x21 / radius, y2 / radius, z21 / radius);
                consumer.addVertex(poseEntry, x22, y2, z22).setColor(255, 255, 255, alpha).setUv(u2, v2).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(poseEntry, x22 / radius, y2 / radius, z22 / radius);
                consumer.addVertex(poseEntry, x12, y1, z12).setColor(255, 255, 255, alpha).setUv(u2, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(poseEntry, x12 / radius, y1 / radius, z12 / radius);
            }
            ++j;
        }
    }

    private void renderFunnelCollapse(PoseStack poseStack, MultiBufferSource buffer, float progress, float fuse) {
        poseStack.pushPose();
        VertexConsumer consumer = buffer.getBuffer(PortalRenderTypes.portal((ResourceLocation)FUNNEL_TEXTURE));
        float topY = 3.1f * (1.0f - progress * 0.7f);
        float topRadius = 2.2f * (1.0f - progress);
        float botRadius = 0.15f;
        PoseStack.Pose poseEntry = poseStack.last();
        int sides = 16;
        float vOff = fuse * 0.3f;
        int i = 0;
        while (true) {
            if (i >= sides) {
                poseStack.popPose();
                return;
            }
            float a1 = (float)((double)i * Math.PI * 2.0 / (double)sides);
            float a2 = (float)((double)(i + 1) * Math.PI * 2.0 / (double)sides);
            float xt1 = (float)Math.cos(a1) * topRadius;
            float zt1 = (float)Math.sin(a1) * topRadius;
            float xt2 = (float)Math.cos(a2) * topRadius;
            float zt2 = (float)Math.sin(a2) * topRadius;
            float xb1 = (float)Math.cos(a1) * botRadius;
            float zb1 = (float)Math.sin(a1) * botRadius;
            float xb2 = (float)Math.cos(a2) * botRadius;
            float zb2 = (float)Math.sin(a2) * botRadius;
            float u1 = (float)i / (float)sides;
            float u2 = (float)(i + 1) / (float)sides;
            consumer.addVertex(poseEntry, xb1, 0.1f, zb1).setColor(255, 255, 255, 255).setUv(u1, vOff + 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(poseEntry, 0.0f, 1.0f, 0.0f);
            consumer.addVertex(poseEntry, xb2, 0.1f, zb2).setColor(255, 255, 255, 255).setUv(u2, vOff + 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(poseEntry, 0.0f, 1.0f, 0.0f);
            consumer.addVertex(poseEntry, xt2, topY, zt2).setColor(255, 255, 255, 255).setUv(u2, vOff).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(poseEntry, 0.0f, 1.0f, 0.0f);
            consumer.addVertex(poseEntry, xt1, topY, zt1).setColor(255, 255, 255, 255).setUv(u1, vOff).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(poseEntry, 0.0f, 1.0f, 0.0f);
            ++i;
        }
    }

    private void renderWhiteCore(PoseStack poseStack, MultiBufferSource buffer, float radius, float fuse) {
        poseStack.pushPose();
        poseStack.translate(0.0, 0.15, 0.0);
        VertexConsumer consumer = buffer.getBuffer(PortalRenderTypes.portal((ResourceLocation)HYPER_BEAM_TEXTURE));
        this.render3DSphere(poseStack, consumer, radius, 10, 14, fuse * 0.05f, 255);
        poseStack.popPose();
    }

    public ResourceLocation getTextureLocation(SelfDestructGunEntity entity) {
        return SKY_BEAM_TEXTURE;
    }
}

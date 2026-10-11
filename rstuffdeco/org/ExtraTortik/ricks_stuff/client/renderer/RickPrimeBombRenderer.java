/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.PoseStack$Pose
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.Entity
 *  org.ExtraTortik.ricks_stuff.client.model.RickPrimeBombModel
 *  org.ExtraTortik.ricks_stuff.client.renderer.PortalRenderTypes
 *  org.ExtraTortik.ricks_stuff.entity.RickPrimeBombEntity
 */
package org.ExtraTortik.ricks_stuff.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.ExtraTortik.ricks_stuff.client.model.RickPrimeBombModel;
import org.ExtraTortik.ricks_stuff.client.renderer.PortalRenderTypes;
import org.ExtraTortik.ricks_stuff.entity.RickPrimeBombEntity;

public class RickPrimeBombRenderer
extends EntityRenderer<RickPrimeBombEntity> {
    public static final ResourceLocation BOMB_TEXTURE_ON = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/entity/bomb/bomb_texture_on.png");
    public static final ResourceLocation BOMB_TEXTURE_FLASH = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/entity/bomb/bomb_texture_flash.png");
    public static final ResourceLocation BOMB_TEXTURE_OFF = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/entity/bomb/bomb_texture_off.png");
    public static final ResourceLocation ENERGY_SPHERE_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/particle/magenta_energy_sphere.png");
    private final RickPrimeBombModel model = new RickPrimeBombModel();

    public RickPrimeBombRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.4f;
    }

    public void render(RickPrimeBombEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        float radius;
        poseStack.pushPose();
        poseStack.translate(0.0, 0.532, 0.0);
        float rollAngle = entity.getRollAngle(partialTicks);
        float rollYaw = entity.getRollYaw(partialTicks);
        if (Math.abs(rollAngle) > 0.01f) {
            poseStack.mulPose(Axis.YP.rotationDegrees(-rollYaw + 90.0f));
            poseStack.mulPose(Axis.ZP.rotationDegrees(-rollAngle));
            poseStack.mulPose(Axis.YP.rotationDegrees(rollYaw - 90.0f));
        }
        ResourceLocation texture = this.getTextureLocation(entity);
        int light = entity.isFlashing() ? 0xF000F0 : packedLight;
        VertexConsumer modelConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull((ResourceLocation)texture));
        this.model.render(poseStack, modelConsumer, light, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        if ((entity.isSphereExpanding() || entity.getFuse() >= 60) && (radius = entity.getSphereRadius(partialTicks)) > 0.01f) {
            float time = (float)entity.tickCount + partialTicks;
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(time * 12.0f));
            VertexConsumer coreConsumer = buffer.getBuffer(PortalRenderTypes.portal((ResourceLocation)ENERGY_SPHERE_TEXTURE));
            this.render3DSphere(poseStack, coreConsumer, radius * 0.65f, 14, 18, time * 0.04f, 255, 255, 255, 255);
            poseStack.popPose();
            poseStack.pushPose();
            float pulse = 1.0f + 0.06f * (float)Math.sin(time * 0.7f);
            poseStack.mulPose(Axis.YP.rotationDegrees(-time * 8.0f));
            poseStack.mulPose(Axis.XP.rotationDegrees(15.0f));
            VertexConsumer outerConsumer = buffer.getBuffer(PortalRenderTypes.portal((ResourceLocation)ENERGY_SPHERE_TEXTURE));
            this.render3DSphere(poseStack, outerConsumer, radius * pulse, 16, 22, -time * 0.025f, 255, 140, 240, 235);
            poseStack.popPose();
        }
        poseStack.popPose();
        super.render((Entity)entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    private void render3DSphere(PoseStack poseStack, VertexConsumer consumer, float radius, int stacks, int slices, float uOffset, int r, int g, int b, int a) {
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
                consumer.addVertex(poseEntry, x11, y1, z11).setColor(r, g, b, a).setUv(u1, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(poseEntry, x11 / radius, y1 / radius, z11 / radius);
                consumer.addVertex(poseEntry, x21, y2, z21).setColor(r, g, b, a).setUv(u1, v2).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(poseEntry, x21 / radius, y2 / radius, z21 / radius);
                consumer.addVertex(poseEntry, x22, y2, z22).setColor(r, g, b, a).setUv(u2, v2).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(poseEntry, x22 / radius, y2 / radius, z22 / radius);
                consumer.addVertex(poseEntry, x12, y1, z12).setColor(r, g, b, a).setUv(u2, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(poseEntry, x12 / radius, y1 / radius, z12 / radius);
            }
            ++j;
        }
    }

    public ResourceLocation getTextureLocation(RickPrimeBombEntity entity) {
        if (entity.isArmed()) return entity.isFlashing() ? BOMB_TEXTURE_FLASH : BOMB_TEXTURE_ON;
        return BOMB_TEXTURE_OFF;
    }
}

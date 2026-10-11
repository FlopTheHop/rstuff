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
 *  org.ExtraTortik.ricks_stuff.client.renderer.PortalRenderTypes
 *  org.ExtraTortik.ricks_stuff.entity.PortalProjectileEntity
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
import org.ExtraTortik.ricks_stuff.client.renderer.PortalRenderTypes;
import org.ExtraTortik.ricks_stuff.entity.PortalProjectileEntity;

public class PortalProjectileRenderer
extends EntityRenderer<PortalProjectileEntity> {
    private static final ResourceLocation PROJECTILE_GREEN = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/entity/projectile/green_fluid_projectile.png");
    private static final ResourceLocation PROJECTILE_BLUE = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/entity/projectile/blue_fluid_projectile.png");
    private static final ResourceLocation PROJECTILE_YELLOW = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/entity/projectile/yellow_fluid_projectile.png");

    public PortalProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public ResourceLocation getTextureLocation(PortalProjectileEntity entity) {
        return switch (entity.getColor()) {
            case 1 -> PROJECTILE_BLUE;
            case 2 -> PROJECTILE_YELLOW;
            default -> PROJECTILE_GREEN;
        };
    }

    public void render(PortalProjectileEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        float time = (float)entity.tickCount + partialTicks;
        int fullBright = 0xF000F0;
        VertexConsumer consumer = buffer.getBuffer(PortalRenderTypes.portal((ResourceLocation)this.getTextureLocation(entity)));
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYRot() - 90.0f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(entity.getXRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(time * 30.0f));
        float length = 0.55f;
        float coreRadius = 0.07f;
        float haloRadius = 0.18f;
        this.renderPlasmaCore(poseStack, consumer, length, coreRadius, fullBright);
        this.renderEnergyAura(poseStack, consumer, length * 1.2f, haloRadius, fullBright);
        poseStack.popPose();
        super.render((Entity)entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    private void renderPlasmaCore(PoseStack poseStack, VertexConsumer consumer, float length, float r, int light) {
        float halfL;
        PoseStack.Pose poseEntry = poseStack.last();
        float headX = halfL = length * 0.5f;
        float tailX = -halfL;
        float midX = halfL * 0.3f;
        float tailR = r * 0.35f;
        this.quad(consumer, poseEntry, headX, 0.0f, 0.0f, midX, r, r, midX, -r, r, headX, 0.0f, 0.0f, 0.4f, 0.4f, 0.6f, 0.6f, light);
        this.quad(consumer, poseEntry, headX, 0.0f, 0.0f, midX, -r, r, midX, -r, -r, headX, 0.0f, 0.0f, 0.4f, 0.4f, 0.6f, 0.6f, light);
        this.quad(consumer, poseEntry, headX, 0.0f, 0.0f, midX, -r, -r, midX, r, -r, headX, 0.0f, 0.0f, 0.4f, 0.4f, 0.6f, 0.6f, light);
        this.quad(consumer, poseEntry, headX, 0.0f, 0.0f, midX, r, -r, midX, r, r, headX, 0.0f, 0.0f, 0.4f, 0.4f, 0.6f, 0.6f, light);
        this.quad(consumer, poseEntry, midX, r, r, tailX, tailR, tailR, tailX, -tailR, tailR, midX, -r, r, 0.2f, 0.2f, 0.8f, 0.8f, light);
        this.quad(consumer, poseEntry, midX, -r, r, tailX, -tailR, tailR, tailX, -tailR, -tailR, midX, -r, -r, 0.2f, 0.2f, 0.8f, 0.8f, light);
        this.quad(consumer, poseEntry, midX, -r, -r, tailX, -tailR, -tailR, tailX, tailR, -tailR, midX, r, -r, 0.2f, 0.2f, 0.8f, 0.8f, light);
        this.quad(consumer, poseEntry, midX, r, -r, tailX, tailR, -tailR, tailX, tailR, tailR, midX, r, r, 0.2f, 0.2f, 0.8f, 0.8f, light);
    }

    private void renderEnergyAura(PoseStack poseStack, VertexConsumer consumer, float length, float haloRadius, int light) {
        PoseStack.Pose poseEntry = poseStack.last();
        float halfL = length * 0.5f;
        this.quad(consumer, poseEntry, -halfL, -haloRadius, 0.0f, halfL, -haloRadius, 0.0f, halfL, haloRadius, 0.0f, -halfL, haloRadius, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, light);
        this.quad(consumer, poseEntry, -halfL, 0.0f, -haloRadius, halfL, 0.0f, -haloRadius, halfL, 0.0f, haloRadius, -halfL, 0.0f, haloRadius, 0.0f, 0.0f, 1.0f, 1.0f, light);
        float diagR = haloRadius * 0.707f;
        this.quad(consumer, poseEntry, -halfL, -diagR, -diagR, halfL, -diagR, -diagR, halfL, diagR, diagR, -halfL, diagR, diagR, 0.0f, 0.0f, 1.0f, 1.0f, light);
        this.quad(consumer, poseEntry, -halfL, -diagR, diagR, halfL, -diagR, diagR, halfL, diagR, -diagR, -halfL, diagR, -diagR, 0.0f, 0.0f, 1.0f, 1.0f, light);
    }

    private void quad(VertexConsumer consumer, PoseStack.Pose poseEntry, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, float u1, float v1, float u2, float v2, int light) {
        this.vertex(consumer, poseEntry, x1, y1, z1, u1, v1, light);
        this.vertex(consumer, poseEntry, x2, y2, z2, u2, v1, light);
        this.vertex(consumer, poseEntry, x3, y3, z3, u2, v2, light);
        this.vertex(consumer, poseEntry, x4, y4, z4, u1, v2, light);
    }

    private void vertex(VertexConsumer consumer, PoseStack.Pose poseEntry, float x, float y, float z, float u, float v, int light) {
        consumer.addVertex(poseEntry, x, y, z).setColor(255, 255, 255, 255).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(poseEntry, 0.0f, 1.0f, 0.0f);
    }
}

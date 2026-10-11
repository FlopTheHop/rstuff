/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.phys.AABB
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.client.model.OmegaDeviceModel
 *  org.ExtraTortik.ricks_stuff.client.renderer.OmegaLightningRenderer
 *  org.ExtraTortik.ricks_stuff.client.renderer.OmegaPlasmaSphereRenderer
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDevicePhase
 */
package org.ExtraTortik.ricks_stuff.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.client.model.OmegaDeviceModel;
import org.ExtraTortik.ricks_stuff.client.renderer.OmegaLightningRenderer;
import org.ExtraTortik.ricks_stuff.client.renderer.OmegaPlasmaSphereRenderer;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDevicePhase;

public class OmegaDeviceRenderer
extends EntityRenderer<OmegaDeviceEntity> {
    private final OmegaDeviceModel model = new OmegaDeviceModel();

    public OmegaDeviceRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.0f;
    }

    public AABB getRenderBoundingBox(OmegaDeviceEntity entity) {
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        return new AABB(x - 70.0, y - 70.0, z - 70.0, x + 70.0, y + 70.0, z + 70.0);
    }

    public boolean shouldRender(OmegaDeviceEntity entity, Frustum frustum, double camX, double camY, double camZ) {
        AABB box = this.getRenderBoundingBox(entity);
        return frustum.isVisible(box);
    }

    public ResourceLocation getTextureLocation(OmegaDeviceEntity entity) {
        return OmegaDeviceModel.TEXTURE;
    }

    public void render(OmegaDeviceEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        OmegaDevicePhase phase = entity.getPhase();
        int phaseTicks = entity.getPhaseTicks();
        float coreScale = entity.getInterpolatedCoreScale(partialTicks);
        float jitterX = 0.0f;
        float jitterY = 0.0f;
        float jitterZ = 0.0f;
        if (phase == OmegaDevicePhase.ARMED || phase == OmegaDevicePhase.POWER_PUMP || phase == OmegaDevicePhase.ERASING) {
            float jitterTime = ((float)entity.tickCount + partialTicks) * 25.0f;
            jitterX = (float)(Math.sin(jitterTime * 1.3f) * (double)0.06f);
            jitterY = (float)(Math.cos(jitterTime * 1.7f) * (double)0.06f);
            jitterZ = (float)(Math.sin(jitterTime * 2.1f) * (double)0.06f);
        }
        if (Config.omegaDeviceModelEnabled) {
            poseStack.pushPose();
            poseStack.translate(jitterX, jitterY, jitterZ);
            VertexConsumer deviceBuffer = bufferSource.getBuffer(RenderType.entityCutoutNoCull((ResourceLocation)OmegaDeviceModel.TEXTURE));
            this.model.render(poseStack, deviceBuffer, packedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
            poseStack.popPose();
        }
        this.renderCentralPlasmaCore(entity, poseStack, bufferSource, partialTicks, packedLight, phase, coreScale);
        poseStack.popPose();
        super.render((Entity)entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    private void renderCentralPlasmaCore(OmegaDeviceEntity entity, PoseStack poseStack, MultiBufferSource bufferSource, float partialTicks, int packedLight, OmegaDevicePhase phase, float coreScale) {
        poseStack.pushPose();
        OmegaPlasmaSphereRenderer.renderPlasmaSphere((OmegaDeviceEntity)entity, (PoseStack)poseStack, (MultiBufferSource)bufferSource, (float)partialTicks, (int)packedLight, (int)OverlayTexture.NO_OVERLAY);
        OmegaLightningRenderer.renderConvergentLightning((OmegaDeviceEntity)entity, (PoseStack)poseStack, (MultiBufferSource)bufferSource, (float)partialTicks);
        poseStack.popPose();
    }
}

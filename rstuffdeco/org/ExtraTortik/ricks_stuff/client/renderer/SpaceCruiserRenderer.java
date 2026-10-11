/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.phys.Vec3
 *  org.ExtraTortik.ricks_stuff.client.model.SpaceCruiserModel
 *  org.ExtraTortik.ricks_stuff.client.renderer.CatastrophicFireRenderer
 *  org.ExtraTortik.ricks_stuff.client.renderer.PortalRenderTypes
 *  org.ExtraTortik.ricks_stuff.client.renderer.RealityCrackRenderer
 *  org.ExtraTortik.ricks_stuff.client.renderer.RealityShatterRenderer
 *  org.ExtraTortik.ricks_stuff.client.renderer.ReentryShockwaveRenderer
 *  org.ExtraTortik.ricks_stuff.client.renderer.SpaceCruiserRenderer$1
 *  org.ExtraTortik.ricks_stuff.client.renderer.ThrusterFlameRenderer
 *  org.ExtraTortik.ricks_stuff.client.renderer.WarpTunnelRenderer
 *  org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity
 *  org.ExtraTortik.ricks_stuff.entity.WarpState
 *  org.joml.Matrix4f
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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.ExtraTortik.ricks_stuff.client.model.SpaceCruiserModel;
import org.ExtraTortik.ricks_stuff.client.renderer.CatastrophicFireRenderer;
import org.ExtraTortik.ricks_stuff.client.renderer.PortalRenderTypes;
import org.ExtraTortik.ricks_stuff.client.renderer.RealityCrackRenderer;
import org.ExtraTortik.ricks_stuff.client.renderer.RealityShatterRenderer;
import org.ExtraTortik.ricks_stuff.client.renderer.ReentryShockwaveRenderer;
import org.ExtraTortik.ricks_stuff.client.renderer.SpaceCruiserRenderer;
import org.ExtraTortik.ricks_stuff.client.renderer.ThrusterFlameRenderer;
import org.ExtraTortik.ricks_stuff.client.renderer.WarpTunnelRenderer;
import org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity;
import org.ExtraTortik.ricks_stuff.entity.WarpState;
import org.joml.Matrix4f;

public class SpaceCruiserRenderer
extends EntityRenderer<SpaceCruiserEntity> {
    private final SpaceCruiserModel model = new SpaceCruiserModel();

    public SpaceCruiserRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 3.5f;
    }

    public void render(SpaceCruiserEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        boolean isDestroyed;
        poseStack.pushPose();
        float yaw = Mth.rotLerp((float)partialTicks, (float)entity.yRotO, (float)entity.getYRot());
        float pitch = Mth.rotLerp((float)partialTicks, (float)entity.xRotO, (float)entity.getXRot());
        float roll = entity.getInterpolatedRoll(partialTicks);
        poseStack.mulPose(Axis.YP.rotationDegrees(-yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
        poseStack.mulPose(Axis.ZP.rotationDegrees(roll));
        WarpState warpState = entity.getWarpStateEnum();
        if (warpState == WarpState.TUNNEL_TRANSIT) {
            WarpTunnelRenderer.renderWarpTunnel((PoseStack)poseStack, (MultiBufferSource)buffer, (SpaceCruiserEntity)entity, (float)partialTicks);
        }
        VertexConsumer bodyConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull((ResourceLocation)this.getTextureLocation(entity)));
        this.model.renderBody(poseStack, bodyConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        VertexConsumer domeConsumer = buffer.getBuffer(PortalRenderTypes.spaceCruiserDome((ResourceLocation)this.getTextureLocation(entity)));
        this.model.renderDome(poseStack, domeConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 0.7f);
        if (entity.isHeadlightsOn()) {
            VertexConsumer beamConsumer = buffer.getBuffer(RenderType.lightning());
            this.renderHeadlightBeam(poseStack, beamConsumer, SpaceCruiserEntity.HEADLIGHT_LEFT);
            this.renderHeadlightBeam(poseStack, beamConsumer, SpaceCruiserEntity.HEADLIGHT_RIGHT);
        }
        if (warpState.isWarping()) {
            float warpIntensity = this.calculateWarpThrusterIntensity(entity, warpState, partialTicks);
            if (warpIntensity > 0.02f) {
                CatastrophicFireRenderer.renderWarpThrusterFlames((SpaceCruiserEntity)entity, (PoseStack)poseStack, (MultiBufferSource)buffer, (float)partialTicks, (float)warpIntensity);
            }
        } else if (entity.isEngineOn() && !entity.isCrashing() && !entity.isBroken()) {
            VertexConsumer thrusterConsumer = buffer.getBuffer(PortalRenderTypes.THRUSTER_FLAME);
            ThrusterFlameRenderer.renderThrusterJets((SpaceCruiserEntity)entity, (PoseStack)poseStack, (VertexConsumer)thrusterConsumer, (float)partialTicks);
        }
        float health = entity.getHealth();
        boolean bl = isDestroyed = health <= 0.0f || entity.isCrashing() || entity.isBroken();
        if (isDestroyed) {
            float fireIntensity = entity.isCrashing() ? 1.0f : 0.85f;
            CatastrophicFireRenderer.renderEngineFire((SpaceCruiserEntity)entity, (PoseStack)poseStack, (MultiBufferSource)buffer, (float)partialTicks, (float)fireIntensity);
        }
        double speedKmh = entity.getDeltaMovement().length() * 72.0;
        float reentryIntensity = 0.0f;
        if (speedKmh > 180.0) {
            reentryIntensity = (float)Math.min(1.0, (speedKmh - 180.0) / 60.0);
        }
        if (reentryIntensity > 0.02f) {
            VertexConsumer shockwaveConsumer = buffer.getBuffer(PortalRenderTypes.REENTRY_PLASMA_SHIELD);
            ReentryShockwaveRenderer.renderShockwave((SpaceCruiserEntity)entity, (PoseStack)poseStack, (VertexConsumer)shockwaveConsumer, (float)partialTicks, (float)reentryIntensity);
        }
        if (warpState == WarpState.CHARGING || warpState == WarpState.RIFT_OPENING || warpState == WarpState.ENTERING) {
            RealityCrackRenderer.renderRealityCrack((PoseStack)poseStack, (MultiBufferSource)buffer, (SpaceCruiserEntity)entity, (float)partialTicks);
        } else if (warpState == WarpState.EXITING) {
            RealityShatterRenderer.renderRealityShatter((PoseStack)poseStack, (MultiBufferSource)buffer, (SpaceCruiserEntity)entity, (float)partialTicks);
        }
        poseStack.popPose();
        super.render((Entity)entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    private float calculateWarpThrusterIntensity(SpaceCruiserEntity entity, WarpState warpState, float partialTicks) {
        int warpTimer = entity.getWarpTimer();
        float timerWithPartial = (float)warpTimer + partialTicks;
        return switch (1.$SwitchMap$org$ExtraTortik$ricks_stuff$entity$WarpState[warpState.ordinal()]) {
            case 1 -> {
                float prog = Mth.clamp((float)(timerWithPartial / (float)Math.max(1, WarpState.CHARGING.getDurationTicks())), (float)0.0f, (float)1.0f);
                yield Mth.lerp((float)prog, (float)0.35f, (float)1.0f);
            }
            case 2 -> 1.0f + 0.12f * (float)Math.sin(((float)entity.tickCount + partialTicks) * 0.8f);
            case 3 -> {
                float prog = Mth.clamp((float)(timerWithPartial / (float)Math.max(1, WarpState.ENTERING.getDurationTicks())), (float)0.0f, (float)1.0f);
                yield Mth.lerp((float)prog, (float)1.0f, (float)1.35f);
            }
            case 4 -> 1.25f + 0.1f * (float)Math.sin(((float)entity.tickCount + partialTicks) * 0.9f);
            case 5 -> {
                float prog = Mth.clamp((float)(timerWithPartial / (float)Math.max(1, WarpState.EXITING.getDurationTicks())), (float)0.0f, (float)1.0f);
                yield Mth.lerp((float)prog, (float)1.15f, (float)0.0f);
            }
            default -> 0.0f;
        };
    }

    private void renderHeadlightBeam(PoseStack poseStack, VertexConsumer consumer, Vec3 origin) {
        poseStack.pushPose();
        poseStack.translate(origin.x, origin.y, origin.z);
        Matrix4f pose = poseStack.last().pose();
        int segments = 12;
        float startRadius = 0.35f;
        float endRadius = 6.5f;
        float length = 36.0f;
        float r = 1.0f;
        float g = 0.95f;
        float b = 0.7f;
        float alphaNear = 0.28f;
        float alphaFar = 0.0f;
        int i = 0;
        while (true) {
            if (i >= segments) {
                poseStack.popPose();
                return;
            }
            float angle1 = (float)((double)(i * 2) * Math.PI / (double)segments);
            float angle2 = (float)((double)((i + 1) * 2) * Math.PI / (double)segments);
            float x1_start = (float)Math.cos(angle1) * startRadius;
            float y1_start = (float)Math.sin(angle1) * startRadius;
            float x2_start = (float)Math.cos(angle2) * startRadius;
            float y2_start = (float)Math.sin(angle2) * startRadius;
            float x1_end = (float)Math.cos(angle1) * endRadius;
            float y1_end = (float)Math.sin(angle1) * endRadius;
            float x2_end = (float)Math.cos(angle2) * endRadius;
            float y2_end = (float)Math.sin(angle2) * endRadius;
            consumer.addVertex(pose, x1_start, y1_start, 0.0f).setColor(r, g, b, alphaNear);
            consumer.addVertex(pose, x2_start, y2_start, 0.0f).setColor(r, g, b, alphaNear);
            consumer.addVertex(pose, x2_end, y2_end, length).setColor(r, g, b, alphaFar);
            consumer.addVertex(pose, x1_end, y1_end, length).setColor(r, g, b, alphaFar);
            ++i;
        }
    }

    public ResourceLocation getTextureLocation(SpaceCruiserEntity entity) {
        return SpaceCruiserModel.TEXTURE;
    }
}

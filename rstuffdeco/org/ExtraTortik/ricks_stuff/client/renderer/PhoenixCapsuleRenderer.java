/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.PlayerModel
 *  net.minecraft.client.model.geom.ModelLayers
 *  net.minecraft.client.multiplayer.PlayerInfo
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.blockentity.BlockEntityRenderer
 *  net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider$Context
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.client.resources.DefaultPlayerSkin
 *  net.minecraft.client.resources.PlayerSkin$Model
 *  net.minecraft.core.Direction
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.FastColor$ARGB32
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.AABB
 *  org.ExtraTortik.ricks_stuff.block.PhoenixCapsuleBlock
 *  org.ExtraTortik.ricks_stuff.block.entity.PhoenixCapsuleBlockEntity
 *  org.ExtraTortik.ricks_stuff.client.renderer.PortalRenderTypes
 *  org.joml.Matrix4f
 */
package org.ExtraTortik.ricks_stuff.client.renderer;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import org.ExtraTortik.ricks_stuff.block.PhoenixCapsuleBlock;
import org.ExtraTortik.ricks_stuff.block.entity.PhoenixCapsuleBlockEntity;
import org.ExtraTortik.ricks_stuff.client.renderer.PortalRenderTypes;
import org.joml.Matrix4f;

public class PhoenixCapsuleRenderer
implements BlockEntityRenderer<PhoenixCapsuleBlockEntity> {
    private final PlayerModel<AbstractClientPlayer> steveModel;
    private final PlayerModel<AbstractClientPlayer> alexModel;

    public PhoenixCapsuleRenderer(BlockEntityRendererProvider.Context context) {
        this.steveModel = new PlayerModel(context.bakeLayer(ModelLayers.PLAYER), false);
        this.alexModel = new PlayerModel(context.bakeLayer(ModelLayers.PLAYER_SLIM), true);
    }

    public AABB getRenderBoundingBox(PhoenixCapsuleBlockEntity be) {
        return new AABB(be.getBlockPos()).inflate(1.5, 3.0, 1.5);
    }

    public boolean shouldRenderOffScreen(PhoenixCapsuleBlockEntity be) {
        return true;
    }

    public void render(PhoenixCapsuleBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (be == null) return;
        if (be.isBroken()) {
            return;
        }
        boolean isPrinting = be.isPrinting();
        boolean isReady = be.isReady();
        Direction facing = (Direction)be.getBlockState().getValue((Property)PhoenixCapsuleBlock.FACING);
        float gameTime = (float)(be.getLevel() != null ? be.getLevel().getGameTime() : 0L) + partialTick;
        if (isReady || isPrinting) {
            PlayerInfo playerInfo;
            UUID ownerUUID = be.getOwnerUUID();
            GameProfile profile = be.getOwnerProfile();
            if (profile == null && ownerUUID != null) {
                profile = new GameProfile(ownerUUID, be.getOwnerName() != null ? be.getOwnerName() : "");
            }
            ResourceLocation skinLocation = null;
            Boolean isSlim = null;
            Minecraft mc = Minecraft.getInstance();
            if (ownerUUID != null && mc.getConnection() != null && (playerInfo = mc.getConnection().getPlayerInfo(ownerUUID)) != null) {
                skinLocation = playerInfo.getSkin().texture();
                isSlim = playerInfo.getSkin().model() == PlayerSkin.Model.SLIM;
            }
            if (skinLocation == null) {
                if (profile != null) {
                    skin = mc.getSkinManager().getInsecureSkin(profile);
                    skinLocation = skin.texture();
                    if (isSlim == null) {
                        isSlim = skin.model() == PlayerSkin.Model.SLIM;
                    }
                } else if (ownerUUID != null) {
                    skin = DefaultPlayerSkin.get((UUID)ownerUUID);
                    skinLocation = skin.texture();
                    if (isSlim == null) {
                        isSlim = skin.model() == PlayerSkin.Model.SLIM;
                    }
                } else {
                    skin = DefaultPlayerSkin.get((UUID)UUID.randomUUID());
                    skinLocation = skin.texture();
                    if (isSlim == null) {
                        isSlim = skin.model() == PlayerSkin.Model.SLIM;
                    }
                }
            }
            if (isSlim == null) {
                isSlim = false;
            }
            PlayerModel<AbstractClientPlayer> activeModel = isSlim != false ? this.alexModel : this.steveModel;
            poseStack.pushPose();
            poseStack.translate(0.5, 0.22, 0.5);
            poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot() + 180.0f));
            float scale = be.getCatalystType() == 4 ? 0.35f : 0.7f;
            poseStack.scale(scale, scale, scale);
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0f));
            poseStack.translate(0.0, -1.65, 0.0);
            activeModel.young = false;
            activeModel.riding = false;
            activeModel.crouching = false;
            activeModel.head.xRot = 0.22f + (float)Math.sin(gameTime * 0.04f) * 0.03f;
            activeModel.head.yRot = 0.0f;
            activeModel.head.zRot = 0.0f;
            activeModel.hat.copyFrom(activeModel.head);
            activeModel.leftArm.xRot = 0.1f;
            activeModel.leftArm.zRot = -0.16f - (float)Math.cos(gameTime * 0.03f) * 0.02f;
            activeModel.leftSleeve.copyFrom(activeModel.leftArm);
            activeModel.rightArm.xRot = 0.1f;
            activeModel.rightArm.zRot = 0.16f + (float)Math.cos(gameTime * 0.03f) * 0.02f;
            activeModel.rightSleeve.copyFrom(activeModel.rightArm);
            activeModel.leftLeg.xRot = 0.06f;
            activeModel.leftLeg.zRot = -0.05f;
            activeModel.leftPants.copyFrom(activeModel.leftLeg);
            activeModel.rightLeg.xRot = 0.06f;
            activeModel.rightLeg.zRot = 0.05f;
            activeModel.rightPants.copyFrom(activeModel.rightLeg);
            if (isReady) {
                float bob = (float)Math.sin(gameTime * 0.06f) * 0.045f;
                poseStack.translate(0.0, (double)bob, 0.0);
                VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucent((ResourceLocation)skinLocation));
                int color = FastColor.ARGB32.color((int)239, (int)255, (int)255, (int)255);
                activeModel.renderToBuffer(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY, color);
            } else {
                float progress = be.getSmoothPrintProgress(partialTick);
                float pulse = (float)Math.sin(gameTime * 0.15f) * 0.05f;
                float alpha = Math.min(0.9f, Math.max(0.35f, 0.4f + progress * 0.45f + pulse));
                VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucent((ResourceLocation)skinLocation));
                int color = FastColor.ARGB32.color((int)((int)(alpha * 255.0f)), (int)191, (int)255, (int)204);
                activeModel.renderToBuffer(poseStack, vc, 0xF000F0, OverlayTexture.NO_OVERLAY, color);
            }
            poseStack.popPose();
            if (buffer instanceof MultiBufferSource.BufferSource) {
                MultiBufferSource.BufferSource bs = (MultiBufferSource.BufferSource)buffer;
                bs.endBatch();
            }
        }
        if (!isPrinting) return;
        float progress = be.getSmoothPrintProgress(partialTick);
        float yFeet = 0.28f;
        float yHead = 1.7f;
        float yScanWorld = yFeet + progress * (yHead - yFeet);
        this.renderScanLaser(buffer, poseStack, yScanWorld, facing, gameTime);
        if (!(buffer instanceof MultiBufferSource.BufferSource)) return;
        MultiBufferSource.BufferSource bs = (MultiBufferSource.BufferSource)buffer;
        bs.endBatch();
    }

    private void renderScanLaser(MultiBufferSource buffer, PoseStack poseStack, float yScanWorld, Direction facing, float gameTime) {
        poseStack.pushPose();
        poseStack.translate(0.5, (double)yScanWorld, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot() + 180.0f));
        poseStack.translate(0.0, 0.0, -0.03125);
        VertexConsumer laserConsumer = buffer.getBuffer(PortalRenderTypes.LASER_SCAN_DISC);
        Matrix4f mat = poseStack.last().pose();
        float radius = 0.34f;
        int segments = 32;
        for (int i = 0; i < segments; ++i) {
            float a1 = (float)((double)i * Math.PI * 2.0 / (double)segments);
            float a2 = (float)((double)(i + 1) * Math.PI * 2.0 / (double)segments);
            float x1 = (float)Math.cos(a1) * radius;
            float z1 = (float)Math.sin(a1) * radius;
            float x2 = (float)Math.cos(a2) * radius;
            float z2 = (float)Math.sin(a2) * radius;
            laserConsumer.addVertex(mat, x1, 0.0f, z1).setColor(0.1f, 0.9f, 0.3f, 0.85f);
            laserConsumer.addVertex(mat, x2, 0.0f, z2).setColor(0.1f, 0.9f, 0.3f, 0.85f);
            laserConsumer.addVertex(mat, 0.0f, 0.0f, 0.0f).setColor(0.7f, 1.0f, 0.85f, 0.95f);
            laserConsumer.addVertex(mat, 0.0f, 0.0f, 0.0f).setColor(0.7f, 1.0f, 0.85f, 0.95f);
            laserConsumer.addVertex(mat, 0.0f, 0.0f, 0.0f).setColor(0.7f, 1.0f, 0.85f, 0.95f);
            laserConsumer.addVertex(mat, 0.0f, 0.0f, 0.0f).setColor(0.7f, 1.0f, 0.85f, 0.95f);
            laserConsumer.addVertex(mat, x2, 0.0f, z2).setColor(0.1f, 0.9f, 0.3f, 0.85f);
            laserConsumer.addVertex(mat, x1, 0.0f, z1).setColor(0.1f, 0.9f, 0.3f, 0.85f);
        }
        float rimHeight = 0.018f;
        for (int i = 0; i < segments; ++i) {
            float a1 = (float)((double)i * Math.PI * 2.0 / (double)segments);
            float a2 = (float)((double)(i + 1) * Math.PI * 2.0 / (double)segments);
            float x1 = (float)Math.cos(a1) * radius;
            float z1 = (float)Math.sin(a1) * radius;
            float x2 = (float)Math.cos(a2) * radius;
            float z2 = (float)Math.sin(a2) * radius;
            laserConsumer.addVertex(mat, x1, -rimHeight, z1).setColor(0.2f, 1.0f, 0.5f, 0.9f);
            laserConsumer.addVertex(mat, x2, -rimHeight, z2).setColor(0.2f, 1.0f, 0.5f, 0.9f);
            laserConsumer.addVertex(mat, x2, rimHeight, z2).setColor(0.2f, 1.0f, 0.5f, 0.9f);
            laserConsumer.addVertex(mat, x1, rimHeight, z1).setColor(0.2f, 1.0f, 0.5f, 0.9f);
            laserConsumer.addVertex(mat, x1, rimHeight, z1).setColor(0.2f, 1.0f, 0.5f, 0.9f);
            laserConsumer.addVertex(mat, x2, rimHeight, z2).setColor(0.2f, 1.0f, 0.5f, 0.9f);
            laserConsumer.addVertex(mat, x2, -rimHeight, z2).setColor(0.2f, 1.0f, 0.5f, 0.9f);
            laserConsumer.addVertex(mat, x1, -rimHeight, z1).setColor(0.2f, 1.0f, 0.5f, 0.9f);
        }
        float rotAngle = gameTime * 0.04f;
        float innerRadius = 0.22f;
        float beamWidth = 0.015f;
        for (int b = 0; b < 4; ++b) {
            float angle = rotAngle + (float)((double)b * Math.PI / 2.0);
            float cos = (float)Math.cos(angle);
            float sin = (float)Math.sin(angle);
            float perpX = -sin * beamWidth;
            float perpZ = cos * beamWidth;
            float endX = cos * radius;
            float endZ = sin * radius;
            laserConsumer.addVertex(mat, -perpX, 0.001f, -perpZ).setColor(0.8f, 1.0f, 0.9f, 0.95f);
            laserConsumer.addVertex(mat, endX - perpX, 0.001f, endZ - perpZ).setColor(0.3f, 1.0f, 0.5f, 0.85f);
            laserConsumer.addVertex(mat, endX + perpX, 0.001f, endZ + perpZ).setColor(0.3f, 1.0f, 0.5f, 0.85f);
            laserConsumer.addVertex(mat, perpX, 0.001f, perpZ).setColor(0.8f, 1.0f, 0.9f, 0.95f);
            laserConsumer.addVertex(mat, perpX, 0.001f, perpZ).setColor(0.8f, 1.0f, 0.9f, 0.95f);
            laserConsumer.addVertex(mat, endX + perpX, 0.001f, endZ + perpZ).setColor(0.3f, 1.0f, 0.5f, 0.85f);
            laserConsumer.addVertex(mat, endX - perpX, 0.001f, endZ - perpZ).setColor(0.3f, 1.0f, 0.5f, 0.85f);
            laserConsumer.addVertex(mat, -perpX, 0.001f, -perpZ).setColor(0.8f, 1.0f, 0.9f, 0.95f);
        }
        int i = 0;
        while (true) {
            if (i >= segments) {
                poseStack.popPose();
                return;
            }
            float a1 = (float)((double)i * Math.PI * 2.0 / (double)segments) - rotAngle * 0.5f;
            float a2 = (float)((double)(i + 1) * Math.PI * 2.0 / (double)segments) - rotAngle * 0.5f;
            float x1_in = (float)Math.cos(a1) * (innerRadius - 0.015f);
            float z1_in = (float)Math.sin(a1) * (innerRadius - 0.015f);
            float x2_in = (float)Math.cos(a2) * (innerRadius - 0.015f);
            float z2_in = (float)Math.sin(a2) * (innerRadius - 0.015f);
            float x1_out = (float)Math.cos(a1) * (innerRadius + 0.015f);
            float z1_out = (float)Math.sin(a1) * (innerRadius + 0.015f);
            float x2_out = (float)Math.cos(a2) * (innerRadius + 0.015f);
            float z2_out = (float)Math.sin(a2) * (innerRadius + 0.015f);
            laserConsumer.addVertex(mat, x1_in, 0.002f, z1_in).setColor(0.6f, 1.0f, 0.8f, 0.9f);
            laserConsumer.addVertex(mat, x2_in, 0.002f, z2_in).setColor(0.6f, 1.0f, 0.8f, 0.9f);
            laserConsumer.addVertex(mat, x2_out, 0.002f, z2_out).setColor(0.2f, 0.9f, 0.4f, 0.8f);
            laserConsumer.addVertex(mat, x1_out, 0.002f, z1_out).setColor(0.2f, 0.9f, 0.4f, 0.8f);
            laserConsumer.addVertex(mat, x1_out, 0.002f, z1_out).setColor(0.2f, 0.9f, 0.4f, 0.8f);
            laserConsumer.addVertex(mat, x2_out, 0.002f, z2_out).setColor(0.2f, 0.9f, 0.4f, 0.8f);
            laserConsumer.addVertex(mat, x2_in, 0.002f, z2_in).setColor(0.6f, 1.0f, 0.8f, 0.9f);
            laserConsumer.addVertex(mat, x1_in, 0.002f, z1_in).setColor(0.6f, 1.0f, 0.8f, 0.9f);
            ++i;
        }
    }
}

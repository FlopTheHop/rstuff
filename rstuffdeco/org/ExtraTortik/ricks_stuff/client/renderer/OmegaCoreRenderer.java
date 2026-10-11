/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.block.BlockRenderDispatcher
 *  net.minecraft.client.renderer.block.model.BakedQuad
 *  net.minecraft.client.renderer.blockentity.BlockEntityRenderer
 *  net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider$Context
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.client.resources.model.BakedModel
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Vec3i
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.client.model.data.ModelData
 *  org.ExtraTortik.ricks_stuff.block.entity.OmegaCoreBlockEntity
 *  org.ExtraTortik.ricks_stuff.client.renderer.OmegaRenderTypes
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaMultiblockPattern
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaMultiblockPattern$BlockRequirement
 *  org.joml.Matrix4f
 */
package org.ExtraTortik.ricks_stuff.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.ExtraTortik.ricks_stuff.block.entity.OmegaCoreBlockEntity;
import org.ExtraTortik.ricks_stuff.client.renderer.OmegaRenderTypes;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaMultiblockPattern;
import org.joml.Matrix4f;

public class OmegaCoreRenderer
implements BlockEntityRenderer<OmegaCoreBlockEntity> {
    public OmegaCoreRenderer(BlockEntityRendererProvider.Context context) {
    }

    public boolean shouldRenderOffScreen(OmegaCoreBlockEntity be) {
        return true;
    }

    public int getViewDistance() {
        return 96;
    }

    public boolean shouldRender(OmegaCoreBlockEntity be, Vec3 cameraPos) {
        return true;
    }

    public void render(OmegaCoreBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (be == null) return;
        if (be.isAssembled()) {
            return;
        }
        Level level = be.getLevel();
        if (level == null) {
            return;
        }
        List allReqs = OmegaMultiblockPattern.getRequirements();
        if (allReqs == null) return;
        if (allReqs.isEmpty()) {
            return;
        }
        ArrayList<OmegaMultiblockPattern.BlockRequirement> missing = new ArrayList<OmegaMultiblockPattern.BlockRequirement>();
        BlockPos centerPos = be.getBlockPos();
        for (OmegaMultiblockPattern.BlockRequirement req : allReqs) {
            BlockPos worldPos = centerPos.offset((Vec3i)req.relPos());
            BlockState currentState = level.getBlockState(worldPos);
            if (!currentState.isAir() && !currentState.canBeReplaced()) continue;
            missing.add(req);
        }
        if (missing.isEmpty()) {
            return;
        }
        float gameTime = (float)level.getGameTime() + partialTick;
        float time = gameTime * 0.08f;
        float alphaGlow = 0.15f + 0.05f * Mth.sin((float)time);
        float alphaWire = 0.45f + 0.1f * Mth.sin((float)time);
        float colorPhase = 0.5f + 0.5f * Mth.sin((float)(gameTime * 0.05f));
        float r = Mth.lerp((float)colorPhase, (float)0.15f, (float)0.85f);
        float g = Mth.lerp((float)colorPhase, (float)0.85f, (float)0.2f);
        float b = 1.0f;
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        VertexConsumer ghostConsumer = bufferSource.getBuffer(RenderType.translucent());
        RandomSource random = RandomSource.create((long)42L);
        float[] colorMuls = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
        int[] lightmap = new int[]{0xF000F0, 0xF000F0, 0xF000F0, 0xF000F0};
        for (OmegaMultiblockPattern.BlockRequirement blockRequirement : missing) {
            BlockPos rel = blockRequirement.relPos();
            poseStack.pushPose();
            poseStack.translate((float)rel.getX(), (float)rel.getY(), (float)rel.getZ());
            try {
                BlockState expectedState = blockRequirement.expectedState();
                BakedModel model = blockRenderer.getBlockModel(expectedState);
                random.setSeed(42L);
                for (BakedQuad quad : model.getQuads(expectedState, null, random, ModelData.EMPTY, null)) {
                    ghostConsumer.putBulkData(poseStack.last(), quad, colorMuls, r, g, b, 0.35f, lightmap, OverlayTexture.NO_OVERLAY, true);
                }
                for (Direction dir : Direction.values()) {
                    random.setSeed(42L);
                    for (BakedQuad quad : model.getQuads(expectedState, dir, random, ModelData.EMPTY, null)) {
                        ghostConsumer.putBulkData(poseStack.last(), quad, colorMuls, r, g, b, 0.35f, lightmap, OverlayTexture.NO_OVERLAY, true);
                    }
                }
            }
            catch (Throwable expectedState) {
                // empty catch block
            }
            poseStack.popPose();
        }
        VertexConsumer faceConsumer = bufferSource.getBuffer(OmegaRenderTypes.ENERGY_SHIELD);
        for (OmegaMultiblockPattern.BlockRequirement req : missing) {
            BlockPos rel = req.relPos();
            poseStack.pushPose();
            poseStack.translate((float)rel.getX(), (float)rel.getY(), (float)rel.getZ());
            Matrix4f pose = poseStack.last().pose();
            OmegaCoreRenderer.drawHoloBoxFaces(pose, faceConsumer, -0.002f, 1.002f, r, g, b, alphaGlow);
            poseStack.popPose();
        }
        VertexConsumer vertexConsumer = bufferSource.getBuffer(OmegaRenderTypes.HOLOGRAM_LINES);
        Iterator iterator = missing.iterator();
        while (iterator.hasNext()) {
            OmegaMultiblockPattern.BlockRequirement req = (OmegaMultiblockPattern.BlockRequirement)iterator.next();
            BlockPos rel = req.relPos();
            poseStack.pushPose();
            poseStack.translate((float)rel.getX(), (float)rel.getY(), (float)rel.getZ());
            Matrix4f pose = poseStack.last().pose();
            OmegaCoreRenderer.drawWireframeBox(pose, vertexConsumer, -0.003f, 1.003f, r, g, b, alphaWire);
            poseStack.popPose();
        }
    }

    private static void drawHoloBoxFaces(Matrix4f pose, VertexConsumer consumer, float min, float max, float r, float g, float b, float a) {
        OmegaCoreRenderer.drawQuad(pose, consumer, min, min, min, max, min, min, max, min, max, min, min, max, r, g, b, a);
        OmegaCoreRenderer.drawQuad(pose, consumer, min, max, max, max, max, max, max, max, min, min, max, min, r, g, b, a);
        OmegaCoreRenderer.drawQuad(pose, consumer, min, min, min, min, max, min, max, max, min, max, min, min, r, g, b, a);
        OmegaCoreRenderer.drawQuad(pose, consumer, max, min, max, max, max, max, min, max, max, min, min, max, r, g, b, a);
        OmegaCoreRenderer.drawQuad(pose, consumer, min, min, max, min, max, max, min, max, min, min, min, min, r, g, b, a);
        OmegaCoreRenderer.drawQuad(pose, consumer, max, min, min, max, max, min, max, max, max, max, min, max, r, g, b, a);
    }

    private static void drawQuad(Matrix4f pose, VertexConsumer consumer, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, float r, float g, float b, float a) {
        consumer.addVertex(pose, x1, y1, z1).setColor(r, g, b, a);
        consumer.addVertex(pose, x2, y2, z2).setColor(r, g, b, a);
        consumer.addVertex(pose, x3, y3, z3).setColor(r, g, b, a);
        consumer.addVertex(pose, x4, y4, z4).setColor(r, g, b, a);
    }

    private static void drawWireframeBox(Matrix4f pose, VertexConsumer consumer, float min, float max, float r, float g, float b, float a) {
        OmegaCoreRenderer.drawLine(pose, consumer, min, min, min, max, min, min, r, g, b, a);
        OmegaCoreRenderer.drawLine(pose, consumer, max, min, min, max, min, max, r, g, b, a);
        OmegaCoreRenderer.drawLine(pose, consumer, max, min, max, min, min, max, r, g, b, a);
        OmegaCoreRenderer.drawLine(pose, consumer, min, min, max, min, min, min, r, g, b, a);
        OmegaCoreRenderer.drawLine(pose, consumer, min, max, min, max, max, min, r, g, b, a);
        OmegaCoreRenderer.drawLine(pose, consumer, max, max, min, max, max, max, r, g, b, a);
        OmegaCoreRenderer.drawLine(pose, consumer, max, max, max, min, max, max, r, g, b, a);
        OmegaCoreRenderer.drawLine(pose, consumer, min, max, max, min, max, min, r, g, b, a);
        OmegaCoreRenderer.drawLine(pose, consumer, min, min, min, min, max, min, r, g, b, a);
        OmegaCoreRenderer.drawLine(pose, consumer, max, min, min, max, max, min, r, g, b, a);
        OmegaCoreRenderer.drawLine(pose, consumer, max, min, max, max, max, max, r, g, b, a);
        OmegaCoreRenderer.drawLine(pose, consumer, min, min, max, min, max, max, r, g, b, a);
    }

    private static void drawLine(Matrix4f pose, VertexConsumer consumer, float x1, float y1, float z1, float x2, float y2, float z2, float r, float g, float b, float a) {
        consumer.addVertex(pose, x1, y1, z1).setColor(r, g, b, a);
        consumer.addVertex(pose, x2, y2, z2).setColor(r, g, b, a);
    }
}

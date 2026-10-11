/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.geom.EntityModelSet
 *  net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ItemDisplayContext
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.ExtraTortik.ricks_stuff.client.model.SpaceCruiserModel
 *  org.ExtraTortik.ricks_stuff.client.renderer.PortalRenderTypes
 *  org.ExtraTortik.ricks_stuff.client.renderer.SpaceCruiserItemRenderer$1
 */
package org.ExtraTortik.ricks_stuff.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.ExtraTortik.ricks_stuff.client.model.SpaceCruiserModel;
import org.ExtraTortik.ricks_stuff.client.renderer.PortalRenderTypes;
import org.ExtraTortik.ricks_stuff.client.renderer.SpaceCruiserItemRenderer;

@OnlyIn(value=Dist.CLIENT)
public class SpaceCruiserItemRenderer
extends BlockEntityWithoutLevelRenderer {
    private static SpaceCruiserItemRenderer instance;
    private final SpaceCruiserModel model = new SpaceCruiserModel();

    public SpaceCruiserItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
    }

    public static SpaceCruiserItemRenderer getInstance() {
        if (instance != null) return instance;
        instance = new SpaceCruiserItemRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        return instance;
    }

    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        switch (1.$SwitchMap$net$minecraft$world$item$ItemDisplayContext[displayContext.ordinal()]) {
            case 1: {
                poseStack.translate(0.5, 0.42, 0.5);
                poseStack.mulPose(Axis.YP.rotationDegrees(45.0f));
                poseStack.mulPose(Axis.XP.rotationDegrees(28.0f));
                float s = 0.115f;
                poseStack.scale(s, s, s);
                break;
            }
            case 2: {
                poseStack.translate(0.5, 0.35, 0.5);
                poseStack.mulPose(Axis.YP.rotationDegrees(15.0f));
                poseStack.mulPose(Axis.XP.rotationDegrees(25.0f));
                float s = 0.04f;
                poseStack.scale(s, s, s);
                break;
            }
            case 3: {
                poseStack.translate(0.5, 0.35, 0.5);
                poseStack.mulPose(Axis.YP.rotationDegrees(-15.0f));
                poseStack.mulPose(Axis.XP.rotationDegrees(25.0f));
                float s = 0.04f;
                poseStack.scale(s, s, s);
                break;
            }
            case 4: {
                poseStack.translate(0.56, 0.4, 0.48);
                poseStack.mulPose(Axis.YP.rotationDegrees(35.0f));
                poseStack.mulPose(Axis.XP.rotationDegrees(15.0f));
                float s = 0.048f;
                poseStack.scale(s, s, s);
                break;
            }
            case 5: {
                poseStack.translate(0.44, 0.4, 0.48);
                poseStack.mulPose(Axis.YP.rotationDegrees(-35.0f));
                poseStack.mulPose(Axis.XP.rotationDegrees(15.0f));
                float s = 0.048f;
                poseStack.scale(s, s, s);
                break;
            }
            case 6: {
                poseStack.translate(0.5, 0.2, 0.5);
                float s = 0.045f;
                poseStack.scale(s, s, s);
                break;
            }
            case 7: {
                poseStack.translate(0.5, 0.5, 0.5);
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
                float s = 0.048f;
                poseStack.scale(s, s, s);
                break;
            }
            case 8: {
                poseStack.translate(0.5, 0.85, 0.5);
                float s = 0.16f;
                poseStack.scale(s, s, s);
                break;
            }
            default: {
                poseStack.translate(0.5, 0.5, 0.5);
                float s = 0.045f;
                poseStack.scale(s, s, s);
            }
        }
        VertexConsumer bodyConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull((ResourceLocation)SpaceCruiserModel.TEXTURE));
        this.model.renderBody(poseStack, bodyConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        VertexConsumer domeConsumer = buffer.getBuffer(PortalRenderTypes.spaceCruiserDome((ResourceLocation)SpaceCruiserModel.TEXTURE));
        this.model.renderDome(poseStack, domeConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 0.7f);
        poseStack.popPose();
    }
}

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
 *  org.ExtraTortik.ricks_stuff.client.model.AntiPortalFieldGenModel
 *  org.ExtraTortik.ricks_stuff.client.renderer.AntiPortalFieldGenItemRenderer$1
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
import org.ExtraTortik.ricks_stuff.client.model.AntiPortalFieldGenModel;
import org.ExtraTortik.ricks_stuff.client.renderer.AntiPortalFieldGenItemRenderer;

@OnlyIn(value=Dist.CLIENT)
public class AntiPortalFieldGenItemRenderer
extends BlockEntityWithoutLevelRenderer {
    private static AntiPortalFieldGenItemRenderer instance;
    private final AntiPortalFieldGenModel model = new AntiPortalFieldGenModel();

    public AntiPortalFieldGenItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
    }

    public static AntiPortalFieldGenItemRenderer getInstance() {
        if (instance != null) return instance;
        instance = new AntiPortalFieldGenItemRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        return instance;
    }

    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        switch (1.$SwitchMap$net$minecraft$world$item$ItemDisplayContext[displayContext.ordinal()]) {
            case 1: {
                poseStack.translate(0.5, 0.04, 0.5);
                poseStack.mulPose(Axis.YP.rotationDegrees(45.0f));
                poseStack.mulPose(Axis.XP.rotationDegrees(22.0f));
                float s = 0.58f;
                poseStack.scale(s, s, s);
                break;
            }
            case 2: {
                poseStack.translate(0.5, 0.15, 0.5);
                poseStack.mulPose(Axis.YP.rotationDegrees(45.0f));
                float s = 0.38f;
                poseStack.scale(s, s, s);
                break;
            }
            case 3: {
                poseStack.translate(0.5, 0.15, 0.5);
                poseStack.mulPose(Axis.YP.rotationDegrees(225.0f));
                float s = 0.38f;
                poseStack.scale(s, s, s);
                break;
            }
            case 4: 
            case 5: {
                poseStack.translate(0.5, 0.1, 0.5);
                float s = 0.32f;
                poseStack.scale(s, s, s);
                break;
            }
            case 6: {
                poseStack.translate(0.5, 0.05, 0.5);
                float s = 0.35f;
                poseStack.scale(s, s, s);
                break;
            }
            case 7: {
                poseStack.translate(0.5, 0.08, 0.5);
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
                float s = 0.45f;
                poseStack.scale(s, s, s);
                break;
            }
            default: {
                poseStack.translate(0.5, 0.0, 0.5);
                float s = 0.5f;
                poseStack.scale(s, s, s);
            }
        }
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout((ResourceLocation)AntiPortalFieldGenModel.TEXTURE));
        this.model.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        poseStack.popPose();
    }
}

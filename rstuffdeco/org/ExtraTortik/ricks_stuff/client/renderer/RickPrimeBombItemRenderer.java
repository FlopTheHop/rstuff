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
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ItemDisplayContext
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.ExtraTortik.ricks_stuff.client.model.RickPrimeBombModel
 *  org.ExtraTortik.ricks_stuff.client.renderer.RickPrimeBombItemRenderer$1
 *  org.ExtraTortik.ricks_stuff.client.renderer.RickPrimeBombRenderer
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.ExtraTortik.ricks_stuff.client.model.RickPrimeBombModel;
import org.ExtraTortik.ricks_stuff.client.renderer.RickPrimeBombItemRenderer;
import org.ExtraTortik.ricks_stuff.client.renderer.RickPrimeBombRenderer;

@OnlyIn(value=Dist.CLIENT)
public class RickPrimeBombItemRenderer
extends BlockEntityWithoutLevelRenderer {
    private static RickPrimeBombItemRenderer instance;
    private final RickPrimeBombModel model = new RickPrimeBombModel();

    public RickPrimeBombItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
    }

    public static RickPrimeBombItemRenderer getInstance() {
        if (instance != null) return instance;
        instance = new RickPrimeBombItemRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        return instance;
    }

    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        switch (1.$SwitchMap$net$minecraft$world$item$ItemDisplayContext[displayContext.ordinal()]) {
            case 1: {
                poseStack.translate(0.5, 0.5, 0.5);
                poseStack.mulPose(Axis.YP.rotationDegrees(45.0f));
                poseStack.mulPose(Axis.XP.rotationDegrees(28.0f));
                float s = 0.65f;
                poseStack.scale(s, s, s);
                break;
            }
            case 2: {
                poseStack.translate(0.56, 0.46, 0.48);
                poseStack.mulPose(Axis.YP.rotationDegrees(75.0f));
                poseStack.mulPose(Axis.XP.rotationDegrees(12.0f));
                float s = 0.5f;
                poseStack.scale(s, s, s);
                break;
            }
            case 3: {
                poseStack.translate(0.44, 0.46, 0.48);
                poseStack.mulPose(Axis.YP.rotationDegrees(-75.0f));
                poseStack.mulPose(Axis.XP.rotationDegrees(12.0f));
                float s = 0.5f;
                poseStack.scale(s, s, s);
                break;
            }
            case 4: {
                poseStack.translate(0.5, 0.38, 0.52);
                poseStack.mulPose(Axis.XP.rotationDegrees(20.0f));
                float s = 0.42f;
                poseStack.scale(s, s, s);
                break;
            }
            case 5: {
                poseStack.translate(0.5, 0.38, 0.52);
                poseStack.mulPose(Axis.XP.rotationDegrees(20.0f));
                float s = 0.42f;
                poseStack.scale(s, s, s);
                break;
            }
            case 6: {
                poseStack.translate(0.5, 0.3, 0.5);
                float s = 0.55f;
                poseStack.scale(s, s, s);
                break;
            }
            case 7: {
                poseStack.translate(0.5, 0.5, 0.5);
                float s = 0.6f;
                poseStack.scale(s, s, s);
                break;
            }
            default: {
                poseStack.translate(0.5, 0.5, 0.5);
                float s = 0.5f;
                poseStack.scale(s, s, s);
            }
        }
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull((ResourceLocation)RickPrimeBombRenderer.BOMB_TEXTURE_ON));
        this.model.render(poseStack, consumer, packedLight, packedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        poseStack.popPose();
    }
}

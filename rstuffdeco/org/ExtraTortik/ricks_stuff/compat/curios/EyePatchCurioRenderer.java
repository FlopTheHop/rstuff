/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.model.EntityModel
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.ItemRenderer
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.ExtraTortik.ricks_stuff.client.model.EyePatchArmorModel
 *  top.theillusivec4.curios.api.SlotContext
 *  top.theillusivec4.curios.api.client.ICurioRenderer
 */
package org.ExtraTortik.ricks_stuff.compat.curios;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.ExtraTortik.ricks_stuff.client.model.EyePatchArmorModel;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

@OnlyIn(value=Dist.CLIENT)
public class EyePatchCurioRenderer
implements ICurioRenderer {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/models/armor/eye_patch_layer_1.png");

    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (slotContext != null) {
            if (!"head".equals(slotContext.identifier())) {
                return;
            }
            if (!slotContext.visible()) {
                return;
            }
        }
        LivingEntity livingEntity = slotContext != null ? slotContext.entity() : null;
        EyePatchArmorModel model = EyePatchArmorModel.getInstance();
        if (livingEntity != null) {
            model.young = livingEntity.isBaby();
            ICurioRenderer.followHeadRotations((LivingEntity)livingEntity, (ModelPart[])new ModelPart[]{model.head, model.hat});
        }
        VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer((MultiBufferSource)renderTypeBuffer, (RenderType)RenderType.armorCutoutNoCull((ResourceLocation)TEXTURE), (boolean)stack.hasFoil());
        model.renderToBuffer(matrixStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
    }
}

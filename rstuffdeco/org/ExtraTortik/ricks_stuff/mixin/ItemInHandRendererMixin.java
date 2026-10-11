/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.client.renderer.ItemInHandRenderer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  org.ExtraTortik.ricks_stuff.client.PortalGunRecoilManager
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyVariable
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package org.ExtraTortik.ricks_stuff.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.ExtraTortik.ricks_stuff.client.PortalGunRecoilManager;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ItemInHandRenderer.class})
public abstract class ItemInHandRendererMixin {
    @Inject(method={"itemUsed"}, at={@At(value="HEAD")}, cancellable=true)
    private void ricks_stuff$preventPortalGunItemUsedDip(InteractionHand hand, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        ItemStack stack = mc.player.getItemInHand(hand);
        if (!(stack.getItem() instanceof PortalGunItem)) return;
        ci.cancel();
    }

    @ModifyVariable(method={"renderArmWithItem"}, at=@At(value="HEAD"), argsOnly=true, ordinal=2)
    private float ricks_stuff$cancelPortalGunSwing(float swingProgress, AbstractClientPlayer player, float partialTicks, float pitch, InteractionHand hand, float _sp, ItemStack stack) {
        if (!(stack.getItem() instanceof PortalGunItem)) return swingProgress;
        return 0.0f;
    }

    @Inject(method={"renderArmWithItem"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")})
    private void ricks_stuff$applyPortalGunRecoil(AbstractClientPlayer player, float partialTicks, float pitch, InteractionHand hand, float swingProgress, ItemStack stack, float equipProgress, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, CallbackInfo ci) {
        if (!(stack.getItem() instanceof PortalGunItem)) return;
        PortalGunRecoilManager.applyFirstPersonRecoil((PoseStack)poseStack, (Player)player, (InteractionHand)hand, (float)partialTicks);
    }
}

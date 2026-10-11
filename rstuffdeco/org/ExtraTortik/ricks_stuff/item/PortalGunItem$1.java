/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.HumanoidModel$ArmPose
 *  net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.client.extensions.common.IClientItemExtensions
 *  org.ExtraTortik.ricks_stuff.client.renderer.PortalGunItemRenderer
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 */
package org.ExtraTortik.ricks_stuff.item;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.ExtraTortik.ricks_stuff.client.renderer.PortalGunItemRenderer;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;

class PortalGunItem.1
implements IClientItemExtensions {
    PortalGunItem.1(PortalGunItem this$0) {
    }

    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return PortalGunItemRenderer.getInstance();
    }

    public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
        if (itemStack.isEmpty()) return HumanoidModel.ArmPose.EMPTY;
        return HumanoidModel.ArmPose.CROSSBOW_HOLD;
    }
}

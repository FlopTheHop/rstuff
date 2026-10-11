/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.HumanoidModel
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.client.extensions.common.IClientItemExtensions
 *  org.ExtraTortik.ricks_stuff.client.model.EyePatchArmorModel
 *  org.ExtraTortik.ricks_stuff.item.EyePatchItem
 */
package org.ExtraTortik.ricks_stuff.item;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.ExtraTortik.ricks_stuff.client.model.EyePatchArmorModel;
import org.ExtraTortik.ricks_stuff.item.EyePatchItem;

class EyePatchItem.1
implements IClientItemExtensions {
    EyePatchItem.1(EyePatchItem this$0) {
    }

    public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
        return EyePatchArmorModel.getInstance();
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 *  top.theillusivec4.curios.api.CuriosApi
 *  top.theillusivec4.curios.api.SlotResult
 */
package org.ExtraTortik.ricks_stuff.compat.curios;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.ExtraTortik.ricks_stuff.init.ModItems;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

class CuriosInternalHelper {
    CuriosInternalHelper() {
    }

    static boolean isItemEquipped(LivingEntity entity, Item item) {
        return CuriosApi.getCuriosHelper().findFirstCurio(entity, item).isPresent();
    }

    static ItemStack getEquippedShield(LivingEntity entity) {
        if (entity != null) return CuriosApi.getCuriosHelper().findFirstCurio(entity, (Item)ModItems.EVIL_MORTY_ENERGY_SHIELD.get()).map(SlotResult::stack).orElse(ItemStack.EMPTY);
        return ItemStack.EMPTY;
    }

    @Deprecated
    static void sendImc() {
    }
}

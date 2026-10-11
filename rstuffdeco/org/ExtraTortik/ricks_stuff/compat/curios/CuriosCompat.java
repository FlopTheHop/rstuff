/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.neoforged.fml.ModList
 *  org.ExtraTortik.ricks_stuff.compat.curios.CuriosInternalHelper
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 *  org.ExtraTortik.ricks_stuff.item.EvilMortyEnergyShieldItem
 *  org.ExtraTortik.ricks_stuff.item.EyePatchItem
 */
package org.ExtraTortik.ricks_stuff.compat.curios;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import org.ExtraTortik.ricks_stuff.compat.curios.CuriosInternalHelper;
import org.ExtraTortik.ricks_stuff.init.ModItems;
import org.ExtraTortik.ricks_stuff.item.EvilMortyEnergyShieldItem;
import org.ExtraTortik.ricks_stuff.item.EyePatchItem;

public class CuriosCompat {
    public static final String CURIOS_MODID = "curios";

    public static boolean isLoaded() {
        return ModList.get().isLoaded(CURIOS_MODID);
    }

    @Deprecated
    public static void sendImc() {
    }

    public static boolean isWearingEyePatch(LivingEntity entity) {
        if (entity == null) {
            return false;
        }
        if (entity.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof EyePatchItem) {
            return true;
        }
        if (!CuriosCompat.isLoaded()) return false;
        return CuriosInternalHelper.isItemEquipped((LivingEntity)entity, (Item)((Item)ModItems.EYE_PATCH.get()));
    }

    public static ItemStack findShield(Player player) {
        ItemStack curioShield;
        if (player == null) {
            return ItemStack.EMPTY;
        }
        if (CuriosCompat.isLoaded() && !(curioShield = CuriosInternalHelper.getEquippedShield((LivingEntity)player)).isEmpty()) {
            return curioShield;
        }
        ItemStack offhand = player.getOffhandItem();
        if (!offhand.isEmpty() && offhand.is((Item)ModItems.EVIL_MORTY_ENERGY_SHIELD.get())) {
            return offhand;
        }
        int i = 0;
        while (i < 9) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && stack.is((Item)ModItems.EVIL_MORTY_ENERGY_SHIELD.get())) {
                return stack;
            }
            ++i;
        }
        return ItemStack.EMPTY;
    }

    public static boolean hasShield(Player player) {
        return !CuriosCompat.findShield(player).isEmpty();
    }

    public static boolean hasActiveShield(Player player) {
        return EvilMortyEnergyShieldItem.isShieldActive((ItemStack)CuriosCompat.findShield(player));
    }

    public static void tickPlayerShield(Player player) {
        if (player == null) {
            return;
        }
        if (!CuriosCompat.isLoaded()) return;
        ItemStack curioShield = CuriosInternalHelper.getEquippedShield((LivingEntity)player);
        if (curioShield.isEmpty()) return;
        EvilMortyEnergyShieldItem.tickShield((ItemStack)curioShield, (Level)player.level(), (Entity)player);
    }
}

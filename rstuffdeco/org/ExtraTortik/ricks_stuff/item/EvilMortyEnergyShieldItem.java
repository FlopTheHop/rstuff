/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.component.DataComponentType
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.component.CustomData
 *  net.minecraft.world.level.Level
 *  org.ExtraTortik.ricks_stuff.Config
 */
package org.ExtraTortik.ricks_stuff.item;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.ExtraTortik.ricks_stuff.Config;

public class EvilMortyEnergyShieldItem
extends Item {
    public static final int MAX_HEARTS = 10;
    public static final String TAG_HEARTS = "ShieldHearts";
    public static final String TAG_COOLDOWN = "ShieldCooldown";

    public EvilMortyEnergyShieldItem(Item.Properties properties) {
        super(properties);
    }

    public static int getCooldownTicks() {
        return (int)Math.round(Config.evilMortyShieldCooldownSeconds * 20.0);
    }

    public static int getShieldHearts(ItemStack stack) {
        if (stack == null) return 0;
        if (stack.isEmpty()) {
            return 0;
        }
        CustomData customData = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return 10;
        CompoundTag tag = customData.copyTag();
        if (!tag.contains(TAG_HEARTS)) return 10;
        return tag.getInt(TAG_HEARTS);
    }

    public static void setShieldHearts(ItemStack stack, int hearts) {
        if (stack == null) return;
        if (stack.isEmpty()) {
            return;
        }
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> tag.putInt(TAG_HEARTS, Math.max(0, Math.min(10, hearts))));
    }

    public static int getShieldCooldown(ItemStack stack) {
        if (stack == null) return 0;
        if (stack.isEmpty()) {
            return 0;
        }
        CustomData customData = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return 0;
        CompoundTag tag = customData.copyTag();
        if (!tag.contains(TAG_COOLDOWN)) return 0;
        return tag.getInt(TAG_COOLDOWN);
    }

    public static void setShieldCooldown(ItemStack stack, int ticks) {
        if (stack == null) return;
        if (stack.isEmpty()) {
            return;
        }
        int clamped = Math.max(0, ticks);
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> {
            if (clamped == 0) {
                tag.remove(TAG_COOLDOWN);
            } else {
                tag.putInt(TAG_COOLDOWN, clamped);
            }
        });
    }

    public static boolean isOnCooldown(ItemStack stack) {
        return EvilMortyEnergyShieldItem.getShieldCooldown(stack) > 0;
    }

    public static boolean isShieldActive(ItemStack stack) {
        return stack != null && !stack.isEmpty() && EvilMortyEnergyShieldItem.getShieldHearts(stack) > 0 && !EvilMortyEnergyShieldItem.isOnCooldown(stack);
    }

    public static int consumeHeart(ItemStack stack, Player player) {
        if (stack == null) return 0;
        if (stack.isEmpty()) {
            return 0;
        }
        int hearts = EvilMortyEnergyShieldItem.getShieldHearts(stack) - 1;
        if (hearts <= 0) {
            EvilMortyEnergyShieldItem.setShieldHearts(stack, 0);
            int cd = EvilMortyEnergyShieldItem.getCooldownTicks();
            EvilMortyEnergyShieldItem.setShieldCooldown(stack, cd);
            if (player == null) return 0;
            Item item = stack.getItem();
            player.getCooldowns().addCooldown(item, cd);
            stack.hurtAndBreak(1, (LivingEntity)player, EquipmentSlot.MAINHAND);
            return 0;
        }
        EvilMortyEnergyShieldItem.setShieldHearts(stack, hearts);
        return hearts;
    }

    public static void tickShield(ItemStack stack, Level level, Entity entity) {
        if (stack == null) return;
        if (stack.isEmpty()) {
            return;
        }
        if (!EvilMortyEnergyShieldItem.isOnCooldown(stack)) return;
        int cd = EvilMortyEnergyShieldItem.getShieldCooldown(stack) - 1;
        EvilMortyEnergyShieldItem.setShieldCooldown(stack, Math.max(0, cd));
        if (cd > 0) return;
        EvilMortyEnergyShieldItem.setShieldHearts(stack, 10);
    }

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        EvilMortyEnergyShieldItem.tickShield(stack, level, entity);
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.evil_morty_energy_shield.desc").withStyle(ChatFormatting.AQUA));
        tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.evil_morty_energy_shield.hint").withStyle(ChatFormatting.GRAY));
        int hearts = EvilMortyEnergyShieldItem.getShieldHearts(stack);
        int cooldown = EvilMortyEnergyShieldItem.getShieldCooldown(stack);
        tooltip.add((Component)Component.literal((String)("Shield Charge: " + hearts + " / 10")).withStyle(ChatFormatting.GOLD));
        if (cooldown <= 0) return;
        int seconds = (int)Math.ceil((double)cooldown / 20.0);
        tooltip.add((Component)Component.literal((String)("Recharging: " + seconds + "s")).withStyle(ChatFormatting.RED));
    }
}

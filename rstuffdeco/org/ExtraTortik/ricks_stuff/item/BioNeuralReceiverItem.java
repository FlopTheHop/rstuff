/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.component.DataComponentType
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.boss.enderdragon.EnderDragon
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.component.CustomData
 *  net.minecraft.world.level.Level
 *  org.ExtraTortik.ricks_stuff.item.BioNeuralReceiverItem$ClientProxy
 */
package org.ExtraTortik.ricks_stuff.item;

import java.util.List;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.ExtraTortik.ricks_stuff.item.BioNeuralReceiverItem;
import org.jetbrains.annotations.Nullable;

/*
 * Exception performing whole class analysis ignored.
 */
public class BioNeuralReceiverItem
extends Item {
    public static final String TAG_LINKED_UUID = "LinkedMobUUID";
    public static final String TAG_LINKED_ID = "LinkedMobId";
    public static final String TAG_LINKED_NAME = "LinkedMobName";

    public BioNeuralReceiverItem(Item.Properties properties) {
        super(properties);
    }

    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        double maxDist;
        if (!player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }
        if (BioNeuralReceiverItem.hasLinkedMob(stack)) {
            return InteractionResult.PASS;
        }
        if (!(target instanceof Mob)) return InteractionResult.FAIL;
        Mob mob = (Mob)target;
        if (target instanceof Player) {
            return InteractionResult.FAIL;
        }
        CompoundTag persistentData = mob.getPersistentData();
        if (persistentData.getBoolean("BioNeuralChipped")) {
            return InteractionResult.FAIL;
        }
        double d = maxDist = mob instanceof EnderDragon ? 14.0 : 3.8;
        if ((double)player.distanceTo((Entity)mob) > maxDist) {
            return InteractionResult.FAIL;
        }
        if (!player.level().isClientSide()) return InteractionResult.sidedSuccess((boolean)player.level().isClientSide());
        ClientProxy.startImplanting((Mob)mob);
        return InteractionResult.sidedSuccess((boolean)player.level().isClientSide());
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return InteractionResultHolder.pass((Object)player.getItemInHand(hand));
    }

    public static void linkStackToMob(ItemStack stack, Mob mob) {
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> {
            tag.putUUID("LinkedMobUUID", mob.getUUID());
            tag.putInt("LinkedMobId", mob.getId());
            tag.putString("LinkedMobName", mob.getDisplayName().getString());
        });
    }

    public static boolean hasLinkedMob(ItemStack stack) {
        if (stack == null) return false;
        if (!stack.isEmpty()) CustomData customData;
        return (customData = (CustomData)stack.get(DataComponents.CUSTOM_DATA)) != null && customData.copyTag().contains("LinkedMobUUID");
        return false;
    }

    @Nullable
    public static UUID getLinkedMobUUID(ItemStack stack) {
        if (stack == null) return null;
        if (stack.isEmpty()) {
            return null;
        }
        CustomData customData = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return null;
        CompoundTag tag = customData.copyTag();
        if (!tag.hasUUID("LinkedMobUUID")) return null;
        return tag.getUUID("LinkedMobUUID");
    }

    public static int getLinkedMobId(ItemStack stack) {
        if (stack == null) return -1;
        if (stack.isEmpty()) {
            return -1;
        }
        CustomData customData = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return -1;
        CompoundTag tag = customData.copyTag();
        if (!tag.contains("LinkedMobId")) return -1;
        return tag.getInt("LinkedMobId");
    }

    @Nullable
    public static String getLinkedMobName(ItemStack stack) {
        if (stack == null) return null;
        if (stack.isEmpty()) {
            return null;
        }
        CustomData customData = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return null;
        CompoundTag tag = customData.copyTag();
        if (!tag.contains("LinkedMobName")) return null;
        return tag.getString("LinkedMobName");
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        if (BioNeuralReceiverItem.hasLinkedMob(stack)) {
            String name = BioNeuralReceiverItem.getLinkedMobName(stack);
            tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.bio_neural_receiver.linked", (Object[])new Object[]{name != null ? name : "Unknown"}).withStyle(ChatFormatting.GOLD));
        } else {
            tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.bio_neural_receiver.unlinked").withStyle(ChatFormatting.GRAY));
            tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.bio_neural_receiver.desc").withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}

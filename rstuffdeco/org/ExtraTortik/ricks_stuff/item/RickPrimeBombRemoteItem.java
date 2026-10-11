/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.component.DataComponentType
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.component.CustomData
 *  net.minecraft.world.level.Level
 *  org.ExtraTortik.ricks_stuff.entity.RickPrimeBombEntity
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 */
package org.ExtraTortik.ricks_stuff.item;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.ExtraTortik.ricks_stuff.entity.RickPrimeBombEntity;
import org.ExtraTortik.ricks_stuff.init.ModSounds;

public class RickPrimeBombRemoteItem
extends Item {
    public RickPrimeBombRemoteItem(Item.Properties properties) {
        super(properties);
    }

    public static void clearBinding(ItemStack stack) {
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> {
            tag.remove("BoundBomb");
            tag.remove("BoundPos");
            tag.remove("BoundDim");
        });
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        CompoundTag tag;
        super.appendHoverText(stack, context, tooltip, flag);
        CustomData customData = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
        CompoundTag compoundTag = tag = customData != null ? customData.copyTag() : null;
        if (tag != null && tag.hasUUID("BoundBomb")) {
            tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.remote.linked").withStyle(ChatFormatting.GREEN));
            UUID uuid = tag.getUUID("BoundBomb");
            String shortId = uuid.toString().substring(0, 8).toUpperCase();
            tooltip.add((Component)Component.literal((String)("\u00a78[\u00a77ID: #" + shortId + "\u00a78]")));
            if (!tag.contains("BoundPos")) return;
            tooltip.add((Component)Component.literal((String)("  " + tag.getString("BoundPos"))).withStyle(ChatFormatting.DARK_GRAY));
        } else {
            tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.remote.unlinked").withStyle(ChatFormatting.DARK_GRAY));
            tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.remote.hint").withStyle(ChatFormatting.YELLOW));
        }
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        RickPrimeBombEntity bomb;
        ServerLevel serverLevel;
        CompoundTag tag;
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide()) {
            return InteractionResultHolder.sidedSuccess((Object)stack, (boolean)true);
        }
        CustomData customData = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
        CompoundTag compoundTag = tag = customData != null ? customData.copyTag() : null;
        if (tag == null || !tag.hasUUID("BoundBomb")) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), (SoundEvent)ModSounds.GUN_EMPTY.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
            return InteractionResultHolder.fail((Object)stack);
        }
        UUID boundUUID = tag.getUUID("BoundBomb");
        Entity foundEntity = null;
        if (level instanceof ServerLevel && (foundEntity = (serverLevel = (ServerLevel)level).getEntity(boundUUID)) == null && serverLevel.getServer() != null) {
            ServerLevel otherLevel;
            Iterator iterator = serverLevel.getServer().getAllLevels().iterator();
            while (iterator.hasNext() && (foundEntity = (otherLevel = (ServerLevel)iterator.next()).getEntity(boundUUID)) == null) {
            }
        }
        if (foundEntity instanceof RickPrimeBombEntity && (bomb = (RickPrimeBombEntity)foundEntity).isAlive()) {
            bomb.triggerRemoteDetonation();
            level.playSound(null, player.getX(), player.getY(), player.getZ(), (SoundEvent)ModSounds.UI_PRESS.get(), SoundSource.PLAYERS, 1.0f, 0.8f);
            RickPrimeBombRemoteItem.clearBinding(stack);
            player.getCooldowns().addCooldown((Item)this, 40);
            return InteractionResultHolder.success((Object)stack);
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), (SoundEvent)ModSounds.GUN_EMPTY.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        RickPrimeBombRemoteItem.clearBinding(stack);
        return InteractionResultHolder.fail((Object)stack);
    }
}

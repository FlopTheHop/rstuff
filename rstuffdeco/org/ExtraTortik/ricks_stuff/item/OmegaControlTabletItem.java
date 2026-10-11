/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.component.DataComponentType
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.component.CustomData
 *  net.minecraft.world.item.context.UseOnContext
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 *  org.ExtraTortik.ricks_stuff.block.entity.OmegaCoreBlockEntity
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDevicePhase
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaMultiblockPattern
 *  org.ExtraTortik.ricks_stuff.init.ModBlocks
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 */
package org.ExtraTortik.ricks_stuff.item;

import java.util.List;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.ExtraTortik.ricks_stuff.block.entity.OmegaCoreBlockEntity;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDevicePhase;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaMultiblockPattern;
import org.ExtraTortik.ricks_stuff.init.ModBlocks;
import org.ExtraTortik.ricks_stuff.init.ModSounds;

public class OmegaControlTabletItem
extends Item {
    public static final String TAG_BOUND_CORE_POS = "BoundCorePos";
    public static final String TAG_BOUND_DIMENSION = "BoundDimension";
    public static final String TAG_BOUND_DEVICE = "BoundDeviceUUID";

    public OmegaControlTabletItem(Item.Properties properties) {
        super(properties);
    }

    public static boolean isBound(ItemStack stack) {
        CustomData customData = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
        return customData != null && customData.copyTag().contains(TAG_BOUND_CORE_POS);
    }

    public static CompoundTag getTag(ItemStack stack) {
        CustomData customData = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
        return customData != null ? customData.copyTag() : new CompoundTag();
    }

    public static void bindToCore(ItemStack stack, BlockPos pos, String dimension) {
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> {
            tag.putLong(TAG_BOUND_CORE_POS, pos.asLong());
            tag.putString(TAG_BOUND_DIMENSION, dimension);
        });
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        BlockEntity be;
        ResourceLocation dimLoc;
        ItemStack stack = player.getItemInHand(hand);
        if (!OmegaControlTabletItem.isBound(stack)) {
            return InteractionResultHolder.pass((Object)stack);
        }
        if (level.isClientSide) {
            return InteractionResultHolder.sidedSuccess((Object)stack, (boolean)true);
        }
        if (!(player instanceof ServerPlayer)) return InteractionResultHolder.pass((Object)stack);
        if (!(level instanceof ServerLevel)) return InteractionResultHolder.pass((Object)stack);
        ServerLevel serverLevel = (ServerLevel)level;
        CompoundTag tag = OmegaControlTabletItem.getTag(stack);
        long posLong = tag.getLong(TAG_BOUND_CORE_POS);
        String dimStr = tag.getString(TAG_BOUND_DIMENSION);
        BlockPos corePos = BlockPos.of((long)posLong);
        ServerLevel targetLevel = serverLevel;
        if (dimStr != null && !dimStr.isEmpty() && (dimLoc = ResourceLocation.tryParse((String)dimStr)) != null && serverLevel.getServer() != null) {
            ResourceKey dimKey = ResourceKey.create((ResourceKey)Registries.DIMENSION, (ResourceLocation)dimLoc);
            ServerLevel foundLevel = serverLevel.getServer().getLevel(dimKey);
            if (foundLevel != null) {
                targetLevel = foundLevel;
            }
        }
        if (!((be = targetLevel.getBlockEntity(corePos)) instanceof OmegaCoreBlockEntity)) return InteractionResultHolder.pass((Object)stack);
        OmegaCoreBlockEntity core = (OmegaCoreBlockEntity)be;
        if (!core.isAssembled()) return InteractionResultHolder.pass((Object)stack);
        UUID deviceUUID = core.getBoundDeviceUUID();
        if (deviceUUID == null) return InteractionResultHolder.pass((Object)stack);
        Entity entity = targetLevel.getEntity(deviceUUID);
        if (!(entity instanceof OmegaDeviceEntity)) return InteractionResultHolder.pass((Object)stack);
        OmegaDeviceEntity device = (OmegaDeviceEntity)entity;
        if (!device.isAlive()) return InteractionResultHolder.pass((Object)stack);
        if (device.getCooldownRemaining() > 0) {
            return InteractionResultHolder.sidedSuccess((Object)stack, (boolean)false);
        }
        OmegaDevicePhase phase = device.getPhase();
        if (phase == null) {
            phase = OmegaDevicePhase.IDLE;
        }
        if (phase == OmegaDevicePhase.IDLE) {
            device.setPhase(OmegaDevicePhase.ARMED);
            device.setPhaseTicks(0);
            targetLevel.playSound(null, device.getX(), device.getY(), device.getZ(), (SoundEvent)ModSounds.OMEGA_DEVICE_START.get(), SoundSource.BLOCKS, 32.0f, 1.0f);
            serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0f, 1.4f);
            player.getCooldowns().addCooldown((Item)this, 20);
            return InteractionResultHolder.sidedSuccess((Object)stack, (boolean)false);
        }
        if (phase != OmegaDevicePhase.ARMED) return InteractionResultHolder.sidedSuccess((Object)stack, (boolean)false);
        device.setPhase(OmegaDevicePhase.IDLE);
        device.setPhaseTicks(0);
        targetLevel.playSound(null, device.getX(), device.getY(), device.getZ(), (SoundEvent)ModSounds.OMEGA_DEVICE_COOLDOWN.get(), SoundSource.BLOCKS, 10.0f, 1.0f);
        serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 1.0f, 0.8f);
        player.getCooldowns().addCooldown((Item)this, 20);
        return InteractionResultHolder.sidedSuccess((Object)stack, (boolean)false);
    }

    public InteractionResult useOn(UseOnContext context) {
        BlockEntity be;
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        BlockState state = level.getBlockState(pos);
        if (state.is((Block)ModBlocks.OMEGA_CORE.get()) && (be = level.getBlockEntity(pos)) instanceof OmegaCoreBlockEntity) {
            boolean boundToThis;
            OmegaCoreBlockEntity core = (OmegaCoreBlockEntity)be;
            ItemStack stack = context.getItemInHand();
            if (!core.isAssembled()) {
                OmegaControlTabletItem.bindToCore(stack, pos, level.dimension().location().toString());
                if (!level.isClientSide) return InteractionResult.sidedSuccess((boolean)level.isClientSide);
                if (player == null) return InteractionResult.sidedSuccess((boolean)level.isClientSide);
                player.playSound((SoundEvent)ModSounds.UI_PRESS.get(), 1.0f, 1.2f);
                return InteractionResult.sidedSuccess((boolean)level.isClientSide);
            }
            CompoundTag tag = OmegaControlTabletItem.getTag(stack);
            boolean bl = boundToThis = OmegaControlTabletItem.isBound(stack) && tag.getLong(TAG_BOUND_CORE_POS) == pos.asLong() && level.dimension().location().toString().equals(tag.getString(TAG_BOUND_DIMENSION));
            if (player != null && player.isShiftKeyDown() && boundToThis) {
                if (level.isClientSide) return InteractionResult.sidedSuccess((boolean)level.isClientSide);
                if (!(level instanceof ServerLevel)) return InteractionResult.sidedSuccess((boolean)level.isClientSide);
                ServerLevel serverLevel = (ServerLevel)level;
                OmegaMultiblockPattern.disassemble((ServerLevel)serverLevel, (BlockPos)pos, (OmegaCoreBlockEntity)core);
                return InteractionResult.sidedSuccess((boolean)level.isClientSide);
            }
            if (player == null) return InteractionResult.PASS;
            InteractionResultHolder<ItemStack> res = this.use(level, player, context.getHand());
            return res.getResult();
        }
        if (context.getPlayer() == null) return InteractionResult.PASS;
        InteractionResultHolder<ItemStack> res = this.use(context.getLevel(), context.getPlayer(), context.getHand());
        return res.getResult();
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        if (OmegaControlTabletItem.isBound(stack)) {
            CompoundTag tag = OmegaControlTabletItem.getTag(stack);
            long posLong = tag.getLong(TAG_BOUND_CORE_POS);
            BlockPos corePos = BlockPos.of((long)posLong);
            tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.omega_tablet.linked").withStyle(ChatFormatting.GREEN));
            tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.omega_tablet.coords", (Object[])new Object[]{corePos.getX(), corePos.getY(), corePos.getZ()}).withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.omega_tablet.unlinked").withStyle(ChatFormatting.DARK_GRAY));
            tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.omega_tablet.hint").withStyle(ChatFormatting.YELLOW));
        }
    }
}

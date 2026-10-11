/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.component.CustomData
 *  net.minecraft.world.item.context.UseOnContext
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.client.extensions.common.IClientItemExtensions
 *  org.ExtraTortik.ricks_stuff.entity.RickPrimeBombEntity
 */
package org.ExtraTortik.ricks_stuff.item;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.ExtraTortik.ricks_stuff.entity.RickPrimeBombEntity;

public class RickPrimeBombItem
extends Item {
    public RickPrimeBombItem(Item.Properties properties) {
        super(properties);
    }

    private static void applySavedUUID(ItemStack stack, RickPrimeBombEntity bomb) {
        CustomData customData = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return;
        CompoundTag tag = customData.copyTag();
        if (!tag.hasUUID("BombId")) return;
        bomb.setUUID(tag.getUUID("BombId"));
    }

    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        Level level = player.level();
        if (level.isClientSide()) return false;
        Vec3 eyePos = player.getEyePosition();
        Vec3 look = player.getLookAngle();
        double spawnX = player.getX();
        double spawnY = eyePos.y - 0.25;
        double spawnZ = player.getZ();
        RickPrimeBombEntity bomb = new RickPrimeBombEntity(level, spawnX, spawnY, spawnZ);
        RickPrimeBombItem.applySavedUUID(item, bomb);
        Vec3 motion = look.scale(0.45).add(player.getDeltaMovement().scale(0.4)).add(0.0, 0.12, 0.0);
        bomb.setDeltaMovement(motion);
        level.addFreshEntity((Entity)bomb);
        level.playSound(null, spawnX, spawnY, spawnZ, SoundEvents.METAL_PLACE, SoundSource.PLAYERS, 0.9f, 1.1f);
        return false;
    }

    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        Direction clickedFace = context.getClickedFace();
        BlockPos spawnPos = clickedPos.relative(clickedFace);
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        if (level.isClientSide()) return InteractionResult.sidedSuccess((boolean)level.isClientSide());
        double spawnX = (double)spawnPos.getX() + 0.5;
        double spawnY = spawnPos.getY();
        double spawnZ = (double)spawnPos.getZ() + 0.5;
        RickPrimeBombEntity bomb = new RickPrimeBombEntity(level, spawnX, spawnY, spawnZ);
        RickPrimeBombItem.applySavedUUID(stack, bomb);
        if (player != null) {
            Vec3 look = player.getLookAngle();
            bomb.setDeltaMovement(new Vec3(look.x * 0.15, 0.0, look.z * 0.15));
        }
        level.addFreshEntity((Entity)bomb);
        level.playSound(null, spawnX, spawnY, spawnZ, SoundEvents.METAL_PLACE, SoundSource.BLOCKS, 1.0f, 0.8f);
        if (player == null) return InteractionResult.sidedSuccess((boolean)level.isClientSide());
        if (player.getAbilities().instabuild) return InteractionResult.sidedSuccess((boolean)level.isClientSide());
        stack.shrink(1);
        return InteractionResult.sidedSuccess((boolean)level.isClientSide());
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (level.isClientSide()) return InteractionResultHolder.sidedSuccess((Object)itemstack, (boolean)level.isClientSide());
        Vec3 eyePos = player.getEyePosition();
        Vec3 look = player.getLookAngle();
        double spawnX = eyePos.x + look.x * 0.8;
        double spawnY = eyePos.y + look.y * 0.8;
        double spawnZ = eyePos.z + look.z * 0.8;
        RickPrimeBombEntity bomb = new RickPrimeBombEntity(level, spawnX, spawnY, spawnZ);
        RickPrimeBombItem.applySavedUUID(itemstack, bomb);
        bomb.setDeltaMovement(look.scale(0.85).add(0.0, 0.18, 0.0));
        level.addFreshEntity((Entity)bomb);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.METAL_PLACE, SoundSource.PLAYERS, 1.0f, 0.8f);
        if (player.getAbilities().instabuild) return InteractionResultHolder.sidedSuccess((Object)itemstack, (boolean)level.isClientSide());
        itemstack.shrink(1);
        return InteractionResultHolder.sidedSuccess((Object)itemstack, (boolean)level.isClientSide());
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions)new /* Unavailable Anonymous Inner Class!! */);
    }
}

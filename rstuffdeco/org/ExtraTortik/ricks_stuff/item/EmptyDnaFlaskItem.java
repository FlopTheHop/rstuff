/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.level.Level
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.DnaSampleFlaskItem
 */
package org.ExtraTortik.ricks_stuff.item;

import com.mojang.authlib.GameProfile;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.DnaSampleFlaskItem;

public class EmptyDnaFlaskItem
extends Item {
    public EmptyDnaFlaskItem(Item.Properties properties) {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide) return InteractionResultHolder.sidedSuccess((Object)stack, (boolean)level.isClientSide);
        ItemStack sample = DnaSampleFlaskItem.createSample((GameProfile)player.getGameProfile());
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        if (!player.getInventory().add(sample)) {
            player.drop(sample, false);
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), (SoundEvent)ModSounds.BLOOD_SPLASH.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        player.getCooldowns().addCooldown((Item)this, 20);
        return InteractionResultHolder.sidedSuccess((Object)stack, (boolean)level.isClientSide);
    }

    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        double dot;
        double dz;
        if (!(target instanceof Player)) return InteractionResult.PASS;
        Player targetPlayer = (Player)target;
        if (!player.isShiftKeyDown() && !player.isCrouching()) {
            return InteractionResult.PASS;
        }
        float yRot = target.getYRot();
        double lookX = -Math.sin(Math.toRadians(yRot));
        double lookZ = Math.cos(Math.toRadians(yRot));
        double dx = player.getX() - target.getX();
        double dist = Math.sqrt(dx * dx + (dz = player.getZ() - target.getZ()) * dz);
        if (dist > 0.001) {
            dx /= dist;
            dz /= dist;
        }
        if (!((dot = lookX * dx + lookZ * dz) < -0.35)) return InteractionResult.FAIL;
        if (player.level().isClientSide) return InteractionResult.sidedSuccess((boolean)player.level().isClientSide);
        ItemStack sample = DnaSampleFlaskItem.createSample((GameProfile)targetPlayer.getGameProfile());
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        if (!player.getInventory().add(sample)) {
            player.drop(sample, false);
        }
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), (SoundEvent)ModSounds.BLOOD_SPLASH.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        player.getCooldowns().addCooldown((Item)this, 20);
        return InteractionResult.sidedSuccess((boolean)player.level().isClientSide);
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.empty_dna_flask.desc").withStyle(ChatFormatting.GRAY));
        tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.empty_dna_flask.hint_self").withStyle(ChatFormatting.DARK_GRAY));
        tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.empty_dna_flask.hint_other").withStyle(ChatFormatting.DARK_GRAY));
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Holder
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.EquipmentSlot$Type
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.LiquidBlock
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.material.FlowingFluid
 *  org.ExtraTortik.ricks_stuff.init.ModParticles
 */
package org.ExtraTortik.ricks_stuff.block;

import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import org.ExtraTortik.ricks_stuff.init.ModParticles;

public class RealAcidBlock
extends LiquidBlock {
    public RealAcidBlock(Supplier<? extends FlowingFluid> fluid, BlockBehaviour.Properties properties) {
        super(fluid.get(), properties);
    }

    public RealAcidBlock(FlowingFluid fluid, BlockBehaviour.Properties properties) {
        super(fluid, properties);
    }

    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        super.entityInside(state, level, pos, entity);
        if (level.isClientSide) return;
        if (entity instanceof ItemEntity) {
            ItemEntity itemEntity = (ItemEntity)entity;
            ItemStack stack = itemEntity.getItem();
            if (stack.has(DataComponents.FIRE_RESISTANT)) return;
            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.6f, 2.0f + (level.random.nextFloat() - level.random.nextFloat()) * 0.4f);
            entity.discard();
            return;
        }
        if (!(entity instanceof LivingEntity)) return;
        LivingEntity living = (LivingEntity)entity;
        DamageSource acidDamage = RealAcidBlock.getAcidDamageSource(level);
        living.hurt(acidDamage, 4.0f);
        living.addEffect(new MobEffectInstance(MobEffects.POISON, 80, 1, false, false, true));
        living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 1, false, false, true));
        if (level.random.nextInt(10) != 0) return;
        if (!(level instanceof ServerLevel)) return;
        ServerLevel serverLevel = (ServerLevel)level;
        EquipmentSlot[] equipmentSlotArray = EquipmentSlot.values();
        int n = equipmentSlotArray.length;
        int n2 = 0;
        while (n2 < n) {
            ItemStack armor;
            EquipmentSlot slot = equipmentSlotArray[n2];
            if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR && !(armor = living.getItemBySlot(slot)).isEmpty() && !armor.has(DataComponents.FIRE_RESISTANT)) {
                ServerPlayer player;
                ServerPlayer sp = living instanceof ServerPlayer ? (player = (ServerPlayer)living) : null;
                armor.hurtAndBreak(2, serverLevel, sp, item -> living.onEquippedItemBroken(item, slot));
            }
            ++n2;
        }
    }

    public static DamageSource getAcidDamageSource(Level level) {
        try {
            Optional holder = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolder(ResourceKey.create((ResourceKey)Registries.DAMAGE_TYPE, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"acid")));
            if (!holder.isPresent()) return level.damageSources().lava();
            return new DamageSource((Holder)holder.get());
        }
        catch (Exception exception) {
            // empty catch block
        }
        return level.damageSources().lava();
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        RealAcidBlock.renderAcidParticles(level, pos, random);
    }

    public static void renderAcidParticles(Level level, BlockPos pos, RandomSource random) {
        double z;
        double y;
        double x;
        BlockPos abovePos = pos.above();
        if (!level.getBlockState(abovePos).isAir()) return;
        if (random.nextInt(3) == 0) {
            x = (double)pos.getX() + random.nextDouble();
            y = (double)pos.getY() + 0.95;
            z = (double)pos.getZ() + random.nextDouble();
            level.addParticle((ParticleOptions)ModParticles.ACID_VAPOR.get(), x, y, z, (random.nextDouble() - 0.5) * 0.02, 0.03 + random.nextDouble() * 0.03, (random.nextDouble() - 0.5) * 0.02);
        }
        if (random.nextInt(4) == 0) {
            x = (double)pos.getX() + random.nextDouble();
            y = (double)pos.getY() + 0.92;
            z = (double)pos.getZ() + random.nextDouble();
            level.addParticle((ParticleOptions)ModParticles.ACID_BUBBLE.get(), x, y, z, 0.0, 0.02, 0.0);
        }
        if (random.nextInt(7) == 0) {
            x = (double)pos.getX() + random.nextDouble();
            y = (double)pos.getY() + 0.95;
            z = (double)pos.getZ() + random.nextDouble();
            level.addParticle((ParticleOptions)ModParticles.GREEN_FLUID_DROPLET.get(), x, y, z, (random.nextDouble() - 0.5) * 0.04, 0.08 + random.nextDouble() * 0.05, (random.nextDouble() - 0.5) * 0.04);
        }
        if (random.nextInt(80) == 0) {
            level.playLocalSound((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS, 0.25f + random.nextFloat() * 0.2f, 1.4f + random.nextFloat() * 0.4f, false);
        }
        if (random.nextInt(150) != 0) return;
        level.playLocalSound((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.35f + random.nextFloat() * 0.2f, 1.5f + random.nextFloat() * 0.3f, false);
    }
}

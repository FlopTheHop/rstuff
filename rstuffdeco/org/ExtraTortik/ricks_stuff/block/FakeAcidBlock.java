/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.LiquidBlock
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.material.FlowingFluid
 *  org.ExtraTortik.ricks_stuff.block.RealAcidBlock
 */
package org.ExtraTortik.ricks_stuff.block;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import org.ExtraTortik.ricks_stuff.block.RealAcidBlock;

public class FakeAcidBlock
extends LiquidBlock {
    public FakeAcidBlock(Supplier<? extends FlowingFluid> fluid, BlockBehaviour.Properties properties) {
        super(fluid.get(), properties);
    }

    public FakeAcidBlock(FlowingFluid fluid, BlockBehaviour.Properties properties) {
        super(fluid, properties);
    }

    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        super.entityInside(state, level, pos, entity);
        if (!(entity instanceof LivingEntity)) return;
        LivingEntity living = (LivingEntity)entity;
        living.setAirSupply(living.getMaxAirSupply());
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        RealAcidBlock.renderAcidParticles((Level)level, (BlockPos)pos, (RandomSource)random);
    }
}

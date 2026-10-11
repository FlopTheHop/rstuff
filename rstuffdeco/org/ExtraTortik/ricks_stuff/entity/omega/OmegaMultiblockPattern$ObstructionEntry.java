/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.level.block.state.BlockState
 */
package org.ExtraTortik.ricks_stuff.entity.omega;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public record OmegaMultiblockPattern.ObstructionEntry(BlockPos pos, BlockState state, double distSq) {
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.level.block.state.BlockState
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaMultiblockPattern$ObstructionEntry
 */
package org.ExtraTortik.ricks_stuff.entity.omega;

import java.util.Collections;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaMultiblockPattern;
import org.jetbrains.annotations.Nullable;

public record OmegaMultiblockPattern.ClearanceResult(boolean clear, List<OmegaMultiblockPattern.ObstructionEntry> obstructions) {
    public static OmegaMultiblockPattern.ClearanceResult ok() {
        return new OmegaMultiblockPattern.ClearanceResult(true, Collections.<OmegaMultiblockPattern.ObstructionEntry>emptyList());
    }

    public static OmegaMultiblockPattern.ClearanceResult obstructed(List<OmegaMultiblockPattern.ObstructionEntry> obstructions) {
        return new OmegaMultiblockPattern.ClearanceResult(false, obstructions);
    }

    public static OmegaMultiblockPattern.ClearanceResult obstructed(BlockPos pos, BlockState state) {
        return new OmegaMultiblockPattern.ClearanceResult(false, List.of((Object)new OmegaMultiblockPattern.ObstructionEntry(pos, state, 0.0)));
    }

    public boolean isClear() {
        return this.clear;
    }

    @Nullable
    public BlockPos getObstructionPos() {
        return this.obstructions.isEmpty() ? null : this.obstructions.get(0).pos();
    }

    @Nullable
    public BlockState getObstructionState() {
        return this.obstructions.isEmpty() ? null : this.obstructions.get(0).state();
    }

    public List<OmegaMultiblockPattern.ObstructionEntry> getObstructions() {
        return this.obstructions;
    }

    public int getObstructionCount() {
        return this.obstructions.size();
    }
}

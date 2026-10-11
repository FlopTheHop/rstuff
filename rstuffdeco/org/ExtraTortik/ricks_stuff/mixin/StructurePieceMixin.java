/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.WorldGenLevel
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.levelgen.structure.BoundingBox
 *  net.minecraft.world.level.levelgen.structure.StructurePiece
 *  org.ExtraTortik.ricks_stuff.entity.omega.MultiverseErasureSavedData
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package org.ExtraTortik.ricks_stuff.mixin;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import org.ExtraTortik.ricks_stuff.entity.omega.MultiverseErasureSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={StructurePiece.class})
public class StructurePieceMixin {
    @Inject(method={"placeBlock"}, at={@At(value="HEAD")}, cancellable=true)
    private void ricks_stuff$preventErasedStructureBlock(WorldGenLevel level, BlockState blockstate, int x, int y, int z, BoundingBox boundingbox, CallbackInfo ci) {
        if (!MultiverseErasureSavedData.hasAnyActiveErasures()) {
            return;
        }
        if (blockstate == null) return;
        if (blockstate.isAir()) return;
        if (level == null) return;
        MultiverseErasureSavedData storage = MultiverseErasureSavedData.getEffective((Level)level.getLevel());
        if (storage == null) return;
        if (!storage.isBlockErased(blockstate)) return;
        ci.cancel();
    }
}

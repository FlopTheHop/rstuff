/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.RenderShape
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  org.ExtraTortik.ricks_stuff.block.entity.OmegaCoreBlockEntity
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaMultiblockPattern
 */
package org.ExtraTortik.ricks_stuff.block;

import java.util.Iterator;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.ExtraTortik.ricks_stuff.block.entity.OmegaCoreBlockEntity;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaMultiblockPattern;

public class OmegaBarrierBlock
extends Block {
    public OmegaBarrierBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0f;
    }

    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        OmegaCoreBlockEntity core;
        BlockPos checkPos;
        BlockEntity be;
        if (level.isClientSide) return super.playerWillDestroy(level, pos, state, player);
        if (!(level instanceof ServerLevel)) return super.playerWillDestroy(level, pos, state, player);
        ServerLevel serverLevel = (ServerLevel)level;
        Iterator iterator = BlockPos.betweenClosed((BlockPos)pos.offset(-1, -2, -1), (BlockPos)pos.offset(1, 0, 1)).iterator();
        do {
            if (!iterator.hasNext()) return super.playerWillDestroy(level, pos, state, player);
        } while (!((be = serverLevel.getBlockEntity(checkPos = (BlockPos)iterator.next())) instanceof OmegaCoreBlockEntity) || !(core = (OmegaCoreBlockEntity)be).isAssembled() || core.isDisassembling());
        OmegaMultiblockPattern.disassembleWithBreak((ServerLevel)serverLevel, (BlockPos)checkPos.immutable(), (OmegaCoreBlockEntity)core, (BlockPos)pos);
        return super.playerWillDestroy(level, pos, state, player);
    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.is(newState.getBlock())) return;
        if (!level.isClientSide && level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            for (BlockPos checkPos : BlockPos.betweenClosed((BlockPos)pos.offset(-1, -2, -1), (BlockPos)pos.offset(1, 0, 1))) {
                OmegaCoreBlockEntity core;
                BlockEntity be = serverLevel.getBlockEntity(checkPos);
                if (!(be instanceof OmegaCoreBlockEntity) || !(core = (OmegaCoreBlockEntity)be).isAssembled() || !core.isStructureAbsorbed() || core.isDisassembling()) continue;
                OmegaMultiblockPattern.disassemble((ServerLevel)serverLevel, (BlockPos)checkPos.immutable(), (OmegaCoreBlockEntity)core);
                break;
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}

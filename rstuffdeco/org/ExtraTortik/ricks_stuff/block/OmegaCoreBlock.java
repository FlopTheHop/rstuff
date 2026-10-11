/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.ItemInteractionResult
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.BaseEntityBlock
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.RenderShape
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityTicker
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.BooleanProperty
 *  net.minecraft.world.level.block.state.properties.DirectionProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.BlockHitResult
 *  org.ExtraTortik.ricks_stuff.block.entity.OmegaCoreBlockEntity
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDevicePhase
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaMultiblockPattern
 *  org.ExtraTortik.ricks_stuff.init.ModBlockEntities
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.OmegaControlTabletItem
 */
package org.ExtraTortik.ricks_stuff.block;

import com.mojang.serialization.MapCodec;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import org.ExtraTortik.ricks_stuff.block.entity.OmegaCoreBlockEntity;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDevicePhase;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaMultiblockPattern;
import org.ExtraTortik.ricks_stuff.init.ModBlockEntities;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.OmegaControlTabletItem;
import org.jetbrains.annotations.Nullable;

public class OmegaCoreBlock
extends BaseEntityBlock {
    public static final MapCodec<OmegaCoreBlock> CODEC = OmegaCoreBlock.simpleCodec(OmegaCoreBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty ACTIVE = BooleanProperty.create((String)"active");

    public OmegaCoreBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)ACTIVE, (Comparable)Boolean.valueOf(false)));
    }

    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState)this.defaultBlockState().setValue((Property)FACING, (Comparable)context.getHorizontalDirection().getOpposite());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, ACTIVE});
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OmegaCoreBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return OmegaCoreBlock.createTickerHelper(type, (BlockEntityType)((BlockEntityType)ModBlockEntities.OMEGA_CORE_BE.get()), OmegaCoreBlockEntity::tick);
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (level.isClientSide) return;
        if (!(level instanceof ServerLevel)) return;
        ServerLevel serverLevel = (ServerLevel)level;
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof OmegaCoreBlockEntity)) return;
        OmegaCoreBlockEntity core = (OmegaCoreBlockEntity)be;
        if (!OmegaMultiblockPattern.getMissingRequirements((Level)serverLevel, (BlockPos)pos).isEmpty()) return;
        if (!OmegaMultiblockPattern.checkClearance((Level)serverLevel, (BlockPos)pos).clear()) return;
        OmegaMultiblockPattern.assemble((ServerLevel)serverLevel, (BlockPos)pos, (OmegaCoreBlockEntity)core);
    }

    protected ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof OmegaCoreBlockEntity)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        OmegaCoreBlockEntity core = (OmegaCoreBlockEntity)be;
        if (!(heldStack.getItem() instanceof OmegaControlTabletItem)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (!core.isAssembled()) {
            OmegaControlTabletItem.bindToCore((ItemStack)heldStack, (BlockPos)pos, (String)level.dimension().location().toString());
            if (!level.isClientSide) return ItemInteractionResult.sidedSuccess((boolean)level.isClientSide);
            player.playSound((SoundEvent)ModSounds.UI_PRESS.get(), 1.0f, 1.2f);
            return ItemInteractionResult.sidedSuccess((boolean)level.isClientSide);
        }
        if (!player.isShiftKeyDown()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (level.isClientSide) return ItemInteractionResult.sidedSuccess((boolean)level.isClientSide);
        if (!(level instanceof ServerLevel)) return ItemInteractionResult.sidedSuccess((boolean)level.isClientSide);
        ServerLevel serverLevel = (ServerLevel)level;
        OmegaMultiblockPattern.disassemble((ServerLevel)serverLevel, (BlockPos)pos, (OmegaCoreBlockEntity)core);
        return ItemInteractionResult.sidedSuccess((boolean)level.isClientSide);
    }

    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        if (level.isClientSide) return;
        if (!(level instanceof ServerLevel)) return;
        ServerLevel serverLevel = (ServerLevel)level;
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof OmegaCoreBlockEntity)) return;
        OmegaCoreBlockEntity core = (OmegaCoreBlockEntity)be;
        if (!core.isAssembled()) {
            if (!OmegaMultiblockPattern.getMissingRequirements((Level)serverLevel, (BlockPos)pos).isEmpty()) return;
            if (!OmegaMultiblockPattern.checkClearance((Level)serverLevel, (BlockPos)pos).clear()) return;
            OmegaMultiblockPattern.assemble((ServerLevel)serverLevel, (BlockPos)pos, (OmegaCoreBlockEntity)core);
        } else if (!OmegaMultiblockPattern.checkActiveStructure((Level)serverLevel, (BlockPos)pos, (OmegaCoreBlockEntity)core)) {
            OmegaMultiblockPattern.disassemble((ServerLevel)serverLevel, (BlockPos)pos, (OmegaCoreBlockEntity)core);
        } else {
            UUID deviceUUID = core.getBoundDeviceUUID();
            if (deviceUUID == null) return;
            Entity entity = serverLevel.getEntity(deviceUUID);
            if (!(entity instanceof OmegaDeviceEntity)) return;
            OmegaDeviceEntity device = (OmegaDeviceEntity)entity;
            if (!device.isAlive()) return;
            boolean hasSignal = level.hasNeighborSignal(pos);
            if (hasSignal) {
                if (device.getPhase() != OmegaDevicePhase.IDLE) return;
                if (device.getCooldownRemaining() > 0) return;
                device.setPhase(OmegaDevicePhase.ARMED);
                device.setPhaseTicks(0);
                serverLevel.playSound(null, device.getX(), device.getY(), device.getZ(), (SoundEvent)ModSounds.OMEGA_DEVICE_START.get(), SoundSource.BLOCKS, 10.0f, 1.0f);
            } else {
                if (device.getPhase() != OmegaDevicePhase.ARMED) return;
                device.setPhase(OmegaDevicePhase.IDLE);
                device.setPhaseTicks(0);
                serverLevel.playSound(null, device.getX(), device.getY(), device.getZ(), (SoundEvent)ModSounds.OMEGA_DEVICE_COOLDOWN.get(), SoundSource.BLOCKS, 5.0f, 1.0f);
            }
        }
    }

    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (level.isClientSide) return super.playerWillDestroy(level, pos, state, player);
        if (!(level instanceof ServerLevel)) return super.playerWillDestroy(level, pos, state, player);
        ServerLevel serverLevel = (ServerLevel)level;
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof OmegaCoreBlockEntity)) return super.playerWillDestroy(level, pos, state, player);
        OmegaCoreBlockEntity core = (OmegaCoreBlockEntity)be;
        if (!core.isAssembled()) return super.playerWillDestroy(level, pos, state, player);
        if (core.isDisassembling()) return super.playerWillDestroy(level, pos, state, player);
        OmegaMultiblockPattern.disassembleWithBreak((ServerLevel)serverLevel, (BlockPos)pos, (OmegaCoreBlockEntity)core, (BlockPos)pos);
        return super.playerWillDestroy(level, pos, state, player);
    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.is(newState.getBlock())) return;
        if (!level.isClientSide && level instanceof ServerLevel) {
            OmegaCoreBlockEntity core;
            ServerLevel serverLevel = (ServerLevel)level;
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof OmegaCoreBlockEntity && (core = (OmegaCoreBlockEntity)be).isAssembled() && core.isStructureAbsorbed() && !core.isDisassembling()) {
                OmegaMultiblockPattern.disassemble((ServerLevel)serverLevel, (BlockPos)pos, (OmegaCoreBlockEntity)core);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.level.BlockGetter
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
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.ExtraTortik.ricks_stuff.block.entity.PhoenixCapsuleBlockEntity
 *  org.ExtraTortik.ricks_stuff.init.ModBlockEntities
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 *  org.ExtraTortik.ricks_stuff.item.DnaSampleFlaskItem
 */
package org.ExtraTortik.ricks_stuff.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.ExtraTortik.ricks_stuff.block.entity.PhoenixCapsuleBlockEntity;
import org.ExtraTortik.ricks_stuff.init.ModBlockEntities;
import org.ExtraTortik.ricks_stuff.init.ModItems;
import org.ExtraTortik.ricks_stuff.item.DnaSampleFlaskItem;
import org.jetbrains.annotations.Nullable;

public class PhoenixCapsuleBlock
extends BaseEntityBlock {
    public static final MapCodec<PhoenixCapsuleBlock> CODEC = PhoenixCapsuleBlock.simpleCodec(PhoenixCapsuleBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty BROKEN = BooleanProperty.create((String)"broken");
    private static final VoxelShape SHAPE = Block.box((double)0.0, (double)0.0, (double)0.0, (double)16.0, (double)32.0, (double)16.0);
    private static final VoxelShape SHAPE_BROKEN = Block.box((double)0.0, (double)0.0, (double)0.0, (double)16.0, (double)6.0, (double)16.0);

    public PhoenixCapsuleBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)BROKEN, (Comparable)Boolean.valueOf(false)));
    }

    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return (Boolean)state.getValue((Property)BROKEN) != false ? SHAPE_BROKEN : SHAPE;
    }

    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return (Boolean)state.getValue((Property)BROKEN) != false ? SHAPE_BROKEN : SHAPE;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState)((BlockState)this.defaultBlockState().setValue((Property)FACING, (Comparable)context.getHorizontalDirection().getOpposite())).setValue((Property)BROKEN, (Comparable)Boolean.valueOf(false));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, BROKEN});
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.sidedSuccess((boolean)level.isClientSide);
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof PhoenixCapsuleBlockEntity)) return InteractionResult.sidedSuccess((boolean)level.isClientSide);
        PhoenixCapsuleBlockEntity capsule = (PhoenixCapsuleBlockEntity)be;
        if (!(player instanceof ServerPlayer)) return InteractionResult.sidedSuccess((boolean)level.isClientSide);
        ServerPlayer serverPlayer = (ServerPlayer)player;
        ItemStack held = player.getMainHandItem();
        if (!(capsule.isBroken() || capsule.isPrinting() || capsule.isReady())) {
            ItemStack inSlot;
            if (held.is((Item)ModItems.DNA_SAMPLE_FLASK.get()) && DnaSampleFlaskItem.hasProfile((ItemStack)held) && (inSlot = capsule.getItemHandler().getStackInSlot(4)).isEmpty()) {
                ItemStack newStack = held.copy();
                newStack.setCount(1);
                capsule.getItemHandler().setStackInSlot(4, newStack);
                if (!player.isCreative()) {
                    held.shrink(1);
                }
                level.playSound(null, (double)pos.getX() + 0.5, (double)pos.getY() + 1.0, (double)pos.getZ() + 0.5, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
                ItemStack currentMeat = capsule.getItemHandler().getStackInSlot(0);
                if (currentMeat.getCount() < 32) return InteractionResult.CONSUME;
                capsule.startPrinting(serverPlayer);
                return InteractionResult.CONSUME;
            }
            if (PhoenixCapsuleBlockEntity.isRawMeat((ItemStack)held)) {
                inSlot = capsule.getItemHandler().getStackInSlot(0);
                int needed = 32 - inSlot.getCount();
                if (needed > 0 && !held.isEmpty()) {
                    int toAdd = Math.min(needed, held.getCount());
                    if (inSlot.isEmpty()) {
                        ItemStack newStack = held.copy();
                        newStack.setCount(toAdd);
                        capsule.getItemHandler().setStackInSlot(0, newStack);
                    } else if (ItemStack.isSameItemSameComponents((ItemStack)inSlot, (ItemStack)held)) {
                        inSlot.grow(toAdd);
                    }
                    if (!player.isCreative()) {
                        held.shrink(toAdd);
                    }
                }
                ItemStack currentMeat = capsule.getItemHandler().getStackInSlot(0);
                ItemStack dnaStack = capsule.getItemHandler().getStackInSlot(4);
                if (currentMeat.getCount() < 32) return InteractionResult.CONSUME;
                if (dnaStack.isEmpty()) return InteractionResult.CONSUME;
                if (!dnaStack.is((Item)ModItems.DNA_SAMPLE_FLASK.get())) return InteractionResult.CONSUME;
                if (!DnaSampleFlaskItem.hasProfile((ItemStack)dnaStack)) return InteractionResult.CONSUME;
                capsule.startPrinting(serverPlayer);
                return InteractionResult.CONSUME;
            }
        }
        serverPlayer.openMenu((MenuProvider)capsule, pos);
        return InteractionResult.sidedSuccess((boolean)level.isClientSide);
    }

    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMovedByPiston) {
        BlockEntity be;
        if (!state.is(newState.getBlock()) && (be = level.getBlockEntity(pos)) instanceof PhoenixCapsuleBlockEntity) {
            PhoenixCapsuleBlockEntity capsule = (PhoenixCapsuleBlockEntity)be;
            capsule.unregisterFromPhoenixManager();
            capsule.drops();
        }
        super.onRemove(state, level, pos, newState, isMovedByPiston);
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PhoenixCapsuleBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return PhoenixCapsuleBlock.createTickerHelper(type, (BlockEntityType)((BlockEntityType)ModBlockEntities.PHOENIX_CAPSULE_BE.get()), (lvl, pos, st, be) -> be.tick(lvl, pos, st));
    }

    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof PhoenixCapsuleBlockEntity)) return 0;
        PhoenixCapsuleBlockEntity capsule = (PhoenixCapsuleBlockEntity)be;
        if (capsule.isBroken()) {
            return 0;
        }
        if (capsule.isReady()) {
            return 15;
        }
        if (!capsule.isPrinting()) return 0;
        int total = capsule.getTotalPrintTicks();
        if (total <= 0) {
            return 1;
        }
        int progress = capsule.getPrintProgress();
        return 1 + (int)((float)progress / (float)total * 13.0f);
    }
}

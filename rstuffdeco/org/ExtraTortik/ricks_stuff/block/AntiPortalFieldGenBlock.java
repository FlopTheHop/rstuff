/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
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
 *  org.ExtraTortik.ricks_stuff.block.entity.AntiPortalFieldGenBlockEntity
 *  org.ExtraTortik.ricks_stuff.init.ModBlockEntities
 */
package org.ExtraTortik.ricks_stuff.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
import org.ExtraTortik.ricks_stuff.block.entity.AntiPortalFieldGenBlockEntity;
import org.ExtraTortik.ricks_stuff.init.ModBlockEntities;
import org.jetbrains.annotations.Nullable;

public class AntiPortalFieldGenBlock
extends BaseEntityBlock {
    public static final MapCodec<AntiPortalFieldGenBlock> CODEC = AntiPortalFieldGenBlock.simpleCodec(AntiPortalFieldGenBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty ACTIVE = BooleanProperty.create((String)"active");
    private static final VoxelShape SHAPE = Block.box((double)0.0, (double)0.0, (double)0.0, (double)16.0, (double)31.2, (double)16.0);

    public AntiPortalFieldGenBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)ACTIVE, (Comparable)Boolean.valueOf(false)));
    }

    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState)((BlockState)this.defaultBlockState().setValue((Property)FACING, (Comparable)context.getHorizontalDirection().getOpposite())).setValue((Property)ACTIVE, (Comparable)Boolean.valueOf(false));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, ACTIVE});
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (level.isClientSide) return;
        if (!(placer instanceof Player)) return;
        Player player = (Player)placer;
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof AntiPortalFieldGenBlockEntity)) return;
        AntiPortalFieldGenBlockEntity generator = (AntiPortalFieldGenBlockEntity)be;
        generator.setOwner(player);
        generator.updateActiveState();
    }

    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.sidedSuccess((boolean)level.isClientSide);
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof AntiPortalFieldGenBlockEntity)) return InteractionResult.sidedSuccess((boolean)level.isClientSide);
        AntiPortalFieldGenBlockEntity generator = (AntiPortalFieldGenBlockEntity)be;
        if (!(player instanceof ServerPlayer)) return InteractionResult.sidedSuccess((boolean)level.isClientSide);
        ServerPlayer serverPlayer = (ServerPlayer)player;
        serverPlayer.openMenu((MenuProvider)generator, pos);
        return InteractionResult.sidedSuccess((boolean)level.isClientSide);
    }

    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        if (level.isClientSide) return;
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof AntiPortalFieldGenBlockEntity)) return;
        AntiPortalFieldGenBlockEntity generator = (AntiPortalFieldGenBlockEntity)be;
        generator.updateActiveState();
    }

    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMovedByPiston) {
        if (state.is(newState.getBlock())) return;
        super.onRemove(state, level, pos, newState, isMovedByPiston);
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AntiPortalFieldGenBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return AntiPortalFieldGenBlock.createTickerHelper(type, (BlockEntityType)((BlockEntityType)ModBlockEntities.ANTI_PORTAL_FIELD_GEN_BE.get()), AntiPortalFieldGenBlockEntity::tick);
    }
}

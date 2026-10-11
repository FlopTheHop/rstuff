/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.particles.BlockParticleOption
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientGamePacketListener
 *  net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.world.Container
 *  net.minecraft.world.Containers
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.SimpleContainer
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.ContainerData
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.neoforged.neoforge.items.IItemHandler
 *  net.neoforged.neoforge.items.ItemStackHandler
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.block.PhoenixCapsuleBlock
 *  org.ExtraTortik.ricks_stuff.client.gui.PhoenixCapsuleMenu
 *  org.ExtraTortik.ricks_stuff.entity.omega.MultiverseErasureSavedData
 *  org.ExtraTortik.ricks_stuff.init.ModBlockEntities
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 *  org.ExtraTortik.ricks_stuff.init.ModParticles
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.DnaSampleFlaskItem
 *  org.ExtraTortik.ricks_stuff.phoenix.PhoenixManager
 */
package org.ExtraTortik.ricks_stuff.block.entity;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.block.PhoenixCapsuleBlock;
import org.ExtraTortik.ricks_stuff.client.gui.PhoenixCapsuleMenu;
import org.ExtraTortik.ricks_stuff.entity.omega.MultiverseErasureSavedData;
import org.ExtraTortik.ricks_stuff.init.ModBlockEntities;
import org.ExtraTortik.ricks_stuff.init.ModItems;
import org.ExtraTortik.ricks_stuff.init.ModParticles;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.DnaSampleFlaskItem;
import org.ExtraTortik.ricks_stuff.phoenix.PhoenixManager;
import org.jetbrains.annotations.Nullable;

public class PhoenixCapsuleBlockEntity
extends BlockEntity
implements MenuProvider {
    public static final int REQUIRED_MEAT = 32;
    public static final int SLOT_MEAT = 0;
    public static final int SLOT_GLASS = 1;
    public static final int SLOT_WATER = 2;
    public static final int SLOT_CATALYST = 3;
    public static final int SLOT_DNA = 4;
    public static final int SLOT_HELMET = 5;
    public static final int SLOT_CHEST = 6;
    public static final int SLOT_LEGS = 7;
    public static final int SLOT_FEET = 8;
    public static final int SLOT_MAINHAND = 9;
    public static final int SLOT_OFFHAND = 10;
    public static final int CONTAINER_SIZE = 11;
    public static final int CATALYST_NONE = 0;
    public static final int CATALYST_QUANTUM = 1;
    public static final int CATALYST_GOLDEN_APPLE = 2;
    public static final int CATALYST_ENCHANTED_APPLE = 3;
    public static final int CATALYST_DARK_MATTER = 4;
    private int catalystType = 0;
    private boolean isCorrupted = false;
    private final ItemStackHandler itemHandler = new /* Unavailable Anonymous Inner Class!! */;
    private final IItemHandler topSideHandler = new /* Unavailable Anonymous Inner Class!! */;
    private final IItemHandler bottomHandler = new /* Unavailable Anonymous Inner Class!! */;
    protected final ContainerData dataAccess = new /* Unavailable Anonymous Inner Class!! */;
    private GameProfile ownerProfile = null;
    private UUID ownerUUID = null;
    private String ownerName = "";
    private int printProgress = 0;
    private float clientPrintProgress = 0.0f;
    private float prevClientPrintProgress = 0.0f;
    private boolean isPrinting = false;
    private boolean isReady = false;
    private boolean isBroken = false;

    public int getTotalPrintTicks() {
        int base = Math.max(20, Config.phoenixPrintTimeSeconds * 20);
        if (this.catalystType == 1) {
            base = Math.max(20, base / 3);
        }
        if (!this.isCorrupted) return base;
        base = (int)((float)base * 1.5f);
        return base;
    }

    public static int getBaseTotalPrintTicks() {
        return Math.max(20, Config.phoenixPrintTimeSeconds * 20);
    }

    public static boolean isCatalyst(ItemStack stack) {
        if (!stack.isEmpty()) return stack.is((Item)ModItems.QUANTUM_ISOTOPE.get()) || stack.is(Items.GOLDEN_APPLE) || stack.is(Items.ENCHANTED_GOLDEN_APPLE) || stack.is((Item)ModItems.DARK_MATTER_DUST.get());
        return false;
    }

    public static int getCatalystTypeFromStack(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        }
        if (stack.is((Item)ModItems.QUANTUM_ISOTOPE.get())) {
            return 1;
        }
        if (stack.is(Items.GOLDEN_APPLE)) {
            return 2;
        }
        if (stack.is(Items.ENCHANTED_GOLDEN_APPLE)) {
            return 3;
        }
        if (!stack.is((Item)ModItems.DARK_MATTER_DUST.get())) return 0;
        return 4;
    }

    public IItemHandler getItemHandlerForSide(@Nullable Direction side) {
        if (side == null) {
            return this.itemHandler;
        }
        if (side != Direction.DOWN) return this.topSideHandler;
        return this.bottomHandler;
    }

    public PhoenixCapsuleBlockEntity(BlockPos pos, BlockState blockState) {
        super((BlockEntityType)ModBlockEntities.PHOENIX_CAPSULE_BE.get(), pos, blockState);
        this.isBroken = (Boolean)blockState.getValue((Property)PhoenixCapsuleBlock.BROKEN);
    }

    public Component getDisplayName() {
        return Component.translatable((String)"container.ricks_stuff.phoenix_capsule");
    }

    @Nullable
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new PhoenixCapsuleMenu(id, inventory, (BlockEntity)this, this.dataAccess);
    }

    public static boolean isRawMeat(ItemStack stack) {
        if (!stack.isEmpty()) return stack.is(Items.BEEF) || stack.is(Items.PORKCHOP) || stack.is(Items.CHICKEN) || stack.is(Items.MUTTON) || stack.is(Items.RABBIT) || stack.is(Items.ROTTEN_FLESH);
        return false;
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        int totalTicks;
        if (level.isClientSide) {
            int totalTicks2 = this.getTotalPrintTicks();
            this.prevClientPrintProgress = this.clientPrintProgress;
            if (this.isPrinting) {
                if (this.clientPrintProgress < (float)totalTicks2) {
                    this.clientPrintProgress += 1.0f;
                }
                if (Math.abs(this.clientPrintProgress - (float)this.printProgress) > 15.0f) {
                    this.clientPrintProgress = this.printProgress;
                }
            } else if (this.isReady) {
                this.clientPrintProgress = totalTicks2;
                this.prevClientPrintProgress = totalTicks2;
            } else {
                this.clientPrintProgress = 0.0f;
                this.prevClientPrintProgress = 0.0f;
            }
            if (this.isPrinting) {
                double px;
                double r;
                double angle;
                float progress = this.getSmoothPrintProgress(0.0f);
                float yScan = 0.32f + progress * 1.3f;
                if (level.random.nextFloat() < 0.65f) {
                    angle = level.random.nextDouble() * Math.PI * 2.0;
                    r = 0.06 + level.random.nextDouble() * 0.22;
                    px = (double)pos.getX() + 0.5 + Math.cos(angle) * r;
                    double py = (double)((float)pos.getY() + yScan) + (level.random.nextDouble() - 0.5) * 0.02;
                    double pz = (double)pos.getZ() + 0.5 + Math.sin(angle) * r;
                    level.addParticle((ParticleOptions)ModParticles.GREEN_DIMENSIONAL_SPARK.get(), px, py, pz, Math.cos(angle) * 0.01, 0.005 + level.random.nextDouble() * 0.01, Math.sin(angle) * 0.01);
                }
                if (!(yScan > 0.45f)) return;
                if (!(level.random.nextFloat() < 0.2f)) return;
                angle = level.random.nextDouble() * Math.PI * 2.0;
                r = level.random.nextDouble() * 0.24;
                px = (double)pos.getX() + 0.5 + Math.cos(angle) * r;
                double maxSpawnY = Math.max(0.35f, yScan - 0.4f);
                double py = (double)pos.getY() + 0.32 + level.random.nextDouble() * (maxSpawnY - 0.32);
                double pz = (double)pos.getZ() + 0.5 + Math.sin(angle) * r;
                level.addParticle((ParticleOptions)ModParticles.ACID_BUBBLE.get(), px, py, pz, (level.random.nextDouble() - 0.5) * 0.003, 0.008 + level.random.nextDouble() * 0.01, (level.random.nextDouble() - 0.5) * 0.003);
            } else {
                double pz;
                double py;
                double px;
                double r;
                double angle;
                if (!this.isReady) return;
                if (level.random.nextFloat() < 0.3f) {
                    angle = level.random.nextDouble() * Math.PI * 2.0;
                    r = level.random.nextDouble() * 0.25;
                    px = (double)pos.getX() + 0.5 + Math.cos(angle) * r;
                    py = (double)pos.getY() + 0.32 + level.random.nextDouble() * 0.55;
                    pz = (double)pos.getZ() + 0.5 + Math.sin(angle) * r;
                    level.addParticle((ParticleOptions)ModParticles.ACID_BUBBLE.get(), px, py, pz, (level.random.nextDouble() - 0.5) * 0.003, 0.008 + level.random.nextDouble() * 0.01, (level.random.nextDouble() - 0.5) * 0.003);
                }
                if (!(level.random.nextFloat() < 0.12f)) return;
                angle = level.random.nextDouble() * Math.PI * 2.0;
                r = level.random.nextDouble() * 0.25;
                px = (double)pos.getX() + 0.5 + Math.cos(angle) * r;
                py = (double)pos.getY() + 0.35 + level.random.nextDouble() * 1.1;
                pz = (double)pos.getZ() + 0.5 + Math.sin(angle) * r;
                level.addParticle((ParticleOptions)ModParticles.GREEN_DIMENSIONAL_SPARK.get(), px, py, pz, 0.0, 0.008, 0.0);
            }
            return;
        }
        if (this.ownerUUID != null && level.getServer() != null && MultiverseErasureSavedData.get((MinecraftServer)level.getServer()).isErased(this.ownerUUID) && (this.isReady || this.isPrinting)) {
            this.shatter();
            return;
        }
        if (!this.isPrinting) return;
        ++this.printProgress;
        if (this.printProgress % 25 == 0) {
            level.playSound(null, (double)pos.getX() + 0.5, (double)pos.getY() + 1.0, (double)pos.getZ() + 0.5, (SoundEvent)ModSounds.CLONE_PRINT_LOOP.get(), SoundSource.BLOCKS, 0.35f, 0.95f + level.random.nextFloat() * 0.1f);
        }
        if (this.printProgress >= (totalTicks = this.getTotalPrintTicks())) {
            this.isPrinting = false;
            this.isReady = true;
            this.printProgress = totalTicks;
            level.playSound(null, (double)pos.getX() + 0.5, (double)pos.getY() + 1.0, (double)pos.getZ() + 0.5, (SoundEvent)ModSounds.CLONE_READY.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
            if (this.ownerUUID != null && level.getServer() != null) {
                PhoenixManager.registerCapsule((MinecraftServer)level.getServer(), (UUID)this.ownerUUID, (BlockPos)pos, (ResourceKey)level.dimension());
            }
            this.setChanged();
            level.sendBlockUpdated(pos, state, state, 3);
            level.updateNeighbourForOutputSignal(pos, state.getBlock());
        } else {
            if (this.printProgress % 10 != 0) return;
            this.setChanged();
            level.sendBlockUpdated(pos, state, state, 3);
            level.updateNeighbourForOutputSignal(pos, state.getBlock());
        }
    }

    public boolean startPrinting(ServerPlayer player) {
        if (this.isBroken) return false;
        if (this.isPrinting) return false;
        if (this.isReady) {
            return false;
        }
        ItemStack dnaStack = this.itemHandler.getStackInSlot(4);
        if (dnaStack.isEmpty()) return false;
        if (!dnaStack.is((Item)ModItems.DNA_SAMPLE_FLASK.get())) return false;
        if (!DnaSampleFlaskItem.hasProfile((ItemStack)dnaStack)) {
            return false;
        }
        GameProfile targetProfile = DnaSampleFlaskItem.getProfile((ItemStack)dnaStack);
        if (targetProfile == null) return false;
        if (targetProfile.getId() == null) {
            return false;
        }
        UUID targetUUID = targetProfile.getId();
        if (player.getServer() != null && MultiverseErasureSavedData.get((MinecraftServer)player.getServer()).isErased(targetUUID)) {
            return false;
        }
        ItemStack meatStack = this.itemHandler.getStackInSlot(0);
        if (!PhoenixCapsuleBlockEntity.isRawMeat(meatStack)) return false;
        if (meatStack.getCount() < 32) {
            return false;
        }
        this.isCorrupted = meatStack.is(Items.ROTTEN_FLESH);
        ItemStack catalystStack = this.itemHandler.getStackInSlot(3);
        this.catalystType = PhoenixCapsuleBlockEntity.getCatalystTypeFromStack(catalystStack);
        if (!catalystStack.isEmpty()) {
            this.itemHandler.extractItem(3, 1, false);
        }
        if (meatStack.getCount() > 32 && this.level != null) {
            int excess = meatStack.getCount() - 32;
            ItemStack excessStack = meatStack.copy();
            excessStack.setCount(excess);
            Containers.dropItemStack((Level)this.level, (double)((double)this.worldPosition.getX() + 0.5), (double)((double)this.worldPosition.getY() + 1.2), (double)((double)this.worldPosition.getZ() + 0.5), (ItemStack)excessStack);
        }
        this.itemHandler.extractItem(0, 32, false);
        this.itemHandler.extractItem(4, 1, false);
        this.ownerProfile = targetProfile;
        this.ownerUUID = targetUUID;
        this.ownerName = targetProfile.getName() != null ? targetProfile.getName() : "";
        this.isPrinting = true;
        this.isReady = false;
        this.printProgress = 0;
        if (this.level == null) return true;
        this.level.playSound(null, (double)this.worldPosition.getX() + 0.5, (double)this.worldPosition.getY() + 1.0, (double)this.worldPosition.getZ() + 0.5, (SoundEvent)ModSounds.CLONE_PRINT_LOOP.get(), SoundSource.BLOCKS, 0.35f, 1.0f);
        this.setChanged();
        this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        this.level.updateNeighbourForOutputSignal(this.worldPosition, this.getBlockState().getBlock());
        return true;
    }

    public boolean repairCapsule(ServerPlayer player) {
        if (!this.isBroken) {
            return false;
        }
        ItemStack glassStack = this.itemHandler.getStackInSlot(1);
        ItemStack waterStack = this.itemHandler.getStackInSlot(2);
        if (glassStack.getCount() < 2) return false;
        if (!glassStack.is(Items.GLASS)) {
            return false;
        }
        if (waterStack.getCount() < 1) return false;
        if (!waterStack.is(Items.WATER_BUCKET)) {
            return false;
        }
        this.itemHandler.extractItem(1, 2, false);
        this.itemHandler.extractItem(2, 1, false);
        ItemStack emptyBuckets = new ItemStack((ItemLike)Items.BUCKET, 1);
        if (this.itemHandler.getStackInSlot(2).isEmpty()) {
            this.itemHandler.setStackInSlot(2, emptyBuckets);
        } else if (!player.getInventory().add(emptyBuckets)) {
            player.drop(emptyBuckets, false);
        }
        this.isBroken = false;
        this.isReady = false;
        this.isPrinting = false;
        this.printProgress = 0;
        if (this.level == null) return true;
        BlockState newState = (BlockState)this.getBlockState().setValue((Property)PhoenixCapsuleBlock.BROKEN, (Comparable)Boolean.valueOf(false));
        this.level.setBlock(this.worldPosition, newState, 3);
        this.level.playSound(null, (double)this.worldPosition.getX() + 0.5, (double)this.worldPosition.getY() + 1.0, (double)this.worldPosition.getZ() + 0.5, (SoundEvent)ModSounds.CAPSULE_REPAIR.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
        this.setChanged();
        this.level.sendBlockUpdated(this.worldPosition, newState, newState, 3);
        this.level.updateNeighbourForOutputSignal(this.worldPosition, newState.getBlock());
        return true;
    }

    public void triggerResurrection(ServerPlayer player) {
        if (this.level == null) return;
        if (this.level.isClientSide) return;
        Level level = this.level;
        if (!(level instanceof ServerLevel)) return;
        ServerLevel serverLevel = (ServerLevel)level;
        this.isReady = false;
        this.isBroken = true;
        this.isPrinting = false;
        this.printProgress = 0;
        BlockState newState = (BlockState)this.getBlockState().setValue((Property)PhoenixCapsuleBlock.BROKEN, (Comparable)Boolean.valueOf(true));
        this.level.setBlock(this.worldPosition, newState, 3);
        serverLevel.playSound(null, (double)this.worldPosition.getX() + 0.5, (double)this.worldPosition.getY() + 1.0, (double)this.worldPosition.getZ() + 0.5, (SoundEvent)ModSounds.CAPSULE_SHATTER.get(), SoundSource.BLOCKS, 2.0f, 0.95f + serverLevel.random.nextFloat() * 0.1f);
        double cx = (double)this.worldPosition.getX() + 0.5;
        double cy = (double)this.worldPosition.getY() + 1.2;
        double cz = (double)this.worldPosition.getZ() + 0.5;
        serverLevel.sendParticles((ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK, Blocks.GLASS.defaultBlockState()), cx, cy, cz, 60, 0.4, 0.6, 0.4, 0.35);
        serverLevel.sendParticles((ParticleOptions)ParticleTypes.SPLASH, cx, cy, cz, 50, 0.4, 0.5, 0.4, 0.4);
        serverLevel.sendParticles((ParticleOptions)ParticleTypes.DRIPPING_WATER, cx, cy, cz, 30, 0.3, 0.4, 0.3, 0.1);
        this.setChanged();
        this.level.sendBlockUpdated(this.worldPosition, newState, newState, 3);
        this.level.updateNeighbourForOutputSignal(this.worldPosition, newState.getBlock());
    }

    public void shatter() {
        if (this.level == null) return;
        if (this.level.isClientSide) {
            return;
        }
        this.isReady = false;
        this.isBroken = true;
        this.isPrinting = false;
        this.printProgress = 0;
        this.unregisterFromPhoenixManager();
        BlockState newState = (BlockState)this.getBlockState().setValue((Property)PhoenixCapsuleBlock.BROKEN, (Comparable)Boolean.valueOf(true));
        this.level.setBlock(this.worldPosition, newState, 3);
        Level level = this.level;
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            serverLevel.playSound(null, (double)this.worldPosition.getX() + 0.5, (double)this.worldPosition.getY() + 1.0, (double)this.worldPosition.getZ() + 0.5, (SoundEvent)ModSounds.CAPSULE_SHATTER.get(), SoundSource.BLOCKS, 2.0f, 0.95f + serverLevel.random.nextFloat() * 0.1f);
            double cx = (double)this.worldPosition.getX() + 0.5;
            double cy = (double)this.worldPosition.getY() + 1.2;
            double cz = (double)this.worldPosition.getZ() + 0.5;
            serverLevel.sendParticles((ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK, Blocks.GLASS.defaultBlockState()), cx, cy, cz, 60, 0.4, 0.6, 0.4, 0.35);
            serverLevel.sendParticles((ParticleOptions)ParticleTypes.SPLASH, cx, cy, cz, 50, 0.4, 0.5, 0.4, 0.4);
        }
        this.setChanged();
        this.level.sendBlockUpdated(this.worldPosition, newState, newState, 3);
        this.level.updateNeighbourForOutputSignal(this.worldPosition, newState.getBlock());
    }

    public void unregisterFromPhoenixManager() {
        if (this.level == null) return;
        if (this.level.isClientSide) return;
        if (this.ownerUUID == null) return;
        if (this.level.getServer() == null) return;
        PhoenixManager.unregisterCapsule((MinecraftServer)this.level.getServer(), (UUID)this.ownerUUID, (BlockPos)this.worldPosition, (ResourceKey)this.level.dimension());
    }

    public void drops() {
        if (this.level == null) return;
        if (this.level.isClientSide) return;
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        int i = 0;
        while (true) {
            if (i >= this.itemHandler.getSlots()) {
                Containers.dropContents((Level)this.level, (BlockPos)this.worldPosition, (Container)inventory);
                return;
            }
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
            ++i;
        }
    }

    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }

    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public GameProfile getOwnerProfile() {
        return this.ownerProfile;
    }

    public int getPrintProgress() {
        return this.printProgress;
    }

    public boolean isPrinting() {
        return this.isPrinting;
    }

    public boolean isReady() {
        return this.isReady;
    }

    public boolean isBroken() {
        return this.isBroken;
    }

    public int getCatalystType() {
        return this.catalystType;
    }

    public void setCatalystType(int type) {
        this.catalystType = type;
    }

    public boolean isCorrupted() {
        return this.isCorrupted;
    }

    public void setCorrupted(boolean corrupted) {
        this.isCorrupted = corrupted;
    }

    public float getSmoothPrintProgress(float partialTick) {
        if (!this.isPrinting && !this.isReady) {
            return 0.0f;
        }
        if (this.isReady) {
            return 1.0f;
        }
        int totalTicks = this.getTotalPrintTicks();
        float lerped = Mth.lerp((float)partialTick, (float)this.prevClientPrintProgress, (float)this.clientPrintProgress);
        return totalTicks > 0 ? Mth.clamp((float)(lerped / (float)totalTicks), (float)0.0f, (float)1.0f) : 1.0f;
    }

    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", (Tag)this.itemHandler.serializeNBT(registries));
        tag.putInt("printProgress", this.printProgress);
        tag.putBoolean("isPrinting", this.isPrinting);
        tag.putBoolean("isReady", this.isReady);
        tag.putBoolean("isBroken", this.isBroken);
        tag.putInt("catalystType", this.catalystType);
        tag.putBoolean("isCorrupted", this.isCorrupted);
        if (this.ownerProfile != null) {
            CompoundTag profileTag = new CompoundTag();
            profileTag.putUUID("Id", this.ownerProfile.getId());
            profileTag.putString("Name", this.ownerProfile.getName());
            tag.put("ownerProfile", (Tag)profileTag);
        } else if (this.ownerUUID != null) {
            tag.putUUID("ownerUUID", this.ownerUUID);
        }
        if (this.ownerName == null) return;
        tag.putString("ownerName", this.ownerName);
    }

    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("inventory")) {
            this.itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        }
        this.printProgress = tag.getInt("printProgress");
        this.isPrinting = tag.getBoolean("isPrinting");
        this.isReady = tag.getBoolean("isReady");
        this.isBroken = tag.getBoolean("isBroken");
        this.catalystType = tag.getInt("catalystType");
        this.isCorrupted = tag.getBoolean("isCorrupted");
        if (tag.contains("ownerProfile", 10)) {
            CompoundTag pTag = tag.getCompound("ownerProfile");
            if (!pTag.hasUUID("Id")) return;
            this.ownerUUID = pTag.getUUID("Id");
            this.ownerName = pTag.getString("Name");
            this.ownerProfile = new GameProfile(this.ownerUUID, this.ownerName != null ? this.ownerName : "");
        } else {
            this.ownerUUID = tag.hasUUID("ownerUUID") ? tag.getUUID("ownerUUID") : null;
            this.ownerName = tag.getString("ownerName");
            this.ownerProfile = this.ownerUUID != null ? new GameProfile(this.ownerUUID, this.ownerName != null ? this.ownerName : "") : null;
        }
    }

    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        this.saveAdditional(tag, registries);
        return tag;
    }

    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create((BlockEntity)this);
    }

    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        super.handleUpdateTag(tag, registries);
        this.loadAdditional(tag, registries);
        if (!this.isPrinting) return;
        if (this.clientPrintProgress != 0.0f) return;
        if (this.printProgress <= 0) return;
        this.clientPrintProgress = this.printProgress;
        this.prevClientPrintProgress = this.printProgress;
    }

    static /* synthetic */ Level access$000(PhoenixCapsuleBlockEntity x0) {
        return x0.level;
    }

    static /* synthetic */ Level access$100(PhoenixCapsuleBlockEntity x0) {
        return x0.level;
    }

    static /* synthetic */ BlockPos access$200(PhoenixCapsuleBlockEntity x0) {
        return x0.worldPosition;
    }

    static /* synthetic */ BlockPos access$300(PhoenixCapsuleBlockEntity x0) {
        return x0.worldPosition;
    }

    static /* synthetic */ BlockPos access$400(PhoenixCapsuleBlockEntity x0) {
        return x0.worldPosition;
    }

    static /* synthetic */ Level access$500(PhoenixCapsuleBlockEntity x0) {
        return x0.level;
    }

    static /* synthetic */ BlockPos access$600(PhoenixCapsuleBlockEntity x0) {
        return x0.worldPosition;
    }

    static /* synthetic */ Level access$700(PhoenixCapsuleBlockEntity x0) {
        return x0.level;
    }

    static /* synthetic */ BlockPos access$800(PhoenixCapsuleBlockEntity x0) {
        return x0.worldPosition;
    }

    static /* synthetic */ Level access$900(PhoenixCapsuleBlockEntity x0) {
        return x0.level;
    }
}

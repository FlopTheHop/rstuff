/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientGamePacketListener
 *  net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.AABB
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaMultiblockPattern
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaMultiblockPattern$BlockRequirement
 *  org.ExtraTortik.ricks_stuff.init.ModBlockEntities
 */
package org.ExtraTortik.ricks_stuff.block.entity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaMultiblockPattern;
import org.ExtraTortik.ricks_stuff.init.ModBlockEntities;
import org.jetbrains.annotations.Nullable;

public class OmegaCoreBlockEntity
extends BlockEntity {
    private UUID boundDeviceUUID;
    private boolean isAssembled = false;
    private boolean isStructureAbsorbed = false;
    private CompoundTag absorbedBlocksTag = new CompoundTag();
    private int tickCounter = 0;
    private boolean isDisassembling = false;

    public OmegaCoreBlockEntity(BlockPos pos, BlockState state) {
        super((BlockEntityType)ModBlockEntities.OMEGA_CORE_BE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, OmegaCoreBlockEntity be) {
        if (level.isClientSide) {
            return;
        }
        ++be.tickCounter;
        if (be.tickCounter % 20 != 0) return;
        if (!(level instanceof ServerLevel)) return;
        ServerLevel serverLevel = (ServerLevel)level;
        if (!be.isAssembled) {
            if (!OmegaMultiblockPattern.getMissingRequirements((Level)serverLevel, (BlockPos)pos).isEmpty()) return;
            if (!OmegaMultiblockPattern.checkClearance((Level)serverLevel, (BlockPos)pos).clear()) return;
            OmegaMultiblockPattern.assemble((ServerLevel)serverLevel, (BlockPos)pos, (OmegaCoreBlockEntity)be);
        } else {
            OmegaDeviceEntity device;
            Entity entity;
            boolean deviceAlive = false;
            if (be.boundDeviceUUID != null && (entity = serverLevel.getEntity(be.boundDeviceUUID)) instanceof OmegaDeviceEntity && (device = (OmegaDeviceEntity)entity).isAlive()) {
                deviceAlive = true;
            }
            if (deviceAlive) {
                if (OmegaMultiblockPattern.checkActiveStructure((Level)serverLevel, (BlockPos)pos, (OmegaCoreBlockEntity)be)) return;
            }
            OmegaMultiblockPattern.disassemble((ServerLevel)serverLevel, (BlockPos)pos, (OmegaCoreBlockEntity)be);
        }
    }

    public List<OmegaMultiblockPattern.BlockRequirement> getMissingRequirements() {
        if (this.level != null) return OmegaMultiblockPattern.getMissingRequirements((Level)this.level, (BlockPos)this.worldPosition);
        return Collections.emptyList();
    }

    public AABB getRenderBoundingBox() {
        return new AABB(this.getBlockPos()).inflate(6.0, 12.0, 6.0);
    }

    @Nullable
    public UUID getBoundDeviceUUID() {
        return this.boundDeviceUUID;
    }

    public void setBoundDeviceUUID(@Nullable UUID boundDeviceUUID) {
        this.boundDeviceUUID = boundDeviceUUID;
        this.setChanged();
        if (this.level == null) return;
        if (this.level.isClientSide) return;
        this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
    }

    public boolean isAssembled() {
        return this.isAssembled;
    }

    public void setAssembled(boolean assembled) {
        this.isAssembled = assembled;
        this.setChanged();
        if (this.level == null) return;
        if (this.level.isClientSide) return;
        this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
    }

    public boolean isStructureAbsorbed() {
        return this.isStructureAbsorbed;
    }

    public void setStructureAbsorbed(boolean structureAbsorbed) {
        this.isStructureAbsorbed = structureAbsorbed;
        this.setChanged();
        if (this.level == null) return;
        if (this.level.isClientSide) return;
        this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
    }

    public CompoundTag getAbsorbedBlocksTag() {
        return this.absorbedBlocksTag;
    }

    public void setAbsorbedBlocksTag(CompoundTag absorbedBlocksTag) {
        this.absorbedBlocksTag = absorbedBlocksTag != null ? absorbedBlocksTag : new CompoundTag();
        this.setChanged();
    }

    public boolean isDisassembling() {
        return this.isDisassembling;
    }

    public void setDisassembling(boolean disassembling) {
        this.isDisassembling = disassembling;
    }

    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("IsAssembled", this.isAssembled);
        if (this.boundDeviceUUID != null) {
            tag.putUUID("BoundDeviceUUID", this.boundDeviceUUID);
        }
        tag.putBoolean("IsStructureAbsorbed", this.isStructureAbsorbed);
        if (this.absorbedBlocksTag == null) return;
        if (this.absorbedBlocksTag.isEmpty()) return;
        tag.put("AbsorbedBlocks", (Tag)this.absorbedBlocksTag);
    }

    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("IsAssembled")) {
            this.isAssembled = tag.getBoolean("IsAssembled");
        }
        this.boundDeviceUUID = tag.hasUUID("BoundDeviceUUID") ? tag.getUUID("BoundDeviceUUID") : null;
        this.isStructureAbsorbed = tag.contains("IsStructureAbsorbed") ? tag.getBoolean("IsStructureAbsorbed") : false;
        this.absorbedBlocksTag = tag.contains("AbsorbedBlocks") ? tag.getCompound("AbsorbedBlocks") : new CompoundTag();
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
    }
}

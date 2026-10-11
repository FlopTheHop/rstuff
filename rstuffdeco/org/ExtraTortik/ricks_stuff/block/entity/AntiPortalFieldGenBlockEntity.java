/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.Vec3i
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientGamePacketListener
 *  net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.ContainerData
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.items.IItemHandler
 *  net.neoforged.neoforge.items.ItemStackHandler
 *  org.ExtraTortik.ricks_stuff.block.AntiPortalFieldGenBlock
 *  org.ExtraTortik.ricks_stuff.client.gui.AntiPortalMenu
 *  org.ExtraTortik.ricks_stuff.entity.PortalEntity
 *  org.ExtraTortik.ricks_stuff.init.ModBlockEntities
 *  org.ExtraTortik.ricks_stuff.init.ModParticles
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.portal.AntiPortalFieldManager
 */
package org.ExtraTortik.ricks_stuff.block.entity;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.ExtraTortik.ricks_stuff.block.AntiPortalFieldGenBlock;
import org.ExtraTortik.ricks_stuff.client.gui.AntiPortalMenu;
import org.ExtraTortik.ricks_stuff.entity.PortalEntity;
import org.ExtraTortik.ricks_stuff.init.ModBlockEntities;
import org.ExtraTortik.ricks_stuff.init.ModParticles;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.portal.AntiPortalFieldManager;
import org.jetbrains.annotations.Nullable;

public class AntiPortalFieldGenBlockEntity
extends BlockEntity
implements MenuProvider {
    public static final int MIN_RADIUS = 8;
    public static final int MAX_RADIUS = 64;
    public static final int DEFAULT_RADIUS = 24;
    public static final int REDSTONE_IGNORE = 0;
    public static final int REDSTONE_REQUIRED = 1;
    public static final int REDSTONE_INVERTED = 2;
    private int radius = 24;
    private int redstoneMode = 0;
    private boolean enabled = true;
    private boolean active = false;
    private UUID ownerUUID;
    private String ownerName;
    public float clientExpansion = 0.0f;
    public float prevClientExpansion = 0.0f;
    public float clientAnimatedRadius = 24.0f;
    public float prevClientAnimatedRadius = 24.0f;
    public int clientTickCount = 0;
    protected final ContainerData dataAccess = new /* Unavailable Anonymous Inner Class!! */;

    public AntiPortalFieldGenBlockEntity(BlockPos pos, BlockState state) {
        super((BlockEntityType)ModBlockEntities.ANTI_PORTAL_FIELD_GEN_BE.get(), pos, state);
    }

    public void onLoad() {
        super.onLoad();
        if (this.level == null) return;
        this.updateActiveState();
        if (!this.active) return;
        AntiPortalFieldManager.register((AntiPortalFieldGenBlockEntity)this);
    }

    public void setRemoved() {
        AntiPortalFieldManager.unregister((AntiPortalFieldGenBlockEntity)this);
        super.setRemoved();
    }

    public void onChunkUnloaded() {
        AntiPortalFieldManager.unregister((AntiPortalFieldGenBlockEntity)this);
        super.onChunkUnloaded();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AntiPortalFieldGenBlockEntity be) {
        if (level.isClientSide) {
            be.clientTick(level, pos);
            return;
        }
        be.serverTick((ServerLevel)level, pos, state);
    }

    private void serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        boolean wasActive = this.active;
        this.updateActiveState();
        if (this.active != wasActive) {
            if (this.active) {
                AntiPortalFieldManager.register((AntiPortalFieldGenBlockEntity)this);
                level.playSound(null, pos, (SoundEvent)ModSounds.ANTI_PORTAL_ACTIVATE.get(), SoundSource.BLOCKS, 0.9f, 1.0f);
            } else {
                AntiPortalFieldManager.unregister((AntiPortalFieldGenBlockEntity)this);
                level.playSound(null, pos, (SoundEvent)ModSounds.ANTI_PORTAL_DEACTIVATE.get(), SoundSource.BLOCKS, 0.8f, 1.0f);
            }
            if (state.hasProperty((Property)AntiPortalFieldGenBlock.ACTIVE) && (Boolean)state.getValue((Property)AntiPortalFieldGenBlock.ACTIVE) != this.active) {
                level.setBlock(pos, (BlockState)state.setValue((Property)AntiPortalFieldGenBlock.ACTIVE, (Comparable)Boolean.valueOf(this.active)), 3);
            }
            this.setChanged();
            level.sendBlockUpdated(pos, state, state, 3);
        }
        if (!this.active) return;
        if (level.getGameTime() % 10L != 0L) return;
        Vec3 center = Vec3.atCenterOf((Vec3i)pos);
        double r = this.radius;
        AABB searchBox = new AABB(center.x - r, center.y - r, center.z - r, center.x + r, center.y + r, center.z + r);
        List portals = level.getEntitiesOfClass(PortalEntity.class, searchBox, p -> p.isAlive() && center.distanceToSqr(p.position()) <= r * r);
        Iterator iterator = portals.iterator();
        while (iterator.hasNext()) {
            PortalEntity portal = (PortalEntity)iterator.next();
            portal.discard();
            level.playSound(null, portal.getX(), portal.getY(), portal.getZ(), (SoundEvent)ModSounds.ANTI_PORTAL_DEFLECT.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
        }
    }

    private void clientTick(Level level, BlockPos pos) {
        ++this.clientTickCount;
        this.prevClientExpansion = this.clientExpansion;
        this.prevClientAnimatedRadius = this.clientAnimatedRadius;
        float targetExpansion = this.active ? 1.0f : 0.0f;
        float expansionSpeed = this.active ? 0.04f : 0.08f;
        this.clientExpansion += (targetExpansion - this.clientExpansion) * expansionSpeed;
        if (Math.abs(targetExpansion - this.clientExpansion) < 0.005f) {
            this.clientExpansion = targetExpansion;
        }
        this.clientAnimatedRadius += ((float)this.radius - this.clientAnimatedRadius) * 0.1f;
        if (!this.active) return;
        if (!(this.clientExpansion > 0.05f)) return;
        double emitterX = (double)pos.getX() + 0.5;
        double emitterY = (double)pos.getY() + 1.85;
        double emitterZ = (double)pos.getZ() + 0.5;
        if (!(level.random.nextFloat() < 0.35f)) return;
        double vx = (level.random.nextDouble() - 0.5) * 0.08;
        double vy = (level.random.nextDouble() - 0.5) * 0.08;
        double vz = (level.random.nextDouble() - 0.5) * 0.08;
        level.addParticle((ParticleOptions)ModParticles.BLUE_DIMENSIONAL_SPARK.get(), emitterX + (level.random.nextDouble() - 0.5) * 0.2, emitterY + (level.random.nextDouble() - 0.5) * 0.2, emitterZ + (level.random.nextDouble() - 0.5) * 0.2, vx, vy, vz);
    }

    public void updateActiveState() {
        if (this.level == null) {
            return;
        }
        this.active = this.enabled;
    }

    public int getRadius() {
        return this.radius;
    }

    public void setRadius(int radius) {
        this.radius = Math.max(8, Math.min(64, radius));
        this.setChanged();
        if (this.level == null) return;
        if (this.level.isClientSide) return;
        this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
    }

    public int getRedstoneMode() {
        return this.redstoneMode;
    }

    public void setRedstoneMode(int redstoneMode) {
        this.redstoneMode = redstoneMode >= 0 && redstoneMode <= 2 ? redstoneMode : 0;
        this.updateActiveState();
        this.setChanged();
        if (this.level == null) return;
        if (this.level.isClientSide) return;
        this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        this.updateActiveState();
        this.setChanged();
        if (this.level == null) return;
        if (this.level.isClientSide) return;
        this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
    }

    public boolean isActive() {
        return this.active;
    }

    public void setOwner(Player player) {
        if (player == null) return;
        this.ownerUUID = player.getUUID();
        this.ownerName = player.getScoreboardName();
        this.setChanged();
    }

    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    public String getOwnerName() {
        return this.ownerName != null ? this.ownerName : "Unknown";
    }

    public ItemStackHandler getItemHandler() {
        return null;
    }

    public IItemHandler getItemHandlerForSide(@Nullable Direction side) {
        return null;
    }

    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("Radius", this.radius);
        tag.putInt("RedstoneMode", this.redstoneMode);
        tag.putBoolean("Enabled", this.enabled);
        tag.putBoolean("Active", this.active);
        if (this.ownerUUID != null) {
            tag.putUUID("OwnerUUID", this.ownerUUID);
        }
        if (this.ownerName == null) return;
        tag.putString("OwnerName", this.ownerName);
    }

    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Radius")) {
            this.radius = tag.getInt("Radius");
        }
        if (tag.contains("RedstoneMode")) {
            this.redstoneMode = tag.getInt("RedstoneMode");
        }
        if (tag.contains("Enabled")) {
            this.enabled = tag.getBoolean("Enabled");
        }
        if (tag.contains("Active")) {
            this.active = tag.getBoolean("Active");
        }
        if (tag.hasUUID("OwnerUUID")) {
            this.ownerUUID = tag.getUUID("OwnerUUID");
        }
        if (!tag.contains("OwnerName")) return;
        this.ownerName = tag.getString("OwnerName");
    }

    public AABB getRenderBoundingBox() {
        return new AABB(this.worldPosition).inflate(70.0);
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

    public Component getDisplayName() {
        return Component.translatable((String)"block.ricks_stuff.anti_portal_field_gen");
    }

    @Nullable
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new AntiPortalMenu(id, inventory, (BlockEntity)this, this.dataAccess);
    }
}

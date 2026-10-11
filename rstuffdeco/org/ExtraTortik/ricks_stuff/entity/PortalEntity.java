/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientGamePacketListener
 *  net.minecraft.network.protocol.game.ClientboundAddEntityPacket
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerEntity
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$RemovalReason
 *  net.minecraft.world.entity.EntityDimensions
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Pose
 *  net.minecraft.world.entity.animal.WaterAnimal
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.item.PrimedTnt
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.entity.vehicle.AbstractMinecart
 *  net.minecraft.world.entity.vehicle.Boat
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.entity.PortalEntity$ClientAudioProxy
 *  org.ExtraTortik.ricks_stuff.init.ModParticles
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.portal.BlenderDimensionHelper
 *  org.ExtraTortik.ricks_stuff.portal.PortalManager
 *  org.ExtraTortik.ricks_stuff.portal.PortalSavedData
 *  org.ExtraTortik.ricks_stuff.portal.PortalTeleportEngine
 */
package org.ExtraTortik.ricks_stuff.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.entity.PortalEntity;
import org.ExtraTortik.ricks_stuff.init.ModParticles;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.portal.BlenderDimensionHelper;
import org.ExtraTortik.ricks_stuff.portal.PortalManager;
import org.ExtraTortik.ricks_stuff.portal.PortalSavedData;
import org.ExtraTortik.ricks_stuff.portal.PortalTeleportEngine;

/*
 * Exception performing whole class analysis ignored.
 */
public class PortalEntity
extends Entity {
    public static final int ORIENTATION_WALL = 0;
    public static final int ORIENTATION_CEILING = 1;
    public static final int ORIENTATION_FLOOR = 2;
    public static final int COLOR_GREEN = 0;
    public static final int COLOR_BLUE = 1;
    public static final int COLOR_YELLOW = 2;
    private static final EntityDataAccessor<Integer> DATA_ORIENTATION = SynchedEntityData.defineId(PortalEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ROTATION = SynchedEntityData.defineId(PortalEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_SCALE = SynchedEntityData.defineId(PortalEntity.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(PortalEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_IS_LINKED = SynchedEntityData.defineId(PortalEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<UUID>> DATA_DUALITY_UUID = SynchedEntityData.defineId(PortalEntity.class, (EntityDataSerializer)EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Boolean> DATA_IS_ROOT = SynchedEntityData.defineId(PortalEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_OWNER_GUN_ID = SynchedEntityData.defineId(PortalEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_AUTO_CLOSE = SynchedEntityData.defineId(PortalEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> DATA_GUN_MODE = SynchedEntityData.defineId(PortalEntity.class, (EntityDataSerializer)EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> DATA_CLOSING = SynchedEntityData.defineId(PortalEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_PASS_TICKS = SynchedEntityData.defineId(PortalEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_BLENDER_GRINDING = SynchedEntityData.defineId(PortalEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_HAZARD_SHIELD = SynchedEntityData.defineId(PortalEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private CompoundTag customLocationData = new CompoundTag();
    private boolean safePlacement = true;
    private final Map<UUID, Integer> teleportCooldowns = new ConcurrentHashMap<UUID, Integer>();
    private int closingTimer = -1;
    private int clientClosingTimer = -1;
    private int clientPassTimer = 0;
    public static final int PASS_ANIMATION_DURATION = 18;
    private int blenderEruptionTicks = 0;
    private int itemGrindTicks = 0;
    private final List<ItemStack> itemGrindQueue = new ArrayList<ItemStack>();

    public PortalEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_ORIENTATION, (Object)0);
        builder.define(DATA_ROTATION, (Object)0);
        builder.define(DATA_SCALE, (Object)Float.valueOf(2.0f));
        builder.define(DATA_COLOR, (Object)0);
        builder.define(DATA_IS_LINKED, (Object)false);
        builder.define(DATA_DUALITY_UUID, Optional.empty());
        builder.define(DATA_IS_ROOT, (Object)false);
        builder.define(DATA_OWNER_GUN_ID, (Object)0);
        builder.define(DATA_AUTO_CLOSE, (Object)false);
        builder.define(DATA_GUN_MODE, (Object)"FIFO");
        builder.define(DATA_CLOSING, (Object)false);
        builder.define(DATA_PASS_TICKS, (Object)0);
        builder.define(DATA_BLENDER_GRINDING, (Object)false);
        builder.define(DATA_HAZARD_SHIELD, (Object)false);
    }

    public int getOrientation() {
        return (Integer)this.entityData.get(DATA_ORIENTATION);
    }

    public void setOrientation(int orientation) {
        this.entityData.set(DATA_ORIENTATION, (Object)orientation);
        this.refreshDimensions();
    }

    public int getRotation() {
        return (Integer)this.entityData.get(DATA_ROTATION);
    }

    public void setRotation(int rotation) {
        this.entityData.set(DATA_ROTATION, (Object)rotation);
    }

    public float getScale() {
        return ((Float)this.entityData.get(DATA_SCALE)).floatValue();
    }

    public void setScale(float scale) {
        this.entityData.set(DATA_SCALE, (Object)Float.valueOf(Math.max(0.1f, scale)));
        this.refreshDimensions();
    }

    public int getColor() {
        return (Integer)this.entityData.get(DATA_COLOR);
    }

    public void setColor(int color) {
        this.entityData.set(DATA_COLOR, (Object)color);
    }

    public boolean isLinked() {
        return (Boolean)this.entityData.get(DATA_IS_LINKED);
    }

    public void setLinked(boolean linked) {
        this.entityData.set(DATA_IS_LINKED, (Object)linked);
    }

    public Optional<UUID> getDualityPortalId() {
        return (Optional)this.entityData.get(DATA_DUALITY_UUID);
    }

    public void setDualityPortalId(UUID uuid) {
        this.entityData.set(DATA_DUALITY_UUID, Optional.ofNullable(uuid));
    }

    public void setDualityPortalId(Optional<UUID> uuid) {
        this.entityData.set(DATA_DUALITY_UUID, uuid != null ? uuid : Optional.empty());
    }

    public boolean isRoot() {
        return (Boolean)this.entityData.get(DATA_IS_ROOT);
    }

    public void setRoot(boolean root) {
        this.entityData.set(DATA_IS_ROOT, (Object)root);
    }

    public int getOwnerGunId() {
        return (Integer)this.entityData.get(DATA_OWNER_GUN_ID);
    }

    public void setOwnerGunId(int ownerGunId) {
        this.entityData.set(DATA_OWNER_GUN_ID, (Object)ownerGunId);
    }

    public boolean isAutoClose() {
        return (Boolean)this.entityData.get(DATA_AUTO_CLOSE);
    }

    public void setAutoClose(boolean autoClose) {
        this.entityData.set(DATA_AUTO_CLOSE, (Object)autoClose);
    }

    public String getGunMode() {
        return (String)this.entityData.get(DATA_GUN_MODE);
    }

    public void setGunMode(String gunMode) {
        this.entityData.set(DATA_GUN_MODE, (Object)gunMode);
    }

    public CompoundTag getCustomLocationData() {
        return this.customLocationData;
    }

    public int getPassTicks() {
        return (Integer)this.entityData.get(DATA_PASS_TICKS);
    }

    public int getClientPassTimer() {
        return this.clientPassTimer;
    }

    public int getClientClosingTimer() {
        return this.clientClosingTimer;
    }

    public void triggerPassAnimation() {
        this.entityData.set(DATA_PASS_TICKS, (Object)18);
        this.clientPassTimer = 18;
    }

    public void setCustomLocationData(CompoundTag data) {
        this.customLocationData = data != null ? data.copy() : new CompoundTag();
    }

    public boolean hasHazardShield() {
        return (Boolean)this.entityData.get(DATA_HAZARD_SHIELD);
    }

    public void setHazardShield(boolean hazardShield) {
        this.entityData.set(DATA_HAZARD_SHIELD, (Object)hazardShield);
    }

    public boolean isSafePlacement() {
        return this.safePlacement;
    }

    public void setSafePlacement(boolean safePlacement) {
        this.safePlacement = safePlacement;
    }

    public void setTeleportCooldown(UUID entityUUID, int ticks) {
        this.teleportCooldowns.put(entityUUID, ticks);
    }

    public boolean hasTeleportCooldown(UUID entityUUID) {
        Integer cd = this.teleportCooldowns.get(entityUUID);
        return cd != null && cd > 0;
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        int val;
        super.onSyncedDataUpdated(key);
        if (DATA_SCALE.equals(key) || DATA_ORIENTATION.equals(key)) {
            this.refreshDimensions();
        }
        if (DATA_PASS_TICKS.equals(key) && (val = ((Integer)this.entityData.get(DATA_PASS_TICKS)).intValue()) > 0) {
            this.clientPassTimer = val;
        }
        if (!DATA_CLOSING.equals(key)) return;
        if ((Boolean)this.entityData.get(DATA_CLOSING) == false) return;
        if (this.clientClosingTimer >= 0) return;
        this.clientClosingTimer = 9;
    }

    protected AABB makeBoundingBox() {
        float scale = this.getScale();
        int orientation = this.getOrientation();
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        if (orientation == 0) {
            float rad = (float)Math.toRadians(this.getYRot());
            double nx = Math.abs(-Math.sin(rad));
            double nz = Math.abs(Math.cos(rad));
            double tx = Math.abs(Math.cos(rad));
            double tz = Math.abs(Math.sin(rad));
            double hw = 0.7 * (double)scale;
            double hh = 1.05 * (double)scale;
            double ht = 0.35 * (double)scale;
            double hx = tx * hw + nx * ht;
            double hz = tz * hw + nz * ht;
            return new AABB(x - hx, y - hh, z - hz, x + hx, y + hh, z + hz);
        }
        double r = 0.65 * (double)scale;
        double ht = 0.3 * (double)scale;
        if (orientation != 2) return new AABB(x - r, y - ht, z - r, x + r, y + 0.1, z + r);
        return new AABB(x - r, y - 0.1, z - r, x + r, y + ht, z + r);
    }

    public EntityDimensions getDimensions(Pose pose) {
        float scale = this.getScale();
        int orientation = this.getOrientation();
        if (orientation != 0) return EntityDimensions.scalable((float)(1.9f * scale), (float)(0.2f * scale));
        return EntityDimensions.scalable((float)(1.9f * scale), (float)(2.1f * scale));
    }

    public boolean isPickable() {
        return !this.isRemoved();
    }

    public boolean isPushable() {
        return false;
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    public void remove(Entity.RemovalReason reason) {
        if (this.level().isClientSide()) {
            ClientAudioProxy.stopPortalGrindSound((UUID)this.getUUID());
        }
        super.remove(reason);
    }

    public void tick() {
        super.tick();
        PortalTeleportEngine.handleAmbientEffects((PortalEntity)this);
        if (this.level().isClientSide) {
            if (this.clientPassTimer > 0) {
                --this.clientPassTimer;
            }
            if (this.clientClosingTimer > 0) {
                --this.clientClosingTimer;
                if (this.clientClosingTimer == 1) {
                    this.spawnCollapseParticles();
                }
            }
            this.spawnClientParticles();
            ClientAudioProxy.handlePortalGrindSound((PortalEntity)this);
        } else {
            int pass = (Integer)this.entityData.get(DATA_PASS_TICKS);
            if (pass > 0) {
                this.entityData.set(DATA_PASS_TICKS, (Object)(pass - 1));
            }
            this.serverTick();
        }
    }

    public boolean isBlenderDimensionPortal() {
        if (this.customLocationData == null) return false;
        if (!this.customLocationData.contains("Dimension")) return false;
        return BlenderDimensionHelper.isBlenderDimension((String)this.customLocationData.getString("Dimension"));
    }

    public void triggerBlenderEruption(int ticks) {
        this.blenderEruptionTicks = Math.max(this.blenderEruptionTicks, ticks);
    }

    public boolean isBlenderGrinding() {
        return (Boolean)this.entityData.get(DATA_BLENDER_GRINDING);
    }

    public void addItemGrind(ItemStack stack) {
        if (stack == null) return;
        if (stack.isEmpty()) {
            return;
        }
        this.itemGrindQueue.add(stack.copy());
        int count = stack.getCount();
        int duration = Math.min(200, 16 + count * 2);
        this.itemGrindTicks = Math.min(240, this.itemGrindTicks + duration);
        this.entityData.set(DATA_BLENDER_GRINDING, (Object)true);
    }

    private void spawnClientParticles() {
        int color = this.getColor();
        float scale = this.getScale();
        int orientation = this.getOrientation();
        if (this.tickCount == 1) {
            SimpleParticleType rayParticle = switch (color) {
                case 1 -> (SimpleParticleType)ModParticles.BLUE_VORTEX_BEAM.get();
                case 2 -> (SimpleParticleType)ModParticles.YELLOW_VORTEX_BEAM.get();
                default -> (SimpleParticleType)ModParticles.GREEN_VORTEX_BEAM.get();
            };
            this.level().addParticle((ParticleOptions)rayParticle, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
        }
        SimpleParticleType dotParticle = switch (color) {
            case 1 -> (SimpleParticleType)ModParticles.BLUE_DIMENSIONAL_SPARK.get();
            case 2 -> (SimpleParticleType)ModParticles.YELLOW_DIMENSIONAL_SPARK.get();
            default -> (SimpleParticleType)ModParticles.GREEN_DIMENSIONAL_SPARK.get();
        };
        int i = 0;
        while (i < 2) {
            double angle = this.random.nextDouble() * Math.PI * 2.0;
            double rx = Math.cos(angle) * 0.85 * (double)scale;
            double ry = Math.sin(angle) * (orientation == 0 ? 0.95 * (double)scale : 0.85 * (double)scale);
            double px = this.getX();
            double py = this.getY();
            double pz = this.getZ();
            if (orientation == 0) {
                float rad = (float)Math.toRadians(this.getYRot());
                px += rx * Math.cos(rad);
                pz += rx * Math.sin(rad);
                py += ry;
            } else {
                px += rx;
                pz += ry;
            }
            this.level().addParticle((ParticleOptions)dotParticle, px, py, pz, (this.random.nextDouble() - 0.5) * 0.02, (this.random.nextDouble() - 0.5) * 0.02, (this.random.nextDouble() - 0.5) * 0.02);
            ++i;
        }
    }

    private void spawnCollapseParticles() {
        int i;
        int color = this.getColor();
        int orientation = this.getOrientation();
        float yaw = this.getYRot();
        SimpleParticleType dropletParticle = switch (color) {
            case 1 -> (SimpleParticleType)ModParticles.BLUE_FLUID_DROPLET.get();
            case 2 -> (SimpleParticleType)ModParticles.YELLOW_FLUID_DROPLET.get();
            default -> (SimpleParticleType)ModParticles.GREEN_FLUID_DROPLET.get();
        };
        SimpleParticleType sparkParticle = switch (color) {
            case 1 -> (SimpleParticleType)ModParticles.BLUE_DIMENSIONAL_SPARK.get();
            case 2 -> (SimpleParticleType)ModParticles.YELLOW_DIMENSIONAL_SPARK.get();
            default -> (SimpleParticleType)ModParticles.GREEN_DIMENSIONAL_SPARK.get();
        };
        Vec3 normal = PortalTeleportEngine.getPortalNormal((int)orientation, (float)yaw);
        double cx = this.getX();
        double cy = this.getY();
        double cz = this.getZ();
        int dropletCount = 6 + this.random.nextInt(5);
        for (i = 0; i < dropletCount; ++i) {
            double speed = 0.14 + this.random.nextDouble() * 0.18;
            double spreadX = (this.random.nextDouble() - 0.5) * 0.08;
            double spreadY = (this.random.nextDouble() - 0.5) * 0.08;
            double spreadZ = (this.random.nextDouble() - 0.5) * 0.08;
            double vx = normal.x * speed + spreadX;
            double vy = normal.y * speed + spreadY + (orientation == 0 ? 0.04 : 0.0);
            double vz = normal.z * speed + spreadZ;
            this.level().addParticle((ParticleOptions)dropletParticle, cx + normal.x * 0.05, cy + normal.y * 0.05, cz + normal.z * 0.05, vx, vy, vz);
        }
        for (i = 0; i < 3; ++i) {
            this.level().addParticle((ParticleOptions)ModParticles.FLUID_EVAPORATION.get(), cx + (this.random.nextDouble() - 0.5) * 0.1, cy + (this.random.nextDouble() - 0.5) * 0.1, cz + (this.random.nextDouble() - 0.5) * 0.1, normal.x * 0.05 + (this.random.nextDouble() - 0.5) * 0.03, normal.y * 0.05 + 0.03, normal.z * 0.05 + (this.random.nextDouble() - 0.5) * 0.03);
        }
        i = 0;
        while (i < 2) {
            this.level().addParticle((ParticleOptions)sparkParticle, cx, cy, cz, normal.x * 0.1 + (this.random.nextDouble() - 0.5) * 0.05, normal.y * 0.1 + (this.random.nextDouble() - 0.5) * 0.05, normal.z * 0.1 + (this.random.nextDouble() - 0.5) * 0.05);
            ++i;
        }
    }

    public Vec3 getNormalVector() {
        return PortalTeleportEngine.getPortalNormal((int)this.getOrientation(), (float)this.getYRot());
    }

    private void serverTick() {
        ServerLevel serverLevel;
        PortalEntity destPortal;
        Level level;
        Optional<UUID> dualityId;
        Level data;
        ServerLevel sl;
        Level level2;
        if (this.tickCount <= 2 && (level2 = this.level()) instanceof ServerLevel && (sl = (ServerLevel)level2).getServer() != null && (data = PortalSavedData.get((MinecraftServer)sl.getServer())).isTombstoned(this.getUUID())) {
            data.removeTombstone(this.getUUID());
            this.discard();
            return;
        }
        if (this.isBlenderDimensionPortal() && (data = this.level()) instanceof ServerLevel) {
            ServerLevel serverLevel2 = (ServerLevel)data;
            if (this.blenderEruptionTicks > 0) {
                --this.blenderEruptionTicks;
                if (this.blenderEruptionTicks % 3 == 0) {
                    BlenderDimensionHelper.spawnBloodEruption((ServerLevel)serverLevel2, (PortalEntity)this, (float)0.35f, (RandomSource)serverLevel2.getRandom());
                }
            }
            if (this.itemGrindTicks > 0) {
                --this.itemGrindTicks;
                if (!this.itemGrindQueue.isEmpty()) {
                    ItemStack current = this.itemGrindQueue.get(0);
                    if (this.itemGrindTicks % 2 == 0) {
                        BlenderDimensionHelper.spawnItemShredEruption((ServerLevel)serverLevel2, (PortalEntity)this, (ItemStack)current, (RandomSource)serverLevel2.getRandom());
                    }
                    if (this.itemGrindTicks % 10 == 0) {
                        serverLevel2.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 0.6f, 0.85f + serverLevel2.getRandom().nextFloat() * 0.3f);
                    }
                    if (this.itemGrindTicks % 40 == 0 && this.itemGrindTicks > 0) {
                        serverLevel2.playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.BLENDER_GRIND.get(), SoundSource.PLAYERS, 0.85f, 0.95f + serverLevel2.getRandom().nextFloat() * 0.1f);
                    }
                }
                if (this.itemGrindTicks == 0) {
                    this.itemGrindQueue.clear();
                    this.entityData.set(DATA_BLENDER_GRINDING, (Object)false);
                }
            }
        }
        if (!this.teleportCooldowns.isEmpty()) {
            for (UUID uuid : this.teleportCooldowns.keySet()) {
                this.teleportCooldowns.computeIfPresent(uuid, (k, cd) -> {
                    int updated = cd - 1;
                    return updated <= 0 ? null : Integer.valueOf(updated);
                });
            }
        }
        if (Config.portalMaxLifetimeTicks > 0 && this.tickCount >= Config.portalMaxLifetimeTicks && !((Boolean)this.entityData.get(DATA_CLOSING)).booleanValue()) {
            this.startClosing();
            return;
        }
        if (((Boolean)this.entityData.get(DATA_CLOSING)).booleanValue()) {
            if (--this.closingTimer > 0) return;
            this.discard();
            return;
        }
        if (this.tickCount % 5 == 0 && this.isLinked() && (dualityId = this.getDualityPortalId()).isPresent() && (level = this.level()) instanceof ServerLevel && (destPortal = this.findPortalByUUID(serverLevel = (ServerLevel)level, dualityId.get())) != null && !destPortal.isRemoved()) {
            PortalTeleportEngine.handleFluidFlow((PortalEntity)this, (PortalEntity)destPortal);
        }
        double rxz = Math.max(3.5, 2.5 * (double)this.getScale());
        double ry = Math.max(3.5, 2.5 * (double)this.getScale());
        AABB box = new AABB(this.getX() - rxz, this.getY() - ry, this.getZ() - rxz, this.getX() + rxz, this.getY() + ry, this.getZ() + rxz);
        List<Entity> colliding = this.level().getEntities((Entity)this, box, this::isEntityIntersectingPortal);
        if (this.getScale() <= 0.5f) {
            colliding = this.filterSmallEntities(colliding);
        }
        Iterator iterator = colliding.iterator();
        while (iterator.hasNext()) {
            Entity entity = (Entity)iterator.next();
            entity.resetFallDistance();
            this.handleEntityTeleport(entity);
        }
    }

    private boolean isEntityIntersectingPortal(Entity entity) {
        double currYDist;
        boolean inY;
        if (entity == null) return false;
        if (entity.isRemoved()) return false;
        if (entity instanceof PortalEntity) return false;
        if (this.hasTeleportCooldown(entity.getUUID())) {
            return false;
        }
        if (!Config.allowEntityTeleportation && !(entity instanceof Player)) {
            return false;
        }
        AABB eBox = entity.getBoundingBox();
        float scale = this.getScale();
        int orientation = this.getOrientation();
        if (orientation == 0) {
            double denom;
            double currNormDist;
            double prevCz;
            boolean inY2;
            float rad = (float)Math.toRadians(this.getYRot());
            double nx = -Math.sin(rad);
            double nz = Math.cos(rad);
            double tx = Math.cos(rad);
            double tz = Math.sin(rad);
            double cx = (eBox.minX + eBox.maxX) * 0.5 - this.getX();
            double cz = (eBox.minZ + eBox.maxZ) * 0.5 - this.getZ();
            double normalDist = Math.abs(cx * nx + cz * nz);
            double tangentDist = Math.abs(cx * tx + cz * tz);
            double halfWidth = 0.65 * (double)scale;
            double halfHeight = 1.05 * (double)scale;
            double entityRadius = Math.max(eBox.getXsize(), eBox.getZsize()) * 0.5;
            double triggerThickness = entityRadius + 0.12 * (double)scale;
            boolean bl = inY2 = eBox.minY <= this.getY() + halfHeight && eBox.maxY >= this.getY() - halfHeight;
            if (inY2 && normalDist <= triggerThickness && tangentDist <= halfWidth && eBox.intersects(this.getBoundingBox())) {
                return true;
            }
            double prevCx = entity.xo - this.getX();
            double prevNormDist = prevCx * nx + (prevCz = entity.zo - this.getZ()) * nz;
            if (!(prevNormDist * (currNormDist = cx * nx + cz * nz) <= 0.0)) {
                if (!(Math.abs(currNormDist) <= 0.35 * (double)scale)) return false;
            }
            double t = Math.abs(denom = prevNormDist - currNormDist) > 1.0E-4 ? Math.max(0.0, Math.min(1.0, prevNormDist / denom)) : 0.5;
            double crossX = entity.xo + (entity.getX() - entity.xo) * t - this.getX();
            double crossY = entity.yo + (entity.getY() - entity.yo) * t + (double)entity.getBbHeight() * 0.5 - this.getY();
            double crossZ = entity.zo + (entity.getZ() - entity.zo) * t - this.getZ();
            double crossTangent = Math.abs(crossX * tx + crossZ * tz);
            if (!(crossTangent <= halfWidth + entityRadius)) return false;
            if (!(Math.abs(crossY) <= halfHeight + (double)entity.getBbHeight() * 0.5)) return false;
            return true;
        }
        if (orientation == 2) {
            double currYDist2;
            boolean inY3;
            double cx = (eBox.minX + eBox.maxX) * 0.5 - this.getX();
            double cz = (eBox.minZ + eBox.maxZ) * 0.5 - this.getZ();
            double radiusSq = cx * cx + cz * cz;
            double maxRadius = 0.55 * (double)scale;
            boolean bl = inY3 = eBox.minY <= this.getY() + 0.35 * (double)scale && eBox.maxY >= this.getY() - 0.35 * (double)scale;
            if (inY3 && radiusSq <= maxRadius * maxRadius && eBox.intersects(this.getBoundingBox())) {
                return true;
            }
            double prevYDist = entity.yo - this.getY();
            if (!(prevYDist * (currYDist2 = entity.getY() - this.getY()) <= 0.0)) {
                if (!(Math.abs(currYDist2) <= 0.4 * (double)scale)) return false;
            }
            if (!(radiusSq <= maxRadius * maxRadius)) return false;
            return true;
        }
        double cx = (eBox.minX + eBox.maxX) * 0.5 - this.getX();
        double cz = (eBox.minZ + eBox.maxZ) * 0.5 - this.getZ();
        double radiusSq = cx * cx + cz * cz;
        double maxRadius = 0.55 * (double)scale;
        boolean bl = inY = eBox.maxY >= this.getY() - 0.35 * (double)scale && eBox.minY <= this.getY() + 0.35 * (double)scale;
        if (inY && radiusSq <= maxRadius * maxRadius && eBox.intersects(this.getBoundingBox())) {
            return true;
        }
        double prevYDist = entity.yo + (double)entity.getBbHeight() - this.getY();
        if (!(prevYDist * (currYDist = entity.getY() + (double)entity.getBbHeight() - this.getY()) <= 0.0)) {
            if (!(Math.abs(currYDist) <= 0.4 * (double)scale)) return false;
        }
        if (!(radiusSq <= maxRadius * maxRadius)) return false;
        return true;
    }

    private List<Entity> filterSmallEntities(List<Entity> entities) {
        ArrayList<Entity> filtered = new ArrayList<Entity>();
        Iterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext()) {
            LivingEntity living;
            Entity entity = iterator.next();
            if (!(entity instanceof ItemEntity) && !(entity instanceof Projectile) && !(entity instanceof Player) && !(entity instanceof AbstractMinecart) && !(entity instanceof Boat) && !(entity instanceof PrimedTnt) && !(entity instanceof WaterAnimal) && (!(entity instanceof LivingEntity) || !(living = (LivingEntity)entity).isBaby())) continue;
            filtered.add(entity);
        }
        return filtered;
    }

    public void handleEntityTeleport(Entity entity) {
        ServerLevel serverLevel;
        PortalEntity destPortal;
        Level level;
        if (entity.isPassenger()) return;
        if (entity.isVehicle()) {
            return;
        }
        if (this.isBlenderDimensionPortal()) {
            BlenderDimensionHelper.shredEntity((PortalEntity)this, (Entity)entity);
            return;
        }
        if (entity.isPassenger()) return;
        if (entity.isVehicle()) {
            return;
        }
        Optional<UUID> dualityId = this.getDualityPortalId();
        if (dualityId.isPresent() && (level = this.level()) instanceof ServerLevel && (destPortal = this.findPortalByUUID(serverLevel = (ServerLevel)level, dualityId.get())) != null && !destPortal.isRemoved()) {
            PortalTeleportEngine.teleportEntity((PortalEntity)this, (PortalEntity)destPortal, (Entity)entity);
            return;
        }
        if (this.customLocationData == null) return;
        if (this.customLocationData.isEmpty()) return;
        PortalTeleportEngine.teleportEntityToCustomLocation((PortalEntity)this, (CompoundTag)this.customLocationData, (Entity)entity);
    }

    private PortalEntity findPortalByUUID(ServerLevel serverLevel, UUID uuid) {
        return PortalManager.findPortal((Level)serverLevel, (UUID)uuid);
    }

    public Vec3 getForwardVector(int orientation, float yRot, int rot) {
        return PortalTeleportEngine.getForwardVector((int)orientation, (float)yRot, (int)rot);
    }

    public double getDetectionRadius() {
        boolean isWall;
        float scale = this.getScale();
        int orientation = this.getOrientation();
        boolean bl = isWall = orientation == 0;
        if (scale <= 0.5f) {
            return 0.8;
        }
        if (!(scale <= 1.0f)) return isWall ? 2.2 : 2.0;
        return isWall ? 1.2 : 1.0;
    }

    public void startClosing() {
        if (((Boolean)this.entityData.get(DATA_CLOSING)).booleanValue()) {
            return;
        }
        this.entityData.set(DATA_CLOSING, (Object)true);
        this.closingTimer = 9;
        this.clientClosingTimer = 9;
        if (!"CUSTOM".equalsIgnoreCase(this.getGunMode())) return;
        if (this.level().isClientSide) return;
        Level level = this.level();
        if (!(level instanceof ServerLevel)) return;
        ServerLevel serverLevel = (ServerLevel)level;
        this.getDualityPortalId().ifPresent(dualityId -> {
            PortalEntity linked = PortalManager.findPortal((Level)serverLevel, (UUID)dualityId);
            if (linked == null) return;
            if (linked.isRemoved()) return;
            if (linked.isClosing()) return;
            PortalManager.playPortalCloseSound((PortalEntity)linked);
            linked.startClosing();
        });
    }

    public boolean isClosing() {
        return (Boolean)this.entityData.get(DATA_CLOSING);
    }

    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Orientation", this.getOrientation());
        tag.putInt("PortalRotation", this.getRotation());
        tag.putFloat("PortalYaw", this.getYRot());
        tag.putFloat("PortalPitch", this.getXRot());
        tag.putFloat("Scale", this.getScale());
        tag.putInt("Color", this.getColor());
        tag.putBoolean("IsLinked", this.isLinked());
        if (this.getDualityPortalId().isPresent()) {
            tag.putUUID("DualityUUID", this.getDualityPortalId().get());
        }
        tag.putBoolean("IsRoot", this.isRoot());
        tag.putInt("OwnerGunId", this.getOwnerGunId());
        tag.putBoolean("AutoClose", this.isAutoClose());
        tag.putString("GunMode", this.getGunMode());
        tag.putBoolean("Closing", this.isClosing());
        tag.putBoolean("HazardShield", this.hasHazardShield());
        tag.putBoolean("SafePlacement", this.safePlacement);
        if (this.customLocationData == null) return;
        if (this.customLocationData.isEmpty()) return;
        tag.put("CustomLocation", (Tag)this.customLocationData);
    }

    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("Orientation")) {
            this.setOrientation(tag.getInt("Orientation"));
        }
        if (tag.contains("PortalRotation")) {
            this.setRotation(tag.getInt("PortalRotation"));
        } else if (tag.contains("Rotation", 3)) {
            this.setRotation(tag.getInt("Rotation"));
        }
        if (tag.contains("PortalYaw")) {
            float yaw = tag.getFloat("PortalYaw");
            this.setYRot(yaw);
            this.yRotO = yaw;
            this.setYHeadRot(yaw);
        }
        if (tag.contains("PortalPitch")) {
            float pitch = tag.getFloat("PortalPitch");
            this.setXRot(pitch);
            this.xRotO = pitch;
        }
        if (tag.contains("Scale")) {
            this.setScale(tag.getFloat("Scale"));
        }
        if (tag.contains("Color")) {
            this.setColor(tag.getInt("Color"));
        }
        if (tag.contains("IsLinked")) {
            this.setLinked(tag.getBoolean("IsLinked"));
        }
        if (tag.hasUUID("DualityUUID")) {
            this.setDualityPortalId(tag.getUUID("DualityUUID"));
        }
        if (tag.contains("IsRoot")) {
            this.setRoot(tag.getBoolean("IsRoot"));
        }
        if (tag.contains("OwnerGunId")) {
            this.setOwnerGunId(tag.getInt("OwnerGunId"));
        }
        if (tag.contains("AutoClose")) {
            this.setAutoClose(tag.getBoolean("AutoClose"));
        }
        if (tag.contains("GunMode")) {
            this.setGunMode(tag.getString("GunMode"));
        }
        if (tag.contains("CustomLocation")) {
            this.setCustomLocationData(tag.getCompound("CustomLocation"));
        }
        if (tag.contains("HazardShield")) {
            this.setHazardShield(tag.getBoolean("HazardShield"));
        }
        if (tag.contains("SafePlacement")) {
            this.setSafePlacement(tag.getBoolean("SafePlacement"));
        }
        if (!tag.contains("Closing")) return;
        if (!tag.getBoolean("Closing")) return;
        this.entityData.set(DATA_CLOSING, (Object)true);
        this.closingTimer = 1;
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket((Entity)this, serverEntity);
    }
}

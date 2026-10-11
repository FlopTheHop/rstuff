/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.BlockPos$MutableBlockPos
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
 *  net.minecraft.server.level.ServerEntity
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$RemovalReason
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.ThrowableProjectile
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.Vec3
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.entity.PortalEntity
 *  org.ExtraTortik.ricks_stuff.init.ModEntities
 *  org.ExtraTortik.ricks_stuff.init.ModParticles
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.portal.AntiPortalFieldManager
 *  org.ExtraTortik.ricks_stuff.portal.PortalManager
 */
package org.ExtraTortik.ricks_stuff.entity;

import net.minecraft.core.BlockPos;
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
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.entity.PortalEntity;
import org.ExtraTortik.ricks_stuff.init.ModEntities;
import org.ExtraTortik.ricks_stuff.init.ModParticles;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.portal.AntiPortalFieldManager;
import org.ExtraTortik.ricks_stuff.portal.PortalManager;

public class PortalProjectileEntity
extends ThrowableProjectile {
    public static final int COLOR_GREEN = 0;
    public static final int COLOR_BLUE = 1;
    public static final int COLOR_YELLOW = 2;
    private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(PortalProjectileEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_PORTAL_SCALE = SynchedEntityData.defineId(PortalProjectileEntity.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_GUN_ID = SynchedEntityData.defineId(PortalProjectileEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> DATA_GUN_MODE = SynchedEntityData.defineId(PortalProjectileEntity.class, (EntityDataSerializer)EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> DATA_AUTO_CLOSE = SynchedEntityData.defineId(PortalProjectileEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_AUTO_AIR_DETONATE = SynchedEntityData.defineId(PortalProjectileEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_AIR_DISTANCE = SynchedEntityData.defineId(PortalProjectileEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_HAZARD_SHIELD = SynchedEntityData.defineId(PortalProjectileEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_SAFE_PLACEMENT = SynchedEntityData.defineId(PortalProjectileEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private Vec3 spawnOrigin;
    private CompoundTag customLocation = new CompoundTag();

    public PortalProjectileEntity(EntityType<? extends PortalProjectileEntity> entityType, Level level) {
        super(entityType, level);
    }

    public PortalProjectileEntity(Level level, LivingEntity shooter) {
        super((EntityType)ModEntities.PORTAL_PROJECTILE.get(), shooter, level);
    }

    public PortalProjectileEntity(Level level, double x, double y, double z) {
        super((EntityType)ModEntities.PORTAL_PROJECTILE.get(), x, y, z, level);
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_COLOR, (Object)0);
        builder.define(DATA_PORTAL_SCALE, (Object)Float.valueOf(2.0f));
        builder.define(DATA_GUN_ID, (Object)0);
        builder.define(DATA_GUN_MODE, (Object)"FIFO");
        builder.define(DATA_AUTO_CLOSE, (Object)false);
        builder.define(DATA_AUTO_AIR_DETONATE, (Object)false);
        builder.define(DATA_AIR_DISTANCE, (Object)15);
        builder.define(DATA_HAZARD_SHIELD, (Object)false);
        builder.define(DATA_SAFE_PLACEMENT, (Object)true);
    }

    public int getColor() {
        return (Integer)this.entityData.get(DATA_COLOR);
    }

    public void setColor(int color) {
        this.entityData.set(DATA_COLOR, (Object)color);
    }

    public float getPortalScale() {
        return ((Float)this.entityData.get(DATA_PORTAL_SCALE)).floatValue();
    }

    public void setPortalScale(float scale) {
        this.entityData.set(DATA_PORTAL_SCALE, (Object)Float.valueOf(scale));
    }

    public int getGunId() {
        return (Integer)this.entityData.get(DATA_GUN_ID);
    }

    public void setGunId(int gunId) {
        this.entityData.set(DATA_GUN_ID, (Object)gunId);
    }

    public String getGunMode() {
        return (String)this.entityData.get(DATA_GUN_MODE);
    }

    public void setGunMode(String gunMode) {
        this.entityData.set(DATA_GUN_MODE, (Object)gunMode);
    }

    public boolean isAutoClose() {
        return (Boolean)this.entityData.get(DATA_AUTO_CLOSE);
    }

    public void setAutoClose(boolean autoClose) {
        this.entityData.set(DATA_AUTO_CLOSE, (Object)autoClose);
    }

    public boolean isAutoAirDetonate() {
        return (Boolean)this.entityData.get(DATA_AUTO_AIR_DETONATE);
    }

    public void setAutoAirDetonate(boolean auto) {
        this.entityData.set(DATA_AUTO_AIR_DETONATE, (Object)auto);
    }

    public int getAirDistance() {
        return (Integer)this.entityData.get(DATA_AIR_DISTANCE);
    }

    public void setAirDistance(int distance) {
        this.entityData.set(DATA_AIR_DISTANCE, (Object)distance);
    }

    public boolean hasHazardShield() {
        return (Boolean)this.entityData.get(DATA_HAZARD_SHIELD);
    }

    public void setHazardShield(boolean hazardShield) {
        this.entityData.set(DATA_HAZARD_SHIELD, (Object)hazardShield);
    }

    public boolean isSafePlacement() {
        return (Boolean)this.entityData.get(DATA_SAFE_PLACEMENT);
    }

    public void setSafePlacement(boolean safePlacement) {
        this.entityData.set(DATA_SAFE_PLACEMENT, (Object)safePlacement);
    }

    public Vec3 getSpawnOrigin() {
        return this.spawnOrigin;
    }

    public void setSpawnOrigin(Vec3 origin) {
        this.spawnOrigin = origin;
    }

    public CompoundTag getCustomLocation() {
        return this.customLocation;
    }

    public void setCustomLocation(CompoundTag customLocation) {
        this.customLocation = customLocation != null ? customLocation.copy() : new CompoundTag();
    }

    public void tick() {
        super.tick();
        if (this.isRemoved()) {
            return;
        }
        if (!this.level().isClientSide) {
            Level level;
            if (AntiPortalFieldManager.isProtected((Level)this.level(), (Vec3)this.position())) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.ANTI_PORTAL_DEFLECT.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                Level level2 = this.level();
                if (level2 instanceof ServerLevel) {
                    ServerLevel sl = (ServerLevel)level2;
                    for (int i = 0; i < 8; ++i) {
                        sl.sendParticles((ParticleOptions)((SimpleParticleType)ModParticles.BLUE_DIMENSIONAL_SPARK.get()), this.getX(), this.getY(), this.getZ(), 1, (this.random.nextDouble() - 0.5) * 0.3, (this.random.nextDouble() - 0.5) * 0.3, (this.random.nextDouble() - 0.5) * 0.3, 0.05);
                    }
                }
                this.discard();
                return;
            }
            if (this.spawnOrigin == null) {
                this.spawnOrigin = this.position();
            }
            double maxDist = Config.portalGunMaxDistance;
            if (this.position().distanceTo(this.spawnOrigin) >= maxDist || this.tickCount > 240) {
                this.discard();
                return;
            }
            if (this.isAutoAirDetonate() && (level = this.level()) instanceof ServerLevel) {
                ServerLevel serverLevel = (ServerLevel)level;
                if (this.position().distanceTo(this.spawnOrigin) >= (double)this.getAirDistance()) {
                    PortalManager.handleAirPortalSpawn((ServerLevel)serverLevel, (Vec3)this.position(), (float)this.getYRot(), (PortalProjectileEntity)this);
                    this.discard();
                    return;
                }
            }
        }
        if (!this.level().isClientSide) return;
        Vec3 movement = this.getDeltaMovement();
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        int color = this.getColor();
        SimpleParticleType dropParticle = switch (color) {
            case 1 -> (SimpleParticleType)ModParticles.BLUE_FLUID_DROPLET.get();
            case 2 -> (SimpleParticleType)ModParticles.YELLOW_FLUID_DROPLET.get();
            default -> (SimpleParticleType)ModParticles.GREEN_FLUID_DROPLET.get();
        };
        SimpleParticleType ringParticle = switch (color) {
            case 1 -> (SimpleParticleType)ModParticles.BLUE_PLASMA_SHOCKWAVE.get();
            case 2 -> (SimpleParticleType)ModParticles.YELLOW_PLASMA_SHOCKWAVE.get();
            default -> (SimpleParticleType)ModParticles.GREEN_PLASMA_SHOCKWAVE.get();
        };
        int i = 0;
        while (true) {
            if (i >= 2) {
                if (this.tickCount % 2 != 0) return;
                this.level().addParticle((ParticleOptions)ringParticle, x, y, z, 0.0, 0.0, 0.0);
                return;
            }
            this.level().addParticle((ParticleOptions)dropParticle, x + (this.random.nextDouble() - 0.5) * 0.06, y + (this.random.nextDouble() - 0.5) * 0.06, z + (this.random.nextDouble() - 0.5) * 0.06, -movement.x * 0.08, -movement.y * 0.08, -movement.z * 0.08);
            ++i;
        }
    }

    protected void onHitEntity(EntityHitResult result) {
        Level level;
        Entity target = result.getEntity();
        Entity owner = this.getOwner();
        if (target == null) return;
        if (target instanceof PortalEntity) {
            return;
        }
        if (owner != null) {
            if (target == owner) return;
            if (target.getUUID().equals(owner.getUUID())) {
                return;
            }
        }
        super.onHitEntity(result);
        if (this.isRemoved()) {
            return;
        }
        if (!this.level().isClientSide() && (level = this.level()) instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            if (target instanceof LivingEntity) {
                LivingEntity living = (LivingEntity)target;
                living.addEffect(new MobEffectInstance(MobEffects.WITHER, 300, 5));
                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 300, 5));
            }
            int color = this.getColor();
            SimpleParticleType poisonParticle = switch (color) {
                case 1 -> (SimpleParticleType)ModParticles.BLUE_FLUID_POISON.get();
                case 2 -> (SimpleParticleType)ModParticles.YELLOW_FLUID_POISON.get();
                default -> (SimpleParticleType)ModParticles.GREEN_FLUID_POISON.get();
            };
            SimpleParticleType groundParticle = switch (color) {
                case 1 -> (SimpleParticleType)ModParticles.BLUE_FLUID_GROUND_DROP.get();
                case 2 -> (SimpleParticleType)ModParticles.YELLOW_FLUID_GROUND_DROP.get();
                default -> (SimpleParticleType)ModParticles.GREEN_FLUID_GROUND_DROP.get();
            };
            double targetX = target.getX();
            double targetY = target.getY() + (double)target.getBbHeight() * 0.5;
            double targetZ = target.getZ();
            for (int i = 0; i < 100; ++i) {
                double theta = this.random.nextDouble() * Math.PI * 2.0;
                double phi = Math.acos(this.random.nextDouble() * 2.0 - 1.0);
                double speed = 0.02 + this.random.nextDouble() * 0.055;
                double vx = Math.sin(phi) * Math.cos(theta) * speed;
                double vy = Math.sin(phi) * Math.sin(theta) * speed + 0.012;
                double vz = Math.cos(phi) * speed;
                serverLevel.sendParticles((ParticleOptions)poisonParticle, targetX, targetY, targetZ, 1, vx, vy, vz, 1.0);
            }
            BlockPos targetPos = target.blockPosition();
            BlockPos.MutableBlockPos scanPos = targetPos.mutable();
            boolean foundGround = false;
            double groundY = 0.0;
            for (int dy = 0; dy <= 4; ++dy) {
                scanPos.set(targetPos.getX(), targetPos.getY() - dy, targetPos.getZ());
                BlockState state = serverLevel.getBlockState((BlockPos)scanPos);
                if (state.isAir() || !state.isSolid() && !state.blocksMotion()) continue;
                foundGround = true;
                groundY = (double)scanPos.getY() + 1.0;
                break;
            }
            if (foundGround && Math.abs(target.getY() - groundY) < 2.5) {
                for (int i = 0; i < 8; ++i) {
                    double gx = target.getX() + (this.random.nextDouble() - 0.5) * 1.8;
                    double gz = target.getZ() + (this.random.nextDouble() - 0.5) * 1.8;
                    serverLevel.sendParticles((ParticleOptions)groundParticle, gx, groundY, gz, 1, 0.0, 0.0, 0.0, 0.0);
                }
            }
            serverLevel.playSound(null, this.blockPosition(), (SoundEvent)ModSounds.PLASMA_SCORCH.get(), SoundSource.NEUTRAL, 1.0f, 1.0f);
        }
        this.discard();
    }

    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (this.level().isClientSide) return;
        Level level = this.level();
        if (!(level instanceof ServerLevel)) return;
        ServerLevel serverLevel = (ServerLevel)level;
        PortalManager.handlePortalSpawn((ServerLevel)serverLevel, (BlockHitResult)result, (PortalProjectileEntity)this);
        this.discard();
    }

    protected void onHit(HitResult result) {
        super.onHit(result);
    }

    protected boolean canHitEntity(Entity entity) {
        if (entity == null) return false;
        if (entity instanceof PortalEntity) {
            return false;
        }
        Entity owner = this.getOwner();
        if (owner == null) return super.canHitEntity(entity);
        if (entity == owner) return false;
        if (!entity.getUUID().equals(owner.getUUID())) return super.canHitEntity(entity);
        return false;
    }

    protected double getDefaultGravity() {
        return 0.0;
    }

    public void remove(Entity.RemovalReason reason) {
        PortalManager.unregisterProjectile((PortalProjectileEntity)this);
        super.remove(reason);
    }

    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Color", this.getColor());
        tag.putFloat("PortalScale", this.getPortalScale());
        tag.putInt("GunId", this.getGunId());
        tag.putString("GunMode", this.getGunMode());
        tag.putBoolean("AutoClose", this.isAutoClose());
        tag.putBoolean("AutoAirDetonate", this.isAutoAirDetonate());
        tag.putInt("AirDistance", this.getAirDistance());
        tag.putBoolean("HazardShield", this.hasHazardShield());
        tag.putBoolean("SafePlacement", this.isSafePlacement());
        if (this.spawnOrigin != null) {
            tag.putDouble("SpawnOriginX", this.spawnOrigin.x);
            tag.putDouble("SpawnOriginY", this.spawnOrigin.y);
            tag.putDouble("SpawnOriginZ", this.spawnOrigin.z);
        }
        if (this.customLocation == null) return;
        if (this.customLocation.isEmpty()) return;
        tag.put("CustomLocation", (Tag)this.customLocation);
    }

    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Color")) {
            this.setColor(tag.getInt("Color"));
        }
        if (tag.contains("PortalScale")) {
            this.setPortalScale(tag.getFloat("PortalScale"));
        }
        if (tag.contains("GunId")) {
            this.setGunId(tag.getInt("GunId"));
        }
        if (tag.contains("GunMode")) {
            this.setGunMode(tag.getString("GunMode"));
        }
        if (tag.contains("AutoClose")) {
            this.setAutoClose(tag.getBoolean("AutoClose"));
        }
        if (tag.contains("AutoAirDetonate")) {
            this.setAutoAirDetonate(tag.getBoolean("AutoAirDetonate"));
        }
        if (tag.contains("AirDistance")) {
            this.setAirDistance(tag.getInt("AirDistance"));
        }
        if (tag.contains("HazardShield")) {
            this.setHazardShield(tag.getBoolean("HazardShield"));
        }
        if (tag.contains("SafePlacement")) {
            this.setSafePlacement(tag.getBoolean("SafePlacement"));
        }
        if (tag.contains("SpawnOriginX") && tag.contains("SpawnOriginY") && tag.contains("SpawnOriginZ")) {
            this.spawnOrigin = new Vec3(tag.getDouble("SpawnOriginX"), tag.getDouble("SpawnOriginY"), tag.getDouble("SpawnOriginZ"));
        }
        if (!tag.contains("CustomLocation")) return;
        this.setCustomLocation(tag.getCompound("CustomLocation"));
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket((Entity)this, serverEntity);
    }
}

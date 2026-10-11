/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.entity.projectile.ThrowableItemProjectile
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.entity.PortalProjectileEntity
 *  org.ExtraTortik.ricks_stuff.entity.omega.MultiverseErasureSavedData
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceCollider
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity$1
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity$ClientAudioProxy
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceManager
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDevicePhase
 *  org.ExtraTortik.ricks_stuff.init.ModParticles
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.S2COmegaDeviceEffectPacket
 */
package org.ExtraTortik.ricks_stuff.entity.omega;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.entity.PortalProjectileEntity;
import org.ExtraTortik.ricks_stuff.entity.omega.MultiverseErasureSavedData;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceCollider;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceManager;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDevicePhase;
import org.ExtraTortik.ricks_stuff.init.ModParticles;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.S2COmegaDeviceEffectPacket;
import org.jetbrains.annotations.Nullable;

/*
 * Exception performing whole class analysis ignored.
 */
public class OmegaDeviceEntity
extends Entity {
    public static final EntityDataAccessor<Integer> DATA_PHASE = SynchedEntityData.defineId(OmegaDeviceEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> DATA_PHASE_TICKS = SynchedEntityData.defineId(OmegaDeviceEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> DATA_TARGET_ENTITY_ID = SynchedEntityData.defineId(OmegaDeviceEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> DATA_CORE_ENERGY_SCALE = SynchedEntityData.defineId(OmegaDeviceEntity.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> DATA_RING_ROTATION_SPEED = SynchedEntityData.defineId(OmegaDeviceEntity.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> DATA_COLOR_INTERPOLATION = SynchedEntityData.defineId(OmegaDeviceEntity.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Integer> DATA_COOLDOWN_REMAINING = SynchedEntityData.defineId(OmegaDeviceEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    public static final EntityDataAccessor<String> DATA_TARGET_UUID = SynchedEntityData.defineId(OmegaDeviceEntity.class, (EntityDataSerializer)EntityDataSerializers.STRING);
    public static final EntityDataAccessor<String> DATA_TARGET_TYPE = SynchedEntityData.defineId(OmegaDeviceEntity.class, (EntityDataSerializer)EntityDataSerializers.STRING);
    public float prevEquatorialYaw;
    public float equatorialYaw;
    public float prevUpperLatitudinalYaw;
    public float upperLatitudinalYaw;
    public float prevUpperLatitudinalPitch;
    public float upperLatitudinalPitch;
    public float prevLowerLatitudinalYaw;
    public float lowerLatitudinalYaw;
    public float prevLowerLatitudinalPitch;
    public float lowerLatitudinalPitch;
    public float prevCoreScale = 1.0f;
    public float coreScale = 1.0f;
    public float prevColorInterp = 0.0f;
    public float colorInterp = 0.0f;
    @Nullable
    private Entity cachedTargetEntity;

    public OmegaDeviceEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
        this.blocksBuilding = true;
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_PHASE, (Object)OmegaDevicePhase.IDLE.getId());
        builder.define(DATA_PHASE_TICKS, (Object)0);
        builder.define(DATA_TARGET_ENTITY_ID, (Object)-1);
        builder.define(DATA_CORE_ENERGY_SCALE, (Object)Float.valueOf(1.0f));
        builder.define(DATA_RING_ROTATION_SPEED, (Object)Float.valueOf(0.2f));
        builder.define(DATA_COLOR_INTERPOLATION, (Object)Float.valueOf(0.0f));
        builder.define(DATA_COOLDOWN_REMAINING, (Object)0);
        builder.define(DATA_TARGET_UUID, (Object)"");
        builder.define(DATA_TARGET_TYPE, (Object)"");
    }

    public void tick() {
        super.tick();
        if (Config.omegaDeviceModelEnabled) {
            List nearby = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(35.0, 35.0, 35.0));
            for (LivingEntity entity : nearby) {
                OmegaDeviceCollider.handleEntityCollision((LivingEntity)entity, (OmegaDeviceEntity)this);
            }
        }
        if (this.level().isClientSide) {
            this.clientTick();
        } else {
            this.serverTick();
        }
    }

    private void serverTick() {
        OmegaDevicePhase phase = this.getPhase();
        int ticks = this.getPhaseTicks();
        int cooldown = this.getCooldownRemaining();
        if (phase == OmegaDevicePhase.IDLE) {
            if (cooldown > 0) {
                this.setCooldownRemaining(cooldown - 1);
            }
            this.setCoreEnergyScale(1.0f);
            this.setRingRotationSpeed(0.2f);
            this.setColorInterpolation(0.0f);
            return;
        }
        this.setPhaseTicks(++ticks);
        Entity target = this.resolveTargetEntity();
        if (target != null && target.isAlive()) {
            this.applyTractorAttractor(target);
        }
        switch (1.$SwitchMap$org$ExtraTortik$ricks_stuff$entity$omega$OmegaDevicePhase[phase.ordinal()]) {
            case 1: {
                this.setCoreEnergyScale(1.0f);
                this.setRingRotationSpeed(2.8f);
                this.setColorInterpolation(1.0f);
                AABB coreBox = new AABB(this.getX() - 18.0, this.getY() - 18.0, this.getZ() - 18.0, this.getX() + 18.0, this.getY() + 18.0, this.getZ() + 18.0);
                ArrayList<Entity> insideEntities = new ArrayList<Entity>(this.level().getEntities((Entity)null, coreBox, e -> e != this && e.isAlive() && !e.isSpectator() && e.distanceToSqr((Entity)this) <= 324.0));
                if (insideEntities.isEmpty()) return;
                insideEntities.removeIf(e -> {
                    if (!(e instanceof PortalProjectileEntity)) return false;
                    PortalProjectileEntity portalProj = (PortalProjectileEntity)e;
                    this.level().playSound(null, portalProj.getX(), portalProj.getY(), portalProj.getZ(), (SoundEvent)ModSounds.ANTI_PORTAL_DEFLECT.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                    Level patt0$temp = this.level();
                    if (patt0$temp instanceof ServerLevel) {
                        ServerLevel sl = (ServerLevel)patt0$temp;
                        for (int i = 0; i < 8; ++i) {
                            sl.sendParticles((ParticleOptions)((SimpleParticleType)ModParticles.BLUE_DIMENSIONAL_SPARK.get()), portalProj.getX(), portalProj.getY(), portalProj.getZ(), 1, (this.random.nextDouble() - 0.5) * 0.3, (this.random.nextDouble() - 0.5) * 0.3, (this.random.nextDouble() - 0.5) * 0.3, 0.05);
                        }
                    }
                    portalProj.discard();
                    return true;
                });
                for (Entity entity : insideEntities) {
                    if (!(entity.distanceToSqr((Entity)this) > 12.25)) continue;
                    this.applyTractorAttractor(entity);
                }
                Entity victim = null;
                for (Entity e3 : insideEntities) {
                    if (!(e3.distanceToSqr((Entity)this) <= 12.25)) continue;
                    victim = e3;
                    break;
                }
                if (victim == null) return;
                if (victim instanceof ItemEntity) {
                    ItemEntity itemEntity = (ItemEntity)victim;
                    ItemStack stack = itemEntity.getItem();
                    if (stack.isEmpty()) {
                        itemEntity.discard();
                        break;
                    }
                    if (!Config.allowItemErasure) {
                        itemEntity.discard();
                        break;
                    }
                    ResourceLocation key = BuiltInRegistries.ITEM.getKey((Object)stack.getItem());
                    if (key == null) return;
                    String itemId = key.toString();
                    this.setTargetEntity(null);
                    this.setTargetUUID(null);
                    this.setTargetType(itemId);
                    this.setPhase(OmegaDevicePhase.ERASING);
                    this.setPhaseTicks(0);
                    Level level = this.level();
                    if (level instanceof ServerLevel) {
                        ServerLevel sl = (ServerLevel)level;
                        MultiverseErasureSavedData.get((MinecraftServer)sl.getServer()).eraseItem(itemId);
                        OmegaDeviceManager.executeMultiverseItemErasure((ServerLevel)sl, (String)itemId, (Vec3)this.position());
                        sl.playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.OMEGA_DEVICE_ERASE.get(), SoundSource.BLOCKS, 32.0f, 1.0f);
                        ModMessages.sendToNear((CustomPacketPayload)new S2COmegaDeviceEffectPacket(2, this.getX(), this.getY(), this.getZ(), 1.0f, 30, 1.0f, 45), (double)this.getX(), (double)this.getY(), (double)this.getZ(), (double)256.0, (ResourceKey)sl.dimension());
                    }
                    itemEntity.discard();
                    break;
                }
                if (victim instanceof ThrowableItemProjectile) {
                    ThrowableItemProjectile tip = (ThrowableItemProjectile)victim;
                    ItemStack stack = tip.getItem();
                    if (stack.isEmpty() || !Config.allowItemErasure) {
                        tip.discard();
                        break;
                    }
                    ResourceLocation key = BuiltInRegistries.ITEM.getKey((Object)stack.getItem());
                    if (key == null) return;
                    String itemId = key.toString();
                    this.setTargetEntity(null);
                    this.setTargetUUID(null);
                    this.setTargetType(itemId);
                    this.setPhase(OmegaDevicePhase.ERASING);
                    this.setPhaseTicks(0);
                    Level level = this.level();
                    if (level instanceof ServerLevel) {
                        ServerLevel sl = (ServerLevel)level;
                        MultiverseErasureSavedData.get((MinecraftServer)sl.getServer()).eraseItem(itemId);
                        OmegaDeviceManager.executeMultiverseItemErasure((ServerLevel)sl, (String)itemId, (Vec3)this.position());
                        sl.playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.OMEGA_DEVICE_ERASE.get(), SoundSource.BLOCKS, 32.0f, 1.0f);
                        ModMessages.sendToNear((CustomPacketPayload)new S2COmegaDeviceEffectPacket(2, this.getX(), this.getY(), this.getZ(), 1.0f, 30, 1.0f, 45), (double)this.getX(), (double)this.getY(), (double)this.getZ(), (double)256.0, (ResourceKey)sl.dimension());
                    }
                    tip.discard();
                    break;
                }
                if (victim instanceof Projectile) {
                    Projectile projectile = (Projectile)victim;
                    if (!Config.allowMobErasure) {
                        projectile.discard();
                        break;
                    }
                    ResourceLocation key = BuiltInRegistries.ENTITY_TYPE.getKey((Object)projectile.getType());
                    if (key == null) return;
                    String typeId = key.toString();
                    this.setTargetEntity(null);
                    this.setTargetUUID(null);
                    this.setTargetType(typeId);
                    this.setPhase(OmegaDevicePhase.ERASING);
                    this.setPhaseTicks(0);
                    Level level = this.level();
                    if (level instanceof ServerLevel) {
                        ServerLevel sl = (ServerLevel)level;
                        MultiverseErasureSavedData.get((MinecraftServer)sl.getServer()).eraseEntityType(typeId);
                        OmegaDeviceManager.executeMultiverseErasure((ServerLevel)sl, (Entity)projectile, (String)typeId, null, (Vec3)this.position());
                        sl.playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.OMEGA_DEVICE_ERASE.get(), SoundSource.BLOCKS, 32.0f, 1.0f);
                        ModMessages.sendToNear((CustomPacketPayload)new S2COmegaDeviceEffectPacket(2, this.getX(), this.getY(), this.getZ(), 1.0f, 30, 1.0f, 45), (double)this.getX(), (double)this.getY(), (double)this.getZ(), (double)256.0, (ResourceKey)sl.dimension());
                    }
                    projectile.discard();
                    break;
                }
                if (victim instanceof Player) {
                    Player playerVictim = (Player)victim;
                    if (!Config.allowPlayerErasure) return;
                    this.setTargetEntity((Entity)playerVictim);
                    this.setTargetUUID(playerVictim.getUUID());
                    ResourceLocation key = BuiltInRegistries.ENTITY_TYPE.getKey((Object)playerVictim.getType());
                    if (key != null) {
                        this.setTargetType(key.toString());
                    }
                    this.setPhase(OmegaDevicePhase.ERASING);
                    this.setPhaseTicks(0);
                    Level level = this.level();
                    if (!(level instanceof ServerLevel)) return;
                    ServerLevel sl = (ServerLevel)level;
                    OmegaDeviceManager.executeMultiverseErasure((ServerLevel)sl, (Entity)playerVictim, (String)this.getTargetType(), (UUID)this.getTargetUUID(), (Vec3)this.position());
                    sl.playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.OMEGA_DEVICE_ERASE.get(), SoundSource.BLOCKS, 32.0f, 1.0f);
                    ModMessages.sendToNear((CustomPacketPayload)new S2COmegaDeviceEffectPacket(2, this.getX(), this.getY(), this.getZ(), 1.0f, 30, 1.0f, 45), (double)this.getX(), (double)this.getY(), (double)this.getZ(), (double)256.0, (ResourceKey)sl.dimension());
                    break;
                }
                if (!Config.allowMobErasure) {
                    victim.discard();
                    break;
                }
                this.setTargetEntity(victim);
                this.setTargetUUID(victim.getUUID());
                ResourceLocation key = BuiltInRegistries.ENTITY_TYPE.getKey((Object)victim.getType());
                if (key != null) {
                    this.setTargetType(key.toString());
                }
                this.setPhase(OmegaDevicePhase.ERASING);
                this.setPhaseTicks(0);
                Level level = this.level();
                if (!(level instanceof ServerLevel)) return;
                ServerLevel sl = (ServerLevel)level;
                OmegaDeviceManager.executeMultiverseErasure((ServerLevel)sl, (Entity)victim, (String)this.getTargetType(), (UUID)this.getTargetUUID(), (Vec3)this.position());
                sl.playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.OMEGA_DEVICE_ERASE.get(), SoundSource.BLOCKS, 32.0f, 1.0f);
                ModMessages.sendToNear((CustomPacketPayload)new S2COmegaDeviceEffectPacket(2, this.getX(), this.getY(), this.getZ(), 1.0f, 30, 1.0f, 45), (double)this.getX(), (double)this.getY(), (double)this.getZ(), (double)256.0, (ResourceKey)sl.dimension());
                break;
            }
            case 2: 
            case 3: 
            case 4: {
                this.setPhase(OmegaDevicePhase.ARMED);
                this.setPhaseTicks(0);
                break;
            }
            case 5: {
                this.setCoreEnergyScale(1.0f);
                this.setRingRotationSpeed(3.5f);
                this.setColorInterpolation(2.0f);
                if (ticks < OmegaDevicePhase.ERASING.getDurationTicks()) return;
                this.setPhase(OmegaDevicePhase.COOLDOWN);
                this.setPhaseTicks(0);
                this.setCooldownRemaining(200);
                Level level = this.level();
                if (!(level instanceof ServerLevel)) return;
                ServerLevel sl = (ServerLevel)level;
                sl.playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.OMEGA_DEVICE_COOLDOWN.get(), SoundSource.BLOCKS, 20.0f, 1.0f);
                break;
            }
            case 6: {
                float progress = Math.min(1.0f, (float)ticks / 40.0f);
                this.setRingRotationSpeed(2.8f * (1.0f - progress) + 0.2f);
                this.setCoreEnergyScale(1.0f);
                this.setColorInterpolation(1.0f - progress);
                if (ticks < 40) return;
                this.setPhase(OmegaDevicePhase.IDLE);
                this.setPhaseTicks(0);
                this.setTargetEntityId(-1);
                this.setTargetUUID(null);
                this.setTargetType("");
                break;
            }
        }
    }

    private void clientTick() {
        float speed = this.getRingRotationSpeed();
        float degSpeed = speed * 57.295776f * 0.05f;
        this.prevEquatorialYaw = this.equatorialYaw;
        this.equatorialYaw += degSpeed;
        this.prevUpperLatitudinalYaw = this.upperLatitudinalYaw;
        this.upperLatitudinalYaw -= degSpeed * 0.75f;
        this.prevUpperLatitudinalPitch = this.upperLatitudinalPitch;
        this.upperLatitudinalPitch = 35.0f + 8.0f * Mth.sin((float)((float)this.tickCount * 0.12f));
        this.prevLowerLatitudinalYaw = this.lowerLatitudinalYaw;
        this.lowerLatitudinalYaw -= degSpeed * 0.75f;
        this.prevLowerLatitudinalPitch = this.lowerLatitudinalPitch;
        this.lowerLatitudinalPitch = -35.0f - 8.0f * Mth.sin((float)((float)this.tickCount * 0.12f));
        this.prevCoreScale = this.coreScale;
        this.coreScale = this.getCoreEnergyScale();
        this.prevColorInterp = this.colorInterp;
        this.colorInterp = this.getColorInterpolation();
        OmegaDevicePhase phase = this.getPhase();
        if (phase.isActivePower()) {
            this.spawnClientAtmosphericParticles(phase);
        }
        if ((phase == OmegaDevicePhase.IDLE || phase == OmegaDevicePhase.ARMED) && this.tickCount % 280 == Math.abs(this.getId()) % 280 && this.random.nextFloat() < 0.7f) {
            double sparkAngle = this.random.nextDouble() * Math.PI * 2.0;
            double sparkRadius = 16.0 + this.random.nextDouble() * 12.0;
            double sx = this.getX() + Math.cos(sparkAngle) * sparkRadius;
            double sy = this.getY() + (this.random.nextDouble() - 0.5) * 14.0;
            double sz = this.getZ() + Math.sin(sparkAngle) * sparkRadius;
            float pitch = 0.9f + this.random.nextFloat() * 0.25f;
            float vol = 0.25f + this.random.nextFloat() * 0.15f;
            this.level().playLocalSound(sx, sy, sz, (SoundEvent)ModSounds.OMEGA_DEVICE_SPARK.get(), SoundSource.BLOCKS, vol, pitch, false);
            for (int i = 0; i < 6; ++i) {
                this.level().addParticle((ParticleOptions)ModParticles.OMEGA_PLASMA_SPARK.get(), sx, sy, sz, (this.random.nextDouble() - 0.5) * 0.25, (this.random.nextDouble() - 0.5) * 0.25, (this.random.nextDouble() - 0.5) * 0.25);
            }
        }
        ClientAudioProxy.handleSound((OmegaDeviceEntity)this);
    }

    private void spawnClientAtmosphericParticles(OmegaDevicePhase phase) {
        if (this.random.nextFloat() < 0.4f) {
            double angle = this.random.nextDouble() * Math.PI * 2.0;
            double radius = 10.0 + this.random.nextDouble() * 20.0;
            double px = this.getX() + Math.cos(angle) * radius;
            double py = this.getY() + (this.random.nextDouble() - 0.5) * 20.0;
            double pz = this.getZ() + Math.sin(angle) * radius;
            double vx = (this.getX() - px) * 0.05;
            double vy = (this.getY() - py) * 0.05;
            double vz = (this.getZ() - pz) * 0.05;
            this.level().addParticle((ParticleOptions)ModParticles.OMEGA_PLASMA_SPARK.get(), px, py, pz, vx, vy, vz);
        }
        if (phase != OmegaDevicePhase.POWER_PUMP) return;
        if (!(this.random.nextFloat() < 0.25f)) return;
        double rx = this.getX() + (this.random.nextDouble() - 0.5) * 8.0;
        double ry = this.getY() + (this.random.nextDouble() - 0.5) * 8.0;
        double rz = this.getZ() + (this.random.nextDouble() - 0.5) * 8.0;
        this.level().addParticle((ParticleOptions)ModParticles.QUANTUM_GLITCH.get(), rx, ry, rz, 0.0, 0.0, 0.0);
    }

    private void applyTractorAttractor(Entity target) {
        Vec3 targetPos;
        if (target == null) return;
        if (target.isSpectator()) {
            return;
        }
        Vec3 center = this.position();
        Vec3 diff = center.subtract(targetPos = target.position());
        double dist = diff.length();
        if (dist > 0.2) {
            Vec3 pullVelocity = diff.normalize().scale(Math.min(dist * 0.25, 1.8));
            target.setDeltaMovement(pullVelocity);
            if (target instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)target;
                serverPlayer.connection.send((Packet)new ClientboundSetEntityMotionPacket(serverPlayer.getId(), pullVelocity));
            }
        } else {
            target.setPos(center.x, center.y, center.z);
            target.setDeltaMovement(Vec3.ZERO);
            if (target instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)target;
                serverPlayer.connection.teleport(center.x, center.y, center.z, target.getYRot(), target.getXRot());
            }
        }
        target.resetFallDistance();
        target.hurtMarked = true;
    }

    @Nullable
    public Entity resolveTargetEntity() {
        UUID uuid;
        int id = this.getTargetEntityId();
        if (id != -1) {
            if (this.cachedTargetEntity != null && this.cachedTargetEntity.getId() == id && this.cachedTargetEntity.isAlive() && !this.cachedTargetEntity.isSpectator()) {
                return this.cachedTargetEntity;
            }
            Entity found = this.level().getEntity(id);
            if (found != null && found.isAlive() && !found.isSpectator()) {
                this.cachedTargetEntity = found;
                return found;
            }
        }
        if ((uuid = this.getTargetUUID()) == null) return null;
        Level level = this.level();
        if (!(level instanceof ServerLevel)) return null;
        ServerLevel sl = (ServerLevel)level;
        Entity found = sl.getEntity(uuid);
        if (found == null) return null;
        if (!found.isAlive()) return null;
        if (found.isSpectator()) return null;
        this.cachedTargetEntity = found;
        this.setTargetEntityId(found.getId());
        return found;
    }

    public OmegaDevicePhase getPhase() {
        return OmegaDevicePhase.byId((int)((Integer)this.entityData.get(DATA_PHASE)));
    }

    public void setPhase(OmegaDevicePhase phase) {
        this.entityData.set(DATA_PHASE, (Object)phase.getId());
    }

    public int getPhaseTicks() {
        return (Integer)this.entityData.get(DATA_PHASE_TICKS);
    }

    public void setPhaseTicks(int ticks) {
        this.entityData.set(DATA_PHASE_TICKS, (Object)ticks);
    }

    public int getTargetEntityId() {
        return (Integer)this.entityData.get(DATA_TARGET_ENTITY_ID);
    }

    public void setTargetEntityId(int id) {
        this.entityData.set(DATA_TARGET_ENTITY_ID, (Object)id);
    }

    public void setTargetEntity(@Nullable Entity target) {
        this.cachedTargetEntity = target;
        if (target != null) {
            this.setTargetEntityId(target.getId());
            this.setTargetUUID(target.getUUID());
        } else {
            this.setTargetEntityId(-1);
        }
    }

    @Nullable
    public Entity getTargetEntity() {
        return this.resolveTargetEntity();
    }

    public float getCoreEnergyScale() {
        return ((Float)this.entityData.get(DATA_CORE_ENERGY_SCALE)).floatValue();
    }

    public void setCoreEnergyScale(float scale) {
        this.entityData.set(DATA_CORE_ENERGY_SCALE, (Object)Float.valueOf(scale));
    }

    public float getRingRotationSpeed() {
        return ((Float)this.entityData.get(DATA_RING_ROTATION_SPEED)).floatValue();
    }

    public void setRingRotationSpeed(float speed) {
        this.entityData.set(DATA_RING_ROTATION_SPEED, (Object)Float.valueOf(speed));
    }

    public float getColorInterpolation() {
        return ((Float)this.entityData.get(DATA_COLOR_INTERPOLATION)).floatValue();
    }

    public void setColorInterpolation(float factor) {
        this.entityData.set(DATA_COLOR_INTERPOLATION, (Object)Float.valueOf(factor));
    }

    public int getCooldownRemaining() {
        return (Integer)this.entityData.get(DATA_COOLDOWN_REMAINING);
    }

    public void setCooldownRemaining(int ticks) {
        this.entityData.set(DATA_COOLDOWN_REMAINING, (Object)ticks);
    }

    @Nullable
    public UUID getTargetUUID() {
        String str = (String)this.entityData.get(DATA_TARGET_UUID);
        if (str == null) return null;
        if (str.isBlank()) {
            return null;
        }
        try {
            return UUID.fromString(str);
        }
        catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void setTargetUUID(@Nullable UUID uuid) {
        this.entityData.set(DATA_TARGET_UUID, (Object)(uuid != null ? uuid.toString() : ""));
    }

    public String getTargetType() {
        return (String)this.entityData.get(DATA_TARGET_TYPE);
    }

    public void setTargetType(@Nullable String targetType) {
        this.entityData.set(DATA_TARGET_TYPE, (Object)(targetType != null ? targetType : ""));
    }

    public float getEquatorialYaw(float partialTicks) {
        return Mth.lerp((float)partialTicks, (float)this.prevEquatorialYaw, (float)this.equatorialYaw);
    }

    public float getUpperLatitudinalYaw(float partialTicks) {
        return Mth.lerp((float)partialTicks, (float)this.prevUpperLatitudinalYaw, (float)this.upperLatitudinalYaw);
    }

    public float getUpperLatitudinalPitch(float partialTicks) {
        return Mth.lerp((float)partialTicks, (float)this.prevUpperLatitudinalPitch, (float)this.upperLatitudinalPitch);
    }

    public float getLowerLatitudinalYaw(float partialTicks) {
        return Mth.lerp((float)partialTicks, (float)this.prevLowerLatitudinalYaw, (float)this.lowerLatitudinalYaw);
    }

    public float getLowerLatitudinalPitch(float partialTicks) {
        return Mth.lerp((float)partialTicks, (float)this.prevLowerLatitudinalPitch, (float)this.lowerLatitudinalPitch);
    }

    public float getInterpolatedCoreScale(float partialTicks) {
        return Mth.lerp((float)partialTicks, (float)this.prevCoreScale, (float)this.coreScale);
    }

    public float getInterpolatedColor(float partialTicks) {
        return Mth.lerp((float)partialTicks, (float)this.prevColorInterp, (float)this.colorInterp);
    }

    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 640000.0;
    }

    public boolean isPickable() {
        return true;
    }

    public boolean isPushable() {
        return false;
    }

    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    public boolean isInvulnerable() {
        return true;
    }

    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("Phase")) {
            this.setPhase(OmegaDevicePhase.byId((int)tag.getInt("Phase")));
        }
        if (tag.contains("PhaseTicks")) {
            this.setPhaseTicks(tag.getInt("PhaseTicks"));
        }
        if (tag.contains("TargetEntityId")) {
            this.setTargetEntityId(tag.getInt("TargetEntityId"));
        }
        if (tag.contains("CoreEnergyScale")) {
            this.setCoreEnergyScale(tag.getFloat("CoreEnergyScale"));
        }
        if (tag.contains("RingRotationSpeed")) {
            this.setRingRotationSpeed(tag.getFloat("RingRotationSpeed"));
        }
        if (tag.contains("ColorInterpolation")) {
            this.setColorInterpolation(tag.getFloat("ColorInterpolation"));
        }
        if (tag.contains("CooldownRemaining")) {
            this.setCooldownRemaining(tag.getInt("CooldownRemaining"));
        }
        if (tag.contains("TargetUUID")) {
            this.entityData.set(DATA_TARGET_UUID, (Object)tag.getString("TargetUUID"));
        }
        if (!tag.contains("TargetType")) return;
        this.entityData.set(DATA_TARGET_TYPE, (Object)tag.getString("TargetType"));
    }

    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Phase", this.getPhase().getId());
        tag.putInt("PhaseTicks", this.getPhaseTicks());
        tag.putInt("TargetEntityId", this.getTargetEntityId());
        tag.putFloat("CoreEnergyScale", this.getCoreEnergyScale());
        tag.putFloat("RingRotationSpeed", this.getRingRotationSpeed());
        tag.putFloat("ColorInterpolation", this.getColorInterpolation());
        tag.putInt("CooldownRemaining", this.getCooldownRemaining());
        tag.putString("TargetUUID", (String)this.entityData.get(DATA_TARGET_UUID));
        tag.putString("TargetType", (String)this.entityData.get(DATA_TARGET_TYPE));
    }

    public void onClientRemoval() {
        super.onClientRemoval();
        if (!this.level().isClientSide) return;
        ClientAudioProxy.stopSound((OmegaDeviceEntity)this);
    }
}

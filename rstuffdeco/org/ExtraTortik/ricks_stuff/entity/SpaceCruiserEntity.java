/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.BlockPos$MutableBlockPos
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.core.Holder
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
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
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.server.level.TicketType
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.tags.BlockTags
 *  net.minecraft.util.Mth
 *  net.minecraft.world.Containers
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.damagesource.DamageTypes
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$MoveFunction
 *  net.minecraft.world.entity.Entity$RemovalReason
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.MoverType
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.ChunkPos
 *  net.minecraft.world.level.GameRules
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.Level$ExplosionInteraction
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.portal.DimensionTransition
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  net.neoforged.neoforge.items.ItemStackHandler
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.entity.CruiserWaypoint
 *  org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity$1
 *  org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity$ClientProxy
 *  org.ExtraTortik.ricks_stuff.entity.WarpState
 *  org.ExtraTortik.ricks_stuff.init.ModEntities
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 *  org.ExtraTortik.ricks_stuff.init.ModParticles
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CSyncCruiserLocationsPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CSyncWarpSequencePacket
 *  org.ExtraTortik.ricks_stuff.portal.PortalManager
 *  org.joml.Quaternionf
 *  org.joml.Vector3f
 */
package org.ExtraTortik.ricks_stuff.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.entity.CruiserWaypoint;
import org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity;
import org.ExtraTortik.ricks_stuff.entity.WarpState;
import org.ExtraTortik.ricks_stuff.init.ModEntities;
import org.ExtraTortik.ricks_stuff.init.ModItems;
import org.ExtraTortik.ricks_stuff.init.ModParticles;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.S2CSyncCruiserLocationsPacket;
import org.ExtraTortik.ricks_stuff.network.packet.S2CSyncWarpSequencePacket;
import org.ExtraTortik.ricks_stuff.portal.PortalManager;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/*
 * Exception performing whole class analysis ignored.
 */
public class SpaceCruiserEntity
extends Entity {
    private static final EntityDataAccessor<Boolean> HEADLIGHTS = SynchedEntityData.defineId(SpaceCruiserEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> THROTTLE = SynchedEntityData.defineId(SpaceCruiserEntity.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> STRAFE = SynchedEntityData.defineId(SpaceCruiserEntity.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> VERTICAL = SynchedEntityData.defineId(SpaceCruiserEntity.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> BOOSTING = SynchedEntityData.defineId(SpaceCruiserEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> ROLL = SynchedEntityData.defineId(SpaceCruiserEntity.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> SHIP_HEALTH = SynchedEntityData.defineId(SpaceCruiserEntity.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ENERGY = SynchedEntityData.defineId(SpaceCruiserEntity.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> HOVER_LOCK = SynchedEntityData.defineId(SpaceCruiserEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LANDED = SynchedEntityData.defineId(SpaceCruiserEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ENGINE_ON = SynchedEntityData.defineId(SpaceCruiserEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CRASHING = SynchedEntityData.defineId(SpaceCruiserEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> BROKEN = SynchedEntityData.defineId(SpaceCruiserEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ENGINE_OVERLOADED = SynchedEntityData.defineId(SpaceCruiserEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> WARP_STATE = SynchedEntityData.defineId(SpaceCruiserEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> WARP_TIMER = SynchedEntityData.defineId(SpaceCruiserEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> WARP_TARGET_X = SynchedEntityData.defineId(SpaceCruiserEntity.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> WARP_TARGET_Y = SynchedEntityData.defineId(SpaceCruiserEntity.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> WARP_TARGET_Z = SynchedEntityData.defineId(SpaceCruiserEntity.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<String> WARP_TARGET_DIM = SynchedEntityData.defineId(SpaceCruiserEntity.class, (EntityDataSerializer)EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Optional<UUID>> WARP_PILOT_UUID = SynchedEntityData.defineId(SpaceCruiserEntity.class, (EntityDataSerializer)EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Optional<UUID>> WARP_PASSENGER_UUID = SynchedEntityData.defineId(SpaceCruiserEntity.class, (EntityDataSerializer)EntityDataSerializers.OPTIONAL_UUID);
    private double warpTargetX = 0.0;
    private double warpTargetY = 0.0;
    private double warpTargetZ = 0.0;
    private String warpTargetDim = "";
    private UUID warpPilotUUID = null;
    private UUID warpPassengerUUID = null;
    private ChunkPos warpTargetChunkPos = null;
    private final List<CruiserWaypoint> savedWaypoints = new ArrayList<CruiserWaypoint>();
    public static final int MAX_WAYPOINTS = 30;
    public static final float MAX_HEALTH = 100.0f;
    public static final float MAX_ENERGY = 100.0f;
    public static final Vec3 PILOT_SEAT = new Vec3(0.9375, 1.28, 0.075);
    public static final Vec3 PASSENGER_SEAT = new Vec3(-0.73125, 1.28, 0.075);
    public static final Vec3 HEADLIGHT_LEFT = new Vec3(-1.125, 1.35, 3.15);
    public static final Vec3 HEADLIGHT_RIGHT = new Vec3(1.6875, 1.35, 3.15);
    public static final Vec3 THRUSTER_RIGHT = new Vec3(3.61875, 1.575, -0.15);
    public static final Vec3 THRUSTER_LEFT = new Vec3(-3.8625, 1.575, -0.15);
    public static final Vec3 TRASH_CAN_RIGHT_TOP = new Vec3(3.12, 2.08, 0.55);
    public static final Vec3 TRASH_CAN_LEFT_TOP = new Vec3(-3.2, 2.08, 0.55);
    public static final Vec3 REAR_HOOD_DECK = new Vec3(0.0, 1.28, -2.15);
    public static final Vec3 WHEEL_FL = new Vec3(-1.73, 0.0, 1.4);
    public static final Vec3 WHEEL_FR = new Vec3(1.76, 0.0, 1.4);
    public static final Vec3 WHEEL_RL = new Vec3(-1.73, 0.0, -1.5);
    public static final Vec3 WHEEL_RR = new Vec3(1.78, 0.0, -1.5);
    private final ItemStackHandler inventory = new ItemStackHandler(27);
    private float clientRoll = 0.0f;
    private float prevRoll = 0.0f;
    private float targetRoll = 0.0f;
    private float inputThrottle = 0.0f;
    private float inputStrafe = 0.0f;
    private float inputVertical = 0.0f;
    private boolean inputBoost = false;
    private float yawVelocity = 0.0f;
    private float pitchVelocity = 0.0f;
    private Vec3 prevVelocity = Vec3.ZERO;
    private float prevFlameIntensity = 0.0f;
    private float clientFlameIntensity = 0.0f;
    private float prevBoostIntensity = 0.0f;
    private float clientBoostIntensity = 0.0f;
    private float prevReverseFlame = 0.0f;
    private float clientReverseFlame = 0.0f;
    private float prevSteerDiff = 0.0f;
    private float clientSteerDiff = 0.0f;
    private int engineOverloadTicks = 0;
    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYRot;
    private double lerpXRot;

    public SpaceCruiserEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.blocksBuilding = true;
    }

    public SpaceCruiserEntity(Level level, double x, double y, double z) {
        this((EntityType)ModEntities.SPACE_CRUISER.get(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(HEADLIGHTS, (Object)false);
        builder.define(THROTTLE, (Object)Float.valueOf(0.0f));
        builder.define(STRAFE, (Object)Float.valueOf(0.0f));
        builder.define(VERTICAL, (Object)Float.valueOf(0.0f));
        builder.define(BOOSTING, (Object)false);
        builder.define(ROLL, (Object)Float.valueOf(0.0f));
        builder.define(SHIP_HEALTH, (Object)Float.valueOf(100.0f));
        builder.define(ENERGY, (Object)Float.valueOf(100.0f));
        builder.define(HOVER_LOCK, (Object)false);
        builder.define(LANDED, (Object)true);
        builder.define(ENGINE_ON, (Object)false);
        builder.define(CRASHING, (Object)false);
        builder.define(BROKEN, (Object)false);
        builder.define(ENGINE_OVERLOADED, (Object)false);
        builder.define(WARP_STATE, (Object)WarpState.IDLE.getId());
        builder.define(WARP_TIMER, (Object)0);
        builder.define(WARP_TARGET_X, (Object)Float.valueOf(0.0f));
        builder.define(WARP_TARGET_Y, (Object)Float.valueOf(0.0f));
        builder.define(WARP_TARGET_Z, (Object)Float.valueOf(0.0f));
        builder.define(WARP_TARGET_DIM, (Object)"");
        builder.define(WARP_PILOT_UUID, Optional.empty());
        builder.define(WARP_PASSENGER_UUID, Optional.empty());
    }

    public void tick() {
        super.tick();
        this.resetFallDistance();
        for (Entity passenger : this.getPassengers()) {
            passenger.resetFallDistance();
            if (!(passenger instanceof LivingEntity)) continue;
            LivingEntity living = (LivingEntity)passenger;
            living.setAirSupply(living.getMaxAirSupply());
            living.setTicksFrozen(0);
        }
        if (this.engineOverloadTicks > 0) {
            --this.engineOverloadTicks;
        }
        if (!this.level().isClientSide) {
            this.entityData.set(ENGINE_OVERLOADED, (Object)(this.engineOverloadTicks > 0 ? 1 : 0));
            boolean isBoosting = this.isBoosting() && this.isEngineOn() && this.getControllingPassenger() != null;
            float currentEnergy = this.getEnergy();
            if (isBoosting && currentEnergy > 0.0f) {
                this.setEnergy(Math.max(0.0f, currentEnergy - (float)Config.spaceCruiserEnergyDrainBoost));
            } else if (currentEnergy < 100.0f && !this.isBroken() && !this.isCrashing()) {
                this.setEnergy(Math.min(100.0f, currentEnergy + 0.4f));
            }
        }
        this.prevRoll = this.clientRoll;
        if (this.getHealth() <= 50.0f && !this.isBroken() && this.isAlive() && this.level().isClientSide && this.random.nextFloat() < 0.65f) {
            Vec3 hood = this.getTransformedRearDeck();
            this.level().addParticle((ParticleOptions)ParticleTypes.CAMPFIRE_COSY_SMOKE, hood.x + (this.random.nextDouble() - 0.5) * 0.5, hood.y + 0.2, hood.z + (this.random.nextDouble() - 0.5) * 0.5, 0.0, 0.05, 0.0);
        }
        if (this.level().isClientSide) {
            this.prevFlameIntensity = this.clientFlameIntensity;
            this.prevBoostIntensity = this.clientBoostIntensity;
            this.prevReverseFlame = this.clientReverseFlame;
            this.prevSteerDiff = this.clientSteerDiff;
            this.updateClientFlameIntensity();
            float target = this.getRoll();
            this.clientRoll = Mth.rotLerp((float)0.25f, (float)this.clientRoll, (float)target);
            if (this.isControlledByLocalInstance()) {
                Vec3 preFlightMotion;
                this.lerpSteps = 0;
                this.syncPacketPositionCodec(this.getX(), this.getY(), this.getZ());
                this.prevVelocity = preFlightMotion = this.getDeltaMovement();
                this.tickFlightMovement();
                Vec3 plannedMotion = this.getDeltaMovement();
                this.move(MoverType.SELF, plannedMotion);
                Vec3 postMoveMotion = this.getDeltaMovement();
                double clientSuddenDecel = plannedMotion.subtract(postMoveMotion).length();
                if (!(clientSuddenDecel >= 0.25)) return;
                if (!(plannedMotion.length() >= 0.35)) return;
                float finalShake = (float)clientSuddenDecel;
                if (!this.level().isClientSide()) return;
                ClientProxy.triggerImpactShake((float)finalShake);
            } else {
                if (this.lerpSteps <= 0) return;
                double dX = this.getX() + (this.lerpX - this.getX()) / (double)this.lerpSteps;
                double dY = this.getY() + (this.lerpY - this.getY()) / (double)this.lerpSteps;
                double dZ = this.getZ() + (this.lerpZ - this.getZ()) / (double)this.lerpSteps;
                float newYaw = (float)((double)this.getYRot() + Mth.wrapDegrees((double)(this.lerpYRot - (double)this.getYRot())) / (double)this.lerpSteps);
                float newPitch = (float)((double)this.getXRot() + (this.lerpXRot - (double)this.getXRot()) / (double)this.lerpSteps);
                --this.lerpSteps;
                this.setPos(dX, dY, dZ);
                this.setRot(newYaw, newPitch);
            }
        } else {
            if (this.getWarpStateEnum().isWarping()) {
                this.tickWarpStateMachine();
            }
            this.prevVelocity = this.getDeltaMovement();
            if (this.getControllingPassenger() instanceof Player) {
                this.tickFlightMovement();
                if (!this.isWarpActive()) return;
                this.move(MoverType.SELF, this.getDeltaMovement());
            } else {
                this.tickFlightMovement();
                this.move(MoverType.SELF, this.getDeltaMovement());
            }
        }
    }

    private void updateClientFlameIntensity() {
        if (!this.isEngineOn() || this.engineOverloadTicks > 0) {
            this.clientFlameIntensity = Mth.approach((float)this.clientFlameIntensity, (float)0.0f, (float)0.06f);
            this.clientBoostIntensity = Mth.approach((float)this.clientBoostIntensity, (float)0.0f, (float)0.12f);
            this.clientReverseFlame = Mth.approach((float)this.clientReverseFlame, (float)0.0f, (float)0.1f);
            this.clientSteerDiff = Mth.approach((float)this.clientSteerDiff, (float)0.0f, (float)0.15f);
            return;
        }
        float throttle = this.getThrottle();
        float vertical = this.getVertical();
        float strafe = this.getStrafe();
        boolean boosting = this.isBoosting() && this.getEnergy() > 0.0f;
        boolean isHovering = this.isHoverLock() && !this.onGround();
        float speed = (float)this.getDeltaMovement().length();
        boolean isIdle = Math.abs(throttle) <= 0.03f && vertical <= 0.03f && speed < 0.06f && !isHovering;
        float targetBase = 0.0f;
        if (!isIdle) {
            targetBase = isHovering ? 0.35f : (throttle > 0.05f ? 0.55f + Math.min(0.45f, throttle * 0.45f) : (vertical > 0.05f ? 0.45f + Math.min(0.35f, vertical * 0.35f) : 0.2f + Math.min(0.4f, speed * 0.3f)));
        }
        float spoolRate = targetBase > this.clientFlameIntensity ? 0.14f : 0.08f;
        this.clientFlameIntensity = Mth.lerp((float)spoolRate, (float)this.clientFlameIntensity, (float)targetBase);
        float targetRev = throttle < -0.05f ? Math.min(1.0f, Math.abs(throttle) * (boosting ? 1.0f : 0.75f)) : 0.0f;
        float revSpoolRate = targetRev > this.clientReverseFlame ? 0.22f : 0.14f;
        this.clientReverseFlame = Mth.lerp((float)revSpoolRate, (float)this.clientReverseFlame, (float)targetRev);
        float targetSteerDiff = Math.abs(strafe) > 0.05f && !isIdle ? strafe * 0.45f : 0.0f;
        this.clientSteerDiff = Mth.lerp((float)0.2f, (float)this.clientSteerDiff, (float)targetSteerDiff);
        boolean hasBoostInput = boosting && this.isEngineOn();
        boolean isActivelyAccelerating = throttle > 0.05f || vertical > 0.05f || speed > 0.28f && !isIdle;
        float targetBoost = hasBoostInput && isActivelyAccelerating ? 1.0f : 0.0f;
        float boostSpoolRate = targetBoost > this.clientBoostIntensity ? 0.18f : 0.12f;
        this.clientBoostIntensity = Mth.lerp((float)boostSpoolRate, (float)this.clientBoostIntensity, (float)targetBoost);
    }

    private void tickFlightMovement() {
        LivingEntity controller = this.getControllingPassenger();
        if (this.isWarpActive()) {
            this.setLanded(false);
            this.setThrottle(0.0f);
            this.setStrafe(0.0f);
            this.setVertical(0.0f);
            this.setBoosting(false);
            float yawRad = (float)Math.toRadians(this.getYRot());
            float pitchRad = (float)Math.toRadians(this.getXRot());
            Vec3 forwardDir = new Vec3(-Math.sin(yawRad) * Math.cos(pitchRad), -Math.sin(pitchRad), Math.cos(yawRad) * Math.cos(pitchRad));
            WarpState warpState = this.getWarpStateEnum();
            int warpTimer = this.getWarpTimer();
            switch (1.$SwitchMap$org$ExtraTortik$ricks_stuff$entity$WarpState[warpState.ordinal()]) {
                case 1: {
                    this.setDeltaMovement(Vec3.ZERO);
                    this.setRoll(Mth.approach((float)this.getRoll(), (float)0.0f, (float)3.0f));
                    this.setXRot(Mth.approach((float)this.getXRot(), (float)0.0f, (float)2.5f));
                    break;
                }
                case 2: {
                    this.setDeltaMovement(forwardDir.scale(0.05));
                    this.setRoll(Mth.approach((float)this.getRoll(), (float)0.0f, (float)3.0f));
                    this.setXRot(Mth.approach((float)this.getXRot(), (float)0.0f, (float)2.5f));
                    break;
                }
                case 3: {
                    float progress = Math.min(1.0f, (float)warpTimer / (float)Math.max(1, WarpState.ENTERING.getDurationTicks()));
                    double speed = Mth.lerp((double)progress, (double)0.1, (double)2.5);
                    this.setDeltaMovement(forwardDir.scale(speed));
                    this.setRoll(Mth.approach((float)this.getRoll(), (float)0.0f, (float)4.0f));
                    break;
                }
                case 4: {
                    this.setDeltaMovement(forwardDir.scale(1.5));
                    this.setRoll(Mth.approach((float)this.getRoll(), (float)0.0f, (float)4.0f));
                    break;
                }
                case 5: {
                    float progress = Math.min(1.0f, (float)warpTimer / (float)Math.max(1, WarpState.EXITING.getDurationTicks()));
                    double speed = Mth.lerp((double)progress, (double)2.0, (double)0.3);
                    this.setDeltaMovement(forwardDir.scale(speed));
                    this.setRoll(Mth.approach((float)this.getRoll(), (float)0.0f, (float)3.0f));
                    break;
                }
            }
            return;
        }
        if (controller instanceof Player) {
            boolean engineOn;
            Player player = (Player)controller;
            float throttle = ((Float)this.entityData.get(THROTTLE)).floatValue();
            float strafe = ((Float)this.entityData.get(STRAFE)).floatValue();
            float vertical = ((Float)this.entityData.get(VERTICAL)).floatValue();
            boolean boosting = (Boolean)this.entityData.get(BOOSTING);
            boolean inWater = this.isInWater() || this.isUnderWater();
            boolean inLava = this.isInLava();
            boolean isCriticallyDamaged = this.isCrashing() || this.isBroken() || this.getHealth() <= 0.0f;
            boolean bl = engineOn = !isCriticallyDamaged && this.isEngineOn() && this.engineOverloadTicks <= 0;
            if (this.isLanded() || this.isBroken()) {
                boolean leftGround;
                boolean takeoffCommand = !isCriticallyDamaged && (vertical > 0.15f || engineOn && throttle > 0.1f || Math.toRadians(this.getXRot()) < -0.08);
                boolean bl2 = leftGround = !this.onGround() && !inWater;
                if (takeoffCommand || leftGround && !this.isBroken()) {
                    this.setLanded(false);
                    if (vertical > 0.15f && engineOn) {
                        this.setDeltaMovement(this.getDeltaMovement().x, Math.max(this.getDeltaMovement().y, 0.45), this.getDeltaMovement().z);
                    }
                }
            } else if ((this.onGround() || inWater) && vertical <= 0.05f && this.getDeltaMovement().y <= 0.08) {
                Object leftGround;
                if (this.isCrashing() && !this.level().isClientSide && (leftGround = this.level()) instanceof ServerLevel) {
                    ServerLevel serverLevel = (ServerLevel)leftGround;
                    if (Config.spaceCruiserExplosionOnDeath && !this.isImpactCushioned()) {
                        this.handleCatastrophicExplosion(serverLevel);
                        return;
                    }
                    this.setCrashing(false);
                    this.setBroken(true);
                    this.setLanded(true);
                    this.setEngineOn(false);
                    this.setHoverLock(false);
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.SPACE_CRUISER_CRASH.get(), SoundSource.NEUTRAL, 1.5f, 0.9f);
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (Holder)SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 0.8f, 1.1f);
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ANVIL_LAND, SoundSource.NEUTRAL, 1.0f, 0.6f);
                    serverLevel.sendParticles((ParticleOptions)ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY() + 0.5, this.getZ(), 32, 2.0, 0.8, 2.0, 0.06);
                    serverLevel.sendParticles((ParticleOptions)ParticleTypes.EXPLOSION, this.getX(), this.getY() + 0.5, this.getZ(), 4, 1.5, 0.5, 1.5, 0.0);
                    for (Entity passenger : this.getPassengers()) {
                        if (!(passenger instanceof LivingEntity)) continue;
                        LivingEntity living = (LivingEntity)passenger;
                        living.hurt(this.damageSources().generic(), 12.0f);
                        living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1));
                        living.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60, 0));
                    }
                }
                this.setLanded(true);
            }
            float energy = this.getEnergy();
            if (inLava && !this.level().isClientSide && this.tickCount % 10 == 0) {
                this.hurt(this.damageSources().lava(), 2.0f);
            }
            float yawRad = (float)Math.toRadians(this.getYRot());
            float pitchRad = (float)Math.toRadians(this.getXRot());
            Vec3 curMotion = this.getDeltaMovement();
            if (this.isLanded()) {
                double maxRevSpeed;
                Vec3 wPosFL = this.position().add(SpaceCruiserEntity.rotateVector(WHEEL_FL, this.getYRot(), 0.0f, 0.0f));
                Vec3 wPosFR = this.position().add(SpaceCruiserEntity.rotateVector(WHEEL_FR, this.getYRot(), 0.0f, 0.0f));
                Vec3 wPosRL = this.position().add(SpaceCruiserEntity.rotateVector(WHEEL_RL, this.getYRot(), 0.0f, 0.0f));
                Vec3 wPosRR = this.position().add(SpaceCruiserEntity.rotateVector(WHEEL_RR, this.getYRot(), 0.0f, 0.0f));
                double hFL = this.getGroundHeightAt(this.level(), wPosFL.x, this.getY(), wPosFL.z);
                double hFR = this.getGroundHeightAt(this.level(), wPosFR.x, this.getY(), wPosFR.z);
                double hRL = this.getGroundHeightAt(this.level(), wPosRL.x, this.getY(), wPosRL.z);
                double hRR = this.getGroundHeightAt(this.level(), wPosRR.x, this.getY(), wPosRR.z);
                double hFront = (hFL + hFR) * 0.5;
                double hRear = (hRL + hRR) * 0.5;
                double hLeft = (hFL + hRL) * 0.5;
                double hRight = (hFR + hRR) * 0.5;
                double wheelbase = 2.9;
                double trackWidth = 3.5;
                float targetPitch = (float)Math.toDegrees(-Math.atan2(hFront - hRear, wheelbase));
                float targetRoll = (float)Math.toDegrees(Math.atan2(hRight - hLeft, trackWidth));
                float speedKmh = (float)(curMotion.horizontalDistance() * 72.0);
                float turnRoll = -this.yawVelocity * 0.45f * Math.min(1.0f, speedKmh / 20.0f);
                targetRoll += turnRoll;
                if (throttle > 0.1f) {
                    targetPitch -= throttle * 1.5f;
                } else if (throttle < -0.1f) {
                    targetPitch += Math.abs(throttle) * 2.5f;
                }
                targetPitch = Mth.clamp((float)targetPitch, (float)-35.0f, (float)35.0f);
                targetRoll = Mth.clamp((float)targetRoll, (float)-35.0f, (float)35.0f);
                float suspensionSpeed = 0.32f;
                this.setXRot(Mth.rotLerp((float)suspensionSpeed, (float)this.getXRot(), (float)targetPitch));
                this.setRoll(Mth.rotLerp((float)suspensionSpeed, (float)this.getRoll(), (float)targetRoll));
                double maxWheelHeight = Math.max(Math.max(hFL, hFR), Math.max(hRL, hRR));
                double stepDelta = maxWheelHeight - this.getY();
                double vertMot = -0.08;
                if (stepDelta > 0.06 && stepDelta <= 1.15 && Math.abs(throttle) > 0.05f) {
                    vertMot = Math.min(0.32, stepDelta * 0.42 + 0.05);
                    if (this.level().isClientSide && stepDelta > 0.25 && this.tickCount % 4 == 0) {
                        this.level().addParticle((ParticleOptions)ParticleTypes.POOF, this.getX(), this.getY() + 0.1, this.getZ(), 0.0, 0.02, 0.0);
                    }
                }
                double fwUnitX = -Math.sin(yawRad);
                double fwUnitZ = Math.cos(yawRad);
                double rightUnitX = -Math.cos(yawRad);
                double rightUnitZ = -Math.sin(yawRad);
                double vFw = curMotion.x * fwUnitX + curMotion.z * fwUnitZ;
                double vLat = curMotion.x * rightUnitX + curMotion.z * rightUnitZ;
                double maxFwSpeed = engineOn && boosting && energy > 0.0f ? 0.95 : (engineOn ? 0.55 : 0.0);
                double d = maxRevSpeed = engineOn ? 0.25 : 0.0;
                if (engineOn && throttle > 0.05f) {
                    double accel = (boosting && energy > 0.0f ? 0.045 : 0.024) * (double)throttle * Math.max(0.12, 1.0 - vFw / maxFwSpeed * 0.7);
                    vFw = Math.min(maxFwSpeed, vFw + accel);
                } else if (engineOn && throttle < -0.05f) {
                    if (vFw > 0.03) {
                        vFw = Math.max(0.0, vFw - 0.038 * (double)Math.abs(throttle));
                    } else {
                        double revAccel = 0.018 * (double)Math.abs(throttle) * Math.max(0.15, 1.0 - Math.abs(vFw) / maxRevSpeed * 0.7);
                        vFw = Math.max(-maxRevSpeed, vFw - revAccel);
                    }
                } else if (Math.abs(vFw *= engineOn ? 0.968 : 0.92) < 0.008) {
                    vFw = 0.0;
                }
                vLat *= 0.65;
                if (Math.abs(vLat) < 0.005) {
                    vLat = 0.0;
                }
                double newMotionX = fwUnitX * vFw + rightUnitX * vLat;
                double newMotionZ = fwUnitZ * vFw + rightUnitZ * vLat;
                this.setDeltaMovement(newMotionX, vertMot, newMotionZ);
            } else {
                Vec3 newMotion;
                double speedMult = Config.spaceCruiserMaxSpeedMultiplier;
                float baseSpeed = (float)((double)(engineOn && boosting && energy > 0.0f ? 2.25f : (engineOn ? 1.15f : 0.0f)) * speedMult);
                float vertSpeed = (float)((double)(engineOn && boosting && energy > 0.0f ? 1.35f : (engineOn ? 0.75f : 0.0f)) * speedMult);
                Vec3 forwardDir = new Vec3(-Math.sin(yawRad) * Math.cos(pitchRad), -Math.sin(pitchRad), Math.cos(yawRad) * Math.cos(pitchRad));
                Vec3 rightDir = new Vec3(-Math.cos(yawRad), 0.0, -Math.sin(yawRad));
                double curSpeed = curMotion.length();
                if (inLava) {
                    double lavaDrag = 0.65;
                    double lavaSink = -0.05;
                    double driveX = engineOn ? forwardDir.x * (double)throttle * (double)baseSpeed * 0.35 + rightDir.x * (double)strafe * (double)baseSpeed * 0.25 : 0.0;
                    double driveY = engineOn ? forwardDir.y * (double)throttle * (double)baseSpeed * 0.35 + (double)(vertical * vertSpeed) * 0.35 : 0.0;
                    double driveZ = engineOn ? forwardDir.z * (double)throttle * (double)baseSpeed * 0.35 + rightDir.z * (double)strafe * (double)baseSpeed * 0.25 : 0.0;
                    newMotion = new Vec3(curMotion.x * lavaDrag + driveX, Math.max(-0.35, curMotion.y * 0.7 + lavaSink + driveY), curMotion.z * lavaDrag + driveZ);
                } else if (inWater) {
                    double wave;
                    double nextVz;
                    double nextVx;
                    double horizSpeed;
                    double waterDrag = 0.8;
                    double maxWaterSpeed = (boosting && energy > 0.0f ? 0.45 : 0.28) * speedMult;
                    double waterAccel = (boosting && energy > 0.0f ? 0.045 : 0.022) * speedMult;
                    double buoyantLift = this.isUnderWater() ? 0.06 : 0.015;
                    double driveX = 0.0;
                    double driveY = 0.0;
                    double driveZ = 0.0;
                    if (engineOn) {
                        if (throttle > 0.05f) {
                            driveX += forwardDir.x * (double)throttle * waterAccel;
                            driveY += forwardDir.y * (double)throttle * waterAccel;
                            driveZ += forwardDir.z * (double)throttle * waterAccel;
                        } else if (throttle < -0.05f) {
                            driveX += forwardDir.x * (double)throttle * waterAccel * 0.6;
                            driveY += forwardDir.y * (double)throttle * waterAccel * 0.6;
                            driveZ += forwardDir.z * (double)throttle * waterAccel * 0.6;
                        }
                        if (Math.abs(strafe) > 0.05f) {
                            driveX += rightDir.x * (double)strafe * waterAccel * 0.7;
                            driveZ += rightDir.z * (double)strafe * waterAccel * 0.7;
                        }
                        if (Math.abs(vertical) > 0.05f) {
                            driveY += (double)vertical * waterAccel * 1.1;
                        }
                    }
                    if ((horizSpeed = Math.sqrt((nextVx = curMotion.x * waterDrag + driveX) * nextVx + (nextVz = curMotion.z * waterDrag + driveZ) * nextVz)) > maxWaterSpeed) {
                        double scale = maxWaterSpeed / horizSpeed;
                        nextVx *= scale;
                        nextVz *= scale;
                    }
                    double nextVy = !this.isUnderWater() && !engineOn && vertical <= 0.0f ? (wave = Math.sin((double)(this.tickCount + this.getId() * 7) * 0.1) * 0.005) : Math.max(-0.4, Math.min(0.22, curMotion.y * 0.8 + buoyantLift + driveY));
                    newMotion = new Vec3(nextVx, nextVy, nextVz);
                    this.setRoll(Mth.approach((float)this.getRoll(), (float)0.0f, (float)3.0f));
                    this.setXRot(Mth.approach((float)this.getXRot(), (float)0.0f, (float)3.0f));
                } else if (engineOn && throttle < -0.05f) {
                    double brakePower = Math.abs(throttle);
                    double brakeDrag = Math.max(0.72, 0.9 - brakePower * 0.18);
                    Vec3 revThrust = forwardDir.scale((double)throttle * (boosting ? 0.35 : 0.18));
                    Vec3 vertThrust = new Vec3(0.0, (double)(vertical * vertSpeed) * 0.6, 0.0);
                    Vec3 strafeThrust = rightDir.scale((double)strafe * (boosting ? 0.45 : 0.25));
                    newMotion = curMotion.scale(brakeDrag).add(revThrust).add(vertThrust).add(strafeThrust);
                } else if (engineOn && this.isHoverLock() && Math.abs(throttle) <= 0.05f && Math.abs(vertical) <= 0.05f) {
                    newMotion = new Vec3(Mth.lerp((double)0.16, (double)curMotion.x, (double)(rightDir.x * (double)strafe * 0.25)), Mth.lerp((double)0.16, (double)curMotion.y, (double)0.0), Mth.lerp((double)0.16, (double)curMotion.z, (double)(rightDir.z * (double)strafe * 0.25)));
                } else {
                    Vec3 motionVec = curMotion;
                    if (isCriticallyDamaged) {
                        double glideDrag = 0.988;
                        double newVx = curMotion.x * glideDrag;
                        double newVz = curMotion.z * glideDrag;
                        double newVy = Math.max(-2.45, curMotion.y - 0.065);
                        if (Math.abs(strafe) > 0.05f) {
                            newVx += rightDir.x * (double)strafe * 0.015;
                            newVz += rightDir.z * (double)strafe * 0.015;
                        }
                        double horiz = Math.sqrt(newVx * newVx + newVz * newVz);
                        float targetDivePitch = (float)Math.toDegrees(Math.atan2(-newVy, Math.max(0.12, horiz)));
                        targetDivePitch = Mth.clamp((float)targetDivePitch, (float)22.0f, (float)68.0f);
                        this.setXRot(Mth.approach((float)this.getXRot(), (float)targetDivePitch, (float)2.2f));
                        if ((double)pitchRad > 0.05) {
                            newVx += forwardDir.x * Math.sin(pitchRad) * 0.045;
                            newVz += forwardDir.z * Math.sin(pitchRad) * 0.045;
                        }
                        this.setRoll(Mth.approach((float)this.getRoll(), (float)((float)Math.sin((double)this.tickCount * 0.15) * 22.0f), (float)1.5f));
                        newMotion = new Vec3(newVx, newVy, newVz);
                    } else {
                        if (curSpeed > 0.04) {
                            Vec3 motionDir = curMotion.scale(1.0 / curSpeed);
                            Vec3 steerTarget = forwardDir;
                            if (engineOn && Math.abs(strafe) > 0.05f) {
                                steerTarget = steerTarget.add(rightDir.scale((double)strafe * 0.35)).normalize();
                            }
                            double steerRate = engineOn && throttle > 0.05f ? 0.22 : 0.14;
                            Vec3 steeredDir = motionDir.lerp(steerTarget, steerRate).normalize();
                            motionVec = steeredDir.scale(curSpeed);
                        }
                        if ((double)pitchRad > 0.05) {
                            double diveAcc = Math.sin(pitchRad) * (boosting ? 0.095 : 0.075);
                            motionVec = motionVec.add(forwardDir.scale(diveAcc));
                        } else if ((double)pitchRad < -0.08) {
                            double climbDecel = Math.sin(-pitchRad) * 0.04;
                            motionVec = motionVec.add(0.0, -climbDecel, 0.0);
                        } else if (!engineOn || Math.abs(throttle) <= 0.05f && vertical <= 0.05f) {
                            motionVec = motionVec.add(0.0, -0.035, 0.0);
                        }
                        if (engineOn && throttle > 0.05f) {
                            double maxLevelSpeed = (boosting && energy > 0.0f ? 2.25 : 1.15) * speedMult;
                            double baseAccel = (boosting && energy > 0.0f ? 0.075 : 0.042) * speedMult;
                            double speedRatio = motionVec.length() / Math.max(0.1, maxLevelSpeed);
                            double torqueCurve = Math.max(0.3, 1.0 - speedRatio * 0.6);
                            double forwardAccel = baseAccel * (double)throttle * torqueCurve;
                            motionVec = motionVec.add(forwardDir.scale(forwardAccel));
                        }
                        if (engineOn && Math.abs(vertical) > 0.05f) {
                            double vAccel = (boosting && energy > 0.0f ? 0.07 : 0.042) * (double)vertical;
                            motionVec = motionVec.add(0.0, vAccel, 0.0);
                        }
                        if (engineOn && Math.abs(strafe) > 0.05f) {
                            double sAccel = (boosting && energy > 0.0f ? 0.055 : 0.032) * (double)strafe;
                            motionVec = motionVec.add(rightDir.scale(sAccel));
                        }
                        double airDrag = 0.992;
                        double updatedSpeed = motionVec.length();
                        if (updatedSpeed > 2.0) {
                            double highSpeedFactor = Math.max(0.93, 1.0 - (updatedSpeed - 2.0) * 0.015);
                            airDrag *= highSpeedFactor;
                        }
                        newMotion = motionVec = motionVec.scale(airDrag);
                    }
                }
                this.setDeltaMovement(newMotion);
                if (Math.abs(this.getRoll()) < 65.0f && strafe != 0.0f) {
                    float bankRoll = strafe * 20.0f + this.yawVelocity * 1.3f;
                    this.setRoll(Mth.approach((float)this.getRoll(), (float)bankRoll, (float)3.5f));
                }
            }
            if (this.level().isClientSide) return;
            double speedSq = this.getDeltaMovement().lengthSqr();
            if (speedSq > 0.3) {
                AABB ramBox = this.getBoundingBox().inflate(0.5);
                List targets = this.level().getEntitiesOfClass(LivingEntity.class, ramBox, e -> e != player && !this.hasPassenger((Entity)e));
                for (LivingEntity target : targets) {
                    float ramDamage = (float)(Math.sqrt(speedSq) * 12.0 * Config.spaceCruiserRamDamageMultiplier);
                    target.hurt(this.damageSources().mobAttack((LivingEntity)player), ramDamage);
                }
            }
            this.tickBlockDestruction(this.getDeltaMovement(), player);
        } else {
            this.setThrottle(0.0f);
            this.setStrafe(0.0f);
            this.setVertical(0.0f);
            this.setBoosting(false);
            Vec3 motion = this.getDeltaMovement();
            boolean inWater = this.isInWater();
            if (inWater) {
                double waterDragH = 0.82;
                double waterDragV = 0.88;
                double buoyantLift = 0.035;
                this.setRoll(Mth.approach((float)this.getRoll(), (float)0.0f, (float)4.0f));
                this.setXRot(Mth.approach((float)this.getXRot(), (float)0.0f, (float)4.0f));
                if (this.isUnderWater()) {
                    motion = new Vec3(motion.x * waterDragH, Math.min(0.18, motion.y * waterDragV + buoyantLift), motion.z * waterDragH);
                } else {
                    if (this.isCrashing() || this.getHealth() <= 0.0f) {
                        this.setCrashing(false);
                        this.setBroken(true);
                    }
                    this.setLanded(true);
                }
                this.setDeltaMovement(motion);
            } else if (this.onGround() || this.isLanded() && !this.isHoverLock() && this.getDeltaMovement().y > -0.2) {
                ServerLevel serverLevel;
                Level level;
                double impactSpeed = -motion.y;
                if (this.isCrashing() || this.getHealth() <= 0.0f) {
                    if (!this.level().isClientSide && (level = this.level()) instanceof ServerLevel) {
                        serverLevel = (ServerLevel)level;
                        if (Config.spaceCruiserExplosionOnDeath && !this.isImpactCushioned() && (impactSpeed >= 0.5 || this.isCrashing())) {
                            this.handleCatastrophicExplosion(serverLevel);
                            return;
                        }
                    }
                    this.setCrashing(false);
                    this.setBroken(true);
                }
                if (!this.isLanded() && impactSpeed > 0.12) {
                    if (impactSpeed >= 0.6) {
                        if (!this.level().isClientSide && (level = this.level()) instanceof ServerLevel) {
                            serverLevel = (ServerLevel)level;
                            if (Config.spaceCruiserExplosionOnDeath && !this.isImpactCushioned()) {
                                this.handleCatastrophicExplosion(serverLevel);
                                return;
                            }
                            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.SPACE_CRUISER_CRASH.get(), SoundSource.NEUTRAL, 1.2f, 1.0f);
                            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (Holder)SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 0.75f, 1.2f);
                            serverLevel.sendParticles((ParticleOptions)ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY() + 0.5, this.getZ(), 24, 2.0, 0.5, 2.0, 0.06);
                            serverLevel.sendParticles((ParticleOptions)ParticleTypes.EXPLOSION, this.getX(), this.getY() + 0.5, this.getZ(), 3, 1.0, 0.5, 1.0, 0.0);
                        }
                        this.setDeltaMovement(motion.x * 0.45, impactSpeed * 0.25, motion.z * 0.45);
                        this.setRoll(Mth.approach((float)this.getRoll(), (float)0.0f, (float)20.0f));
                        this.setXRot(Mth.approach((float)this.getXRot(), (float)0.0f, (float)20.0f));
                    } else if (impactSpeed >= 0.25) {
                        if (!this.level().isClientSide) {
                            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.SPACE_CRUISER_BUMP.get(), SoundSource.NEUTRAL, 1.0f, 1.0f);
                            level = this.level();
                            if (level instanceof ServerLevel) {
                                serverLevel = (ServerLevel)level;
                                serverLevel.sendParticles((ParticleOptions)ParticleTypes.POOF, this.getX(), this.getY() + 0.2, this.getZ(), 16, 1.5, 0.2, 1.5, 0.05);
                            }
                            float crashDamage = (float)(impactSpeed * 30.0);
                            this.hurt(this.damageSources().fall(), crashDamage);
                        }
                        this.setDeltaMovement(motion.x * 0.6, impactSpeed * 0.18, motion.z * 0.6);
                        this.setRoll(Mth.approach((float)this.getRoll(), (float)0.0f, (float)15.0f));
                        this.setXRot(Mth.approach((float)this.getXRot(), (float)0.0f, (float)15.0f));
                    } else {
                        if (!this.level().isClientSide) {
                            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.SPACE_CRUISER_LAND.get(), SoundSource.NEUTRAL, 0.9f, 1.0f);
                        }
                        this.setLanded(true);
                        this.setDeltaMovement(motion.multiply(0.75, 0.0, 0.75));
                        this.setXRot(0.0f);
                        this.xRotO = 0.0f;
                        this.setRoll(0.0f);
                    }
                } else {
                    this.setLanded(true);
                    double mx = motion.x * 0.7;
                    double mz = motion.z * 0.7;
                    if (Math.abs(mx) < 0.005) {
                        mx = 0.0;
                    }
                    if (Math.abs(mz) < 0.005) {
                        mz = 0.0;
                    }
                    this.setDeltaMovement(mx, -0.05, mz);
                    Vec3 wPosFL = this.position().add(SpaceCruiserEntity.rotateVector(WHEEL_FL, this.getYRot(), 0.0f, 0.0f));
                    Vec3 wPosFR = this.position().add(SpaceCruiserEntity.rotateVector(WHEEL_FR, this.getYRot(), 0.0f, 0.0f));
                    Vec3 wPosRL = this.position().add(SpaceCruiserEntity.rotateVector(WHEEL_RL, this.getYRot(), 0.0f, 0.0f));
                    Vec3 wPosRR = this.position().add(SpaceCruiserEntity.rotateVector(WHEEL_RR, this.getYRot(), 0.0f, 0.0f));
                    double hFL = this.getGroundHeightAt(this.level(), wPosFL.x, this.getY(), wPosFL.z);
                    double hFR = this.getGroundHeightAt(this.level(), wPosFR.x, this.getY(), wPosFR.z);
                    double hRL = this.getGroundHeightAt(this.level(), wPosRL.x, this.getY(), wPosRL.z);
                    double hRR = this.getGroundHeightAt(this.level(), wPosRR.x, this.getY(), wPosRR.z);
                    double hFront = (hFL + hFR) * 0.5;
                    double hRear = (hRL + hRR) * 0.5;
                    double hLeft = (hFL + hRL) * 0.5;
                    double hRight = (hFR + hRR) * 0.5;
                    float targetPitch = (float)Math.toDegrees(-Math.atan2(hFront - hRear, 2.9));
                    float targetRoll = (float)Math.toDegrees(Math.atan2(hRight - hLeft, 3.5));
                    targetPitch = Mth.clamp((float)targetPitch, (float)-35.0f, (float)35.0f);
                    targetRoll = Mth.clamp((float)targetRoll, (float)-35.0f, (float)35.0f);
                    if (Math.abs(targetPitch - this.getXRot()) > 0.4f) {
                        this.setXRot(Mth.approach((float)this.getXRot(), (float)targetPitch, (float)2.5f));
                    }
                    if (!(Math.abs(targetRoll - this.getRoll()) > 0.4f)) return;
                    this.setRoll(Mth.approach((float)this.getRoll(), (float)targetRoll, (float)2.5f));
                }
            } else if (this.isHoverLock()) {
                double bob = Math.sin((double)(this.tickCount + this.getId() * 10) * 0.08) * 0.008;
                this.setDeltaMovement(motion.x * 0.88, bob, motion.z * 0.88);
                this.setRoll(Mth.approach((float)this.getRoll(), (float)0.0f, (float)3.0f));
                this.setXRot(Mth.approach((float)this.getXRot(), (float)0.0f, (float)2.5f));
                this.yawVelocity *= 0.85f;
                this.pitchVelocity *= 0.85f;
            } else {
                this.setLanded(false);
                double airDrag = 0.988;
                double newVx = motion.x * airDrag;
                double newVz = motion.z * airDrag;
                double newVy = Math.max(-1.85, motion.y - 0.065);
                this.setDeltaMovement(newVx, newVy, newVz);
                this.setRoll(Mth.approach((float)this.getRoll(), (float)0.0f, (float)2.2f));
                double totalHoriz = Math.sqrt(newVx * newVx + newVz * newVz);
                float targetPitch = (float)Math.toDegrees(Math.atan2(-newVy, Math.max(0.12, totalHoriz)));
                targetPitch = Mth.clamp((float)targetPitch, (float)18.0f, (float)65.0f);
                this.setXRot(Mth.approach((float)this.getXRot(), (float)targetPitch, (float)2.2f));
                if (totalHoriz > 0.35) {
                    float targetYaw = (float)Math.toDegrees(Math.atan2(-newVx, newVz));
                    this.setYRot(Mth.rotLerp((float)0.08f, (float)this.getYRot(), (float)targetYaw));
                }
                this.tickBlockDestruction(this.getDeltaMovement(), null);
            }
        }
    }

    private static boolean isFragileBlock(BlockState state) {
        return state.is(BlockTags.LEAVES) || state.is(BlockTags.FLOWERS) || state.is(BlockTags.CROPS) || state.is(BlockTags.SAPLINGS) || state.is(BlockTags.FIRE) || state.is(BlockTags.UNDERWATER_BONEMEALS) || state.is(BlockTags.ALL_SIGNS) || state.is(BlockTags.BANNERS) || state.is(Blocks.GLASS_PANE) || state.is(Blocks.COBWEB) || state.is(Blocks.SWEET_BERRY_BUSH) || state.is(Blocks.SUGAR_CANE) || state.is(Blocks.DEAD_BUSH) || state.is(Blocks.LILY_PAD) || state.is(Blocks.FERN) || state.is(Blocks.LARGE_FERN) || state.is(Blocks.SHORT_GRASS) || state.is(Blocks.TALL_GRASS) || state.is(Blocks.SEAGRASS) || state.is(Blocks.TALL_SEAGRASS) || state.is(Blocks.KELP) || state.is(Blocks.KELP_PLANT) || state.is(Blocks.VINE) || state.is(Blocks.GLOW_LICHEN) || state.is(Blocks.HANGING_ROOTS) || state.is(Blocks.SPORE_BLOSSOM) || state.is(Blocks.BIG_DRIPLEAF) || state.is(Blocks.BIG_DRIPLEAF_STEM) || state.is(Blocks.SMALL_DRIPLEAF) || state.is(Blocks.SCULK_VEIN) || state.is(Blocks.SNOW);
    }

    private void tickBlockDestruction(Vec3 curMotion, @Nullable Player pilot) {
        AABB sweptBox;
        boolean canGrief;
        if (this.level().isClientSide) {
            return;
        }
        Level level = this.level();
        if (!(level instanceof ServerLevel)) return;
        ServerLevel serverLevel = (ServerLevel)level;
        double speed = curMotion.length();
        if (speed < 0.35) {
            return;
        }
        if (!Config.spaceCruiserBlockBreaking) {
            return;
        }
        boolean bl = canGrief = serverLevel.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) || pilot != null;
        if (!canGrief) {
            return;
        }
        boolean canBreakSolid = this.isBoosting() && speed >= 1.3 && this.getEnergy() >= 15.0f && this.engineOverloadTicks <= 0 && Config.spaceCruiserBreakSolidBlocks;
        float maxAllowedHardness = canBreakSolid ? (float)Config.spaceCruiserMaxBlockHardness : 1.0f;
        Vec3 stepVec = curMotion.scale(1.15);
        if (this.isCrashing()) {
            AABB noseBox = new AABB(this.getX() - 1.8, this.getY() - 0.2, this.getZ() - 1.8, this.getX() + 1.8, this.getY() + 2.2, this.getZ() + 1.8);
            sweptBox = noseBox.minmax(noseBox.move(stepVec));
        } else {
            AABB currentBox = this.getBoundingBox().inflate(0.1);
            sweptBox = currentBox.minmax(currentBox.move(stepVec));
        }
        int minX = Mth.floor((double)sweptBox.minX);
        int minY = Mth.floor((double)sweptBox.minY);
        int minZ = Mth.floor((double)sweptBox.minZ);
        int maxX = Mth.floor((double)sweptBox.maxX);
        int maxY = Mth.floor((double)sweptBox.maxY);
        int maxZ = Mth.floor((double)sweptBox.maxZ);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        int fragileBroken = 0;
        int solidBroken = 0;
        int MAX_FRAGILE_PER_TICK = 16;
        int MAX_SOLID_PER_TICK = 4;
        int x = minX;
        block0: while (true) {
            if (x > maxX) {
                int totalBroken = fragileBroken + solidBroken;
                if (totalBroken <= 0) return;
                if (solidBroken > 0) {
                    double dragFactor = Math.max(0.1, 0.25 - (double)solidBroken * 0.05);
                    this.setDeltaMovement(curMotion.scale(dragFactor));
                    float energyCost = 25.0f + (float)solidBroken * 8.0f;
                    float currentEnergy = this.getEnergy();
                    this.setEnergy(Math.max(0.0f, currentEnergy - energyCost));
                    this.engineOverloadTicks = 35;
                    float rawDamage = 16.0f + (float)solidBroken * 6.0f;
                    if (currentEnergy > 0.0f) {
                        float absorbed = Math.min(currentEnergy, rawDamage * 1.5f);
                        rawDamage -= absorbed * 0.6f;
                    }
                    if (rawDamage > 0.0f) {
                        this.hurt(this.damageSources().flyIntoWall(), rawDamage);
                    }
                    serverLevel.playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.SPACE_CRUISER_CRASH.get(), SoundSource.NEUTRAL, 1.3f, 1.0f);
                    serverLevel.playSound(null, this.getX(), this.getY(), this.getZ(), (Holder)SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 0.8f, 1.2f);
                    serverLevel.sendParticles((ParticleOptions)ParticleTypes.EXPLOSION, this.getX(), this.getY() + 0.5, this.getZ(), Math.min(solidBroken, 4), 1.5, 0.5, 1.5, 0.0);
                    serverLevel.sendParticles((ParticleOptions)ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY() + 0.5, this.getZ(), solidBroken * 4, 1.2, 0.5, 1.2, 0.06);
                } else {
                    double dragFactor = Math.max(0.85, 1.0 - (double)fragileBroken * 0.015);
                    this.setDeltaMovement(curMotion.scale(dragFactor));
                    serverLevel.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.GRASS_BREAK, SoundSource.NEUTRAL, 0.8f, 1.1f);
                }
                return;
            }
            int y = minY;
            while (true) {
                if (y <= maxY) {
                } else {
                    ++x;
                    continue block0;
                }
                for (int z = minZ; z <= maxZ; ++z) {
                    float hardness;
                    pos.set(x, y, z);
                    BlockState state = serverLevel.getBlockState((BlockPos)pos);
                    if (state.isAir() || state.liquid() || (hardness = state.getDestroySpeed((BlockGetter)serverLevel, (BlockPos)pos)) < 0.0f || state.is(BlockTags.WITHER_IMMUNE) || state.is(Blocks.OBSIDIAN) || state.is(Blocks.CRYING_OBSIDIAN) || state.is(Blocks.REINFORCED_DEEPSLATE) || state.is(Blocks.RESPAWN_ANCHOR)) continue;
                    boolean isFragile = SpaceCruiserEntity.isFragileBlock(state);
                    if (isFragile) {
                        if (fragileBroken >= 16) continue;
                        ++fragileBroken;
                        serverLevel.destroyBlock((BlockPos)pos, false, (Entity)pilot);
                        continue;
                    }
                    if (!canBreakSolid || !(hardness <= maxAllowedHardness) || solidBroken >= 4) continue;
                    ++solidBroken;
                    serverLevel.destroyBlock((BlockPos)pos, false, (Entity)pilot);
                }
                ++y;
            }
            break;
        }
    }

    public InteractionResult interact(Player player, InteractionHand hand) {
        if (!player.isShiftKeyDown()) {
            if (this.level().isClientSide) return InteractionResult.sidedSuccess((boolean)this.level().isClientSide);
            if (this.getPassengers().size() >= 2) return InteractionResult.sidedSuccess((boolean)this.level().isClientSide);
            player.startRiding((Entity)this);
            return InteractionResult.SUCCESS;
        }
        ItemStack held = player.getItemInHand(hand);
        if (held.is(Items.IRON_INGOT) || held.is(Items.IRON_BLOCK)) {
            float healAmount = held.is(Items.IRON_BLOCK) ? 100.0f : 20.0f;
            float health = this.getHealth();
            if (health < 100.0f || this.isBroken()) {
                float newHealth = Math.min(100.0f, health + healAmount);
                this.setHealth(newHealth);
                if (!player.getAbilities().instabuild) {
                    held.shrink(1);
                }
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ANVIL_USE, SoundSource.PLAYERS, 0.8f, 1.2f);
                if (!this.isBroken()) return InteractionResult.sidedSuccess((boolean)this.level().isClientSide);
                if (!(newHealth >= 20.0f)) return InteractionResult.sidedSuccess((boolean)this.level().isClientSide);
                this.setBroken(false);
                this.setCrashing(false);
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.SPACE_CRUISER_STARTUP.get(), SoundSource.NEUTRAL, 1.0f, 1.0f);
                return InteractionResult.sidedSuccess((boolean)this.level().isClientSide);
            }
        }
        if (!held.is(Items.REDSTONE)) return InteractionResult.PASS;
        if (!(this.getEnergy() < 100.0f)) return InteractionResult.PASS;
        this.setEnergy(Math.min(100.0f, this.getEnergy() + 25.0f));
        this.engineOverloadTicks = 0;
        if (!player.getAbilities().instabuild) {
            held.shrink(1);
        }
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.SPACE_CRUISER_LOW_ENERGY.get(), SoundSource.PLAYERS, 0.8f, 1.4f);
        return InteractionResult.sidedSuccess((boolean)this.level().isClientSide);
    }

    public void lerpTo(double x, double y, double z, float yaw, float pitch, int steps) {
        if (this.isControlledByLocalInstance()) return;
        double distSq = this.distanceToSqr(x, y, z);
        if (distSq > 64.0) {
            this.setPos(x, y, z);
            this.setRot(yaw, pitch);
            this.lerpSteps = 0;
        } else {
            this.lerpX = x;
            this.lerpY = y;
            this.lerpZ = z;
            this.lerpYRot = yaw;
            this.lerpXRot = pitch;
            this.lerpSteps = steps;
        }
    }

    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().size() < 2;
    }

    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        if (this.level().isClientSide) return;
        if (!(passenger instanceof ServerPlayer)) return;
        ServerPlayer serverPlayer = (ServerPlayer)passenger;
        this.syncSavedWaypoints(serverPlayer);
    }

    public boolean isControlledByLocalInstance() {
        Player player;
        LivingEntity livingEntity = this.getControllingPassenger();
        return livingEntity instanceof Player && (player = (Player)livingEntity).isLocalPlayer();
    }

    @Nullable
    public LivingEntity getControllingPassenger() {
        List passengers = this.getPassengers();
        if (passengers.isEmpty()) return null;
        Object e = passengers.get(0);
        if (!(e instanceof LivingEntity)) return null;
        LivingEntity living = (LivingEntity)e;
        return living;
    }

    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    protected void positionRider(Entity passenger, Entity.MoveFunction callback) {
        if (!this.hasPassenger(passenger)) {
            return;
        }
        int seatIndex = this.getPassengers().indexOf(passenger);
        Vec3 localSeat = seatIndex == 0 ? PILOT_SEAT : PASSENGER_SEAT;
        Vec3 worldOffset = SpaceCruiserEntity.rotateVector(localSeat, this.getYRot(), this.getXRot(), this.getRoll());
        callback.accept(passenger, this.getX() + worldOffset.x, this.getY() + worldOffset.y, this.getZ() + worldOffset.z);
        if (!(passenger instanceof LivingEntity)) return;
        LivingEntity living = (LivingEntity)passenger;
        living.yBodyRot = this.getYRot();
    }

    public static Vec3 rotateVector(Vec3 vec, float yawDeg, float pitchDeg, float rollDeg) {
        Vector3f v = new Vector3f((float)vec.x, (float)vec.y, (float)vec.z);
        Quaternionf q = new Quaternionf();
        q.rotateY((float)Math.toRadians(-yawDeg));
        q.rotateX((float)Math.toRadians(pitchDeg));
        q.rotateZ((float)Math.toRadians(rollDeg));
        v = q.transform(v);
        return new Vec3((double)v.x(), (double)v.y(), (double)v.z());
    }

    public double getGroundHeightAt(Level level, double x, double baseEntityY, double z) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        double bestHeight = baseEntityY - 1.0;
        int minBlockY = Mth.floor((double)(baseEntityY - 1.2));
        int maxBlockY = Mth.floor((double)(baseEntityY + 1.2));
        int bx = Mth.floor((double)x);
        int bz = Mth.floor((double)z);
        double relX = x - (double)bx;
        double relZ = z - (double)bz;
        int by = maxBlockY;
        while (by >= minBlockY) {
            double worldY;
            double localMaxY;
            VoxelShape shape;
            pos.set(bx, by, bz);
            BlockState state = level.getBlockState((BlockPos)pos);
            if (!(state.isAir() || (shape = state.getCollisionShape((BlockGetter)level, (BlockPos)pos, CollisionContext.of((Entity)this))).isEmpty() || Double.isInfinite(localMaxY = shape.max(Direction.Axis.Y, relX, relZ)) || Double.isNaN(localMaxY) || !((worldY = (double)by + localMaxY) <= baseEntityY + 1.15))) {
                return worldY;
            }
            --by;
        }
        return bestHeight;
    }

    public void updatePilotState(float throttle, float strafe, float vertical, boolean boosting, float yaw, float pitch, float roll, double x, double y, double z, double vx, double vy, double vz) {
        this.inputThrottle = throttle;
        this.inputStrafe = strafe;
        this.inputVertical = vertical;
        this.inputBoost = boosting;
        if (this.isWarpActive()) {
            return;
        }
        this.yawVelocity = Mth.wrapDegrees((float)(yaw - this.getYRot()));
        this.pitchVelocity = pitch - this.getXRot();
        this.setThrottle(throttle);
        this.setStrafe(strafe);
        this.setVertical(vertical);
        this.setBoosting(boosting);
        this.setYRot(yaw);
        this.setXRot(pitch);
        this.setRoll(roll);
        if (this.level().isClientSide) return;
        Vec3 incomingMotion = new Vec3(vx, vy, vz);
        double prevSpeed = this.getDeltaMovement().length();
        double incomingSpeed = incomingMotion.length();
        double suddenDecel = Math.max(0.0, prevSpeed - incomingSpeed);
        double distSq = this.distanceToSqr(x, y, z);
        if (distSq > 16.0) {
            this.setPos(x, y, z);
        } else {
            this.setPos(Mth.lerp((double)0.5, (double)this.getX(), (double)x), Mth.lerp((double)0.5, (double)this.getY(), (double)y), Mth.lerp((double)0.5, (double)this.getZ(), (double)z));
        }
        this.setDeltaMovement(vx, vy, vz);
        if (!(suddenDecel >= 0.3)) return;
        if (!(prevSpeed >= 0.4)) return;
        this.handleKineticImpact(suddenDecel, incomingMotion);
    }

    public void updatePilotInputs(float throttle, float strafe, float vertical, boolean boosting, float yaw, float pitch, float roll) {
        this.updatePilotState(throttle, strafe, vertical, boosting, yaw, pitch, roll, this.getX(), this.getY(), this.getZ(), this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z);
    }

    public void setHeadlightsOn(boolean on) {
        this.entityData.set(HEADLIGHTS, (Object)on);
    }

    public void toggleHeadlights() {
        boolean current = (Boolean)this.entityData.get(HEADLIGHTS);
        this.entityData.set(HEADLIGHTS, (Object)(!current ? 1 : 0));
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.SPACE_CRUISER_SWITCH.get(), SoundSource.PLAYERS, 1.0f, current ? 0.9f : 1.1f);
    }

    public boolean isHeadlightsOn() {
        return (Boolean)this.entityData.get(HEADLIGHTS);
    }

    public float getThrottle() {
        return ((Float)this.entityData.get(THROTTLE)).floatValue();
    }

    public void setThrottle(float throttle) {
        this.entityData.set(THROTTLE, (Object)Float.valueOf(throttle));
    }

    public float getStrafe() {
        return ((Float)this.entityData.get(STRAFE)).floatValue();
    }

    public void setStrafe(float strafe) {
        this.entityData.set(STRAFE, (Object)Float.valueOf(strafe));
    }

    public float getVertical() {
        return ((Float)this.entityData.get(VERTICAL)).floatValue();
    }

    public void setVertical(float vertical) {
        this.entityData.set(VERTICAL, (Object)Float.valueOf(vertical));
    }

    public boolean isBoosting() {
        return (Boolean)this.entityData.get(BOOSTING);
    }

    public void setBoosting(boolean boosting) {
        this.entityData.set(BOOSTING, (Object)boosting);
    }

    public float getYawVelocity() {
        return this.yawVelocity;
    }

    public float getPitchVelocity() {
        return this.pitchVelocity;
    }

    public float getRoll() {
        return ((Float)this.entityData.get(ROLL)).floatValue();
    }

    public float getInterpolatedRoll(float partialTicks) {
        return Mth.rotLerp((float)partialTicks, (float)this.prevRoll, (float)this.clientRoll);
    }

    public float getInterpolatedFlameIntensity(float partialTicks) {
        return Mth.lerp((float)partialTicks, (float)this.prevFlameIntensity, (float)this.clientFlameIntensity);
    }

    public float getInterpolatedReverseFlame(float partialTicks) {
        return Mth.lerp((float)partialTicks, (float)this.prevReverseFlame, (float)this.clientReverseFlame);
    }

    public float getInterpolatedSteerDiff(float partialTicks) {
        return Mth.lerp((float)partialTicks, (float)this.prevSteerDiff, (float)this.clientSteerDiff);
    }

    public float getInterpolatedBoostIntensity(float partialTicks) {
        return Mth.lerp((float)partialTicks, (float)this.prevBoostIntensity, (float)this.clientBoostIntensity);
    }

    public void setRoll(float roll) {
        this.entityData.set(ROLL, (Object)Float.valueOf(roll));
    }

    public float getHealth() {
        return ((Float)this.entityData.get(SHIP_HEALTH)).floatValue();
    }

    public void setHealth(float health) {
        this.entityData.set(SHIP_HEALTH, (Object)Float.valueOf(Mth.clamp((float)health, (float)0.0f, (float)100.0f)));
    }

    public float getEnergy() {
        return ((Float)this.entityData.get(ENERGY)).floatValue();
    }

    public void setEnergy(float energy) {
        this.entityData.set(ENERGY, (Object)Float.valueOf(Mth.clamp((float)energy, (float)0.0f, (float)100.0f)));
    }

    public boolean isHoverLock() {
        return (Boolean)this.entityData.get(HOVER_LOCK);
    }

    public void setHoverLock(boolean hoverLock) {
        if (hoverLock) {
            if (this.isBroken()) return;
            if (this.isCrashing()) return;
            if (this.getHealth() <= 0.0f) {
                return;
            }
        }
        boolean prev = this.isHoverLock();
        this.entityData.set(HOVER_LOCK, (Object)hoverLock);
        if (prev == hoverLock) return;
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.SPACE_CRUISER_HOVER.get(), SoundSource.NEUTRAL, 0.9f, hoverLock ? 1.1f : 0.85f);
    }

    public boolean isLanded() {
        return (Boolean)this.entityData.get(LANDED);
    }

    public void setLanded(boolean landed) {
        this.entityData.set(LANDED, (Object)landed);
    }

    public boolean isEngineOn() {
        return (Boolean)this.entityData.get(ENGINE_ON);
    }

    public void setEngineOn(boolean engineOn) {
        if (engineOn && (this.isBroken() || this.isCrashing() || this.getHealth() <= 0.0f)) {
            this.entityData.set(ENGINE_ON, (Object)false);
            return;
        }
        this.entityData.set(ENGINE_ON, (Object)engineOn);
    }

    public void toggleEngine() {
        if (this.isBroken() || this.isCrashing() || this.getHealth() <= 0.0f) {
            this.entityData.set(ENGINE_ON, (Object)false);
            return;
        }
        this.setEngineOn(!this.isEngineOn());
    }

    public boolean isEngineOverloaded() {
        return (Boolean)this.entityData.get(ENGINE_OVERLOADED);
    }

    public boolean isCrashing() {
        return (Boolean)this.entityData.get(CRASHING);
    }

    public void setCrashing(boolean crashing) {
        this.entityData.set(CRASHING, (Object)crashing);
    }

    public boolean isBroken() {
        return (Boolean)this.entityData.get(BROKEN);
    }

    public void setBroken(boolean broken) {
        this.entityData.set(BROKEN, (Object)broken);
    }

    public Vec3 getTransformedThruster(boolean right) {
        Vec3 local = right ? THRUSTER_RIGHT : THRUSTER_LEFT;
        Vec3 rotated = SpaceCruiserEntity.rotateVector(local, this.getYRot(), this.getXRot(), this.getRoll());
        return this.position().add(rotated);
    }

    public Vec3 getTransformedTrashCanTop(boolean right) {
        Vec3 local = right ? TRASH_CAN_RIGHT_TOP : TRASH_CAN_LEFT_TOP;
        Vec3 rotated = SpaceCruiserEntity.rotateVector(local, this.getYRot(), this.getXRot(), this.getRoll());
        return this.position().add(rotated);
    }

    public Vec3 getTransformedRearDeck() {
        Vec3 rotated = SpaceCruiserEntity.rotateVector(REAR_HOOD_DECK, this.getYRot(), this.getXRot(), this.getRoll());
        return this.position().add(rotated);
    }

    public void remove(Entity.RemovalReason reason) {
        if (reason != Entity.RemovalReason.CHANGED_DIMENSION) {
            this.releaseWarpChunkTicket();
        }
        super.remove(reason);
    }

    public boolean hurt(DamageSource source, float amount) {
        Level level;
        boolean isHeavyExplosive;
        Player player;
        LivingEntity livingEntity;
        if (this.isWarpActive() && this.getWarpStateEnum().isTunnel()) {
            return false;
        }
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        if (Config.spaceCruiserInvulnerableCreative && (livingEntity = this.getControllingPassenger()) instanceof Player && (player = (Player)livingEntity).isCreative()) {
            return false;
        }
        if (this.level().isClientSide) return false;
        if (this.isRemoved()) return false;
        float newHealth = Math.max(0.0f, this.getHealth() - amount);
        this.setHealth(newHealth);
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.SPACE_CRUISER_HURT.get(), SoundSource.NEUTRAL, 1.0f, 1.0f);
        if (!(newHealth <= 0.0f)) return true;
        this.setEngineOn(false);
        this.setHoverLock(false);
        boolean bl = isHeavyExplosive = source.is(DamageTypes.EXPLOSION) || source.is(DamageTypes.PLAYER_EXPLOSION) || amount >= 80.0f;
        if (isHeavyExplosive && Config.spaceCruiserExplosionOnDeath && (level = this.level()) instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            this.handleCatastrophicExplosion(serverLevel);
            return true;
        }
        if (this.onGround() || this.isLanded()) {
            this.setCrashing(false);
            this.setBroken(true);
            this.setLanded(true);
        } else {
            this.setCrashing(true);
            this.setBroken(false);
            this.setLanded(false);
        }
        return true;
    }

    public void handleKineticImpact(double impactDeltaV, Vec3 residualMotion) {
        Player player;
        LivingEntity livingEntity;
        if (this.level().isClientSide) {
            return;
        }
        Level level = this.level();
        if (!(level instanceof ServerLevel)) return;
        ServerLevel serverLevel = (ServerLevel)level;
        if (impactDeltaV < 0.25) {
            return;
        }
        if (Config.spaceCruiserInvulnerableCreative && (livingEntity = this.getControllingPassenger()) instanceof Player && (player = (Player)livingEntity).isCreative()) {
            return;
        }
        float rawDamage = (float)(Math.pow(impactDeltaV - 0.2, 2.0) * 120.0 * Config.spaceCruiserRamDamageMultiplier);
        if (this.isImpactCushioned()) {
            rawDamage *= 0.25f;
        }
        float currentEnergy = this.getEnergy();
        float absorbedDamage = 0.0f;
        if (currentEnergy > 0.0f && rawDamage > 0.0f) {
            float absorbMax = Math.min(currentEnergy, rawDamage * 1.25f);
            absorbedDamage = absorbMax * 0.7f;
            float energySpent = absorbMax;
            this.setEnergy(Math.max(0.0f, currentEnergy - energySpent));
            rawDamage = Math.max(0.0f, rawDamage - absorbedDamage);
            this.engineOverloadTicks = Math.max(this.engineOverloadTicks, (int)(15.0 + impactDeltaV * 20.0));
        }
        if (impactDeltaV >= 0.7) {
            serverLevel.playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.SPACE_CRUISER_CRASH.get(), SoundSource.NEUTRAL, 1.4f, 0.9f);
            serverLevel.playSound(null, this.getX(), this.getY(), this.getZ(), (Holder)SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 0.8f, 1.1f);
            serverLevel.sendParticles((ParticleOptions)ParticleTypes.EXPLOSION, this.getX(), this.getY() + 0.5, this.getZ(), 3, 1.2, 0.5, 1.2, 0.0);
        } else {
            serverLevel.playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.SPACE_CRUISER_BUMP.get(), SoundSource.NEUTRAL, 1.1f, 1.0f);
            serverLevel.sendParticles((ParticleOptions)ParticleTypes.POOF, this.getX(), this.getY() + 0.3, this.getZ(), 12, 1.2, 0.3, 1.2, 0.05);
        }
        if (impactDeltaV >= 0.45) {
            float passengerDamage = (float)((impactDeltaV - 0.45) * 22.0);
            for (Entity passenger : this.getPassengers()) {
                if (passengerDamage > 0.0f) {
                    passenger.hurt(this.damageSources().generic(), passengerDamage);
                }
                if (!(passenger instanceof LivingEntity)) continue;
                LivingEntity living = (LivingEntity)passenger;
                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1));
                living.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60, 0));
            }
        }
        if (rawDamage > 0.0f) {
            this.hurt(this.damageSources().flyIntoWall(), rawDamage);
        }
        if (!(this.getHealth() <= 0.0f)) return;
        if (Config.spaceCruiserExplosionOnDeath && !this.isImpactCushioned() && (impactDeltaV >= 0.8 || this.isCrashing())) {
            this.handleCatastrophicExplosion(serverLevel);
        } else {
            this.setCrashing(false);
            this.setBroken(true);
            this.setLanded(true);
            this.setEngineOn(false);
            this.setHoverLock(false);
        }
    }

    public boolean isImpactCushioned() {
        if (this.isInWater()) return true;
        if (this.isUnderWater()) {
            return true;
        }
        AABB box = this.getBoundingBox().inflate(0.6);
        BlockPos min = BlockPos.containing((double)box.minX, (double)box.minY, (double)box.minZ);
        BlockPos max = BlockPos.containing((double)box.maxX, (double)box.maxY, (double)box.maxZ);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        int leafCount = 0;
        int waterCount = 0;
        int solidCount = 0;
        int x = min.getX();
        block0: while (true) {
            if (x > max.getX()) {
                if (waterCount > 0) {
                    return true;
                }
                if (leafCount <= 0) return false;
                if (solidCount != 0) return false;
                return true;
            }
            int y = min.getY();
            while (true) {
                if (y <= max.getY()) {
                } else {
                    ++x;
                    continue block0;
                }
                for (int z = min.getZ(); z <= max.getZ(); ++z) {
                    pos.set(x, y, z);
                    BlockState state = this.level().getBlockState((BlockPos)pos);
                    if (state.isAir()) continue;
                    if (state.is(BlockTags.LEAVES)) {
                        ++leafCount;
                        continue;
                    }
                    if (state.liquid() || state.is(Blocks.WATER)) {
                        ++waterCount;
                        continue;
                    }
                    if (SpaceCruiserEntity.isFragileBlock(state)) continue;
                    ++solidCount;
                }
                ++y;
            }
            break;
        }
    }

    public void handleCatastrophicExplosion(ServerLevel level) {
        if (this.isRemoved()) {
            return;
        }
        if (this.isImpactCushioned()) {
            this.setCrashing(false);
            this.setBroken(true);
            this.setLanded(true);
            this.setEngineOn(false);
            this.setHoverLock(false);
            if (this.isInWater()) {
                level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_SPLASH, SoundSource.NEUTRAL, 1.5f, 0.8f);
                level.sendParticles((ParticleOptions)ParticleTypes.SPLASH, this.getX(), this.getY() + 0.5, this.getZ(), 40, 2.0, 0.5, 2.0, 0.2);
            } else {
                level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.GRASS_BREAK, SoundSource.NEUTRAL, 1.4f, 0.9f);
                level.playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.SPACE_CRUISER_BUMP.get(), SoundSource.NEUTRAL, 1.2f, 1.0f);
                level.sendParticles((ParticleOptions)ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY() + 0.5, this.getZ(), 16, 1.5, 0.5, 1.5, 0.05);
            }
            return;
        }
        double ex = this.getX();
        double ey = this.getY() + 0.5;
        double ez = this.getZ();
        ArrayList passengers = new ArrayList(this.getPassengers());
        this.ejectPassengers();
        for (Entity passenger : passengers) {
            if (!(passenger instanceof LivingEntity)) continue;
            LivingEntity living = (LivingEntity)passenger;
            living.hurt(this.damageSources().generic(), 15.0f);
            living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
            living.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 1));
            living.setDeltaMovement((this.random.nextDouble() - 0.5) * 0.8, 0.5 + this.random.nextDouble() * 0.4, (this.random.nextDouble() - 0.5) * 0.8);
        }
        boolean canGrief = level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
        level.explode((Entity)this, ex, ey, ez, 3.8f, canGrief ? Level.ExplosionInteraction.MOB : Level.ExplosionInteraction.NONE);
        level.playSound(null, ex, ey, ez, (Holder)SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 1.5f, 0.7f);
        level.playSound(null, ex, ey, ez, (SoundEvent)ModSounds.SPACE_CRUISER_CRASH.get(), SoundSource.NEUTRAL, 1.8f, 0.8f);
        level.sendParticles((ParticleOptions)((SimpleParticleType)ModParticles.MAGENTA_EXPLOSION_CLOUD.get()), ex, ey, ez, 1, 0.0, 0.0, 0.0, 0.0);
        level.sendParticles((ParticleOptions)((SimpleParticleType)ModParticles.GREEN_PLASMA_SHOCKWAVE.get()), ex, ey, ez, 1, 0.0, 0.0, 0.0, 0.0);
        int ironCount = 3 + this.random.nextInt(4);
        Containers.dropItemStack((Level)level, (double)ex, (double)ey, (double)ez, (ItemStack)new ItemStack((ItemLike)Items.IRON_INGOT, ironCount));
        int redstoneCount = 2 + this.random.nextInt(4);
        Containers.dropItemStack((Level)level, (double)ex, (double)ey, (double)ez, (ItemStack)new ItemStack((ItemLike)Items.REDSTONE, redstoneCount));
        Containers.dropItemStack((Level)level, (double)ex, (double)ey, (double)ez, (ItemStack)new ItemStack((ItemLike)ModItems.MICROVERSE_BATTERY.get(), 1));
        int i = 0;
        while (true) {
            if (i >= this.inventory.getSlots()) {
                this.discard();
                return;
            }
            ItemStack stack = this.inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                Containers.dropItemStack((Level)level, (double)ex, (double)ey, (double)ez, (ItemStack)stack);
            }
            ++i;
        }
    }

    public boolean isPickable() {
        return !this.isRemoved();
    }

    public boolean canBeCollidedWith() {
        return this.isAlive();
    }

    public boolean canCollideWith(Entity other) {
        return (other.canBeCollidedWith() || other.isPushable()) && !this.hasPassenger(other) && !this.isPassengerOfSameVehicle(other);
    }

    public boolean isPushable() {
        return false;
    }

    public void push(Entity entity) {
        double dz;
        if (this.hasPassenger(entity)) return;
        if (entity.hasPassenger((Entity)this)) return;
        if (entity.noPhysics) return;
        if (this.noPhysics) return;
        double dx = entity.getX() - this.getX();
        double dist = Mth.absMax((double)dx, (double)(dz = entity.getZ() - this.getZ()));
        if (!(dist >= 0.01)) return;
        dist = Math.sqrt(dx * dx + dz * dz);
        if (!(dist > 0.0)) return;
        double pushStrength = 0.05;
        entity.push((dx /= dist) * pushStrength, 0.0, (dz /= dist) * pushStrength);
    }

    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("Headlights")) {
            this.entityData.set(HEADLIGHTS, (Object)tag.getBoolean("Headlights"));
        }
        if (tag.contains("Health")) {
            this.setHealth(tag.getFloat("Health"));
        }
        if (tag.contains("Energy")) {
            this.setEnergy(tag.getFloat("Energy"));
        }
        if (tag.contains("HoverLock")) {
            this.setHoverLock(tag.getBoolean("HoverLock"));
        }
        if (tag.contains("Landed")) {
            this.setLanded(tag.getBoolean("Landed"));
        }
        if (tag.contains("EngineOn")) {
            this.setEngineOn(tag.getBoolean("EngineOn"));
        }
        if (tag.contains("Crashing")) {
            this.entityData.set(CRASHING, (Object)tag.getBoolean("Crashing"));
        }
        if (tag.contains("Broken")) {
            this.entityData.set(BROKEN, (Object)tag.getBoolean("Broken"));
        }
        if (tag.contains("Inventory")) {
            this.inventory.deserializeNBT((HolderLookup.Provider)this.registryAccess(), tag.getCompound("Inventory"));
        }
        if (tag.contains("WarpState")) {
            this.setWarpState(tag.getInt("WarpState"));
        }
        if (tag.contains("WarpTimer")) {
            this.setWarpTimer(tag.getInt("WarpTimer"));
        }
        if (tag.contains("WarpTargetX")) {
            this.warpTargetX = tag.getDouble("WarpTargetX");
            this.entityData.set(WARP_TARGET_X, (Object)Float.valueOf((float)this.warpTargetX));
        }
        if (tag.contains("WarpTargetY")) {
            this.warpTargetY = tag.getDouble("WarpTargetY");
            this.entityData.set(WARP_TARGET_Y, (Object)Float.valueOf((float)this.warpTargetY));
        }
        if (tag.contains("WarpTargetZ")) {
            this.warpTargetZ = tag.getDouble("WarpTargetZ");
            this.entityData.set(WARP_TARGET_Z, (Object)Float.valueOf((float)this.warpTargetZ));
        }
        if (tag.contains("WarpTargetDim")) {
            this.warpTargetDim = tag.getString("WarpTargetDim");
            this.entityData.set(WARP_TARGET_DIM, (Object)this.warpTargetDim);
        }
        if (tag.hasUUID("WarpPilotUUID")) {
            this.setWarpPilotUUID(tag.getUUID("WarpPilotUUID"));
        }
        if (tag.hasUUID("WarpPassengerUUID")) {
            this.setWarpPassengerUUID(tag.getUUID("WarpPassengerUUID"));
        }
        if (!tag.contains("SavedWaypoints", 9)) return;
        this.savedWaypoints.clear();
        ListTag list = tag.getList("SavedWaypoints", 10);
        int i = 0;
        while (i < list.size()) {
            this.savedWaypoints.add(CruiserWaypoint.fromNbt((CompoundTag)list.getCompound(i)));
            ++i;
        }
    }

    protected void addAdditionalSaveData(CompoundTag tag) {
        UUID passUUID;
        tag.putBoolean("Headlights", this.isHeadlightsOn());
        tag.putFloat("Health", this.getHealth());
        tag.putFloat("Energy", this.getEnergy());
        tag.putBoolean("HoverLock", this.isHoverLock());
        tag.putBoolean("Landed", this.isLanded());
        tag.putBoolean("EngineOn", this.isEngineOn());
        tag.putBoolean("Crashing", this.isCrashing());
        tag.putBoolean("Broken", this.isBroken());
        tag.put("Inventory", (Tag)this.inventory.serializeNBT((HolderLookup.Provider)this.registryAccess()));
        tag.putInt("WarpState", this.getWarpState());
        tag.putInt("WarpTimer", this.getWarpTimer());
        tag.putDouble("WarpTargetX", this.getTargetX());
        tag.putDouble("WarpTargetY", this.getTargetY());
        tag.putDouble("WarpTargetZ", this.getTargetZ());
        tag.putString("WarpTargetDim", this.getTargetDimension());
        UUID pilotUUID = this.getWarpPilotUUID();
        if (pilotUUID != null) {
            tag.putUUID("WarpPilotUUID", pilotUUID);
        }
        if ((passUUID = this.getWarpPassengerUUID()) != null) {
            tag.putUUID("WarpPassengerUUID", passUUID);
        }
        ListTag waypointsList = new ListTag();
        Iterator<CruiserWaypoint> iterator = this.savedWaypoints.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                tag.put("SavedWaypoints", (Tag)waypointsList);
                return;
            }
            CruiserWaypoint wp = iterator.next();
            waypointsList.add((Object)wp.toNbt());
        }
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket((Entity)this, serverEntity);
    }

    public List<CruiserWaypoint> getSavedWaypoints() {
        return Collections.unmodifiableList(new ArrayList<CruiserWaypoint>(this.savedWaypoints));
    }

    public void setSavedWaypoints(List<CruiserWaypoint> waypoints) {
        this.savedWaypoints.clear();
        if (waypoints == null) return;
        this.savedWaypoints.addAll(waypoints);
    }

    public boolean addSavedWaypoint(String name, double x, double y, double z, String dimension) {
        return this.addSavedWaypoint(new CruiserWaypoint(name, x, y, z, dimension));
    }

    public boolean addSavedWaypoint(CruiserWaypoint waypoint) {
        if (waypoint == null) {
            return false;
        }
        if (this.savedWaypoints.size() >= 30) {
            return false;
        }
        this.savedWaypoints.add(waypoint);
        if (this.level().isClientSide) return true;
        this.syncSavedWaypoints();
        return true;
    }

    public boolean removeSavedWaypoint(UUID waypointId) {
        if (waypointId == null) {
            return false;
        }
        boolean removed = this.savedWaypoints.removeIf(wp -> wp.id().equals(waypointId));
        if (!removed) return removed;
        if (this.level().isClientSide) return removed;
        this.syncSavedWaypoints();
        return removed;
    }

    public void syncSavedWaypoints() {
        if (this.level().isClientSide) return;
        ModMessages.sendToClients((CustomPacketPayload)new S2CSyncCruiserLocationsPacket(this.getId(), this.savedWaypoints));
    }

    public void syncSavedWaypoints(ServerPlayer player) {
        if (this.level().isClientSide) return;
        if (player == null) return;
        ModMessages.sendToPlayer((CustomPacketPayload)new S2CSyncCruiserLocationsPacket(this.getId(), this.savedWaypoints), (ServerPlayer)player);
    }

    public int getWarpState() {
        return (Integer)this.entityData.get(WARP_STATE);
    }

    public WarpState getWarpStateEnum() {
        return WarpState.fromId((int)this.getWarpState());
    }

    public void setWarpState(WarpState state) {
        this.entityData.set(WARP_STATE, (Object)state.getId());
        if (state != WarpState.IDLE) return;
        this.releaseWarpChunkTicket();
        this.setWarpPilotUUID(null);
        this.setWarpPassengerUUID(null);
    }

    @Nullable
    public UUID getWarpPilotUUID() {
        Optional opt = (Optional)this.entityData.get(WARP_PILOT_UUID);
        return opt.orElse(this.warpPilotUUID);
    }

    public void setWarpPilotUUID(@Nullable UUID uuid) {
        this.warpPilotUUID = uuid;
        this.entityData.set(WARP_PILOT_UUID, Optional.ofNullable(uuid));
    }

    @Nullable
    public UUID getWarpPassengerUUID() {
        Optional opt = (Optional)this.entityData.get(WARP_PASSENGER_UUID);
        return opt.orElse(this.warpPassengerUUID);
    }

    public void setWarpPassengerUUID(@Nullable UUID uuid) {
        this.warpPassengerUUID = uuid;
        this.entityData.set(WARP_PASSENGER_UUID, Optional.ofNullable(uuid));
    }

    public void setWarpState(int state) {
        this.entityData.set(WARP_STATE, (Object)state);
        if (state != WarpState.IDLE.getId()) return;
        this.releaseWarpChunkTicket();
        this.setWarpPilotUUID(null);
        this.setWarpPassengerUUID(null);
    }

    public void releaseWarpChunkTicket() {
        ServerLevel targetLevel;
        if (this.warpTargetChunkPos == null) return;
        if (this.level().isClientSide) return;
        Level level = this.level();
        if (!(level instanceof ServerLevel)) return;
        ServerLevel currentLevel = (ServerLevel)level;
        MinecraftServer server = currentLevel.getServer();
        if (server != null && (targetLevel = PortalManager.resolveTargetLevel((MinecraftServer)server, (String)this.getTargetDimension(), (ServerLevel)currentLevel)) != null) {
            try {
                targetLevel.getChunkSource().removeRegionTicket(TicketType.PORTAL, this.warpTargetChunkPos, 3, (Object)this.warpTargetChunkPos.getWorldPosition());
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        this.warpTargetChunkPos = null;
    }

    public int getWarpTimer() {
        return (Integer)this.entityData.get(WARP_TIMER);
    }

    public void setWarpTimer(int timer) {
        this.entityData.set(WARP_TIMER, (Object)timer);
    }

    public double getTargetX() {
        if (this.warpTargetX != 0.0) return this.warpTargetX;
        if ((double)((Float)this.entityData.get(WARP_TARGET_X)).floatValue() != 0.0) return ((Float)this.entityData.get(WARP_TARGET_X)).floatValue();
        return this.warpTargetX;
    }

    public double getTargetY() {
        if (this.warpTargetY != 0.0) return this.warpTargetY;
        if ((double)((Float)this.entityData.get(WARP_TARGET_Y)).floatValue() != 0.0) return ((Float)this.entityData.get(WARP_TARGET_Y)).floatValue();
        return this.warpTargetY;
    }

    public double getTargetZ() {
        if (this.warpTargetZ != 0.0) return this.warpTargetZ;
        if ((double)((Float)this.entityData.get(WARP_TARGET_Z)).floatValue() != 0.0) return ((Float)this.entityData.get(WARP_TARGET_Z)).floatValue();
        return this.warpTargetZ;
    }

    public String getTargetDimension() {
        if (this.warpTargetDim == null) return (String)this.entityData.get(WARP_TARGET_DIM);
        if (this.warpTargetDim.isEmpty()) return (String)this.entityData.get(WARP_TARGET_DIM);
        return this.warpTargetDim;
    }

    public boolean isWarpActive() {
        return this.getWarpStateEnum().isWarping();
    }

    public void handleWarpSync(int warpState, int warpTimer, double targetX, double targetY, double targetZ, String targetDimension) {
        this.entityData.set(WARP_STATE, (Object)warpState);
        this.entityData.set(WARP_TIMER, (Object)warpTimer);
        this.entityData.set(WARP_TARGET_X, (Object)Float.valueOf((float)targetX));
        this.entityData.set(WARP_TARGET_Y, (Object)Float.valueOf((float)targetY));
        this.entityData.set(WARP_TARGET_Z, (Object)Float.valueOf((float)targetZ));
        this.entityData.set(WARP_TARGET_DIM, (Object)(targetDimension != null ? targetDimension : ""));
        this.warpTargetX = targetX;
        this.warpTargetY = targetY;
        this.warpTargetZ = targetZ;
        this.warpTargetDim = targetDimension != null ? targetDimension : "";
    }

    public boolean initiateWarp(double x, double y, double z, String dimension) {
        ServerLevel currentLevel;
        MinecraftServer server;
        ServerLevel targetLevel;
        Level level;
        if (this.isBroken()) return false;
        if (this.isCrashing()) return false;
        if (this.getHealth() <= 0.0f) {
            return false;
        }
        if (this.getWarpStateEnum() != WarpState.IDLE) {
            return false;
        }
        List currentPassengers = this.getPassengers();
        this.setWarpPilotUUID(!currentPassengers.isEmpty() ? ((Entity)currentPassengers.get(0)).getUUID() : null);
        this.setWarpPassengerUUID(currentPassengers.size() > 1 ? ((Entity)currentPassengers.get(1)).getUUID() : null);
        this.setEnergy(this.getEnergy() - 25.0f);
        this.warpTargetX = x;
        this.warpTargetY = y;
        this.warpTargetZ = z;
        this.warpTargetDim = dimension != null ? dimension : "";
        this.entityData.set(WARP_TARGET_X, (Object)Float.valueOf((float)x));
        this.entityData.set(WARP_TARGET_Y, (Object)Float.valueOf((float)y));
        this.entityData.set(WARP_TARGET_Z, (Object)Float.valueOf((float)z));
        this.entityData.set(WARP_TARGET_DIM, (Object)this.warpTargetDim);
        this.setWarpState(WarpState.CHARGING);
        this.setWarpTimer(0);
        this.setLanded(false);
        this.setHoverLock(false);
        if (!this.level().isClientSide && (level = this.level()) instanceof ServerLevel && (targetLevel = PortalManager.resolveTargetLevel((MinecraftServer)(server = (currentLevel = (ServerLevel)level).getServer()), (String)this.warpTargetDim, (ServerLevel)currentLevel)) != null) {
            ChunkPos chunkPos;
            this.warpTargetChunkPos = chunkPos = new ChunkPos(BlockPos.containing((double)x, (double)y, (double)z));
            targetLevel.getChunkSource().addRegionTicket(TicketType.PORTAL, chunkPos, 3, (Object)chunkPos.getWorldPosition());
        }
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.SPACE_CRUISER_STARTUP.get(), SoundSource.NEUTRAL, 1.2f, 1.2f);
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.PORTAL_OPEN.get(), SoundSource.NEUTRAL, 0.8f, 1.4f);
        if (this.level().isClientSide) return true;
        ModMessages.sendToClients((CustomPacketPayload)new S2CSyncWarpSequencePacket(this.getId(), WarpState.CHARGING.getId(), 0, x, y, z, this.warpTargetDim, this.getWarpPilotUUID(), this.getWarpPassengerUUID()));
        return true;
    }

    public void abortWarp(String reason) {
        if (this.getWarpStateEnum() == WarpState.IDLE) {
            return;
        }
        this.releaseWarpChunkTicket();
        this.setWarpState(WarpState.IDLE);
        this.setWarpTimer(0);
        this.setWarpPilotUUID(null);
        this.setWarpPassengerUUID(null);
        if (this.level().isClientSide) return;
        ModMessages.sendToClients((CustomPacketPayload)new S2CSyncWarpSequencePacket(this.getId(), WarpState.IDLE.getId(), 0, this.getTargetX(), this.getTargetY(), this.getTargetZ(), this.getTargetDimension(), null, null));
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.SPACE_CRUISER_SHUTDOWN.get(), SoundSource.NEUTRAL, 1.0f, 1.0f);
    }

    private void tickWarpStateMachine() {
        if (this.level().isClientSide) {
            return;
        }
        Level level = this.level();
        if (!(level instanceof ServerLevel)) return;
        ServerLevel currentLevel = (ServerLevel)level;
        WarpState state = this.getWarpStateEnum();
        if (state == WarpState.IDLE) {
            return;
        }
        if (state == WarpState.TUNNEL_TRANSIT || state == WarpState.EXITING) {
            Player passenger;
            UUID passengerUUID;
            Player pilot;
            UUID pilotUUID = this.getWarpPilotUUID();
            if (pilotUUID != null && (pilot = this.level().getPlayerByUUID(pilotUUID)) != null && !this.hasPassenger((Entity)pilot)) {
                if (pilot instanceof ServerPlayer) {
                    ServerPlayer sp = (ServerPlayer)pilot;
                    sp.connection.teleport(this.getX(), this.getY(), this.getZ(), sp.getYRot(), sp.getXRot());
                } else {
                    pilot.teleportTo(this.getX(), this.getY(), this.getZ());
                }
                pilot.resetFallDistance();
                pilot.setDeltaMovement(this.getDeltaMovement());
                if (!this.getPassengers().isEmpty()) {
                    ArrayList existing = new ArrayList(this.getPassengers());
                    this.ejectPassengers();
                    pilot.startRiding((Entity)this, true);
                    for (Entity other : existing) {
                        if (other == pilot || this.getPassengers().size() >= 2) continue;
                        other.startRiding((Entity)this, true);
                    }
                } else {
                    pilot.startRiding((Entity)this, true);
                }
            }
            if ((passengerUUID = this.getWarpPassengerUUID()) != null && (passenger = this.level().getPlayerByUUID(passengerUUID)) != null && !this.hasPassenger((Entity)passenger) && this.getPassengers().size() < 2) {
                if (passenger instanceof ServerPlayer) {
                    ServerPlayer sp = (ServerPlayer)passenger;
                    sp.connection.teleport(this.getX(), this.getY(), this.getZ(), sp.getYRot(), sp.getXRot());
                } else {
                    passenger.teleportTo(this.getX(), this.getY(), this.getZ());
                }
                passenger.resetFallDistance();
                passenger.setDeltaMovement(this.getDeltaMovement());
                passenger.startRiding((Entity)this, true);
            }
        }
        int timer = this.getWarpTimer() + 1;
        this.setWarpTimer(timer);
        switch (1.$SwitchMap$org$ExtraTortik$ricks_stuff$entity$WarpState[state.ordinal()]) {
            case 1: {
                boolean chunkReady;
                if (Math.abs(this.inputThrottle) > 0.1f || Math.abs(this.inputStrafe) > 0.1f || Math.abs(this.inputVertical) > 0.1f) {
                    this.abortWarp("USER_INPUT");
                    return;
                }
                if (this.isBroken() || this.isCrashing() || this.getHealth() <= 0.0f) {
                    this.abortWarp("DAMAGED");
                    return;
                }
                if (timer < WarpState.CHARGING.getDurationTicks()) return;
                ChunkPos chunkPos = this.warpTargetChunkPos != null ? this.warpTargetChunkPos : new ChunkPos(BlockPos.containing((double)this.getTargetX(), (double)this.getTargetY(), (double)this.getTargetZ()));
                Level targetLevel = PortalManager.resolveTargetLevel((MinecraftServer)currentLevel.getServer(), (String)this.getTargetDimension(), (ServerLevel)currentLevel);
                if (this.warpTargetChunkPos == null && targetLevel != null) {
                    this.warpTargetChunkPos = chunkPos;
                    targetLevel.getChunkSource().addRegionTicket(TicketType.PORTAL, chunkPos, 3, (Object)chunkPos.getWorldPosition());
                }
                boolean bl = chunkReady = targetLevel != null && targetLevel.getChunkSource().hasChunk(chunkPos.x, chunkPos.z);
                if (!chunkReady) {
                    if (timer < 80) return;
                }
                this.setWarpState(WarpState.RIFT_OPENING);
                this.setWarpTimer(0);
                ModMessages.sendToClients((CustomPacketPayload)new S2CSyncWarpSequencePacket(this.getId(), WarpState.RIFT_OPENING.getId(), 0, this.getTargetX(), this.getTargetY(), this.getTargetZ(), this.getTargetDimension(), this.getWarpPilotUUID(), this.getWarpPassengerUUID()));
                currentLevel.playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.PORTAL_OPEN.get(), SoundSource.NEUTRAL, 1.5f, 1.0f);
                break;
            }
            case 2: {
                if (this.isBroken() || this.isCrashing() || this.getHealth() <= 0.0f) {
                    this.abortWarp("DAMAGED");
                    return;
                }
                if (timer < WarpState.RIFT_OPENING.getDurationTicks()) return;
                this.setWarpState(WarpState.ENTERING);
                this.setWarpTimer(0);
                ModMessages.sendToClients((CustomPacketPayload)new S2CSyncWarpSequencePacket(this.getId(), WarpState.ENTERING.getId(), 0, this.getTargetX(), this.getTargetY(), this.getTargetZ(), this.getTargetDimension(), this.getWarpPilotUUID(), this.getWarpPassengerUUID()));
                currentLevel.playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.SPACE_CRUISER_ENGINE.get(), SoundSource.NEUTRAL, 1.5f, 1.4f);
                break;
            }
            case 3: {
                if (timer < WarpState.ENTERING.getDurationTicks()) return;
                this.setWarpState(WarpState.TUNNEL_TRANSIT);
                this.setWarpTimer(0);
                ModMessages.sendToClients((CustomPacketPayload)new S2CSyncWarpSequencePacket(this.getId(), WarpState.TUNNEL_TRANSIT.getId(), 0, this.getTargetX(), this.getTargetY(), this.getTargetZ(), this.getTargetDimension(), this.getWarpPilotUUID(), this.getWarpPassengerUUID()));
                currentLevel.playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.PORTAL_TELEPORT.get(), SoundSource.NEUTRAL, 1.2f, 0.8f);
                break;
            }
            case 4: {
                if (timer == 40) {
                    this.performWarpTeleport(currentLevel);
                }
                if (timer < WarpState.TUNNEL_TRANSIT.getDurationTicks()) return;
                this.setWarpState(WarpState.EXITING);
                this.setWarpTimer(0);
                ModMessages.sendToClients((CustomPacketPayload)new S2CSyncWarpSequencePacket(this.getId(), WarpState.EXITING.getId(), 0, this.getTargetX(), this.getTargetY(), this.getTargetZ(), this.getTargetDimension(), this.getWarpPilotUUID(), this.getWarpPassengerUUID()));
                Level targetLevel = this.level();
                if (!(targetLevel instanceof ServerLevel)) return;
                ServerLevel exitLevel = (ServerLevel)targetLevel;
                exitLevel.playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.PORTAL_CLOSE.get(), SoundSource.NEUTRAL, 1.5f, 1.0f);
                break;
            }
            case 5: {
                if (timer < WarpState.EXITING.getDurationTicks()) return;
                this.releaseWarpChunkTicket();
                this.setWarpState(WarpState.IDLE);
                this.setWarpTimer(0);
                UUID oldPilot = this.getWarpPilotUUID();
                UUID oldPass = this.getWarpPassengerUUID();
                this.setWarpPilotUUID(null);
                this.setWarpPassengerUUID(null);
                ModMessages.sendToClients((CustomPacketPayload)new S2CSyncWarpSequencePacket(this.getId(), WarpState.IDLE.getId(), 0, this.getTargetX(), this.getTargetY(), this.getTargetZ(), this.getTargetDimension(), oldPilot, oldPass));
                break;
            }
        }
    }

    private void performWarpTeleport(ServerLevel currentLevel) {
        ChunkPos chunkPos;
        double destX = this.getTargetX();
        double destY = this.getTargetY();
        double destZ = this.getTargetZ();
        String targetDimStr = this.getTargetDimension();
        MinecraftServer server = currentLevel.getServer();
        ServerLevel destWorld = currentLevel;
        if (targetDimStr != null && !targetDimStr.isEmpty() && server != null) {
            destWorld = PortalManager.resolveTargetLevel((MinecraftServer)server, (String)targetDimStr, (ServerLevel)currentLevel);
        }
        this.warpTargetChunkPos = chunkPos = this.warpTargetChunkPos != null ? this.warpTargetChunkPos : new ChunkPos(BlockPos.containing((double)destX, (double)destY, (double)destZ));
        destWorld.getChunkSource().addRegionTicket(TicketType.PORTAL, chunkPos, 3, (Object)chunkPos.getWorldPosition());
        float curYaw = this.getYRot();
        float curPitch = this.getXRot();
        float yawRad = (float)Math.toRadians(curYaw);
        float pitchRad = (float)Math.toRadians(curPitch);
        Vec3 forwardDir = new Vec3(-Math.sin(yawRad) * Math.cos(pitchRad), -Math.sin(pitchRad), Math.cos(yawRad) * Math.cos(pitchRad));
        Vec3 warpVelocity = forwardDir.scale(1.5);
        ArrayList passengers = new ArrayList(this.getPassengers());
        if (destWorld == currentLevel) {
            this.teleportTo(destX, destY, destZ);
            this.setDeltaMovement(warpVelocity);
            this.hurtMarked = true;
            this.hasImpulse = true;
            for (Entity passenger : passengers) {
                if (passenger instanceof ServerPlayer) {
                    ServerPlayer sp = (ServerPlayer)passenger;
                    sp.connection.teleport(destX, destY, destZ, sp.getYRot(), sp.getXRot());
                    sp.resetFallDistance();
                    sp.setDeltaMovement(warpVelocity);
                    sp.hurtMarked = true;
                    sp.hasImpulse = true;
                    sp.startRiding((Entity)this, true);
                    continue;
                }
                passenger.teleportTo(destX, destY, destZ);
                passenger.resetFallDistance();
                passenger.setDeltaMovement(warpVelocity);
                passenger.hurtMarked = true;
                passenger.hasImpulse = true;
                passenger.startRiding((Entity)this, true);
            }
            if (server != null) {
                server.execute(() -> {
                    if (!this.isAlive()) return;
                    Iterator iterator = passengers.iterator();
                    while (iterator.hasNext()) {
                        Entity p = (Entity)iterator.next();
                        if (!p.isAlive() || this.hasPassenger(p) || this.getPassengers().size() >= 2) continue;
                        p.startRiding((Entity)this, true);
                    }
                });
            }
            ModMessages.sendToClients((CustomPacketPayload)new S2CSyncWarpSequencePacket(this.getId(), WarpState.TUNNEL_TRANSIT.getId(), this.getWarpTimer(), destX, destY, destZ, targetDimStr, this.getWarpPilotUUID(), this.getWarpPassengerUUID()));
        } else {
            SpaceCruiserEntity sc;
            this.ejectPassengers();
            ServerLevel targetDestWorld = destWorld;
            double finalX = destX;
            double finalY = destY;
            double finalZ = destZ;
            float finalYaw = curYaw;
            float finalPitch = curPitch;
            Vec3 finalVel = warpVelocity;
            DimensionTransition transition = new DimensionTransition(targetDestWorld, new Vec3(finalX, finalY, finalZ), finalVel, finalYaw, finalPitch, DimensionTransition.DO_NOTHING);
            Entity repositioned = this.changeDimension(transition);
            SpaceCruiserEntity targetCruiser = repositioned instanceof SpaceCruiserEntity ? (sc = (SpaceCruiserEntity)repositioned) : this;
            CompoundTag nbt = new CompoundTag();
            this.addAdditionalSaveData(nbt);
            targetCruiser.readAdditionalSaveData(nbt);
            targetCruiser.inventory.deserializeNBT((HolderLookup.Provider)this.registryAccess(), this.inventory.serializeNBT((HolderLookup.Provider)this.registryAccess()));
            targetCruiser.setWarpState(WarpState.TUNNEL_TRANSIT);
            targetCruiser.setWarpTimer(40);
            targetCruiser.warpTargetX = destX;
            targetCruiser.warpTargetY = destY;
            targetCruiser.warpTargetZ = destZ;
            targetCruiser.warpTargetDim = targetDimStr;
            targetCruiser.setWarpPilotUUID(this.getWarpPilotUUID());
            targetCruiser.setWarpPassengerUUID(this.getWarpPassengerUUID());
            targetCruiser.warpTargetChunkPos = chunkPos;
            targetCruiser.entityData.set(WARP_TARGET_X, (Object)Float.valueOf((float)destX));
            targetCruiser.entityData.set(WARP_TARGET_Y, (Object)Float.valueOf((float)destY));
            targetCruiser.entityData.set(WARP_TARGET_Z, (Object)Float.valueOf((float)destZ));
            targetCruiser.entityData.set(WARP_TARGET_DIM, (Object)(targetDimStr != null ? targetDimStr : ""));
            for (Entity passenger : passengers) {
                if (passenger instanceof ServerPlayer) {
                    ServerPlayer sp = (ServerPlayer)passenger;
                    sp.teleportTo(destWorld, finalX, finalY, finalZ, sp.getYRot(), sp.getXRot());
                    sp.resetFallDistance();
                    sp.setDeltaMovement(finalVel);
                    sp.hurtMarked = true;
                    sp.hasImpulse = true;
                    sp.startRiding((Entity)targetCruiser, true);
                    continue;
                }
                DimensionTransition passTransition = new DimensionTransition(destWorld, new Vec3(finalX, finalY, finalZ), finalVel, passenger.getYRot(), passenger.getXRot(), DimensionTransition.DO_NOTHING);
                Entity repPassenger = passenger.changeDimension(passTransition);
                if (repPassenger == null) continue;
                repPassenger.startRiding((Entity)targetCruiser, true);
            }
            if (server != null) {
                server.execute(() -> {
                    if (!targetCruiser.isAlive()) return;
                    Iterator iterator = passengers.iterator();
                    while (iterator.hasNext()) {
                        Entity p = (Entity)iterator.next();
                        if (!p.isAlive() || targetCruiser.hasPassenger(p) || targetCruiser.getPassengers().size() >= 2) continue;
                        p.startRiding((Entity)targetCruiser, true);
                    }
                });
            }
            ModMessages.sendToClients((CustomPacketPayload)new S2CSyncWarpSequencePacket(targetCruiser.getId(), WarpState.TUNNEL_TRANSIT.getId(), 40, destX, destY, destZ, targetDimStr, targetCruiser.getWarpPilotUUID(), targetCruiser.getWarpPassengerUUID()));
        }
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.nbt.CompoundTag
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
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.tags.BlockTags
 *  net.minecraft.tags.DamageTypeTags
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.MoverType
 *  net.minecraft.world.entity.item.PrimedTnt
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.FireChargeItem
 *  net.minecraft.world.item.FlintAndSteelItem
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.component.CustomData
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.BushBlock
 *  net.minecraft.world.level.block.CarpetBlock
 *  net.minecraft.world.level.block.FlowerPotBlock
 *  net.minecraft.world.level.block.IronBarsBlock
 *  net.minecraft.world.level.block.LanternBlock
 *  net.minecraft.world.level.block.SnowLayerBlock
 *  net.minecraft.world.level.block.StainedGlassBlock
 *  net.minecraft.world.level.block.TintedGlassBlock
 *  net.minecraft.world.level.block.TorchBlock
 *  net.minecraft.world.level.block.TransparentBlock
 *  net.minecraft.world.level.block.WebBlock
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.common.Tags$Blocks
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.init.ModEntities
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 *  org.ExtraTortik.ricks_stuff.init.ModParticles
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.RickPrimeBombRemoteItem
 */
package org.ExtraTortik.ricks_stuff.entity;

import java.util.List;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FireChargeItem;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.TintedGlassBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.init.ModEntities;
import org.ExtraTortik.ricks_stuff.init.ModItems;
import org.ExtraTortik.ricks_stuff.init.ModParticles;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.RickPrimeBombRemoteItem;

public class RickPrimeBombEntity
extends Entity {
    private static final EntityDataAccessor<Integer> FUSE = SynchedEntityData.defineId(RickPrimeBombEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_FLASHING = SynchedEntityData.defineId(RickPrimeBombEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(RickPrimeBombEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_ARMED = SynchedEntityData.defineId(RickPrimeBombEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    public static final int FUSE_BEEP_END = 60;
    public static final int FUSE_SPHERE_END = 70;
    public static final int MAX_FUSE = 70;
    private float rollAngle = 0.0f;
    private float prevRollAngle = 0.0f;
    private float rollYaw = 0.0f;
    private float prevRollYaw = 0.0f;
    private boolean hasExploded = false;

    public RickPrimeBombEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.blocksBuilding = true;
    }

    public RickPrimeBombEntity(Level level, double x, double y, double z) {
        this((EntityType)ModEntities.RICK_PRIME_BOMB.get(), level);
        this.setPos(x, y, z);
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(FUSE, (Object)0);
        builder.define(IS_FLASHING, (Object)false);
        builder.define(STATE, (Object)0);
        builder.define(IS_ARMED, (Object)false);
    }

    public int getFuse() {
        return (Integer)this.entityData.get(FUSE);
    }

    public void setFuse(int fuse) {
        this.entityData.set(FUSE, (Object)fuse);
    }

    public boolean isFlashing() {
        return (Boolean)this.entityData.get(IS_FLASHING);
    }

    public void setFlashing(boolean flashing) {
        this.entityData.set(IS_FLASHING, (Object)flashing);
    }

    public int getState() {
        return (Integer)this.entityData.get(STATE);
    }

    public void setState(int state) {
        this.entityData.set(STATE, (Object)state);
    }

    public boolean isArmed() {
        return (Boolean)this.entityData.get(IS_ARMED);
    }

    public void setArmed(boolean armed) {
        this.entityData.set(IS_ARMED, (Object)armed);
    }

    public void triggerRemoteDetonation() {
        if (this.level().isClientSide()) return;
        if (!this.isArmed()) {
            this.setArmed(true);
            this.setFuse(0);
        } else {
            this.hasExploded = true;
            this.detonate();
        }
    }

    public boolean isSphereExpanding() {
        int fuse = this.getFuse();
        return this.isArmed() && fuse >= 60 && fuse < 70;
    }

    public float getSphereRadius(float partialTicks) {
        if (!this.isArmed()) {
            return 0.0f;
        }
        float f = (float)this.getFuse() + partialTicks;
        if (f < 60.0f) {
            return 0.0f;
        }
        if (f >= 70.0f) {
            return 2.0f;
        }
        float progress = (f - 60.0f) / 10.0f;
        return 0.2f + 1.8f * (float)Math.sin((double)progress * Math.PI * 0.5);
    }

    public float getRollAngle(float partialTicks) {
        return this.prevRollAngle + (this.rollAngle - this.prevRollAngle) * partialTicks;
    }

    public float getRollYaw(float partialTicks) {
        return this.prevRollYaw + (this.rollYaw - this.prevRollYaw) * partialTicks;
    }

    public boolean isPickable() {
        return true;
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    public boolean isAttackable() {
        return true;
    }

    public boolean skipAttackInteraction(Entity attacker) {
        return false;
    }

    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        if (source.is(DamageTypeTags.IS_EXPLOSION)) {
            if (this.hasExploded) return true;
            this.hasExploded = true;
            this.detonate();
            return true;
        }
        Entity attacker = source.getEntity();
        if (!(attacker instanceof Player)) return super.hurt(source, amount);
        Player player = (Player)attacker;
        if (player.isShiftKeyDown() && !this.isArmed()) {
            if (this.level().isClientSide()) return true;
            ItemStack bombItem = new ItemStack((ItemLike)ModItems.RICK_PRIME_BOMB.get());
            bombItem.update(DataComponents.CUSTOM_DATA, (Object)CustomData.EMPTY, c -> c.update(t -> t.putUUID("BombId", this.getUUID())));
            if (!player.addItem(bombItem)) {
                player.drop(bombItem, false);
            }
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0f, 1.0f);
            this.discard();
            return true;
        }
        Vec3 look = attacker.getLookAngle();
        double force = 0.65 + Math.min((double)amount * 0.1, 0.55);
        Vec3 impulse = new Vec3(look.x, Math.max(0.12, look.y + 0.22), look.z).normalize().scale(force);
        this.setDeltaMovement(this.getDeltaMovement().add(impulse));
        this.hasImpulse = true;
        this.hurtMarked = true;
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ANVIL_PLACE, SoundSource.NEUTRAL, 0.6f, 1.8f);
        return true;
    }

    public InteractionResult interact(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isShiftKeyDown() && !this.isArmed()) {
            if (this.level().isClientSide()) return InteractionResult.sidedSuccess((boolean)this.level().isClientSide());
            ItemStack bombItem = new ItemStack((ItemLike)ModItems.RICK_PRIME_BOMB.get());
            bombItem.update(DataComponents.CUSTOM_DATA, (Object)CustomData.EMPTY, c -> c.update(t -> t.putUUID("BombId", this.getUUID())));
            if (!player.addItem(bombItem)) {
                player.drop(bombItem, false);
            }
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0f, 1.0f);
            this.discard();
            return InteractionResult.sidedSuccess((boolean)this.level().isClientSide());
        }
        if (stack.getItem() instanceof FlintAndSteelItem || stack.getItem() instanceof FireChargeItem) {
            if (this.level().isClientSide()) return InteractionResult.sidedSuccess((boolean)this.level().isClientSide());
            if (stack.getItem() instanceof FlintAndSteelItem && !player.getAbilities().instabuild) {
                stack.hurtAndBreak(1, (LivingEntity)player, LivingEntity.getSlotForHand((InteractionHand)hand));
            } else if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.FLINTANDSTEEL_USE, SoundSource.PLAYERS, 1.0f, 1.0f);
            this.hasExploded = true;
            this.detonate();
            return InteractionResult.sidedSuccess((boolean)this.level().isClientSide());
        }
        if (stack.getItem() instanceof RickPrimeBombRemoteItem) {
            if (this.level().isClientSide()) return InteractionResult.sidedSuccess((boolean)this.level().isClientSide());
            stack.update(DataComponents.CUSTOM_DATA, (Object)CustomData.EMPTY, c -> c.update(t -> {
                t.putUUID("BoundBomb", this.getUUID());
                t.remove("BombX");
                t.remove("BombY");
                t.remove("BombZ");
            }));
            this.level().playSound(null, player.getX(), player.getY(), player.getZ(), (SoundEvent)ModSounds.UI_DIAL.get(), SoundSource.PLAYERS, 1.0f, 1.2f);
            return InteractionResult.sidedSuccess((boolean)this.level().isClientSide());
        }
        if (!stack.isEmpty()) return super.interact(player, hand);
        if (this.isArmed()) return super.interact(player, hand);
        if (this.level().isClientSide()) return InteractionResult.sidedSuccess((boolean)this.level().isClientSide());
        ItemStack bombItem = new ItemStack((ItemLike)ModItems.RICK_PRIME_BOMB.get());
        bombItem.update(DataComponents.CUSTOM_DATA, (Object)CustomData.EMPTY, c -> c.update(t -> t.putUUID("BombId", this.getUUID())));
        if (!player.addItem(bombItem)) {
            player.drop(bombItem, false);
        }
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0f, 1.0f);
        this.discard();
        return InteractionResult.sidedSuccess((boolean)this.level().isClientSide());
    }

    public void tick() {
        List primedTnts;
        super.tick();
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.04, 0.0));
        }
        Vec3 prevMotion = this.getDeltaMovement();
        this.move(MoverType.SELF, prevMotion);
        Vec3 curMotion = this.getDeltaMovement();
        if (this.horizontalCollision) {
            double bounceX = Math.abs(prevMotion.x) > Math.abs(curMotion.x) + 0.001 ? -prevMotion.x * 0.55 : curMotion.x;
            double bounceZ = Math.abs(prevMotion.z) > Math.abs(curMotion.z) + 0.001 ? -prevMotion.z * 0.55 : curMotion.z;
            curMotion = new Vec3(bounceX, curMotion.y, bounceZ);
            this.setDeltaMovement(curMotion);
        }
        if (this.onGround()) {
            double vy = curMotion.y;
            vy = prevMotion.y < -0.08 ? -prevMotion.y * 0.42 : 0.0;
            double rollFriction = 0.82;
            double vx = curMotion.x * rollFriction;
            double vz = curMotion.z * rollFriction;
            if (Math.abs(vx) < 0.004) {
                vx = 0.0;
            }
            if (Math.abs(vz) < 0.004) {
                vz = 0.0;
            }
            this.setDeltaMovement(new Vec3(vx, vy, vz));
        } else {
            this.setDeltaMovement(curMotion.multiply(0.98, 0.98, 0.98));
        }
        Vec3 motion = this.getDeltaMovement();
        double hSpeed = Math.sqrt(motion.x * motion.x + motion.z * motion.z);
        this.prevRollAngle = this.rollAngle;
        this.prevRollYaw = this.rollYaw;
        if (hSpeed > 0.002) {
            double dTheta = hSpeed / 0.5 * 57.29577951308232;
            this.rollAngle += (float)dTheta;
            this.rollYaw = (float)Math.toDegrees(Math.atan2(motion.x, motion.z));
        }
        if (!(this.level().isClientSide() || this.hasExploded || (primedTnts = this.level().getEntitiesOfClass(PrimedTnt.class, this.getBoundingBox().inflate(4.5))).isEmpty())) {
            this.hasExploded = true;
            this.detonate();
            return;
        }
        if (!this.isArmed()) {
            this.setFlashing(false);
            return;
        }
        int currentFuse = this.getFuse() + 1;
        this.setFuse(currentFuse);
        if (currentFuse < 60) {
            boolean isBeepTick;
            float pitch;
            int flashDuration;
            int interval;
            if (currentFuse < 24) {
                interval = 8;
                flashDuration = 2;
                pitch = 1.0f + (float)currentFuse / 60.0f * 0.3f;
            } else if (currentFuse < 42) {
                interval = 5;
                flashDuration = 2;
                pitch = 1.35f + (float)(currentFuse - 24) / 18.0f * 0.3f;
            } else if (currentFuse < 54) {
                interval = 3;
                flashDuration = 1;
                pitch = 1.75f;
            } else {
                interval = 2;
                flashDuration = 1;
                pitch = 2.2f;
            }
            boolean bl = isBeepTick = currentFuse % interval == 0;
            if (isBeepTick && !this.level().isClientSide()) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.BOMB_BEEP.get(), SoundSource.NEUTRAL, 1.2f, pitch);
            }
            boolean isFlashingTick = currentFuse % interval < flashDuration;
            this.setFlashing(isFlashingTick);
            if (!this.level().isClientSide()) return;
            if (!(this.random.nextFloat() < 0.4f)) return;
            double rx = this.getX() + (this.random.nextDouble() - 0.5) * 0.4;
            double ry = this.getY() + 0.532 + (this.random.nextDouble() - 0.5) * 0.3;
            double rz = this.getZ() + (this.random.nextDouble() - 0.5) * 0.4;
            this.level().addParticle((ParticleOptions)ModParticles.MAGENTA_DIMENSIONAL_SPARK.get(), rx, ry, rz, (this.random.nextDouble() - 0.5) * 0.05, this.random.nextDouble() * 0.05, (this.random.nextDouble() - 0.5) * 0.05);
        } else if (currentFuse == 60) {
            this.setFlashing(true);
            this.setState(1);
            if (this.level().isClientSide()) return;
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.BOMB_CHARGE.get(), SoundSource.NEUTRAL, 2.5f, 1.0f);
        } else if (currentFuse < 70) {
            this.setFlashing(true);
            this.setState(1);
            if (!this.level().isClientSide()) return;
            this.spawnSphereParticles();
        } else {
            if (this.hasExploded) return;
            this.hasExploded = true;
            this.setState(2);
            this.detonate();
        }
    }

    private void spawnSphereParticles() {
        float radius = this.getSphereRadius(0.0f);
        int i = 0;
        while (i < 8) {
            double theta = this.random.nextDouble() * Math.PI * 2.0;
            double phi = (this.random.nextDouble() - 0.5) * Math.PI;
            double dist = (double)radius * (0.8 + this.random.nextDouble() * 0.4);
            double sx = this.getX() + dist * Math.cos(phi) * Math.cos(theta);
            double sy = this.getY() + 0.532 + dist * Math.sin(phi);
            double sz = this.getZ() + dist * Math.cos(phi) * Math.sin(theta);
            double vx = (this.getX() - sx) * 0.14;
            double vy = (this.getY() + 0.532 - sy) * 0.14;
            double vz = (this.getZ() - sz) * 0.14;
            this.level().addParticle((ParticleOptions)ModParticles.MAGENTA_DIMENSIONAL_SPARK.get(), sx, sy, sz, vx, vy, vz);
            ++i;
        }
    }

    /*
     * Unable to fully structure code
     */
    private void detonate() {
        block18: {
            cx = this.getX();
            cy = this.getY() + 0.532;
            cz = this.getZ();
            if (this.level().isClientSide()) break block18;
            this.level().playSound(null, cx, cy, cz, (SoundEvent)ModSounds.BOMB_EXPLODE.get(), SoundSource.NEUTRAL, 4.0f, 1.0f);
            var8_4 = this.level();
            if (var8_4 instanceof ServerLevel) {
                serverLevel = (ServerLevel)var8_4;
                for (i = 0; i < 75; ++i) {
                    theta = this.random.nextDouble() * 3.141592653589793 * 2.0;
                    phi = (this.random.nextDouble() - 0.5) * 3.141592653589793;
                    speed = 0.35 + this.random.nextDouble() * 0.55;
                    vx = Math.cos(phi) * Math.cos(theta) * speed;
                    vy = Math.sin(phi) * speed + 0.06;
                    vz = Math.cos(phi) * Math.sin(theta) * speed;
                    serverLevel.sendParticles((ParticleOptions)((SimpleParticleType)ModParticles.MAGENTA_EXPLOSION_CLOUD.get()), cx, cy, cz, 0, vx, vy, vz, 1.0);
                }
                for (i = 0; i < 18; ++i) {
                    serverLevel.sendParticles((ParticleOptions)((SimpleParticleType)ModParticles.MAGENTA_PLASMA_SHOCKWAVE.get()), cx, this.getY() + 0.05, cz, 0, 0.0, 0.0, 0.0, 1.0);
                }
                for (i = 0; i < 36; ++i) {
                    theta = this.random.nextDouble() * 3.141592653589793 * 2.0;
                    phi = (this.random.nextDouble() - 0.35) * 3.141592653589793;
                    speed = 0.7 + this.random.nextDouble() * 0.8;
                    vx = Math.cos(phi) * Math.cos(theta) * speed;
                    vy = Math.sin(phi) * speed + 0.08;
                    vz = Math.cos(phi) * Math.sin(theta) * speed;
                    serverLevel.sendParticles((ParticleOptions)((SimpleParticleType)ModParticles.MAGENTA_VORTEX_BEAM.get()), cx, cy, cz, 0, vx, vy, vz, 1.0);
                }
                for (i = 0; i < 60; ++i) {
                    theta = this.random.nextDouble() * 3.141592653589793 * 2.0;
                    phi = (this.random.nextDouble() * 0.4 + 0.15) * 3.141592653589793;
                    speed = 0.6 + this.random.nextDouble() * 0.95;
                    vx = Math.cos(phi) * Math.cos(theta) * speed;
                    vy = Math.sin(phi) * speed;
                    vz = Math.cos(phi) * Math.sin(theta) * speed;
                    serverLevel.sendParticles((ParticleOptions)((SimpleParticleType)ModParticles.MAGENTA_BURNING_SHRAPNEL.get()), cx, cy, cz, 0, vx, vy, vz, 1.0);
                }
                for (i = 0; i < 110; ++i) {
                    theta = this.random.nextDouble() * 3.141592653589793 * 2.0;
                    phi = (this.random.nextDouble() - 0.5) * 3.141592653589793;
                    speed = 0.6 + this.random.nextDouble() * 0.8;
                    vx = Math.cos(phi) * Math.cos(theta) * speed;
                    vy = Math.sin(phi) * speed + 0.1;
                    vz = Math.cos(phi) * Math.sin(theta) * speed;
                    serverLevel.sendParticles((ParticleOptions)((SimpleParticleType)ModParticles.MAGENTA_DIMENSIONAL_SPARK.get()), cx, cy, cz, 0, vx, vy, vz, 1.0);
                }
                for (i = 0; i < 35; ++i) {
                    driftX = (this.random.nextDouble() - 0.5) * 0.04;
                    driftZ = (this.random.nextDouble() - 0.5) * 0.04;
                    upSpeed = 0.16 + this.random.nextDouble() * 0.1;
                    serverLevel.sendParticles((ParticleOptions)ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, cx, cy, cz, 0, driftX, upSpeed, driftZ, 1.0);
                    serverLevel.sendParticles((ParticleOptions)ParticleTypes.CAMPFIRE_COSY_SMOKE, cx, cy, cz, 0, driftX * 1.5, upSpeed * 0.85, driftZ * 1.5, 1.0);
                }
            }
            if (!Config.rickPrimeBombBreakBlocks) ** GOTO lbl-1000
            blockRadius = Config.rickPrimeBombRadius;
            centerPos = this.blockPosition();
            r = (int)Math.ceil(blockRadius);
            dx = -r;
            while (true) {
                if (dx <= r) {
                } else lbl-1000:
                // 2 sources

                {
                    blastRadius = (double)Config.rickPrimeBombRadius * 1.15;
                    damageArea = this.getBoundingBox().inflate(blastRadius);
                    victims = this.level().getEntitiesOfClass(LivingEntity.class, damageArea);
                    damageSource = this.damageSources().explosion((Entity)this, null);
                    bombCenter = new Vec3(cx, cy, cz);
                    var13_22 = victims.iterator();
                    break;
                }
                for (dy = -r; dy <= r; ++dy) {
                    for (dz = -r; dz <= r; ++dz) {
                        if (!((double)(dx * dx + dy * dy + dz * dz) <= blockRadius * blockRadius)) continue;
                        targetPos = centerPos.offset(dx, dy, dz);
                        state = this.level().getBlockState(targetPos);
                        if (state.isAir() || !this.shouldDestroyBlock(state)) continue;
                        this.level().destroyBlock(targetPos, true);
                    }
                }
                ++dx;
            }
            while (var13_22.hasNext()) {
                victim = (LivingEntity)var13_22.next();
                dist = victim.position().distanceTo(bombCenter);
                if (!(dist <= blastRadius)) continue;
                if (victim instanceof Player) {
                    player = (Player)victim;
                    if (player.isCreative()) continue;
                    if (Config.rickPrimeBombDisintegrateEntities) {
                        player.hurt(damageSource, 3.4028235E38f);
                        if (player.isAlive()) {
                            player.kill();
                        }
                    } else {
                        damage = (float)((1.0 - dist / blastRadius) * 105.0 + 25.0);
                        player.hurt(damageSource, damage);
                    }
                    if (!player.isAlive()) continue;
                    knockbackDir = player.position().subtract(bombCenter);
                    knockbackDir = knockbackDir.lengthSqr() < 1.0E-4 ? new Vec3(0.0, 1.0, 0.0) : knockbackDir.normalize();
                    force = (1.0 - dist / blastRadius) * 5.2 + 1.2;
                    velocity = knockbackDir.scale(force).add(0.0, 0.65, 0.0);
                    player.setDeltaMovement(player.getDeltaMovement().add(velocity));
                    player.hasImpulse = true;
                    continue;
                }
                if (Config.rickPrimeBombDisintegrateEntities) {
                    victim.discard();
                    continue;
                }
                damage = (float)((1.0 - dist / blastRadius) * 105.0 + 25.0);
                victim.hurt(damageSource, damage);
                knockbackDir = victim.position().subtract(bombCenter);
                knockbackDir = knockbackDir.lengthSqr() < 1.0E-4 ? new Vec3(0.0, 1.0, 0.0) : knockbackDir.normalize();
                force = (1.0 - dist / blastRadius) * 5.2 + 1.2;
                velocity = knockbackDir.scale(force).add(0.0, 0.65, 0.0);
                victim.setDeltaMovement(victim.getDeltaMovement().add(velocity));
                victim.hasImpulse = true;
            }
        }
        this.discard();
    }

    private boolean shouldDestroyBlock(BlockState state) {
        if (state.canBeReplaced()) {
            return true;
        }
        if (state.is(BlockTags.REPLACEABLE)) return true;
        if (state.is(BlockTags.REPLACEABLE_BY_TREES)) {
            return true;
        }
        if (state.is(Tags.Blocks.GLASS_BLOCKS)) return true;
        if (state.is(Tags.Blocks.GLASS_PANES)) return true;
        if (state.is(BlockTags.IMPERMEABLE)) return true;
        if (state.getBlock() instanceof TransparentBlock) return true;
        if (state.getBlock() instanceof StainedGlassBlock) return true;
        if (state.getBlock() instanceof TintedGlassBlock) return true;
        if (state.getBlock() instanceof IronBarsBlock && state.getBlock().getDescriptionId().contains("glass")) {
            return true;
        }
        if (state.is(BlockTags.FLOWERS)) return true;
        if (state.is(BlockTags.TALL_FLOWERS)) return true;
        if (state.is(BlockTags.SMALL_FLOWERS)) {
            return true;
        }
        if (state.is(BlockTags.CROPS)) return true;
        if (state.is(BlockTags.SAPLINGS)) return true;
        if (state.is(BlockTags.LEAVES)) {
            return true;
        }
        if (state.is(BlockTags.ALL_SIGNS)) return true;
        if (state.is(BlockTags.BANNERS)) return true;
        if (state.is(BlockTags.CAMPFIRES)) return true;
        if (state.is(BlockTags.CANDLES)) {
            return true;
        }
        Block block = state.getBlock();
        if (block instanceof TorchBlock) return true;
        if (block instanceof LanternBlock) return true;
        if (block instanceof WebBlock) return true;
        if (block instanceof BushBlock) return true;
        if (block instanceof CarpetBlock) return true;
        if (block instanceof FlowerPotBlock) return true;
        if (!(block instanceof SnowLayerBlock)) return false;
        return true;
    }

    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("Fuse")) {
            this.setFuse(tag.getInt("Fuse"));
        }
        if (tag.contains("IsFlashing")) {
            this.setFlashing(tag.getBoolean("IsFlashing"));
        }
        if (tag.contains("State")) {
            this.setState(tag.getInt("State"));
        }
        if (!tag.contains("IsArmed")) return;
        this.setArmed(tag.getBoolean("IsArmed"));
    }

    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Fuse", this.getFuse());
        tag.putBoolean("IsFlashing", this.isFlashing());
        tag.putInt("State", this.getState());
        tag.putBoolean("IsArmed", this.isArmed());
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket((Entity)this, serverEntity);
    }
}

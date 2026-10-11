/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.nbt.CompoundTag
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
 *  net.minecraft.server.level.ServerEntity
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.MoverType
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.Level$ExplosionInteraction
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  org.ExtraTortik.ricks_stuff.init.ModEntities
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 *  org.ExtraTortik.ricks_stuff.init.ModParticles
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CTeleportOverlayPacket
 *  org.ExtraTortik.ricks_stuff.portal.PortalManager
 */
package org.ExtraTortik.ricks_stuff.entity;

import java.util.Iterator;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.ExtraTortik.ricks_stuff.init.ModEntities;
import org.ExtraTortik.ricks_stuff.init.ModItems;
import org.ExtraTortik.ricks_stuff.init.ModParticles;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.S2CTeleportOverlayPacket;
import org.ExtraTortik.ricks_stuff.portal.PortalManager;

public class SelfDestructGunEntity
extends Entity {
    private static final EntityDataAccessor<Integer> FUSE = SynchedEntityData.defineId(SelfDestructGunEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final EntityDataAccessor<ItemStack> GUN_ITEM = SynchedEntityData.defineId(SelfDestructGunEntity.class, (EntityDataSerializer)EntityDataSerializers.ITEM_STACK);
    public static final int FUSE_START_EXPLOSION = 100;
    public static final int MAX_FUSE = 205;

    public SelfDestructGunEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.blocksBuilding = true;
    }

    public SelfDestructGunEntity(Level level, double x, double y, double z, ItemStack gunStack) {
        this((EntityType)ModEntities.SELF_DESTRUCT_GUN.get(), level);
        this.setPos(x, y, z);
        this.setGunItem(gunStack);
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(FUSE, (Object)0);
        builder.define(GUN_ITEM, (Object)new ItemStack((ItemLike)ModItems.RICK_PORTAL_GUN.get()));
    }

    public int getFuse() {
        return (Integer)this.entityData.get(FUSE);
    }

    public void setFuse(int fuse) {
        this.entityData.set(FUSE, (Object)fuse);
    }

    public ItemStack getGunItem() {
        return (ItemStack)this.entityData.get(GUN_ITEM);
    }

    public void setGunItem(ItemStack stack) {
        this.entityData.set(GUN_ITEM, (Object)stack.copy());
    }

    public float getDomeRadius(float partialTicks) {
        float f = (float)this.getFuse() + partialTicks;
        if (f < 35.0f) {
            return 0.0f;
        }
        if (f < 85.0f) {
            float progress = (f - 35.0f) / 50.0f;
            return (float)Math.sin((double)progress * Math.PI * 0.5) * 6.5f;
        }
        if (f < 95.0f) {
            float progress = (f - 85.0f) / 10.0f;
            return 6.5f - progress * 1.5f;
        }
        float progress = (f - 95.0f) / 5.0f;
        return 5.0f + progress * 4.0f;
    }

    public void tick() {
        Level level;
        super.tick();
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.04, 0.0));
        }
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.92));
        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.7, -0.5, 0.7));
        }
        int currentFuse = this.getFuse() + 1;
        this.setFuse(currentFuse);
        if (currentFuse == 1 && !this.level().isClientSide() && (level = this.level()) instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            int gunId = PortalGunItem.getGunId((ItemStack)this.getGunItem());
            PortalManager.removeAllPortalsForGun((ServerLevel)serverLevel, (int)gunId);
        }
        if (currentFuse < 100) {
            int interval = Math.max(2, 10 - currentFuse / 12);
            if (currentFuse % interval == 0) {
                float pitch = 0.9f + (float)currentFuse / 100.0f * 0.9f;
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.GUN_MALFUNCTION.get(), SoundSource.NEUTRAL, 0.3f, pitch);
            }
            if (!this.level().isClientSide()) return;
            int sparkCount = 1 + currentFuse / 35;
            this.spawnSparks((ParticleOptions)ModParticles.GREEN_DIMENSIONAL_SPARK.get(), sparkCount);
        } else if (currentFuse == 100) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.PORTAL_OPEN.get(), SoundSource.NEUTRAL, 2.5f, 1.4f);
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.PLASMA_SCORCH.get(), SoundSource.NEUTRAL, 2.5f, 0.9f);
        } else if (currentFuse > 100 && currentFuse < 160) {
            if (currentFuse % 6 == 0) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.PORTAL_TELEPORT.get(), SoundSource.NEUTRAL, 1.5f, 0.7f + (float)(currentFuse - 100) * 0.015f);
            }
            float prog = ((float)currentFuse - 115.0f) / 45.0f;
            double orbY = this.getY() + 0.5 + (double)Math.max(0.0f, prog) * 2.6;
            this.pullEntitiesTowards(new Vec3(this.getX(), orbY, this.getZ()), 12.0, 0.14);
            if (!this.level().isClientSide()) return;
            if (currentFuse < 115) return;
            this.spawnVortexParticles(orbY);
        } else if (currentFuse >= 160 && currentFuse < 185) {
            if (currentFuse % 4 == 0) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.PORTAL_CLOSE.get(), SoundSource.NEUTRAL, 1.8f, 1.2f + (float)(currentFuse - 160) * 0.03f);
            }
            this.pullEntitiesTowards(this.position().add(0.0, 0.2, 0.0), 15.0, 0.22);
            if (!this.level().isClientSide()) return;
            this.spawnFunnelParticles();
        } else if (currentFuse >= 185 && currentFuse < 205) {
            if (currentFuse == 185) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.GUN_FIRE.get(), SoundSource.NEUTRAL, 2.5f, 0.5f);
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.PLASMA_SCORCH.get(), SoundSource.NEUTRAL, 2.5f, 1.2f);
            }
            if (!this.level().isClientSide()) return;
            this.spawnHyperBeamParticles();
        } else {
            if (currentFuse < 205) return;
            if (!this.level().isClientSide()) {
                this.explodeSupernova();
            }
            this.discard();
        }
    }

    private void pullEntitiesTowards(Vec3 center, double pullRadius, double strengthFactor) {
        AABB area = this.getBoundingBox().inflate(pullRadius);
        List targets = this.level().getEntitiesOfClass(LivingEntity.class, area);
        Iterator iterator = targets.iterator();
        while (iterator.hasNext()) {
            LivingEntity target = (LivingEntity)iterator.next();
            Vec3 toCenter = center.subtract(target.position());
            double dist = toCenter.length();
            if (!(dist > 0.5) || !(dist <= pullRadius)) continue;
            double strength = (1.0 - dist / pullRadius) * strengthFactor;
            Vec3 pull = toCenter.normalize().scale(strength);
            target.setDeltaMovement(target.getDeltaMovement().add(pull));
            target.hasImpulse = true;
        }
    }

    private void spawnFunnelParticles() {
        int i = 0;
        while (i < 6) {
            double angle = this.random.nextDouble() * Math.PI * 2.0;
            double r = 0.5 + this.random.nextDouble() * 2.0;
            double px = this.getX() + Math.cos(angle) * r;
            double py = this.getY() + 0.2 + this.random.nextDouble() * 2.5;
            double pz = this.getZ() + Math.sin(angle) * r;
            double vx = (this.getX() - px) * 0.2;
            double vy = (this.getY() + 0.2 - py) * 0.2;
            double vz = (this.getZ() - pz) * 0.2;
            this.level().addParticle((ParticleOptions)ModParticles.GREEN_DIMENSIONAL_SPARK.get(), px, py, pz, vx, vy, vz);
            ++i;
        }
    }

    private void spawnHyperBeamParticles() {
        int i = 0;
        while (i < 15) {
            double rx = (this.random.nextDouble() - 0.5) * 4.0;
            double ry = this.random.nextDouble() * 8.0;
            double rz = (this.random.nextDouble() - 0.5) * 4.0;
            this.level().addParticle((ParticleOptions)ModParticles.GREEN_DIMENSIONAL_SPARK.get(), this.getX() + rx, this.getY() + ry, this.getZ() + rz, rx * 0.5, 0.4 + this.random.nextDouble() * 0.6, rz * 0.5);
            if (this.random.nextFloat() < 0.4f) {
                this.level().addParticle((ParticleOptions)ModParticles.GREEN_PLASMA_SHOCKWAVE.get(), this.getX() + rx * 0.5, this.getY() + ry * 0.5, this.getZ() + rz * 0.5, 0.0, 0.0, 0.0);
            }
            ++i;
        }
    }

    private void spawnSparks(ParticleOptions particle, int count) {
        int i = 0;
        while (i < count) {
            double rx = (this.random.nextDouble() - 0.5) * 0.4;
            double ry = this.random.nextDouble() * 0.3;
            double rz = (this.random.nextDouble() - 0.5) * 0.4;
            this.level().addParticle(particle, this.getX() + rx, this.getY() + ry, this.getZ() + rz, rx * 2.0, 0.1 + ry * 2.0, rz * 2.0);
            ++i;
        }
    }

    private void spawnVortexParticles(double orbY) {
        float radius = 2.2f;
        int particleCount = 2;
        int i = 0;
        while (i < particleCount) {
            double angle = this.random.nextDouble() * Math.PI * 2.0;
            double h = (this.random.nextDouble() - 0.5) * (double)radius * 1.5;
            double r = Math.sqrt(Math.max(0.0, (double)(radius * radius) - h * h));
            double px = this.getX() + Math.cos(angle) * r;
            double py = orbY + h;
            double pz = this.getZ() + Math.sin(angle) * r;
            double vx = -Math.sin(angle) * 0.3 - (px - this.getX()) * 0.05;
            double vy = 0.02;
            double vz = Math.cos(angle) * 0.3 - (pz - this.getZ()) * 0.05;
            this.level().addParticle((ParticleOptions)ModParticles.GREEN_VORTEX_BEAM.get(), px, py, pz, vx, vy, vz);
            if (this.random.nextFloat() < 0.3f) {
                this.level().addParticle((ParticleOptions)ModParticles.GREEN_PLASMA_SHOCKWAVE.get(), px, py, pz, 0.0, 0.0, 0.0);
            }
            if (this.random.nextFloat() < 0.2f) {
                this.level().addParticle((ParticleOptions)ModParticles.FLUID_EVAPORATION.get(), px, py, pz, 0.0, 0.1, 0.0);
            }
            ++i;
        }
    }

    private void explodeSupernova() {
        Vec3 center = this.position().add(0.0, 0.5, 0.0);
        double blastRadius = 8.5;
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (Holder)SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0f, 0.6f);
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)ModSounds.PLASMA_SCORCH.get(), SoundSource.BLOCKS, 4.0f, 0.5f);
        long whistleDurationMs = 5660L;
        int slownessTicks = 114;
        for (ServerPlayer player : this.level().getEntitiesOfClass(ServerPlayer.class, this.getBoundingBox().inflate(64.0))) {
            Vec3 toCenter = center.subtract(player.getEyePosition()).normalize();
            Vec3 look = player.getLookAngle().normalize();
            double dot = look.dot(toCenter);
            boolean hasLineOfSight = player.hasLineOfSight((Entity)this);
            if (hasLineOfSight && dot > 0.25) {
                ModMessages.sendToPlayer((CustomPacketPayload)new S2CTeleportOverlayPacket(99, whistleDurationMs), (ServerPlayer)player);
                player.playNotifySound((SoundEvent)ModSounds.EXPLOSION_WISTLE.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, slownessTicks, 3, false, false, true));
                continue;
            }
            if (!(player.position().distanceTo(center) <= 20.0)) continue;
            ModMessages.sendToPlayer((CustomPacketPayload)new S2CTeleportOverlayPacket(99, 800L), (ServerPlayer)player);
        }
        AABB damageArea = this.getBoundingBox().inflate(blastRadius);
        List victims = this.level().getEntitiesOfClass(LivingEntity.class, damageArea);
        DamageSource damageSource = this.damageSources().explosion((Entity)this, null);
        Iterator iterator = victims.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                this.level().explode((Entity)this, this.getX(), this.getY(), this.getZ(), 4.5f, Level.ExplosionInteraction.MOB);
                return;
            }
            LivingEntity victim = (LivingEntity)iterator.next();
            double dist = victim.position().distanceTo(center);
            if (!(dist <= blastRadius)) continue;
            float damage = (float)((1.0 - dist / blastRadius) * 60.0 + 10.0);
            victim.hurt(damageSource, damage);
            Vec3 knockback = victim.position().subtract(center).normalize().scale((1.0 - dist / blastRadius) * 2.5);
            victim.setDeltaMovement(victim.getDeltaMovement().add(knockback));
            victim.hasImpulse = true;
        }
    }

    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("Fuse")) {
            this.setFuse(tag.getInt("Fuse"));
        }
        if (!tag.contains("GunItem")) return;
        ItemStack.parse((HolderLookup.Provider)this.registryAccess(), (Tag)tag.getCompound("GunItem")).ifPresent(this::setGunItem);
    }

    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Fuse", this.getFuse());
        tag.put("GunItem", this.getGunItem().save((HolderLookup.Provider)this.registryAccess()));
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket((Entity)this, serverEntity);
    }
}

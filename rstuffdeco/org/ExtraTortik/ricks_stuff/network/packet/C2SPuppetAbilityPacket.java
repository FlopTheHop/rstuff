/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.animal.IronGolem
 *  net.minecraft.world.entity.boss.enderdragon.EnderDragon
 *  net.minecraft.world.entity.boss.wither.WitherBoss
 *  net.minecraft.world.entity.monster.AbstractSkeleton
 *  net.minecraft.world.entity.monster.Blaze
 *  net.minecraft.world.entity.monster.Creeper
 *  net.minecraft.world.entity.monster.EnderMan
 *  net.minecraft.world.entity.monster.Evoker
 *  net.minecraft.world.entity.monster.Ghast
 *  net.minecraft.world.entity.monster.Pillager
 *  net.minecraft.world.entity.monster.Spider
 *  net.minecraft.world.entity.monster.Witch
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.projectile.Arrow
 *  net.minecraft.world.entity.projectile.DragonFireball
 *  net.minecraft.world.entity.projectile.EvokerFangs
 *  net.minecraft.world.entity.projectile.LargeFireball
 *  net.minecraft.world.entity.projectile.SmallFireball
 *  net.minecraft.world.entity.projectile.ThrownPotion
 *  net.minecraft.world.entity.projectile.WitherSkull
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.alchemy.PotionContents
 *  net.minecraft.world.item.alchemy.Potions
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.ExtraTortik.ricks_stuff.compat.curios.CuriosCompat
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.compat.curios.CuriosCompat;

public record C2SPuppetAbilityPacket(int mobId, float yaw, float pitch) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2SPuppetAbilityPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"c2s_puppet_ability"));
    public static final StreamCodec<FriendlyByteBuf, C2SPuppetAbilityPacket> STREAM_CODEC = StreamCodec.ofMember(C2SPuppetAbilityPacket::write, C2SPuppetAbilityPacket::new);

    public C2SPuppetAbilityPacket(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readFloat(), buf.readFloat());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.mobId);
        buf.writeFloat(this.yaw);
        buf.writeFloat(this.pitch);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(C2SPuppetAbilityPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            long last;
            UUID owner;
            Player patt0$temp = context.player();
            if (!(patt0$temp instanceof ServerPlayer)) return;
            ServerPlayer sender = (ServerPlayer)patt0$temp;
            Level patt1$temp = sender.level();
            if (!(patt1$temp instanceof ServerLevel)) return;
            ServerLevel level = (ServerLevel)patt1$temp;
            if (!CuriosCompat.isWearingEyePatch((LivingEntity)sender)) {
                return;
            }
            Entity entity = level.getEntity(packet.mobId());
            if (!(entity instanceof Mob)) return;
            Mob mob = (Mob)entity;
            if (!mob.isAlive()) {
                return;
            }
            CompoundTag data = mob.getPersistentData();
            if (!data.getBoolean("BioNeuralChipped")) {
                return;
            }
            UUID uUID = owner = data.hasUUID("BioNeuralOwner") ? data.getUUID("BioNeuralOwner") : null;
            if (!sender.getUUID().equals(owner)) {
                return;
            }
            if (sender.distanceToSqr((Entity)mob) > 4096.0) {
                return;
            }
            long now = level.getGameTime();
            if (now - (last = data.getLong("LastAbilityTick")) < 20L && !(mob instanceof AbstractSkeleton) && !(mob instanceof Pillager)) {
                return;
            }
            data.putLong("LastAbilityTick", now);
            float targetYaw = packet.yaw();
            if (mob instanceof EnderDragon) {
                targetYaw += 180.0f;
            }
            mob.setYRot(targetYaw);
            mob.setXRot(packet.pitch());
            mob.setYHeadRot(targetYaw);
            mob.yBodyRot = targetYaw;
            C2SPuppetAbilityPacket.executeMobAbility(level, sender, mob);
        });
    }

    private static void executeMobAbility(ServerLevel level, ServerPlayer sender, Mob mob) {
        Vec3 look = mob.getViewVector(1.0f);
        if (mob instanceof Creeper) {
            Creeper creeper = (Creeper)mob;
            creeper.ignite();
            level.playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.CREEPER_PRIMED, SoundSource.HOSTILE, 1.2f, 1.0f);
        } else if (mob instanceof AbstractSkeleton) {
            CompoundTag mobData;
            boolean isDrawing;
            AbstractSkeleton skeleton = (AbstractSkeleton)mob;
            if (!skeleton.getMainHandItem().is(Items.BOW)) {
                skeleton.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack((ItemLike)Items.BOW));
            }
            if (!(isDrawing = (mobData = skeleton.getPersistentData()).getBoolean("DrawingBow"))) {
                skeleton.startUsingItem(InteractionHand.MAIN_HAND);
                mobData.putBoolean("DrawingBow", true);
                mobData.putLong("BowDrawStart", level.getGameTime());
                level.playSound(null, skeleton.getX(), skeleton.getY(), skeleton.getZ(), SoundEvents.CROSSBOW_LOADING_START, SoundSource.HOSTILE, 0.6f, 0.8f);
            } else {
                long drawTicks = level.getGameTime() - mobData.getLong("BowDrawStart");
                float power = Math.min(1.0f, Math.max(0.25f, (float)drawTicks / 20.0f));
                Arrow arrow = new Arrow((Level)level, (LivingEntity)skeleton, new ItemStack((ItemLike)Items.ARROW), skeleton.getMainHandItem());
                arrow.setPos(skeleton.getX(), skeleton.getEyeY() - 0.1, skeleton.getZ());
                arrow.shoot(look.x, look.y, look.z, power * 2.8f, 1.0f);
                if (power >= 0.85f) {
                    arrow.setCritArrow(true);
                }
                level.addFreshEntity((Entity)arrow);
                level.playSound(null, skeleton.getX(), skeleton.getY(), skeleton.getZ(), SoundEvents.SKELETON_SHOOT, SoundSource.HOSTILE, 1.0f, 1.0f / (level.random.nextFloat() * 0.4f + 0.8f));
                skeleton.stopUsingItem();
                mobData.remove("DrawingBow");
                mobData.remove("BowDrawStart");
                mobData.putLong("LastAbilityTick", level.getGameTime());
            }
        } else if (mob instanceof Pillager) {
            CompoundTag mobData;
            boolean isDrawing;
            Pillager pillager = (Pillager)mob;
            if (!pillager.getMainHandItem().is(Items.CROSSBOW)) {
                pillager.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack((ItemLike)Items.CROSSBOW));
            }
            if (!(isDrawing = (mobData = pillager.getPersistentData()).getBoolean("DrawingBow"))) {
                pillager.startUsingItem(InteractionHand.MAIN_HAND);
                mobData.putBoolean("DrawingBow", true);
                mobData.putLong("BowDrawStart", level.getGameTime());
                level.playSound(null, pillager.getX(), pillager.getY(), pillager.getZ(), SoundEvents.CROSSBOW_LOADING_START, SoundSource.HOSTILE, 1.0f, 1.0f);
            } else {
                Arrow arrow = new Arrow((Level)level, (LivingEntity)pillager, new ItemStack((ItemLike)Items.ARROW), pillager.getMainHandItem());
                arrow.setPos(pillager.getX(), pillager.getEyeY() - 0.1, pillager.getZ());
                arrow.shoot(look.x, look.y, look.z, 3.1f, 1.0f);
                level.addFreshEntity((Entity)arrow);
                pillager.stopUsingItem();
                mobData.remove("DrawingBow");
                mobData.remove("BowDrawStart");
                mobData.putLong("LastAbilityTick", level.getGameTime());
            }
        } else if (mob instanceof Witch) {
            ThrownPotion potion = new ThrownPotion((Level)level, (LivingEntity)mob);
            potion.setItem(PotionContents.createItemStack((Item)Items.SPLASH_POTION, (Holder)Potions.HARMING));
            potion.setPos(mob.getX(), mob.getEyeY(), mob.getZ());
            potion.shoot(look.x, look.y + 0.2, look.z, 1.1f, 1.0f);
            level.addFreshEntity((Entity)potion);
            level.playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.WITCH_THROW, SoundSource.HOSTILE, 1.0f, 1.0f);
        } else if (mob instanceof EnderMan) {
            double targetX = mob.getX() + look.x * 12.0;
            double targetY = mob.getY() + look.y * 12.0;
            double targetZ = mob.getZ() + look.z * 12.0;
            mob.teleportTo(targetX, targetY, targetZ);
            level.sendParticles((ParticleOptions)ParticleTypes.PORTAL, mob.getX(), mob.getY() + 1.0, mob.getZ(), 35, 0.5, 0.5, 0.5, 0.1);
            level.playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.HOSTILE, 1.0f, 1.0f);
        } else if (mob instanceof Blaze) {
            SmallFireball fireball = new SmallFireball((Level)level, (LivingEntity)mob, new Vec3(look.x, look.y, look.z));
            fireball.setPos(mob.getX(), mob.getEyeY(), mob.getZ());
            level.addFreshEntity((Entity)fireball);
            level.playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.BLAZE_SHOOT, SoundSource.HOSTILE, 1.0f, 1.0f);
        } else if (mob instanceof Ghast) {
            LargeFireball fireball = new LargeFireball((Level)level, (LivingEntity)mob, new Vec3(look.x, look.y, look.z), 1);
            fireball.setPos(mob.getX() + look.x * 2.0, mob.getEyeY(), mob.getZ() + look.z * 2.0);
            level.addFreshEntity((Entity)fireball);
            level.playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.GHAST_SHOOT, SoundSource.HOSTILE, 1.0f, 1.0f);
        } else {
            if (mob instanceof IronGolem) {
                level.sendParticles((ParticleOptions)ParticleTypes.EXPLOSION, mob.getX(), mob.getY(), mob.getZ(), 4, 1.0, 0.2, 1.0, 0.0);
                level.playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.IRON_GOLEM_ATTACK, SoundSource.NEUTRAL, 1.4f, 0.8f);
                List enemies = level.getEntitiesOfClass(LivingEntity.class, mob.getBoundingBox().inflate(5.0), e -> e != mob && e != sender && e.isAlive());
                Iterator isDrawing = enemies.iterator();
                while (isDrawing.hasNext()) {
                    LivingEntity enemy = (LivingEntity)isDrawing.next();
                    enemy.hurt(level.damageSources().mobAttack((LivingEntity)mob), 10.0f);
                    enemy.setDeltaMovement(enemy.getDeltaMovement().add(0.0, 0.6, 0.0));
                    enemy.hasImpulse = true;
                }
                return;
            }
            if (mob instanceof Spider) {
                mob.setDeltaMovement(look.x * 1.4, 0.52, look.z * 1.4);
                mob.hasImpulse = true;
                level.playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.SPIDER_HURT, SoundSource.HOSTILE, 1.0f, 1.2f);
            } else if (mob instanceof Evoker) {
                for (int i = 1; i <= 6; ++i) {
                    double fx = mob.getX() + look.x * (double)i * 1.6;
                    double fz = mob.getZ() + look.z * (double)i * 1.6;
                    double fy = mob.getY();
                    EvokerFangs fangs = new EvokerFangs((Level)level, fx, fy, fz, mob.getYRot(), i * 2, (LivingEntity)mob);
                    level.addFreshEntity((Entity)fangs);
                }
                level.playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.HOSTILE, 1.0f, 1.0f);
            } else if (mob instanceof EnderDragon) {
                EnderDragon dragon = (EnderDragon)mob;
                DragonFireball fireball = new DragonFireball((Level)level, (LivingEntity)dragon, new Vec3(look.x, look.y, look.z));
                fireball.setPos(dragon.head.getX() + look.x * 2.0, dragon.head.getY(), dragon.head.getZ() + look.z * 2.0);
                level.addFreshEntity((Entity)fireball);
                level.playSound(null, dragon.getX(), dragon.getY(), dragon.getZ(), SoundEvents.ENDER_DRAGON_SHOOT, SoundSource.HOSTILE, 2.5f, 1.0f);
            } else {
                if (!(mob instanceof WitherBoss)) return;
                WitherBoss wither = (WitherBoss)mob;
                WitherSkull skull = new WitherSkull((Level)level, (LivingEntity)wither, new Vec3(look.x, look.y, look.z));
                skull.setPos(wither.getX() + look.x * 1.5, wither.getEyeY() + 0.1, wither.getZ() + look.z * 1.5);
                if (mob.isSprinting()) {
                    skull.setDangerous(true);
                }
                level.addFreshEntity((Entity)skull);
                level.playSound(null, wither.getX(), wither.getY(), wither.getZ(), SoundEvents.WITHER_SHOOT, SoundSource.HOSTILE, 1.5f, 1.0f);
            }
        }
    }
}

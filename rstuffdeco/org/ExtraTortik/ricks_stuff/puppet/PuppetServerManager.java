/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.goal.WrappedGoal
 *  net.minecraft.world.entity.boss.enderdragon.EnderDragon
 *  net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase
 *  net.minecraft.world.entity.boss.wither.WitherBoss
 *  net.minecraft.world.entity.monster.MagmaCube
 *  net.minecraft.world.entity.monster.Slime
 *  net.minecraft.world.entity.projectile.ProjectileUtil
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.server.ServerLifecycleHooks
 *  org.ExtraTortik.ricks_stuff.compat.curios.CuriosCompat
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CPuppetDisconnectPacket
 *  org.ExtraTortik.ricks_stuff.puppet.PuppetServerManager$PuppetInput
 *  org.ExtraTortik.ricks_stuff.puppet.PuppetServerManager$PuppetSession
 */
package org.ExtraTortik.ricks_stuff.puppet;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.ExtraTortik.ricks_stuff.compat.curios.CuriosCompat;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.S2CPuppetDisconnectPacket;
import org.ExtraTortik.ricks_stuff.puppet.PuppetServerManager;

public class PuppetServerManager {
    private static final Map<Integer, PuppetSession> sessionsByMob = new ConcurrentHashMap<Integer, PuppetSession>();
    private static final Map<UUID, Integer> mobByPlayer = new ConcurrentHashMap<UUID, Integer>();

    public static boolean isPossessed(Mob mob) {
        if (mob == null) return false;
        if (mob.isAlive()) return sessionsByMob.containsKey(mob.getId());
        return false;
    }

    public static boolean isPossessed(int mobId) {
        return sessionsByMob.containsKey(mobId);
    }

    @Nullable
    public static PuppetSession getSession(int mobId) {
        return sessionsByMob.get(mobId);
    }

    @Nullable
    public static PuppetSession getSessionForPlayer(UUID playerUuid) {
        Integer mobId = mobByPlayer.get(playerUuid);
        if (playerUuid != null) return mobId != null ? sessionsByMob.get(mobId) : null;
        return null;
    }

    public static void startPossession(ServerPlayer player, int mobId) {
        UUID owner;
        if (player == null) return;
        if (!player.isAlive()) {
            return;
        }
        Level level = player.level();
        if (!(level instanceof ServerLevel)) return;
        ServerLevel serverLevel = (ServerLevel)level;
        if (!CuriosCompat.isWearingEyePatch((LivingEntity)player)) {
            return;
        }
        Entity entity = serverLevel.getEntity(mobId);
        if (!(entity instanceof Mob)) return;
        Mob mob = (Mob)entity;
        if (!mob.isAlive()) {
            return;
        }
        if (player.distanceToSqr((Entity)mob) > 4096.0) {
            return;
        }
        CompoundTag data = mob.getPersistentData();
        if (!data.getBoolean("BioNeuralChipped")) {
            return;
        }
        UUID uUID = owner = data.hasUUID("BioNeuralOwner") ? data.getUUID("BioNeuralOwner") : null;
        if (!player.getUUID().equals(owner)) {
            return;
        }
        Integer prevMobId = mobByPlayer.get(player.getUUID());
        if (prevMobId != null) {
            PuppetServerManager.disconnect(prevMobId);
        }
        float origMaxUpStep = mob.maxUpStep();
        if (mob.getAttribute(Attributes.STEP_HEIGHT) != null) {
            mob.getAttribute(Attributes.STEP_HEIGHT).setBaseValue(1.0);
        }
        mob.setTarget(null);
        if (!mob.getNavigation().isDone()) {
            mob.getNavigation().stop();
        }
        for (WrappedGoal goal : mob.goalSelector.getAvailableGoals()) {
            if (!goal.isRunning()) continue;
            goal.stop();
        }
        if (mob instanceof EnderDragon) {
            EnderDragon dragon = (EnderDragon)mob;
            dragon.getPhaseManager().setPhase(EnderDragonPhase.HOVERING);
        }
        long now = serverLevel.getGameTime();
        PuppetSession session = new PuppetSession(player.getUUID(), mobId, null, origMaxUpStep, now);
        sessionsByMob.put(mobId, session);
        mobByPlayer.put(player.getUUID(), mobId);
        data.putLong("LastPossessedTick", now);
        data.putUUID("PossessingPlayerUUID", player.getUUID());
    }

    public static void handleControlPacket(ServerPlayer sender, int mobId, float forward, float strafe, boolean jump, boolean sprint, boolean sneak, float yaw, float pitch, boolean attack) {
        UUID owner;
        if (sender == null) {
            return;
        }
        Level level = sender.level();
        if (!(level instanceof ServerLevel)) return;
        ServerLevel serverLevel = (ServerLevel)level;
        if (!CuriosCompat.isWearingEyePatch((LivingEntity)sender)) {
            return;
        }
        Entity entity = serverLevel.getEntity(mobId);
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
        long now = serverLevel.getGameTime();
        PuppetInput input = new PuppetInput(forward, strafe, jump, sprint, sneak, yaw, pitch, now);
        PuppetSession session = sessionsByMob.get(mobId);
        if (session == null) {
            Integer prevMobId = mobByPlayer.get(sender.getUUID());
            if (prevMobId != null && prevMobId != mobId) {
                PuppetServerManager.disconnect(prevMobId);
            }
            float origMaxUpStep = mob.maxUpStep();
            session = new PuppetSession(sender.getUUID(), mobId, input, origMaxUpStep, now);
            sessionsByMob.put(mobId, session);
            mobByPlayer.put(sender.getUUID(), mobId);
        } else {
            session.latestInput = input;
            session.lastActiveGameTime = now;
        }
        if (mob.getAttribute(Attributes.STEP_HEIGHT) != null) {
            mob.getAttribute(Attributes.STEP_HEIGHT).setBaseValue(1.0);
        }
        data.putLong("LastPossessedTick", now);
        data.putUUID("PossessingPlayerUUID", sender.getUUID());
        if (!attack) return;
        PuppetServerManager.handleAttack(sender, mob);
    }

    private static void handleAttack(ServerPlayer sender, Mob mob) {
        mob.swing(InteractionHand.MAIN_HAND);
        PuppetSession session = sessionsByMob.get(mob.getId());
        PuppetInput input = session != null ? session.latestInput : null;
        Vec3 eye = mob.getEyePosition();
        Vec3 look = mob instanceof EnderDragon && input != null ? Vec3.directionFromRotation((float)input.pitch(), (float)input.yaw()).scale(8.0) : mob.getViewVector(1.0f).scale(3.0);
        AABB box = mob.getBoundingBox().expandTowards(look).inflate(1.0);
        EntityHitResult hit = ProjectileUtil.getEntityHitResult((Level)mob.level(), (Entity)mob, (Vec3)eye, (Vec3)eye.add(look), (AABB)box, e -> e != mob && e != sender && e.isAlive());
        if (!(hit instanceof EntityHitResult)) return;
        EntityHitResult entityHit = hit;
        Entity entity = entityHit.getEntity();
        if (!(entity instanceof LivingEntity)) return;
        LivingEntity victim = (LivingEntity)entity;
        if (mob.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE)) {
            mob.doHurtTarget((Entity)victim);
        } else if (mob instanceof EnderDragon) {
            victim.hurt(mob.damageSources().mobAttack((LivingEntity)mob), 10.0f);
        } else {
            victim.hurt(mob.damageSources().mobAttack((LivingEntity)mob), 2.0f);
        }
    }

    public static void tickPuppet(Mob mob) {
        boolean noEyePatch;
        MinecraftServer server;
        if (mob == null) return;
        if (!mob.isAlive()) {
            return;
        }
        PuppetSession session = sessionsByMob.get(mob.getId());
        if (session == null) {
            return;
        }
        mob.setTarget(null);
        if (!mob.getNavigation().isDone()) {
            mob.getNavigation().stop();
        }
        if ((server = mob.getServer()) == null) {
            server = ServerLifecycleHooks.getCurrentServer();
        }
        ServerPlayer player = server != null ? server.getPlayerList().getPlayer(session.playerUuid) : null;
        long gameTime = mob.level().getGameTime();
        boolean timedOut = gameTime - session.lastActiveGameTime > 30L;
        boolean playerInvalid = player == null || !player.isAlive() || player.level() != mob.level();
        boolean distanceTooFar = player != null && player.distanceToSqr((Entity)mob) > 4096.0;
        boolean bl = noEyePatch = player != null && !CuriosCompat.isWearingEyePatch((LivingEntity)player);
        if (timedOut || playerInvalid || distanceTooFar || noEyePatch) {
            PuppetServerManager.disconnect(mob.getId(), mob);
            return;
        }
        PuppetInput input = session.latestInput;
        if (input == null) {
            return;
        }
        float targetYaw = input.yaw();
        if (mob instanceof EnderDragon) {
            targetYaw += 180.0f;
        }
        mob.setYRot(targetYaw);
        mob.setXRot(input.pitch());
        mob.setYHeadRot(targetYaw);
        mob.yBodyRot = targetYaw;
        mob.yRotO = targetYaw;
        mob.xRotO = input.pitch();
        mob.yHeadRotO = targetYaw;
        mob.yBodyRotO = targetYaw;
        mob.setSprinting(input.sprint());
        mob.setShiftKeyDown(input.sneak());
        double baseSpeed = mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
        if (input.sneak()) {
            baseSpeed *= 0.65;
        }
        if (mob.isUsingItem()) {
            baseSpeed *= 0.5;
        }
        mob.setSpeed((float)baseSpeed);
        if (mob instanceof Slime) {
            boolean hasMovement;
            Slime slime = (Slime)mob;
            mob.xxa = 0.0f;
            mob.zza = 0.0f;
            boolean bl2 = hasMovement = input.forward() != 0.0f || input.strafe() != 0.0f;
            if (mob.onGround() && (hasMovement || input.jump())) {
                double jumpPower = input.jump() ? 0.46 + (double)mob.getJumpBoostPower() : 0.32 + Math.min(0.2, (double)slime.getSize() * 0.04) + (double)mob.getJumpBoostPower();
                double rad = Math.toRadians(input.yaw());
                double sin = Math.sin(rad);
                double cos = Math.cos(rad);
                double hSpeed = baseSpeed * (input.sprint() ? 1.2 : 0.85);
                double mx = ((double)input.strafe() * cos - (double)input.forward() * sin) * hSpeed;
                double mz = ((double)input.forward() * cos + (double)input.strafe() * sin) * hSpeed;
                mob.setDeltaMovement(mx, jumpPower, mz);
                mob.hasImpulse = true;
                slime.targetSquish = -0.5f;
                PuppetServerManager.playSlimeJumpSound(slime);
            } else if (!mob.onGround() && hasMovement) {
                double rad = Math.toRadians(input.yaw());
                double sin = Math.sin(rad);
                double cos = Math.cos(rad);
                double airFactor = 0.015;
                double ax = ((double)input.strafe() * cos - (double)input.forward() * sin) * airFactor;
                double az = ((double)input.forward() * cos + (double)input.strafe() * sin) * airFactor;
                mob.setDeltaMovement(mob.getDeltaMovement().add(ax, 0.0, az));
            } else if (mob.onGround() && !hasMovement && !input.jump()) {
                Vec3 delta = mob.getDeltaMovement();
                if (delta.x != 0.0 || delta.z != 0.0) {
                    mob.setDeltaMovement(0.0, delta.y, 0.0);
                }
            }
            if (!mob.isInWater()) {
                if (!mob.isInLava()) return;
            }
            if (!input.jump()) return;
            mob.setDeltaMovement(mob.getDeltaMovement().add(0.0, 0.04, 0.0));
        } else if (mob instanceof EnderDragon) {
            boolean hasInput;
            EnderDragon dragon = (EnderDragon)mob;
            mob.xxa = 0.0f;
            mob.zza = 0.0f;
            double forward = input.forward();
            double strafe = input.strafe();
            double flightSpeed = input.sprint() ? 0.75 : 0.45;
            double rad = Math.toRadians(input.yaw());
            double sin = Math.sin(rad);
            double cos = Math.cos(rad);
            double vx = (strafe * cos - forward * sin) * flightSpeed;
            double vz = (forward * cos + strafe * sin) * flightSpeed;
            double vy = 0.0;
            if (forward != 0.0) {
                double pitchRad = Math.toRadians(input.pitch());
                vy -= Math.sin(pitchRad) * forward * flightSpeed;
            }
            if (input.jump()) {
                vy += 0.3;
            }
            if (input.sneak()) {
                vy -= 0.3;
            }
            boolean bl3 = hasInput = forward != 0.0 || strafe != 0.0 || input.jump() || input.sneak();
            if (hasInput) {
                dragon.setDeltaMovement(vx, vy, vz);
                dragon.hasImpulse = true;
                dragon.flapTime += 0.08f;
            } else {
                Vec3 current = dragon.getDeltaMovement();
                if (current.lengthSqr() > 0.001) {
                    dragon.setDeltaMovement(current.scale(0.5));
                } else {
                    dragon.setDeltaMovement(Vec3.ZERO);
                }
                dragon.flapTime += 0.03f;
            }
        } else if (mob instanceof WitherBoss) {
            boolean hasInput;
            WitherBoss wither = (WitherBoss)mob;
            mob.xxa = 0.0f;
            mob.zza = 0.0f;
            double forward = input.forward();
            double strafe = input.strafe();
            double flightSpeed = input.sprint() ? 0.48 : 0.3;
            double rad = Math.toRadians(input.yaw());
            double sin = Math.sin(rad);
            double cos = Math.cos(rad);
            double mx = (strafe * cos - forward * sin) * flightSpeed;
            double mz = (forward * cos + strafe * sin) * flightSpeed;
            double my = 0.0;
            if (forward != 0.0) {
                double pitchRad = Math.toRadians(input.pitch());
                my -= Math.sin(pitchRad) * forward * flightSpeed * 0.8;
            }
            if (input.jump()) {
                my += 0.25;
            } else if (input.sneak()) {
                my -= 0.25;
            }
            boolean bl4 = hasInput = forward != 0.0 || strafe != 0.0 || input.jump() || input.sneak();
            if (hasInput) {
                wither.setDeltaMovement(mx, my, mz);
                wither.hasImpulse = true;
            } else {
                Vec3 current = wither.getDeltaMovement();
                if (current.lengthSqr() > 0.001) {
                    wither.setDeltaMovement(current.scale(0.5));
                } else {
                    wither.setDeltaMovement(Vec3.ZERO);
                }
            }
        } else {
            mob.xxa = input.strafe() * (float)baseSpeed;
            mob.zza = input.forward() * (float)baseSpeed;
            if (input.jump()) {
                if (mob.isInWater() || mob.isInLava()) {
                    mob.setDeltaMovement(mob.getDeltaMovement().add(0.0, 0.04, 0.0));
                } else {
                    if (!mob.onGround()) return;
                    mob.getJumpControl().jump();
                    mob.setJumping(true);
                    double jumpPower = 0.42 + (double)mob.getJumpBoostPower();
                    Vec3 delta = mob.getDeltaMovement();
                    mob.setDeltaMovement(delta.x, jumpPower, delta.z);
                    mob.hasImpulse = true;
                }
            } else {
                mob.setJumping(false);
            }
        }
    }

    private static void playSlimeJumpSound(Slime slime) {
        SoundEvent sound = slime instanceof MagmaCube ? SoundEvents.MAGMA_CUBE_JUMP : (slime.isTiny() ? SoundEvents.SLIME_JUMP_SMALL : SoundEvents.SLIME_JUMP);
        float volume = 0.4f * (float)slime.getSize();
        float pitch = ((slime.getRandom().nextFloat() - slime.getRandom().nextFloat()) * 0.2f + 1.0f) * (slime.isTiny() ? 1.4f : 0.8f);
        slime.playSound(sound, volume, pitch);
    }

    public static void disconnect(int mobId) {
        PuppetServerManager.disconnect(mobId, null);
    }

    public static void disconnect(Mob mob) {
        if (mob == null) return;
        PuppetServerManager.disconnect(mob.getId(), mob);
    }

    public static void disconnectForPlayer(UUID playerUuid) {
        if (playerUuid == null) {
            return;
        }
        Integer mobId = mobByPlayer.remove(playerUuid);
        if (mobId != null) {
            PuppetServerManager.disconnect(mobId, null);
        } else {
            Map.Entry<Integer, PuppetSession> entry;
            Iterator<Map.Entry<Integer, PuppetSession>> iterator = sessionsByMob.entrySet().iterator();
            do {
                if (!iterator.hasNext()) return;
                entry = iterator.next();
            } while (!playerUuid.equals(entry.getValue().playerUuid));
            PuppetServerManager.disconnect(entry.getKey(), null);
        }
    }

    public static void disconnect(int mobId, @Nullable Mob mobInstance) {
        ServerPlayer player;
        MinecraftServer server;
        UUID playerUuid;
        PuppetSession session = sessionsByMob.remove(mobId);
        UUID uUID = playerUuid = session != null ? session.playerUuid : null;
        if (playerUuid != null) {
            mobByPlayer.remove(playerUuid);
        } else {
            for (Map.Entry<UUID, Integer> entry : mobByPlayer.entrySet()) {
                if (entry.getValue() != mobId) continue;
                playerUuid = entry.getKey();
                mobByPlayer.remove(playerUuid);
                break;
            }
        }
        MinecraftServer minecraftServer = server = mobInstance != null ? mobInstance.getServer() : ServerLifecycleHooks.getCurrentServer();
        if (playerUuid != null && server != null && (player = server.getPlayerList().getPlayer(playerUuid)) != null) {
            ModMessages.sendToPlayer((CustomPacketPayload)new S2CPuppetDisconnectPacket(mobId), (ServerPlayer)player);
        }
        if (mobInstance == null && server != null) {
            mobInstance = PuppetServerManager.findMobInServer(server, mobId);
        }
        if (mobInstance == null) return;
        if (session != null && mobInstance.getAttribute(Attributes.STEP_HEIGHT) != null) {
            mobInstance.getAttribute(Attributes.STEP_HEIGHT).setBaseValue((double)session.originalMaxUpStep);
        }
        mobInstance.zza = 0.0f;
        mobInstance.xxa = 0.0f;
        mobInstance.stopUsingItem();
        mobInstance.setSprinting(false);
        mobInstance.setShiftKeyDown(false);
        if (mobInstance instanceof EnderDragon) {
            EnderDragon dragon = (EnderDragon)mobInstance;
            dragon.getPhaseManager().setPhase(EnderDragonPhase.HOLDING_PATTERN);
        }
        CompoundTag data = mobInstance.getPersistentData();
        data.remove("LastPossessedTick");
        data.remove("PossessingPlayerUUID");
        data.remove("DrawingBow");
        data.remove("BowDrawStart");
    }

    private static Mob findMobInServer(MinecraftServer server, int mobId) {
        ServerLevel level;
        Entity entity;
        if (server == null) {
            return null;
        }
        Iterator iterator = server.getAllLevels().iterator();
        do {
            if (!iterator.hasNext()) return null;
        } while (!((entity = (level = (ServerLevel)iterator.next()).getEntity(mobId)) instanceof Mob));
        Mob mob = (Mob)entity;
        return mob;
    }
}

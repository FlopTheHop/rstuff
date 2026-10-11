/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Position
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.material.Fluid
 *  net.minecraft.world.level.material.FluidState
 *  net.minecraft.world.level.material.Fluids
 *  net.minecraft.world.level.portal.DimensionTransition
 *  net.minecraft.world.phys.Vec3
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.entity.PortalEntity
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CTeleportOverlayPacket
 *  org.ExtraTortik.ricks_stuff.portal.AntiPortalFieldManager
 *  org.ExtraTortik.ricks_stuff.portal.BlenderDimensionHelper
 *  org.ExtraTortik.ricks_stuff.portal.PortalManager
 *  org.ExtraTortik.ricks_stuff.portal.PortalPlacementHelper
 */
package org.ExtraTortik.ricks_stuff.portal;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.entity.PortalEntity;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.S2CTeleportOverlayPacket;
import org.ExtraTortik.ricks_stuff.portal.AntiPortalFieldManager;
import org.ExtraTortik.ricks_stuff.portal.BlenderDimensionHelper;
import org.ExtraTortik.ricks_stuff.portal.PortalManager;
import org.ExtraTortik.ricks_stuff.portal.PortalPlacementHelper;

public class PortalTeleportEngine {
    public static final int DEFAULT_COOLDOWN_TICKS = 30;
    private static final Map<UUID, Integer> GLOBAL_COOLDOWNS = new ConcurrentHashMap<UUID, Integer>();
    private static final Map<UUID, Integer> HAZARD_SHIELD_COOLDOWNS = new ConcurrentHashMap<UUID, Integer>();

    public static void tickServer() {
        if (!GLOBAL_COOLDOWNS.isEmpty()) {
            for (UUID uuid : GLOBAL_COOLDOWNS.keySet()) {
                GLOBAL_COOLDOWNS.computeIfPresent(uuid, (k, ticks) -> {
                    int updated = ticks - 1;
                    return updated <= 0 ? null : Integer.valueOf(updated);
                });
            }
        }
        if (HAZARD_SHIELD_COOLDOWNS.isEmpty()) return;
        Iterator<UUID> iterator = HAZARD_SHIELD_COOLDOWNS.keySet().iterator();
        while (iterator.hasNext()) {
            UUID uuid;
            uuid = iterator.next();
            HAZARD_SHIELD_COOLDOWNS.computeIfPresent(uuid, (k, ticks) -> {
                int updated = ticks - 1;
                return updated <= 0 ? null : Integer.valueOf(updated);
            });
        }
    }

    public static void setCooldown(UUID entityUUID, int ticks) {
        if (entityUUID == null) return;
        GLOBAL_COOLDOWNS.put(entityUUID, Math.max(GLOBAL_COOLDOWNS.getOrDefault(entityUUID, 0), ticks));
    }

    public static boolean hasCooldown(UUID entityUUID) {
        Integer ticks = GLOBAL_COOLDOWNS.get(entityUUID);
        if (entityUUID != null) return ticks != null && ticks > 0;
        return false;
    }

    public static boolean hasCooldown(Entity entity) {
        return entity != null && PortalTeleportEngine.hasCooldown(entity.getUUID());
    }

    public static boolean teleportEntity(PortalEntity entrance, PortalEntity exit, Entity entity) {
        float exitYaw;
        if (entrance == null) return false;
        if (exit == null) return false;
        if (entity == null) return false;
        if (entity.isRemoved()) {
            return false;
        }
        if (entrance.level().isClientSide) return false;
        Level level = entrance.level();
        if (!(level instanceof ServerLevel)) return false;
        ServerLevel entranceLevel = (ServerLevel)level;
        Level level2 = exit.level();
        if (!(level2 instanceof ServerLevel)) return false;
        ServerLevel exitLevel = (ServerLevel)level2;
        UUID uuid = entity.getUUID();
        if (PortalTeleportEngine.hasCooldown(uuid)) return false;
        if (entrance.hasTeleportCooldown(uuid)) return false;
        if (exit.hasTeleportCooldown(uuid)) {
            return false;
        }
        if (entity.isPassenger()) return false;
        if (entity.isVehicle()) {
            return false;
        }
        Vec3 exitForward = PortalTeleportEngine.getForwardVector(exit.getOrientation(), exit.getYRot(), exit.getRotation());
        double halfEntityDepth = Math.max(0.08, (double)entity.getBbWidth() * 0.5);
        double offsetDist = Math.max(0.75, halfEntityDepth + 0.35);
        double exitX = exit.getX();
        double exitY = exit.getY();
        double exitZ = exit.getZ();
        if (exit.getOrientation() == 0) {
            exitX += exitForward.x * offsetDist;
            exitZ += exitForward.z * offsetDist;
        } else if (exit.getOrientation() == 2) {
            exitY += 0.05;
        } else if (exit.getOrientation() == 1) {
            exitY -= (double)entity.getBbHeight() + 0.05;
        }
        float exitPitch = entity.getXRot();
        if (entrance.getOrientation() == 0 && exit.getOrientation() == 0) {
            float rotDiff = exit.getYRot() - entrance.getYRot() + 180.0f;
            exitYaw = (entity.getYRot() + rotDiff) % 360.0f;
        } else {
            exitYaw = exit.getOrientation() == 0 ? exit.getYRot() : entity.getYRot();
        }
        Vec3 inVel = entity.getDeltaMovement();
        Vec3 outVel = PortalTeleportEngine.transformVelocity(entrance, exit, inVel);
        if (AntiPortalFieldManager.isProtected((Level)exitLevel, (Vec3)new Vec3(exitX, exitY, exitZ))) {
            entrance.playSound((SoundEvent)ModSounds.ANTI_PORTAL_DEFLECT.get(), 1.0f, 1.0f);
            if (entity instanceof ServerPlayer) {
                ServerPlayer player = (ServerPlayer)entity;
                player.displayClientMessage((Component)Component.translatable((String)"message.ricks_stuff.anti_portal_blocked").withStyle(ChatFormatting.RED), true);
            }
            PortalTeleportEngine.setCooldown(uuid, 20);
            return false;
        }
        BlockPos targetPos = BlockPos.containing((double)exitX, (double)exitY, (double)exitZ);
        exitLevel.getChunk(targetPos);
        PortalTeleportEngine.performTeleport(entity, exitLevel, exitX, exitY, exitZ, exitYaw, exitPitch, outVel);
        PortalTeleportEngine.applyHazardShieldEffects(entrance, exit, entity);
        entity.resetFallDistance();
        PortalTeleportEngine.setCooldown(uuid, 30);
        entrance.setTeleportCooldown(uuid, 30);
        exit.setTeleportCooldown(uuid, 30);
        PortalTeleportEngine.recordTeleportHistory(entranceLevel, entrance, exitX, exitY, exitZ, exitLevel.dimension().location().toString());
        entrance.playSound((SoundEvent)ModSounds.PORTAL_TELEPORT.get(), 0.4f, 1.0f);
        exit.playSound((SoundEvent)ModSounds.PORTAL_TELEPORT.get(), 0.4f, 1.0f);
        entrance.triggerPassAnimation();
        exit.triggerPassAnimation();
        if (entity instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)entity;
            ModMessages.sendToPlayer((CustomPacketPayload)new S2CTeleportOverlayPacket(entrance.getColor()), (ServerPlayer)serverPlayer);
        }
        if (entrance.isAutoClose()) {
            PortalManager.removePortal((PortalEntity)entrance, (boolean)false);
        }
        if (!exit.isAutoClose()) return true;
        PortalManager.removePortal((PortalEntity)exit, (boolean)false);
        return true;
    }

    public static boolean teleportEntityToCustomLocation(PortalEntity entrance, CompoundTag data, Entity entity) {
        BlockPos targetBlock;
        BlockPos safeBlock;
        if (entrance == null) return false;
        if (data == null) return false;
        if (data.isEmpty()) return false;
        if (entity == null) return false;
        if (entity.isRemoved()) {
            return false;
        }
        if (entrance.level().isClientSide) return false;
        Level level = entrance.level();
        if (!(level instanceof ServerLevel)) return false;
        ServerLevel entranceLevel = (ServerLevel)level;
        UUID uuid = entity.getUUID();
        if (PortalTeleportEngine.hasCooldown(uuid)) return false;
        if (entrance.hasTeleportCooldown(uuid)) {
            return false;
        }
        if (!data.contains("X")) return false;
        if (!data.contains("Y")) return false;
        if (!data.contains("Z")) {
            return false;
        }
        double targetX = data.getDouble("X");
        double targetY = data.getDouble("Y");
        double targetZ = data.getDouble("Z");
        float targetYaw = entity.getYRot();
        float targetPitch = entity.getXRot();
        ServerLevel targetLevel = entranceLevel;
        if (data.contains("Dimension")) {
            String dimStr = data.getString("Dimension");
            if (BlenderDimensionHelper.isBlenderDimension((String)dimStr)) {
                BlenderDimensionHelper.shredEntity((PortalEntity)entrance, (Entity)entity);
                return true;
            }
            if (entranceLevel.getServer() != null) {
                targetLevel = PortalManager.resolveTargetLevel((MinecraftServer)entranceLevel.getServer(), (String)dimStr, (ServerLevel)entranceLevel);
            }
        }
        boolean safePlacement = entrance.isSafePlacement();
        if (data.contains("SafePlacement")) {
            safePlacement = data.getBoolean("SafePlacement");
        }
        if (data.getBoolean("IsWorldSpawn")) {
            Vec3 spawnPos = PortalPlacementHelper.getDimensionSpawnPos((ServerLevel)targetLevel);
            targetX = spawnPos.x;
            targetY = spawnPos.y;
            targetZ = spawnPos.z;
        } else if (safePlacement && (safeBlock = PortalPlacementHelper.findSafePlacement((ServerLevel)targetLevel, (BlockPos)(targetBlock = BlockPos.containing((double)targetX, (double)targetY, (double)targetZ)), (boolean)true)) != null) {
            targetX = (double)safeBlock.getX() + 0.5;
            targetY = safeBlock.getY();
            targetZ = (double)safeBlock.getZ() + 0.5;
        }
        targetLevel.getChunk(BlockPos.containing((double)targetX, (double)targetY, (double)targetZ));
        if (AntiPortalFieldManager.isProtected((Level)targetLevel, (Vec3)new Vec3(targetX, targetY, targetZ))) {
            entrance.playSound((SoundEvent)ModSounds.ANTI_PORTAL_DEFLECT.get(), 1.0f, 1.0f);
            if (entity instanceof ServerPlayer) {
                ServerPlayer player = (ServerPlayer)entity;
                player.displayClientMessage((Component)Component.translatable((String)"message.ricks_stuff.anti_portal_blocked").withStyle(ChatFormatting.RED), true);
            }
            PortalTeleportEngine.setCooldown(uuid, 20);
            return false;
        }
        Vec3 inVel = entity.getDeltaMovement();
        Vec3 outVel = new Vec3(inVel.x * 0.5, Math.max(inVel.y * 0.5, 0.05), inVel.z * 0.5);
        PortalTeleportEngine.performTeleport(entity, targetLevel, targetX, targetY, targetZ, targetYaw, targetPitch, outVel);
        PortalTeleportEngine.applyHazardShieldEffects(entrance, null, entity);
        entity.resetFallDistance();
        PortalTeleportEngine.setCooldown(uuid, 30);
        entrance.setTeleportCooldown(uuid, 30);
        PortalTeleportEngine.recordTeleportHistory(entranceLevel, entrance, targetX, targetY, targetZ, targetLevel.dimension().location().toString());
        if (entity instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)entity;
            ModMessages.sendToPlayer((CustomPacketPayload)new S2CTeleportOverlayPacket(entrance.getColor()), (ServerPlayer)serverPlayer);
        }
        if (!entrance.isAutoClose()) return true;
        PortalManager.removePortal((PortalEntity)entrance, (boolean)true);
        return true;
    }

    private static void performTeleport(Entity entity, ServerLevel targetLevel, double x, double y, double z, float yaw, float pitch, Vec3 velocity) {
        if (entity.level() == targetLevel) {
            if (entity instanceof ServerPlayer) {
                ServerPlayer player = (ServerPlayer)entity;
                player.connection.teleport(x, y, z, yaw, pitch);
                player.setDeltaMovement(velocity);
                player.connection.send((Packet)new ClientboundSetEntityMotionPacket(player.getId(), velocity));
                player.hurtMarked = true;
                player.hasImpulse = true;
            } else {
                entity.teleportTo(x, y, z);
                entity.setYRot(yaw);
                entity.setXRot(pitch);
                entity.setYHeadRot(yaw);
                entity.setDeltaMovement(velocity);
                if (entity instanceof Projectile) {
                    Projectile projectile = (Projectile)entity;
                    double speed = velocity.length();
                    if (speed > 0.001) {
                        double hSpeed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
                        float newYaw = (float)(Math.atan2(velocity.x, velocity.z) * 57.29577951308232);
                        float newPitch = (float)(Math.atan2(velocity.y, hSpeed) * 57.29577951308232);
                        projectile.setYRot(newYaw);
                        projectile.setXRot(newPitch);
                        projectile.yRotO = newYaw;
                        projectile.xRotO = newPitch;
                    }
                }
                entity.hurtMarked = true;
                entity.hasImpulse = true;
            }
        } else {
            DimensionTransition transition = new DimensionTransition(targetLevel, new Vec3(x, y, z), velocity, yaw, pitch, DimensionTransition.DO_NOTHING);
            if (entity instanceof ServerPlayer) {
                ServerPlayer player = (ServerPlayer)entity;
                player.changeDimension(transition);
                player.setDeltaMovement(velocity);
                player.connection.send((Packet)new ClientboundSetEntityMotionPacket(player.getId(), velocity));
                player.hurtMarked = true;
                player.hasImpulse = true;
            } else {
                Entity transferred = entity.changeDimension(transition);
                if (transferred == null) return;
                transferred.setDeltaMovement(velocity);
                transferred.resetFallDistance();
                if (!(transferred instanceof Projectile)) return;
                Projectile projectile = (Projectile)transferred;
                double speed = velocity.length();
                if (!(speed > 0.001)) return;
                double hSpeed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
                float newYaw = (float)(Math.atan2(velocity.x, velocity.z) * 57.29577951308232);
                float newPitch = (float)(Math.atan2(velocity.y, hSpeed) * 57.29577951308232);
                projectile.setYRot(newYaw);
                projectile.setXRot(newPitch);
                projectile.yRotO = newYaw;
                projectile.xRotO = newPitch;
            }
        }
    }

    public static void handleFluidFlow(PortalEntity entrance, PortalEntity exit) {
        if (entrance == null) return;
        if (exit == null) return;
        if (entrance.isRemoved()) return;
        if (exit.isRemoved()) {
            return;
        }
        if (entrance.level().isClientSide) return;
        if (exit.level().isClientSide) {
            return;
        }
        Level inLevel = entrance.level();
        Level outLevel = exit.level();
        BlockPos inPos = entrance.blockPosition();
        FluidState inFluid = inLevel.getFluidState(inPos);
        if (inFluid.isEmpty()) {
            inFluid = inLevel.getFluidState(BlockPos.containing((Position)entrance.position()));
        }
        if (inFluid.isEmpty()) {
            inFluid = inLevel.getFluidState(inPos.above());
        }
        if (inFluid.isEmpty()) {
            inFluid = inLevel.getFluidState(inPos.below());
        }
        if (inFluid.isEmpty()) return;
        if (inFluid.getType() == Fluids.EMPTY) {
            return;
        }
        Vec3 exitForward = PortalTeleportEngine.getForwardVector(exit.getOrientation(), exit.getYRot(), exit.getRotation());
        BlockPos outPos = exit.getOrientation() == 0 ? BlockPos.containing((double)(exit.getX() + exitForward.x * 1.1), (double)(exit.getY() + 0.2), (double)(exit.getZ() + exitForward.z * 1.1)) : (exit.getOrientation() == 2 ? exit.blockPosition().above() : exit.blockPosition().below());
        BlockState outState = outLevel.getBlockState(outPos);
        Fluid fluid = inFluid.getType();
        if (!outState.isAir()) {
            if (!outState.canBeReplaced(fluid)) return;
        }
        BlockState newFluidState = fluid == Fluids.WATER || fluid == Fluids.FLOWING_WATER ? Blocks.WATER.defaultBlockState() : (fluid == Fluids.LAVA || fluid == Fluids.FLOWING_LAVA ? Blocks.LAVA.defaultBlockState() : fluid.defaultFluidState().createLegacyBlock());
        if (newFluidState == null) return;
        if (newFluidState.isAir()) return;
        outLevel.setBlock(outPos, newFluidState, 3);
        outLevel.scheduleTick(outPos, fluid, fluid.getTickDelay((LevelReader)outLevel));
        if (fluid != Fluids.LAVA) {
            if (fluid != Fluids.FLOWING_LAVA) return;
        }
        entrance.playSound((SoundEvent)ModSounds.PLASMA_SCORCH.get(), 0.5f, 1.0f);
        exit.playSound((SoundEvent)ModSounds.PLASMA_SCORCH.get(), 0.5f, 1.0f);
    }

    public static void handleAmbientEffects(PortalEntity portal) {
    }

    public static Vec3 getPortalNormal(int orientation, float yaw) {
        if (orientation == 0) {
            float rad = (float)Math.toRadians(yaw);
            return new Vec3(-Math.sin(rad), 0.0, Math.cos(rad));
        }
        if (orientation != 2) return new Vec3(0.0, -1.0, 0.0);
        return new Vec3(0.0, 1.0, 0.0);
    }

    public static Vec3 getPortalTangentX(int orientation, float yaw) {
        float rad = (float)Math.toRadians(yaw);
        return new Vec3(Math.cos(rad), 0.0, Math.sin(rad));
    }

    public static Vec3 getPortalTangentY(int orientation, float yaw) {
        if (orientation == 0) {
            return new Vec3(0.0, 1.0, 0.0);
        }
        float rad = (float)Math.toRadians(yaw);
        return new Vec3(-Math.sin(rad), 0.0, Math.cos(rad));
    }

    public static Vec3 transformVelocity(PortalEntity entrance, PortalEntity exit, Vec3 inVel) {
        Vec3 nIn = PortalTeleportEngine.getPortalNormal(entrance.getOrientation(), entrance.getYRot());
        Vec3 txIn = PortalTeleportEngine.getPortalTangentX(entrance.getOrientation(), entrance.getYRot());
        Vec3 tyIn = PortalTeleportEngine.getPortalTangentY(entrance.getOrientation(), entrance.getYRot());
        Vec3 nOut = PortalTeleportEngine.getPortalNormal(exit.getOrientation(), exit.getYRot());
        Vec3 txOut = PortalTeleportEngine.getPortalTangentX(exit.getOrientation(), exit.getYRot());
        Vec3 tyOut = PortalTeleportEngine.getPortalTangentY(exit.getOrientation(), exit.getYRot());
        double vN = inVel.dot(nIn);
        double vTx = inVel.dot(txIn);
        double vTy = inVel.dot(tyIn);
        double totalSpeed = inVel.length();
        double forwardSpeed = Math.max(Math.abs(vN), Math.max(0.45, totalSpeed * 0.95));
        double outTx = vTx * 0.75;
        double outTy = vTy * 0.75;
        double vx = nOut.x * forwardSpeed + txOut.x * outTx + tyOut.x * outTy;
        double vy = nOut.y * forwardSpeed + txOut.y * outTx + tyOut.y * outTy;
        double vz = nOut.z * forwardSpeed + txOut.z * outTx + tyOut.z * outTy;
        if (exit.getOrientation() == 0 && vy < 0.05) {
            vy = 0.05;
        } else {
            if (exit.getOrientation() != 1) return new Vec3(vx, vy, vz);
            if (!(vy > -0.2)) return new Vec3(vx, vy, vz);
            vy = -0.35;
        }
        return new Vec3(vx, vy, vz);
    }

    public static Vec3 getForwardVector(int orientation, float yRot, int rot) {
        return PortalTeleportEngine.getPortalNormal(orientation, yRot);
    }

    public static void recordTeleportHistory(ServerLevel level, PortalEntity entrance, double x, double y, double z, String dimension) {
        if (entrance == null) return;
        if (level == null) return;
        if (level.getServer() == null) {
            return;
        }
        int gunId = entrance.getOwnerGunId();
        if (gunId == 0) {
            return;
        }
        ItemStack gunStack = PortalManager.findGunOnServer((MinecraftServer)level.getServer(), (int)gunId);
        if (gunStack.isEmpty()) return;
        if (!PortalGunItem.hasMemoryCard((ItemStack)gunStack)) return;
        CompoundTag entry = new CompoundTag();
        Object dimName = dimension.contains(":") ? dimension.substring(dimension.indexOf(":") + 1) : dimension;
        dimName = ((String)dimName).substring(0, 1).toUpperCase(Locale.ROOT) + ((String)dimName).substring(1).replace("_", " ");
        entry.putString("Name", (String)dimName + " [" + (int)Math.round(x) + ", " + (int)Math.round(y) + ", " + (int)Math.round(z) + "]");
        entry.putDouble("X", (double)Math.round(x * 10.0) / 10.0);
        entry.putDouble("Y", (double)Math.round(y * 10.0) / 10.0);
        entry.putDouble("Z", (double)Math.round(z * 10.0) / 10.0);
        entry.putString("Dimension", dimension);
        PortalGunItem.addHistoryLocation((ItemStack)gunStack, (CompoundTag)entry);
    }

    private static void applyHazardShieldEffects(PortalEntity entrance, PortalEntity exit, Entity entity) {
        boolean hasShield = entrance != null && entrance.hasHazardShield() || exit != null && exit.hasHazardShield();
        if (!hasShield) return;
        if (!(entity instanceof LivingEntity)) return;
        LivingEntity living = (LivingEntity)entity;
        UUID uuid = living.getUUID();
        if (HAZARD_SHIELD_COOLDOWNS.containsKey(uuid)) {
            return;
        }
        living.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0));
        living.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 120, 0));
        living.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 160, 0));
        int cooldownTicks = (int)(Config.hazardShieldCooldownSeconds * 20.0);
        HAZARD_SHIELD_COOLDOWNS.put(uuid, Math.max(20, cooldownTicks));
    }
}

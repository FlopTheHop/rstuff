/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.Vec3
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.compat.InfinityCompat
 *  org.ExtraTortik.ricks_stuff.entity.PortalEntity
 *  org.ExtraTortik.ricks_stuff.entity.PortalProjectileEntity
 *  org.ExtraTortik.ricks_stuff.init.ModEntities
 *  org.ExtraTortik.ricks_stuff.init.ModParticles
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.UpgradeType
 *  org.ExtraTortik.ricks_stuff.portal.AntiPortalFieldManager
 *  org.ExtraTortik.ricks_stuff.portal.BlenderDimensionHelper
 *  org.ExtraTortik.ricks_stuff.portal.PortalManager$1
 *  org.ExtraTortik.ricks_stuff.portal.PortalManager$SoundThrottleEntry
 *  org.ExtraTortik.ricks_stuff.portal.PortalMode
 *  org.ExtraTortik.ricks_stuff.portal.PortalPlacementHelper
 *  org.ExtraTortik.ricks_stuff.portal.PortalPlacementHelper$Placement
 *  org.ExtraTortik.ricks_stuff.portal.PortalSavedData
 */
package org.ExtraTortik.ricks_stuff.portal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.compat.InfinityCompat;
import org.ExtraTortik.ricks_stuff.entity.PortalEntity;
import org.ExtraTortik.ricks_stuff.entity.PortalProjectileEntity;
import org.ExtraTortik.ricks_stuff.init.ModEntities;
import org.ExtraTortik.ricks_stuff.init.ModParticles;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.ExtraTortik.ricks_stuff.item.UpgradeType;
import org.ExtraTortik.ricks_stuff.portal.AntiPortalFieldManager;
import org.ExtraTortik.ricks_stuff.portal.BlenderDimensionHelper;
import org.ExtraTortik.ricks_stuff.portal.PortalManager;
import org.ExtraTortik.ricks_stuff.portal.PortalMode;
import org.ExtraTortik.ricks_stuff.portal.PortalPlacementHelper;
import org.ExtraTortik.ricks_stuff.portal.PortalSavedData;
import org.jetbrains.annotations.Nullable;

public class PortalManager {
    public static final int MAX_PORTALS_PER_GUN = 10;
    private static final Map<Integer, List<UUID>> GUN_PORTALS = new ConcurrentHashMap<Integer, List<UUID>>();
    private static final Map<UUID, PortalProjectileEntity> ACTIVE_PROJECTILES = new ConcurrentHashMap<UUID, PortalProjectileEntity>();
    private static final Map<ResourceKey<Level>, List<SoundThrottleEntry>> RECENT_CLOSE_SOUNDS = new ConcurrentHashMap<ResourceKey<Level>, List<SoundThrottleEntry>>();
    private static final double CLOSE_SOUND_THROTTLE_RADIUS_SQR = 400.0;
    private static final long CLOSE_SOUND_THROTTLE_TICKS = 8L;

    public static void playPortalCloseSound(PortalEntity portal) {
        if (portal == null) {
            return;
        }
        PortalManager.playPortalCloseSound(portal.level(), portal.position());
    }

    public static void playPortalCloseSound(Level level, Vec3 pos) {
        if (level == null) return;
        if (pos == null) {
            return;
        }
        long gameTime = level.getGameTime();
        ResourceKey dim = level.dimension();
        List entries = RECENT_CLOSE_SOUNDS.computeIfAbsent((ResourceKey<Level>)dim, k -> new CopyOnWriteArrayList());
        entries.removeIf(e -> gameTime - e.gameTime > 30L || e.gameTime > gameTime);
        for (SoundThrottleEntry entry : entries) {
            double dz;
            double dy;
            double dx;
            if (Math.abs(gameTime - entry.gameTime) > 8L || !((dx = pos.x - entry.x) * dx + (dy = pos.y - entry.y) * dy + (dz = pos.z - entry.z) * dz <= 400.0)) continue;
            return;
        }
        entries.add(new SoundThrottleEntry(pos.x, pos.y, pos.z, gameTime));
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            serverLevel.playSound(null, pos.x, pos.y, pos.z, (SoundEvent)ModSounds.PORTAL_CLOSE.get(), SoundSource.NEUTRAL, 1.0f, 1.0f);
        } else {
            level.playLocalSound(pos.x, pos.y, pos.z, (SoundEvent)ModSounds.PORTAL_CLOSE.get(), SoundSource.NEUTRAL, 1.0f, 1.0f, false);
        }
    }

    public static void registerProjectile(Player player, PortalProjectileEntity projectile) {
        if (player == null) return;
        if (projectile == null) return;
        ACTIVE_PROJECTILES.put(player.getUUID(), projectile);
    }

    public static PortalProjectileEntity getActiveProjectile(Player player) {
        List projs;
        if (player == null) {
            return null;
        }
        PortalProjectileEntity proj = ACTIVE_PROJECTILES.get(player.getUUID());
        if (proj != null && !proj.isRemoved() && proj.isAlive()) {
            return proj;
        }
        if (player.level() != null && !(projs = player.level().getEntitiesOfClass(PortalProjectileEntity.class, player.getBoundingBox().inflate(128.0), p -> p.getOwner() == player && !p.isRemoved() && p.isAlive())).isEmpty()) {
            PortalProjectileEntity found = (PortalProjectileEntity)projs.get(0);
            ACTIVE_PROJECTILES.put(player.getUUID(), found);
            return found;
        }
        ACTIVE_PROJECTILES.remove(player.getUUID());
        return null;
    }

    public static boolean hasActiveProjectile(Player player) {
        return PortalManager.getActiveProjectile(player) != null;
    }

    public static void unregisterProjectile(PortalProjectileEntity projectile) {
        Map.Entry<UUID, PortalProjectileEntity> entry;
        if (projectile == null) return;
        Iterator<Map.Entry<UUID, PortalProjectileEntity>> iterator = ACTIVE_PROJECTILES.entrySet().iterator();
        do {
            if (!iterator.hasNext()) return;
        } while ((entry = iterator.next()).getValue() != projectile);
        ACTIVE_PROJECTILES.remove(entry.getKey());
    }

    public static ItemStack findGunInPlayer(Player player, int gunId) {
        ItemStack item;
        if (player == null) {
            return ItemStack.EMPTY;
        }
        if (PortalManager.isMatchingGun(player.getMainHandItem(), gunId)) {
            return player.getMainHandItem();
        }
        if (PortalManager.isMatchingGun(player.getOffhandItem(), gunId)) {
            return player.getOffhandItem();
        }
        Iterator iterator = player.getInventory().items.iterator();
        do {
            if (!iterator.hasNext()) return ItemStack.EMPTY;
        } while (!PortalManager.isMatchingGun(item = (ItemStack)iterator.next(), gunId));
        return item;
    }

    private static boolean isMatchingGun(ItemStack stack, int gunId) {
        if (stack == null) return false;
        if (stack.isEmpty()) {
            return false;
        }
        if (stack.getItem() instanceof PortalGunItem) return gunId == 0 || PortalGunItem.getGunId((ItemStack)stack) == gunId;
        if (!(stack.getItem() instanceof DischargedPortalGunItem)) return false;
        return gunId == 0 || PortalGunItem.getGunId((ItemStack)stack) == gunId;
    }

    public static ItemStack findGunOnServer(MinecraftServer server, int gunId) {
        ServerPlayer player;
        ItemStack stack;
        if (server == null) return ItemStack.EMPTY;
        if (gunId == 0) {
            return ItemStack.EMPTY;
        }
        Iterator iterator = server.getPlayerList().getPlayers().iterator();
        do {
            if (!iterator.hasNext()) return ItemStack.EMPTY;
        } while ((stack = PortalManager.findGunInPlayer((Player)(player = (ServerPlayer)iterator.next()), gunId)).isEmpty());
        return stack;
    }

    public static void syncGunActivePortals(MinecraftServer server, Player player, int gunId, List<UUID> portals) {
        if (gunId == 0) {
            return;
        }
        ItemStack gun = PortalManager.findGunInPlayer(player, gunId);
        if (gun.isEmpty() && server != null) {
            gun = PortalManager.findGunOnServer(server, gunId);
        }
        if (!gun.isEmpty()) {
            PortalGunItem.setActivePortals((ItemStack)gun, portals);
        }
        if (server == null) return;
        if (portals == null) return;
        PortalSavedData data = PortalSavedData.get((MinecraftServer)server);
        Iterator<UUID> iterator = portals.iterator();
        while (iterator.hasNext()) {
            UUID id = iterator.next();
            if (id == null) continue;
            data.registerPortal(gunId, id);
        }
    }

    public static void removePortalFromGunNBT(MinecraftServer server, Player player, int gunId, UUID portalUUID) {
        if (gunId == 0) return;
        if (portalUUID == null) {
            return;
        }
        ItemStack gun = PortalManager.findGunInPlayer(player, gunId);
        if (gun.isEmpty() && server != null) {
            gun = PortalManager.findGunOnServer(server, gunId);
        }
        if (!gun.isEmpty()) {
            PortalGunItem.removeActivePortal((ItemStack)gun, (UUID)portalUUID);
        }
        if (server == null) return;
        PortalSavedData.get((MinecraftServer)server).tombstonePortal(portalUUID, gunId);
    }

    public static ResourceKey<Level> parseDimensionKey(String dimStr) {
        if (dimStr == null) return Level.OVERWORLD;
        if (dimStr.trim().isEmpty()) {
            return Level.OVERWORLD;
        }
        String clean = dimStr.trim().toLowerCase();
        if (clean.equals("overworld")) return Level.OVERWORLD;
        if (clean.equals("minecraft:overworld")) {
            return Level.OVERWORLD;
        }
        if (clean.equals("the_nether")) return Level.NETHER;
        if (clean.equals("nether")) return Level.NETHER;
        if (clean.equals("minecraft:the_nether")) {
            return Level.NETHER;
        }
        if (clean.equals("the_end")) return Level.END;
        if (clean.equals("end")) return Level.END;
        if (clean.equals("minecraft:the_end")) {
            return Level.END;
        }
        ResourceLocation loc = ResourceLocation.tryParse((String)(clean.contains(":") ? clean : "minecraft:" + clean));
        if (loc == null) return null;
        return ResourceKey.create((ResourceKey)Registries.DIMENSION, (ResourceLocation)loc);
    }

    public static boolean isDimensionBlacklisted(ResourceLocation dimLoc) {
        if (dimLoc == null) return false;
        if (Config.dimensionBlacklist == null) return false;
        if (Config.dimensionBlacklist.isEmpty()) {
            return false;
        }
        String dimStr = dimLoc.toString();
        String pathOnly = dimLoc.getPath();
        Iterator iterator = Config.dimensionBlacklist.iterator();
        while (iterator.hasNext()) {
            String entry = (String)iterator.next();
            if (entry == null) continue;
            String trimmed = entry.trim();
            if (trimmed.equalsIgnoreCase(dimStr)) return true;
            if (trimmed.equalsIgnoreCase(pathOnly)) {
                return true;
            }
            ResourceLocation parsed = ResourceLocation.tryParse((String)trimmed);
            if (parsed != null && parsed.equals((Object)dimLoc)) return true;
        }
        return false;
    }

    public static ServerLevel resolveTargetLevel(MinecraftServer server, String targetDimStr, ServerLevel fallbackLevel) {
        ResourceKey infKey;
        ServerLevel targetLevel;
        if (server == null) {
            return fallbackLevel;
        }
        ResourceKey<Level> targetDimKey = PortalManager.parseDimensionKey(targetDimStr);
        ServerLevel serverLevel = targetLevel = targetDimKey != null ? server.getLevel(targetDimKey) : null;
        if (targetLevel != null || !InfinityCompat.isLoaded() || (infKey = InfinityCompat.resolveOrGenerateDimension((MinecraftServer)server, (String)targetDimStr)) == null) return targetLevel != null ? targetLevel : fallbackLevel;
        targetLevel = server.getLevel(infKey);
        return targetLevel != null ? targetLevel : fallbackLevel;
    }

    public static boolean isDestinationTooClose(ServerLevel level, Vec3 spawnPos, CompoundTag customLocation) {
        Vec3 safePos;
        if (level == null) return false;
        if (spawnPos == null) return false;
        if (customLocation == null) return false;
        if (customLocation.isEmpty()) {
            return false;
        }
        MinecraftServer server = level.getServer();
        double targetX = customLocation.getDouble("X");
        double targetY = customLocation.getDouble("Y");
        double targetZ = customLocation.getDouble("Z");
        String targetDimStr = customLocation.getString("Dimension");
        if (BlenderDimensionHelper.isBlenderDimension((String)targetDimStr)) {
            return false;
        }
        if (Config.allowPlayerTargeting && customLocation.contains("TargetPlayerUUID") && server != null) {
            try {
                UUID targetUUID = UUID.fromString(customLocation.getString("TargetPlayerUUID"));
                ServerPlayer targetPlayer = server.getPlayerList().getPlayer(targetUUID);
                if (targetPlayer != null && !targetPlayer.isRemoved()) {
                    targetDimStr = targetPlayer.level().dimension().location().toString();
                    Vec3 playerLook = targetPlayer.getLookAngle();
                    targetX = targetPlayer.getX() - playerLook.x * 1.5;
                    targetY = targetPlayer.getY();
                    targetZ = targetPlayer.getZ() - playerLook.z * 1.5;
                }
            }
            catch (Exception targetUUID) {
                // empty catch block
            }
        }
        ServerLevel targetLevel = PortalManager.resolveTargetLevel(server, targetDimStr, level);
        if (!level.dimension().equals(targetLevel.dimension())) {
            return false;
        }
        if (customLocation.getBoolean("IsWorldSpawn")) {
            Vec3 spawnPosVec = PortalPlacementHelper.getDimensionSpawnPos((ServerLevel)targetLevel);
            targetX = spawnPosVec.x;
            targetY = spawnPosVec.y;
            targetZ = spawnPosVec.z;
            safePos = spawnPosVec;
        } else {
            safePos = customLocation.contains("SafePlacement") && !customLocation.getBoolean("SafePlacement") ? new Vec3(targetX, targetY, targetZ) : PortalPlacementHelper.findSafePlacement((ServerLevel)targetLevel, (Vec3)new Vec3(targetX, targetY, targetZ));
        }
        double dist3D = safePos.distanceTo(spawnPos);
        double dx = safePos.x - spawnPos.x;
        double dz = safePos.z - spawnPos.z;
        double dy = Math.abs(safePos.y - spawnPos.y);
        double distXZ = Math.sqrt(dx * dx + dz * dz);
        double rawDx = targetX - spawnPos.x;
        double rawDz = targetZ - spawnPos.z;
        double rawDistXZ = Math.sqrt(rawDx * rawDx + rawDz * rawDz);
        return dist3D < 3.5 || distXZ < 3.0 && dy < 6.0 || rawDistXZ < 3.0 && Math.abs(targetY - spawnPos.y) < 6.0;
    }

    public static boolean isDestinationProtected(ServerLevel level, CompoundTag customLocation) {
        if (level == null) return false;
        if (customLocation == null) return false;
        if (customLocation.isEmpty()) {
            return false;
        }
        MinecraftServer server = level.getServer();
        double targetX = customLocation.getDouble("X");
        double targetY = customLocation.getDouble("Y");
        double targetZ = customLocation.getDouble("Z");
        String targetDimStr = customLocation.getString("Dimension");
        if (BlenderDimensionHelper.isBlenderDimension((String)targetDimStr)) {
            return false;
        }
        if (Config.allowPlayerTargeting && customLocation.contains("TargetPlayerUUID") && server != null) {
            try {
                UUID targetUUID = UUID.fromString(customLocation.getString("TargetPlayerUUID"));
                ServerPlayer targetPlayer = server.getPlayerList().getPlayer(targetUUID);
                if (targetPlayer != null && !targetPlayer.isRemoved()) {
                    if (AntiPortalFieldManager.isPlayerHidden((Player)targetPlayer)) {
                        return true;
                    }
                    targetDimStr = targetPlayer.level().dimension().location().toString();
                    Vec3 playerLook = targetPlayer.getLookAngle();
                    targetX = targetPlayer.getX() - playerLook.x * 1.5;
                    targetY = targetPlayer.getY();
                    targetZ = targetPlayer.getZ() - playerLook.z * 1.5;
                }
            }
            catch (Exception targetUUID) {
                // empty catch block
            }
        }
        ServerLevel targetLevel = PortalManager.resolveTargetLevel(server, targetDimStr, level);
        Vec3 safePos = customLocation.getBoolean("IsWorldSpawn") ? PortalPlacementHelper.getDimensionSpawnPos((ServerLevel)targetLevel) : (customLocation.contains("SafePlacement") && !customLocation.getBoolean("SafePlacement") ? new Vec3(targetX, targetY, targetZ) : PortalPlacementHelper.findSafePlacement((ServerLevel)targetLevel, (Vec3)new Vec3(targetX, targetY, targetZ)));
        return AntiPortalFieldManager.isProtected((Level)targetLevel, (Vec3)safePos);
    }

    private static void triggerMalfunctionFeedback(ServerLevel level, Vec3 pos, @Nullable Player player) {
        level.playSound(null, pos.x, pos.y, pos.z, (SoundEvent)ModSounds.GUN_MALFUNCTION.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        int i = 0;
        while (i < 16) {
            double rx = (level.random.nextDouble() - 0.5) * 0.8;
            double ry = (level.random.nextDouble() - 0.5) * 0.8;
            double rz = (level.random.nextDouble() - 0.5) * 0.8;
            level.sendParticles((ParticleOptions)((SimpleParticleType)ModParticles.FLUID_EVAPORATION.get()), pos.x + rx, pos.y + ry, pos.z + rz, 1, (level.random.nextDouble() - 0.5) * 0.1, level.random.nextDouble() * 0.1, (level.random.nextDouble() - 0.5) * 0.1, 0.05);
            ++i;
        }
    }

    private static void triggerAntiPortalFeedback(ServerLevel level, Vec3 pos, @Nullable Player player) {
        level.playSound(null, pos.x, pos.y, pos.z, (SoundEvent)ModSounds.ANTI_PORTAL_DEFLECT.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
        int i = 0;
        while (true) {
            if (i >= 16) {
                if (player == null) return;
                player.displayClientMessage((Component)Component.translatable((String)"message.ricks_stuff.anti_portal_blocked").withStyle(ChatFormatting.RED), true);
                return;
            }
            double rx = (level.random.nextDouble() - 0.5) * 0.8;
            double ry = (level.random.nextDouble() - 0.5) * 0.8;
            double rz = (level.random.nextDouble() - 0.5) * 0.8;
            level.sendParticles((ParticleOptions)((SimpleParticleType)ModParticles.BLUE_DIMENSIONAL_SPARK.get()), pos.x + rx, pos.y + ry, pos.z + rz, 1, (level.random.nextDouble() - 0.5) * 0.2, level.random.nextDouble() * 0.2, (level.random.nextDouble() - 0.5) * 0.2, 0.05);
            ++i;
        }
    }

    public static PortalEntity handlePortalSpawn(ServerLevel level, BlockHitResult hit, PortalProjectileEntity projectile) {
        Player p;
        if (level == null) return null;
        if (hit == null) return null;
        if (projectile == null) {
            return null;
        }
        int gunId = projectile.getGunId();
        String modeStr = projectile.getGunMode();
        PortalMode mode = PortalMode.fromString((String)modeStr);
        int color = projectile.getColor();
        float scale = projectile.getPortalScale();
        boolean autoClose = projectile.isAutoClose();
        CompoundTag customLocation = projectile.getCustomLocation();
        Entity entity = projectile.getOwner();
        Player player = entity instanceof Player ? (p = (Player)entity) : null;
        PortalPlacementHelper.Placement placement = PortalPlacementHelper.calculatePlacement((BlockHitResult)hit, (Player)player, (Level)level);
        if (AntiPortalFieldManager.isProtected((Level)level, (Vec3)placement.getPos())) {
            PortalManager.triggerAntiPortalFeedback(level, placement.getPos(), player);
            PortalManager.unregisterProjectile(projectile);
            projectile.discard();
            return null;
        }
        if (mode == PortalMode.CUSTOM) {
            if (PortalManager.isDestinationProtected(level, customLocation)) {
                PortalManager.triggerAntiPortalFeedback(level, placement.getPos(), player);
                PortalManager.unregisterProjectile(projectile);
                projectile.discard();
                return null;
            }
            if (PortalManager.isDestinationTooClose(level, placement.getPos(), customLocation)) {
                PortalManager.triggerMalfunctionFeedback(level, placement.getPos(), player);
                PortalManager.unregisterProjectile(projectile);
                projectile.discard();
                return null;
            }
        }
        PortalManager.closeOverlappingPortals(level, placement.getPos(), 1.25, null);
        PortalEntity portal = new PortalEntity((EntityType)ModEntities.PORTAL.get(), (Level)level);
        portal.moveTo(placement.getPos().x, placement.getPos().y, placement.getPos().z, placement.getYaw(), 0.0f);
        portal.setOrientation(placement.getOrientation());
        portal.setRotation(placement.getRotation());
        portal.setColor(color);
        portal.setScale(scale);
        portal.setOwnerGunId(gunId);
        portal.setGunMode(mode.getName());
        portal.setAutoClose(autoClose);
        portal.setHazardShield(projectile.hasHazardShield());
        portal.setSafePlacement(projectile.isSafePlacement());
        if (customLocation != null && !customLocation.isEmpty()) {
            portal.setCustomLocationData(customLocation);
        }
        level.addFreshEntity((Entity)portal);
        portal.playSound((SoundEvent)ModSounds.PORTAL_OPEN.get(), 1.0f, 1.0f);
        PortalPlacementHelper.clearVegetationInPortalArea((ServerLevel)level, (Vec3)placement.getPos(), (int)placement.getOrientation(), (float)placement.getYaw(), (float)scale);
        PortalManager.applyModeLinking(level, portal, gunId, mode, customLocation, player, color, scale, autoClose, placement.getOrientation(), placement.getRotation(), placement.getYaw());
        PortalManager.unregisterProjectile(projectile);
        return portal;
    }

    public static PortalEntity handleAirPortalSpawn(ServerLevel level, Vec3 pos, float yaw, PortalProjectileEntity projectile) {
        Player p;
        if (level == null) return null;
        if (pos == null) return null;
        if (projectile == null) {
            return null;
        }
        int gunId = projectile.getGunId();
        String modeStr = projectile.getGunMode();
        PortalMode mode = PortalMode.fromString((String)modeStr);
        int color = projectile.getColor();
        float scale = projectile.getPortalScale();
        boolean autoClose = projectile.isAutoClose();
        CompoundTag customLocation = projectile.getCustomLocation();
        Entity entity = projectile.getOwner();
        Player player = entity instanceof Player ? (p = (Player)entity) : null;
        int orientation = 0;
        int rotation = 0;
        float pitch = player != null ? player.getXRot() : 0.0f;
        Vec3 vel = projectile.getDeltaMovement();
        double horizontalSpeed = Math.sqrt(vel.x * vel.x + vel.z * vel.z);
        float rawYaw = player != null ? player.getYRot() : yaw;
        float normalizedYaw = (rawYaw % 360.0f + 360.0f) % 360.0f;
        int cardinalSector = Math.round(normalizedYaw / 90.0f) % 4;
        float cardinalYaw = (float)cardinalSector * 90.0f;
        if (pitch < -45.0f || vel.y > 0.35 && vel.y > horizontalSpeed * 0.9) {
            orientation = 1;
            rotation = cardinalSector;
            yaw = cardinalYaw;
        } else if (pitch > 45.0f || vel.y < -0.35 && -vel.y > horizontalSpeed * 0.9) {
            orientation = 2;
            rotation = cardinalSector;
            yaw = cardinalYaw;
        } else {
            orientation = 0;
            rotation = cardinalSector;
            yaw = cardinalYaw;
        }
        pos = PortalPlacementHelper.adjustAirPortalPosition((Level)level, (Vec3)pos, (int)orientation, (float)yaw, (float)scale);
        if (AntiPortalFieldManager.isProtected((Level)level, (Vec3)pos)) {
            PortalManager.triggerAntiPortalFeedback(level, pos, player);
            PortalManager.unregisterProjectile(projectile);
            projectile.discard();
            return null;
        }
        if (mode == PortalMode.CUSTOM) {
            if (PortalManager.isDestinationProtected(level, customLocation)) {
                PortalManager.triggerAntiPortalFeedback(level, pos, player);
                PortalManager.unregisterProjectile(projectile);
                projectile.discard();
                return null;
            }
            if (PortalManager.isDestinationTooClose(level, pos, customLocation)) {
                PortalManager.triggerMalfunctionFeedback(level, pos, player);
                PortalManager.unregisterProjectile(projectile);
                projectile.discard();
                return null;
            }
        }
        PortalManager.closeOverlappingPortals(level, pos, 1.25, null);
        PortalEntity portal = new PortalEntity((EntityType)ModEntities.PORTAL.get(), (Level)level);
        portal.moveTo(pos.x, pos.y, pos.z, yaw, 0.0f);
        portal.setOrientation(orientation);
        portal.setRotation(rotation);
        portal.setColor(color);
        portal.setScale(scale);
        portal.setOwnerGunId(gunId);
        portal.setGunMode(mode.getName());
        portal.setAutoClose(autoClose);
        portal.setHazardShield(projectile.hasHazardShield());
        portal.setSafePlacement(projectile.isSafePlacement());
        if (customLocation != null && !customLocation.isEmpty()) {
            portal.setCustomLocationData(customLocation);
        }
        level.addFreshEntity((Entity)portal);
        portal.playSound((SoundEvent)ModSounds.PORTAL_OPEN.get(), 1.0f, 1.0f);
        PortalPlacementHelper.clearVegetationInPortalArea((ServerLevel)level, (Vec3)pos, (int)orientation, (float)yaw, (float)scale);
        PortalManager.applyModeLinking(level, portal, gunId, mode, customLocation, player, color, scale, autoClose, orientation, rotation, yaw);
        PortalManager.unregisterProjectile(projectile);
        projectile.discard();
        return portal;
    }

    private static void applyModeLinking(ServerLevel level, PortalEntity portal, int gunId, PortalMode mode, CompoundTag customLocation, Player player, int color, float scale, boolean autoClose, int orientation, int rotation, float yaw) {
        if (gunId == 0) {
            return;
        }
        ItemStack gunStack = PortalManager.findGunInPlayer(player, gunId);
        if (gunStack.isEmpty() && level.getServer() != null) {
            gunStack = PortalManager.findGunOnServer(level.getServer(), gunId);
        }
        List portalIds = GUN_PORTALS.computeIfAbsent(gunId, k -> new CopyOnWriteArrayList());
        if (!gunStack.isEmpty()) {
            List nbtPortals = PortalGunItem.getActivePortals((ItemStack)gunStack);
            for (UUID id : nbtPortals) {
                if (portalIds.contains(id)) continue;
                portalIds.add(id);
            }
        }
        ArrayList<PortalEntity> activePortals = new ArrayList<PortalEntity>();
        for (UUID id : portalIds) {
            PortalEntity p = PortalManager.findPortal((Level)level, id);
            if (p == null || p.isRemoved() || p.isClosing()) continue;
            activePortals.add(p);
        }
        portalIds.clear();
        for (PortalEntity p : activePortals) {
            portalIds.add(p.getUUID());
        }
        portalIds.add(portal.getUUID());
        activePortals.add(portal);
        switch (1.$SwitchMap$org$ExtraTortik$ricks_stuff$portal$PortalMode[mode.ordinal()]) {
            case 1: {
                if (activePortals.size() == 2) {
                    PortalManager.linkPortals((PortalEntity)activePortals.get(0), (PortalEntity)activePortals.get(1));
                    break;
                }
                if (activePortals.size() <= 2) break;
                PortalEntity oldest = (PortalEntity)activePortals.get(0);
                PortalManager.removePortal(oldest, false);
                portalIds.remove(oldest.getUUID());
                activePortals.remove(0);
                PortalManager.linkPortals((PortalEntity)activePortals.get(0), (PortalEntity)activePortals.get(1));
                break;
            }
            case 2: {
                if (activePortals.size() == 2) {
                    PortalManager.linkPortals((PortalEntity)activePortals.get(0), (PortalEntity)activePortals.get(1));
                    break;
                }
                if (activePortals.size() <= 2) break;
                PortalEntity newer = (PortalEntity)activePortals.get(1);
                PortalManager.removePortal(newer, false);
                portalIds.remove(newer.getUUID());
                activePortals.remove(1);
                PortalManager.linkPortals((PortalEntity)activePortals.get(0), (PortalEntity)activePortals.get(1));
                break;
            }
            case 3: {
                if (activePortals.size() > 10) {
                    PortalManager.removeAllPortalsForGun(level, gunId);
                    portalIds.clear();
                    portalIds.add(portal.getUUID());
                    break;
                }
                if (activePortals.size() % 2 != 0) break;
                PortalEntity a = (PortalEntity)activePortals.get(activePortals.size() - 2);
                PortalEntity b = (PortalEntity)activePortals.get(activePortals.size() - 1);
                PortalManager.linkPortals(a, b);
                break;
            }
            case 4: {
                if (activePortals.size() > 10) {
                    PortalManager.removeAllPortalsForGun(level, gunId);
                    portalIds.clear();
                    portalIds.add(portal.getUUID());
                    portal.setRoot(true);
                    break;
                }
                if (activePortals.size() == 1) {
                    portal.setRoot(true);
                    break;
                }
                PortalEntity root = (PortalEntity)activePortals.get(0);
                root.setRoot(true);
                portal.setRoot(false);
                PortalManager.linkPortals(root, portal);
                break;
            }
            case 5: {
                double destY;
                int destOrientation;
                Vec3 safePos;
                boolean isCreative;
                String currentDimStr;
                Object oldP;
                for (ServerLevel serverLevel : level.getServer().getAllLevels()) {
                    for (Entity e : serverLevel.getAllEntities()) {
                        if (!(e instanceof PortalEntity) || (oldP = (PortalEntity)e).getOwnerGunId() != gunId || oldP.getUUID().equals(portal.getUUID()) || oldP.isRemoved() || oldP.isClosing()) continue;
                        PortalManager.removePortal((PortalEntity)oldP, false);
                    }
                }
                activePortals.clear();
                portalIds.clear();
                portalIds.add(portal.getUUID());
                if (customLocation == null || customLocation.isEmpty()) break;
                boolean isTargetingPlayer = customLocation.contains("TargetPlayerUUID");
                if (isTargetingPlayer) {
                    boolean isCreative2;
                    boolean bl = isCreative2 = player != null && player.getAbilities().instabuild;
                    if (!isCreative2 && !PortalGunItem.hasUpgrade((ItemStack)gunStack, (UpgradeType)UpgradeType.PLAYER_LOCATOR)) {
                        PortalManager.removePortal(portal, true);
                        portalIds.clear();
                        PortalManager.syncGunActivePortals(level.getServer(), player, gunId, portalIds);
                        PortalManager.triggerMalfunctionFeedback(level, portal.position(), player);
                        return;
                    }
                }
                String targetDimStr = customLocation.getString("Dimension");
                boolean isBlender = BlenderDimensionHelper.isBlenderDimension((String)targetDimStr);
                ResourceLocation targetDimLoc = ResourceLocation.tryParse((String)targetDimStr);
                if (PortalManager.isDimensionBlacklisted(level.dimension().location()) || !isBlender && targetDimLoc != null && PortalManager.isDimensionBlacklisted(targetDimLoc)) {
                    PortalManager.removePortal(portal, true);
                    portalIds.clear();
                    PortalManager.syncGunActivePortals(level.getServer(), player, gunId, portalIds);
                    PortalManager.triggerMalfunctionFeedback(level, portal.position(), player);
                    return;
                }
                if (isBlender) {
                    oldP = level.getServer().getAllLevels().iterator();
                    block14: while (true) {
                        if (!oldP.hasNext()) {
                            PortalManager.syncGunActivePortals(level.getServer(), player, gunId, portalIds);
                            return;
                        }
                        ServerLevel serverLevel = (ServerLevel)oldP.next();
                        Iterator iterator = serverLevel.getAllEntities().iterator();
                        while (true) {
                            PortalEntity oldP2;
                            if (!iterator.hasNext()) continue block14;
                            Entity e = (Entity)iterator.next();
                            if (!(e instanceof PortalEntity) || (oldP2 = (PortalEntity)e).getOwnerGunId() != gunId || oldP2.getUUID().equals(portal.getUUID()) || oldP2.isRemoved() || oldP2.isClosing()) continue;
                            PortalManager.removePortal(oldP2, false);
                        }
                        break;
                    }
                }
                MinecraftServer server = level.getServer();
                double targetX = customLocation.getDouble("X");
                double targetY = customLocation.getDouble("Y");
                double targetZ = customLocation.getDouble("Z");
                float targetYaw = yaw;
                if (Config.allowPlayerTargeting && isTargetingPlayer && server != null) {
                    try {
                        UUID targetUUID = UUID.fromString(customLocation.getString("TargetPlayerUUID"));
                        ServerPlayer targetPlayer = server.getPlayerList().getPlayer(targetUUID);
                        if (targetPlayer != null && !targetPlayer.isRemoved()) {
                            targetDimStr = targetPlayer.level().dimension().location().toString();
                            Vec3 playerLook = targetPlayer.getLookAngle();
                            targetX = targetPlayer.getX() - playerLook.x * 1.5;
                            targetY = targetPlayer.getY();
                            targetZ = targetPlayer.getZ() - playerLook.z * 1.5;
                            targetYaw = targetPlayer.getYRot();
                        }
                    }
                    catch (Exception targetUUID) {
                        // empty catch block
                    }
                }
                boolean isCrossDimensional = !targetDimStr.equals(currentDimStr = level.dimension().location().toString());
                boolean bl = isCreative = player != null && player.getAbilities().instabuild;
                if (isCrossDimensional && !Config.allowCrossDimensionTeleport) {
                    PortalManager.removePortal(portal, true);
                    portalIds.clear();
                    PortalManager.syncGunActivePortals(level.getServer(), player, gunId, portalIds);
                    PortalManager.triggerMalfunctionFeedback(level, portal.position(), player);
                    return;
                }
                if (isCrossDimensional && !isCreative && !PortalGunItem.hasUpgrade((ItemStack)gunStack, (UpgradeType)UpgradeType.DIMENSIONAL)) {
                    PortalManager.removePortal(portal, true);
                    portalIds.clear();
                    PortalManager.syncGunActivePortals(level.getServer(), player, gunId, portalIds);
                    PortalManager.triggerMalfunctionFeedback(level, portal.position(), player);
                    return;
                }
                ServerLevel targetLevel = PortalManager.resolveTargetLevel(server, targetDimStr, level);
                boolean safePlacement = portal.isSafePlacement();
                if (customLocation.contains("SafePlacement")) {
                    safePlacement = customLocation.getBoolean("SafePlacement");
                }
                if (customLocation.getBoolean("IsWorldSpawn")) {
                    Vec3 spawnPosVec = PortalPlacementHelper.getDimensionSpawnPos((ServerLevel)targetLevel);
                    targetX = spawnPosVec.x;
                    targetY = spawnPosVec.y;
                    targetZ = spawnPosVec.z;
                    safePos = spawnPosVec;
                } else if (safePlacement) {
                    targetBlockPos = BlockPos.containing((double)targetX, (double)targetY, (double)targetZ);
                    targetLevel.getChunk(targetBlockPos);
                    safePos = PortalPlacementHelper.findSafePlacement((ServerLevel)targetLevel, (Vec3)new Vec3(targetX, targetY, targetZ));
                } else {
                    targetBlockPos = BlockPos.containing((double)targetX, (double)targetY, (double)targetZ);
                    targetLevel.getChunk(targetBlockPos);
                    safePos = new Vec3(targetX, targetY, targetZ);
                }
                if (targetLevel == level) {
                    double dist3D = safePos.distanceTo(portal.position());
                    double dx = safePos.x - portal.getX();
                    double dz = safePos.z - portal.getZ();
                    double dy = Math.abs(safePos.y - portal.getY());
                    double distXZ = Math.sqrt(dx * dx + dz * dz);
                    double rawDx = targetX - portal.getX();
                    double rawDz = targetZ - portal.getZ();
                    double rawDistXZ = Math.sqrt(rawDx * rawDx + rawDz * rawDz);
                    if (dist3D < 3.5 || distXZ < 3.0 && dy < 6.0 || rawDistXZ < 3.0 && Math.abs(targetY - portal.getY()) < 6.0) {
                        PortalManager.removePortal(portal, true);
                        portalIds.clear();
                        PortalManager.syncGunActivePortals(server, player, gunId, portalIds);
                        PortalManager.triggerMalfunctionFeedback(level, portal.position(), player);
                        return;
                    }
                }
                for (Entity e : targetLevel.getAllEntities()) {
                    PortalEntity p;
                    if (!(e instanceof PortalEntity) || (p = (PortalEntity)e).getOwnerGunId() != gunId || p.getUUID().equals(portal.getUUID()) || p.isRemoved() || p.isClosing()) continue;
                    PortalManager.removePortal(p, false);
                }
                PortalManager.closeOverlappingPortals(targetLevel, safePos, 6.0, portal);
                BlockPos safeBlockPos = BlockPos.containing((double)safePos.x, (double)safePos.y, (double)safePos.z);
                boolean hasCeilingSpace = true;
                for (int dy = 1; dy <= 3; ++dy) {
                    BlockState stateAbove = targetLevel.getBlockState(safeBlockPos.above(dy));
                    if (stateAbove.isAir() || !stateAbove.blocksMotion()) continue;
                    hasCeilingSpace = false;
                    break;
                }
                if (!safePlacement) {
                    destOrientation = 0;
                    destY = targetY;
                } else if (targetLevel.dimension().equals(Level.END) && customLocation.getBoolean("IsWorldSpawn")) {
                    destOrientation = 0;
                    destY = safePos.y + 1.05;
                    targetYaw = -90.0f;
                } else {
                    boolean spawnOverhead;
                    boolean bl2 = spawnOverhead = hasCeilingSpace && ThreadLocalRandom.current().nextBoolean();
                    if (spawnOverhead) {
                        destOrientation = 1;
                        destY = safePos.y + 2.85;
                    } else {
                        destOrientation = 0;
                        destY = safePos.y + 1.05;
                    }
                }
                PortalEntity destPortal = new PortalEntity((EntityType)ModEntities.PORTAL.get(), (Level)targetLevel);
                destPortal.moveTo(safePos.x, destY, safePos.z, targetYaw, 0.0f);
                destPortal.setOrientation(destOrientation);
                destPortal.setRotation(0);
                destPortal.setColor(color);
                destPortal.setScale(scale);
                destPortal.setOwnerGunId(gunId);
                destPortal.setGunMode("CUSTOM");
                destPortal.setAutoClose(autoClose);
                destPortal.setHazardShield(portal.hasHazardShield());
                destPortal.setSafePlacement(safePlacement);
                targetLevel.addFreshEntity((Entity)destPortal);
                destPortal.playSound((SoundEvent)ModSounds.PORTAL_OPEN.get(), 1.0f, 1.0f);
                PortalPlacementHelper.clearVegetationInPortalArea((ServerLevel)targetLevel, (Vec3)new Vec3(safePos.x, destY, safePos.z), (int)destOrientation, (float)targetYaw, (float)scale);
                PortalManager.linkPortals(portal, destPortal);
                portalIds.add(destPortal.getUUID());
                break;
            }
        }
        PortalManager.syncGunActivePortals(level.getServer(), player, gunId, portalIds);
    }

    public static void linkPortals(PortalEntity a, PortalEntity b) {
        if (a == null) return;
        if (b == null) return;
        if (a.isRemoved()) return;
        if (b.isRemoved()) {
            return;
        }
        a.setDualityPortalId(b.getUUID());
        a.setLinked(true);
        b.setDualityPortalId(a.getUUID());
        b.setLinked(true);
    }

    public static void removePortal(PortalEntity portal, boolean removeLinked) {
        boolean shouldRemoveLinked;
        MinecraftServer minecraftServer;
        if (portal == null) {
            return;
        }
        Level level = portal.level();
        if (level instanceof ServerLevel) {
            ServerLevel sl = (ServerLevel)level;
            minecraftServer = sl.getServer();
        } else {
            minecraftServer = null;
        }
        MinecraftServer server = minecraftServer;
        boolean bl = shouldRemoveLinked = removeLinked || "CUSTOM".equalsIgnoreCase(portal.getGunMode());
        if (shouldRemoveLinked && portal.getDualityPortalId().isPresent()) {
            int gunId;
            UUID linkedUUID = (UUID)portal.getDualityPortalId().get();
            PortalEntity linked = PortalManager.findPortal(portal.level(), linkedUUID);
            int linkedGunId = 0;
            if (linked != null) {
                linkedGunId = linked.getOwnerGunId();
                if (!linked.isRemoved() && !linked.isClosing()) {
                    PortalManager.playPortalCloseSound(linked);
                    linked.startClosing();
                }
                if (linkedGunId != 0) {
                    List<UUID> linkedIds = GUN_PORTALS.get(linkedGunId);
                    if (linkedIds != null) {
                        linkedIds.remove(linkedUUID);
                    }
                    PortalManager.removePortalFromGunNBT(server, null, linkedGunId, linkedUUID);
                }
            } else if (server != null && (gunId = portal.getOwnerGunId()) != 0) {
                List<UUID> ids = GUN_PORTALS.get(gunId);
                if (ids != null) {
                    ids.remove(linkedUUID);
                }
                PortalManager.removePortalFromGunNBT(server, null, gunId, linkedUUID);
            }
            if (server != null) {
                PortalSavedData.get((MinecraftServer)server).tombstonePortal(linkedUUID, linkedGunId != 0 ? linkedGunId : portal.getOwnerGunId());
            }
        }
        if (!portal.isRemoved() && !portal.isClosing()) {
            PortalManager.playPortalCloseSound(portal);
            portal.startClosing();
        }
        int gunId = portal.getOwnerGunId();
        UUID portalUUID = portal.getUUID();
        if (server != null) {
            PortalSavedData.get((MinecraftServer)server).tombstonePortal(portalUUID, gunId);
        }
        if (gunId == 0) return;
        List<UUID> portalIds = GUN_PORTALS.get(gunId);
        if (portalIds != null) {
            portalIds.remove(portalUUID);
        }
        PortalManager.removePortalFromGunNBT(server, null, gunId, portalUUID);
    }

    public static void closeOverlappingPortals(ServerLevel level, Vec3 pos, double radius, @Nullable PortalEntity exclude) {
        if (level == null) return;
        if (pos == null) {
            return;
        }
        List portals = level.getEntitiesOfClass(PortalEntity.class, new AABB(pos.x - radius, pos.y - radius, pos.z - radius, pos.x + radius, pos.y + radius, pos.z + radius));
        Iterator iterator = portals.iterator();
        while (iterator.hasNext()) {
            PortalEntity p = (PortalEntity)iterator.next();
            if (p == exclude || p.isRemoved() || p.isClosing() || !(p.position().distanceToSqr(pos) <= radius * radius)) continue;
            PortalManager.removePortal(p, true);
        }
    }

    public static int removeAllPortalsForGun(ServerLevel level, int gunId) {
        Iterator savedIds;
        if (gunId == 0) {
            return 0;
        }
        int closedCount = 0;
        HashSet<UUID> closedUUIDs = new HashSet<UUID>();
        MinecraftServer server = level != null ? level.getServer() : null;
        List<UUID> ids = GUN_PORTALS.remove(gunId);
        if (server != null) {
            savedIds = PortalSavedData.get((MinecraftServer)server).getPortalsForGun(gunId);
            PortalSavedData.get((MinecraftServer)server).tombstoneAllPortalsForGun(gunId, ids);
            if (ids == null) {
                ids = new ArrayList(savedIds);
            } else {
                Iterator iterator = savedIds.iterator();
                while (iterator.hasNext()) {
                    UUID sId = (UUID)iterator.next();
                    if (ids.contains(sId)) continue;
                    ids.add(sId);
                }
            }
        }
        if (ids != null) {
            for (UUID id : ids) {
                PortalEntity p = PortalManager.findPortal((Level)level, id);
                if (p == null || p.isRemoved() || p.isClosing()) continue;
                PortalManager.playPortalCloseSound(p);
                p.startClosing();
                closedUUIDs.add(p.getUUID());
                ++closedCount;
            }
        }
        if (server == null) return closedCount;
        savedIds = server.getAllLevels().iterator();
        block2: while (true) {
            if (!savedIds.hasNext()) {
                ItemStack gunStack = PortalManager.findGunOnServer(server, gunId);
                if (gunStack.isEmpty()) return closedCount;
                PortalGunItem.setActivePortals((ItemStack)gunStack, Collections.emptyList());
                return closedCount;
            }
            ServerLevel serverLevel = (ServerLevel)savedIds.next();
            Iterator iterator = serverLevel.getAllEntities().iterator();
            while (true) {
                PortalEntity p;
                if (!iterator.hasNext()) continue block2;
                Entity entity = (Entity)iterator.next();
                if (!(entity instanceof PortalEntity) || (p = (PortalEntity)entity).getOwnerGunId() != gunId || p.isRemoved() || p.isClosing() || closedUUIDs.contains(p.getUUID())) continue;
                PortalManager.playPortalCloseSound(p);
                p.startClosing();
                closedUUIDs.add(p.getUUID());
                ++closedCount;
            }
            break;
        }
    }

    public static PortalEntity findPortal(Level level, UUID uuid) {
        PortalEntity otherPortal;
        Entity otherEntity;
        ServerLevel otherLevel;
        PortalEntity portal;
        if (uuid == null) {
            return null;
        }
        if (!(level instanceof ServerLevel)) return null;
        ServerLevel serverLevel = (ServerLevel)level;
        Entity entity = serverLevel.getEntity(uuid);
        if (entity instanceof PortalEntity && !(portal = (PortalEntity)entity).isRemoved()) {
            return portal;
        }
        MinecraftServer server = serverLevel.getServer();
        if (server == null) return null;
        Iterator iterator = server.getAllLevels().iterator();
        do {
            if (!iterator.hasNext()) return null;
        } while ((otherLevel = (ServerLevel)iterator.next()) == serverLevel || !((otherEntity = otherLevel.getEntity(uuid)) instanceof PortalEntity) || (otherPortal = (PortalEntity)otherEntity).isRemoved());
        return otherPortal;
    }

    public static List<PortalEntity> getPortalsForGun(MinecraftServer server, int gunId) {
        List nbtPortals;
        ItemStack gunStack;
        ArrayList<PortalEntity> result = new ArrayList<PortalEntity>();
        if (server == null) return result;
        if (gunId == 0) {
            return result;
        }
        List<UUID> ids = GUN_PORTALS.get(gunId);
        if (!(ids != null && !ids.isEmpty() || (gunStack = PortalManager.findGunOnServer(server, gunId)).isEmpty() || (nbtPortals = PortalGunItem.getActivePortals((ItemStack)gunStack)).isEmpty())) {
            ids = new CopyOnWriteArrayList<UUID>(nbtPortals);
            GUN_PORTALS.put(gunId, ids);
        }
        if (ids != null && !ids.isEmpty()) {
            for (UUID id : ids) {
                PortalEntity portal = PortalManager.findPortal((Level)server.overworld(), id);
                if (portal == null || portal.getOwnerGunId() != gunId || portal.isRemoved() || portal.isClosing() || result.contains(portal)) continue;
                result.add(portal);
            }
            if (!result.isEmpty()) {
                return result;
            }
        }
        Iterator<Object> iterator = server.getAllLevels().iterator();
        block1: while (iterator.hasNext()) {
            ServerLevel level = (ServerLevel)iterator.next();
            Iterator iterator2 = level.getAllEntities().iterator();
            while (true) {
                PortalEntity portal;
                if (!iterator2.hasNext()) continue block1;
                Entity entity = (Entity)iterator2.next();
                if (!(entity instanceof PortalEntity) || (portal = (PortalEntity)entity).getOwnerGunId() != gunId || portal.isRemoved() || portal.isClosing()) continue;
                result.add(portal);
            }
            break;
        }
        return result;
    }
}

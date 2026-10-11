/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.Container
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.GameType
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.phys.Vec3
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.block.entity.PhoenixCapsuleBlockEntity
 *  org.ExtraTortik.ricks_stuff.entity.omega.ErasurePurgeHelper
 *  org.ExtraTortik.ricks_stuff.entity.omega.MultiverseErasureSavedData
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaBlockErasureManager
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDevicePhase
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.S2COmegaDeviceEffectPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CSyncMultiverseErasurePacket
 *  org.ExtraTortik.ricks_stuff.phoenix.PhoenixManager
 *  org.ExtraTortik.ricks_stuff.phoenix.PhoenixManager$CapsuleLocation
 */
package org.ExtraTortik.ricks_stuff.entity.omega;

import java.util.Iterator;
import java.util.UUID;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.block.entity.PhoenixCapsuleBlockEntity;
import org.ExtraTortik.ricks_stuff.entity.omega.ErasurePurgeHelper;
import org.ExtraTortik.ricks_stuff.entity.omega.MultiverseErasureSavedData;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaBlockErasureManager;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDevicePhase;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.S2COmegaDeviceEffectPacket;
import org.ExtraTortik.ricks_stuff.network.packet.S2CSyncMultiverseErasurePacket;
import org.ExtraTortik.ricks_stuff.phoenix.PhoenixManager;
import org.jetbrains.annotations.Nullable;

public class OmegaDeviceManager {
    public static final int COOLDOWN_DURATION_TICKS = 200;

    public static boolean canActivate(OmegaDeviceEntity device, @Nullable Player player, @Nullable ItemStack dnaVial) {
        if (device == null) return false;
        if (!device.isAlive()) {
            return false;
        }
        if (device.getPhase() != OmegaDevicePhase.IDLE) {
            return false;
        }
        if (device.getCooldownRemaining() <= 0) return true;
        return false;
    }

    public static boolean startSequence(OmegaDeviceEntity device, @Nullable Entity target, @Nullable String targetType, @Nullable UUID targetUUID) {
        if (device == null) return false;
        if (device.getPhase() != OmegaDevicePhase.IDLE) return false;
        if (device.getCooldownRemaining() > 0) {
            return false;
        }
        if (target != null) {
            ResourceLocation key;
            device.setTargetEntity(target);
            if (targetUUID == null) {
                targetUUID = target.getUUID();
            }
            if (targetType == null && (key = BuiltInRegistries.ENTITY_TYPE.getKey((Object)target.getType())) != null) {
                targetType = key.toString();
            }
        }
        if (targetUUID != null) {
            device.setTargetUUID(targetUUID);
        }
        if (targetType != null) {
            device.setTargetType(targetType);
        }
        device.setPhase(OmegaDevicePhase.TARGETING);
        device.setPhaseTicks(0);
        Level level = device.level();
        if (!(level instanceof ServerLevel)) return true;
        ServerLevel serverLevel = (ServerLevel)level;
        serverLevel.playSound(null, device.getX(), device.getY(), device.getZ(), (SoundEvent)ModSounds.OMEGA_DEVICE_START.get(), SoundSource.BLOCKS, 32.0f, 1.0f);
        return true;
    }

    public static void executeMultiverseErasure(ServerLevel level, @Nullable Entity target, @Nullable String targetType, @Nullable UUID targetUUID, Vec3 originPos) {
        boolean isPlayer;
        MinecraftServer server = level.getServer();
        if (server == null) {
            return;
        }
        MultiverseErasureSavedData storage = MultiverseErasureSavedData.get((MinecraftServer)server);
        UUID effectiveUUID = targetUUID;
        String effectiveType = targetType;
        if (target instanceof ItemEntity) {
            ItemEntity itemEntity = (ItemEntity)target;
            ItemStack stack = itemEntity.getItem();
            if (stack.isEmpty()) return;
            ResourceLocation key = BuiltInRegistries.ITEM.getKey((Object)stack.getItem());
            if (key == null) return;
            OmegaDeviceManager.executeMultiverseItemErasure(level, key.toString(), originPos);
            return;
        }
        if (target != null) {
            ResourceLocation key;
            if (effectiveUUID == null) {
                effectiveUUID = target.getUUID();
            }
            if (effectiveType == null && (key = BuiltInRegistries.ENTITY_TYPE.getKey((Object)target.getType())) != null) {
                effectiveType = key.toString();
            }
        }
        boolean bl = isPlayer = target instanceof ServerPlayer || effectiveUUID != null && server.getPlayerList().getPlayer(effectiveUUID) != null;
        if (isPlayer && !Config.allowPlayerErasure) {
            return;
        }
        if (!isPlayer && effectiveType != null && !Config.allowMobErasure) {
            return;
        }
        if (isPlayer && effectiveUUID != null) {
            storage.eraseEntity(effectiveUUID, null);
        } else if (!isPlayer && effectiveType != null) {
            storage.eraseEntityType(effectiveType);
        }
        if (isPlayer && effectiveUUID != null) {
            OmegaDeviceManager.shatterPhoenixCapsules(server, effectiveUUID);
        }
        if (target != null && target.isAlive()) {
            if (target instanceof ServerPlayer) {
                ServerPlayer playerTarget = (ServerPlayer)target;
                if (Config.allowPlayerErasure) {
                    OmegaDeviceManager.handlePlayerErasure(playerTarget);
                }
            } else {
                target.discard();
            }
        }
        if (!isPlayer || effectiveUUID == null) {
            if (!isPlayer && effectiveType != null && Config.allowMobErasure) {
                for (ServerLevel sLevel : server.getAllLevels()) {
                    for (Entity e : sLevel.getAllEntities()) {
                        ResourceLocation key;
                        if (e == null || !e.isAlive() || e instanceof Player || (key = BuiltInRegistries.ENTITY_TYPE.getKey((Object)e.getType())) == null || !key.toString().equals(effectiveType)) continue;
                        e.discard();
                    }
                }
            }
        } else {
            for (ServerLevel sLevel : server.getAllLevels()) {
                Entity found = sLevel.getEntity(effectiveUUID);
                if (found == null || found == target || !found.isAlive()) continue;
                if (found instanceof ServerPlayer) {
                    ServerPlayer playerFound = (ServerPlayer)found;
                    OmegaDeviceManager.handlePlayerErasure(playerFound);
                    continue;
                }
                found.discard();
            }
        }
        ModMessages.sendToClients((CustomPacketPayload)new S2CSyncMultiverseErasurePacket(storage));
        double ox = originPos != null ? originPos.x : 0.0;
        double oy = originPos != null ? originPos.y : 0.0;
        double oz = originPos != null ? originPos.z : 0.0;
        ModMessages.sendToClients((CustomPacketPayload)new S2COmegaDeviceEffectPacket(2, ox, oy, oz, 1.0f, 40, 1.0f, 60));
        Iterator iterator = server.getAllLevels().iterator();
        while (iterator.hasNext()) {
            ServerLevel sLevel = (ServerLevel)iterator.next();
            sLevel.playSound(null, ox, oy, oz, (SoundEvent)ModSounds.OMEGA_DEVICE_ERASE.get(), SoundSource.AMBIENT, 100.0f, 1.0f);
        }
    }

    public static void executeMultiverseItemErasure(ServerLevel level, String itemId, Vec3 originPos) {
        if (!Config.allowItemErasure) {
            return;
        }
        MinecraftServer server = level.getServer();
        if (server == null) return;
        if (itemId == null) return;
        if (itemId.isBlank()) {
            return;
        }
        MultiverseErasureSavedData storage = MultiverseErasureSavedData.get((MinecraftServer)server);
        storage.eraseItem(itemId);
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            ErasurePurgeHelper.purgePlayerInventories((ServerPlayer)player, (MultiverseErasureSavedData)storage);
        }
        Iterator<Object> iterator = server.getAllLevels().iterator();
        block1: while (true) {
            if (!iterator.hasNext()) {
                if (Config.allowBlockErasure) {
                    OmegaBlockErasureManager.clearCleanedCache();
                    OmegaBlockErasureManager.queueAllOnlinePlayers((MinecraftServer)server);
                }
                ModMessages.sendToClients((CustomPacketPayload)new S2CSyncMultiverseErasurePacket(storage));
                double ox = originPos != null ? originPos.x : 0.0;
                double oy = originPos != null ? originPos.y : 0.0;
                double oz = originPos != null ? originPos.z : 0.0;
                ModMessages.sendToClients((CustomPacketPayload)new S2COmegaDeviceEffectPacket(2, ox, oy, oz, 1.0f, 40, 1.0f, 60));
                Iterator iterator2 = server.getAllLevels().iterator();
                while (iterator2.hasNext()) {
                    ServerLevel sLevel = (ServerLevel)iterator2.next();
                    sLevel.playSound(null, ox, oy, oz, (SoundEvent)ModSounds.OMEGA_DEVICE_ERASE.get(), SoundSource.AMBIENT, 100.0f, 1.0f);
                }
                return;
            }
            ServerLevel sLevel = (ServerLevel)iterator.next();
            Iterator iterator3 = sLevel.getAllEntities().iterator();
            while (true) {
                ItemEntity itemEntity;
                if (!iterator3.hasNext()) continue block1;
                Entity entity = (Entity)iterator3.next();
                if (entity instanceof ItemEntity && (itemEntity = (ItemEntity)entity).isAlive()) {
                    ItemStack stack = itemEntity.getItem();
                    if (stack.isEmpty() || !storage.isItemErased(stack.getItem())) continue;
                    itemEntity.discard();
                    continue;
                }
                if (!(entity instanceof Container)) continue;
                Container containerEntity = (Container)entity;
                ErasurePurgeHelper.purgeContainer((Container)containerEntity, (MultiverseErasureSavedData)storage);
            }
            break;
        }
    }

    public static void handlePlayerErasure(ServerPlayer player) {
        player.getPersistentData().putBoolean("OmegaErased", true);
        player.setGameMode(GameType.SPECTATOR);
        if (player.getVehicle() != null) {
            player.stopRiding();
        }
        player.setDeltaMovement(Vec3.ZERO);
    }

    public static void handlePlayerUnerasure(ServerPlayer player) {
        if (player == null) {
            return;
        }
        player.getPersistentData().remove("OmegaErased");
        if (player.gameMode.getGameModeForPlayer() != GameType.SPECTATOR) return;
        GameType defaultType = player.server != null && player.server.getDefaultGameType() != null ? player.server.getDefaultGameType() : GameType.SURVIVAL;
        player.setGameMode(defaultType);
    }

    public static void clearAllErasures(MinecraftServer server) {
        if (server == null) {
            return;
        }
        MultiverseErasureSavedData.get((MinecraftServer)server).clearAll();
        OmegaBlockErasureManager.clearQueue();
        Iterator iterator = server.getPlayerList().getPlayers().iterator();
        while (true) {
            if (!iterator.hasNext()) {
                ModMessages.sendToClients((CustomPacketPayload)new S2CSyncMultiverseErasurePacket(MultiverseErasureSavedData.get((MinecraftServer)server)));
                return;
            }
            ServerPlayer player = (ServerPlayer)iterator.next();
            if (!player.getPersistentData().getBoolean("OmegaErased") && player.gameMode.getGameModeForPlayer() != GameType.SPECTATOR) continue;
            OmegaDeviceManager.handlePlayerUnerasure(player);
        }
    }

    public static boolean unerasePlayer(MinecraftServer server, UUID uuid) {
        if (server == null) return false;
        if (uuid == null) {
            return false;
        }
        boolean removed = MultiverseErasureSavedData.get((MinecraftServer)server).unerase(uuid);
        ServerPlayer player = server.getPlayerList().getPlayer(uuid);
        if (player != null) {
            if (player.getPersistentData().getBoolean("OmegaErased") || player.gameMode.getGameModeForPlayer() == GameType.SPECTATOR) {
                removed = true;
            }
            OmegaDeviceManager.handlePlayerUnerasure(player);
        }
        ModMessages.sendToClients((CustomPacketPayload)new S2CSyncMultiverseErasurePacket(MultiverseErasureSavedData.get((MinecraftServer)server)));
        return removed;
    }

    public static boolean uneraseEntityType(MinecraftServer server, String entityTypeId) {
        if (server == null) return false;
        if (entityTypeId == null) return false;
        if (entityTypeId.isBlank()) {
            return false;
        }
        boolean res = MultiverseErasureSavedData.get((MinecraftServer)server).uneraseType(entityTypeId);
        if (!res) return res;
        ModMessages.sendToClients((CustomPacketPayload)new S2CSyncMultiverseErasurePacket(MultiverseErasureSavedData.get((MinecraftServer)server)));
        return res;
    }

    public static boolean uneraseItem(MinecraftServer server, String itemId) {
        if (server == null) return false;
        if (itemId == null) return false;
        if (itemId.isBlank()) {
            return false;
        }
        boolean res = MultiverseErasureSavedData.get((MinecraftServer)server).uneraseItem(itemId);
        if (!res) return res;
        ModMessages.sendToClients((CustomPacketPayload)new S2CSyncMultiverseErasurePacket(MultiverseErasureSavedData.get((MinecraftServer)server)));
        return res;
    }

    public static void shatterPhoenixCapsules(MinecraftServer server, UUID playerUUID) {
        if (server == null) return;
        if (playerUUID == null) {
            return;
        }
        while (PhoenixManager.hasReadyCapsule((MinecraftServer)server, (UUID)playerUUID)) {
            ServerLevel targetLevel;
            PhoenixManager.CapsuleLocation loc = PhoenixManager.popNextReadyCapsule((MinecraftServer)server, (UUID)playerUUID);
            if (loc == null || (targetLevel = server.getLevel(loc.dimension)) == null) continue;
            targetLevel.getChunk(loc.pos);
            BlockEntity be = targetLevel.getBlockEntity(loc.pos);
            if (!(be instanceof PhoenixCapsuleBlockEntity)) continue;
            PhoenixCapsuleBlockEntity capsule = (PhoenixCapsuleBlockEntity)be;
            capsule.shatter();
        }
    }

    public static boolean isEntityErased(MinecraftServer server, Entity entity) {
        if (server == null) return false;
        if (entity == null) {
            return false;
        }
        MultiverseErasureSavedData storage = MultiverseErasureSavedData.get((MinecraftServer)server);
        return storage.isEntityOrTypeErased(entity);
    }

    public static boolean isPlayerErased(MinecraftServer server, UUID playerUUID) {
        if (server == null) return false;
        if (playerUUID == null) {
            return false;
        }
        MultiverseErasureSavedData storage = MultiverseErasureSavedData.get((MinecraftServer)server);
        return storage.isErased(playerUUID);
    }

    public static boolean isTypeErased(MinecraftServer server, EntityType<?> type) {
        if (server == null) return false;
        if (type == null) {
            return false;
        }
        MultiverseErasureSavedData storage = MultiverseErasureSavedData.get((MinecraftServer)server);
        return storage.isTypeErased(type);
    }

    public static boolean isTypeErased(MinecraftServer server, String typeId) {
        if (server == null) return false;
        if (typeId == null) {
            return false;
        }
        MultiverseErasureSavedData storage = MultiverseErasureSavedData.get((MinecraftServer)server);
        return storage.isTypeErased(typeId);
    }

    public static boolean isItemErased(MinecraftServer server, Item item) {
        if (server == null) return false;
        if (item == null) {
            return false;
        }
        MultiverseErasureSavedData storage = MultiverseErasureSavedData.get((MinecraftServer)server);
        return storage.isItemErased(item);
    }

    public static boolean isItemErased(MinecraftServer server, String itemId) {
        if (server == null) return false;
        if (itemId == null) {
            return false;
        }
        MultiverseErasureSavedData storage = MultiverseErasureSavedData.get((MinecraftServer)server);
        return storage.isItemErased(itemId);
    }
}

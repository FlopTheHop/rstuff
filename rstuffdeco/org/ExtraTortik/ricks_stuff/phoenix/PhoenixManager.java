/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.saveddata.SavedData
 *  net.minecraft.world.level.saveddata.SavedData$Factory
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.phoenix.PhoenixManager$CapsuleLocation
 */
package org.ExtraTortik.ricks_stuff.phoenix;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.phoenix.PhoenixManager;
import org.jetbrains.annotations.NotNull;

public class PhoenixManager
extends SavedData {
    private static final String DATA_NAME = "ricks_stuff_phoenix_queue";
    private final Map<UUID, List<CapsuleLocation>> playerQueues = new ConcurrentHashMap<UUID, List<CapsuleLocation>>();
    public static final SavedData.Factory<PhoenixManager> FACTORY = new SavedData.Factory(PhoenixManager::new, PhoenixManager::load, null);

    public static PhoenixManager get(MinecraftServer server) {
        if (server == null) {
            return new PhoenixManager();
        }
        ServerLevel overworld = server.overworld();
        return (PhoenixManager)overworld.getDataStorage().computeIfAbsent(FACTORY, DATA_NAME);
    }

    public static boolean registerCapsule(MinecraftServer server, UUID playerUUID, BlockPos pos, ResourceKey<Level> dimension) {
        CapsuleLocation loc;
        if (!Config.phoenixProtocolEnabled) {
            return false;
        }
        if (server == null) return false;
        if (playerUUID == null) return false;
        if (pos == null) return false;
        if (dimension == null) {
            return false;
        }
        PhoenixManager manager = PhoenixManager.get(server);
        List queue = manager.playerQueues.computeIfAbsent(playerUUID, k -> new CopyOnWriteArrayList());
        if (queue.contains(loc = new CapsuleLocation(pos, dimension))) return true;
        if (queue.size() >= Config.maxCapsulesPerPlayer) {
            return false;
        }
        queue.add(loc);
        manager.setDirty();
        return true;
    }

    public static void unregisterCapsule(MinecraftServer server, UUID playerUUID, BlockPos pos, ResourceKey<Level> dimension) {
        if (server == null) return;
        if (playerUUID == null) return;
        if (pos == null) return;
        if (dimension == null) {
            return;
        }
        PhoenixManager manager = PhoenixManager.get(server);
        List<CapsuleLocation> queue = manager.playerQueues.get(playerUUID);
        if (queue == null) return;
        queue.remove(new CapsuleLocation(pos, dimension));
        if (queue.isEmpty()) {
            manager.playerQueues.remove(playerUUID);
        }
        manager.setDirty();
    }

    public static boolean hasReadyCapsule(MinecraftServer server, UUID playerUUID) {
        if (server == null) return false;
        if (playerUUID == null) {
            return false;
        }
        PhoenixManager manager = PhoenixManager.get(server);
        List<CapsuleLocation> queue = manager.playerQueues.get(playerUUID);
        return queue != null && !queue.isEmpty();
    }

    public static CapsuleLocation popNextReadyCapsule(MinecraftServer server, UUID playerUUID) {
        if (server == null) return null;
        if (playerUUID == null) {
            return null;
        }
        PhoenixManager manager = PhoenixManager.get(server);
        List<CapsuleLocation> queue = manager.playerQueues.get(playerUUID);
        if (queue == null) return null;
        if (queue.isEmpty()) {
            return null;
        }
        CapsuleLocation loc = queue.remove(0);
        if (queue.isEmpty()) {
            manager.playerQueues.remove(playerUUID);
        }
        manager.setDirty();
        return loc;
    }

    public static PhoenixManager load(CompoundTag tag, HolderLookup.Provider registries) {
        PhoenixManager manager = new PhoenixManager();
        if (!tag.contains("Queues", 10)) return manager;
        CompoundTag queuesTag = tag.getCompound("Queues");
        Iterator iterator = queuesTag.getAllKeys().iterator();
        while (iterator.hasNext()) {
            String key = (String)iterator.next();
            try {
                UUID uuid = UUID.fromString(key);
                ListTag list = queuesTag.getList(key, 10);
                CopyOnWriteArrayList<CapsuleLocation> queue = new CopyOnWriteArrayList<CapsuleLocation>();
                for (int i = 0; i < list.size(); ++i) {
                    CompoundTag locTag = list.getCompound(i);
                    BlockPos pos = BlockPos.of((long)locTag.getLong("Pos"));
                    String dimStr = locTag.getString("Dimension");
                    ResourceLocation dimLoc = ResourceLocation.tryParse((String)dimStr);
                    if (dimLoc == null) continue;
                    ResourceKey dimKey = ResourceKey.create((ResourceKey)Registries.DIMENSION, (ResourceLocation)dimLoc);
                    queue.add(new CapsuleLocation(pos, dimKey));
                }
                manager.playerQueues.put(uuid, queue);
            }
            catch (Exception exception) {}
        }
        return manager;
    }

    @NotNull
    public CompoundTag save(@NotNull CompoundTag tag, HolderLookup.Provider registries) {
        CompoundTag queuesTag = new CompoundTag();
        Iterator<Map.Entry<UUID, List<CapsuleLocation>>> iterator = this.playerQueues.entrySet().iterator();
        while (true) {
            if (!iterator.hasNext()) {
                tag.put("Queues", (Tag)queuesTag);
                return tag;
            }
            Map.Entry<UUID, List<CapsuleLocation>> entry = iterator.next();
            ListTag list = new ListTag();
            for (CapsuleLocation loc : entry.getValue()) {
                CompoundTag locTag = new CompoundTag();
                locTag.putLong("Pos", loc.pos.asLong());
                locTag.putString("Dimension", loc.dimension.location().toString());
                list.add((Object)locTag);
            }
            queuesTag.put(entry.getKey().toString(), (Tag)list);
        }
    }
}

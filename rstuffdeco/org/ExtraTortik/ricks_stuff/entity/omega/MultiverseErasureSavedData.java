/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.StringTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.saveddata.SavedData
 *  net.minecraft.world.level.saveddata.SavedData$Factory
 *  net.neoforged.neoforge.server.ServerLifecycleHooks
 */
package org.ExtraTortik.ricks_stuff.entity.omega;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;

public class MultiverseErasureSavedData
extends SavedData {
    public static final SavedData.Factory<MultiverseErasureSavedData> FACTORY = new SavedData.Factory(MultiverseErasureSavedData::new, MultiverseErasureSavedData::load, null);
    public static final String DATA_NAME = "ricks_stuff_multiverse_erasure";
    public static final MultiverseErasureSavedData CLIENT_INSTANCE = new MultiverseErasureSavedData();
    private final Set<UUID> erasedUUIDs = ConcurrentHashMap.newKeySet();
    private final Set<String> erasedEntityTypes = ConcurrentHashMap.newKeySet();
    private final Set<String> erasedItemTypes = ConcurrentHashMap.newKeySet();
    private static volatile MultiverseErasureSavedData serverInstance = null;
    private static volatile MinecraftServer cachedServer = null;
    private int revision = 0;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void resetCache() {
        Class<MultiverseErasureSavedData> clazz = MultiverseErasureSavedData.class;
        synchronized (MultiverseErasureSavedData.class) {
            serverInstance = null;
            cachedServer = null;
            // ** MonitorExit[var0] (shouldn't be in output)
            return;
        }
    }

    public static boolean hasAnyActiveErasures() {
        MultiverseErasureSavedData cached = serverInstance;
        if (cached == null) return !MultiverseErasureSavedData.CLIENT_INSTANCE.erasedItemTypes.isEmpty();
        return !cached.erasedItemTypes.isEmpty();
    }

    public int getRevision() {
        return this.revision;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static MultiverseErasureSavedData get(MinecraftServer server) {
        if (server == null) {
            return new MultiverseErasureSavedData();
        }
        MultiverseErasureSavedData cached = serverInstance;
        if (cached != null && cachedServer == server) {
            return cached;
        }
        Class<MultiverseErasureSavedData> clazz = MultiverseErasureSavedData.class;
        synchronized (MultiverseErasureSavedData.class) {
            MultiverseErasureSavedData data;
            if (serverInstance != null && cachedServer == server) {
                // ** MonitorExit[var2_2] (shouldn't be in output)
                return serverInstance;
            }
            ServerLevel overworld = server.overworld();
            if (overworld == null) {
                // ** MonitorExit[var2_2] (shouldn't be in output)
                return new MultiverseErasureSavedData();
            }
            serverInstance = data = (MultiverseErasureSavedData)overworld.getDataStorage().computeIfAbsent(FACTORY, DATA_NAME);
            cachedServer = server;
            // ** MonitorExit[var2_2] (shouldn't be in output)
            return data;
        }
    }

    public static MultiverseErasureSavedData get(Level level) {
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            return MultiverseErasureSavedData.get(serverLevel.getServer());
        }
        if (level == null) return new MultiverseErasureSavedData();
        if (!level.isClientSide()) return new MultiverseErasureSavedData();
        return CLIENT_INSTANCE;
    }

    public static MultiverseErasureSavedData getEffective(@Nullable Level level) {
        if (level instanceof ServerLevel) {
            ServerLevel sl = (ServerLevel)level;
            return MultiverseErasureSavedData.get(sl.getServer());
        }
        if (level != null && level.isClientSide()) {
            return CLIENT_INSTANCE;
        }
        MultiverseErasureSavedData cached = serverInstance;
        if (cached != null) {
            return cached;
        }
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return CLIENT_INSTANCE;
        return MultiverseErasureSavedData.get(server);
    }

    public static MultiverseErasureSavedData load(CompoundTag tag, HolderLookup.Provider provider) {
        int i;
        MultiverseErasureSavedData data = new MultiverseErasureSavedData();
        if (tag.contains("ErasedUUIDs", 9)) {
            ListTag uuidList = tag.getList("ErasedUUIDs", 10);
            for (i = 0; i < uuidList.size(); ++i) {
                CompoundTag uuidTag = uuidList.getCompound(i);
                if (!uuidTag.hasUUID("UUID")) continue;
                data.erasedUUIDs.add(uuidTag.getUUID("UUID"));
            }
        }
        if (tag.contains("ErasedEntityTypes", 9)) {
            ListTag typeList = tag.getList("ErasedEntityTypes", 8);
            for (i = 0; i < typeList.size(); ++i) {
                String typeStr = typeList.getString(i);
                if (typeStr.isBlank() || typeStr.equals("minecraft:item") || typeStr.equals("item") || typeStr.equals("minecraft:player") || typeStr.equals("player")) continue;
                data.erasedEntityTypes.add(typeStr);
            }
        }
        if (!tag.contains("ErasedItemTypes", 9)) return data;
        ListTag itemList = tag.getList("ErasedItemTypes", 8);
        i = 0;
        while (i < itemList.size()) {
            String itemStr = itemList.getString(i);
            if (!(itemStr.isBlank() || itemStr.equals("minecraft:air") || itemStr.equals("air"))) {
                data.erasedItemTypes.add(itemStr);
            }
            ++i;
        }
        return data;
    }

    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        ListTag uuidList = new ListTag();
        for (UUID uUID : this.erasedUUIDs) {
            CompoundTag uuidTag = new CompoundTag();
            uuidTag.putUUID("UUID", uUID);
            uuidList.add((Object)uuidTag);
        }
        tag.put("ErasedUUIDs", (Tag)uuidList);
        ListTag typeList = new ListTag();
        for (String typeStr : this.erasedEntityTypes) {
            typeList.add((Object)StringTag.valueOf((String)typeStr));
        }
        tag.put("ErasedEntityTypes", (Tag)typeList);
        ListTag listTag = new ListTag();
        Iterator<String> iterator = this.erasedItemTypes.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                tag.put("ErasedItemTypes", (Tag)listTag);
                return tag;
            }
            String itemStr = iterator.next();
            listTag.add((Object)StringTag.valueOf((String)itemStr));
        }
    }

    public void eraseEntity(UUID uuid, @Nullable EntityType<?> entityType, @Nullable String name) {
        ResourceLocation key;
        if (uuid != null) {
            this.erasedUUIDs.add(uuid);
        }
        if (entityType != null && entityType != EntityType.ITEM && entityType != EntityType.PLAYER && (key = BuiltInRegistries.ENTITY_TYPE.getKey(entityType)) != null && !key.getPath().equals("item") && !key.getPath().equals("player")) {
            this.erasedEntityTypes.add(key.toString());
        }
        this.setDirty();
    }

    public void eraseEntity(UUID uuid, @Nullable String entityTypeId) {
        if (uuid != null) {
            this.erasedUUIDs.add(uuid);
        }
        if (!(entityTypeId == null || entityTypeId.isBlank() || entityTypeId.equals("minecraft:item") || entityTypeId.equals("item") || entityTypeId.equals("minecraft:player") || entityTypeId.equals("player"))) {
            this.erasedEntityTypes.add(entityTypeId);
        }
        this.setDirty();
    }

    public void eraseEntityType(EntityType<?> entityType) {
        if (entityType == null) return;
        if (entityType == EntityType.ITEM) return;
        if (entityType == EntityType.PLAYER) return;
        ResourceLocation key = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
        if (key == null) return;
        if (key.getPath().equals("item")) return;
        if (key.getPath().equals("player")) return;
        this.erasedEntityTypes.add(key.toString());
        this.setDirty();
    }

    public void eraseEntityType(String entityTypeId) {
        if (entityTypeId == null) return;
        if (entityTypeId.isBlank()) return;
        if (entityTypeId.equals("minecraft:item")) return;
        if (entityTypeId.equals("item")) return;
        if (entityTypeId.equals("minecraft:player")) return;
        if (entityTypeId.equals("player")) return;
        this.erasedEntityTypes.add(entityTypeId);
        this.setDirty();
    }

    public void eraseItem(Item item) {
        if (item == null) return;
        if (item == Items.AIR) return;
        ResourceLocation key = BuiltInRegistries.ITEM.getKey((Object)item);
        if (key == null) return;
        if (key.getPath().equals("air")) return;
        this.erasedItemTypes.add(key.toString());
        ++this.revision;
        this.setDirty();
    }

    public void eraseItem(String itemId) {
        if (itemId == null) return;
        if (itemId.isBlank()) return;
        if (itemId.equals("minecraft:air")) return;
        if (itemId.equals("air")) return;
        this.erasedItemTypes.add(itemId);
        ++this.revision;
        this.setDirty();
    }

    public boolean isItemErased(Item item) {
        if (item == null) return false;
        ResourceLocation key = BuiltInRegistries.ITEM.getKey((Object)item);
        if (item != Items.AIR) return key != null && !key.getPath().equals("air") && this.erasedItemTypes.contains(key.toString());
        return false;
    }

    public boolean isItemErased(String itemId) {
        if (itemId == null) return false;
        if (itemId.isBlank()) return false;
        if (itemId.equals("minecraft:air")) return false;
        if (!itemId.equals("air")) return this.erasedItemTypes.contains(itemId);
        return false;
    }

    public boolean isBlockErased(Block block) {
        if (block == null) return false;
        if (block == Blocks.AIR) return false;
        if (this.erasedItemTypes.isEmpty()) {
            return false;
        }
        ResourceLocation key = BuiltInRegistries.BLOCK.getKey((Object)block);
        if (key != null && this.erasedItemTypes.contains(key.toString())) {
            return true;
        }
        Item item = block.asItem();
        if (item == null) return false;
        if (item == Items.AIR) return false;
        return this.isItemErased(item);
    }

    public boolean isBlockErased(BlockState state) {
        if (state == null) return false;
        if (!state.isAir()) return this.isBlockErased(state.getBlock());
        return false;
    }

    public boolean isErased(UUID uuid) {
        if (uuid != null) return this.erasedUUIDs.contains(uuid);
        return false;
    }

    public boolean isTypeErased(EntityType<?> entityType) {
        if (entityType == null) return false;
        if (entityType == EntityType.ITEM) return false;
        ResourceLocation key = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
        if (entityType != EntityType.PLAYER) return key != null && !key.getPath().equals("item") && !key.getPath().equals("player") && this.erasedEntityTypes.contains(key.toString());
        return false;
    }

    public boolean isTypeErased(String entityTypeId) {
        if (entityTypeId == null) return false;
        if (entityTypeId.isBlank()) return false;
        if (entityTypeId.equals("minecraft:item")) return false;
        if (entityTypeId.equals("item")) return false;
        if (entityTypeId.equals("minecraft:player")) return false;
        if (!entityTypeId.equals("player")) return this.erasedEntityTypes.contains(entityTypeId);
        return false;
    }

    public boolean isTypeErased(ResourceLocation entityTypeLoc) {
        if (entityTypeLoc == null) return false;
        if (entityTypeLoc.getPath().equals("item")) return false;
        if (!entityTypeLoc.getPath().equals("player")) return this.erasedEntityTypes.contains(entityTypeLoc.toString());
        return false;
    }

    public boolean isEntityOrTypeErased(Entity entity) {
        if (entity == null) {
            return false;
        }
        if (this.isErased(entity.getUUID())) {
            return true;
        }
        if (!(entity instanceof ItemEntity)) return this.isTypeErased(entity.getType());
        ItemEntity itemEntity = (ItemEntity)entity;
        return this.isItemErased(itemEntity.getItem().getItem());
    }

    public boolean unerase(UUID uuid) {
        if (uuid == null) return false;
        if (!this.erasedUUIDs.remove(uuid)) return false;
        this.setDirty();
        return true;
    }

    public boolean uneraseType(String entityTypeId) {
        if (entityTypeId == null) return false;
        if (!this.erasedEntityTypes.remove(entityTypeId)) return false;
        this.setDirty();
        return true;
    }

    public boolean uneraseItem(String itemId) {
        if (itemId == null) return false;
        if (!this.erasedItemTypes.remove(itemId)) return false;
        ++this.revision;
        this.setDirty();
        return true;
    }

    public Set<UUID> getErasedUUIDs() {
        return Collections.unmodifiableSet(this.erasedUUIDs);
    }

    public Set<String> getErasedEntityTypes() {
        return Collections.unmodifiableSet(this.erasedEntityTypes);
    }

    public Set<String> getErasedItemTypes() {
        return Collections.unmodifiableSet(this.erasedItemTypes);
    }

    public void syncFrom(Set<UUID> uuids, Set<String> entityTypes, Set<String> itemTypes) {
        this.erasedUUIDs.clear();
        if (uuids != null) {
            this.erasedUUIDs.addAll(uuids);
        }
        this.erasedEntityTypes.clear();
        if (entityTypes != null) {
            for (String t : entityTypes) {
                if (t == null || t.isBlank() || t.equals("minecraft:item") || t.equals("item") || t.equals("minecraft:player") || t.equals("player")) continue;
                this.erasedEntityTypes.add(t);
            }
        }
        this.erasedItemTypes.clear();
        if (itemTypes != null) {
            for (String item : itemTypes) {
                if (item == null || item.isBlank() || item.equals("minecraft:air") || item.equals("air")) continue;
                this.erasedItemTypes.add(item);
            }
        }
        ++this.revision;
    }

    public void clearAll() {
        this.erasedUUIDs.clear();
        this.erasedEntityTypes.clear();
        this.erasedItemTypes.clear();
        ++this.revision;
        this.setDirty();
    }
}

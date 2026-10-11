/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.level.saveddata.SavedData
 *  net.minecraft.world.level.saveddata.SavedData$Factory
 */
package org.ExtraTortik.ricks_stuff.portal;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class PortalSavedData
extends SavedData {
    private static final String DATA_NAME = "ricks_stuff_portals";
    public static final SavedData.Factory<PortalSavedData> FACTORY = new SavedData.Factory(PortalSavedData::new, PortalSavedData::load, null);
    private final Set<UUID> tombstonedPortals = ConcurrentHashMap.newKeySet();
    private final Map<Integer, Set<UUID>> gunActivePortals = new ConcurrentHashMap<Integer, Set<UUID>>();

    public static PortalSavedData get(MinecraftServer server) {
        if (server == null) {
            return new PortalSavedData();
        }
        ServerLevel overworld = server.overworld();
        return (PortalSavedData)overworld.getDataStorage().computeIfAbsent(FACTORY, DATA_NAME);
    }

    public void registerPortal(int gunId, UUID portalUUID) {
        if (portalUUID == null) {
            return;
        }
        this.tombstonedPortals.remove(portalUUID);
        if (gunId != 0) {
            this.gunActivePortals.computeIfAbsent(gunId, k -> ConcurrentHashMap.newKeySet()).add(portalUUID);
        }
        this.setDirty();
    }

    public void tombstonePortal(UUID portalUUID, int gunId) {
        Set<UUID> set;
        if (portalUUID == null) {
            return;
        }
        this.tombstonedPortals.add(portalUUID);
        if (gunId != 0 && (set = this.gunActivePortals.get(gunId)) != null) {
            set.remove(portalUUID);
            if (set.isEmpty()) {
                this.gunActivePortals.remove(gunId);
            }
        }
        this.setDirty();
    }

    public void tombstoneAllPortalsForGun(int gunId, Collection<UUID> additionalIds) {
        Set<UUID> active;
        if (gunId != 0 && (active = this.gunActivePortals.remove(gunId)) != null) {
            this.tombstonedPortals.addAll(active);
        }
        if (additionalIds != null) {
            for (UUID id : additionalIds) {
                if (id == null) continue;
                this.tombstonedPortals.add(id);
            }
        }
        this.setDirty();
    }

    public boolean isTombstoned(UUID portalUUID) {
        if (portalUUID != null) return this.tombstonedPortals.contains(portalUUID);
        return false;
    }

    public boolean removeTombstone(UUID portalUUID) {
        if (portalUUID == null) {
            return false;
        }
        boolean removed = this.tombstonedPortals.remove(portalUUID);
        if (!removed) return removed;
        this.setDirty();
        return removed;
    }

    public Set<UUID> getPortalsForGun(int gunId) {
        Set<UUID> set = this.gunActivePortals.get(gunId);
        if (gunId != 0) return set != null ? new HashSet<UUID>(set) : Collections.emptySet();
        return Collections.emptySet();
    }

    public static PortalSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        PortalSavedData data = new PortalSavedData();
        if (tag.contains("Tombstones", 9)) {
            ListTag list = tag.getList("Tombstones", 10);
            for (int i = 0; i < list.size(); ++i) {
                CompoundTag entry = list.getCompound(i);
                if (!entry.hasUUID("UUID")) continue;
                data.tombstonedPortals.add(entry.getUUID("UUID"));
            }
        }
        if (!tag.contains("GunPortals", 10)) return data;
        CompoundTag gunsTag = tag.getCompound("GunPortals");
        Iterator iterator = gunsTag.getAllKeys().iterator();
        while (iterator.hasNext()) {
            String key = (String)iterator.next();
            try {
                int gunId = Integer.parseInt(key);
                ListTag list = gunsTag.getList(key, 10);
                ConcurrentHashMap.KeySetView set = ConcurrentHashMap.newKeySet();
                for (int i = 0; i < list.size(); ++i) {
                    CompoundTag entry = list.getCompound(i);
                    if (!entry.hasUUID("UUID")) continue;
                    set.add(entry.getUUID("UUID"));
                }
                if (set.isEmpty()) continue;
                data.gunActivePortals.put(gunId, set);
            }
            catch (NumberFormatException numberFormatException) {}
        }
        return data;
    }

    @NotNull
    public CompoundTag save(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        Object entry;
        ListTag tombstoneList = new ListTag();
        for (UUID uuid : this.tombstonedPortals) {
            entry = new CompoundTag();
            entry.putUUID("UUID", uuid);
            tombstoneList.add(entry);
        }
        tag.put("Tombstones", (Tag)tombstoneList);
        CompoundTag gunsTag = new CompoundTag();
        Iterator<Map.Entry<Integer, Set<UUID>>> iterator = this.gunActivePortals.entrySet().iterator();
        while (true) {
            if (!iterator.hasNext()) {
                tag.put("GunPortals", (Tag)gunsTag);
                return tag;
            }
            entry = iterator.next();
            ListTag list = new ListTag();
            for (UUID uuid : (Set)entry.getValue()) {
                CompoundTag uTag = new CompoundTag();
                uTag.putUUID("UUID", uuid);
                list.add((Object)uTag);
            }
            gunsTag.put(String.valueOf(entry.getKey()), (Tag)list);
        }
    }
}

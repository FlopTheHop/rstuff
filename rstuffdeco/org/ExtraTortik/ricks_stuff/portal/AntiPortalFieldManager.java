/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Vec3i
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  org.ExtraTortik.ricks_stuff.block.entity.AntiPortalFieldGenBlockEntity
 */
package org.ExtraTortik.ricks_stuff.portal;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.ExtraTortik.ricks_stuff.block.entity.AntiPortalFieldGenBlockEntity;

public class AntiPortalFieldManager {
    private static final Map<ResourceKey<Level>, Set<AntiPortalFieldGenBlockEntity>> ACTIVE_GENERATORS = new ConcurrentHashMap<ResourceKey<Level>, Set<AntiPortalFieldGenBlockEntity>>();

    public static void register(AntiPortalFieldGenBlockEntity be) {
        if (be == null) return;
        if (be.getLevel() == null) {
            return;
        }
        ResourceKey dim = be.getLevel().dimension();
        ACTIVE_GENERATORS.computeIfAbsent((ResourceKey<Level>)dim, k -> ConcurrentHashMap.newKeySet()).add(be);
    }

    public static void unregister(AntiPortalFieldGenBlockEntity be) {
        if (be == null) return;
        if (be.getLevel() == null) {
            return;
        }
        ResourceKey dim = be.getLevel().dimension();
        Set<AntiPortalFieldGenBlockEntity> set = ACTIVE_GENERATORS.get(dim);
        if (set == null) return;
        set.remove(be);
        if (!set.isEmpty()) return;
        ACTIVE_GENERATORS.remove(dim);
    }

    public static void clear() {
        ACTIVE_GENERATORS.clear();
    }

    public static void clearDimension(ResourceKey<Level> dim) {
        ACTIVE_GENERATORS.remove(dim);
    }

    public static Set<AntiPortalFieldGenBlockEntity> getActiveGenerators(Level level) {
        Set<AntiPortalFieldGenBlockEntity> set = ACTIVE_GENERATORS.get(level.dimension());
        if (level != null) return set != null ? Collections.unmodifiableSet(set) : Collections.emptySet();
        return Collections.emptySet();
    }

    public static boolean isProtected(Level level, Vec3 pos) {
        if (level == null) return false;
        if (pos != null) return AntiPortalFieldManager.isProtected((ResourceKey<Level>)level.dimension(), pos);
        return false;
    }

    public static boolean isProtected(Level level, BlockPos pos) {
        if (level == null) return false;
        if (pos != null) return AntiPortalFieldManager.isProtected((ResourceKey<Level>)level.dimension(), Vec3.atCenterOf((Vec3i)pos));
        return false;
    }

    public static boolean isProtected(ResourceKey<Level> dim, Vec3 pos) {
        if (dim == null) return false;
        if (pos == null) {
            return false;
        }
        Set<AntiPortalFieldGenBlockEntity> set = ACTIVE_GENERATORS.get(dim);
        if (set == null) return false;
        if (set.isEmpty()) {
            return false;
        }
        Iterator<AntiPortalFieldGenBlockEntity> iterator = set.iterator();
        while (iterator.hasNext()) {
            AntiPortalFieldGenBlockEntity be = iterator.next();
            if (be.isRemoved() || !be.isActive()) continue;
            Vec3 center = Vec3.atCenterOf((Vec3i)be.getBlockPos());
            double radius = be.getRadius();
            if (center.distanceToSqr(pos) <= radius * radius) return true;
        }
        return false;
    }

    public static boolean isPlayerHidden(Player player) {
        if (player == null) return false;
        if (player.level() != null) return AntiPortalFieldManager.isProtected(player.level(), player.position().add(0.0, (double)player.getEyeHeight() * 0.5, 0.0));
        return false;
    }

    @Nullable
    public static AntiPortalFieldGenBlockEntity getNearestProtectingGenerator(Level level, Vec3 pos) {
        if (level == null) return null;
        if (pos == null) {
            return null;
        }
        Set<AntiPortalFieldGenBlockEntity> set = ACTIVE_GENERATORS.get(level.dimension());
        if (set == null) return null;
        if (set.isEmpty()) {
            return null;
        }
        AntiPortalFieldGenBlockEntity nearest = null;
        double minDistanceSq = Double.MAX_VALUE;
        Iterator<AntiPortalFieldGenBlockEntity> iterator = set.iterator();
        while (iterator.hasNext()) {
            double radius;
            Vec3 center;
            double distSq;
            AntiPortalFieldGenBlockEntity be = iterator.next();
            if (be.isRemoved() || !be.isActive() || !((distSq = (center = Vec3.atCenterOf((Vec3i)be.getBlockPos())).distanceToSqr(pos)) <= (radius = (double)be.getRadius()) * radius) || !(distSq < minDistanceSq)) continue;
            minDistanceSq = distSq;
            nearest = be;
        }
        return nearest;
    }
}

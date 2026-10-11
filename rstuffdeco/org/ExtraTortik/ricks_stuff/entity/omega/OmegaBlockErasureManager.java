/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.longs.LongOpenHashSet
 *  net.minecraft.core.BlockPos
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.level.ChunkPos
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.chunk.LevelChunk
 *  net.minecraft.world.level.levelgen.Heightmap$Types
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.entity.omega.ErasurePurgeHelper
 *  org.ExtraTortik.ricks_stuff.entity.omega.MultiverseErasureSavedData
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaBlockErasureManager$ChunkOffset
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaBlockErasureManager$ChunkScanTask
 */
package org.ExtraTortik.ricks_stuff.entity.omega;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.lang.invoke.LambdaMetafactory;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.entity.omega.ErasurePurgeHelper;
import org.ExtraTortik.ricks_stuff.entity.omega.MultiverseErasureSavedData;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaBlockErasureManager;

public class OmegaBlockErasureManager {
    private static final long MAX_NANOS_PER_TICK = 1500000L;
    private static final int DEFAULT_PLAYER_RADIUS_CHUNKS = 8;
    private static final List<ChunkOffset> SORTED_OFFSETS = new ArrayList<ChunkOffset>();
    private static final Queue<ChunkScanTask> SCAN_QUEUE;
    private static final Set<Long> QUEUED_KEYS;
    private static final Map<UUID, ChunkPos> LAST_PLAYER_CHUNK;
    private static final Map<ResourceKey<Level>, LongOpenHashSet> CLEANED_CHUNKS;
    private static int lastRevision;
    private static int tickCounter;

    public static synchronized void checkRevision(MultiverseErasureSavedData storage) {
        if (storage == null) {
            return;
        }
        int rev = storage.getRevision();
        if (rev == lastRevision) return;
        OmegaBlockErasureManager.clearCleanedCache();
        lastRevision = rev;
    }

    public static synchronized void clearCleanedCache() {
        CLEANED_CHUNKS.clear();
    }

    public static synchronized void queueAllOnlinePlayers(MinecraftServer server) {
        if (server == null) return;
        if (!Config.allowBlockErasure) {
            return;
        }
        MultiverseErasureSavedData storage = MultiverseErasureSavedData.get((MinecraftServer)server);
        if (storage == null) return;
        if (storage.getErasedItemTypes().isEmpty()) {
            return;
        }
        Iterator iterator = server.getPlayerList().getPlayers().iterator();
        while (iterator.hasNext()) {
            ServerPlayer player = (ServerPlayer)iterator.next();
            if (player == null || player.level() == null) continue;
            OmegaBlockErasureManager.queueAroundPlayer(player, 8);
        }
    }

    public static synchronized void queueAroundPlayer(ServerPlayer player, int radius) {
        if (player == null) return;
        Level level = player.level();
        if (!(level instanceof ServerLevel)) return;
        ServerLevel serverLevel = (ServerLevel)level;
        if (!Config.allowBlockErasure) {
            return;
        }
        ResourceKey dim = serverLevel.dimension();
        ChunkPos center = player.chunkPosition();
        int maxDistSq = radius * radius;
        Iterator<ChunkOffset> iterator = SORTED_OFFSETS.iterator();
        while (iterator.hasNext()) {
            ChunkPos targetPos;
            long key;
            ChunkOffset offset = iterator.next();
            if (offset.distSq > maxDistSq || !QUEUED_KEYS.add(key = (targetPos = new ChunkPos(center.x + offset.dx, center.z + offset.dz)).toLong() ^ (long)dim.location().hashCode() << 32)) continue;
            SCAN_QUEUE.offer(new ChunkScanTask(dim, targetPos));
        }
    }

    /*
     * Unable to fully structure code
     */
    public static boolean cleanLevelChunk(ServerLevel level, LevelChunk chunk, MultiverseErasureSavedData storage) {
        if (level == null) return false;
        if (chunk == null) return false;
        if (storage == null) return false;
        if (storage.getErasedItemTypes().isEmpty() != false) return false;
        if (!Config.allowBlockErasure) {
            return false;
        }
        OmegaBlockErasureManager.checkRevision(storage);
        cleanedSet = OmegaBlockErasureManager.CLEANED_CHUNKS.computeIfAbsent((ResourceKey<Level>)level.dimension(), (Function<ResourceKey, LongOpenHashSet>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, lambda$cleanLevelChunk$1(net.minecraft.resources.ResourceKey ), (Lnet/minecraft/resources/ResourceKey;)Lit/unimi/dsi/fastutil/longs/LongOpenHashSet;)());
        chunkKey = chunk.getPos().toLong();
        if (cleanedSet.contains(chunkKey)) {
            return false;
        }
        cleanedSet.add(chunkKey);
        anyChanged = false;
        if (!chunk.getBlockEntities().isEmpty()) {
            for (BlockEntity be : new ArrayList<V>(chunk.getBlockEntities().values())) {
                if (be == null || be.isRemoved()) continue;
                ErasurePurgeHelper.purgeBlockEntity((BlockEntity)be, (MultiverseErasureSavedData)storage);
            }
        }
        sections = chunk.getSections();
        minSection = chunk.getMinSection();
        chunkMinX = chunk.getPos().getMinBlockX();
        chunkMinZ = chunk.getPos().getMinBlockZ();
        sIdx = 0;
        block1: while (true) {
            if (sIdx >= sections.length) {
                if (anyChanged == false) return anyChanged;
                chunk.setUnsaved(true);
                return anyChanged;
            }
            section = sections[sIdx];
            if (section == null || section.hasOnlyAir()) ** GOTO lbl-1000
            states = section.getStates();
            if (!states.maybeHas((Predicate<BlockState>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Z, isBlockErased(net.minecraft.world.level.block.state.BlockState ), (Lnet/minecraft/world/level/block/state/BlockState;)Z)((MultiverseErasureSavedData)storage))) ** GOTO lbl-1000
            sectionY = minSection + sIdx << 4;
            motionBlocking = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.MOTION_BLOCKING);
            worldSurface = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE);
            motionBlockingNoLeaves = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES);
            oceanFloor = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR);
            x = 0;
            while (true) {
                if (x < 16) {
                } else lbl-1000:
                // 3 sources

                {
                    ++sIdx;
                    continue block1;
                }
                for (y = 0; y < 16; ++y) {
                    for (z = 0; z < 16; ++z) {
                        state = (BlockState)states.get(x, y, z);
                        if (state.isAir() || !storage.isBlockErased(state)) continue;
                        worldY = sectionY + y;
                        section.setBlockState(x, y, z, Blocks.AIR.defaultBlockState(), false);
                        if (motionBlocking != null) {
                            motionBlocking.update(x, worldY, z, Blocks.AIR.defaultBlockState());
                        }
                        if (worldSurface != null) {
                            worldSurface.update(x, worldY, z, Blocks.AIR.defaultBlockState());
                        }
                        if (motionBlockingNoLeaves != null) {
                            motionBlockingNoLeaves.update(x, worldY, z, Blocks.AIR.defaultBlockState());
                        }
                        if (oceanFloor != null) {
                            oceanFloor.update(x, worldY, z, Blocks.AIR.defaultBlockState());
                        }
                        bPos = new BlockPos(chunkMinX + x, worldY, chunkMinZ + z);
                        chunk.removeBlockEntity(bPos);
                        if (level.getChunkSource().hasChunk(chunk.getPos().x, chunk.getPos().z)) {
                            level.getChunkSource().blockChanged(bPos);
                        }
                        anyChanged = true;
                    }
                }
                ++x;
            }
            break;
        }
    }

    public static boolean cleanChunk(ServerLevel level, ChunkPos pos, MultiverseErasureSavedData storage) {
        if (level == null) return false;
        if (pos == null) return false;
        if (storage == null) return false;
        if (storage.getErasedItemTypes().isEmpty()) return false;
        if (!Config.allowBlockErasure) {
            return false;
        }
        LevelChunk chunk = level.getChunkSource().getChunkNow(pos.x, pos.z);
        if (chunk != null) return OmegaBlockErasureManager.cleanLevelChunk(level, chunk, storage);
        return false;
    }

    public static synchronized void clearQueue() {
        SCAN_QUEUE.clear();
        QUEUED_KEYS.clear();
        LAST_PLAYER_CHUNK.clear();
        OmegaBlockErasureManager.clearCleanedCache();
    }

    public static synchronized void tick(MinecraftServer server) {
        if (server == null) return;
        if (!Config.allowBlockErasure) {
            return;
        }
        MultiverseErasureSavedData storage = MultiverseErasureSavedData.get((MinecraftServer)server);
        if (storage == null || storage.getErasedItemTypes().isEmpty()) {
            if (SCAN_QUEUE.isEmpty()) {
                if (QUEUED_KEYS.isEmpty()) return;
            }
            OmegaBlockErasureManager.clearQueue();
            return;
        }
        OmegaBlockErasureManager.checkRevision(storage);
        boolean periodicScan = ++tickCounter % 20 == 0;
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (player == null || !(player.level() instanceof ServerLevel)) continue;
            ChunkPos currentPos = player.chunkPosition();
            ChunkPos lastPos = LAST_PLAYER_CHUNK.get(player.getUUID());
            if (!periodicScan && lastPos != null && lastPos.equals((Object)currentPos)) continue;
            LAST_PLAYER_CHUNK.put(player.getUUID(), currentPos);
            OmegaBlockErasureManager.queueAroundPlayer(player, 8);
        }
        if (tickCounter % 100 == 0 && SCAN_QUEUE.isEmpty()) {
            QUEUED_KEYS.clear();
        }
        if (SCAN_QUEUE.isEmpty()) {
            return;
        }
        long startNanos = System.nanoTime();
        while (!SCAN_QUEUE.isEmpty()) {
            ServerLevel level;
            if (System.nanoTime() - startNanos >= 1500000L) {
                return;
            }
            ChunkScanTask task = SCAN_QUEUE.poll();
            if (task == null || (level = server.getLevel(task.dimension)) == null) continue;
            OmegaBlockErasureManager.cleanChunk(level, task.pos, storage);
        }
    }

    private static /* synthetic */ LongOpenHashSet lambda$cleanLevelChunk$1(ResourceKey k) {
        return new LongOpenHashSet();
    }

    static {
        int dx = -8;
        while (true) {
            if (dx > 8) {
                SORTED_OFFSETS.sort(Comparator.comparingInt(o -> o.distSq));
                SCAN_QUEUE = new ArrayDeque<ChunkScanTask>();
                QUEUED_KEYS = new HashSet<Long>();
                LAST_PLAYER_CHUNK = new ConcurrentHashMap<UUID, ChunkPos>();
                CLEANED_CHUNKS = new ConcurrentHashMap<ResourceKey<Level>, LongOpenHashSet>();
                lastRevision = -1;
                tickCounter = 0;
                return;
            }
            for (int dz = -8; dz <= 8; ++dz) {
                SORTED_OFFSETS.add(new ChunkOffset(dx, dz, dx * dx + dz * dz));
            }
            ++dx;
        }
    }
}

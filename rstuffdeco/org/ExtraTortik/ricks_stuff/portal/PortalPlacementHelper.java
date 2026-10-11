/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.BlockPos$MutableBlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.tags.BlockTags
 *  net.minecraft.tags.FluidTags
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.BushBlock
 *  net.minecraft.world.level.block.DoublePlantBlock
 *  net.minecraft.world.level.block.GrowingPlantBlock
 *  net.minecraft.world.level.block.SnowLayerBlock
 *  net.minecraft.world.level.block.VineBlock
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.chunk.status.ChunkStatus
 *  net.minecraft.world.level.levelgen.Heightmap$Types
 *  net.minecraft.world.level.levelgen.feature.EndPlatformFeature
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.Vec3
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.ExtraTortik.ricks_stuff.compat.InfinityCompat
 *  org.ExtraTortik.ricks_stuff.init.ModParticles
 *  org.ExtraTortik.ricks_stuff.portal.BlenderDimensionHelper
 *  org.ExtraTortik.ricks_stuff.portal.PortalPlacementHelper$1
 *  org.ExtraTortik.ricks_stuff.portal.PortalPlacementHelper$Placement
 */
package org.ExtraTortik.ricks_stuff.portal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.GrowingPlantBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.EndPlatformFeature;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.ExtraTortik.ricks_stuff.compat.InfinityCompat;
import org.ExtraTortik.ricks_stuff.init.ModParticles;
import org.ExtraTortik.ricks_stuff.portal.BlenderDimensionHelper;
import org.ExtraTortik.ricks_stuff.portal.PortalPlacementHelper;

public class PortalPlacementHelper {
    public static Placement calculatePlacement(BlockHitResult hit, Player player, Level level) {
        Vec3 pos;
        float yaw;
        int rotation;
        int orientation;
        BlockState hitState;
        Direction face = hit.getDirection();
        BlockPos hitPos = hit.getBlockPos();
        if (level != null && (PortalPlacementHelper.isBreakableFoliage(hitState = level.getBlockState(hitPos)) || hitState.canBeReplaced())) {
            BlockPos.MutableBlockPos scan = hitPos.mutable();
            int minY = level.getMinBuildHeight();
            while (scan.getY() > minY) {
                scan.move(Direction.DOWN);
                BlockState belowState = level.getBlockState((BlockPos)scan);
                if (belowState.isAir() || !belowState.isSolid() && !belowState.blocksMotion()) continue;
                hitPos = scan.immutable();
                face = Direction.UP;
                break;
            }
        }
        float playerYaw = player != null ? player.getYRot() : 0.0f;
        float normalizedYaw = (playerYaw % 360.0f + 360.0f) % 360.0f;
        if (face == Direction.UP) {
            orientation = 2;
            rotation = PortalPlacementHelper.getYawQuadrant(normalizedYaw);
            yaw = (float)rotation * 90.0f;
            pos = new Vec3((double)hitPos.getX() + 0.5, (double)hitPos.getY() + 1.035, (double)hitPos.getZ() + 0.5);
        } else if (face == Direction.DOWN) {
            orientation = 1;
            rotation = PortalPlacementHelper.getYawQuadrant(normalizedYaw);
            yaw = (float)rotation * 90.0f;
            pos = new Vec3((double)hitPos.getX() + 0.5, (double)hitPos.getY() - 0.035, (double)hitPos.getZ() + 0.5);
        } else {
            double ceilBottom;
            double floorTop;
            orientation = 0;
            yaw = face.toYRot();
            rotation = switch (1.$SwitchMap$net$minecraft$core$Direction[face.ordinal()]) {
                case 1 -> 0;
                case 2 -> 1;
                case 3 -> 2;
                case 4 -> 3;
                default -> 0;
            };
            double targetCenterY = (double)hitPos.getY() + 0.5;
            double halfH = 1.05;
            BlockPos wallStep = hitPos.relative(face);
            BlockPos blockBelow = BlockPos.containing((double)wallStep.getX(), (double)(targetCenterY - halfH), (double)wallStep.getZ());
            if (level != null && level.getBlockState(blockBelow).isSolid() && targetCenterY - halfH < (floorTop = (double)blockBelow.getY() + 1.0)) {
                targetCenterY = floorTop + halfH;
            }
            BlockPos blockAbove = BlockPos.containing((double)wallStep.getX(), (double)(targetCenterY + halfH), (double)wallStep.getZ());
            if (level != null && level.getBlockState(blockAbove).isSolid() && targetCenterY + halfH > (ceilBottom = (double)blockAbove.getY())) {
                targetCenterY = ceilBottom - halfH;
            }
            pos = new Vec3((double)hitPos.getX() + 0.5 + (double)face.getStepX() * 0.535, targetCenterY, (double)hitPos.getZ() + 0.5 + (double)face.getStepZ() * 0.535);
        }
        return new Placement(pos, orientation, rotation, yaw);
    }

    private static int getYawQuadrant(float yaw) {
        float ry = (yaw + 180.0f) % 360.0f;
        if (ry < 45.0f) return 0;
        if (ry >= 315.0f) {
            return 0;
        }
        if (ry < 135.0f) {
            return 1;
        }
        if (!(ry < 225.0f)) return 3;
        return 2;
    }

    public static Vec3 adjustAirPortalPosition(Level level, Vec3 pos, int orientation, float yaw, float scale) {
        if (level == null) return pos;
        if (pos == null) {
            return pos;
        }
        double effectiveScale = Math.max(0.1, (double)scale);
        double halfH = 1.05 * effectiveScale;
        double halfW = 0.95 * effectiveScale;
        if (orientation == 0) {
            int n;
            double[] dArray;
            double lowestCeilY;
            double targetCenterY;
            float rad = (float)Math.toRadians(yaw);
            double sideX = Math.cos(rad);
            double sideZ = Math.sin(rad);
            double[] sampleOffsets = new double[]{-0.8, -0.4, 0.0, 0.4, 0.8};
            double highestFloorY = Double.NEGATIVE_INFINITY;
            double[] dArray2 = sampleOffsets;
            int n2 = dArray2.length;
            int n3 = 0;
            while (true) {
                int maxBlockY;
                int minBlockY;
                double sz;
                double sx;
                if (n3 < n2) {
                    double offsetRatio = dArray2[n3];
                    sx = pos.x + sideX * (offsetRatio * halfW);
                    sz = pos.z + sideZ * (offsetRatio * halfW);
                    minBlockY = (int)Math.floor(pos.y - halfH - 0.5);
                } else {
                    double currentBottomY = pos.y - halfH;
                    if (!(highestFloorY > currentBottomY)) return pos;
                    targetCenterY = highestFloorY + halfH + 0.02;
                    lowestCeilY = Double.POSITIVE_INFINITY;
                    dArray = sampleOffsets;
                    n = dArray.length;
                    break;
                }
                for (int by = maxBlockY = (int)Math.floor(pos.y + 0.2); by >= minBlockY; --by) {
                    double blockTop;
                    BlockPos bPos = BlockPos.containing((double)sx, (double)by, (double)sz);
                    BlockState state = level.getBlockState(bPos);
                    if (state.isAir() || PortalPlacementHelper.isBreakableFoliage(state) || !state.blocksMotion()) continue;
                    VoxelShape shape = state.getCollisionShape((BlockGetter)level, bPos);
                    if (!shape.isEmpty()) {
                        blockTop = (double)bPos.getY() + shape.max(Direction.Axis.Y);
                        if (!(blockTop > highestFloorY)) continue;
                        highestFloorY = blockTop;
                        continue;
                    }
                    if (!state.isSolid() || !((blockTop = (double)bPos.getY() + 1.0) > highestFloorY)) continue;
                    highestFloorY = blockTop;
                }
                ++n3;
            }
            for (int i = 0; i < n; ++i) {
                double offsetRatio = dArray[i];
                double sx = pos.x + sideX * (offsetRatio * halfW);
                double sz = pos.z + sideZ * (offsetRatio * halfW);
                int startY = (int)Math.floor(pos.y + halfH);
                int endY = (int)Math.ceil(targetCenterY + halfH + 0.5);
                for (int by = startY; by <= endY; ++by) {
                    double blockBottom;
                    BlockPos bPos = BlockPos.containing((double)sx, (double)by, (double)sz);
                    BlockState state = level.getBlockState(bPos);
                    if (state.isAir() || PortalPlacementHelper.isBreakableFoliage(state) || !state.blocksMotion()) continue;
                    VoxelShape shape = state.getCollisionShape((BlockGetter)level, bPos);
                    if (!shape.isEmpty()) {
                        blockBottom = (double)bPos.getY() + shape.min(Direction.Axis.Y);
                        if (!(blockBottom < lowestCeilY)) continue;
                        lowestCeilY = blockBottom;
                        continue;
                    }
                    if (!state.isSolid() || !((blockBottom = (double)bPos.getY()) < lowestCeilY)) continue;
                    lowestCeilY = blockBottom;
                }
            }
            if (lowestCeilY < Double.POSITIVE_INFINITY) {
                double maxAllowedCenterY = lowestCeilY - halfH - 0.02;
                targetCenterY = Math.min(targetCenterY, maxAllowedCenterY);
            }
            if (!(targetCenterY > pos.y)) return pos;
            return new Vec3(pos.x, targetCenterY, pos.z);
        }
        double radius = 0.8 * halfW;
        double[][] samplePoints = new double[][]{{pos.x, pos.z}, {pos.x + radius, pos.z}, {pos.x - radius, pos.z}, {pos.x, pos.z + radius}, {pos.x, pos.z - radius}};
        double highestFloorY = Double.NEGATIVE_INFINITY;
        double[][] sampleOffsets = samplePoints;
        int n = sampleOffsets.length;
        int n4 = 0;
        while (true) {
            if (n4 >= n) {
                if (!(highestFloorY > pos.y - 0.05)) return pos;
                double targetY = highestFloorY + 0.05;
                BlockPos above = BlockPos.containing((double)pos.x, (double)(targetY + 1.0), (double)pos.z);
                BlockState aboveState = level.getBlockState(above);
                if (aboveState.isAir()) return new Vec3(pos.x, targetY, pos.z);
                if (PortalPlacementHelper.isBreakableFoliage(aboveState)) return new Vec3(pos.x, targetY, pos.z);
                if (aboveState.blocksMotion()) return pos;
                return new Vec3(pos.x, targetY, pos.z);
            }
            double[] pt = sampleOffsets[n4];
            int by = (int)Math.floor(pos.y);
            BlockPos bPos = BlockPos.containing((double)pt[0], (double)by, (double)pt[1]);
            BlockState state = level.getBlockState(bPos);
            if (!state.isAir() && !PortalPlacementHelper.isBreakableFoliage(state) && state.blocksMotion()) {
                double blockTop;
                VoxelShape shape = state.getCollisionShape((BlockGetter)level, bPos);
                if (!shape.isEmpty()) {
                    blockTop = (double)bPos.getY() + shape.max(Direction.Axis.Y);
                    if (blockTop > highestFloorY) {
                        highestFloorY = blockTop;
                    }
                } else if (state.isSolid() && (blockTop = (double)bPos.getY() + 1.0) > highestFloorY) {
                    highestFloorY = blockTop;
                }
            }
            ++n4;
        }
    }

    public static Vec3 findSafePlacement(ServerLevel level, Vec3 target) {
        if (level == null) return target;
        if (target == null) {
            return target;
        }
        BlockPos targetBlock = BlockPos.containing((double)target.x, (double)target.y, (double)target.z);
        BlockPos safeBlock = PortalPlacementHelper.findSafePlacement(level, targetBlock, true);
        if (safeBlock == null) return target;
        return new Vec3((double)safeBlock.getX() + 0.5, (double)safeBlock.getY(), (double)safeBlock.getZ() + 0.5);
    }

    public static BlockPos findSafePlacement(ServerLevel level, BlockPos target, boolean scanGround) {
        BlockPos verticalCandidate;
        BlockPos hmPos;
        if (level == null) return target;
        if (target == null) {
            return target;
        }
        int targetChunkX = target.getX() >> 4;
        int targetChunkZ = target.getZ() >> 4;
        try {
            level.getChunk(targetChunkX, targetChunkZ, ChunkStatus.FULL, true);
        }
        catch (Exception exception) {
            // empty catch block
        }
        boolean hasCeiling = level.dimensionType().hasCeiling();
        if (PortalPlacementHelper.isSafeLocation(level, target)) {
            return target;
        }
        if (!hasCeiling && (hmPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, target)).getY() > level.getMinBuildHeight() + 2 && PortalPlacementHelper.isSafeLocation(level, hmPos)) {
            if (target.getY() <= level.getMinBuildHeight() + 10) return hmPos;
            if (target.getY() == 0) return hmPos;
            if (Math.abs(target.getY() - hmPos.getY()) <= 24) {
                return hmPos;
            }
        }
        if ((verticalCandidate = PortalPlacementHelper.scanVerticalColumn(level, target.getX(), target.getZ(), target.getY(), hasCeiling)) != null) {
            return verticalCandidate;
        }
        int maxRadius = 32;
        int step = 2;
        int r = 2;
        block2: while (r <= maxRadius) {
            int dx = -r;
            while (true) {
                if (dx <= r) {
                } else {
                    r += step;
                    continue block2;
                }
                for (int dz = -r; dz <= r; dz += step) {
                    BlockPos found;
                    BlockPos candPos;
                    BlockPos hm;
                    if (Math.abs(dx) != r && Math.abs(dz) != r) continue;
                    int cx = target.getX() + dx;
                    int cz = target.getZ() + dz;
                    if (!hasCeiling && (hm = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, candPos = new BlockPos(cx, target.getY(), cz))).getY() > level.getMinBuildHeight() + 2 && PortalPlacementHelper.isSafeLocation(level, hm)) {
                        if (target.getY() <= level.getMinBuildHeight() + 10) return hm;
                        if (target.getY() == 0) return hm;
                        if (Math.abs(target.getY() - hm.getY()) <= 24) {
                            return hm;
                        }
                    }
                    if ((found = PortalPlacementHelper.scanVerticalColumn(level, cx, cz, target.getY(), hasCeiling)) == null) continue;
                    return found;
                }
                dx += step;
            }
            break;
        }
        return target;
    }

    private static BlockPos scanVerticalColumn(ServerLevel level, int x, int z, int preferredY, boolean hasCeiling) {
        int maxY;
        int minY;
        int logicalHeight = level.getLogicalHeight();
        int minBuildHeight = level.getMinBuildHeight();
        int maxBuildHeight = level.getMaxBuildHeight();
        if (hasCeiling) {
            int ceilingLimit = logicalHeight - 4;
            if (preferredY >= logicalHeight - 1) {
                minY = logicalHeight;
                maxY = maxBuildHeight - 2;
            } else {
                minY = minBuildHeight + 2;
                maxY = Math.max(minY + 2, ceilingLimit);
            }
        } else {
            minY = minBuildHeight + 2;
            maxY = maxBuildHeight - 2;
        }
        int clampedY = Math.max(minY, Math.min(maxY, preferredY));
        int maxDelta = Math.max(clampedY - minY, maxY - clampedY);
        int delta = 0;
        while (delta <= maxDelta) {
            if (delta == 0) {
                BlockPos cand = new BlockPos(x, clampedY, z);
                if (PortalPlacementHelper.isSafeLocation(level, cand)) {
                    return cand;
                }
            } else {
                BlockPos cand;
                BlockPos cand2;
                int yUp = clampedY + delta;
                if (yUp >= minY && yUp <= maxY && PortalPlacementHelper.isSafeLocation(level, cand2 = new BlockPos(x, yUp, z))) {
                    return cand2;
                }
                int yDown = clampedY - delta;
                if (yDown >= minY && yDown <= maxY && PortalPlacementHelper.isSafeLocation(level, cand = new BlockPos(x, yDown, z))) {
                    return cand;
                }
            }
            ++delta;
        }
        return null;
    }

    private static boolean isSafeLocation(ServerLevel level, BlockPos pos) {
        if (pos.getY() <= level.getMinBuildHeight() + 1) return false;
        if (pos.getY() >= level.getMaxBuildHeight() - 1) {
            return false;
        }
        boolean hasCeiling = level.dimensionType().hasCeiling();
        if (hasCeiling && pos.getY() >= level.getLogicalHeight() - 4 && pos.getY() < level.getLogicalHeight()) {
            return false;
        }
        BlockState feet = level.getBlockState(pos);
        BlockState head = level.getBlockState(pos.above());
        BlockState ground = level.getBlockState(pos.below());
        boolean feetPassable = (feet.isAir() || PortalPlacementHelper.isBreakableFoliage(feet) || !feet.blocksMotion()) && !feet.is(Blocks.FIRE) && !feet.is(Blocks.SOUL_FIRE);
        boolean headPassable = (head.isAir() || PortalPlacementHelper.isBreakableFoliage(head) || !head.blocksMotion()) && !head.is(Blocks.FIRE) && !head.is(Blocks.SOUL_FIRE);
        boolean groundSolid = !ground.isAir() && ground.blocksMotion() && !PortalPlacementHelper.isBreakableFoliage(ground) && !ground.is(Blocks.MAGMA_BLOCK) && !ground.is(Blocks.CAMPFIRE) && !ground.is(Blocks.SOUL_CAMPFIRE);
        boolean notLava = !feet.getFluidState().is(FluidTags.LAVA) && !ground.getFluidState().is(FluidTags.LAVA) && !head.getFluidState().is(FluidTags.LAVA);
        return feetPassable && headPassable && groundSolid && notLava;
    }

    public static Vec3 getDimensionSpawnPos(ServerLevel level) {
        BlockPos safe;
        if (level == null) {
            return new Vec3(0.5, 64.0, 0.5);
        }
        ResourceKey dim = level.dimension();
        if (dim.equals(Level.END)) {
            try {
                EndPlatformFeature.createEndPlatform((ServerLevelAccessor)level, (BlockPos)ServerLevel.END_SPAWN_POINT.below(), (boolean)true);
            }
            catch (Exception exception) {
                // empty catch block
            }
            return new Vec3(98.8, 49.0, 0.5);
        }
        if (dim.equals(Level.NETHER) || level.dimensionType().hasCeiling()) {
            BlockPos center = new BlockPos(0, 64, 0);
            BlockPos safe2 = PortalPlacementHelper.findSafePlacement(level, center, true);
            if (safe2 == null) return new Vec3(0.5, 64.0, 0.5);
            return new Vec3((double)safe2.getX() + 0.5, (double)safe2.getY(), (double)safe2.getZ() + 0.5);
        }
        BlockPos target = level.getSharedSpawnPos();
        if (target.getY() <= level.getMinBuildHeight() + 2 || target.getX() == 0 && target.getY() == 0 && target.getZ() == 0) {
            target = new BlockPos(0, 64, 0);
        }
        if ((safe = PortalPlacementHelper.findSafePlacement(level, target, true)) == null) return new Vec3((double)target.getX() + 0.5, Math.max(64.0, (double)target.getY()), (double)target.getZ() + 0.5);
        return new Vec3((double)safe.getX() + 0.5, (double)safe.getY(), (double)safe.getZ() + 0.5);
    }

    public static String formatDimensionName(String dimStr) {
        if (dimStr == null) return "Overworld";
        if (dimStr.trim().isEmpty()) {
            return "Overworld";
        }
        String clean = dimStr.trim();
        String lower = clean.toLowerCase();
        if (lower.equals("overworld")) return "Overworld";
        if (lower.equals("minecraft:overworld")) {
            return "Overworld";
        }
        if (lower.equals("the_nether")) return "The Nether";
        if (lower.equals("nether")) return "The Nether";
        if (lower.equals("minecraft:the_nether")) {
            return "The Nether";
        }
        if (lower.equals("the_end")) return "The End";
        if (lower.equals("end")) return "The End";
        if (lower.equals("minecraft:the_end")) {
            return "The End";
        }
        if (BlenderDimensionHelper.isBlenderDimension((String)clean)) {
            return "Blender Dimension";
        }
        if (lower.startsWith("infinity:")) {
            String path = clean.substring("infinity:".length());
            return "\"" + path + "\" (Infinity)";
        }
        String namespace = "minecraft";
        String path = clean;
        if (clean.contains(":")) {
            String[] parts = clean.split(":", 2);
            namespace = parts[0].toLowerCase();
            path = parts[1];
        } else if (InfinityCompat.isLoaded()) {
            return "\"" + clean + "\" (Infinity)";
        }
        String[] words = path.replace('_', ' ').split(" ");
        StringBuilder sb = new StringBuilder();
        String[] stringArray = words;
        int n = stringArray.length;
        int n2 = 0;
        while (true) {
            if (n2 >= n) {
                String formattedPath = sb.toString().trim();
                if (!namespace.equals("minecraft")) return formattedPath + " (" + namespace + ")";
                return formattedPath;
            }
            String w = stringArray[n2];
            if (!w.isEmpty()) {
                sb.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1)).append(" ");
            }
            ++n2;
        }
    }

    public static boolean isBreakableFoliage(BlockState state) {
        if (state == null) return false;
        if (state.isAir()) {
            return false;
        }
        if (state.is(BlockTags.FLOWERS)) return true;
        if (state.is(BlockTags.CROPS)) return true;
        if (state.is(BlockTags.SAPLINGS)) return true;
        if (state.is(BlockTags.REPLACEABLE_BY_TREES)) return true;
        if (state.is(BlockTags.REPLACEABLE)) {
            return true;
        }
        Block block = state.getBlock();
        if (block instanceof BushBlock) return true;
        if (block instanceof DoublePlantBlock) return true;
        if (block instanceof GrowingPlantBlock) return true;
        if (block instanceof VineBlock) return true;
        if (!(block instanceof SnowLayerBlock)) return !state.isSolid() && state.canBeReplaced();
        return true;
    }

    public static void clearVegetationInPortalArea(ServerLevel level, Vec3 centerPos, int orientation, float yaw, float scale) {
        BlockPos center;
        double effectiveScale;
        block12: {
            block11: {
                block10: {
                    if (level == null) return;
                    if (centerPos == null) {
                        return;
                    }
                    effectiveScale = Math.max(0.5, Math.min(3.0, (double)scale));
                    center = BlockPos.containing((double)centerPos.x, (double)centerPos.y, (double)centerPos.z);
                    if (orientation == 2) break block10;
                    if (orientation == 0) break block11;
                    break block12;
                }
                int radius = Math.max(1, (int)Math.ceil(1.3 * effectiveScale));
                double maxDistSq = 1.35 * effectiveScale * (1.35 * effectiveScale);
                int dx = -radius;
                while (dx <= radius) {
                    for (int dz = -radius; dz <= radius; ++dz) {
                        double distSq = dx * dx + dz * dz;
                        if (!(distSq <= maxDistSq)) continue;
                        for (int dy = -1; dy <= 2; ++dy) {
                            BlockPos targetPos = center.offset(dx, dy, dz);
                            BlockState state = level.getBlockState(targetPos);
                            if (!PortalPlacementHelper.isBreakableFoliage(state)) continue;
                            level.destroyBlock(targetPos, true);
                            PortalPlacementHelper.spawnClearingParticles(level, targetPos);
                        }
                    }
                    ++dx;
                }
                return;
            }
            int radiusH = Math.max(1, (int)Math.ceil(1.0 * effectiveScale));
            int radiusV = Math.max(1, (int)Math.ceil(1.4 * effectiveScale));
            int dx = -radiusH;
            while (dx <= radiusH) {
                for (int dz = -radiusH; dz <= radiusH; ++dz) {
                    for (int dy = -radiusV; dy <= radiusV; ++dy) {
                        BlockPos targetPos = center.offset(dx, dy, dz);
                        BlockState state = level.getBlockState(targetPos);
                        if (!PortalPlacementHelper.isBreakableFoliage(state)) continue;
                        level.destroyBlock(targetPos, true);
                        PortalPlacementHelper.spawnClearingParticles(level, targetPos);
                    }
                }
                ++dx;
            }
            return;
        }
        if (orientation != 1) return;
        int radius = Math.max(1, (int)Math.ceil(1.3 * effectiveScale));
        double maxDistSq = 1.35 * effectiveScale * (1.35 * effectiveScale);
        int dx = -radius;
        while (dx <= radius) {
            for (int dz = -radius; dz <= radius; ++dz) {
                double distSq = dx * dx + dz * dz;
                if (!(distSq <= maxDistSq)) continue;
                for (int dy = -2; dy <= 1; ++dy) {
                    BlockPos targetPos = center.offset(dx, dy, dz);
                    BlockState state = level.getBlockState(targetPos);
                    if (!PortalPlacementHelper.isBreakableFoliage(state)) continue;
                    level.destroyBlock(targetPos, true);
                    PortalPlacementHelper.spawnClearingParticles(level, targetPos);
                }
            }
            ++dx;
        }
    }

    private static void spawnClearingParticles(ServerLevel level, BlockPos pos) {
        level.sendParticles((ParticleOptions)((SimpleParticleType)ModParticles.FLUID_EVAPORATION.get()), (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, 4, 0.2, 0.2, 0.2, 0.02);
    }
}

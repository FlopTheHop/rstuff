/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.HolderGetter
 *  net.minecraft.core.Vec3i
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.NbtUtils
 *  net.minecraft.nbt.Tag
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.AABB
 *  org.ExtraTortik.ricks_stuff.block.OmegaCoreBlock
 *  org.ExtraTortik.ricks_stuff.block.entity.OmegaCoreBlockEntity
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDevicePhase
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaMultiblockPattern$BlockRequirement
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaMultiblockPattern$ClearanceResult
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaMultiblockPattern$ObstructionEntry
 *  org.ExtraTortik.ricks_stuff.init.ModBlocks
 *  org.ExtraTortik.ricks_stuff.init.ModEntities
 *  org.ExtraTortik.ricks_stuff.init.ModParticles
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 */
package org.ExtraTortik.ricks_stuff.entity.omega;

import java.lang.invoke.LambdaMetafactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import org.ExtraTortik.ricks_stuff.block.OmegaCoreBlock;
import org.ExtraTortik.ricks_stuff.block.entity.OmegaCoreBlockEntity;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDevicePhase;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaMultiblockPattern;
import org.ExtraTortik.ricks_stuff.init.ModBlocks;
import org.ExtraTortik.ricks_stuff.init.ModEntities;
import org.ExtraTortik.ricks_stuff.init.ModParticles;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.jetbrains.annotations.Nullable;

/*
 * Exception performing whole class analysis ignored.
 */
public class OmegaMultiblockPattern {
    private static final List<BlockRequirement> REQUIREMENTS;
    private static final Set<BlockPos> MULTIBLOCK_POSITIONS;

    public static Set<BlockPos> getMultiblockPositions() {
        return MULTIBLOCK_POSITIONS;
    }

    public static List<BlockRequirement> getRequirements() {
        return REQUIREMENTS;
    }

    public static boolean matches(BlockState expected, BlockState current) {
        if (!current.is(expected.getBlock())) {
            return false;
        }
        if (!expected.hasProperty((Property)BlockStateProperties.FACING)) return true;
        return current.hasProperty((Property)BlockStateProperties.FACING) && current.getValue((Property)BlockStateProperties.FACING) == expected.getValue((Property)BlockStateProperties.FACING);
    }

    private static boolean isAdjacentToCore(int dx, int dy, int dz) {
        return Math.abs(dx) <= 1 && Math.abs(dz) <= 1 && dy >= -1 && dy <= 1;
    }

    public static ClearanceResult checkClearance(Level level, BlockPos centerPos) {
        int minY = centerPos.getY() - 18;
        int maxY = centerPos.getY() + 30;
        int radius = 18;
        int radiusSq = radius * radius;
        ArrayList<ObstructionEntry> obstructions = new ArrayList<ObstructionEntry>();
        int dy = -18;
        block0: while (true) {
            if (dy > 30) {
                obstructions.sort(Comparator.comparingDouble(ObstructionEntry::distSq));
                return obstructions.isEmpty() ? ClearanceResult.ok() : ClearanceResult.obstructed(obstructions);
            }
            int y = centerPos.getY() + dy;
            int dx = -radius;
            while (true) {
                if (dx <= radius) {
                } else {
                    ++dy;
                    continue block0;
                }
                for (int dz = -radius; dz <= radius; ++dz) {
                    BlockState state;
                    BlockPos checkPos;
                    if (dx * dx + dz * dz > radiusSq || OmegaMultiblockPattern.isAdjacentToCore(dx, dy, dz) || dy >= 0 && dy <= 11 && MULTIBLOCK_POSITIONS.contains(new BlockPos(dx, dy, dz)) || level.isOutsideBuildHeight(checkPos = centerPos.offset(dx, dy, dz)) || !OmegaMultiblockPattern.isObstruction(level, checkPos, state = level.getBlockState(checkPos))) continue;
                    double distSq = centerPos.distSqr((Vec3i)checkPos);
                    obstructions.add(new ObstructionEntry(checkPos.immutable(), state, distSq));
                }
                ++dx;
            }
            break;
        }
    }

    private static boolean isObstruction(Level level, BlockPos pos, BlockState state) {
        if (state.isAir()) {
            return false;
        }
        if (!state.getFluidState().isEmpty()) return false;
        if (state.liquid()) {
            return false;
        }
        if (state.canBeReplaced()) {
            return false;
        }
        if (!state.blocksMotion() && !state.isSolid()) {
            return false;
        }
        if (!state.getCollisionShape((BlockGetter)level, pos).isEmpty()) return true;
        return false;
    }

    public static List<BlockRequirement> getMissingRequirements(Level level, BlockPos centerPos) {
        ArrayList<BlockRequirement> missing = new ArrayList<BlockRequirement>();
        Iterator<BlockRequirement> iterator = REQUIREMENTS.iterator();
        while (iterator.hasNext()) {
            BlockRequirement req = iterator.next();
            BlockPos pos = centerPos.offset((Vec3i)req.relPos());
            BlockState state = level.getBlockState(pos);
            if (OmegaMultiblockPattern.matches(req.expectedState(), state)) continue;
            missing.add(req);
        }
        return missing;
    }

    public static boolean checkStructure(Level level, BlockPos centerPos) {
        BlockState state;
        BlockRequirement req;
        if (!level.getBlockState(centerPos).is((Block)ModBlocks.OMEGA_CORE.get())) {
            return false;
        }
        Iterator<BlockRequirement> iterator = REQUIREMENTS.iterator();
        do {
            if (!iterator.hasNext()) return OmegaMultiblockPattern.checkClearance(level, centerPos).clear();
            req = iterator.next();
            BlockPos pos = centerPos.offset((Vec3i)req.relPos());
            state = level.getBlockState(pos);
        } while (OmegaMultiblockPattern.matches(req.expectedState(), state));
        return false;
    }

    public static boolean checkActiveStructure(Level level, BlockPos centerPos, OmegaCoreBlockEntity core) {
        if (!level.getBlockState(centerPos).is((Block)ModBlocks.OMEGA_CORE.get())) {
            return false;
        }
        if (!core.isStructureAbsorbed()) return OmegaMultiblockPattern.checkStructure(level, centerPos);
        int dx = -1;
        block0: while (dx <= 1) {
            int dy = 0;
            while (true) {
                if (dy <= 2) {
                } else {
                    ++dx;
                    continue block0;
                }
                for (int dz = -1; dz <= 1; ++dz) {
                    BlockPos barrierPos;
                    if (dx == 0 && dy == 0 && dz == 0 || level.getBlockState(barrierPos = centerPos.offset(dx, dy, dz)).is((Block)ModBlocks.OMEGA_BARRIER.get())) continue;
                    return false;
                }
                ++dy;
            }
            break;
        }
        return true;
    }

    public static boolean assemble(ServerLevel level, BlockPos centerPos, OmegaCoreBlockEntity core) {
        OmegaDeviceEntity dev;
        Entity existing;
        if (!OmegaMultiblockPattern.checkStructure((Level)level, centerPos)) {
            return false;
        }
        if (core.isAssembled() && core.getBoundDeviceUUID() != null && (existing = level.getEntity(core.getBoundDeviceUUID())) instanceof OmegaDeviceEntity && (dev = (OmegaDeviceEntity)existing).isAlive()) {
            return true;
        }
        double spawnX = (double)centerPos.getX() + 0.5;
        double spawnY = (double)centerPos.getY() + 5.0;
        double spawnZ = (double)centerPos.getZ() + 0.5;
        OmegaDeviceEntity device = (OmegaDeviceEntity)((EntityType)ModEntities.OMEGA_DEVICE.get()).create((Level)level);
        if (device == null) {
            return false;
        }
        device.moveTo(spawnX, spawnY, spawnZ, 0.0f, 0.0f);
        if (level.hasNeighborSignal(centerPos)) {
            device.setPhase(OmegaDevicePhase.ARMED);
            device.setPhaseTicks(0);
        } else {
            device.setPhase(OmegaDevicePhase.IDLE);
        }
        level.addFreshEntity((Entity)device);
        core.setBoundDeviceUUID(device.getUUID());
        core.setAssembled(true);
        ListTag blocksList = new ListTag();
        for (BlockRequirement req : REQUIREMENTS) {
            BlockPos pos = centerPos.offset((Vec3i)req.relPos());
            BlockState state = level.getBlockState(pos);
            CompoundTag entry = new CompoundTag();
            entry.putInt("X", req.relPos().getX());
            entry.putInt("Y", req.relPos().getY());
            entry.putInt("Z", req.relPos().getZ());
            entry.put("State", (Tag)NbtUtils.writeBlockState((BlockState)state));
            blocksList.add((Object)entry);
        }
        CompoundTag absorbedTag = new CompoundTag();
        absorbedTag.put("Blocks", (Tag)blocksList);
        core.setAbsorbedBlocksTag(absorbedTag);
        core.setStructureAbsorbed(true);
        ArrayList<BlockPos> positionsToRemove = new ArrayList<BlockPos>();
        for (BlockRequirement req : REQUIREMENTS) {
            positionsToRemove.add(centerPos.offset((Vec3i)req.relPos()));
        }
        positionsToRemove.sort((a, b) -> Integer.compare(b.getY(), a.getY()));
        AABB protectionArea = new AABB(centerPos).inflate(18.0, 18.0, 18.0);
        List nearbyPlayers = level.getEntitiesOfClass(Player.class, protectionArea);
        for (Player player : nearbyPlayers) {
            player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 240, 0, false, false, true));
            player.fallDistance = 0.0f;
        }
        for (BlockPos pos : positionsToRemove) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }
        int dx = -1;
        block4: while (true) {
            if (dx > 1) {
                level.playSound(null, spawnX, spawnY, spawnZ, (SoundEvent)ModSounds.OMEGA_DEVICE_START.get(), SoundSource.BLOCKS, 10.0f, 1.0f);
                level.sendParticles((ParticleOptions)((SimpleParticleType)ModParticles.OMEGA_LIGHTNING.get()), spawnX, spawnY + 1.0, spawnZ, 40, 1.0, 1.0, 1.0, 0.1);
                BlockState state = level.getBlockState(centerPos);
                if (!state.hasProperty((Property)OmegaCoreBlock.ACTIVE)) return true;
                if ((Boolean)state.getValue((Property)OmegaCoreBlock.ACTIVE) != false) return true;
                level.setBlock(centerPos, (BlockState)state.setValue((Property)OmegaCoreBlock.ACTIVE, (Comparable)Boolean.valueOf(true)), 3);
                return true;
            }
            int dy = 0;
            while (true) {
                if (dy <= 2) {
                } else {
                    ++dx;
                    continue block4;
                }
                for (int dz = -1; dz <= 1; ++dz) {
                    if (dx == 0 && dy == 0 && dz == 0) continue;
                    BlockPos barrierPos = centerPos.offset(dx, dy, dz);
                    level.setBlock(barrierPos, ((Block)ModBlocks.OMEGA_BARRIER.get()).defaultBlockState(), 3);
                }
                ++dy;
            }
            break;
        }
    }

    public static void disassemble(ServerLevel level, BlockPos centerPos, @Nullable OmegaCoreBlockEntity core) {
        OmegaMultiblockPattern.disassembleInternal(level, centerPos, core, null);
    }

    public static void disassembleWithBreak(ServerLevel level, BlockPos centerPos, @Nullable OmegaCoreBlockEntity core, BlockPos brokenPos) {
        OmegaMultiblockPattern.disassembleInternal(level, centerPos, core, brokenPos);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Unable to fully structure code
     */
    private static void disassembleInternal(ServerLevel level, BlockPos centerPos, @Nullable OmegaCoreBlockEntity core, @Nullable BlockPos brokenPos) {
        block26: {
            block27: {
                if (core != null) {
                    if (core.isDisassembling() != false) return;
                    if (!core.isAssembled()) {
                        return;
                    }
                    core.setDisassembling(true);
                }
                wasAbsorbed = core != null && core.isStructureAbsorbed() != false;
                v0 = absorbedTag = core != null ? core.getAbsorbedBlocksTag() : null;
                if (core != null) {
                    core.setStructureAbsorbed(false);
                    core.setAssembled(false);
                    core.setAbsorbedBlocksTag(new CompoundTag());
                }
                dx = -1;
lbl14:
                // 2 sources

                while (true) {
                    if (dx <= 1) break block26;
                    if (wasAbsorbed) {
                        restoredPositions = new ArrayList<BlockPos>();
                        if (absorbedTag != null && absorbedTag.contains("Blocks", 9)) {
                            blocksList = absorbedTag.getList("Blocks", 10);
                            tagEntries = new ArrayList<E>();
                            for (i = 0; i < blocksList.size(); ++i) {
                                tagEntries.add(blocksList.getCompound(i));
                            }
                            tagEntries.sort((Comparator)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;Ljava/lang/Object;)I, lambda$disassembleInternal$1(net.minecraft.nbt.CompoundTag net.minecraft.nbt.CompoundTag ), (Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/nbt/CompoundTag;)I)());
                            i = tagEntries.iterator();
                            while (i.hasNext()) {
                                entry = (CompoundTag)i.next();
                                relX = entry.getInt("X");
                                relY = entry.getInt("Y");
                                relZ = entry.getInt("Z");
                                restorePos = centerPos.offset(relX, relY, relZ);
                                restoredState = NbtUtils.readBlockState((HolderGetter)level.holderLookup(Registries.BLOCK), (CompoundTag)entry.getCompound("State"));
                                level.setBlock(restorePos, restoredState, 3);
                                restoredPositions.add(restorePos);
                            }
                        } else {
                            sortedReqs = new ArrayList<BlockRequirement>(OmegaMultiblockPattern.REQUIREMENTS);
                            sortedReqs.sort((Comparator)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;Ljava/lang/Object;)I, lambda$disassembleInternal$2(org.ExtraTortik.ricks_stuff.entity.omega.OmegaMultiblockPattern$BlockRequirement org.ExtraTortik.ricks_stuff.entity.omega.OmegaMultiblockPattern$BlockRequirement ), (Lorg/ExtraTortik/ricks_stuff/entity/omega/OmegaMultiblockPattern$BlockRequirement;Lorg/ExtraTortik/ricks_stuff/entity/omega/OmegaMultiblockPattern$BlockRequirement;)I)());
                            for (BlockRequirement req : sortedReqs) {
                                restorePos = centerPos.offset((Vec3i)req.relPos());
                                level.setBlock(restorePos, req.expectedState(), 3);
                                restoredPositions.add(restorePos);
                            }
                        }
                        if (brokenPos != null && !restoredPositions.isEmpty()) {
                            posToBreak = null;
                            minCryingDist = 1.7976931348623157E308;
                            minAnyDist = 1.7976931348623157E308;
                            nearestAny = null;
                            for (BlockPos rPos : restoredPositions) {
                                dist = brokenPos.distSqr((Vec3i)rPos);
                                rState = level.getBlockState(rPos);
                                if (rState.is(Blocks.CRYING_OBSIDIAN) && dist < minCryingDist) {
                                    minCryingDist = dist;
                                    posToBreak = rPos;
                                }
                                if (!(dist < minAnyDist)) continue;
                                minAnyDist = dist;
                                nearestAny = rPos;
                            }
                            if (posToBreak == null) {
                                posToBreak = nearestAny;
                            }
                            if (posToBreak != null) {
                                level.destroyBlock(posToBreak, true);
                            }
                        }
                    }
                    if (core != null) {
                        boundUUID = core.getBoundDeviceUUID();
                        if (boundUUID != null && (entity = level.getEntity(boundUUID)) instanceof OmegaDeviceEntity) {
                            dev = (OmegaDeviceEntity)entity;
                            dev.discard();
                        }
                        core.setBoundDeviceUUID(null);
                    }
                    area = new AABB(centerPos).inflate(12.0);
                    nearby = level.getEntitiesOfClass(OmegaDeviceEntity.class, area);
                    for (OmegaDeviceEntity dev : nearby) {
                        dev.discard();
                    }
                    break block27;
                    break;
                }
                finally {
                    if (core != null) {
                        core.setDisassembling(false);
                    }
                }
            }
            x = (double)centerPos.getX() + 0.5;
            y = (double)centerPos.getY() + 5.0;
            z = (double)centerPos.getZ() + 0.5;
            level.playSound(null, x, y, z, (SoundEvent)ModSounds.OMEGA_DEVICE_COOLDOWN.get(), SoundSource.BLOCKS, 5.0f, 1.0f);
            state = level.getBlockState(centerPos);
            if (state.hasProperty((Property)OmegaCoreBlock.ACTIVE) == false) return;
            if ((Boolean)state.getValue((Property)OmegaCoreBlock.ACTIVE) == false) return;
            level.setBlock(centerPos, (BlockState)state.setValue((Property)OmegaCoreBlock.ACTIVE, (Comparable)Boolean.valueOf(false)), 3);
            return;
        }
        dy = 0;
        while (true) {
            if (dy <= 2) {
            } else {
                ++dx;
                ** continue;
            }
            for (dz = -1; dz <= 1; ++dz) {
                if (dx == 0 && dy == 0 && dz == 0 || !level.getBlockState(barrierPos = centerPos.offset(dx, dy, dz)).is((Block)ModBlocks.OMEGA_BARRIER.get())) continue;
                level.setBlock(barrierPos, Blocks.AIR.defaultBlockState(), 3);
            }
            ++dy;
        }
    }

    private static /* synthetic */ int lambda$disassembleInternal$2(BlockRequirement a, BlockRequirement b) {
        return Integer.compare(a.relPos().getY(), b.relPos().getY());
    }

    private static /* synthetic */ int lambda$disassembleInternal$1(CompoundTag a, CompoundTag b) {
        return Integer.compare(a.getInt("Y"), b.getInt("Y"));
    }

    static {
        int dx;
        BlockState state;
        int dz;
        ArrayList<BlockRequirement> list = new ArrayList<BlockRequirement>(128);
        list.add(new BlockRequirement(new BlockPos(-5, 0, -5), Blocks.NETHERITE_BLOCK.defaultBlockState()));
        list.add(new BlockRequirement(new BlockPos(-5, 0, 5), Blocks.NETHERITE_BLOCK.defaultBlockState()));
        list.add(new BlockRequirement(new BlockPos(5, 0, -5), Blocks.NETHERITE_BLOCK.defaultBlockState()));
        list.add(new BlockRequirement(new BlockPos(5, 0, 5), Blocks.NETHERITE_BLOCK.defaultBlockState()));
        list.add(new BlockRequirement(new BlockPos(-2, 0, 0), Blocks.LODESTONE.defaultBlockState()));
        list.add(new BlockRequirement(new BlockPos(2, 0, 0), Blocks.LODESTONE.defaultBlockState()));
        list.add(new BlockRequirement(new BlockPos(0, 0, -2), Blocks.LODESTONE.defaultBlockState()));
        list.add(new BlockRequirement(new BlockPos(0, 0, 2), Blocks.LODESTONE.defaultBlockState()));
        int[] nArray = new int[]{-5, 5};
        int n = nArray.length;
        int n2 = 0;
        while (true) {
            if (n2 >= n) break;
            dz = nArray[n2];
            for (int dx2 = -4; dx2 <= 4; ++dx2) {
                state = Math.abs(dx2) % 2 == 0 ? Blocks.CRYING_OBSIDIAN.defaultBlockState() : Blocks.OBSIDIAN.defaultBlockState();
                list.add(new BlockRequirement(new BlockPos(dx2, 0, dz), state));
            }
            ++n2;
        }
        nArray = new int[]{-5, 5};
        n = nArray.length;
        n2 = 0;
        while (true) {
            if (n2 >= n) break;
            dx = nArray[n2];
            for (int dz2 = -4; dz2 <= 4; ++dz2) {
                state = Math.abs(dz2) % 2 == 0 ? Blocks.CRYING_OBSIDIAN.defaultBlockState() : Blocks.OBSIDIAN.defaultBlockState();
                list.add(new BlockRequirement(new BlockPos(dx, 0, dz2), state));
            }
            ++n2;
        }
        nArray = new int[]{-5, 5};
        n = nArray.length;
        n2 = 0;
        block4: while (true) {
            if (n2 >= n) break;
            dx = nArray[n2];
            int[] dz2 = new int[]{-5, 5};
            int state2 = dz2.length;
            int n3 = 0;
            while (true) {
                BlockState state3;
                int dy;
                int dz3;
                if (n3 < state2) {
                    dz3 = dz2[n3];
                    list.add(new BlockRequirement(new BlockPos(dx, 1, dz3), Blocks.RESPAWN_ANCHOR.defaultBlockState()));
                } else {
                    ++n2;
                    continue block4;
                }
                for (dy = 2; dy <= 4; ++dy) {
                    state3 = dy % 2 == 0 ? Blocks.CRYING_OBSIDIAN.defaultBlockState() : Blocks.OBSIDIAN.defaultBlockState();
                    list.add(new BlockRequirement(new BlockPos(dx, dy, dz3), state3));
                }
                list.add(new BlockRequirement(new BlockPos(dx, 5, dz3), Blocks.LODESTONE.defaultBlockState()));
                for (dy = 6; dy <= 8; ++dy) {
                    state3 = dy % 2 == 0 ? Blocks.CRYING_OBSIDIAN.defaultBlockState() : Blocks.OBSIDIAN.defaultBlockState();
                    list.add(new BlockRequirement(new BlockPos(dx, dy, dz3), state3));
                }
                list.add(new BlockRequirement(new BlockPos(dx, 9, dz3), Blocks.NETHERITE_BLOCK.defaultBlockState()));
                ++n3;
            }
            break;
        }
        list.add(new BlockRequirement(new BlockPos(-5, 10, -5), Blocks.BEACON.defaultBlockState()));
        list.add(new BlockRequirement(new BlockPos(-5, 10, 5), Blocks.BEACON.defaultBlockState()));
        list.add(new BlockRequirement(new BlockPos(5, 10, -5), Blocks.BEACON.defaultBlockState()));
        list.add(new BlockRequirement(new BlockPos(5, 10, 5), Blocks.BEACON.defaultBlockState()));
        list.add(new BlockRequirement(new BlockPos(0, 10, 0), Blocks.BEACON.defaultBlockState()));
        nArray = new int[]{-5, 5};
        n = nArray.length;
        n2 = 0;
        while (true) {
            if (n2 < n) {
                dz = nArray[n2];
            } else {
                nArray = new int[]{-5, 5};
                n = nArray.length;
                break;
            }
            for (int dx3 = -4; dx3 <= 4; ++dx3) {
                BlockState state4 = dx3 == 0 ? Blocks.LODESTONE.defaultBlockState() : (Math.abs(dx3) % 2 == 0 ? Blocks.CRYING_OBSIDIAN.defaultBlockState() : Blocks.OBSIDIAN.defaultBlockState());
                list.add(new BlockRequirement(new BlockPos(dx3, 10, dz), state4));
            }
            ++n2;
        }
        for (n2 = 0; n2 < n; ++n2) {
            dx = nArray[n2];
            for (int dz4 = -4; dz4 <= 4; ++dz4) {
                BlockState state5 = dz4 == 0 ? Blocks.LODESTONE.defaultBlockState() : (Math.abs(dz4) % 2 == 0 ? Blocks.CRYING_OBSIDIAN.defaultBlockState() : Blocks.OBSIDIAN.defaultBlockState());
                list.add(new BlockRequirement(new BlockPos(dx, 10, dz4), state5));
            }
        }
        BlockState rodUp = (BlockState)Blocks.LIGHTNING_ROD.defaultBlockState().setValue((Property)BlockStateProperties.FACING, (Comparable)Direction.UP);
        list.add(new BlockRequirement(new BlockPos(-5, 11, -5), rodUp));
        list.add(new BlockRequirement(new BlockPos(-5, 11, 5), rodUp));
        list.add(new BlockRequirement(new BlockPos(5, 11, -5), rodUp));
        list.add(new BlockRequirement(new BlockPos(5, 11, 5), rodUp));
        REQUIREMENTS = Collections.unmodifiableList(list);
        HashSet<BlockPos> positions = new HashSet<BlockPos>();
        positions.add(BlockPos.ZERO);
        Iterator iterator = list.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                MULTIBLOCK_POSITIONS = Collections.unmodifiableSet(positions);
                return;
            }
            BlockRequirement req = (BlockRequirement)iterator.next();
            positions.add(req.relPos());
        }
    }
}

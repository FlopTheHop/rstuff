/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.util.Pair
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Holder
 *  net.minecraft.core.HolderSet
 *  net.minecraft.core.HolderSet$Direct
 *  net.minecraft.core.Registry
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.chunk.ChunkAccess
 *  net.minecraft.world.level.chunk.status.ChunkStatus
 *  net.minecraft.world.level.levelgen.Heightmap$Types
 *  net.minecraft.world.level.levelgen.structure.BoundingBox
 *  net.minecraft.world.level.levelgen.structure.Structure
 *  net.minecraft.world.level.levelgen.structure.StructureStart
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.network.PacketDistributor
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.UpgradeType
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CStructureFoundPacket
 *  org.ExtraTortik.ricks_stuff.portal.PortalMode
 *  org.ExtraTortik.ricks_stuff.portal.PortalPlacementHelper
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import com.mojang.datafixers.util.Pair;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.ExtraTortik.ricks_stuff.item.UpgradeType;
import org.ExtraTortik.ricks_stuff.network.packet.S2CStructureFoundPacket;
import org.ExtraTortik.ricks_stuff.portal.PortalMode;
import org.ExtraTortik.ricks_stuff.portal.PortalPlacementHelper;

public record C2STargetStructurePacket(String structureId, String targetDimension) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2STargetStructurePacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"target_structure"));
    public static final StreamCodec<RegistryFriendlyByteBuf, C2STargetStructurePacket> STREAM_CODEC = StreamCodec.ofMember(C2STargetStructurePacket::write, C2STargetStructurePacket::new);

    public C2STargetStructurePacket(RegistryFriendlyByteBuf buf) {
        this(buf.readUtf(), buf.readUtf());
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeUtf(this.structureId);
        buf.writeUtf(this.targetDimension);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(C2STargetStructurePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player patt0$temp = context.player();
            if (!(patt0$temp instanceof ServerPlayer)) return;
            ServerPlayer player = (ServerPlayer)patt0$temp;
            if (player.getServer() == null) return;
            ItemStack stack = C2STargetStructurePacket.getHeldGun(player);
            if (stack.isEmpty()) {
                return;
            }
            if (!player.getAbilities().instabuild && !PortalGunItem.hasUpgrade((ItemStack)stack, (UpgradeType)UpgradeType.STRUCTURE_SCANNER)) {
                PacketDistributor.sendToPlayer((ServerPlayer)player, (CustomPacketPayload)new S2CStructureFoundPacket(false, C2STargetStructurePacket.formatStructureName(packet.structureId()), 0.0, 0.0, 0.0, packet.targetDimension()), (CustomPacketPayload[])new CustomPacketPayload[0]);
                player.playNotifySound((SoundEvent)ModSounds.GUN_MALFUNCTION.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
                return;
            }
            ResourceLocation dimLoc = ResourceLocation.parse((String)packet.targetDimension());
            ResourceKey dimKey = ResourceKey.create((ResourceKey)Registries.DIMENSION, (ResourceLocation)dimLoc);
            ServerLevel targetLevel = player.getServer().getLevel(dimKey);
            if (targetLevel == null) {
                targetLevel = player.serverLevel();
            }
            String currentDimStr = player.level().dimension().location().toString();
            String targetDimStr = targetLevel.dimension().location().toString();
            if (!(targetDimStr.equals(currentDimStr) || player.getAbilities().instabuild || PortalGunItem.hasUpgrade((ItemStack)stack, (UpgradeType)UpgradeType.DIMENSIONAL))) {
                PacketDistributor.sendToPlayer((ServerPlayer)player, (CustomPacketPayload)new S2CStructureFoundPacket(false, C2STargetStructurePacket.formatStructureName(packet.structureId()), 0.0, 0.0, 0.0, packet.targetDimension()), (CustomPacketPayload[])new CustomPacketPayload[0]);
                player.playNotifySound((SoundEvent)ModSounds.GUN_MALFUNCTION.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
                return;
            }
            ResourceLocation structLoc = ResourceLocation.parse((String)packet.structureId());
            ResourceKey structKey = ResourceKey.create((ResourceKey)Registries.STRUCTURE, (ResourceLocation)structLoc);
            Registry structRegistry = targetLevel.registryAccess().registryOrThrow(Registries.STRUCTURE);
            Holder holder = structRegistry.getHolder(structKey).orElse(null);
            if (holder == null) {
                PacketDistributor.sendToPlayer((ServerPlayer)player, (CustomPacketPayload)new S2CStructureFoundPacket(false, C2STargetStructurePacket.formatStructureName(packet.structureId()), 0.0, 0.0, 0.0, packet.targetDimension()), (CustomPacketPayload[])new CustomPacketPayload[0]);
                player.playNotifySound((SoundEvent)ModSounds.GUN_MALFUNCTION.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
                return;
            }
            ServerLevel searchLevel = targetLevel;
            BlockPos searchOrigin = searchLevel == player.serverLevel() ? player.blockPosition() : searchLevel.getSharedSpawnPos();
            HolderSet.Direct holderSet = HolderSet.direct((Holder[])new Holder[]{holder});
            CompletableFuture.supplyAsync(() -> C2STargetStructurePacket.lambda$handle$0(searchLevel, (HolderSet)holderSet, searchOrigin)).thenAcceptAsync(pair -> {
                if (pair != null && pair.getFirst() != null) {
                    int preferredY;
                    BlockPos foundPos = (BlockPos)pair.getFirst();
                    int chunkX = foundPos.getX() >> 4;
                    int chunkZ = foundPos.getZ() >> 4;
                    ChunkAccess chunk = null;
                    try {
                        chunk = searchLevel.getChunk(chunkX, chunkZ, ChunkStatus.STRUCTURE_STARTS, true);
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                    StructureStart start = chunk != null ? chunk.getStartForStructure((Structure)holder.value()) : null;
                    int candidateX = foundPos.getX();
                    int candidateZ = foundPos.getZ();
                    boolean hasCeiling = searchLevel.dimensionType().hasCeiling();
                    if (start != null && start.isValid()) {
                        BoundingBox box = start.getBoundingBox();
                        BlockPos center = box.getCenter();
                        candidateX = center.getX();
                        candidateZ = center.getZ();
                        if (!hasCeiling) {
                            int surfaceY = searchLevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, candidateX, candidateZ);
                            preferredY = box.maxY() < surfaceY - 12 ? Math.max(searchLevel.getMinBuildHeight() + 2, box.minY() + 1) : Math.max(surfaceY, box.minY() + 1);
                        } else {
                            int minY = searchLevel.getMinBuildHeight() + 4;
                            int maxY = searchLevel.getLogicalHeight() - 10;
                            preferredY = Math.max(minY, Math.min(maxY, box.minY() + 1));
                        }
                    } else {
                        int surfaceY;
                        preferredY = !hasCeiling ? ((surfaceY = searchLevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, candidateX, candidateZ)) > searchLevel.getMinBuildHeight() + 2 ? surfaceY : 64) : 64;
                    }
                    BlockPos candidatePos = new BlockPos(candidateX, preferredY, candidateZ);
                    Vec3 safeVec = PortalPlacementHelper.findSafePlacement((ServerLevel)searchLevel, (Vec3)new Vec3((double)candidatePos.getX() + 0.5, (double)candidatePos.getY(), (double)candidatePos.getZ() + 0.5));
                    CompoundTag locTag = new CompoundTag();
                    locTag.putDouble("X", (double)Math.round(safeVec.x * 10.0) / 10.0);
                    locTag.putDouble("Y", (double)Math.round(safeVec.y * 10.0) / 10.0);
                    locTag.putDouble("Z", (double)Math.round(safeVec.z * 10.0) / 10.0);
                    locTag.putString("Dimension", searchLevel.dimension().location().toString());
                    locTag.putString("RawText", packet.targetDimension());
                    String structName = C2STargetStructurePacket.formatStructureName(packet.structureId());
                    locTag.putString("Name", structName);
                    locTag.putString("StructureId", packet.structureId());
                    PortalGunItem.setCustomLocation((ItemStack)stack, (CompoundTag)locTag);
                    PortalGunItem.setMode((ItemStack)stack, (PortalMode)PortalMode.CUSTOM);
                    player.containerMenu.broadcastChanges();
                    player.inventoryMenu.broadcastChanges();
                    player.playNotifySound((SoundEvent)ModSounds.UI_DIAL.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
                    PacketDistributor.sendToPlayer((ServerPlayer)player, (CustomPacketPayload)new S2CStructureFoundPacket(true, structName, safeVec.x, safeVec.y, safeVec.z, searchLevel.dimension().location().toString()), (CustomPacketPayload[])new CustomPacketPayload[0]);
                } else {
                    player.playNotifySound((SoundEvent)ModSounds.GUN_MALFUNCTION.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
                    PacketDistributor.sendToPlayer((ServerPlayer)player, (CustomPacketPayload)new S2CStructureFoundPacket(false, C2STargetStructurePacket.formatStructureName(packet.structureId()), 0.0, 0.0, 0.0, packet.targetDimension()), (CustomPacketPayload[])new CustomPacketPayload[0]);
                }
            }, (Executor)player.getServer());
        });
    }

    public static String formatStructureName(String structureId) {
        if (structureId == null) return "Structure";
        if (structureId.isEmpty()) {
            return "Structure";
        }
        String path = structureId.contains(":") ? structureId.substring(structureId.indexOf(58) + 1) : structureId;
        String[] words = path.split("_");
        StringBuilder sb = new StringBuilder();
        String[] stringArray = words;
        int n = stringArray.length;
        int n2 = 0;
        while (n2 < n) {
            String w = stringArray[n2];
            if (!w.isEmpty()) {
                if (!sb.isEmpty()) {
                    sb.append(" ");
                }
                sb.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1).toLowerCase());
            }
            ++n2;
        }
        return sb.toString();
    }

    private static ItemStack getHeldGun(ServerPlayer player) {
        if (player.getMainHandItem().getItem() instanceof PortalGunItem) return player.getMainHandItem();
        if (player.getMainHandItem().getItem() instanceof DischargedPortalGunItem) {
            return player.getMainHandItem();
        }
        if (player.getOffhandItem().getItem() instanceof PortalGunItem) return player.getOffhandItem();
        if (!(player.getOffhandItem().getItem() instanceof DischargedPortalGunItem)) return ItemStack.EMPTY;
        return player.getOffhandItem();
    }

    private static /* synthetic */ Pair lambda$handle$0(ServerLevel searchLevel, HolderSet holderSet, BlockPos searchOrigin) {
        try {
            return searchLevel.getChunkSource().getGenerator().findNearestMapStructure(searchLevel, holderSet, searchOrigin, 100, false);
        }
        catch (Exception e) {
            return null;
        }
    }
}

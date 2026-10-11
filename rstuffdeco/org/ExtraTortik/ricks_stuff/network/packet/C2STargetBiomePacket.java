/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.FriendlyByteBuf
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
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.levelgen.Heightmap$Types
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.UpgradeType
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CBiomeFoundPacket
 *  org.ExtraTortik.ricks_stuff.portal.PortalMode
 *  org.ExtraTortik.ricks_stuff.portal.PortalPlacementHelper
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.ExtraTortik.ricks_stuff.item.UpgradeType;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.S2CBiomeFoundPacket;
import org.ExtraTortik.ricks_stuff.portal.PortalMode;
import org.ExtraTortik.ricks_stuff.portal.PortalPlacementHelper;

public record C2STargetBiomePacket(String biomeId, String targetDimension) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2STargetBiomePacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"target_biome"));
    public static final StreamCodec<FriendlyByteBuf, C2STargetBiomePacket> STREAM_CODEC = StreamCodec.ofMember(C2STargetBiomePacket::write, C2STargetBiomePacket::new);

    public C2STargetBiomePacket(FriendlyByteBuf buf) {
        this(buf.readUtf(), buf.readUtf());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.biomeId != null ? this.biomeId : "");
        buf.writeUtf(this.targetDimension != null ? this.targetDimension : "minecraft:overworld");
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(C2STargetBiomePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player patt0$temp = context.player();
            if (!(patt0$temp instanceof ServerPlayer)) return;
            ServerPlayer player = (ServerPlayer)patt0$temp;
            if (player.getServer() == null) {
                return;
            }
            ItemStack stack = C2STargetBiomePacket.getHeldGun(player);
            if (stack.isEmpty()) {
                return;
            }
            if (!player.getAbilities().instabuild && !PortalGunItem.hasUpgrade((ItemStack)stack, (UpgradeType)UpgradeType.BIOME_LOCATOR)) {
                ModMessages.sendToPlayer((CustomPacketPayload)new S2CBiomeFoundPacket(false, C2STargetBiomePacket.formatBiomeName(packet.biomeId()), 0.0, 0.0, 0.0, packet.targetDimension()), (ServerPlayer)player);
                player.playNotifySound((SoundEvent)ModSounds.GUN_MALFUNCTION.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
                return;
            }
            ResourceLocation dimLoc = ResourceLocation.tryParse((String)packet.targetDimension());
            ResourceKey dimKey = dimLoc != null ? ResourceKey.create((ResourceKey)Registries.DIMENSION, (ResourceLocation)dimLoc) : Level.OVERWORLD;
            ServerLevel targetLevel = player.getServer().getLevel(dimKey);
            if (targetLevel == null) {
                targetLevel = player.serverLevel();
            }
            String currentDimStr = player.level().dimension().location().toString();
            String targetDimStr = targetLevel.dimension().location().toString();
            if (!(targetDimStr.equals(currentDimStr) || player.getAbilities().instabuild || PortalGunItem.hasUpgrade((ItemStack)stack, (UpgradeType)UpgradeType.DIMENSIONAL))) {
                ModMessages.sendToPlayer((CustomPacketPayload)new S2CBiomeFoundPacket(false, C2STargetBiomePacket.formatBiomeName(packet.biomeId()), 0.0, 0.0, 0.0, packet.targetDimension()), (ServerPlayer)player);
                player.playNotifySound((SoundEvent)ModSounds.GUN_MALFUNCTION.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
                return;
            }
            ResourceLocation biomeLoc = ResourceLocation.tryParse((String)packet.biomeId());
            if (biomeLoc == null) {
                ModMessages.sendToPlayer((CustomPacketPayload)new S2CBiomeFoundPacket(false, C2STargetBiomePacket.formatBiomeName(packet.biomeId()), 0.0, 0.0, 0.0, packet.targetDimension()), (ServerPlayer)player);
                player.playNotifySound((SoundEvent)ModSounds.GUN_MALFUNCTION.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
                return;
            }
            ResourceKey biomeKey = ResourceKey.create((ResourceKey)Registries.BIOME, (ResourceLocation)biomeLoc);
            ServerLevel searchLevel = targetLevel;
            BlockPos searchOrigin = searchLevel == player.serverLevel() ? player.blockPosition() : searchLevel.getSharedSpawnPos();
            CompletableFuture.supplyAsync(() -> {
                try {
                    return searchLevel.findClosestBiome3d(holder -> holder.is(biomeKey), searchOrigin, 6400, 32, 64);
                }
                catch (Exception e) {
                    return null;
                }
            }).thenAcceptAsync(pair -> {
                if (pair != null && pair.getFirst() != null) {
                    int preferredY;
                    BlockPos foundPos = (BlockPos)pair.getFirst();
                    int candidateX = foundPos.getX();
                    int candidateZ = foundPos.getZ();
                    boolean hasCeiling = searchLevel.dimensionType().hasCeiling();
                    if (!hasCeiling) {
                        int surfaceY = searchLevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, candidateX, candidateZ);
                        preferredY = foundPos.getY() < surfaceY - 20 ? Math.max(searchLevel.getMinBuildHeight() + 2, foundPos.getY()) : (surfaceY > searchLevel.getMinBuildHeight() + 2 ? surfaceY : 64);
                    } else {
                        int minY = searchLevel.getMinBuildHeight() + 4;
                        int maxY = searchLevel.getLogicalHeight() - 10;
                        preferredY = Math.max(minY, Math.min(maxY, foundPos.getY()));
                    }
                    BlockPos candidatePos = new BlockPos(candidateX, preferredY, candidateZ);
                    Vec3 safeVec = PortalPlacementHelper.findSafePlacement((ServerLevel)searchLevel, (Vec3)new Vec3((double)candidatePos.getX() + 0.5, (double)candidatePos.getY(), (double)candidatePos.getZ() + 0.5));
                    CompoundTag locTag = new CompoundTag();
                    locTag.putDouble("X", (double)Math.round(safeVec.x * 10.0) / 10.0);
                    locTag.putDouble("Y", (double)Math.round(safeVec.y * 10.0) / 10.0);
                    locTag.putDouble("Z", (double)Math.round(safeVec.z * 10.0) / 10.0);
                    locTag.putString("Dimension", searchLevel.dimension().location().toString());
                    locTag.putString("RawText", packet.targetDimension());
                    String biomeName = C2STargetBiomePacket.formatBiomeName(packet.biomeId());
                    locTag.putString("Name", "Biome: " + biomeName);
                    locTag.putString("BiomeId", packet.biomeId());
                    PortalGunItem.setCustomLocation((ItemStack)stack, (CompoundTag)locTag);
                    PortalGunItem.setMode((ItemStack)stack, (PortalMode)PortalMode.CUSTOM);
                    player.containerMenu.broadcastChanges();
                    player.inventoryMenu.broadcastChanges();
                    player.playNotifySound((SoundEvent)ModSounds.UI_DIAL.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
                    ModMessages.sendToPlayer((CustomPacketPayload)new S2CBiomeFoundPacket(true, biomeName, safeVec.x, safeVec.y, safeVec.z, searchLevel.dimension().location().toString()), (ServerPlayer)player);
                } else {
                    player.playNotifySound((SoundEvent)ModSounds.GUN_MALFUNCTION.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
                    ModMessages.sendToPlayer((CustomPacketPayload)new S2CBiomeFoundPacket(false, C2STargetBiomePacket.formatBiomeName(packet.biomeId()), 0.0, 0.0, 0.0, packet.targetDimension()), (ServerPlayer)player);
                }
            }, (Executor)player.getServer());
        });
    }

    public static String formatBiomeName(String biomeId) {
        if (biomeId == null) return "Biome";
        if (biomeId.isEmpty()) {
            return "Biome";
        }
        String path = biomeId.contains(":") ? biomeId.substring(biomeId.indexOf(58) + 1) : biomeId;
        String[] words = path.split("_");
        StringBuilder sb = new StringBuilder();
        String[] stringArray = words;
        int n = stringArray.length;
        int n2 = 0;
        while (n2 < n) {
            String w = stringArray[n2];
            if (!w.isEmpty()) {
                sb.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1)).append(" ");
            }
            ++n2;
        }
        return sb.toString().trim();
    }

    private static ItemStack getHeldGun(ServerPlayer player) {
        ItemStack main = player.getMainHandItem();
        if (C2STargetBiomePacket.isGun(main)) {
            return main;
        }
        ItemStack off = player.getOffhandItem();
        if (!C2STargetBiomePacket.isGun(off)) return ItemStack.EMPTY;
        return off;
    }

    private static boolean isGun(ItemStack stack) {
        return stack != null && !stack.isEmpty() && (stack.getItem() instanceof PortalGunItem || stack.getItem() instanceof DischargedPortalGunItem);
    }
}

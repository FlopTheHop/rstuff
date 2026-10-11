/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.ExtraTortik.ricks_stuff.block.entity.PhoenixCapsuleBlockEntity
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.block.entity.PhoenixCapsuleBlockEntity;

public record C2SRepairCapsulePacket(BlockPos pos) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2SRepairCapsulePacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"repair_capsule"));
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SRepairCapsulePacket> STREAM_CODEC = StreamCodec.ofMember(C2SRepairCapsulePacket::write, C2SRepairCapsulePacket::new);

    public C2SRepairCapsulePacket(RegistryFriendlyByteBuf buf) {
        this(buf.readBlockPos());
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(C2SRepairCapsulePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player patt0$temp = context.player();
            if (!(patt0$temp instanceof ServerPlayer)) return;
            ServerPlayer player = (ServerPlayer)patt0$temp;
            if (!player.level().isLoaded(packet.pos())) return;
            if (!(player.distanceToSqr((double)packet.pos().getX() + 0.5, (double)packet.pos().getY() + 0.5, (double)packet.pos().getZ() + 0.5) <= 64.0)) return;
            BlockEntity be = player.level().getBlockEntity(packet.pos());
            if (!(be instanceof PhoenixCapsuleBlockEntity)) return;
            PhoenixCapsuleBlockEntity capsule = (PhoenixCapsuleBlockEntity)be;
            capsule.repairCapsule(player);
        });
    }
}

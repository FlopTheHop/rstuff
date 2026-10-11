/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity;

public record C2SInitiateWarpPacket(double targetX, double targetY, double targetZ, String targetDimension) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2SInitiateWarpPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"initiate_warp"));
    public static final StreamCodec<FriendlyByteBuf, C2SInitiateWarpPacket> STREAM_CODEC = StreamCodec.ofMember(C2SInitiateWarpPacket::write, C2SInitiateWarpPacket::new);

    public C2SInitiateWarpPacket(FriendlyByteBuf buf) {
        this(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readUtf());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeDouble(this.targetX);
        buf.writeDouble(this.targetY);
        buf.writeDouble(this.targetZ);
        buf.writeUtf(this.targetDimension != null ? this.targetDimension : "");
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(C2SInitiateWarpPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player patt0$temp = context.player();
            if (!(patt0$temp instanceof ServerPlayer)) return;
            ServerPlayer player = (ServerPlayer)patt0$temp;
            Entity patt1$temp = player.getVehicle();
            if (!(patt1$temp instanceof SpaceCruiserEntity)) return;
            SpaceCruiserEntity ship = (SpaceCruiserEntity)patt1$temp;
            if (ship.getControllingPassenger() != player) return;
            ship.initiateWarp(packet.targetX(), packet.targetY(), packet.targetZ(), packet.targetDimension());
        });
    }
}

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

public record C2SAbortWarpPacket() implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2SAbortWarpPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"abort_warp"));
    public static final StreamCodec<FriendlyByteBuf, C2SAbortWarpPacket> STREAM_CODEC = StreamCodec.unit((Object)((Object)new C2SAbortWarpPacket()));

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(C2SAbortWarpPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player patt0$temp = context.player();
            if (!(patt0$temp instanceof ServerPlayer)) return;
            ServerPlayer player = (ServerPlayer)patt0$temp;
            Entity patt1$temp = player.getVehicle();
            if (!(patt1$temp instanceof SpaceCruiserEntity)) return;
            SpaceCruiserEntity ship = (SpaceCruiserEntity)patt1$temp;
            if (ship.getControllingPassenger() != player) return;
            ship.abortWarp("PILOT_INTERRUPT");
        });
    }
}

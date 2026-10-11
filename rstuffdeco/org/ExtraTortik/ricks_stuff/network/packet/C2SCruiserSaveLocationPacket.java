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

public record C2SCruiserSaveLocationPacket(int entityId, String name, double x, double y, double z, String dimension) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2SCruiserSaveLocationPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"cruiser_save_location"));
    public static final StreamCodec<FriendlyByteBuf, C2SCruiserSaveLocationPacket> STREAM_CODEC = StreamCodec.ofMember(C2SCruiserSaveLocationPacket::write, C2SCruiserSaveLocationPacket::new);

    public C2SCruiserSaveLocationPacket(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readUtf(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readUtf());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeUtf(this.name != null ? this.name : "");
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeUtf(this.dimension != null ? this.dimension : "minecraft:overworld");
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(C2SCruiserSaveLocationPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            boolean isNear;
            Player patt0$temp = context.player();
            if (!(patt0$temp instanceof ServerPlayer)) return;
            ServerPlayer player = (ServerPlayer)patt0$temp;
            if (player.level() == null) return;
            Entity targetEntity = player.level().getEntity(packet.entityId());
            if (!(targetEntity instanceof SpaceCruiserEntity)) return;
            SpaceCruiserEntity ship = (SpaceCruiserEntity)targetEntity;
            boolean isRiding = player.getVehicle() == ship;
            boolean bl = isNear = player.distanceToSqr((Entity)ship) < 64.0;
            if (!isRiding) {
                if (!isNear) return;
            }
            ship.addSavedWaypoint(packet.name(), packet.x(), packet.y(), packet.z(), packet.dimension());
        });
    }
}

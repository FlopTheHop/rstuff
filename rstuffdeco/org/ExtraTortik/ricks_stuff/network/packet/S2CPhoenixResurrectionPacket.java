/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CPhoenixResurrectionPacket$ClientHandler
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.network.packet.S2CPhoenixResurrectionPacket;

/*
 * Exception performing whole class analysis ignored.
 */
public record S2CPhoenixResurrectionPacket(long duration, int catalystType, boolean isCorrupted) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<S2CPhoenixResurrectionPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"phoenix_resurrection"));
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CPhoenixResurrectionPacket> STREAM_CODEC = StreamCodec.ofMember(S2CPhoenixResurrectionPacket::write, S2CPhoenixResurrectionPacket::new);

    public S2CPhoenixResurrectionPacket(RegistryFriendlyByteBuf buf) {
        this(buf.readLong(), buf.readInt(), buf.readBoolean());
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeLong(this.duration);
        buf.writeInt(this.catalystType);
        buf.writeBoolean(this.isCorrupted);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(S2CPhoenixResurrectionPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientHandler.trigger((long)packet.duration(), (int)packet.catalystType(), (boolean)packet.isCorrupted()));
    }
}

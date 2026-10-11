/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CPuppetDisconnectPacket$ClientHandler
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.network.packet.S2CPuppetDisconnectPacket;

/*
 * Exception performing whole class analysis ignored.
 */
public record S2CPuppetDisconnectPacket(int mobId) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<S2CPuppetDisconnectPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"s2c_puppet_disconnect"));
    public static final StreamCodec<FriendlyByteBuf, S2CPuppetDisconnectPacket> STREAM_CODEC = StreamCodec.ofMember(S2CPuppetDisconnectPacket::write, S2CPuppetDisconnectPacket::new);

    public S2CPuppetDisconnectPacket(FriendlyByteBuf buf) {
        this(buf.readInt());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.mobId);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(S2CPuppetDisconnectPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientHandler.handle());
    }
}

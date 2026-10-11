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
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CGunRecoilPacket$ClientHandler
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.network.packet.S2CGunRecoilPacket;

/*
 * Exception performing whole class analysis ignored.
 */
public record S2CGunRecoilPacket(int entityId, byte handOrdinal) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<S2CGunRecoilPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"s2c_gun_recoil"));
    public static final StreamCodec<FriendlyByteBuf, S2CGunRecoilPacket> STREAM_CODEC = StreamCodec.ofMember(S2CGunRecoilPacket::write, S2CGunRecoilPacket::new);

    public S2CGunRecoilPacket(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readByte());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeByte(this.handOrdinal);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(S2CGunRecoilPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientHandler.handle((int)packet.entityId(), (byte)packet.handOrdinal()));
    }
}

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
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CSyncPlayerScalePacket$ClientHandler
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.network.packet.S2CSyncPlayerScalePacket;

/*
 * Exception performing whole class analysis ignored.
 */
public record S2CSyncPlayerScalePacket(int entityId, boolean isTiny) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<S2CSyncPlayerScalePacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"sync_player_scale"));
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CSyncPlayerScalePacket> STREAM_CODEC = StreamCodec.ofMember(S2CSyncPlayerScalePacket::write, S2CSyncPlayerScalePacket::new);

    public S2CSyncPlayerScalePacket(RegistryFriendlyByteBuf buf) {
        this(buf.readInt(), buf.readBoolean());
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeBoolean(this.isTiny);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(S2CSyncPlayerScalePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientHandler.handle((int)packet.entityId(), (boolean)packet.isTiny()));
    }
}

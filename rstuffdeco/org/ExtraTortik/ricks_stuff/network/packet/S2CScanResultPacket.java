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
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CScanResultPacket$ClientHandler
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.network.packet.S2CScanResultPacket;

/*
 * Exception performing whole class analysis ignored.
 */
public record S2CScanResultPacket(int targetEntityId, String predictionText, int durationTicks, boolean success) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<S2CScanResultPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"s2c_scan_result"));
    public static final StreamCodec<FriendlyByteBuf, S2CScanResultPacket> STREAM_CODEC = StreamCodec.ofMember(S2CScanResultPacket::write, S2CScanResultPacket::new);

    public S2CScanResultPacket(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readUtf(), buf.readInt(), buf.readBoolean());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.targetEntityId);
        buf.writeUtf(this.predictionText != null ? this.predictionText : "");
        buf.writeInt(this.durationTicks);
        buf.writeBoolean(this.success);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(S2CScanResultPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientHandler.handle((S2CScanResultPacket)packet));
    }
}

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
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CSyncWarpSequencePacket$ClientHandler
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.network.packet.S2CSyncWarpSequencePacket;

/*
 * Exception performing whole class analysis ignored.
 */
public record S2CSyncWarpSequencePacket(int entityId, int warpState, int warpTimer, double targetX, double targetY, double targetZ, String targetDimension, @Nullable UUID pilotUUID, @Nullable UUID passengerUUID) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<S2CSyncWarpSequencePacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"sync_warp_sequence"));
    public static final StreamCodec<FriendlyByteBuf, S2CSyncWarpSequencePacket> STREAM_CODEC = StreamCodec.ofMember(S2CSyncWarpSequencePacket::write, S2CSyncWarpSequencePacket::new);

    public S2CSyncWarpSequencePacket(int entityId, int warpState, int warpTimer, double targetX, double targetY, double targetZ, String targetDimension) {
        this(entityId, warpState, warpTimer, targetX, targetY, targetZ, targetDimension, null, null);
    }

    public S2CSyncWarpSequencePacket(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readInt(), buf.readInt(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readUtf(), buf.readBoolean() ? buf.readUUID() : null, buf.readBoolean() ? buf.readUUID() : null);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeInt(this.warpState);
        buf.writeInt(this.warpTimer);
        buf.writeDouble(this.targetX);
        buf.writeDouble(this.targetY);
        buf.writeDouble(this.targetZ);
        buf.writeUtf(this.targetDimension != null ? this.targetDimension : "");
        buf.writeBoolean(this.pilotUUID != null);
        if (this.pilotUUID != null) {
            buf.writeUUID(this.pilotUUID);
        }
        buf.writeBoolean(this.passengerUUID != null);
        if (this.passengerUUID == null) return;
        buf.writeUUID(this.passengerUUID);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(S2CSyncWarpSequencePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientHandler.handle((S2CSyncWarpSequencePacket)packet));
    }
}

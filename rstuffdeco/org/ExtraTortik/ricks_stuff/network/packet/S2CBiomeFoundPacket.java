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
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CBiomeFoundPacket$ClientHandler
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.network.packet.S2CBiomeFoundPacket;

/*
 * Exception performing whole class analysis ignored.
 */
public record S2CBiomeFoundPacket(boolean success, String biomeName, double x, double y, double z, String dimension) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<S2CBiomeFoundPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"biome_found"));
    public static final StreamCodec<FriendlyByteBuf, S2CBiomeFoundPacket> STREAM_CODEC = StreamCodec.ofMember(S2CBiomeFoundPacket::write, S2CBiomeFoundPacket::new);

    public S2CBiomeFoundPacket(FriendlyByteBuf buf) {
        this(buf.readBoolean(), buf.readUtf(128), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readUtf(128));
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(this.success);
        buf.writeUtf(this.biomeName != null ? this.biomeName : "", 128);
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeUtf(this.dimension != null ? this.dimension : "minecraft:overworld", 128);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(S2CBiomeFoundPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientHandler.handle((S2CBiomeFoundPacket)packet));
    }
}

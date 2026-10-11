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
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CTeleportOverlayPacket$ClientHandler
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.network.packet.S2CTeleportOverlayPacket;

/*
 * Exception performing whole class analysis ignored.
 */
public record S2CTeleportOverlayPacket(int color, long duration) implements CustomPacketPayload
{
    public static final int COLOR_WHITE_FLASH = 99;
    public static final CustomPacketPayload.Type<S2CTeleportOverlayPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"teleport_overlay"));
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CTeleportOverlayPacket> STREAM_CODEC = StreamCodec.ofMember(S2CTeleportOverlayPacket::write, S2CTeleportOverlayPacket::new);

    public S2CTeleportOverlayPacket(int color) {
        this(color, 600L);
    }

    public S2CTeleportOverlayPacket(RegistryFriendlyByteBuf buf) {
        this(buf.readInt(), buf.readLong());
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(this.color);
        buf.writeLong(this.duration);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(S2CTeleportOverlayPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientHandler.trigger((int)packet.color(), (long)packet.duration()));
    }
}

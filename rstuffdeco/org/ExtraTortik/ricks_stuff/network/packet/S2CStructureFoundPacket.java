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
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CStructureFoundPacket$ClientHandler
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.network.packet.S2CStructureFoundPacket;

/*
 * Exception performing whole class analysis ignored.
 */
public record S2CStructureFoundPacket(boolean success, String structureName, double x, double y, double z, String dimension) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<S2CStructureFoundPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"structure_found"));
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CStructureFoundPacket> STREAM_CODEC = StreamCodec.ofMember(S2CStructureFoundPacket::write, S2CStructureFoundPacket::new);

    public S2CStructureFoundPacket(boolean success, String structureName, double x, double y, double z, String dimension) {
        this.success = success;
        this.structureName = structureName != null ? structureName : "";
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension != null ? dimension : "minecraft:overworld";
    }

    public S2CStructureFoundPacket(RegistryFriendlyByteBuf buf) {
        this(buf.readBoolean(), buf.readUtf(128), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readUtf(128));
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeBoolean(this.success);
        buf.writeUtf(this.structureName, 128);
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeUtf(this.dimension, 128);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(S2CStructureFoundPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientHandler.handle((boolean)packet.success(), (String)packet.structureName(), (double)packet.x(), (double)packet.y(), (double)packet.z(), (String)packet.dimension()));
    }
}

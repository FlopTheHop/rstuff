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
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CPuppetListPacket$ClientHandler
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CPuppetListPacket$PuppetEntryData
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.network.packet.S2CPuppetListPacket;

/*
 * Exception performing whole class analysis ignored.
 */
public record S2CPuppetListPacket(List<PuppetEntryData> entries) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<S2CPuppetListPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"s2c_puppet_list"));
    public static final StreamCodec<FriendlyByteBuf, S2CPuppetListPacket> STREAM_CODEC = StreamCodec.ofMember(S2CPuppetListPacket::write, S2CPuppetListPacket::new);

    public S2CPuppetListPacket(FriendlyByteBuf buf) {
        this(S2CPuppetListPacket.readEntries(buf));
    }

    private static List<PuppetEntryData> readEntries(FriendlyByteBuf buf) {
        int count = buf.readInt();
        ArrayList<PuppetEntryData> list = new ArrayList<PuppetEntryData>(count);
        int i = 0;
        while (i < count) {
            list.add(new PuppetEntryData(buf.readInt(), buf.readUUID(), buf.readUtf(256), buf.readFloat(), buf.readFloat(), buf.readDouble(), buf.readDouble(), buf.readDouble()));
            ++i;
        }
        return list;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entries.size());
        Iterator<PuppetEntryData> iterator = this.entries.iterator();
        while (iterator.hasNext()) {
            PuppetEntryData e = iterator.next();
            buf.writeInt(e.id());
            buf.writeUUID(e.uuid());
            buf.writeUtf(e.name(), 256);
            buf.writeFloat(e.health());
            buf.writeFloat(e.maxHealth());
            buf.writeDouble(e.x());
            buf.writeDouble(e.y());
            buf.writeDouble(e.z());
        }
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(S2CPuppetListPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientHandler.handle((S2CPuppetListPacket)packet));
    }
}

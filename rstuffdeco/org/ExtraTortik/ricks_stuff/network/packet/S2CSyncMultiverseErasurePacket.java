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
 *  org.ExtraTortik.ricks_stuff.entity.omega.MultiverseErasureSavedData
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CSyncMultiverseErasurePacket$ClientHandler
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.entity.omega.MultiverseErasureSavedData;
import org.ExtraTortik.ricks_stuff.network.packet.S2CSyncMultiverseErasurePacket;

/*
 * Exception performing whole class analysis ignored.
 */
public record S2CSyncMultiverseErasurePacket(Set<String> erasedItemTypes, Set<String> erasedEntityTypes, Set<UUID> erasedUUIDs) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<S2CSyncMultiverseErasurePacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"sync_multiverse_erasure"));
    public static final StreamCodec<FriendlyByteBuf, S2CSyncMultiverseErasurePacket> STREAM_CODEC = StreamCodec.ofMember(S2CSyncMultiverseErasurePacket::write, S2CSyncMultiverseErasurePacket::new);

    public S2CSyncMultiverseErasurePacket(MultiverseErasureSavedData storage) {
        this(storage != null ? storage.getErasedItemTypes() : null, storage != null ? storage.getErasedEntityTypes() : null, storage != null ? storage.getErasedUUIDs() : null);
    }

    public S2CSyncMultiverseErasurePacket(FriendlyByteBuf buf) {
        this((Set)buf.readCollection(HashSet::new, FriendlyByteBuf::readUtf), (Set)buf.readCollection(HashSet::new, FriendlyByteBuf::readUtf), (Set)buf.readCollection(HashSet::new, b -> b.readUUID()));
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeCollection(this.erasedItemTypes != null ? this.erasedItemTypes : Set.of(), FriendlyByteBuf::writeUtf);
        buf.writeCollection(this.erasedEntityTypes != null ? this.erasedEntityTypes : Set.of(), FriendlyByteBuf::writeUtf);
        buf.writeCollection(this.erasedUUIDs != null ? this.erasedUUIDs : Set.of(), (b, u) -> b.writeUUID(u));
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(S2CSyncMultiverseErasurePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientHandler.handle((S2CSyncMultiverseErasurePacket)packet));
    }
}

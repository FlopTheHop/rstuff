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
 *  org.ExtraTortik.ricks_stuff.entity.CruiserWaypoint
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CSyncCruiserLocationsPacket$ClientHandler
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
import org.ExtraTortik.ricks_stuff.entity.CruiserWaypoint;
import org.ExtraTortik.ricks_stuff.network.packet.S2CSyncCruiserLocationsPacket;

/*
 * Exception performing whole class analysis ignored.
 */
public record S2CSyncCruiserLocationsPacket(int entityId, List<CruiserWaypoint> waypoints) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<S2CSyncCruiserLocationsPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"sync_cruiser_locations"));
    public static final StreamCodec<FriendlyByteBuf, S2CSyncCruiserLocationsPacket> STREAM_CODEC = StreamCodec.ofMember(S2CSyncCruiserLocationsPacket::write, S2CSyncCruiserLocationsPacket::new);

    public S2CSyncCruiserLocationsPacket(int entityId, List<CruiserWaypoint> waypoints) {
        this.entityId = entityId;
        this.waypoints = waypoints != null ? new ArrayList<CruiserWaypoint>(waypoints) : new ArrayList();
    }

    public S2CSyncCruiserLocationsPacket(FriendlyByteBuf buf) {
        this(buf.readInt(), S2CSyncCruiserLocationsPacket.readWaypointsList(buf));
    }

    private static List<CruiserWaypoint> readWaypointsList(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        ArrayList<CruiserWaypoint> list = new ArrayList<CruiserWaypoint>(size);
        int i = 0;
        while (i < size) {
            list.add(CruiserWaypoint.readFromBuf((FriendlyByteBuf)buf));
            ++i;
        }
        return list;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeVarInt(this.waypoints.size());
        Iterator<CruiserWaypoint> iterator = this.waypoints.iterator();
        while (iterator.hasNext()) {
            CruiserWaypoint waypoint = iterator.next();
            waypoint.writeToBuf(buf);
        }
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(S2CSyncCruiserLocationsPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientHandler.handle((S2CSyncCruiserLocationsPacket)packet));
    }
}

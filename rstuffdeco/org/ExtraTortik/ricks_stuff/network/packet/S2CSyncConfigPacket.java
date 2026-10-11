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
 *  org.ExtraTortik.ricks_stuff.Config
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.Config;

public record S2CSyncConfigPacket(boolean allowPlayerTargeting, int maxExpansionCards, int portalMaxLifetimeTicks, boolean allowCrossDimensionTeleport, boolean allowEntityTeleportation, double portalGunMaxDistance, boolean allowPlayerTrackingAcrossDims, double spaceCruiserMaxSpeedMultiplier, boolean spaceCruiserBlockBreaking, boolean selfDestructGunEnabled, boolean phoenixProtocolEnabled, int phoenixPrintTimeSeconds, List<String> dimensionBlacklist) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<S2CSyncConfigPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"sync_config"));
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CSyncConfigPacket> STREAM_CODEC = StreamCodec.ofMember(S2CSyncConfigPacket::write, S2CSyncConfigPacket::new);

    public S2CSyncConfigPacket(RegistryFriendlyByteBuf buf) {
        this(buf.readBoolean(), buf.readVarInt(), buf.readVarInt(), buf.readBoolean(), buf.readBoolean(), buf.readDouble(), buf.readBoolean(), buf.readDouble(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readVarInt(), S2CSyncConfigPacket.readStringList(buf));
    }

    public static S2CSyncConfigPacket createFromCurrent() {
        return new S2CSyncConfigPacket(Config.allowPlayerTargeting, Config.maxExpansionCards, Config.portalMaxLifetimeTicks, Config.allowCrossDimensionTeleport, Config.allowEntityTeleportation, Config.portalGunMaxDistance, Config.allowPlayerTrackingAcrossDims, Config.spaceCruiserMaxSpeedMultiplier, Config.spaceCruiserBlockBreaking, Config.selfDestructGunEnabled, Config.phoenixProtocolEnabled, Config.phoenixPrintTimeSeconds, Config.dimensionBlacklist);
    }

    private static List<String> readStringList(RegistryFriendlyByteBuf buf) {
        int listSize = buf.readVarInt();
        ArrayList<String> list = new ArrayList<String>(listSize);
        int i = 0;
        while (i < listSize) {
            list.add(buf.readUtf(256));
            ++i;
        }
        return list;
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeBoolean(this.allowPlayerTargeting);
        buf.writeVarInt(this.maxExpansionCards);
        buf.writeVarInt(this.portalMaxLifetimeTicks);
        buf.writeBoolean(this.allowCrossDimensionTeleport);
        buf.writeBoolean(this.allowEntityTeleportation);
        buf.writeDouble(this.portalGunMaxDistance);
        buf.writeBoolean(this.allowPlayerTrackingAcrossDims);
        buf.writeDouble(this.spaceCruiserMaxSpeedMultiplier);
        buf.writeBoolean(this.spaceCruiserBlockBreaking);
        buf.writeBoolean(this.selfDestructGunEnabled);
        buf.writeBoolean(this.phoenixProtocolEnabled);
        buf.writeVarInt(this.phoenixPrintTimeSeconds);
        List<String> list = this.dimensionBlacklist != null ? this.dimensionBlacklist : List.of();
        buf.writeVarInt(list.size());
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            buf.writeUtf(s != null ? s : "");
        }
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(S2CSyncConfigPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Config.allowPlayerTargeting = packet.allowPlayerTargeting();
            Config.maxExpansionCards = packet.maxExpansionCards();
            Config.portalMaxLifetimeTicks = packet.portalMaxLifetimeTicks();
            Config.allowCrossDimensionTeleport = packet.allowCrossDimensionTeleport();
            Config.allowEntityTeleportation = packet.allowEntityTeleportation();
            Config.portalGunMaxDistance = packet.portalGunMaxDistance();
            Config.allowPlayerTrackingAcrossDims = packet.allowPlayerTrackingAcrossDims();
            Config.spaceCruiserMaxSpeedMultiplier = packet.spaceCruiserMaxSpeedMultiplier();
            Config.spaceCruiserBlockBreaking = packet.spaceCruiserBlockBreaking();
            Config.selfDestructGunEnabled = packet.selfDestructGunEnabled();
            Config.phoenixProtocolEnabled = packet.phoenixProtocolEnabled();
            Config.phoenixPrintTimeSeconds = packet.phoenixPrintTimeSeconds();
            Config.dimensionBlacklist = new ArrayList<String>(packet.dimensionBlacklist());
        });
    }
}

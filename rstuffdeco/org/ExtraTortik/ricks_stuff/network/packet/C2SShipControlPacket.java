/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity;
import org.ExtraTortik.ricks_stuff.init.ModSounds;

public record C2SShipControlPacket(float throttle, float strafe, float vertical, boolean boosting, float yaw, float pitch, float roll, double posX, double posY, double posZ, double velX, double velY, double velZ, boolean toggleHeadlights, boolean toggleHoverLock, boolean toggleEngine) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2SShipControlPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"ship_control"));
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SShipControlPacket> STREAM_CODEC = StreamCodec.ofMember(C2SShipControlPacket::write, C2SShipControlPacket::new);

    public C2SShipControlPacket(RegistryFriendlyByteBuf buf) {
        this(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readBoolean(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeFloat(this.throttle);
        buf.writeFloat(this.strafe);
        buf.writeFloat(this.vertical);
        buf.writeBoolean(this.boosting);
        buf.writeFloat(this.yaw);
        buf.writeFloat(this.pitch);
        buf.writeFloat(this.roll);
        buf.writeDouble(this.posX);
        buf.writeDouble(this.posY);
        buf.writeDouble(this.posZ);
        buf.writeDouble(this.velX);
        buf.writeDouble(this.velY);
        buf.writeDouble(this.velZ);
        buf.writeBoolean(this.toggleHeadlights);
        buf.writeBoolean(this.toggleHoverLock);
        buf.writeBoolean(this.toggleEngine);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(C2SShipControlPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player patt0$temp = context.player();
            if (!(patt0$temp instanceof ServerPlayer)) return;
            ServerPlayer player = (ServerPlayer)patt0$temp;
            Entity patt1$temp = player.getVehicle();
            if (!(patt1$temp instanceof SpaceCruiserEntity)) return;
            SpaceCruiserEntity ship = (SpaceCruiserEntity)patt1$temp;
            if (ship.getControllingPassenger() != player) return;
            ship.updatePilotState(packet.throttle(), packet.strafe(), packet.vertical(), packet.boosting(), packet.yaw(), packet.pitch(), packet.roll(), packet.posX(), packet.posY(), packet.posZ(), packet.velX(), packet.velY(), packet.velZ());
            if (packet.toggleHeadlights()) {
                ship.toggleHeadlights();
            }
            if (packet.toggleHoverLock()) {
                ship.setHoverLock(!ship.isHoverLock());
            }
            if (!packet.toggleEngine()) return;
            if (ship.isBroken() || ship.isCrashing() || ship.getHealth() <= 0.0f) {
                ship.setEngineOn(false);
                player.level().playSound(null, ship.getX(), ship.getY(), ship.getZ(), (SoundEvent)ModSounds.SPACE_CRUISER_LOW_ENERGY.get(), SoundSource.NEUTRAL, 0.9f, 1.0f);
            } else {
                boolean wasOn = ship.isEngineOn();
                ship.toggleEngine();
                boolean nowOn = ship.isEngineOn();
                if (wasOn == nowOn) return;
                player.level().playSound(null, ship.getX(), ship.getY(), ship.getZ(), nowOn ? (SoundEvent)ModSounds.SPACE_CRUISER_STARTUP.get() : (SoundEvent)ModSounds.SPACE_CRUISER_SHUTDOWN.get(), SoundSource.NEUTRAL, 1.0f, 1.0f);
            }
        });
    }
}

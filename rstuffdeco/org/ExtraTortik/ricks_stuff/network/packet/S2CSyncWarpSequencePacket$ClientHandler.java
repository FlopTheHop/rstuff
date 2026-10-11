/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.world.entity.Entity
 *  org.ExtraTortik.ricks_stuff.client.SpaceCruiserClientHandler
 *  org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CSyncWarpSequencePacket
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.ExtraTortik.ricks_stuff.client.SpaceCruiserClientHandler;
import org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity;
import org.ExtraTortik.ricks_stuff.network.packet.S2CSyncWarpSequencePacket;

private static class S2CSyncWarpSequencePacket.ClientHandler {
    private S2CSyncWarpSequencePacket.ClientHandler() {
    }

    private static void handle(S2CSyncWarpSequencePacket packet) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            Entity entity = mc.level.getEntity(packet.entityId());
            if (entity instanceof SpaceCruiserEntity) {
                SpaceCruiserEntity ship = (SpaceCruiserEntity)entity;
                ship.handleWarpSync(packet.warpState(), packet.warpTimer(), packet.targetX(), packet.targetY(), packet.targetZ(), packet.targetDimension());
                if (packet.pilotUUID() != null) {
                    ship.setWarpPilotUUID(packet.pilotUUID());
                }
                if (packet.passengerUUID() != null) {
                    ship.setWarpPassengerUUID(packet.passengerUUID());
                }
                if (mc.player == null) return;
                if (packet.warpState() == 0) return;
                boolean isAssignedRider = packet.pilotUUID() != null && packet.pilotUUID().equals(mc.player.getUUID()) || packet.passengerUUID() != null && packet.passengerUUID().equals(mc.player.getUUID());
                if (!isAssignedRider) return;
                if (mc.player.isPassenger()) {
                    if (mc.player.getVehicle() == ship) return;
                }
                if (!((double)mc.player.distanceTo((Entity)ship) < 32.0)) return;
                mc.player.startRiding((Entity)ship, true);
            } else {
                SpaceCruiserClientHandler.queuePendingWarpSync((int)packet.entityId(), (int)packet.warpState(), (int)packet.warpTimer(), (double)packet.targetX(), (double)packet.targetY(), (double)packet.targetZ(), (String)packet.targetDimension(), (UUID)packet.pilotUUID(), (UUID)packet.passengerUUID());
            }
        } else {
            SpaceCruiserClientHandler.queuePendingWarpSync((int)packet.entityId(), (int)packet.warpState(), (int)packet.warpTimer(), (double)packet.targetX(), (double)packet.targetY(), (double)packet.targetZ(), (String)packet.targetDimension(), (UUID)packet.pilotUUID(), (UUID)packet.passengerUUID());
        }
    }
}

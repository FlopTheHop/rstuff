/*
 * Decompiled with CFR 0.152.
 */
package org.ExtraTortik.ricks_stuff.client;

import java.util.UUID;

public static class SpaceCruiserClientHandler.PendingWarpSync {
    public final int entityId;
    public final int warpState;
    public final int warpTimer;
    public final double targetX;
    public final double targetY;
    public final double targetZ;
    public final String targetDimension;
    public final UUID pilotUUID;
    public final UUID passengerUUID;
    public int ticksRemaining;

    public SpaceCruiserClientHandler.PendingWarpSync(int entityId, int warpState, int warpTimer, double targetX, double targetY, double targetZ, String targetDimension, UUID pilotUUID, UUID passengerUUID) {
        this.entityId = entityId;
        this.warpState = warpState;
        this.warpTimer = warpTimer;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
        this.targetDimension = targetDimension;
        this.pilotUUID = pilotUUID;
        this.passengerUUID = passengerUUID;
        this.ticksRemaining = 80;
    }
}

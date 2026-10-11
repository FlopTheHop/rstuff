/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.ExtraTortik.ricks_stuff.client.overlay.PhoenixResurrectionOverlay
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import org.ExtraTortik.ricks_stuff.client.overlay.PhoenixResurrectionOverlay;

private static class S2CPhoenixResurrectionPacket.ClientHandler {
    private S2CPhoenixResurrectionPacket.ClientHandler() {
    }

    private static void trigger(long duration, int catalystType, boolean isCorrupted) {
        PhoenixResurrectionOverlay.trigger((long)duration, (int)catalystType, (boolean)isCorrupted);
    }
}

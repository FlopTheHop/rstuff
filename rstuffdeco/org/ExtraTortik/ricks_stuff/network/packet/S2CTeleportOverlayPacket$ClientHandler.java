/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.ExtraTortik.ricks_stuff.client.overlay.PortalFadeOverlay
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import org.ExtraTortik.ricks_stuff.client.overlay.PortalFadeOverlay;

private static class S2CTeleportOverlayPacket.ClientHandler {
    private S2CTeleportOverlayPacket.ClientHandler() {
    }

    private static void trigger(int color, long duration) {
        PortalFadeOverlay.trigger((int)color, (long)duration);
    }
}

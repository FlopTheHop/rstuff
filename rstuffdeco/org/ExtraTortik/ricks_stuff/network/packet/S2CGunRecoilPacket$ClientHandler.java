/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  org.ExtraTortik.ricks_stuff.client.PortalGunRecoilManager
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.client.Minecraft;
import org.ExtraTortik.ricks_stuff.client.PortalGunRecoilManager;

private static class S2CGunRecoilPacket.ClientHandler {
    private S2CGunRecoilPacket.ClientHandler() {
    }

    private static void handle(int entityId, byte handOrdinal) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.player.getId() == entityId && PortalGunRecoilManager.hasRecentlyFired((int)entityId, (int)handOrdinal, (int)2)) {
            return;
        }
        PortalGunRecoilManager.triggerRecoil((int)entityId, (int)handOrdinal);
    }
}

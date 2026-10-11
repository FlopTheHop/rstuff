/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.ExtraTortik.ricks_stuff.client.PortalGunRecoilManager
 */
package org.ExtraTortik.ricks_stuff.item;

import org.ExtraTortik.ricks_stuff.client.PortalGunRecoilManager;

private static class PortalGunItem.ClientProxy {
    private PortalGunItem.ClientProxy() {
    }

    private static void triggerRecoil(int playerId, int hand) {
        PortalGunRecoilManager.triggerRecoil((int)playerId, (int)hand);
    }
}

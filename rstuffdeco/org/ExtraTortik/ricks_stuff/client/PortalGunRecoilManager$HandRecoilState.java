/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.InteractionHand
 *  org.ExtraTortik.ricks_stuff.client.PortalGunRecoilManager$RecoilState
 */
package org.ExtraTortik.ricks_stuff.client;

import net.minecraft.world.InteractionHand;
import org.ExtraTortik.ricks_stuff.client.PortalGunRecoilManager;

public static class PortalGunRecoilManager.HandRecoilState {
    public final PortalGunRecoilManager.RecoilState mainHand = new PortalGunRecoilManager.RecoilState();
    public final PortalGunRecoilManager.RecoilState offHand = new PortalGunRecoilManager.RecoilState();

    public PortalGunRecoilManager.RecoilState get(InteractionHand hand) {
        return hand == InteractionHand.MAIN_HAND ? this.mainHand : this.offHand;
    }
}

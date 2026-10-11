/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.ExtraTortik.ricks_stuff.network.packet.OmegaEffectClientBridge
 *  org.ExtraTortik.ricks_stuff.network.packet.S2COmegaDeviceEffectPacket
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import org.ExtraTortik.ricks_stuff.network.packet.OmegaEffectClientBridge;
import org.ExtraTortik.ricks_stuff.network.packet.S2COmegaDeviceEffectPacket;

private static class S2COmegaDeviceEffectPacket.ClientHandler {
    private S2COmegaDeviceEffectPacket.ClientHandler() {
    }

    private static void handle(S2COmegaDeviceEffectPacket packet) {
        OmegaEffectClientBridge.triggerClientEffect((int)packet.effectType(), (double)packet.originX(), (double)packet.originY(), (double)packet.originZ(), (float)packet.flashIntensity(), (int)packet.flashDuration(), (float)packet.shakeIntensity(), (int)packet.shakeDuration());
    }
}

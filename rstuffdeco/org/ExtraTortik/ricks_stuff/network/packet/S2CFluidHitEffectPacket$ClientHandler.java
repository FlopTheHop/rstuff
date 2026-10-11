/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.ExtraTortik.ricks_stuff.client.particle.ClientPortalParticleHelper
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CFluidHitEffectPacket
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import org.ExtraTortik.ricks_stuff.client.particle.ClientPortalParticleHelper;
import org.ExtraTortik.ricks_stuff.network.packet.S2CFluidHitEffectPacket;

private static class S2CFluidHitEffectPacket.ClientHandler {
    private S2CFluidHitEffectPacket.ClientHandler() {
    }

    private static void handle(S2CFluidHitEffectPacket packet) {
        ClientPortalParticleHelper.spawnFluidHitParticles((double)packet.x(), (double)packet.y(), (double)packet.z(), (double)packet.groundY(), (boolean)packet.hasGround(), (int)packet.rgb());
    }
}

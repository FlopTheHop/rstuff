/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.phys.Vec3
 *  org.ExtraTortik.ricks_stuff.client.renderer.EvilMortyShieldClientManager
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CEvilMortyShieldEffectPacket
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.world.phys.Vec3;
import org.ExtraTortik.ricks_stuff.client.renderer.EvilMortyShieldClientManager;
import org.ExtraTortik.ricks_stuff.network.packet.S2CEvilMortyShieldEffectPacket;

private static class S2CEvilMortyShieldEffectPacket.ClientHandler {
    private S2CEvilMortyShieldEffectPacket.ClientHandler() {
    }

    private static void handle(S2CEvilMortyShieldEffectPacket packet) {
        EvilMortyShieldClientManager.addShieldEffect((int)packet.entityId(), (Vec3)new Vec3(packet.impactX(), packet.impactY(), packet.impactZ()), (Vec3)new Vec3(packet.dirX(), packet.dirY(), packet.dirZ()), (boolean)packet.isBreak());
    }
}

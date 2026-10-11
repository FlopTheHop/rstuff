/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.world.Container
 *  org.ExtraTortik.ricks_stuff.entity.omega.MultiverseErasureSavedData
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CSyncMultiverseErasurePacket
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.world.Container;
import org.ExtraTortik.ricks_stuff.entity.omega.MultiverseErasureSavedData;
import org.ExtraTortik.ricks_stuff.network.packet.S2CSyncMultiverseErasurePacket;

private static class S2CSyncMultiverseErasurePacket.ClientHandler {
    private S2CSyncMultiverseErasurePacket.ClientHandler() {
    }

    private static void handle(S2CSyncMultiverseErasurePacket packet) {
        MultiverseErasureSavedData.CLIENT_INSTANCE.syncFrom(packet.erasedUUIDs(), packet.erasedEntityTypes(), packet.erasedItemTypes());
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (mc.player.containerMenu == null) return;
        mc.player.containerMenu.slotsChanged((Container)mc.player.getInventory());
    }
}

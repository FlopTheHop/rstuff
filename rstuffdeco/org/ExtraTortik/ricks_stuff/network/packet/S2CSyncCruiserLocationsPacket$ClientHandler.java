/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.world.entity.Entity
 *  org.ExtraTortik.ricks_stuff.client.gui.SpaceCruiserNavScreen
 *  org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CSyncCruiserLocationsPacket
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.Entity;
import org.ExtraTortik.ricks_stuff.client.gui.SpaceCruiserNavScreen;
import org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity;
import org.ExtraTortik.ricks_stuff.network.packet.S2CSyncCruiserLocationsPacket;

private static class S2CSyncCruiserLocationsPacket.ClientHandler {
    private S2CSyncCruiserLocationsPacket.ClientHandler() {
    }

    private static void handle(S2CSyncCruiserLocationsPacket packet) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        Entity entity = mc.level.getEntity(packet.entityId());
        if (!(entity instanceof SpaceCruiserEntity)) return;
        SpaceCruiserEntity ship = (SpaceCruiserEntity)entity;
        ship.setSavedWaypoints(packet.waypoints());
        Screen screen = mc.screen;
        if (!(screen instanceof SpaceCruiserNavScreen)) return;
        SpaceCruiserNavScreen navScreen = (SpaceCruiserNavScreen)screen;
        navScreen.refreshWaypoints();
    }
}

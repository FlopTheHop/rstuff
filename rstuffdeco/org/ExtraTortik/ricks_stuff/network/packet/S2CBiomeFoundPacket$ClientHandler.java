/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  org.ExtraTortik.ricks_stuff.client.gui.SetCoordinatesScreen
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CBiomeFoundPacket
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.ExtraTortik.ricks_stuff.client.gui.SetCoordinatesScreen;
import org.ExtraTortik.ricks_stuff.network.packet.S2CBiomeFoundPacket;

private static class S2CBiomeFoundPacket.ClientHandler {
    private S2CBiomeFoundPacket.ClientHandler() {
    }

    private static void handle(S2CBiomeFoundPacket packet) {
        Minecraft mc = Minecraft.getInstance();
        Screen screen = mc.screen;
        if (!(screen instanceof SetCoordinatesScreen)) return;
        SetCoordinatesScreen setCoords = (SetCoordinatesScreen)screen;
        setCoords.onBiomeFound(packet.success(), packet.biomeName(), packet.x(), packet.y(), packet.z(), packet.dimension());
    }
}

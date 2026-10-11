/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  org.ExtraTortik.ricks_stuff.client.gui.SetCoordinatesScreen
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.ExtraTortik.ricks_stuff.client.gui.SetCoordinatesScreen;

private static class S2CStructureFoundPacket.ClientHandler {
    private S2CStructureFoundPacket.ClientHandler() {
    }

    private static void handle(boolean success, String structureName, double x, double y, double z, String dimension) {
        Minecraft mc = Minecraft.getInstance();
        Screen screen = mc.screen;
        if (!(screen instanceof SetCoordinatesScreen)) return;
        SetCoordinatesScreen setCoords = (SetCoordinatesScreen)screen;
        setCoords.onStructureFound(success, structureName, x, y, z, dimension);
    }
}

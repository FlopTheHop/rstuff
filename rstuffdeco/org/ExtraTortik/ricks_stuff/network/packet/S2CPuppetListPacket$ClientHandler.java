/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  org.ExtraTortik.ricks_stuff.client.gui.PuppetRadialMenuScreen
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CPuppetListPacket
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.ExtraTortik.ricks_stuff.client.gui.PuppetRadialMenuScreen;
import org.ExtraTortik.ricks_stuff.network.packet.S2CPuppetListPacket;

private static class S2CPuppetListPacket.ClientHandler {
    private S2CPuppetListPacket.ClientHandler() {
    }

    private static void handle(S2CPuppetListPacket packet) {
        Screen screen = Minecraft.getInstance().screen;
        if (!(screen instanceof PuppetRadialMenuScreen)) return;
        PuppetRadialMenuScreen screen2 = (PuppetRadialMenuScreen)screen;
        screen2.updateServerPuppets(packet.entries());
    }
}

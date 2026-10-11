/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.nbt.CompoundTag
 *  org.ExtraTortik.ricks_stuff.client.gui.PortalGunScreen
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import org.ExtraTortik.ricks_stuff.client.gui.PortalGunScreen;

private static class S2COpenGunMenuPacket.ClientHandler {
    private S2COpenGunMenuPacket.ClientHandler() {
    }

    private static void openMenu(CompoundTag nbt, int slot) {
        Minecraft.getInstance().setScreen((Screen)new PortalGunScreen(nbt, slot));
    }
}

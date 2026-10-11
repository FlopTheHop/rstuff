/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

private static class S2CSyncPlayerScalePacket.ClientHandler {
    private S2CSyncPlayerScalePacket.ClientHandler() {
    }

    private static void handle(int entityId, boolean isTiny) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        Entity entity = mc.level.getEntity(entityId);
        if (!(entity instanceof Player)) return;
        Player player = (Player)entity;
        player.getPersistentData().putBoolean("IsTinyRick", isTiny);
        player.refreshDimensions();
    }
}

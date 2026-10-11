/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Mob
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import java.util.UUID;
import net.minecraft.world.entity.Mob;

public record PuppetRadialMenuScreen.PuppetEntry(int id, UUID uuid, String name, float health, float maxHealth, double distance, Mob mob) {
}

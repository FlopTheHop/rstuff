/*
 * Decompiled with CFR 0.152.
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import java.util.UUID;

public record S2CPuppetListPacket.PuppetEntryData(int id, UUID uuid, String name, float health, float maxHealth, double x, double y, double z) {
}

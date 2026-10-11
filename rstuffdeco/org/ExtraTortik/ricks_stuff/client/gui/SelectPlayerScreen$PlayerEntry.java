/*
 * Decompiled with CFR 0.152.
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import java.util.UUID;

public record SelectPlayerScreen.PlayerEntry(UUID uuid, String name, int latency, boolean isSelf) {
}

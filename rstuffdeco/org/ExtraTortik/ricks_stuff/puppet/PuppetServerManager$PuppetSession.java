/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.ExtraTortik.ricks_stuff.puppet.PuppetServerManager$PuppetInput
 */
package org.ExtraTortik.ricks_stuff.puppet;

import java.util.UUID;
import org.ExtraTortik.ricks_stuff.puppet.PuppetServerManager;

public static class PuppetServerManager.PuppetSession {
    public final UUID playerUuid;
    public final int mobId;
    public volatile PuppetServerManager.PuppetInput latestInput;
    public final float originalMaxUpStep;
    public volatile long lastActiveGameTime;

    public PuppetServerManager.PuppetSession(UUID playerUuid, int mobId, PuppetServerManager.PuppetInput latestInput, float originalMaxUpStep, long lastActiveGameTime) {
        this.playerUuid = playerUuid;
        this.mobId = mobId;
        this.latestInput = latestInput;
        this.originalMaxUpStep = originalMaxUpStep;
        this.lastActiveGameTime = lastActiveGameTime;
    }
}

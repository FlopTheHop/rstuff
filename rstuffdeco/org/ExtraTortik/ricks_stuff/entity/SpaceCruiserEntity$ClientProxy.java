/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.ExtraTortik.ricks_stuff.client.SpaceCruiserClientHandler
 */
package org.ExtraTortik.ricks_stuff.entity;

import org.ExtraTortik.ricks_stuff.client.SpaceCruiserClientHandler;

private static class SpaceCruiserEntity.ClientProxy {
    private SpaceCruiserEntity.ClientProxy() {
    }

    private static void triggerImpactShake(float finalShake) {
        try {
            SpaceCruiserClientHandler.triggerImpactShake((float)finalShake);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }
}

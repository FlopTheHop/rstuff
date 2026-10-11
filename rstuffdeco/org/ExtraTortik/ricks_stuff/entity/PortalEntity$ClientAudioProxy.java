/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.ExtraTortik.ricks_stuff.client.audio.ClientAudioHelper
 *  org.ExtraTortik.ricks_stuff.entity.PortalEntity
 */
package org.ExtraTortik.ricks_stuff.entity;

import java.util.UUID;
import org.ExtraTortik.ricks_stuff.client.audio.ClientAudioHelper;
import org.ExtraTortik.ricks_stuff.entity.PortalEntity;

private static class PortalEntity.ClientAudioProxy {
    private PortalEntity.ClientAudioProxy() {
    }

    private static void stopPortalGrindSound(UUID uuid) {
        try {
            ClientAudioHelper.stopPortalGrindSound((UUID)uuid);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static void handlePortalGrindSound(PortalEntity portal) {
        try {
            ClientAudioHelper.handlePortalGrindSound((PortalEntity)portal);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }
}

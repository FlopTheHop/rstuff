/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.ExtraTortik.ricks_stuff.client.audio.ClientAudioHelper
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity
 */
package org.ExtraTortik.ricks_stuff.entity.omega;

import java.util.UUID;
import org.ExtraTortik.ricks_stuff.client.audio.ClientAudioHelper;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity;

private static class OmegaDeviceEntity.ClientAudioProxy {
    private OmegaDeviceEntity.ClientAudioProxy() {
    }

    private static void handleSound(OmegaDeviceEntity entity) {
        if (!entity.level().isClientSide()) return;
        ClientAudioHelper.handleOmegaDeviceSound((OmegaDeviceEntity)entity);
    }

    private static void stopSound(OmegaDeviceEntity entity) {
        if (!entity.level().isClientSide()) return;
        ClientAudioHelper.stopOmegaDeviceSound((UUID)entity.getUUID());
    }
}

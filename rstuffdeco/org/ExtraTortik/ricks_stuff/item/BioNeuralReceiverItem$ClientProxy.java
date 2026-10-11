/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Mob
 *  org.ExtraTortik.ricks_stuff.client.BioNeuralImplantManager
 */
package org.ExtraTortik.ricks_stuff.item;

import net.minecraft.world.entity.Mob;
import org.ExtraTortik.ricks_stuff.client.BioNeuralImplantManager;

private static class BioNeuralReceiverItem.ClientProxy {
    private BioNeuralReceiverItem.ClientProxy() {
    }

    private static void startImplanting(Mob mob) {
        BioNeuralImplantManager.startImplanting((Mob)mob);
    }
}

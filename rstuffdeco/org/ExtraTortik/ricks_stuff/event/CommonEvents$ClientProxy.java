/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.boss.enderdragon.EnderDragon
 *  org.ExtraTortik.ricks_stuff.client.BioNeuralImplantManager
 */
package org.ExtraTortik.ricks_stuff.event;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.ExtraTortik.ricks_stuff.client.BioNeuralImplantManager;

private static class CommonEvents.ClientProxy {
    private CommonEvents.ClientProxy() {
    }

    private static void startImplanting(EnderDragon dragon) {
        BioNeuralImplantManager.startImplanting((Mob)dragon);
    }
}

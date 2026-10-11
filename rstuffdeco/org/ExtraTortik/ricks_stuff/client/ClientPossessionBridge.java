/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Mob
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.fml.loading.FMLEnvironment
 *  org.ExtraTortik.ricks_stuff.client.BioNeuralPossessionManager
 */
package org.ExtraTortik.ricks_stuff.client;

import net.minecraft.world.entity.Mob;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import org.ExtraTortik.ricks_stuff.client.BioNeuralPossessionManager;

public class ClientPossessionBridge {
    public static boolean isPossessingMob(Mob mob) {
        if (FMLEnvironment.dist != Dist.CLIENT) return false;
        if (mob == null) return false;
        return BioNeuralPossessionManager.isPossessing() && BioNeuralPossessionManager.getTargetMobId() == mob.getId();
    }

    public static boolean isPossessing() {
        if (FMLEnvironment.dist != Dist.CLIENT) return false;
        return BioNeuralPossessionManager.isPossessing();
    }
}

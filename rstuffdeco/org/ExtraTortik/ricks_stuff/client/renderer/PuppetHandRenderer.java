/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.client.event.RenderHandEvent
 *  org.ExtraTortik.ricks_stuff.client.BioNeuralPossessionManager
 */
package org.ExtraTortik.ricks_stuff.client.renderer;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import org.ExtraTortik.ricks_stuff.client.BioNeuralPossessionManager;

@OnlyIn(value=Dist.CLIENT)
@EventBusSubscriber(modid="ricks_stuff", value={Dist.CLIENT})
public class PuppetHandRenderer {
    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        if (!BioNeuralPossessionManager.isPossessing()) return;
        event.setCanceled(true);
    }
}

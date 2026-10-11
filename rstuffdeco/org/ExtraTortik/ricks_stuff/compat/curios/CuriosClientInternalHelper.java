/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.Item
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.ExtraTortik.ricks_stuff.compat.curios.EyePatchCurioRenderer
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 *  top.theillusivec4.curios.api.client.CuriosRendererRegistry
 */
package org.ExtraTortik.ricks_stuff.compat.curios;

import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.ExtraTortik.ricks_stuff.compat.curios.EyePatchCurioRenderer;
import org.ExtraTortik.ricks_stuff.init.ModItems;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@OnlyIn(value=Dist.CLIENT)
class CuriosClientInternalHelper {
    CuriosClientInternalHelper() {
    }

    static void registerRenderers() {
        CuriosRendererRegistry.register((Item)((Item)ModItems.EYE_PATCH.get()), EyePatchCurioRenderer::new);
    }
}

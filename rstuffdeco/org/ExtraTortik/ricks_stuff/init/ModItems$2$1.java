/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
 *  net.neoforged.neoforge.client.extensions.common.IClientItemExtensions
 *  org.ExtraTortik.ricks_stuff.client.renderer.PortalGunItemRenderer
 *  org.ExtraTortik.ricks_stuff.init.ModItems$2
 */
package org.ExtraTortik.ricks_stuff.init;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.ExtraTortik.ricks_stuff.client.renderer.PortalGunItemRenderer;
import org.ExtraTortik.ricks_stuff.init.ModItems;

class ModItems.1
implements IClientItemExtensions {
    ModItems.1(ModItems.2 this$0) {
    }

    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return PortalGunItemRenderer.getInstance();
    }
}

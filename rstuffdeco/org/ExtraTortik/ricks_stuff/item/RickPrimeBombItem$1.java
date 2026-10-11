/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
 *  net.neoforged.neoforge.client.extensions.common.IClientItemExtensions
 *  org.ExtraTortik.ricks_stuff.client.renderer.RickPrimeBombItemRenderer
 *  org.ExtraTortik.ricks_stuff.item.RickPrimeBombItem
 */
package org.ExtraTortik.ricks_stuff.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.ExtraTortik.ricks_stuff.client.renderer.RickPrimeBombItemRenderer;
import org.ExtraTortik.ricks_stuff.item.RickPrimeBombItem;

class RickPrimeBombItem.1
implements IClientItemExtensions {
    RickPrimeBombItem.1(RickPrimeBombItem this$0) {
    }

    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return RickPrimeBombItemRenderer.getInstance();
    }
}

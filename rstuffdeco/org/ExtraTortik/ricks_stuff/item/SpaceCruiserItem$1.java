/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
 *  net.neoforged.neoforge.client.extensions.common.IClientItemExtensions
 *  org.ExtraTortik.ricks_stuff.client.renderer.SpaceCruiserItemRenderer
 *  org.ExtraTortik.ricks_stuff.item.SpaceCruiserItem
 */
package org.ExtraTortik.ricks_stuff.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.ExtraTortik.ricks_stuff.client.renderer.SpaceCruiserItemRenderer;
import org.ExtraTortik.ricks_stuff.item.SpaceCruiserItem;

class SpaceCruiserItem.1
implements IClientItemExtensions {
    SpaceCruiserItem.1(SpaceCruiserItem this$0) {
    }

    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return SpaceCruiserItemRenderer.getInstance();
    }
}

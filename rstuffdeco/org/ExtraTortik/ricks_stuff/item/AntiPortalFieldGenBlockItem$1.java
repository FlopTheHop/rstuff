/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
 *  net.neoforged.neoforge.client.extensions.common.IClientItemExtensions
 *  org.ExtraTortik.ricks_stuff.client.renderer.AntiPortalFieldGenItemRenderer
 *  org.ExtraTortik.ricks_stuff.item.AntiPortalFieldGenBlockItem
 */
package org.ExtraTortik.ricks_stuff.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.ExtraTortik.ricks_stuff.client.renderer.AntiPortalFieldGenItemRenderer;
import org.ExtraTortik.ricks_stuff.item.AntiPortalFieldGenBlockItem;

class AntiPortalFieldGenBlockItem.1
implements IClientItemExtensions {
    AntiPortalFieldGenBlockItem.1(AntiPortalFieldGenBlockItem this$0) {
    }

    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return AntiPortalFieldGenItemRenderer.getInstance();
    }
}

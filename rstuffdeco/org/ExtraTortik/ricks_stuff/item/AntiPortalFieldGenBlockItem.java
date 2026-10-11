/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.BlockItem
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.level.block.Block
 *  net.neoforged.neoforge.client.extensions.common.IClientItemExtensions
 */
package org.ExtraTortik.ricks_stuff.item;

import java.util.function.Consumer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class AntiPortalFieldGenBlockItem
extends BlockItem {
    public AntiPortalFieldGenBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions)new /* Unavailable Anonymous Inner Class!! */);
    }
}

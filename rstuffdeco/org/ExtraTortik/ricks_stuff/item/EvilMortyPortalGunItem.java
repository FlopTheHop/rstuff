/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 */
package org.ExtraTortik.ricks_stuff.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.ExtraTortik.ricks_stuff.init.ModItems;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;

public class EvilMortyPortalGunItem
extends PortalGunItem {
    public static final int EVIL_FLUID_COLOR = 16765465;

    public EvilMortyPortalGunItem(Item.Properties properties) {
        super(properties);
    }

    public int getDefaultFluidColor() {
        return 16765465;
    }

    public int getColor() {
        return 2;
    }

    public int getColor(ItemStack stack) {
        if (!EvilMortyPortalGunItem.hasCustomFluidColor((ItemStack)stack)) return 2;
        return EvilMortyPortalGunItem.getFluidColor((ItemStack)stack);
    }

    public Item getDischargedVariant() {
        return (Item)ModItems.EVIL_MORTY_PORTAL_GUN_DISCHARGED.get();
    }
}

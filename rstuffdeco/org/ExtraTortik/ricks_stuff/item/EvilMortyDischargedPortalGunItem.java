/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 *  org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem
 */
package org.ExtraTortik.ricks_stuff.item;

import net.minecraft.world.item.Item;
import org.ExtraTortik.ricks_stuff.init.ModItems;
import org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem;

public class EvilMortyDischargedPortalGunItem
extends DischargedPortalGunItem {
    public EvilMortyDischargedPortalGunItem(Item.Properties properties) {
        super(properties);
    }

    public Item getChargedGunItem() {
        return (Item)ModItems.EVIL_MORTY_PORTAL_GUN.get();
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.ItemStack
 *  org.ExtraTortik.ricks_stuff.item.ExpansionCardItem
 *  org.ExtraTortik.ricks_stuff.item.UpgradeType
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import net.minecraft.world.item.ItemStack;
import org.ExtraTortik.ricks_stuff.item.ExpansionCardItem;
import org.ExtraTortik.ricks_stuff.item.UpgradeType;

public record UpgradesScreen.InvCardEntry(int invSlot, ItemStack stack, ExpansionCardItem item, UpgradeType type) {
}

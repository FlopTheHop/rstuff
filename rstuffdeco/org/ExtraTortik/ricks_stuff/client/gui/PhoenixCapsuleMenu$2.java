/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.neoforged.neoforge.items.IItemHandler
 *  net.neoforged.neoforge.items.SlotItemHandler
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

class PhoenixCapsuleMenu.2
extends SlotItemHandler {
    PhoenixCapsuleMenu.2(IItemHandler arg0, int arg1, int arg2, int arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    public boolean mayPlace(ItemStack stack) {
        return stack.is(Items.GLASS);
    }

    public boolean isActive() {
        return PhoenixCapsuleMenu.this.isBroken();
    }
}

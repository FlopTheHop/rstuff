/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.items.IItemHandler
 *  net.neoforged.neoforge.items.SlotItemHandler
 *  org.ExtraTortik.ricks_stuff.block.entity.PhoenixCapsuleBlockEntity
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.ExtraTortik.ricks_stuff.block.entity.PhoenixCapsuleBlockEntity;

class PhoenixCapsuleMenu.4
extends SlotItemHandler {
    PhoenixCapsuleMenu.4(IItemHandler arg0, int arg1, int arg2, int arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    public boolean mayPlace(ItemStack stack) {
        return PhoenixCapsuleBlockEntity.isCatalyst((ItemStack)stack);
    }

    public boolean isActive() {
        return !PhoenixCapsuleMenu.this.isBroken() && !PhoenixCapsuleMenu.this.isPrinting() && !PhoenixCapsuleMenu.this.isReady();
    }
}

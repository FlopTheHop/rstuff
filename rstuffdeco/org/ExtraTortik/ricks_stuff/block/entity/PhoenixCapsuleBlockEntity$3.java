/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.neoforged.neoforge.items.IItemHandler
 */
package org.ExtraTortik.ricks_stuff.block.entity;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

class PhoenixCapsuleBlockEntity.3
implements IItemHandler {
    PhoenixCapsuleBlockEntity.3() {
    }

    public int getSlots() {
        return PhoenixCapsuleBlockEntity.this.itemHandler.getSlots();
    }

    @NotNull
    public ItemStack getStackInSlot(int slot) {
        return PhoenixCapsuleBlockEntity.this.itemHandler.getStackInSlot(slot);
    }

    @NotNull
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return stack;
    }

    @NotNull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot != 2) return ItemStack.EMPTY;
        ItemStack inSlot = PhoenixCapsuleBlockEntity.this.itemHandler.getStackInSlot(2);
        if (!inSlot.is(Items.BUCKET)) return ItemStack.EMPTY;
        return PhoenixCapsuleBlockEntity.this.itemHandler.extractItem(slot, amount, simulate);
    }

    public int getSlotLimit(int slot) {
        return PhoenixCapsuleBlockEntity.this.itemHandler.getSlotLimit(slot);
    }

    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return false;
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.items.IItemHandler
 */
package org.ExtraTortik.ricks_stuff.block.entity;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

class PortalFluidColorizerBlockEntity.2
implements IItemHandler {
    PortalFluidColorizerBlockEntity.2() {
    }

    public int getSlots() {
        return PortalFluidColorizerBlockEntity.this.itemHandler.getSlots();
    }

    @NotNull
    public ItemStack getStackInSlot(int slot) {
        return PortalFluidColorizerBlockEntity.this.itemHandler.getStackInSlot(slot);
    }

    @NotNull
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (slot != 4) return PortalFluidColorizerBlockEntity.this.itemHandler.insertItem(slot, stack, simulate);
        return stack;
    }

    @NotNull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }

    public int getSlotLimit(int slot) {
        return PortalFluidColorizerBlockEntity.this.itemHandler.getSlotLimit(slot);
    }

    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return PortalFluidColorizerBlockEntity.this.itemHandler.isItemValid(slot, stack);
    }
}

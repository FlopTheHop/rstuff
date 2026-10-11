/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.neoforged.neoforge.items.IItemHandler
 *  org.ExtraTortik.ricks_stuff.block.entity.PhoenixCapsuleBlockEntity
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 *  org.ExtraTortik.ricks_stuff.item.DnaSampleFlaskItem
 */
package org.ExtraTortik.ricks_stuff.block.entity;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.items.IItemHandler;
import org.ExtraTortik.ricks_stuff.block.entity.PhoenixCapsuleBlockEntity;
import org.ExtraTortik.ricks_stuff.init.ModItems;
import org.ExtraTortik.ricks_stuff.item.DnaSampleFlaskItem;
import org.jetbrains.annotations.NotNull;

/*
 * Exception performing whole class analysis ignored.
 */
class PhoenixCapsuleBlockEntity.2
implements IItemHandler {
    PhoenixCapsuleBlockEntity.2() {
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
        if (PhoenixCapsuleBlockEntity.this.isBroken) {
            if (slot == 1 && stack.is(Items.GLASS)) {
                return PhoenixCapsuleBlockEntity.this.itemHandler.insertItem(slot, stack, simulate);
            }
            if (slot != 2) return stack;
            if (!stack.is(Items.WATER_BUCKET)) return stack;
            return PhoenixCapsuleBlockEntity.this.itemHandler.insertItem(slot, stack, simulate);
        }
        if (PhoenixCapsuleBlockEntity.this.isPrinting) return stack;
        if (PhoenixCapsuleBlockEntity.this.isReady) return stack;
        if (slot == 0 && PhoenixCapsuleBlockEntity.isRawMeat((ItemStack)stack)) {
            return PhoenixCapsuleBlockEntity.this.itemHandler.insertItem(slot, stack, simulate);
        }
        if (slot == 3 && PhoenixCapsuleBlockEntity.isCatalyst((ItemStack)stack)) {
            return PhoenixCapsuleBlockEntity.this.itemHandler.insertItem(slot, stack, simulate);
        }
        if (slot != 4) return stack;
        if (!stack.is((Item)ModItems.DNA_SAMPLE_FLASK.get())) return stack;
        if (!DnaSampleFlaskItem.hasProfile((ItemStack)stack)) return stack;
        return PhoenixCapsuleBlockEntity.this.itemHandler.insertItem(slot, stack, simulate);
    }

    @NotNull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }

    public int getSlotLimit(int slot) {
        return PhoenixCapsuleBlockEntity.this.itemHandler.getSlotLimit(slot);
    }

    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return PhoenixCapsuleBlockEntity.this.itemHandler.isItemValid(slot, stack);
    }
}

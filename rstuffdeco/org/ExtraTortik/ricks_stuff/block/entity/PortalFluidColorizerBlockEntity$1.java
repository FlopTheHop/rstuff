/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.DyeItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.items.ItemStackHandler
 *  org.ExtraTortik.ricks_stuff.block.entity.PortalFluidColorizerBlockEntity
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 */
package org.ExtraTortik.ricks_stuff.block.entity;

import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.ExtraTortik.ricks_stuff.block.entity.PortalFluidColorizerBlockEntity;
import org.ExtraTortik.ricks_stuff.init.ModItems;
import org.jetbrains.annotations.NotNull;

/*
 * Exception performing whole class analysis ignored.
 */
class PortalFluidColorizerBlockEntity.1
extends ItemStackHandler {
    PortalFluidColorizerBlockEntity.1(int arg0) {
        super(arg0);
    }

    public void setSize(int size) {
        super.setSize(5);
    }

    protected void onContentsChanged(int slot) {
        PortalFluidColorizerBlockEntity.this.setChanged();
        if (PortalFluidColorizerBlockEntity.access$000((PortalFluidColorizerBlockEntity)PortalFluidColorizerBlockEntity.this) == null) return;
        if (PortalFluidColorizerBlockEntity.access$100((PortalFluidColorizerBlockEntity)PortalFluidColorizerBlockEntity.this).isClientSide) return;
        PortalFluidColorizerBlockEntity.access$300((PortalFluidColorizerBlockEntity)PortalFluidColorizerBlockEntity.this).sendBlockUpdated(PortalFluidColorizerBlockEntity.access$200((PortalFluidColorizerBlockEntity)PortalFluidColorizerBlockEntity.this), PortalFluidColorizerBlockEntity.this.getBlockState(), PortalFluidColorizerBlockEntity.this.getBlockState(), 3);
    }

    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        if (slot == 0) {
            return stack.is((Item)ModItems.PORTAL_FLUID.get()) || stack.is((Item)ModItems.INFINITY_PORTAL_FLUID.get());
        }
        if (slot >= 1 && slot <= 3) {
            return stack.getItem() instanceof DyeItem;
        }
        if (slot != 4) return false;
        return false;
    }

    public int getSlotLimit(int slot) {
        if (slot == 0) return 1;
        if (slot != 4) return super.getSlotLimit(slot);
        return 1;
    }
}

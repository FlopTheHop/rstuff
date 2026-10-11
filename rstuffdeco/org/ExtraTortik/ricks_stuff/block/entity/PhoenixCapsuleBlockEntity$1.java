/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.Containers
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.Level
 *  net.neoforged.neoforge.items.ItemStackHandler
 *  org.ExtraTortik.ricks_stuff.block.entity.PhoenixCapsuleBlockEntity
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 *  org.ExtraTortik.ricks_stuff.item.DnaSampleFlaskItem
 */
package org.ExtraTortik.ricks_stuff.block.entity;

import net.minecraft.world.Containers;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.ExtraTortik.ricks_stuff.block.entity.PhoenixCapsuleBlockEntity;
import org.ExtraTortik.ricks_stuff.init.ModItems;
import org.ExtraTortik.ricks_stuff.item.DnaSampleFlaskItem;
import org.jetbrains.annotations.NotNull;

/*
 * Exception performing whole class analysis ignored.
 */
class PhoenixCapsuleBlockEntity.1
extends ItemStackHandler {
    PhoenixCapsuleBlockEntity.1(int arg0) {
        super(arg0);
    }

    public void setSize(int size) {
        super.setSize(11);
    }

    protected void onContentsChanged(int slot) {
        ItemStack meatStack;
        if (PhoenixCapsuleBlockEntity.access$000((PhoenixCapsuleBlockEntity)PhoenixCapsuleBlockEntity.this) == null) return;
        if (PhoenixCapsuleBlockEntity.access$100((PhoenixCapsuleBlockEntity)PhoenixCapsuleBlockEntity.this).isClientSide) return;
        if (slot == 0 && (meatStack = this.getStackInSlot(0)).getCount() > 32) {
            int excessCount = meatStack.getCount() - 32;
            ItemStack excessStack = meatStack.copy();
            excessStack.setCount(excessCount);
            meatStack.setCount(32);
            this.setStackInSlot(0, meatStack);
            double dropX = (double)PhoenixCapsuleBlockEntity.access$200((PhoenixCapsuleBlockEntity)PhoenixCapsuleBlockEntity.this).getX() + 0.5;
            double dropY = (double)PhoenixCapsuleBlockEntity.access$300((PhoenixCapsuleBlockEntity)PhoenixCapsuleBlockEntity.this).getY() + 1.2;
            double dropZ = (double)PhoenixCapsuleBlockEntity.access$400((PhoenixCapsuleBlockEntity)PhoenixCapsuleBlockEntity.this).getZ() + 0.5;
            Containers.dropItemStack((Level)PhoenixCapsuleBlockEntity.access$500((PhoenixCapsuleBlockEntity)PhoenixCapsuleBlockEntity.this), (double)dropX, (double)dropY, (double)dropZ, (ItemStack)excessStack);
        }
        PhoenixCapsuleBlockEntity.access$700((PhoenixCapsuleBlockEntity)PhoenixCapsuleBlockEntity.this).sendBlockUpdated(PhoenixCapsuleBlockEntity.access$600((PhoenixCapsuleBlockEntity)PhoenixCapsuleBlockEntity.this), PhoenixCapsuleBlockEntity.this.getBlockState(), PhoenixCapsuleBlockEntity.this.getBlockState(), 3);
        PhoenixCapsuleBlockEntity.access$900((PhoenixCapsuleBlockEntity)PhoenixCapsuleBlockEntity.this).updateNeighbourForOutputSignal(PhoenixCapsuleBlockEntity.access$800((PhoenixCapsuleBlockEntity)PhoenixCapsuleBlockEntity.this), PhoenixCapsuleBlockEntity.this.getBlockState().getBlock());
    }

    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        if (slot == 0) {
            return PhoenixCapsuleBlockEntity.isRawMeat((ItemStack)stack);
        }
        if (slot == 1) {
            return stack.is(Items.GLASS);
        }
        if (slot == 2) {
            return stack.is(Items.WATER_BUCKET);
        }
        if (slot == 3) {
            return PhoenixCapsuleBlockEntity.isCatalyst((ItemStack)stack);
        }
        if (slot == 4) {
            return stack.is((Item)ModItems.DNA_SAMPLE_FLASK.get()) && DnaSampleFlaskItem.hasProfile((ItemStack)stack);
        }
        if (slot == 5) {
            return stack.getEquipmentSlot() == EquipmentSlot.HEAD;
        }
        if (slot == 6) {
            return stack.getEquipmentSlot() == EquipmentSlot.CHEST;
        }
        if (slot == 7) {
            return stack.getEquipmentSlot() == EquipmentSlot.LEGS;
        }
        if (slot == 8) return stack.getEquipmentSlot() == EquipmentSlot.FEET;
        if (slot == 9) return true;
        if (slot != 10) return false;
        return true;
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.world.Container
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.ContainerLevelAccess
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.DyeItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.neoforged.neoforge.items.ItemStackHandler
 *  org.ExtraTortik.ricks_stuff.block.entity.PortalFluidColorizerBlockEntity
 *  org.ExtraTortik.ricks_stuff.init.ModBlocks
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 *  org.ExtraTortik.ricks_stuff.init.ModMenuTypes
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.ExtraTortik.ricks_stuff.block.entity.PortalFluidColorizerBlockEntity;
import org.ExtraTortik.ricks_stuff.init.ModBlocks;
import org.ExtraTortik.ricks_stuff.init.ModItems;
import org.ExtraTortik.ricks_stuff.init.ModMenuTypes;

public class PortalFluidColorizerMenu
extends AbstractContainerMenu {
    private final PortalFluidColorizerBlockEntity blockEntity;
    private final BlockPos blockPos;
    private final ContainerLevelAccess levelAccess;

    public PortalFluidColorizerMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public PortalFluidColorizerMenu(int containerId, Inventory inv, BlockEntity entity) {
        this(containerId, inv, entity instanceof PortalFluidColorizerBlockEntity ? (be = (PortalFluidColorizerBlockEntity)entity) : null);
        PortalFluidColorizerBlockEntity be;
    }

    public PortalFluidColorizerMenu(int containerId, Inventory inv, PortalFluidColorizerBlockEntity entity) {
        super((MenuType)ModMenuTypes.PORTAL_FLUID_COLORIZER_MENU.get(), containerId);
        this.blockEntity = entity;
        BlockPos blockPos = this.blockPos = entity != null ? entity.getBlockPos() : BlockPos.ZERO;
        this.levelAccess = entity != null && entity.getLevel() != null ? ContainerLevelAccess.create((Level)entity.getLevel(), (BlockPos)this.blockPos) : (inv.player != null && inv.player.level() != null ? ContainerLevelAccess.create((Level)inv.player.level(), (BlockPos)this.blockPos) : ContainerLevelAccess.NULL);
        ItemStackHandler handler = this.blockEntity != null ? this.blockEntity.getItemHandler() : new ItemStackHandler(5);
        this.addSlot((Slot)new /* Unavailable Anonymous Inner Class!! */);
        this.addSlot((Slot)new /* Unavailable Anonymous Inner Class!! */);
        this.addSlot((Slot)new /* Unavailable Anonymous Inner Class!! */);
        this.addSlot((Slot)new /* Unavailable Anonymous Inner Class!! */);
        this.addSlot((Slot)new /* Unavailable Anonymous Inner Class!! */);
        this.addPlayerInventory(inv);
        this.addPlayerHotbar(inv);
    }

    public PortalFluidColorizerBlockEntity getBlockEntity() {
        return this.blockEntity;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public boolean isReadyToColorize() {
        if (this.slots.size() < 5) {
            return false;
        }
        ItemStack input = ((Slot)this.slots.get(0)).getItem();
        if (input.isEmpty()) return false;
        if (!input.is((Item)ModItems.PORTAL_FLUID.get()) && !input.is((Item)ModItems.INFINITY_PORTAL_FLUID.get())) {
            return false;
        }
        int i = 1;
        while (i <= 3) {
            ItemStack dye = ((Slot)this.slots.get(i)).getItem();
            if (dye.isEmpty()) return false;
            if (!(dye.getItem() instanceof DyeItem)) return false;
            if (dye.getCount() < 1) {
                return false;
            }
            ++i;
        }
        return true;
    }

    public boolean stillValid(Player player) {
        return PortalFluidColorizerMenu.stillValid((ContainerLevelAccess)this.levelAccess, (Player)player, (Block)((Block)ModBlocks.PORTAL_FLUID_COLORIZER.get()));
    }

    private void addPlayerInventory(Inventory playerInventory) {
        int row = 0;
        while (row < 3) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot((Container)playerInventory, col + row * 9 + 9, 18 + col * 18, 142 + row * 18));
            }
            ++row;
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        int col = 0;
        while (col < 9) {
            this.addSlot(new Slot((Container)playerInventory, col, 18 + col * 18, 200));
            ++col;
        }
    }

    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);
        if (slot == null) return itemstack;
        if (!slot.hasItem()) return itemstack;
        ItemStack stackInSlot = slot.getItem();
        itemstack = stackInSlot.copy();
        if (index < 5 ? !this.moveItemStackTo(stackInSlot, 5, this.slots.size(), true) : (stackInSlot.is((Item)ModItems.PORTAL_FLUID.get()) || stackInSlot.is((Item)ModItems.INFINITY_PORTAL_FLUID.get()) ? !this.moveItemStackTo(stackInSlot, 0, 1, false) : (stackInSlot.getItem() instanceof DyeItem ? !this.moveItemStackTo(stackInSlot, 1, 4, false) : (index < 32 ? !this.moveItemStackTo(stackInSlot, 32, this.slots.size(), false) : !this.moveItemStackTo(stackInSlot, 5, 32, false))))) {
            return ItemStack.EMPTY;
        }
        if (stackInSlot.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        if (stackInSlot.getCount() == itemstack.getCount()) {
            return ItemStack.EMPTY;
        }
        slot.onTake(player, stackInSlot);
        return itemstack;
    }
}

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
 *  net.minecraft.world.inventory.ContainerData
 *  net.minecraft.world.inventory.ContainerLevelAccess
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.inventory.SimpleContainerData
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  org.ExtraTortik.ricks_stuff.block.entity.AntiPortalFieldGenBlockEntity
 *  org.ExtraTortik.ricks_stuff.init.ModBlocks
 *  org.ExtraTortik.ricks_stuff.init.ModMenuTypes
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.ExtraTortik.ricks_stuff.block.entity.AntiPortalFieldGenBlockEntity;
import org.ExtraTortik.ricks_stuff.init.ModBlocks;
import org.ExtraTortik.ricks_stuff.init.ModMenuTypes;

public class AntiPortalMenu
extends AbstractContainerMenu {
    private final AntiPortalFieldGenBlockEntity blockEntity;
    private final ContainerData data;
    private final ContainerLevelAccess levelAccess;

    public AntiPortalMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), (ContainerData)new SimpleContainerData(4));
    }

    public AntiPortalMenu(int containerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super((MenuType)ModMenuTypes.ANTI_PORTAL_MENU.get(), containerId);
        this.blockEntity = (AntiPortalFieldGenBlockEntity)entity;
        this.data = data;
        this.levelAccess = entity != null ? ContainerLevelAccess.create((Level)entity.getLevel(), (BlockPos)entity.getBlockPos()) : ContainerLevelAccess.NULL;
        this.addPlayerInventory(inv);
        this.addPlayerHotbar(inv);
        this.addDataSlots(data);
    }

    public AntiPortalFieldGenBlockEntity getBlockEntity() {
        return this.blockEntity;
    }

    public BlockPos getBlockPos() {
        return this.blockEntity != null ? this.blockEntity.getBlockPos() : BlockPos.ZERO;
    }

    public int getRadius() {
        return this.data.get(0);
    }

    public void setRadius(int radius) {
        this.data.set(0, radius);
    }

    public int getRedstoneMode() {
        return this.data.get(1);
    }

    public boolean isEnabled() {
        return this.data.get(2) == 1;
    }

    public void setEnabled(boolean enabled) {
        this.data.set(2, enabled ? 1 : 0);
    }

    public boolean isActive() {
        return this.data.get(3) == 1;
    }

    public boolean stillValid(Player player) {
        return AntiPortalMenu.stillValid((ContainerLevelAccess)this.levelAccess, (Player)player, (Block)((Block)ModBlocks.ANTI_PORTAL_FIELD_GEN.get()));
    }

    private void addPlayerInventory(Inventory playerInventory) {
        int row = 0;
        while (row < 3) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot((Container)playerInventory, col + row * 9 + 9, 8 + col * 18, 94 + row * 18));
            }
            ++row;
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        int col = 0;
        while (col < 9) {
            this.addSlot(new Slot((Container)playerInventory, col, 8 + col * 18, 152));
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
        if (index < 27 ? !this.moveItemStackTo(stackInSlot, 27, 36, false) : !this.moveItemStackTo(stackInSlot, 0, 27, false)) {
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

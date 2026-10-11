/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.world.Container
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.ContainerData
 *  net.minecraft.world.inventory.ContainerLevelAccess
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.inventory.SimpleContainerData
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.neoforged.neoforge.items.IItemHandler
 *  net.neoforged.neoforge.items.ItemStackHandler
 *  net.neoforged.neoforge.items.SlotItemHandler
 *  org.ExtraTortik.ricks_stuff.block.entity.PhoenixCapsuleBlockEntity
 *  org.ExtraTortik.ricks_stuff.init.ModBlocks
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 *  org.ExtraTortik.ricks_stuff.init.ModMenuTypes
 *  org.ExtraTortik.ricks_stuff.item.DnaSampleFlaskItem
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.ExtraTortik.ricks_stuff.block.entity.PhoenixCapsuleBlockEntity;
import org.ExtraTortik.ricks_stuff.init.ModBlocks;
import org.ExtraTortik.ricks_stuff.init.ModItems;
import org.ExtraTortik.ricks_stuff.init.ModMenuTypes;
import org.ExtraTortik.ricks_stuff.item.DnaSampleFlaskItem;

public class PhoenixCapsuleMenu
extends AbstractContainerMenu {
    private final PhoenixCapsuleBlockEntity blockEntity;
    private final BlockPos blockPos;
    private final ContainerData data;
    private final ContainerLevelAccess levelAccess;

    public PhoenixCapsuleMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, extraData.readBlockPos(), (ContainerData)new SimpleContainerData(7));
    }

    public PhoenixCapsuleMenu(int containerId, Inventory inv, BlockEntity entity, ContainerData data) {
        this(containerId, inv, entity instanceof PhoenixCapsuleBlockEntity ? (be = (PhoenixCapsuleBlockEntity)entity) : null, entity != null ? entity.getBlockPos() : BlockPos.ZERO, data);
        PhoenixCapsuleBlockEntity be;
    }

    public PhoenixCapsuleMenu(int containerId, Inventory inv, BlockPos pos, ContainerData data) {
        PhoenixCapsuleBlockEntity be;
        BlockEntity blockEntity = inv.player.level().getBlockEntity(pos);
        this(containerId, inv, blockEntity instanceof PhoenixCapsuleBlockEntity ? (be = (PhoenixCapsuleBlockEntity)blockEntity) : null, pos, data);
    }

    public PhoenixCapsuleMenu(int containerId, Inventory inv, PhoenixCapsuleBlockEntity entity, BlockPos pos, ContainerData data) {
        super((MenuType)ModMenuTypes.PHOENIX_CAPSULE_MENU.get(), containerId);
        this.blockEntity = entity;
        this.blockPos = pos != null ? pos : (entity != null ? entity.getBlockPos() : BlockPos.ZERO);
        this.data = data;
        ContainerLevelAccess containerLevelAccess = entity != null && entity.getLevel() != null ? ContainerLevelAccess.create((Level)entity.getLevel(), (BlockPos)this.blockPos) : (this.levelAccess = inv.player != null && inv.player.level() != null ? ContainerLevelAccess.create((Level)inv.player.level(), (BlockPos)this.blockPos) : ContainerLevelAccess.NULL);
        if (this.blockEntity != null) {
            this.data.set(0, this.blockEntity.getPrintProgress());
            this.data.set(1, this.blockEntity.isPrinting() ? 1 : 0);
            this.data.set(2, this.blockEntity.isReady() ? 1 : 0);
            this.data.set(3, this.blockEntity.isBroken() ? 1 : 0);
            this.data.set(4, this.blockEntity.getTotalPrintTicks());
            this.data.set(5, this.blockEntity.getCatalystType());
            this.data.set(6, this.blockEntity.isCorrupted() ? 1 : 0);
        }
        ItemStackHandler handler = this.blockEntity != null ? this.blockEntity.getItemHandler() : new ItemStackHandler(11);
        this.addSlot((Slot)new /* Unavailable Anonymous Inner Class!! */);
        this.addSlot((Slot)new /* Unavailable Anonymous Inner Class!! */);
        this.addSlot((Slot)new /* Unavailable Anonymous Inner Class!! */);
        this.addSlot((Slot)new /* Unavailable Anonymous Inner Class!! */);
        this.addSlot((Slot)new /* Unavailable Anonymous Inner Class!! */);
        this.addSlot((Slot)new /* Unavailable Anonymous Inner Class!! */);
        this.addSlot((Slot)new /* Unavailable Anonymous Inner Class!! */);
        this.addSlot((Slot)new /* Unavailable Anonymous Inner Class!! */);
        this.addSlot((Slot)new /* Unavailable Anonymous Inner Class!! */);
        this.addSlot((Slot)new SlotItemHandler((IItemHandler)handler, 9, 156, 31));
        this.addSlot((Slot)new SlotItemHandler((IItemHandler)handler, 10, 156, 58));
        this.addPlayerInventory(inv);
        this.addPlayerHotbar(inv);
        this.addDataSlots(data);
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public PhoenixCapsuleBlockEntity getBlockEntity() {
        return this.blockEntity;
    }

    public boolean isPrinting() {
        return this.data.get(1) == 1;
    }

    public boolean isReady() {
        return this.data.get(2) == 1;
    }

    public boolean isBroken() {
        return this.data.get(3) == 1;
    }

    public int getPrintProgress() {
        return this.data.get(0);
    }

    public int getTotalPrintTicks() {
        return this.data.getCount() > 4 && this.data.get(4) > 0 ? this.data.get(4) : PhoenixCapsuleBlockEntity.getBaseTotalPrintTicks();
    }

    public int getCatalystType() {
        return this.data.getCount() > 5 ? this.data.get(5) : 0;
    }

    public boolean isCorrupted() {
        return this.data.getCount() > 6 && this.data.get(6) == 1;
    }

    public int getScaledProgress(int pixels) {
        int progress = this.data.get(0);
        int maxProgress = this.getTotalPrintTicks();
        return maxProgress != 0 && progress != 0 ? progress * pixels / maxProgress : 0;
    }

    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = (Slot)this.slots.get(index);
        if (sourceSlot == null) return ItemStack.EMPTY;
        if (!sourceSlot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();
        if (index < 11) {
            if (!this.moveItemStackTo(sourceStack, 11, 47, true)) {
                return ItemStack.EMPTY;
            }
        } else if (this.isBroken()) {
            if (sourceStack.is(Items.GLASS)) {
                if (!this.moveItemStackTo(sourceStack, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!sourceStack.is(Items.WATER_BUCKET)) return ItemStack.EMPTY;
                if (!this.moveItemStackTo(sourceStack, 2, 3, false)) {
                    return ItemStack.EMPTY;
                }
            }
        } else if (sourceStack.getEquipmentSlot() == EquipmentSlot.HEAD ? !this.moveItemStackTo(sourceStack, 5, 6, false) : (sourceStack.getEquipmentSlot() == EquipmentSlot.CHEST ? !this.moveItemStackTo(sourceStack, 6, 7, false) : (sourceStack.getEquipmentSlot() == EquipmentSlot.LEGS ? !this.moveItemStackTo(sourceStack, 7, 8, false) : (sourceStack.getEquipmentSlot() == EquipmentSlot.FEET ? !this.moveItemStackTo(sourceStack, 8, 9, false) : (sourceStack.is((Item)ModItems.DNA_SAMPLE_FLASK.get()) && DnaSampleFlaskItem.hasProfile((ItemStack)sourceStack) ? !this.moveItemStackTo(sourceStack, 4, 5, false) : (PhoenixCapsuleBlockEntity.isRawMeat((ItemStack)sourceStack) ? !this.moveItemStackTo(sourceStack, 0, 1, false) : (PhoenixCapsuleBlockEntity.isCatalyst((ItemStack)sourceStack) ? !this.moveItemStackTo(sourceStack, 3, 4, false) : !this.moveItemStackTo(sourceStack, 9, 11, false)))))))) {
            return ItemStack.EMPTY;
        }
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    public boolean stillValid(Player player) {
        return PhoenixCapsuleMenu.stillValid((ContainerLevelAccess)this.levelAccess, (Player)player, (Block)((Block)ModBlocks.PHOENIX_CAPSULE.get()));
    }

    private void addPlayerInventory(Inventory playerInventory) {
        int i = 0;
        while (i < 3) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot((Container)playerInventory, l + i * 9 + 9, 18 + l * 18, 138 + i * 18));
            }
            ++i;
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        int i = 0;
        while (i < 9) {
            this.addSlot(new Slot((Container)playerInventory, i, 18 + i * 18, 196));
            ++i;
        }
    }
}

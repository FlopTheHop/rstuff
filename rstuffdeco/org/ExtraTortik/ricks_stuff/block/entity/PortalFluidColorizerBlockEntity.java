/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.component.DataComponentType
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientGamePacketListener
 *  net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
 *  net.minecraft.world.Container
 *  net.minecraft.world.Containers
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.SimpleContainer
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.item.DyeItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.component.CustomData
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  net.neoforged.neoforge.items.IItemHandler
 *  net.neoforged.neoforge.items.ItemStackHandler
 *  org.ExtraTortik.ricks_stuff.client.gui.PortalFluidColorizerMenu
 *  org.ExtraTortik.ricks_stuff.init.ModBlockEntities
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 */
package org.ExtraTortik.ricks_stuff.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.ExtraTortik.ricks_stuff.client.gui.PortalFluidColorizerMenu;
import org.ExtraTortik.ricks_stuff.init.ModBlockEntities;
import org.ExtraTortik.ricks_stuff.init.ModItems;
import org.jetbrains.annotations.Nullable;

public class PortalFluidColorizerBlockEntity
extends BlockEntity
implements MenuProvider {
    public static final String TAG_FLUID_COLOR = "FluidColor";
    public static final String TAG_CORE_COLOR = "CoreColor";
    public static final int DEFAULT_FLUID_COLOR = 2356545;
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_DYE_1 = 1;
    public static final int SLOT_DYE_2 = 2;
    public static final int SLOT_DYE_3 = 3;
    public static final int SLOT_OUTPUT = 4;
    public static final int CONTAINER_SIZE = 5;
    private final ItemStackHandler itemHandler = new /* Unavailable Anonymous Inner Class!! */;
    private final IItemHandler topSideHandler = new /* Unavailable Anonymous Inner Class!! */;
    private final IItemHandler bottomHandler = new /* Unavailable Anonymous Inner Class!! */;

    public PortalFluidColorizerBlockEntity(BlockPos pos, BlockState state) {
        super((BlockEntityType)ModBlockEntities.PORTAL_FLUID_COLORIZER_BE.get(), pos, state);
    }

    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }

    public IItemHandler getItemHandlerForSide(@Nullable Direction side) {
        if (side == null) {
            return this.itemHandler;
        }
        if (side != Direction.DOWN) return this.topSideHandler;
        return this.bottomHandler;
    }

    public boolean canColorize() {
        ItemStack input = this.itemHandler.getStackInSlot(0);
        if (input.isEmpty()) return false;
        if (!input.is((Item)ModItems.PORTAL_FLUID.get()) && !input.is((Item)ModItems.INFINITY_PORTAL_FLUID.get())) {
            return false;
        }
        int i = 1;
        while (true) {
            if (i > 3) {
                ItemStack output = this.itemHandler.getStackInSlot(4);
                if (output.isEmpty()) return true;
                if (output.getCount() >= output.getMaxStackSize()) return false;
                if (output.is(input.getItem())) return true;
                return false;
            }
            ItemStack dye = this.itemHandler.getStackInSlot(i);
            if (dye.isEmpty()) return false;
            if (!(dye.getItem() instanceof DyeItem)) {
                return false;
            }
            ++i;
        }
    }

    public boolean canColorize(int targetColorRgb) {
        if (!this.canColorize()) {
            return false;
        }
        ItemStack output = this.itemHandler.getStackInSlot(4);
        if (output.isEmpty()) return true;
        CustomData customData = (CustomData)output.get(DataComponents.CUSTOM_DATA);
        CompoundTag tag = customData != null ? customData.copyTag() : null;
        int currentColor = tag != null && tag.contains(TAG_FLUID_COLOR) ? tag.getInt(TAG_FLUID_COLOR) : 2356545;
        if (currentColor == targetColorRgb) return true;
        return false;
    }

    public boolean colorize(int targetColorRgb) {
        return this.colorize(targetColorRgb, targetColorRgb);
    }

    public boolean colorize(int fluidColorRgb, int coreColorRgb) {
        ItemStack newOutput;
        if (!this.canColorize()) {
            return false;
        }
        ItemStack input = this.itemHandler.getStackInSlot(0);
        ItemStack existingOutput = this.itemHandler.getStackInSlot(4);
        if (existingOutput.isEmpty()) {
            newOutput = input.copy();
            newOutput.setCount(1);
            CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)newOutput, tag -> {
                tag.putInt(TAG_FLUID_COLOR, fluidColorRgb);
                tag.putInt(TAG_CORE_COLOR, coreColorRgb);
            });
        } else {
            CustomData customData = (CustomData)existingOutput.get(DataComponents.CUSTOM_DATA);
            CompoundTag tag2 = customData != null ? customData.copyTag() : new CompoundTag();
            int currentColor = tag2.contains(TAG_FLUID_COLOR) ? tag2.getInt(TAG_FLUID_COLOR) : 2356545;
            int currentCore = tag2.contains(TAG_CORE_COLOR) ? tag2.getInt(TAG_CORE_COLOR) : currentColor;
            if (currentColor != fluidColorRgb) return false;
            if (currentCore != coreColorRgb) return false;
            if (existingOutput.getCount() >= existingOutput.getMaxStackSize()) return false;
            if (!existingOutput.is(input.getItem())) {
                return false;
            }
            existingOutput.grow(1);
            newOutput = existingOutput;
        }
        input.shrink(1);
        this.itemHandler.setStackInSlot(0, input.isEmpty() ? ItemStack.EMPTY : input);
        int i = 1;
        while (true) {
            if (i > 3) {
                this.itemHandler.setStackInSlot(4, newOutput);
                this.setChanged();
                if (this.level == null) return true;
                if (this.level.isClientSide) return true;
                this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
                return true;
            }
            ItemStack dye = this.itemHandler.getStackInSlot(i);
            dye.shrink(1);
            this.itemHandler.setStackInSlot(i, dye.isEmpty() ? ItemStack.EMPTY : dye);
            ++i;
        }
    }

    public void drops() {
        if (this.level == null) return;
        if (this.level.isClientSide) return;
        SimpleContainer container = new SimpleContainer(5);
        int i = 0;
        while (true) {
            if (i >= 5) {
                Containers.dropContents((Level)this.level, (BlockPos)this.worldPosition, (Container)container);
                return;
            }
            container.setItem(i, this.itemHandler.getStackInSlot(i));
            ++i;
        }
    }

    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", (Tag)this.itemHandler.serializeNBT(registries));
    }

    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (!tag.contains("inventory")) return;
        this.itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
    }

    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        this.saveAdditional(tag, registries);
        return tag;
    }

    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create((BlockEntity)this);
    }

    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        super.handleUpdateTag(tag, registries);
        this.loadAdditional(tag, registries);
    }

    public Component getDisplayName() {
        return Component.translatable((String)"block.ricks_stuff.portal_fluid_colorizer");
    }

    @Nullable
    public AbstractContainerMenu createMenu(int containerId, Inventory inv, Player player) {
        return new PortalFluidColorizerMenu(containerId, inv, this);
    }

    static /* synthetic */ Level access$000(PortalFluidColorizerBlockEntity x0) {
        return x0.level;
    }

    static /* synthetic */ Level access$100(PortalFluidColorizerBlockEntity x0) {
        return x0.level;
    }

    static /* synthetic */ BlockPos access$200(PortalFluidColorizerBlockEntity x0) {
        return x0.worldPosition;
    }

    static /* synthetic */ Level access$300(PortalFluidColorizerBlockEntity x0) {
        return x0.level;
    }
}

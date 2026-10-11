/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.Container
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.component.CustomData
 *  net.minecraft.world.item.component.ItemContainerContents
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.neoforged.neoforge.capabilities.Capabilities$ItemHandler
 *  net.neoforged.neoforge.items.IItemHandler
 *  net.neoforged.neoforge.items.IItemHandlerModifiable
 *  org.ExtraTortik.ricks_stuff.entity.omega.MultiverseErasureSavedData
 */
package org.ExtraTortik.ricks_stuff.entity.omega;

import java.util.ArrayList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.ExtraTortik.ricks_stuff.entity.omega.MultiverseErasureSavedData;

public class ErasurePurgeHelper {
    public static void purgePlayerInventories(ServerPlayer player, MultiverseErasureSavedData storage) {
        if (player == null) return;
        if (storage == null) return;
        if (storage.getErasedItemTypes().isEmpty()) {
            return;
        }
        ErasurePurgeHelper.purgeContainer((Container)player.getInventory(), storage);
        ErasurePurgeHelper.purgeContainer((Container)player.getEnderChestInventory(), storage);
        if (player.containerMenu != null) {
            ErasurePurgeHelper.purgeMenu(player.containerMenu, storage);
        }
        if (player.inventoryMenu != null && player.inventoryMenu != player.containerMenu) {
            ErasurePurgeHelper.purgeMenu((AbstractContainerMenu)player.inventoryMenu, storage);
        }
        try {
            IItemHandler entityHandler = (IItemHandler)player.getCapability(Capabilities.ItemHandler.ENTITY, null);
            if (entityHandler == null) return;
            ErasurePurgeHelper.purgeItemHandler(entityHandler, storage);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static void purgeContainer(Container container, MultiverseErasureSavedData storage) {
        if (container == null) return;
        if (storage == null) return;
        if (storage.getErasedItemTypes().isEmpty()) {
            return;
        }
        int i = 0;
        while (true) {
            if (i >= container.getContainerSize()) {
                container.setChanged();
                return;
            }
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                if (storage.isItemErased(stack.getItem())) {
                    container.setItem(i, ItemStack.EMPTY);
                } else {
                    ErasurePurgeHelper.purgeNestedItemsInStack(stack, storage);
                }
            }
            ++i;
        }
    }

    public static void purgeItemHandler(IItemHandler handler, MultiverseErasureSavedData storage) {
        if (handler == null) return;
        if (storage == null) return;
        if (storage.getErasedItemTypes().isEmpty()) {
            return;
        }
        int i = 0;
        while (i < handler.getSlots()) {
            ItemStack stack = handler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (storage.isItemErased(stack.getItem())) {
                    if (handler instanceof IItemHandlerModifiable) {
                        IItemHandlerModifiable modifiable = (IItemHandlerModifiable)handler;
                        modifiable.setStackInSlot(i, ItemStack.EMPTY);
                    } else {
                        handler.extractItem(i, stack.getCount(), false);
                    }
                } else {
                    ErasurePurgeHelper.purgeNestedItemsInStack(stack, storage);
                }
            }
            ++i;
        }
    }

    public static void purgeMenu(AbstractContainerMenu menu, MultiverseErasureSavedData storage) {
        if (menu == null) return;
        if (storage == null) return;
        if (storage.getErasedItemTypes().isEmpty()) {
            return;
        }
        for (Slot slot : menu.slots) {
            if (!slot.hasItem()) continue;
            ItemStack stack = slot.getItem();
            if (storage.isItemErased(stack.getItem())) {
                slot.set(ItemStack.EMPTY);
                continue;
            }
            ErasurePurgeHelper.purgeNestedItemsInStack(stack, storage);
        }
        ItemStack carried = menu.getCarried();
        if (!carried.isEmpty()) {
            if (storage.isItemErased(carried.getItem())) {
                menu.setCarried(ItemStack.EMPTY);
            } else {
                ErasurePurgeHelper.purgeNestedItemsInStack(carried, storage);
            }
        }
        menu.broadcastChanges();
    }

    public static boolean purgeNestedItemsInStack(ItemStack stack, MultiverseErasureSavedData storage) {
        CompoundTag bet;
        CustomData customData;
        if (stack == null) return false;
        if (stack.isEmpty()) return false;
        if (storage == null) return false;
        if (storage.getErasedItemTypes().isEmpty()) {
            return false;
        }
        boolean modified = false;
        ItemContainerContents container = (ItemContainerContents)stack.get(DataComponents.CONTAINER);
        if (container != null) {
            ArrayList<ItemStack> list = new ArrayList<ItemStack>();
            boolean changed = false;
            for (ItemStack item : container.nonEmptyItems()) {
                if (storage.isItemErased(item.getItem())) {
                    changed = true;
                    continue;
                }
                if (ErasurePurgeHelper.purgeNestedItemsInStack(item, storage)) {
                    changed = true;
                }
                list.add(item);
            }
            if (changed) {
                stack.set(DataComponents.CONTAINER, (Object)ItemContainerContents.fromItems(list));
                modified = true;
            }
        }
        if ((customData = (CustomData)stack.get(DataComponents.CUSTOM_DATA)) == null) return modified;
        CompoundTag tag = customData.copyTag();
        boolean customModified = false;
        if (tag.contains("BlockEntityTag", 10) && (bet = tag.getCompound("BlockEntityTag")).contains("Items", 9)) {
            ListTag items = bet.getList("Items", 10);
            for (int i = items.size() - 1; i >= 0; --i) {
                CompoundTag itemTag = items.getCompound(i);
                String id = itemTag.getString("id");
                if (!storage.isItemErased(id)) continue;
                items.remove(i);
                customModified = true;
            }
            if (items.isEmpty()) {
                bet.remove("Items");
            }
        }
        if (tag.contains("Items", 9)) {
            ListTag items = tag.getList("Items", 10);
            for (int i = items.size() - 1; i >= 0; --i) {
                CompoundTag itemTag = items.getCompound(i);
                String id = itemTag.getString("id");
                if (!storage.isItemErased(id)) continue;
                items.remove(i);
                customModified = true;
            }
            if (items.isEmpty()) {
                tag.remove("Items");
            }
        }
        if (!customModified) return modified;
        stack.set(DataComponents.CUSTOM_DATA, (Object)CustomData.of((CompoundTag)tag));
        modified = true;
        return modified;
    }

    public static void purgeBlockEntity(BlockEntity blockEntity, MultiverseErasureSavedData storage) {
        if (blockEntity == null) return;
        if (storage == null) return;
        if (storage.getErasedItemTypes().isEmpty()) {
            return;
        }
        try {
            if (blockEntity instanceof Container) {
                Container container = (Container)blockEntity;
                ErasurePurgeHelper.purgeContainer(container, storage);
            }
            if (blockEntity.getLevel() == null) return;
            IItemHandler handler = (IItemHandler)blockEntity.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, null);
            if (handler == null) return;
            ErasurePurgeHelper.purgeItemHandler(handler, storage);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static void purgeEntity(Entity entity, MultiverseErasureSavedData storage) {
        if (entity == null) return;
        if (storage == null) return;
        if (storage.getErasedItemTypes().isEmpty()) {
            return;
        }
        try {
            if (entity instanceof ServerPlayer) {
                ServerPlayer player = (ServerPlayer)entity;
                ErasurePurgeHelper.purgePlayerInventories(player, storage);
            } else {
                IItemHandler handler;
                if (entity instanceof Container) {
                    Container container = (Container)entity;
                    ErasurePurgeHelper.purgeContainer(container, storage);
                }
                if ((handler = (IItemHandler)entity.getCapability(Capabilities.ItemHandler.ENTITY, null)) == null) return;
                ErasurePurgeHelper.purgeItemHandler(handler, storage);
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }
}

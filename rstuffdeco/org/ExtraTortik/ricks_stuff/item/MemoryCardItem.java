/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.component.DataComponentType
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.component.CustomData
 */
package org.ExtraTortik.ricks_stuff.item;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.Nullable;

public class MemoryCardItem
extends Item {
    public static final String TAG_SAVED_LOCATIONS = "SavedLocations";
    public static final String TAG_HISTORY_LOCATIONS = "HistoryLocations";
    public static final String TAG_CUSTOM_LOCATION_INDEX = "CustomLocationIndex";
    public static final String TAG_CUSTOM_LOCATION = "CustomLocation";

    public MemoryCardItem(Item.Properties properties) {
        super(properties);
    }

    public static ListTag getSavedLocations(ItemStack stack) {
        CustomData customData = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return new ListTag();
        CompoundTag tag = customData.copyTag();
        if (!tag.contains(TAG_SAVED_LOCATIONS, 9)) return new ListTag();
        return tag.getList(TAG_SAVED_LOCATIONS, 10);
    }

    public static void setSavedLocations(ItemStack stack, ListTag list) {
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> {
            if (list != null && !list.isEmpty()) {
                tag.put(TAG_SAVED_LOCATIONS, (Tag)list);
            } else {
                tag.remove(TAG_SAVED_LOCATIONS);
            }
        });
    }

    public static ListTag getHistoryLocations(ItemStack stack) {
        CustomData customData = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return new ListTag();
        CompoundTag tag = customData.copyTag();
        if (!tag.contains(TAG_HISTORY_LOCATIONS, 9)) return new ListTag();
        return tag.getList(TAG_HISTORY_LOCATIONS, 10);
    }

    public static void setHistoryLocations(ItemStack stack, ListTag list) {
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> {
            if (list != null && !list.isEmpty()) {
                tag.put(TAG_HISTORY_LOCATIONS, (Tag)list);
            } else {
                tag.remove(TAG_HISTORY_LOCATIONS);
            }
        });
    }

    public static CompoundTag getCustomLocation(ItemStack stack) {
        CustomData customData = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return null;
        CompoundTag tag = customData.copyTag();
        if (!tag.contains(TAG_CUSTOM_LOCATION, 10)) return null;
        return tag.getCompound(TAG_CUSTOM_LOCATION);
    }

    public static void setCustomLocation(ItemStack stack, @Nullable CompoundTag loc) {
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> {
            if (loc != null) {
                tag.put(TAG_CUSTOM_LOCATION, (Tag)loc);
            } else {
                tag.remove(TAG_CUSTOM_LOCATION);
            }
        });
    }

    public static int getCustomLocationIndex(ItemStack stack) {
        CustomData customData = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return -1;
        CompoundTag tag = customData.copyTag();
        if (!tag.contains(TAG_CUSTOM_LOCATION_INDEX)) return -1;
        return tag.getInt(TAG_CUSTOM_LOCATION_INDEX);
    }

    public static void setCustomLocationIndex(ItemStack stack, int index) {
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> {
            if (index >= 0) {
                tag.putInt(TAG_CUSTOM_LOCATION_INDEX, index);
            } else {
                tag.remove(TAG_CUSTOM_LOCATION_INDEX);
            }
        });
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        ListTag savedList = MemoryCardItem.getSavedLocations(stack);
        ListTag historyList = MemoryCardItem.getHistoryLocations(stack);
        int savedCount = savedList.size();
        int historyCount = historyList.size();
        if (savedCount == 0 && historyCount == 0) {
            tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.memory_card.empty").withStyle(ChatFormatting.DARK_GRAY));
        } else {
            if (savedCount > 0) {
                tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.memory_card.locations", (Object[])new Object[]{savedCount}).withStyle(ChatFormatting.AQUA));
            }
            if (historyCount > 0) {
                tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.memory_card.history", (Object[])new Object[]{historyCount}).withStyle(ChatFormatting.GRAY));
            }
        }
        tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.memory_card.hint").withStyle(new ChatFormatting[]{ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC}));
    }
}

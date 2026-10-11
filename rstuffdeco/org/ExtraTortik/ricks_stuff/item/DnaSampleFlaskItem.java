/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.component.DataComponentType
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.component.CustomData
 *  net.minecraft.world.item.component.ResolvableProfile
 *  net.minecraft.world.level.ItemLike
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 */
package org.ExtraTortik.ricks_stuff.item;

import com.mojang.authlib.GameProfile;
import java.util.List;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.ItemLike;
import org.ExtraTortik.ricks_stuff.init.ModItems;
import org.jetbrains.annotations.Nullable;

public class DnaSampleFlaskItem
extends Item {
    public static final String TAG_TARGET_PROFILE = "TargetProfile";
    public static final String TAG_TARGET_NAME = "TargetName";
    public static final String TAG_TARGET_UUID = "TargetUUID";

    public DnaSampleFlaskItem(Item.Properties properties) {
        super(properties);
    }

    public static boolean hasProfile(ItemStack stack) {
        if (stack == null) return false;
        if (stack.isEmpty()) {
            return false;
        }
        if (stack.has(DataComponents.PROFILE)) {
            return true;
        }
        CustomData customData = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return false;
        CompoundTag tag = customData.copyTag();
        return tag.contains(TAG_TARGET_PROFILE) || tag.hasUUID(TAG_TARGET_UUID);
    }

    @Nullable
    public static GameProfile getProfile(ItemStack stack) {
        if (!DnaSampleFlaskItem.hasProfile(stack)) {
            return null;
        }
        ResolvableProfile profileComponent = (ResolvableProfile)stack.get(DataComponents.PROFILE);
        if (profileComponent != null && profileComponent.gameProfile() != null) {
            return profileComponent.gameProfile();
        }
        CustomData customData = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return null;
        CompoundTag tag = customData.copyTag();
        if (!tag.hasUUID(TAG_TARGET_UUID)) return null;
        UUID uuid = tag.getUUID(TAG_TARGET_UUID);
        String name = tag.contains(TAG_TARGET_NAME) ? tag.getString(TAG_TARGET_NAME) : "";
        return new GameProfile(uuid, name);
    }

    public static void setProfile(ItemStack stack, @Nullable GameProfile profile) {
        if (profile == null) {
            stack.remove(DataComponents.PROFILE);
            CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> {
                tag.remove(TAG_TARGET_PROFILE);
                tag.remove(TAG_TARGET_UUID);
                tag.remove(TAG_TARGET_NAME);
            });
            return;
        }
        stack.set(DataComponents.PROFILE, (Object)new ResolvableProfile(profile));
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> {
            if (profile.getId() != null) {
                tag.putUUID(TAG_TARGET_UUID, profile.getId());
            }
            if (profile.getName() == null) return;
            tag.putString(TAG_TARGET_NAME, profile.getName());
        });
    }

    public static ItemStack createSample(GameProfile profile) {
        ItemStack stack = new ItemStack((ItemLike)ModItems.DNA_SAMPLE_FLASK.get());
        DnaSampleFlaskItem.setProfile(stack, profile);
        return stack;
    }

    public boolean isFoil(ItemStack stack) {
        return DnaSampleFlaskItem.hasProfile(stack) || super.isFoil(stack);
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        GameProfile profile = DnaSampleFlaskItem.getProfile(stack);
        if (profile != null) {
            CustomData cd;
            String name = profile.getName();
            if ((name == null || name.isEmpty()) && (cd = (CustomData)stack.get(DataComponents.CUSTOM_DATA)) != null && cd.copyTag().contains(TAG_TARGET_NAME)) {
                name = cd.copyTag().getString(TAG_TARGET_NAME);
            }
            if (name == null || name.isEmpty()) {
                name = "Unknown";
            }
            tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.dna_sample.subject", (Object[])new Object[]{name}).withStyle(ChatFormatting.GREEN));
            if (profile.getId() != null) {
                tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.dna_sample.uuid", (Object[])new Object[]{profile.getId().toString()}).withStyle(ChatFormatting.DARK_GRAY));
            }
            tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.dna_sample.compatible").withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.dna_sample.empty").withStyle(ChatFormatting.RED));
        }
    }
}

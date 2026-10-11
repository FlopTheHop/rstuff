/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.component.DataComponentType
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.component.CustomData
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.UpgradeType
 *  org.ExtraTortik.ricks_stuff.portal.PortalMode
 */
package org.ExtraTortik.ricks_stuff.item;

import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.ExtraTortik.ricks_stuff.init.ModItems;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.ExtraTortik.ricks_stuff.item.UpgradeType;
import org.ExtraTortik.ricks_stuff.portal.PortalMode;

public class CreativePortalGunItem
extends PortalGunItem {
    public CreativePortalGunItem(Item.Properties properties) {
        super(properties);
    }

    public static void initializeCreativeGunNBT(ItemStack stack) {
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> {
            tag.putInt("Charge", 100);
            if (!tag.contains("GunId") || tag.getInt("GunId") == 0) {
                tag.putInt("GunId", ThreadLocalRandom.current().nextInt(1, 1000000));
            }
            if (!tag.contains("Mode")) {
                tag.putString("Mode", PortalMode.FIFO.getName());
            }
            if (!tag.contains("Scale")) {
                tag.putFloat("Scale", 2.0f);
            }
            if (!tag.contains("AutoClose")) {
                tag.putBoolean("AutoClose", false);
            }
            if (!tag.contains("SafePlacement")) {
                tag.putBoolean("SafePlacement", true);
            }
            if (!tag.contains("AutoAirDetonate")) {
                tag.putBoolean("AutoAirDetonate", false);
            }
            if (!tag.contains("AirDistance")) {
                tag.putInt("AirDistance", 15);
            }
            if (!tag.contains("ChargeBar")) {
                tag.putBoolean("ChargeBar", true);
            }
            tag.putBoolean("HasMemoryCard", true);
            if (!tag.contains("SavedLocations")) {
                tag.put("SavedLocations", (Tag)new ListTag());
            }
            if (!tag.contains("HistoryLocations")) {
                tag.put("HistoryLocations", (Tag)new ListTag());
            }
            if (!tag.contains("ActivePortals")) {
                tag.put("ActivePortals", (Tag)new ListTag());
            }
            ListTag upgrades = new ListTag();
            UpgradeType[] upgradeTypeArray = UpgradeType.values();
            int n = upgradeTypeArray.length;
            int n2 = 0;
            while (true) {
                if (n2 >= n) {
                    tag.put("Upgrades", (Tag)upgrades);
                    return;
                }
                UpgradeType type = upgradeTypeArray[n2];
                CompoundTag uTag = new CompoundTag();
                uTag.putString("Id", type.getId());
                upgrades.add((Object)uTag);
                ++n2;
            }
        });
    }

    public static ItemStack createFullGun() {
        ItemStack stack = new ItemStack((ItemLike)ModItems.RICK_PORTAL_GUN_CREATIVE.get());
        CreativePortalGunItem.initializeCreativeGunNBT(stack);
        return stack;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CreativePortalGunItem.initializeCreativeGunNBT(stack);
        return super.use(level, player, hand);
    }

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        CompoundTag tag = CreativePortalGunItem.getGunTag((ItemStack)stack);
        if (tag == null || !tag.contains("Upgrades") || tag.getList("Upgrades", 10).isEmpty()) {
            CreativePortalGunItem.initializeCreativeGunNBT(stack);
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }
}

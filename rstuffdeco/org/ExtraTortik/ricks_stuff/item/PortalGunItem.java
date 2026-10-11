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
 *  net.minecraft.network.chat.Style
 *  net.minecraft.network.chat.TextColor
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.component.CustomData
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.client.extensions.common.IClientItemExtensions
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.entity.PortalProjectileEntity
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.CreativePortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.ExpansionCardItem
 *  org.ExtraTortik.ricks_stuff.item.MemoryCardItem
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem$ClientProxy
 *  org.ExtraTortik.ricks_stuff.item.UpgradeType
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CGunRecoilPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.S2COpenGunMenuPacket
 *  org.ExtraTortik.ricks_stuff.portal.PortalManager
 *  org.ExtraTortik.ricks_stuff.portal.PortalMode
 */
package org.ExtraTortik.ricks_stuff.item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.entity.PortalProjectileEntity;
import org.ExtraTortik.ricks_stuff.init.ModItems;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.CreativePortalGunItem;
import org.ExtraTortik.ricks_stuff.item.ExpansionCardItem;
import org.ExtraTortik.ricks_stuff.item.MemoryCardItem;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.ExtraTortik.ricks_stuff.item.UpgradeType;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.S2CGunRecoilPacket;
import org.ExtraTortik.ricks_stuff.network.packet.S2COpenGunMenuPacket;
import org.ExtraTortik.ricks_stuff.portal.PortalManager;
import org.ExtraTortik.ricks_stuff.portal.PortalMode;
import org.jetbrains.annotations.Nullable;

/*
 * Exception performing whole class analysis ignored.
 */
public class PortalGunItem
extends Item {
    public static final String TAG_CHARGE = "Charge";
    public static final String TAG_GUN_ID = "GunId";
    public static final String TAG_MODE = "Mode";
    public static final String TAG_SCALE = "Scale";
    public static final String TAG_AUTO_CLOSE = "AutoClose";
    public static final String TAG_SAFE_PLACEMENT = "SafePlacement";
    public static final String TAG_CHARGE_BAR = "ChargeBar";
    public static final String TAG_AUTO_AIR_DETONATE = "AutoAirDetonate";
    public static final String TAG_AIR_DISTANCE = "AirDistance";
    public static final String TAG_SAVED_LOCATIONS = "SavedLocations";
    public static final String TAG_HISTORY_LOCATIONS = "HistoryLocations";
    public static final String TAG_CUSTOM_LOCATION_INDEX = "CustomLocationIndex";
    public static final String TAG_CUSTOM_LOCATION = "CustomLocation";
    public static final String TAG_ACTIVE_PORTALS = "ActivePortals";
    public static final String TAG_HAS_MEMORY_CARD = "HasMemoryCard";
    public static final String TAG_UPGRADES = "Upgrades";
    public static final String TAG_INFINITY_BULB = "InfinityBulb";
    public static final String TAG_FLUID_COLOR = "FluidColor";
    public static final String TAG_CORE_COLOR = "CoreColor";
    public static final int DEFAULT_FLUID_COLOR = 2356545;

    public int getDefaultFluidColor() {
        return 2356545;
    }

    public static int getFluidColor(ItemStack stack) {
        CompoundTag tag = PortalGunItem.getGunTag(stack);
        if (tag != null && tag.contains("FluidColor")) {
            return tag.getInt("FluidColor");
        }
        if (stack == null) return 2356545;
        Item item = stack.getItem();
        if (!(item instanceof PortalGunItem)) return 2356545;
        PortalGunItem pgi = (PortalGunItem)item;
        return pgi.getDefaultFluidColor();
    }

    public static void setFluidColor(ItemStack stack, int rgb) {
        if (stack == null) return;
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> tag.putInt("FluidColor", rgb));
    }

    public static boolean hasCustomFluidColor(ItemStack stack) {
        CompoundTag tag = PortalGunItem.getGunTag(stack);
        return tag != null && tag.contains("FluidColor");
    }

    public static int getCoreColor(ItemStack stack) {
        CompoundTag tag = PortalGunItem.getGunTag(stack);
        if (tag == null) return PortalGunItem.getFluidColor(stack);
        if (!tag.contains("CoreColor")) return PortalGunItem.getFluidColor(stack);
        return tag.getInt("CoreColor");
    }

    public static void setCoreColor(ItemStack stack, int rgb) {
        if (stack == null) return;
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> tag.putInt("CoreColor", rgb));
    }

    public static boolean hasCustomCoreColor(ItemStack stack) {
        CompoundTag tag = PortalGunItem.getGunTag(stack);
        return tag != null && tag.contains("CoreColor");
    }

    public static boolean hasInfinityBulb(ItemStack stack) {
        if (stack == null) return false;
        if (stack.isEmpty()) {
            return false;
        }
        if (!(stack.getItem() instanceof CreativePortalGunItem)) CompoundTag tag;
        return (tag = PortalGunItem.getGunTag(stack)) != null && tag.getBoolean("InfinityBulb");
        return true;
    }

    public static void setInfinityBulb(ItemStack stack, boolean infinity) {
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> tag.putBoolean("InfinityBulb", infinity));
    }

    public int getColor(ItemStack stack) {
        if (!PortalGunItem.hasCustomFluidColor(stack)) return this.getColor();
        return PortalGunItem.getFluidColor(stack);
    }

    public static int getMaxUpgrades() {
        return Config.maxExpansionCards;
    }

    public PortalGunItem(Item.Properties properties) {
        super(properties);
    }

    public int getColor() {
        return 0;
    }

    public static CompoundTag getGunTag(ItemStack stack) {
        CustomData customData = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
        return customData != null ? customData.copyTag() : null;
    }

    public static CompoundTag getOrCreateGunTag(ItemStack stack) {
        CustomData customData = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
        return customData != null ? customData.copyTag() : new CompoundTag();
    }

    public static void initializeGunNBT(ItemStack stack) {
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> {
            if (!tag.contains("Charge")) {
                tag.putInt("Charge", 100);
            }
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
            if (!tag.contains("HasMemoryCard")) {
                if (tag.contains("SavedLocations", 9) && !tag.getList("SavedLocations", 10).isEmpty() || tag.contains("HistoryLocations", 9) && !tag.getList("HistoryLocations", 10).isEmpty()) {
                    tag.putBoolean("HasMemoryCard", true);
                } else {
                    tag.putBoolean("HasMemoryCard", false);
                }
            }
            if (!tag.contains("ActivePortals")) {
                tag.put("ActivePortals", (Tag)new ListTag());
            }
            if (tag.contains("Upgrades")) return;
            tag.put("Upgrades", (Tag)new ListTag());
        });
    }

    public static int getCharge(ItemStack stack) {
        CompoundTag tag = PortalGunItem.getGunTag(stack);
        if (tag == null) return 100;
        if (!tag.contains("Charge")) return 100;
        return tag.getInt("Charge");
    }

    public static void setCharge(ItemStack stack, int charge) {
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> tag.putInt("Charge", Math.max(0, Math.min(100, charge))));
    }

    public static void consumeCharge(ItemStack stack, int amount) {
        int current = PortalGunItem.getCharge(stack);
        PortalGunItem.setCharge(stack, current - amount);
    }

    public static int getGunId(ItemStack stack) {
        CompoundTag tag = PortalGunItem.getGunTag(stack);
        if (tag == null) return 0;
        if (!tag.contains("GunId")) return 0;
        return tag.getInt("GunId");
    }

    public static PortalMode getMode(ItemStack stack) {
        CompoundTag tag = PortalGunItem.getGunTag(stack);
        if (tag == null) return PortalMode.FIFO;
        if (!tag.contains("Mode")) return PortalMode.FIFO;
        return PortalMode.fromString((String)tag.getString("Mode"));
    }

    public static void setMode(ItemStack stack, PortalMode mode) {
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> tag.putString("Mode", mode.getName()));
    }

    public static float getScale(ItemStack stack) {
        CompoundTag tag = PortalGunItem.getGunTag(stack);
        if (tag == null) return 2.0f;
        if (!tag.contains("Scale")) return 2.0f;
        return tag.getFloat("Scale");
    }

    public static void setScale(ItemStack stack, float scale) {
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> tag.putFloat("Scale", Math.max(0.25f, Math.min(2.0f, scale))));
    }

    public static boolean isAutoClose(ItemStack stack) {
        CompoundTag tag = PortalGunItem.getGunTag(stack);
        return tag != null && tag.getBoolean("AutoClose");
    }

    public static void setAutoClose(ItemStack stack, boolean autoClose) {
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> tag.putBoolean("AutoClose", autoClose));
    }

    public static boolean isSafePlacement(ItemStack stack) {
        CompoundTag tag = PortalGunItem.getGunTag(stack);
        return tag == null || !tag.contains("SafePlacement") || tag.getBoolean("SafePlacement");
    }

    public static void setSafePlacement(ItemStack stack, boolean safe) {
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> tag.putBoolean("SafePlacement", safe));
    }

    public static boolean isAutoAirDetonate(ItemStack stack) {
        CompoundTag tag = PortalGunItem.getGunTag(stack);
        return tag != null && tag.getBoolean("AutoAirDetonate");
    }

    public static void setAutoAirDetonate(ItemStack stack, boolean auto) {
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> tag.putBoolean("AutoAirDetonate", auto));
    }

    public static int getAirDistance(ItemStack stack) {
        CompoundTag tag = PortalGunItem.getGunTag(stack);
        if (tag == null) return 15;
        if (!tag.contains("AirDistance")) return 15;
        return tag.getInt("AirDistance");
    }

    public static void setAirDistance(ItemStack stack, int distance) {
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> tag.putInt("AirDistance", Math.max(1, Math.min(500, distance))));
    }

    public static boolean isChargeBar(ItemStack stack) {
        CompoundTag tag = PortalGunItem.getGunTag(stack);
        return tag == null || !tag.contains("ChargeBar") || tag.getBoolean("ChargeBar");
    }

    public static void setChargeBar(ItemStack stack, boolean chargeBar) {
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> tag.putBoolean("ChargeBar", chargeBar));
    }

    public static boolean hasMemoryCard(ItemStack stack) {
        if (stack == null) return false;
        if (stack.isEmpty()) {
            return false;
        }
        CompoundTag tag = PortalGunItem.getGunTag(stack);
        if (tag == null) {
            return false;
        }
        if (tag.contains("HasMemoryCard")) return tag.getBoolean("HasMemoryCard");
        if (tag.contains("SavedLocations", 9) && !tag.getList("SavedLocations", 10).isEmpty() || tag.contains("HistoryLocations", 9) && !tag.getList("HistoryLocations", 10).isEmpty()) {
            PortalGunItem.setHasMemoryCard(stack, true);
            return true;
        }
        PortalGunItem.setHasMemoryCard(stack, false);
        return false;
    }

    public static void setHasMemoryCard(ItemStack stack, boolean hasCard) {
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> tag.putBoolean("HasMemoryCard", hasCard));
    }

    public static boolean insertMemoryCard(ItemStack gunStack, ItemStack cardStack) {
        if (gunStack.isEmpty()) return false;
        if (cardStack.isEmpty()) return false;
        if (PortalGunItem.hasMemoryCard(gunStack)) {
            return false;
        }
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)gunStack, gunTag -> {
            gunTag.putBoolean("HasMemoryCard", true);
            CompoundTag cardTag = MemoryCardItem.getCustomLocation((ItemStack)cardStack) != null || !MemoryCardItem.getSavedLocations((ItemStack)cardStack).isEmpty() || !MemoryCardItem.getHistoryLocations((ItemStack)cardStack).isEmpty() ? ((CustomData)cardStack.getOrDefault(DataComponents.CUSTOM_DATA, (Object)CustomData.EMPTY)).copyTag() : null;
            if (cardTag == null) return;
            if (cardTag.contains("SavedLocations", 9)) {
                gunTag.put("SavedLocations", (Tag)cardTag.getList("SavedLocations", 10).copy());
            }
            if (cardTag.contains("HistoryLocations", 9)) {
                gunTag.put("HistoryLocations", (Tag)cardTag.getList("HistoryLocations", 10).copy());
            }
            if (cardTag.contains("CustomLocation", 10)) {
                gunTag.put("CustomLocation", (Tag)cardTag.getCompound("CustomLocation").copy());
            }
            if (!cardTag.contains("CustomLocationIndex")) return;
            gunTag.putInt("CustomLocationIndex", cardTag.getInt("CustomLocationIndex"));
        });
        return true;
    }

    public static ItemStack ejectMemoryCard(ItemStack gunStack, @Nullable Player player) {
        if (gunStack.isEmpty()) return ItemStack.EMPTY;
        if (!PortalGunItem.hasMemoryCard(gunStack)) {
            return ItemStack.EMPTY;
        }
        ItemStack cardStack = new ItemStack((ItemLike)ModItems.MEMORY_CARD.get());
        CompoundTag gunTag = PortalGunItem.getOrCreateGunTag(gunStack);
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)cardStack, cardTag -> {
            if (gunTag.contains("SavedLocations", 9)) {
                cardTag.put("SavedLocations", (Tag)gunTag.getList("SavedLocations", 10).copy());
            }
            if (gunTag.contains("HistoryLocations", 9)) {
                cardTag.put("HistoryLocations", (Tag)gunTag.getList("HistoryLocations", 10).copy());
            }
            if (gunTag.contains("CustomLocation", 10)) {
                cardTag.put("CustomLocation", (Tag)gunTag.getCompound("CustomLocation").copy());
            }
            if (!gunTag.contains("CustomLocationIndex")) return;
            cardTag.putInt("CustomLocationIndex", gunTag.getInt("CustomLocationIndex"));
        });
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)gunStack, tag -> {
            tag.remove("SavedLocations");
            tag.remove("HistoryLocations");
            tag.remove("CustomLocation");
            tag.remove("CustomLocationIndex");
            tag.putBoolean("HasMemoryCard", false);
        });
        if (player == null) return cardStack;
        if (!player.getInventory().add(cardStack)) {
            player.drop(cardStack, false);
        }
        player.playSound((SoundEvent)ModSounds.UI_PRESS.get(), 1.0f, 1.2f);
        return cardStack;
    }

    public static List<UpgradeType> getUpgrades(@Nullable CompoundTag tag) {
        UpgradeType ut;
        ArrayList<UpgradeType> list = new ArrayList<UpgradeType>();
        if (tag == null) return list;
        if (!tag.contains("Upgrades", 9)) {
            return list;
        }
        ListTag listTag = tag.getList("Upgrades", 10);
        for (int i = 0; i < listTag.size(); ++i) {
            CompoundTag entry = listTag.getCompound(i);
            if (!entry.contains("Id") || (ut = UpgradeType.fromId((String)entry.getString("Id"))) == null || list.contains(ut)) continue;
            list.add(ut);
        }
        if (!list.isEmpty()) return list;
        ListTag strList = tag.getList("Upgrades", 8);
        int i = 0;
        while (i < strList.size()) {
            ut = UpgradeType.fromId((String)strList.getString(i));
            if (ut != null && !list.contains(ut)) {
                list.add(ut);
            }
            ++i;
        }
        return list;
    }

    public static List<UpgradeType> getUpgrades(ItemStack stack) {
        if (stack == null) return new ArrayList<UpgradeType>();
        if (!stack.isEmpty()) return PortalGunItem.getUpgrades(PortalGunItem.getGunTag(stack));
        return new ArrayList<UpgradeType>();
    }

    public static boolean hasUpgrade(ItemStack stack, UpgradeType type) {
        if (type != null) return PortalGunItem.getUpgrades(stack).contains(type);
        return false;
    }

    public static boolean hasUpgrade(@Nullable CompoundTag tag, @Nullable UpgradeType type) {
        if (tag == null) return false;
        if (type != null) return PortalGunItem.getUpgrades(tag).contains(type);
        return false;
    }

    public static boolean canInstallUpgrade(ItemStack stack, UpgradeType type) {
        if (stack == null) return false;
        if (stack.isEmpty()) return false;
        List<UpgradeType> upgrades = PortalGunItem.getUpgrades(stack);
        if (type != null) return upgrades.size() < PortalGunItem.getMaxUpgrades() && !upgrades.contains(type);
        return false;
    }

    public static boolean installUpgrade(@Nullable CompoundTag tag, UpgradeType type) {
        if (tag == null) return false;
        if (type == null) {
            return false;
        }
        List<UpgradeType> upgrades = PortalGunItem.getUpgrades(tag);
        if (upgrades.size() >= PortalGunItem.getMaxUpgrades()) return false;
        if (upgrades.contains(type)) {
            return false;
        }
        ListTag listTag = tag.contains("Upgrades", 9) ? tag.getList("Upgrades", 10) : new ListTag();
        CompoundTag entry = new CompoundTag();
        entry.putString("Id", type.getId());
        listTag.add((Object)entry);
        tag.put("Upgrades", (Tag)listTag);
        return true;
    }

    public static boolean installUpgrade(ItemStack stack, UpgradeType type) {
        if (!PortalGunItem.canInstallUpgrade(stack, type)) {
            return false;
        }
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> PortalGunItem.installUpgrade(tag, type));
        return true;
    }

    public static UpgradeType ejectUpgrade(@Nullable CompoundTag tag, int slotIndex) {
        if (tag == null) {
            return null;
        }
        List<UpgradeType> upgrades = PortalGunItem.getUpgrades(tag);
        if (slotIndex < 0) return null;
        if (slotIndex >= upgrades.size()) {
            return null;
        }
        UpgradeType removedType = upgrades.remove(slotIndex);
        ListTag listTag = new ListTag();
        Iterator<UpgradeType> iterator = upgrades.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                tag.put("Upgrades", (Tag)listTag);
                return removedType;
            }
            UpgradeType ut = iterator.next();
            CompoundTag entry = new CompoundTag();
            entry.putString("Id", ut.getId());
            listTag.add((Object)entry);
        }
    }

    public static ItemStack ejectUpgrade(ItemStack stack, int slotIndex, @Nullable Player player) {
        if (stack == null) return ItemStack.EMPTY;
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        List<UpgradeType> upgrades = PortalGunItem.getUpgrades(stack);
        if (slotIndex < 0) return ItemStack.EMPTY;
        if (slotIndex >= upgrades.size()) {
            return ItemStack.EMPTY;
        }
        UpgradeType removedType = upgrades.remove(slotIndex);
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> {
            ListTag listTag = new ListTag();
            Iterator iterator = upgrades.iterator();
            while (true) {
                if (!iterator.hasNext()) {
                    tag.put("Upgrades", (Tag)listTag);
                    return;
                }
                UpgradeType ut = (UpgradeType)iterator.next();
                CompoundTag entry = new CompoundTag();
                entry.putString("Id", ut.getId());
                listTag.add((Object)entry);
            }
        });
        ItemStack cardStack = new ItemStack((ItemLike)removedType.getItem());
        if (player == null) return cardStack;
        if (!player.getInventory().add(cardStack)) {
            player.drop(cardStack, false);
        }
        player.playSound((SoundEvent)ModSounds.UI_PRESS.get(), 1.0f, 1.2f);
        return cardStack;
    }

    public static boolean installUpgradeFromStack(ItemStack gunStack, ItemStack cardStack) {
        if (gunStack.isEmpty()) return false;
        if (cardStack.isEmpty()) {
            return false;
        }
        UpgradeType type = UpgradeType.fromStack((ItemStack)cardStack);
        if (type != null) return PortalGunItem.installUpgrade(gunStack, type);
        return false;
    }

    public static ListTag getSavedLocations(ItemStack stack) {
        if (!PortalGunItem.hasMemoryCard(stack)) {
            return new ListTag();
        }
        CompoundTag tag = PortalGunItem.getGunTag(stack);
        if (tag == null) return new ListTag();
        if (!tag.contains("SavedLocations", 9)) return new ListTag();
        return tag.getList("SavedLocations", 10);
    }

    public static void addSavedLocation(ItemStack stack, CompoundTag locationData) {
        if (!PortalGunItem.hasMemoryCard(stack)) {
            return;
        }
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> {
            ListTag list = tag.contains("SavedLocations", 9) ? tag.getList("SavedLocations", 10) : new ListTag();
            if (list.size() >= 30) return;
            list.add((Object)locationData);
            tag.put("SavedLocations", (Tag)list);
        });
    }

    public static void addSavedLocation(ItemStack stack, String name, double x, double y, double z, String dimension) {
        if (!PortalGunItem.hasMemoryCard(stack)) {
            return;
        }
        CompoundTag loc = new CompoundTag();
        loc.putString("Name", name);
        loc.putDouble("X", x);
        loc.putDouble("Y", y);
        loc.putDouble("Z", z);
        loc.putString("Dimension", dimension);
        PortalGunItem.addSavedLocation(stack, loc);
    }

    public static void removeSavedLocation(ItemStack stack, int index) {
        if (!PortalGunItem.hasMemoryCard(stack)) {
            return;
        }
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> {
            if (!tag.contains("SavedLocations", 9)) return;
            ListTag list = tag.getList("SavedLocations", 10);
            if (index < 0) return;
            if (index >= list.size()) return;
            list.remove(index);
            tag.put("SavedLocations", (Tag)list);
        });
    }

    public static ListTag getHistoryLocations(ItemStack stack) {
        if (!PortalGunItem.hasMemoryCard(stack)) {
            return new ListTag();
        }
        CompoundTag tag = PortalGunItem.getGunTag(stack);
        if (tag == null) return new ListTag();
        if (!tag.contains("HistoryLocations", 9)) return new ListTag();
        return tag.getList("HistoryLocations", 10);
    }

    public static void addHistoryLocation(ItemStack stack, CompoundTag locationData) {
        if (!PortalGunItem.hasMemoryCard(stack)) {
            return;
        }
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> {
            ListTag list;
            ListTag listTag = list = tag.contains("HistoryLocations", 9) ? tag.getList("HistoryLocations", 10) : new ListTag();
            if (list.size() >= 30) {
                list.remove(0);
            }
            list.add((Object)locationData);
            tag.put("HistoryLocations", (Tag)list);
        });
    }

    public static int getCustomLocationIndex(ItemStack stack) {
        CompoundTag tag = PortalGunItem.getGunTag(stack);
        if (tag == null) return -1;
        if (!tag.contains("CustomLocationIndex")) return -1;
        return tag.getInt("CustomLocationIndex");
    }

    public static void setCustomLocationIndex(ItemStack stack, int index) {
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> {
            if (index >= 0) {
                tag.putInt("CustomLocationIndex", index);
            } else {
                tag.remove("CustomLocationIndex");
            }
        });
    }

    public static CompoundTag getCustomLocation(ItemStack stack) {
        CompoundTag tag = PortalGunItem.getGunTag(stack);
        if (tag == null) return null;
        if (!tag.contains("CustomLocation")) return null;
        return tag.getCompound("CustomLocation");
    }

    public static void setCustomLocation(ItemStack stack, CompoundTag locationData) {
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> {
            if (locationData != null) {
                tag.put("CustomLocation", (Tag)locationData);
            } else {
                tag.remove("CustomLocation");
            }
        });
    }

    public static List<UUID> getActivePortals(ItemStack stack) {
        ArrayList<UUID> result = new ArrayList<UUID>();
        CompoundTag tag = PortalGunItem.getGunTag(stack);
        if (tag == null) return result;
        if (!tag.contains("ActivePortals")) return result;
        ListTag list = tag.getList("ActivePortals", 10);
        int i = 0;
        while (i < list.size()) {
            CompoundTag entry = list.getCompound(i);
            if (entry.hasUUID("UUID")) {
                result.add(entry.getUUID("UUID"));
            }
            ++i;
        }
        return result;
    }

    public static void setActivePortals(ItemStack stack, List<?> portals) {
        ListTag list = new ListTag();
        if (portals != null) {
            for (Object obj : portals) {
                if (!(obj instanceof UUID)) continue;
                UUID uuid = (UUID)obj;
                CompoundTag entry = new CompoundTag();
                entry.putUUID("UUID", uuid);
                list.add((Object)entry);
            }
        }
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> tag.put("ActivePortals", (Tag)list));
    }

    public static void addActivePortal(ItemStack stack, UUID portalUUID) {
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> {
            ListTag list = tag.contains("ActivePortals", 9) ? tag.getList("ActivePortals", 10) : new ListTag();
            CompoundTag entry = new CompoundTag();
            entry.putUUID("UUID", portalUUID);
            list.add((Object)entry);
            tag.put("ActivePortals", (Tag)list);
        });
    }

    public static void removeActivePortal(ItemStack stack, UUID portalUUID) {
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> {
            if (!tag.contains("ActivePortals")) return;
            ListTag list = tag.getList("ActivePortals", 10);
            for (int i = list.size() - 1; i >= 0; --i) {
                CompoundTag entry = list.getCompound(i);
                if (!entry.hasUUID("UUID") || !entry.getUUID("UUID").equals(portalUUID)) continue;
                list.remove(i);
                break;
            }
            tag.put("ActivePortals", (Tag)list);
        });
    }

    public static void clearActivePortals(ItemStack stack) {
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> tag.put("ActivePortals", (Tag)new ListTag()));
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        int chargeCost;
        Item item;
        ItemStack stack = player.getItemInHand(hand);
        PortalGunItem.initializeGunNBT(stack);
        InteractionHand otherHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack offhandStack = player.getItemInHand(otherHand);
        if (!offhandStack.isEmpty() && offhandStack.is((Item)ModItems.MEMORY_CARD.get()) && !PortalGunItem.hasMemoryCard(stack) && PortalGunItem.insertMemoryCard(stack, offhandStack)) {
            if (!player.getAbilities().instabuild) {
                offhandStack.shrink(1);
            }
            player.playSound((SoundEvent)ModSounds.UI_PRESS.get(), 1.0f, 1.4f);
            player.getCooldowns().addCooldown((Item)this, 10);
            return InteractionResultHolder.sidedSuccess((Object)stack, (boolean)level.isClientSide());
        }
        if (!offhandStack.isEmpty() && (item = offhandStack.getItem()) instanceof ExpansionCardItem) {
            ExpansionCardItem cardItem = (ExpansionCardItem)item;
            UpgradeType type = cardItem.getUpgradeType();
            if (!PortalGunItem.canInstallUpgrade(stack, type)) {
                player.playSound((SoundEvent)ModSounds.GUN_EMPTY.get(), 1.0f, 1.0f);
                return InteractionResultHolder.fail((Object)stack);
            }
            if (PortalGunItem.installUpgrade(stack, type)) {
                if (!player.getAbilities().instabuild) {
                    offhandStack.shrink(1);
                }
                player.playSound((SoundEvent)ModSounds.UI_PRESS.get(), 1.0f, 1.4f);
                player.getCooldowns().addCooldown((Item)this, 10);
                return InteractionResultHolder.sidedSuccess((Object)stack, (boolean)level.isClientSide());
            }
        }
        if (!offhandStack.isEmpty() && offhandStack.is((Item)ModItems.INFINITY_PORTAL_FLUID.get()) && !PortalGunItem.hasInfinityBulb(stack)) {
            int oldCharge = PortalGunItem.getCharge(stack);
            if (oldCharge > 0) {
                oldTube = new ItemStack((ItemLike)ModItems.PORTAL_FLUID.get());
                CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)oldTube, tag -> tag.putInt("Charge", oldCharge));
                if (PortalGunItem.hasCustomFluidColor(stack)) {
                    PortalGunItem.setFluidColor(oldTube, PortalGunItem.getFluidColor(stack));
                }
                if (PortalGunItem.hasCustomCoreColor(stack)) {
                    PortalGunItem.setCoreColor(oldTube, PortalGunItem.getCoreColor(stack));
                }
                if (!player.getInventory().add(oldTube)) {
                    player.drop(oldTube, false);
                }
            } else {
                oldTube = new ItemStack((ItemLike)ModItems.EMPTY_TUBE.get());
                if (!player.getInventory().add(oldTube)) {
                    player.drop(oldTube, false);
                }
            }
            if (PortalGunItem.hasCustomFluidColor(offhandStack)) {
                PortalGunItem.setFluidColor(stack, PortalGunItem.getFluidColor(offhandStack));
            } else {
                CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> tag.remove("FluidColor"));
            }
            if (PortalGunItem.hasCustomCoreColor(offhandStack)) {
                PortalGunItem.setCoreColor(stack, PortalGunItem.getCoreColor(offhandStack));
            } else {
                CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> tag.remove("CoreColor"));
            }
            PortalGunItem.setInfinityBulb(stack, true);
            PortalGunItem.setCharge(stack, 100);
            if (!player.getAbilities().instabuild) {
                offhandStack.shrink(1);
            }
            player.playSound((SoundEvent)ModSounds.BULB_INSERT.get(), 1.0f, 1.0f);
            player.getCooldowns().addCooldown((Item)this, 20);
            return InteractionResultHolder.sidedSuccess((Object)stack, (boolean)level.isClientSide());
        }
        if (!offhandStack.isEmpty() && offhandStack.is((Item)ModItems.PORTAL_FLUID.get()) && !PortalGunItem.hasInfinityBulb(stack) && PortalGunItem.getCharge(stack) < 100) {
            int oldCharge;
            int tubeCharge = 100;
            CompoundTag fluidTag = PortalGunItem.getGunTag(offhandStack);
            if (fluidTag != null && fluidTag.contains("Charge")) {
                tubeCharge = fluidTag.getInt("Charge");
            }
            if ((oldCharge = PortalGunItem.getCharge(stack)) > 0) {
                ItemStack oldTube = new ItemStack((ItemLike)ModItems.PORTAL_FLUID.get());
                CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)oldTube, tag -> tag.putInt("Charge", oldCharge));
                if (PortalGunItem.hasCustomFluidColor(stack)) {
                    PortalGunItem.setFluidColor(oldTube, PortalGunItem.getFluidColor(stack));
                }
                if (PortalGunItem.hasCustomCoreColor(stack)) {
                    PortalGunItem.setCoreColor(oldTube, PortalGunItem.getCoreColor(stack));
                }
                if (!player.getInventory().add(oldTube)) {
                    player.drop(oldTube, false);
                }
            } else {
                ItemStack oldTube = new ItemStack((ItemLike)ModItems.EMPTY_TUBE.get());
                if (!player.getInventory().add(oldTube)) {
                    player.drop(oldTube, false);
                }
            }
            if (PortalGunItem.hasCustomFluidColor(offhandStack)) {
                PortalGunItem.setFluidColor(stack, PortalGunItem.getFluidColor(offhandStack));
            } else {
                CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> tag.remove("FluidColor"));
            }
            if (PortalGunItem.hasCustomCoreColor(offhandStack)) {
                PortalGunItem.setCoreColor(stack, PortalGunItem.getCoreColor(offhandStack));
            } else {
                CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> tag.remove("CoreColor"));
            }
            PortalGunItem.setCharge(stack, tubeCharge);
            if (!player.getAbilities().instabuild) {
                offhandStack.shrink(1);
            }
            player.playSound((SoundEvent)ModSounds.BULB_INSERT.get(), 1.0f, 1.0f);
            player.getCooldowns().addCooldown((Item)this, 20);
            return InteractionResultHolder.sidedSuccess((Object)stack, (boolean)level.isClientSide());
        }
        if (player.isShiftKeyDown()) {
            if (!level.isClientSide && player instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)player;
                int slotIndex = hand == InteractionHand.MAIN_HAND ? player.getInventory().selected : 40;
                ModMessages.sendToPlayer((CustomPacketPayload)new S2COpenGunMenuPacket(PortalGunItem.getOrCreateGunTag(stack), slotIndex, Config.allowPlayerTargeting), (ServerPlayer)serverPlayer);
            }
            player.playSound((SoundEvent)ModSounds.MENU_OPEN.get(), 1.0f, 1.0f);
            return InteractionResultHolder.sidedSuccess((Object)stack, (boolean)level.isClientSide());
        }
        if (PortalManager.hasActiveProjectile((Player)player)) {
            if (!level.isClientSide && level instanceof ServerLevel) {
                ServerLevel serverLevel = (ServerLevel)level;
                PortalProjectileEntity activeProj = PortalManager.getActiveProjectile((Player)player);
                if (activeProj != null && activeProj.isAlive() && !activeProj.isRemoved()) {
                    Vec3 pos = activeProj.position();
                    float yaw = player.getYRot();
                    PortalManager.handleAirPortalSpawn((ServerLevel)serverLevel, (Vec3)pos, (float)yaw, (PortalProjectileEntity)activeProj);
                }
            }
            player.getCooldowns().addCooldown((Item)this, 6);
            return InteractionResultHolder.sidedSuccess((Object)stack, (boolean)level.isClientSide());
        }
        if (player.getCooldowns().isOnCooldown((Item)this)) {
            return InteractionResultHolder.pass((Object)stack);
        }
        int currentCharge = PortalGunItem.getCharge(stack);
        int n = chargeCost = PortalGunItem.hasUpgrade(stack, UpgradeType.FLUX_OPTIMIZER) ? 1 : 2;
        if (!PortalGunItem.hasInfinityBulb(stack) && currentCharge < chargeCost && !player.getAbilities().instabuild) {
            player.playSound((SoundEvent)ModSounds.GUN_EMPTY.get(), 1.0f, 1.0f);
            return InteractionResultHolder.fail((Object)stack);
        }
        if (!player.getAbilities().instabuild && !PortalGunItem.hasInfinityBulb(stack)) {
            PortalGunItem.consumeCharge(stack, chargeCost);
        }
        player.playSound((SoundEvent)ModSounds.GUN_FIRE.get(), 1.0f, 1.0f);
        if (level.isClientSide()) {
            ClientProxy.triggerRecoil((int)player.getId(), (int)(hand == InteractionHand.MAIN_HAND ? 0 : 1));
        } else if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            ModMessages.sendToNearby((CustomPacketPayload)new S2CGunRecoilPacket(player.getId(), (byte)(hand != InteractionHand.MAIN_HAND ? 1 : 0)), (Level)serverLevel, (double)player.getX(), (double)player.getY(), (double)player.getZ(), (double)64.0);
            float velocity = 3.2f;
            Vec3 look = player.getLookAngle();
            Vec3 playerVel = player.getDeltaMovement();
            double spawnX = player.getX() + look.x * 0.75;
            double spawnY = player.getEyeY() - 0.15 + look.y * 0.75;
            double spawnZ = player.getZ() + look.z * 0.75;
            PortalProjectileEntity projectile = new PortalProjectileEntity((Level)serverLevel, (LivingEntity)player);
            projectile.setPos(spawnX, spawnY, spawnZ);
            projectile.setColor(this.getColor(stack));
            projectile.setPortalScale(PortalGunItem.getScale(stack));
            projectile.setGunId(PortalGunItem.getGunId(stack));
            projectile.setGunMode(PortalGunItem.getMode(stack).getName());
            projectile.setAutoClose(PortalGunItem.isAutoClose(stack));
            projectile.setAutoAirDetonate(PortalGunItem.isAutoAirDetonate(stack));
            projectile.setAirDistance(PortalGunItem.getAirDistance(stack));
            projectile.setHazardShield(PortalGunItem.hasUpgrade(stack, UpgradeType.HAZARD_SHIELD));
            projectile.setSafePlacement(PortalGunItem.isSafePlacement(stack));
            if (PortalGunItem.getMode(stack) == PortalMode.CUSTOM) {
                projectile.setCustomLocation(PortalGunItem.getCustomLocation(stack));
            }
            Vec3 muzzleVel = look.scale((double)velocity);
            Vec3 totalVel = muzzleVel.add(playerVel);
            projectile.setDeltaMovement(totalVel);
            serverLevel.addFreshEntity((Entity)projectile);
            PortalManager.registerProjectile((Player)player, (PortalProjectileEntity)projectile);
        }
        player.getCooldowns().addCooldown((Item)this, 3);
        return InteractionResultHolder.sidedSuccess((Object)stack, (boolean)level.isClientSide());
    }

    public static ItemStack dismountGun(ItemStack gunStack, @Nullable Player player) {
        return PortalGunItem.unplugTube(gunStack, player);
    }

    public static ItemStack unplugTube(ItemStack gunStack, @Nullable Player player) {
        CustomData customData;
        Item item;
        Item item2;
        boolean isInfinity = PortalGunItem.hasInfinityBulb(gunStack);
        int charge = PortalGunItem.getCharge(gunStack);
        if (gunStack != null && (item2 = gunStack.getItem()) instanceof PortalGunItem) {
            PortalGunItem pgi = (PortalGunItem)item2;
            item = pgi.getDischargedVariant();
        } else {
            item = (Item)ModItems.RICK_PORTAL_GUN_DISCHARGED.get();
        }
        Item dischargedItem = item;
        ItemStack baseStack = new ItemStack((ItemLike)dischargedItem);
        CustomData customData2 = customData = gunStack != null ? (CustomData)gunStack.get(DataComponents.CUSTOM_DATA) : null;
        if (customData != null) {
            baseStack.set(DataComponents.CUSTOM_DATA, (Object)customData);
        }
        PortalGunItem.initializeGunNBT(baseStack);
        PortalGunItem.setInfinityBulb(baseStack, false);
        PortalGunItem.setCharge(baseStack, 0);
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)baseStack, tag -> {
            tag.remove("FluidColor");
            tag.remove("CoreColor");
            tag.remove("InfinityBulb");
        });
        int fluidColor = PortalGunItem.getFluidColor(gunStack);
        boolean hasCore = PortalGunItem.hasCustomCoreColor(gunStack);
        int coreColor = PortalGunItem.getCoreColor(gunStack);
        if (isInfinity) {
            ItemStack infinityStack = new ItemStack((ItemLike)ModItems.INFINITY_PORTAL_FLUID.get());
            if (PortalGunItem.hasCustomFluidColor(gunStack)) {
                PortalGunItem.setFluidColor(infinityStack, fluidColor);
            }
            if (hasCore) {
                PortalGunItem.setCoreColor(infinityStack, coreColor);
            }
            if (player != null && !player.getInventory().add(infinityStack)) {
                player.drop(infinityStack, false);
            }
        } else if (charge > 0) {
            ItemStack fluidStack = new ItemStack((ItemLike)ModItems.PORTAL_FLUID.get());
            CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)fluidStack, tag -> tag.putInt("Charge", charge));
            if (PortalGunItem.hasCustomFluidColor(gunStack)) {
                PortalGunItem.setFluidColor(fluidStack, fluidColor);
            }
            if (hasCore) {
                PortalGunItem.setCoreColor(fluidStack, coreColor);
            }
            if (player != null && !player.getInventory().add(fluidStack)) {
                player.drop(fluidStack, false);
            }
        } else {
            ItemStack emptyTube = new ItemStack((ItemLike)ModItems.EMPTY_TUBE.get());
            if (player != null && !player.getInventory().add(emptyTube)) {
                player.drop(emptyTube, false);
            }
        }
        if (player == null) return baseStack;
        player.playSound((SoundEvent)ModSounds.BULB_EJECT.get(), 1.0f, 1.0f);
        return baseStack;
    }

    public static Item getChargedVariant() {
        return (Item)ModItems.RICK_PORTAL_GUN.get();
    }

    public Item getDischargedVariant() {
        return (Item)ModItems.RICK_PORTAL_GUN_DISCHARGED.get();
    }

    public static Item getMatchingFluidTube() {
        return (Item)ModItems.PORTAL_FLUID.get();
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        int charge = PortalGunItem.getCharge(stack);
        PortalMode mode = PortalGunItem.getMode(stack);
        float scale = PortalGunItem.getScale(stack);
        int gunId = PortalGunItem.getGunId(stack);
        if (PortalGunItem.hasInfinityBulb(stack)) {
            tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.gun_charge.infinite").withStyle(ChatFormatting.GREEN));
        } else {
            tooltip.add((Component)Component.literal((String)("Charge: " + charge + "%")).withStyle(charge > 20 ? ChatFormatting.GREEN : ChatFormatting.RED));
        }
        tooltip.add((Component)Component.literal((String)("Mode: " + mode.getDisplayName())).withStyle(ChatFormatting.AQUA));
        tooltip.add((Component)Component.literal((String)("Scale: " + scale + "x")).withStyle(ChatFormatting.GRAY));
        tooltip.add((Component)Component.literal((String)("ID: #" + gunId)).withStyle(ChatFormatting.DARK_GRAY));
        if (PortalGunItem.hasMemoryCard(stack)) {
            int locCount = PortalGunItem.getSavedLocations(stack).size();
            tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.gun_memory_card.installed", (Object[])new Object[]{locCount}).withStyle(ChatFormatting.GREEN));
        } else {
            tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.gun_memory_card.missing").withStyle(ChatFormatting.DARK_GRAY));
        }
        List<UpgradeType> upgrades = PortalGunItem.getUpgrades(stack);
        if (upgrades.isEmpty()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.gun_upgrades.none", (Object[])new Object[]{PortalGunItem.getMaxUpgrades()}).withStyle(ChatFormatting.DARK_GRAY));
            return;
        }
        tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.gun_upgrades.installed", (Object[])new Object[]{upgrades.size(), PortalGunItem.getMaxUpgrades()}).withStyle(ChatFormatting.GOLD));
        Iterator<UpgradeType> iterator = upgrades.iterator();
        while (iterator.hasNext()) {
            UpgradeType upgrade = iterator.next();
            tooltip.add((Component)Component.literal((String)" \u2022 ").withStyle(ChatFormatting.DARK_GRAY).append((Component)Component.translatable((String)upgrade.getTranslationKey()).withStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)(upgrade.getColor() & 0xFFFFFF))))));
        }
    }

    public boolean isBarVisible(ItemStack stack) {
        if (PortalGunItem.hasInfinityBulb(stack)) return false;
        if (!(stack.getItem() instanceof CreativePortalGunItem)) int charge;
        return (charge = PortalGunItem.getCharge(stack)) < 100;
        return false;
    }

    public int getBarWidth(ItemStack stack) {
        int charge = PortalGunItem.getCharge(stack);
        return Math.round(13.0f * (float)charge / 100.0f);
    }

    public int getBarColor(ItemStack stack) {
        int charge = PortalGunItem.getCharge(stack);
        return charge > 25 ? 65382 : (charge > 10 ? -17664 : -56798);
    }

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (level.isClientSide) return;
        if (!(entity instanceof Player)) return;
        Player player = (Player)entity;
        if (!stack.has(DataComponents.CUSTOM_DATA)) return;
        if (stack.getItem() instanceof CreativePortalGunItem) return;
        PortalGunItem.trimExcessUpgrades(stack, player);
    }

    public static void trimExcessUpgrades(ItemStack gunStack, @Nullable Player player) {
        if (gunStack == null) return;
        if (gunStack.isEmpty()) return;
        if (!gunStack.has(DataComponents.CUSTOM_DATA)) {
            return;
        }
        if (gunStack.getItem() instanceof CreativePortalGunItem) {
            return;
        }
        int maxAllowed = PortalGunItem.getMaxUpgrades();
        List<UpgradeType> upgrades = PortalGunItem.getUpgrades(gunStack);
        if (upgrades.size() <= maxAllowed) return;
        while (true) {
            if (upgrades.size() <= maxAllowed) {
                if (player == null) return;
                player.inventoryMenu.broadcastChanges();
                return;
            }
            int removeIdx = upgrades.size() - 1;
            ItemStack card = PortalGunItem.ejectUpgrade(gunStack, removeIdx, null);
            upgrades.remove(removeIdx);
            if (player == null || card == null || card.isEmpty() || player.getInventory().add(card)) continue;
            player.drop(card, false);
        }
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (!slotChanged) return oldStack.getItem() != newStack.getItem();
        return true;
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions)new /* Unavailable Anonymous Inner Class!! */);
    }
}

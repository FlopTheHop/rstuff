/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.component.DataComponentType
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.Style
 *  net.minecraft.network.chat.TextColor
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.component.CustomData
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.neoforged.neoforge.client.extensions.common.IClientItemExtensions
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.ExpansionCardItem
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.UpgradeType
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.S2COpenGunMenuPacket
 *  org.ExtraTortik.ricks_stuff.portal.PortalMode
 */
package org.ExtraTortik.ricks_stuff.item;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.init.ModItems;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.ExpansionCardItem;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.ExtraTortik.ricks_stuff.item.UpgradeType;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.S2COpenGunMenuPacket;
import org.ExtraTortik.ricks_stuff.portal.PortalMode;

public class DischargedPortalGunItem
extends Item {
    public DischargedPortalGunItem(Item.Properties properties) {
        super(properties);
    }

    public Item getChargedGunItem() {
        return (Item)ModItems.RICK_PORTAL_GUN.get();
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        Item fluidTag2;
        ItemStack stack = player.getItemInHand(hand);
        if (!player.isShiftKeyDown() && player.getCooldowns().isOnCooldown((Item)this)) {
            return InteractionResultHolder.pass((Object)stack);
        }
        InteractionHand otherHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack offhandStack = player.getItemInHand(otherHand);
        if (!offhandStack.isEmpty() && offhandStack.is((Item)ModItems.INFINITY_PORTAL_FLUID.get())) {
            ItemStack chargedGun = new ItemStack((ItemLike)this.getChargedGunItem());
            CustomData gunData = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
            if (gunData != null) {
                chargedGun.set(DataComponents.CUSTOM_DATA, (Object)gunData);
            }
            PortalGunItem.initializeGunNBT((ItemStack)chargedGun);
            PortalGunItem.setInfinityBulb((ItemStack)chargedGun, (boolean)true);
            PortalGunItem.setCharge((ItemStack)chargedGun, (int)100);
            if (PortalGunItem.hasCustomFluidColor((ItemStack)offhandStack)) {
                PortalGunItem.setFluidColor((ItemStack)chargedGun, (int)PortalGunItem.getFluidColor((ItemStack)offhandStack));
            } else {
                CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)chargedGun, tag -> tag.remove("FluidColor"));
            }
            if (PortalGunItem.hasCustomCoreColor((ItemStack)offhandStack)) {
                PortalGunItem.setCoreColor((ItemStack)chargedGun, (int)PortalGunItem.getCoreColor((ItemStack)offhandStack));
            } else {
                CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)chargedGun, tag -> tag.remove("CoreColor"));
            }
            if (!player.getAbilities().instabuild) {
                offhandStack.shrink(1);
            }
            player.playSound((SoundEvent)ModSounds.BULB_INSERT.get(), 1.0f, 1.0f);
            player.getCooldowns().addCooldown((Item)this, 20);
            return InteractionResultHolder.sidedSuccess((Object)chargedGun, (boolean)level.isClientSide());
        }
        if (!offhandStack.isEmpty() && offhandStack.is((Item)ModItems.PORTAL_FLUID.get())) {
            int tubeCharge = 100;
            CompoundTag fluidTag2 = PortalGunItem.getGunTag((ItemStack)offhandStack);
            if (fluidTag2 != null && fluidTag2.contains("Charge")) {
                tubeCharge = fluidTag2.getInt("Charge");
            }
            ItemStack chargedGun = new ItemStack((ItemLike)this.getChargedGunItem());
            CustomData gunData = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
            if (gunData != null) {
                chargedGun.set(DataComponents.CUSTOM_DATA, (Object)gunData);
            }
            PortalGunItem.initializeGunNBT((ItemStack)chargedGun);
            PortalGunItem.setInfinityBulb((ItemStack)chargedGun, (boolean)false);
            PortalGunItem.setCharge((ItemStack)chargedGun, (int)tubeCharge);
            if (PortalGunItem.hasCustomFluidColor((ItemStack)offhandStack)) {
                PortalGunItem.setFluidColor((ItemStack)chargedGun, (int)PortalGunItem.getFluidColor((ItemStack)offhandStack));
            } else {
                CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)chargedGun, tag -> tag.remove("FluidColor"));
            }
            if (PortalGunItem.hasCustomCoreColor((ItemStack)offhandStack)) {
                PortalGunItem.setCoreColor((ItemStack)chargedGun, (int)PortalGunItem.getCoreColor((ItemStack)offhandStack));
            } else {
                CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)chargedGun, tag -> tag.remove("CoreColor"));
            }
            if (!player.getAbilities().instabuild) {
                offhandStack.shrink(1);
            }
            player.playSound((SoundEvent)ModSounds.BULB_INSERT.get(), 1.0f, 1.0f);
            player.getCooldowns().addCooldown((Item)this, 20);
            return InteractionResultHolder.sidedSuccess((Object)chargedGun, (boolean)level.isClientSide());
        }
        if (!offhandStack.isEmpty() && offhandStack.is((Item)ModItems.MEMORY_CARD.get()) && !PortalGunItem.hasMemoryCard((ItemStack)stack) && PortalGunItem.insertMemoryCard((ItemStack)stack, (ItemStack)offhandStack)) {
            if (!player.getAbilities().instabuild) {
                offhandStack.shrink(1);
            }
            player.playSound((SoundEvent)ModSounds.UI_PRESS.get(), 1.0f, 1.4f);
            player.getCooldowns().addCooldown((Item)this, 10);
            return InteractionResultHolder.sidedSuccess((Object)stack, (boolean)level.isClientSide());
        }
        if (!offhandStack.isEmpty() && (fluidTag2 = offhandStack.getItem()) instanceof ExpansionCardItem) {
            ExpansionCardItem cardItem = (ExpansionCardItem)fluidTag2;
            UpgradeType type = cardItem.getUpgradeType();
            if (!PortalGunItem.canInstallUpgrade((ItemStack)stack, (UpgradeType)type)) {
                player.playSound((SoundEvent)ModSounds.GUN_EMPTY.get(), 1.0f, 1.0f);
                return InteractionResultHolder.fail((Object)stack);
            }
            if (PortalGunItem.installUpgrade((ItemStack)stack, (UpgradeType)type)) {
                if (!player.getAbilities().instabuild) {
                    offhandStack.shrink(1);
                }
                player.playSound((SoundEvent)ModSounds.UI_PRESS.get(), 1.0f, 1.4f);
                player.getCooldowns().addCooldown((Item)this, 10);
                return InteractionResultHolder.sidedSuccess((Object)stack, (boolean)level.isClientSide());
            }
        }
        if (!player.isShiftKeyDown()) {
            player.playSound((SoundEvent)ModSounds.GUN_EMPTY.get(), 1.0f, 1.0f);
            return InteractionResultHolder.fail((Object)stack);
        }
        if (!level.isClientSide() && player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)player;
            int slotIndex = hand == InteractionHand.MAIN_HAND ? player.getInventory().selected : 40;
            ModMessages.sendToPlayer((CustomPacketPayload)new S2COpenGunMenuPacket(PortalGunItem.getOrCreateGunTag((ItemStack)stack), slotIndex, Config.allowPlayerTargeting), (ServerPlayer)serverPlayer);
        }
        player.playSound((SoundEvent)ModSounds.MENU_OPEN.get(), 1.0f, 1.0f);
        return InteractionResultHolder.sidedSuccess((Object)stack, (boolean)level.isClientSide());
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        int charge = PortalGunItem.getCharge((ItemStack)stack);
        PortalMode mode = PortalGunItem.getMode((ItemStack)stack);
        float scale = PortalGunItem.getScale((ItemStack)stack);
        int gunId = PortalGunItem.getGunId((ItemStack)stack);
        tooltip.add((Component)Component.literal((String)("Charge: " + charge + "% (Discharged)")).withStyle(ChatFormatting.RED));
        tooltip.add((Component)Component.literal((String)("Mode: " + mode.getDisplayName())).withStyle(ChatFormatting.AQUA));
        tooltip.add((Component)Component.literal((String)("Scale: " + scale + "x")).withStyle(ChatFormatting.GRAY));
        tooltip.add((Component)Component.literal((String)("ID: #" + gunId)).withStyle(ChatFormatting.DARK_GRAY));
        if (PortalGunItem.hasMemoryCard((ItemStack)stack)) {
            int locCount = PortalGunItem.getSavedLocations((ItemStack)stack).size();
            tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.gun_memory_card.installed", (Object[])new Object[]{locCount}).withStyle(ChatFormatting.GREEN));
        } else {
            tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.gun_memory_card.missing").withStyle(ChatFormatting.DARK_GRAY));
        }
        List upgrades = PortalGunItem.getUpgrades((ItemStack)stack);
        if (upgrades.isEmpty()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.gun_upgrades.none", (Object[])new Object[]{PortalGunItem.getMaxUpgrades()}).withStyle(ChatFormatting.DARK_GRAY));
            return;
        }
        tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.gun_upgrades.installed", (Object[])new Object[]{upgrades.size(), PortalGunItem.getMaxUpgrades()}).withStyle(ChatFormatting.GOLD));
        Iterator iterator = upgrades.iterator();
        while (iterator.hasNext()) {
            UpgradeType upgrade = (UpgradeType)iterator.next();
            tooltip.add((Component)Component.literal((String)" \u2022 ").withStyle(ChatFormatting.DARK_GRAY).append((Component)Component.translatable((String)upgrade.getTranslationKey()).withStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)(upgrade.getColor() & 0xFFFFFF))))));
        }
    }

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (level.isClientSide) return;
        if (!(entity instanceof Player)) return;
        Player player = (Player)entity;
        if (!stack.has(DataComponents.CUSTOM_DATA)) return;
        PortalGunItem.trimExcessUpgrades((ItemStack)stack, (Player)player);
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (!slotChanged) return oldStack.getItem() != newStack.getItem();
        return true;
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions)new /* Unavailable Anonymous Inner Class!! */);
    }
}

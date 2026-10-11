/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.component.DataComponentType
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResultHolder
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
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.CreativePortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 */
package org.ExtraTortik.ricks_stuff.init;

import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.ExtraTortik.ricks_stuff.init.ModItems;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.CreativePortalGunItem;
import org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;

class ModItems.1
extends Item {
    ModItems.1(Item.Properties arg0) {
        super(arg0);
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        CompoundTag tag = PortalGunItem.getGunTag((ItemStack)stack);
        int charge = tag != null && tag.contains("Charge") ? tag.getInt("Charge") : 100;
        tooltip.add((Component)Component.literal((String)("Charge: " + charge + "%")).withStyle(charge > 20 ? ChatFormatting.GREEN : ChatFormatting.RED));
        if (PortalGunItem.hasCustomFluidColor((ItemStack)stack)) {
            int rgb = PortalGunItem.getFluidColor((ItemStack)stack);
            tooltip.add((Component)Component.literal((String)String.format("Fluid: #%06X", rgb & 0xFFFFFF)).withStyle(ChatFormatting.GRAY));
        }
        if (!PortalGunItem.hasCustomCoreColor((ItemStack)stack)) return;
        int coreRgb = PortalGunItem.getCoreColor((ItemStack)stack);
        tooltip.add((Component)Component.literal((String)String.format("Core: #%06X", coreRgb & 0xFFFFFF)).withStyle(ChatFormatting.DARK_AQUA));
    }

    public boolean isBarVisible(ItemStack stack) {
        CompoundTag tag = PortalGunItem.getGunTag((ItemStack)stack);
        int charge = tag != null && tag.contains("Charge") ? tag.getInt("Charge") : 100;
        return charge < 100;
    }

    public int getBarWidth(ItemStack stack) {
        CompoundTag tag = PortalGunItem.getGunTag((ItemStack)stack);
        int charge = tag != null && tag.contains("Charge") ? tag.getInt("Charge") : 100;
        return Math.round(13.0f * (float)charge / 100.0f);
    }

    public int getBarColor(ItemStack stack) {
        int charge;
        CompoundTag tag = PortalGunItem.getGunTag((ItemStack)stack);
        int n = charge = tag != null && tag.contains("Charge") ? tag.getInt("Charge") : 100;
        return charge > 25 ? 65382 : (charge > 10 ? -17664 : -56798);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        int oldCharge;
        ItemStack stack = player.getItemInHand(hand);
        InteractionHand otherHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack otherStack = player.getItemInHand(otherHand);
        if (otherStack.isEmpty()) return InteractionResultHolder.pass((Object)stack);
        Item item = otherStack.getItem();
        if (item instanceof DischargedPortalGunItem) {
            DischargedPortalGunItem dpg = (DischargedPortalGunItem)item;
            int tubeCharge = 100;
            CompoundTag fluidTag = PortalGunItem.getGunTag((ItemStack)stack);
            if (fluidTag != null && fluidTag.contains("Charge")) {
                tubeCharge = fluidTag.getInt("Charge");
            }
            ItemStack chargedGun = new ItemStack((ItemLike)dpg.getChargedGunItem());
            CustomData gunData = (CustomData)otherStack.get(DataComponents.CUSTOM_DATA);
            if (gunData != null) {
                chargedGun.set(DataComponents.CUSTOM_DATA, (Object)gunData);
            }
            PortalGunItem.initializeGunNBT((ItemStack)chargedGun);
            PortalGunItem.setInfinityBulb((ItemStack)chargedGun, (boolean)false);
            PortalGunItem.setCharge((ItemStack)chargedGun, (int)tubeCharge);
            if (PortalGunItem.hasCustomFluidColor((ItemStack)stack)) {
                PortalGunItem.setFluidColor((ItemStack)chargedGun, (int)PortalGunItem.getFluidColor((ItemStack)stack));
            } else {
                CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)chargedGun, tag -> tag.remove("FluidColor"));
            }
            if (PortalGunItem.hasCustomCoreColor((ItemStack)stack)) {
                PortalGunItem.setCoreColor((ItemStack)chargedGun, (int)PortalGunItem.getCoreColor((ItemStack)stack));
            } else {
                CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)chargedGun, tag -> tag.remove("CoreColor"));
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            player.setItemInHand(otherHand, chargedGun);
            player.playSound((SoundEvent)ModSounds.BULB_INSERT.get(), 1.0f, 1.0f);
            player.getCooldowns().addCooldown((Item)this, 20);
            return InteractionResultHolder.sidedSuccess((Object)stack, (boolean)level.isClientSide());
        }
        if (!(otherStack.getItem() instanceof PortalGunItem)) return InteractionResultHolder.pass((Object)stack);
        if (otherStack.getItem() instanceof CreativePortalGunItem) return InteractionResultHolder.pass((Object)stack);
        if (PortalGunItem.hasInfinityBulb((ItemStack)otherStack)) return InteractionResultHolder.pass((Object)stack);
        if (PortalGunItem.getCharge((ItemStack)otherStack) >= 100) return InteractionResultHolder.pass((Object)stack);
        int tubeCharge = 100;
        CompoundTag fluidTag = PortalGunItem.getGunTag((ItemStack)stack);
        if (fluidTag != null && fluidTag.contains("Charge")) {
            tubeCharge = fluidTag.getInt("Charge");
        }
        if ((oldCharge = PortalGunItem.getCharge((ItemStack)otherStack)) > 0) {
            ItemStack oldTube = new ItemStack((ItemLike)ModItems.PORTAL_FLUID.get());
            CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)oldTube, tag -> tag.putInt("Charge", oldCharge));
            if (PortalGunItem.hasCustomFluidColor((ItemStack)otherStack)) {
                PortalGunItem.setFluidColor((ItemStack)oldTube, (int)PortalGunItem.getFluidColor((ItemStack)otherStack));
            }
            if (PortalGunItem.hasCustomCoreColor((ItemStack)otherStack)) {
                PortalGunItem.setCoreColor((ItemStack)oldTube, (int)PortalGunItem.getCoreColor((ItemStack)otherStack));
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
        PortalGunItem.setCharge((ItemStack)otherStack, (int)tubeCharge);
        if (PortalGunItem.hasCustomFluidColor((ItemStack)stack)) {
            PortalGunItem.setFluidColor((ItemStack)otherStack, (int)PortalGunItem.getFluidColor((ItemStack)stack));
        } else {
            CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)otherStack, tag -> tag.remove("FluidColor"));
        }
        if (PortalGunItem.hasCustomCoreColor((ItemStack)stack)) {
            PortalGunItem.setCoreColor((ItemStack)otherStack, (int)PortalGunItem.getCoreColor((ItemStack)stack));
        } else {
            CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)otherStack, tag -> tag.remove("CoreColor"));
        }
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        player.playSound((SoundEvent)ModSounds.BULB_INSERT.get(), 1.0f, 1.0f);
        player.getCooldowns().addCooldown((Item)this, 20);
        return InteractionResultHolder.sidedSuccess((Object)stack, (boolean)level.isClientSide());
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions)new /* Unavailable Anonymous Inner Class!! */);
    }
}

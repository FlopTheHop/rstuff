/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.Style
 *  net.minecraft.network.chat.TextColor
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.UpgradeType
 */
package org.ExtraTortik.ricks_stuff.item;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.ExtraTortik.ricks_stuff.item.UpgradeType;

public class ExpansionCardItem
extends Item {
    private final UpgradeType upgradeType;

    public ExpansionCardItem(Item.Properties properties, UpgradeType upgradeType) {
        super(properties);
        this.upgradeType = upgradeType;
    }

    public UpgradeType getUpgradeType() {
        return this.upgradeType;
    }

    public Component getName(ItemStack stack) {
        if (this.upgradeType == null) return super.getName(stack);
        return Component.translatable((String)this.getDescriptionId(stack)).withStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)(this.upgradeType.getColor() & 0xFFFFFF))));
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        if (this.upgradeType != null) {
            tooltip.add((Component)Component.translatable((String)this.upgradeType.getDescriptionKey()).withStyle(ChatFormatting.GRAY));
        }
        tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.expansion_card.gun_limit", (Object[])new Object[]{PortalGunItem.getMaxUpgrades()}).withStyle(ChatFormatting.DARK_GRAY));
        tooltip.add((Component)Component.translatable((String)"tooltip.ricks_stuff.expansion_card.install_hint").withStyle(ChatFormatting.GRAY));
    }
}

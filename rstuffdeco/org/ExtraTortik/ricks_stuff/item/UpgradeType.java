/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 *  org.ExtraTortik.ricks_stuff.item.ExpansionCardItem
 */
package org.ExtraTortik.ricks_stuff.item;

import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.ExtraTortik.ricks_stuff.init.ModItems;
import org.ExtraTortik.ricks_stuff.item.ExpansionCardItem;
import org.jetbrains.annotations.Nullable;

public enum UpgradeType {
    DIMENSIONAL("dimensional", "item.ricks_stuff.dimensional_card", "tooltip.ricks_stuff.dimensional_card.desc", -4177665, () -> (Item)ModItems.DIMENSIONAL_CARD.get()),
    PLAYER_LOCATOR("player_locator", "item.ricks_stuff.player_locator_card", "tooltip.ricks_stuff.player_locator_card.desc", -13183000, () -> (Item)ModItems.PLAYER_LOCATOR_CARD.get()),
    FLUX_OPTIMIZER("flux_optimizer", "item.ricks_stuff.flux_optimizer_card", "tooltip.ricks_stuff.flux_optimizer_card.desc", -12720539, () -> (Item)ModItems.FLUX_OPTIMIZER_CARD.get()),
    HAZARD_SHIELD("hazard_shield", "item.ricks_stuff.hazard_shield_card", "tooltip.ricks_stuff.hazard_shield_card.desc", -26334, () -> (Item)ModItems.HAZARD_SHIELD_CARD.get()),
    STRUCTURE_SCANNER("structure_scanner", "item.ricks_stuff.structure_scanner_card", "tooltip.ricks_stuff.structure_scanner_card.desc", -16711766, () -> (Item)ModItems.STRUCTURE_SCANNER_CARD.get()),
    BIOME_LOCATOR("biome_locator", "item.ricks_stuff.biome_locator_card", "tooltip.ricks_stuff.biome_locator_card.desc", -13707917, () -> (Item)ModItems.BIOME_LOCATOR_CARD.get());

    private final String id;
    private final String translationKey;
    private final String descriptionKey;
    private final int color;
    private final Supplier<Item> itemSupplier;

    private UpgradeType(String id, String translationKey, String descriptionKey, int color, Supplier<Item> itemSupplier) {
        this.id = id;
        this.translationKey = translationKey;
        this.descriptionKey = descriptionKey;
        this.color = color;
        this.itemSupplier = itemSupplier;
    }

    public String getId() {
        return this.id;
    }

    public String getTranslationKey() {
        return this.translationKey;
    }

    public String getDescriptionKey() {
        return this.descriptionKey;
    }

    public Component getDisplayName() {
        return Component.translatable((String)this.translationKey);
    }

    public String getDescription() {
        return Component.translatable((String)this.descriptionKey).getString();
    }

    public int getColor() {
        return this.color;
    }

    public Item getItem() {
        return this.itemSupplier.get();
    }

    @Nullable
    public static UpgradeType fromId(@Nullable String id) {
        if (id == null) return null;
        if (id.isEmpty()) {
            return null;
        }
        UpgradeType[] upgradeTypeArray = UpgradeType.values();
        int n = upgradeTypeArray.length;
        int n2 = 0;
        while (n2 < n) {
            UpgradeType type = upgradeTypeArray[n2];
            if (type.id.equalsIgnoreCase(id)) return type;
            if (type.name().equalsIgnoreCase(id)) {
                return type;
            }
            ++n2;
        }
        return null;
    }

    @Nullable
    public static UpgradeType fromItem(@Nullable Item item) {
        if (item instanceof ExpansionCardItem) {
            ExpansionCardItem cardItem = (ExpansionCardItem)item;
            return cardItem.getUpgradeType();
        }
        if (item == null) {
            return null;
        }
        UpgradeType[] upgradeTypeArray = UpgradeType.values();
        int n = upgradeTypeArray.length;
        int n2 = 0;
        while (n2 < n) {
            UpgradeType type = upgradeTypeArray[n2];
            if (type.getItem() == item) {
                return type;
            }
            ++n2;
        }
        return null;
    }

    @Nullable
    public static UpgradeType fromStack(@Nullable ItemStack stack) {
        if (stack == null) return null;
        if (!stack.isEmpty()) return UpgradeType.fromItem(stack.getItem());
        return null;
    }
}

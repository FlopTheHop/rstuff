/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.item.CreativeModeTab
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 *  org.ExtraTortik.ricks_stuff.init.ModBlocks
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 */
package org.ExtraTortik.ricks_stuff.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.ExtraTortik.ricks_stuff.init.ModBlocks;
import org.ExtraTortik.ricks_stuff.init.ModItems;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create((ResourceKey)Registries.CREATIVE_MODE_TAB, (String)"ricks_stuff");
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RICKS_STUFF_TAB = CREATIVE_MODE_TABS.register("ricks_stuff_tab", () -> CreativeModeTab.builder().title((Component)Component.translatable((String)"itemGroup.ricks_stuff")).icon(() -> new ItemStack((ItemLike)ModItems.RICK_PORTAL_GUN.get())).displayItems((parameters, output) -> {
        output.accept((ItemLike)ModItems.RICK_PORTAL_GUN.get());
        output.accept((ItemLike)ModItems.RICK_PORTAL_GUN_CREATIVE.get());
        output.accept((ItemLike)ModItems.RICK_PORTAL_GUN_DISCHARGED.get());
        output.accept((ItemLike)ModItems.EVIL_MORTY_PORTAL_GUN.get());
        output.accept((ItemLike)ModItems.EVIL_MORTY_PORTAL_GUN_DISCHARGED.get());
        output.accept((ItemLike)ModItems.PORTAL_FLUID.get());
        output.accept((ItemLike)ModItems.INFINITY_PORTAL_FLUID.get());
        output.accept((ItemLike)ModItems.EMPTY_TUBE.get());
        output.accept((ItemLike)ModItems.QUANTUM_ISOTOPE.get());
        output.accept((ItemLike)ModItems.QUANTUM_CHIP.get());
        output.accept((ItemLike)ModItems.DARK_MATTER_DUST.get());
        output.accept((ItemLike)ModItems.MEMORY_CARD.get());
        output.accept((ItemLike)ModItems.DIMENSIONAL_CARD.get());
        output.accept((ItemLike)ModItems.PLAYER_LOCATOR_CARD.get());
        output.accept((ItemLike)ModItems.FLUX_OPTIMIZER_CARD.get());
        output.accept((ItemLike)ModItems.HAZARD_SHIELD_CARD.get());
        output.accept((ItemLike)ModItems.STRUCTURE_SCANNER_CARD.get());
        output.accept((ItemLike)ModItems.BIOME_LOCATOR_CARD.get());
        output.accept((ItemLike)ModItems.MICROVERSE_BATTERY.get());
        output.accept((ItemLike)ModItems.SPACE_CRUISER.get());
        output.accept((ItemLike)ModItems.RICK_PRIME_BOMB.get());
        output.accept((ItemLike)ModItems.RICK_PRIME_BOMB_REMOTE.get());
        output.accept((ItemLike)ModBlocks.PHOENIX_CAPSULE.get());
        output.accept((ItemLike)ModBlocks.ANTI_PORTAL_FIELD_GEN.get());
        output.accept((ItemLike)ModItems.TACHYON_INHIBITOR_CORE.get());
        output.accept((ItemLike)ModItems.SUBQUANTUM_RESONATOR.get());
        output.accept((ItemLike)ModItems.REAL_ACID_BUCKET.get());
        output.accept((ItemLike)ModItems.FAKE_ACID_BUCKET.get());
        output.accept((ItemLike)ModBlocks.OMEGA_CORE.get());
        output.accept((ItemLike)ModItems.OMEGA_CONTROL_TABLET.get());
        output.accept((ItemLike)ModBlocks.PORTAL_FLUID_COLORIZER.get());
        output.accept((ItemLike)ModItems.EMPTY_DNA_FLASK.get());
        output.accept((ItemLike)ModItems.DNA_SAMPLE_FLASK.get());
        output.accept((ItemLike)ModItems.EYE_PATCH.get());
        output.accept((ItemLike)ModItems.BIO_NEURAL_RECEIVER.get());
        output.accept((ItemLike)ModItems.EVIL_MORTY_ENERGY_SHIELD.get());
    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}

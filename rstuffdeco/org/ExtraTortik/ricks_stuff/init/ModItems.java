/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.BucketItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.Rarity
 *  net.minecraft.world.level.material.Fluid
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredItem
 *  net.neoforged.neoforge.registries.DeferredRegister
 *  net.neoforged.neoforge.registries.DeferredRegister$Items
 *  org.ExtraTortik.ricks_stuff.init.ModFluids
 *  org.ExtraTortik.ricks_stuff.item.BioNeuralReceiverItem
 *  org.ExtraTortik.ricks_stuff.item.CreativePortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.DnaSampleFlaskItem
 *  org.ExtraTortik.ricks_stuff.item.EmptyDnaFlaskItem
 *  org.ExtraTortik.ricks_stuff.item.EvilMortyDischargedPortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.EvilMortyEnergyShieldItem
 *  org.ExtraTortik.ricks_stuff.item.EvilMortyPortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.ExpansionCardItem
 *  org.ExtraTortik.ricks_stuff.item.EyePatchItem
 *  org.ExtraTortik.ricks_stuff.item.MemoryCardItem
 *  org.ExtraTortik.ricks_stuff.item.MicroverseBatteryItem
 *  org.ExtraTortik.ricks_stuff.item.OmegaControlTabletItem
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.RickPrimeBombItem
 *  org.ExtraTortik.ricks_stuff.item.RickPrimeBombRemoteItem
 *  org.ExtraTortik.ricks_stuff.item.SpaceCruiserItem
 *  org.ExtraTortik.ricks_stuff.item.UpgradeType
 */
package org.ExtraTortik.ricks_stuff.init;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.ExtraTortik.ricks_stuff.init.ModFluids;
import org.ExtraTortik.ricks_stuff.item.BioNeuralReceiverItem;
import org.ExtraTortik.ricks_stuff.item.CreativePortalGunItem;
import org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem;
import org.ExtraTortik.ricks_stuff.item.DnaSampleFlaskItem;
import org.ExtraTortik.ricks_stuff.item.EmptyDnaFlaskItem;
import org.ExtraTortik.ricks_stuff.item.EvilMortyDischargedPortalGunItem;
import org.ExtraTortik.ricks_stuff.item.EvilMortyEnergyShieldItem;
import org.ExtraTortik.ricks_stuff.item.EvilMortyPortalGunItem;
import org.ExtraTortik.ricks_stuff.item.ExpansionCardItem;
import org.ExtraTortik.ricks_stuff.item.EyePatchItem;
import org.ExtraTortik.ricks_stuff.item.MemoryCardItem;
import org.ExtraTortik.ricks_stuff.item.MicroverseBatteryItem;
import org.ExtraTortik.ricks_stuff.item.OmegaControlTabletItem;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.ExtraTortik.ricks_stuff.item.RickPrimeBombItem;
import org.ExtraTortik.ricks_stuff.item.RickPrimeBombRemoteItem;
import org.ExtraTortik.ricks_stuff.item.SpaceCruiserItem;
import org.ExtraTortik.ricks_stuff.item.UpgradeType;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems((String)"ricks_stuff");
    public static final DeferredItem<Item> RICK_PORTAL_GUN = ITEMS.register("rick_portal_gun", () -> new PortalGunItem(new Item.Properties().stacksTo(1).fireResistant()));
    public static final DeferredItem<Item> RICK_PORTAL_GUN_DISCHARGED = ITEMS.register("rick_portal_gun_discharged", () -> new DischargedPortalGunItem(new Item.Properties().stacksTo(1).fireResistant()));
    public static final DeferredItem<Item> RICK_PORTAL_GUN_CREATIVE = ITEMS.register("rick_portal_gun_creative", () -> new CreativePortalGunItem(new Item.Properties().stacksTo(1).fireResistant()));
    public static final DeferredItem<Item> EVIL_MORTY_PORTAL_GUN = ITEMS.register("evil_morty_portal_gun", () -> new EvilMortyPortalGunItem(new Item.Properties().stacksTo(1).fireResistant()));
    public static final DeferredItem<Item> EVIL_MORTY_PORTAL_GUN_DISCHARGED = ITEMS.register("evil_morty_portal_gun_discharged", () -> new EvilMortyDischargedPortalGunItem(new Item.Properties().stacksTo(1).fireResistant()));
    public static final DeferredItem<Item> PORTAL_FLUID = ITEMS.register("portal_fluid", () -> new /* Unavailable Anonymous Inner Class!! */);
    public static final DeferredItem<Item> INFINITY_PORTAL_FLUID = ITEMS.register("infinity_portal_fluid", () -> new /* Unavailable Anonymous Inner Class!! */);
    public static final DeferredItem<Item> EMPTY_TUBE = ITEMS.register("empty_tube", () -> new /* Unavailable Anonymous Inner Class!! */);
    public static final DeferredItem<Item> QUANTUM_ISOTOPE = ITEMS.register("quantum_isotope", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> QUANTUM_CHIP = ITEMS.register("quantum_chip", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> DARK_MATTER_DUST = ITEMS.register("dark_matter_dust", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> MEMORY_CARD = ITEMS.register("memory_card", () -> new MemoryCardItem(new Item.Properties().stacksTo(1).fireResistant()));
    public static final DeferredItem<Item> DIMENSIONAL_CARD = ITEMS.register("dimensional_card", () -> new ExpansionCardItem(new Item.Properties().stacksTo(1).fireResistant(), UpgradeType.DIMENSIONAL));
    public static final DeferredItem<Item> PLAYER_LOCATOR_CARD = ITEMS.register("player_locator_card", () -> new ExpansionCardItem(new Item.Properties().stacksTo(1).fireResistant(), UpgradeType.PLAYER_LOCATOR));
    public static final DeferredItem<Item> FLUX_OPTIMIZER_CARD = ITEMS.register("flux_optimizer_card", () -> new ExpansionCardItem(new Item.Properties().stacksTo(1).fireResistant(), UpgradeType.FLUX_OPTIMIZER));
    public static final DeferredItem<Item> HAZARD_SHIELD_CARD = ITEMS.register("hazard_shield_card", () -> new ExpansionCardItem(new Item.Properties().stacksTo(1).fireResistant(), UpgradeType.HAZARD_SHIELD));
    public static final DeferredItem<Item> STRUCTURE_SCANNER_CARD = ITEMS.register("structure_scanner_card", () -> new ExpansionCardItem(new Item.Properties().stacksTo(1).fireResistant(), UpgradeType.STRUCTURE_SCANNER));
    public static final DeferredItem<Item> BIOME_LOCATOR_CARD = ITEMS.register("biome_locator_card", () -> new ExpansionCardItem(new Item.Properties().stacksTo(1).fireResistant(), UpgradeType.BIOME_LOCATOR));
    public static final DeferredItem<Item> MICROVERSE_BATTERY = ITEMS.register("microverse_battery", () -> new MicroverseBatteryItem(new Item.Properties().stacksTo(16).fireResistant()));
    public static final DeferredItem<Item> SPACE_CRUISER = ITEMS.register("space_cruiser", () -> new SpaceCruiserItem(new Item.Properties().stacksTo(1).fireResistant()));
    public static final DeferredItem<Item> RICK_PRIME_BOMB = ITEMS.register("rick_prime_bomb", () -> new RickPrimeBombItem(new Item.Properties().stacksTo(16).fireResistant()));
    public static final DeferredItem<Item> RICK_PRIME_BOMB_REMOTE = ITEMS.register("rick_prime_bomb_remote", () -> new RickPrimeBombRemoteItem(new Item.Properties().stacksTo(1).fireResistant()));
    public static final DeferredItem<Item> TACHYON_INHIBITOR_CORE = ITEMS.register("tachyon_inhibitor_core", () -> new Item(new Item.Properties().stacksTo(16).fireResistant()));
    public static final DeferredItem<Item> SUBQUANTUM_RESONATOR = ITEMS.register("subquantum_resonator", () -> new Item(new Item.Properties().stacksTo(64).fireResistant()));
    public static final DeferredItem<Item> REAL_ACID_BUCKET = ITEMS.register("real_acid_bucket", () -> new BucketItem((Fluid)ModFluids.REAL_ACID_SOURCE.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final DeferredItem<Item> FAKE_ACID_BUCKET = ITEMS.register("fake_acid_bucket", () -> new BucketItem((Fluid)ModFluids.FAKE_ACID_SOURCE.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final DeferredItem<Item> OMEGA_CONTROL_TABLET = ITEMS.register("omega_control_tablet", () -> new OmegaControlTabletItem(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC)));
    public static final DeferredItem<Item> EYE_PATCH = ITEMS.register("eye_patch", () -> new EyePatchItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BIO_NEURAL_RECEIVER = ITEMS.register("bio_neural_receiver", () -> new BioNeuralReceiverItem(new Item.Properties().stacksTo(16).fireResistant()));
    public static final DeferredItem<Item> EVIL_MORTY_ENERGY_SHIELD = ITEMS.register("evil_morty_energy_shield", () -> new EvilMortyEnergyShieldItem(new Item.Properties().durability(64).fireResistant().rarity(Rarity.RARE)));
    public static final DeferredItem<Item> EMPTY_DNA_FLASK = ITEMS.register("empty_dna_flask", () -> new EmptyDnaFlaskItem(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> DNA_SAMPLE_FLASK = ITEMS.register("dna_sample_flask", () -> new DnaSampleFlaskItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

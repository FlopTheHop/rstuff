/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.BlockItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.LiquidBlock
 *  net.minecraft.world.level.block.SoundType
 *  net.minecraft.world.level.block.state.BlockBehaviour
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.material.MapColor
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredBlock
 *  net.neoforged.neoforge.registries.DeferredItem
 *  net.neoforged.neoforge.registries.DeferredRegister
 *  net.neoforged.neoforge.registries.DeferredRegister$Blocks
 *  org.ExtraTortik.ricks_stuff.block.AntiPortalFieldGenBlock
 *  org.ExtraTortik.ricks_stuff.block.FakeAcidBlock
 *  org.ExtraTortik.ricks_stuff.block.OmegaBarrierBlock
 *  org.ExtraTortik.ricks_stuff.block.OmegaCoreBlock
 *  org.ExtraTortik.ricks_stuff.block.PhoenixCapsuleBlock
 *  org.ExtraTortik.ricks_stuff.block.PortalFluidColorizerBlock
 *  org.ExtraTortik.ricks_stuff.block.RealAcidBlock
 *  org.ExtraTortik.ricks_stuff.init.ModFluids
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 *  org.ExtraTortik.ricks_stuff.item.AntiPortalFieldGenBlockItem
 */
package org.ExtraTortik.ricks_stuff.init;

import java.util.function.Supplier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.ExtraTortik.ricks_stuff.block.AntiPortalFieldGenBlock;
import org.ExtraTortik.ricks_stuff.block.FakeAcidBlock;
import org.ExtraTortik.ricks_stuff.block.OmegaBarrierBlock;
import org.ExtraTortik.ricks_stuff.block.OmegaCoreBlock;
import org.ExtraTortik.ricks_stuff.block.PhoenixCapsuleBlock;
import org.ExtraTortik.ricks_stuff.block.PortalFluidColorizerBlock;
import org.ExtraTortik.ricks_stuff.block.RealAcidBlock;
import org.ExtraTortik.ricks_stuff.init.ModFluids;
import org.ExtraTortik.ricks_stuff.init.ModItems;
import org.ExtraTortik.ricks_stuff.item.AntiPortalFieldGenBlockItem;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks((String)"ricks_stuff");
    public static final DeferredBlock<Block> PHOENIX_CAPSULE = ModBlocks.registerBlock("phoenix_capsule", () -> new PhoenixCapsuleBlock(BlockBehaviour.Properties.ofFullCopy((BlockBehaviour)Blocks.IRON_BLOCK).strength(3.5f, 6.0f).sound(SoundType.METAL).noOcclusion().dynamicShape()));
    public static final DeferredBlock<Block> ANTI_PORTAL_FIELD_GEN = BLOCKS.register("anti_portal_field_gen", () -> new AntiPortalFieldGenBlock(BlockBehaviour.Properties.ofFullCopy((BlockBehaviour)Blocks.IRON_BLOCK).strength(4.0f, 8.0f).sound(SoundType.METAL).noOcclusion().dynamicShape()));
    public static final DeferredBlock<LiquidBlock> REAL_ACID_BLOCK = BLOCKS.register("real_acid_block", () -> new RealAcidBlock((Supplier)ModFluids.REAL_ACID_SOURCE, BlockBehaviour.Properties.ofFullCopy((BlockBehaviour)Blocks.WATER).noCollission().strength(100.0f).noLootTable().lightLevel(state -> 8)));
    public static final DeferredBlock<LiquidBlock> FAKE_ACID_BLOCK = BLOCKS.register("fake_acid_block", () -> new FakeAcidBlock((Supplier)ModFluids.FAKE_ACID_SOURCE, BlockBehaviour.Properties.ofFullCopy((BlockBehaviour)Blocks.WATER).noCollission().strength(100.0f).noLootTable().lightLevel(state -> 8)));
    public static final DeferredBlock<Block> OMEGA_CORE = ModBlocks.registerBlock("omega_core", () -> new OmegaCoreBlock(BlockBehaviour.Properties.ofFullCopy((BlockBehaviour)Blocks.NETHERITE_BLOCK).strength(5.0f, 12.0f).sound(SoundType.NETHERITE_BLOCK).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> OMEGA_BARRIER = BLOCKS.register("omega_barrier", () -> new OmegaBarrierBlock(BlockBehaviour.Properties.of().mapColor(MapColor.NONE).strength(2.0f, 6.0f).noOcclusion()));
    public static final DeferredBlock<Block> PORTAL_FLUID_COLORIZER = ModBlocks.registerBlock("portal_fluid_colorizer", () -> new PortalFluidColorizerBlock(BlockBehaviour.Properties.ofFullCopy((BlockBehaviour)Blocks.IRON_BLOCK).strength(3.5f, 6.0f).sound(SoundType.METAL).noOcclusion()));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock toReturn = BLOCKS.register(name, block);
        ModBlocks.registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredItem<Item> registerBlockItem(String name, DeferredBlock<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem((Block)block.get(), new Item.Properties().stacksTo(1)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    static {
        ModItems.ITEMS.register("anti_portal_field_gen", () -> new AntiPortalFieldGenBlockItem((Block)ANTI_PORTAL_FIELD_GEN.get(), new Item.Properties().stacksTo(1).fireResistant()));
    }
}

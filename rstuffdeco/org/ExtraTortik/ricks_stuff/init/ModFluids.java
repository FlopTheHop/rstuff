/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Registry
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.level.material.FlowingFluid
 *  net.minecraft.world.level.material.Fluid
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.fluids.BaseFlowingFluid$Flowing
 *  net.neoforged.neoforge.fluids.BaseFlowingFluid$Properties
 *  net.neoforged.neoforge.fluids.BaseFlowingFluid$Source
 *  net.neoforged.neoforge.fluids.FluidType
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 *  net.neoforged.neoforge.registries.NeoForgeRegistries
 *  org.ExtraTortik.ricks_stuff.fluid.AcidFluidType
 *  org.ExtraTortik.ricks_stuff.init.ModBlocks
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 */
package org.ExtraTortik.ricks_stuff.init;

import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.ExtraTortik.ricks_stuff.fluid.AcidFluidType;
import org.ExtraTortik.ricks_stuff.init.ModBlocks;
import org.ExtraTortik.ricks_stuff.init.ModItems;

public class ModFluids {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create((Registry)NeoForgeRegistries.FLUID_TYPES, (String)"ricks_stuff");
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create((ResourceKey)Registries.FLUID, (String)"ricks_stuff");
    public static final DeferredHolder<FluidType, FluidType> REAL_ACID_TYPE = FLUID_TYPES.register("real_acid", () -> new AcidFluidType("real_acid", AcidFluidType.createProperties()));
    public static final DeferredHolder<Fluid, FlowingFluid> REAL_ACID_SOURCE = FLUIDS.register("real_acid", () -> new BaseFlowingFluid.Source(REAL_ACID_PROPERTIES));
    public static final DeferredHolder<Fluid, FlowingFluid> REAL_ACID_FLOWING = FLUIDS.register("real_acid_flowing", () -> new BaseFlowingFluid.Flowing(REAL_ACID_PROPERTIES));
    public static final BaseFlowingFluid.Properties REAL_ACID_PROPERTIES = new BaseFlowingFluid.Properties(REAL_ACID_TYPE, REAL_ACID_SOURCE, REAL_ACID_FLOWING).slopeFindDistance(4).levelDecreasePerBlock(1).block((Supplier)ModBlocks.REAL_ACID_BLOCK).bucket((Supplier)ModItems.REAL_ACID_BUCKET).tickRate(5).explosionResistance(100.0f);
    public static final DeferredHolder<FluidType, FluidType> FAKE_ACID_TYPE = FLUID_TYPES.register("fake_acid", () -> new AcidFluidType("fake_acid", AcidFluidType.createProperties()));
    public static final DeferredHolder<Fluid, FlowingFluid> FAKE_ACID_SOURCE = FLUIDS.register("fake_acid", () -> new BaseFlowingFluid.Source(FAKE_ACID_PROPERTIES));
    public static final DeferredHolder<Fluid, FlowingFluid> FAKE_ACID_FLOWING = FLUIDS.register("fake_acid_flowing", () -> new BaseFlowingFluid.Flowing(FAKE_ACID_PROPERTIES));
    public static final BaseFlowingFluid.Properties FAKE_ACID_PROPERTIES = new BaseFlowingFluid.Properties(FAKE_ACID_TYPE, FAKE_ACID_SOURCE, FAKE_ACID_FLOWING).slopeFindDistance(4).levelDecreasePerBlock(1).block((Supplier)ModBlocks.FAKE_ACID_BLOCK).bucket((Supplier)ModItems.FAKE_ACID_BUCKET).tickRate(5).explosionResistance(100.0f);

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
        FLUIDS.register(eventBus);
    }
}

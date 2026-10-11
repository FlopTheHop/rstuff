/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvents
 *  net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
 *  net.neoforged.neoforge.common.SoundActions
 *  net.neoforged.neoforge.fluids.FluidType
 *  net.neoforged.neoforge.fluids.FluidType$Properties
 */
package org.ExtraTortik.ricks_stuff.fluid;

import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;

public class AcidFluidType
extends FluidType {
    private static final ResourceLocation STILL_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"block/acid_still");
    private static final ResourceLocation FLOWING_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"block/acid_flow");
    private static final ResourceLocation OVERLAY_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"block/acid_overlay");

    public AcidFluidType(String name, FluidType.Properties properties) {
        super(properties);
    }

    public static FluidType.Properties createProperties() {
        return FluidType.Properties.create().descriptionId("fluid.ricks_stuff.acid").fallDistanceModifier(0.0f).canExtinguish(true).canDrown(false).canSwim(true).canPushEntity(true).supportsBoating(true).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA).sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH).density(1200).viscosity(1200).lightLevel(8);
    }

    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept((IClientFluidTypeExtensions)new /* Unavailable Anonymous Inner Class!! */);
    }
}

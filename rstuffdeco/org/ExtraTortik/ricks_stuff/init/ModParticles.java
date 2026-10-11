/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleType
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 */
package org.ExtraTortik.ricks_stuff.init;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create((ResourceKey)Registries.PARTICLE_TYPE, (String)"ricks_stuff");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GREEN_DIMENSIONAL_SPARK = PARTICLE_TYPES.register("green_dimensional_spark", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLUE_DIMENSIONAL_SPARK = PARTICLE_TYPES.register("blue_dimensional_spark", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> YELLOW_DIMENSIONAL_SPARK = PARTICLE_TYPES.register("yellow_dimensional_spark", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MAGENTA_DIMENSIONAL_SPARK = PARTICLE_TYPES.register("magenta_dimensional_spark", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GREEN_FLUID_DROPLET = PARTICLE_TYPES.register("green_fluid_droplet", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLUE_FLUID_DROPLET = PARTICLE_TYPES.register("blue_fluid_droplet", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> YELLOW_FLUID_DROPLET = PARTICLE_TYPES.register("yellow_fluid_droplet", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GREEN_PLASMA_SHOCKWAVE = PARTICLE_TYPES.register("green_plasma_shockwave", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLUE_PLASMA_SHOCKWAVE = PARTICLE_TYPES.register("blue_plasma_shockwave", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> YELLOW_PLASMA_SHOCKWAVE = PARTICLE_TYPES.register("yellow_plasma_shockwave", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MAGENTA_PLASMA_SHOCKWAVE = PARTICLE_TYPES.register("magenta_plasma_shockwave", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GREEN_VORTEX_BEAM = PARTICLE_TYPES.register("green_vortex_beam", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLUE_VORTEX_BEAM = PARTICLE_TYPES.register("blue_vortex_beam", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> YELLOW_VORTEX_BEAM = PARTICLE_TYPES.register("yellow_vortex_beam", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MAGENTA_VORTEX_BEAM = PARTICLE_TYPES.register("magenta_vortex_beam", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GREEN_FLUID_POISON = PARTICLE_TYPES.register("green_fluid_poison", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLUE_FLUID_POISON = PARTICLE_TYPES.register("blue_fluid_poison", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> YELLOW_FLUID_POISON = PARTICLE_TYPES.register("yellow_fluid_poison", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GREEN_FLUID_GROUND_DROP = PARTICLE_TYPES.register("green_fluid_ground_drop", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLUE_FLUID_GROUND_DROP = PARTICLE_TYPES.register("blue_fluid_ground_drop", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> YELLOW_FLUID_GROUND_DROP = PARTICLE_TYPES.register("yellow_fluid_ground_drop", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLUID_EVAPORATION = PARTICLE_TYPES.register("fluid_evaporation", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLOOD_DROPLET = PARTICLE_TYPES.register("blood_droplet", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLOOD_SHOCKWAVE = PARTICLE_TYPES.register("blood_shockwave", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLOOD_VAPOR = PARTICLE_TYPES.register("blood_vapor", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MAGENTA_EXPLOSION_CLOUD = PARTICLE_TYPES.register("magenta_explosion_cloud", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MAGENTA_BURNING_SHRAPNEL = PARTICLE_TYPES.register("magenta_burning_shrapnel", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ACID_VAPOR = PARTICLE_TYPES.register("acid_vapor", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ACID_BUBBLE = PARTICLE_TYPES.register("acid_bubble", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> OMEGA_LIGHTNING = PARTICLE_TYPES.register("omega_lightning", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> OMEGA_PLASMA_SPARK = PARTICLE_TYPES.register("omega_plasma_spark", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> QUANTUM_GLITCH = PARTICLE_TYPES.register("quantum_glitch", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> OMEGA_CORONA_PETAL = PARTICLE_TYPES.register("omega_corona_petal", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> OMEGA_SHOCKWAVE = PARTICLE_TYPES.register("omega_shockwave", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLUID_POISON = PARTICLE_TYPES.register("fluid_poison", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLUID_GROUND_DROP = PARTICLE_TYPES.register("fluid_ground_drop", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PORTAL_VORTEX_BEAM = PARTICLE_TYPES.register("portal_vortex_beam", () -> new SimpleParticleType(false));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}

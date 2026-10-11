/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  net.minecraft.core.component.DataComponentType
 *  net.minecraft.core.component.DataComponentType$Builder
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 *  net.neoforged.neoforge.registries.DeferredRegister$DataComponents
 */
package org.ExtraTortik.ricks_stuff.init;

import com.mojang.serialization.Codec;
import java.util.function.UnaryOperator;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents((ResourceKey)Registries.DATA_COMPONENT_TYPE, (String)"ricks_stuff");
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> CHARGE = ModDataComponents.register("charge", builder -> builder.persistent((Codec)Codec.INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> GUN_ID = ModDataComponents.register("gun_id", builder -> builder.persistent((Codec)Codec.INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> MODE = ModDataComponents.register("mode", builder -> builder.persistent((Codec)Codec.STRING));

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.registerComponentType(name, builderOperator);
    }

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }
}

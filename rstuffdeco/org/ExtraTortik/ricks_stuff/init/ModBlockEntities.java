/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.entity.BlockEntityType$Builder
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 *  org.ExtraTortik.ricks_stuff.block.entity.AntiPortalFieldGenBlockEntity
 *  org.ExtraTortik.ricks_stuff.block.entity.OmegaCoreBlockEntity
 *  org.ExtraTortik.ricks_stuff.block.entity.PhoenixCapsuleBlockEntity
 *  org.ExtraTortik.ricks_stuff.block.entity.PortalFluidColorizerBlockEntity
 *  org.ExtraTortik.ricks_stuff.init.ModBlocks
 */
package org.ExtraTortik.ricks_stuff.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.ExtraTortik.ricks_stuff.block.entity.AntiPortalFieldGenBlockEntity;
import org.ExtraTortik.ricks_stuff.block.entity.OmegaCoreBlockEntity;
import org.ExtraTortik.ricks_stuff.block.entity.PhoenixCapsuleBlockEntity;
import org.ExtraTortik.ricks_stuff.block.entity.PortalFluidColorizerBlockEntity;
import org.ExtraTortik.ricks_stuff.init.ModBlocks;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create((ResourceKey)Registries.BLOCK_ENTITY_TYPE, (String)"ricks_stuff");
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PhoenixCapsuleBlockEntity>> PHOENIX_CAPSULE_BE = BLOCK_ENTITIES.register("phoenix_capsule_be", () -> BlockEntityType.Builder.of(PhoenixCapsuleBlockEntity::new, (Block[])new Block[]{(Block)ModBlocks.PHOENIX_CAPSULE.get()}).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AntiPortalFieldGenBlockEntity>> ANTI_PORTAL_FIELD_GEN_BE = BLOCK_ENTITIES.register("anti_portal_field_gen_be", () -> BlockEntityType.Builder.of(AntiPortalFieldGenBlockEntity::new, (Block[])new Block[]{(Block)ModBlocks.ANTI_PORTAL_FIELD_GEN.get()}).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<OmegaCoreBlockEntity>> OMEGA_CORE_BE = BLOCK_ENTITIES.register("omega_core_be", () -> BlockEntityType.Builder.of(OmegaCoreBlockEntity::new, (Block[])new Block[]{(Block)ModBlocks.OMEGA_CORE.get()}).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PortalFluidColorizerBlockEntity>> PORTAL_FLUID_COLORIZER_BE = BLOCK_ENTITIES.register("portal_fluid_colorizer_be", () -> BlockEntityType.Builder.of(PortalFluidColorizerBlockEntity::new, (Block[])new Block[]{(Block)ModBlocks.PORTAL_FLUID_COLORIZER.get()}).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}

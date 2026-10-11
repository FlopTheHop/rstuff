/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.capabilities.Capabilities$ItemHandler
 *  net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
 *  org.ExtraTortik.ricks_stuff.init.ModBlockEntities
 *  org.ExtraTortik.ricks_stuff.init.ModEntities
 */
package org.ExtraTortik.ricks_stuff.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.ExtraTortik.ricks_stuff.init.ModBlockEntities;
import org.ExtraTortik.ricks_stuff.init.ModEntities;

@EventBusSubscriber(modid="ricks_stuff", bus=EventBusSubscriber.Bus.MOD)
public class ModCapabilities {
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, (BlockEntityType)ModBlockEntities.PHOENIX_CAPSULE_BE.get(), (be, side) -> be.getItemHandlerForSide(side));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, (BlockEntityType)ModBlockEntities.ANTI_PORTAL_FIELD_GEN_BE.get(), (be, side) -> be.getItemHandlerForSide(side));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, (BlockEntityType)ModBlockEntities.PORTAL_FLUID_COLORIZER_BE.get(), (be, side) -> be.getItemHandlerForSide(side));
        event.registerEntity(Capabilities.ItemHandler.ENTITY, (EntityType)ModEntities.SPACE_CRUISER.get(), (entity, context) -> entity.getInventory());
        event.registerEntity(Capabilities.ItemHandler.ENTITY_AUTOMATION, (EntityType)ModEntities.SPACE_CRUISER.get(), (entity, side) -> entity.getInventory());
    }
}

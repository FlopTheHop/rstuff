/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.logging.LogUtils
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.ModContainer
 *  net.neoforged.fml.common.Mod
 *  net.neoforged.fml.config.IConfigSpec
 *  net.neoforged.fml.config.ModConfig$Type
 *  net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
 *  net.neoforged.neoforge.common.NeoForge
 *  net.neoforged.neoforge.event.server.ServerStartingEvent
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.event.CommonEvents
 *  org.ExtraTortik.ricks_stuff.init.ModBlockEntities
 *  org.ExtraTortik.ricks_stuff.init.ModBlocks
 *  org.ExtraTortik.ricks_stuff.init.ModCreativeTabs
 *  org.ExtraTortik.ricks_stuff.init.ModDataComponents
 *  org.ExtraTortik.ricks_stuff.init.ModEntities
 *  org.ExtraTortik.ricks_stuff.init.ModFluids
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 *  org.ExtraTortik.ricks_stuff.init.ModMenuTypes
 *  org.ExtraTortik.ricks_stuff.init.ModParticles
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.ModArmorMaterials
 */
package org.ExtraTortik.ricks_stuff;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.event.CommonEvents;
import org.ExtraTortik.ricks_stuff.init.ModBlockEntities;
import org.ExtraTortik.ricks_stuff.init.ModBlocks;
import org.ExtraTortik.ricks_stuff.init.ModCreativeTabs;
import org.ExtraTortik.ricks_stuff.init.ModDataComponents;
import org.ExtraTortik.ricks_stuff.init.ModEntities;
import org.ExtraTortik.ricks_stuff.init.ModFluids;
import org.ExtraTortik.ricks_stuff.init.ModItems;
import org.ExtraTortik.ricks_stuff.init.ModMenuTypes;
import org.ExtraTortik.ricks_stuff.init.ModParticles;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.ModArmorMaterials;
import org.slf4j.Logger;

@Mod(value="ricks_stuff")
public class RicksStuff {
    public static final String MODID = "ricks_stuff";
    private static final Logger LOGGER = LogUtils.getLogger();

    public RicksStuff(IEventBus modEventBus, ModContainer modContainer) {
        ModDataComponents.register((IEventBus)modEventBus);
        ModFluids.register((IEventBus)modEventBus);
        ModSounds.register((IEventBus)modEventBus);
        ModArmorMaterials.register((IEventBus)modEventBus);
        ModItems.register((IEventBus)modEventBus);
        ModBlocks.register((IEventBus)modEventBus);
        ModBlockEntities.register((IEventBus)modEventBus);
        ModEntities.register((IEventBus)modEventBus);
        ModParticles.register((IEventBus)modEventBus);
        ModMenuTypes.register((IEventBus)modEventBus);
        ModCreativeTabs.register((IEventBus)modEventBus);
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register((Object)this);
        NeoForge.EVENT_BUS.register(CommonEvents.class);
        modContainer.registerConfig(ModConfig.Type.COMMON, (IConfigSpec)Config.SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, (IConfigSpec)Config.CLIENT_SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("RicksStuff common setup complete");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("RicksStuff server starting");
    }
}

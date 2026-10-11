/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.InputConstants$Type
 *  net.minecraft.client.KeyMapping
 *  net.minecraft.client.renderer.ItemBlockRenderTypes
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.resources.model.ModelResourceLocation
 *  net.minecraft.core.particles.ParticleType
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.packs.resources.PreparableReloadListener
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.material.Fluid
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
 *  net.neoforged.neoforge.client.event.EntityRenderersEvent$RegisterRenderers
 *  net.neoforged.neoforge.client.event.ModelEvent$RegisterAdditional
 *  net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent
 *  net.neoforged.neoforge.client.event.RegisterGuiLayersEvent
 *  net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent
 *  net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
 *  net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent
 *  net.neoforged.neoforge.client.gui.VanillaGuiLayers
 *  net.neoforged.neoforge.client.settings.IKeyConflictContext
 *  net.neoforged.neoforge.client.settings.KeyConflictContext
 *  org.ExtraTortik.ricks_stuff.client.SpaceCruiserClientHandler
 *  org.ExtraTortik.ricks_stuff.client.gui.AntiPortalScreen
 *  org.ExtraTortik.ricks_stuff.client.gui.PhoenixCapsuleScreen
 *  org.ExtraTortik.ricks_stuff.client.gui.PortalFluidColorizerScreen
 *  org.ExtraTortik.ricks_stuff.client.overlay.BioNeuralImplantOverlay
 *  org.ExtraTortik.ricks_stuff.client.overlay.EvilMortyScannerOverlay
 *  org.ExtraTortik.ricks_stuff.client.overlay.OmegaScreenDistortionOverlay
 *  org.ExtraTortik.ricks_stuff.client.overlay.PhoenixResurrectionOverlay
 *  org.ExtraTortik.ricks_stuff.client.overlay.PortalFadeOverlay
 *  org.ExtraTortik.ricks_stuff.client.overlay.PortalGunHudOverlay
 *  org.ExtraTortik.ricks_stuff.client.overlay.PuppetLinkOverlay
 *  org.ExtraTortik.ricks_stuff.client.overlay.SpaceCruiserHudOverlay
 *  org.ExtraTortik.ricks_stuff.client.particle.AcidBubbleParticle$Provider
 *  org.ExtraTortik.ricks_stuff.client.particle.AcidVaporParticle$Provider
 *  org.ExtraTortik.ricks_stuff.client.particle.DimensionalSparkParticle$BlueProvider
 *  org.ExtraTortik.ricks_stuff.client.particle.DimensionalSparkParticle$GreenProvider
 *  org.ExtraTortik.ricks_stuff.client.particle.DimensionalSparkParticle$MagentaProvider
 *  org.ExtraTortik.ricks_stuff.client.particle.DimensionalSparkParticle$YellowProvider
 *  org.ExtraTortik.ricks_stuff.client.particle.FluidDropletParticle$BloodProvider
 *  org.ExtraTortik.ricks_stuff.client.particle.FluidDropletParticle$BlueProvider
 *  org.ExtraTortik.ricks_stuff.client.particle.FluidDropletParticle$GreenProvider
 *  org.ExtraTortik.ricks_stuff.client.particle.FluidDropletParticle$YellowProvider
 *  org.ExtraTortik.ricks_stuff.client.particle.FluidEvaporationParticle$BloodProvider
 *  org.ExtraTortik.ricks_stuff.client.particle.FluidEvaporationParticle$Provider
 *  org.ExtraTortik.ricks_stuff.client.particle.FluidGroundDropParticle$BlueProvider
 *  org.ExtraTortik.ricks_stuff.client.particle.FluidGroundDropParticle$GreenProvider
 *  org.ExtraTortik.ricks_stuff.client.particle.FluidGroundDropParticle$Provider
 *  org.ExtraTortik.ricks_stuff.client.particle.FluidGroundDropParticle$YellowProvider
 *  org.ExtraTortik.ricks_stuff.client.particle.FluidPoisonParticle$BlueProvider
 *  org.ExtraTortik.ricks_stuff.client.particle.FluidPoisonParticle$GreenProvider
 *  org.ExtraTortik.ricks_stuff.client.particle.FluidPoisonParticle$Provider
 *  org.ExtraTortik.ricks_stuff.client.particle.FluidPoisonParticle$YellowProvider
 *  org.ExtraTortik.ricks_stuff.client.particle.MagentaBurningShrapnelParticle$Provider
 *  org.ExtraTortik.ricks_stuff.client.particle.MagentaExplosionCloudParticle$Provider
 *  org.ExtraTortik.ricks_stuff.client.particle.OmegaCoronaPetalParticle$Provider
 *  org.ExtraTortik.ricks_stuff.client.particle.OmegaLightningParticle$Provider
 *  org.ExtraTortik.ricks_stuff.client.particle.OmegaPlasmaSparkParticle$Provider
 *  org.ExtraTortik.ricks_stuff.client.particle.OmegaShockwaveParticle$Provider
 *  org.ExtraTortik.ricks_stuff.client.particle.PlasmaShockwaveParticle$BloodProvider
 *  org.ExtraTortik.ricks_stuff.client.particle.PlasmaShockwaveParticle$BlueProvider
 *  org.ExtraTortik.ricks_stuff.client.particle.PlasmaShockwaveParticle$GreenProvider
 *  org.ExtraTortik.ricks_stuff.client.particle.PlasmaShockwaveParticle$MagentaProvider
 *  org.ExtraTortik.ricks_stuff.client.particle.PlasmaShockwaveParticle$YellowProvider
 *  org.ExtraTortik.ricks_stuff.client.particle.QuantumGlitchParticle$Provider
 *  org.ExtraTortik.ricks_stuff.client.particle.VortexBeamParticle$BlueProvider
 *  org.ExtraTortik.ricks_stuff.client.particle.VortexBeamParticle$GreenProvider
 *  org.ExtraTortik.ricks_stuff.client.particle.VortexBeamParticle$MagentaProvider
 *  org.ExtraTortik.ricks_stuff.client.particle.VortexBeamParticle$Provider
 *  org.ExtraTortik.ricks_stuff.client.particle.VortexBeamParticle$YellowProvider
 *  org.ExtraTortik.ricks_stuff.client.portal.CustomPortalTextureManager
 *  org.ExtraTortik.ricks_stuff.client.renderer.AntiPortalFieldGenRenderer
 *  org.ExtraTortik.ricks_stuff.client.renderer.OmegaCoreRenderer
 *  org.ExtraTortik.ricks_stuff.client.renderer.OmegaDeviceRenderer
 *  org.ExtraTortik.ricks_stuff.client.renderer.PhoenixCapsuleRenderer
 *  org.ExtraTortik.ricks_stuff.client.renderer.PortalEntityRenderer
 *  org.ExtraTortik.ricks_stuff.client.renderer.PortalProjectileRenderer
 *  org.ExtraTortik.ricks_stuff.client.renderer.RickPrimeBombRenderer
 *  org.ExtraTortik.ricks_stuff.client.renderer.SelfDestructGunRenderer
 *  org.ExtraTortik.ricks_stuff.client.renderer.SpaceCruiserRenderer
 *  org.ExtraTortik.ricks_stuff.compat.curios.CuriosClientCompat
 *  org.ExtraTortik.ricks_stuff.init.ModBlockEntities
 *  org.ExtraTortik.ricks_stuff.init.ModBlocks
 *  org.ExtraTortik.ricks_stuff.init.ModEntities
 *  org.ExtraTortik.ricks_stuff.init.ModFluids
 *  org.ExtraTortik.ricks_stuff.init.ModMenuTypes
 *  org.ExtraTortik.ricks_stuff.init.ModParticles
 */
package org.ExtraTortik.ricks_stuff.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.client.settings.IKeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.ExtraTortik.ricks_stuff.client.SpaceCruiserClientHandler;
import org.ExtraTortik.ricks_stuff.client.gui.AntiPortalScreen;
import org.ExtraTortik.ricks_stuff.client.gui.PhoenixCapsuleScreen;
import org.ExtraTortik.ricks_stuff.client.gui.PortalFluidColorizerScreen;
import org.ExtraTortik.ricks_stuff.client.overlay.BioNeuralImplantOverlay;
import org.ExtraTortik.ricks_stuff.client.overlay.EvilMortyScannerOverlay;
import org.ExtraTortik.ricks_stuff.client.overlay.OmegaScreenDistortionOverlay;
import org.ExtraTortik.ricks_stuff.client.overlay.PhoenixResurrectionOverlay;
import org.ExtraTortik.ricks_stuff.client.overlay.PortalFadeOverlay;
import org.ExtraTortik.ricks_stuff.client.overlay.PortalGunHudOverlay;
import org.ExtraTortik.ricks_stuff.client.overlay.PuppetLinkOverlay;
import org.ExtraTortik.ricks_stuff.client.overlay.SpaceCruiserHudOverlay;
import org.ExtraTortik.ricks_stuff.client.particle.AcidBubbleParticle;
import org.ExtraTortik.ricks_stuff.client.particle.AcidVaporParticle;
import org.ExtraTortik.ricks_stuff.client.particle.DimensionalSparkParticle;
import org.ExtraTortik.ricks_stuff.client.particle.FluidDropletParticle;
import org.ExtraTortik.ricks_stuff.client.particle.FluidEvaporationParticle;
import org.ExtraTortik.ricks_stuff.client.particle.FluidGroundDropParticle;
import org.ExtraTortik.ricks_stuff.client.particle.FluidPoisonParticle;
import org.ExtraTortik.ricks_stuff.client.particle.MagentaBurningShrapnelParticle;
import org.ExtraTortik.ricks_stuff.client.particle.MagentaExplosionCloudParticle;
import org.ExtraTortik.ricks_stuff.client.particle.OmegaCoronaPetalParticle;
import org.ExtraTortik.ricks_stuff.client.particle.OmegaLightningParticle;
import org.ExtraTortik.ricks_stuff.client.particle.OmegaPlasmaSparkParticle;
import org.ExtraTortik.ricks_stuff.client.particle.OmegaShockwaveParticle;
import org.ExtraTortik.ricks_stuff.client.particle.PlasmaShockwaveParticle;
import org.ExtraTortik.ricks_stuff.client.particle.QuantumGlitchParticle;
import org.ExtraTortik.ricks_stuff.client.particle.VortexBeamParticle;
import org.ExtraTortik.ricks_stuff.client.portal.CustomPortalTextureManager;
import org.ExtraTortik.ricks_stuff.client.renderer.AntiPortalFieldGenRenderer;
import org.ExtraTortik.ricks_stuff.client.renderer.OmegaCoreRenderer;
import org.ExtraTortik.ricks_stuff.client.renderer.OmegaDeviceRenderer;
import org.ExtraTortik.ricks_stuff.client.renderer.PhoenixCapsuleRenderer;
import org.ExtraTortik.ricks_stuff.client.renderer.PortalEntityRenderer;
import org.ExtraTortik.ricks_stuff.client.renderer.PortalProjectileRenderer;
import org.ExtraTortik.ricks_stuff.client.renderer.RickPrimeBombRenderer;
import org.ExtraTortik.ricks_stuff.client.renderer.SelfDestructGunRenderer;
import org.ExtraTortik.ricks_stuff.client.renderer.SpaceCruiserRenderer;
import org.ExtraTortik.ricks_stuff.compat.curios.CuriosClientCompat;
import org.ExtraTortik.ricks_stuff.init.ModBlockEntities;
import org.ExtraTortik.ricks_stuff.init.ModBlocks;
import org.ExtraTortik.ricks_stuff.init.ModEntities;
import org.ExtraTortik.ricks_stuff.init.ModFluids;
import org.ExtraTortik.ricks_stuff.init.ModMenuTypes;
import org.ExtraTortik.ricks_stuff.init.ModParticles;

@EventBusSubscriber(modid="ricks_stuff", bus=EventBusSubscriber.Bus.MOD, value={Dist.CLIENT})
public class ClientSetup {
    public static final KeyMapping CLOSE_PORTALS_KEY = new KeyMapping("key.ricks_stuff.close_portals", (IKeyConflictContext)KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 82, "key.categories.ricks_stuff");
    public static final KeyMapping RADIAL_MENU_KEY = new KeyMapping("key.ricks_stuff.radial_menu", (IKeyConflictContext)KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 86, "key.categories.ricks_stuff");
    public static final KeyMapping CYCLE_MODE_KEY = new KeyMapping("key.ricks_stuff.cycle_mode", (IKeyConflictContext)KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 66, "key.categories.ricks_stuff");
    public static final KeyMapping PUPPET_SELF_DESTRUCT_KEY = new KeyMapping("key.ricks_stuff.puppet_self_destruct", (IKeyConflictContext)KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 90, "key.categories.ricks_stuff");
    public static final KeyMapping PUPPET_DISCONNECT_KEY = new KeyMapping("key.ricks_stuff.puppet_disconnect", (IKeyConflictContext)KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 71, "key.categories.ricks_stuff");
    public static final KeyMapping PUPPET_RADIAL_MENU_KEY = new KeyMapping("key.ricks_stuff.puppet_radial_menu", (IKeyConflictContext)KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 89, "key.categories.ricks_stuff");
    public static final KeyMapping PUPPET_ABILITY_KEY = new KeyMapping("key.ricks_stuff.puppet_ability", (IKeyConflictContext)KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 82, "key.categories.ricks_stuff");
    public static final KeyMapping NEURAL_SCAN_KEY = new KeyMapping("key.ricks_stuff.neural_scan", (IKeyConflictContext)KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 88, "key.categories.ricks_stuff");

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        CuriosClientCompat.registerRenderers();
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer((Block)((Block)ModBlocks.ANTI_PORTAL_FIELD_GEN.get()), (RenderType)RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer((Block)((Block)ModBlocks.PHOENIX_CAPSULE.get()), (RenderType)RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer((Fluid)((Fluid)ModFluids.REAL_ACID_SOURCE.get()), (RenderType)RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer((Fluid)((Fluid)ModFluids.REAL_ACID_FLOWING.get()), (RenderType)RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer((Block)((Block)ModBlocks.REAL_ACID_BLOCK.get()), (RenderType)RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer((Fluid)((Fluid)ModFluids.FAKE_ACID_SOURCE.get()), (RenderType)RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer((Fluid)((Fluid)ModFluids.FAKE_ACID_FLOWING.get()), (RenderType)RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer((Block)((Block)ModBlocks.FAKE_ACID_BLOCK.get()), (RenderType)RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer((Block)((Block)ModBlocks.PORTAL_FLUID_COLORIZER.get()), (RenderType)RenderType.cutout());
        });
    }

    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register((MenuType)ModMenuTypes.PHOENIX_CAPSULE_MENU.get(), PhoenixCapsuleScreen::new);
        event.register((MenuType)ModMenuTypes.ANTI_PORTAL_MENU.get(), AntiPortalScreen::new);
        event.register((MenuType)ModMenuTypes.PORTAL_FLUID_COLORIZER_MENU.get(), PortalFluidColorizerScreen::new);
    }

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(CLOSE_PORTALS_KEY);
        event.register(RADIAL_MENU_KEY);
        event.register(CYCLE_MODE_KEY);
        event.register(SpaceCruiserClientHandler.KEY_HEADLIGHTS);
        event.register(SpaceCruiserClientHandler.KEY_HOVER_LOCK);
        event.register(SpaceCruiserClientHandler.KEY_ROLL_LEFT);
        event.register(SpaceCruiserClientHandler.KEY_ROLL_RIGHT);
        event.register(SpaceCruiserClientHandler.KEY_ENGINE);
        event.register(SpaceCruiserClientHandler.KEY_SHIP_NAV);
        event.register(PUPPET_SELF_DESTRUCT_KEY);
        event.register(PUPPET_DISCONNECT_KEY);
        event.register(PUPPET_RADIAL_MENU_KEY);
        event.register(PUPPET_ABILITY_KEY);
        event.register(NEURAL_SCAN_KEY);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer((EntityType)ModEntities.PORTAL.get(), PortalEntityRenderer::new);
        event.registerEntityRenderer((EntityType)ModEntities.PORTAL_PROJECTILE.get(), PortalProjectileRenderer::new);
        event.registerEntityRenderer((EntityType)ModEntities.SELF_DESTRUCT_GUN.get(), SelfDestructGunRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)ModBlockEntities.PHOENIX_CAPSULE_BE.get(), PhoenixCapsuleRenderer::new);
        event.registerEntityRenderer((EntityType)ModEntities.SPACE_CRUISER.get(), SpaceCruiserRenderer::new);
        event.registerEntityRenderer((EntityType)ModEntities.RICK_PRIME_BOMB.get(), RickPrimeBombRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)ModBlockEntities.ANTI_PORTAL_FIELD_GEN_BE.get(), AntiPortalFieldGenRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)ModBlockEntities.OMEGA_CORE_BE.get(), OmegaCoreRenderer::new);
        event.registerEntityRenderer((EntityType)ModEntities.OMEGA_DEVICE.get(), OmegaDeviceRenderer::new);
    }

    @SubscribeEvent
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        event.register(ModelResourceLocation.standalone((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"item/rick_portal_gun_base")));
        event.register(ModelResourceLocation.standalone((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"item/portal_fluid_base")));
        event.register(ModelResourceLocation.standalone((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"item/infinity_portal_fluid_base")));
        event.register(ModelResourceLocation.standalone((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"item/empty_tube")));
        event.register(ModelResourceLocation.standalone((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"item/empty_tube_base")));
    }

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.HOTBAR, ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"portal_gun_hud"), PortalGunHudOverlay.HUD_OVERLAY);
        event.registerAbove(VanillaGuiLayers.CAMERA_OVERLAYS, ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"portal_fade_overlay"), PortalFadeOverlay.OVERLAY);
        event.registerAbove(VanillaGuiLayers.HOTBAR, ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"space_cruiser_hud"), SpaceCruiserHudOverlay.HUD_OVERLAY);
        event.registerAbove(VanillaGuiLayers.CAMERA_OVERLAYS, ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"phoenix_resurrection_overlay"), PhoenixResurrectionOverlay.OVERLAY);
        event.registerAbove(VanillaGuiLayers.CAMERA_OVERLAYS, ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"omega_screen_distortion_overlay"), OmegaScreenDistortionOverlay.OVERLAY);
        event.registerAbove(VanillaGuiLayers.CROSSHAIR, ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"bio_neural_implant_overlay"), BioNeuralImplantOverlay.HUD_OVERLAY);
        event.registerAbove(VanillaGuiLayers.CROSSHAIR, ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"puppet_link_overlay"), PuppetLinkOverlay.HUD_OVERLAY);
        event.registerAbove(VanillaGuiLayers.CROSSHAIR, ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"evil_morty_scanner_overlay"), EvilMortyScannerOverlay.HUD_OVERLAY);
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet((ParticleType)ModParticles.GREEN_DIMENSIONAL_SPARK.get(), DimensionalSparkParticle.GreenProvider::new);
        event.registerSpriteSet((ParticleType)ModParticles.BLUE_DIMENSIONAL_SPARK.get(), DimensionalSparkParticle.BlueProvider::new);
        event.registerSpriteSet((ParticleType)ModParticles.YELLOW_DIMENSIONAL_SPARK.get(), DimensionalSparkParticle.YellowProvider::new);
        event.registerSpriteSet((ParticleType)ModParticles.MAGENTA_DIMENSIONAL_SPARK.get(), DimensionalSparkParticle.MagentaProvider::new);
        event.registerSpriteSet((ParticleType)ModParticles.GREEN_FLUID_DROPLET.get(), FluidDropletParticle.GreenProvider::new);
        event.registerSpriteSet((ParticleType)ModParticles.BLUE_FLUID_DROPLET.get(), FluidDropletParticle.BlueProvider::new);
        event.registerSpriteSet((ParticleType)ModParticles.YELLOW_FLUID_DROPLET.get(), FluidDropletParticle.YellowProvider::new);
        event.registerSpriteSet((ParticleType)ModParticles.GREEN_PLASMA_SHOCKWAVE.get(), PlasmaShockwaveParticle.GreenProvider::new);
        event.registerSpriteSet((ParticleType)ModParticles.BLUE_PLASMA_SHOCKWAVE.get(), PlasmaShockwaveParticle.BlueProvider::new);
        event.registerSpriteSet((ParticleType)ModParticles.YELLOW_PLASMA_SHOCKWAVE.get(), PlasmaShockwaveParticle.YellowProvider::new);
        event.registerSpriteSet((ParticleType)ModParticles.MAGENTA_PLASMA_SHOCKWAVE.get(), PlasmaShockwaveParticle.MagentaProvider::new);
        event.registerSpriteSet((ParticleType)ModParticles.GREEN_VORTEX_BEAM.get(), VortexBeamParticle.GreenProvider::new);
        event.registerSpriteSet((ParticleType)ModParticles.BLUE_VORTEX_BEAM.get(), VortexBeamParticle.BlueProvider::new);
        event.registerSpriteSet((ParticleType)ModParticles.YELLOW_VORTEX_BEAM.get(), VortexBeamParticle.YellowProvider::new);
        event.registerSpriteSet((ParticleType)ModParticles.MAGENTA_VORTEX_BEAM.get(), VortexBeamParticle.MagentaProvider::new);
        event.registerSpriteSet((ParticleType)ModParticles.FLUID_EVAPORATION.get(), FluidEvaporationParticle.Provider::new);
        event.registerSpriteSet((ParticleType)ModParticles.GREEN_FLUID_POISON.get(), FluidPoisonParticle.GreenProvider::new);
        event.registerSpriteSet((ParticleType)ModParticles.BLUE_FLUID_POISON.get(), FluidPoisonParticle.BlueProvider::new);
        event.registerSpriteSet((ParticleType)ModParticles.YELLOW_FLUID_POISON.get(), FluidPoisonParticle.YellowProvider::new);
        event.registerSpriteSet((ParticleType)ModParticles.GREEN_FLUID_GROUND_DROP.get(), FluidGroundDropParticle.GreenProvider::new);
        event.registerSpriteSet((ParticleType)ModParticles.BLUE_FLUID_GROUND_DROP.get(), FluidGroundDropParticle.BlueProvider::new);
        event.registerSpriteSet((ParticleType)ModParticles.YELLOW_FLUID_GROUND_DROP.get(), FluidGroundDropParticle.YellowProvider::new);
        event.registerSpriteSet((ParticleType)ModParticles.BLOOD_DROPLET.get(), FluidDropletParticle.BloodProvider::new);
        event.registerSpriteSet((ParticleType)ModParticles.BLOOD_SHOCKWAVE.get(), PlasmaShockwaveParticle.BloodProvider::new);
        event.registerSpriteSet((ParticleType)ModParticles.BLOOD_VAPOR.get(), FluidEvaporationParticle.BloodProvider::new);
        event.registerSpriteSet((ParticleType)ModParticles.MAGENTA_EXPLOSION_CLOUD.get(), MagentaExplosionCloudParticle.Provider::new);
        event.registerSpriteSet((ParticleType)ModParticles.MAGENTA_BURNING_SHRAPNEL.get(), MagentaBurningShrapnelParticle.Provider::new);
        event.registerSpriteSet((ParticleType)ModParticles.ACID_VAPOR.get(), AcidVaporParticle.Provider::new);
        event.registerSpriteSet((ParticleType)ModParticles.ACID_BUBBLE.get(), AcidBubbleParticle.Provider::new);
        event.registerSpriteSet((ParticleType)ModParticles.OMEGA_LIGHTNING.get(), OmegaLightningParticle.Provider::new);
        event.registerSpriteSet((ParticleType)ModParticles.OMEGA_PLASMA_SPARK.get(), OmegaPlasmaSparkParticle.Provider::new);
        event.registerSpriteSet((ParticleType)ModParticles.QUANTUM_GLITCH.get(), QuantumGlitchParticle.Provider::new);
        event.registerSpriteSet((ParticleType)ModParticles.OMEGA_CORONA_PETAL.get(), OmegaCoronaPetalParticle.Provider::new);
        event.registerSpriteSet((ParticleType)ModParticles.OMEGA_SHOCKWAVE.get(), OmegaShockwaveParticle.Provider::new);
        event.registerSpriteSet((ParticleType)ModParticles.FLUID_POISON.get(), FluidPoisonParticle.Provider::new);
        event.registerSpriteSet((ParticleType)ModParticles.FLUID_GROUND_DROP.get(), FluidGroundDropParticle.Provider::new);
        event.registerSpriteSet((ParticleType)ModParticles.PORTAL_VORTEX_BEAM.get(), VortexBeamParticle.Provider::new);
    }

    @SubscribeEvent
    public static void registerClientReloadListeners(RegisterClientReloadListenersEvent event) {
        CustomPortalTextureManager.init();
        event.registerReloadListener((PreparableReloadListener)CustomPortalTextureManager.createReloadListener());
    }
}

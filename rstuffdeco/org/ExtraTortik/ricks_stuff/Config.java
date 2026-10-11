/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.fml.event.config.ModConfigEvent
 *  net.neoforged.neoforge.common.ModConfigSpec
 *  net.neoforged.neoforge.common.ModConfigSpec$BooleanValue
 *  net.neoforged.neoforge.common.ModConfigSpec$Builder
 *  net.neoforged.neoforge.common.ModConfigSpec$ConfigValue
 *  net.neoforged.neoforge.common.ModConfigSpec$DoubleValue
 *  net.neoforged.neoforge.common.ModConfigSpec$IntValue
 */
package org.ExtraTortik.ricks_stuff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid="ricks_stuff", bus=EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec.BooleanValue SPACE_CRUISER_BLOCK_BREAKING;
    public static final ModConfigSpec.DoubleValue SPACE_CRUISER_MAX_BLOCK_HARDNESS;
    public static final ModConfigSpec.BooleanValue SPACE_CRUISER_BREAK_SOLID_BLOCKS;
    public static final ModConfigSpec.DoubleValue SPACE_CRUISER_RAM_DAMAGE_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue SPACE_CRUISER_MAX_SPEED_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue SPACE_CRUISER_ENERGY_DRAIN_BOOST;
    public static final ModConfigSpec.BooleanValue SPACE_CRUISER_EXPLOSION_ON_DEATH;
    public static final ModConfigSpec.BooleanValue SPACE_CRUISER_INVULNERABLE_CREATIVE;
    public static final ModConfigSpec.BooleanValue ALLOW_PLAYER_TARGETING;
    public static final ModConfigSpec.IntValue MAX_EXPANSION_CARDS;
    public static final ModConfigSpec.IntValue PORTAL_MAX_LIFETIME_TICKS;
    public static final ModConfigSpec.BooleanValue ALLOW_CROSS_DIMENSION_TELEPORT;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> DIMENSION_BLACKLIST;
    public static final ModConfigSpec.BooleanValue ALLOW_ENTITY_TELEPORTATION;
    public static final ModConfigSpec.DoubleValue PORTAL_GUN_MAX_DISTANCE;
    public static final ModConfigSpec.BooleanValue ALLOW_PLAYER_TRACKING_ACROSS_DIMS;
    public static final ModConfigSpec.DoubleValue HAZARD_SHIELD_COOLDOWN_SECONDS;
    public static final ModConfigSpec.BooleanValue RICK_PRIME_BOMB_BREAK_BLOCKS;
    public static final ModConfigSpec.IntValue RICK_PRIME_BOMB_RADIUS;
    public static final ModConfigSpec.BooleanValue RICK_PRIME_BOMB_DISINTEGRATE_ENTITIES;
    public static final ModConfigSpec.BooleanValue SELF_DESTRUCT_GUN_ENABLED;
    public static final ModConfigSpec.BooleanValue PHOENIX_PROTOCOL_ENABLED;
    public static final ModConfigSpec.IntValue PHOENIX_PRINT_TIME_SECONDS;
    public static final ModConfigSpec.IntValue MAX_CAPSULES_PER_PLAYER;
    public static final ModConfigSpec.BooleanValue PHOENIX_CROSS_DIMENSION_SPAWN;
    public static final ModConfigSpec.IntValue OMEGA_COOLDOWN_TICKS;
    public static final ModConfigSpec.BooleanValue ALLOW_PLAYER_ERASURE;
    public static final ModConfigSpec.BooleanValue ALLOW_MOB_ERASURE;
    public static final ModConfigSpec.BooleanValue ALLOW_ITEM_ERASURE;
    public static final ModConfigSpec.BooleanValue ALLOW_BLOCK_ERASURE;
    public static final ModConfigSpec.BooleanValue OMEGA_DEVICE_MODEL_ENABLED;
    public static final ModConfigSpec.DoubleValue EVIL_MORTY_SHIELD_COOLDOWN_SECONDS;
    public static final ModConfigSpec.BooleanValue EVIL_MORTY_SHIELD_PROTECT_ALL_DAMAGE;
    public static final ModConfigSpec SPEC;
    private static final ModConfigSpec.Builder CLIENT_BUILDER;
    public static final ModConfigSpec.ConfigValue<String> HUD_ANCHOR;
    public static final ModConfigSpec.IntValue HUD_OFFSET_X;
    public static final ModConfigSpec.IntValue HUD_OFFSET_Y;
    public static final ModConfigSpec.DoubleValue HUD_SCALE;
    public static final ModConfigSpec.ConfigValue<String> THRUSTER_EFFECTS_QUALITY;
    public static final ModConfigSpec.DoubleValue PORTAL_VORTEX_ANIMATION_SPEED;
    public static final ModConfigSpec.DoubleValue CAMERA_SHAKE_INTENSITY;
    public static final ModConfigSpec.DoubleValue SPACE_CRUISER_ENGINE_VOLUME;
    public static final ModConfigSpec CLIENT_SPEC;
    public static boolean spaceCruiserBlockBreaking;
    public static boolean spaceCruiserBreakSolidBlocks;
    public static double spaceCruiserMaxBlockHardness;
    public static double spaceCruiserRamDamageMultiplier;
    public static double spaceCruiserMaxSpeedMultiplier;
    public static double spaceCruiserEnergyDrainBoost;
    public static boolean spaceCruiserExplosionOnDeath;
    public static boolean spaceCruiserInvulnerableCreative;
    public static boolean allowPlayerTargeting;
    public static int maxExpansionCards;
    public static int portalMaxLifetimeTicks;
    public static boolean allowCrossDimensionTeleport;
    public static List<String> dimensionBlacklist;
    public static boolean allowEntityTeleportation;
    public static double portalGunMaxDistance;
    public static boolean allowPlayerTrackingAcrossDims;
    public static double hazardShieldCooldownSeconds;
    public static boolean rickPrimeBombBreakBlocks;
    public static int rickPrimeBombRadius;
    public static boolean rickPrimeBombDisintegrateEntities;
    public static boolean selfDestructGunEnabled;
    public static boolean phoenixProtocolEnabled;
    public static int phoenixPrintTimeSeconds;
    public static int maxCapsulesPerPlayer;
    public static boolean phoenixCrossDimensionSpawn;
    public static int omegaCooldownTicks;
    public static boolean allowPlayerErasure;
    public static boolean allowMobErasure;
    public static boolean allowItemErasure;
    public static boolean allowBlockErasure;
    public static boolean omegaDeviceModelEnabled;
    public static double evilMortyShieldCooldownSeconds;
    public static boolean evilMortyShieldProtectAllDamage;
    public static String hudAnchor;
    public static int hudOffsetX;
    public static int hudOffsetY;
    public static double hudScale;
    public static String thrusterEffectsQuality;
    public static double portalVortexAnimationSpeed;
    public static double cameraShakeIntensity;
    public static double spaceCruiserEngineVolume;

    @SubscribeEvent
    static void onLoad(ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC) {
            spaceCruiserBlockBreaking = (Boolean)SPACE_CRUISER_BLOCK_BREAKING.get();
            spaceCruiserMaxBlockHardness = (Double)SPACE_CRUISER_MAX_BLOCK_HARDNESS.get();
            spaceCruiserBreakSolidBlocks = (Boolean)SPACE_CRUISER_BREAK_SOLID_BLOCKS.get();
            spaceCruiserRamDamageMultiplier = (Double)SPACE_CRUISER_RAM_DAMAGE_MULTIPLIER.get();
            spaceCruiserMaxSpeedMultiplier = (Double)SPACE_CRUISER_MAX_SPEED_MULTIPLIER.get();
            spaceCruiserEnergyDrainBoost = (Double)SPACE_CRUISER_ENERGY_DRAIN_BOOST.get();
            spaceCruiserExplosionOnDeath = (Boolean)SPACE_CRUISER_EXPLOSION_ON_DEATH.get();
            Config.spaceCruuberInvulnerableCreative();
            allowPlayerTargeting = (Boolean)ALLOW_PLAYER_TARGETING.get();
            maxExpansionCards = (Integer)MAX_EXPANSION_CARDS.get();
            portalMaxLifetimeTicks = (Integer)PORTAL_MAX_LIFETIME_TICKS.get();
            allowCrossDimensionTeleport = (Boolean)ALLOW_CROSS_DIMENSION_TELEPORT.get();
            dimensionBlacklist = new ArrayList<String>((Collection)DIMENSION_BLACKLIST.get());
            allowEntityTeleportation = (Boolean)ALLOW_ENTITY_TELEPORTATION.get();
            portalGunMaxDistance = (Double)PORTAL_GUN_MAX_DISTANCE.get();
            allowPlayerTrackingAcrossDims = (Boolean)ALLOW_PLAYER_TRACKING_ACROSS_DIMS.get();
            hazardShieldCooldownSeconds = (Double)HAZARD_SHIELD_COOLDOWN_SECONDS.get();
            rickPrimeBombBreakBlocks = (Boolean)RICK_PRIME_BOMB_BREAK_BLOCKS.get();
            rickPrimeBombRadius = (Integer)RICK_PRIME_BOMB_RADIUS.get();
            rickPrimeBombDisintegrateEntities = (Boolean)RICK_PRIME_BOMB_DISINTEGRATE_ENTITIES.get();
            selfDestructGunEnabled = (Boolean)SELF_DESTRUCT_GUN_ENABLED.get();
            phoenixProtocolEnabled = (Boolean)PHOENIX_PROTOCOL_ENABLED.get();
            phoenixPrintTimeSeconds = (Integer)PHOENIX_PRINT_TIME_SECONDS.get();
            maxCapsulesPerPlayer = (Integer)MAX_CAPSULES_PER_PLAYER.get();
            phoenixCrossDimensionSpawn = (Boolean)PHOENIX_CROSS_DIMENSION_SPAWN.get();
            omegaCooldownTicks = (Integer)OMEGA_COOLDOWN_TICKS.get();
            allowPlayerErasure = (Boolean)ALLOW_PLAYER_ERASURE.get();
            allowMobErasure = (Boolean)ALLOW_MOB_ERASURE.get();
            allowItemErasure = (Boolean)ALLOW_ITEM_ERASURE.get();
            allowBlockErasure = (Boolean)ALLOW_BLOCK_ERASURE.get();
            omegaDeviceModelEnabled = (Boolean)OMEGA_DEVICE_MODEL_ENABLED.get();
            evilMortyShieldCooldownSeconds = (Double)EVIL_MORTY_SHIELD_COOLDOWN_SECONDS.get();
            evilMortyShieldProtectAllDamage = (Boolean)EVIL_MORTY_SHIELD_PROTECT_ALL_DAMAGE.get();
        }
        if (event.getConfig().getSpec() != CLIENT_SPEC) return;
        hudAnchor = (String)HUD_ANCHOR.get();
        hudOffsetX = (Integer)HUD_OFFSET_X.get();
        hudOffsetY = (Integer)HUD_OFFSET_Y.get();
        hudScale = (Double)HUD_SCALE.get();
        thrusterEffectsQuality = (String)THRUSTER_EFFECTS_QUALITY.get();
        portalVortexAnimationSpeed = (Double)PORTAL_VORTEX_ANIMATION_SPEED.get();
        cameraShakeIntensity = (Double)CAMERA_SHAKE_INTENSITY.get();
        spaceCruiserEngineVolume = (Double)SPACE_CRUISER_ENGINE_VOLUME.get();
    }

    private static void spaceCruuberInvulnerableCreative() {
        spaceCruiserInvulnerableCreative = (Boolean)SPACE_CRUISER_INVULNERABLE_CREATIVE.get();
    }

    static {
        COMMON_BUILDER.comment("Space Cruiser settings").push("space_cruiser");
        SPACE_CRUISER_BLOCK_BREAKING = COMMON_BUILDER.comment("Allows the space cruiser to break blocks while boosting.").define("blockBreaking", true);
        SPACE_CRUISER_BREAK_SOLID_BLOCKS = COMMON_BUILDER.comment("Allows the space cruiser to break solid blocks (stone, wood, ores) while boosting. When false, only fragile blocks like foliage and glass break.").define("breakSolidBlocks", false);
        SPACE_CRUISER_MAX_BLOCK_HARDNESS = COMMON_BUILDER.comment("Maximum block hardness breakable by the space cruiser (e.g. stone is 1.5, obsidian is 50.0).").defineInRange("maxBlockHardness", 5.5, 0.0, 100.0);
        SPACE_CRUISER_RAM_DAMAGE_MULTIPLIER = COMMON_BUILDER.comment("Multiplier for collision damage dealt to entities when ramming with the space cruiser.").defineInRange("ramDamageMultiplier", 1.0, 0.0, 10.0);
        SPACE_CRUISER_MAX_SPEED_MULTIPLIER = COMMON_BUILDER.comment("Maximum flight speed multiplier for the space cruiser.").defineInRange("maxSpeedMultiplier", 1.0, 0.1, 5.0);
        SPACE_CRUISER_ENERGY_DRAIN_BOOST = COMMON_BUILDER.comment("Energy consumed per tick while boosting.").defineInRange("energyDrainBoost", 0.15, 0.0, 5.0);
        SPACE_CRUISER_EXPLOSION_ON_DEATH = COMMON_BUILDER.comment("Whether the space cruiser explodes when its health reaches 0.").define("explosionOnDeath", true);
        SPACE_CRUISER_INVULNERABLE_CREATIVE = COMMON_BUILDER.comment("Makes the space cruiser invulnerable when piloted by a player in creative mode.").define("invulnerableInCreative", true);
        COMMON_BUILDER.pop();
        COMMON_BUILDER.comment("Portal Gun and teleportation settings").push("portals");
        ALLOW_PLAYER_TARGETING = COMMON_BUILDER.comment("Allows the portal gun to target and track online players.").define("allowPlayerTargeting", true);
        MAX_EXPANSION_CARDS = COMMON_BUILDER.comment("Maximum number of expansion cards that can be installed on a portal gun.").defineInRange("maxExpansionCards", 2, 1, 10);
        PORTAL_MAX_LIFETIME_TICKS = COMMON_BUILDER.comment("Maximum portal lifetime in ticks before closing automatically. Set to -1 for infinite lifetime (1200 ticks = 60s).").defineInRange("maxLifetimeTicks", 1200, -1, 72000);
        ALLOW_CROSS_DIMENSION_TELEPORT = COMMON_BUILDER.comment("Allows opening portals across different dimensions.").define("allowCrossDimensionTeleport", true);
        DIMENSION_BLACKLIST = COMMON_BUILDER.comment("List of dimension resource locations where portals cannot be opened (e.g. [\"minecraft:the_end\"]).").defineListAllowEmpty("dimensionBlacklist", Collections.emptyList(), obj -> obj instanceof String);
        ALLOW_ENTITY_TELEPORTATION = COMMON_BUILDER.comment("Allows non-player entities (mobs, items, projectiles, vehicles) to teleport through portals.").define("allowEntityTeleportation", true);
        PORTAL_GUN_MAX_DISTANCE = COMMON_BUILDER.comment("Maximum shooting distance in blocks for the portal gun.").defineInRange("maxDistance", 128.0, 16.0, 1024.0);
        ALLOW_PLAYER_TRACKING_ACROSS_DIMS = COMMON_BUILDER.comment("Allows the Player Locator Card to locate players across dimensions.").define("allowPlayerTrackingAcrossDimensions", true);
        HAZARD_SHIELD_COOLDOWN_SECONDS = COMMON_BUILDER.comment("Cooldown in seconds per entity before Hazard Shield buffs can be reapplied upon teleporting.").defineInRange("hazardShieldCooldownSeconds", 45.0, 1.0, 300.0);
        COMMON_BUILDER.pop();
        COMMON_BUILDER.comment("Weapons and explosives settings").push("weapons");
        RICK_PRIME_BOMB_BREAK_BLOCKS = COMMON_BUILDER.comment("Allows the Rick Prime bomb to destroy blocks on detonation.").define("rickPrimeBombBreakBlocks", true);
        RICK_PRIME_BOMB_RADIUS = COMMON_BUILDER.comment("Detonation radius of the Rick Prime bomb in blocks.").defineInRange("rickPrimeBombRadius", 32, 4, 128);
        RICK_PRIME_BOMB_DISINTEGRATE_ENTITIES = COMMON_BUILDER.comment("Whether the Rick Prime bomb eliminates all caught entities, bypassing totems and invulnerability.").define("rickPrimeBombDisintegrateEntities", true);
        SELF_DESTRUCT_GUN_ENABLED = COMMON_BUILDER.comment("Enables the self-destruct gun.").define("selfDestructGunEnabled", true);
        COMMON_BUILDER.pop();
        COMMON_BUILDER.comment("Phoenix Protocol settings").push("phoenix_protocol");
        PHOENIX_PROTOCOL_ENABLED = COMMON_BUILDER.comment("Enables clone respawning through Phoenix Capsules.").define("enabled", true);
        PHOENIX_PRINT_TIME_SECONDS = COMMON_BUILDER.comment("Time in seconds to print a replacement clone in the Phoenix Capsule.").defineInRange("printTimeSeconds", 30, 1, 600);
        MAX_CAPSULES_PER_PLAYER = COMMON_BUILDER.comment("Maximum number of active Phoenix Capsules allowed per player.").defineInRange("maxCapsulesPerPlayer", 3, 1, 50);
        PHOENIX_CROSS_DIMENSION_SPAWN = COMMON_BUILDER.comment("Allows respawning in a Phoenix Capsule located in another dimension.").define("crossDimensionSpawn", true);
        COMMON_BUILDER.comment("Omega Device settings").push("omega_device");
        OMEGA_DEVICE_MODEL_ENABLED = COMMON_BUILDER.comment("Enables the custom 3D model and physical walkable surfaces/platforms for the Omega Device. If set to false, the apparatus model is hidden and entities will not collide with or walk on its surfaces.").define("modelEnabled", true);
        OMEGA_COOLDOWN_TICKS = COMMON_BUILDER.comment("Cooldown duration in ticks after an erasure sequence (6000 ticks = 5 minutes).").defineInRange("cooldownTicks", 6000, 0, 72000);
        ALLOW_PLAYER_ERASURE = COMMON_BUILDER.comment("Allows the Omega Device to target and erase players across the Multiverse.").define("allowPlayerErasure", true);
        ALLOW_MOB_ERASURE = COMMON_BUILDER.comment("Allows the Omega Device to target and erase mobs and entities across the Multiverse.").define("allowMobErasure", true);
        ALLOW_ITEM_ERASURE = COMMON_BUILDER.comment("Allows the Omega Device to target and erase item types across the Multiverse.").define("allowItemErasure", true);
        ALLOW_BLOCK_ERASURE = COMMON_BUILDER.comment("Allows the Omega Device to erase placed blocks in the world when their item is erased.").define("allowBlockErasure", true);
        COMMON_BUILDER.pop();
        COMMON_BUILDER.comment("Evil Morty Shield settings").push("evil_morty_shield");
        EVIL_MORTY_SHIELD_COOLDOWN_SECONDS = COMMON_BUILDER.comment("Cooldown duration in seconds after Evil Morty Shield hearts are depleted.").defineInRange("cooldownSeconds", 10.0, 1.0, 300.0);
        EVIL_MORTY_SHIELD_PROTECT_ALL_DAMAGE = COMMON_BUILDER.comment("Whether the shield protects against all combat damage (melee, explosions, mob attacks) or only projectiles. Default is false (projectiles only).").define("protectAllDamage", false);
        COMMON_BUILDER.pop();
        COMMON_BUILDER.pop();
        SPEC = COMMON_BUILDER.build();
        CLIENT_BUILDER = new ModConfigSpec.Builder();
        CLIENT_BUILDER.comment("HUD display settings").push("hud");
        HUD_ANCHOR = CLIENT_BUILDER.comment("HUD screen anchor position (BOTTOM_LEFT, BOTTOM_RIGHT, TOP_LEFT, TOP_RIGHT).").define("hudAnchor", (Object)"BOTTOM_LEFT");
        HUD_OFFSET_X = CLIENT_BUILDER.comment("HUD horizontal offset in pixels from the anchor.").defineInRange("hudOffsetX", 12, -2000, 2000);
        HUD_OFFSET_Y = CLIENT_BUILDER.comment("HUD vertical offset in pixels from the anchor.").defineInRange("hudOffsetY", 12, -2000, 2000);
        HUD_SCALE = CLIENT_BUILDER.comment("HUD render scale factor.").defineInRange("hudScale", 1.0, 0.5, 3.0);
        CLIENT_BUILDER.pop();
        CLIENT_BUILDER.comment("Visual and audio effects settings").push("effects");
        THRUSTER_EFFECTS_QUALITY = CLIENT_BUILDER.comment("Space cruiser thruster visual quality (HIGH, MEDIUM, LOW).").define("thrusterQuality", (Object)"HIGH");
        PORTAL_VORTEX_ANIMATION_SPEED = CLIENT_BUILDER.comment("Animation speed multiplier for portal swirls.").defineInRange("portalVortexSpeed", 1.0, 0.0, 5.0);
        CAMERA_SHAKE_INTENSITY = CLIENT_BUILDER.comment("Camera shake intensity multiplier during boosts and explosions (0 to disable).").defineInRange("cameraShakeIntensity", 1.0, 0.0, 2.0);
        SPACE_CRUISER_ENGINE_VOLUME = CLIENT_BUILDER.comment("Volume multiplier for the space cruiser engine sound.").defineInRange("engineSoundVolume", 1.0, 0.0, 1.0);
        CLIENT_BUILDER.pop();
        CLIENT_SPEC = CLIENT_BUILDER.build();
        spaceCruiserBlockBreaking = true;
        spaceCruiserBreakSolidBlocks = false;
        spaceCruiserMaxBlockHardness = 5.5;
        spaceCruiserRamDamageMultiplier = 1.0;
        spaceCruiserMaxSpeedMultiplier = 1.0;
        spaceCruiserEnergyDrainBoost = 0.15;
        spaceCruiserExplosionOnDeath = true;
        spaceCruiserInvulnerableCreative = true;
        allowPlayerTargeting = true;
        maxExpansionCards = 2;
        portalMaxLifetimeTicks = 1200;
        allowCrossDimensionTeleport = true;
        dimensionBlacklist = new ArrayList<String>();
        allowEntityTeleportation = true;
        portalGunMaxDistance = 128.0;
        allowPlayerTrackingAcrossDims = true;
        hazardShieldCooldownSeconds = 45.0;
        rickPrimeBombBreakBlocks = true;
        rickPrimeBombRadius = 32;
        rickPrimeBombDisintegrateEntities = true;
        selfDestructGunEnabled = true;
        phoenixProtocolEnabled = true;
        phoenixPrintTimeSeconds = 30;
        maxCapsulesPerPlayer = 3;
        phoenixCrossDimensionSpawn = true;
        omegaCooldownTicks = 6000;
        allowPlayerErasure = true;
        allowMobErasure = true;
        allowItemErasure = true;
        allowBlockErasure = true;
        omegaDeviceModelEnabled = true;
        evilMortyShieldCooldownSeconds = 10.0;
        evilMortyShieldProtectAllDamage = false;
        hudAnchor = "BOTTOM_LEFT";
        hudOffsetX = 12;
        hudOffsetY = 12;
        hudScale = 1.0;
        thrusterEffectsQuality = "HIGH";
        portalVortexAnimationSpeed = 1.0;
        cameraShakeIntensity = 1.0;
        spaceCruiserEngineVolume = 1.0;
    }
}

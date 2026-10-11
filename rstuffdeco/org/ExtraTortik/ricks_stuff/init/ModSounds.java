/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 */
package org.ExtraTortik.ricks_stuff.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create((ResourceKey)Registries.SOUND_EVENT, (String)"ricks_stuff");
    public static final DeferredHolder<SoundEvent, SoundEvent> PORTAL_TELEPORT = ModSounds.registerSoundEvent("portal_teleport");
    public static final DeferredHolder<SoundEvent, SoundEvent> GUN_FIRE = ModSounds.registerSoundEvent("gun_fire");
    public static final DeferredHolder<SoundEvent, SoundEvent> PLASMA_SCORCH = ModSounds.registerSoundEvent("plasma_scorch");
    public static final DeferredHolder<SoundEvent, SoundEvent> PORTAL_OPEN = ModSounds.registerSoundEvent("portal_open");
    public static final DeferredHolder<SoundEvent, SoundEvent> PORTAL_CLOSE = ModSounds.registerSoundEvent("portal_close");
    public static final DeferredHolder<SoundEvent, SoundEvent> MENU_OPEN = ModSounds.registerSoundEvent("menu_open");
    public static final DeferredHolder<SoundEvent, SoundEvent> GUN_MALFUNCTION = ModSounds.registerSoundEvent("gun_malfunction");
    public static final DeferredHolder<SoundEvent, SoundEvent> GUN_EMPTY = ModSounds.registerSoundEvent("gun_empty");
    public static final DeferredHolder<SoundEvent, SoundEvent> UI_PRESS = ModSounds.registerSoundEvent("ui_press");
    public static final DeferredHolder<SoundEvent, SoundEvent> BULB_INSERT = ModSounds.registerSoundEvent("bulb_insert");
    public static final DeferredHolder<SoundEvent, SoundEvent> BULB_EJECT = ModSounds.registerSoundEvent("bulb_eject");
    public static final DeferredHolder<SoundEvent, SoundEvent> UI_DIAL = ModSounds.registerSoundEvent("ui_dial");
    public static final DeferredHolder<SoundEvent, SoundEvent> EXPLOSION_WISTLE = ModSounds.registerSoundEvent("explosion_wistle");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLENDER_GRIND = ModSounds.registerSoundEvent("blender_grind");
    public static final DeferredHolder<SoundEvent, SoundEvent> MEAT_SQUELCH = ModSounds.registerSoundEvent("meat_squelch");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOOD_SPLASH = ModSounds.registerSoundEvent("blood_splash");
    public static final DeferredHolder<SoundEvent, SoundEvent> CLONE_PRINT_LOOP = ModSounds.registerSoundEvent("clone_print_loop");
    public static final DeferredHolder<SoundEvent, SoundEvent> CLONE_READY = ModSounds.registerSoundEvent("clone_ready");
    public static final DeferredHolder<SoundEvent, SoundEvent> CAPSULE_SHATTER = ModSounds.registerSoundEvent("capsule_shatter");
    public static final DeferredHolder<SoundEvent, SoundEvent> CAPSULE_REPAIR = ModSounds.registerSoundEvent("capsule_repair");
    public static final DeferredHolder<SoundEvent, SoundEvent> BOMB_BEEP = ModSounds.registerSoundEvent("bomb_beep");
    public static final DeferredHolder<SoundEvent, SoundEvent> BOMB_CHARGE = ModSounds.registerSoundEvent("bomb_charge");
    public static final DeferredHolder<SoundEvent, SoundEvent> BOMB_EXPLODE = ModSounds.registerSoundEvent("bomb_explode");
    public static final DeferredHolder<SoundEvent, SoundEvent> ANTI_PORTAL_HUM = ModSounds.registerSoundEvent("anti_portal_hum");
    public static final DeferredHolder<SoundEvent, SoundEvent> ANTI_PORTAL_ACTIVATE = ModSounds.registerSoundEvent("anti_portal_activate");
    public static final DeferredHolder<SoundEvent, SoundEvent> ANTI_PORTAL_DEACTIVATE = ModSounds.registerSoundEvent("anti_portal_deactivate");
    public static final DeferredHolder<SoundEvent, SoundEvent> ANTI_PORTAL_DEFLECT = ModSounds.registerSoundEvent("anti_portal_deflect");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPACE_CRUISER_ENGINE = ModSounds.registerSoundEvent("space_cruiser_engine");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPACE_CRUISER_IDLE = ModSounds.registerSoundEvent("space_cruiser_idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPACE_CRUISER_STARTUP = ModSounds.registerSoundEvent("space_cruiser_startup");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPACE_CRUISER_SHUTDOWN = ModSounds.registerSoundEvent("space_cruiser_shutdown");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPACE_CRUISER_HOVER = ModSounds.registerSoundEvent("space_cruiser_hover");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPACE_CRUISER_SWITCH = ModSounds.registerSoundEvent("space_cruiser_switch");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPACE_CRUISER_LAND = ModSounds.registerSoundEvent("space_cruiser_land");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPACE_CRUISER_BUMP = ModSounds.registerSoundEvent("space_cruiser_bump");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPACE_CRUISER_CRASH = ModSounds.registerSoundEvent("space_cruiser_crash");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPACE_CRUISER_LOW_ENERGY = ModSounds.registerSoundEvent("space_cruiser_low_energy");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPACE_CRUISER_ALARM = ModSounds.registerSoundEvent("space_cruiser_alarm");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPACE_CRUISER_HURT = ModSounds.registerSoundEvent("space_cruiser_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPACE_CRUISER_WARP_CHARGE = ModSounds.registerSoundEvent("space_cruiser_warp_charge");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPACE_CRUISER_REALITY_CRACK = ModSounds.registerSoundEvent("space_cruiser_reality_crack");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPACE_CRUISER_WARP_LOOP = ModSounds.registerSoundEvent("space_cruiser_warp_loop");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPACE_CRUISER_REALITY_SHATTER = ModSounds.registerSoundEvent("space_cruiser_reality_shatter");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPACE_CRUISER_WARP_EXIT = ModSounds.registerSoundEvent("space_cruiser_warp_exit");
    public static final DeferredHolder<SoundEvent, SoundEvent> OMEGA_DEVICE_IDLE = ModSounds.registerSoundEvent("omega_device_idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> OMEGA_DEVICE_START = ModSounds.registerSoundEvent("omega_device_start");
    public static final DeferredHolder<SoundEvent, SoundEvent> OMEGA_DEVICE_WARMUP = ModSounds.registerSoundEvent("omega_device_warmup");
    public static final DeferredHolder<SoundEvent, SoundEvent> OMEGA_DEVICE_CHARGE = ModSounds.registerSoundEvent("omega_device_charge");
    public static final DeferredHolder<SoundEvent, SoundEvent> OMEGA_DEVICE_PUMPING = ModSounds.registerSoundEvent("omega_device_pumping");
    public static final DeferredHolder<SoundEvent, SoundEvent> OMEGA_DEVICE_ERASE = ModSounds.registerSoundEvent("omega_device_erase");
    public static final DeferredHolder<SoundEvent, SoundEvent> OMEGA_DEVICE_COOLDOWN = ModSounds.registerSoundEvent("omega_device_cooldown");
    public static final DeferredHolder<SoundEvent, SoundEvent> OMEGA_DEVICE_AMBIENT_LOOP = ModSounds.registerSoundEvent("omega_device_ambient_loop");
    public static final DeferredHolder<SoundEvent, SoundEvent> OMEGA_DEVICE_SPARK = ModSounds.registerSoundEvent("omega_device_spark");

    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}

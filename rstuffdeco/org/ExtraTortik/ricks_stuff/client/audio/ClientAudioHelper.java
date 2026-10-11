/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.ExtraTortik.ricks_stuff.client.audio.BlenderGrindSoundInstance
 *  org.ExtraTortik.ricks_stuff.client.audio.OmegaDeviceSoundInstance
 *  org.ExtraTortik.ricks_stuff.client.audio.SpaceCruiserWarpSoundInstance
 *  org.ExtraTortik.ricks_stuff.entity.PortalEntity
 *  org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity
 *  org.ExtraTortik.ricks_stuff.entity.WarpState
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 */
package org.ExtraTortik.ricks_stuff.client.audio;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.ExtraTortik.ricks_stuff.client.audio.BlenderGrindSoundInstance;
import org.ExtraTortik.ricks_stuff.client.audio.OmegaDeviceSoundInstance;
import org.ExtraTortik.ricks_stuff.client.audio.SpaceCruiserWarpSoundInstance;
import org.ExtraTortik.ricks_stuff.entity.PortalEntity;
import org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity;
import org.ExtraTortik.ricks_stuff.entity.WarpState;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity;
import org.ExtraTortik.ricks_stuff.init.ModSounds;

@OnlyIn(value=Dist.CLIENT)
public class ClientAudioHelper {
    private static final Map<UUID, BlenderGrindSoundInstance> ACTIVE_GRIND_SOUNDS = new ConcurrentHashMap<UUID, BlenderGrindSoundInstance>();
    private static final Map<UUID, SpaceCruiserWarpSoundInstance> ACTIVE_WARP_SOUNDS = new ConcurrentHashMap<UUID, SpaceCruiserWarpSoundInstance>();
    private static final Map<UUID, OmegaDeviceSoundInstance> ACTIVE_OMEGA_SOUNDS = new ConcurrentHashMap<UUID, OmegaDeviceSoundInstance>();

    public static void handlePortalGrindSound(PortalEntity portal) {
        if (portal == null) {
            return;
        }
        if (portal.isBlenderGrinding() && !portal.isRemoved()) {
            ACTIVE_GRIND_SOUNDS.compute(portal.getUUID(), (uuid, currentSound) -> {
                if (currentSound != null) {
                    if (!currentSound.isStopped()) return currentSound;
                }
                BlenderGrindSoundInstance newSound = new BlenderGrindSoundInstance(portal);
                Minecraft.getInstance().getSoundManager().play((SoundInstance)newSound);
                return newSound;
            });
            return;
        }
        ClientAudioHelper.stopPortalGrindSound(portal.getUUID());
    }

    public static void stopPortalGrindSound(UUID portalUUID) {
        if (portalUUID == null) {
            return;
        }
        BlenderGrindSoundInstance sound = ACTIVE_GRIND_SOUNDS.remove(portalUUID);
        if (sound != null) {
            sound.stopSound();
        }
        Minecraft.getInstance().getSoundManager().stop(((SoundEvent)ModSounds.BLENDER_GRIND.get()).getLocation(), SoundSource.PLAYERS);
    }

    public static void handleCruiserWarpSound(SpaceCruiserEntity cruiser) {
        if (cruiser == null) {
            return;
        }
        if (!cruiser.isRemoved() && cruiser.getWarpStateEnum() == WarpState.TUNNEL_TRANSIT) {
            ACTIVE_WARP_SOUNDS.compute(cruiser.getUUID(), (uuid, currentSound) -> {
                if (currentSound != null) {
                    if (!currentSound.isStopped()) return currentSound;
                }
                SpaceCruiserWarpSoundInstance newSound = new SpaceCruiserWarpSoundInstance(cruiser);
                Minecraft.getInstance().getSoundManager().play((SoundInstance)newSound);
                return newSound;
            });
            return;
        }
        ClientAudioHelper.stopCruiserWarpSound(cruiser.getUUID());
    }

    public static void stopCruiserWarpSound(UUID cruiserUUID) {
        if (cruiserUUID == null) {
            return;
        }
        SpaceCruiserWarpSoundInstance sound = ACTIVE_WARP_SOUNDS.remove(cruiserUUID);
        if (sound != null) {
            sound.stopSound();
        }
        Minecraft.getInstance().getSoundManager().stop(((SoundEvent)ModSounds.SPACE_CRUISER_WARP_LOOP.get()).getLocation(), SoundSource.NEUTRAL);
    }

    public static void handleOmegaDeviceSound(OmegaDeviceEntity device) {
        if (device == null) {
            return;
        }
        if (device.isRemoved()) {
            ClientAudioHelper.stopOmegaDeviceSound(device.getUUID());
            return;
        }
        ACTIVE_OMEGA_SOUNDS.compute(device.getUUID(), (uuid, currentSound) -> {
            if (currentSound != null) {
                if (!currentSound.isStopped()) return currentSound;
            }
            OmegaDeviceSoundInstance newSound = new OmegaDeviceSoundInstance(device);
            Minecraft.getInstance().getSoundManager().play((SoundInstance)newSound);
            return newSound;
        });
    }

    public static void stopOmegaDeviceSound(UUID deviceUUID) {
        if (deviceUUID == null) {
            return;
        }
        OmegaDeviceSoundInstance sound = ACTIVE_OMEGA_SOUNDS.remove(deviceUUID);
        if (sound != null) {
            sound.stopSound();
        }
        Minecraft.getInstance().getSoundManager().stop(((SoundEvent)ModSounds.OMEGA_DEVICE_AMBIENT_LOOP.get()).getLocation(), SoundSource.BLOCKS);
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.resources.sounds.AbstractTickableSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.ExtraTortik.ricks_stuff.client.audio.OmegaDeviceSoundInstance$1
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDevicePhase
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 */
package org.ExtraTortik.ricks_stuff.client.audio;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.ExtraTortik.ricks_stuff.client.audio.OmegaDeviceSoundInstance;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDevicePhase;
import org.ExtraTortik.ricks_stuff.init.ModSounds;

@OnlyIn(value=Dist.CLIENT)
public class OmegaDeviceSoundInstance
extends AbstractTickableSoundInstance {
    private static final float MAX_DISTANCE = 192.0f;
    private final OmegaDeviceEntity device;
    private int tickCount = 0;

    public OmegaDeviceSoundInstance(OmegaDeviceEntity device) {
        super((SoundEvent)ModSounds.OMEGA_DEVICE_AMBIENT_LOOP.get(), SoundSource.BLOCKS, SoundInstance.createUnseededRandom());
        this.device = device;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.85f;
        this.pitch = 1.0f;
        this.x = device.getX();
        this.y = device.getY();
        this.z = device.getZ();
    }

    public void tick() {
        float targetPitch;
        if (this.device == null || this.device.isRemoved()) {
            this.stop();
            return;
        }
        ++this.tickCount;
        this.x = this.device.getX();
        this.y = this.device.getY();
        this.z = this.device.getZ();
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            this.volume = 0.0f;
            return;
        }
        double distSq = player.distanceToSqr(this.x, this.y, this.z);
        if (distSq > 36864.0) {
            this.volume = 0.0f;
            return;
        }
        float distance = (float)Math.sqrt(distSq);
        float distanceAttenuation = (float)Math.pow(1.0f - Mth.clamp((float)(distance / 192.0f), (float)0.0f, (float)1.0f), 0.6);
        OmegaDevicePhase phase = this.device.getPhase();
        if (phase == null) {
            phase = OmegaDevicePhase.IDLE;
        }
        int phaseTicks = this.device.getPhaseTicks();
        float progress = (float)phaseTicks / Math.max(1.0f, (float)phase.getDurationTicks());
        progress = Mth.clamp((float)progress, (float)0.0f, (float)1.0f);
        float finalVolume = (switch (1.$SwitchMap$org$ExtraTortik$ricks_stuff$entity$omega$OmegaDevicePhase[phase.ordinal()]) {
            case 1 -> {
                targetPitch = 1.0f + 0.015f * (float)Math.sin((float)this.tickCount * 0.05f);
                yield 1.35f;
            }
            case 2 -> {
                targetPitch = 1.1f + 0.02f * (float)Math.sin((float)this.tickCount * 0.1f);
                yield 1.5f;
            }
            case 3 -> {
                targetPitch = 1.05f;
                yield 1.2f;
            }
            case 4 -> {
                targetPitch = Mth.lerp((float)progress, (float)1.0f, (float)1.35f);
                yield Mth.lerp((float)progress, (float)1.2f, (float)1.5f);
            }
            case 5 -> {
                float oscillation = 0.05f * (float)Math.sin((float)this.tickCount * 0.4f);
                targetPitch = Mth.lerp((float)progress, (float)1.25f, (float)1.6f) + oscillation;
                yield 1.6f;
            }
            case 6 -> {
                targetPitch = 1.0f;
                yield 0.2f;
            }
            case 7 -> {
                targetPitch = Mth.lerp((float)progress, (float)1.2f, (float)1.0f);
                yield Mth.lerp((float)progress, (float)0.6f, (float)1.35f);
            }
            default -> {
                targetPitch = 1.0f;
                yield 0.5f;
            }
        }) * distanceAttenuation;
        this.pitch = Mth.lerp((float)0.12f, (float)this.pitch, (float)targetPitch);
        this.volume = Mth.lerp((float)0.12f, (float)this.volume, (float)finalVolume);
    }

    public void stopSound() {
        this.stop();
    }

    public OmegaDeviceEntity getDevice() {
        return this.device;
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.resources.sounds.AbstractTickableSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 */
package org.ExtraTortik.ricks_stuff.client.audio;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity;
import org.ExtraTortik.ricks_stuff.init.ModSounds;

public class SpaceCruiserEngineSoundInstance
extends AbstractTickableSoundInstance {
    private final SpaceCruiserEntity ship;

    public SpaceCruiserEngineSoundInstance(SpaceCruiserEntity ship) {
        super((SoundEvent)ModSounds.SPACE_CRUISER_ENGINE.get(), SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.ship = ship;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.1f;
        this.pitch = 1.0f;
        this.x = ship.getX();
        this.y = ship.getY();
        this.z = ship.getZ();
    }

    public void tick() {
        float targetPitch;
        float targetVolume;
        if (this.ship.isRemoved()) {
            this.stop();
            return;
        }
        this.x = this.ship.getX();
        this.y = this.ship.getY();
        this.z = this.ship.getZ();
        boolean hasPilot = this.ship.getControllingPassenger() != null;
        float speed = (float)this.ship.getDeltaMovement().length();
        boolean boosting = this.ship.isBoosting();
        boolean isLanded = this.ship.isLanded();
        if (!this.ship.isEngineOn()) {
            targetVolume = 0.0f;
            targetPitch = 0.5f;
        } else if (isLanded) {
            float groundSpeed = (float)this.ship.getDeltaMovement().horizontalDistance();
            targetPitch = 0.7f + Mth.clamp((float)(groundSpeed * 0.5f), (float)0.0f, (float)0.35f);
            targetVolume = hasPilot ? 0.35f + Mth.clamp((float)(groundSpeed * 0.4f), (float)0.0f, (float)0.35f) : 0.12f;
        } else {
            float speedKmh = (float)((double)speed * 20.0 * 3.6);
            float throttleBoost = Math.abs(this.ship.getThrottle()) * 0.12f;
            targetPitch = 0.85f + Mth.clamp((float)(speedKmh / 140.0f), (float)0.0f, (float)0.85f) + (boosting ? 0.28f : 0.0f) + throttleBoost;
            targetVolume = hasPilot ? 0.6f + Mth.clamp((float)(speedKmh / 100.0f), (float)0.0f, (float)0.4f) : (this.ship.isHoverLock() ? 0.3f : 0.1f);
        }
        this.pitch = Mth.lerp((float)0.14f, (float)this.pitch, (float)targetPitch);
        this.volume = Mth.lerp((float)0.14f, (float)this.volume, (float)(targetVolume * (float)Config.spaceCruiserEngineVolume));
    }

    public void stopSound() {
        this.stop();
    }
}

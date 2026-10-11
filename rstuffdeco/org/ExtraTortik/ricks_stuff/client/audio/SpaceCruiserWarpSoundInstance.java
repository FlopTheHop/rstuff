/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.resources.sounds.AbstractTickableSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity
 *  org.ExtraTortik.ricks_stuff.entity.WarpState
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 */
package org.ExtraTortik.ricks_stuff.client.audio;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity;
import org.ExtraTortik.ricks_stuff.entity.WarpState;
import org.ExtraTortik.ricks_stuff.init.ModSounds;

@OnlyIn(value=Dist.CLIENT)
public class SpaceCruiserWarpSoundInstance
extends AbstractTickableSoundInstance {
    private final SpaceCruiserEntity ship;
    private int tickCount = 0;

    public SpaceCruiserWarpSoundInstance(SpaceCruiserEntity ship) {
        super((SoundEvent)ModSounds.SPACE_CRUISER_WARP_LOOP.get(), SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.ship = ship;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.01f;
        this.pitch = 1.0f;
        this.x = ship.getX();
        this.y = ship.getY();
        this.z = ship.getZ();
    }

    public void tick() {
        if (this.ship.isRemoved()) {
            this.stop();
            return;
        }
        ++this.tickCount;
        this.x = this.ship.getX();
        this.y = this.ship.getY();
        this.z = this.ship.getZ();
        WarpState warpState = this.ship.getWarpStateEnum();
        if (warpState == WarpState.TUNNEL_TRANSIT) {
            float maxVolume = 1.2f * (float)Config.spaceCruiserEngineVolume;
            this.volume = Mth.lerp((float)0.12f, (float)this.volume, (float)maxVolume);
        } else {
            this.volume = Mth.lerp((float)0.15f, (float)this.volume, (float)0.0f);
            if (this.volume < 0.01f || warpState == WarpState.IDLE) {
                this.stop();
                return;
            }
        }
        this.pitch = 1.0f + (float)Math.sin((float)this.tickCount * 0.1f) * 0.05f;
    }

    public void stopSound() {
        this.stop();
    }

    public SpaceCruiserEntity getShip() {
        return this.ship;
    }
}

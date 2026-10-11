/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.resources.sounds.AbstractTickableSoundInstance
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.ExtraTortik.ricks_stuff.entity.PortalEntity
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 */
package org.ExtraTortik.ricks_stuff.client.audio;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.ExtraTortik.ricks_stuff.entity.PortalEntity;
import org.ExtraTortik.ricks_stuff.init.ModSounds;

@OnlyIn(value=Dist.CLIENT)
public class BlenderGrindSoundInstance
extends AbstractTickableSoundInstance {
    private final PortalEntity portal;

    public BlenderGrindSoundInstance(PortalEntity portal) {
        super((SoundEvent)ModSounds.BLENDER_GRIND.get(), SoundSource.PLAYERS, portal.level().getRandom());
        this.portal = portal;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.85f;
        this.pitch = 1.0f;
        this.x = portal.getX();
        this.y = portal.getY();
        this.z = portal.getZ();
    }

    public void tick() {
        if (!this.portal.isRemoved() && this.portal.isBlenderGrinding()) {
            this.x = this.portal.getX();
            this.y = this.portal.getY();
            this.z = this.portal.getZ();
            return;
        }
        this.stop();
    }

    public void stopSound() {
        this.stop();
    }
}

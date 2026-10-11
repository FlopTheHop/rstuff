/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.particle.ParticleProvider
 *  net.minecraft.client.particle.SpriteSet
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.ExtraTortik.ricks_stuff.client.particle.PlasmaShockwaveParticle
 */
package org.ExtraTortik.ricks_stuff.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.ExtraTortik.ricks_stuff.client.particle.PlasmaShockwaveParticle;

@OnlyIn(value=Dist.CLIENT)
public static class PlasmaShockwaveParticle.MagentaProvider
implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet sprites;

    public PlasmaShockwaveParticle.MagentaProvider(SpriteSet sprites) {
        this.sprites = sprites;
    }

    public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
        return new PlasmaShockwaveParticle(level, x, y, z, vx, vy, vz, 1.0f, 0.15f, 0.8f, 2.4f, 16, this.sprites);
    }
}

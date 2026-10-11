/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.particle.ParticleRenderType
 *  net.minecraft.client.particle.SpriteSet
 *  net.minecraft.client.particle.TextureSheetParticle
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 */
package org.ExtraTortik.ricks_stuff.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(value=Dist.CLIENT)
public class PlasmaShockwaveParticle
extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final float baseSize;

    public PlasmaShockwaveParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, float r, float g, float b, SpriteSet sprites) {
        this(level, x, y, z, vx, vy, vz, r, g, b, 0.35f, 8, sprites);
    }

    public PlasmaShockwaveParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, float r, float g, float b, float customBaseSize, int customLifetime, SpriteSet sprites) {
        super(level, x, y, z, vx, vy, vz);
        this.sprites = sprites;
        this.rCol = r;
        this.gCol = g;
        this.bCol = b;
        this.xd = vx * 0.1;
        this.yd = vy * 0.1;
        this.zd = vz * 0.1;
        this.lifetime = customLifetime;
        this.quadSize = this.baseSize = customBaseSize;
        this.hasPhysics = false;
        this.setSpriteFromAge(sprites);
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public int getLightColor(float partialTick) {
        return 0xF000F0;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }
        this.setSpriteFromAge(this.sprites);
        float progress = (float)this.age / (float)this.lifetime;
        this.quadSize = this.baseSize * (1.0f + progress * 4.0f);
        this.alpha = (float)Math.sin((double)(1.0f - progress) * Math.PI * 0.5);
    }
}

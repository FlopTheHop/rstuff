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
public class VortexBeamParticle
extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final float baseSize;

    public VortexBeamParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, float r, float g, float b, SpriteSet sprites) {
        this(level, x, y, z, vx, vy, vz, r, g, b, 0.85f, 6, 0.1, sprites);
    }

    public VortexBeamParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, float r, float g, float b, float customBaseSize, int customLifetime, double speedMult, SpriteSet sprites) {
        super(level, x, y, z, vx, vy, vz);
        this.sprites = sprites;
        this.rCol = r;
        this.gCol = g;
        this.bCol = b;
        this.xd = vx * speedMult;
        this.yd = vy * speedMult;
        this.zd = vz * speedMult;
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
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.92;
        this.yd *= 0.92;
        this.zd *= 0.92;
        float progress = (float)this.age / (float)this.lifetime;
        this.quadSize = this.baseSize * (1.0f + progress * 0.35f);
        this.alpha = progress > 0.6f ? 1.0f - (progress - 0.6f) / 0.4f : 1.0f;
    }
}

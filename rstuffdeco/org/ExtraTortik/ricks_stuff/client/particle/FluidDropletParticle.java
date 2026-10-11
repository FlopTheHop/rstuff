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
public class FluidDropletParticle
extends TextureSheetParticle {
    protected final SpriteSet sprites;
    protected final float baseSize;

    public FluidDropletParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, float r, float g, float b, SpriteSet sprites) {
        super(level, x, y, z, vx, vy, vz);
        this.sprites = sprites;
        this.rCol = r;
        this.gCol = g;
        this.bCol = b;
        this.xd = vx + (Math.random() - 0.5) * 0.08;
        this.yd = vy + Math.random() * 0.05;
        this.zd = vz + (Math.random() - 0.5) * 0.08;
        this.gravity = 0.45f;
        this.lifetime = (int)(14.0 + Math.random() * 8.0);
        this.quadSize = this.baseSize = 0.095f + (float)(Math.random() * (double)0.025f);
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
        this.yd -= 0.04 * (double)this.gravity;
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.96;
        this.yd *= 0.96;
        this.zd *= 0.96;
        if (this.onGround) {
            this.xd *= 0.7;
            this.zd *= 0.7;
        }
        float progress = (float)this.age / (float)this.lifetime;
        this.quadSize = this.baseSize * Math.max(0.0f, 1.0f - progress);
        this.alpha = Math.max(0.0f, 1.0f - progress * progress);
    }
}

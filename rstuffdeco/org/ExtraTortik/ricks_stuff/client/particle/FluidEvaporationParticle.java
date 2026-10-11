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
public class FluidEvaporationParticle
extends TextureSheetParticle {
    private final SpriteSet sprites;

    public FluidEvaporationParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, SpriteSet sprites) {
        super(level, x, y, z, vx, vy, vz);
        this.sprites = sprites;
        this.rCol = 0.2f;
        this.gCol = 0.95f;
        this.bCol = 0.15f;
        this.xd = vx * 0.1 + (Math.random() - 0.5) * 0.04;
        this.yd = vy * 0.1 + 0.04 + Math.random() * 0.03;
        this.zd = vz * 0.1 + (Math.random() - 0.5) * 0.04;
        this.lifetime = (int)(12.0 + Math.random() * 8.0);
        this.quadSize = 0.055f + (float)(Math.random() * (double)0.025f);
        this.hasPhysics = true;
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
        this.yd *= 0.96;
        this.zd *= 0.92;
        float progress = (float)this.age / (float)this.lifetime;
        this.alpha = 1.0f - progress;
    }
}

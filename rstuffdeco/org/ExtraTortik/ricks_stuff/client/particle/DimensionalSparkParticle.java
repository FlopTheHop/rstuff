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
public class DimensionalSparkParticle
extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final float baseQuadSize;

    public DimensionalSparkParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, float r, float g, float b, SpriteSet sprites) {
        super(level, x, y, z, vx, vy, vz);
        this.sprites = sprites;
        this.rCol = r;
        this.gCol = g;
        this.bCol = b;
        this.xd = vx * 0.2 + (Math.random() - 0.5) * 0.04;
        this.yd = vy * 0.2 + (Math.random() - 0.5) * 0.04;
        this.zd = vz * 0.2 + (Math.random() - 0.5) * 0.04;
        this.lifetime = (int)(12.0 + Math.random() * 12.0);
        this.quadSize = this.baseQuadSize = 0.06f + (float)(Math.random() * (double)0.04f);
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
        this.xd *= 0.88;
        this.yd *= 0.88;
        this.zd *= 0.88;
        float progress = (float)this.age / (float)this.lifetime;
        this.quadSize = this.baseQuadSize * (float)Math.sin((double)progress * Math.PI);
        this.alpha = 1.0f - progress * progress;
    }
}

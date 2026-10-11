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
public class FluidPoisonParticle
extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final float baseSize;

    public FluidPoisonParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, SpriteSet sprites) {
        super(level, x, y, z, vx, vy, vz);
        this.sprites = sprites;
        this.rCol = 1.0f;
        this.gCol = 1.0f;
        this.bCol = 1.0f;
        this.xd = vx * 0.35;
        this.yd = vy * 0.35;
        this.zd = vz * 0.35;
        this.lifetime = (int)(20.0 + Math.random() * 10.0);
        this.quadSize = this.baseSize = 0.48f + (float)(Math.random() * (double)0.16f);
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
        this.xd *= 0.82;
        this.yd = this.yd * 0.82 + 0.008;
        this.zd *= 0.82;
        float progress = (float)this.age / (float)this.lifetime;
        this.quadSize = this.baseSize * (1.0f + progress * 0.25f);
        this.alpha = (float)Math.sin((double)(1.0f - progress) * Math.PI * 0.5);
    }
}

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
public class OmegaLightningParticle
extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final float baseSize;
    private final float jitterIntensity;

    public OmegaLightningParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, SpriteSet sprites) {
        super(level, x, y, z, vx, vy, vz);
        this.sprites = sprites;
        float colorTint = level.random.nextFloat();
        if (colorTint < 0.35f) {
            this.rCol = 0.9f;
            this.gCol = 1.0f;
            this.bCol = 0.95f;
        } else if (colorTint < 0.75f) {
            this.rCol = 0.22f;
            this.gCol = 1.0f;
            this.bCol = 0.25f;
        } else {
            this.rCol = 0.05f;
            this.gCol = 0.9f;
            this.bCol = 1.0f;
        }
        this.xd = vx * 0.3 + (Math.random() - 0.5) * 0.15;
        this.yd = vy * 0.3 + (Math.random() - 0.5) * 0.15;
        this.zd = vz * 0.3 + (Math.random() - 0.5) * 0.15;
        this.lifetime = (int)(6.0 + Math.random() * 8.0);
        this.quadSize = this.baseSize = 0.22f + (float)(Math.random() * 0.25);
        this.hasPhysics = false;
        this.jitterIntensity = 0.08f + (float)(Math.random() * (double)0.06f);
        this.oRoll = this.roll = (float)(Math.random() * Math.PI * 2.0);
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
        this.oRoll = this.roll;
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }
        this.x += this.xd + (Math.random() - 0.5) * (double)this.jitterIntensity;
        this.y += this.yd + (Math.random() - 0.5) * (double)this.jitterIntensity;
        this.z += this.zd + (Math.random() - 0.5) * (double)this.jitterIntensity;
        this.roll += (float)((Math.random() - 0.5) * 0.6);
        float progress = (float)this.age / (float)this.lifetime;
        this.alpha = 1.0f - progress;
        this.quadSize = this.baseSize * (1.0f + 0.5f * (float)Math.sin((double)progress * Math.PI));
        this.setSpriteFromAge(this.sprites);
    }
}

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
public class QuantumGlitchParticle
extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final float baseSize;
    private int glitchTimer = 0;

    public QuantumGlitchParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, SpriteSet sprites) {
        super(level, x, y, z, vx, vy, vz);
        this.sprites = sprites;
        this.xd = vx * 0.1;
        this.yd = vy * 0.1;
        this.zd = vz * 0.1;
        this.lifetime = (int)(12.0 + Math.random() * 16.0);
        this.quadSize = this.baseSize = 0.25f + (float)(Math.random() * (double)0.35f);
        this.hasPhysics = false;
        this.updateGlitchColor();
        this.setSpriteFromAge(sprites);
    }

    private void updateGlitchColor() {
        int colorMode = this.random.nextInt(4);
        switch (colorMode) {
            case 0: {
                this.rCol = 0.05f;
                this.gCol = 0.95f;
                this.bCol = 1.0f;
                break;
            }
            case 1: {
                this.rCol = 1.0f;
                this.gCol = 0.1f;
                this.bCol = 0.85f;
                break;
            }
            case 2: {
                this.rCol = 0.3f;
                this.gCol = 1.0f;
                this.bCol = 0.1f;
                break;
            }
            default: {
                this.rCol = 0.95f;
                this.gCol = 0.98f;
                this.bCol = 1.0f;
            }
        }
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
        if (++this.glitchTimer % 2 == 0) {
            this.updateGlitchColor();
            double jump = (this.random.nextDouble() - 0.5) * 0.4;
            if (this.random.nextBoolean()) {
                this.x += jump;
            } else {
                this.z += jump;
            }
            this.y += (this.random.nextDouble() - 0.5) * 0.15;
            this.quadSize = this.baseSize * (0.6f + this.random.nextFloat() * 0.9f);
        }
        this.x += this.xd;
        this.y += this.yd;
        this.z += this.zd;
        float baseAlpha = 1.0f - (float)this.age / (float)this.lifetime;
        this.alpha = this.random.nextFloat() > 0.15f ? baseAlpha * (0.7f + this.random.nextFloat() * 0.3f) : 0.1f;
        this.setSpriteFromAge(this.sprites);
    }
}

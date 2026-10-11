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
public class AcidVaporParticle
extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final float baseSize;
    private final double driftPhase;

    public AcidVaporParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, SpriteSet sprites) {
        super(level, x, y, z, vx, vy, vz);
        this.sprites = sprites;
        this.rCol = 1.0f;
        this.gCol = 1.0f;
        this.bCol = 1.0f;
        this.xd = vx * 0.2 + (Math.random() - 0.5) * 0.02;
        this.yd = vy * 0.2 + 0.025 + Math.random() * 0.035;
        this.zd = vz * 0.2 + (Math.random() - 0.5) * 0.02;
        this.lifetime = (int)(24.0 + Math.random() * 16.0);
        this.quadSize = this.baseSize = 0.25f + (float)(Math.random() * (double)0.18f);
        this.hasPhysics = false;
        this.driftPhase = Math.random() * Math.PI * 2.0;
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
        this.xd += Math.sin((double)this.age * 0.15 + this.driftPhase) * 0.003;
        this.zd += Math.cos((double)this.age * 0.15 + this.driftPhase) * 0.003;
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.94;
        this.yd = this.yd * 0.95 + 0.003;
        this.zd *= 0.94;
        float progress = (float)this.age / (float)this.lifetime;
        this.quadSize = this.baseSize * (1.0f + progress * 0.5f);
        this.alpha = Math.max(0.0f, (1.0f - progress) * 0.9f);
    }
}

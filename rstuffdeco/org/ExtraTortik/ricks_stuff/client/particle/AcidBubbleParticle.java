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
public class AcidBubbleParticle
extends TextureSheetParticle {
    private final SpriteSet sprites;

    public AcidBubbleParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, SpriteSet sprites) {
        super(level, x, y, z, vx, vy, vz);
        this.sprites = sprites;
        this.rCol = 1.0f;
        this.gCol = 1.0f;
        this.bCol = 1.0f;
        this.xd = vx * 0.1 + (Math.random() - 0.5) * 0.01;
        this.yd = vy * 0.1 + 0.03 + Math.random() * 0.02;
        this.zd = vz * 0.1 + (Math.random() - 0.5) * 0.01;
        this.lifetime = (int)(14.0 + Math.random() * 12.0);
        this.quadSize = 0.08f + (float)(Math.random() * (double)0.06f);
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
        this.xd *= 0.96;
        this.yd *= 0.98;
        this.zd *= 0.96;
        float progress = (float)this.age / (float)this.lifetime;
        this.alpha = progress > 0.8f ? (1.0f - progress) * 5.0f : 1.0f;
    }
}

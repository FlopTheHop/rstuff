/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.particle.ParticleRenderType
 *  net.minecraft.client.particle.SpriteSet
 *  net.minecraft.client.particle.TextureSheetParticle
 *  net.minecraft.util.Mth
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 */
package org.ExtraTortik.ricks_stuff.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(value=Dist.CLIENT)
public class OmegaShockwaveParticle
extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final float baseSize;
    private final float maxSize;

    public OmegaShockwaveParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, SpriteSet sprites) {
        super(level, x, y, z, vx, vy, vz);
        this.sprites = sprites;
        float colorTint = level.random.nextFloat();
        if (colorTint < 0.7f) {
            this.rCol = 0.22f;
            this.gCol = 1.0f;
            this.bCol = 0.25f;
        } else {
            this.rCol = 0.05f;
            this.gCol = 0.9f;
            this.bCol = 1.0f;
        }
        this.xd = vx * 0.1;
        this.yd = vy * 0.1;
        this.zd = vz * 0.1;
        this.lifetime = (int)(14.0 + Math.random() * 8.0);
        this.baseSize = 1.2f + (float)(Math.random() * (double)0.8f);
        this.maxSize = this.baseSize * (4.5f + (float)(Math.random() * 2.5));
        this.quadSize = this.baseSize;
        this.hasPhysics = false;
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
        this.x += this.xd;
        this.y += this.yd;
        this.z += this.zd;
        float progress = (float)this.age / (float)this.lifetime;
        float eased = 1.0f - (1.0f - progress) * (1.0f - progress);
        this.quadSize = Mth.lerp((float)eased, (float)this.baseSize, (float)this.maxSize);
        this.alpha = (1.0f - progress * progress) * 0.9f;
        this.setSpriteFromAge(this.sprites);
    }
}

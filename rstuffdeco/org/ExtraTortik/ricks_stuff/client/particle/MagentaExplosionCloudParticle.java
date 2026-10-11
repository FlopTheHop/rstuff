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
public class MagentaExplosionCloudParticle
extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final float baseSize;
    private final float rotSpeed;

    public MagentaExplosionCloudParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, SpriteSet sprites) {
        super(level, x, y, z, vx, vy, vz);
        this.sprites = sprites;
        this.rCol = 1.0f;
        this.gCol = 1.0f;
        this.bCol = 1.0f;
        this.xd = vx * 0.45 + (Math.random() - 0.5) * 0.08;
        this.yd = vy * 0.45 + 0.05 + Math.random() * 0.06;
        this.zd = vz * 0.45 + (Math.random() - 0.5) * 0.08;
        this.lifetime = (int)(32.0 + Math.random() * 18.0);
        this.quadSize = this.baseSize = 3.2f + (float)(Math.random() * (double)1.6f);
        this.hasPhysics = false;
        this.oRoll = this.roll = (float)(Math.random() * Math.PI * 2.0);
        this.rotSpeed = (float)((Math.random() - 0.5) * 0.04);
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
        this.oRoll = this.roll;
        this.roll += this.rotSpeed;
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.93;
        this.yd = this.yd * 0.93 + 0.018;
        this.zd *= 0.93;
        float progress = (float)this.age / (float)this.lifetime;
        if (progress < 0.65f) {
            float expandProgress = progress / 0.65f;
            this.quadSize = this.baseSize * (1.0f + expandProgress * 3.8f);
        } else {
            this.quadSize = this.baseSize * 4.8f;
        }
        if (progress > 0.65f) {
            float fadeProgress = (progress - 0.65f) / 0.35f;
            this.alpha = 1.0f - fadeProgress;
        } else {
            this.alpha = 1.0f;
        }
    }
}

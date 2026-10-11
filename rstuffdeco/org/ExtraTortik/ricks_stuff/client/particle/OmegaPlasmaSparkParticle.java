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
public class OmegaPlasmaSparkParticle
extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final float baseSize;
    private final double orbitRadius;
    private double orbitAngle;
    private final double orbitSpeed;
    private final double targetY;

    public OmegaPlasmaSparkParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, SpriteSet sprites) {
        super(level, x, y, z, vx, vy, vz);
        this.sprites = sprites;
        float colorRand = level.random.nextFloat();
        if (colorRand < 0.5f) {
            this.rCol = 0.22f + level.random.nextFloat() * 0.15f;
            this.gCol = 1.0f;
            this.bCol = 0.15f + level.random.nextFloat() * 0.25f;
        } else if (colorRand < 0.85f) {
            this.rCol = 0.0f;
            this.gCol = 0.9f + level.random.nextFloat() * 0.1f;
            this.bCol = 1.0f;
        } else {
            this.rCol = 0.7f + level.random.nextFloat() * 0.3f;
            this.gCol = 1.0f;
            this.bCol = 0.2f;
        }
        this.orbitRadius = Math.sqrt(vx * vx + vz * vz) + 0.1;
        this.orbitAngle = Math.atan2(vz, vx);
        this.orbitSpeed = 0.15 + (Math.random() - 0.5) * 0.1;
        this.targetY = vy;
        this.xd = vx;
        this.yd = vy;
        this.zd = vz;
        this.lifetime = (int)(16.0 + Math.random() * 16.0);
        this.quadSize = this.baseSize = 0.15f + (float)(Math.random() * (double)0.18f);
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
        this.orbitAngle += this.orbitSpeed;
        double currentRadius = this.orbitRadius * (1.0 - (double)this.age / (double)this.lifetime * 0.3);
        this.xd = this.xd * 0.92 + Math.cos(this.orbitAngle) * currentRadius * 0.05;
        this.zd = this.zd * 0.92 + Math.sin(this.orbitAngle) * currentRadius * 0.05;
        this.yd = this.yd * 0.95 + (Math.random() - 0.5) * 0.02;
        this.move(this.xd, this.yd, this.zd);
        this.roll += 0.2f;
        float progress = (float)this.age / (float)this.lifetime;
        this.alpha = (float)Math.sin((double)progress * Math.PI);
        this.quadSize = this.baseSize * (1.0f + 0.3f * (float)Math.sin((double)progress * Math.PI * 2.0));
        this.setSpriteFromAge(this.sprites);
    }
}

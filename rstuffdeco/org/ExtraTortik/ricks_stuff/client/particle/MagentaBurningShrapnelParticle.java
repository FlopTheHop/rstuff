/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.particle.ParticleRenderType
 *  net.minecraft.client.particle.SpriteSet
 *  net.minecraft.client.particle.TextureSheetParticle
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.ExtraTortik.ricks_stuff.init.ModParticles
 */
package org.ExtraTortik.ricks_stuff.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.ExtraTortik.ricks_stuff.init.ModParticles;

@OnlyIn(value=Dist.CLIENT)
public class MagentaBurningShrapnelParticle
extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final float baseQuadSize;
    private final float rotSpeed;

    public MagentaBurningShrapnelParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, SpriteSet sprites) {
        super(level, x, y, z, vx, vy, vz);
        this.sprites = sprites;
        this.rCol = 1.0f;
        this.gCol = 1.0f;
        this.bCol = 1.0f;
        this.xd = vx;
        this.yd = vy;
        this.zd = vz;
        this.lifetime = (int)(35.0 + Math.random() * 25.0);
        this.quadSize = this.baseQuadSize = 0.28f + (float)(Math.random() * (double)0.16f);
        this.hasPhysics = true;
        this.gravity = 1.15f;
        this.oRoll = this.roll = (float)(Math.random() * Math.PI * 2.0);
        this.rotSpeed = (float)((Math.random() - 0.5) * 0.45);
        this.setSpriteFromAge(sprites);
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public int getLightColor(float partialTick) {
        return 0xF000F0;
    }

    public void tick() {
        float progress;
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }
        this.oRoll = this.roll;
        this.roll += this.rotSpeed;
        this.setSpriteFromAge(this.sprites);
        this.yd -= 0.04 * (double)this.gravity;
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.98;
        this.yd *= 0.98;
        this.zd *= 0.98;
        if (this.onGround) {
            this.xd *= 0.7;
            this.zd *= 0.7;
        }
        if (this.level != null && !this.onGround) {
            this.level.addParticle((ParticleOptions)ParticleTypes.CAMPFIRE_COSY_SMOKE, this.x, this.y, this.z, -this.xd * 0.15 + (Math.random() - 0.5) * 0.02, 0.02 + Math.random() * 0.03, -this.zd * 0.15 + (Math.random() - 0.5) * 0.02);
            if (this.age % 2 == 0) {
                this.level.addParticle((ParticleOptions)ModParticles.MAGENTA_DIMENSIONAL_SPARK.get(), this.x, this.y, this.z, -this.xd * 0.2 + (Math.random() - 0.5) * 0.03, 0.02, -this.zd * 0.2 + (Math.random() - 0.5) * 0.03);
            }
        }
        if (!((progress = (float)this.age / (float)this.lifetime) > 0.7f)) return;
        float fade = 1.0f - (progress - 0.7f) / 0.3f;
        this.quadSize = this.baseQuadSize * Math.max(0.0f, fade);
        this.alpha = Math.max(0.0f, fade);
    }
}

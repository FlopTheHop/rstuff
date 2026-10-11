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
public class OmegaCoronaPetalParticle
extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final float baseSize;
    private final float targetSize;
    private final float rotSpeed;

    public OmegaCoronaPetalParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, SpriteSet sprites) {
        super(level, x, y, z, vx, vy, vz);
        this.sprites = sprites;
        float blend = level.random.nextFloat();
        if (blend < 0.6f) {
            this.rCol = 0.68f;
            this.gCol = 1.0f;
            this.bCol = 0.18f;
        } else {
            this.rCol = 0.15f;
            this.gCol = 0.95f;
            this.bCol = 1.0f;
        }
        this.xd = vx * 0.4;
        this.yd = vy * 0.4;
        this.zd = vz * 0.4;
        this.lifetime = (int)(18.0 + Math.random() * 12.0);
        this.baseSize = 0.8f + (float)(Math.random() * (double)0.6f);
        this.targetSize = this.baseSize * (2.4f + (float)(Math.random() * (double)1.2f));
        this.quadSize = this.baseSize;
        this.hasPhysics = false;
        this.oRoll = this.roll = (float)(Math.random() * Math.PI * 2.0);
        this.rotSpeed = (float)((Math.random() - 0.5) * 0.08);
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
        this.roll += this.rotSpeed;
        float progress = (float)this.age / (float)this.lifetime;
        this.quadSize = Mth.lerp((float)progress, (float)this.baseSize, (float)this.targetSize);
        this.alpha = (1.0f - progress) * 0.85f;
        this.setSpriteFromAge(this.sprites);
    }
}

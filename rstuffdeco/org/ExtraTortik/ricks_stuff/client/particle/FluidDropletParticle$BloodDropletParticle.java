/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.particle.SpriteSet
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.ExtraTortik.ricks_stuff.client.particle.FluidDropletParticle
 */
package org.ExtraTortik.ricks_stuff.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.ExtraTortik.ricks_stuff.client.particle.FluidDropletParticle;

@OnlyIn(value=Dist.CLIENT)
public static class FluidDropletParticle.BloodDropletParticle
extends FluidDropletParticle {
    public FluidDropletParticle.BloodDropletParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, SpriteSet sprites) {
        super(level, x, y, z, vx, vy, vz, 0.85f, 0.05f, 0.08f, sprites);
        this.xd = vx;
        this.yd = vy;
        this.zd = vz;
        this.gravity = 0.38f;
        this.lifetime = (int)(35.0 + Math.random() * 35.0);
        this.quadSize = 0.065f + (float)(Math.random() * (double)0.04f);
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
        this.yd -= 0.04 * (double)this.gravity;
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.985;
        this.yd *= 0.985;
        this.zd *= 0.985;
        if (this.onGround) {
            this.xd *= 0.6;
            this.zd *= 0.6;
        }
        float progress = (float)this.age / (float)this.lifetime;
        this.quadSize = this.baseSize * Math.max(0.0f, 1.0f - progress * 0.6f);
        this.alpha = Math.max(0.0f, 1.0f - progress * progress);
    }
}

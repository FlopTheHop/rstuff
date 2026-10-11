/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.Camera
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.particle.ParticleRenderType
 *  net.minecraft.client.particle.SpriteSet
 *  net.minecraft.client.particle.TextureSheetParticle
 *  net.minecraft.util.Mth
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 */
package org.ExtraTortik.ricks_stuff.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(value=Dist.CLIENT)
public class FluidGroundDropParticle
extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final float baseSize;

    public FluidGroundDropParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z, 0.0, 0.0, 0.0);
        this.sprites = sprites;
        this.rCol = 1.0f;
        this.gCol = 1.0f;
        this.bCol = 1.0f;
        this.xd = 0.0;
        this.yd = 0.0;
        this.zd = 0.0;
        this.lifetime = (int)(35.0 + Math.random() * 25.0);
        this.quadSize = this.baseSize = 0.45f + (float)(Math.random() * 0.25);
        this.roll = (float)(Math.random() * Math.PI * 2.0);
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
        float progress = (float)this.age / (float)this.lifetime;
        this.alpha = progress > 0.65f ? 1.0f - (progress - 0.65f) / 0.35f : 1.0f;
    }

    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        Vec3 camPos = camera.getPosition();
        float px = (float)(Mth.lerp((double)partialTicks, (double)this.xo, (double)this.x) - camPos.x());
        float py = (float)(Mth.lerp((double)partialTicks, (double)this.yo, (double)this.y) - camPos.y()) + 0.015f;
        float pz = (float)(Mth.lerp((double)partialTicks, (double)this.zo, (double)this.z) - camPos.z());
        float size = this.quadSize;
        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();
        int light = this.getLightColor(partialTicks);
        float sin = Mth.sin((float)this.roll);
        float cos = Mth.cos((float)this.roll);
        float x0 = (-cos - sin) * size;
        float z0 = (-sin + cos) * size;
        float x1 = (cos - sin) * size;
        float z1 = (sin + cos) * size;
        float x2 = (cos + sin) * size;
        float z2 = (sin - cos) * size;
        float x3 = (-cos + sin) * size;
        float z3 = (-sin - cos) * size;
        consumer.addVertex(px + x0, py, pz + z0).setUv(u0, v1).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(light);
        consumer.addVertex(px + x1, py, pz + z1).setUv(u1, v1).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(light);
        consumer.addVertex(px + x2, py, pz + z2).setUv(u1, v0).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(light);
        consumer.addVertex(px + x3, py, pz + z3).setUv(u0, v0).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(light);
    }
}

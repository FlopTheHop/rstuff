/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.core.particles.ParticleOptions
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.ExtraTortik.ricks_stuff.init.ModParticles
 */
package org.ExtraTortik.ricks_stuff.client.particle;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleOptions;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.ExtraTortik.ricks_stuff.init.ModParticles;

@OnlyIn(value=Dist.CLIENT)
public class ClientPortalParticleHelper {
    public static void spawnTintedParticle(ParticleOptions options, double x, double y, double z, double vx, double vy, double vz, float r, float g, float b) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        if (mc.particleEngine == null) {
            return;
        }
        Particle p = mc.particleEngine.createParticle(options, x, y, z, vx, vy, vz);
        if (p == null) return;
        p.setColor(r, g, b);
    }

    public static void spawnPortalBeam(double x, double y, double z, float r, float g, float b) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        if (mc.particleEngine == null) {
            return;
        }
        Particle p = mc.particleEngine.createParticle((ParticleOptions)ModParticles.PORTAL_VORTEX_BEAM.get(), x, y, z, (double)r, (double)g, (double)b);
        if (p == null) return;
        p.setColor(r, g, b);
    }

    public static void spawnPortalSpark(double x, double y, double z, double vx, double vy, double vz, float r, float g, float b) {
        ClientPortalParticleHelper.spawnTintedParticle((ParticleOptions)ModParticles.GREEN_DIMENSIONAL_SPARK.get(), x, y, z, vx, vy, vz, r, g, b);
    }

    public static void spawnPortalDroplet(double x, double y, double z, double vx, double vy, double vz, float r, float g, float b) {
        ClientPortalParticleHelper.spawnTintedParticle((ParticleOptions)ModParticles.GREEN_FLUID_DROPLET.get(), x, y, z, vx, vy, vz, r, g, b);
    }

    public static void spawnPortalShockwave(double x, double y, double z, float r, float g, float b) {
        ClientPortalParticleHelper.spawnTintedParticle((ParticleOptions)ModParticles.GREEN_PLASMA_SHOCKWAVE.get(), x, y, z, 0.0, 0.0, 0.0, r, g, b);
    }

    public static void spawnPortalEvaporation(double x, double y, double z, double vx, double vy, double vz, float r, float g, float b) {
        ClientPortalParticleHelper.spawnTintedParticle((ParticleOptions)ModParticles.FLUID_EVAPORATION.get(), x, y, z, vx, vy, vz, r, g, b);
    }

    public static void spawnFluidHitParticles(double x, double y, double z, double groundY, boolean hasGround, int rgb) {
        int i;
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        if (mc.particleEngine == null) {
            return;
        }
        float r = (float)(rgb >> 16 & 0xFF) / 255.0f;
        float g = (float)(rgb >> 8 & 0xFF) / 255.0f;
        float b = (float)(rgb & 0xFF) / 255.0f;
        Random random = new Random();
        for (i = 0; i < 100; ++i) {
            double theta = random.nextDouble() * Math.PI * 2.0;
            double cosPhi = random.nextDouble() * 2.0 - 1.0;
            double sinPhi = Math.sqrt(Math.max(0.0, 1.0 - cosPhi * cosPhi));
            double speed = 0.15 + random.nextDouble() * 0.3;
            double vx = sinPhi * Math.cos(theta) * speed;
            double vy = cosPhi * speed + 0.02;
            double vz = sinPhi * Math.sin(theta) * speed;
            double px = x + (random.nextDouble() - 0.5) * 0.35;
            double py = y + (random.nextDouble() - 0.5) * 0.35;
            double pz = z + (random.nextDouble() - 0.5) * 0.35;
            Particle p = mc.particleEngine.createParticle((ParticleOptions)ModParticles.FLUID_POISON.get(), px, py, pz, vx, vy, vz);
            if (p == null) continue;
            p.setColor(r, g, b);
        }
        if (!hasGround) return;
        i = 0;
        while (i < 10) {
            double angle = random.nextDouble() * Math.PI * 2.0;
            double dist = 0.2 + Math.sqrt(random.nextDouble()) * 1.4;
            double gx = x + Math.cos(angle) * dist;
            double gz = z + Math.sin(angle) * dist;
            Particle p = mc.particleEngine.createParticle((ParticleOptions)ModParticles.FLUID_GROUND_DROP.get(), gx, groundY, gz, 0.0, 0.0, 0.0);
            if (p != null) {
                p.setColor(r, g, b);
            }
            ++i;
        }
    }
}

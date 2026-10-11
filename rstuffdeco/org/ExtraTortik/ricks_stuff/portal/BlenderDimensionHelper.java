/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.particles.ItemParticleOption
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.damagesource.DamageType
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  org.ExtraTortik.ricks_stuff.entity.PortalEntity
 *  org.ExtraTortik.ricks_stuff.init.ModParticles
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.portal.PortalTeleportEngine
 */
package org.ExtraTortik.ricks_stuff.portal;

import java.util.Locale;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.ExtraTortik.ricks_stuff.entity.PortalEntity;
import org.ExtraTortik.ricks_stuff.init.ModParticles;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.portal.PortalTeleportEngine;

public class BlenderDimensionHelper {
    public static final ResourceKey<DamageType> BLENDER_DAMAGE_TYPE = ResourceKey.create((ResourceKey)Registries.DAMAGE_TYPE, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"blender_dimension"));

    public static boolean isBlenderDimension(String dimensionStr) {
        if (dimensionStr == null) return false;
        if (dimensionStr.trim().isEmpty()) {
            return false;
        }
        String clean = dimensionStr.trim().toLowerCase(Locale.ROOT).replace(" ", "_").replace("-", "_");
        if (!clean.contains(":")) return clean.equals("blenders_dimension") || clean.equals("blender_dimension") || clean.equals("blender") || clean.equals("blenders") || clean.equals("dimension_of_blenders") || clean.equals("dimension_blenders") || clean.contains("blender") || clean.contains("\u0431\u043b\u0435\u043d\u0434\u0435\u0440");
        clean = clean.substring(clean.indexOf(":") + 1);
        return clean.equals("blenders_dimension") || clean.equals("blender_dimension") || clean.equals("blender") || clean.equals("blenders") || clean.equals("dimension_of_blenders") || clean.equals("dimension_blenders") || clean.contains("blender") || clean.contains("\u0431\u043b\u0435\u043d\u0434\u0435\u0440");
    }

    public static DamageSource getDamageSource(Level level) {
        try {
            return new DamageSource((Holder)level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(BLENDER_DAMAGE_TYPE));
        }
        catch (Exception e) {
            return level.damageSources().genericKill();
        }
    }

    public static void shredEntity(PortalEntity portal, Entity entity) {
        Player player;
        if (entity == null) return;
        if (entity.isRemoved()) return;
        Level level = portal.level();
        if (!(level instanceof ServerLevel)) return;
        ServerLevel serverLevel = (ServerLevel)level;
        RandomSource random = serverLevel.getRandom();
        if (entity instanceof ItemEntity) {
            ItemEntity itemEntity = (ItemEntity)entity;
            ItemStack stack = itemEntity.getItem();
            if (!stack.isEmpty()) {
                portal.addItemGrind(stack);
                BlenderDimensionHelper.spawnItemShredEruption(serverLevel, portal, stack, random);
            }
            serverLevel.playSound(null, portal.getX(), portal.getY(), portal.getZ(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 0.8f, 0.9f + random.nextFloat() * 0.2f);
            serverLevel.playSound(null, portal.getX(), portal.getY(), portal.getZ(), (SoundEvent)ModSounds.BLENDER_GRIND.get(), SoundSource.PLAYERS, 0.85f, 0.95f + random.nextFloat() * 0.1f);
            itemEntity.discard();
            return;
        }
        entity.setPos(portal.getX(), portal.getY() - (double)entity.getBbHeight() * 0.3, portal.getZ());
        entity.setDeltaMovement(Vec3.ZERO);
        entity.hurtMarked = true;
        serverLevel.playSound(null, portal.getX(), portal.getY(), portal.getZ(), (SoundEvent)ModSounds.BLENDER_GRIND.get(), SoundSource.PLAYERS, 0.75f, 0.95f + random.nextFloat() * 0.1f);
        serverLevel.playSound(null, portal.getX(), portal.getY(), portal.getZ(), (SoundEvent)ModSounds.MEAT_SQUELCH.get(), SoundSource.PLAYERS, 0.7f, 0.85f + random.nextFloat() * 0.2f);
        serverLevel.playSound(null, portal.getX(), portal.getY(), portal.getZ(), (SoundEvent)ModSounds.BLOOD_SPLASH.get(), SoundSource.PLAYERS, 0.65f, 1.0f);
        BlenderDimensionHelper.spawnBloodEruption(serverLevel, portal, 1.0f, random);
        portal.triggerBlenderEruption(50);
        DamageSource ds = BlenderDimensionHelper.getDamageSource((Level)serverLevel);
        entity.hurt(ds, 10000.0f);
        if (!entity.isAlive()) return;
        if (entity instanceof Player && (player = (Player)entity).isCreative()) {
            player.hurt(ds, 19.0f);
        } else {
            entity.kill();
        }
    }

    public static void spawnItemShredEruption(ServerLevel level, PortalEntity portal, ItemStack stack, RandomSource random) {
        Vec3 normal = PortalTeleportEngine.getPortalNormal((int)portal.getOrientation(), (float)portal.getYRot()).normalize();
        double cx = portal.getX();
        double cy = portal.getY();
        double cz = portal.getZ();
        float scale = portal.getScale();
        boolean isWall = portal.getOrientation() == 0;
        Vec3 upGuide = Math.abs(normal.y) < 0.95 ? new Vec3(0.0, 1.0, 0.0) : new Vec3(1.0, 0.0, 0.0);
        Vec3 tangent = normal.cross(upGuide).normalize();
        Vec3 bitangent = normal.cross(tangent).normalize();
        int particleCount = 45 + random.nextInt(25);
        ItemParticleOption itemParticle = new ItemParticleOption(ParticleTypes.ITEM, stack);
        int i = 0;
        while (i < particleCount) {
            double discTheta = random.nextDouble() * Math.PI * 2.0;
            double discR = Math.sqrt(random.nextDouble()) * 0.4 * (double)scale;
            double px = cx + tangent.x * discR * Math.cos(discTheta) + bitangent.x * discR * Math.sin(discTheta) + normal.x * 0.08;
            double py = cy + tangent.y * discR * Math.cos(discTheta) + bitangent.y * discR * Math.sin(discTheta) + normal.y * 0.08;
            double pz = cz + tangent.z * discR * Math.cos(discTheta) + bitangent.z * discR * Math.sin(discTheta) + normal.z * 0.08;
            double theta = random.nextDouble() * Math.PI * 2.0;
            double phi = Math.pow(random.nextDouble(), 0.7) * 1.1;
            double speed = (0.4 + random.nextDouble() * 0.85) * (double)scale;
            double dirNorm = Math.cos(phi);
            double dirRad = Math.sin(phi);
            double vx = (normal.x * dirNorm + (tangent.x * Math.cos(theta) + bitangent.x * Math.sin(theta)) * dirRad) * speed;
            double vy = (normal.y * dirNorm + (tangent.y * Math.cos(theta) + bitangent.y * Math.sin(theta)) * dirRad) * speed;
            double vz = (normal.z * dirNorm + (tangent.z * Math.cos(theta) + bitangent.z * Math.sin(theta)) * dirRad) * speed;
            if (isWall) {
                vy += (0.05 + random.nextDouble() * 0.12) * (double)scale;
            }
            level.sendParticles((ParticleOptions)itemParticle, px, py, pz, 0, vx, vy, vz, 1.0);
            ++i;
        }
    }

    public static void spawnBloodEruption(ServerLevel level, PortalEntity portal, float intensity, RandomSource random) {
        int i;
        Vec3 normal = PortalTeleportEngine.getPortalNormal((int)portal.getOrientation(), (float)portal.getYRot()).normalize();
        double cx = portal.getX();
        double cy = portal.getY();
        double cz = portal.getZ();
        float scale = portal.getScale();
        boolean isWall = portal.getOrientation() == 0;
        Vec3 upGuide = Math.abs(normal.y) < 0.95 ? new Vec3(0.0, 1.0, 0.0) : new Vec3(1.0, 0.0, 0.0);
        Vec3 tangent = normal.cross(upGuide).normalize();
        Vec3 bitangent = normal.cross(tangent).normalize();
        int dropletCount = (int)((float)(55 + random.nextInt(30)) * intensity);
        int vaporCount = (int)((float)(10 + random.nextInt(6)) * intensity);
        for (i = 0; i < dropletCount; ++i) {
            double discTheta = random.nextDouble() * Math.PI * 2.0;
            double discR = Math.sqrt(random.nextDouble()) * 0.45 * (double)scale;
            double px = cx + tangent.x * discR * Math.cos(discTheta) + bitangent.x * discR * Math.sin(discTheta) + normal.x * 0.08;
            double py = cy + tangent.y * discR * Math.cos(discTheta) + bitangent.y * discR * Math.sin(discTheta) + normal.y * 0.08;
            double pz = cz + tangent.z * discR * Math.cos(discTheta) + bitangent.z * discR * Math.sin(discTheta) + normal.z * 0.08;
            double theta = random.nextDouble() * Math.PI * 2.0;
            double phi = Math.pow(random.nextDouble(), 0.7) * 1.15;
            double speed = (0.45 + random.nextDouble() * 1.15) * (double)scale;
            double dirNorm = Math.cos(phi);
            double dirRad = Math.sin(phi);
            double vx = (normal.x * dirNorm + (tangent.x * Math.cos(theta) + bitangent.x * Math.sin(theta)) * dirRad) * speed;
            double vy = (normal.y * dirNorm + (tangent.y * Math.cos(theta) + bitangent.y * Math.sin(theta)) * dirRad) * speed;
            double vz = (normal.z * dirNorm + (tangent.z * Math.cos(theta) + bitangent.z * Math.sin(theta)) * dirRad) * speed;
            if (isWall) {
                vy += (0.06 + random.nextDouble() * 0.12) * (double)scale;
            }
            level.sendParticles((ParticleOptions)((SimpleParticleType)ModParticles.BLOOD_DROPLET.get()), px, py, pz, 0, vx, vy, vz, 1.0);
        }
        i = 0;
        while (true) {
            if (i >= vaporCount) {
                level.sendParticles((ParticleOptions)((SimpleParticleType)ModParticles.BLOOD_SHOCKWAVE.get()), cx + normal.x * 0.06, cy + normal.y * 0.06, cz + normal.z * 0.06, 1, 0.0, 0.0, 0.0, 0.0);
                return;
            }
            double theta = random.nextDouble() * Math.PI * 2.0;
            double phi = random.nextDouble() * 0.85;
            double speed = (0.08 + random.nextDouble() * 0.18) * (double)scale;
            double vx = (normal.x * Math.cos(phi) + (tangent.x * Math.cos(theta) + bitangent.x * Math.sin(theta)) * Math.sin(phi)) * speed;
            double vy = (normal.y * Math.cos(phi) + (tangent.y * Math.cos(theta) + bitangent.y * Math.sin(theta)) * Math.sin(phi)) * speed + 0.02;
            double vz = (normal.z * Math.cos(phi) + (tangent.z * Math.cos(theta) + bitangent.z * Math.sin(theta)) * Math.sin(phi)) * speed;
            level.sendParticles((ParticleOptions)((SimpleParticleType)ModParticles.BLOOD_VAPOR.get()), cx + (random.nextDouble() - 0.5) * 0.4 * (double)scale + normal.x * 0.1, cy + (random.nextDouble() - 0.5) * 0.4 * (double)scale + normal.y * 0.1, cz + (random.nextDouble() - 0.5) * 0.4 * (double)scale + normal.z * 0.1, 0, vx, vy, vz, 1.0);
            ++i;
        }
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.Holder$Reference
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.damagesource.DamageType
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.animal.AbstractFish
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  org.ExtraTortik.ricks_stuff.entity.PortalEntity
 */
package org.ExtraTortik.ricks_stuff.portal;

import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.ExtraTortik.ricks_stuff.entity.PortalEntity;

public class FishDimensionHelper {
    public static final ResourceKey<DamageType> FISH_DAMAGE_TYPE = ResourceKey.create((ResourceKey)Registries.DAMAGE_TYPE, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"fish_dimension"));
    private static final List<EntityType<? extends AbstractFish>> FISH_TYPES = List.of((Object)EntityType.COD, (Object)EntityType.SALMON, (Object)EntityType.PUFFERFISH, (Object)EntityType.TROPICAL_FISH);

    public static boolean isFishDimension(String dimensionStr) {
        if (dimensionStr == null) return false;
        if (dimensionStr.trim().isEmpty()) {
            return false;
        }
        String clean = dimensionStr.trim().toLowerCase(Locale.ROOT).replace(" ", "_").replace("-", "_");
        if (!clean.contains(":")) return clean.equals("fish") || clean.equals("fishes") || clean.equals("fish_dimension") || clean.equals("fishes_dimension") || clean.equals("dimension_of_fish") || clean.equals("dimension_of_fishes") || clean.contains("fish") || clean.contains("\u0440\u044b\u0431\u0430") || clean.contains("\u0440\u044b\u0431\u044b");
        clean = clean.substring(clean.indexOf(":") + 1);
        return clean.equals("fish") || clean.equals("fishes") || clean.equals("fish_dimension") || clean.equals("fishes_dimension") || clean.equals("dimension_of_fish") || clean.equals("dimension_of_fishes") || clean.contains("fish") || clean.contains("\u0440\u044b\u0431\u0430") || clean.contains("\u0440\u044b\u0431\u044b");
    }

    public static DamageSource getDamageSource(Level level, @Nullable Entity attacker) {
        try {
            Holder.Reference holder = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(FISH_DAMAGE_TYPE);
            return new DamageSource((Holder)holder, attacker);
        }
        catch (Exception e) {
            return level.damageSources().generic();
        }
    }

    public static void spawnFishEruption(ServerLevel level, PortalEntity portal, @Nullable LivingEntity attachedEntity) {
        Vec3 velocity;
        double pz;
        double py;
        double px;
        RandomSource random = level.getRandom();
        if (attachedEntity != null) {
            px = attachedEntity.getX();
            py = attachedEntity.getY() + (double)attachedEntity.getBbHeight() * 0.5;
            pz = attachedEntity.getZ();
        } else {
            px = portal.getX();
            py = portal.getY();
            pz = portal.getZ();
        }
        level.sendParticles((ParticleOptions)ParticleTypes.SPLASH, px, py, pz, 15, 0.25, 0.25, 0.25, 0.1);
        level.sendParticles((ParticleOptions)ParticleTypes.BUBBLE, px, py, pz, 10, 0.2, 0.2, 0.2, 0.05);
        level.playSound(null, px, py, pz, SoundEvents.GENERIC_SPLASH, SoundSource.NEUTRAL, 0.6f, 0.8f + random.nextFloat() * 0.4f);
        AABB checkArea = new AABB(px - 10.0, py - 10.0, pz - 10.0, px + 10.0, py + 10.0, pz + 10.0);
        List nearbyFish = level.getEntitiesOfClass(AbstractFish.class, checkArea, Entity::isAlive);
        if (nearbyFish.size() > 50) {
            return;
        }
        EntityType<? extends AbstractFish> type = FISH_TYPES.get(random.nextInt(FISH_TYPES.size()));
        AbstractFish fish = (AbstractFish)type.create((Level)level);
        if (fish == null) {
            return;
        }
        fish.moveTo(px, py, pz, random.nextFloat() * 360.0f, 0.0f);
        double speed = 0.4 + random.nextDouble() * 0.4;
        if (attachedEntity != null) {
            Vec3 look = attachedEntity.getLookAngle();
            double spreadX = (random.nextDouble() - 0.5) * 0.5;
            double spreadZ = (random.nextDouble() - 0.5) * 0.5;
            double vy = 0.2 + random.nextDouble() * 0.3;
            velocity = new Vec3(look.x + spreadX, vy, look.z + spreadZ).normalize().scale(speed);
        } else {
            Vec3 normal = portal.getNormalVector();
            double spreadX = (random.nextDouble() - 0.5) * 0.7;
            double spreadZ = (random.nextDouble() - 0.5) * 0.7;
            double vy = 0.2 + random.nextDouble() * 0.3;
            velocity = new Vec3(normal.x + spreadX, normal.y + vy, normal.z + spreadZ).normalize().scale(speed);
        }
        fish.setDeltaMovement(velocity);
        fish.hasImpulse = true;
        level.addFreshEntity((Entity)fish);
    }
}

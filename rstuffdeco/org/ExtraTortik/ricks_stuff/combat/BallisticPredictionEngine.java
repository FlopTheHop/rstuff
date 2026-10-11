/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.projectile.AbstractArrow
 *  net.minecraft.world.entity.projectile.Fireball
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.entity.projectile.ShulkerBullet
 *  net.minecraft.world.entity.projectile.ThrowableProjectile
 *  net.minecraft.world.entity.projectile.ThrownPotion
 *  net.minecraft.world.entity.projectile.WitherSkull
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.ExtraTortik.ricks_stuff.combat.BallisticPredictionEngine$ThreatLevel
 *  org.ExtraTortik.ricks_stuff.combat.BallisticPredictionEngine$Trajectory
 *  org.ExtraTortik.ricks_stuff.compat.curios.CuriosCompat
 *  org.ExtraTortik.ricks_stuff.mixin.AbstractArrowAccessor
 */
package org.ExtraTortik.ricks_stuff.combat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.ExtraTortik.ricks_stuff.combat.BallisticPredictionEngine;
import org.ExtraTortik.ricks_stuff.compat.curios.CuriosCompat;
import org.ExtraTortik.ricks_stuff.mixin.AbstractArrowAccessor;

@OnlyIn(value=Dist.CLIENT)
public class BallisticPredictionEngine {
    private static List<Trajectory> cachedTrajectories = Collections.emptyList();
    private static boolean hadCriticalThreatLastTick = false;

    public static List<Trajectory> getActiveTrajectories() {
        return cachedTrajectories;
    }

    public static boolean hasCriticalThreat() {
        return hadCriticalThreatLastTick;
    }

    public static void clientTick(Minecraft mc) {
        if (mc.player == null || mc.level == null) {
            cachedTrajectories = Collections.emptyList();
            hadCriticalThreatLastTick = false;
            return;
        }
        boolean hasEyepatch = CuriosCompat.isWearingEyePatch((LivingEntity)mc.player);
        if (!hasEyepatch) {
            cachedTrajectories = Collections.emptyList();
            hadCriticalThreatLastTick = false;
            return;
        }
        LocalPlayer player = mc.player;
        ClientLevel level = mc.level;
        Vec3 playerEye = player.getEyePosition();
        AABB searchBox = player.getBoundingBox().inflate(36.0);
        List projectiles = level.getEntitiesOfClass(Projectile.class, searchBox, arg_0 -> BallisticPredictionEngine.lambda$clientTick$0((Player)player, arg_0));
        if (projectiles.isEmpty()) {
            cachedTrajectories = Collections.emptyList();
            hadCriticalThreatLastTick = false;
            return;
        }
        ArrayList<Trajectory> calculated = new ArrayList<Trajectory>(projectiles.size());
        boolean currentCritical = false;
        Iterator iterator = projectiles.iterator();
        while (true) {
            ThreatLevel threat;
            if (!iterator.hasNext()) {
                cachedTrajectories = calculated;
                hadCriticalThreatLastTick = currentCritical;
                return;
            }
            Projectile projectile = (Projectile)iterator.next();
            Vec3 vel = projectile.getDeltaMovement();
            if (vel.lengthSqr() < 0.04) continue;
            double gravity = BallisticPredictionEngine.getProjectileGravity(projectile);
            double drag = projectile.isInWater() ? 0.6 : BallisticPredictionEngine.getProjectileDrag(projectile);
            Vec3 cur = projectile.position();
            ArrayList<Vec3> points = new ArrayList<Vec3>(42);
            points.add(cur);
            Vec3 impact = null;
            float minDistance = Float.MAX_VALUE;
            for (int step = 0; step < 40; ++step) {
                Vec3 next = cur.add(vel);
                BlockHitResult hit = level.clip(new ClipContext(cur, next, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)projectile));
                if (hit.getType() != HitResult.Type.MISS) {
                    impact = hit.getLocation();
                    points.add(impact);
                    minDistance = Math.min(minDistance, BallisticPredictionEngine.distanceToSegment(cur, impact, playerEye));
                    break;
                }
                points.add(next);
                minDistance = Math.min(minDistance, BallisticPredictionEngine.distanceToSegment(cur, next, playerEye));
                cur = next;
                vel = vel.scale(drag).subtract(0.0, gravity, 0.0);
            }
            if (impact == null && !points.isEmpty()) {
                impact = (Vec3)points.get(points.size() - 1);
            }
            if (minDistance < 2.0f) {
                threat = ThreatLevel.CRITICAL;
                currentCritical = true;
            } else {
                threat = minDistance < 5.0f ? ThreatLevel.WARNING : ThreatLevel.PASS;
            }
            calculated.add(new Trajectory(projectile.getId(), points, impact, threat, minDistance));
        }
    }

    private static double getProjectileGravity(Projectile p) {
        if (p.isNoGravity()) {
            return 0.0;
        }
        if (p instanceof AbstractArrow) {
            return 0.05;
        }
        if (p instanceof ThrownPotion) {
            return 0.05;
        }
        if (p instanceof ThrowableProjectile) {
            return 0.03;
        }
        if (p instanceof Fireball) return 0.0;
        if (p instanceof WitherSkull) return 0.0;
        if (!(p instanceof ShulkerBullet)) return 0.03;
        return 0.0;
    }

    private static double getProjectileDrag(Projectile p) {
        if (p instanceof Fireball) return 1.0;
        if (p instanceof WitherSkull) return 1.0;
        if (!(p instanceof ShulkerBullet)) return 0.99;
        return 1.0;
    }

    private static float distanceToSegment(Vec3 a, Vec3 b, Vec3 p) {
        Vec3 ab = b.subtract(a);
        double lenSq = ab.lengthSqr();
        if (lenSq < 1.0E-6) {
            return (float)a.distanceTo(p);
        }
        double t = Math.max(0.0, Math.min(1.0, p.subtract(a).dot(ab) / lenSq));
        Vec3 projection = a.add(ab.scale(t));
        return (float)projection.distanceTo(p);
    }

    private static /* synthetic */ boolean lambda$clientTick$0(Player player, Projectile p) {
        if (!p.isAlive()) return false;
        if (p.getOwner() == player) {
            return false;
        }
        if (p.onGround()) return false;
        if (p.verticalCollision) return false;
        if (p.horizontalCollision) {
            return false;
        }
        if (p.getDeltaMovement().lengthSqr() < 0.04) {
            return false;
        }
        if (!(p instanceof AbstractArrow)) return true;
        AbstractArrow arrow = (AbstractArrow)p;
        try {
            if (!((AbstractArrowAccessor)arrow).ricksStuff$isInGround()) return true;
            return false;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return true;
    }
}

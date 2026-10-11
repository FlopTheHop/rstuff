/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.client.event.ClientTickEvent$Post
 *  org.ExtraTortik.ricks_stuff.client.renderer.EvilMortyShieldClientManager$ShieldInstance
 *  org.ExtraTortik.ricks_stuff.client.renderer.EvilMortyShieldClientManager$SparkInstance
 */
package org.ExtraTortik.ricks_stuff.client.renderer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.ExtraTortik.ricks_stuff.client.renderer.EvilMortyShieldClientManager;

@OnlyIn(value=Dist.CLIENT)
@EventBusSubscriber(modid="ricks_stuff", value={Dist.CLIENT})
public class EvilMortyShieldClientManager {
    private static final List<ShieldInstance> ACTIVE_SHIELDS = new CopyOnWriteArrayList<ShieldInstance>();
    private static final List<SparkInstance> ACTIVE_SPARKS = new CopyOnWriteArrayList<SparkInstance>();

    public static void addShieldEffect(int entityId, Vec3 impactPos) {
        EvilMortyShieldClientManager.addShieldEffect(entityId, impactPos, null, false);
    }

    public static void addShieldEffect(int entityId, Vec3 impactPos, Vec3 impactDir) {
        EvilMortyShieldClientManager.addShieldEffect(entityId, impactPos, impactDir, false);
    }

    public static void addShieldEffect(int entityId, Vec3 impactPos, Vec3 impactDir, boolean isBreak) {
        Minecraft mc = Minecraft.getInstance();
        Vec3 finalDir = impactDir;
        if (finalDir == null || finalDir.lengthSqr() < 1.0E-4) {
            Vec3 center;
            Vec3 diff;
            Entity entity;
            finalDir = new Vec3(0.0, 0.0, 1.0);
            if (mc.level != null && (entity = mc.level.getEntity(entityId)) != null && (diff = impactPos.subtract(center = entity.position().add(0.0, (double)entity.getBbHeight() * 0.5, 0.0))).lengthSqr() > 1.0E-4) {
                finalDir = diff.normalize();
            }
        } else {
            finalDir = finalDir.normalize();
        }
        ACTIVE_SHIELDS.add(new ShieldInstance(entityId, impactPos, finalDir, isBreak));
        Vec3 normal = finalDir;
        normal = normal.lengthSqr() < 1.0E-4 ? new Vec3(0.0, 1.0, 0.0) : normal.normalize();
        Vec3 ref = Math.abs(normal.y) < 0.95 ? new Vec3(0.0, 1.0, 0.0) : new Vec3(1.0, 0.0, 0.0);
        Vec3 t1 = normal.cross(ref).normalize();
        Vec3 t2 = normal.cross(t1).normalize();
        RandomSource random = mc.level != null ? mc.level.getRandom() : RandomSource.create();
        int sparkCount = isBreak ? 50 + random.nextInt(21) : 25 + random.nextInt(11);
        int i = 0;
        while (i < sparkCount) {
            double theta = random.nextDouble() * (Math.PI * 2);
            double cos = Math.cos(theta);
            double sin = Math.sin(theta);
            double forwardSpeed = isBreak ? 0.4 + random.nextDouble() * 0.5 : 0.25 + random.nextDouble() * 0.35;
            double radialSpeed = isBreak ? 0.25 + random.nextDouble() * 0.45 : 0.15 + random.nextDouble() * 0.3;
            double lift = isBreak ? 0.1 + random.nextDouble() * 0.25 : 0.05 + random.nextDouble() * 0.15;
            Vec3 velocity = normal.scale(forwardSpeed).add(t1.scale(cos).add(t2.scale(sin)).scale(radialSpeed)).add(0.0, lift, 0.0);
            int maxAge = (isBreak ? 20 : 16) + random.nextInt(isBreak ? 11 : 9);
            ACTIVE_SPARKS.add(new SparkInstance(impactPos.x, impactPos.y, impactPos.z, velocity.x, velocity.y, velocity.z, maxAge, isBreak));
            ++i;
        }
    }

    public static List<SparkInstance> getActiveSparks() {
        return ACTIVE_SPARKS;
    }

    public static List<ShieldInstance> getActiveShields() {
        return ACTIVE_SHIELDS;
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            ACTIVE_SHIELDS.clear();
            ACTIVE_SPARKS.clear();
            return;
        }
        if (mc.isPaused()) {
            return;
        }
        ACTIVE_SHIELDS.removeIf(ShieldInstance::tickAndCheckExpired);
        ACTIVE_SPARKS.removeIf(spark -> spark.tick((Level)mc.level));
    }
}

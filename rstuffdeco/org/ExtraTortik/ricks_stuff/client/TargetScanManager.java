/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.projectile.ProjectileUtil
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.ExtraTortik.ricks_stuff.combat.NeuralScanEngine
 *  org.ExtraTortik.ricks_stuff.compat.curios.CuriosCompat
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SScanEntityPacket
 */
package org.ExtraTortik.ricks_stuff.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.ExtraTortik.ricks_stuff.combat.NeuralScanEngine;
import org.ExtraTortik.ricks_stuff.compat.curios.CuriosCompat;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.C2SScanEntityPacket;

@OnlyIn(value=Dist.CLIENT)
public class TargetScanManager {
    private static final TargetScanManager INSTANCE = new TargetScanManager();
    private int activeTargetId = -1;
    private int scanTimer = 0;
    private int elapsedScanTicks = 0;
    private int cooldownTimer = 0;
    private String predictionText = "";
    private float scanAnimationProgress = 0.0f;

    public static TargetScanManager getInstance() {
        return INSTANCE;
    }

    public boolean isScanning() {
        return this.scanTimer > 0 && this.activeTargetId != -1;
    }

    public boolean isCoolingDown() {
        return this.cooldownTimer > 0 && this.scanTimer == 0;
    }

    public boolean isAnalysisComplete() {
        return this.isScanning() && this.elapsedScanTicks >= 10;
    }

    public int getActiveTargetId() {
        return this.activeTargetId;
    }

    public String getPredictionText() {
        return this.predictionText;
    }

    public float getScanAnimationProgress() {
        return this.scanAnimationProgress;
    }

    public int getCooldownTimer() {
        return this.cooldownTimer;
    }

    public int getScanTimer() {
        return this.scanTimer;
    }

    public void onScanResult(int entityId, String text, int durationTicks, boolean success) {
        if (entityId != this.activeTargetId) {
            return;
        }
        if (success) {
            this.predictionText = text != null ? text : "";
            if (this.scanTimer > 0) return;
            this.scanTimer = durationTicks;
            this.elapsedScanTicks = 0;
            this.scanAnimationProgress = 0.0f;
        } else {
            this.clearGlowing(Minecraft.getInstance());
            this.activeTargetId = -1;
            this.scanTimer = 0;
            this.elapsedScanTicks = 0;
            this.cooldownTimer = 0;
        }
    }

    public LivingEntity findTargetEntity(Player player, double maxDistance) {
        AABB searchBox;
        Vec3 viewVec;
        Vec3 reachVec;
        Vec3 eyePos = player.getEyePosition(1.0f);
        EntityHitResult hit = ProjectileUtil.getEntityHitResult((Entity)player, (Vec3)eyePos, (Vec3)(reachVec = eyePos.add((viewVec = player.getViewVector(1.0f)).scale(maxDistance))), (AABB)(searchBox = player.getBoundingBox().expandTowards(viewVec.scale(maxDistance)).inflate(2.0)), e -> e instanceof LivingEntity && e.isAlive() && e != player, (double)(maxDistance * maxDistance));
        if (hit == null) return null;
        Entity entity = hit.getEntity();
        if (!(entity instanceof LivingEntity)) return null;
        LivingEntity living = (LivingEntity)entity;
        return living;
    }

    public boolean tryTriggerScan(Player player) {
        if (!CuriosCompat.isWearingEyePatch((LivingEntity)player)) {
            return false;
        }
        LivingEntity living = this.findTargetEntity(player, 24.0);
        if (living == null) {
            return false;
        }
        if (this.isScanning()) {
            if (this.activeTargetId == living.getId()) {
                return false;
            }
            this.clearGlowing(Minecraft.getInstance());
        }
        player.playSound((SoundEvent)ModSounds.UI_DIAL.get(), 1.0f, 1.2f);
        this.activeTargetId = living.getId();
        this.scanTimer = 120;
        this.elapsedScanTicks = 0;
        this.predictionText = NeuralScanEngine.predictIntent((LivingEntity)living);
        this.scanAnimationProgress = 0.0f;
        this.cooldownTimer = 0;
        ModMessages.sendToServer((CustomPacketPayload)new C2SScanEntityPacket(living.getId(), true));
        return true;
    }

    public void tick(Minecraft mc) {
        if (mc.player == null || mc.level == null) {
            if (this.scanTimer <= 0) return;
            this.scanTimer = 0;
            this.activeTargetId = -1;
            return;
        }
        if (this.scanTimer > 0) {
            LivingEntity living;
            --this.scanTimer;
            ++this.elapsedScanTicks;
            this.scanAnimationProgress = Math.min(1.0f, this.scanAnimationProgress + 0.1f);
            if (!CuriosCompat.isWearingEyePatch((LivingEntity)mc.player)) {
                this.clearGlowing(mc);
                this.activeTargetId = -1;
                this.scanTimer = 0;
                this.cooldownTimer = 0;
                return;
            }
            Entity target = mc.level.getEntity(this.activeTargetId);
            if (!(target instanceof LivingEntity) || !(living = (LivingEntity)target).isAlive() || mc.player.distanceToSqr((Entity)living) > 1024.0) {
                this.clearGlowing(mc);
                this.activeTargetId = -1;
                this.scanTimer = 0;
                this.elapsedScanTicks = 0;
                this.cooldownTimer = 0;
                return;
            }
            String dynamicIntent = NeuralScanEngine.predictIntent((LivingEntity)living);
            if (!"OFFLINE".equals(dynamicIntent) && !"UNKNOWN".equals(dynamicIntent)) {
                this.predictionText = dynamicIntent;
            }
            if (this.scanTimer != 0) return;
            this.clearGlowing(mc);
            this.cooldownTimer = 0;
            this.activeTargetId = -1;
            this.elapsedScanTicks = 0;
        } else {
            if (this.cooldownTimer <= 0) return;
            --this.cooldownTimer;
        }
    }

    public void reset() {
        this.activeTargetId = -1;
        this.scanTimer = 0;
        this.elapsedScanTicks = 0;
        this.cooldownTimer = 0;
        this.predictionText = "";
        this.scanAnimationProgress = 0.0f;
    }

    private void clearGlowing(Minecraft mc) {
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.animal.IronGolem
 *  net.minecraft.world.entity.boss.enderdragon.EnderDragon
 *  net.minecraft.world.entity.boss.wither.WitherBoss
 *  net.minecraft.world.entity.monster.AbstractSkeleton
 *  net.minecraft.world.entity.monster.Blaze
 *  net.minecraft.world.entity.monster.Creeper
 *  net.minecraft.world.entity.monster.EnderMan
 *  net.minecraft.world.entity.monster.Evoker
 *  net.minecraft.world.entity.monster.Ghast
 *  net.minecraft.world.entity.monster.Pillager
 *  net.minecraft.world.entity.monster.Spider
 *  net.minecraft.world.entity.monster.Witch
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.phys.AABB
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent$LoggingOut
 *  net.neoforged.neoforge.client.event.MovementInputUpdateEvent
 *  org.ExtraTortik.ricks_stuff.compat.curios.CuriosCompat
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.BioNeuralReceiverItem
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SPuppetAbilityPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SPuppetConnectPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SPuppetControlPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SPuppetDisconnectPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SPuppetSelfDestructPacket
 */
package org.ExtraTortik.ricks_stuff.client;

import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import org.ExtraTortik.ricks_stuff.compat.curios.CuriosCompat;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.BioNeuralReceiverItem;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.C2SPuppetAbilityPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SPuppetConnectPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SPuppetControlPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SPuppetDisconnectPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SPuppetSelfDestructPacket;

@OnlyIn(value=Dist.CLIENT)
@EventBusSubscriber(modid="ricks_stuff", value={Dist.CLIENT})
public class BioNeuralPossessionManager {
    private static boolean possessing = false;
    private static int targetMobId = -1;
    private static UUID targetMobUUID = null;
    private static String targetMobName = "";
    private static float targetHealth = 20.0f;
    private static float targetMaxHealth = 20.0f;
    private static int signalStrength = 100;
    private static int attackCooldown = 0;
    private static int entityNotFoundTicks = 0;

    public static boolean isPossessing() {
        return possessing;
    }

    public static int getTargetMobId() {
        return targetMobId;
    }

    public static String getTargetMobName() {
        return targetMobName;
    }

    public static float getTargetHealth() {
        return targetHealth;
    }

    public static float getTargetMaxHealth() {
        return targetMaxHealth;
    }

    public static int getSignalStrength() {
        return signalStrength;
    }

    public static void tryStartPossession(Player player, ItemStack receiverStack) {
        Mob mob;
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        int id = BioNeuralReceiverItem.getLinkedMobId((ItemStack)receiverStack);
        UUID uuid = BioNeuralReceiverItem.getLinkedMobUUID((ItemStack)receiverStack);
        Entity target = mc.level.getEntity(id);
        if (target == null && uuid != null) {
            AABB searchBox = player.getBoundingBox().inflate(64.0);
            for (Mob m : mc.level.getEntitiesOfClass(Mob.class, searchBox)) {
                if (!m.getUUID().equals(uuid)) continue;
                target = m;
                break;
            }
        }
        if (!(target instanceof Mob) || !(mob = (Mob)target).isAlive()) {
            player.playSound((SoundEvent)ModSounds.UI_DIAL.get(), 0.4f, 0.5f);
            return;
        }
        if (player.distanceToSqr((Entity)mob) > 4096.0) {
            player.playSound((SoundEvent)ModSounds.UI_DIAL.get(), 0.4f, 0.5f);
            return;
        }
        possessing = true;
        targetMobId = mob.getId();
        targetMobUUID = mob.getUUID();
        targetMobName = mob.getDisplayName().getString();
        targetHealth = mob.getHealth();
        targetMaxHealth = mob.getMaxHealth();
        ModMessages.sendToServer((CustomPacketPayload)new C2SPuppetConnectPacket(mob.getId()));
        float startYaw = mob.getYRot();
        if (mob instanceof EnderDragon) {
            startYaw -= 180.0f;
        }
        player.setYRot(startYaw);
        player.setXRot(mob.getXRot());
        mc.setCameraEntity((Entity)mob);
        player.playSound((SoundEvent)ModSounds.UI_DIAL.get(), 1.0f, 1.2f);
    }

    public static void tryStartPossessionDirect(Mob mob) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (mc.level == null) return;
        if (mob == null) return;
        if (!mob.isAlive()) {
            return;
        }
        boolean hasEyepatch = CuriosCompat.isWearingEyePatch((LivingEntity)mc.player);
        if (!hasEyepatch) {
            return;
        }
        if (mc.player.distanceToSqr((Entity)mob) > 4096.0) {
            mc.player.playSound((SoundEvent)ModSounds.UI_DIAL.get(), 0.4f, 0.5f);
            return;
        }
        possessing = true;
        targetMobId = mob.getId();
        targetMobUUID = mob.getUUID();
        targetMobName = mob.getDisplayName().getString();
        targetHealth = mob.getHealth();
        targetMaxHealth = mob.getMaxHealth();
        float startYaw = mob.getYRot();
        if (mob instanceof EnderDragon) {
            startYaw -= 180.0f;
        }
        mc.player.setYRot(startYaw);
        mc.player.setXRot(mob.getXRot());
        ModMessages.sendToServer((CustomPacketPayload)new C2SPuppetConnectPacket(mob.getId()));
        mc.setCameraEntity((Entity)mob);
        mc.player.playSound((SoundEvent)ModSounds.UI_DIAL.get(), 1.0f, 1.2f);
    }

    public static void tryStartPossession(int mobId, UUID uuid, String name, float health, float maxHealth, Mob mob) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (mc.level == null) {
            return;
        }
        boolean hasEyepatch = CuriosCompat.isWearingEyePatch((LivingEntity)mc.player);
        if (!hasEyepatch) {
            return;
        }
        possessing = true;
        targetMobId = mobId;
        targetMobUUID = uuid;
        targetMobName = name != null ? name : "Puppet";
        targetHealth = health;
        targetMaxHealth = maxHealth;
        entityNotFoundTicks = 0;
        ModMessages.sendToServer((CustomPacketPayload)new C2SPuppetConnectPacket(mobId));
        if (mob != null) {
            float startYaw = mob.getYRot();
            if (mob instanceof EnderDragon) {
                startYaw -= 180.0f;
            }
            mc.player.setYRot(startYaw);
            mc.player.setXRot(mob.getXRot());
        }
        if (mob != null) {
            mc.setCameraEntity((Entity)mob);
        } else {
            Entity target = mc.level.getEntity(mobId);
            if (target != null) {
                mc.setCameraEntity(target);
            }
        }
        mc.player.playSound((SoundEvent)ModSounds.UI_DIAL.get(), 1.0f, 1.2f);
    }

    public static void stopPossession() {
        if (!possessing) {
            return;
        }
        int oldId = targetMobId;
        possessing = false;
        targetMobId = -1;
        targetMobUUID = null;
        entityNotFoundTicks = 0;
        if (oldId != -1) {
            ModMessages.sendToServer((CustomPacketPayload)new C2SPuppetDisconnectPacket(oldId));
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        mc.setCameraEntity((Entity)mc.player);
        mc.player.playSound((SoundEvent)ModSounds.UI_DIAL.get(), 0.8f, 0.8f);
    }

    public static void stopPossessionClientOnly() {
        if (!possessing) {
            return;
        }
        possessing = false;
        targetMobId = -1;
        targetMobUUID = null;
        entityNotFoundTicks = 0;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        mc.setCameraEntity((Entity)mc.player);
        mc.player.playSound((SoundEvent)ModSounds.UI_DIAL.get(), 0.8f, 0.8f);
    }

    public static String getCurrentAbilityName() {
        if (!possessing) return null;
        if (targetMobId == -1) {
            return null;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return null;
        }
        Entity target = mc.level.getEntity(targetMobId);
        if (!(target instanceof Mob)) return null;
        Mob mob = (Mob)target;
        if (!mob.isAlive()) {
            return null;
        }
        if (mob instanceof Creeper) {
            return "IGNITE";
        }
        if (mob instanceof AbstractSkeleton) {
            AbstractSkeleton skeleton = (AbstractSkeleton)mob;
            return skeleton.isUsingItem() ? "RELEASE SHOT" : "DRAW BOW";
        }
        if (mob instanceof Pillager) {
            Pillager pillager = (Pillager)mob;
            return pillager.isUsingItem() ? "FIRE CROSSBOW" : "LOAD CROSSBOW";
        }
        if (mob instanceof Witch) {
            return "POTION";
        }
        if (mob instanceof EnderMan) {
            return "TELEPORT";
        }
        if (mob instanceof Blaze) {
            return "FIREBALL";
        }
        if (mob instanceof Ghast) {
            return "GHAST BOMB";
        }
        if (mob instanceof IronGolem) {
            return "GROUND SLAM";
        }
        if (mob instanceof Spider) {
            return "LEAP";
        }
        if (mob instanceof Evoker) {
            return "FANGS";
        }
        if (mob instanceof EnderDragon) {
            return "DRAGON BREATH";
        }
        if (!(mob instanceof WitherBoss)) return null;
        return "WITHER SKULL";
    }

    public static void triggerAbility() {
        if (!possessing) return;
        if (targetMobId == -1) return;
        if (BioNeuralPossessionManager.getCurrentAbilityName() == null) return;
        Minecraft mc = Minecraft.getInstance();
        float yaw = mc.player != null ? mc.player.getYRot() : 0.0f;
        float pitch = mc.player != null ? mc.player.getXRot() : 0.0f;
        ModMessages.sendToServer((CustomPacketPayload)new C2SPuppetAbilityPacket(targetMobId, yaw, pitch));
    }

    public static void triggerSelfDestruct() {
        if (!possessing) return;
        if (targetMobId == -1) return;
        ModMessages.sendToServer((CustomPacketPayload)new C2SPuppetSelfDestructPacket(targetMobId));
        BioNeuralPossessionManager.stopPossession();
    }

    public static void clientTick(Minecraft mc) {
        boolean attack;
        Mob mob;
        if (!possessing) {
            return;
        }
        if (mc.player == null || !mc.player.isAlive() || mc.level == null) {
            BioNeuralPossessionManager.stopPossession();
            return;
        }
        boolean hasEyepatch = CuriosCompat.isWearingEyePatch((LivingEntity)mc.player);
        if (!hasEyepatch) {
            BioNeuralPossessionManager.stopPossession();
            return;
        }
        Entity target = mc.level.getEntity(targetMobId);
        if (target == null && targetMobUUID != null) {
            for (Entity e : mc.level.entitiesForRendering()) {
                if (!targetMobUUID.equals(e.getUUID())) continue;
                target = e;
                targetMobId = e.getId();
                break;
            }
        }
        if (target != null && mc.getCameraEntity() != target) {
            mc.setCameraEntity(target);
        }
        if (!(target instanceof Mob) || !(mob = (Mob)target).isAlive()) {
            if (++entityNotFoundTicks <= 10) return;
            BioNeuralPossessionManager.stopPossession();
            return;
        }
        entityNotFoundTicks = 0;
        double distSq = mc.player.distanceToSqr((Entity)mob);
        if (distSq > 4096.0) {
            BioNeuralPossessionManager.stopPossession();
            return;
        }
        targetHealth = mob.getHealth();
        targetMaxHealth = mob.getMaxHealth();
        double dist = Math.sqrt(distSq);
        signalStrength = Math.max(0, Math.min(100, (int)((1.0 - dist / 64.0) * 100.0)));
        if (attackCooldown > 0) {
            --attackCooldown;
        }
        float forward = 0.0f;
        if (mc.options.keyUp.isDown()) {
            forward += 1.0f;
        }
        if (mc.options.keyDown.isDown()) {
            forward -= 1.0f;
        }
        float strafe = 0.0f;
        if (mc.options.keyLeft.isDown()) {
            strafe += 1.0f;
        }
        if (mc.options.keyRight.isDown()) {
            strafe -= 1.0f;
        }
        boolean jump = mc.options.keyJump.isDown();
        boolean sprint = mc.options.keySprint.isDown();
        boolean sneak = mc.options.keyShift.isDown();
        boolean bl = attack = mc.options.keyAttack.isDown() && attackCooldown == 0;
        if (attack) {
            attackCooldown = 10;
        }
        float yaw = mc.player.getYRot();
        float pitch = mc.player.getXRot();
        float targetYaw = yaw;
        if (mob instanceof EnderDragon) {
            targetYaw += 180.0f;
        }
        mob.setYRot(targetYaw);
        mob.setXRot(pitch);
        mob.setYHeadRot(targetYaw);
        mob.yBodyRot = targetYaw;
        mob.yRotO = targetYaw;
        mob.xRotO = pitch;
        mob.yHeadRotO = targetYaw;
        mob.yBodyRotO = targetYaw;
        ModMessages.sendToServer((CustomPacketPayload)new C2SPuppetControlPacket(targetMobId, forward, strafe, jump, sprint, sneak, yaw, pitch, attack));
    }

    @SubscribeEvent
    public static void onMovementInputUpdate(MovementInputUpdateEvent event) {
        if (!possessing) return;
        event.getInput().forwardImpulse = 0.0f;
        event.getInput().leftImpulse = 0.0f;
        event.getInput().jumping = false;
        event.getInput().shiftKeyDown = false;
    }

    @SubscribeEvent
    public static void onClientLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        BioNeuralPossessionManager.stopPossession();
    }
}

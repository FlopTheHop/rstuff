/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.Pose
 *  net.minecraft.world.entity.ai.memory.MemoryModuleType
 *  net.minecraft.world.entity.ambient.AmbientCreature
 *  net.minecraft.world.entity.animal.Animal
 *  net.minecraft.world.entity.animal.Cat
 *  net.minecraft.world.entity.animal.Ocelot
 *  net.minecraft.world.entity.boss.enderdragon.EnderDragon
 *  net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase
 *  net.minecraft.world.entity.boss.wither.WitherBoss
 *  net.minecraft.world.entity.monster.AbstractSkeleton
 *  net.minecraft.world.entity.monster.Blaze
 *  net.minecraft.world.entity.monster.Creeper
 *  net.minecraft.world.entity.monster.EnderMan
 *  net.minecraft.world.entity.monster.Enemy
 *  net.minecraft.world.entity.monster.Ghast
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.monster.Pillager
 *  net.minecraft.world.entity.monster.Spider
 *  net.minecraft.world.entity.monster.Witch
 *  net.minecraft.world.entity.monster.Zombie
 *  net.minecraft.world.entity.monster.warden.Warden
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.AxeItem
 *  net.minecraft.world.item.BowItem
 *  net.minecraft.world.item.CrossbowItem
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.PotionItem
 *  net.minecraft.world.item.ShieldItem
 *  net.minecraft.world.item.SwordItem
 *  net.minecraft.world.item.UseAnim
 */
package org.ExtraTortik.ricks_stuff.combat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.UseAnim;

public class NeuralScanEngine {
    public static String predictIntent(LivingEntity target) {
        if (target == null) return "OFFLINE";
        if (!target.isAlive()) {
            return "OFFLINE";
        }
        if (target instanceof Player) {
            Player player = (Player)target;
            ItemStack main = player.getMainHandItem();
            ItemStack off = player.getOffhandItem();
            boolean usingItem = player.isUsingItem();
            ItemStack useStack = player.getUseItem();
            if (usingItem && !useStack.isEmpty()) {
                if (useStack.getItem() instanceof BowItem) {
                    return "AIMING BOW";
                }
                if (useStack.getItem() instanceof CrossbowItem) {
                    return "CHARGING CROSSBOW";
                }
                if (useStack.getItem() instanceof ShieldItem) {
                    return "BLOCKING";
                }
                if (useStack.getItem() instanceof PotionItem) return "CONSUMING POTION";
                if (useStack.getUseAnimation() == UseAnim.DRINK) {
                    return "CONSUMING POTION";
                }
                if (useStack.getUseAnimation() == UseAnim.EAT) {
                    return "EATING";
                }
                if (useStack.is(Items.SPYGLASS)) {
                    return "SCOUTING";
                }
            }
            if (CrossbowItem.isCharged((ItemStack)main)) return "AIMING CROSSBOW";
            if (CrossbowItem.isCharged((ItemStack)off)) {
                return "AIMING CROSSBOW";
            }
            if (player.isFallFlying()) {
                return "GLIDING";
            }
            if (player.isSleeping()) {
                return "DORMANT";
            }
            if (player.getPose() == Pose.SWIMMING && player.isInWater()) {
                return "SWIMMING";
            }
            if (player.isSprinting()) {
                if (main.getItem() instanceof SwordItem) return "SPRINT ATTACK";
                if (!(main.getItem() instanceof AxeItem)) return "CHARGING";
                return "SPRINT ATTACK";
            }
            if (player.isShiftKeyDown()) {
                return "SNEAKING";
            }
            if (main.getItem() instanceof SwordItem) return "COMBAT STANCE";
            if (off.getItem() instanceof SwordItem) {
                return "COMBAT STANCE";
            }
            if (main.getItem() instanceof AxeItem) {
                return "AGGRESSIVE (AXE)";
            }
            if (main.getItem() instanceof BowItem) return "RANGED THREAT";
            if (off.getItem() instanceof BowItem) {
                return "RANGED THREAT";
            }
            if (main.getItem() instanceof CrossbowItem) return "RANGED THREAT";
            if (off.getItem() instanceof CrossbowItem) {
                return "RANGED THREAT";
            }
            if (main.getItem() instanceof ShieldItem) return "DEFENSIVE";
            if (off.getItem() instanceof ShieldItem) {
                return "DEFENSIVE";
            }
            if (!(player.getHealth() <= 6.0f)) return "NEUTRAL PLAYER";
            return "RETREAT / CRITICAL";
        }
        if (target instanceof Creeper) {
            Creeper creeper = (Creeper)target;
            int fuse = creeper.getSwellDir();
            if (fuse > 0) return "DETONATING";
            if (creeper.isIgnited()) {
                return "DETONATING";
            }
            if (creeper.getTarget() == null) return "STALKING";
            return "PURSUING (PRIMING)";
        }
        if (target instanceof Zombie) {
            Zombie zombie = (Zombie)target;
            if (zombie.getTarget() == null) return "WANDERING";
            return zombie.isAggressive() ? "ENGAGING MELEE" : "PURSUING";
        }
        if (target instanceof AbstractSkeleton) {
            AbstractSkeleton skeleton = (AbstractSkeleton)target;
            if (skeleton.isUsingItem() && skeleton.getUseItem().getItem() instanceof BowItem) {
                return "SNIPING";
            }
            if (skeleton.getTarget() == null) return "PATROLLING";
            return "DRAWING BOW";
        }
        if (target instanceof Pillager) {
            Pillager pillager = (Pillager)target;
            if (!pillager.isChargingCrossbow()) return pillager.getTarget() != null ? "AIMING" : "SCOUTING";
            return "LOADING CROSSBOW";
        }
        if (target instanceof Warden) {
            Warden warden = (Warden)target;
            if (warden.getBrain().hasMemoryValue(MemoryModuleType.SONIC_BOOM_COOLDOWN)) {
                return "ROARING / CHARGING";
            }
            if (warden.getTarget() == null) return "SNIFFING / TRACKING";
            return "SONIC AGGRESSION";
        }
        if (target instanceof EnderMan) {
            EnderMan enderman = (EnderMan)target;
            if (enderman.isCreepy()) return "ENRAGED / TELEPORTING";
            if (enderman.getTarget() == null) return "OBSERVING";
            return "ENRAGED / TELEPORTING";
        }
        if (target instanceof Witch) {
            Witch witch = (Witch)target;
            if (!witch.isDrinkingPotion()) return witch.getTarget() != null ? "BREWING HEX" : "IDLE";
            return "BUFFING / DRINKING";
        }
        if (target instanceof Blaze) {
            Blaze blaze = (Blaze)target;
            return blaze.isOnFire() ? "CHARGING FIREBALL" : "HOVERING";
        }
        if (target instanceof Ghast) {
            Ghast ghast = (Ghast)target;
            return ghast.isCharging() ? "PREPARING FIREBALL" : "FLOATING";
        }
        if (target instanceof WitherBoss) {
            WitherBoss wither = (WitherBoss)target;
            if (wither.getInvulnerableTicks() <= 0) return "BOMBARDING";
            return "SHIELDED (SPAWNING)";
        }
        if (target instanceof EnderDragon) {
            EnderDragon dragon = (EnderDragon)target;
            EnderDragonPhase phase = dragon.getPhaseManager().getCurrentPhase().getPhase();
            if (phase == EnderDragonPhase.STRAFE_PLAYER) return "DIVE BOMBING";
            if (phase != EnderDragonPhase.CHARGING_PLAYER) return "CIRCLING PERCH";
            return "DIVE BOMBING";
        }
        if (target instanceof Spider) {
            Spider spider = (Spider)target;
            return spider.getTarget() != null ? "LEAPING ATTACK" : "AMBUSHING";
        }
        if (target instanceof Monster || target instanceof Enemy) {
            if (!(target instanceof Mob)) return "HOSTILE";
            Mob mob = (Mob)target;
            if (mob.getTarget() == null) return "HOSTILE";
            return "TARGET ACQUIRED";
        }
        if (!(target instanceof Animal) && !(target instanceof AmbientCreature)) {
            if (!(target instanceof Mob)) return "UNKNOWN";
            Mob mob = (Mob)target;
            if (mob.isAggressive()) return "UNKNOWN";
            if (mob.getTarget() != null) return "UNKNOWN";
        }
        if (target instanceof Cat) return "PASSIVE / AVOIDING";
        if (!(target instanceof Ocelot)) return "PASSIVE";
        return "PASSIVE / AVOIDING";
    }
}

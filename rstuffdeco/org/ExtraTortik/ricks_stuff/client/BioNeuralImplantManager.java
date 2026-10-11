/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.boss.EnderDragonPart
 *  net.minecraft.world.entity.boss.enderdragon.EnderDragon
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.ExtraTortik.ricks_stuff.client.BioNeuralImplantManager$State
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.BioNeuralReceiverItem
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SImplantChipPacket
 */
package org.ExtraTortik.ricks_stuff.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.ExtraTortik.ricks_stuff.client.BioNeuralImplantManager;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.BioNeuralReceiverItem;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.C2SImplantChipPacket;

@OnlyIn(value=Dist.CLIENT)
public class BioNeuralImplantManager {
    public static final float TOTAL_TICKS = 30.0f;
    public static final double MAX_DISTANCE = 3.8;
    private static State currentState = State.IDLE;
    private static int targetMobId = -1;
    private static float progress = 0.0f;
    private static float failAlpha = 0.0f;
    private static float lastPlayerHealth = -1.0f;

    public static State getState() {
        return currentState;
    }

    public static float getProgress() {
        return progress;
    }

    public static float getFailAlpha() {
        return failAlpha;
    }

    public static boolean isImplanting() {
        return currentState == State.FILLING;
    }

    public static void startImplanting(Mob mob) {
        if (mob == null) return;
        if (!mob.isAlive()) {
            return;
        }
        if (currentState == State.FILLING && targetMobId == mob.getId()) {
            return;
        }
        if (currentState == State.FAILED) {
            failAlpha = 0.0f;
        }
        currentState = State.FILLING;
        targetMobId = mob.getId();
        progress = 0.0f;
        LocalPlayer player = Minecraft.getInstance().player;
        lastPlayerHealth = player != null ? player.getHealth() : 20.0f;
        if (player == null) return;
        player.playSound((SoundEvent)ModSounds.UI_DIAL.get(), 0.3f, 1.8f);
    }

    public static void triggerFailure() {
        if (currentState != State.FILLING) return;
        currentState = State.FAILED;
        failAlpha = 1.0f;
        targetMobId = -1;
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        player.playSound((SoundEvent)ModSounds.UI_DIAL.get(), 0.5f, 0.6f);
    }

    public static void clientTick(Minecraft mc) {
        if (currentState == State.FILLING) {
            double allowedDist;
            double dist;
            Mob mob;
            LocalPlayer player = mc.player;
            if (player == null || mc.level == null) {
                BioNeuralImplantManager.triggerFailure();
                return;
            }
            boolean hasChip = BioNeuralImplantManager.isHoldingUnlinkedChip((Player)player);
            if (!hasChip) {
                BioNeuralImplantManager.triggerFailure();
                return;
            }
            Entity entity = mc.level.getEntity(targetMobId);
            if (!(entity instanceof Mob) || !(mob = (Mob)entity).isAlive()) {
                BioNeuralImplantManager.triggerFailure();
                return;
            }
            if (mob instanceof EnderDragon) {
                EnderDragon dragon = (EnderDragon)mob;
                dist = player.distanceTo((Entity)dragon);
                for (EnderDragonPart part : dragon.getSubEntities()) {
                    dist = Math.min(dist, (double)player.distanceTo((Entity)part));
                }
            } else {
                dist = player.distanceTo((Entity)mob);
            }
            double d = allowedDist = mob instanceof EnderDragon ? 14.0 : 3.8;
            if (dist > allowedDist) {
                BioNeuralImplantManager.triggerFailure();
                return;
            }
            if (player.hurtTime > 0 || player.getHealth() < lastPlayerHealth) {
                BioNeuralImplantManager.triggerFailure();
                return;
            }
            lastPlayerHealth = player.getHealth();
            if (!((progress += 0.033333335f) >= 1.0f)) return;
            progress = 1.0f;
            currentState = State.SUCCESS;
            ModMessages.sendToServer((CustomPacketPayload)new C2SImplantChipPacket(targetMobId));
            targetMobId = -1;
            progress = 0.0f;
            currentState = State.IDLE;
        } else {
            if (currentState != State.FAILED) return;
            if (!((failAlpha -= 0.04f) <= 0.0f)) return;
            failAlpha = 0.0f;
            currentState = State.IDLE;
        }
    }

    public static boolean isHoldingUnlinkedChip(Player player) {
        ItemStack main = player.getMainHandItem();
        if (!(main.getItem() instanceof BioNeuralReceiverItem) || BioNeuralReceiverItem.hasLinkedMob((ItemStack)main)) ItemStack off;
        return (off = player.getOffhandItem()).getItem() instanceof BioNeuralReceiverItem && !BioNeuralReceiverItem.hasLinkedMob((ItemStack)off);
        return true;
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.math.Axis
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.Mth
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.HumanoidArm
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemDisplayContext
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.ClientTickEvent$Post
 *  org.ExtraTortik.ricks_stuff.client.PortalGunRecoilManager$HandRecoilState
 *  org.ExtraTortik.ricks_stuff.client.PortalGunRecoilManager$RecoilState
 */
package org.ExtraTortik.ricks_stuff.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.ExtraTortik.ricks_stuff.client.PortalGunRecoilManager;

@OnlyIn(value=Dist.CLIENT)
@EventBusSubscriber(modid="ricks_stuff", bus=EventBusSubscriber.Bus.GAME, value={Dist.CLIENT})
public class PortalGunRecoilManager {
    public static final float TOTAL_DURATION_MS = 340.0f;
    public static final float PEAK_MS = 65.0f;
    @Deprecated
    public static final float TOTAL_DURATION_TICKS = 7.5f;
    @Deprecated
    public static final float PEAK_TICKS = 1.5f;
    private static int clientTickCount = 0;
    private static final Map<Integer, HandRecoilState> RECOIL_MAP = new ConcurrentHashMap<Integer, HandRecoilState>();
    private static final HandRecoilState LOCAL_PLAYER_FALLBACK = new HandRecoilState();
    private static final ThreadLocal<LivingEntity> CURRENT_RENDERING_ENTITY = new ThreadLocal();
    private static final ThreadLocal<Integer> CURRENT_RENDERING_ENTITY_ID = new ThreadLocal();

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.isPaused()) {
            return;
        }
        if (++clientTickCount % 40 != 0) return;
        long now = System.currentTimeMillis();
        RECOIL_MAP.entrySet().removeIf(entry -> {
            HandRecoilState state = (HandRecoilState)entry.getValue();
            boolean mainDone = state.mainHand.startTimeMs <= 0L || (float)(now - state.mainHand.startTimeMs) > 2340.0f;
            boolean offDone = state.offHand.startTimeMs <= 0L || (float)(now - state.offHand.startTimeMs) > 2340.0f;
            return mainDone && offDone;
        });
    }

    public static void triggerRecoil(int entityId, int handOrdinal) {
        PortalGunRecoilManager.triggerRecoil(entityId, handOrdinal == 1 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
    }

    public static void triggerRecoil(int entityId, InteractionHand hand) {
        long now = System.currentTimeMillis();
        Minecraft mc = Minecraft.getInstance();
        HandRecoilState entityState = RECOIL_MAP.computeIfAbsent(entityId, k -> new HandRecoilState());
        PortalGunRecoilManager.triggerStateRecoil(entityState.get(hand), now);
        if (mc.player == null) return;
        if (entityId != mc.player.getId()) {
            if (entityId != -1) return;
        }
        PortalGunRecoilManager.triggerStateRecoil(LOCAL_PLAYER_FALLBACK.get(hand), now);
    }

    private static void triggerStateRecoil(RecoilState state, long now) {
        float currentAmount;
        state.startAmount = currentAmount = PortalGunRecoilManager.calculateCurrentRecoil(state, now);
        state.targetAmount = currentAmount > 0.05f ? Math.min(1.4f, currentAmount + 0.85f) : 1.0f;
        state.startTimeMs = now;
        state.lastTriggerTick = clientTickCount;
    }

    public static boolean hasRecentlyFired(int entityId, int handOrdinal, int maxTickDiff) {
        return PortalGunRecoilManager.hasRecentlyFired(entityId, handOrdinal == 1 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND, maxTickDiff);
    }

    public static boolean hasRecentlyFired(int entityId, InteractionHand hand, int maxTickDiff) {
        HandRecoilState entityState = RECOIL_MAP.get(entityId);
        if (entityState == null) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return false;
            if (entityId != mc.player.getId()) {
                if (entityId != -1) return false;
            }
            entityState = LOCAL_PLAYER_FALLBACK;
        }
        RecoilState state = entityState.get(hand);
        if (state.lastTriggerTick >= 0) return clientTickCount - state.lastTriggerTick <= maxTickDiff;
        return false;
    }

    public static float getRecoilAmount(int entityId, InteractionHand hand, float partialTick) {
        return PortalGunRecoilManager.getRecoilAmount(entityId, hand);
    }

    public static float getRecoilAmount(int entityId, InteractionHand hand) {
        long now = System.currentTimeMillis();
        HandRecoilState entityState = RECOIL_MAP.get(entityId);
        float amount = 0.0f;
        if (entityState != null) {
            amount = PortalGunRecoilManager.calculateCurrentRecoil(entityState.get(hand), now);
        }
        if (!(amount <= 1.0E-4f)) return amount;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return amount;
        if (entityId != mc.player.getId()) {
            if (entityId != -1) return amount;
        }
        amount = PortalGunRecoilManager.calculateCurrentRecoil(LOCAL_PLAYER_FALLBACK.get(hand), now);
        return amount;
    }

    public static float calculateCurrentRecoil(RecoilState state, long now) {
        if (state == null) return 0.0f;
        if (state.startTimeMs <= 0L) {
            return 0.0f;
        }
        float elapsed = now - state.startTimeMs;
        if (elapsed < 0.0f) return 0.0f;
        if (elapsed >= 340.0f) {
            return 0.0f;
        }
        if (elapsed < 65.0f) {
            float rise = Mth.sin((float)(elapsed / 65.0f * (float)Math.PI * 0.5f));
            return state.startAmount + (state.targetAmount - state.startAmount) * rise;
        }
        float x = (elapsed - 65.0f) / 275.0f;
        float decay = (float)Math.pow(1.0f - x, 2.2) * Mth.cos((float)(x * (float)Math.PI * 0.5f));
        return state.targetAmount * decay;
    }

    public static float calculateRecoilCurve(float elapsedMs, float startAmount, float targetAmount) {
        if (elapsedMs <= 0.0f) {
            return startAmount;
        }
        if (elapsedMs >= 340.0f) {
            return 0.0f;
        }
        if (elapsedMs < 65.0f) {
            float rise = Mth.sin((float)(elapsedMs / 65.0f * (float)Math.PI * 0.5f));
            return startAmount + (targetAmount - startAmount) * rise;
        }
        float x = (elapsedMs - 65.0f) / 275.0f;
        float decay = (float)Math.pow(1.0f - x, 2.2) * Mth.cos((float)(x * (float)Math.PI * 0.5f));
        return targetAmount * decay;
    }

    public static void applyFirstPersonRecoil(PoseStack poseStack, Player player, InteractionHand hand, float partialTick) {
        HumanoidArm arm;
        if (poseStack == null) {
            return;
        }
        int entityId = player != null ? player.getId() : -1;
        float recoil = PortalGunRecoilManager.getRecoilAmount(entityId, hand, partialTick);
        if (recoil <= 1.0E-4f) {
            recoil = PortalGunRecoilManager.calculateCurrentRecoil(LOCAL_PLAYER_FALLBACK.get(hand), System.currentTimeMillis());
        }
        if (recoil <= 1.0E-4f) {
            return;
        }
        if (player != null) {
            boolean isMainHand = hand == InteractionHand.MAIN_HAND;
            arm = isMainHand ? player.getMainArm() : player.getMainArm().getOpposite();
        } else {
            arm = hand == InteractionHand.MAIN_HAND ? HumanoidArm.RIGHT : HumanoidArm.LEFT;
        }
        boolean isRightArm = arm == HumanoidArm.RIGHT;
        float armSign = isRightArm ? 1.0f : -1.0f;
        float kickBackZ = 0.18f * recoil;
        float liftY = 0.03f * recoil;
        float kickX = -0.01f * armSign * recoil;
        float pitchDeg = 13.5f * recoil;
        float rollDeg = -1.8f * armSign * recoil;
        poseStack.translate(kickX, liftY, kickBackZ);
        poseStack.translate(0.0f, -0.2f, 0.0f);
        poseStack.mulPose(Axis.XP.rotationDegrees(pitchDeg));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rollDeg));
        poseStack.translate(0.0f, 0.2f, 0.0f);
    }

    public static void setCurrentRenderingEntity(LivingEntity entity) {
        CURRENT_RENDERING_ENTITY.set(entity);
    }

    public static void clearCurrentRenderingEntity() {
        CURRENT_RENDERING_ENTITY.remove();
    }

    public static void setCurrentRenderingEntityId(int entityId) {
        CURRENT_RENDERING_ENTITY_ID.set(entityId);
    }

    public static void clearCurrentRenderingEntityId() {
        CURRENT_RENDERING_ENTITY_ID.remove();
    }

    public static void applyThirdPersonRecoil(PoseStack poseStack, ItemDisplayContext context, float partialTick) {
        Entity e;
        Integer id;
        if (poseStack == null) return;
        if (context == null) {
            return;
        }
        if (context != ItemDisplayContext.THIRD_PERSON_RIGHT_HAND && context != ItemDisplayContext.THIRD_PERSON_LEFT_HAND) {
            return;
        }
        LivingEntity entity = CURRENT_RENDERING_ENTITY.get();
        if (entity == null && (id = CURRENT_RENDERING_ENTITY_ID.get()) != null && Minecraft.getInstance().level != null && (e = Minecraft.getInstance().level.getEntity(id.intValue())) instanceof LivingEntity) {
            LivingEntity le;
            entity = le = (LivingEntity)e;
        }
        if (entity == null) {
            entity = Minecraft.getInstance().player;
        }
        if (entity == null) {
            return;
        }
        boolean isRightHand = context == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
        HumanoidArm mainArm = entity.getMainArm();
        InteractionHand hand = isRightHand == (mainArm == HumanoidArm.RIGHT) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        PortalGunRecoilManager.applyThirdPersonRecoil(poseStack, context, entity.getId(), hand, partialTick);
    }

    public static void applyThirdPersonRecoil(PoseStack poseStack, ItemDisplayContext context, int entityId, InteractionHand hand, float partialTick) {
        if (poseStack == null) {
            return;
        }
        float recoil = PortalGunRecoilManager.getRecoilAmount(entityId, hand, partialTick);
        if (recoil <= 1.0E-4f) {
            return;
        }
        float kickBack = 0.08f * recoil;
        float pitchDeg = 9.0f * recoil;
        poseStack.translate(0.0f, 0.02f * recoil, -kickBack);
        poseStack.translate(0.0f, -0.1f, 0.0f);
        poseStack.mulPose(Axis.XP.rotationDegrees(-pitchDeg));
        poseStack.translate(0.0f, 0.1f, 0.0f);
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.InputConstants$Type
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.logging.LogUtils
 *  com.mojang.math.Axis
 *  net.minecraft.client.KeyMapping
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.ClientTickEvent$Post
 *  net.neoforged.neoforge.client.event.RenderLivingEvent$Post
 *  net.neoforged.neoforge.client.event.RenderLivingEvent$Pre
 *  net.neoforged.neoforge.client.event.ViewportEvent$ComputeCameraAngles
 *  net.neoforged.neoforge.client.event.ViewportEvent$ComputeFov
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.client.SpaceCruiserClientHandler$1
 *  org.ExtraTortik.ricks_stuff.client.SpaceCruiserClientHandler$PendingWarpSync
 *  org.ExtraTortik.ricks_stuff.client.audio.SpaceCruiserEngineSoundInstance
 *  org.ExtraTortik.ricks_stuff.client.audio.SpaceCruiserWarpSoundInstance
 *  org.ExtraTortik.ricks_stuff.client.gui.SpaceCruiserNavScreen
 *  org.ExtraTortik.ricks_stuff.client.overlay.OmegaScreenDistortionOverlay
 *  org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity
 *  org.ExtraTortik.ricks_stuff.entity.WarpState
 *  org.ExtraTortik.ricks_stuff.init.ModParticles
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SAbortWarpPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SShipControlPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.OmegaEffectClientBridge
 */
package org.ExtraTortik.ricks_stuff.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.client.SpaceCruiserClientHandler;
import org.ExtraTortik.ricks_stuff.client.audio.SpaceCruiserEngineSoundInstance;
import org.ExtraTortik.ricks_stuff.client.audio.SpaceCruiserWarpSoundInstance;
import org.ExtraTortik.ricks_stuff.client.gui.SpaceCruiserNavScreen;
import org.ExtraTortik.ricks_stuff.client.overlay.OmegaScreenDistortionOverlay;
import org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity;
import org.ExtraTortik.ricks_stuff.entity.WarpState;
import org.ExtraTortik.ricks_stuff.init.ModParticles;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.C2SAbortWarpPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SShipControlPacket;
import org.ExtraTortik.ricks_stuff.network.packet.OmegaEffectClientBridge;
import org.slf4j.Logger;

@EventBusSubscriber(modid="ricks_stuff", bus=EventBusSubscriber.Bus.GAME, value={Dist.CLIENT})
public class SpaceCruiserClientHandler {
    public static final KeyMapping KEY_HEADLIGHTS = new KeyMapping("key.ricks_stuff.ship_headlights", InputConstants.Type.KEYSYM, 72, "key.categories.ricks_stuff");
    public static final KeyMapping KEY_HOVER_LOCK = new KeyMapping("key.ricks_stuff.ship_hover_lock", InputConstants.Type.KEYSYM, 75, "key.categories.ricks_stuff");
    public static final KeyMapping KEY_ROLL_LEFT = new KeyMapping("key.ricks_stuff.ship_roll_left", InputConstants.Type.KEYSYM, 90, "key.categories.ricks_stuff");
    public static final KeyMapping KEY_ROLL_RIGHT = new KeyMapping("key.ricks_stuff.ship_roll_right", InputConstants.Type.KEYSYM, 67, "key.categories.ricks_stuff");
    public static final KeyMapping KEY_ENGINE = new KeyMapping("key.ricks_stuff.ship_engine", InputConstants.Type.KEYSYM, 82, "key.categories.ricks_stuff");
    public static final KeyMapping KEY_SHIP_NAV = new KeyMapping("key.ricks_stuff.ship_nav", InputConstants.Type.KEYSYM, 78, "key.categories.ricks_stuff");
    private static final Map<UUID, SpaceCruiserEngineSoundInstance> ACTIVE_ENGINE_SOUNDS = new HashMap<UUID, SpaceCruiserEngineSoundInstance>();
    private static final Map<UUID, SpaceCruiserWarpSoundInstance> ACTIVE_WARP_SOUNDS = new HashMap<UUID, SpaceCruiserWarpSoundInstance>();
    private static final Map<UUID, WarpState> LAST_WARP_STATES = new HashMap<UUID, WarpState>();
    private static ResourceKey<Level> lastClientDimension = null;
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Map<UUID, Integer> WARP_TRANSIT_WATCHDOG_TICKS = new HashMap<UUID, Integer>();
    private static final List<PendingWarpSync> PENDING_WARP_SYNCS = new ArrayList<PendingWarpSync>();
    private static float lastThrottle = 0.0f;
    private static float lastStrafe = 0.0f;
    private static float lastVertical = 0.0f;
    private static boolean lastBoost = false;
    private static int packetCooldown = 0;
    private static float clientShipYaw = 0.0f;
    private static float clientShipPitch = 0.0f;
    private static float clientShipRoll = 0.0f;
    public static float impactCameraShake = 0.0f;
    private static float clientYawVelocity = 0.0f;
    private static float clientPitchVelocity = 0.0f;
    private static boolean initializedShipAngles = false;

    public static void queuePendingWarpSync(int entityId, int warpState, int warpTimer, double targetX, double targetY, double targetZ, String targetDimension, UUID pilotUUID, UUID passengerUUID) {
        PENDING_WARP_SYNCS.removeIf(p -> p.entityId == entityId);
        PENDING_WARP_SYNCS.add(new PendingWarpSync(entityId, warpState, warpTimer, targetX, targetY, targetZ, targetDimension, pilotUUID, passengerUUID));
    }

    public static boolean isLocalPlayerInWarpTransit() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return false;
        if (mc.level == null) {
            return false;
        }
        Entity entity = mc.player.getVehicle();
        if (entity instanceof SpaceCruiserEntity) {
            SpaceCruiserEntity cruiser = (SpaceCruiserEntity)entity;
            return cruiser.getWarpStateEnum() == WarpState.TUNNEL_TRANSIT;
        }
        Iterator iterator = mc.level.getEntitiesOfClass(SpaceCruiserEntity.class, mc.player.getBoundingBox().inflate(32.0)).iterator();
        while (iterator.hasNext()) {
            SpaceCruiserEntity cruiser = (SpaceCruiserEntity)iterator.next();
            if (cruiser.getWarpStateEnum() != WarpState.TUNNEL_TRANSIT) continue;
            UUID pilot = cruiser.getWarpPilotUUID();
            UUID pass = cruiser.getWarpPassengerUUID();
            if (pilot != null) {
                if (pilot.equals(mc.player.getUUID())) return true;
            }
            if (pass != null && pass.equals(mc.player.getUUID())) return true;
        }
        return false;
    }

    public static void triggerImpactShake(float decel) {
        impactCameraShake = Math.max(impactCameraShake, Math.min(4.0f, decel * 3.5f));
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        SpaceCruiserEntity ship;
        LocalPlayer player;
        Entity entity;
        OmegaEffectClientBridge.clientTick();
        OmegaScreenDistortionOverlay.clientTick();
        if (impactCameraShake > 0.0f && (impactCameraShake *= 0.65f) < 0.05f) {
            impactCameraShake = 0.0f;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) {
            ACTIVE_ENGINE_SOUNDS.values().forEach(s -> {
                if (s == null) return;
                s.stopSound();
            });
            ACTIVE_ENGINE_SOUNDS.clear();
            ACTIVE_WARP_SOUNDS.values().forEach(s -> {
                if (s == null) return;
                s.stopSound();
            });
            ACTIVE_WARP_SOUNDS.clear();
            LAST_WARP_STATES.clear();
            WARP_TRANSIT_WATCHDOG_TICKS.clear();
            lastClientDimension = null;
            return;
        }
        ResourceKey currentDim = mc.level.dimension();
        if (lastClientDimension != currentDim) {
            lastClientDimension = currentDim;
            ACTIVE_ENGINE_SOUNDS.values().forEach(s -> {
                if (s == null) return;
                s.stopSound();
            });
            ACTIVE_ENGINE_SOUNDS.clear();
            ACTIVE_WARP_SOUNDS.values().forEach(s -> {
                if (s == null) return;
                s.stopSound();
            });
            ACTIVE_WARP_SOUNDS.clear();
            LAST_WARP_STATES.clear();
            WARP_TRANSIT_WATCHDOG_TICKS.clear();
        }
        if (!PENDING_WARP_SYNCS.isEmpty()) {
            Iterator<PendingWarpSync> it = PENDING_WARP_SYNCS.iterator();
            while (it.hasNext()) {
                PendingWarpSync pending = it.next();
                entity = mc.level.getEntity(pending.entityId);
                if (entity instanceof SpaceCruiserEntity) {
                    SpaceCruiserEntity ship2 = (SpaceCruiserEntity)entity;
                    ship2.handleWarpSync(pending.warpState, pending.warpTimer, pending.targetX, pending.targetY, pending.targetZ, pending.targetDimension);
                    if (pending.warpState == 0) {
                        ship2.setWarpPilotUUID(null);
                        ship2.setWarpPassengerUUID(null);
                        SpaceCruiserWarpSoundInstance warpSound = ACTIVE_WARP_SOUNDS.remove(ship2.getUUID());
                        if (warpSound != null && !warpSound.isStopped()) {
                            warpSound.stopSound();
                        }
                    } else {
                        boolean isAssignedRider;
                        if (pending.pilotUUID != null) {
                            ship2.setWarpPilotUUID(pending.pilotUUID);
                        }
                        if (pending.passengerUUID != null) {
                            ship2.setWarpPassengerUUID(pending.passengerUUID);
                        }
                        boolean bl = isAssignedRider = pending.pilotUUID != null && pending.pilotUUID.equals(mc.player.getUUID()) || pending.passengerUUID != null && pending.passengerUUID.equals(mc.player.getUUID());
                        if (isAssignedRider && (!mc.player.isPassenger() || mc.player.getVehicle() != ship2) && (double)mc.player.distanceTo((Entity)ship2) < 32.0) {
                            mc.player.startRiding((Entity)ship2, true);
                        }
                    }
                    it.remove();
                    continue;
                }
                --pending.ticksRemaining;
                if (pending.ticksRemaining > 0) continue;
                it.remove();
            }
        }
        if ((entity = (player = mc.player).getVehicle()) instanceof SpaceCruiserEntity && (ship = (SpaceCruiserEntity)entity).getControllingPassenger() == player) {
            boolean isShipDisabled;
            if (KEY_SHIP_NAV.consumeClick() && mc.screen == null) {
                mc.setScreen((Screen)new SpaceCruiserNavScreen(ship));
            }
            float throttle = (mc.options.keyUp.isDown() ? 1.0f : 0.0f) - (mc.options.keyDown.isDown() ? 1.0f : 0.0f);
            float strafe = (mc.options.keyLeft.isDown() ? -1.0f : 0.0f) + (mc.options.keyRight.isDown() ? 1.0f : 0.0f);
            float vertical = (mc.options.keyJump.isDown() ? 1.0f : 0.0f) - (mc.options.keyShift.isDown() ? 1.0f : 0.0f);
            boolean boosting = mc.options.keySprint.isDown();
            if (ship.getWarpStateEnum() == WarpState.CHARGING) {
                boolean inputAttempted;
                boolean bl = inputAttempted = Math.abs(throttle) > 0.01f || Math.abs(strafe) > 0.01f || Math.abs(vertical) > 0.01f || mc.options.keyJump.isDown() || mc.options.keyShift.isDown() || boosting;
                if (inputAttempted) {
                    ModMessages.sendToServer((CustomPacketPayload)new C2SAbortWarpPacket());
                    player.playSound((SoundEvent)ModSounds.GUN_MALFUNCTION.get(), 1.0f, 1.0f);
                    ship.abortWarp("PILOT_INTERRUPT");
                }
            }
            boolean toggleHeadlights = KEY_HEADLIGHTS.consumeClick();
            boolean toggleHover = KEY_HOVER_LOCK.consumeClick();
            boolean toggleEngine = KEY_ENGINE.consumeClick();
            double speedKmh = ship.getDeltaMovement().length() * 72.0;
            boolean isMovingFastEnough = speedKmh > 10.0;
            boolean isAlarmActive = isMovingFastEnough && (ship.isCrashing() || ship.getHealth() <= 0.0f);
            int alarmInterval = 39;
            if (isAlarmActive && (player.tickCount + ship.getId()) % alarmInterval == 0) {
                player.playSound((SoundEvent)ModSounds.SPACE_CRUISER_ALARM.get(), 1.0f, 1.0f);
            }
            float targetYaw = player.getYRot();
            float targetPitch = player.getXRot();
            if (!initializedShipAngles) {
                clientShipYaw = ship.getYRot();
                clientShipPitch = ship.getXRot();
                clientShipRoll = ship.getRoll();
                initializedShipAngles = true;
            }
            boolean bl = isShipDisabled = ship.isBroken() || ship.isCrashing() || ship.getHealth() <= 0.0f;
            if (ship.getWarpStateEnum().isWarping()) {
                clientShipYaw = ship.getYRot();
                clientShipPitch = ship.getXRot();
                clientShipRoll = ship.getRoll();
                clientYawVelocity = 0.0f;
                clientPitchVelocity = 0.0f;
            } else if ((ship.isLanded() || ship.isBroken()) && !isShipDisabled && (vertical > 0.15f || ship.isEngineOn() && throttle > 0.05f || !ship.onGround() && !ship.isInWater())) {
                ship.setLanded(false);
            }
            if (!ship.getWarpStateEnum().isWarping()) {
                if (ship.isLanded() || ship.isBroken()) {
                    clientShipPitch = ship.getXRot();
                    clientPitchVelocity = 0.0f;
                    clientShipRoll = ship.getRoll();
                    Vec3 motion = ship.getDeltaMovement();
                    float yawRad = (float)Math.toRadians(clientShipYaw);
                    double fwUnitX = -Math.sin(yawRad);
                    double fwUnitZ = Math.cos(yawRad);
                    double vFw = motion.x * fwUnitX + motion.z * fwUnitZ;
                    double horizSpeed = motion.horizontalDistance();
                    if (horizSpeed > 0.008 || Math.abs(throttle) > 0.05f) {
                        float forwardDirection = vFw < -0.01 || throttle < -0.05f && vFw <= 0.03 ? -1.0f : 1.0f;
                        float speedFactor = (float)Mth.clamp((double)(horizSpeed / 0.2), (double)0.25, (double)1.0);
                        float steerInput = strafe * 2.8f * forwardDirection * speedFactor;
                        float deltaYaw = Mth.wrapDegrees((float)(targetYaw - clientShipYaw));
                        clientYawVelocity = clientYawVelocity * 0.7f + (steerInput + Mth.clamp((float)(deltaYaw * 0.12f), (float)-2.5f, (float)2.5f) * speedFactor) * 0.3f;
                        clientShipYaw += clientYawVelocity;
                    } else {
                        clientYawVelocity = 0.0f;
                    }
                } else if (ship.isCrashing()) {
                    Vec3 motion = ship.getDeltaMovement();
                    double horiz = motion.horizontalDistance();
                    float diveTargetPitch = (float)Math.toDegrees(Math.atan2(-motion.y, Math.max(0.12, horiz)));
                    diveTargetPitch = Mth.clamp((float)diveTargetPitch, (float)22.0f, (float)68.0f);
                    float deltaPitch = Mth.wrapDegrees((float)(targetPitch - clientShipPitch));
                    clientPitchVelocity = clientPitchVelocity * 0.8f + (deltaPitch * 0.08f + (diveTargetPitch - clientShipPitch) * 0.12f);
                    clientPitchVelocity = Mth.clamp((float)clientPitchVelocity, (float)-8.0f, (float)8.0f);
                    clientShipPitch = Mth.wrapDegrees((float)(clientShipPitch + clientPitchVelocity));
                    float deltaYaw = Mth.wrapDegrees((float)(targetYaw - clientShipYaw));
                    clientYawVelocity = clientYawVelocity * 0.8f + deltaYaw * 0.08f;
                    clientYawVelocity = Mth.clamp((float)clientYawVelocity, (float)-8.0f, (float)8.0f);
                    clientShipYaw += clientYawVelocity;
                    clientShipRoll = Mth.rotLerp((float)0.06f, (float)clientShipRoll, (float)((float)Math.sin((float)ship.tickCount * 0.15f) * 22.0f));
                } else {
                    float deltaYaw = Mth.wrapDegrees((float)(targetYaw - clientShipYaw));
                    clientYawVelocity = clientYawVelocity * 0.72f + deltaYaw * 0.2f;
                    clientYawVelocity = Mth.clamp((float)clientYawVelocity, (float)-18.0f, (float)18.0f);
                    clientShipYaw += clientYawVelocity;
                    float deltaPitch = targetPitch <= -85.0f ? -4.0f : (targetPitch >= 85.0f ? 4.0f : Mth.wrapDegrees((float)(targetPitch - clientShipPitch)));
                    clientPitchVelocity = clientPitchVelocity * 0.72f + deltaPitch * 0.2f;
                    clientPitchVelocity = Mth.clamp((float)clientPitchVelocity, (float)-18.0f, (float)18.0f);
                    clientShipPitch = Mth.wrapDegrees((float)(clientShipPitch + clientPitchVelocity));
                    float manualRoll = (KEY_ROLL_LEFT.isDown() ? -1.0f : 0.0f) + (KEY_ROLL_RIGHT.isDown() ? 1.0f : 0.0f);
                    if (manualRoll != 0.0f) {
                        clientShipRoll = Mth.wrapDegrees((float)(clientShipRoll + manualRoll * 5.5f));
                    } else {
                        float autoBank = strafe * 20.0f + clientYawVelocity * 1.3f;
                        if (Math.abs(clientShipRoll) < 65.0f) {
                            clientShipRoll = Mth.rotLerp((float)0.18f, (float)clientShipRoll, (float)autoBank);
                        }
                    }
                }
            }
            if (!ship.getWarpStateEnum().isWarping()) {
                ship.setYRot(clientShipYaw);
                ship.setXRot(clientShipPitch);
                ship.setRoll(clientShipRoll);
                ship.setThrottle(throttle);
                ship.setStrafe(strafe);
                ship.setVertical(vertical);
                ship.setBoosting(boosting);
                lastThrottle = throttle;
                lastStrafe = strafe;
                lastVertical = vertical;
                lastBoost = boosting;
                Vec3 shipMotion = ship.getDeltaMovement();
                ModMessages.sendToServer((CustomPacketPayload)new C2SShipControlPacket(throttle, strafe, vertical, boosting, clientShipYaw, clientShipPitch, clientShipRoll, ship.getX(), ship.getY(), ship.getZ(), shipMotion.x, shipMotion.y, shipMotion.z, toggleHeadlights, toggleHover, toggleEngine));
            }
        } else {
            initializedShipAngles = false;
            clientYawVelocity = 0.0f;
            clientPitchVelocity = 0.0f;
        }
        HashSet<SpaceCruiserEntity> nearbyAndVehicleCruisers = new HashSet<SpaceCruiserEntity>(mc.level.getEntitiesOfClass(SpaceCruiserEntity.class, player.getBoundingBox().inflate(64.0)));
        Entity strafe = player.getVehicle();
        if (strafe instanceof SpaceCruiserEntity) {
            SpaceCruiserEntity vehicleCruiser = (SpaceCruiserEntity)strafe;
            nearbyAndVehicleCruisers.add(vehicleCruiser);
        }
        Iterator iterator = nearbyAndVehicleCruisers.iterator();
        while (true) {
            WarpState previousWarpState;
            WarpState currentWarpState;
            SpaceCruiserEngineSoundInstance currentEngineSound;
            boolean isAssigned;
            if (!iterator.hasNext()) {
                ACTIVE_ENGINE_SOUNDS.entrySet().removeIf(entry -> {
                    SpaceCruiserEngineSoundInstance sound = (SpaceCruiserEngineSoundInstance)entry.getValue();
                    if (sound == null) return true;
                    if (sound.isStopped()) return true;
                    if (!mc.getSoundManager().isActive((SoundInstance)sound)) {
                        return true;
                    }
                    if (!mc.level.getEntitiesOfClass(SpaceCruiserEntity.class, player.getBoundingBox().inflate(64.0), s -> s.getUUID().equals(entry.getKey())).isEmpty()) return false;
                    sound.stopSound();
                    return true;
                });
                ACTIVE_WARP_SOUNDS.entrySet().removeIf(entry -> {
                    SpaceCruiserWarpSoundInstance sound = (SpaceCruiserWarpSoundInstance)entry.getValue();
                    if (sound == null) return true;
                    if (sound.isStopped()) return true;
                    if (!mc.getSoundManager().isActive((SoundInstance)sound)) {
                        return true;
                    }
                    if (!mc.level.getEntitiesOfClass(SpaceCruiserEntity.class, player.getBoundingBox().inflate(64.0), s -> s.getUUID().equals(entry.getKey())).isEmpty()) return false;
                    sound.stopSound();
                    return true;
                });
                LAST_WARP_STATES.keySet().removeIf(uuid -> mc.level.getEntitiesOfClass(SpaceCruiserEntity.class, player.getBoundingBox().inflate(64.0), s -> s.getUUID().equals(uuid)).isEmpty());
                WARP_TRANSIT_WATCHDOG_TICKS.keySet().removeIf(uuid -> mc.level.getEntitiesOfClass(SpaceCruiserEntity.class, player.getBoundingBox().inflate(64.0), s -> s.getUUID().equals(uuid)).isEmpty() && (player.getVehicle() == null || !player.getVehicle().getUUID().equals(uuid)));
                return;
            }
            SpaceCruiserEntity ship3 = (SpaceCruiserEntity)iterator.next();
            UUID shipId = ship3.getUUID();
            boolean isPlayerCruiser = player.getVehicle() == ship3;
            UUID pilot = ship3.getWarpPilotUUID();
            UUID pass = ship3.getWarpPassengerUUID();
            boolean bl = isAssigned = pilot != null && pilot.equals(player.getUUID()) || pass != null && pass.equals(player.getUUID());
            if (isPlayerCruiser || isAssigned) {
                if (ship3.getWarpStateEnum() == WarpState.TUNNEL_TRANSIT) {
                    int transitTicks = WARP_TRANSIT_WATCHDOG_TICKS.getOrDefault(shipId, 0) + 1;
                    if (transitTicks > 160) {
                        LOGGER.warn("Space cruiser {} watchdog triggered: stuck in TUNNEL_TRANSIT for {} ticks, resetting to IDLE", (Object)shipId, (Object)transitTicks);
                        ship3.setWarpState(WarpState.IDLE);
                        ship3.setWarpTimer(0);
                        SpaceCruiserWarpSoundInstance warpSound = ACTIVE_WARP_SOUNDS.remove(shipId);
                        if (warpSound != null && !warpSound.isStopped()) {
                            warpSound.stopSound();
                        }
                        WARP_TRANSIT_WATCHDOG_TICKS.remove(shipId);
                    } else {
                        WARP_TRANSIT_WATCHDOG_TICKS.put(shipId, transitTicks);
                    }
                } else {
                    WARP_TRANSIT_WATCHDOG_TICKS.remove(shipId);
                }
            }
            if ((currentEngineSound = ACTIVE_ENGINE_SOUNDS.get(shipId)) == null || currentEngineSound.isStopped() || !mc.getSoundManager().isActive((SoundInstance)currentEngineSound)) {
                SpaceCruiserEngineSoundInstance newEngineSound = new SpaceCruiserEngineSoundInstance(ship3);
                ACTIVE_ENGINE_SOUNDS.put(shipId, newEngineSound);
                mc.getSoundManager().play((SoundInstance)newEngineSound);
            }
            if ((currentWarpState = ship3.getWarpStateEnum()) != (previousWarpState = LAST_WARP_STATES.getOrDefault(shipId, WarpState.IDLE))) {
                LAST_WARP_STATES.put(shipId, currentWarpState);
                if (currentWarpState == WarpState.CHARGING) {
                    player.level().playLocalSound(ship3.getX(), ship3.getY(), ship3.getZ(), (SoundEvent)ModSounds.SPACE_CRUISER_WARP_CHARGE.get(), SoundSource.NEUTRAL, 1.2f, 1.0f, false);
                } else if (currentWarpState == WarpState.RIFT_OPENING) {
                    player.level().playLocalSound(ship3.getX(), ship3.getY(), ship3.getZ(), (SoundEvent)ModSounds.SPACE_CRUISER_REALITY_CRACK.get(), SoundSource.NEUTRAL, 1.5f, 1.0f, false);
                } else if (currentWarpState == WarpState.EXITING) {
                    player.level().playLocalSound(ship3.getX(), ship3.getY(), ship3.getZ(), (SoundEvent)ModSounds.SPACE_CRUISER_REALITY_SHATTER.get(), SoundSource.NEUTRAL, 1.5f, 1.0f, false);
                } else if (currentWarpState == WarpState.IDLE && previousWarpState == WarpState.EXITING) {
                    player.level().playLocalSound(ship3.getX(), ship3.getY(), ship3.getZ(), (SoundEvent)ModSounds.SPACE_CRUISER_WARP_EXIT.get(), SoundSource.NEUTRAL, 1.2f, 1.0f, false);
                }
            }
            if (currentWarpState == WarpState.TUNNEL_TRANSIT) {
                SpaceCruiserWarpSoundInstance currentWarpSound = ACTIVE_WARP_SOUNDS.get(shipId);
                if (currentWarpSound == null || currentWarpSound.isStopped() || !mc.getSoundManager().isActive((SoundInstance)currentWarpSound)) {
                    SpaceCruiserWarpSoundInstance newWarpSound = new SpaceCruiserWarpSoundInstance(ship3);
                    ACTIVE_WARP_SOUNDS.put(shipId, newWarpSound);
                    mc.getSoundManager().play((SoundInstance)newWarpSound);
                }
            } else {
                SpaceCruiserWarpSoundInstance warpSound = ACTIVE_WARP_SOUNDS.remove(shipId);
                if (warpSound != null && !warpSound.isStopped()) {
                    warpSound.stopSound();
                }
            }
            SpaceCruiserClientHandler.spawnThrusterParticles(mc, ship3);
        }
    }

    private static void spawnThrusterParticles(Minecraft mc, SpaceCruiserEntity ship) {
        boolean hoverActive;
        Vec3 rearDeck = ship.getTransformedRearDeck();
        Vec3 leftThrusterRear = ship.getTransformedThruster(false);
        Vec3 rightThrusterRear = ship.getTransformedThruster(true);
        if (ship.isCrashing()) {
            Vec3 v = ship.getDeltaMovement();
            double vx = -v.x * 0.2 + (mc.level.random.nextDouble() - 0.5) * 0.05;
            double vy = -v.y * 0.2 + 0.05;
            double vz = -v.z * 0.2 + (mc.level.random.nextDouble() - 0.5) * 0.05;
            mc.level.addParticle((ParticleOptions)ParticleTypes.LARGE_SMOKE, leftThrusterRear.x, leftThrusterRear.y, leftThrusterRear.z, vx, vy, vz);
            mc.level.addParticle((ParticleOptions)ParticleTypes.LARGE_SMOKE, rightThrusterRear.x, rightThrusterRear.y, rightThrusterRear.z, vx, vy, vz);
            mc.level.addParticle((ParticleOptions)ParticleTypes.CAMPFIRE_COSY_SMOKE, rearDeck.x, rearDeck.y + 0.05, rearDeck.z, vx * 0.5, vy + 0.05, vz * 0.5);
            return;
        }
        if (ship.isBroken()) {
            if (mc.level.random.nextFloat() < 0.6f) {
                mc.level.addParticle((ParticleOptions)ParticleTypes.SMOKE, leftThrusterRear.x, leftThrusterRear.y + 0.1, leftThrusterRear.z, 0.0, 0.03, 0.0);
                mc.level.addParticle((ParticleOptions)ParticleTypes.SMOKE, rightThrusterRear.x, rightThrusterRear.y + 0.1, rightThrusterRear.z, 0.0, 0.03, 0.0);
                mc.level.addParticle((ParticleOptions)ParticleTypes.SMOKE, rearDeck.x, rearDeck.y + 0.05, rearDeck.z, 0.0, 0.03, 0.0);
            }
            if (!(mc.level.random.nextFloat() < 0.15f)) return;
            mc.level.addParticle((ParticleOptions)ModParticles.BLUE_DIMENSIONAL_SPARK.get(), leftThrusterRear.x, leftThrusterRear.y + 0.1, leftThrusterRear.z, 0.0, 0.02, 0.0);
            mc.level.addParticle((ParticleOptions)ModParticles.BLUE_DIMENSIONAL_SPARK.get(), rightThrusterRear.x, rightThrusterRear.y + 0.1, rightThrusterRear.z, 0.0, 0.02, 0.0);
            return;
        }
        if (ship.isWarpActive()) {
            float yawRad = (float)Math.toRadians(ship.getYRot());
            float pitchRad = (float)Math.toRadians(ship.getXRot());
            double backX = Math.sin(yawRad) * Math.cos(pitchRad) * 1.1;
            double backY = Math.sin(pitchRad) * 1.1;
            double backZ = -Math.cos(yawRad) * Math.cos(pitchRad) * 1.1;
            int i = 0;
            while (i < 2) {
                double rx1 = (mc.level.random.nextDouble() - 0.5) * 0.2;
                double ry1 = (mc.level.random.nextDouble() - 0.5) * 0.2;
                double rz1 = (mc.level.random.nextDouble() - 0.5) * 0.2;
                double rx2 = (mc.level.random.nextDouble() - 0.5) * 0.2;
                double ry2 = (mc.level.random.nextDouble() - 0.5) * 0.2;
                double rz2 = (mc.level.random.nextDouble() - 0.5) * 0.2;
                mc.level.addParticle((ParticleOptions)ModParticles.BLUE_DIMENSIONAL_SPARK.get(), leftThrusterRear.x, leftThrusterRear.y, leftThrusterRear.z, backX + rx1, backY + ry1, backZ + rz1);
                mc.level.addParticle((ParticleOptions)ModParticles.BLUE_DIMENSIONAL_SPARK.get(), rightThrusterRear.x, rightThrusterRear.y, rightThrusterRear.z, backX + rx2, backY + ry2, backZ + rz2);
                ++i;
            }
            return;
        }
        if (!ship.isEngineOn()) {
            return;
        }
        boolean hasThrust = Math.abs(ship.getThrottle()) > 0.05f || Math.abs(ship.getVertical()) > 0.05f || ship.isBoosting();
        boolean bl = hoverActive = !ship.onGround() && ship.isHoverLock();
        if (hasThrust || hoverActive) {
            float yawRad = (float)Math.toRadians(ship.getYRot());
            float pitchRad = (float)Math.toRadians(ship.getXRot());
            double backX = Math.sin(yawRad) * Math.cos(pitchRad) * (ship.isBoosting() ? 0.8 : 0.35);
            double backY = Math.sin(pitchRad) * (ship.isBoosting() ? 0.8 : 0.35);
            double backZ = -Math.cos(yawRad) * Math.cos(pitchRad) * (ship.isBoosting() ? 0.8 : 0.35);
            double rx1 = (mc.level.random.nextDouble() - 0.5) * 0.12;
            double ry1 = (mc.level.random.nextDouble() - 0.5) * 0.12;
            double rz1 = (mc.level.random.nextDouble() - 0.5) * 0.12;
            double rx2 = (mc.level.random.nextDouble() - 0.5) * 0.12;
            double ry2 = (mc.level.random.nextDouble() - 0.5) * 0.12;
            double rz2 = (mc.level.random.nextDouble() - 0.5) * 0.12;
            mc.level.addParticle((ParticleOptions)ModParticles.BLUE_DIMENSIONAL_SPARK.get(), leftThrusterRear.x, leftThrusterRear.y, leftThrusterRear.z, backX + rx1, backY + ry1, backZ + rz1);
            mc.level.addParticle((ParticleOptions)ModParticles.BLUE_DIMENSIONAL_SPARK.get(), rightThrusterRear.x, rightThrusterRear.y, rightThrusterRear.z, backX + rx2, backY + ry2, backZ + rz2);
        }
        if (!ship.onGround() && !ship.isHoverLock() && ship.getDeltaMovement().lengthSqr() > 0.45) {
            mc.level.addParticle((ParticleOptions)ParticleTypes.CLOUD, leftThrusterRear.x, leftThrusterRear.y, leftThrusterRear.z, -ship.getDeltaMovement().x * 0.15, -ship.getDeltaMovement().y * 0.15, -ship.getDeltaMovement().z * 0.15);
            mc.level.addParticle((ParticleOptions)ParticleTypes.CLOUD, rightThrusterRear.x, rightThrusterRear.y, rightThrusterRear.z, -ship.getDeltaMovement().x * 0.15, -ship.getDeltaMovement().y * 0.15, -ship.getDeltaMovement().z * 0.15);
        }
        if (!(ship.getHealth() < 40.0f)) return;
        if (!(mc.level.random.nextFloat() < 0.3f)) return;
        mc.level.addParticle((ParticleOptions)ParticleTypes.SMOKE, ship.getX() + (mc.level.random.nextDouble() - 0.5) * 2.0, ship.getY() + 1.2, ship.getZ() + (mc.level.random.nextDouble() - 0.5) * 2.0, 0.0, 0.05, 0.0);
    }

    @SubscribeEvent
    public static void onRenderLivingPre(RenderLivingEvent.Pre<?, ?> event) {
        LivingEntity living = event.getEntity();
        Entity entity = living.getVehicle();
        if (!(entity instanceof SpaceCruiserEntity)) return;
        SpaceCruiserEntity ship = (SpaceCruiserEntity)entity;
        PoseStack poseStack = event.getPoseStack();
        poseStack.pushPose();
        float partialTick = event.getPartialTick();
        float shipYaw = Mth.rotLerp((float)partialTick, (float)ship.yRotO, (float)ship.getYRot());
        float shipPitch = Mth.rotLerp((float)partialTick, (float)ship.xRotO, (float)ship.getXRot());
        float shipRoll = ship.getInterpolatedRoll(partialTick);
        int seatIndex = ship.getPassengers().indexOf(living);
        Vec3 localSeat = seatIndex == 0 ? SpaceCruiserEntity.PILOT_SEAT : SpaceCruiserEntity.PASSENGER_SEAT;
        Vec3 rotatedOffset = SpaceCruiserEntity.rotateVector((Vec3)localSeat, (float)shipYaw, (float)shipPitch, (float)shipRoll);
        double shipX = Mth.lerp((double)partialTick, (double)ship.xo, (double)ship.getX());
        double shipY = Mth.lerp((double)partialTick, (double)ship.yo, (double)ship.getY());
        double shipZ = Mth.lerp((double)partialTick, (double)ship.zo, (double)ship.getZ());
        double exactSeatX = shipX + rotatedOffset.x;
        double exactSeatY = shipY + rotatedOffset.y;
        double exactSeatZ = shipZ + rotatedOffset.z;
        double vanillaX = Mth.lerp((double)partialTick, (double)living.xOld, (double)living.getX());
        double vanillaY = Mth.lerp((double)partialTick, (double)living.yOld, (double)living.getY());
        double vanillaZ = Mth.lerp((double)partialTick, (double)living.zOld, (double)living.getZ());
        poseStack.translate(exactSeatX - vanillaX, exactSeatY - vanillaY, exactSeatZ - vanillaZ);
        float pivotY = 0.6f;
        poseStack.translate(0.0f, pivotY, 0.0f);
        poseStack.mulPose(Axis.YP.rotationDegrees(-shipYaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(shipPitch));
        poseStack.mulPose(Axis.ZP.rotationDegrees(shipRoll));
        poseStack.mulPose(Axis.YP.rotationDegrees(shipYaw));
        poseStack.translate(0.0f, -pivotY, 0.0f);
    }

    @SubscribeEvent
    public static void onRenderLivingPost(RenderLivingEvent.Post<?, ?> event) {
        if (!(event.getEntity().getVehicle() instanceof SpaceCruiserEntity)) return;
        event.getPoseStack().popPose();
    }

    @SubscribeEvent
    public static void onComputeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        Entity entity = mc.player.getVehicle();
        if (!(entity instanceof SpaceCruiserEntity)) return;
        SpaceCruiserEntity ship = (SpaceCruiserEntity)entity;
        float partialTick = (float)event.getPartialTick();
        float shipYaw = Mth.rotLerp((float)partialTick, (float)ship.yRotO, (float)ship.getYRot());
        float shipPitch = Mth.rotLerp((float)partialTick, (float)ship.xRotO, (float)ship.getXRot());
        float shipRoll = ship.getInterpolatedRoll(partialTick);
        float shakePitch = 0.0f;
        float shakeRoll = 0.0f;
        float shakeYaw = 0.0f;
        if (impactCameraShake > 0.05f && Config.cameraShakeIntensity > 0.0) {
            float shake = impactCameraShake * (float)Config.cameraShakeIntensity;
            float time = ((float)mc.player.tickCount + partialTick) * 2.5f;
            shakePitch = (float)Math.sin(time * 1.4f) * shake * 0.35f;
            shakeRoll = (float)Math.cos(time * 1.8f) * shake * 0.45f;
            shakeYaw = (float)Math.sin(time * 1.0f) * shake * 0.2f;
        }
        if (ship.getWarpStateEnum() != WarpState.IDLE) {
            float warpIntensity = 1.0f;
            int timer = ship.getWarpTimer();
            switch (1.$SwitchMap$org$ExtraTortik$ricks_stuff$entity$WarpState[ship.getWarpStateEnum().ordinal()]) {
                case 1: {
                    warpIntensity = Math.min(1.0f, (float)timer / 50.0f) * 0.8f;
                    break;
                }
                case 2: {
                    warpIntensity = 1.2f;
                    break;
                }
                case 3: {
                    warpIntensity = 1.8f;
                    break;
                }
                case 4: {
                    warpIntensity = 1.0f;
                    break;
                }
                case 5: {
                    warpIntensity = Math.max(0.0f, 1.0f - (float)timer / 30.0f) * 1.5f;
                    break;
                }
                default: {
                    warpIntensity = 1.0f;
                }
            }
            if (Config.cameraShakeIntensity > 0.0) {
                warpIntensity *= (float)Config.cameraShakeIntensity;
            }
            float time = (float)mc.player.tickCount + partialTick;
            shakePitch += (float)Math.sin(time * 30.0f) * warpIntensity * 0.65f;
            shakeRoll += (float)Math.cos(time * 35.0f) * warpIntensity * 0.85f;
            shakeYaw += (float)Math.sin(time * 25.0f) * warpIntensity * 0.4f;
        }
        if (mc.options.getCameraType().isFirstPerson()) {
            event.setYaw(shipYaw + shakeYaw);
            event.setPitch(shipPitch + shakePitch);
            event.setRoll(shipRoll + shakeRoll);
        } else {
            event.setRoll(event.getRoll() + shipRoll + shakeRoll);
            event.setPitch(event.getPitch() + shakePitch);
            event.setYaw(event.getYaw() + shakeYaw);
        }
    }

    @SubscribeEvent
    public static void onComputeFov(ViewportEvent.ComputeFov event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        Entity entity = mc.player.getVehicle();
        if (!(entity instanceof SpaceCruiserEntity)) return;
        SpaceCruiserEntity ship = (SpaceCruiserEntity)entity;
        WarpState warpState = ship.getWarpStateEnum();
        if (warpState == WarpState.IDLE) return;
        int timer = ship.getWarpTimer();
        float partialTick = (float)event.getPartialTick();
        switch (1.$SwitchMap$org$ExtraTortik$ricks_stuff$entity$WarpState[warpState.ordinal()]) {
            case 1: {
                event.setFOV(event.getFOV() * (double)(1.0f - (float)timer / 50.0f * 0.08f));
                break;
            }
            case 2: {
                event.setFOV(event.getFOV() * (double)0.9f);
                break;
            }
            case 3: {
                event.setFOV(event.getFOV() * (double)(1.0f + (float)timer / 20.0f * 0.3f));
                break;
            }
            case 4: {
                float time = (float)mc.player.tickCount + partialTick;
                float breathing = (float)Math.sin(time * 0.15f) * 0.02f;
                event.setFOV(event.getFOV() * (double)(1.3f + breathing));
                break;
            }
            case 5: {
                float progress = Math.min(1.0f, (float)timer / 30.0f);
                float decay = Mth.lerp((float)progress, (float)1.35f, (float)1.0f);
                event.setFOV(event.getFOV() * (double)decay);
                break;
            }
        }
    }
}

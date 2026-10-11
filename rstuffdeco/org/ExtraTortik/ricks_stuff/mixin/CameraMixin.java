/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Camera
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.boss.enderdragon.EnderDragon
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.phys.Vec3
 *  org.ExtraTortik.ricks_stuff.client.ClientPossessionBridge
 *  org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Constant
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyConstant
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package org.ExtraTortik.ricks_stuff.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import org.ExtraTortik.ricks_stuff.client.ClientPossessionBridge;
import org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Camera.class})
public abstract class CameraMixin {
    @Shadow
    protected abstract void setPosition(double var1, double var3, double var5);

    @Shadow
    protected abstract void setRotation(float var1, float var2);

    @ModifyConstant(method={"setup"}, constant={@Constant(floatValue=4.0f)})
    private float ricks_stuff$modifyCameraDistance(float original) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.player.getVehicle() instanceof SpaceCruiserEntity) {
            return 9.0f;
        }
        if (!ClientPossessionBridge.isPossessing()) return original;
        Entity cameraEntity = mc.getCameraEntity();
        if (!(cameraEntity instanceof EnderDragon)) return original;
        return 14.0f;
    }

    @Inject(method={"setup"}, at={@At(value="HEAD")})
    private void ricks_stuff$onSetupCameraHead(BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick, CallbackInfo ci) {
        if (!ClientPossessionBridge.isPossessing()) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (!(entity instanceof LivingEntity)) return;
        LivingEntity living = (LivingEntity)entity;
        float yaw = mc.player.getYRot();
        float pitch = mc.player.getXRot();
        if (living instanceof EnderDragon) {
            float dragonYaw = yaw + 180.0f;
            living.setYRot(dragonYaw);
            living.setXRot(pitch);
            living.yRotO = dragonYaw;
            living.xRotO = pitch;
            living.yHeadRot = dragonYaw;
            living.yHeadRotO = dragonYaw;
            living.yBodyRot = dragonYaw;
            living.yBodyRotO = dragonYaw;
        } else {
            living.setYRot(yaw);
            living.setXRot(pitch);
            living.yRotO = yaw;
            living.xRotO = pitch;
            living.yHeadRot = yaw;
            living.yHeadRotO = yaw;
            living.yBodyRot = yaw;
            living.yBodyRotO = yaw;
        }
    }

    @Inject(method={"setup"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/Camera;setRotation(FF)V", ordinal=0, shift=At.Shift.AFTER)})
    private void ricks_stuff$onSetupCameraRotation(BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick, CallbackInfo ci) {
        if (!ClientPossessionBridge.isPossessing()) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (!(entity instanceof EnderDragon)) return;
        this.setRotation(mc.player.getYRot(), mc.player.getXRot());
    }

    @Inject(method={"setup"}, at={@At(value="TAIL")})
    private void ricks_stuff$onSetupCamera(BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick, CallbackInfo ci) {
        if (!(entity instanceof LivingEntity)) return;
        LivingEntity living = (LivingEntity)entity;
        Entity entity2 = living.getVehicle();
        if (!(entity2 instanceof SpaceCruiserEntity)) return;
        SpaceCruiserEntity ship = (SpaceCruiserEntity)entity2;
        if (detached) return;
        float shipYaw = Mth.rotLerp((float)partialTick, (float)ship.yRotO, (float)ship.getYRot());
        float shipPitch = Mth.rotLerp((float)partialTick, (float)ship.xRotO, (float)ship.getXRot());
        float shipRoll = ship.getInterpolatedRoll(partialTick);
        int seatIndex = ship.getPassengers().indexOf(living);
        Vec3 localSeat = seatIndex == 0 ? SpaceCruiserEntity.PILOT_SEAT : SpaceCruiserEntity.PASSENGER_SEAT;
        Vec3 localEye = localSeat.add(0.0, 1.48, 0.08);
        Vec3 rotatedEye = SpaceCruiserEntity.rotateVector((Vec3)localEye, (float)shipYaw, (float)shipPitch, (float)shipRoll);
        double shipX = Mth.lerp((double)partialTick, (double)ship.xo, (double)ship.getX());
        double shipY = Mth.lerp((double)partialTick, (double)ship.yo, (double)ship.getY());
        double shipZ = Mth.lerp((double)partialTick, (double)ship.zo, (double)ship.getZ());
        this.setPosition(shipX + rotatedEye.x, shipY + rotatedEye.y, shipZ + rotatedEye.z);
    }
}

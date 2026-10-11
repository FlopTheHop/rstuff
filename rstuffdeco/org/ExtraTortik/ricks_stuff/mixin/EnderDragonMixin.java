/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MoverType
 *  net.minecraft.world.entity.boss.enderdragon.EnderDragon
 *  net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance
 *  net.minecraft.world.phys.Vec3
 *  org.ExtraTortik.ricks_stuff.puppet.PuppetServerManager
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Redirect
 */
package org.ExtraTortik.ricks_stuff.mixin;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import net.minecraft.world.phys.Vec3;
import org.ExtraTortik.ricks_stuff.puppet.PuppetServerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={EnderDragon.class})
public abstract class EnderDragonMixin {
    @Redirect(method={"aiStep"}, at=@At(value="INVOKE", target="Lnet/minecraft/world/entity/boss/enderdragon/phases/DragonPhaseInstance;doServerTick()V"))
    private void ricks_stuff$onDragonServerPhaseTick(DragonPhaseInstance instance) {
        EnderDragon dragon = (EnderDragon)this;
        if (PuppetServerManager.isPossessed((Mob)dragon)) {
            return;
        }
        instance.doServerTick();
    }

    @Redirect(method={"aiStep"}, at=@At(value="INVOKE", target="Lnet/minecraft/world/entity/boss/enderdragon/phases/DragonPhaseInstance;getFlyTargetLocation()Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 ricks_stuff$onDragonFlyTarget(DragonPhaseInstance instance) {
        EnderDragon dragon = (EnderDragon)this;
        if (!PuppetServerManager.isPossessed((Mob)dragon)) return instance.getFlyTargetLocation();
        PuppetServerManager.tickPuppet((Mob)dragon);
        dragon.move(MoverType.SELF, dragon.getDeltaMovement());
        return null;
    }
}

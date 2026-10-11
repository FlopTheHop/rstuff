/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.boss.enderdragon.EnderDragon
 *  net.minecraft.world.level.Level
 *  org.ExtraTortik.ricks_stuff.client.ClientPossessionBridge
 *  org.ExtraTortik.ricks_stuff.puppet.PuppetServerManager
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package org.ExtraTortik.ricks_stuff.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.Level;
import org.ExtraTortik.ricks_stuff.client.ClientPossessionBridge;
import org.ExtraTortik.ricks_stuff.puppet.PuppetServerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Mob.class})
public abstract class MobMixin
extends LivingEntity {
    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method={"serverAiStep"}, at={@At(value="HEAD")}, cancellable=true)
    private void ricks_stuff$onServerAiStep(CallbackInfo ci) {
        Mob mob = (Mob)this;
        if (mob instanceof EnderDragon) {
            return;
        }
        if (!PuppetServerManager.isPossessed((Mob)mob)) return;
        super.serverAiStep();
        PuppetServerManager.tickPuppet((Mob)mob);
        ci.cancel();
    }

    @Inject(method={"tickHeadTurn"}, at={@At(value="HEAD")}, cancellable=true)
    private void ricks_stuff$cancelHeadTurnWhenPossessed(float yRot, float animStep, CallbackInfoReturnable<Float> cir) {
        Mob mob = (Mob)this;
        if (!mob.level().isClientSide()) return;
        if (!ClientPossessionBridge.isPossessingMob((Mob)mob)) return;
        cir.setReturnValue((Object)Float.valueOf(animStep));
    }
}

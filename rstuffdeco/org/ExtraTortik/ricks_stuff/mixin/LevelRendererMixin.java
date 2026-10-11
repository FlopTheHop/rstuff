/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Camera
 *  net.minecraft.client.renderer.LevelRenderer
 *  net.minecraft.client.renderer.LightTexture
 *  net.minecraft.client.renderer.RenderType
 *  org.ExtraTortik.ricks_stuff.client.SpaceCruiserClientHandler
 *  org.joml.Matrix4f
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package org.ExtraTortik.ricks_stuff.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import org.ExtraTortik.ricks_stuff.client.SpaceCruiserClientHandler;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={LevelRenderer.class})
public abstract class LevelRendererMixin {
    @Unique
    private boolean ricks_stuff$isInTunnelTransit() {
        return SpaceCruiserClientHandler.isLocalPlayerInWarpTransit();
    }

    @Inject(method={"renderSectionLayer"}, at={@At(value="HEAD")}, cancellable=true)
    private void ricks_stuff$cancelSectionLayerInTunnel(RenderType renderType, double camX, double camY, double camZ, Matrix4f modelViewMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
        if (!this.ricks_stuff$isInTunnelTransit()) return;
        ci.cancel();
    }

    @Inject(method={"renderSky"}, at={@At(value="HEAD")}, cancellable=true)
    private void ricks_stuff$cancelSkyInTunnel(Matrix4f modelViewMatrix, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean isFoggy, Runnable skyFogSetup, CallbackInfo ci) {
        if (!this.ricks_stuff$isInTunnelTransit()) return;
        ci.cancel();
    }

    @Inject(method={"renderClouds"}, at={@At(value="HEAD")}, cancellable=true)
    private void ricks_stuff$cancelCloudsInTunnel(PoseStack poseStack, Matrix4f modelViewMatrix, Matrix4f projectionMatrix, float partialTick, double camX, double camY, double camZ, CallbackInfo ci) {
        if (!this.ricks_stuff$isInTunnelTransit()) return;
        ci.cancel();
    }

    @Inject(method={"renderSnowAndRain"}, at={@At(value="HEAD")}, cancellable=true)
    private void ricks_stuff$cancelSnowAndRainInTunnel(LightTexture lightTexture, float partialTick, double camX, double camY, double camZ, CallbackInfo ci) {
        if (!this.ricks_stuff$isInTunnelTransit()) return;
        ci.cancel();
    }
}

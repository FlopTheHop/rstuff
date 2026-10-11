/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.shaders.FogShape
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.Camera
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.renderer.FogRenderer$FogMode
 *  net.minecraft.resources.ResourceLocation
 *  net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
 *  org.ExtraTortik.ricks_stuff.fluid.AcidFluidType
 *  org.joml.Vector3f
 */
package org.ExtraTortik.ricks_stuff.fluid;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.ExtraTortik.ricks_stuff.fluid.AcidFluidType;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

class AcidFluidType.1
implements IClientFluidTypeExtensions {
    AcidFluidType.1(AcidFluidType this$0) {
    }

    public ResourceLocation getStillTexture() {
        return AcidFluidType.STILL_TEXTURE;
    }

    public ResourceLocation getFlowingTexture() {
        return AcidFluidType.FLOWING_TEXTURE;
    }

    public ResourceLocation getOverlayTexture() {
        return AcidFluidType.OVERLAY_TEXTURE;
    }

    public int getTintColor() {
        return -1;
    }

    @NotNull
    public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
        return new Vector3f(0.25f, 0.98f, 0.15f);
    }

    public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
        RenderSystem.setShaderFogStart((float)0.0f);
        RenderSystem.setShaderFogEnd((float)3.5f);
    }
}

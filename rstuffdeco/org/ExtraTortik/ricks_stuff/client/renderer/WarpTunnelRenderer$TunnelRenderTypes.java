/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.DefaultVertexFormat
 *  com.mojang.blaze3d.vertex.VertexFormat
 *  com.mojang.blaze3d.vertex.VertexFormat$Mode
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.RenderType$CompositeState
 */
package org.ExtraTortik.ricks_stuff.client.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;

public static final class WarpTunnelRenderer.TunnelRenderTypes
extends RenderType {
    public static final RenderType TUNNEL_WALLS = WarpTunnelRenderer.TunnelRenderTypes.create((String)"warp_tunnel_walls", (VertexFormat)DefaultVertexFormat.POSITION_COLOR, (VertexFormat.Mode)VertexFormat.Mode.QUADS, (int)8192, (boolean)false, (boolean)true, (RenderType.CompositeState)RenderType.CompositeState.builder().setShaderState(RENDERTYPE_LIGHTNING_SHADER).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL).setDepthTestState(LEQUAL_DEPTH_TEST).setWriteMaskState(COLOR_WRITE).createCompositeState(false));
    public static final RenderType TUNNEL_SPEEDLINES = WarpTunnelRenderer.TunnelRenderTypes.create((String)"warp_tunnel_speedlines", (VertexFormat)DefaultVertexFormat.POSITION_COLOR, (VertexFormat.Mode)VertexFormat.Mode.QUADS, (int)4096, (boolean)false, (boolean)true, (RenderType.CompositeState)RenderType.CompositeState.builder().setShaderState(RENDERTYPE_LIGHTNING_SHADER).setTransparencyState(LIGHTNING_TRANSPARENCY).setCullState(NO_CULL).setDepthTestState(LEQUAL_DEPTH_TEST).setWriteMaskState(COLOR_WRITE).createCompositeState(false));

    private WarpTunnelRenderer.TunnelRenderTypes(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }
}

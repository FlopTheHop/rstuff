/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.BufferBuilder
 *  com.mojang.blaze3d.vertex.BufferUploader
 *  com.mojang.blaze3d.vertex.DefaultVertexFormat
 *  com.mojang.blaze3d.vertex.MeshData
 *  com.mojang.blaze3d.vertex.Tesselator
 *  com.mojang.blaze3d.vertex.VertexFormat$Mode
 *  net.minecraft.client.renderer.GameRenderer
 *  org.joml.Matrix4f
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;

public class RadialRenderUtils {
    public static void drawSector(Matrix4f matrix, float cx, float cy, float rIn, float rOut, float startAngle, float endAngle, int argb, int steps) {
        float a = (float)(argb >> 24 & 0xFF) / 255.0f;
        float r = (float)(argb >> 16 & 0xFF) / 255.0f;
        float g = (float)(argb >> 8 & 0xFF) / 255.0f;
        float b = (float)(argb & 0xFF) / 255.0f;
        if (a <= 0.0f) {
            return;
        }
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        int count = Math.max(4, steps);
        int i = 0;
        while (true) {
            if (i > count) {
                BufferUploader.drawWithShader((MeshData)buffer.buildOrThrow());
                RenderSystem.disableBlend();
                return;
            }
            float t = (float)i / (float)count;
            float angle = startAngle + t * (endAngle - startAngle);
            float cos = (float)Math.cos(angle);
            float sin = (float)Math.sin(angle);
            buffer.addVertex(matrix, cx + rOut * cos, cy + rOut * sin, 0.0f).setColor(r, g, b, a);
            buffer.addVertex(matrix, cx + rIn * cos, cy + rIn * sin, 0.0f).setColor(r, g, b, a);
            ++i;
        }
    }

    public static void drawRing(Matrix4f matrix, float cx, float cy, float rIn, float rOut, int argb, int steps) {
        RadialRenderUtils.drawSector(matrix, cx, cy, rIn, rOut, 0.0f, (float)Math.PI * 2, argb, steps);
    }

    public static void drawRingOutline(Matrix4f matrix, float cx, float cy, float rIn, float rOut, int argb, float strokeWidth, int steps) {
        RadialRenderUtils.drawCircleOutline(matrix, cx, cy, rOut, argb, strokeWidth, steps);
        RadialRenderUtils.drawCircleOutline(matrix, cx, cy, rIn, argb, strokeWidth, steps);
    }

    public static void drawSectorGradient(Matrix4f matrix, float cx, float cy, float rIn, float rOut, float startAngle, float endAngle, int innerArgb, int outerArgb, int steps) {
        float inA = (float)(innerArgb >> 24 & 0xFF) / 255.0f;
        float inR = (float)(innerArgb >> 16 & 0xFF) / 255.0f;
        float inG = (float)(innerArgb >> 8 & 0xFF) / 255.0f;
        float inB = (float)(innerArgb & 0xFF) / 255.0f;
        float outA = (float)(outerArgb >> 24 & 0xFF) / 255.0f;
        float outR = (float)(outerArgb >> 16 & 0xFF) / 255.0f;
        float outG = (float)(outerArgb >> 8 & 0xFF) / 255.0f;
        float outB = (float)(outerArgb & 0xFF) / 255.0f;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        int count = Math.max(4, steps);
        int i = 0;
        while (true) {
            if (i > count) {
                BufferUploader.drawWithShader((MeshData)buffer.buildOrThrow());
                RenderSystem.disableBlend();
                return;
            }
            float t = (float)i / (float)count;
            float angle = startAngle + t * (endAngle - startAngle);
            float cos = (float)Math.cos(angle);
            float sin = (float)Math.sin(angle);
            buffer.addVertex(matrix, cx + rOut * cos, cy + rOut * sin, 0.0f).setColor(outR, outG, outB, outA);
            buffer.addVertex(matrix, cx + rIn * cos, cy + rIn * sin, 0.0f).setColor(inR, inG, inB, inA);
            ++i;
        }
    }

    public static void drawSectorOutline(Matrix4f matrix, float cx, float cy, float rIn, float rOut, float startAngle, float endAngle, int argb, float strokeWidth, int steps) {
        float sin;
        float cos;
        float angle;
        float t;
        int i;
        float a = (float)(argb >> 24 & 0xFF) / 255.0f;
        float r = (float)(argb >> 16 & 0xFF) / 255.0f;
        float g = (float)(argb >> 8 & 0xFF) / 255.0f;
        float b = (float)(argb & 0xFF) / 255.0f;
        if (a <= 0.0f) return;
        if (strokeWidth <= 0.0f) {
            return;
        }
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tesselator tessellator = Tesselator.getInstance();
        int count = Math.max(4, steps);
        float halfStroke = strokeWidth / 2.0f;
        BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        for (i = 0; i <= count; ++i) {
            t = (float)i / (float)count;
            angle = startAngle + t * (endAngle - startAngle);
            cos = (float)Math.cos(angle);
            sin = (float)Math.sin(angle);
            buffer.addVertex(matrix, cx + (rOut + halfStroke) * cos, cy + (rOut + halfStroke) * sin, 0.0f).setColor(r, g, b, a);
            buffer.addVertex(matrix, cx + (rOut - halfStroke) * cos, cy + (rOut - halfStroke) * sin, 0.0f).setColor(r, g, b, a);
        }
        BufferUploader.drawWithShader((MeshData)buffer.buildOrThrow());
        buffer = tessellator.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        i = 0;
        while (true) {
            if (i > count) {
                BufferUploader.drawWithShader((MeshData)buffer.buildOrThrow());
                float cosStart = (float)Math.cos(startAngle);
                float sinStart = (float)Math.sin(startAngle);
                float normalX1 = -sinStart * halfStroke;
                float normalY1 = cosStart * halfStroke;
                buffer = tessellator.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
                buffer.addVertex(matrix, cx + rOut * cosStart + normalX1, cy + rOut * sinStart + normalY1, 0.0f).setColor(r, g, b, a);
                buffer.addVertex(matrix, cx + rOut * cosStart - normalX1, cy + rOut * sinStart - normalY1, 0.0f).setColor(r, g, b, a);
                buffer.addVertex(matrix, cx + rIn * cosStart + normalX1, cy + rIn * sinStart + normalY1, 0.0f).setColor(r, g, b, a);
                buffer.addVertex(matrix, cx + rIn * cosStart - normalX1, cy + rIn * sinStart - normalY1, 0.0f).setColor(r, g, b, a);
                BufferUploader.drawWithShader((MeshData)buffer.buildOrThrow());
                float cosEnd = (float)Math.cos(endAngle);
                float sinEnd = (float)Math.sin(endAngle);
                float normalX2 = -sinEnd * halfStroke;
                float normalY2 = cosEnd * halfStroke;
                buffer = tessellator.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
                buffer.addVertex(matrix, cx + rOut * cosEnd + normalX2, cy + rOut * sinEnd + normalY2, 0.0f).setColor(r, g, b, a);
                buffer.addVertex(matrix, cx + rOut * cosEnd - normalX2, cy + rOut * sinEnd - normalY2, 0.0f).setColor(r, g, b, a);
                buffer.addVertex(matrix, cx + rIn * cosEnd + normalX2, cy + rIn * sinEnd + normalY2, 0.0f).setColor(r, g, b, a);
                buffer.addVertex(matrix, cx + rIn * cosEnd - normalX2, cy + rIn * sinEnd - normalY2, 0.0f).setColor(r, g, b, a);
                BufferUploader.drawWithShader((MeshData)buffer.buildOrThrow());
                RenderSystem.disableBlend();
                return;
            }
            t = (float)i / (float)count;
            angle = startAngle + t * (endAngle - startAngle);
            cos = (float)Math.cos(angle);
            sin = (float)Math.sin(angle);
            buffer.addVertex(matrix, cx + (rIn + halfStroke) * cos, cy + (rIn + halfStroke) * sin, 0.0f).setColor(r, g, b, a);
            buffer.addVertex(matrix, cx + (rIn - halfStroke) * cos, cy + (rIn - halfStroke) * sin, 0.0f).setColor(r, g, b, a);
            ++i;
        }
    }

    public static void drawCircle(Matrix4f matrix, float cx, float cy, float radius, int argb, int steps) {
        float a = (float)(argb >> 24 & 0xFF) / 255.0f;
        float r = (float)(argb >> 16 & 0xFF) / 255.0f;
        float g = (float)(argb >> 8 & 0xFF) / 255.0f;
        float b = (float)(argb & 0xFF) / 255.0f;
        if (a <= 0.0f) {
            return;
        }
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        buffer.addVertex(matrix, cx, cy, 0.0f).setColor(r, g, b, a);
        int count = Math.max(8, steps);
        int i = 0;
        while (true) {
            if (i > count) {
                BufferUploader.drawWithShader((MeshData)buffer.buildOrThrow());
                RenderSystem.disableBlend();
                return;
            }
            float angle = (float)((double)i * 2.0 * Math.PI / (double)count);
            float cos = (float)Math.cos(angle);
            float sin = (float)Math.sin(angle);
            buffer.addVertex(matrix, cx + radius * cos, cy + radius * sin, 0.0f).setColor(r, g, b, a);
            ++i;
        }
    }

    public static void drawCircleOutline(Matrix4f matrix, float cx, float cy, float radius, int argb, float strokeWidth, int steps) {
        float a = (float)(argb >> 24 & 0xFF) / 255.0f;
        float r = (float)(argb >> 16 & 0xFF) / 255.0f;
        float g = (float)(argb >> 8 & 0xFF) / 255.0f;
        float b = (float)(argb & 0xFF) / 255.0f;
        if (a <= 0.0f) return;
        if (strokeWidth <= 0.0f) {
            return;
        }
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        int count = Math.max(8, steps);
        float halfStroke = strokeWidth / 2.0f;
        int i = 0;
        while (true) {
            if (i > count) {
                BufferUploader.drawWithShader((MeshData)buffer.buildOrThrow());
                RenderSystem.disableBlend();
                return;
            }
            float angle = (float)((double)i * 2.0 * Math.PI / (double)count);
            float cos = (float)Math.cos(angle);
            float sin = (float)Math.sin(angle);
            buffer.addVertex(matrix, cx + (radius + halfStroke) * cos, cy + (radius + halfStroke) * sin, 0.0f).setColor(r, g, b, a);
            buffer.addVertex(matrix, cx + (radius - halfStroke) * cos, cy + (radius - halfStroke) * sin, 0.0f).setColor(r, g, b, a);
            ++i;
        }
    }
}

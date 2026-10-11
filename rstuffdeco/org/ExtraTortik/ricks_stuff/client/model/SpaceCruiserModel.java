/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.PoseStack$Pose
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.logging.LogUtils
 *  net.minecraft.resources.ResourceLocation
 */
package org.ExtraTortik.ricks_stuff.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.logging.LogUtils;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class SpaceCruiserModel {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/entity/space_cruiser/space_cruiser.png");
    private static final String MESH_PATH = "/assets/ricks_stuff/models/entity/space_cruiser.mesh";
    private static boolean loaded = false;
    private static float[] bodyVertices;
    private static float[] domeVertices;
    private static int bodyVertexCount;
    private static int domeVertexCount;

    public static synchronized void ensureLoaded() {
        if (loaded) {
            return;
        }
        try (InputStream is = SpaceCruiserModel.class.getResourceAsStream(MESH_PATH);){
            if (is == null) {
                LOGGER.error("Failed to find space cruiser mesh resource: {}", (Object)MESH_PATH);
                return;
            }
            try (BufferedInputStream bis = new BufferedInputStream(is);
                 DataInputStream dis = new DataInputStream(bis);){
                byte[] magic = new byte[4];
                dis.readFully(magic);
                if (magic[0] != 83 || magic[1] != 72 || magic[2] != 73 || magic[3] != 80) {
                    LOGGER.error("Invalid magic header in space cruiser mesh!");
                    return;
                }
                byte[] headerBytes = new byte[12];
                dis.readFully(headerBytes);
                ByteBuffer headerBuf = ByteBuffer.wrap(headerBytes).order(ByteOrder.LITTLE_ENDIAN);
                int version = headerBuf.getInt();
                int bodyQuads = headerBuf.getInt();
                int domeQuads = headerBuf.getInt();
                bodyVertexCount = bodyQuads * 4;
                domeVertexCount = domeQuads * 4;
                int bodyFloats = bodyVertexCount * 8;
                int domeFloats = domeVertexCount * 8;
                byte[] bodyData = new byte[bodyFloats * 4];
                dis.readFully(bodyData);
                ByteBuffer bodyBuf = ByteBuffer.wrap(bodyData).order(ByteOrder.LITTLE_ENDIAN);
                bodyVertices = new float[bodyFloats];
                bodyBuf.asFloatBuffer().get(bodyVertices);
                byte[] domeData = new byte[domeFloats * 4];
                dis.readFully(domeData);
                ByteBuffer domeBuf = ByteBuffer.wrap(domeData).order(ByteOrder.LITTLE_ENDIAN);
                domeVertices = new float[domeFloats];
                domeBuf.asFloatBuffer().get(domeVertices);
                loaded = true;
                LOGGER.info("Loaded Space Cruiser model: {} body quads, {} dome quads", (Object)bodyQuads, (Object)domeQuads);
            }
        }
        catch (Exception e) {
            LOGGER.error("Failed to load space cruiser mesh", e);
        }
    }

    public void renderBody(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        SpaceCruiserModel.ensureLoaded();
        if (!loaded) return;
        if (bodyVertices == null) {
            return;
        }
        this.renderBuffer(bodyVertices, bodyVertexCount, poseStack.last(), buffer, packedLight, packedOverlay, r, g, b, a);
    }

    public void renderDome(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        SpaceCruiserModel.ensureLoaded();
        if (!loaded) return;
        if (domeVertices == null) {
            return;
        }
        this.renderBuffer(domeVertices, domeVertexCount, poseStack.last(), buffer, packedLight, packedOverlay, r, g, b, a);
    }

    private void renderBuffer(float[] vertices, int vertexCount, PoseStack.Pose poseEntry, VertexConsumer buffer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        int i = 0;
        while (i < vertexCount) {
            int offset = i * 8;
            float x = vertices[offset];
            float y = vertices[offset + 1];
            float z = vertices[offset + 2];
            float nx = vertices[offset + 3];
            float ny = vertices[offset + 4];
            float nz = vertices[offset + 5];
            float u = vertices[offset + 6];
            float v = vertices[offset + 7];
            buffer.addVertex(poseEntry, x, y, z).setColor(r, g, b, a).setUv(u, v).setOverlay(packedOverlay).setLight(packedLight).setNormal(poseEntry, nx, ny, nz);
            ++i;
        }
    }

    static {
        bodyVertexCount = 0;
        domeVertexCount = 0;
    }
}

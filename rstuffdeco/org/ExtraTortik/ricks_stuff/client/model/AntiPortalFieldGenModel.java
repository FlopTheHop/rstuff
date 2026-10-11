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

public class AntiPortalFieldGenModel {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/block/anti_portal_field_gen.png");
    private static final String MESH_PATH = "/assets/ricks_stuff/models/entity/anti_portal_field_gen.mesh";
    private static boolean loaded = false;
    private static float[] vertices;
    private static int vertexCount;

    public static synchronized void ensureLoaded() {
        if (loaded) {
            return;
        }
        try (InputStream is = AntiPortalFieldGenModel.class.getResourceAsStream(MESH_PATH);){
            if (is == null) {
                LOGGER.error("Failed to find anti-portal field generator mesh resource: {}", (Object)MESH_PATH);
                return;
            }
            try (BufferedInputStream bis = new BufferedInputStream(is);
                 DataInputStream dis = new DataInputStream(bis);){
                byte[] magic = new byte[4];
                dis.readFully(magic);
                if (magic[0] != 84 || magic[1] != 69 || magic[2] != 83 || magic[3] != 76) {
                    LOGGER.error("Invalid magic header in anti-portal generator mesh!");
                    return;
                }
                byte[] headerBytes = new byte[12];
                dis.readFully(headerBytes);
                ByteBuffer headerBuf = ByteBuffer.wrap(headerBytes).order(ByteOrder.LITTLE_ENDIAN);
                int version = headerBuf.getInt();
                int quads = headerBuf.getInt();
                int extra = headerBuf.getInt();
                vertexCount = quads * 4;
                int totalFloats = vertexCount * 8;
                byte[] data = new byte[totalFloats * 4];
                dis.readFully(data);
                ByteBuffer dataBuf = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
                vertices = new float[totalFloats];
                dataBuf.asFloatBuffer().get(vertices);
                loaded = true;
                LOGGER.info("Loaded Anti-Portal Field Generator model: {} quads (version {})", (Object)quads, (Object)version);
            }
        }
        catch (Exception e) {
            LOGGER.error("Failed to load anti-portal generator mesh", e);
        }
    }

    public void render(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        AntiPortalFieldGenModel.ensureLoaded();
        if (!loaded) return;
        if (vertices == null) {
            return;
        }
        PoseStack.Pose poseEntry = poseStack.last();
        int i = 0;
        while (i < vertexCount) {
            int idx = i * 8;
            float x = vertices[idx];
            float y = vertices[idx + 1];
            float z = vertices[idx + 2];
            float nx = vertices[idx + 3];
            float ny = vertices[idx + 4];
            float nz = vertices[idx + 5];
            float u = vertices[idx + 6];
            float v = vertices[idx + 7];
            buffer.addVertex(poseEntry, x, y, z).setColor(r, g, b, a).setUv(u, v).setOverlay(packedOverlay).setLight(packedLight).setNormal(poseEntry, nx, ny, nz);
            ++i;
        }
    }

    static {
        vertexCount = 0;
    }
}

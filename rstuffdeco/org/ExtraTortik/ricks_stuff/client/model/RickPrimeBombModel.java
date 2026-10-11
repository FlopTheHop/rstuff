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

public class RickPrimeBombModel {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final ResourceLocation TEXTURE_ON = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/entity/bomb/bomb_texture_on.png");
    public static final ResourceLocation TEXTURE_FLASH = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/entity/bomb/bomb_texture_flash.png");
    public static final ResourceLocation TEXTURE_OFF = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/entity/bomb/bomb_texture_off.png");
    private static final String MESH_PATH = "/assets/ricks_stuff/models/entity/rick_prime_bomb.mesh";
    private static boolean loaded = false;
    private static float[] vertices;
    private static int vertexCount;

    public static synchronized void ensureLoaded() {
        if (loaded) {
            return;
        }
        try (InputStream is = RickPrimeBombModel.class.getResourceAsStream(MESH_PATH);){
            if (is == null) {
                LOGGER.error("Failed to find rick prime bomb mesh resource: {}", (Object)MESH_PATH);
                return;
            }
            try (BufferedInputStream bis = new BufferedInputStream(is);
                 DataInputStream dis = new DataInputStream(bis);){
                byte[] magic = new byte[4];
                dis.readFully(magic);
                if (magic[0] != 66 || magic[1] != 79 || magic[2] != 77 || magic[3] != 66) {
                    LOGGER.error("Invalid magic header in rick prime bomb mesh!");
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
                LOGGER.info("Loaded Rick Prime Bomb model: {} quads (version {})", (Object)quads, (Object)version);
            }
        }
        catch (Exception e) {
            LOGGER.error("Failed to load rick prime bomb mesh", e);
        }
    }

    public void render(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        RickPrimeBombModel.ensureLoaded();
        if (!loaded) return;
        if (vertices == null) {
            return;
        }
        PoseStack.Pose poseEntry = poseStack.last();
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
        vertexCount = 0;
    }
}

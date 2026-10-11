/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.logging.LogUtils
 *  net.minecraft.resources.ResourceLocation
 *  org.joml.Matrix3f
 *  org.joml.Matrix4f
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
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.slf4j.Logger;

public class OmegaDeviceModel {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final ResourceLocation TEXTURE;
    public static final ResourceLocation TEXTURE_ARMOR;
    public static final ResourceLocation TEXTURE_CAGE;
    public static final ResourceLocation TEXTURE_EMISSIVE;
    public static final ResourceLocation TEXTURE_PLASMA_CORE;
    public static final ResourceLocation TEXTURE_ENERGY_BEAM;
    private static final String MESH_PATH = "/assets/ricks_stuff/models/entity/omega_device.mesh";
    private static boolean loaded;
    private static float[] vertices;
    private static int vertexCount;

    public static synchronized void ensureLoaded() {
        if (loaded) {
            return;
        }
        try (InputStream is = OmegaDeviceModel.class.getResourceAsStream(MESH_PATH);){
            if (is == null) {
                LOGGER.error("Failed to find omega device mesh resource: {}", (Object)MESH_PATH);
                return;
            }
            try (BufferedInputStream bis = new BufferedInputStream(is);
                 DataInputStream dis = new DataInputStream(bis);){
                byte[] magic = new byte[4];
                dis.readFully(magic);
                if (magic[0] != 79 || magic[1] != 77 || magic[2] != 69 || magic[3] != 71) {
                    LOGGER.error("Invalid magic header in omega device mesh!");
                    return;
                }
                byte[] headerBytes = new byte[12];
                dis.readFully(headerBytes);
                ByteBuffer headerBuf = ByteBuffer.wrap(headerBytes).order(ByteOrder.LITTLE_ENDIAN);
                int version = headerBuf.getInt();
                int quads = headerBuf.getInt();
                vertexCount = quads * 4;
                int totalFloats = vertexCount * 8;
                byte[] data = new byte[totalFloats * 4];
                dis.readFully(data);
                ByteBuffer buf = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
                vertices = new float[totalFloats];
                buf.asFloatBuffer().get(vertices);
                loaded = true;
                LOGGER.info("Loaded Omega Device model: {} quads", (Object)quads);
            }
        }
        catch (Exception e) {
            LOGGER.error("Failed to load omega device mesh", e);
        }
    }

    public void render(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        OmegaDeviceModel.ensureLoaded();
        if (!loaded) return;
        if (vertices == null) {
            return;
        }
        Matrix4f pose = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();
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
            buffer.addVertex(poseStack.last(), x, y, z).setColor(r, g, b, a).setUv(u, v).setOverlay(packedOverlay).setLight(packedLight).setNormal(poseStack.last(), nx, ny, nz);
            ++i;
        }
    }

    static {
        TEXTURE_ARMOR = TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/entity/omega_device/omega_device.png");
        TEXTURE_CAGE = TEXTURE;
        TEXTURE_EMISSIVE = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/entity/omega_device/omega_emissive_mask.png");
        TEXTURE_PLASMA_CORE = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/entity/omega_device/omega_plasma_core.png");
        TEXTURE_ENERGY_BEAM = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/entity/omega_device/omega_energy_beam.png");
        loaded = false;
        vertexCount = 0;
    }
}

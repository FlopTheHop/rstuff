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
 *  org.joml.Vector3f
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
import org.joml.Vector3f;
import org.slf4j.Logger;

public class EvilMortyGunModel {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/entity/evil_morty_portal_gun/evil_morty_portal_gun.png");
    public static final ResourceLocation ITEM_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/item/evil_morty_portal_gun.png");
    private static final String MESH_PATH = "/assets/ricks_stuff/models/entity/evil_morty_portal_gun.mesh";
    public static final Vector3f TUBE_SOCKET_CENTER = new Vector3f(-0.03125f, 0.4375f, 0.15f);
    public static final float TUBE_SOCKET_ROT_X = -31.0f;
    public static final Vector3f LIGHT_ORB_CENTER = new Vector3f(0.0f, 0.38125f, 0.39375f);
    public static final float LIGHT_ORB_RADIUS = 0.08125f;
    private static boolean loaded = false;
    private static float[] chassisVertices;
    private static float[] lightVertices;
    private static int chassisVertexCount;
    private static int lightVertexCount;

    public static synchronized void ensureLoaded() {
        if (loaded) {
            return;
        }
        try (InputStream is = EvilMortyGunModel.class.getResourceAsStream(MESH_PATH);){
            if (is == null) {
                LOGGER.error("Failed to find evil morty portal gun mesh resource: {}", (Object)MESH_PATH);
                return;
            }
            try (BufferedInputStream bis = new BufferedInputStream(is);
                 DataInputStream dis = new DataInputStream(bis);){
                boolean validMagic;
                byte[] magic = new byte[4];
                dis.readFully(magic);
                boolean bl = validMagic = magic[0] == 69 && magic[1] == 77 && magic[2] == 80 && magic[3] == 71 || magic[0] == 69 && magic[1] == 86 && magic[2] == 73 && magic[3] == 76;
                if (!validMagic) {
                    LOGGER.error("Invalid magic header in evil morty portal gun mesh!");
                    return;
                }
                byte[] headerBytes = new byte[12];
                dis.readFully(headerBytes);
                ByteBuffer headerBuf = ByteBuffer.wrap(headerBytes).order(ByteOrder.LITTLE_ENDIAN);
                int version = headerBuf.getInt();
                int chassisQuads = headerBuf.getInt();
                int lightQuads = headerBuf.getInt();
                chassisVertexCount = chassisQuads * 4;
                lightVertexCount = lightQuads * 4;
                int chassisFloats = chassisVertexCount * 8;
                int lightFloats = lightVertexCount * 8;
                if (chassisFloats > 0) {
                    byte[] chassisData = new byte[chassisFloats * 4];
                    dis.readFully(chassisData);
                    ByteBuffer chassisBuf = ByteBuffer.wrap(chassisData).order(ByteOrder.LITTLE_ENDIAN);
                    chassisVertices = new float[chassisFloats];
                    chassisBuf.asFloatBuffer().get(chassisVertices);
                }
                if (lightFloats > 0) {
                    byte[] lightData = new byte[lightFloats * 4];
                    dis.readFully(lightData);
                    ByteBuffer lightBuf = ByteBuffer.wrap(lightData).order(ByteOrder.LITTLE_ENDIAN);
                    lightVertices = new float[lightFloats];
                    lightBuf.asFloatBuffer().get(lightVertices);
                }
                loaded = true;
                LOGGER.info("Loaded Evil Morty Portal Gun model: {} chassis quads, {} light quads (version {})", chassisQuads, lightQuads, version);
            }
        }
        catch (Exception e) {
            LOGGER.error("Failed to load evil morty portal gun mesh", e);
        }
    }

    public static boolean isLoaded() {
        return loaded;
    }

    public static float[] getChassisVertices() {
        EvilMortyGunModel.ensureLoaded();
        return chassisVertices;
    }

    public static int getChassisVertexCount() {
        EvilMortyGunModel.ensureLoaded();
        return chassisVertexCount;
    }

    public static float[] getLightVertices() {
        EvilMortyGunModel.ensureLoaded();
        return lightVertices;
    }

    public static int getLightVertexCount() {
        EvilMortyGunModel.ensureLoaded();
        return lightVertexCount;
    }

    public static void renderChassis(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        EvilMortyGunModel.ensureLoaded();
        if (!loaded) return;
        if (chassisVertices == null) {
            return;
        }
        Matrix4f pose = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();
        EvilMortyGunModel.renderBuffer(chassisVertices, chassisVertexCount, pose, normal, buffer, packedLight, packedOverlay, r, g, b, a);
    }

    public static void renderLightOrb(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        EvilMortyGunModel.ensureLoaded();
        if (!loaded) return;
        if (lightVertices == null) {
            return;
        }
        Matrix4f pose = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();
        EvilMortyGunModel.renderBuffer(lightVertices, lightVertexCount, pose, normal, buffer, packedLight, packedOverlay, r, g, b, a);
    }

    public static void renderGlowingSpherePosColor(PoseStack poseStack, VertexConsumer buffer, float scale, int r, int g, int b, int a) {
        EvilMortyGunModel.ensureLoaded();
        if (!loaded) return;
        if (lightVertices == null) {
            return;
        }
        Matrix4f pose = poseStack.last().pose();
        int i = 0;
        while (i < lightVertexCount) {
            int offset = i * 8;
            float x = (lightVertices[offset] - EvilMortyGunModel.LIGHT_ORB_CENTER.x) * scale + EvilMortyGunModel.LIGHT_ORB_CENTER.x;
            float y = (lightVertices[offset + 1] - EvilMortyGunModel.LIGHT_ORB_CENTER.y) * scale + EvilMortyGunModel.LIGHT_ORB_CENTER.y;
            float z = (lightVertices[offset + 2] - EvilMortyGunModel.LIGHT_ORB_CENTER.z) * scale + EvilMortyGunModel.LIGHT_ORB_CENTER.z;
            buffer.addVertex(pose, x, y, z).setColor(r, g, b, a);
            ++i;
        }
    }

    public static void render(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        EvilMortyGunModel.renderChassis(poseStack, buffer, packedLight, packedOverlay, r, g, b, a);
    }

    public static void render(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay) {
        EvilMortyGunModel.renderChassis(poseStack, buffer, packedLight, packedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
    }

    private static void renderBuffer(float[] vertices, int vertexCount, Matrix4f pose, Matrix3f normal, VertexConsumer buffer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        if (r > 1.0f || g > 1.0f || b > 1.0f || a > 1.0f) {
            r /= 255.0f;
            g /= 255.0f;
            b /= 255.0f;
            a /= 255.0f;
        }
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
            float tnx = normal.m00 * nx + normal.m10 * ny + normal.m20 * nz;
            float tny = normal.m01 * nx + normal.m11 * ny + normal.m21 * nz;
            float tnz = normal.m02 * nx + normal.m12 * ny + normal.m22 * nz;
            buffer.addVertex(pose, x, y, z).setColor(r, g, b, a).setUv(u, v).setOverlay(packedOverlay).setLight(packedLight).setNormal(tnx, tny, tnz);
            ++i;
        }
    }

    static {
        chassisVertexCount = 0;
        lightVertexCount = 0;
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.logging.LogUtils
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.phys.Vec3
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceCollider$QuadCollider
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity
 */
package org.ExtraTortik.ricks_stuff.entity.omega;

import com.mojang.logging.LogUtils;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceCollider;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity;
import org.slf4j.Logger;

public class OmegaDeviceCollider {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String MESH_PATH = "/assets/ricks_stuff/models/entity/omega_device.mesh";
    private static boolean initialized = false;
    private static final List<QuadCollider> WALKABLE_QUADS = new ArrayList<QuadCollider>();

    public static synchronized void ensureInitialized() {
        if (initialized) {
            return;
        }
        try (InputStream is = OmegaDeviceCollider.class.getResourceAsStream(MESH_PATH);){
            if (is == null) {
                LOGGER.error("Failed to find mesh for collider: {}", (Object)MESH_PATH);
                return;
            }
            try (BufferedInputStream bis = new BufferedInputStream(is);
                 DataInputStream dis = new DataInputStream(bis);){
                byte[] magic = new byte[4];
                dis.readFully(magic);
                if (magic[0] != 79 || magic[1] != 77 || magic[2] != 69 || magic[3] != 71) {
                    LOGGER.error("Invalid magic header in omega device collider mesh!");
                    return;
                }
                byte[] headerBytes = new byte[12];
                dis.readFully(headerBytes);
                ByteBuffer headerBuf = ByteBuffer.wrap(headerBytes).order(ByteOrder.LITTLE_ENDIAN);
                int version = headerBuf.getInt();
                int quadsCount = headerBuf.getInt();
                int floatsPerQuad = 32;
                byte[] quadData = new byte[floatsPerQuad * 4];
                for (int q = 0; q < quadsCount; ++q) {
                    dis.readFully(quadData);
                    ByteBuffer quadBuf = ByteBuffer.wrap(quadData).order(ByteOrder.LITTLE_ENDIAN);
                    float[] quadFloats = new float[floatsPerQuad];
                    quadBuf.asFloatBuffer().get(quadFloats);
                    QuadCollider collider = new QuadCollider(quadFloats);
                    WALKABLE_QUADS.add(collider);
                }
                initialized = true;
                LOGGER.info("Initialized Omega Device Collider with {} quad colliders", (Object)WALKABLE_QUADS.size());
            }
        }
        catch (Exception e) {
            LOGGER.error("Failed to load omega device collider", e);
        }
    }

    public static Double getGroundHeightAt(double relX, double relY, double relZ) {
        double[][] sampleOffsets;
        if (!Config.omegaDeviceModelEnabled) {
            return null;
        }
        OmegaDeviceCollider.ensureInitialized();
        if (WALKABLE_QUADS.isEmpty()) {
            return null;
        }
        double bestY = -9999.0;
        double[][] dArrayArray = sampleOffsets = new double[][]{{0.0, 0.0}, {0.25, 0.25}, {-0.25, 0.25}, {0.25, -0.25}, {-0.25, -0.25}};
        int n = dArrayArray.length;
        int n2 = 0;
        while (n2 < n) {
            double[] offset = dArrayArray[n2];
            double sx = relX + offset[0];
            double sz = relZ + offset[1];
            for (QuadCollider quad : WALKABLE_QUADS) {
                double surfaceY;
                if (sx < (double)quad.minX || sx > (double)quad.maxX || sz < (double)quad.minZ || sz > (double)quad.maxZ || (double)quad.maxY > relY + 2.5 || (double)quad.minY < relY - 4.0 || !quad.containsPoint2D(sx, sz) || !((surfaceY = quad.getSurfaceHeight(sx, sz)) > bestY) || !(surfaceY <= relY + 0.85)) continue;
                bestY = surfaceY;
            }
            ++n2;
        }
        return bestY > -9000.0 ? Double.valueOf(bestY) : null;
    }

    public static void handleEntityCollision(LivingEntity entity, OmegaDeviceEntity device) {
        if (!Config.omegaDeviceModelEnabled) {
            return;
        }
        if (entity == null) return;
        if (device == null) return;
        if (!entity.isAlive()) return;
        if (!device.isAlive()) {
            return;
        }
        if (entity.isSpectator()) {
            return;
        }
        double relX = entity.getX() - device.getX();
        double relY = entity.getY() - device.getY();
        double relZ = entity.getZ() - device.getZ();
        double distXZSq = relX * relX + relZ * relZ;
        if (distXZSq > 1024.0) return;
        if (Math.abs(relY) > 32.0) {
            return;
        }
        Double groundY = OmegaDeviceCollider.getGroundHeightAt(relX, relY, relZ);
        if (groundY == null) return;
        double targetWorldY = device.getY() + groundY;
        double currentY = entity.getY();
        double diffY = targetWorldY - currentY;
        Vec3 motion = entity.getDeltaMovement();
        if (diffY > 0.001 && diffY <= 0.85) {
            entity.setPos(entity.getX(), targetWorldY, entity.getZ());
            if (motion.y < 0.0) {
                entity.setDeltaMovement(motion.x, 0.0, motion.z);
            }
            entity.fallDistance = 0.0f;
            entity.setOnGround(true);
        } else {
            if (!(diffY >= -0.65)) return;
            if (!(diffY <= 0.001)) return;
            if (!(motion.y <= 0.01)) return;
            entity.setPos(entity.getX(), targetWorldY, entity.getZ());
            if (motion.y < 0.0) {
                entity.setDeltaMovement(motion.x, 0.0, motion.z);
            }
            entity.fallDistance = 0.0f;
            entity.setOnGround(true);
        }
    }

    private static boolean pointInTriangle2D(double px, double pz, double ax, double az, double bx, double bz, double cx, double cz) {
        double d1 = (px - bx) * (az - bz) - (ax - bx) * (pz - bz);
        double d2 = (px - cx) * (bz - cz) - (bx - cx) * (pz - cz);
        double d3 = (px - ax) * (cz - az) - (cx - ax) * (pz - az);
        boolean hasNeg = d1 < -1.0E-4 || d2 < -1.0E-4 || d3 < -1.0E-4;
        boolean hasPos = d1 > 1.0E-4 || d2 > 1.0E-4 || d3 > 1.0E-4;
        return !hasNeg || !hasPos;
    }
}

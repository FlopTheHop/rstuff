/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.phys.Vec3
 */
package org.ExtraTortik.ricks_stuff.portal;

import net.minecraft.world.phys.Vec3;

public static class PortalPlacementHelper.Placement {
    public final Vec3 pos;
    public final int orientation;
    public final int rotation;
    public final float yaw;

    public PortalPlacementHelper.Placement(Vec3 pos, int orientation, int rotation, float yaw) {
        this.pos = pos;
        this.orientation = orientation;
        this.rotation = rotation;
        this.yaw = yaw;
    }

    public Vec3 getPos() {
        return this.pos;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public int getRotation() {
        return this.rotation;
    }

    public float getYaw() {
        return this.yaw;
    }
}

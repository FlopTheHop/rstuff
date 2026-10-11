/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceCollider
 */
package org.ExtraTortik.ricks_stuff.entity.omega;

import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceCollider;

/*
 * Exception performing whole class analysis ignored.
 */
public static class OmegaDeviceCollider.QuadCollider {
    public final float x0;
    public final float y0;
    public final float z0;
    public final float x1;
    public final float y1;
    public final float z1;
    public final float x2;
    public final float y2;
    public final float z2;
    public final float x3;
    public final float y3;
    public final float z3;
    public final float minX;
    public final float maxX;
    public final float minZ;
    public final float maxZ;
    public final float minY;
    public final float maxY;
    public final float nx;
    public final float ny;
    public final float nz;

    public OmegaDeviceCollider.QuadCollider(float[] v) {
        this.x0 = v[0];
        this.y0 = v[1];
        this.z0 = v[2];
        this.x1 = v[8];
        this.y1 = v[9];
        this.z1 = v[10];
        this.x2 = v[16];
        this.y2 = v[17];
        this.z2 = v[18];
        this.x3 = v[24];
        this.y3 = v[25];
        this.z3 = v[26];
        float padding = 0.35f;
        this.minX = Math.min(Math.min(this.x0, this.x1), Math.min(this.x2, this.x3)) - padding;
        this.maxX = Math.max(Math.max(this.x0, this.x1), Math.max(this.x2, this.x3)) + padding;
        this.minZ = Math.min(Math.min(this.z0, this.z1), Math.min(this.z2, this.z3)) - padding;
        this.maxZ = Math.max(Math.max(this.z0, this.z1), Math.max(this.z2, this.z3)) + padding;
        this.minY = Math.min(Math.min(this.y0, this.y1), Math.min(this.y2, this.y3));
        this.maxY = Math.max(Math.max(this.y0, this.y1), Math.max(this.y2, this.y3));
        float e1x = this.x1 - this.x0;
        float e1y = this.y1 - this.y0;
        float e1z = this.z1 - this.z0;
        float e2x = this.x2 - this.x0;
        float e2y = this.y2 - this.y0;
        float e2z = this.z2 - this.z0;
        this.nx = e1y * e2z - e1z * e2y;
        this.ny = e1z * e2x - e1x * e2z;
        this.nz = e1x * e2y - e1y * e2x;
    }

    public boolean containsPoint2D(double px, double pz) {
        return OmegaDeviceCollider.pointInTriangle2D((double)px, (double)pz, (double)this.x0, (double)this.z0, (double)this.x1, (double)this.z1, (double)this.x2, (double)this.z2) || OmegaDeviceCollider.pointInTriangle2D((double)px, (double)pz, (double)this.x0, (double)this.z0, (double)this.x2, (double)this.z2, (double)this.x3, (double)this.z3);
    }

    public double getSurfaceHeight(double px, double pz) {
        if (!(Math.abs(this.ny) > 1.0E-4f)) return this.maxY;
        return (double)this.y0 - ((double)this.nx * (px - (double)this.x0) + (double)this.nz * (pz - (double)this.z0)) / (double)this.ny;
    }
}

/*
 * Decompiled with CFR 0.152.
 */
package org.ExtraTortik.ricks_stuff.client.renderer;

private static class RealityCrackRenderer.ShardData {
    final float x0;
    final float y0;
    final float z0;
    final float nx;
    final float ny;
    final float speed;
    final float fwdSpeed;
    final float seedX;
    final float seedY;
    final float seedZ;
    final float[][] verts2D;
    final float thickness;
    final float faceR;
    final float faceG;
    final float faceB;
    final float edgeR;
    final float edgeG;
    final float edgeB;
    final float edgeWidth;

    RealityCrackRenderer.ShardData(float x0, float y0, float z0, float nx, float ny, float speed, float fwdSpeed, float seedX, float seedY, float seedZ, float[][] verts2D, float thickness, float faceR, float faceG, float faceB, float edgeR, float edgeG, float edgeB, float edgeWidth) {
        this.x0 = x0;
        this.y0 = y0;
        this.z0 = z0;
        this.nx = nx;
        this.ny = ny;
        this.speed = speed;
        this.fwdSpeed = fwdSpeed;
        this.seedX = seedX;
        this.seedY = seedY;
        this.seedZ = seedZ;
        this.verts2D = verts2D;
        this.thickness = thickness;
        this.faceR = faceR;
        this.faceG = faceG;
        this.faceB = faceB;
        this.edgeR = edgeR;
        this.edgeG = edgeG;
        this.edgeB = edgeB;
        this.edgeWidth = edgeWidth;
    }
}

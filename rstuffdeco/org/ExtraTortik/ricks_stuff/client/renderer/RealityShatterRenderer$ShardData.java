/*
 * Decompiled with CFR 0.152.
 */
package org.ExtraTortik.ricks_stuff.client.renderer;

private static class RealityShatterRenderer.ShardData {
    final float initX;
    final float initY;
    final float initZ;
    final float radDirX;
    final float radDirY;
    final float radialSpeed;
    final float forwardSpeed;
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

    RealityShatterRenderer.ShardData(float initX, float initY, float initZ, float radDirX, float radDirY, float radialSpeed, float forwardSpeed, float seedX, float seedY, float seedZ, float[][] verts2D, float thickness, float faceR, float faceG, float faceB, float edgeR, float edgeG, float edgeB, float edgeWidth) {
        this.initX = initX;
        this.initY = initY;
        this.initZ = initZ;
        this.radDirX = radDirX;
        this.radDirY = radDirY;
        this.radialSpeed = radialSpeed;
        this.forwardSpeed = forwardSpeed;
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

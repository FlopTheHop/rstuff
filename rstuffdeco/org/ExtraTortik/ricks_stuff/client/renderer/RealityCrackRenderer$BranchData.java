/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.ExtraTortik.ricks_stuff.client.renderer.RealityCrackRenderer$SubBranchData
 */
package org.ExtraTortik.ricks_stuff.client.renderer;

import org.ExtraTortik.ricks_stuff.client.renderer.RealityCrackRenderer;

private static class RealityCrackRenderer.BranchData {
    final float[][] points;
    final float[] activationThresholds;
    final RealityCrackRenderer.SubBranchData[] subBranches;

    RealityCrackRenderer.BranchData(float[][] points, float[] activationThresholds, RealityCrackRenderer.SubBranchData[] subBranches) {
        this.points = points;
        this.activationThresholds = activationThresholds;
        this.subBranches = subBranches;
    }
}

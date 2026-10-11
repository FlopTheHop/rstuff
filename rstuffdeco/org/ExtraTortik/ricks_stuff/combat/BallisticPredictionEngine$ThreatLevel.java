/*
 * Decompiled with CFR 0.152.
 */
package org.ExtraTortik.ricks_stuff.combat;

public static enum BallisticPredictionEngine.ThreatLevel {
    CRITICAL(-58880, 1.0f),
    WARNING(-30720, 0.85f),
    PASS(-2171, 0.65f);

    private final int colorRgba;
    private final float alpha;

    private BallisticPredictionEngine.ThreatLevel(int colorRgba, float alpha) {
        this.colorRgba = colorRgba;
        this.alpha = alpha;
    }

    public int getColorRgba() {
        return this.colorRgba;
    }

    public float getAlpha() {
        return this.alpha;
    }
}

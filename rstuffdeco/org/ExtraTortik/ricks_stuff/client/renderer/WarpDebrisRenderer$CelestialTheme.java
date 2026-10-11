/*
 * Decompiled with CFR 0.152.
 */
package org.ExtraTortik.ricks_stuff.client.renderer;

public static enum WarpDebrisRenderer.CelestialTheme {
    TERRESTRIAL(0),
    MARS_CRIMSON(1),
    GAS_GIANT_RINGS(2),
    TOXIC_RADIOACTIVE(3),
    PURPLE_EXOPLANET(4),
    MOLTEN_VOLCANIC(5),
    ICE_EUROPA(6),
    ASTEROID_CLUSTER(7);

    private final int id;

    private WarpDebrisRenderer.CelestialTheme(int id) {
        this.id = id;
    }

    public static WarpDebrisRenderer.CelestialTheme fromId(int id) {
        WarpDebrisRenderer.CelestialTheme[] values = WarpDebrisRenderer.CelestialTheme.values();
        return values[Math.floorMod(id, values.length)];
    }
}

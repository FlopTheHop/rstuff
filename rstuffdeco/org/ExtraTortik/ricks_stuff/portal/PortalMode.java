/*
 * Decompiled with CFR 0.152.
 */
package org.ExtraTortik.ricks_stuff.portal;

import java.util.Locale;

public enum PortalMode {
    FIFO("FIFO", "Cycle"),
    LIFO("LIFO", "Override"),
    MULTI_PAIR("Multi-Pair", "Multi-Pairs"),
    ROOT("Root", "Base Hub"),
    CUSTOM("CUSTOM", "GPS Target");

    private final String name;
    private final String displayName;

    private PortalMode(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public PortalMode next() {
        PortalMode[] values = PortalMode.values();
        return values[(this.ordinal() + 1) % values.length];
    }

    public PortalMode previous() {
        PortalMode[] values = PortalMode.values();
        return values[(this.ordinal() - 1 + values.length) % values.length];
    }

    public static PortalMode fromString(String name) {
        if (name == null) return FIFO;
        if (name.isEmpty()) {
            return FIFO;
        }
        String clean = name.trim().toUpperCase(Locale.ROOT).replace("-", "_").replace(" ", "_");
        if (clean.equals("CYCLE")) return FIFO;
        if (clean.equals("CYCLE_MODE")) return FIFO;
        if (clean.equals("FIFO")) {
            return FIFO;
        }
        if (clean.equals("OVERRIDE")) return LIFO;
        if (clean.equals("OVERRIDE_MODE")) return LIFO;
        if (clean.equals("LIFO")) {
            return LIFO;
        }
        if (clean.equals("MULTI_PAIR")) return MULTI_PAIR;
        if (clean.equals("MULTI_PAIRS")) return MULTI_PAIR;
        if (clean.equals("MULTIPAIR")) return MULTI_PAIR;
        if (clean.equals("MULTIPAIRS")) {
            return MULTI_PAIR;
        }
        if (clean.equals("ROOT")) return ROOT;
        if (clean.equals("HUB")) return ROOT;
        if (clean.equals("BASE_HUB")) {
            return ROOT;
        }
        if (clean.equals("CUSTOM")) return CUSTOM;
        if (clean.equals("GPS")) return CUSTOM;
        if (clean.equals("GPS_TARGET")) return CUSTOM;
        if (clean.equals("TARGET")) {
            return CUSTOM;
        }
        PortalMode[] portalModeArray = PortalMode.values();
        int n = portalModeArray.length;
        int n2 = 0;
        while (n2 < n) {
            PortalMode mode = portalModeArray[n2];
            if (mode.name().equalsIgnoreCase(clean)) return mode;
            if (mode.name.equalsIgnoreCase(name.trim())) return mode;
            if (mode.displayName.equalsIgnoreCase(name.trim())) {
                return mode;
            }
            ++n2;
        }
        return FIFO;
    }
}

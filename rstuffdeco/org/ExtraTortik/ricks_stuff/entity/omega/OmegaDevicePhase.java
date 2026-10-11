/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.MatchException
 */
package org.ExtraTortik.ricks_stuff.entity.omega;

public enum OmegaDevicePhase {
    IDLE(0, 0, false, 58879),
    ARMED(1, 0, true, 12087551),
    TARGETING(2, 40, false, 3800852),
    SPINUP(3, 50, true, 3800852),
    POWER_PUMP(4, 60, true, 11403055),
    ERASING(5, 88, true, 3800852),
    COOLDOWN(6, 40, false, 58879);

    private final int id;
    private final int durationTicks;
    private final boolean activePower;
    private final int primaryGlowColor;

    private OmegaDevicePhase(int id, int durationTicks, boolean activePower, int primaryGlowColor) {
        this.id = id;
        this.durationTicks = durationTicks;
        this.activePower = activePower;
        this.primaryGlowColor = primaryGlowColor;
    }

    public int getId() {
        return this.id;
    }

    public int getDurationTicks() {
        return this.durationTicks;
    }

    public boolean isActivePower() {
        return this.activePower;
    }

    public int getPrimaryGlowColor() {
        return this.primaryGlowColor;
    }

    public OmegaDevicePhase getNextPhase() {
        return switch (this.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> ARMED;
            case 1 -> ERASING;
            case 2 -> SPINUP;
            case 3 -> POWER_PUMP;
            case 4 -> ERASING;
            case 5 -> COOLDOWN;
            case 6 -> IDLE;
        };
    }

    public static OmegaDevicePhase byId(int id) {
        OmegaDevicePhase[] omegaDevicePhaseArray = OmegaDevicePhase.values();
        int n = omegaDevicePhaseArray.length;
        int n2 = 0;
        while (n2 < n) {
            OmegaDevicePhase phase = omegaDevicePhaseArray[n2];
            if (phase.id == id) {
                return phase;
            }
            ++n2;
        }
        return IDLE;
    }
}

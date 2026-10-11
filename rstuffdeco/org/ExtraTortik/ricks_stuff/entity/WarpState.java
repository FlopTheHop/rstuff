/*
 * Decompiled with CFR 0.152.
 */
package org.ExtraTortik.ricks_stuff.entity;

public enum WarpState {
    IDLE(0, 0),
    CHARGING(1, 50),
    RIFT_OPENING(2, 30),
    ENTERING(3, 20),
    TUNNEL_TRANSIT(4, 100),
    EXITING(5, 30);

    private final int id;
    private final int durationTicks;

    private WarpState(int id, int durationTicks) {
        this.id = id;
        this.durationTicks = durationTicks;
    }

    public int getId() {
        return this.id;
    }

    public int getDurationTicks() {
        return this.durationTicks;
    }

    public boolean isWarping() {
        return this != IDLE;
    }

    public boolean isControlsLocked() {
        return this != IDLE;
    }

    public boolean isTunnel() {
        return this == TUNNEL_TRANSIT;
    }

    public static WarpState fromId(int id) {
        WarpState[] warpStateArray = WarpState.values();
        int n = warpStateArray.length;
        int n2 = 0;
        while (n2 < n) {
            WarpState state = warpStateArray[n2];
            if (state.id == id) {
                return state;
            }
            ++n2;
        }
        return IDLE;
    }
}

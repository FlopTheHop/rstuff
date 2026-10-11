/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.phys.Vec3
 *  org.ExtraTortik.ricks_stuff.combat.BallisticPredictionEngine$ThreatLevel
 */
package org.ExtraTortik.ricks_stuff.combat;

import java.util.List;
import net.minecraft.world.phys.Vec3;
import org.ExtraTortik.ricks_stuff.combat.BallisticPredictionEngine;

public static class BallisticPredictionEngine.Trajectory {
    private final int entityId;
    private final List<Vec3> points;
    private final Vec3 impactPoint;
    private final BallisticPredictionEngine.ThreatLevel threatLevel;
    private final float closestDistance;

    public BallisticPredictionEngine.Trajectory(int entityId, List<Vec3> points, Vec3 impactPoint, BallisticPredictionEngine.ThreatLevel threatLevel, float closestDistance) {
        this.entityId = entityId;
        this.points = points;
        this.impactPoint = impactPoint;
        this.threatLevel = threatLevel;
        this.closestDistance = closestDistance;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public List<Vec3> getPoints() {
        return this.points;
    }

    public Vec3 getImpactPoint() {
        return this.impactPoint;
    }

    public BallisticPredictionEngine.ThreatLevel getThreatLevel() {
        return this.threatLevel;
    }

    public float getClosestDistance() {
        return this.closestDistance;
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.phys.Vec3
 */
package org.ExtraTortik.ricks_stuff.client.renderer;

import net.minecraft.world.phys.Vec3;

public static class EvilMortyShieldClientManager.ShieldInstance {
    private final int entityId;
    private final Vec3 impactPos;
    private final Vec3 impactDir;
    private final boolean isBreak;
    private int age;
    public static final int MAX_AGE = 25;

    public EvilMortyShieldClientManager.ShieldInstance(int entityId, Vec3 impactPos, Vec3 impactDir, boolean isBreak) {
        this.entityId = entityId;
        this.impactPos = impactPos;
        this.impactDir = impactDir;
        this.isBreak = isBreak;
        this.age = 0;
    }

    public EvilMortyShieldClientManager.ShieldInstance(int entityId, Vec3 impactPos, Vec3 impactDir) {
        this(entityId, impactPos, impactDir, false);
    }

    public boolean tickAndCheckExpired() {
        ++this.age;
        return this.age >= 25;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public Vec3 getImpactPos() {
        return this.impactPos;
    }

    public Vec3 getImpactDir() {
        return this.impactDir;
    }

    public boolean isBreak() {
        return this.isBreak;
    }

    public int getAge() {
        return this.age;
    }

    public int getMaxAge() {
        return 25;
    }
}

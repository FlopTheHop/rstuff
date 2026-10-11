/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.util.Mth
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.Vec3
 */
package org.ExtraTortik.ricks_stuff.client.renderer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public static class EvilMortyShieldClientManager.SparkInstance {
    private double x;
    private double y;
    private double z;
    private double xo;
    private double yo;
    private double zo;
    private double vx;
    private double vy;
    private double vz;
    private int age;
    private final int maxAge;
    private final List<Vec3> trailHistory = new CopyOnWriteArrayList<Vec3>();
    private final boolean isBreak;

    public EvilMortyShieldClientManager.SparkInstance(double x, double y, double z, double vx, double vy, double vz, int maxAge, boolean isBreak) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
        this.age = 0;
        this.maxAge = maxAge;
        this.isBreak = isBreak;
    }

    public EvilMortyShieldClientManager.SparkInstance(double x, double y, double z, double vx, double vy, double vz, int maxAge) {
        this(x, y, z, vx, vy, vz, maxAge, false);
    }

    public boolean tick(Level level) {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.trailHistory.add(0, new Vec3(this.x, this.y, this.z));
        while (this.trailHistory.size() > 6) {
            this.trailHistory.remove(this.trailHistory.size() - 1);
        }
        this.vy -= 0.042;
        this.vx *= 0.96;
        this.vy *= 0.96;
        this.vz *= 0.96;
        double nx = this.x + this.vx;
        double ny = this.y + this.vy;
        double nz = this.z + this.vz;
        if (level != null) {
            BlockState stateCorner;
            BlockPos posCorner;
            BlockState stateZ;
            BlockPos posZ;
            BlockState stateX;
            BlockPos posX;
            BlockState stateY;
            BlockPos posY = BlockPos.containing((double)this.x, (double)ny, (double)this.z);
            if (level.hasChunkAt(posY) && !(stateY = level.getBlockState(posY)).isAir() && !stateY.getCollisionShape((BlockGetter)level, posY).isEmpty()) {
                this.vy = -this.vy * 0.4;
                this.vx *= 0.65;
                this.vz *= 0.65;
                ny = this.y;
            }
            if (level.hasChunkAt(posX = BlockPos.containing((double)nx, (double)ny, (double)this.z)) && !(stateX = level.getBlockState(posX)).isAir() && !stateX.getCollisionShape((BlockGetter)level, posX).isEmpty()) {
                this.vx = -this.vx * 0.4;
                this.vy *= 0.65;
                this.vz *= 0.65;
                nx = this.x;
            }
            if (level.hasChunkAt(posZ = BlockPos.containing((double)nx, (double)ny, (double)nz)) && !(stateZ = level.getBlockState(posZ)).isAir() && !stateZ.getCollisionShape((BlockGetter)level, posZ).isEmpty()) {
                this.vz = -this.vz * 0.4;
                this.vx *= 0.65;
                this.vz *= 0.65;
                nz = this.z;
            }
            if (level.hasChunkAt(posCorner = BlockPos.containing((double)nx, (double)ny, (double)nz)) && !(stateCorner = level.getBlockState(posCorner)).isAir() && !stateCorner.getCollisionShape((BlockGetter)level, posCorner).isEmpty()) {
                this.vx = -this.vx * 0.4;
                this.vy = -this.vy * 0.4;
                this.vz = -this.vz * 0.4;
                nx = this.x;
                ny = this.y;
                nz = this.z;
            }
        }
        this.x = nx;
        this.y = ny;
        this.z = nz;
        ++this.age;
        return this.age >= this.maxAge;
    }

    public double getX(float pt) {
        return Mth.lerp((double)pt, (double)this.xo, (double)this.x);
    }

    public double getY(float pt) {
        return Mth.lerp((double)pt, (double)this.yo, (double)this.y);
    }

    public double getZ(float pt) {
        return Mth.lerp((double)pt, (double)this.zo, (double)this.z);
    }

    public List<Vec3> getTrailHistory() {
        return this.trailHistory;
    }

    public int getAge() {
        return this.age;
    }

    public int getMaxAge() {
        return this.maxAge;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public double getVx() {
        return this.vx;
    }

    public double getVy() {
        return this.vy;
    }

    public double getVz() {
        return this.vz;
    }

    public boolean isBreak() {
        return this.isBreak;
    }
}

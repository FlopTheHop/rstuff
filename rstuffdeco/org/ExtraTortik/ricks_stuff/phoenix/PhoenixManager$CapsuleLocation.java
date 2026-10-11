/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.level.Level
 */
package org.ExtraTortik.ricks_stuff.phoenix;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public static class PhoenixManager.CapsuleLocation {
    public final BlockPos pos;
    public final ResourceKey<Level> dimension;

    public PhoenixManager.CapsuleLocation(BlockPos pos, ResourceKey<Level> dimension) {
        this.pos = pos;
        this.dimension = dimension;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) return false;
        if (this.getClass() != o.getClass()) {
            return false;
        }
        PhoenixManager.CapsuleLocation that = (PhoenixManager.CapsuleLocation)o;
        return Objects.equals(this.pos, that.pos) && Objects.equals(this.dimension, that.dimension);
    }

    public int hashCode() {
        return Objects.hash(this.pos, this.dimension);
    }
}

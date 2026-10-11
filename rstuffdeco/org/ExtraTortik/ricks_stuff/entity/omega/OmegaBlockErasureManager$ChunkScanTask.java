/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.level.ChunkPos
 *  net.minecraft.world.level.Level
 */
package org.ExtraTortik.ricks_stuff.entity.omega;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

private static class OmegaBlockErasureManager.ChunkScanTask {
    final ResourceKey<Level> dimension;
    final ChunkPos pos;

    OmegaBlockErasureManager.ChunkScanTask(ResourceKey<Level> dimension, ChunkPos pos) {
        this.dimension = dimension;
        this.pos = pos;
    }
}

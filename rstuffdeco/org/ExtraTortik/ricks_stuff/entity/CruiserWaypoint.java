/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.FriendlyByteBuf
 */
package org.ExtraTortik.ricks_stuff.entity;

import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public record CruiserWaypoint(UUID id, String name, double x, double y, double z, String dimension, long timestamp) {
    public CruiserWaypoint(String name, double x, double y, double z, String dimension) {
        this(UUID.randomUUID(), name != null ? name : "Waypoint", x, y, z, dimension != null ? dimension : "minecraft:overworld", System.currentTimeMillis());
    }

    public CompoundTag toNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("Id", this.id != null ? this.id : UUID.randomUUID());
        tag.putString("Name", this.name != null ? this.name : "");
        tag.putDouble("X", this.x);
        tag.putDouble("Y", this.y);
        tag.putDouble("Z", this.z);
        tag.putString("Dimension", this.dimension != null ? this.dimension : "minecraft:overworld");
        tag.putLong("Timestamp", this.timestamp);
        return tag;
    }

    public static CruiserWaypoint fromNbt(CompoundTag tag) {
        UUID id = tag.hasUUID("Id") ? tag.getUUID("Id") : (tag.contains("Id") ? UUID.fromString(tag.getString("Id")) : UUID.randomUUID());
        String name = tag.getString("Name");
        double x = tag.getDouble("X");
        double y = tag.getDouble("Y");
        double z = tag.getDouble("Z");
        String dimension = tag.contains("Dimension") ? tag.getString("Dimension") : "minecraft:overworld";
        long timestamp = tag.getLong("Timestamp");
        return new CruiserWaypoint(id, name, x, y, z, dimension, timestamp);
    }

    public void writeToBuf(FriendlyByteBuf buf) {
        buf.writeUUID(this.id != null ? this.id : UUID.randomUUID());
        buf.writeUtf(this.name != null ? this.name : "");
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeUtf(this.dimension != null ? this.dimension : "minecraft:overworld");
        buf.writeLong(this.timestamp);
    }

    public static CruiserWaypoint readFromBuf(FriendlyByteBuf buf) {
        UUID id = buf.readUUID();
        String name = buf.readUtf();
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        String dimension = buf.readUtf();
        long timestamp = buf.readLong();
        return new CruiserWaypoint(id, name, x, y, z, dimension, timestamp);
    }
}

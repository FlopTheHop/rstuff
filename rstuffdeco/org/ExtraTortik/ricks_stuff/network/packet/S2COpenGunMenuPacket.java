/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.network.packet.S2COpenGunMenuPacket$ClientHandler
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.network.packet.S2COpenGunMenuPacket;

/*
 * Exception performing whole class analysis ignored.
 */
public record S2COpenGunMenuPacket(CompoundTag gunNbt, int slotIndex, boolean allowPlayerTargeting) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<S2COpenGunMenuPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"open_gun_menu"));
    public static final StreamCodec<RegistryFriendlyByteBuf, S2COpenGunMenuPacket> STREAM_CODEC = StreamCodec.ofMember(S2COpenGunMenuPacket::write, S2COpenGunMenuPacket::new);

    public S2COpenGunMenuPacket(CompoundTag gunNbt, int slotIndex) {
        this(gunNbt, slotIndex, Config.allowPlayerTargeting);
    }

    public S2COpenGunMenuPacket(RegistryFriendlyByteBuf buf) {
        this(buf.readNbt(), buf.readInt(), buf.readBoolean());
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeNbt((Tag)(this.gunNbt != null ? this.gunNbt : new CompoundTag()));
        buf.writeInt(this.slotIndex);
        buf.writeBoolean(this.allowPlayerTargeting);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(S2COpenGunMenuPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Config.allowPlayerTargeting = packet.allowPlayerTargeting();
            ClientHandler.openMenu((CompoundTag)packet.gunNbt(), (int)packet.slotIndex());
        });
    }
}

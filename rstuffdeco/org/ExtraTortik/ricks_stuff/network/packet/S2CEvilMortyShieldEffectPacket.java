/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CEvilMortyShieldEffectPacket$ClientHandler
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.network.packet.S2CEvilMortyShieldEffectPacket;

/*
 * Exception performing whole class analysis ignored.
 */
public record S2CEvilMortyShieldEffectPacket(int entityId, double impactX, double impactY, double impactZ, double dirX, double dirY, double dirZ, boolean isBreak) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<S2CEvilMortyShieldEffectPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"s2c_evil_morty_shield_effect"));
    public static final StreamCodec<FriendlyByteBuf, S2CEvilMortyShieldEffectPacket> STREAM_CODEC = StreamCodec.ofMember(S2CEvilMortyShieldEffectPacket::write, S2CEvilMortyShieldEffectPacket::new);

    public S2CEvilMortyShieldEffectPacket(FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readBoolean());
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeDouble(this.impactX);
        buffer.writeDouble(this.impactY);
        buffer.writeDouble(this.impactZ);
        buffer.writeDouble(this.dirX);
        buffer.writeDouble(this.dirY);
        buffer.writeDouble(this.dirZ);
        buffer.writeBoolean(this.isBreak);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(S2CEvilMortyShieldEffectPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientHandler.handle((S2CEvilMortyShieldEffectPacket)packet));
    }
}

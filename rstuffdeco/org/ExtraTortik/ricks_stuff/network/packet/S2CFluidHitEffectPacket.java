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
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CFluidHitEffectPacket$ClientHandler
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.network.packet.S2CFluidHitEffectPacket;

/*
 * Exception performing whole class analysis ignored.
 */
public record S2CFluidHitEffectPacket(double x, double y, double z, double groundY, boolean hasGround, int rgb) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<S2CFluidHitEffectPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"fluid_hit_effect"));
    public static final StreamCodec<FriendlyByteBuf, S2CFluidHitEffectPacket> STREAM_CODEC = StreamCodec.ofMember(S2CFluidHitEffectPacket::write, S2CFluidHitEffectPacket::new);

    public S2CFluidHitEffectPacket(FriendlyByteBuf buffer) {
        this(buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readBoolean(), buffer.readInt());
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeDouble(this.x);
        buffer.writeDouble(this.y);
        buffer.writeDouble(this.z);
        buffer.writeDouble(this.groundY);
        buffer.writeBoolean(this.hasGround);
        buffer.writeInt(this.rgb);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(S2CFluidHitEffectPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientHandler.handle((S2CFluidHitEffectPacket)packet));
    }
}

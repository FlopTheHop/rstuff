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
 *  org.ExtraTortik.ricks_stuff.network.packet.S2COmegaDeviceEffectPacket$ClientHandler
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.network.packet.S2COmegaDeviceEffectPacket;

/*
 * Exception performing whole class analysis ignored.
 */
public record S2COmegaDeviceEffectPacket(int effectType, double originX, double originY, double originZ, float flashIntensity, int flashDuration, float shakeIntensity, int shakeDuration) implements CustomPacketPayload
{
    public static final int TYPE_FLASH = 0;
    public static final int TYPE_SCREEN_SHAKE = 1;
    public static final int TYPE_ERASURE_PEAK = 2;
    public static final int TYPE_PHASE_CHANGE = 3;
    public static final CustomPacketPayload.Type<S2COmegaDeviceEffectPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"omega_device_effect"));
    public static final StreamCodec<FriendlyByteBuf, S2COmegaDeviceEffectPacket> STREAM_CODEC = StreamCodec.ofMember(S2COmegaDeviceEffectPacket::write, S2COmegaDeviceEffectPacket::new);

    public S2COmegaDeviceEffectPacket(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readFloat(), buf.readInt(), buf.readFloat(), buf.readInt());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.effectType);
        buf.writeDouble(this.originX);
        buf.writeDouble(this.originY);
        buf.writeDouble(this.originZ);
        buf.writeFloat(this.flashIntensity);
        buf.writeInt(this.flashDuration);
        buf.writeFloat(this.shakeIntensity);
        buf.writeInt(this.shakeDuration);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(S2COmegaDeviceEffectPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientHandler.handle((S2COmegaDeviceEffectPacket)packet));
    }
}

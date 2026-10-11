/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.ExtraTortik.ricks_stuff.puppet.PuppetServerManager
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.puppet.PuppetServerManager;

public record C2SPuppetControlPacket(int mobId, float forward, float strafe, boolean jump, boolean sprint, boolean sneak, float yaw, float pitch, boolean attack) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2SPuppetControlPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"c2s_puppet_control"));
    public static final StreamCodec<FriendlyByteBuf, C2SPuppetControlPacket> STREAM_CODEC = StreamCodec.ofMember(C2SPuppetControlPacket::write, C2SPuppetControlPacket::new);

    public C2SPuppetControlPacket(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readFloat(), buf.readFloat(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readFloat(), buf.readFloat(), buf.readBoolean());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.mobId);
        buf.writeFloat(this.forward);
        buf.writeFloat(this.strafe);
        buf.writeBoolean(this.jump);
        buf.writeBoolean(this.sprint);
        buf.writeBoolean(this.sneak);
        buf.writeFloat(this.yaw);
        buf.writeFloat(this.pitch);
        buf.writeBoolean(this.attack);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(C2SPuppetControlPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player patt0$temp = context.player();
            if (!(patt0$temp instanceof ServerPlayer)) return;
            ServerPlayer sender = (ServerPlayer)patt0$temp;
            PuppetServerManager.handleControlPacket((ServerPlayer)sender, (int)packet.mobId(), (float)packet.forward(), (float)packet.strafe(), (boolean)packet.jump(), (boolean)packet.sprint(), (boolean)packet.sneak(), (float)packet.yaw(), (float)packet.pitch(), (boolean)packet.attack());
        });
    }
}

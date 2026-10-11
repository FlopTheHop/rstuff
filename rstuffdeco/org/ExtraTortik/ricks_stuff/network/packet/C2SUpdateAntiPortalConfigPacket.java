/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.ExtraTortik.ricks_stuff.block.entity.AntiPortalFieldGenBlockEntity
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.block.entity.AntiPortalFieldGenBlockEntity;

public record C2SUpdateAntiPortalConfigPacket(BlockPos pos, int radius, boolean enabled) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2SUpdateAntiPortalConfigPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"update_anti_portal_config"));
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SUpdateAntiPortalConfigPacket> STREAM_CODEC = StreamCodec.ofMember(C2SUpdateAntiPortalConfigPacket::write, C2SUpdateAntiPortalConfigPacket::new);

    public C2SUpdateAntiPortalConfigPacket(RegistryFriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readInt(), buf.readBoolean());
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeInt(this.radius);
        buf.writeBoolean(this.enabled);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(C2SUpdateAntiPortalConfigPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player patt0$temp = context.player();
            if (!(patt0$temp instanceof ServerPlayer)) return;
            ServerPlayer player = (ServerPlayer)patt0$temp;
            if (!player.level().isLoaded(packet.pos())) return;
            BlockEntity be = player.level().getBlockEntity(packet.pos());
            if (!(be instanceof AntiPortalFieldGenBlockEntity)) return;
            AntiPortalFieldGenBlockEntity generator = (AntiPortalFieldGenBlockEntity)be;
            generator.setRadius(packet.radius());
            generator.setEnabled(packet.enabled());
        });
    }
}

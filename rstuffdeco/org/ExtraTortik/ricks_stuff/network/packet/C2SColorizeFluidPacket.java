/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.ExtraTortik.ricks_stuff.block.entity.PortalFluidColorizerBlockEntity
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.block.entity.PortalFluidColorizerBlockEntity;
import org.ExtraTortik.ricks_stuff.init.ModSounds;

public record C2SColorizeFluidPacket(BlockPos pos, int fluidColorRgb, int coreColorRgb) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2SColorizeFluidPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"colorize_fluid"));
    public static final StreamCodec<FriendlyByteBuf, C2SColorizeFluidPacket> STREAM_CODEC = StreamCodec.ofMember(C2SColorizeFluidPacket::write, C2SColorizeFluidPacket::new);

    public C2SColorizeFluidPacket(BlockPos pos, int fluidColorRgb) {
        this(pos, fluidColorRgb, fluidColorRgb);
    }

    public C2SColorizeFluidPacket(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readInt(), buf.readableBytes() >= 4 ? buf.readInt() : 0);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeInt(this.fluidColorRgb);
        buf.writeInt(this.coreColorRgb);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(C2SColorizeFluidPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player patt0$temp = context.player();
            if (!(patt0$temp instanceof ServerPlayer)) return;
            ServerPlayer player = (ServerPlayer)patt0$temp;
            if (!player.level().isLoaded(packet.pos())) return;
            if (!(player.distanceToSqr((double)packet.pos().getX() + 0.5, (double)packet.pos().getY() + 0.5, (double)packet.pos().getZ() + 0.5) <= 64.0)) return;
            Level level = player.level();
            BlockEntity be = level.getBlockEntity(packet.pos());
            if (!(be instanceof PortalFluidColorizerBlockEntity)) return;
            PortalFluidColorizerBlockEntity colorizer = (PortalFluidColorizerBlockEntity)be;
            int core = packet.coreColorRgb() != 0 ? packet.coreColorRgb() : packet.fluidColorRgb();
            if (!colorizer.colorize(packet.fluidColorRgb(), core)) return;
            level.playSound(null, packet.pos(), (SoundEvent)ModSounds.BULB_INSERT.get(), SoundSource.BLOCKS, 1.0f, 1.2f);
        });
    }
}

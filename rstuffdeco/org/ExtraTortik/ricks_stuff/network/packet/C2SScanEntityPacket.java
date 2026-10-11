/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.ExtraTortik.ricks_stuff.combat.NeuralScanEngine
 *  org.ExtraTortik.ricks_stuff.compat.curios.CuriosCompat
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CScanResultPacket
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.combat.NeuralScanEngine;
import org.ExtraTortik.ricks_stuff.compat.curios.CuriosCompat;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.S2CScanResultPacket;

public record C2SScanEntityPacket(int targetEntityId, boolean startScan) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2SScanEntityPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"c2s_scan_entity"));
    public static final StreamCodec<FriendlyByteBuf, C2SScanEntityPacket> STREAM_CODEC = StreamCodec.ofMember(C2SScanEntityPacket::write, C2SScanEntityPacket::new);

    public C2SScanEntityPacket(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readBoolean());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.targetEntityId);
        buf.writeBoolean(this.startScan);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(C2SScanEntityPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player patt0$temp = context.player();
            if (!(patt0$temp instanceof ServerPlayer)) return;
            ServerPlayer sender = (ServerPlayer)patt0$temp;
            Level patt1$temp = sender.level();
            if (!(patt1$temp instanceof ServerLevel)) return;
            ServerLevel serverLevel = (ServerLevel)patt1$temp;
            Entity target = serverLevel.getEntity(packet.targetEntityId());
            if (!(target instanceof LivingEntity)) return;
            LivingEntity livingTarget = (LivingEntity)target;
            if (!packet.startScan()) return;
            if (!CuriosCompat.isWearingEyePatch((LivingEntity)sender)) {
                return;
            }
            if (!livingTarget.isAlive()) return;
            if (sender.distanceToSqr((Entity)livingTarget) > 784.0) {
                return;
            }
            String prediction = NeuralScanEngine.predictIntent((LivingEntity)livingTarget);
            ModMessages.sendToPlayer((CustomPacketPayload)new S2CScanResultPacket(packet.targetEntityId(), prediction, 120, true), (ServerPlayer)sender);
        });
    }
}

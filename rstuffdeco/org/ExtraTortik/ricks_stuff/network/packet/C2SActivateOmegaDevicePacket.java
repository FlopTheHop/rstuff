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
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceManager
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceManager;

public record C2SActivateOmegaDevicePacket(int entityId, String targetType, @Nullable UUID targetUUID) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2SActivateOmegaDevicePacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"activate_omega_device"));
    public static final StreamCodec<FriendlyByteBuf, C2SActivateOmegaDevicePacket> STREAM_CODEC = StreamCodec.ofMember(C2SActivateOmegaDevicePacket::write, C2SActivateOmegaDevicePacket::new);

    public C2SActivateOmegaDevicePacket(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readUtf(), buf.readBoolean() ? buf.readUUID() : null);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeUtf(this.targetType != null ? this.targetType : "");
        if (this.targetUUID != null) {
            buf.writeBoolean(true);
            buf.writeUUID(this.targetUUID);
        } else {
            buf.writeBoolean(false);
        }
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(C2SActivateOmegaDevicePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Entity e;
            Player patt0$temp = context.player();
            if (!(patt0$temp instanceof ServerPlayer)) return;
            ServerPlayer player = (ServerPlayer)patt0$temp;
            Level patt1$temp = player.level();
            if (!(patt1$temp instanceof ServerLevel)) return;
            ServerLevel sl = (ServerLevel)patt1$temp;
            OmegaDeviceEntity device = null;
            if (packet.entityId() != -1 && (e = sl.getEntity(packet.entityId())) instanceof OmegaDeviceEntity) {
                OmegaDeviceEntity ode;
                device = ode = (OmegaDeviceEntity)e;
            }
            if (device == null) return;
            if (!(player.distanceToSqr(device) <= 64.0)) return;
            if (!OmegaDeviceManager.canActivate(device, (Player)player, null)) return;
            Entity target = null;
            if (packet.targetUUID() != null) {
                target = sl.getEntity(packet.targetUUID());
            }
            OmegaDeviceManager.startSequence((OmegaDeviceEntity)device, (Entity)target, (String)packet.targetType(), (UUID)packet.targetUUID());
        });
    }
}

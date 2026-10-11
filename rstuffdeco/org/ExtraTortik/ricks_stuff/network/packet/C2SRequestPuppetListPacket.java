/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.phys.AABB
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CPuppetListPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CPuppetListPacket$PuppetEntryData
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.S2CPuppetListPacket;

public record C2SRequestPuppetListPacket() implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2SRequestPuppetListPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"c2s_request_puppet_list"));
    public static final StreamCodec<FriendlyByteBuf, C2SRequestPuppetListPacket> STREAM_CODEC = StreamCodec.unit((Object)((Object)new C2SRequestPuppetListPacket()));

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(C2SRequestPuppetListPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player patt0$temp = context.player();
            if (!(patt0$temp instanceof ServerPlayer)) return;
            ServerPlayer sender = (ServerPlayer)patt0$temp;
            if (sender.serverLevel() == null) {
                return;
            }
            AABB box = sender.getBoundingBox().inflate(64.0);
            List mobs = sender.serverLevel().getEntitiesOfClass(Mob.class, box, m -> {
                if (!m.isAlive()) {
                    return false;
                }
                CompoundTag data = m.getPersistentData();
                if (!data.getBoolean("BioNeuralChipped")) {
                    return false;
                }
                UUID owner = data.hasUUID("BioNeuralOwner") ? data.getUUID("BioNeuralOwner") : null;
                return sender.getUUID().equals(owner);
            });
            ArrayList<S2CPuppetListPacket.PuppetEntryData> entries = new ArrayList<S2CPuppetListPacket.PuppetEntryData>(mobs.size());
            Iterator iterator = mobs.iterator();
            while (true) {
                if (!iterator.hasNext()) {
                    ModMessages.sendToPlayer((CustomPacketPayload)new S2CPuppetListPacket(entries), (ServerPlayer)sender);
                    return;
                }
                Mob mob = (Mob)iterator.next();
                entries.add(new S2CPuppetListPacket.PuppetEntryData(mob.getId(), mob.getUUID(), mob.getDisplayName().getString(), mob.getHealth(), mob.getMaxHealth(), mob.getX(), mob.getY(), mob.getZ()));
            }
        });
    }
}

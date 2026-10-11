/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.ExtraTortik.ricks_stuff.compat.InfinityCompat
 *  org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.ExtraTortik.ricks_stuff.portal.BlenderDimensionHelper
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.compat.InfinityCompat;
import org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.ExtraTortik.ricks_stuff.portal.BlenderDimensionHelper;

public record C2SSaveLocationPacket(String name, double x, double y, double z, String dimension) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2SSaveLocationPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"save_location"));
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SSaveLocationPacket> STREAM_CODEC = StreamCodec.ofMember(C2SSaveLocationPacket::write, C2SSaveLocationPacket::new);

    public C2SSaveLocationPacket(RegistryFriendlyByteBuf buf) {
        this(buf.readUtf(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readUtf());
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeUtf(this.name);
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeUtf(this.dimension);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(C2SSaveLocationPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ResourceKey infKey;
            Player patt0$temp = context.player();
            if (!(patt0$temp instanceof ServerPlayer)) return;
            ServerPlayer player = (ServerPlayer)patt0$temp;
            ItemStack stack = C2SSaveLocationPacket.getHeldGun(player);
            if (stack == null) return;
            if (stack.isEmpty()) return;
            if (!PortalGunItem.hasMemoryCard((ItemStack)stack)) {
                return;
            }
            MinecraftServer server = player.getServer();
            String actualDim = packet.dimension();
            if (BlenderDimensionHelper.isBlenderDimension((String)packet.dimension())) {
                actualDim = "blenders_dimension";
            } else if (server != null && InfinityCompat.isLoaded() && (infKey = InfinityCompat.resolveOrGenerateDimension((MinecraftServer)server, (String)packet.dimension())) != null) {
                actualDim = infKey.location().toString();
            }
            PortalGunItem.addSavedLocation((ItemStack)stack, (String)packet.name(), (double)packet.x(), (double)packet.y(), (double)packet.z(), (String)actualDim);
        });
    }

    private static ItemStack getHeldGun(ServerPlayer player) {
        if (player.getMainHandItem().getItem() instanceof PortalGunItem) return player.getMainHandItem();
        if (player.getMainHandItem().getItem() instanceof DischargedPortalGunItem) {
            return player.getMainHandItem();
        }
        if (player.getOffhandItem().getItem() instanceof PortalGunItem) return player.getOffhandItem();
        if (!(player.getOffhandItem().getItem() instanceof DischargedPortalGunItem)) return null;
        return player.getOffhandItem();
    }
}

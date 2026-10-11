/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.ExtraTortik.ricks_stuff.portal.PortalManager
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import java.util.Collections;
import java.util.Iterator;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.ExtraTortik.ricks_stuff.portal.PortalManager;

public record C2SCloseAllPortalsPacket() implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2SCloseAllPortalsPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"close_all_portals"));
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SCloseAllPortalsPacket> STREAM_CODEC = StreamCodec.ofMember(C2SCloseAllPortalsPacket::write, C2SCloseAllPortalsPacket::new);

    public C2SCloseAllPortalsPacket(RegistryFriendlyByteBuf buf) {
        this();
    }

    public void write(RegistryFriendlyByteBuf buf) {
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(C2SCloseAllPortalsPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player patt0$temp = context.player();
            if (!(patt0$temp instanceof ServerPlayer)) return;
            ServerPlayer player = (ServerPlayer)patt0$temp;
            ItemStack stack = C2SCloseAllPortalsPacket.getHeldGun(player);
            if (stack == null) return;
            Level patt1$temp = player.level();
            if (!(patt1$temp instanceof ServerLevel)) return;
            ServerLevel serverLevel = (ServerLevel)patt1$temp;
            int gunId = PortalGunItem.getGunId((ItemStack)stack);
            PortalManager.removeAllPortalsForGun((ServerLevel)serverLevel, (int)gunId);
            PortalGunItem.setActivePortals((ItemStack)stack, Collections.emptyList());
        });
    }

    private static ItemStack getHeldGun(ServerPlayer player) {
        ItemStack item;
        if (player.getMainHandItem().getItem() instanceof PortalGunItem) {
            return player.getMainHandItem();
        }
        if (player.getOffhandItem().getItem() instanceof PortalGunItem) {
            return player.getOffhandItem();
        }
        Iterator iterator = player.getInventory().items.iterator();
        do {
            if (!iterator.hasNext()) return null;
        } while (!((item = (ItemStack)iterator.next()).getItem() instanceof PortalGunItem));
        return item;
    }
}

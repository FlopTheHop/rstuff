/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.component.CustomData
 *  net.minecraft.world.level.Level
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.ExtraTortik.ricks_stuff.portal.PortalManager
 *  org.ExtraTortik.ricks_stuff.portal.PortalMode
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.ExtraTortik.ricks_stuff.portal.PortalManager;
import org.ExtraTortik.ricks_stuff.portal.PortalMode;

public record C2SResetGunPacket() implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2SResetGunPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"reset_gun"));
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SResetGunPacket> STREAM_CODEC = StreamCodec.ofMember(C2SResetGunPacket::write, C2SResetGunPacket::new);

    public C2SResetGunPacket(RegistryFriendlyByteBuf buf) {
        this();
    }

    public void write(RegistryFriendlyByteBuf buf) {
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(C2SResetGunPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player patt0$temp = context.player();
            if (!(patt0$temp instanceof ServerPlayer)) return;
            ServerPlayer player = (ServerPlayer)patt0$temp;
            ItemStack stack = C2SResetGunPacket.getHeldGun(player);
            if (stack == null) return;
            if (stack.isEmpty()) return;
            int gunId = PortalGunItem.getGunId((ItemStack)stack);
            boolean hadCard = PortalGunItem.hasMemoryCard((ItemStack)stack);
            stack.update(DataComponents.CUSTOM_DATA, (Object)CustomData.EMPTY, c -> c.update(tag -> {
                tag.remove("SavedLocations");
                tag.remove("HistoryLocations");
                tag.remove("CustomLocation");
                tag.remove("CustomLocationIndex");
                tag.remove("ActivePortals");
            }));
            PortalGunItem.setMode((ItemStack)stack, (PortalMode)PortalMode.FIFO);
            PortalGunItem.setScale((ItemStack)stack, (float)2.0f);
            PortalGunItem.setAutoClose((ItemStack)stack, (boolean)false);
            PortalGunItem.setSafePlacement((ItemStack)stack, (boolean)true);
            PortalGunItem.setChargeBar((ItemStack)stack, (boolean)true);
            PortalGunItem.setAutoAirDetonate((ItemStack)stack, (boolean)false);
            PortalGunItem.setAirDistance((ItemStack)stack, (int)15);
            PortalGunItem.initializeGunNBT((ItemStack)stack);
            PortalGunItem.setHasMemoryCard((ItemStack)stack, (boolean)hadCard);
            Level patt1$temp = player.level();
            if (!(patt1$temp instanceof ServerLevel)) return;
            ServerLevel serverLevel = (ServerLevel)patt1$temp;
            PortalManager.removeAllPortalsForGun((ServerLevel)serverLevel, (int)gunId);
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

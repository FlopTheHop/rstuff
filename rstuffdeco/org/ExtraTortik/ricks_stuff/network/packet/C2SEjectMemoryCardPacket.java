/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;

public record C2SEjectMemoryCardPacket() implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2SEjectMemoryCardPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"eject_memory_card"));
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SEjectMemoryCardPacket> STREAM_CODEC = StreamCodec.ofMember(C2SEjectMemoryCardPacket::write, C2SEjectMemoryCardPacket::new);

    public C2SEjectMemoryCardPacket(RegistryFriendlyByteBuf buf) {
        this();
    }

    public void write(RegistryFriendlyByteBuf buf) {
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(C2SEjectMemoryCardPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player patt0$temp = context.player();
            if (!(patt0$temp instanceof ServerPlayer)) return;
            ServerPlayer player = (ServerPlayer)patt0$temp;
            InteractionHand hand = C2SEjectMemoryCardPacket.isGun(player.getMainHandItem()) ? InteractionHand.MAIN_HAND : (C2SEjectMemoryCardPacket.isGun(player.getOffhandItem()) ? InteractionHand.OFF_HAND : null);
            if (hand == null) return;
            ItemStack gunStack = player.getItemInHand(hand);
            PortalGunItem.ejectMemoryCard((ItemStack)gunStack, (Player)player);
        });
    }

    private static boolean isGun(ItemStack stack) {
        return stack != null && !stack.isEmpty() && (stack.getItem() instanceof PortalGunItem || stack.getItem() instanceof DischargedPortalGunItem);
    }
}

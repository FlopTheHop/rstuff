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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;

public record C2SEjectUpgradePacket(int upgradeSlotIndex) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2SEjectUpgradePacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"eject_upgrade"));
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SEjectUpgradePacket> STREAM_CODEC = StreamCodec.ofMember(C2SEjectUpgradePacket::write, C2SEjectUpgradePacket::new);

    public C2SEjectUpgradePacket(RegistryFriendlyByteBuf buf) {
        this(buf.readVarInt());
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(this.upgradeSlotIndex);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(C2SEjectUpgradePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player patt0$temp = context.player();
            if (!(patt0$temp instanceof ServerPlayer)) return;
            ServerPlayer player = (ServerPlayer)patt0$temp;
            ItemStack gunStack = C2SEjectUpgradePacket.getHeldGun(player);
            if (gunStack == null || gunStack.isEmpty()) {
                for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
                    ItemStack candidate = player.getInventory().getItem(i);
                    if (!(candidate.getItem() instanceof PortalGunItem) && !(candidate.getItem() instanceof DischargedPortalGunItem)) continue;
                    gunStack = candidate;
                    break;
                }
            }
            if (gunStack == null) return;
            if (gunStack.isEmpty()) return;
            PortalGunItem.ejectUpgrade((ItemStack)gunStack, (int)packet.upgradeSlotIndex(), (Player)player);
            player.inventoryMenu.broadcastChanges();
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

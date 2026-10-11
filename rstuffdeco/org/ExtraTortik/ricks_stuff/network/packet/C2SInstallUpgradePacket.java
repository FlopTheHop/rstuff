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
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.ExpansionCardItem
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.UpgradeType
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem;
import org.ExtraTortik.ricks_stuff.item.ExpansionCardItem;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.ExtraTortik.ricks_stuff.item.UpgradeType;

public record C2SInstallUpgradePacket(int invSlot) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2SInstallUpgradePacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"install_upgrade"));
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SInstallUpgradePacket> STREAM_CODEC = StreamCodec.ofMember(C2SInstallUpgradePacket::write, C2SInstallUpgradePacket::new);

    public C2SInstallUpgradePacket(RegistryFriendlyByteBuf buf) {
        this(buf.readVarInt());
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(this.invSlot);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(C2SInstallUpgradePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player patt0$temp = context.player();
            if (!(patt0$temp instanceof ServerPlayer)) return;
            ServerPlayer player = (ServerPlayer)patt0$temp;
            ItemStack gunStack = C2SInstallUpgradePacket.getHeldGun(player);
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
            if (packet.invSlot() < 0) return;
            if (packet.invSlot() >= player.getInventory().getContainerSize()) return;
            ItemStack cardStack = player.getInventory().getItem(packet.invSlot());
            if (cardStack.isEmpty()) return;
            Item patt1$temp = cardStack.getItem();
            if (!(patt1$temp instanceof ExpansionCardItem)) return;
            ExpansionCardItem cardItem = (ExpansionCardItem)patt1$temp;
            UpgradeType type = cardItem.getUpgradeType();
            if (PortalGunItem.canInstallUpgrade((ItemStack)gunStack, (UpgradeType)type)) {
                if (!PortalGunItem.installUpgrade((ItemStack)gunStack, (UpgradeType)type)) return;
                if (!player.getAbilities().instabuild) {
                    cardStack.shrink(1);
                }
                player.inventoryMenu.broadcastChanges();
                player.playSound((SoundEvent)ModSounds.UI_PRESS.get(), 1.0f, 1.4f);
            } else {
                player.playSound((SoundEvent)ModSounds.GUN_EMPTY.get(), 1.0f, 1.0f);
            }
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

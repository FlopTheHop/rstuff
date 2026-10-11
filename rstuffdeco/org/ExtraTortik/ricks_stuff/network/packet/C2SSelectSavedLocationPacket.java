/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
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
 *  org.ExtraTortik.ricks_stuff.item.UpgradeType
 *  org.ExtraTortik.ricks_stuff.portal.PortalMode
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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
import org.ExtraTortik.ricks_stuff.item.UpgradeType;
import org.ExtraTortik.ricks_stuff.portal.PortalMode;

public record C2SSelectSavedLocationPacket(int index) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2SSelectSavedLocationPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"select_saved_location"));
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SSelectSavedLocationPacket> STREAM_CODEC = StreamCodec.ofMember(C2SSelectSavedLocationPacket::write, C2SSelectSavedLocationPacket::new);

    public C2SSelectSavedLocationPacket(RegistryFriendlyByteBuf buf) {
        this(buf.readInt());
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(this.index);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(C2SSelectSavedLocationPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            String currentDim;
            Player patt0$temp = context.player();
            if (!(patt0$temp instanceof ServerPlayer)) return;
            ServerPlayer player = (ServerPlayer)patt0$temp;
            ItemStack stack = C2SSelectSavedLocationPacket.getHeldGun(player);
            if (stack == null) return;
            if (stack.isEmpty()) return;
            if (!PortalGunItem.hasMemoryCard((ItemStack)stack)) {
                return;
            }
            ListTag savedList = PortalGunItem.getSavedLocations((ItemStack)stack);
            if (packet.index() < 0) return;
            if (packet.index() >= savedList.size()) return;
            CompoundTag locTag = savedList.getCompound(packet.index());
            String dim = locTag.getString("Dimension");
            if (dim.isEmpty()) {
                dim = "minecraft:overworld";
            }
            if (!(dim.equalsIgnoreCase(currentDim = player.level().dimension().location().toString()) || PortalGunItem.hasUpgrade((ItemStack)stack, (UpgradeType)UpgradeType.DIMENSIONAL) || player.getAbilities().instabuild)) {
                return;
            }
            PortalGunItem.setCustomLocationIndex((ItemStack)stack, (int)packet.index());
            PortalGunItem.setCustomLocation((ItemStack)stack, (CompoundTag)locTag);
            PortalGunItem.setMode((ItemStack)stack, (PortalMode)PortalMode.CUSTOM);
            Object name = locTag.getString("Name");
            if (!((String)name).isEmpty()) return;
            name = "Custom (" + (int)locTag.getDouble("X") + ", " + (int)locTag.getDouble("Y") + ", " + (int)locTag.getDouble("Z") + ")";
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

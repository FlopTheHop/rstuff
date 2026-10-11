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

public record C2SUpdateGunSettingsPacket(int scaleIdx, boolean autoClose, boolean safePlacement, boolean chargeBar, boolean autoAirDetonate, int airDistance) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2SUpdateGunSettingsPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"update_gun_settings"));
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SUpdateGunSettingsPacket> STREAM_CODEC = StreamCodec.ofMember(C2SUpdateGunSettingsPacket::write, C2SUpdateGunSettingsPacket::new);

    public C2SUpdateGunSettingsPacket(RegistryFriendlyByteBuf buf) {
        this(buf.readInt(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readInt());
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(this.scaleIdx);
        buf.writeBoolean(this.autoClose);
        buf.writeBoolean(this.safePlacement);
        buf.writeBoolean(this.chargeBar);
        buf.writeBoolean(this.autoAirDetonate);
        buf.writeInt(this.airDistance);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public float getScale() {
        return switch (this.scaleIdx) {
            case 0 -> 0.5f;
            case 1 -> 1.0f;
            case 2 -> 1.5f;
            case 3 -> 2.0f;
            default -> 2.0f;
        };
    }

    public static int getScaleIndex(float scale) {
        if (Math.abs(scale - 0.5f) < 0.1f) {
            return 0;
        }
        if (Math.abs(scale - 1.0f) < 0.1f) {
            return 1;
        }
        if (Math.abs(scale - 1.5f) < 0.1f) {
            return 2;
        }
        if (!(Math.abs(scale - 2.0f) < 0.1f)) return 3;
        return 3;
    }

    public static void handle(C2SUpdateGunSettingsPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player patt0$temp = context.player();
            if (!(patt0$temp instanceof ServerPlayer)) return;
            ServerPlayer player = (ServerPlayer)patt0$temp;
            ItemStack stack = C2SUpdateGunSettingsPacket.getHeldGun(player);
            if (stack == null) return;
            if (stack.isEmpty()) return;
            PortalGunItem.setScale((ItemStack)stack, (float)packet.getScale());
            PortalGunItem.setAutoClose((ItemStack)stack, (boolean)packet.autoClose());
            PortalGunItem.setSafePlacement((ItemStack)stack, (boolean)packet.safePlacement());
            PortalGunItem.setChargeBar((ItemStack)stack, (boolean)packet.chargeBar());
            PortalGunItem.setAutoAirDetonate((ItemStack)stack, (boolean)packet.autoAirDetonate());
            PortalGunItem.setAirDistance((ItemStack)stack, (int)packet.airDistance());
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

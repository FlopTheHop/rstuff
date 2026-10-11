/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.component.CustomData
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.UpgradeType
 *  org.ExtraTortik.ricks_stuff.portal.AntiPortalFieldManager
 *  org.ExtraTortik.ricks_stuff.portal.PortalMode
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.ExtraTortik.ricks_stuff.item.UpgradeType;
import org.ExtraTortik.ricks_stuff.portal.AntiPortalFieldManager;
import org.ExtraTortik.ricks_stuff.portal.PortalMode;

public record C2STargetPlayerPacket(UUID targetUUID, String targetName, boolean liveTracking) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2STargetPlayerPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"target_player"));
    public static final StreamCodec<RegistryFriendlyByteBuf, C2STargetPlayerPacket> STREAM_CODEC = StreamCodec.ofMember(C2STargetPlayerPacket::write, C2STargetPlayerPacket::new);

    public C2STargetPlayerPacket(UUID targetUUID, String targetName, boolean liveTracking) {
        this.targetUUID = targetUUID;
        this.targetName = targetName != null ? targetName : "Player";
        this.liveTracking = liveTracking;
    }

    public C2STargetPlayerPacket(RegistryFriendlyByteBuf buf) {
        this(buf.readUUID(), buf.readUtf(64), buf.readBoolean());
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeUUID(this.targetUUID);
        buf.writeUtf(this.targetName, 64);
        buf.writeBoolean(this.liveTracking);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(C2STargetPlayerPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player patt0$temp = context.player();
            if (!(patt0$temp instanceof ServerPlayer)) return;
            ServerPlayer player = (ServerPlayer)patt0$temp;
            if (player.getServer() == null) return;
            if (!Config.allowPlayerTargeting) {
                return;
            }
            ItemStack stack = C2STargetPlayerPacket.getHeldGun(player);
            if (stack.isEmpty()) return;
            if (!player.getAbilities().instabuild && !PortalGunItem.hasUpgrade((ItemStack)stack, (UpgradeType)UpgradeType.PLAYER_LOCATOR)) {
                return;
            }
            CompoundTag locTag = new CompoundTag();
            locTag.putString("Name", "Player: " + packet.targetName());
            locTag.putString("TargetPlayerUUID", packet.targetUUID().toString());
            locTag.putString("TargetPlayerName", packet.targetName());
            locTag.putBoolean("LivePlayerLock", packet.liveTracking());
            ServerPlayer targetPlayer = player.getServer().getPlayerList().getPlayer(packet.targetUUID());
            if (targetPlayer != null) {
                if (AntiPortalFieldManager.isPlayerHidden((Player)targetPlayer)) {
                    player.displayClientMessage((Component)Component.translatable((String)"message.ricks_stuff.anti_portal_target_jammed").withStyle(ChatFormatting.RED), true);
                    return;
                }
                boolean sameDim = targetPlayer.level().dimension().equals(player.level().dimension());
                if (!sameDim && !Config.allowPlayerTrackingAcrossDims) {
                    player.displayClientMessage((Component)Component.translatable((String)"message.ricks_stuff.tracking_cross_dim_disabled").withStyle(ChatFormatting.RED), true);
                    return;
                }
                locTag.putDouble("X", (double)Math.round(targetPlayer.getX() * 10.0) / 10.0);
                locTag.putDouble("Y", (double)Math.round(targetPlayer.getY() * 10.0) / 10.0);
                locTag.putDouble("Z", (double)Math.round(targetPlayer.getZ() * 10.0) / 10.0);
                locTag.putString("Dimension", targetPlayer.level().dimension().location().toString());
            } else {
                locTag.putDouble("X", (double)Math.round(player.getX() * 10.0) / 10.0);
                locTag.putDouble("Y", (double)Math.round(player.getY() * 10.0) / 10.0);
                locTag.putDouble("Z", (double)Math.round(player.getZ() * 10.0) / 10.0);
                locTag.putString("Dimension", player.level().dimension().location().toString());
            }
            stack.update(DataComponents.CUSTOM_DATA, (Object)CustomData.EMPTY, c -> c.update(tag -> {
                tag.putString("Mode", PortalMode.CUSTOM.getName());
                tag.put("CustomLocation", (Tag)locTag);
            }));
        });
    }

    private static ItemStack getHeldGun(ServerPlayer player) {
        if (player.getMainHandItem().getItem() instanceof PortalGunItem) return player.getMainHandItem();
        if (player.getMainHandItem().getItem() instanceof DischargedPortalGunItem) {
            return player.getMainHandItem();
        }
        if (player.getOffhandItem().getItem() instanceof PortalGunItem) return player.getOffhandItem();
        if (!(player.getOffhandItem().getItem() instanceof DischargedPortalGunItem)) return ItemStack.EMPTY;
        return player.getOffhandItem();
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.entity.SelfDestructGunEntity
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.ExtraTortik.ricks_stuff.portal.PortalManager
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import java.util.Collections;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.entity.SelfDestructGunEntity;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.ExtraTortik.ricks_stuff.portal.PortalManager;

public record C2SSelfDestructPacket() implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2SSelfDestructPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"self_destruct"));
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SSelfDestructPacket> STREAM_CODEC = StreamCodec.ofMember(C2SSelfDestructPacket::write, C2SSelfDestructPacket::new);

    public C2SSelfDestructPacket(RegistryFriendlyByteBuf buf) {
        this();
    }

    public void write(RegistryFriendlyByteBuf buf) {
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(C2SSelfDestructPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player patt0$temp = context.player();
            if (!(patt0$temp instanceof ServerPlayer)) return;
            ServerPlayer player = (ServerPlayer)patt0$temp;
            if (player.isRemoved()) return;
            if (!Config.selfDestructGunEnabled) {
                player.displayClientMessage((Component)Component.translatable((String)"message.ricks_stuff.self_destruct_disabled").withStyle(ChatFormatting.RED), true);
                return;
            }
            InteractionHand hand = null;
            ItemStack gunStack = ItemStack.EMPTY;
            if (player.getMainHandItem().getItem() instanceof PortalGunItem) {
                hand = InteractionHand.MAIN_HAND;
                gunStack = player.getMainHandItem().copy();
            } else if (player.getOffhandItem().getItem() instanceof PortalGunItem) {
                hand = InteractionHand.OFF_HAND;
                gunStack = player.getOffhandItem().copy();
            }
            if (hand == null) return;
            if (gunStack.isEmpty()) return;
            Level patt1$temp = player.level();
            if (patt1$temp instanceof ServerLevel) {
                ServerLevel serverLevel = (ServerLevel)patt1$temp;
                int gunId = PortalGunItem.getGunId((ItemStack)gunStack);
                PortalManager.removeAllPortalsForGun((ServerLevel)serverLevel, (int)gunId);
                PortalGunItem.setActivePortals((ItemStack)gunStack, Collections.emptyList());
            }
            player.setItemInHand(hand, ItemStack.EMPTY);
            Vec3 look = player.getLookAngle();
            Vec3 spawnPos = player.position().add(0.0, (double)player.getEyeHeight() - 0.2, 0.0).add(look.scale(0.6));
            Vec3 throwVelocity = look.scale(0.65).add(0.0, 0.25, 0.0);
            SelfDestructGunEntity gunEntity = new SelfDestructGunEntity(player.level(), spawnPos.x, spawnPos.y, spawnPos.z, gunStack);
            gunEntity.setDeltaMovement(throwVelocity);
            player.level().addFreshEntity((Entity)gunEntity);
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), (SoundEvent)ModSounds.GUN_MALFUNCTION.get(), SoundSource.PLAYERS, 1.5f, 1.8f);
        });
    }
}

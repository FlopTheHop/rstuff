/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.boss.enderdragon.EnderDragon
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.BioNeuralReceiverItem
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.BioNeuralReceiverItem;

public record C2SImplantChipPacket(int mobId) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2SImplantChipPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"c2s_implant_chip"));
    public static final StreamCodec<FriendlyByteBuf, C2SImplantChipPacket> STREAM_CODEC = StreamCodec.ofMember(C2SImplantChipPacket::write, C2SImplantChipPacket::new);

    public C2SImplantChipPacket(FriendlyByteBuf buf) {
        this(buf.readInt());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.mobId);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(C2SImplantChipPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            double maxDist;
            Player patt0$temp = context.player();
            if (!(patt0$temp instanceof ServerPlayer)) return;
            ServerPlayer sender = (ServerPlayer)patt0$temp;
            Level patt1$temp = sender.level();
            if (!(patt1$temp instanceof ServerLevel)) return;
            ServerLevel serverLevel = (ServerLevel)patt1$temp;
            Entity entity = serverLevel.getEntity(packet.mobId());
            if (!(entity instanceof Mob)) return;
            Mob mob = (Mob)entity;
            if (!mob.isAlive()) {
                return;
            }
            double d = maxDist = mob instanceof EnderDragon ? 16.0 : 4.5;
            if ((double)sender.distanceTo((Entity)mob) > maxDist) {
                return;
            }
            CompoundTag persistentData = mob.getPersistentData();
            if (persistentData.getBoolean("BioNeuralChipped")) {
                return;
            }
            ItemStack chipStack = ItemStack.EMPTY;
            for (ItemStack s : sender.getInventory().items) {
                if (!(s.getItem() instanceof BioNeuralReceiverItem) || BioNeuralReceiverItem.hasLinkedMob((ItemStack)s)) continue;
                chipStack = s;
                break;
            }
            if (chipStack.isEmpty() && sender.getOffhandItem().getItem() instanceof BioNeuralReceiverItem && !BioNeuralReceiverItem.hasLinkedMob((ItemStack)sender.getOffhandItem())) {
                chipStack = sender.getOffhandItem();
            }
            if (chipStack.isEmpty()) {
                return;
            }
            persistentData.putBoolean("BioNeuralChipped", true);
            persistentData.putUUID("BioNeuralOwner", sender.getUUID());
            persistentData.putString("BioNeuralOwnerName", sender.getScoreboardName());
            mob.setPersistenceRequired();
            serverLevel.sendParticles((ParticleOptions)ParticleTypes.ELECTRIC_SPARK, mob.getX(), mob.getEyeY(), mob.getZ(), 16, 0.25, 0.25, 0.25, 0.1);
            serverLevel.playSound(null, mob.getX(), mob.getY(), mob.getZ(), (SoundEvent)ModSounds.UI_DIAL.get(), SoundSource.PLAYERS, 1.0f, 1.5f);
            chipStack.shrink(1);
            sender.containerMenu.broadcastChanges();
            sender.inventoryMenu.broadcastChanges();
        });
    }
}

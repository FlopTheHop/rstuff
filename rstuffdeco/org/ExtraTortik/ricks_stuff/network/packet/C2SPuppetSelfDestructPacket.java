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
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.Level$ExplosionInteraction
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.ExtraTortik.ricks_stuff.compat.curios.CuriosCompat
 *  org.ExtraTortik.ricks_stuff.puppet.PuppetServerManager
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import java.util.UUID;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.compat.curios.CuriosCompat;
import org.ExtraTortik.ricks_stuff.puppet.PuppetServerManager;

public record C2SPuppetSelfDestructPacket(int mobId) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2SPuppetSelfDestructPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"c2s_puppet_self_destruct"));
    public static final StreamCodec<FriendlyByteBuf, C2SPuppetSelfDestructPacket> STREAM_CODEC = StreamCodec.ofMember(C2SPuppetSelfDestructPacket::write, C2SPuppetSelfDestructPacket::new);

    public C2SPuppetSelfDestructPacket(FriendlyByteBuf buf) {
        this(buf.readInt());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.mobId);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(C2SPuppetSelfDestructPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            UUID owner;
            Player patt0$temp = context.player();
            if (!(patt0$temp instanceof ServerPlayer)) return;
            ServerPlayer sender = (ServerPlayer)patt0$temp;
            Level patt1$temp = sender.level();
            if (!(patt1$temp instanceof ServerLevel)) return;
            ServerLevel serverLevel = (ServerLevel)patt1$temp;
            if (!CuriosCompat.isWearingEyePatch((LivingEntity)sender)) {
                return;
            }
            Entity entity = serverLevel.getEntity(packet.mobId());
            if (!(entity instanceof Mob)) return;
            Mob mob = (Mob)entity;
            if (!mob.isAlive()) {
                return;
            }
            CompoundTag data = mob.getPersistentData();
            if (!data.getBoolean("BioNeuralChipped")) {
                return;
            }
            UUID uUID = owner = data.hasUUID("BioNeuralOwner") ? data.getUUID("BioNeuralOwner") : null;
            if (!sender.getUUID().equals(owner)) {
                return;
            }
            if (sender.distanceToSqr((Entity)mob) > 4096.0) {
                return;
            }
            serverLevel.sendParticles((ParticleOptions)ParticleTypes.ELECTRIC_SPARK, mob.getX(), mob.getY() + 1.0, mob.getZ(), 35, 0.4, 0.4, 0.4, 0.15);
            serverLevel.explode((Entity)sender, mob.getX(), mob.getY(), mob.getZ(), 2.8f, Level.ExplosionInteraction.MOB);
            data.putBoolean("BioNeuralSelfDestructed", true);
            data.remove("BioNeuralChipped");
            data.remove("BioNeuralOwner");
            data.remove("BioNeuralOwnerName");
            data.remove("LastPossessedTick");
            data.remove("PossessingPlayerUUID");
            PuppetServerManager.disconnect((Mob)mob);
            mob.discard();
        });
    }
}

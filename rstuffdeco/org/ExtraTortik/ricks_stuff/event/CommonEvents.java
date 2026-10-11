/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  net.minecraft.commands.CommandBuildContext
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket
 *  net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.tags.DamageTypeTags
 *  net.minecraft.util.Mth
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.damagesource.DamageTypes
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.ai.attributes.AttributeInstance
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.boss.EnderDragonPart
 *  net.minecraft.world.entity.boss.enderdragon.EnderDragon
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.projectile.AbstractArrow
 *  net.minecraft.world.entity.projectile.FishingHook
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ChunkPos
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.bus.api.EventPriority
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.neoforge.event.OnDatapackSyncEvent
 *  net.neoforged.neoforge.event.RegisterCommandsEvent
 *  net.neoforged.neoforge.event.entity.EntityEvent$Size
 *  net.neoforged.neoforge.event.entity.EntityJoinLevelEvent
 *  net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent
 *  net.neoforged.neoforge.event.entity.ProjectileImpactEvent
 *  net.neoforged.neoforge.event.entity.item.ItemExpireEvent
 *  net.neoforged.neoforge.event.entity.item.ItemTossEvent
 *  net.neoforged.neoforge.event.entity.living.LivingDeathEvent
 *  net.neoforged.neoforge.event.entity.living.LivingFallEvent
 *  net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
 *  net.neoforged.neoforge.event.entity.player.AttackEntityEvent
 *  net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent$Post
 *  net.neoforged.neoforge.event.entity.player.PlayerContainerEvent$Close
 *  net.neoforged.neoforge.event.entity.player.PlayerContainerEvent$Open
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$ItemCraftedEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$PlayerChangedDimensionEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$PlayerLoggedInEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$PlayerLoggedOutEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$PlayerRespawnEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$StartTracking
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$EntityInteractSpecific
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$LeftClickBlock
 *  net.neoforged.neoforge.event.level.BlockEvent$BreakEvent
 *  net.neoforged.neoforge.event.level.BlockEvent$EntityPlaceEvent
 *  net.neoforged.neoforge.event.level.ChunkWatchEvent$Watch
 *  net.neoforged.neoforge.event.server.ServerStoppedEvent
 *  net.neoforged.neoforge.event.tick.EntityTickEvent$Post
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Post
 *  net.neoforged.neoforge.event.tick.ServerTickEvent$Post
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.block.entity.PhoenixCapsuleBlockEntity
 *  org.ExtraTortik.ricks_stuff.command.OmegaCommand
 *  org.ExtraTortik.ricks_stuff.compat.curios.CuriosCompat
 *  org.ExtraTortik.ricks_stuff.entity.PortalEntity
 *  org.ExtraTortik.ricks_stuff.entity.PortalProjectileEntity
 *  org.ExtraTortik.ricks_stuff.entity.RickPrimeBombEntity
 *  org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity
 *  org.ExtraTortik.ricks_stuff.entity.omega.ErasurePurgeHelper
 *  org.ExtraTortik.ricks_stuff.entity.omega.MultiverseErasureSavedData
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaBlockErasureManager
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceCollider
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceManager
 *  org.ExtraTortik.ricks_stuff.event.CommonEvents$ClientProxy
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.BioNeuralReceiverItem
 *  org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.EvilMortyEnergyShieldItem
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CEvilMortyShieldEffectPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CPhoenixResurrectionPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CSyncConfigPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CSyncMultiverseErasurePacket
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CSyncPlayerScalePacket
 *  org.ExtraTortik.ricks_stuff.phoenix.PhoenixManager
 *  org.ExtraTortik.ricks_stuff.phoenix.PhoenixManager$CapsuleLocation
 *  org.ExtraTortik.ricks_stuff.portal.PortalManager
 *  org.ExtraTortik.ricks_stuff.portal.PortalSavedData
 *  org.ExtraTortik.ricks_stuff.portal.PortalTeleportEngine
 *  org.ExtraTortik.ricks_stuff.puppet.PuppetServerManager
 */
package org.ExtraTortik.ricks_stuff.event;

import com.mojang.brigadier.CommandDispatcher;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.item.ItemExpireEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.block.entity.PhoenixCapsuleBlockEntity;
import org.ExtraTortik.ricks_stuff.command.OmegaCommand;
import org.ExtraTortik.ricks_stuff.compat.curios.CuriosCompat;
import org.ExtraTortik.ricks_stuff.entity.PortalEntity;
import org.ExtraTortik.ricks_stuff.entity.PortalProjectileEntity;
import org.ExtraTortik.ricks_stuff.entity.RickPrimeBombEntity;
import org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity;
import org.ExtraTortik.ricks_stuff.entity.omega.ErasurePurgeHelper;
import org.ExtraTortik.ricks_stuff.entity.omega.MultiverseErasureSavedData;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaBlockErasureManager;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceCollider;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceManager;
import org.ExtraTortik.ricks_stuff.event.CommonEvents;
import org.ExtraTortik.ricks_stuff.init.ModItems;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.BioNeuralReceiverItem;
import org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem;
import org.ExtraTortik.ricks_stuff.item.EvilMortyEnergyShieldItem;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.S2CEvilMortyShieldEffectPacket;
import org.ExtraTortik.ricks_stuff.network.packet.S2CPhoenixResurrectionPacket;
import org.ExtraTortik.ricks_stuff.network.packet.S2CSyncConfigPacket;
import org.ExtraTortik.ricks_stuff.network.packet.S2CSyncMultiverseErasurePacket;
import org.ExtraTortik.ricks_stuff.network.packet.S2CSyncPlayerScalePacket;
import org.ExtraTortik.ricks_stuff.phoenix.PhoenixManager;
import org.ExtraTortik.ricks_stuff.portal.PortalManager;
import org.ExtraTortik.ricks_stuff.portal.PortalSavedData;
import org.ExtraTortik.ricks_stuff.portal.PortalTeleportEngine;
import org.ExtraTortik.ricks_stuff.puppet.PuppetServerManager;

/*
 * Exception performing whole class analysis ignored.
 */
public class CommonEvents {
    private static final Set<Integer> DROPPED_GUN_ENTITIES = ConcurrentHashMap.newKeySet();
    private static final Set<Integer> PICKED_UP_GUN_ENTITIES = ConcurrentHashMap.newKeySet();
    private static final ResourceLocation TINY_RICK_HEALTH_ID = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"tiny_rick_health");
    private static final ResourceLocation TINY_RICK_ATTACK_ID = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"tiny_rick_attack");
    private static final ResourceLocation TINY_RICK_SPEED_ID = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"tiny_rick_speed");

    public static void deflectProjectileWithShield(Player player, Projectile projectile) {
        int remaining;
        boolean isBreak;
        Level level;
        ItemStack shield = CuriosCompat.findShield((Player)player);
        if (!EvilMortyEnergyShieldItem.isShieldActive((ItemStack)shield)) {
            return;
        }
        Vec3 center = player.position().add(0.0, (double)player.getEyeHeight() * 0.5, 0.0);
        Vec3 projPos = projectile.position();
        Vec3 projVel = projectile.getDeltaMovement();
        Vec3 toProj = projPos.subtract(center);
        Vec3 dir = toProj.lengthSqr() > 1.0E-4 ? toProj.normalize() : (projVel.lengthSqr() > 1.0E-4 ? projVel.reverse().normalize() : player.getLookAngle());
        double rx = 1.35;
        double ry = 1.45;
        double rz = 1.35;
        Vec3 shieldImpactPos = new Vec3(center.x + dir.x * rx, center.y + dir.y * ry, center.z + dir.z * rz);
        Vec3 normal = dir;
        Vec3 v = projVel;
        double speed = Math.max(1.4, v.length() * 1.25);
        Vec3 vNew = v.subtract(normal.scale(2.0 * v.dot(normal)));
        vNew = vNew.dot(normal) < 0.25 ? normal.scale(speed) : vNew.normalize().scale(speed);
        double pushDist = 0.35;
        double newX = shieldImpactPos.x + normal.x * pushDist;
        double newY = shieldImpactPos.y + normal.y * pushDist;
        double newZ = shieldImpactPos.z + normal.z * pushDist;
        projectile.setPos(newX, newY, newZ);
        projectile.setDeltaMovement(vNew);
        projectile.hasImpulse = true;
        projectile.hurtMarked = true;
        double horizSpeed = Math.sqrt(vNew.x * vNew.x + vNew.z * vNew.z);
        float yRot = (float)(Mth.atan2((double)vNew.x, (double)vNew.z) * 57.29577951308232);
        float xRot = (float)(Mth.atan2((double)vNew.y, (double)horizSpeed) * 57.29577951308232);
        projectile.setYRot(yRot);
        projectile.setXRot(xRot);
        projectile.yRotO = yRot;
        projectile.xRotO = xRot;
        if (projectile instanceof AbstractArrow) {
            AbstractArrow arrow = (AbstractArrow)projectile;
            arrow.setCritArrow(false);
            arrow.setOwner((Entity)player);
            arrow.tickCount = 0;
        }
        if ((level = projectile.level()) instanceof ServerLevel) {
            ServerLevel sl = (ServerLevel)level;
            sl.getChunkSource().broadcast((Entity)projectile, (Packet)new ClientboundSetEntityMotionPacket((Entity)projectile));
            sl.getChunkSource().broadcast((Entity)projectile, (Packet)new ClientboundTeleportEntityPacket((Entity)projectile));
        }
        boolean bl = isBreak = (remaining = EvilMortyEnergyShieldItem.consumeHeart((ItemStack)shield, (Player)player)) == 0;
        if (isBreak) {
            player.level().playSound(null, shieldImpactPos.x, shieldImpactPos.y, shieldImpactPos.z, SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.5f, 0.8f + player.getRandom().nextFloat() * 0.2f);
            player.level().playSound(null, shieldImpactPos.x, shieldImpactPos.y, shieldImpactPos.z, SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 0.8f, 1.6f);
        } else {
            player.level().playSound(null, shieldImpactPos.x, shieldImpactPos.y, shieldImpactPos.z, (SoundEvent)ModSounds.ANTI_PORTAL_DEFLECT.get(), SoundSource.PLAYERS, 1.2f, 1.1f + player.getRandom().nextFloat() * 0.2f);
        }
        S2CEvilMortyShieldEffectPacket packet = new S2CEvilMortyShieldEffectPacket(player.getId(), shieldImpactPos.x, shieldImpactPos.y, shieldImpactPos.z, dir.x, dir.y, dir.z, isBreak);
        ModMessages.sendToNearby((CustomPacketPayload)packet, (Level)player.level(), (double)player.getX(), (double)player.getY(), (double)player.getZ(), (double)64.0);
        if (!(player instanceof ServerPlayer)) return;
        ServerPlayer sp = (ServerPlayer)player;
        ModMessages.sendToPlayer((CustomPacketPayload)packet, (ServerPlayer)sp);
    }

    public static void triggerShieldDeflect(Player player, Vec3 sourcePos) {
        boolean isBreak;
        Vec3 diff;
        ItemStack shield = CuriosCompat.findShield((Player)player);
        if (!EvilMortyEnergyShieldItem.isShieldActive((ItemStack)shield)) {
            return;
        }
        Vec3 center = player.position().add(0.0, (double)player.getEyeHeight() * 0.5, 0.0);
        Vec3 dir = sourcePos != null ? ((diff = sourcePos.subtract(center)).lengthSqr() > 1.0E-4 ? diff.normalize() : player.getLookAngle()) : player.getLookAngle();
        double rx = 1.35;
        double ry = 1.45;
        double rz = 1.35;
        Vec3 shieldImpactPos = new Vec3(center.x + dir.x * rx, center.y + dir.y * ry, center.z + dir.z * rz);
        int remaining = EvilMortyEnergyShieldItem.consumeHeart((ItemStack)shield, (Player)player);
        boolean bl = isBreak = remaining == 0;
        if (isBreak) {
            player.level().playSound(null, shieldImpactPos.x, shieldImpactPos.y, shieldImpactPos.z, SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.5f, 0.8f + player.getRandom().nextFloat() * 0.2f);
            player.level().playSound(null, shieldImpactPos.x, shieldImpactPos.y, shieldImpactPos.z, SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 0.8f, 1.6f);
        } else {
            player.level().playSound(null, shieldImpactPos.x, shieldImpactPos.y, shieldImpactPos.z, (SoundEvent)ModSounds.ANTI_PORTAL_DEFLECT.get(), SoundSource.PLAYERS, 1.2f, 1.1f + player.getRandom().nextFloat() * 0.2f);
        }
        S2CEvilMortyShieldEffectPacket packet = new S2CEvilMortyShieldEffectPacket(player.getId(), shieldImpactPos.x, shieldImpactPos.y, shieldImpactPos.z, dir.x, dir.y, dir.z, isBreak);
        ModMessages.sendToNearby((CustomPacketPayload)packet, (Level)player.level(), (double)player.getX(), (double)player.getY(), (double)player.getZ(), (double)64.0);
        if (!(player instanceof ServerPlayer)) return;
        ServerPlayer sp = (ServerPlayer)player;
        ModMessages.sendToPlayer((CustomPacketPayload)packet, (ServerPlayer)sp);
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        MinecraftServer server;
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer)) return;
        ServerPlayer serverPlayer = (ServerPlayer)player;
        ModMessages.sendToPlayer((CustomPacketPayload)S2CSyncConfigPacket.createFromCurrent(), (ServerPlayer)serverPlayer);
        if (serverPlayer.getPersistentData().getBoolean("IsTinyRick")) {
            CommonEvents.applyTinyRick(serverPlayer, true);
        }
        if ((server = serverPlayer.getServer()) == null) return;
        MultiverseErasureSavedData storage = MultiverseErasureSavedData.get((MinecraftServer)server);
        if (storage.isErased(serverPlayer.getUUID())) {
            OmegaDeviceManager.handlePlayerErasure((ServerPlayer)serverPlayer);
        } else if (serverPlayer.getPersistentData().getBoolean("OmegaErased")) {
            OmegaDeviceManager.handlePlayerUnerasure((ServerPlayer)serverPlayer);
        }
        ErasurePurgeHelper.purgePlayerInventories((ServerPlayer)serverPlayer, (MultiverseErasureSavedData)storage);
        OmegaBlockErasureManager.queueAroundPlayer((ServerPlayer)serverPlayer, (int)6);
        ModMessages.sendToPlayer((CustomPacketPayload)new S2CSyncMultiverseErasurePacket(storage), (ServerPlayer)serverPlayer);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer)) return;
        ServerPlayer serverPlayer = (ServerPlayer)player;
        MinecraftServer server = serverPlayer.getServer();
        if (server == null) return;
        MultiverseErasureSavedData storage = MultiverseErasureSavedData.get((MinecraftServer)server);
        ErasurePurgeHelper.purgePlayerInventories((ServerPlayer)serverPlayer, (MultiverseErasureSavedData)storage);
        ModMessages.sendToPlayer((CustomPacketPayload)new S2CSyncMultiverseErasurePacket(storage), (ServerPlayer)serverPlayer);
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer)) return;
        ServerPlayer serverPlayer = (ServerPlayer)player;
        MinecraftServer server = serverPlayer.getServer();
        if (server == null) return;
        MultiverseErasureSavedData storage = MultiverseErasureSavedData.get((MinecraftServer)server);
        ErasurePurgeHelper.purgePlayerInventories((ServerPlayer)serverPlayer, (MultiverseErasureSavedData)storage);
        OmegaBlockErasureManager.queueAroundPlayer((ServerPlayer)serverPlayer, (int)6);
        ModMessages.sendToPlayer((CustomPacketPayload)new S2CSyncMultiverseErasurePacket(storage), (ServerPlayer)serverPlayer);
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        MultiverseErasureSavedData storage;
        MinecraftServer server = event.getPlayerList() != null ? event.getPlayerList().getServer() : (event.getPlayer() != null ? event.getPlayer().getServer() : null);
        MultiverseErasureSavedData multiverseErasureSavedData = storage = server != null ? MultiverseErasureSavedData.get((MinecraftServer)server) : null;
        if (event.getPlayer() != null) {
            ModMessages.sendToPlayer((CustomPacketPayload)S2CSyncConfigPacket.createFromCurrent(), (ServerPlayer)event.getPlayer());
            if (storage == null) return;
            ModMessages.sendToPlayer((CustomPacketPayload)new S2CSyncMultiverseErasurePacket(storage), (ServerPlayer)event.getPlayer());
            return;
        }
        if (event.getPlayerList() == null) return;
        Iterator iterator = event.getPlayerList().getPlayers().iterator();
        while (iterator.hasNext()) {
            ServerPlayer player = (ServerPlayer)iterator.next();
            ModMessages.sendToPlayer((CustomPacketPayload)S2CSyncConfigPacket.createFromCurrent(), (ServerPlayer)player);
            if (storage == null) continue;
            ModMessages.sendToPlayer((CustomPacketPayload)new S2CSyncMultiverseErasurePacket(storage), (ServerPlayer)player);
        }
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Entity serverLevel;
        Level player;
        Entity itemEntity;
        if (event.getLevel().isClientSide()) return;
        MinecraftServer server = event.getEntity().getServer();
        if (server != null) {
            MultiverseErasureSavedData storage = MultiverseErasureSavedData.get((MinecraftServer)server);
            Entity entity = event.getEntity();
            if (entity instanceof ItemEntity) {
                itemEntity = (ItemEntity)entity;
                ItemStack stack = itemEntity.getItem();
                if (!stack.isEmpty() && storage.isItemErased(stack.getItem())) {
                    event.setCanceled(true);
                    itemEntity.discard();
                    return;
                }
            } else if (entity instanceof Player) {
                player = (Player)entity;
                if (storage.isErased(player.getUUID())) {
                    OmegaDeviceManager.handlePlayerErasure((ServerPlayer)((ServerPlayer)player));
                }
            } else if (storage.isErased(entity.getUUID()) || storage.isTypeErased(entity.getType())) {
                event.setCanceled(true);
                entity.discard();
                return;
            }
        }
        if ((itemEntity = event.getEntity()) instanceof PortalEntity) {
            PortalEntity portal = (PortalEntity)itemEntity;
            player = event.getLevel();
            if (!(player instanceof ServerLevel)) return;
            serverLevel = (ServerLevel)player;
            if (serverLevel.getServer() == null) return;
            PortalSavedData data = PortalSavedData.get((MinecraftServer)serverLevel.getServer());
            if (!data.isTombstoned(portal.getUUID())) return;
            data.removeTombstone(portal.getUUID());
            event.setCanceled(true);
            portal.discard();
            return;
        }
        serverLevel = event.getEntity();
        if (!(serverLevel instanceof ItemEntity)) return;
        ItemEntity itemEntity2 = (ItemEntity)serverLevel;
        ItemStack stack = itemEntity2.getItem();
        if (!(stack.getItem() instanceof PortalGunItem)) {
            if (!(stack.getItem() instanceof DischargedPortalGunItem)) return;
        }
        DROPPED_GUN_ENTITIES.add(itemEntity2.getId());
    }

    @SubscribeEvent
    public static void onItemPickup(ItemEntityPickupEvent.Post event) {
        ItemEntity itemEntity = event.getItemEntity();
        if (itemEntity == null) return;
        ItemStack stack = itemEntity.getItem();
        if (!(stack.getItem() instanceof PortalGunItem)) {
            if (!(stack.getItem() instanceof DischargedPortalGunItem)) return;
        }
        PICKED_UP_GUN_ENTITIES.add(itemEntity.getId());
    }

    @SubscribeEvent
    public static void onEntityLeaveLevel(EntityLeaveLevelEvent event) {
        int gunId;
        if (event.getLevel().isClientSide()) return;
        Entity entity = event.getEntity();
        if (!(entity instanceof ItemEntity)) return;
        ItemEntity itemEntity = (ItemEntity)entity;
        int entityId = itemEntity.getId();
        if (!DROPPED_GUN_ENTITIES.remove(entityId)) return;
        boolean wasPickedUp = PICKED_UP_GUN_ENTITIES.remove(entityId);
        if (wasPickedUp) return;
        Level level = event.getLevel();
        if (!(level instanceof ServerLevel)) return;
        ServerLevel serverLevel = (ServerLevel)level;
        ItemStack stack = itemEntity.getItem();
        if (!(stack.getItem() instanceof PortalGunItem)) {
            if (!(stack.getItem() instanceof DischargedPortalGunItem)) return;
        }
        if ((gunId = PortalGunItem.getGunId((ItemStack)stack)) == 0) return;
        PortalManager.removeAllPortalsForGun((ServerLevel)serverLevel, (int)gunId);
    }

    @SubscribeEvent
    public static void onItemExpire(ItemExpireEvent event) {
        int gunId;
        if (event.getEntity().level().isClientSide()) return;
        if (event.getEntity() == null) return;
        ItemEntity itemEntity = event.getEntity();
        ItemStack stack = itemEntity.getItem();
        if (!(stack.getItem() instanceof PortalGunItem)) {
            if (!(stack.getItem() instanceof DischargedPortalGunItem)) return;
        }
        if ((gunId = PortalGunItem.getGunId((ItemStack)stack)) == 0) return;
        Level level = itemEntity.level();
        if (!(level instanceof ServerLevel)) return;
        ServerLevel serverLevel = (ServerLevel)level;
        PortalManager.removeAllPortalsForGun((ServerLevel)serverLevel, (int)gunId);
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        int id;
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer)) return;
        ServerPlayer player2 = (ServerPlayer)player;
        if (!((player = player2.level()) instanceof ServerLevel)) return;
        ServerLevel serverLevel = (ServerLevel)player;
        HashSet<Integer> gunIds = new HashSet<Integer>();
        for (ItemStack stack : player2.getInventory().items) {
            if (!CommonEvents.isPortalGun(stack) || (id = PortalGunItem.getGunId((ItemStack)stack)) == 0) continue;
            gunIds.add(id);
        }
        for (ItemStack stack : player2.getInventory().offhand) {
            if (!CommonEvents.isPortalGun(stack) || (id = PortalGunItem.getGunId((ItemStack)stack)) == 0) continue;
            gunIds.add(id);
        }
        for (ItemStack stack : player2.getInventory().armor) {
            if (!CommonEvents.isPortalGun(stack) || (id = PortalGunItem.getGunId((ItemStack)stack)) == 0) continue;
            gunIds.add(id);
        }
        Iterator iterator = gunIds.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                PuppetServerManager.disconnectForPlayer((UUID)player2.getUUID());
                return;
            }
            int gunId = (Integer)iterator.next();
            PortalManager.removeAllPortalsForGun((ServerLevel)serverLevel, (int)gunId);
        }
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        PortalTeleportEngine.tickServer();
        OmegaBlockErasureManager.tick((MinecraftServer)event.getServer());
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public static void onLivingFall(LivingFallEvent event) {
        PortalEntity p;
        Object object;
        LivingEntity entity = event.getEntity();
        if (entity == null) {
            return;
        }
        if (entity.getVehicle() instanceof SpaceCruiserEntity) {
            event.setCanceled(true);
            event.setDistance(0.0f);
            entity.resetFallDistance();
            return;
        }
        if (Config.omegaDeviceModelEnabled) {
            List nearbyDevices = entity.level().getEntitiesOfClass(OmegaDeviceEntity.class, entity.getBoundingBox().inflate(32.0));
            object = nearbyDevices.iterator();
            while (object.hasNext()) {
                double relZ;
                double relY;
                OmegaDeviceEntity device = (OmegaDeviceEntity)object.next();
                double relX = entity.getX() - device.getX();
                Double groundY = OmegaDeviceCollider.getGroundHeightAt((double)relX, (double)(relY = entity.getY() - device.getY()), (double)(relZ = entity.getZ() - device.getZ()));
                if (groundY == null || !(Math.abs(device.getY() + groundY - entity.getY()) <= 1.0)) continue;
                event.setCanceled(true);
                event.setDistance(0.0f);
                entity.resetFallDistance();
                return;
            }
        }
        if (!((object = entity.level()) instanceof ServerLevel)) return;
        ServerLevel serverLevel = (ServerLevel)object;
        AABB checkArea = entity.getBoundingBox().inflate(0.5, 1.5, 0.5);
        List portals = serverLevel.getEntitiesOfClass(PortalEntity.class, checkArea);
        Iterator iterator = portals.iterator();
        do {
            if (!iterator.hasNext()) return;
        } while ((p = (PortalEntity)iterator.next()).isRemoved() || p.isClosing());
        event.setCanceled(true);
        event.setDistance(0.0f);
        entity.resetFallDistance();
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        DamageSource src;
        DamageSource source;
        Player player;
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof Player && CuriosCompat.hasActiveShield((Player)(player = (Player)livingEntity)) && !(source = event.getSource()).is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            boolean isProjectile;
            boolean bl = isProjectile = source.is(DamageTypeTags.IS_PROJECTILE) || source.getDirectEntity() instanceof Projectile;
            boolean shouldDeflect = Config.evilMortyShieldProtectAllDamage ? isProjectile || source.is(DamageTypeTags.IS_EXPLOSION) || source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.MOB_ATTACK_NO_AGGRO) || source.is(DamageTypes.PLAYER_ATTACK) || source.getEntity() instanceof Mob || source.getEntity() instanceof Player && source.getEntity() != player : isProjectile;
            if (shouldDeflect) {
                event.setCanceled(true);
                Entity entity = source.getDirectEntity();
                if (entity instanceof Projectile) {
                    Projectile projectile = (Projectile)entity;
                    CommonEvents.deflectProjectileWithShield(player, projectile);
                } else {
                    Vec3 srcPos = source.getSourcePosition() != null ? source.getSourcePosition() : (source.getEntity() != null ? source.getEntity().position() : null);
                    CommonEvents.triggerShieldDeflect(player, srcPos);
                }
                return;
            }
        }
        if (event.getEntity() != null && event.getEntity().getVehicle() instanceof SpaceCruiserEntity && ((src = event.getSource()).is(DamageTypes.FALL) || src.is(DamageTypeTags.IS_FALL) || src.is(DamageTypes.FLY_INTO_WALL) || src.is(DamageTypes.IN_WALL) || src.is(DamageTypes.DROWN) || src.is(DamageTypes.FREEZE))) {
            event.setCanceled(true);
            event.getEntity().resetFallDistance();
            return;
        }
        if (!event.getSource().is(DamageTypes.FALL)) {
            if (!event.getSource().is(DamageTypeTags.IS_FALL)) return;
        }
        if (!((source = event.getEntity().level()) instanceof ServerLevel)) return;
        ServerLevel serverLevel = (ServerLevel)source;
        AABB checkArea = event.getEntity().getBoundingBox().inflate(0.5, 1.5, 0.5);
        List portals = serverLevel.getEntitiesOfClass(PortalEntity.class, checkArea);
        if (portals.isEmpty()) return;
        event.setCanceled(true);
        event.getEntity().resetFallDistance();
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        BlockState state;
        MultiverseErasureSavedData storage;
        ServerLevel serverLevel;
        LevelAccessor levelAccessor;
        if (!event.getLevel().isClientSide() && Config.allowBlockErasure && (levelAccessor = event.getLevel()) instanceof ServerLevel && (serverLevel = (ServerLevel)levelAccessor).getServer() != null && (storage = MultiverseErasureSavedData.get((MinecraftServer)serverLevel.getServer())) != null && !storage.getErasedItemTypes().isEmpty() && storage.isBlockErased(state = event.getState())) {
            event.setCanceled(true);
            serverLevel.setBlock(event.getPos(), Blocks.AIR.defaultBlockState(), 3);
            return;
        }
        Player player = event.getPlayer();
        if (player == null) return;
        if (!player.isCreative()) return;
        ItemStack main = player.getMainHandItem();
        ItemStack off = player.getOffhandItem();
        if (!CommonEvents.isPortalGun(main)) {
            if (!CommonEvents.isPortalGun(off)) return;
        }
        event.setCanceled(true);
    }

    private static boolean isPortalGun(ItemStack stack) {
        if (stack == null) return false;
        if (!stack.isEmpty()) Item item;
        return (item = stack.getItem()) instanceof PortalGunItem || item instanceof DischargedPortalGunItem;
        return false;
    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        Entity entity = event.getTarget();
        if (entity instanceof SpaceCruiserEntity) {
            SpaceCruiserEntity ship = (SpaceCruiserEntity)entity;
            ship.level().playSound(null, ship.getX(), ship.getY(), ship.getZ(), (SoundEvent)ModSounds.SPACE_CRUISER_HURT.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        }
        if (!((entity = event.getTarget()) instanceof PortalEntity)) return;
        PortalEntity portal = (PortalEntity)entity;
        ItemStack held = player.getMainHandItem();
        if (!(held.getItem() instanceof PortalGunItem)) {
            if (!(held.getItem() instanceof DischargedPortalGunItem)) return;
        }
        event.setCanceled(true);
        if (player.level().isClientSide) return;
        boolean removeLinked = player.isShiftKeyDown() || "CUSTOM".equalsIgnoreCase(portal.getGunMode());
        PortalManager.removePortal((PortalEntity)portal, (boolean)removeLinked);
    }

    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        Player player;
        if (event.getProjectile().level().isClientSide) {
            return;
        }
        HitResult hitResult = event.getRayTraceResult();
        if (!(hitResult instanceof EntityHitResult)) return;
        EntityHitResult hitResult2 = (EntityHitResult)hitResult;
        Entity entity = hitResult2.getEntity();
        if (entity instanceof Player && CuriosCompat.hasActiveShield((Player)(player = (Player)entity))) {
            event.setCanceled(true);
            CommonEvents.deflectProjectileWithShield(player, event.getProjectile());
            return;
        }
        entity = hitResult2.getEntity();
        if (!(entity instanceof PortalEntity)) return;
        PortalEntity portal = (PortalEntity)entity;
        Projectile projectile = event.getProjectile();
        if (projectile instanceof FishingHook) {
            FishingHook hook = (FishingHook)projectile;
            hook.discard();
            event.setCanceled(true);
            return;
        }
        if (event.getProjectile() instanceof PortalProjectileEntity) {
            return;
        }
        Projectile projectile2 = event.getProjectile();
        event.setCanceled(true);
        if (projectile2.level().isClientSide) return;
        portal.handleEntityTeleport((Entity)projectile2);
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity server;
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer)livingEntity;
            PuppetServerManager.disconnectForPlayer((UUID)player.getUUID());
            server = player.getServer();
            if (server != null && MultiverseErasureSavedData.get((MinecraftServer)server).isErased(player.getUUID())) {
                OmegaDeviceManager.shatterPhoenixCapsules((MinecraftServer)server, (UUID)player.getUUID());
                return;
            }
            if (Config.phoenixProtocolEnabled && server != null && PhoenixManager.hasReadyCapsule((MinecraftServer)server, (UUID)player.getUUID())) {
                PhoenixManager.CapsuleLocation loc = null;
                PhoenixCapsuleBlockEntity readyCapsule = null;
                ServerLevel targetLevel = null;
                while (PhoenixManager.hasReadyCapsule((MinecraftServer)server, (UUID)player.getUUID())) {
                    PhoenixCapsuleBlockEntity capsule;
                    loc = PhoenixManager.popNextReadyCapsule((MinecraftServer)server, (UUID)player.getUUID());
                    if (loc == null || !Config.phoenixCrossDimensionSpawn && !loc.dimension.equals(player.level().dimension()) || (targetLevel = server.getLevel(loc.dimension)) == null) continue;
                    targetLevel.getChunk(loc.pos);
                    BlockEntity be = targetLevel.getBlockEntity(loc.pos);
                    if (!(be instanceof PhoenixCapsuleBlockEntity) || !(capsule = (PhoenixCapsuleBlockEntity)be).isReady() || capsule.isBroken()) continue;
                    readyCapsule = capsule;
                    break;
                }
                if (readyCapsule != null && targetLevel != null && loc != null) {
                    event.setCanceled(true);
                    player.setHealth(player.getMaxHealth());
                    player.removeAllEffects();
                    player.clearFire();
                    player.resetFallDistance();
                    double tx = (double)loc.pos.getX() + 0.5;
                    double ty = (double)loc.pos.getY() + 0.2;
                    double tz = (double)loc.pos.getZ() + 0.5;
                    if (player.level() != targetLevel) {
                        player.teleportTo(targetLevel, tx, ty, tz, player.getYRot(), player.getXRot());
                    } else {
                        player.teleportTo(tx, ty, tz);
                    }
                    CommonEvents.equipOrGive(player, EquipmentSlot.HEAD, readyCapsule.getItemHandler().getStackInSlot(5));
                    CommonEvents.equipOrGive(player, EquipmentSlot.CHEST, readyCapsule.getItemHandler().getStackInSlot(6));
                    CommonEvents.equipOrGive(player, EquipmentSlot.LEGS, readyCapsule.getItemHandler().getStackInSlot(7));
                    CommonEvents.equipOrGive(player, EquipmentSlot.FEET, readyCapsule.getItemHandler().getStackInSlot(8));
                    CommonEvents.equipOrGive(player, EquipmentSlot.MAINHAND, readyCapsule.getItemHandler().getStackInSlot(9));
                    CommonEvents.equipOrGive(player, EquipmentSlot.OFFHAND, readyCapsule.getItemHandler().getStackInSlot(10));
                    readyCapsule.getItemHandler().setStackInSlot(5, ItemStack.EMPTY);
                    readyCapsule.getItemHandler().setStackInSlot(6, ItemStack.EMPTY);
                    readyCapsule.getItemHandler().setStackInSlot(7, ItemStack.EMPTY);
                    readyCapsule.getItemHandler().setStackInSlot(8, ItemStack.EMPTY);
                    readyCapsule.getItemHandler().setStackInSlot(9, ItemStack.EMPTY);
                    readyCapsule.getItemHandler().setStackInSlot(10, ItemStack.EMPTY);
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 1));
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 2));
                    player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0));
                    int catalyst = readyCapsule.getCatalystType();
                    boolean corrupted = readyCapsule.isCorrupted();
                    if (catalyst == 1) {
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 1));
                        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 400, 1));
                    } else if (catalyst == 2) {
                        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 1));
                        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1));
                    } else if (catalyst == 3) {
                        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 3));
                        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 400, 4));
                        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2400, 1));
                        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 6000, 0));
                    } else if (catalyst == 4) {
                        CommonEvents.applyTinyRick(player, true);
                    } else if (player.getPersistentData().getBoolean("IsTinyRick")) {
                        CommonEvents.applyTinyRick(player, false);
                    }
                    if (corrupted) {
                        player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 400, 1));
                        player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
                        player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 300, 0));
                    }
                    ModMessages.sendToPlayer((CustomPacketPayload)new S2CPhoenixResurrectionPacket(2400L, catalyst, corrupted), (ServerPlayer)player);
                    readyCapsule.triggerResurrection(player);
                }
            }
        }
        if (!((server = event.getEntity()) instanceof Mob)) return;
        Mob mob = (Mob)server;
        if (mob.level().isClientSide()) return;
        PuppetServerManager.disconnect((int)mob.getId(), (Mob)mob);
        CompoundTag data = mob.getPersistentData();
        if (!data.getBoolean("BioNeuralChipped")) return;
        if (data.getBoolean("BioNeuralSelfDestructed")) return;
        data.remove("BioNeuralChipped");
        data.remove("BioNeuralOwner");
        data.remove("BioNeuralOwnerName");
        data.remove("LastPossessedTick");
        data.remove("PossessingPlayerUUID");
        data.remove("DrawingBow");
        data.remove("BowDrawStart");
    }

    @SubscribeEvent(priority=EventPriority.LOWEST)
    public static void onPlayerDeathBombTrigger(LivingDeathEvent event) {
        if (event.isCanceled()) {
            return;
        }
        LivingEntity livingEntity = event.getEntity();
        if (!(livingEntity instanceof ServerPlayer)) return;
        ServerPlayer player = (ServerPlayer)livingEntity;
        boolean hadBomb = false;
        for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.isEmpty() || !stack.is((Item)ModItems.RICK_PRIME_BOMB.get())) continue;
            stack.shrink(1);
            hadBomb = true;
            break;
        }
        if (!hadBomb) return;
        RickPrimeBombEntity bomb = new RickPrimeBombEntity(player.level(), player.getX(), player.getY() + 0.25, player.getZ());
        bomb.setArmed(true);
        bomb.setDeltaMovement((player.level().random.nextDouble() - 0.5) * 0.35, 0.28, (player.level().random.nextDouble() - 0.5) * 0.35);
        player.level().addFreshEntity((Entity)bomb);
    }

    private static void equipOrGive(ServerPlayer player, EquipmentSlot slot, ItemStack stack) {
        if (stack == null) return;
        if (stack.isEmpty()) {
            return;
        }
        ItemStack existing = player.getItemBySlot(slot);
        if (existing.isEmpty()) {
            player.setItemSlot(slot, stack.copy());
        } else {
            if (player.getInventory().add(stack.copy())) return;
            player.drop(stack.copy(), false);
        }
    }

    public static void applyTinyRick(ServerPlayer player, boolean isTiny) {
        player.getPersistentData().putBoolean("IsTinyRick", isTiny);
        AttributeInstance healthAttr = player.getAttribute(Attributes.MAX_HEALTH);
        AttributeInstance attackAttr = player.getAttribute(Attributes.ATTACK_DAMAGE);
        AttributeInstance speedAttr = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (healthAttr != null) {
            healthAttr.removeModifier(TINY_RICK_HEALTH_ID);
            if (isTiny) {
                healthAttr.addPermanentModifier(new AttributeModifier(TINY_RICK_HEALTH_ID, -10.0, AttributeModifier.Operation.ADD_VALUE));
                if (player.getHealth() > player.getMaxHealth()) {
                    player.setHealth(player.getMaxHealth());
                }
            }
        }
        if (attackAttr != null) {
            attackAttr.removeModifier(TINY_RICK_ATTACK_ID);
            if (isTiny) {
                attackAttr.addPermanentModifier(new AttributeModifier(TINY_RICK_ATTACK_ID, -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            }
        }
        if (speedAttr != null) {
            speedAttr.removeModifier(TINY_RICK_SPEED_ID);
            if (isTiny) {
                speedAttr.addPermanentModifier(new AttributeModifier(TINY_RICK_SPEED_ID, 0.4, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            }
        }
        player.refreshDimensions();
        ModMessages.sendToPlayer((CustomPacketPayload)new S2CSyncPlayerScalePacket(player.getId(), isTiny), (ServerPlayer)player);
        ModMessages.sendToClients((CustomPacketPayload)new S2CSyncPlayerScalePacket(player.getId(), isTiny));
    }

    @SubscribeEvent
    public static void onEntitySize(EntityEvent.Size event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) return;
        Player player = (Player)entity;
        if (!player.getPersistentData().getBoolean("IsTinyRick")) return;
        event.setNewSize(event.getNewSize().scale(0.5f));
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        Entity entity = event.getTarget();
        if (!(entity instanceof ServerPlayer)) return;
        ServerPlayer targetPlayer = (ServerPlayer)entity;
        entity = event.getEntity();
        if (!(entity instanceof ServerPlayer)) return;
        ServerPlayer viewer = (ServerPlayer)entity;
        if (!targetPlayer.getPersistentData().getBoolean("IsTinyRick")) return;
        ModMessages.sendToPlayer((CustomPacketPayload)new S2CSyncPlayerScalePacket(targetPlayer.getId(), true), (ServerPlayer)viewer);
    }

    @SubscribeEvent
    public static void onContainerOpen(PlayerContainerEvent.Open event) {
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer)) return;
        ServerPlayer serverPlayer = (ServerPlayer)player;
        MinecraftServer server = serverPlayer.getServer();
        if (server == null) return;
        MultiverseErasureSavedData storage = MultiverseErasureSavedData.get((MinecraftServer)server);
        if (storage == null) return;
        if (storage.getErasedItemTypes().isEmpty()) return;
        ErasurePurgeHelper.purgeMenu((AbstractContainerMenu)event.getContainer(), (MultiverseErasureSavedData)storage);
    }

    @SubscribeEvent
    public static void onContainerClose(PlayerContainerEvent.Close event) {
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer)) return;
        ServerPlayer serverPlayer = (ServerPlayer)player;
        MinecraftServer server = serverPlayer.getServer();
        if (server == null) return;
        MultiverseErasureSavedData storage = MultiverseErasureSavedData.get((MinecraftServer)server);
        if (storage == null) return;
        if (storage.getErasedItemTypes().isEmpty()) return;
        ErasurePurgeHelper.purgeMenu((AbstractContainerMenu)event.getContainer(), (MultiverseErasureSavedData)storage);
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getLevel().isClientSide()) return;
        if (!Config.allowBlockErasure) return;
        Level level = event.getLevel();
        if (!(level instanceof ServerLevel)) return;
        ServerLevel serverLevel = (ServerLevel)level;
        if (serverLevel.getServer() == null) return;
        MultiverseErasureSavedData storage = MultiverseErasureSavedData.get((MinecraftServer)serverLevel.getServer());
        if (storage == null) return;
        if (storage.getErasedItemTypes().isEmpty()) return;
        BlockState state = serverLevel.getBlockState(event.getPos());
        if (!storage.isBlockErased(state)) return;
        event.setCanceled(true);
        serverLevel.setBlock(event.getPos(), Blocks.AIR.defaultBlockState(), 3);
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer)) return;
        ServerPlayer sp = (ServerPlayer)player;
        ErasurePurgeHelper.purgePlayerInventories((ServerPlayer)sp, (MultiverseErasureSavedData)storage);
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (!Config.allowBlockErasure) return;
        LevelAccessor levelAccessor = event.getLevel();
        if (!(levelAccessor instanceof ServerLevel)) return;
        ServerLevel serverLevel = (ServerLevel)levelAccessor;
        if (serverLevel.getServer() == null) return;
        MultiverseErasureSavedData storage = MultiverseErasureSavedData.get((MinecraftServer)serverLevel.getServer());
        if (storage == null) return;
        if (storage.getErasedItemTypes().isEmpty()) return;
        BlockState state = event.getPlacedBlock();
        if (!storage.isBlockErased(state)) return;
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onChunkWatch(ChunkWatchEvent.Watch event) {
        if (event.getLevel().isClientSide()) return;
        if (!Config.allowBlockErasure) return;
        ServerLevel serverLevel = event.getLevel();
        if (serverLevel.getServer() == null) return;
        MultiverseErasureSavedData storage = MultiverseErasureSavedData.get((MinecraftServer)serverLevel.getServer());
        if (storage == null) return;
        if (storage.getErasedItemTypes().isEmpty()) return;
        OmegaBlockErasureManager.cleanChunk((ServerLevel)serverLevel, (ChunkPos)event.getPos(), (MultiverseErasureSavedData)storage);
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        OmegaCommand.register((CommandDispatcher)event.getDispatcher(), (CommandBuildContext)event.getBuildContext());
    }

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        ItemStack crafted = event.getCrafting();
        if (event.getEntity() == null) return;
        if (event.getEntity().getServer() == null) return;
        MultiverseErasureSavedData storage = MultiverseErasureSavedData.get((MinecraftServer)event.getEntity().getServer());
        if (storage == null) return;
        if (storage.getErasedItemTypes().isEmpty()) return;
        if (crafted.isEmpty()) return;
        if (!storage.isItemErased(crafted.getItem())) return;
        crafted.setCount(0);
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer)) return;
        ServerPlayer sp = (ServerPlayer)player;
        ErasurePurgeHelper.purgePlayerInventories((ServerPlayer)sp, (MultiverseErasureSavedData)storage);
    }

    @SubscribeEvent
    public static void onItemToss(ItemTossEvent event) {
        ItemEntity itemEntity = event.getEntity();
        if (itemEntity == null) return;
        if (itemEntity.level().isClientSide) return;
        ItemStack stack = itemEntity.getItem();
        if (stack.isEmpty()) return;
        if (event.getPlayer() == null) return;
        if (event.getPlayer().getServer() == null) return;
        MultiverseErasureSavedData storage = MultiverseErasureSavedData.get((MinecraftServer)event.getPlayer().getServer());
        if (!storage.isItemErased(stack.getItem())) return;
        event.setCanceled(true);
        itemEntity.discard();
    }

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity)) return;
        LivingEntity entity2 = (LivingEntity)entity;
        if (entity2.level().isClientSide) {
            return;
        }
        if (!Config.omegaDeviceModelEnabled) return;
        List devices = entity2.level().getEntitiesOfClass(OmegaDeviceEntity.class, entity2.getBoundingBox().inflate(35.0));
        Iterator iterator = devices.iterator();
        while (iterator.hasNext()) {
            OmegaDeviceEntity device = (OmegaDeviceEntity)iterator.next();
            OmegaDeviceCollider.handleEntityCollision((LivingEntity)entity2, (OmegaDeviceEntity)device);
        }
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        MultiverseErasureSavedData.resetCache();
        OmegaBlockErasureManager.clearQueue();
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteractSpecific event) {
        Entity entity = event.getTarget();
        if (!(entity instanceof EnderDragonPart)) return;
        EnderDragonPart part = (EnderDragonPart)entity;
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof BioNeuralReceiverItem)) return;
        if (!player.isShiftKeyDown()) return;
        EnderDragon dragon = part.parentMob;
        if (dragon == null) return;
        if (!dragon.isAlive()) return;
        if (event.getLevel().isClientSide()) {
            ClientProxy.startImplanting((EnderDragon)dragon);
        }
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.sidedSuccess((boolean)event.getLevel().isClientSide()));
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        CuriosCompat.tickPlayerShield((Player)event.getEntity());
    }
}

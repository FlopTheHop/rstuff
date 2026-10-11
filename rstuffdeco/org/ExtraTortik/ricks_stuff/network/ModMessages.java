/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.level.Level
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.network.PacketDistributor
 *  net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
 *  net.neoforged.neoforge.network.registration.PayloadRegistrar
 *  net.neoforged.neoforge.server.ServerLifecycleHooks
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SAbortWarpPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SActivateOmegaDevicePacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SCloseAllPortalsPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SColorizeFluidPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SCruiserDeleteLocationPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SCruiserSaveLocationPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SDeleteLocationPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SDismountGunPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SEjectMemoryCardPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SEjectUpgradePacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SImplantChipPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SInitiateWarpPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SInstallUpgradePacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SPuppetAbilityPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SPuppetConnectPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SPuppetControlPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SPuppetDisconnectPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SPuppetSelfDestructPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SRepairCapsulePacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SRequestPuppetListPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SResetGunPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SSaveLocationPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SScanEntityPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SSelectModePacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SSelectSavedLocationPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SSelfDestructPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SSetCoordinatesPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SShipControlPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SStartClonePrintPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2STargetBiomePacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2STargetPlayerPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2STargetStructurePacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SUpdateAntiPortalConfigPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SUpdateGunSettingsPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CBiomeFoundPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CEvilMortyShieldEffectPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CFluidHitEffectPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CGunRecoilPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.S2COmegaDeviceEffectPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.S2COpenGunMenuPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CPhoenixResurrectionPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CPuppetDisconnectPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CPuppetListPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CScanResultPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CStructureFoundPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CSyncConfigPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CSyncCruiserLocationsPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CSyncMultiverseErasurePacket
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CSyncPlayerScalePacket
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CSyncWarpSequencePacket
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CTeleportOverlayPacket
 */
package org.ExtraTortik.ricks_stuff.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.ExtraTortik.ricks_stuff.network.packet.C2SAbortWarpPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SActivateOmegaDevicePacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SCloseAllPortalsPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SColorizeFluidPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SCruiserDeleteLocationPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SCruiserSaveLocationPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SDeleteLocationPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SDismountGunPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SEjectMemoryCardPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SEjectUpgradePacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SImplantChipPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SInitiateWarpPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SInstallUpgradePacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SPuppetAbilityPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SPuppetConnectPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SPuppetControlPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SPuppetDisconnectPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SPuppetSelfDestructPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SRepairCapsulePacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SRequestPuppetListPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SResetGunPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SSaveLocationPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SScanEntityPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SSelectModePacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SSelectSavedLocationPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SSelfDestructPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SSetCoordinatesPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SShipControlPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SStartClonePrintPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2STargetBiomePacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2STargetPlayerPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2STargetStructurePacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SUpdateAntiPortalConfigPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SUpdateGunSettingsPacket;
import org.ExtraTortik.ricks_stuff.network.packet.S2CBiomeFoundPacket;
import org.ExtraTortik.ricks_stuff.network.packet.S2CEvilMortyShieldEffectPacket;
import org.ExtraTortik.ricks_stuff.network.packet.S2CFluidHitEffectPacket;
import org.ExtraTortik.ricks_stuff.network.packet.S2CGunRecoilPacket;
import org.ExtraTortik.ricks_stuff.network.packet.S2COmegaDeviceEffectPacket;
import org.ExtraTortik.ricks_stuff.network.packet.S2COpenGunMenuPacket;
import org.ExtraTortik.ricks_stuff.network.packet.S2CPhoenixResurrectionPacket;
import org.ExtraTortik.ricks_stuff.network.packet.S2CPuppetDisconnectPacket;
import org.ExtraTortik.ricks_stuff.network.packet.S2CPuppetListPacket;
import org.ExtraTortik.ricks_stuff.network.packet.S2CScanResultPacket;
import org.ExtraTortik.ricks_stuff.network.packet.S2CStructureFoundPacket;
import org.ExtraTortik.ricks_stuff.network.packet.S2CSyncConfigPacket;
import org.ExtraTortik.ricks_stuff.network.packet.S2CSyncCruiserLocationsPacket;
import org.ExtraTortik.ricks_stuff.network.packet.S2CSyncMultiverseErasurePacket;
import org.ExtraTortik.ricks_stuff.network.packet.S2CSyncPlayerScalePacket;
import org.ExtraTortik.ricks_stuff.network.packet.S2CSyncWarpSequencePacket;
import org.ExtraTortik.ricks_stuff.network.packet.S2CTeleportOverlayPacket;

@EventBusSubscriber(modid="ricks_stuff", bus=EventBusSubscriber.Bus.MOD)
public class ModMessages {
    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1.0");
        registrar.playToServer(C2SUpdateGunSettingsPacket.TYPE, C2SUpdateGunSettingsPacket.STREAM_CODEC, C2SUpdateGunSettingsPacket::handle);
        registrar.playToServer(C2SSaveLocationPacket.TYPE, C2SSaveLocationPacket.STREAM_CODEC, C2SSaveLocationPacket::handle);
        registrar.playToServer(C2SDeleteLocationPacket.TYPE, C2SDeleteLocationPacket.STREAM_CODEC, C2SDeleteLocationPacket::handle);
        registrar.playToServer(C2SSetCoordinatesPacket.TYPE, C2SSetCoordinatesPacket.STREAM_CODEC, C2SSetCoordinatesPacket::handle);
        registrar.playToServer(C2SSelectModePacket.TYPE, C2SSelectModePacket.STREAM_CODEC, C2SSelectModePacket::handle);
        registrar.playToServer(C2SResetGunPacket.TYPE, C2SResetGunPacket.STREAM_CODEC, C2SResetGunPacket::handle);
        registrar.playToServer(C2SDismountGunPacket.TYPE, C2SDismountGunPacket.STREAM_CODEC, C2SDismountGunPacket::handle);
        registrar.playToServer(C2SCloseAllPortalsPacket.TYPE, C2SCloseAllPortalsPacket.STREAM_CODEC, C2SCloseAllPortalsPacket::handle);
        registrar.playToServer(C2STargetPlayerPacket.TYPE, C2STargetPlayerPacket.STREAM_CODEC, C2STargetPlayerPacket::handle);
        registrar.playToServer(C2SSelfDestructPacket.TYPE, C2SSelfDestructPacket.STREAM_CODEC, C2SSelfDestructPacket::handle);
        registrar.playToServer(C2SStartClonePrintPacket.TYPE, C2SStartClonePrintPacket.STREAM_CODEC, C2SStartClonePrintPacket::handle);
        registrar.playToServer(C2SRepairCapsulePacket.TYPE, C2SRepairCapsulePacket.STREAM_CODEC, C2SRepairCapsulePacket::handle);
        registrar.playToServer(C2SSelectSavedLocationPacket.TYPE, C2SSelectSavedLocationPacket.STREAM_CODEC, C2SSelectSavedLocationPacket::handle);
        registrar.playToServer(C2SEjectMemoryCardPacket.TYPE, C2SEjectMemoryCardPacket.STREAM_CODEC, C2SEjectMemoryCardPacket::handle);
        registrar.playToServer(C2SInstallUpgradePacket.TYPE, C2SInstallUpgradePacket.STREAM_CODEC, C2SInstallUpgradePacket::handle);
        registrar.playToServer(C2SEjectUpgradePacket.TYPE, C2SEjectUpgradePacket.STREAM_CODEC, C2SEjectUpgradePacket::handle);
        registrar.playToServer(C2STargetStructurePacket.TYPE, C2STargetStructurePacket.STREAM_CODEC, C2STargetStructurePacket::handle);
        registrar.playToServer(C2SShipControlPacket.TYPE, C2SShipControlPacket.STREAM_CODEC, C2SShipControlPacket::handle);
        registrar.playToServer(C2SUpdateAntiPortalConfigPacket.TYPE, C2SUpdateAntiPortalConfigPacket.STREAM_CODEC, C2SUpdateAntiPortalConfigPacket::handle);
        registrar.playToServer(C2SInitiateWarpPacket.TYPE, C2SInitiateWarpPacket.STREAM_CODEC, C2SInitiateWarpPacket::handle);
        registrar.playToServer(C2SAbortWarpPacket.TYPE, C2SAbortWarpPacket.STREAM_CODEC, C2SAbortWarpPacket::handle);
        registrar.playToServer(C2SCruiserSaveLocationPacket.TYPE, C2SCruiserSaveLocationPacket.STREAM_CODEC, C2SCruiserSaveLocationPacket::handle);
        registrar.playToServer(C2SCruiserDeleteLocationPacket.TYPE, C2SCruiserDeleteLocationPacket.STREAM_CODEC, C2SCruiserDeleteLocationPacket::handle);
        registrar.playToServer(C2SActivateOmegaDevicePacket.TYPE, C2SActivateOmegaDevicePacket.STREAM_CODEC, C2SActivateOmegaDevicePacket::handle);
        registrar.playToServer(C2SColorizeFluidPacket.TYPE, C2SColorizeFluidPacket.STREAM_CODEC, C2SColorizeFluidPacket::handle);
        registrar.playToServer(C2STargetBiomePacket.TYPE, C2STargetBiomePacket.STREAM_CODEC, C2STargetBiomePacket::handle);
        registrar.playToClient(S2COpenGunMenuPacket.TYPE, S2COpenGunMenuPacket.STREAM_CODEC, S2COpenGunMenuPacket::handle);
        registrar.playToClient(S2CTeleportOverlayPacket.TYPE, S2CTeleportOverlayPacket.STREAM_CODEC, S2CTeleportOverlayPacket::handle);
        registrar.playToClient(S2CSyncConfigPacket.TYPE, S2CSyncConfigPacket.STREAM_CODEC, S2CSyncConfigPacket::handle);
        registrar.playToClient(S2CStructureFoundPacket.TYPE, S2CStructureFoundPacket.STREAM_CODEC, S2CStructureFoundPacket::handle);
        registrar.playToClient(S2CPhoenixResurrectionPacket.TYPE, S2CPhoenixResurrectionPacket.STREAM_CODEC, S2CPhoenixResurrectionPacket::handle);
        registrar.playToClient(S2CSyncPlayerScalePacket.TYPE, S2CSyncPlayerScalePacket.STREAM_CODEC, S2CSyncPlayerScalePacket::handle);
        registrar.playToClient(S2CSyncWarpSequencePacket.TYPE, S2CSyncWarpSequencePacket.STREAM_CODEC, S2CSyncWarpSequencePacket::handle);
        registrar.playToClient(S2CSyncCruiserLocationsPacket.TYPE, S2CSyncCruiserLocationsPacket.STREAM_CODEC, S2CSyncCruiserLocationsPacket::handle);
        registrar.playToClient(S2COmegaDeviceEffectPacket.TYPE, S2COmegaDeviceEffectPacket.STREAM_CODEC, S2COmegaDeviceEffectPacket::handle);
        registrar.playToClient(S2CSyncMultiverseErasurePacket.TYPE, S2CSyncMultiverseErasurePacket.STREAM_CODEC, S2CSyncMultiverseErasurePacket::handle);
        registrar.playToClient(S2CFluidHitEffectPacket.TYPE, S2CFluidHitEffectPacket.STREAM_CODEC, S2CFluidHitEffectPacket::handle);
        registrar.playToClient(S2CBiomeFoundPacket.TYPE, S2CBiomeFoundPacket.STREAM_CODEC, S2CBiomeFoundPacket::handle);
        registrar.playToServer(C2SImplantChipPacket.TYPE, C2SImplantChipPacket.STREAM_CODEC, C2SImplantChipPacket::handle);
        registrar.playToServer(C2SPuppetConnectPacket.TYPE, C2SPuppetConnectPacket.STREAM_CODEC, C2SPuppetConnectPacket::handle);
        registrar.playToServer(C2SPuppetControlPacket.TYPE, C2SPuppetControlPacket.STREAM_CODEC, C2SPuppetControlPacket::handle);
        registrar.playToServer(C2SPuppetDisconnectPacket.TYPE, C2SPuppetDisconnectPacket.STREAM_CODEC, C2SPuppetDisconnectPacket::handle);
        registrar.playToServer(C2SPuppetSelfDestructPacket.TYPE, C2SPuppetSelfDestructPacket.STREAM_CODEC, C2SPuppetSelfDestructPacket::handle);
        registrar.playToServer(C2SPuppetAbilityPacket.TYPE, C2SPuppetAbilityPacket.STREAM_CODEC, C2SPuppetAbilityPacket::handle);
        registrar.playToServer(C2SRequestPuppetListPacket.TYPE, C2SRequestPuppetListPacket.STREAM_CODEC, C2SRequestPuppetListPacket::handle);
        registrar.playToClient(S2CPuppetDisconnectPacket.TYPE, S2CPuppetDisconnectPacket.STREAM_CODEC, S2CPuppetDisconnectPacket::handle);
        registrar.playToClient(S2CPuppetListPacket.TYPE, S2CPuppetListPacket.STREAM_CODEC, S2CPuppetListPacket::handle);
        registrar.playToClient(S2CEvilMortyShieldEffectPacket.TYPE, S2CEvilMortyShieldEffectPacket.STREAM_CODEC, S2CEvilMortyShieldEffectPacket::handle);
        registrar.playToServer(C2SScanEntityPacket.TYPE, C2SScanEntityPacket.STREAM_CODEC, C2SScanEntityPacket::handle);
        registrar.playToClient(S2CScanResultPacket.TYPE, S2CScanResultPacket.STREAM_CODEC, S2CScanResultPacket::handle);
        registrar.playToClient(S2CGunRecoilPacket.TYPE, S2CGunRecoilPacket.STREAM_CODEC, S2CGunRecoilPacket::handle);
    }

    public static void sendToServer(CustomPacketPayload message) {
        PacketDistributor.sendToServer((CustomPacketPayload)message, (CustomPacketPayload[])new CustomPacketPayload[0]);
    }

    public static void sendToPlayer(CustomPacketPayload message, ServerPlayer player) {
        if (player == null) return;
        PacketDistributor.sendToPlayer((ServerPlayer)player, (CustomPacketPayload)message, (CustomPacketPayload[])new CustomPacketPayload[0]);
    }

    public static void sendToClients(CustomPacketPayload message) {
        PacketDistributor.sendToAllPlayers((CustomPacketPayload)message, (CustomPacketPayload[])new CustomPacketPayload[0]);
    }

    public static void sendToNear(CustomPacketPayload message, double x, double y, double z, double radius, ResourceKey<Level> dimension) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;
        ServerLevel level = server.getLevel(dimension);
        if (level == null) return;
        PacketDistributor.sendToPlayersNear((ServerLevel)level, null, (double)x, (double)y, (double)z, (double)radius, (CustomPacketPayload)message, (CustomPacketPayload[])new CustomPacketPayload[0]);
    }

    public static void sendToNearby(CustomPacketPayload message, Level level, double x, double y, double z, double radius) {
        if (!(level instanceof ServerLevel)) return;
        ServerLevel serverLevel = (ServerLevel)level;
        PacketDistributor.sendToPlayersNear((ServerLevel)serverLevel, null, (double)x, (double)y, (double)z, (double)radius, (CustomPacketPayload)message, (CustomPacketPayload[])new CustomPacketPayload[0]);
    }
}

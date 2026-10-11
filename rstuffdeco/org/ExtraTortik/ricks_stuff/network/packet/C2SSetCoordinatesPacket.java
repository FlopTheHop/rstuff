/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.ExtraTortik.ricks_stuff.compat.InfinityCompat
 *  org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.UpgradeType
 *  org.ExtraTortik.ricks_stuff.portal.BlenderDimensionHelper
 *  org.ExtraTortik.ricks_stuff.portal.PortalMode
 *  org.ExtraTortik.ricks_stuff.portal.PortalPlacementHelper
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ExtraTortik.ricks_stuff.compat.InfinityCompat;
import org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.ExtraTortik.ricks_stuff.item.UpgradeType;
import org.ExtraTortik.ricks_stuff.portal.BlenderDimensionHelper;
import org.ExtraTortik.ricks_stuff.portal.PortalMode;
import org.ExtraTortik.ricks_stuff.portal.PortalPlacementHelper;

public record C2SSetCoordinatesPacket(double x, double y, double z, String dimension, boolean isWorldSpawn) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<C2SSetCoordinatesPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"set_coordinates"));
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SSetCoordinatesPacket> STREAM_CODEC = StreamCodec.ofMember(C2SSetCoordinatesPacket::write, C2SSetCoordinatesPacket::new);

    public C2SSetCoordinatesPacket(double x, double y, double z, String dimension) {
        this(x, y, z, dimension, false);
    }

    public C2SSetCoordinatesPacket(RegistryFriendlyByteBuf buf) {
        this(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readUtf(), buf.readBoolean());
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeUtf(this.dimension);
        buf.writeBoolean(this.isWorldSpawn);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(C2SSetCoordinatesPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ResourceKey infKey;
            Player patt0$temp = context.player();
            if (!(patt0$temp instanceof ServerPlayer)) return;
            ServerPlayer player = (ServerPlayer)patt0$temp;
            ItemStack stack = C2SSetCoordinatesPacket.getHeldGun(player);
            if (stack == null) return;
            if (stack.isEmpty()) return;
            MinecraftServer server = player.getServer();
            String actualDim = packet.dimension();
            boolean isBlender = BlenderDimensionHelper.isBlenderDimension((String)packet.dimension());
            if (isBlender) {
                actualDim = "blenders_dimension";
            } else if (server != null && InfinityCompat.isLoaded() && (infKey = InfinityCompat.resolveOrGenerateDimension((MinecraftServer)server, (String)packet.dimension())) != null) {
                actualDim = infKey.location().toString();
            }
            String currentDimStr = player.level().dimension().location().toString();
            if (!(isBlender || actualDim.equals(currentDimStr) || player.getAbilities().instabuild || PortalGunItem.hasUpgrade((ItemStack)stack, (UpgradeType)UpgradeType.DIMENSIONAL))) {
                actualDim = currentDimStr;
            }
            CompoundTag locTag = new CompoundTag();
            locTag.putDouble("X", packet.x());
            locTag.putDouble("Y", packet.y());
            locTag.putDouble("Z", packet.z());
            locTag.putString("Dimension", actualDim);
            locTag.putString("RawText", packet.dimension());
            locTag.putBoolean("IsWorldSpawn", packet.isWorldSpawn());
            locTag.putBoolean("SafePlacement", PortalGunItem.isSafePlacement((ItemStack)stack));
            String dimTitle = PortalPlacementHelper.formatDimensionName((String)actualDim);
            if (packet.isWorldSpawn()) {
                locTag.putString("Name", dimTitle + " (Spawn)");
            } else {
                locTag.putString("Name", (String)(isBlender ? "Blender Dimension" : "Custom (" + (int)packet.x() + ", " + (int)packet.y() + ", " + (int)packet.z() + ")"));
            }
            PortalGunItem.setCustomLocation((ItemStack)stack, (CompoundTag)locTag);
            PortalGunItem.setMode((ItemStack)stack, (PortalMode)PortalMode.CUSTOM);
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

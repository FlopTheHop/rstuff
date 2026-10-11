/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.component.CustomData
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.ClientTickEvent$Post
 *  net.neoforged.neoforge.client.event.InputEvent$InteractionKeyMappingTriggered
 *  net.neoforged.neoforge.client.event.InputEvent$Key
 *  net.neoforged.neoforge.client.event.InputEvent$MouseButton$Pre
 *  net.neoforged.neoforge.client.event.InputEvent$MouseScrollingEvent
 *  net.neoforged.neoforge.client.event.RenderPlayerEvent$Post
 *  net.neoforged.neoforge.client.event.RenderPlayerEvent$Pre
 *  net.neoforged.neoforge.client.event.ViewportEvent$ComputeCameraAngles
 *  org.ExtraTortik.ricks_stuff.client.BioNeuralImplantManager
 *  org.ExtraTortik.ricks_stuff.client.BioNeuralPossessionManager
 *  org.ExtraTortik.ricks_stuff.client.ClientSetup
 *  org.ExtraTortik.ricks_stuff.client.TargetScanManager
 *  org.ExtraTortik.ricks_stuff.client.gui.PuppetRadialMenuScreen
 *  org.ExtraTortik.ricks_stuff.client.gui.RadialLocationScreen
 *  org.ExtraTortik.ricks_stuff.combat.BallisticPredictionEngine
 *  org.ExtraTortik.ricks_stuff.compat.curios.CuriosCompat
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SCloseAllPortalsPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SSelectModePacket
 *  org.ExtraTortik.ricks_stuff.portal.PortalMode
 */
package org.ExtraTortik.ricks_stuff.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import org.ExtraTortik.ricks_stuff.client.BioNeuralImplantManager;
import org.ExtraTortik.ricks_stuff.client.BioNeuralPossessionManager;
import org.ExtraTortik.ricks_stuff.client.ClientSetup;
import org.ExtraTortik.ricks_stuff.client.TargetScanManager;
import org.ExtraTortik.ricks_stuff.client.gui.PuppetRadialMenuScreen;
import org.ExtraTortik.ricks_stuff.client.gui.RadialLocationScreen;
import org.ExtraTortik.ricks_stuff.combat.BallisticPredictionEngine;
import org.ExtraTortik.ricks_stuff.compat.curios.CuriosCompat;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.C2SCloseAllPortalsPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SSelectModePacket;
import org.ExtraTortik.ricks_stuff.portal.PortalMode;

@EventBusSubscriber(modid="ricks_stuff", bus=EventBusSubscriber.Bus.GAME, value={Dist.CLIENT})
public static class ClientSetup.ClientGameEvents {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        boolean hasEyepatch;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        while (ClientSetup.CLOSE_PORTALS_KEY.consumeClick()) {
            boolean hasGun;
            if (mc.screen != null) continue;
            ItemStack main = mc.player.getMainHandItem();
            ItemStack off = mc.player.getOffhandItem();
            boolean bl = hasGun = ClientSetup.ClientGameEvents.isGun(main) || ClientSetup.ClientGameEvents.isGun(off);
            if (!hasGun) {
                for (ItemStack item : mc.player.getInventory().items) {
                    if (!ClientSetup.ClientGameEvents.isGun(item)) continue;
                    hasGun = true;
                    break;
                }
            }
            if (!hasGun) continue;
            ModMessages.sendToServer((CustomPacketPayload)new C2SCloseAllPortalsPacket());
        }
        while (ClientSetup.CYCLE_MODE_KEY.consumeClick()) {
            if (mc.screen != null || mc.player == null) continue;
            ClientSetup.ClientGameEvents.cycleHeldGunMode(mc.player, true);
        }
        if (ClientSetup.PUPPET_SELF_DESTRUCT_KEY.consumeClick() && BioNeuralPossessionManager.isPossessing()) {
            BioNeuralPossessionManager.triggerSelfDestruct();
        }
        if (ClientSetup.PUPPET_DISCONNECT_KEY.consumeClick() && BioNeuralPossessionManager.isPossessing()) {
            BioNeuralPossessionManager.stopPossession();
        }
        if (ClientSetup.PUPPET_ABILITY_KEY.consumeClick() && BioNeuralPossessionManager.isPossessing()) {
            BioNeuralPossessionManager.triggerAbility();
        }
        if (event.getAction() == 1 && ClientSetup.PUPPET_RADIAL_MENU_KEY.matches(event.getKey(), event.getScanCode()) && mc.screen == null && mc.player != null && (hasEyepatch = CuriosCompat.isWearingEyePatch((LivingEntity)mc.player))) {
            mc.setScreen((Screen)new PuppetRadialMenuScreen(event.getKey()));
        }
        if (event.getAction() == 1 && ClientSetup.NEURAL_SCAN_KEY.matches(event.getKey(), event.getScanCode()) && mc.screen == null && mc.player != null) {
            TargetScanManager.getInstance().tryTriggerScan((Player)mc.player);
        }
        if (event.getAction() != 1) return;
        if (!ClientSetup.RADIAL_MENU_KEY.matches(event.getKey(), event.getScanCode())) return;
        if (mc.screen != null) return;
        ClientSetup.ClientGameEvents.openRadialMenu(mc, event.getKey());
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (mc.screen != null) {
            return;
        }
        if (!mc.player.isShiftKeyDown()) return;
        ItemStack stack = ClientSetup.ClientGameEvents.getHeldGun(mc.player);
        if (stack == null) return;
        double delta = event.getScrollDeltaY();
        if (!(Math.abs(delta) > 0.01)) return;
        event.setCanceled(true);
        ClientSetup.ClientGameEvents.cycleHeldGunMode(mc.player, delta > 0.0);
    }

    @SubscribeEvent
    public static void onInteractionKey(InputEvent.InteractionKeyMappingTriggered event) {
        if (!BioNeuralPossessionManager.isPossessing()) return;
        event.setCanceled(true);
        event.setSwingHand(false);
    }

    @SubscribeEvent
    public static void onComputeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        if (!BioNeuralPossessionManager.isPossessing()) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        float yaw = mc.player.getYRot();
        float pitch = mc.player.getXRot();
        event.setYaw(yaw);
        event.setPitch(pitch);
        event.setRoll(0.0f);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        BioNeuralPossessionManager.clientTick((Minecraft)Minecraft.getInstance());
        BioNeuralImplantManager.clientTick((Minecraft)Minecraft.getInstance());
        TargetScanManager.getInstance().tick(Minecraft.getInstance());
        BallisticPredictionEngine.clientTick((Minecraft)Minecraft.getInstance());
    }

    private static void cycleHeldGunMode(LocalPlayer player, boolean forward) {
        ItemStack stack = ClientSetup.ClientGameEvents.getHeldGun(player);
        if (stack == null) {
            return;
        }
        PortalMode currentMode = PortalGunItem.getMode((ItemStack)stack);
        PortalMode newMode = forward ? currentMode.next() : currentMode.previous();
        PortalGunItem.setMode((ItemStack)stack, (PortalMode)newMode);
        ModMessages.sendToServer((CustomPacketPayload)new C2SSelectModePacket(newMode.getName()));
        player.playSound((SoundEvent)ModSounds.UI_DIAL.get(), 1.0f, 1.0f);
    }

    private static boolean isGun(ItemStack stack) {
        return stack != null && !stack.isEmpty() && (stack.getItem() instanceof PortalGunItem || stack.getItem() instanceof DischargedPortalGunItem);
    }

    private static ItemStack getHeldGun(LocalPlayer player) {
        if (ClientSetup.ClientGameEvents.isGun(player.getMainHandItem())) {
            return player.getMainHandItem();
        }
        if (!ClientSetup.ClientGameEvents.isGun(player.getOffhandItem())) return null;
        return player.getOffhandItem();
    }

    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseButton.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        if (event.getAction() != 1) return;
        if (!ClientSetup.RADIAL_MENU_KEY.matchesMouse(event.getButton())) return;
        if (mc.screen != null) return;
        ClientSetup.ClientGameEvents.openRadialMenu(mc, -1);
    }

    private static void openRadialMenu(Minecraft mc, int triggerKey) {
        if (mc.player == null) {
            return;
        }
        ItemStack main = mc.player.getMainHandItem();
        ItemStack off = mc.player.getOffhandItem();
        ItemStack gunStack = null;
        int slotIndex = -1;
        if (ClientSetup.ClientGameEvents.isGun(main)) {
            gunStack = main;
            slotIndex = mc.player.getInventory().selected;
        } else if (ClientSetup.ClientGameEvents.isGun(off)) {
            gunStack = off;
            slotIndex = 40;
        } else {
            for (int i = 0; i < mc.player.getInventory().items.size(); ++i) {
                ItemStack item = (ItemStack)mc.player.getInventory().items.get(i);
                if (!ClientSetup.ClientGameEvents.isGun(item)) continue;
                gunStack = item;
                slotIndex = i;
                break;
            }
        }
        if (gunStack == null) return;
        CompoundTag tag = ((CustomData)gunStack.getOrDefault(DataComponents.CUSTOM_DATA, (Object)CustomData.EMPTY)).copyTag();
        mc.setScreen((Screen)new RadialLocationScreen(tag, slotIndex, triggerKey));
    }

    @SubscribeEvent
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();
        if (!player.getPersistentData().getBoolean("IsTinyRick")) return;
        event.getPoseStack().pushPose();
        event.getPoseStack().scale(0.5f, 0.5f, 0.5f);
    }

    @SubscribeEvent
    public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        Player player = event.getEntity();
        if (!player.getPersistentData().getBoolean("IsTinyRick")) return;
        event.getPoseStack().popPose();
    }
}

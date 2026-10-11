/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.MatchException
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Renderable
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.component.CustomData
 *  net.minecraft.world.level.ItemLike
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton$Style
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetGuiUtils
 *  org.ExtraTortik.ricks_stuff.client.gui.PortalGunScreen$1
 *  org.ExtraTortik.ricks_stuff.client.gui.SavedLocationsScreen
 *  org.ExtraTortik.ricks_stuff.client.gui.SelectModeScreen
 *  org.ExtraTortik.ricks_stuff.client.gui.SetCoordinatesScreen
 *  org.ExtraTortik.ricks_stuff.client.gui.SettingsScreen
 *  org.ExtraTortik.ricks_stuff.client.gui.UpgradesScreen
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.UpgradeType
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SCloseAllPortalsPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SDismountGunPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SEjectMemoryCardPacket
 *  org.ExtraTortik.ricks_stuff.portal.PortalMode
 *  org.ExtraTortik.ricks_stuff.portal.PortalPlacementHelper
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ItemLike;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetButton;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetGuiUtils;
import org.ExtraTortik.ricks_stuff.client.gui.PortalGunScreen;
import org.ExtraTortik.ricks_stuff.client.gui.SavedLocationsScreen;
import org.ExtraTortik.ricks_stuff.client.gui.SelectModeScreen;
import org.ExtraTortik.ricks_stuff.client.gui.SetCoordinatesScreen;
import org.ExtraTortik.ricks_stuff.client.gui.SettingsScreen;
import org.ExtraTortik.ricks_stuff.client.gui.UpgradesScreen;
import org.ExtraTortik.ricks_stuff.init.ModItems;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.ExtraTortik.ricks_stuff.item.UpgradeType;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.C2SCloseAllPortalsPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SDismountGunPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SEjectMemoryCardPacket;
import org.ExtraTortik.ricks_stuff.portal.PortalMode;
import org.ExtraTortik.ricks_stuff.portal.PortalPlacementHelper;

public class PortalGunScreen
extends Screen {
    private final CompoundTag gunNbt;
    private final int slotIndex;
    private ItemStack gunStack;

    public PortalGunScreen(CompoundTag gunNbt, int slotIndex) {
        super((Component)Component.literal((String)"Portal Gun Interface"));
        this.gunNbt = gunNbt != null ? gunNbt : new CompoundTag();
        this.slotIndex = slotIndex;
    }

    public PortalGunScreen() {
        this(new CompoundTag(), -1);
    }

    protected void init() {
        super.init();
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (this.slotIndex >= 0 && this.slotIndex < player.getInventory().getContainerSize()) {
                this.gunStack = player.getInventory().getItem(this.slotIndex);
            }
            if (this.gunStack == null || !PortalGunScreen.isGun(this.gunStack)) {
                if (PortalGunScreen.isGun(player.getMainHandItem())) {
                    this.gunStack = player.getMainHandItem();
                } else if (PortalGunScreen.isGun(player.getOffhandItem())) {
                    this.gunStack = player.getOffhandItem();
                }
            }
        }
        int panelWidth = 280;
        int panelHeight = 236;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;
        int btnWidth = 125;
        int btnHeight = 18;
        int col1X = left + 12;
        int col2X = left + panelWidth - btnWidth - 12;
        boolean hasCard = this.gunStack != null && PortalGunItem.hasMemoryCard((ItemStack)this.gunStack) || this.getUpdatedNbt().getBoolean("HasMemoryCard");
        List upgrades = this.gunStack != null ? PortalGunItem.getUpgrades((ItemStack)this.gunStack) : PortalGunItem.getUpgrades((CompoundTag)this.getUpdatedNbt());
        int upgradeCount = upgrades.size();
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"Saved Locations"), b -> {
            this.playClickSound();
            Minecraft.getInstance().setScreen((Screen)new SavedLocationsScreen((Screen)this, this.getUpdatedNbt(), this.slotIndex));
        }).bounds(col1X, top + 114, btnWidth, btnHeight).build());
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"Settings"), b -> {
            this.playClickSound();
            Minecraft.getInstance().setScreen((Screen)new SettingsScreen((Screen)this, this.getUpdatedNbt(), this.slotIndex));
        }).bounds(col2X, top + 114, btnWidth, btnHeight).build());
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"Set Coordinates"), b -> {
            this.playClickSound();
            Minecraft.getInstance().setScreen((Screen)new SetCoordinatesScreen((Screen)this, this.getUpdatedNbt(), this.slotIndex));
        }).bounds(col1X, top + 135, btnWidth, btnHeight).build());
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)("Expansions (" + upgradeCount + "/" + PortalGunItem.getMaxUpgrades() + ")")), b -> {
            this.playClickSound();
            Minecraft.getInstance().setScreen((Screen)new UpgradesScreen((Screen)this, this.getUpdatedNbt(), this.slotIndex));
        }).bounds(col2X, top + 135, btnWidth, btnHeight).build());
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"Protocol Mode"), b -> {
            this.playClickSound();
            Minecraft.getInstance().setScreen((Screen)new SelectModeScreen((Screen)this, this.getUpdatedNbt(), this.slotIndex));
        }).bounds(col1X, top + 156, btnWidth, btnHeight).build());
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"Eject Fluid Tube"), b -> {
            this.playClickSound();
            ModMessages.sendToServer((CustomPacketPayload)new C2SDismountGunPacket());
            this.onClose();
        }).bounds(col2X, top + 156, btnWidth, btnHeight).build());
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"Close All Portals"), b -> {
            this.playClickSound();
            ModMessages.sendToServer((CustomPacketPayload)new C2SCloseAllPortalsPacket());
        }).bounds(col1X, top + 177, btnWidth, btnHeight).style(GadgetButton.Style.DANGER).build());
        if (hasCard) {
            this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.translatable((String)"gui.ricks_stuff.eject_memory_card"), b -> {
                this.playClickSound();
                ModMessages.sendToServer((CustomPacketPayload)new C2SEjectMemoryCardPacket());
                this.onClose();
            }).bounds(col2X, top + 177, btnWidth, btnHeight).build());
        } else {
            GadgetButton ejectCardBtn = GadgetButton.gadget((Component)Component.translatable((String)"gui.ricks_stuff.eject_memory_card"), b -> {}).bounds(col2X, top + 177, btnWidth, btnHeight).build();
            ejectCardBtn.active = false;
            this.addRenderableWidget((GuiEventListener)ejectCardBtn);
        }
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"Exit"), b -> {
            this.playClickSound();
            this.onClose();
        }).bounds(left + (panelWidth - 140) / 2, top + 202, 140, 20).build());
    }

    private CompoundTag getUpdatedNbt() {
        if (this.gunStack == null) return this.gunNbt;
        CompoundTag customData = ((CustomData)this.gunStack.getOrDefault(DataComponents.CUSTOM_DATA, (Object)CustomData.EMPTY)).copyTag();
        if (customData.isEmpty()) return this.gunNbt;
        return customData;
    }

    private void playClickSound() {
        if (Minecraft.getInstance().player == null) return;
        Minecraft.getInstance().player.playSound((SoundEvent)ModSounds.UI_PRESS.get(), 1.0f, 1.0f);
    }

    private static String formatUpgradeShortName(UpgradeType ut) {
        return switch (1.$SwitchMap$org$ExtraTortik$ricks_stuff$item$UpgradeType[ut.ordinal()]) {
            default -> throw new MatchException(null, null);
            case 1 -> "Dimensional";
            case 2 -> "Player Locator";
            case 3 -> "Flux Optimizer";
            case 4 -> "Hazard Shield";
            case 5 -> "Struct Scanner";
            case 6 -> "Biome Locator";
        };
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        int panelWidth = 280;
        int panelHeight = 236;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;
        GadgetGuiUtils.renderChassis((GuiGraphics)guiGraphics, (int)left, (int)top, (int)panelWidth, (int)panelHeight);
        GadgetGuiUtils.renderHeader((GuiGraphics)guiGraphics, (int)(left + 4), (int)(top + 4), (int)(panelWidth - 8), (int)30);
        guiGraphics.fill(left + 8, top + 6, left + 34, top + 32, -15592425);
        guiGraphics.renderOutline(left + 8, top + 6, 26, 26, -13289150);
        ItemStack displayGun = this.gunStack != null && !this.gunStack.isEmpty() ? this.gunStack : new ItemStack((ItemLike)ModItems.RICK_PORTAL_GUN.get());
        guiGraphics.renderItem(displayGun, left + 13, top + 11);
        String gunName = this.gunStack != null ? this.gunStack.getHoverName().getString() : "C-137 PORTAL GUN";
        guiGraphics.drawString(this.font, gunName, left + 40, top + 9, -12720539, false);
        guiGraphics.drawString(this.font, "INTERDIMENSIONAL CONTROL PANEL", left + 40, top + 21, -7696228, false);
        int screenX = left + 12;
        int screenY = top + 36;
        int screenW = panelWidth - 24;
        int screenH = 74;
        GadgetGuiUtils.renderTerminalScreen((GuiGraphics)guiGraphics, (int)screenX, (int)screenY, (int)screenW, (int)screenH);
        CompoundTag tag = this.getUpdatedNbt();
        int charge = tag.contains("Charge") ? tag.getInt("Charge") : 100;
        String modeName = tag.contains("Mode") ? tag.getString("Mode") : PortalMode.FIFO.getName();
        PortalMode mode = PortalMode.fromString((String)modeName);
        int gunId = tag.contains("GunId") ? tag.getInt("GunId") : 0;
        guiGraphics.drawString(this.font, "FLUID:", screenX + 6, screenY + 5, -7696228, false);
        GadgetGuiUtils.renderFluidTube((GuiGraphics)guiGraphics, (Font)this.font, (int)(screenX + 46), (int)(screenY + 4), (int)140, (int)9, (int)charge, (boolean)true);
        guiGraphics.drawString(this.font, "MODE:", screenX + 6, screenY + 18, -7696228, false);
        guiGraphics.drawString(this.font, mode.getDisplayName(), screenX + 46, screenY + 18, -13183000, false);
        String deviceText = "DEVICE #" + gunId;
        int devTextW = this.font.width(deviceText);
        guiGraphics.drawString(this.font, deviceText, screenX + screenW - devTextW - 8, screenY + 18, -7696228, false);
        List upgrades = this.gunStack != null ? PortalGunItem.getUpgrades((ItemStack)this.gunStack) : PortalGunItem.getUpgrades((CompoundTag)tag);
        guiGraphics.drawString(this.font, "EXPANSIONS:", screenX + 6, screenY + 31, -7696228, false);
        int expX = screenX + 68;
        int maxCards = Config.maxExpansionCards;
        if (upgrades.isEmpty()) {
            guiGraphics.drawString(this.font, "NONE (0/" + maxCards + ")", expX, screenY + 31, -7696228, false);
        } else {
            String countStr = " (" + upgrades.size() + "/" + maxCards + ")";
            int countW = this.font.width(countStr);
            int maxExpW = screenW - (expX - screenX) - 8;
            int availableNamesW = Math.max(10, maxExpW - countW);
            int curX = expX;
            for (int i = 0; i < upgrades.size(); ++i) {
                UpgradeType ut = (UpgradeType)upgrades.get(i);
                String shortName = PortalGunScreen.formatUpgradeShortName(ut);
                String separator = i < upgrades.size() - 1 ? ", " : "";
                String fullToken = shortName + separator;
                int tokenW = this.font.width(fullToken);
                int col = ut.getColor() | 0xFF000000;
                if (curX + tokenW <= expX + availableNamesW) {
                    guiGraphics.drawString(this.font, shortName, curX, screenY + 31, col, false);
                    curX += this.font.width(shortName);
                    if (separator.isEmpty()) continue;
                    guiGraphics.drawString(this.font, separator, curX, screenY + 31, -7696228, false);
                    curX += this.font.width(separator);
                    continue;
                }
                int remainingW = expX + availableNamesW - curX;
                if (remainingW >= this.font.width("\u2026") + 6) {
                    String clipped = this.font.plainSubstrByWidth(shortName, remainingW - 6) + "\u2026";
                    guiGraphics.drawString(this.font, clipped, curX, screenY + 31, col, false);
                    curX += this.font.width(clipped);
                    break;
                }
                if (curX == expX) {
                    guiGraphics.drawString(this.font, "\u2026", curX, screenY + 31, col, false);
                    curX += this.font.width("\u2026");
                    break;
                }
                guiGraphics.drawString(this.font, "\u2026", curX, screenY + 31, -7696228, false);
                curX += this.font.width("\u2026");
                break;
            }
            guiGraphics.drawString(this.font, countStr, curX, screenY + 31, -12720539, false);
        }
        boolean hasMemory = tag.getBoolean("HasMemoryCard");
        guiGraphics.drawString(this.font, "MEMORY:", screenX + 6, screenY + 44, -7696228, false);
        int memX = screenX + 52;
        if (hasMemory) {
            int locCount = tag.getList("SavedLocations", 10).size();
            String cardStr = "CARD INSTALLED (" + locCount + "/30)";
            guiGraphics.drawString(this.font, cardStr, memX, screenY + 44, -12720539, false);
        } else {
            guiGraphics.drawString(this.font, "NO MEMORY CARD", memX, screenY + 44, -7696228, false);
        }
        guiGraphics.drawString(this.font, "TARGET:", screenX + 6, screenY + 57, -7696228, false);
        int targetX = screenX + 52;
        int maxTargetWidth = screenW - (targetX - screenX) - 8;
        if (mode == PortalMode.CUSTOM && tag.contains("CustomLocation")) {
            CompoundTag customLoc = tag.getCompound("CustomLocation");
            if (customLoc.contains("TargetPlayerName")) {
                String pName = customLoc.getString("TargetPlayerName");
                String displayPlayer = "Player: " + pName;
                if (this.font.width(displayPlayer) > maxTargetWidth) {
                    displayPlayer = this.font.plainSubstrByWidth(displayPlayer, maxTargetWidth - 6) + "\u2026";
                }
                guiGraphics.drawString(this.font, displayPlayer, targetX, screenY + 57, -12720539, false);
            } else if (customLoc.getBoolean("IsWorldSpawn")) {
                dim = customLoc.getString("Dimension");
                dimName = PortalPlacementHelper.formatDimensionName((String)dim);
                String displaySpawn = dimName + " [Spawn]";
                if (this.font.width(displaySpawn) > maxTargetWidth) {
                    displaySpawn = this.font.plainSubstrByWidth(displaySpawn, maxTargetWidth - 6) + "\u2026";
                }
                guiGraphics.drawString(this.font, displaySpawn, targetX, screenY + 57, -19674, false);
            } else {
                dim = customLoc.getString("Dimension");
                dimName = PortalPlacementHelper.formatDimensionName((String)dim);
                String locStr = (int)customLoc.getDouble("X") + " " + (int)customLoc.getDouble("Y") + " " + (int)customLoc.getDouble("Z") + " (" + dimName + ")";
                if (this.font.width(locStr) > maxTargetWidth) {
                    locStr = this.font.plainSubstrByWidth(locStr, maxTargetWidth - 6) + "\u2026";
                }
                guiGraphics.drawString(this.font, locStr, targetX, screenY + 57, -19674, false);
            }
        } else {
            guiGraphics.drawString(this.font, "READY", targetX, screenY + 57, -12720539, false);
        }
        int ledColor = charge > 25 ? -16711834 : (charge > 0 ? -13312 : -56798);
        GadgetGuiUtils.renderLed((GuiGraphics)guiGraphics, (int)(screenX + screenW - 12), (int)(screenY + 6), (int)ledColor);
        Iterator iterator = this.renderables.iterator();
        while (iterator.hasNext()) {
            Renderable renderable = (Renderable)iterator.next();
            renderable.render(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    protected void renderBlurredBackground(float partialTick) {
    }

    private static boolean isGun(ItemStack s) {
        return s != null && !s.isEmpty() && (s.getItem() instanceof PortalGunItem || s.getItem() instanceof DischargedPortalGunItem);
    }

    public boolean isPauseScreen() {
        return false;
    }
}

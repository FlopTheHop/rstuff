/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.EditBox
 *  net.minecraft.client.gui.components.Renderable
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.component.CustomData
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton$Style
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetGuiUtils
 *  org.ExtraTortik.ricks_stuff.client.gui.SelectBiomeScreen
 *  org.ExtraTortik.ricks_stuff.client.gui.SelectPlayerScreen
 *  org.ExtraTortik.ricks_stuff.client.gui.SelectStructureScreen
 *  org.ExtraTortik.ricks_stuff.compat.InfinityCompat
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.UpgradeType
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SSaveLocationPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SSetCoordinatesPacket
 *  org.ExtraTortik.ricks_stuff.portal.BlenderDimensionHelper
 *  org.ExtraTortik.ricks_stuff.portal.FishDimensionHelper
 *  org.ExtraTortik.ricks_stuff.portal.PortalMode
 *  org.ExtraTortik.ricks_stuff.portal.PortalPlacementHelper
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetButton;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetGuiUtils;
import org.ExtraTortik.ricks_stuff.client.gui.SelectBiomeScreen;
import org.ExtraTortik.ricks_stuff.client.gui.SelectPlayerScreen;
import org.ExtraTortik.ricks_stuff.client.gui.SelectStructureScreen;
import org.ExtraTortik.ricks_stuff.compat.InfinityCompat;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.ExtraTortik.ricks_stuff.item.UpgradeType;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.C2SSaveLocationPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SSetCoordinatesPacket;
import org.ExtraTortik.ricks_stuff.portal.BlenderDimensionHelper;
import org.ExtraTortik.ricks_stuff.portal.FishDimensionHelper;
import org.ExtraTortik.ricks_stuff.portal.PortalMode;
import org.ExtraTortik.ricks_stuff.portal.PortalPlacementHelper;

public class SetCoordinatesScreen
extends Screen {
    private final Screen parent;
    private final CompoundTag gunNbt;
    private final int slotIndex;
    private ItemStack gunStack;
    private final List<String> availableDimensions = new ArrayList<String>();
    private int selectedDimIndex = 0;
    private String selectedDimension = "minecraft:overworld";
    private EditBox coordsField;
    private EditBox nameField;
    private EditBox dimensionField;
    private String statusMessage = "";
    private int statusColor = 0x55FF55;
    private boolean isScanning = false;

    public SetCoordinatesScreen(Screen parent, CompoundTag gunNbt, int slotIndex) {
        super((Component)Component.literal((String)"Set Coordinates & Dimension"));
        this.parent = parent;
        this.gunNbt = gunNbt != null ? gunNbt : new CompoundTag();
        this.slotIndex = slotIndex;
    }

    public void setStatusMessage(String msg, int color) {
        this.isScanning = false;
        this.statusMessage = msg != null ? msg : "";
        this.statusColor = color;
    }

    public void startScanning() {
        this.isScanning = true;
        this.statusMessage = "";
    }

    public void onStructureFound(boolean success, String structureName, double x, double y, double z, String dimension) {
        this.isScanning = false;
        if (success) {
            this.selectedDimension = dimension;
            if (this.dimensionField != null) {
                this.dimensionField.setValue(dimension);
            }
            if (this.coordsField != null) {
                this.coordsField.setValue((int)x + " " + (int)y + " " + (int)z);
            }
            if (this.nameField != null) {
                this.nameField.setValue("Structure: " + structureName);
            }
            String dimTitle = PortalPlacementHelper.formatDimensionName((String)dimension);
            this.statusMessage = "Target locked: " + (int)x + " " + (int)y + " " + (int)z + " [" + dimTitle + "]";
            this.statusColor = 0x55FF55;
            CompoundTag locTag = new CompoundTag();
            locTag.putDouble("X", (double)Math.round(x * 10.0) / 10.0);
            locTag.putDouble("Y", (double)Math.round(y * 10.0) / 10.0);
            locTag.putDouble("Z", (double)Math.round(z * 10.0) / 10.0);
            locTag.putString("Dimension", dimension);
            locTag.putString("RawText", dimension);
            locTag.putString("Name", "Structure: " + structureName);
            this.updateLocalCustomLocation(locTag);
        } else {
            this.statusMessage = "No structure found nearby!";
            this.statusColor = 0xFF5555;
        }
    }

    public void onBiomeFound(boolean success, String biomeName, double x, double y, double z, String dimension) {
        this.isScanning = false;
        if (success) {
            this.selectedDimension = dimension;
            if (this.dimensionField != null) {
                this.dimensionField.setValue(dimension);
            }
            if (this.coordsField != null) {
                this.coordsField.setValue((int)x + " " + (int)y + " " + (int)z);
            }
            if (this.nameField != null) {
                this.nameField.setValue("Biome: " + biomeName);
            }
            String dimTitle = PortalPlacementHelper.formatDimensionName((String)dimension);
            this.statusMessage = "Target locked: " + (int)x + " " + (int)y + " " + (int)z + " [" + dimTitle + "]";
            this.statusColor = 0x55FF55;
            CompoundTag locTag = new CompoundTag();
            locTag.putDouble("X", x);
            locTag.putDouble("Y", y);
            locTag.putDouble("Z", z);
            locTag.putString("Dimension", dimension);
            locTag.putString("RawText", dimension);
            locTag.putString("Name", "Biome: " + biomeName);
            this.updateLocalCustomLocation(locTag);
        } else {
            this.statusMessage = "No biome found nearby!";
            this.statusColor = 0xFF5555;
        }
    }

    private boolean hasBiomeLocatorUpgrade() {
        return this.gunStack != null && PortalGunItem.hasUpgrade((ItemStack)this.gunStack, (UpgradeType)UpgradeType.BIOME_LOCATOR) || PortalGunItem.hasUpgrade((CompoundTag)this.gunNbt, (UpgradeType)UpgradeType.BIOME_LOCATOR);
    }

    private boolean hasDimUpgrade() {
        return this.gunStack != null && PortalGunItem.hasUpgrade((ItemStack)this.gunStack, (UpgradeType)UpgradeType.DIMENSIONAL) || PortalGunItem.hasUpgrade((CompoundTag)this.gunNbt, (UpgradeType)UpgradeType.DIMENSIONAL);
    }

    private boolean hasPlayerLocUpgrade() {
        return this.gunStack != null && PortalGunItem.hasUpgrade((ItemStack)this.gunStack, (UpgradeType)UpgradeType.PLAYER_LOCATOR) || PortalGunItem.hasUpgrade((CompoundTag)this.gunNbt, (UpgradeType)UpgradeType.PLAYER_LOCATOR);
    }

    private boolean hasStructureScannerUpgrade() {
        return this.gunStack != null && PortalGunItem.hasUpgrade((ItemStack)this.gunStack, (UpgradeType)UpgradeType.STRUCTURE_SCANNER) || PortalGunItem.hasUpgrade((CompoundTag)this.gunNbt, (UpgradeType)UpgradeType.STRUCTURE_SCANNER);
    }

    private void populateDimensions() {
        this.availableDimensions.clear();
        Minecraft mc = Minecraft.getInstance();
        if (mc.getConnection() != null) {
            Set levels = mc.getConnection().levels();
            for (ResourceKey key : levels) {
                String dimId = key.location().toString();
                if (this.availableDimensions.contains(dimId)) continue;
                this.availableDimensions.add(dimId);
            }
        }
        if (!this.availableDimensions.contains("minecraft:overworld")) {
            this.availableDimensions.add("minecraft:overworld");
        }
        if (!this.availableDimensions.contains("minecraft:the_nether")) {
            this.availableDimensions.add("minecraft:the_nether");
        }
        if (!this.availableDimensions.contains("minecraft:the_end")) {
            this.availableDimensions.add("minecraft:the_end");
        }
        this.availableDimensions.sort((a, b) -> {
            int rankB;
            int rankA = this.getDimensionRank((String)a);
            if (rankA == (rankB = this.getDimensionRank((String)b))) return a.compareToIgnoreCase((String)b);
            return Integer.compare(rankA, rankB);
        });
        this.selectedDimIndex = this.availableDimensions.indexOf(this.selectedDimension);
        if (this.selectedDimIndex >= 0) return;
        this.selectedDimIndex = 0;
        this.selectedDimension = this.availableDimensions.get(0);
    }

    private int getDimensionRank(String dim) {
        if (dim.equals("minecraft:overworld")) {
            return 0;
        }
        if (dim.equals("minecraft:the_nether")) return 1;
        if (dim.equals("the_nether")) return 1;
        if (dim.equals("nether")) {
            return 1;
        }
        if (dim.equals("minecraft:the_end")) return 2;
        if (dim.equals("the_end")) return 2;
        if (!dim.equals("end")) return 10;
        return 2;
    }

    protected void init() {
        double initZ;
        super.init();
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            CompoundTag tag;
            CustomData customData;
            if (this.slotIndex >= 0 && this.slotIndex < player.getInventory().getContainerSize()) {
                this.gunStack = player.getInventory().getItem(this.slotIndex);
            }
            if (this.gunStack == null || !SetCoordinatesScreen.isGun(this.gunStack)) {
                if (SetCoordinatesScreen.isGun(player.getMainHandItem())) {
                    this.gunStack = player.getMainHandItem();
                } else if (SetCoordinatesScreen.isGun(player.getOffhandItem())) {
                    this.gunStack = player.getOffhandItem();
                }
            }
            if (this.gunStack != null && this.gunStack.has(DataComponents.CUSTOM_DATA) && (customData = (CustomData)this.gunStack.get(DataComponents.CUSTOM_DATA)) != null && (tag = customData.copyTag()).contains("CustomLocation")) {
                this.gunNbt.put("CustomLocation", (Tag)tag.getCompound("CustomLocation").copy());
            }
            if (this.selectedDimension.equals("minecraft:overworld")) {
                this.selectedDimension = player.level().dimension().location().toString();
            }
        }
        if (!this.hasDimUpgrade() && player != null) {
            this.selectedDimension = player.level().dimension().location().toString();
        }
        double initX = player != null ? (double)Math.round(player.getX()) : 0.0;
        double initY = player != null ? (double)Math.round(player.getY()) : 64.0;
        double d = initZ = player != null ? (double)Math.round(player.getZ()) : 0.0;
        if (this.gunNbt.contains("CustomLocation")) {
            CompoundTag custom = this.gunNbt.getCompound("CustomLocation");
            initX = custom.getDouble("X");
            initY = custom.getDouble("Y");
            initZ = custom.getDouble("Z");
            if (custom.contains("RawText") && !custom.getString("RawText").isEmpty()) {
                this.selectedDimension = custom.getString("RawText");
            } else if (custom.contains("Dimension")) {
                this.selectedDimension = custom.getString("Dimension");
            }
        }
        if (!this.hasDimUpgrade() && player != null) {
            this.selectedDimension = player.level().dimension().location().toString();
        }
        this.populateDimensions();
        int panelWidth = 290;
        int panelHeight = 225;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;
        int dimNavY = top + 36;
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"\u25c0"), b -> {
            if (this.hasDimUpgrade()) {
                this.playClickSound();
                this.cycleDimension(-1);
                return;
            }
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.playSound((SoundEvent)ModSounds.GUN_MALFUNCTION.get(), 1.0f, 1.0f);
            }
            this.statusMessage = "Dimensional Card required!";
            this.statusColor = 0xFF5555;
        }).bounds(left + 15, dimNavY, 20, 18).build());
        this.dimensionField = new EditBox(this.font, left + 38, dimNavY, 214, 18, (Component)Component.literal((String)"Dimension ID"));
        this.dimensionField.setMaxLength(64);
        this.dimensionField.setValue(this.selectedDimension);
        if (InfinityCompat.isLoaded()) {
            this.dimensionField.setHint((Component)Component.literal((String)"Dimension ID or Infinity text...").withStyle(ChatFormatting.DARK_GRAY));
        }
        this.dimensionField.setResponder(val -> {
            String trimmed = val.trim();
            if (!this.hasDimUpgrade() && Minecraft.getInstance().player != null) {
                String currentDim = Minecraft.getInstance().player.level().dimension().location().toString();
                if (!trimmed.isEmpty() && !trimmed.equalsIgnoreCase(currentDim)) {
                    this.statusMessage = "Dimensional Card required!";
                    this.statusColor = 0xFF5555;
                }
            }
            this.selectedDimension = trimmed;
            int idx = this.availableDimensions.indexOf(trimmed);
            if (idx < 0) return;
            this.selectedDimIndex = idx;
        });
        this.addRenderableWidget((GuiEventListener)this.dimensionField);
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"\u25b6"), b -> {
            if (this.hasDimUpgrade()) {
                this.playClickSound();
                this.cycleDimension(1);
                return;
            }
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.playSound((SoundEvent)ModSounds.GUN_MALFUNCTION.get(), 1.0f, 1.0f);
            }
            this.statusMessage = "Dimensional Card required!";
            this.statusColor = 0xFF5555;
        }).bounds(left + 255, dimNavY, 20, 18).build());
        int coordsY = top + 68;
        this.coordsField = new EditBox(this.font, left + 15, coordsY, 204, 18, (Component)Component.literal((String)"Coordinates"));
        this.coordsField.setValue((int)initX + " " + (int)initY + " " + (int)initZ);
        this.coordsField.setHint((Component)Component.literal((String)"X Y Z (e.g. 100 64 -200)...").withStyle(ChatFormatting.DARK_GRAY));
        this.addRenderableWidget((GuiEventListener)this.coordsField);
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"GPS"), arg_0 -> this.lambda$init$4((Player)player, arg_0)).bounds(left + 225, coordsY, 50, 18).build());
        int nameY = top + 102;
        this.nameField = new EditBox(this.font, left + 15, nameY, 260, 18, (Component)Component.literal((String)"Name"));
        this.nameField.setHint((Component)Component.literal((String)"Custom location label (optional)...").withStyle(ChatFormatting.DARK_GRAY));
        this.addRenderableWidget((GuiEventListener)this.nameField);
        int actRow1Y = top + 130;
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"Lock Coords"), b -> {
            this.playClickSound();
            this.tryApplyCoordinates(false, false);
        }).bounds(left + 15, actRow1Y, 84, 20).style(GadgetButton.Style.PRIMARY).build());
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"Target Spawn"), b -> {
            this.playClickSound();
            this.tryApplyCoordinates(true, false);
        }).bounds(left + 103, actRow1Y, 96, 20).style(GadgetButton.Style.PRIMARY).build());
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"Save"), b -> {
            this.playClickSound();
            this.tryApplyCoordinates(false, true);
        }).bounds(left + 203, actRow1Y, 72, 20).build());
        int actRow2Y = top + 163;
        boolean hasPlayerLoc = this.hasPlayerLocUpgrade();
        MutableComponent targetPlayerLabel = hasPlayerLoc ? Component.literal((String)"Target Player") : Component.literal((String)"\u00a78Target Player\u00a7r");
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)targetPlayerLabel, b -> {
            if (!this.hasPlayerLocUpgrade()) {
                this.statusMessage = "Player Locator Card required!";
                this.statusColor = 0xFF5555;
                if (Minecraft.getInstance().player == null) return;
                Minecraft.getInstance().player.playSound((SoundEvent)ModSounds.GUN_MALFUNCTION.get(), 1.0f, 1.0f);
                return;
            }
            if (!Config.allowPlayerTargeting) {
                this.statusMessage = Component.translatable((String)"message.ricks_stuff.player_targeting_disabled").getString();
                this.statusColor = 0xFF5555;
                if (Minecraft.getInstance().player == null) return;
                Minecraft.getInstance().player.playSound((SoundEvent)ModSounds.GUN_MALFUNCTION.get(), 1.0f, 1.0f);
                return;
            }
            this.playClickSound();
            Minecraft.getInstance().setScreen((Screen)new SelectPlayerScreen((Screen)this, this.gunNbt, this.slotIndex));
        }).bounds(left + 15, actRow2Y, 84, 20).build());
        boolean hasStructScanner = this.hasStructureScannerUpgrade();
        MutableComponent targetStructLabel = hasStructScanner ? Component.literal((String)"Target Struct") : Component.literal((String)"\u00a78Target Struct\u00a7r");
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)targetStructLabel, b -> {
            if (!this.hasStructureScannerUpgrade()) {
                this.statusMessage = "Structure Scanner Card required!";
                this.statusColor = 0xFF5555;
                if (Minecraft.getInstance().player == null) return;
                Minecraft.getInstance().player.playSound((SoundEvent)ModSounds.GUN_MALFUNCTION.get(), 1.0f, 1.0f);
                return;
            }
            this.playClickSound();
            Minecraft.getInstance().setScreen((Screen)new SelectStructureScreen((Screen)this, this.gunNbt, this.slotIndex));
        }).bounds(left + 103, actRow2Y, 84, 20).build());
        boolean hasBiomeLoc = this.hasBiomeLocatorUpgrade();
        MutableComponent targetBiomeLabel = hasBiomeLoc ? Component.literal((String)"Target Biome") : Component.literal((String)"\u00a78Target Biome\u00a7r");
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)targetBiomeLabel, b -> {
            if (!this.hasBiomeLocatorUpgrade()) {
                this.statusMessage = "Biome Locator Card required!";
                this.statusColor = 0xFF5555;
                if (Minecraft.getInstance().player == null) return;
                Minecraft.getInstance().player.playSound((SoundEvent)ModSounds.GUN_MALFUNCTION.get(), 1.0f, 1.0f);
                return;
            }
            this.playClickSound();
            Minecraft.getInstance().setScreen((Screen)new SelectBiomeScreen((Screen)this, this.gunNbt, this.slotIndex));
        }).bounds(left + 191, actRow2Y, 84, 20).build());
        int actRow3Y = top + 189;
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"Back"), b -> {
            this.playClickSound();
            Minecraft.getInstance().setScreen(this.parent);
        }).bounds(left + 90, actRow3Y, 110, 20).build());
    }

    private void cycleDimension(int direction) {
        if (this.availableDimensions.isEmpty()) {
            return;
        }
        this.selectedDimIndex = (this.selectedDimIndex + direction + this.availableDimensions.size()) % this.availableDimensions.size();
        this.selectedDimension = this.availableDimensions.get(this.selectedDimIndex);
        if (this.dimensionField == null) return;
        this.dimensionField.setValue(this.selectedDimension);
    }

    private String getFormattedSelectedDim() {
        String dim = this.dimensionField != null ? this.dimensionField.getValue().trim() : this.selectedDimension;
        return PortalPlacementHelper.formatDimensionName((String)(dim.isEmpty() ? "minecraft:overworld" : dim));
    }

    private void tryApplyCoordinates(boolean isWorldSpawn, boolean saveToLocations) {
        String currentDim;
        Object rawDim;
        Object object = rawDim = this.dimensionField != null ? this.dimensionField.getValue().trim() : this.selectedDimension;
        if (((String)rawDim).isEmpty()) {
            rawDim = "minecraft:overworld";
        } else if (!(((String)rawDim).contains(":") || BlenderDimensionHelper.isBlenderDimension((String)rawDim) || FishDimensionHelper.isFishDimension((String)rawDim))) {
            String lower = ((String)rawDim).toLowerCase();
            if (lower.equals("overworld") || lower.equals("the_nether") || lower.equals("nether") || lower.equals("the_end") || lower.equals("end")) {
                rawDim = "minecraft:" + (lower.equals("nether") ? "the_nether" : (lower.equals("end") ? "the_end" : lower));
            } else if (!InfinityCompat.isLoaded()) {
                rawDim = "minecraft:" + (String)rawDim;
            }
        }
        if (!(this.hasDimUpgrade() || BlenderDimensionHelper.isBlenderDimension((String)rawDim) || FishDimensionHelper.isFishDimension((String)rawDim) || this.minecraft == null || this.minecraft.player == null || ((String)rawDim).equalsIgnoreCase(currentDim = this.minecraft.player.level().dimension().location().toString()))) {
            this.selectedDimension = currentDim;
            if (this.dimensionField != null) {
                this.dimensionField.setValue(currentDim);
            }
            this.statusMessage = "Dimensional Card required!";
            this.statusColor = 0xFF5555;
            this.minecraft.player.playSound((SoundEvent)ModSounds.GUN_MALFUNCTION.get(), 1.0f, 1.0f);
            return;
        }
        this.selectedDimension = rawDim;
        String dimTitle = this.getFormattedSelectedDim();
        if (BlenderDimensionHelper.isBlenderDimension((String)rawDim)) {
            ModMessages.sendToServer((CustomPacketPayload)new C2SSetCoordinatesPacket(0.0, 0.0, 0.0, this.selectedDimension, false));
            CompoundTag locTag = new CompoundTag();
            locTag.putDouble("X", 0.0);
            locTag.putDouble("Y", 0.0);
            locTag.putDouble("Z", 0.0);
            locTag.putString("Dimension", "blenders_dimension");
            locTag.putString("RawText", this.selectedDimension);
            locTag.putBoolean("IsWorldSpawn", false);
            locTag.putString("Name", "Blender Dimension");
            this.updateLocalCustomLocation(locTag);
            if (saveToLocations) {
                String name = this.nameField.getValue().trim();
                if (name.isEmpty()) {
                    name = "Blender Dimension";
                }
                ModMessages.sendToServer((CustomPacketPayload)new C2SSaveLocationPacket(name, 0.0, 0.0, 0.0, this.selectedDimension));
            }
            if (this.minecraft != null && this.minecraft.player != null) {
                this.minecraft.player.playSound((SoundEvent)ModSounds.UI_DIAL.get(), 1.0f, 1.0f);
            }
            this.statusMessage = "Target locked: [Blender Dimension]";
            this.statusColor = 0x55FF55;
            return;
        }
        if (FishDimensionHelper.isFishDimension((String)rawDim)) {
            ModMessages.sendToServer((CustomPacketPayload)new C2SSetCoordinatesPacket(0.0, 0.0, 0.0, this.selectedDimension, false));
            CompoundTag locTag = new CompoundTag();
            locTag.putDouble("X", 0.0);
            locTag.putDouble("Y", 0.0);
            locTag.putDouble("Z", 0.0);
            locTag.putString("Dimension", "fish_dimension");
            locTag.putString("RawText", this.selectedDimension);
            locTag.putBoolean("IsWorldSpawn", false);
            locTag.putString("Name", "Dimension of Fishes");
            this.updateLocalCustomLocation(locTag);
            if (saveToLocations) {
                String name = this.nameField.getValue().trim();
                if (name.isEmpty()) {
                    name = "Dimension of Fishes";
                }
                ModMessages.sendToServer((CustomPacketPayload)new C2SSaveLocationPacket(name, 0.0, 0.0, 0.0, this.selectedDimension));
            }
            if (this.minecraft != null && this.minecraft.player != null) {
                this.minecraft.player.playSound((SoundEvent)ModSounds.UI_DIAL.get(), 1.0f, 1.0f);
            }
            this.statusMessage = "Target locked: [Dimension of Fishes]";
            this.statusColor = 0x55FF55;
            return;
        }
        if (isWorldSpawn) {
            double spawnX = 0.0;
            double spawnY = 64.0;
            double spawnZ = 0.0;
            String lowerDim = ((String)rawDim).toLowerCase();
            if (lowerDim.equals("the_end") || lowerDim.equals("end") || lowerDim.equals("minecraft:the_end")) {
                spawnX = 100.0;
                spawnY = 49.0;
                spawnZ = 0.0;
            } else if (lowerDim.equals("the_nether") || lowerDim.equals("nether") || lowerDim.equals("minecraft:the_nether")) {
                spawnX = 0.0;
                spawnY = 64.0;
                spawnZ = 0.0;
            } else if (this.minecraft != null && this.minecraft.level != null && this.minecraft.level.dimension().location().toString().equals(rawDim)) {
                BlockPos sp = this.minecraft.level.getSharedSpawnPos();
                spawnX = sp.getX();
                spawnY = sp.getY();
                spawnZ = sp.getZ();
            }
            if (this.coordsField != null) {
                this.coordsField.setValue((int)spawnX + " " + (int)spawnY + " " + (int)spawnZ);
            }
            ModMessages.sendToServer((CustomPacketPayload)new C2SSetCoordinatesPacket(spawnX, spawnY, spawnZ, this.selectedDimension, true));
            CompoundTag locTag = new CompoundTag();
            locTag.putDouble("X", spawnX);
            locTag.putDouble("Y", spawnY);
            locTag.putDouble("Z", spawnZ);
            locTag.putString("Dimension", this.selectedDimension);
            locTag.putString("RawText", this.selectedDimension);
            locTag.putBoolean("IsWorldSpawn", true);
            locTag.putString("Name", dimTitle + " (Spawn)");
            this.updateLocalCustomLocation(locTag);
            if (saveToLocations) {
                Object name = this.nameField.getValue().trim();
                if (((String)name).isEmpty()) {
                    name = dimTitle + " (Spawn)";
                }
                ModMessages.sendToServer((CustomPacketPayload)new C2SSaveLocationPacket((String)name, spawnX, spawnY, spawnZ, this.selectedDimension));
            }
            if (this.minecraft != null && this.minecraft.player != null) {
                this.minecraft.player.playSound((SoundEvent)ModSounds.UI_DIAL.get(), 1.0f, 1.0f);
            }
            this.statusMessage = "Target locked: [" + dimTitle + "] World Spawn";
            this.statusColor = 0x55FF55;
            return;
        }
        try {
            String coordsText = this.coordsField != null ? this.coordsField.getValue().trim() : "";
            String[] tokens = coordsText.split("[,\\s]+");
            if (tokens.length < 3) {
                this.statusMessage = "Enter X Y Z separated by space!";
                this.statusColor = 0xFF5555;
                if (this.minecraft == null) return;
                if (this.minecraft.player == null) return;
                this.minecraft.player.playSound((SoundEvent)ModSounds.GUN_MALFUNCTION.get(), 1.0f, 1.0f);
                return;
            }
            double x = Double.parseDouble(tokens[0]);
            double y = Double.parseDouble(tokens[1]);
            double z = Double.parseDouble(tokens[2]);
            if (y < -64.0 || y > 320.0) {
                this.statusMessage = "Y must be between -64 and 320";
                this.statusColor = 0xFF5555;
                if (this.minecraft == null) return;
                if (this.minecraft.player == null) return;
                this.minecraft.player.playSound((SoundEvent)ModSounds.GUN_MALFUNCTION.get(), 1.0f, 1.0f);
                return;
            }
            ModMessages.sendToServer((CustomPacketPayload)new C2SSetCoordinatesPacket(x, y, z, this.selectedDimension, false));
            CompoundTag locTag = new CompoundTag();
            locTag.putDouble("X", x);
            locTag.putDouble("Y", y);
            locTag.putDouble("Z", z);
            locTag.putString("Dimension", this.selectedDimension);
            locTag.putString("RawText", this.selectedDimension);
            locTag.putBoolean("IsWorldSpawn", false);
            locTag.putString("Name", dimTitle + " (" + (int)x + ", " + (int)y + ", " + (int)z + ")");
            this.updateLocalCustomLocation(locTag);
            if (saveToLocations) {
                Object name = this.nameField.getValue().trim();
                if (((String)name).isEmpty()) {
                    name = dimTitle + " (" + (int)x + ", " + (int)y + ", " + (int)z + ")";
                }
                ModMessages.sendToServer((CustomPacketPayload)new C2SSaveLocationPacket((String)name, x, y, z, this.selectedDimension));
            }
            if (this.minecraft != null && this.minecraft.player != null) {
                this.minecraft.player.playSound((SoundEvent)ModSounds.UI_DIAL.get(), 1.0f, 1.0f);
            }
            this.statusMessage = "Target locked: " + (int)x + " " + (int)y + " " + (int)z + " [" + dimTitle + "]";
            this.statusColor = 0x55FF55;
        }
        catch (NumberFormatException e) {
            this.statusMessage = "Error: Invalid coordinates!";
            if (this.minecraft == null) return;
            if (this.minecraft.player == null) return;
            this.minecraft.player.playSound((SoundEvent)ModSounds.GUN_MALFUNCTION.get(), 1.0f, 1.0f);
        }
    }

    private void updateLocalCustomLocation(CompoundTag locTag) {
        if (!locTag.contains("SafePlacement")) {
            boolean safe = this.gunNbt.contains("SafePlacement") ? this.gunNbt.getBoolean("SafePlacement") : true;
            locTag.putBoolean("SafePlacement", safe);
        }
        this.gunNbt.put("CustomLocation", (Tag)locTag);
        this.gunNbt.putString("Mode", PortalMode.CUSTOM.getName());
        if (this.gunStack == null) return;
        PortalGunItem.setCustomLocation((ItemStack)this.gunStack, (CompoundTag)locTag);
        PortalGunItem.setMode((ItemStack)this.gunStack, (PortalMode)PortalMode.CUSTOM);
    }

    private void playClickSound() {
        if (Minecraft.getInstance().player == null) return;
        Minecraft.getInstance().player.playSound((SoundEvent)ModSounds.UI_PRESS.get(), 1.0f, 1.0f);
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        int panelWidth = 290;
        int panelHeight = 225;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;
        GadgetGuiUtils.renderChassis((GuiGraphics)guiGraphics, (int)left, (int)top, (int)panelWidth, (int)panelHeight);
        GadgetGuiUtils.renderHeader((GuiGraphics)guiGraphics, (int)(left + 4), (int)(top + 4), (int)(panelWidth - 8), (int)22);
        guiGraphics.drawString(this.font, "DIMENSIONAL COORDINATE TRANSMITTER", left + 12, top + 7, -12720539, false);
        boolean hasDim = this.hasDimUpgrade();
        if (!hasDim) {
            guiGraphics.drawString(this.font, "Target Dim: \u00a7e" + this.getFormattedSelectedDim() + " \u00a77(Current)\u00a7r", left + 15, top + 26, -7696228, false);
        } else {
            Object dimCounter = this.availableDimensions.contains(this.selectedDimension) ? " (" + (this.availableDimensions.indexOf(this.selectedDimension) + 1) + "/" + this.availableDimensions.size() + ")" : (InfinityCompat.isLoaded() ? " \u00a7d[Infinity Universe]\u00a7r" : " [Custom]");
            guiGraphics.drawString(this.font, "Target Dim: \u00a7e" + this.getFormattedSelectedDim() + "\u00a7r" + (String)dimCounter, left + 15, top + 26, -7696228, false);
        }
        guiGraphics.drawString(this.font, "Coordinates (X Y Z)", left + 15, top + 57, -7696228, false);
        guiGraphics.drawString(this.font, "Location Label (Optional)", left + 15, top + 91, -7696228, false);
        if (this.isScanning) {
            int dots = (int)(System.currentTimeMillis() / 400L % 4L);
            String dotStr = switch (dots) {
                case 1 -> ".";
                case 2 -> "..";
                case 3 -> "...";
                default -> "";
            };
            String scanText = "Scanning" + dotStr;
            guiGraphics.drawCenteredString(this.font, "\u00a7e" + scanText, left + panelWidth / 2, top + 156, -13312);
        } else if (!this.statusMessage.isEmpty()) {
            Object displayMsg = this.statusMessage;
            int maxMsgWidth = panelWidth - 24;
            if (this.font.width((String)displayMsg) > maxMsgWidth) {
                displayMsg = this.font.plainSubstrByWidth((String)displayMsg, maxMsgWidth - 6) + "\u2026";
            }
            guiGraphics.drawCenteredString(this.font, (String)displayMsg, left + panelWidth / 2, top + 156, this.statusColor);
        }
        Iterator iterator = this.renderables.iterator();
        while (iterator.hasNext()) {
            Renderable renderable = (Renderable)iterator.next();
            renderable.render(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    private static boolean isGun(ItemStack s) {
        return s != null && !s.isEmpty() && (s.getItem() instanceof PortalGunItem || s.getItem() instanceof DischargedPortalGunItem);
    }

    public boolean isPauseScreen() {
        return false;
    }

    private /* synthetic */ void lambda$init$4(Player player, Button b) {
        this.playClickSound();
        if (player == null) return;
        this.coordsField.setValue((int)player.getX() + " " + (int)player.getY() + " " + (int)player.getZ());
        this.selectedDimension = player.level().dimension().location().toString();
        this.selectedDimIndex = this.availableDimensions.indexOf(this.selectedDimension);
        if (this.selectedDimIndex < 0) {
            this.selectedDimIndex = 0;
        }
        if (this.dimensionField != null) {
            this.dimensionField.setValue(this.selectedDimension);
        }
        this.statusColor = 0x55FF55;
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.EditBox
 *  net.minecraft.client.gui.components.Renderable
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.item.ItemStack
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton$Style
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetGuiUtils
 *  org.ExtraTortik.ricks_stuff.client.gui.HistoryScreen
 *  org.ExtraTortik.ricks_stuff.client.gui.HowToUseScreen
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SResetGunPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SSelfDestructPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SUpdateGunSettingsPacket
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import java.util.Iterator;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetButton;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetGuiUtils;
import org.ExtraTortik.ricks_stuff.client.gui.HistoryScreen;
import org.ExtraTortik.ricks_stuff.client.gui.HowToUseScreen;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.C2SResetGunPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SSelfDestructPacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SUpdateGunSettingsPacket;

public class SettingsScreen
extends Screen {
    public static final float[] SCALE_OPTIONS = new float[]{0.5f, 1.0f, 1.5f, 2.0f};
    public static final String[] SCALE_LABELS = new String[]{"0.5x", "1.0x", "1.5x", "2.0x"};
    private final Screen parent;
    private final CompoundTag gunNbt;
    private final int slotIndex;
    private ItemStack gunStack;
    private int scaleIdx = 3;
    private boolean autoClose = false;
    private boolean safePlacement = true;
    private boolean chargeBar = true;
    private boolean autoAirDetonate = false;
    private int airDistance = 15;
    private EditBox airDistanceField;
    private boolean confirmReset = false;
    private boolean confirmSelfDestruct = false;
    private String statusMsg = "";

    public SettingsScreen(Screen parent, CompoundTag gunNbt, int slotIndex) {
        super((Component)Component.literal((String)"Portal Gun Settings"));
        this.parent = parent;
        this.gunNbt = gunNbt != null ? gunNbt : new CompoundTag();
        this.slotIndex = slotIndex;
        this.loadSettingsFromNbt();
    }

    private void loadSettingsFromNbt() {
        if (this.gunNbt.contains("Scale")) {
            float scale = this.gunNbt.getFloat("Scale");
            this.scaleIdx = C2SUpdateGunSettingsPacket.getScaleIndex((float)scale);
        }
        if (this.gunNbt.contains("AutoClose")) {
            this.autoClose = this.gunNbt.getBoolean("AutoClose");
        }
        if (this.gunNbt.contains("SafePlacement")) {
            this.safePlacement = this.gunNbt.getBoolean("SafePlacement");
        }
        if (this.gunNbt.contains("ChargeBar")) {
            this.chargeBar = this.gunNbt.getBoolean("ChargeBar");
        }
        if (this.gunNbt.contains("AutoAirDetonate")) {
            this.autoAirDetonate = this.gunNbt.getBoolean("AutoAirDetonate");
        }
        if (!this.gunNbt.contains("AirDistance")) return;
        this.airDistance = this.gunNbt.getInt("AirDistance");
    }

    protected void init() {
        float currentScale;
        super.init();
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (this.slotIndex >= 0 && this.slotIndex < player.getInventory().getContainerSize()) {
                this.gunStack = player.getInventory().getItem(this.slotIndex);
            }
            if (this.gunStack == null || !SettingsScreen.isGun(this.gunStack)) {
                if (SettingsScreen.isGun(player.getMainHandItem())) {
                    this.gunStack = player.getMainHandItem();
                } else if (SettingsScreen.isGun(player.getOffhandItem())) {
                    this.gunStack = player.getOffhandItem();
                }
            }
        }
        float f = this.gunStack != null ? PortalGunItem.getScale((ItemStack)this.gunStack) : (currentScale = this.gunNbt.contains("Scale") ? this.gunNbt.getFloat("Scale") : 2.0f);
        this.scaleIdx = Math.abs(currentScale - 0.5f) < 0.1f ? 0 : (Math.abs(currentScale - 1.0f) < 0.1f ? 1 : (Math.abs(currentScale - 1.5f) < 0.1f ? 2 : (Math.abs(currentScale - 2.0f) < 0.1f ? 3 : 3)));
        int panelWidth = 320;
        int panelHeight = 224;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;
        int rowH = 18;
        int startY = top + 32;
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)("Scale: " + this.getScaleLabel())), b -> {
            this.playClickSound();
            this.scaleIdx = (this.scaleIdx + 1) % 4;
            b.setMessage((Component)Component.literal((String)("Scale: " + this.getScaleLabel())));
        }).bounds(left + 15, startY, 140, rowH).build());
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)("Auto Close: " + (this.autoClose ? "\u00a7aON" : "\u00a7cOFF"))), b -> {
            this.playClickSound();
            this.autoClose = !this.autoClose;
            b.setMessage((Component)Component.literal((String)("Auto Close: " + (this.autoClose ? "\u00a7aON" : "\u00a7cOFF"))));
        }).bounds(left + 165, startY, 140, rowH).build());
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)("Safe Placement: " + (this.safePlacement ? "\u00a7aON" : "\u00a7cOFF"))), b -> {
            this.playClickSound();
            this.safePlacement = !this.safePlacement;
            b.setMessage((Component)Component.literal((String)("Safe Placement: " + (this.safePlacement ? "\u00a7aON" : "\u00a7cOFF"))));
        }).bounds(left + 15, startY + 21, 140, rowH).build());
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)("HUD Status Bar: " + (this.chargeBar ? "\u00a7aON" : "\u00a7cOFF"))), b -> {
            this.playClickSound();
            this.chargeBar = !this.chargeBar;
            b.setMessage((Component)Component.literal((String)("HUD Status Bar: " + (this.chargeBar ? "\u00a7aON" : "\u00a7cOFF"))));
        }).bounds(left + 165, startY + 21, 140, rowH).build());
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)("Air Deploy: " + (this.autoAirDetonate ? "\u00a7aON" : "\u00a7cOFF"))), b -> {
            this.playClickSound();
            this.autoAirDetonate = !this.autoAirDetonate;
            b.setMessage((Component)Component.literal((String)("Air Deploy: " + (this.autoAirDetonate ? "\u00a7aON" : "\u00a7cOFF"))));
            this.updateAirDistanceFieldState();
        }).bounds(left + 15, startY + 42, 140, rowH).build());
        this.airDistanceField = new EditBox(this.font, left + 235, startY + 42, 70, rowH, (Component)Component.literal((String)"Deploy Distance"));
        this.airDistanceField.setMaxLength(4);
        this.airDistanceField.setFilter(s -> s.matches("\\d*"));
        this.airDistanceField.setValue(String.valueOf(this.airDistance));
        this.airDistanceField.setHint((Component)Component.literal((String)"m").withStyle(ChatFormatting.DARK_GRAY));
        this.updateAirDistanceFieldState();
        this.addRenderableWidget((GuiEventListener)this.airDistanceField);
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"View History"), b -> {
            this.playClickSound();
            Minecraft.getInstance().setScreen((Screen)new HistoryScreen((Screen)this, this.gunNbt, this.slotIndex));
        }).bounds(left + 15, startY + 63, 290, rowH).build());
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"Controls & Guide"), b -> {
            this.playClickSound();
            Minecraft.getInstance().setScreen((Screen)new HowToUseScreen(this.gunStack, this.slotIndex, (Screen)this));
        }).bounds(left + 15, startY + 84, 290, 18).build());
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)(this.confirmReset ? "Confirm Reset?" : "Reset Memory")), b -> {
            this.playClickSound();
            if (!this.confirmReset) {
                this.confirmReset = true;
                b.setMessage((Component)Component.literal((String)"Confirm Reset?"));
            } else {
                ModMessages.sendToServer((CustomPacketPayload)new C2SResetGunPacket());
                this.confirmReset = false;
                this.statusMsg = "Gun memory reset.";
                b.setMessage((Component)Component.literal((String)"Reset Memory"));
            }
        }).bounds(left + 15, startY + 105, 290, 18).style(GadgetButton.Style.DANGER).build());
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)(this.confirmSelfDestruct ? "CONFIRM DETONATE?" : "Self-Destruct")), b -> {
            this.playClickSound();
            if (!this.confirmSelfDestruct) {
                this.confirmSelfDestruct = true;
                b.setMessage((Component)Component.literal((String)"CONFIRM DETONATE?"));
            } else {
                ModMessages.sendToServer((CustomPacketPayload)new C2SSelfDestructPacket());
                this.onClose();
            }
        }).bounds(left + 15, startY + 126, 290, 18).style(GadgetButton.Style.DANGER).build());
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"Save Settings"), b -> {
            this.playClickSound();
            this.applySettings();
            this.statusMsg = "Settings applied.";
        }).bounds(left + 15, top + 194, 140, 20).style(GadgetButton.Style.PRIMARY).build());
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"Back"), b -> {
            this.playClickSound();
            this.applySettings();
            Minecraft.getInstance().setScreen(this.parent);
        }).bounds(left + 165, top + 194, 140, 20).build());
    }

    private void updateAirDistanceFieldState() {
        if (this.airDistanceField == null) return;
        this.airDistanceField.setEditable(this.autoAirDetonate);
        this.airDistanceField.setTextColor(this.autoAirDetonate ? 0xFFFFFF : 0x777777);
    }

    private void applySettings() {
        String val;
        int dist = this.airDistance;
        if (this.airDistanceField != null && !(val = this.airDistanceField.getValue().trim()).isEmpty()) {
            try {
                dist = Integer.parseInt(val);
                dist = Math.max(1, Math.min(500, dist));
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        this.airDistance = dist;
        if (this.airDistanceField != null) {
            this.airDistanceField.setValue(String.valueOf(this.airDistance));
        }
        float scale = switch (this.scaleIdx) {
            case 0 -> 0.5f;
            case 1 -> 1.0f;
            case 2 -> 1.5f;
            case 3 -> 2.0f;
            default -> 1.0f;
        };
        this.gunNbt.putFloat("Scale", scale);
        if (this.gunStack != null) {
            PortalGunItem.setScale((ItemStack)this.gunStack, (float)scale);
        }
        this.gunNbt.putBoolean("AutoClose", this.autoClose);
        this.gunNbt.putBoolean("SafePlacement", this.safePlacement);
        this.gunNbt.putBoolean("ChargeBar", this.chargeBar);
        this.gunNbt.putBoolean("AutoAirDetonate", this.autoAirDetonate);
        this.gunNbt.putInt("AirDistance", this.airDistance);
        ModMessages.sendToServer((CustomPacketPayload)new C2SUpdateGunSettingsPacket(this.scaleIdx, this.autoClose, this.safePlacement, this.chargeBar, this.autoAirDetonate, this.airDistance));
    }

    private String getScaleLabel() {
        return switch (this.scaleIdx) {
            case 0 -> "\u00a7e0.5x";
            case 1 -> "\u00a7a1.0x";
            case 2 -> "\u00a7b1.5x";
            case 3 -> "\u00a7d2.0x";
            default -> "\u00a7a1.0x";
        };
    }

    private void playClickSound() {
        if (Minecraft.getInstance().player == null) return;
        Minecraft.getInstance().player.playSound((SoundEvent)ModSounds.UI_PRESS.get(), 1.0f, 1.0f);
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        int panelWidth = 320;
        int panelHeight = 224;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;
        int startY = top + 32;
        GadgetGuiUtils.renderChassis((GuiGraphics)guiGraphics, (int)left, (int)top, (int)panelWidth, (int)panelHeight);
        GadgetGuiUtils.renderHeader((GuiGraphics)guiGraphics, (int)(left + 4), (int)(top + 4), (int)(panelWidth - 8), (int)22);
        guiGraphics.drawString(this.font, "DEVICE DIAGNOSTICS & CONFIG", left + 12, top + 7, -12720539, false);
        int labelColor = this.autoAirDetonate ? -13183000 : -7696228;
        guiGraphics.drawString(this.font, "Deploy Dist:", left + 165, startY + 47, labelColor, false);
        if (!this.statusMsg.isEmpty()) {
            guiGraphics.drawCenteredString(this.font, this.statusMsg, left + panelWidth / 2, top + 178, -12720539);
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
}

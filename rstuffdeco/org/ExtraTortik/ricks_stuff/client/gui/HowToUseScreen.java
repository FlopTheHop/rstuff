/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Renderable
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.item.ItemStack
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetGuiUtils
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import java.util.Iterator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetButton;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetGuiUtils;
import org.ExtraTortik.ricks_stuff.init.ModSounds;

public class HowToUseScreen
extends Screen {
    private final ItemStack gunStack;
    private final int slotIndex;
    private final Screen parentScreen;

    public HowToUseScreen(ItemStack gunStack, int slotIndex, Screen parentScreen) {
        super((Component)Component.literal((String)"How to Use Portal Gun"));
        this.gunStack = gunStack;
        this.slotIndex = slotIndex;
        this.parentScreen = parentScreen;
    }

    public HowToUseScreen(Screen parentScreen, CompoundTag gunNbt, int slotIndex) {
        this(ItemStack.EMPTY, slotIndex, parentScreen);
    }

    protected void init() {
        super.init();
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"Back"), btn -> {
            if (this.minecraft == null) return;
            if (this.minecraft.player != null) {
                this.minecraft.player.playSound((SoundEvent)ModSounds.UI_PRESS.get(), 1.0f, 1.0f);
            }
            this.minecraft.setScreen(this.parentScreen);
        }).bounds(centerX - 100, centerY + 86, 200, 20).build());
    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        int panelWidth = 320;
        int panelHeight = 225;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;
        GadgetGuiUtils.renderChassis((GuiGraphics)graphics, (int)left, (int)top, (int)panelWidth, (int)panelHeight);
        GadgetGuiUtils.renderHeader((GuiGraphics)graphics, (int)(left + 4), (int)(top + 4), (int)(panelWidth - 8), (int)24);
        graphics.drawString(this.font, "OPERATION & TELEMETRY GUIDE", left + 12, top + 8, -12720539, false);
        int termY = top + 34;
        int termH = 150;
        GadgetGuiUtils.renderTerminalScreen((GuiGraphics)graphics, (int)(left + 12), (int)termY, (int)(panelWidth - 24), (int)termH);
        int textY = termY + 6;
        graphics.drawString(this.font, "Fire Portal:", left + 20, textY, -19674, false);
        graphics.drawString(this.font, "Right-Click (Aim at surface / in air)", left + 100, textY, -1, false);
        graphics.drawString(this.font, "Air Deploy:", left + 20, textY += 16, -19674, false);
        graphics.drawString(this.font, "Re-click in flight OR Auto in Settings", left + 100, textY, -1, false);
        graphics.drawString(this.font, "Close All:", left + 20, textY += 16, -19674, false);
        graphics.drawString(this.font, "Press [R] key anytime with gun", left + 100, textY, -1, false);
        graphics.drawString(this.font, "Open Menu:", left + 20, textY += 16, -19674, false);
        graphics.drawString(this.font, "Sneak + Right-Click", left + 100, textY, -1, false);
        graphics.drawString(this.font, "Close Portal:", left + 20, textY += 16, -19674, false);
        graphics.drawString(this.font, "Left-Click while aiming at portal", left + 100, textY, -1, false);
        graphics.drawString(this.font, "Radial Menu:", left + 20, textY += 16, -19674, false);
        graphics.drawString(this.font, "Press [V] key to select location", left + 100, textY, -1, false);
        graphics.drawString(this.font, "Load Fluid:", left + 20, textY += 16, -19674, false);
        graphics.drawString(this.font, "Hold Tube in offhand + Use Base", left + 100, textY, -1, false);
        graphics.drawString(this.font, "Dismount Gun:", left + 20, textY += 16, -19674, false);
        graphics.drawString(this.font, "Use 'Eject Fluid Tube' in Menu", left + 100, textY, -1, false);
        Iterator iterator = this.renderables.iterator();
        while (iterator.hasNext()) {
            Renderable renderable = (Renderable)iterator.next();
            renderable.render(graphics, mouseX, mouseY, partialTick);
        }
    }

    public boolean isPauseScreen() {
        return false;
    }
}

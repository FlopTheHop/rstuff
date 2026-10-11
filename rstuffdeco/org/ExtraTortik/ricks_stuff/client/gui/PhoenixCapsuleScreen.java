/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.InventoryMenu
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  org.ExtraTortik.ricks_stuff.block.entity.PhoenixCapsuleBlockEntity
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton$Style
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetGuiUtils
 *  org.ExtraTortik.ricks_stuff.client.gui.PhoenixCapsuleMenu
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.DnaSampleFlaskItem
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SRepairCapsulePacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SStartClonePrintPacket
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.ExtraTortik.ricks_stuff.block.entity.PhoenixCapsuleBlockEntity;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetButton;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetGuiUtils;
import org.ExtraTortik.ricks_stuff.client.gui.PhoenixCapsuleMenu;
import org.ExtraTortik.ricks_stuff.init.ModItems;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.DnaSampleFlaskItem;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.C2SRepairCapsulePacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SStartClonePrintPacket;

public class PhoenixCapsuleScreen
extends AbstractContainerScreen<PhoenixCapsuleMenu> {
    private static final ResourceLocation VANILLA_CONTAINER_BG = ResourceLocation.fromNamespaceAndPath((String)"minecraft", (String)"textures/gui/container/generic_54.png");
    private Button printButton;
    private Button repairButton;

    public PhoenixCapsuleScreen(PhoenixCapsuleMenu menu, Inventory playerInventory, Component title) {
        super((AbstractContainerMenu)menu, playerInventory, title);
        this.imageWidth = 196;
        this.imageHeight = 220;
        this.inventoryLabelX = 18;
        this.inventoryLabelY = 126;
    }

    protected void init() {
        super.init();
        int left = this.leftPos;
        int top = this.topPos;
        this.printButton = GadgetButton.gadget((Component)Component.translatable((String)"gui.ricks_stuff.phoenix_capsule.print_clone"), b -> {
            if (this.minecraft != null && this.minecraft.player != null) {
                this.minecraft.player.playSound((SoundEvent)ModSounds.UI_PRESS.get(), 1.0f, 1.0f);
            }
            if (((PhoenixCapsuleMenu)this.menu).getBlockPos() == null) return;
            ModMessages.sendToServer((CustomPacketPayload)new C2SStartClonePrintPacket(((PhoenixCapsuleMenu)this.menu).getBlockPos()));
        }).bounds(left + 94, top + 101, 92, 18).style(GadgetButton.Style.PRIMARY).build();
        this.printButton.visible = false;
        this.addRenderableWidget((GuiEventListener)this.printButton);
        this.repairButton = GadgetButton.gadget((Component)Component.translatable((String)"gui.ricks_stuff.phoenix_capsule.repair_tank"), b -> {
            if (this.minecraft != null && this.minecraft.player != null) {
                this.minecraft.player.playSound((SoundEvent)ModSounds.UI_PRESS.get(), 1.0f, 1.0f);
            }
            if (((PhoenixCapsuleMenu)this.menu).getBlockPos() == null) return;
            ModMessages.sendToServer((CustomPacketPayload)new C2SRepairCapsulePacket(((PhoenixCapsuleMenu)this.menu).getBlockPos()));
        }).bounds(left + 94, top + 101, 92, 18).style(GadgetButton.Style.DANGER).build();
        this.repairButton.visible = false;
        this.addRenderableWidget((GuiEventListener)this.repairButton);
        this.updateButtonStates();
    }

    protected void containerTick() {
        super.containerTick();
        this.updateButtonStates();
    }

    private void updateButtonStates() {
        boolean isBroken = ((PhoenixCapsuleMenu)this.menu).isBroken();
        boolean isPrinting = ((PhoenixCapsuleMenu)this.menu).isPrinting();
        boolean isReady = ((PhoenixCapsuleMenu)this.menu).isReady();
        boolean hasEnoughMeat = false;
        boolean hasDna = false;
        boolean hasRepairMaterials = false;
        if (((PhoenixCapsuleMenu)this.menu).slots.size() >= 5) {
            ItemStack meatStack = ((PhoenixCapsuleMenu)this.menu).getSlot(0).getItem();
            hasEnoughMeat = PhoenixCapsuleBlockEntity.isRawMeat((ItemStack)meatStack) && meatStack.getCount() >= 32;
            ItemStack dnaStack = ((PhoenixCapsuleMenu)this.menu).getSlot(4).getItem();
            hasDna = !dnaStack.isEmpty() && dnaStack.is((Item)ModItems.DNA_SAMPLE_FLASK.get()) && DnaSampleFlaskItem.hasProfile((ItemStack)dnaStack);
            ItemStack glassStack = ((PhoenixCapsuleMenu)this.menu).getSlot(1).getItem();
            ItemStack waterStack = ((PhoenixCapsuleMenu)this.menu).getSlot(2).getItem();
            boolean bl = hasRepairMaterials = glassStack.getCount() >= 2 && glassStack.is(Items.GLASS) && waterStack.getCount() >= 1 && waterStack.is(Items.WATER_BUCKET);
        }
        if (this.printButton != null) {
            this.printButton.visible = !isBroken && !isPrinting && !isReady;
            this.printButton.active = hasEnoughMeat && hasDna;
        }
        if (this.repairButton == null) return;
        this.repairButton.visible = isBroken;
        this.repairButton.active = hasRepairMaterials;
    }

    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int crtBorder;
        int left = this.leftPos;
        int top = this.topPos;
        guiGraphics.fill(left, top, left + this.imageWidth, top + this.imageHeight, -300804582);
        GadgetGuiUtils.renderChassis((GuiGraphics)guiGraphics, (int)left, (int)top, (int)this.imageWidth, (int)this.imageHeight);
        boolean isBroken = ((PhoenixCapsuleMenu)this.menu).isBroken();
        boolean isPrinting = ((PhoenixCapsuleMenu)this.menu).isPrinting();
        boolean isReady = ((PhoenixCapsuleMenu)this.menu).isReady();
        int n = isBroken ? -2937041 : (isPrinting ? -14505387 : (crtBorder = isReady ? -14492280 : -14790592));
        int crtBg = isBroken ? 0x33FF2222 : (isPrinting ? 0x2222FF44 : (isReady ? 0x2222FF88 : -16248309));
        guiGraphics.fill(left + 10, top + 18, left + 118, top + 96, crtBg);
        guiGraphics.renderOutline(left + 10, top + 18, 108, 78, crtBorder);
        int crtCenterX = left + 64;
        if (isBroken) {
            guiGraphics.drawCenteredString(this.font, (Component)Component.translatable((String)"gui.ricks_stuff.phoenix_capsule.breached"), crtCenterX, top + 38, 0xFF5555);
            guiGraphics.drawCenteredString(this.font, (Component)Component.translatable((String)"gui.ricks_stuff.phoenix_capsule.req_repairs"), crtCenterX, top + 52, -13312);
            guiGraphics.drawCenteredString(this.font, (Component)Component.translatable((String)"gui.ricks_stuff.phoenix_capsule.rep_items"), crtCenterX, top + 66, -2236963);
        } else if (isPrinting) {
            String owner;
            guiGraphics.drawCenteredString(this.font, (Component)Component.translatable((String)"gui.ricks_stuff.phoenix_capsule.synthesizing"), crtCenterX, top + 34, 0x55FF55);
            int progressPixels = ((PhoenixCapsuleMenu)this.menu).getScaledProgress(94);
            guiGraphics.fill(left + 17, top + 48, left + 111, top + 60, -15063520);
            guiGraphics.fill(left + 17, top + 48, left + 17 + progressPixels, top + 60, -14492331);
            guiGraphics.renderOutline(left + 17, top + 48, 94, 12, -11141240);
            int totalTicks = ((PhoenixCapsuleMenu)this.menu).getTotalPrintTicks();
            int pct = totalTicks > 0 ? ((PhoenixCapsuleMenu)this.menu).getPrintProgress() * 100 / totalTicks : 0;
            guiGraphics.drawCenteredString(this.font, pct + "%", crtCenterX, top + 50, 0xFFFFFF);
            int remSec = Math.max(0, (totalTicks - ((PhoenixCapsuleMenu)this.menu).getPrintProgress()) / 20);
            String string = owner = ((PhoenixCapsuleMenu)this.menu).getBlockEntity() != null ? ((PhoenixCapsuleMenu)this.menu).getBlockEntity().getOwnerName() : "";
            if (owner != null && !owner.isEmpty()) {
                guiGraphics.drawCenteredString(this.font, "\u00a77Subject: \u00a7a" + owner, crtCenterX, top + 66, 0xAAAAAA);
                guiGraphics.drawCenteredString(this.font, "\u00a77ETA: " + remSec + "s", crtCenterX, top + 78, 0x88AAAA);
            } else {
                guiGraphics.drawCenteredString(this.font, "\u00a77ETA: " + remSec + "s", crtCenterX, top + 68, 0xAAAAAA);
            }
        } else if (isReady) {
            String owner;
            guiGraphics.drawCenteredString(this.font, (Component)Component.translatable((String)"gui.ricks_stuff.phoenix_capsule.stasis"), crtCenterX, top + 42, 0x55FFAA);
            String string = owner = ((PhoenixCapsuleMenu)this.menu).getBlockEntity() != null ? ((PhoenixCapsuleMenu)this.menu).getBlockEntity().getOwnerName() : "";
            if (owner != null && !owner.isEmpty()) {
                guiGraphics.drawCenteredString(this.font, "\u00a77Subject: \u00a7a" + owner, crtCenterX, top + 56, 0x55FFAA);
                guiGraphics.drawCenteredString(this.font, (Component)Component.translatable((String)"gui.ricks_stuff.phoenix_capsule.ready"), crtCenterX, top + 70, 0xAAAAAA);
            } else {
                guiGraphics.drawCenteredString(this.font, (Component)Component.translatable((String)"gui.ricks_stuff.phoenix_capsule.ready"), crtCenterX, top + 60, 0xAAAAAA);
            }
        } else {
            boolean hasDna;
            ItemStack meatStack = ((PhoenixCapsuleMenu)this.menu).slots.size() > 0 ? ((PhoenixCapsuleMenu)this.menu).getSlot(0).getItem() : ItemStack.EMPTY;
            boolean hasMeat = PhoenixCapsuleBlockEntity.isRawMeat((ItemStack)meatStack) && meatStack.getCount() >= 32;
            ItemStack dnaStack = ((PhoenixCapsuleMenu)this.menu).slots.size() > 4 ? ((PhoenixCapsuleMenu)this.menu).getSlot(4).getItem() : ItemStack.EMPTY;
            boolean bl = hasDna = !dnaStack.isEmpty() && dnaStack.is((Item)ModItems.DNA_SAMPLE_FLASK.get()) && DnaSampleFlaskItem.hasProfile((ItemStack)dnaStack);
            if (!hasMeat) {
                guiGraphics.drawCenteredString(this.font, (Component)Component.translatable((String)"gui.ricks_stuff.phoenix_capsule.standby"), crtCenterX, top + 42, 0xCCCCCC);
                guiGraphics.drawCenteredString(this.font, (Component)Component.translatable((String)"gui.ricks_stuff.phoenix_capsule.insert_meat", (Object[])new Object[]{32}), crtCenterX, top + 56, 0x88CCAA);
                if (!hasDna) {
                    guiGraphics.drawCenteredString(this.font, "\u00a78+ DNA Sample", crtCenterX, top + 70, 0x557766);
                } else {
                    p = DnaSampleFlaskItem.getProfile((ItemStack)dnaStack);
                    name = p != null && p.getName() != null ? p.getName() : "Unknown";
                    guiGraphics.drawCenteredString(this.font, "\u00a7bDNA: \u00a7f" + name, crtCenterX, top + 70, 0x55D5FF);
                }
            } else if (!hasDna) {
                guiGraphics.drawCenteredString(this.font, "\u00a7eDNA REQUIRED", crtCenterX, top + 44, 0xFFDD44);
                guiGraphics.drawCenteredString(this.font, "\u00a77Insert DNA Flask", crtCenterX, top + 60, 0xAAAAAA);
            } else {
                p = DnaSampleFlaskItem.getProfile((ItemStack)dnaStack);
                name = p != null && p.getName() != null ? p.getName() : "Unknown";
                guiGraphics.drawCenteredString(this.font, "\u00a7aREADY TO CLONE", crtCenterX, top + 44, 0x55FF88);
                guiGraphics.drawCenteredString(this.font, "\u00a77Subject: \u00a7b" + name, crtCenterX, top + 60, 0x55D5FF);
            }
        }
        guiGraphics.fill(left + 122, top + 18, left + 186, top + 96, -15854564);
        guiGraphics.renderOutline(left + 122, top + 18, 64, 78, -14004112);
        this.drawSlotBox(guiGraphics, left + 127, top + 21);
        this.drawSlotBox(guiGraphics, left + 127, top + 39);
        this.drawSlotBox(guiGraphics, left + 127, top + 57);
        this.drawSlotBox(guiGraphics, left + 127, top + 75);
        this.drawSlotBox(guiGraphics, left + 155, top + 30);
        this.drawSlotBox(guiGraphics, left + 155, top + 57);
        if (((PhoenixCapsuleMenu)this.menu).slots.size() >= 11) {
            if (((PhoenixCapsuleMenu)this.menu).getSlot(5).getItem().isEmpty()) {
                this.renderSlotIcon(guiGraphics, left + 128, top + 22, InventoryMenu.EMPTY_ARMOR_SLOT_HELMET);
            }
            if (((PhoenixCapsuleMenu)this.menu).getSlot(6).getItem().isEmpty()) {
                this.renderSlotIcon(guiGraphics, left + 128, top + 40, InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE);
            }
            if (((PhoenixCapsuleMenu)this.menu).getSlot(7).getItem().isEmpty()) {
                this.renderSlotIcon(guiGraphics, left + 128, top + 58, InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS);
            }
            if (((PhoenixCapsuleMenu)this.menu).getSlot(8).getItem().isEmpty()) {
                this.renderSlotIcon(guiGraphics, left + 128, top + 76, InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS);
            }
            if (((PhoenixCapsuleMenu)this.menu).getSlot(10).getItem().isEmpty()) {
                this.renderSlotIcon(guiGraphics, left + 156, top + 58, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
            }
        }
        this.drawSlotBox(guiGraphics, left + 17, top + 101);
        this.drawSlotBox(guiGraphics, left + 43, top + 101);
        this.drawSlotBox(guiGraphics, left + 69, top + 101);
        if (((PhoenixCapsuleMenu)this.menu).slots.size() >= 5) {
            if (((PhoenixCapsuleMenu)this.menu).getSlot(0).getItem().isEmpty() && ((PhoenixCapsuleMenu)this.menu).getSlot(1).getItem().isEmpty()) {
                guiGraphics.drawCenteredString(this.font, isBroken ? "\u00a78G" : "\u00a78M", left + 26, top + 106, 0x446677);
            }
            if (((PhoenixCapsuleMenu)this.menu).getSlot(2).getItem().isEmpty() && ((PhoenixCapsuleMenu)this.menu).getSlot(3).getItem().isEmpty()) {
                guiGraphics.drawCenteredString(this.font, isBroken ? "\u00a78W" : "\u00a78C", left + 52, top + 106, 0x446677);
            }
            if (!isBroken && ((PhoenixCapsuleMenu)this.menu).getSlot(4).getItem().isEmpty()) {
                guiGraphics.drawCenteredString(this.font, "\u00a78D", left + 78, top + 106, 0x446677);
            }
        }
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.drawVanillaSlot(guiGraphics, left + 17 + col * 18, top + 137 + row * 18);
            }
        }
        int col = 0;
        while (col < 9) {
            this.drawVanillaSlot(guiGraphics, left + 17 + col * 18, top + 195);
            ++col;
        }
    }

    private void drawSlotBox(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.fill(x, y, x + 18, y + 18, -15459292);
        guiGraphics.renderOutline(x, y, 18, 18, -13399962);
    }

    private void drawVanillaSlot(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.fill(x, y, x + 18, y + 18, 0x44000000);
        guiGraphics.renderOutline(x, y, 18, 18, 0x55335544);
    }

    private void renderSlotIcon(GuiGraphics guiGraphics, int x, int y, ResourceLocation iconLocation) {
        TextureAtlasSprite sprite = (TextureAtlasSprite)Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(iconLocation);
        guiGraphics.blit(x, y, 0, 16, 16, sprite);
    }

    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, (Component)Component.translatable((String)"gui.ricks_stuff.phoenix_capsule.title"), 10, 6, 65433, false);
        guiGraphics.drawString(this.font, (Component)Component.translatable((String)"gui.ricks_stuff.phoenix_capsule.loadout"), 132, 6, 0x55D5FF, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 9080988, false);
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}

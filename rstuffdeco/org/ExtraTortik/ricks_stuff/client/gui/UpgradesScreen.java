/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.AbstractWidget
 *  net.minecraft.client.gui.components.Renderable
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.component.CustomData
 *  net.minecraft.world.level.ItemLike
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton$Style
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetGuiUtils
 *  org.ExtraTortik.ricks_stuff.client.gui.UpgradesScreen$InvCardEntry
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.ExpansionCardItem
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.UpgradeType
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SEjectUpgradePacket
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SInstallUpgradePacket
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ItemLike;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetButton;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetGuiUtils;
import org.ExtraTortik.ricks_stuff.client.gui.UpgradesScreen;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem;
import org.ExtraTortik.ricks_stuff.item.ExpansionCardItem;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.ExtraTortik.ricks_stuff.item.UpgradeType;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.C2SEjectUpgradePacket;
import org.ExtraTortik.ricks_stuff.network.packet.C2SInstallUpgradePacket;

public class UpgradesScreen
extends Screen {
    private final Screen parent;
    private final CompoundTag gunNbt;
    private final int slotIndex;
    private ItemStack gunStack;
    private final List<AbstractWidget> dynamicWidgets = new ArrayList<AbstractWidget>();
    private final List<InvCardEntry> invCards = new ArrayList<InvCardEntry>();
    private int scrollOffset = 0;
    private static final int ITEMS_PER_PAGE = 3;
    private int lastKnownStateHash = 0;

    public UpgradesScreen(Screen parent, CompoundTag gunNbt, int slotIndex) {
        super((Component)Component.literal((String)"Portal Gun Expansion Matrix"));
        this.parent = parent;
        this.gunNbt = gunNbt != null ? gunNbt : new CompoundTag();
        this.slotIndex = slotIndex;
    }

    public UpgradesScreen(Screen parent) {
        this(parent, new CompoundTag(), -1);
    }

    protected void init() {
        super.init();
        this.updateGunState();
        this.lastKnownStateHash = this.computeStateHash();
        this.refreshContent();
    }

    private int computeStateHash() {
        int hash = 1;
        List<UpgradeType> list = this.getInstalledUpgrades();
        hash = 31 * hash + list.hashCode();
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return hash;
        int i = 0;
        while (i < player.getInventory().getContainerSize()) {
            ItemStack s = player.getInventory().getItem(i);
            if (!s.isEmpty() && s.getItem() instanceof ExpansionCardItem) {
                hash = 31 * hash + i * 37 + s.getItem().hashCode() + s.getCount();
            }
            ++i;
        }
        return hash;
    }

    public void tick() {
        super.tick();
        this.updateGunState();
        int currentHash = this.computeStateHash();
        if (currentHash == this.lastKnownStateHash) return;
        this.lastKnownStateHash = currentHash;
        this.refreshContent();
    }

    private void updateGunState() {
        ItemStack candidate;
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        this.gunStack = null;
        if (this.slotIndex >= 0 && this.slotIndex < player.getInventory().getContainerSize() && UpgradesScreen.isGun(candidate = player.getInventory().getItem(this.slotIndex))) {
            this.gunStack = candidate;
        }
        if (this.gunStack == null) {
            if (UpgradesScreen.isGun(player.getMainHandItem())) {
                this.gunStack = player.getMainHandItem();
            } else if (UpgradesScreen.isGun(player.getOffhandItem())) {
                this.gunStack = player.getOffhandItem();
            } else {
                for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
                    ItemStack candidate2 = player.getInventory().getItem(i);
                    if (!UpgradesScreen.isGun(candidate2)) continue;
                    this.gunStack = candidate2;
                    break;
                }
            }
        }
        if (this.gunStack == null) return;
        CompoundTag tag = ((CustomData)this.gunStack.getOrDefault(DataComponents.CUSTOM_DATA, (Object)CustomData.EMPTY)).copyTag();
        if (tag.contains("Upgrades")) {
            this.gunNbt.put("Upgrades", tag.get("Upgrades").copy());
        } else {
            this.gunNbt.remove("Upgrades");
        }
    }

    private List<UpgradeType> getInstalledUpgrades() {
        if (this.gunStack == null) return PortalGunItem.getUpgrades((CompoundTag)this.gunNbt);
        if (this.gunStack.isEmpty()) return PortalGunItem.getUpgrades((CompoundTag)this.gunNbt);
        return PortalGunItem.getUpgrades((ItemStack)this.gunStack);
    }

    private void loadInventoryCards() {
        this.invCards.clear();
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            int size = player.getInventory().getContainerSize();
            for (int i = 0; i < size; ++i) {
                ExpansionCardItem cardItem;
                UpgradeType type;
                Item item;
                ItemStack stack = player.getInventory().getItem(i);
                if (stack.isEmpty() || !((item = stack.getItem()) instanceof ExpansionCardItem) || (type = (cardItem = (ExpansionCardItem)item).getUpgradeType()) == null) continue;
                this.invCards.add(new InvCardEntry(i, stack, cardItem, type));
            }
        }
        if (this.scrollOffset + 3 <= this.invCards.size()) return;
        this.scrollOffset = Math.max(0, this.invCards.size() - 3);
    }

    private <T extends AbstractWidget> T addDynamicWidget(T widget) {
        this.dynamicWidgets.add(widget);
        return (T)((AbstractWidget)this.addRenderableWidget((GuiEventListener)widget));
    }

    public void refreshContent() {
        int index;
        this.setFocused(null);
        for (AbstractWidget w : this.dynamicWidgets) {
            this.removeWidget((GuiEventListener)w);
        }
        this.dynamicWidgets.clear();
        this.updateGunState();
        this.loadInventoryCards();
        this.lastKnownStateHash = this.computeStateHash();
        int panelWidth = 290;
        int panelHeight = 235;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;
        List<UpgradeType> installed = this.getInstalledUpgrades();
        int maxSlots = PortalGunItem.getMaxUpgrades();
        boolean compact = maxSlots > 2;
        int slotHeight = compact ? 22 : 38;
        int slotGap = compact ? 2 : 3;
        int startY = top + 28;
        for (int i = 0; i < maxSlots; ++i) {
            int slotIdx = i;
            if (slotIdx >= installed.size()) continue;
            int slotY = startY + slotIdx * (slotHeight + slotGap);
            int btnY = compact ? slotY + 2 : slotY + 10;
            int btnH = compact ? 18 : 18;
            this.addDynamicWidget(GadgetButton.gadget((Component)Component.literal((String)"Eject"), b -> {
                this.playClickSound();
                LocalPlayer player = Minecraft.getInstance().player;
                List<UpgradeType> currentUpgrades = this.getInstalledUpgrades();
                if (slotIdx < currentUpgrades.size()) {
                    UpgradeType removed = currentUpgrades.get(slotIdx);
                    if (this.gunStack != null && !this.gunStack.isEmpty()) {
                        PortalGunItem.ejectUpgrade((ItemStack)this.gunStack, (int)slotIdx, null);
                        CompoundTag tag = ((CustomData)this.gunStack.getOrDefault(DataComponents.CUSTOM_DATA, (Object)CustomData.EMPTY)).copyTag();
                        if (tag.contains("Upgrades")) {
                            this.gunNbt.put("Upgrades", (Tag)tag.getList("Upgrades", 10).copy());
                        }
                    } else {
                        PortalGunItem.ejectUpgrade((CompoundTag)this.gunNbt, (int)slotIdx);
                    }
                    if (player != null) {
                        ItemStack card = new ItemStack((ItemLike)removed.getItem());
                        if (!player.getInventory().add(card)) {
                            player.drop(card, false);
                        }
                    }
                    ModMessages.sendToServer((CustomPacketPayload)new C2SEjectUpgradePacket(slotIdx));
                }
                this.refreshContent();
            }).bounds(left + 224, btnY, 52, btnH).style(GadgetButton.Style.DANGER).build());
        }
        int invSectionY = startY + maxSlots * (slotHeight + slotGap) + (compact ? 2 : 4);
        int invBoxY = invSectionY + 11;
        int count = Math.min(3, this.invCards.size() - this.scrollOffset);
        for (int i = 0; i < count && (index = this.scrollOffset + i) < this.invCards.size(); ++i) {
            GadgetButton btn;
            boolean isFull;
            InvCardEntry entry = this.invCards.get(index);
            int rowY = invBoxY + 3 + i * 24;
            boolean alreadyInstalled = installed.contains(entry.type());
            boolean bl = isFull = installed.size() >= PortalGunItem.getMaxUpgrades();
            if (alreadyInstalled) {
                btn = GadgetButton.gadget((Component)Component.literal((String)"[Installed]"), b -> {}).bounds(left + 206, rowY + 2, 70, 18).build();
                btn.active = false;
                this.addDynamicWidget(btn);
                continue;
            }
            if (isFull) {
                btn = GadgetButton.gadget((Component)Component.literal((String)"[Full]"), b -> {}).bounds(left + 206, rowY + 2, 70, 18).build();
                btn.active = false;
                this.addDynamicWidget(btn);
                continue;
            }
            this.addDynamicWidget(GadgetButton.gadget((Component)Component.literal((String)"Install"), b -> {
                Item patt0$temp;
                ItemStack cardStack;
                int invSlot;
                this.playClickSound();
                LocalPlayer player = Minecraft.getInstance().player;
                if (player != null && (invSlot = entry.invSlot()) >= 0 && invSlot < player.getInventory().getContainerSize() && !(cardStack = player.getInventory().getItem(invSlot)).isEmpty() && (patt0$temp = cardStack.getItem()) instanceof ExpansionCardItem) {
                    ExpansionCardItem cardItem = (ExpansionCardItem)patt0$temp;
                    UpgradeType toInstall = cardItem.getUpgradeType();
                    if (this.gunStack != null && !this.gunStack.isEmpty()) {
                        PortalGunItem.installUpgrade((ItemStack)this.gunStack, (UpgradeType)toInstall);
                        CompoundTag tag = ((CustomData)this.gunStack.getOrDefault(DataComponents.CUSTOM_DATA, (Object)CustomData.EMPTY)).copyTag();
                        if (tag.contains("Upgrades")) {
                            this.gunNbt.put("Upgrades", (Tag)tag.getList("Upgrades", 10).copy());
                        }
                    } else {
                        PortalGunItem.installUpgrade((CompoundTag)this.gunNbt, (UpgradeType)toInstall);
                    }
                    if (!player.getAbilities().instabuild) {
                        cardStack.shrink(1);
                    }
                    ModMessages.sendToServer((CustomPacketPayload)new C2SInstallUpgradePacket(invSlot));
                }
                this.refreshContent();
            }).bounds(left + 206, rowY + 2, 70, 18).style(GadgetButton.Style.PRIMARY).build());
        }
        if (this.scrollOffset > 0) {
            this.addDynamicWidget(GadgetButton.gadget((Component)Component.literal((String)"\u25b2"), b -> {
                this.scrollOffset = Math.max(0, this.scrollOffset - 1);
                this.refreshContent();
            }).bounds(left + panelWidth - 13, invBoxY + 2, 10, 14).build());
        }
        if (this.scrollOffset + 3 < this.invCards.size()) {
            this.addDynamicWidget(GadgetButton.gadget((Component)Component.literal((String)"\u25bc"), b -> {
                ++this.scrollOffset;
                this.refreshContent();
            }).bounds(left + panelWidth - 13, invBoxY + 76 - 16, 10, 14).build());
        }
        this.addDynamicWidget(GadgetButton.gadget((Component)Component.literal((String)"Back"), b -> {
            this.playClickSound();
            Minecraft.getInstance().setScreen(this.parent);
        }).bounds(left + (panelWidth - 100) / 2, top + 206, 100, 20).build());
    }

    private void playClickSound() {
        if (Minecraft.getInstance().player == null) return;
        Minecraft.getInstance().player.playSound((SoundEvent)ModSounds.UI_PRESS.get(), 1.0f, 1.0f);
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (scrollY > 0.0 && this.scrollOffset > 0) {
            --this.scrollOffset;
            this.refreshContent();
            return true;
        }
        if (!(scrollY < 0.0)) return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        if (this.scrollOffset + 3 >= this.invCards.size()) return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        ++this.scrollOffset;
        this.refreshContent();
        return true;
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        int panelWidth = 290;
        int panelHeight = 235;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;
        GadgetGuiUtils.renderChassis((GuiGraphics)guiGraphics, (int)left, (int)top, (int)panelWidth, (int)panelHeight);
        GadgetGuiUtils.renderHeader((GuiGraphics)guiGraphics, (int)(left + 4), (int)(top + 4), (int)(panelWidth - 8), (int)22);
        guiGraphics.drawString(this.font, "PORTAL GUN EXPANSION MATRIX", left + 12, top + 7, -12720539, false);
        List<UpgradeType> installed = this.getInstalledUpgrades();
        int maxSlots = PortalGunItem.getMaxUpgrades();
        boolean compact = maxSlots > 2;
        int slotHeight = compact ? 22 : 38;
        int slotGap = compact ? 2 : 3;
        int startY = top + 28;
        String slotStatusText = "SLOTS: " + installed.size() + "/" + maxSlots;
        int statusW = this.font.width(slotStatusText);
        guiGraphics.drawString(this.font, slotStatusText, left + panelWidth - statusW - 12, top + 7, -13183000, false);
        for (int i = 0; i < maxSlots; ++i) {
            this.renderSlot(guiGraphics, left + 12, startY + i * (slotHeight + slotGap), i, installed, compact);
        }
        int invSectionY = startY + maxSlots * (slotHeight + slotGap) + (compact ? 2 : 4);
        guiGraphics.drawString(this.font, "INVENTORY EXPANSION MODULES", left + 12, invSectionY, -7696228, false);
        int invBoxY = invSectionY + 11;
        int invBoxH = Math.max(30, top + panelHeight - 10 - invBoxY);
        GadgetGuiUtils.renderTerminalScreen((GuiGraphics)guiGraphics, (int)(left + 12), (int)invBoxY, (int)(panelWidth - 24), (int)invBoxH);
        if (this.invCards.isEmpty()) {
            guiGraphics.drawCenteredString(this.font, "No expansion cards found in inventory.", left + panelWidth / 2, invBoxY + 24, -7696228);
            guiGraphics.drawCenteredString(this.font, "Craft them to upgrade gun.", left + panelWidth / 2, invBoxY + 40, -10061944);
        } else {
            int index;
            int count = Math.min(3, this.invCards.size() - this.scrollOffset);
            for (int i = 0; i < count && (index = this.scrollOffset + i) < this.invCards.size(); ++i) {
                InvCardEntry entry = this.invCards.get(index);
                int rowY = invBoxY + 3 + i * 24;
                int bg = i % 2 == 0 ? 571155474 : 285942802;
                guiGraphics.fill(left + 14, rowY, left + panelWidth - 14, rowY + 23, bg);
                guiGraphics.renderItem(entry.stack(), left + 16, rowY + 3);
                MutableComponent nameComp = Component.translatable((String)entry.type().getTranslationKey());
                Object nameStr = nameComp.getString();
                if (this.font.width((String)nameStr) > 140) {
                    nameStr = this.font.plainSubstrByWidth((String)nameStr, 135) + "\u2026";
                }
                int nameColor = entry.type().getColor() | 0xFF000000;
                guiGraphics.drawString(this.font, (String)nameStr, left + 36, rowY + 7, nameColor, false);
            }
        }
        Iterator iterator = this.renderables.iterator();
        while (iterator.hasNext()) {
            Renderable renderable = (Renderable)iterator.next();
            renderable.render(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    private void renderSlot(GuiGraphics guiGraphics, int x, int y, int slotIdx, List<UpgradeType> installed, boolean compact) {
        int width = 266;
        int height = compact ? 22 : 38;
        GadgetGuiUtils.renderTerminalScreen((GuiGraphics)guiGraphics, (int)x, (int)y, (int)width, (int)height);
        if (slotIdx < installed.size()) {
            UpgradeType type = installed.get(slotIdx);
            ItemStack cardStack = new ItemStack((ItemLike)type.getItem());
            int iconY = compact ? y + 3 : y + 11;
            guiGraphics.renderItem(cardStack, x + 4, iconY);
            MutableComponent nameComp = Component.translatable((String)type.getTranslationKey());
            Object title = nameComp.getString();
            int maxTitleWidth = 195;
            if (this.font.width((String)title) > maxTitleWidth) {
                title = this.font.plainSubstrByWidth((String)title, maxTitleWidth - 6) + "\u2026";
            }
            int titleColor = type.getColor() | 0xFF000000;
            guiGraphics.drawString(this.font, (String)title, x + 24, y + 7, titleColor, false);
            if (compact) return;
            MutableComponent descComp = Component.translatable((String)type.getDescriptionKey());
            Object desc = descComp.getString();
            int maxDescWidth = 195;
            if (this.font.width((String)desc) > maxDescWidth) {
                desc = this.font.plainSubstrByWidth((String)desc, maxDescWidth - 6) + "\u2026";
            }
            guiGraphics.drawString(this.font, (String)desc, x + 24, y + 21, -7696228, false);
        } else {
            int iconY = compact ? y + 3 : y + 11;
            guiGraphics.fill(x + 4, iconY, x + 20, iconY + 16, 0x33000000);
            guiGraphics.renderOutline(x + 4, iconY, 16, 16, 0x44555555);
            String emptyTitle = "\u00a78[ EMPTY EXPANSION SLOT " + (slotIdx + 1) + " ]\u00a7r";
            guiGraphics.drawString(this.font, emptyTitle, x + 24, y + 7, -7829368, false);
            if (compact) return;
            String emptyDesc = "\u00a77Ready for module installation\u00a7r";
            guiGraphics.drawString(this.font, emptyDesc, x + 24, y + 21, -7696228, false);
        }
    }

    private static boolean isGun(ItemStack s) {
        return s != null && !s.isEmpty() && (s.getItem() instanceof PortalGunItem || s.getItem() instanceof DischargedPortalGunItem);
    }

    public boolean isPauseScreen() {
        return false;
    }
}

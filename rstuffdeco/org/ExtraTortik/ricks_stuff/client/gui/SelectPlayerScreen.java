/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.AbstractWidget
 *  net.minecraft.client.gui.components.EditBox
 *  net.minecraft.client.gui.components.Renderable
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.multiplayer.PlayerInfo
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton$Style
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetGuiUtils
 *  org.ExtraTortik.ricks_stuff.client.gui.SelectPlayerScreen$PlayerEntry
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.UpgradeType
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.C2STargetPlayerPacket
 *  org.ExtraTortik.ricks_stuff.portal.AntiPortalFieldManager
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetButton;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetGuiUtils;
import org.ExtraTortik.ricks_stuff.client.gui.SelectPlayerScreen;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.ExtraTortik.ricks_stuff.item.UpgradeType;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.C2STargetPlayerPacket;
import org.ExtraTortik.ricks_stuff.portal.AntiPortalFieldManager;

public class SelectPlayerScreen
extends Screen {
    private final Screen parent;
    private final CompoundTag gunNbt;
    private final int slotIndex;
    private ItemStack gunStack;
    private EditBox searchBox;
    private final List<AbstractWidget> dynamicWidgets = new ArrayList<AbstractWidget>();
    private final List<PlayerEntry> allPlayers = new ArrayList<PlayerEntry>();
    private final List<PlayerEntry> filteredPlayers = new ArrayList<PlayerEntry>();
    private int scrollOffset = 0;
    private static final int ITEMS_PER_PAGE = 4;

    public SelectPlayerScreen(Screen parent, CompoundTag gunNbt, int slotIndex) {
        super((Component)Component.literal((String)"Target Player Locator"));
        this.parent = parent;
        this.gunNbt = gunNbt != null ? gunNbt : new CompoundTag();
        this.slotIndex = slotIndex;
    }

    private boolean hasPlayerLocUpgrade() {
        return this.gunStack != null && PortalGunItem.hasUpgrade((ItemStack)this.gunStack, (UpgradeType)UpgradeType.PLAYER_LOCATOR) || PortalGunItem.hasUpgrade((CompoundTag)this.gunNbt, (UpgradeType)UpgradeType.PLAYER_LOCATOR);
    }

    private void loadOnlinePlayers() {
        this.allPlayers.clear();
        Minecraft mc = Minecraft.getInstance();
        if (mc.getConnection() != null) {
            Collection players = mc.getConnection().getOnlinePlayers();
            UUID selfUuid = mc.player != null ? mc.player.getUUID() : null;
            for (PlayerInfo p : players) {
                Player playerEntity;
                UUID uuid = p.getProfile().getId();
                if (mc.level != null && (playerEntity = mc.level.getPlayerByUUID(uuid)) != null && AntiPortalFieldManager.isPlayerHidden((Player)playerEntity)) continue;
                String name = p.getProfile().getName();
                int latency = p.getLatency();
                boolean isSelf = selfUuid != null && selfUuid.equals(uuid);
                this.allPlayers.add(new PlayerEntry(uuid, name, latency, isSelf));
            }
        }
        this.allPlayers.sort((a, b) -> {
            if (a.isSelf) {
                return -1;
            }
            if (!b.isSelf) return a.name.compareToIgnoreCase(b.name);
            return 1;
        });
        this.filterPlayers();
    }

    private void filterPlayers() {
        this.filteredPlayers.clear();
        String query = this.searchBox != null ? this.searchBox.getValue().trim().toLowerCase() : "";
        Iterator<PlayerEntry> iterator = this.allPlayers.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                this.scrollOffset = 0;
                return;
            }
            PlayerEntry p = iterator.next();
            if (!query.isEmpty() && !p.name.toLowerCase().contains(query)) continue;
            this.filteredPlayers.add(p);
        }
    }

    protected void init() {
        super.init();
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (this.slotIndex >= 0 && this.slotIndex < player.getInventory().getContainerSize()) {
                this.gunStack = player.getInventory().getItem(this.slotIndex);
            }
            if (this.gunStack == null || !SelectPlayerScreen.isGun(this.gunStack)) {
                if (SelectPlayerScreen.isGun(player.getMainHandItem())) {
                    this.gunStack = player.getMainHandItem();
                } else if (SelectPlayerScreen.isGun(player.getOffhandItem())) {
                    this.gunStack = player.getOffhandItem();
                }
            }
        }
        this.loadOnlinePlayers();
        int panelWidth = 300;
        int panelHeight = 220;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;
        String prevSearch = this.searchBox != null ? this.searchBox.getValue() : "";
        boolean prevSearchFocus = this.searchBox != null && this.searchBox.isFocused();
        this.searchBox = new EditBox(this.font, left + 12, top + 34, panelWidth - 24, 16, (Component)Component.literal((String)"Search"));
        this.searchBox.setHint((Component)Component.literal((String)"Search player by name...").withStyle(ChatFormatting.DARK_GRAY));
        this.searchBox.setValue(prevSearch);
        this.searchBox.setResponder(s -> {
            this.filterPlayers();
            this.refreshRows();
        });
        this.addRenderableWidget((GuiEventListener)this.searchBox);
        if (prevSearchFocus) {
            this.searchBox.setFocused(true);
        }
        this.addRenderableWidget((GuiEventListener)GadgetButton.gadget((Component)Component.literal((String)"Back"), b -> {
            this.playClickSound();
            Minecraft.getInstance().setScreen(this.parent);
        }).bounds(left + (panelWidth - 100) / 2, top + 185, 100, 20).build());
        this.refreshRows();
    }

    private <T extends AbstractWidget> T addDynamicWidget(T widget) {
        this.dynamicWidgets.add(widget);
        return (T)((AbstractWidget)this.addRenderableWidget((GuiEventListener)widget));
    }

    private void refreshRows() {
        int index;
        for (AbstractWidget w : this.dynamicWidgets) {
            this.removeWidget((GuiEventListener)w);
        }
        this.dynamicWidgets.clear();
        int panelWidth = 300;
        int panelHeight = 220;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;
        int listTop = top + 54;
        int rowHeight = 26;
        boolean hasUpgrade = this.hasPlayerLocUpgrade();
        if (!hasUpgrade) return;
        int count = Math.min(4, this.filteredPlayers.size() - this.scrollOffset);
        for (int i = 0; i < count && (index = this.scrollOffset + i) < this.filteredPlayers.size(); ++i) {
            PlayerEntry entry = this.filteredPlayers.get(index);
            int rowY = listTop + i * rowHeight;
            this.addDynamicWidget(GadgetButton.gadget((Component)Component.literal((String)"Select Target"), b -> {
                if (Config.allowPlayerTargeting) {
                    this.playClickSound();
                    ModMessages.sendToServer((CustomPacketPayload)new C2STargetPlayerPacket(entry.uuid, entry.name, true));
                    Minecraft.getInstance().setScreen(this.parent);
                    return;
                }
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().player.playSound((SoundEvent)ModSounds.GUN_MALFUNCTION.get(), 1.0f, 1.0f);
                }
                Minecraft.getInstance().setScreen(this.parent);
            }).bounds(left + panelWidth - 110, rowY + 2, 95, 18).style(GadgetButton.Style.PRIMARY).build());
        }
        if (this.scrollOffset > 0) {
            this.addDynamicWidget(GadgetButton.gadget((Component)Component.literal((String)"\u25b2"), b -> {
                this.scrollOffset = Math.max(0, this.scrollOffset - 1);
                this.refreshRows();
            }).bounds(left + panelWidth - 16, top + 34, 14, 16).build());
        }
        if (this.scrollOffset + 4 >= this.filteredPlayers.size()) return;
        this.addDynamicWidget(GadgetButton.gadget((Component)Component.literal((String)"\u25bc"), b -> {
            ++this.scrollOffset;
            this.refreshRows();
        }).bounds(left + panelWidth - 16, listTop + 4 * rowHeight - 14, 14, 16).build());
    }

    private void playClickSound() {
        if (Minecraft.getInstance().player == null) return;
        Minecraft.getInstance().player.playSound((SoundEvent)ModSounds.UI_PRESS.get(), 1.0f, 1.0f);
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (scrollY > 0.0 && this.scrollOffset > 0) {
            --this.scrollOffset;
            this.refreshRows();
            return true;
        }
        if (!(scrollY < 0.0)) return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        if (this.scrollOffset + 4 >= this.filteredPlayers.size()) return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        ++this.scrollOffset;
        this.refreshRows();
        return true;
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        int panelWidth = 300;
        int panelHeight = 220;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;
        GadgetGuiUtils.renderChassis((GuiGraphics)guiGraphics, (int)left, (int)top, (int)panelWidth, (int)panelHeight);
        GadgetGuiUtils.renderHeader((GuiGraphics)guiGraphics, (int)(left + 4), (int)(top + 4), (int)(panelWidth - 8), (int)24);
        guiGraphics.drawString(this.font, "PLAYER TARGET LOCATOR", left + 12, top + 8, -12720539, false);
        String onlineText = "Online: " + this.allPlayers.size();
        int onlineW = this.font.width(onlineText);
        guiGraphics.drawString(this.font, onlineText, left + panelWidth - onlineW - 12, top + 8, -13183000, false);
        int listTop = top + 54;
        int rowHeight = 26;
        GadgetGuiUtils.renderTerminalScreen((GuiGraphics)guiGraphics, (int)(left + 10), (int)listTop, (int)(panelWidth - 20), (int)(4 * rowHeight));
        boolean hasUpgrade = this.hasPlayerLocUpgrade();
        if (!hasUpgrade) {
            guiGraphics.drawCenteredString(this.font, "\u00a7c[!] PLAYER LOCATOR CARD REQUIRED", left + panelWidth / 2, listTop + 32, -43691);
            guiGraphics.drawCenteredString(this.font, "Install Player Locator Card into the gun", left + panelWidth / 2, listTop + 50, -7696228);
            guiGraphics.drawCenteredString(this.font, "to track players across the multiverse.", left + panelWidth / 2, listTop + 63, -7696228);
        } else if (this.filteredPlayers.isEmpty()) {
            guiGraphics.drawCenteredString(this.font, "No online players found.", left + panelWidth / 2, listTop + 45, -7696228);
        } else {
            int index;
            int count = Math.min(4, this.filteredPlayers.size() - this.scrollOffset);
            for (int i = 0; i < count && (index = this.scrollOffset + i) < this.filteredPlayers.size(); ++i) {
                PlayerEntry entry = this.filteredPlayers.get(index);
                int rowY = listTop + i * rowHeight;
                int bg = i % 2 == 0 ? 571155474 : 285942802;
                guiGraphics.fill(left + 12, rowY + 1, left + panelWidth - 12, rowY + rowHeight - 1, bg);
                String nameText = entry.name + (entry.isSelf ? " (You)" : "");
                int nameColor = entry.isSelf ? -12720539 : -1;
                guiGraphics.drawString(this.font, nameText, left + 18, rowY + 5, nameColor, false);
                guiGraphics.drawString(this.font, entry.latency + "ms", left + 18, rowY + 15, -7696228, false);
            }
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

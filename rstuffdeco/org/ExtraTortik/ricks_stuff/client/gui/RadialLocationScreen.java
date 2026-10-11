/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Renderable
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.component.CustomData
 *  net.minecraft.world.level.ItemLike
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetGuiUtils
 *  org.ExtraTortik.ricks_stuff.client.gui.RadialLocationScreen$LocationEntry
 *  org.ExtraTortik.ricks_stuff.client.gui.RadialRenderUtils
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SSelectSavedLocationPacket
 *  org.ExtraTortik.ricks_stuff.portal.PortalPlacementHelper
 *  org.joml.Matrix4f
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ItemLike;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetGuiUtils;
import org.ExtraTortik.ricks_stuff.client.gui.RadialLocationScreen;
import org.ExtraTortik.ricks_stuff.client.gui.RadialRenderUtils;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.C2SSelectSavedLocationPacket;
import org.ExtraTortik.ricks_stuff.portal.PortalPlacementHelper;
import org.joml.Matrix4f;

public class RadialLocationScreen
extends Screen {
    private final Screen parent;
    private final CompoundTag gunNbt;
    private final int slotIndex;
    private final int triggerKey;
    private ItemStack gunStack;
    public static final int LOCATIONS_PER_PAGE = 10;
    private final List<LocationEntry> allLocations = new ArrayList<LocationEntry>();
    private int currentPage = 0;
    private int totalPages = 1;
    private float[] hoverProgress = new float[0];
    private long lastTimeNanos = 0L;
    private int hoveredIndex = -1;
    private int activeIndex = -1;
    private int lastSoundHoveredIndex = -1;
    private static final float R_INNER = 44.0f;
    private static final float R_OUTER = 112.0f;
    private static final float R_DEADZONE = 38.0f;
    private static final float R_MAX = 150.0f;
    private static final float MAX_HOVER_OFFSET = 14.0f;
    private static final float ANIMATION_SPEED = 16.0f;

    public RadialLocationScreen(Screen parent, CompoundTag gunNbt, int slotIndex, int triggerKey) {
        super((Component)Component.literal((String)"Portal Gun Radial Menu"));
        this.parent = parent;
        this.gunNbt = gunNbt != null ? gunNbt : new CompoundTag();
        this.slotIndex = slotIndex;
        this.triggerKey = triggerKey;
    }

    public RadialLocationScreen(CompoundTag gunNbt, int slotIndex, int triggerKey) {
        this(null, gunNbt, slotIndex, triggerKey);
    }

    protected void init() {
        super.init();
        this.loadGunStackAndLocations();
        this.lastTimeNanos = System.nanoTime();
    }

    private void loadGunStackAndLocations() {
        CompoundTag nbt;
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (this.slotIndex >= 0 && this.slotIndex < player.getInventory().getContainerSize()) {
                this.gunStack = player.getInventory().getItem(this.slotIndex);
            }
            if (this.gunStack == null || !(this.gunStack.getItem() instanceof PortalGunItem) && !(this.gunStack.getItem() instanceof DischargedPortalGunItem)) {
                if (player.getMainHandItem().getItem() instanceof PortalGunItem || player.getMainHandItem().getItem() instanceof DischargedPortalGunItem) {
                    this.gunStack = player.getMainHandItem();
                } else if (player.getOffhandItem().getItem() instanceof PortalGunItem || player.getOffhandItem().getItem() instanceof DischargedPortalGunItem) {
                    this.gunStack = player.getOffhandItem();
                }
            }
        }
        CompoundTag compoundTag = nbt = this.gunStack != null ? ((CustomData)this.gunStack.getOrDefault(DataComponents.CUSTOM_DATA, (Object)CustomData.EMPTY)).copyTag() : this.gunNbt;
        if (nbt.isEmpty() && this.gunNbt != null) {
            nbt = this.gunNbt;
        }
        this.allLocations.clear();
        if (nbt != null) {
            this.activeIndex = PortalGunItem.getCustomLocationIndex((ItemStack)(this.gunStack != null ? this.gunStack : new ItemStack((ItemLike)Items.AIR)));
            if (nbt.contains("CustomLocationIndex")) {
                this.activeIndex = nbt.getInt("CustomLocationIndex");
            }
            if (nbt.contains("SavedLocations", 9)) {
                ListTag list = nbt.getList("SavedLocations", 10);
                for (int i = 0; i < list.size(); ++i) {
                    boolean isActive;
                    CompoundTag loc = list.getCompound(i);
                    Object name = loc.getString("Name");
                    double x = loc.getDouble("X");
                    double y = loc.getDouble("Y");
                    double z = loc.getDouble("Z");
                    String dim = loc.getString("Dimension");
                    boolean bl = isActive = i == this.activeIndex;
                    if (((String)name).isEmpty()) {
                        name = "Loc #" + (i + 1);
                    }
                    this.allLocations.add(new LocationEntry(i, (String)name, x, y, z, dim, isActive));
                }
            }
        }
        this.totalPages = Math.max(1, (this.allLocations.size() + 10 - 1) / 10);
        if (this.activeIndex >= 0 && this.activeIndex < this.allLocations.size()) {
            this.currentPage = this.activeIndex / 10;
        } else if (this.currentPage >= this.totalPages) {
            this.currentPage = Math.max(0, this.totalPages - 1);
        }
        this.syncPageHoverArray();
    }

    private void syncPageHoverArray() {
        int count = this.getCurrentPageLocations().size();
        if (this.hoverProgress.length != count) {
            this.hoverProgress = new float[count];
        } else {
            for (int i = 0; i < count; ++i) {
                this.hoverProgress[i] = 0.0f;
            }
        }
        this.hoveredIndex = -1;
        this.lastSoundHoveredIndex = -1;
    }

    private List<LocationEntry> getCurrentPageLocations() {
        if (this.allLocations.isEmpty()) {
            return List.of();
        }
        int from = this.currentPage * 10;
        int to = Math.min(from + 10, this.allLocations.size());
        if (from < this.allLocations.size()) return this.allLocations.subList(from, to);
        return List.of();
    }

    public void nextPage() {
        if (this.totalPages <= 1) return;
        this.currentPage = (this.currentPage + 1) % this.totalPages;
        this.playDialSound();
        this.syncPageHoverArray();
    }

    public void prevPage() {
        if (this.totalPages <= 1) return;
        this.currentPage = (this.currentPage - 1 + this.totalPages) % this.totalPages;
        this.playDialSound();
        this.syncPageHoverArray();
    }

    private void playDialSound() {
        if (Minecraft.getInstance().player == null) return;
        Minecraft.getInstance().player.playSound((SoundEvent)ModSounds.UI_DIAL.get(), 0.7f, 1.2f);
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        long now = System.nanoTime();
        float dt = this.lastTimeNanos > 0L ? (float)((double)(now - this.lastTimeNanos) / 1.0E9) : 0.016f;
        this.lastTimeNanos = now;
        dt = Math.min(0.1f, Math.max(0.001f, dt));
        float cx = (float)this.width / 2.0f;
        float cy = (float)this.height / 2.0f;
        List<LocationEntry> pageLocs = this.getCurrentPageLocations();
        this.updateHoveredSector(cx, cy, mouseX, mouseY, pageLocs);
        if (this.hoveredIndex != this.lastSoundHoveredIndex) {
            if (this.hoveredIndex >= 0 && Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.playSound((SoundEvent)ModSounds.UI_DIAL.get(), 0.6f, 1.1f);
            }
            this.lastSoundHoveredIndex = this.hoveredIndex;
        }
        for (int i = 0; i < pageLocs.size(); ++i) {
            float target = i == this.hoveredIndex ? 1.0f : 0.0f;
            int n = i;
            this.hoverProgress[n] = this.hoverProgress[n] + (target - this.hoverProgress[i]) * Math.min(1.0f, dt * 16.0f);
            if (!(Math.abs(target - this.hoverProgress[i]) < 0.002f)) continue;
            this.hoverProgress[i] = target;
        }
        if (this.allLocations.isEmpty()) {
            this.renderEmptyState(guiGraphics, cx, cy);
        } else {
            this.renderRadialSectors(guiGraphics, cx, cy, pageLocs);
            this.renderCenterHub(guiGraphics, cx, cy, pageLocs);
            if (this.totalPages > 1) {
                this.renderPaginationControls(guiGraphics, cx, cy, mouseX, mouseY);
            }
        }
        Iterator iterator = this.renderables.iterator();
        while (iterator.hasNext()) {
            Renderable renderable = (Renderable)iterator.next();
            renderable.render(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    }

    protected void renderBlurredBackground(float partialTick) {
    }

    private void updateHoveredSector(float cx, float cy, int mouseX, int mouseY, List<LocationEntry> pageLocs) {
        float norm;
        if (pageLocs.isEmpty()) {
            this.hoveredIndex = -1;
            return;
        }
        float dx = (float)mouseX - cx;
        float dy = (float)mouseY - cy;
        float dist = (float)Math.sqrt(dx * dx + dy * dy);
        if (dist < 38.0f || dist > 150.0f) {
            this.hoveredIndex = -1;
            return;
        }
        int count = pageLocs.size();
        if (count == 1) {
            this.hoveredIndex = 0;
            return;
        }
        float angle = (float)Math.atan2(dy, dx);
        float step = (float)(Math.PI * 2 / (double)count);
        for (norm = angle + 1.5707964f + step / 2.0f; norm < 0.0f; norm += (float)Math.PI * 2) {
        }
        while (norm >= (float)Math.PI * 2) {
            norm -= (float)Math.PI * 2;
        }
        int index = (int)(norm / step);
        this.hoveredIndex = index >= 0 && index < count ? index : -1;
    }

    private void renderRadialSectors(GuiGraphics guiGraphics, float cx, float cy, List<LocationEntry> pageLocs) {
        Matrix4f matrix = guiGraphics.pose().last().pose();
        int count = pageLocs.size();
        if (count == 1) {
            LocationEntry loc = pageLocs.get(0);
            float progress = this.hoverProgress[0];
            int baseFill = -669245402;
            int hoverFill = -200397545;
            int activeFill = -668064234;
            int fillColor = loc.isActive() ? RadialLocationScreen.lerpColor(activeFill, hoverFill, progress) : RadialLocationScreen.lerpColor(baseFill, hoverFill, progress);
            int baseOutline = loc.isActive() ? -21988 : -13091512;
            int hoverOutline = -12720539;
            int outlineColor = RadialLocationScreen.lerpColor(baseOutline, hoverOutline, progress);
            float strokeWidth = 1.6f + progress * 1.0f;
            RadialRenderUtils.drawRing((Matrix4f)matrix, (float)cx, (float)cy, (float)44.0f, (float)112.0f, (int)fillColor, (int)64);
            RadialRenderUtils.drawRingOutline((Matrix4f)matrix, (float)cx, (float)cy, (float)44.0f, (float)112.0f, (int)outlineColor, (float)strokeWidth, (int)64);
            this.renderSectorContent(guiGraphics, cx, cy, -1.5707964f, (float)Math.PI * 2, loc, progress);
            return;
        }
        float step = (float)(Math.PI * 2 / (double)count);
        float angularGap = Math.min(0.06f, 0.25f / (float)count);
        int i = 0;
        while (i < count) {
            LocationEntry loc = pageLocs.get(i);
            float progress = this.hoverProgress[i];
            float midAngle = (float)((double)((float)i * step) - 1.5707963267948966);
            float startAngle = midAngle - step / 2.0f + angularGap / 2.0f;
            float endAngle = midAngle + step / 2.0f - angularGap / 2.0f;
            float radialOffset = progress * 14.0f;
            float ox = cx + (float)Math.cos(midAngle) * radialOffset;
            float oy = cy + (float)Math.sin(midAngle) * radialOffset;
            int baseFill = -669245402;
            int hoverFill = -200397545;
            int activeFill = -668064234;
            int fillColor = loc.isActive() ? RadialLocationScreen.lerpColor(activeFill, hoverFill, progress) : RadialLocationScreen.lerpColor(baseFill, hoverFill, progress);
            int baseOutline = loc.isActive() ? -21988 : -13091512;
            int hoverOutline = -12720539;
            int outlineColor = RadialLocationScreen.lerpColor(baseOutline, hoverOutline, progress);
            float strokeWidth = 1.6f + progress * 1.0f;
            int steps = Math.max(8, 64 / count);
            RadialRenderUtils.drawSector((Matrix4f)matrix, (float)ox, (float)oy, (float)44.0f, (float)112.0f, (float)startAngle, (float)endAngle, (int)fillColor, (int)steps);
            RadialRenderUtils.drawSectorOutline((Matrix4f)matrix, (float)ox, (float)oy, (float)44.0f, (float)112.0f, (float)startAngle, (float)endAngle, (int)outlineColor, (float)strokeWidth, (int)steps);
            this.renderSectorContent(guiGraphics, ox, oy, midAngle, step, loc, progress);
            ++i;
        }
    }

    private void renderSectorContent(GuiGraphics guiGraphics, float ox, float oy, float midAngle, float step, LocationEntry loc, float progress) {
        float rText = 78.0f;
        float tx = ox + (float)Math.cos(midAngle) * rText;
        float ty = oy + (float)Math.sin(midAngle) * rText;
        Font font = this.font;
        int count = this.getCurrentPageLocations().size();
        float arcWidth = (float)((double)(rText * step) - 12.0);
        Object name = loc.name();
        if (count > 1) {
            if (arcWidth < 50.0f && ((String)name).length() > 6) {
                name = ((String)name).substring(0, 5) + "\u2026";
            } else if (arcWidth < 80.0f && (float)font.width((String)name) > arcWidth) {
                name = font.plainSubstrByWidth((String)name, (int)arcWidth - 4) + "\u2026";
            }
        } else if (font.width((String)name) > 100) {
            name = font.plainSubstrByWidth((String)name, 96) + "\u2026";
        }
        int nameColor = progress > 0.5f ? -12720539 : (loc.isActive() ? -19674 : -2038292);
        int nameWidth = font.width((String)name);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(tx, ty, 0.0f);
        String badge = RadialLocationScreen.formatDimensionBadge(loc.dimension());
        int badgeColor = RadialLocationScreen.getDimColor(loc.dimension());
        int badgeWidth = font.width(badge);
        if (count <= 6) {
            guiGraphics.drawString(font, (String)name, -nameWidth / 2, -7, nameColor, false);
            guiGraphics.drawString(font, badge, -badgeWidth / 2, 3, badgeColor, false);
        } else {
            guiGraphics.drawString(font, (String)name, -nameWidth / 2, -4, nameColor, false);
        }
        guiGraphics.pose().popPose();
    }

    private void renderCenterHub(GuiGraphics guiGraphics, float cx, float cy, List<LocationEntry> pageLocs) {
        Matrix4f matrix = guiGraphics.pose().last().pose();
        RadialRenderUtils.drawCircle((Matrix4f)matrix, (float)cx, (float)cy, (float)43.0f, (int)-16379127, (int)32);
        RadialRenderUtils.drawCircleOutline((Matrix4f)matrix, (float)cx, (float)cy, (float)43.0f, (int)-14992606, (float)1.5f, (int)32);
        RadialRenderUtils.drawCircleOutline((Matrix4f)matrix, (float)cx, (float)cy, (float)41.0f, (int)-16047086, (float)1.0f, (int)32);
        Font font = this.font;
        if (this.hoveredIndex >= 0 && this.hoveredIndex < pageLocs.size()) {
            LocationEntry loc = pageLocs.get(this.hoveredIndex);
            Object name = loc.name();
            if (font.width((String)name) > 68) {
                name = font.plainSubstrByWidth((String)name, 64) + "\u2026";
            }
            int nw = font.width((String)name);
            guiGraphics.drawString(font, (String)name, (int)(cx - (float)(nw / 2)), (int)(cy - 20.0f), -12720539, false);
            Object dimName = PortalPlacementHelper.formatDimensionName((String)loc.dimension());
            if (font.width((String)dimName) > 68) {
                dimName = font.plainSubstrByWidth((String)dimName, 64) + "\u2026";
            }
            int dw = font.width((String)dimName);
            guiGraphics.drawString(font, (String)dimName, (int)(cx - (float)(dw / 2)), (int)(cy - 9.0f), RadialLocationScreen.getDimColor(loc.dimension()), false);
            String coords = (int)loc.x() + " " + (int)loc.y() + " " + (int)loc.z();
            int cw = font.width(coords);
            guiGraphics.drawString(font, coords, (int)(cx - (float)(cw / 2)), (int)(cy + 3.0f), -7696228, false);
            String action = loc.isActive() ? "\u00a76[ACTIVE]" : "\u00a7a\u25ba SELECT \u25c4";
            int aw = font.width(action);
            guiGraphics.drawString(font, action, (int)(cx - (float)(aw / 2)), (int)(cy + 14.0f), -1, false);
        } else if (this.activeIndex >= 0 && this.activeIndex < this.allLocations.size()) {
            LocationEntry active = this.allLocations.get(this.activeIndex);
            String title = "ACTIVE TARGET";
            int tw = font.width(title);
            guiGraphics.drawString(font, title, (int)(cx - (float)(tw / 2)), (int)(cy - 18.0f), -19674, false);
            Object name = active.name();
            if (font.width((String)name) > 68) {
                name = font.plainSubstrByWidth((String)name, 64) + "\u2026";
            }
            int nw = font.width((String)name);
            guiGraphics.drawString(font, (String)name, (int)(cx - (float)(nw / 2)), (int)(cy - 7.0f), -12720539, false);
            String coords = (int)active.x() + " " + (int)active.y() + " " + (int)active.z();
            int cw = font.width(coords);
            guiGraphics.drawString(font, coords, (int)(cx - (float)(cw / 2)), (int)(cy + 5.0f), -7696228, false);
            if (this.totalPages > 1) {
                String pageText = "\u00a77[P." + (this.currentPage + 1) + "/" + this.totalPages + "]";
                int pw = font.width(pageText);
                guiGraphics.drawString(font, pageText, (int)(cx - (float)(pw / 2)), (int)(cy + 16.0f), -7696228, false);
            } else {
                String hint = "\u00a78(Hover)";
                int hw = font.width(hint);
                guiGraphics.drawString(font, hint, (int)(cx - (float)(hw / 2)), (int)(cy + 16.0f), -10063744, false);
            }
        } else {
            String title = "PORTAL TARGET";
            int tw = font.width(title);
            guiGraphics.drawString(font, title, (int)(cx - (float)(tw / 2)), (int)(cy - 14.0f), -12720539, false);
            String prompt = "SELECT SECTOR";
            int pw = font.width(prompt);
            guiGraphics.drawString(font, prompt, (int)(cx - (float)(pw / 2)), (int)(cy - 2.0f), -7696228, false);
            if (this.totalPages > 1) {
                String pageText = "\u00a77[P." + (this.currentPage + 1) + "/" + this.totalPages + "]";
                int pgw = font.width(pageText);
                guiGraphics.drawString(font, pageText, (int)(cx - (float)(pgw / 2)), (int)(cy + 10.0f), -7696228, false);
            } else {
                String hint = "\u00a78(Release/Click)";
                int hw = font.width(hint);
                guiGraphics.drawString(font, hint, (int)(cx - (float)(hw / 2)), (int)(cy + 10.0f), -10063744, false);
            }
        }
    }

    private void renderPaginationControls(GuiGraphics guiGraphics, float cx, float cy, int mouseX, int mouseY) {
        int btnW = 16;
        int btnH = 14;
        int btnY = (int)(cy + 112.0f + 12.0f);
        int btnLeftX = (int)(cx - 42.0f);
        int btnRightX = (int)(cx + 26.0f);
        Font font = this.font;
        boolean hoverPrev = mouseX >= btnLeftX && mouseX <= btnLeftX + btnW && mouseY >= btnY && mouseY <= btnY + btnH;
        int prevBg = hoverPrev ? -14398932 : -15065822;
        int prevBorder = hoverPrev ? -12720539 : -13289150;
        guiGraphics.fill(btnLeftX, btnY, btnLeftX + btnW, btnY + btnH, prevBg);
        guiGraphics.renderOutline(btnLeftX, btnY, btnW, btnH, prevBorder);
        guiGraphics.drawString(font, "\u25c4", btnLeftX + 4, btnY + 3, hoverPrev ? -12720539 : -7696228, false);
        boolean hoverNext = mouseX >= btnRightX && mouseX <= btnRightX + btnW && mouseY >= btnY && mouseY <= btnY + btnH;
        int nextBg = hoverNext ? -14398932 : -15065822;
        int nextBorder = hoverNext ? -12720539 : -13289150;
        guiGraphics.fill(btnRightX, btnY, btnRightX + btnW, btnY + btnH, nextBg);
        guiGraphics.renderOutline(btnRightX, btnY, btnW, btnH, nextBorder);
        guiGraphics.drawString(font, "\u25ba", btnRightX + 5, btnY + 3, hoverNext ? -12720539 : -7696228, false);
        String pageInfo = this.currentPage + 1 + " / " + this.totalPages;
        int piw = font.width(pageInfo);
        guiGraphics.drawString(font, pageInfo, (int)(cx - (float)(piw / 2)), btnY + 3, -19674, false);
        String scrollHint = "\u00a78[Scroll / A-D to switch]";
        int shw = font.width(scrollHint);
        guiGraphics.drawString(font, scrollHint, (int)(cx - (float)(shw / 2)), btnY + 18, -10063744, false);
    }

    private void renderEmptyState(GuiGraphics guiGraphics, float cx, float cy) {
        boolean hasCard;
        int panelW = 200;
        int panelH = 90;
        int left = (int)(cx - (float)(panelW / 2));
        int top = (int)(cy - (float)(panelH / 2));
        GadgetGuiUtils.renderChassis((GuiGraphics)guiGraphics, (int)left, (int)top, (int)panelW, (int)panelH);
        GadgetGuiUtils.renderHeader((GuiGraphics)guiGraphics, (int)(left + 4), (int)(top + 4), (int)(panelW - 8), (int)16);
        Font font = this.font;
        String header = "TARGET LOCATIONS";
        int hw = font.width(header);
        guiGraphics.drawString(font, header, (int)(cx - (float)(hw / 2)), top + 8, -12720539, false);
        boolean bl = hasCard = this.gunStack != null && PortalGunItem.hasMemoryCard((ItemStack)this.gunStack) || this.gunNbt != null && this.gunNbt.getBoolean("HasMemoryCard");
        if (!hasCard) {
            msg1 = "\u00a7cNo Memory Card";
            m1w = font.width(msg1);
            guiGraphics.drawString(font, msg1, (int)(cx - (float)(m1w / 2)), top + 28, -43691, false);
            String msg2 = "Insert Quantum Memory Card";
            int m2w = font.width(msg2);
            guiGraphics.drawString(font, msg2, (int)(cx - (float)(m2w / 2)), top + 42, -7696228, false);
            String hint = "\u00a78[to save & select locations]";
            int hnw = font.width(hint);
            guiGraphics.drawString(font, hint, (int)(cx - (float)(hnw / 2)), top + 56, -10063744, false);
        } else {
            msg1 = "No Saved Locations";
            m1w = font.width(msg1);
            guiGraphics.drawString(font, msg1, (int)(cx - (float)(m1w / 2)), top + 28, -19674, false);
            String msg2 = "Save positions via Gun Menu";
            int m2w = font.width(msg2);
            guiGraphics.drawString(font, msg2, (int)(cx - (float)(m2w / 2)), top + 42, -7696228, false);
            String hint = "\u00a78[Shift + Right Click to configure]";
            int hnw = font.width(hint);
            guiGraphics.drawString(font, hint, (int)(cx - (float)(hnw / 2)), top + 56, -10063744, false);
        }
        String closePrompt = "\u00a7c[Click or ESC to close]";
        int cpw = font.width(closePrompt);
        guiGraphics.drawString(font, closePrompt, (int)(cx - (float)(cpw / 2)), top + 72, -6707787, false);
    }

    private void selectLocation(int pageIndex) {
        List<LocationEntry> pageLocs = this.getCurrentPageLocations();
        if (pageIndex < 0) return;
        if (pageIndex >= pageLocs.size()) return;
        LocationEntry loc = pageLocs.get(pageIndex);
        ModMessages.sendToServer((CustomPacketPayload)new C2SSelectSavedLocationPacket(loc.originalIndex()));
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.playSound((SoundEvent)ModSounds.UI_PRESS.get(), 1.0f, 1.0f);
        }
        this.onClose();
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (this.totalPages <= 1) return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        if (scrollY < 0.0) {
            this.nextPage();
            return true;
        }
        if (!(scrollY > 0.0)) return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        this.prevPage();
        return true;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) {
            if (button != 1) return super.mouseClicked(mouseX, mouseY, button);
            this.onClose();
            return true;
        }
        float cx = (float)this.width / 2.0f;
        float cy = (float)this.height / 2.0f;
        if (this.totalPages > 1) {
            int btnW = 16;
            int btnH = 14;
            int btnY = (int)(cy + 112.0f + 12.0f);
            int btnLeftX = (int)(cx - 42.0f);
            int btnRightX = (int)(cx + 26.0f);
            if (mouseX >= (double)btnLeftX && mouseX <= (double)(btnLeftX + btnW) && mouseY >= (double)btnY && mouseY <= (double)(btnY + btnH)) {
                this.prevPage();
                return true;
            }
            if (mouseX >= (double)btnRightX && mouseX <= (double)(btnRightX + btnW) && mouseY >= (double)btnY && mouseY <= (double)(btnY + btnH)) {
                this.nextPage();
                return true;
            }
        }
        List<LocationEntry> pageLocs = this.getCurrentPageLocations();
        if (this.hoveredIndex >= 0 && this.hoveredIndex < pageLocs.size()) {
            this.selectLocation(this.hoveredIndex);
            return true;
        }
        float dx = (float)(mouseX - (double)cx);
        float dy = (float)(mouseY - (double)cy);
        float dist = (float)Math.sqrt(dx * dx + dy * dy);
        if (!(dist < 38.0f)) {
            if (!this.allLocations.isEmpty()) return super.mouseClicked(mouseX, mouseY, button);
        }
        this.onClose();
        return true;
    }

    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (this.triggerKey <= 0) return super.keyReleased(keyCode, scanCode, modifiers);
        if (keyCode != this.triggerKey) return super.keyReleased(keyCode, scanCode, modifiers);
        List<LocationEntry> pageLocs = this.getCurrentPageLocations();
        if (this.hoveredIndex >= 0 && this.hoveredIndex < pageLocs.size()) {
            this.selectLocation(this.hoveredIndex);
        } else {
            this.onClose();
        }
        return true;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            this.onClose();
            return true;
        }
        if (this.totalPages <= 1) return super.keyPressed(keyCode, scanCode, modifiers);
        if (keyCode == 262 || keyCode == 68 || keyCode == 258) {
            this.nextPage();
            return true;
        }
        if (keyCode != 263) {
            if (keyCode != 65) return super.keyPressed(keyCode, scanCode, modifiers);
        }
        this.prevPage();
        return true;
    }

    public boolean isPauseScreen() {
        return false;
    }

    private static int lerpColor(int c1, int c2, float t) {
        t = Math.max(0.0f, Math.min(1.0f, t));
        int a1 = c1 >> 24 & 0xFF;
        int r1 = c1 >> 16 & 0xFF;
        int g1 = c1 >> 8 & 0xFF;
        int b1 = c1 & 0xFF;
        int a2 = c2 >> 24 & 0xFF;
        int r2 = c2 >> 16 & 0xFF;
        int g2 = c2 >> 8 & 0xFF;
        int b2 = c2 & 0xFF;
        int a = (int)((float)a1 + (float)(a2 - a1) * t);
        int r = (int)((float)r1 + (float)(r2 - r1) * t);
        int g = (int)((float)g1 + (float)(g2 - g1) * t);
        int b = (int)((float)b1 + (float)(b2 - b1) * t);
        return a << 24 | r << 16 | g << 8 | b;
    }

    private static int getDimColor(String dim) {
        if (dim == null) return -16711834;
        if (dim.isEmpty()) {
            return -16711834;
        }
        String lower = dim.toLowerCase();
        if (lower.contains("nether")) {
            return -48060;
        }
        if (lower.contains("end")) {
            return -43521;
        }
        if (lower.contains("overworld")) {
            return -16711834;
        }
        if (!lower.contains("blender")) return -16718337;
        return -19674;
    }

    private static String formatDimensionBadge(String dim) {
        if (dim == null) return "[OVER]";
        if (dim.isEmpty()) {
            return "[OVER]";
        }
        String lower = dim.toLowerCase();
        if (lower.contains("nether")) {
            return "[NETH]";
        }
        if (lower.contains("end")) {
            return "[ END]";
        }
        if (lower.contains("overworld")) {
            return "[OVER]";
        }
        if (lower.contains("blender")) {
            return "[BLND]";
        }
        String path = lower.contains(":") ? lower.substring(lower.indexOf(":") + 1) : lower;
        String badge = path.replace("_", "").toUpperCase();
        if (badge.length() <= 4) return "[" + String.format("%-4s", badge) + "]";
        badge = badge.substring(0, 4);
        return "[" + String.format("%-4s", badge) + "]";
    }
}

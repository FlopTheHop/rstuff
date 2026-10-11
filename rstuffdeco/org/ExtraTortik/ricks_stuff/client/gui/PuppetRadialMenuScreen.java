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
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.phys.AABB
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.ExtraTortik.ricks_stuff.client.BioNeuralPossessionManager
 *  org.ExtraTortik.ricks_stuff.client.gui.PuppetRadialMenuScreen$PuppetEntry
 *  org.ExtraTortik.ricks_stuff.client.gui.RadialRenderUtils
 *  org.ExtraTortik.ricks_stuff.init.ModSounds
 *  org.ExtraTortik.ricks_stuff.item.BioNeuralReceiverItem
 *  org.ExtraTortik.ricks_stuff.network.ModMessages
 *  org.ExtraTortik.ricks_stuff.network.packet.C2SRequestPuppetListPacket
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CPuppetListPacket$PuppetEntryData
 *  org.joml.Matrix4f
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.ExtraTortik.ricks_stuff.client.BioNeuralPossessionManager;
import org.ExtraTortik.ricks_stuff.client.gui.PuppetRadialMenuScreen;
import org.ExtraTortik.ricks_stuff.client.gui.RadialRenderUtils;
import org.ExtraTortik.ricks_stuff.init.ModSounds;
import org.ExtraTortik.ricks_stuff.item.BioNeuralReceiverItem;
import org.ExtraTortik.ricks_stuff.network.ModMessages;
import org.ExtraTortik.ricks_stuff.network.packet.C2SRequestPuppetListPacket;
import org.ExtraTortik.ricks_stuff.network.packet.S2CPuppetListPacket;
import org.joml.Matrix4f;

@OnlyIn(value=Dist.CLIENT)
public class PuppetRadialMenuScreen
extends Screen {
    public static final int MOBS_PER_PAGE = 10;
    private final List<PuppetEntry> allPuppets = new ArrayList<PuppetEntry>();
    private int currentPage = 0;
    private int totalPages = 1;
    private float[] hoverProgress = new float[0];
    private long lastTimeNanos = 0L;
    private int hoveredIndex = -1;
    private int lastSoundHoveredIndex = -1;
    private final int triggerKey;
    private static final float R_INNER = 44.0f;
    private static final float R_OUTER = 112.0f;
    private static final float R_DEADZONE = 38.0f;
    private static final float R_MAX = 150.0f;
    private static final float ANIMATION_SPEED = 16.0f;

    public PuppetRadialMenuScreen(int triggerKey) {
        super((Component)Component.literal((String)"Puppet Radial Menu"));
        this.triggerKey = triggerKey;
    }

    protected void init() {
        super.init();
        this.loadChippedMobs();
        ModMessages.sendToServer((CustomPacketPayload)new C2SRequestPuppetListPacket());
        this.lastTimeNanos = System.nanoTime();
    }

    private void loadChippedMobs() {
        this.allPuppets.clear();
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        if (Minecraft.getInstance().level == null) {
            return;
        }
        for (ItemStack stack : player.getInventory().items) {
            this.checkAndAddChip((Player)player, stack);
        }
        this.checkAndAddChip((Player)player, player.getOffhandItem());
        AABB box = player.getBoundingBox().inflate(64.0);
        List nearbyMobs = Minecraft.getInstance().level.getEntitiesOfClass(Mob.class, box, LivingEntity::isAlive);
        Iterator iterator = nearbyMobs.iterator();
        while (true) {
            UUID owner;
            if (!iterator.hasNext()) {
                this.allPuppets.sort((a, b) -> Double.compare(a.distance(), b.distance()));
                this.totalPages = Math.max(1, (int)Math.ceil((double)this.allPuppets.size() / 10.0));
                this.updatePageArray();
                return;
            }
            Mob mob = (Mob)iterator.next();
            CompoundTag data = mob.getPersistentData();
            if (!data.getBoolean("BioNeuralChipped")) continue;
            UUID uUID = owner = data.hasUUID("BioNeuralOwner") ? data.getUUID("BioNeuralOwner") : null;
            if (!player.getUUID().equals(owner)) continue;
            this.addPuppetIfAbsent(new PuppetEntry(mob.getId(), mob.getUUID(), mob.getDisplayName().getString(), mob.getHealth(), mob.getMaxHealth(), (double)player.distanceTo((Entity)mob), mob));
        }
    }

    private void checkAndAddChip(Player player, ItemStack stack) {
        if (stack == null) return;
        if (stack.isEmpty()) return;
        if (!(stack.getItem() instanceof BioNeuralReceiverItem)) return;
        if (!BioNeuralReceiverItem.hasLinkedMob((ItemStack)stack)) return;
        UUID uuid = BioNeuralReceiverItem.getLinkedMobUUID((ItemStack)stack);
        int id = BioNeuralReceiverItem.getLinkedMobId((ItemStack)stack);
        Mob mob = this.findMobInClientLevel(id, uuid);
        if (mob == null) return;
        if (!mob.isAlive()) return;
        double dist = player.distanceTo((Entity)mob);
        if (!(dist <= 64.0)) return;
        this.addPuppetIfAbsent(new PuppetEntry(mob.getId(), mob.getUUID(), mob.getDisplayName().getString(), mob.getHealth(), mob.getMaxHealth(), dist, mob));
    }

    public void updateServerPuppets(List<S2CPuppetListPacket.PuppetEntryData> serverPuppets) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        if (Minecraft.getInstance().level == null) {
            return;
        }
        Iterator<S2CPuppetListPacket.PuppetEntryData> iterator = serverPuppets.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                this.allPuppets.sort((a, b) -> Double.compare(a.distance(), b.distance()));
                this.totalPages = Math.max(1, (int)Math.ceil((double)this.allPuppets.size() / 10.0));
                this.updatePageArray();
                return;
            }
            S2CPuppetListPacket.PuppetEntryData data = iterator.next();
            Mob mob = this.findMobInClientLevel(data.id(), data.uuid());
            double dist = player.distanceToSqr(data.x(), data.y(), data.z());
            dist = Math.sqrt(dist);
            this.addPuppetIfAbsent(new PuppetEntry(data.id(), data.uuid(), data.name(), data.health(), data.maxHealth(), dist, mob));
        }
    }

    private Mob findMobInClientLevel(int id, UUID uuid) {
        Mob mob;
        Entity e;
        Mob mob2;
        if (Minecraft.getInstance().level == null) {
            return null;
        }
        Entity entity = Minecraft.getInstance().level.getEntity(id);
        if (entity instanceof Mob && (mob2 = (Mob)entity).isAlive()) {
            return mob2;
        }
        if (uuid == null) return null;
        Iterator iterator = Minecraft.getInstance().level.entitiesForRendering().iterator();
        do {
            if (!iterator.hasNext()) return null;
        } while (!(e = (Entity)iterator.next()).getUUID().equals(uuid) || !(e instanceof Mob) || !(mob = (Mob)e).isAlive());
        return mob;
    }

    private void addPuppetIfAbsent(PuppetEntry entry) {
        int i = 0;
        while (true) {
            if (i >= this.allPuppets.size()) {
                this.allPuppets.add(entry);
                return;
            }
            PuppetEntry existing = this.allPuppets.get(i);
            if (existing.uuid().equals(entry.uuid()) || existing.id() == entry.id()) {
                if (existing.mob() != null) return;
                if (entry.mob() == null) return;
                this.allPuppets.set(i, entry);
                return;
            }
            ++i;
        }
    }

    private void updatePageArray() {
        int count = this.getPagePuppets().size();
        if (this.hoverProgress.length != count) {
            this.hoverProgress = new float[count];
        }
        this.hoveredIndex = -1;
        this.lastSoundHoveredIndex = -1;
    }

    private List<PuppetEntry> getPagePuppets() {
        int start = this.currentPage * 10;
        int end = Math.min(start + 10, this.allPuppets.size());
        if (start < end) return this.allPuppets.subList(start, end);
        return List.of();
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        long now = System.nanoTime();
        float dt = (float)(now - this.lastTimeNanos) / 1.0E9f;
        this.lastTimeNanos = now;
        if (dt > 0.1f) {
            dt = 0.1f;
        }
        float cx = (float)this.width / 2.0f;
        float cy = (float)this.height / 2.0f;
        List<PuppetEntry> pagePuppets = this.getPagePuppets();
        this.updateHoveredSector(cx, cy, mouseX, mouseY, pagePuppets);
        if (this.hoveredIndex != this.lastSoundHoveredIndex) {
            if (this.hoveredIndex >= 0 && Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.playSound((SoundEvent)ModSounds.UI_DIAL.get(), 0.25f, 1.4f);
            }
            this.lastSoundHoveredIndex = this.hoveredIndex;
        }
        for (int i = 0; i < pagePuppets.size(); ++i) {
            float target = i == this.hoveredIndex ? 1.0f : 0.0f;
            int n = i;
            this.hoverProgress[n] = this.hoverProgress[n] + (target - this.hoverProgress[i]) * Math.min(1.0f, dt * 16.0f);
        }
        guiGraphics.fillGradient(0, 0, this.width, this.height, -2012213226, -1442314228);
        if (this.allPuppets.isEmpty()) {
            this.renderEmptyState(guiGraphics, cx, cy);
        } else {
            this.renderRadialSectors(guiGraphics, cx, cy, pagePuppets);
            this.renderCenterHub(guiGraphics, cx, cy, pagePuppets);
            if (this.totalPages > 1) {
                this.renderPagination(guiGraphics, cx, cy);
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

    private void updateHoveredSector(float cx, float cy, int mouseX, int mouseY, List<PuppetEntry> pagePuppets) {
        float norm;
        if (pagePuppets.isEmpty()) {
            this.hoveredIndex = -1;
            return;
        }
        int count = pagePuppets.size();
        if (count == 1) {
            this.hoveredIndex = 0;
            return;
        }
        float dx = (float)mouseX - cx;
        float dy = (float)mouseY - cy;
        float dist = (float)Math.sqrt(dx * dx + dy * dy);
        if (dist < 38.0f || dist > 150.0f) {
            this.hoveredIndex = this.lastSoundHoveredIndex >= 0 && this.lastSoundHoveredIndex < count ? this.lastSoundHoveredIndex : -1;
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

    private void renderRadialSectors(GuiGraphics guiGraphics, float cx, float cy, List<PuppetEntry> pagePuppets) {
        Matrix4f matrix = guiGraphics.pose().last().pose();
        int count = pagePuppets.size();
        if (count == 1) {
            PuppetEntry entry = pagePuppets.get(0);
            float progress = this.hoverProgress[0];
            int fillColor = PuppetRadialMenuScreen.lerpColor(-669245916, -298313216, progress);
            int outlineColor = PuppetRadialMenuScreen.lerpColor(-10868214, -50432, progress);
            RadialRenderUtils.drawRing((Matrix4f)matrix, (float)cx, (float)cy, (float)44.0f, (float)112.0f, (int)fillColor, (int)64);
            RadialRenderUtils.drawRingOutline((Matrix4f)matrix, (float)cx, (float)cy, (float)44.0f, (float)112.0f, (int)outlineColor, (float)(1.8f + progress * 1.0f), (int)64);
            this.renderSectorContent(guiGraphics, cx, cy, -1.5707964f, entry, progress);
            return;
        }
        float step = (float)(Math.PI * 2 / (double)count);
        float angularGap = Math.min(0.06f, 0.25f / (float)count);
        int i = 0;
        while (i < count) {
            PuppetEntry entry = pagePuppets.get(i);
            float progress = this.hoverProgress[i];
            float midAngle = (float)((double)((float)i * step) - 1.5707963267948966);
            float startAngle = midAngle - step / 2.0f + angularGap / 2.0f;
            float endAngle = midAngle + step / 2.0f - angularGap / 2.0f;
            float rOut = 112.0f + progress * 10.0f;
            int fillColor = PuppetRadialMenuScreen.lerpColor(-669245916, -298313216, progress);
            int outlineColor = PuppetRadialMenuScreen.lerpColor(-10868214, -50432, progress);
            RadialRenderUtils.drawSector((Matrix4f)matrix, (float)cx, (float)cy, (float)44.0f, (float)rOut, (float)startAngle, (float)endAngle, (int)fillColor, (int)32);
            RadialRenderUtils.drawSectorOutline((Matrix4f)matrix, (float)cx, (float)cy, (float)44.0f, (float)rOut, (float)startAngle, (float)endAngle, (int)outlineColor, (float)(1.6f + progress * 1.2f), (int)32);
            this.renderSectorContent(guiGraphics, cx, cy, midAngle, entry, progress);
            ++i;
        }
    }

    private void renderSectorContent(GuiGraphics guiGraphics, float cx, float cy, float midAngle, PuppetEntry entry, float progress) {
        Font font = this.font;
        float rText = 78.0f + progress * 5.0f;
        float tx = cx + (float)Math.cos(midAngle) * rText;
        float ty = cy + (float)Math.sin(midAngle) * rText;
        Object name = entry.name().toUpperCase();
        if (((String)name).length() > 14) {
            name = ((String)name).substring(0, 12) + "..";
        }
        int nameColor = PuppetRadialMenuScreen.lerpColor(-2171, -1, progress);
        guiGraphics.drawString(font, (String)name, (int)(tx - (float)font.width((String)name) / 2.0f), (int)(ty - 6.0f), nameColor, true);
        String distText = String.format("%.1fm", entry.distance());
        guiGraphics.drawString(font, distText, (int)(tx - (float)font.width(distText) / 2.0f), (int)(ty + 4.0f), -22016, true);
    }

    private void renderCenterHub(GuiGraphics guiGraphics, float cx, float cy, List<PuppetEntry> pagePuppets) {
        Matrix4f matrix = guiGraphics.pose().last().pose();
        Font font = this.font;
        RadialRenderUtils.drawRing((Matrix4f)matrix, (float)cx, (float)cy, (float)0.0f, (float)38.0f, (int)-300805608, (int)48);
        RadialRenderUtils.drawRingOutline((Matrix4f)matrix, (float)cx, (float)cy, (float)0.0f, (float)38.0f, (int)-50432, (float)2.0f, (int)48);
        if (this.hoveredIndex >= 0 && this.hoveredIndex < pagePuppets.size()) {
            PuppetEntry entry = pagePuppets.get(this.hoveredIndex);
            Object title = entry.name().toUpperCase();
            if (((String)title).length() > 9) {
                title = ((String)title).substring(0, 7) + "..";
            }
            guiGraphics.drawString(font, (String)title, (int)(cx - (float)font.width((String)title) / 2.0f), (int)(cy - 14.0f), -2171, true);
            String hpStr = String.format("%.0f/%.0f HP", Float.valueOf(entry.health()), Float.valueOf(entry.maxHealth()));
            guiGraphics.drawString(font, hpStr, (int)(cx - (float)font.width(hpStr) / 2.0f), (int)(cy - 3.0f), -16711800, true);
            String act = "[POSSESS]";
            guiGraphics.drawString(font, act, (int)(cx - (float)font.width(act) / 2.0f), (int)(cy + 8.0f), -50432, true);
        } else {
            String label1 = "NEURAL";
            String label2 = "LINK";
            guiGraphics.drawString(font, label1, (int)(cx - (float)font.width(label1) / 2.0f), (int)(cy - 8.0f), -2171, true);
            guiGraphics.drawString(font, label2, (int)(cx - (float)font.width(label2) / 2.0f), (int)(cy + 2.0f), -7829351, true);
        }
    }

    private void renderEmptyState(GuiGraphics guiGraphics, float cx, float cy) {
        Font font = this.font;
        int boxW = 200;
        int boxH = 38;
        int x = (int)(cx - (float)boxW / 2.0f);
        int y = (int)(cy - (float)boxH / 2.0f);
        guiGraphics.fill(x, y, x + boxW, y + boxH, -300279262);
        guiGraphics.fill(x - 1, y - 1, x + boxW + 1, y, -50432);
        guiGraphics.fill(x - 1, y + boxH, x + boxW + 1, y + boxH + 1, -50432);
        guiGraphics.fill(x - 1, y, x, y + boxH, -50432);
        guiGraphics.fill(x + boxW, y, x + boxW + 1, y + boxH + 1, -50432);
        String title = "NO CHIPPED MOBS IN RANGE";
        String desc = "Signal limit: 64 meters";
        guiGraphics.drawString(font, title, (int)(cx - (float)font.width(title) / 2.0f), y + 8, -50432, true);
        guiGraphics.drawString(font, desc, (int)(cx - (float)font.width(desc) / 2.0f), y + 21, -7829351, true);
    }

    private void renderPagination(GuiGraphics guiGraphics, float cx, float cy) {
        String pageStr = String.format("PAGE %d / %d (SCROLL)", this.currentPage + 1, this.totalPages);
        guiGraphics.drawString(this.font, pageStr, (int)(cx - (float)this.font.width(pageStr) / 2.0f), (int)(cy + 112.0f + 18.0f), -2171, true);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return super.mouseClicked(mouseX, mouseY, button);
        int count = this.getPagePuppets().size();
        if (this.hoveredIndex < 0 && count != 1) {
            if (this.lastSoundHoveredIndex < 0) return super.mouseClicked(mouseX, mouseY, button);
        }
        this.selectHoveredMob();
        return true;
    }

    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode != this.triggerKey) return super.keyReleased(keyCode, scanCode, modifiers);
        int count = this.getPagePuppets().size();
        if (this.hoveredIndex >= 0 || count == 1 || this.lastSoundHoveredIndex >= 0) {
            this.selectHoveredMob();
        } else {
            this.onClose();
        }
        return true;
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (this.totalPages <= 1) return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        if (scrollY < 0.0) {
            this.currentPage = (this.currentPage + 1) % this.totalPages;
            this.updatePageArray();
            return true;
        }
        if (!(scrollY > 0.0)) return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        this.currentPage = (this.currentPage - 1 + this.totalPages) % this.totalPages;
        this.updatePageArray();
        return true;
    }

    private void selectHoveredMob() {
        List<PuppetEntry> pagePuppets = this.getPagePuppets();
        int index = this.hoveredIndex;
        if (index < 0) {
            if (pagePuppets.size() == 1) {
                index = 0;
            } else if (this.lastSoundHoveredIndex >= 0 && this.lastSoundHoveredIndex < pagePuppets.size()) {
                index = this.lastSoundHoveredIndex;
            }
        }
        if (index < 0) return;
        if (index >= pagePuppets.size()) return;
        PuppetEntry selected = pagePuppets.get(index);
        Mob mobToPossess = selected.mob() != null ? selected.mob() : this.findMobInClientLevel(selected.id(), selected.uuid());
        BioNeuralPossessionManager.tryStartPossession((int)selected.id(), (UUID)selected.uuid(), (String)selected.name(), (float)selected.health(), (float)selected.maxHealth(), (Mob)mobToPossess);
        this.onClose();
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

    public boolean isPauseScreen() {
        return false;
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.client.event.RenderGuiLayerEvent$Post
 *  net.neoforged.neoforge.client.event.RenderGuiLayerEvent$Pre
 *  net.neoforged.neoforge.client.gui.VanillaGuiLayers
 *  org.ExtraTortik.ricks_stuff.compat.curios.CuriosCompat
 *  org.ExtraTortik.ricks_stuff.item.EvilMortyEnergyShieldItem
 */
package org.ExtraTortik.ricks_stuff.client.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import org.ExtraTortik.ricks_stuff.compat.curios.CuriosCompat;
import org.ExtraTortik.ricks_stuff.item.EvilMortyEnergyShieldItem;

@OnlyIn(value=Dist.CLIENT)
@EventBusSubscriber(modid="ricks_stuff", value={Dist.CLIENT})
public class EvilMortyShieldHudOverlay {
    public static final ResourceLocation SHIELD_HEARTS_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/gui/shield_hearts.png");
    private static final int TEXTURE_WIDTH = 64;
    private static final int TEXTURE_HEIGHT = 32;
    private static final int HEART_SIZE = 9;
    private static final int HEART_SPACING = 8;
    private static int savedLeft;
    private static int savedTop;
    private static boolean hasCoords;

    @SubscribeEvent
    public static void onRenderGuiOverlayPre(RenderGuiLayerEvent.Pre event) {
        if (!event.getName().equals((Object)VanillaGuiLayers.PLAYER_HEALTH)) {
            return;
        }
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        if (player.isCreative()) return;
        if (player.isSpectator()) {
            return;
        }
        if (!CuriosCompat.hasActiveShield((Player)player)) {
            return;
        }
        savedLeft = event.getGuiGraphics().guiWidth() / 2 - 91;
        savedTop = event.getGuiGraphics().guiHeight() - Minecraft.getInstance().gui.leftHeight;
        hasCoords = true;
    }

    @SubscribeEvent
    public static void onRenderGuiOverlayPost(RenderGuiLayerEvent.Post event) {
        if (!event.getName().equals((Object)VanillaGuiLayers.PLAYER_HEALTH)) {
            return;
        }
        if (!hasCoords) {
            return;
        }
        hasCoords = false;
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        if (player.isCreative()) return;
        if (player.isSpectator()) {
            return;
        }
        if (!CuriosCompat.hasActiveShield((Player)player)) {
            return;
        }
        ItemStack shield = CuriosCompat.findShield((Player)player);
        int hearts = Mth.clamp((int)EvilMortyEnergyShieldItem.getShieldHearts((ItemStack)shield), (int)0, (int)10);
        if (hearts <= 0) {
            return;
        }
        int regen = -1;
        if (player.hasEffect(MobEffects.REGENERATION)) {
            regen = Minecraft.getInstance().gui.getGuiTicks() % Mth.ceil((float)(player.getMaxHealth() + 5.0f));
        }
        float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);
        float time = ((float)Minecraft.getInstance().gui.getGuiTicks() + partialTick) * 0.12f;
        float pulse = Mth.sin((float)time) * 0.5f + 0.5f;
        float alpha = 0.3f + 0.3f * pulse;
        GuiGraphics guiGraphics = event.getGuiGraphics();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, alpha);
        int i = 0;
        while (true) {
            if (i >= hearts) {
                guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
                RenderSystem.disableBlend();
                return;
            }
            int x = savedLeft + i * 8;
            int y = savedTop + (i == regen ? -2 : 0);
            guiGraphics.blit(SHIELD_HEARTS_TEXTURE, x, y, 9.0f, 0.0f, 9, 9, 64, 32);
            ++i;
        }
    }
}

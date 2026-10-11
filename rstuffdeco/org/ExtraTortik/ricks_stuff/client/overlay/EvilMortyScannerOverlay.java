/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.BufferBuilder
 *  com.mojang.blaze3d.vertex.BufferUploader
 *  com.mojang.blaze3d.vertex.DefaultVertexFormat
 *  com.mojang.blaze3d.vertex.MeshData
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.Tesselator
 *  com.mojang.blaze3d.vertex.VertexFormat$Mode
 *  net.minecraft.client.Camera
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.LayeredDraw$Layer
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.GameRenderer
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.monster.MagmaCube
 *  net.minecraft.world.entity.monster.Slime
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.ExtraTortik.ricks_stuff.client.TargetScanManager
 *  org.ExtraTortik.ricks_stuff.client.gui.RadialRenderUtils
 *  org.ExtraTortik.ricks_stuff.compat.curios.CuriosCompat
 *  org.joml.Matrix4f
 *  org.joml.Quaternionf
 *  org.joml.Quaternionfc
 *  org.joml.Vector3f
 */
package org.ExtraTortik.ricks_stuff.client.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.Objects;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.ExtraTortik.ricks_stuff.client.TargetScanManager;
import org.ExtraTortik.ricks_stuff.client.gui.RadialRenderUtils;
import org.ExtraTortik.ricks_stuff.compat.curios.CuriosCompat;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;

@OnlyIn(value=Dist.CLIENT)
public class EvilMortyScannerOverlay {
    public static final int COLOR_YELLOW = -2171;
    public static final int COLOR_ORANGE = -50432;
    public static final int COLOR_ORANGE_ACCENT = -43776;
    public static final int COLOR_WARM_ORANGE = -30720;
    public static final LayeredDraw.Layer HUD_OVERLAY;
    public static final LayeredDraw.Layer HUD_SCANNER;

    private static void renderOverlay(GuiGraphics guiGraphics, float partialTick, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;
        if (mc.options.hideGui) {
            return;
        }
        if (!CuriosCompat.isWearingEyePatch((LivingEntity)player)) {
            return;
        }
        TargetScanManager manager = TargetScanManager.getInstance();
        if (!manager.isScanning() && !manager.isCoolingDown()) {
            return;
        }
        Font font = mc.font;
        if (manager.isCoolingDown()) {
            EvilMortyScannerOverlay.renderRechargeIndicator(guiGraphics, font, width, height, manager);
        }
        if (!manager.isScanning()) return;
        EvilMortyScannerOverlay.renderScanHud(guiGraphics, font, partialTick, width, height, mc, player, manager);
    }

    private static void renderRechargeIndicator(GuiGraphics guiGraphics, Font font, int width, int height, TargetScanManager manager) {
        int sec = (manager.getCooldownTimer() + 19) / 20;
        String text = "RECHARGING NEURAL LINK... [" + sec + "s]";
        int textWidth = font.width(text);
        int textX = (width - textWidth) / 2;
        int textY = height - 45;
        guiGraphics.drawString(font, text, textX - 1, textY, -50432, false);
        guiGraphics.drawString(font, text, textX + 1, textY, -50432, false);
        guiGraphics.drawString(font, text, textX, textY - 1, -50432, false);
        guiGraphics.drawString(font, text, textX, textY + 1, -50432, false);
        guiGraphics.drawString(font, text, textX, textY, -2171, false);
    }

    private static void renderScanHud(GuiGraphics guiGraphics, Font font, float partialTick, int width, int height, Minecraft mc, LocalPlayer player, TargetScanManager manager) {
        Entity rawEntity = mc.level != null ? mc.level.getEntity(manager.getActiveTargetId()) : null;
        if (!(rawEntity instanceof LivingEntity)) return;
        LivingEntity target = (LivingEntity)rawEntity;
        if (!target.isAlive()) {
            return;
        }
        double targetYOffset = target instanceof Slime || target instanceof MagmaCube ? target.getBoundingBox().getYsize() * 0.5 : (double)target.getEyeHeight();
        double tx = Mth.lerp((double)partialTick, (double)target.xo, (double)target.getX());
        double ty = Mth.lerp((double)partialTick, (double)target.yo, (double)target.getY()) + targetYOffset;
        double tz = Mth.lerp((double)partialTick, (double)target.zo, (double)target.getZ());
        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 camPos = camera.getPosition();
        double dx = tx - camPos.x;
        double dy = ty - camPos.y;
        double dz = tz - camPos.z;
        Quaternionf invRot = new Quaternionf((Quaternionfc)camera.rotation()).conjugate();
        Vector3f camRel = new Vector3f((float)dx, (float)dy, (float)dz);
        camRel.rotate((Quaternionfc)invRot);
        float forward = -camRel.z();
        boolean onScreen = false;
        float screenX = 0.0f;
        float screenY = 0.0f;
        double scale = 0.0;
        if (forward > 0.2f) {
            double fov = ((Integer)mc.options.fov().get()).intValue();
            double halfHeightTan = Math.tan(Math.toRadians(fov / 2.0));
            scale = (double)height / 2.0 / halfHeightTan;
            screenX = (float)((double)width / 2.0 + (double)(camRel.x() / forward) * scale);
            screenY = (float)((double)height / 2.0 - (double)(camRel.y() / forward) * scale);
            if (screenX >= -100.0f && screenX <= (float)width + 100.0f && screenY >= -100.0f && screenY <= (float)height + 100.0f) {
                onScreen = true;
            }
        }
        if (onScreen) {
            EvilMortyScannerOverlay.renderReticle(guiGraphics, font, screenX, screenY, target, forward, scale, player, manager);
        }
        if (!manager.isAnalysisComplete()) return;
        EvilMortyScannerOverlay.renderPredictionBanner(guiGraphics, font, width, height, manager);
    }

    private static void renderReticle(GuiGraphics guiGraphics, Font font, float screenX, float screenY, LivingEntity target, float forward, double projScale, LocalPlayer player, TargetScanManager manager) {
        float headSize = (float)Math.max(0.45, Math.min(1.2, (double)target.getBbWidth() * 0.85));
        float baseScale = (float)((double)headSize * 1.35 * projScale / ((double)forward * 58.0));
        float distanceScale = Mth.clamp((float)baseScale, (float)0.22f, (float)1.25f);
        float anim = manager.getScanAnimationProgress();
        float animScale = Mth.lerp((float)anim, (float)1.25f, (float)1.0f);
        float reticleScale = distanceScale * animScale;
        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        pose.translate(screenX, screenY, 0.0f);
        pose.scale(reticleScale, reticleScale, 1.0f);
        Matrix4f matrix = pose.last().pose();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        EvilMortyScannerOverlay.renderRickPrimeReticle(matrix);
        pose.popPose();
        float textScale = Mth.clamp((float)distanceScale, (float)0.7f, (float)1.0f);
        Objects.requireNonNull(font);
        float textY = screenY - 36.0f * reticleScale - 9.0f * textScale;
        pose.pushPose();
        pose.translate(screenX, textY, 0.0f);
        pose.scale(textScale, textScale, 1.0f);
        EvilMortyScannerOverlay.renderScanningBrainText(guiGraphics, font, player);
        pose.popPose();
        RenderSystem.enableDepthTest();
    }

    private static void renderRickPrimeReticle(Matrix4f matrix) {
        RadialRenderUtils.drawSectorGradient((Matrix4f)matrix, (float)0.0f, (float)-2.0f, (float)0.0f, (float)36.0f, (float)0.0f, (float)((float)Math.PI * 2), (int)872388864, (int)16726784, (int)24);
        float topApexX = 0.0f;
        float topApexY = -32.0f;
        float topArmLeftX = -12.0f;
        float topArmLeftY = -12.0f;
        float topArmRightX = 12.0f;
        float topArmRightY = -12.0f;
        float blCornerX = -30.0f;
        float blCornerY = 19.0f;
        float blArmUpX = -20.0f;
        float blArmUpY = 2.0f;
        float blArmBaseX = -10.0f;
        float blArmBaseY = 19.0f;
        float brCornerX = 30.0f;
        float brCornerY = 19.0f;
        float brArmUpX = 20.0f;
        float brArmUpY = 2.0f;
        float brArmBaseX = 10.0f;
        float brArmBaseY = 19.0f;
        float yGlowWidth = 4.0f;
        int yGlowColor = 1442809856;
        EvilMortyScannerOverlay.drawLine(matrix, topApexX, topApexY, topArmLeftX, topArmLeftY, yGlowWidth, yGlowColor);
        EvilMortyScannerOverlay.drawLine(matrix, topApexX, topApexY, topArmRightX, topArmRightY, yGlowWidth, yGlowColor);
        EvilMortyScannerOverlay.drawLine(matrix, blCornerX, blCornerY, blArmUpX, blArmUpY, yGlowWidth, yGlowColor);
        EvilMortyScannerOverlay.drawLine(matrix, blCornerX, blCornerY, blArmBaseX, blArmBaseY, yGlowWidth, yGlowColor);
        EvilMortyScannerOverlay.drawLine(matrix, brCornerX, brCornerY, brArmUpX, brArmUpY, yGlowWidth, yGlowColor);
        EvilMortyScannerOverlay.drawLine(matrix, brCornerX, brCornerY, brArmBaseX, brArmBaseY, yGlowWidth, yGlowColor);
        float yCoreWidth = 2.0f;
        int yCoreColor = -2171;
        EvilMortyScannerOverlay.drawLine(matrix, topApexX, topApexY, topArmLeftX, topArmLeftY, yCoreWidth, yCoreColor);
        EvilMortyScannerOverlay.drawLine(matrix, topApexX, topApexY, topArmRightX, topArmRightY, yCoreWidth, yCoreColor);
        EvilMortyScannerOverlay.drawLine(matrix, blCornerX, blCornerY, blArmUpX, blArmUpY, yCoreWidth, yCoreColor);
        EvilMortyScannerOverlay.drawLine(matrix, blCornerX, blCornerY, blArmBaseX, blArmBaseY, yCoreWidth, yCoreColor);
        EvilMortyScannerOverlay.drawLine(matrix, brCornerX, brCornerY, brArmUpX, brArmUpY, yCoreWidth, yCoreColor);
        EvilMortyScannerOverlay.drawLine(matrix, brCornerX, brCornerY, brArmBaseX, brArmBaseY, yCoreWidth, yCoreColor);
        float redGlowWidth = 5.5f;
        float redCoreWidth = 3.6f;
        int rGlowColor = 1728002816;
        int rCoreColor = -50432;
        EvilMortyScannerOverlay.drawLine(matrix, -13.0f, -22.0f, -33.0f, 13.0f, redGlowWidth, rGlowColor);
        EvilMortyScannerOverlay.drawLine(matrix, -13.0f, -22.0f, -33.0f, 13.0f, redCoreWidth, rCoreColor);
        EvilMortyScannerOverlay.drawLine(matrix, -21.0f, -6.0f, -30.0f, -12.0f, redGlowWidth, rGlowColor);
        EvilMortyScannerOverlay.drawLine(matrix, -21.0f, -6.0f, -30.0f, -12.0f, 3.0f, rCoreColor);
        EvilMortyScannerOverlay.drawLine(matrix, 13.0f, -22.0f, 33.0f, 13.0f, redGlowWidth, rGlowColor);
        EvilMortyScannerOverlay.drawLine(matrix, 13.0f, -22.0f, 33.0f, 13.0f, redCoreWidth, rCoreColor);
        EvilMortyScannerOverlay.drawLine(matrix, 21.0f, -6.0f, 30.0f, -12.0f, redGlowWidth, rGlowColor);
        EvilMortyScannerOverlay.drawLine(matrix, 21.0f, -6.0f, 30.0f, -12.0f, 3.0f, rCoreColor);
        EvilMortyScannerOverlay.drawLine(matrix, -21.0f, 26.0f, 21.0f, 26.0f, redGlowWidth, rGlowColor);
        EvilMortyScannerOverlay.drawLine(matrix, -21.0f, 26.0f, 21.0f, 26.0f, 4.2f, rCoreColor);
        EvilMortyScannerOverlay.drawLine(matrix, 0.0f, 26.0f, 0.0f, 36.0f, redGlowWidth, rGlowColor);
        EvilMortyScannerOverlay.drawLine(matrix, 0.0f, 26.0f, 0.0f, 36.0f, 3.6f, rCoreColor);
    }

    private static void renderScanningBrainText(GuiGraphics guiGraphics, Font font, LocalPlayer player) {
        int dotCount = player.tickCount / 5 % 4;
        String dots = switch (dotCount) {
            case 1 -> ".";
            case 2 -> "..";
            case 3 -> "...";
            default -> "";
        };
        String scanText = "SCANNING BRAIN" + dots;
        int textW = font.width(scanText);
        int textX = -textW / 2;
        int textY = 0;
        guiGraphics.drawString(font, scanText, textX - 1, textY, -50432, false);
        guiGraphics.drawString(font, scanText, textX + 1, textY, -50432, false);
        guiGraphics.drawString(font, scanText, textX, textY - 1, -50432, false);
        guiGraphics.drawString(font, scanText, textX, textY + 1, -50432, false);
        guiGraphics.drawString(font, scanText, textX, textY, -2171, false);
    }

    private static void renderPredictionBanner(GuiGraphics guiGraphics, Font font, int width, int height, TargetScanManager manager) {
        String predText = manager.getPredictionText();
        if (predText == null || predText.trim().isEmpty()) {
            predText = "ANALYZING...";
        }
        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        pose.translate((float)width / 2.0f, (float)height - 70.0f, 0.0f);
        float bannerScale = 2.2f;
        pose.scale(bannerScale, bannerScale, 1.0f);
        int textW = font.width(predText);
        int drawX = -textW / 2;
        Objects.requireNonNull(font);
        int drawY = -9 / 2;
        guiGraphics.drawString(font, predText, drawX - 1, drawY - 1, -50432, false);
        guiGraphics.drawString(font, predText, drawX + 1, drawY - 1, -50432, false);
        guiGraphics.drawString(font, predText, drawX - 1, drawY + 1, -50432, false);
        guiGraphics.drawString(font, predText, drawX + 1, drawY + 1, -50432, false);
        guiGraphics.drawString(font, predText, drawX - 1, drawY, -50432, false);
        guiGraphics.drawString(font, predText, drawX + 1, drawY, -50432, false);
        guiGraphics.drawString(font, predText, drawX, drawY - 1, -50432, false);
        guiGraphics.drawString(font, predText, drawX, drawY + 1, -50432, false);
        guiGraphics.drawString(font, predText, drawX, drawY, -2171, false);
        pose.popPose();
    }

    private static void drawLine(Matrix4f matrix, float x1, float y1, float x2, float y2, float width, int argb) {
        float a = (float)(argb >> 24 & 0xFF) / 255.0f;
        float r = (float)(argb >> 16 & 0xFF) / 255.0f;
        float g = (float)(argb >> 8 & 0xFF) / 255.0f;
        float b = (float)(argb & 0xFF) / 255.0f;
        if (a <= 0.0f) return;
        if (width <= 0.0f) {
            return;
        }
        float dx = x2 - x1;
        float dy = y2 - y1;
        float len = (float)Math.sqrt(dx * dx + dy * dy);
        if (len < 1.0E-4f) {
            return;
        }
        float nx = -dy / len * (width / 2.0f);
        float ny = dx / len * (width / 2.0f);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        buffer.addVertex(matrix, x1 + nx, y1 + ny, 0.0f).setColor(r, g, b, a);
        buffer.addVertex(matrix, x2 + nx, y2 + ny, 0.0f).setColor(r, g, b, a);
        buffer.addVertex(matrix, x2 - nx, y2 - ny, 0.0f).setColor(r, g, b, a);
        buffer.addVertex(matrix, x1 - nx, y1 - ny, 0.0f).setColor(r, g, b, a);
        BufferUploader.drawWithShader((MeshData)buffer.buildOrThrow());
    }

    static {
        HUD_SCANNER = HUD_OVERLAY = (guiGraphics, deltaTracker) -> EvilMortyScannerOverlay.renderOverlay(guiGraphics, deltaTracker.getGameTimeDeltaPartialTick(false), guiGraphics.guiWidth(), guiGraphics.guiHeight());
    }
}

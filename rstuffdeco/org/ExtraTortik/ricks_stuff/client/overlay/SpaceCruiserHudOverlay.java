/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.math.Axis
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.LayeredDraw$Layer
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  org.ExtraTortik.ricks_stuff.Config
 *  org.ExtraTortik.ricks_stuff.client.overlay.SpaceCruiserHudOverlay$1
 *  org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity
 *  org.ExtraTortik.ricks_stuff.entity.WarpState
 */
package org.ExtraTortik.ricks_stuff.client.overlay;

import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.ExtraTortik.ricks_stuff.Config;
import org.ExtraTortik.ricks_stuff.client.overlay.SpaceCruiserHudOverlay;
import org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity;
import org.ExtraTortik.ricks_stuff.entity.WarpState;

public class SpaceCruiserHudOverlay {
    public static final LayeredDraw.Layer HUD_OVERLAY = (guiGraphics, deltaTracker) -> {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;
        if (mc.options.hideGui) {
            return;
        }
        Entity patt0$temp = player.getVehicle();
        if (!(patt0$temp instanceof SpaceCruiserEntity)) return;
        SpaceCruiserEntity ship = (SpaceCruiserEntity)patt0$temp;
        Font font = mc.font;
        boolean isPilot = ship.getControllingPassenger() == player;
        float partialTick = deltaTracker.getGameTimeDeltaPartialTick(true);
        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();
        SpaceCruiserHudOverlay.renderEmergencyAlerts(guiGraphics, font, width, height, ship, partialTick);
        SpaceCruiserHudOverlay.renderWarpAlerts(guiGraphics, font, width, height, ship, partialTick);
        if (isPilot && mc.options.getCameraType().isFirstPerson()) {
            SpaceCruiserHudOverlay.renderWindshieldReticle(guiGraphics, width, height, ship, partialTick);
        }
        SpaceCruiserHudOverlay.renderCruiserDashboard(guiGraphics, font, width, height, ship, isPilot, partialTick);
    };

    private static void renderEmergencyAlerts(GuiGraphics guiGraphics, Font font, int width, int height, SpaceCruiserEntity ship, float partialTick) {
        boolean isCritical;
        boolean bl = isCritical = ship.isCrashing() || ship.isBroken() || ship.getHealth() <= 0.0f;
        if (!isCritical) {
            return;
        }
        float pulse = (float)Math.pow(Math.max(0.0, Math.sin(((float)ship.tickCount + partialTick) * 0.35f)), 2.0);
        int vAlpha = (int)(pulse * (float)(ship.isCrashing() ? 100 : (ship.isBroken() ? 60 : 40)));
        if (vAlpha > 0) {
            int topColor = vAlpha << 24 | 0xDD0000;
            guiGraphics.fillGradient(0, 0, width, height / 4, topColor, 0xDD0000);
            guiGraphics.fillGradient(0, height * 3 / 4, width, height, 0xDD0000, topColor);
        }
        if (ship.isCrashing()) {
            String warnTitle = "\u00a7c\u00a7l[ ! ] CRITICAL FAILURE: ENGINE ENGULFED [ ! ]";
            String warnSub = "\u00a7e\u00a7lPULL UP - TERRAIN PROXIMITY";
            int tw = font.width(warnTitle);
            int sw = font.width(warnSub);
            int bannerY = height / 5;
            guiGraphics.fill(width / 2 - tw / 2 - 8, bannerY - 4, width / 2 + tw / 2 + 8, bannerY + 24, -870841854);
            guiGraphics.fill(width / 2 - tw / 2 - 8, bannerY - 4, width / 2 + tw / 2 + 8, bannerY - 3, -56798);
            guiGraphics.fill(width / 2 - tw / 2 - 8, bannerY + 23, width / 2 + tw / 2 + 8, bannerY + 24, -56798);
            guiGraphics.drawString(font, warnTitle, width / 2 - tw / 2, bannerY, 0xFF5555, false);
            guiGraphics.drawString(font, warnSub, width / 2 - sw / 2, bannerY + 12, 0xFFFFAA, false);
        } else {
            if (!ship.isBroken()) return;
            String warnTitle = "\u00a7c\u00a7l[ ! ] SYSTEM DISABLED: REPAIRS REQUIRED [ ! ]";
            String warnSub = "\u00a77Shift + Right-Click with Iron Ingot to repair hull";
            int tw = font.width(warnTitle);
            int sw = font.width(warnSub);
            int bannerY = height / 5;
            guiGraphics.fill(width / 2 - tw / 2 - 8, bannerY - 4, width / 2 + tw / 2 + 8, bannerY + 24, -870841854);
            guiGraphics.fill(width / 2 - tw / 2 - 8, bannerY - 4, width / 2 + tw / 2 + 8, bannerY - 3, -43691);
            guiGraphics.fill(width / 2 - tw / 2 - 8, bannerY + 23, width / 2 + tw / 2 + 8, bannerY + 24, -43691);
            guiGraphics.drawString(font, warnTitle, width / 2 - tw / 2, bannerY, 0xFF5555, false);
            guiGraphics.drawString(font, warnSub, width / 2 - sw / 2, bannerY + 12, 0xCCCCCC, false);
        }
    }

    private static void renderWarpAlerts(GuiGraphics guiGraphics, Font font, int width, int height, SpaceCruiserEntity ship, float partialTick) {
        int subColor;
        int titleColor;
        int bgColor;
        int borderColor;
        Object title;
        WarpState warpState = ship.getWarpStateEnum();
        if (warpState == WarpState.IDLE) {
            return;
        }
        int timer = ship.getWarpTimer();
        int duration = Math.max(1, warpState.getDurationTicks());
        float progress = Math.min(1.0f, (float)timer / (float)duration);
        String sub = "";
        boolean drawProgressBar = false;
        float pulse = (float)(0.5 + 0.5 * Math.sin(((float)ship.tickCount + partialTick) * 0.45f));
        switch (1.$SwitchMap$org$ExtraTortik$ricks_stuff$entity$WarpState[warpState.ordinal()]) {
            case 1: {
                int pct = Math.min(100, (int)(progress * 100.0f));
                title = String.format("\u00a76\u00a7l[ WARP CHARGING: %02d%% ]", pct);
                sub = "\u00a7e\u00a7l[ ANY CONTROL INPUT ABORTS ]";
                int alpha = (int)(190.0f + pulse * 55.0f);
                borderColor = alpha << 24 | 0xFFAA00;
                bgColor = alpha << 24 | 0x332000;
                titleColor = 0xFFAA00;
                subColor = 0xFFFF55;
                drawProgressBar = true;
                int vAlpha = (int)(pulse * 55.0f);
                int edgeColor = vAlpha << 24 | 0xFFAA00;
                guiGraphics.fillGradient(0, 0, width, height / 5, edgeColor, 0xFFAA00);
                guiGraphics.fillGradient(0, height * 4 / 5, width, height, 0xFFAA00, edgeColor);
                break;
            }
            case 2: {
                title = "\u00a7a\u00a7l[ REALITY FRACTURE DETECTED // PREPARE FOR ENTRY ]";
                sub = "\u00a72DIMENSIONAL RIFT OPENING // ALIGNING VECTOR";
                borderColor = -16711834;
                bgColor = -587193848;
                titleColor = 3728734;
                subColor = 0x88FFAA;
                int vAlpha = (int)(30.0f + pulse * 25.0f);
                int edgeColor = vAlpha << 24 | 0xFF66;
                guiGraphics.fillGradient(0, 0, width, height / 6, edgeColor, 65382);
                guiGraphics.fillGradient(0, height * 5 / 6, width, height, 65382, edgeColor);
                break;
            }
            case 3: {
                title = "\u00a7b\u00a7l[ ENGAGING RIFT // HYPERDRIVE 100% ]";
                sub = "\u00a73EVENT HORIZON CONTACT // SPACETIME DISPLACEMENT";
                borderColor = -16718337;
                bgColor = -587195344;
                titleColor = 58879;
                subColor = 8448767;
                int vAlpha = (int)(40.0f + pulse * 35.0f);
                int edgeColor = vAlpha << 24 | 0xE5FF;
                guiGraphics.fillGradient(0, 0, width, height / 5, edgeColor, 58879);
                guiGraphics.fillGradient(0, height * 4 / 5, width, height, 58879, edgeColor);
                break;
            }
            case 4: {
                String dim = SpaceCruiserHudOverlay.formatDimensionName(ship.getTargetDimension());
                int tx = (int)ship.getTargetX();
                int ty = (int)ship.getTargetY();
                int tz = (int)ship.getTargetZ();
                title = "\u00a7d\u00a7l[ MULTIVERSE TRANSIT // DEST: " + dim + " ]";
                sub = String.format("\u00a75COORDINATES: \u00a7f[%d, %d, %d] \u00a78// \u00a7dDRIFT NOMINAL", tx, ty, tz);
                borderColor = -2545921;
                bgColor = -584841674;
                titleColor = 14231295;
                subColor = 0xFFAAFF;
                int vAlpha = (int)(35.0f + pulse * 30.0f);
                int edgeColor = vAlpha << 24 | 0x9900FF;
                guiGraphics.fillGradient(0, 0, width, height / 5, edgeColor, 0x9900FF);
                guiGraphics.fillGradient(0, height * 4 / 5, width, height, 0x9900FF, edgeColor);
                break;
            }
            case 5: {
                title = "\u00a7a\u00a7l[ REALITY BREACH // DESTINATION REACHED ]";
                sub = "\u00a72DECELERATING TO SUBLIGHT // RE-ENTERING NORMAL SPACE";
                borderColor = -13638053;
                bgColor = -586932210;
                titleColor = 3139163;
                subColor = 0xAAFFAA;
                int vAlpha = (int)(45.0f + pulse * 35.0f);
                int edgeColor = vAlpha << 24 | 0x2FE65B;
                guiGraphics.fillGradient(0, 0, width, height / 5, edgeColor, 3139163);
                guiGraphics.fillGradient(0, height * 4 / 5, width, height, 3139163, edgeColor);
                break;
            }
            default: {
                return;
            }
        }
        int tw = font.width((String)title);
        int sw = font.width(sub);
        int boxW = Math.max(tw, sw) + 24;
        int boxH = sub.isEmpty() ? 18 : (drawProgressBar ? 32 : 26);
        int bannerY = height / 6;
        int bannerX = (width - boxW) / 2;
        guiGraphics.fill(bannerX, bannerY, bannerX + boxW, bannerY + boxH, bgColor);
        guiGraphics.fill(bannerX, bannerY, bannerX + boxW, bannerY + 1, borderColor);
        guiGraphics.fill(bannerX, bannerY + boxH - 1, bannerX + boxW, bannerY + boxH, borderColor);
        guiGraphics.fill(bannerX, bannerY, bannerX + 1, bannerY + boxH, borderColor);
        guiGraphics.fill(bannerX + boxW - 1, bannerY, bannerX + boxW, bannerY + boxH, borderColor);
        int cSize = 3;
        guiGraphics.fill(bannerX, bannerY, bannerX + cSize, bannerY + cSize, -1);
        guiGraphics.fill(bannerX + boxW - cSize, bannerY, bannerX + boxW, bannerY + cSize, -1);
        guiGraphics.fill(bannerX, bannerY + boxH - cSize, bannerX + cSize, bannerY + boxH, -1);
        guiGraphics.fill(bannerX + boxW - cSize, bannerY + boxH - cSize, bannerX + boxW, bannerY + boxH, -1);
        guiGraphics.drawString(font, (String)title, (width - tw) / 2, bannerY + 4, titleColor, false);
        if (!sub.isEmpty()) {
            guiGraphics.drawString(font, sub, (width - sw) / 2, bannerY + 14, subColor, false);
        }
        if (!drawProgressBar) return;
        int pBarW = boxW - 20;
        int pBarH = 4;
        int pBarX = bannerX + 10;
        int pBarY = bannerY + 24;
        guiGraphics.fill(pBarX, pBarY, pBarX + pBarW, pBarY + pBarH, -15068672);
        int fillW = (int)((float)pBarW * progress);
        guiGraphics.fill(pBarX, pBarY, pBarX + fillW, pBarY + pBarH, -22016);
        guiGraphics.fill(pBarX, pBarY, pBarX + fillW, pBarY + 1, -3926);
    }

    private static String formatDimensionName(String dimId) {
        if (dimId == null) return "UNKNOWN SECTOR";
        if (dimId.isEmpty()) {
            return "UNKNOWN SECTOR";
        }
        if (!dimId.contains(":")) return dimId.replace('_', ' ').toUpperCase();
        dimId = dimId.substring(dimId.indexOf(58) + 1);
        return dimId.replace('_', ' ').toUpperCase();
    }

    private static void renderWindshieldReticle(GuiGraphics guiGraphics, int width, int height, SpaceCruiserEntity ship, float partialTick) {
        int cx = width / 2;
        int cy = height / 2;
        float roll = ship.getInterpolatedRoll(partialTick);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate((float)cx, (float)cy, 0.0f);
        guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(-roll));
        WarpState warpState = ship.getWarpStateEnum();
        boolean isWarping = warpState.isWarping();
        int reticleColor = -2009537186;
        if (isWarping) {
            reticleColor = switch (1.$SwitchMap$org$ExtraTortik$ricks_stuff$entity$WarpState[warpState.ordinal()]) {
                case 1 -> -855660032;
                case 2 -> -872349850;
                case 3 -> -872356353;
                case 4 -> -858183937;
                case 5 -> -869276069;
                default -> -2009537186;
            };
        } else if (ship.isCrashing() || ship.isEngineOverloaded()) {
            reticleColor = -1426118102;
        } else if (ship.isBoosting()) {
            reticleColor = -1442781697;
        }
        guiGraphics.fill(-2, -1, 3, 0, reticleColor);
        guiGraphics.fill(-1, -2, 0, 3, reticleColor);
        guiGraphics.fill(1, -2, 2, 3, reticleColor);
        guiGraphics.fill(0, 0, 1, 1, 0);
        guiGraphics.fill(-22, -1, -8, 0, reticleColor);
        guiGraphics.fill(-22, 0, -21, 4, reticleColor);
        guiGraphics.fill(-14, 0, -13, 2, reticleColor);
        guiGraphics.fill(9, -1, 23, 0, reticleColor);
        guiGraphics.fill(22, 0, 23, 4, reticleColor);
        guiGraphics.fill(14, 0, 15, 2, reticleColor);
        guiGraphics.fill(-5, -12, 6, -11, reticleColor & 0xFFFFFF | 0x44000000);
        guiGraphics.fill(-5, 11, 6, 12, reticleColor & 0xFFFFFF | 0x44000000);
        if (ship.isBoosting() && !isWarping) {
            float pulse = (float)Math.sin(((float)ship.tickCount + partialTick) * 0.5f);
            int spread = (int)(26.0f + pulse * 2.0f);
            int boostColor = -858183937;
            guiGraphics.fill(-spread - 3, -4, -spread - 2, 5, boostColor);
            guiGraphics.fill(-spread - 1, -2, -spread, 3, boostColor);
            guiGraphics.fill(spread + 2, -4, spread + 3, 5, boostColor);
            guiGraphics.fill(spread, -2, spread + 1, 3, boostColor);
        }
        if (isWarping) {
            float time = (float)ship.tickCount + partialTick;
            float pulse = (float)(0.5 + 0.5 * Math.sin(time * 0.4f));
            float progress = (float)ship.getWarpTimer() / (float)Math.max(1, warpState.getDurationTicks());
            int bracketRadius = switch (1.$SwitchMap$org$ExtraTortik$ricks_stuff$entity$WarpState[warpState.ordinal()]) {
                case 1 -> (int)Mth.lerp((float)progress, (float)34.0f, (float)18.0f);
                case 2 -> (int)(20.0f + pulse * 3.0f);
                case 3 -> (int)Mth.lerp((float)progress, (float)20.0f, (float)12.0f);
                case 4 -> (int)(16.0 + Math.sin(time * 0.8f) * 4.0);
                case 5 -> (int)Mth.lerp((float)progress, (float)12.0f, (float)28.0f);
                default -> 24;
            };
            int bracketColor = reticleColor;
            guiGraphics.fill(-bracketRadius - 4, -bracketRadius - 4, -bracketRadius + 4, -bracketRadius - 3, bracketColor);
            guiGraphics.fill(-bracketRadius - 4, -bracketRadius - 4, -bracketRadius - 3, -bracketRadius + 4, bracketColor);
            guiGraphics.fill(bracketRadius - 4, -bracketRadius - 4, bracketRadius + 4, -bracketRadius - 3, bracketColor);
            guiGraphics.fill(bracketRadius + 3, -bracketRadius - 4, bracketRadius + 4, -bracketRadius + 4, bracketColor);
            guiGraphics.fill(-bracketRadius - 4, bracketRadius + 3, -bracketRadius + 4, bracketRadius + 4, bracketColor);
            guiGraphics.fill(-bracketRadius - 4, bracketRadius - 4, -bracketRadius - 3, bracketRadius + 4, bracketColor);
            guiGraphics.fill(bracketRadius - 4, bracketRadius + 3, bracketRadius + 4, bracketRadius + 4, bracketColor);
            guiGraphics.fill(bracketRadius + 3, bracketRadius - 4, bracketRadius + 4, bracketRadius + 4, bracketColor);
            guiGraphics.pose().pushPose();
            float rotAngle = time * (warpState == WarpState.TUNNEL_TRANSIT ? 8.0f : 4.0f);
            guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(rotAngle));
            int dColor = bracketColor & 0xFFFFFF | 0x88000000;
            guiGraphics.fill(-5, -1, -4, 1, dColor);
            guiGraphics.fill(4, -1, 5, 1, dColor);
            guiGraphics.fill(-1, -5, 1, -4, dColor);
            guiGraphics.fill(-1, 4, 1, 5, dColor);
            guiGraphics.pose().popPose();
        }
        guiGraphics.pose().popPose();
    }

    private static void renderCruiserDashboard(GuiGraphics guiGraphics, Font font, int width, int height, SpaceCruiserEntity ship, boolean isPilot, float partialTick) {
        int modeBg;
        String modeTxt;
        int engBg;
        boolean engineOn;
        int x;
        int boxW = 204;
        int boxH = 58;
        String anchor = Config.hudAnchor != null ? Config.hudAnchor.trim().toUpperCase() : "BOTTOM_LEFT";
        int offX = Config.hudOffsetX;
        int offY = Config.hudOffsetY;
        int y = switch (anchor) {
            case "TOP_LEFT" -> {
                x = offX;
                yield offY;
            }
            case "TOP_RIGHT" -> {
                x = width - boxW - offX;
                yield offY;
            }
            case "BOTTOM_RIGHT" -> {
                x = width - boxW - offX;
                yield height - boxH - offY;
            }
            case "BOTTOM_LEFT" -> {
                x = offX;
                yield height - boxH - offY;
            }
            default -> {
                x = offX;
                yield height - boxH - offY;
            }
        };
        float scale = (float)Config.hudScale;
        boolean scaled = Math.abs(scale - 1.0f) > 0.01f;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0f, 0.0f, 400.0f);
        if (scaled) {
            guiGraphics.pose().translate((float)x, (float)y, 0.0f);
            guiGraphics.pose().scale(scale, scale, 1.0f);
            guiGraphics.pose().translate((float)(-x), (float)(-y), 0.0f);
        }
        guiGraphics.fill(x - 1, y - 1, x + boxW + 1, y + boxH + 1, -16250356);
        guiGraphics.fill(x, y, x + boxW, y + boxH, -15460581);
        guiGraphics.fill(x, y, x + boxW, y + 1, -13222841);
        guiGraphics.fill(x, y, x + 1, y + boxH, -13222841);
        guiGraphics.fill(x, y + boxH - 1, x + boxW, y + boxH, -16119026);
        guiGraphics.fill(x + boxW - 1, y, x + boxW, y + boxH, -16119026);
        SpaceCruiserHudOverlay.renderRivet(guiGraphics, x + 2, y + 2);
        SpaceCruiserHudOverlay.renderRivet(guiGraphics, x + boxW - 5, y + 2);
        SpaceCruiserHudOverlay.renderRivet(guiGraphics, x + 2, y + boxH - 5);
        SpaceCruiserHudOverlay.renderRivet(guiGraphics, x + boxW - 5, y + boxH - 5);
        SpaceCruiserHudOverlay.renderDuctTapePatch(guiGraphics, x + boxW - 46, y - 1, 28, 7);
        String roleText = isPilot ? "\u00a7aPILOT" : "\u00a77PASSENGER";
        guiGraphics.drawString(font, "\u00a78[\u00a72CRUISER MK-III\u00a78] " + roleText, x + 7, y + 3, 0xFFFFFF, false);
        double speedBlocksPerSec = ship.getDeltaMovement().length() * 20.0;
        int speedKmh = (int)(speedBlocksPerSec * 3.6);
        int alt = (int)ship.getY();
        float healthPct = ship.getHealth() / 100.0f;
        float energyPct = ship.getEnergy() / 100.0f;
        int bay1X = x + 5;
        int bay1Y = y + 13;
        int bay1W = 50;
        int bay1H = 40;
        guiGraphics.fill(bay1X, bay1Y, bay1X + bay1W, bay1Y + bay1H, -16511721);
        guiGraphics.fill(bay1X, bay1Y, bay1X + bay1W, bay1Y + 1, -16113096);
        guiGraphics.fill(bay1X, bay1Y, bay1X + 1, bay1Y + bay1H, -16113096);
        guiGraphics.fill(bay1X + bay1W - 1, bay1Y, bay1X + bay1W, bay1Y + bay1H, -16113096);
        guiGraphics.fill(bay1X, bay1Y + bay1H - 1, bay1X + bay1W, bay1Y + bay1H, -16113096);
        int nrgPct = (int)(energyPct * 100.0f);
        guiGraphics.drawString(font, "\u00a73PWR", bay1X + 3, bay1Y + 3, 58879, false);
        guiGraphics.drawString(font, String.format("\u00a7b%d%%", nrgPct), bay1X + bay1W - font.width(nrgPct + "%") - 3, bay1Y + 3, 0xFFFFFF, false);
        int numNrgSegments = 6;
        int segW = 6;
        int segH = 14;
        int segGap = 1;
        int segStartX = bay1X + 4;
        int segStartY = bay1Y + 14;
        int activeNrgSegments = Math.round(energyPct * (float)numNrgSegments);
        for (int i = 0; i < numNrgSegments; ++i) {
            boolean active;
            int sx = segStartX + i * (segW + segGap);
            boolean bl = active = i < activeNrgSegments;
            if (active) {
                int segColor = energyPct < 0.2f && ship.tickCount % 10 < 5 ? -22016 : -16723201;
                guiGraphics.fill(sx, segStartY, sx + segW, segStartY + segH, segColor);
                guiGraphics.fill(sx + 1, segStartY + 1, sx + segW - 1, segStartY + 3, -8328449);
                continue;
            }
            guiGraphics.fill(sx, segStartY, sx + segW, segStartY + segH, -16377820);
        }
        String nrgStatus = ship.isBoosting() ? "\u00a7d[BOOST]" : (energyPct < 0.2f ? "\u00a7e[LOW]" : "\u00a73CHARGE");
        guiGraphics.drawString(font, nrgStatus, bay1X + 3, bay1Y + 30, 0x88AACC, false);
        int bay2X = x + 58;
        int bay2Y = y + 13;
        int bay2W = 86;
        int bay2H = 40;
        guiGraphics.fill(bay2X, bay2Y, bay2X + bay2W, bay2Y + bay2H, -16576762);
        guiGraphics.fill(bay2X, bay2Y, bay2X + bay2W, bay2Y + 1, -15912168);
        guiGraphics.fill(bay2X, bay2Y, bay2X + 1, bay2Y + bay2H, -15912168);
        guiGraphics.fill(bay2X + bay2W - 1, bay2Y, bay2X + bay2W, bay2Y + bay2H, -15912168);
        guiGraphics.fill(bay2X, bay2Y + bay2H - 1, bay2X + bay2W, bay2Y + bay2H, -15912168);
        for (int sl = bay2Y + 2; sl < bay2Y + bay2H - 1; sl += 2) {
            guiGraphics.fill(bay2X + 1, sl, bay2X + bay2W - 1, sl + 1, -16377844);
        }
        guiGraphics.drawString(font, "\u00a72SPD \u00a7a" + speedKmh + " \u00a77km/h", bay2X + 4, bay2Y + 3, 3728734, false);
        guiGraphics.drawString(font, "\u00a72ALT \u00a7a" + alt + " \u00a77m", bay2X + 4, bay2Y + 12, 3728734, false);
        float yaw = (ship.getYRot() % 360.0f + 360.0f) % 360.0f;
        String dirStr = SpaceCruiserHudOverlay.getDirectionHeading(yaw);
        guiGraphics.drawString(font, String.format("\u00a72HDG \u00a7a%03d\u00b0 \u00a77%s", (int)yaw, dirStr), bay2X + 4, bay2Y + 21, 3728734, false);
        int hY = bay2Y + 33;
        guiGraphics.fill(bay2X + 4, hY, bay2X + bay2W - 4, hY + 1, -15844068);
        int centerBlip = bay2X + bay2W / 2;
        int bankOffset = Mth.clamp((int)((int)(ship.getRoll() * 0.4f)), (int)-12, (int)12);
        guiGraphics.fill(centerBlip + bankOffset - 3, hY - 1, centerBlip + bankOffset + 3, hY + 2, -13048482);
        int bay3X = x + 147;
        int bay3Y = y + 13;
        int bay3W = 52;
        int bay3H = 40;
        guiGraphics.fill(bay3X, bay3Y, bay3X + bay3W, bay3Y + bay3H, -15855340);
        guiGraphics.fill(bay3X, bay3Y, bay3X + bay3W, bay3Y + 1, -14538704);
        guiGraphics.fill(bay3X, bay3Y, bay3X + 1, bay3Y + bay3H, -14538704);
        guiGraphics.fill(bay3X + bay3W - 1, bay3Y, bay3X + bay3W, bay3Y + bay3H, -14538704);
        guiGraphics.fill(bay3X, bay3Y + bay3H - 1, bay3X + bay3W, bay3Y + bay3H, -14538704);
        int hullPct = (int)(healthPct * 100.0f);
        guiGraphics.drawString(font, "\u00a7cHULL", bay3X + 3, bay3Y + 3, 0xFF5555, false);
        guiGraphics.drawString(font, String.format("\u00a7f%d%%", hullPct), bay3X + bay3W - font.width(hullPct + "%") - 3, bay3Y + 3, 0xFFFFFF, false);
        int numHullSegments = 6;
        int hSegW = 6;
        int hSegH = 6;
        int hSegGap = 1;
        int hSegStartX = bay3X + 4;
        int hSegStartY = bay3Y + 13;
        int activeHullSegments = Math.round(healthPct * (float)numHullSegments);
        for (int i = 0; i < numHullSegments; ++i) {
            boolean active;
            int sx = hSegStartX + i * (hSegW + hSegGap);
            boolean bl = active = i < activeHullSegments;
            if (active) {
                int hullColor;
                int n = healthPct > 0.5f ? -13638053 : (hullColor = healthPct > 0.25f ? -22016 : -54742);
                if (healthPct <= 0.25f && ship.tickCount % 6 < 3) {
                    hullColor = -39322;
                }
                guiGraphics.fill(sx, hSegStartY, sx + hSegW, hSegStartY + hSegH, hullColor);
                continue;
            }
            guiGraphics.fill(sx, hSegStartY, sx + hSegW, hSegStartY + hSegH, -14415350);
        }
        int chipY = bay3Y + 22;
        boolean engineStall = ship.isEngineOverloaded();
        boolean bl = engineOn = ship.isEngineOn() && !ship.isBroken() && !ship.isCrashing();
        int n = engineStall ? (ship.tickCount % 6 < 3 ? -855694814 : 0x66440000) : (engBg = engineOn ? -1442039788 : -1441260768);
        String engTxt = engineStall ? "\u00a7fSTL" : (engineOn ? "\u00a7aENG" : "\u00a78OFF");
        SpaceCruiserHudOverlay.renderAnnunciatorChip(guiGraphics, font, bay3X + 3, chipY, 21, 7, engBg, engTxt);
        if (ship.isWarpActive()) {
            modeTxt = "\u00a7dWRP";
            modeBg = -1437987232;
        } else if (ship.isCrashing()) {
            modeTxt = "\u00a7cDVE";
            modeBg = -1437270016;
        } else if (ship.isLanded()) {
            modeTxt = "\u00a76LND";
            modeBg = -1438832632;
        } else if (ship.isHoverLock()) {
            modeTxt = "\u00a79HVR";
            modeBg = -1442044856;
        } else {
            modeTxt = "\u00a7bFLT";
            modeBg = -1442305992;
        }
        SpaceCruiserHudOverlay.renderAnnunciatorChip(guiGraphics, font, bay3X + 27, chipY, 22, 7, modeBg, modeTxt);
        int chip2Y = bay3Y + 31;
        boolean lightsOn = ship.isHeadlightsOn();
        int lgtBg = lightsOn ? -1438631928 : -1441260768;
        String lgtTxt = lightsOn ? "\u00a7eLGT" : "\u00a78LGT";
        SpaceCruiserHudOverlay.renderAnnunciatorChip(guiGraphics, font, bay3X + 3, chip2Y, 21, 7, lgtBg, lgtTxt);
        boolean boosting = ship.isBoosting();
        int bstBg = boosting ? -858183937 : -1441260768;
        String bstTxt = boosting ? "\u00a7fBST" : "\u00a78BST";
        SpaceCruiserHudOverlay.renderAnnunciatorChip(guiGraphics, font, bay3X + 27, chip2Y, 22, 7, bstBg, bstTxt);
        guiGraphics.pose().popPose();
    }

    private static void renderAnnunciatorChip(GuiGraphics guiGraphics, Font font, int cx, int cy, int cw, int ch, int bgColor, String text) {
        guiGraphics.fill(cx, cy, cx + cw, cy + ch, bgColor);
        guiGraphics.fill(cx, cy, cx + cw, cy + 1, 0x44FFFFFF);
        guiGraphics.fill(cx, cy, cx + 1, cy + ch, 0x44FFFFFF);
        guiGraphics.fill(cx, cy + ch - 1, cx + cw, cy + ch, 0x44000000);
        guiGraphics.fill(cx + cw - 1, cy, cx + cw, cy + ch, 0x44000000);
        int tw = font.width(text);
        int tx = cx + (cw - tw) / 2;
        int ty = cy + (ch - 8) / 2;
        guiGraphics.drawString(font, text, tx, ty, 0xFFFFFF, false);
    }

    private static void renderDuctTapePatch(GuiGraphics guiGraphics, int tx, int ty, int tw, int th) {
        guiGraphics.fill(tx, ty, tx + tw, ty + th, -9604227);
        guiGraphics.fill(tx, ty, tx + tw, ty + 1, -7695714);
        guiGraphics.fill(tx, ty + th - 1, tx + tw, ty + th, -11841189);
        int i = 3;
        while (i < tw - 3) {
            guiGraphics.fill(tx + i, ty + 1, tx + i + 1, ty + th - 1, -10985623);
            guiGraphics.fill(tx + i + 1, ty + 1, tx + i + 2, ty + th - 1, -8551023);
            i += 6;
        }
    }

    private static void renderRivet(GuiGraphics guiGraphics, int rx, int ry) {
        guiGraphics.fill(rx, ry, rx + 3, ry + 3, -16118770);
        guiGraphics.fill(rx + 1, ry + 1, rx + 2, ry + 2, -10722192);
    }

    private static String getDirectionHeading(float yaw) {
        if (yaw >= 337.5f) return "S";
        if (yaw < 22.5f) {
            return "S";
        }
        if (yaw >= 22.5f && yaw < 67.5f) {
            return "SW";
        }
        if (yaw >= 67.5f && yaw < 112.5f) {
            return "W";
        }
        if (yaw >= 112.5f && yaw < 157.5f) {
            return "NW";
        }
        if (yaw >= 157.5f && yaw < 202.5f) {
            return "N";
        }
        if (yaw >= 202.5f && yaw < 247.5f) {
            return "NE";
        }
        if (!(yaw >= 247.5f)) return "SE";
        if (!(yaw < 292.5f)) return "SE";
        return "E";
    }
}

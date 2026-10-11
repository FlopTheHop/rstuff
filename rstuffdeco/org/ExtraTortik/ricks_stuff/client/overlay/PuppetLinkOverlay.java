/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.LayeredDraw$Layer
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.ExtraTortik.ricks_stuff.client.BioNeuralPossessionManager
 */
package org.ExtraTortik.ricks_stuff.client.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.LayeredDraw;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.ExtraTortik.ricks_stuff.client.BioNeuralPossessionManager;

@OnlyIn(value=Dist.CLIENT)
public class PuppetLinkOverlay {
    public static final LayeredDraw.Layer HUD_OVERLAY = (guiGraphics, deltaTracker) -> {
        if (!BioNeuralPossessionManager.isPossessing()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui) {
            return;
        }
        Font font = mc.font;
        String mobName = BioNeuralPossessionManager.getTargetMobName();
        int signal = BioNeuralPossessionManager.getSignalStrength();
        float hp = BioNeuralPossessionManager.getTargetHealth();
        float maxHp = BioNeuralPossessionManager.getTargetMaxHealth();
        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();
        String title = "[DIRECT NEURAL LINK: " + mobName.toUpperCase() + "]";
        int titleW = font.width(title);
        int topY = 16;
        int centerX = width / 2;
        guiGraphics.fill(centerX - titleW / 2 - 8, topY - 3, centerX + titleW / 2 + 8, topY + 11, -2012147434);
        guiGraphics.drawString(font, title, centerX - titleW / 2, topY, -2171, false);
        int signalColor = signal > 50 ? -16711800 : (signal > 25 ? -17664 : -56832);
        String signalText = "SIGNAL: " + signal + "%";
        int sigW = font.width(signalText);
        guiGraphics.drawString(font, signalText, centerX - sigW / 2, topY + 14, signalColor, true);
        int barW = 140;
        int barH = 6;
        int barX = centerX - barW / 2;
        int barY = height - 46;
        guiGraphics.fill(barX - 2, barY - 2, barX + barW + 2, barY + barH + 2, -1441722090);
        guiGraphics.fill(barX, barY, barX + barW, barY + barH, -13421762);
        float pct = Math.max(0.0f, Math.min(1.0f, hp / Math.max(1.0f, maxHp)));
        int fillW = (int)((float)barW * pct);
        int hpColor = pct > 0.5f ? -16718337 : (pct > 0.25f ? -26368 : -58880);
        guiGraphics.fill(barX, barY, barX + fillW, barY + barH, hpColor);
        String hpText = String.format("HP: %.1f / %.1f", Float.valueOf(hp), Float.valueOf(maxHp));
        int hpW = font.width(hpText);
        guiGraphics.drawString(font, hpText, centerX - hpW / 2, barY - 10, -1, true);
        String ability = BioNeuralPossessionManager.getCurrentAbilityName();
        Object controls = ability != null ? "\u00a77[\u00a7eLMB\u00a77] ATTACK  \u00a78|  \u00a77[\u00a7eG\u00a77] DISCONNECT  \u00a78|  \u00a77[\u00a7bR\u00a77] " + ability + "  \u00a78|  \u00a77[\u00a7cZ\u00a77] SELF-DESTRUCT" : "\u00a77[\u00a7eLMB\u00a77] ATTACK  \u00a78|  \u00a77[\u00a7eG\u00a77] DISCONNECT  \u00a78|  \u00a77[\u00a7cZ\u00a77] SELF-DESTRUCT";
        int ctrlW = font.width((String)controls);
        guiGraphics.drawString(font, (String)controls, centerX - ctrlW / 2, barY + 12, -1, true);
        int bracketColor = -50432;
        int cornerMargin = 18;
        int bracketSize = 16;
        guiGraphics.fill(cornerMargin, cornerMargin, cornerMargin + bracketSize, cornerMargin + 2, bracketColor);
        guiGraphics.fill(cornerMargin, cornerMargin, cornerMargin + 2, cornerMargin + bracketSize, bracketColor);
        guiGraphics.fill(width - cornerMargin - bracketSize, cornerMargin, width - cornerMargin, cornerMargin + 2, bracketColor);
        guiGraphics.fill(width - cornerMargin - 2, cornerMargin, width - cornerMargin, cornerMargin + bracketSize, bracketColor);
        guiGraphics.fill(cornerMargin, height - cornerMargin - 2, cornerMargin + bracketSize, height - cornerMargin, bracketColor);
        guiGraphics.fill(cornerMargin, height - cornerMargin - bracketSize, cornerMargin + 2, height - cornerMargin, bracketColor);
        guiGraphics.fill(width - cornerMargin - bracketSize, height - cornerMargin - 2, width - cornerMargin, height - cornerMargin, bracketColor);
        guiGraphics.fill(width - cornerMargin - 2, height - cornerMargin - bracketSize, width - cornerMargin, height - cornerMargin, bracketColor);
    };
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.components.Button$OnPress
 *  net.minecraft.network.chat.Component
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton
 *  org.ExtraTortik.ricks_stuff.client.gui.GadgetButton$Style
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.ExtraTortik.ricks_stuff.client.gui.GadgetButton;

public static class GadgetButton.Builder {
    private final Component message;
    private final Button.OnPress onPress;
    private int x;
    private int y;
    private int width = 120;
    private int height = 20;
    private GadgetButton.Style style = GadgetButton.Style.NORMAL;

    public GadgetButton.Builder(Component message, Button.OnPress onPress) {
        this.message = message;
        this.onPress = onPress;
    }

    public GadgetButton.Builder bounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        return this;
    }

    public GadgetButton.Builder style(GadgetButton.Style style) {
        this.style = style;
        return this;
    }

    public GadgetButton build() {
        return new GadgetButton(this.x, this.y, this.width, this.height, this.message, this.onPress, this.style);
    }
}

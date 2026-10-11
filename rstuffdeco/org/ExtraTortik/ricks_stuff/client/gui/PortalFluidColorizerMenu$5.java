/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.items.IItemHandler
 *  net.neoforged.neoforge.items.SlotItemHandler
 *  org.ExtraTortik.ricks_stuff.client.gui.PortalFluidColorizerMenu
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.ExtraTortik.ricks_stuff.client.gui.PortalFluidColorizerMenu;
import org.jetbrains.annotations.NotNull;

class PortalFluidColorizerMenu.5
extends SlotItemHandler {
    PortalFluidColorizerMenu.5(PortalFluidColorizerMenu this$0, IItemHandler arg0, int arg1, int arg2, int arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.items.IItemHandler
 *  net.neoforged.neoforge.items.SlotItemHandler
 *  org.ExtraTortik.ricks_stuff.client.gui.PortalFluidColorizerMenu
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.ExtraTortik.ricks_stuff.client.gui.PortalFluidColorizerMenu;
import org.ExtraTortik.ricks_stuff.init.ModItems;
import org.jetbrains.annotations.NotNull;

class PortalFluidColorizerMenu.1
extends SlotItemHandler {
    PortalFluidColorizerMenu.1(PortalFluidColorizerMenu this$0, IItemHandler arg0, int arg1, int arg2, int arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.is((Item)ModItems.PORTAL_FLUID.get()) || stack.is((Item)ModItems.INFINITY_PORTAL_FLUID.get());
    }
}

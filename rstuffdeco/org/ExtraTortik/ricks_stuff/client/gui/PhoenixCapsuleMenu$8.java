/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.items.IItemHandler
 *  net.neoforged.neoforge.items.SlotItemHandler
 *  org.ExtraTortik.ricks_stuff.client.gui.PhoenixCapsuleMenu
 */
package org.ExtraTortik.ricks_stuff.client.gui;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.ExtraTortik.ricks_stuff.client.gui.PhoenixCapsuleMenu;

class PhoenixCapsuleMenu.8
extends SlotItemHandler {
    PhoenixCapsuleMenu.8(PhoenixCapsuleMenu this$0, IItemHandler arg0, int arg1, int arg2, int arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    public boolean mayPlace(ItemStack stack) {
        return stack.getEquipmentSlot() == EquipmentSlot.LEGS;
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.CraftingContainer
 *  net.minecraft.world.inventory.ResultSlot
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  org.ExtraTortik.ricks_stuff.entity.omega.MultiverseErasureSavedData
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package org.ExtraTortik.ricks_stuff.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.ExtraTortik.ricks_stuff.entity.omega.MultiverseErasureSavedData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ResultSlot.class})
public class ResultSlotMixin {
    @Shadow
    @Final
    private CraftingContainer craftSlots;
    @Shadow
    @Final
    private Player player;

    @Inject(method={"onTake"}, at={@At(value="HEAD")}, cancellable=true)
    private void ricks_stuff$preventTakeErased(Player player, ItemStack stack, CallbackInfo ci) {
        MultiverseErasureSavedData storage = MultiverseErasureSavedData.getEffective((Level)(player != null ? player.level() : null));
        if (storage == null) return;
        if (storage.getErasedItemTypes().isEmpty()) return;
        if (!stack.isEmpty() && storage.isItemErased(stack.getItem())) {
            stack.setCount(0);
            ci.cancel();
            return;
        }
        if (this.craftSlots == null) return;
        int i = 0;
        while (i < this.craftSlots.getContainerSize()) {
            ItemStack inSlot = this.craftSlots.getItem(i);
            if (!inSlot.isEmpty() && storage.isItemErased(inSlot.getItem())) {
                stack.setCount(0);
                ci.cancel();
                return;
            }
            ++i;
        }
    }
}

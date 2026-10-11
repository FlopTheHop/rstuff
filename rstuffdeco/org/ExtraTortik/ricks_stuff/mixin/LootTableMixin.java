/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.storage.loot.LootContext
 *  net.minecraft.world.level.storage.loot.LootTable
 *  org.ExtraTortik.ricks_stuff.entity.omega.MultiverseErasureSavedData
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package org.ExtraTortik.ricks_stuff.mixin;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.ExtraTortik.ricks_stuff.entity.omega.MultiverseErasureSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={LootTable.class})
public class LootTableMixin {
    @Inject(method={"getRandomItems(Lnet/minecraft/world/level/storage/loot/LootContext;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;"}, at={@At(value="RETURN")}, cancellable=true)
    private void ricks_stuff$filterErasedLoot(LootContext context, CallbackInfoReturnable<ObjectArrayList<ItemStack>> cir) {
        ObjectArrayList items = (ObjectArrayList)cir.getReturnValue();
        if (items == null) return;
        if (items.isEmpty()) return;
        if (context == null) return;
        MultiverseErasureSavedData storage = MultiverseErasureSavedData.getEffective((Level)context.getLevel());
        if (storage == null) return;
        if (storage.getErasedItemTypes().isEmpty()) return;
        boolean removedAny = items.removeIf(stack -> !stack.isEmpty() && storage.isItemErased(stack.getItem()));
        if (!removedAny) return;
        cir.setReturnValue((Object)items);
    }
}

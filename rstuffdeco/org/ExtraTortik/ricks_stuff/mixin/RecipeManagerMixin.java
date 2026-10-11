/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.Recipe
 *  net.minecraft.world.item.crafting.RecipeHolder
 *  net.minecraft.world.item.crafting.RecipeInput
 *  net.minecraft.world.item.crafting.RecipeManager
 *  net.minecraft.world.item.crafting.RecipeType
 *  net.minecraft.world.level.Level
 *  org.ExtraTortik.ricks_stuff.entity.omega.MultiverseErasureSavedData
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package org.ExtraTortik.ricks_stuff.mixin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.ExtraTortik.ricks_stuff.entity.omega.MultiverseErasureSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={RecipeManager.class})
public class RecipeManagerMixin {
    @Inject(method={"getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/item/crafting/RecipeInput;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;"}, at={@At(value="RETURN")}, cancellable=true)
    private <I extends RecipeInput, T extends Recipe<I>> void ricks_stuff$blockErasedRecipe(RecipeType<T> type, I input, Level level, CallbackInfoReturnable<Optional<RecipeHolder<T>>> cir) {
        if (!((Optional)cir.getReturnValue()).isPresent()) return;
        RecipeHolder recipe = (RecipeHolder)((Optional)cir.getReturnValue()).get();
        if (!RecipeManagerMixin.ricks_stuff$isRecipeBlocked(recipe, input, level)) return;
        cir.setReturnValue(Optional.empty());
    }

    @Inject(method={"getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/item/crafting/RecipeInput;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/crafting/RecipeHolder;)Ljava/util/Optional;"}, at={@At(value="RETURN")}, cancellable=true)
    private <I extends RecipeInput, T extends Recipe<I>> void ricks_stuff$blockErasedRecipeWithLast(RecipeType<T> type, I input, Level level, RecipeHolder<T> lastRecipe, CallbackInfoReturnable<Optional<RecipeHolder<T>>> cir) {
        if (!((Optional)cir.getReturnValue()).isPresent()) return;
        RecipeHolder recipe = (RecipeHolder)((Optional)cir.getReturnValue()).get();
        if (!RecipeManagerMixin.ricks_stuff$isRecipeBlocked(recipe, input, level)) return;
        cir.setReturnValue(Optional.empty());
    }

    @Inject(method={"getRecipesFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/item/crafting/RecipeInput;Lnet/minecraft/world/level/Level;)Ljava/util/List;"}, at={@At(value="RETURN")}, cancellable=true)
    private <I extends RecipeInput, T extends Recipe<I>> void ricks_stuff$filterErasedRecipes(RecipeType<T> type, I input, Level level, CallbackInfoReturnable<List<RecipeHolder<T>>> cir) {
        List recipes = (List)cir.getReturnValue();
        if (recipes == null) return;
        if (recipes.isEmpty()) return;
        ArrayList<RecipeHolder> filtered = new ArrayList<RecipeHolder>();
        Iterator iterator = recipes.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                cir.setReturnValue(filtered);
                return;
            }
            RecipeHolder recipe = (RecipeHolder)iterator.next();
            if (RecipeManagerMixin.ricks_stuff$isRecipeBlocked(recipe, input, level)) continue;
            filtered.add(recipe);
        }
    }

    private static <I extends RecipeInput, T extends Recipe<I>> boolean ricks_stuff$isRecipeBlocked(RecipeHolder<T> recipeHolder, I input, Level level) {
        if (recipeHolder == null) {
            return false;
        }
        MultiverseErasureSavedData storage = MultiverseErasureSavedData.getEffective((Level)level);
        if (storage == null) return false;
        if (storage.getErasedItemTypes().isEmpty()) {
            return false;
        }
        if (input != null) {
            for (int i = 0; i < input.size(); ++i) {
                ItemStack inSlot = input.getItem(i);
                if (inSlot.isEmpty() || !storage.isItemErased(inSlot.getItem())) continue;
                return true;
            }
        }
        try {
            if (level == null) return false;
            if (level.registryAccess() == null) return false;
            ItemStack result = recipeHolder.value().getResultItem((HolderLookup.Provider)level.registryAccess());
            if (result.isEmpty()) return false;
            if (!storage.isItemErased(result.getItem())) return false;
            return true;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return false;
    }
}

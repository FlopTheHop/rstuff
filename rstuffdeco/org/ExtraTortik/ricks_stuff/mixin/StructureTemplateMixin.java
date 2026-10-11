/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate$StructureBlockInfo
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
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.ExtraTortik.ricks_stuff.entity.omega.MultiverseErasureSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={StructureTemplate.class})
public class StructureTemplateMixin {
    @Inject(method={"processBlockInfos(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;Ljava/util/List;)Ljava/util/List;"}, at={@At(value="RETURN")}, cancellable=true)
    private static void ricks_stuff$filterErasedStructureBlocks(ServerLevelAccessor level, BlockPos pos1, BlockPos pos2, StructurePlaceSettings settings, List<StructureTemplate.StructureBlockInfo> blockInfos, CallbackInfoReturnable<List<StructureTemplate.StructureBlockInfo>> cir) {
        List list = (List)cir.getReturnValue();
        if (list == null) return;
        if (list.isEmpty()) return;
        if (level == null) return;
        ServerLevel actualLevel = null;
        try {
            actualLevel = level.getLevel();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        if (actualLevel == null) return;
        if (actualLevel.isClientSide()) {
            return;
        }
        MultiverseErasureSavedData storage = MultiverseErasureSavedData.getEffective((Level)actualLevel);
        if (storage == null) return;
        if (storage.getErasedItemTypes().isEmpty()) return;
        ArrayList<StructureTemplate.StructureBlockInfo> filtered = new ArrayList<StructureTemplate.StructureBlockInfo>(list.size());
        boolean modified = false;
        Iterator iterator = list.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                if (!modified) return;
                cir.setReturnValue(filtered);
                return;
            }
            StructureTemplate.StructureBlockInfo info = (StructureTemplate.StructureBlockInfo)iterator.next();
            if (info != null && storage.isBlockErased(info.state())) {
                modified = true;
                continue;
            }
            filtered.add(info);
        }
    }
}

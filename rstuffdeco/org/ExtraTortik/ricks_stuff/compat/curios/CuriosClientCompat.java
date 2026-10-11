/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.ExtraTortik.ricks_stuff.compat.curios.CuriosClientInternalHelper
 *  org.ExtraTortik.ricks_stuff.compat.curios.CuriosCompat
 */
package org.ExtraTortik.ricks_stuff.compat.curios;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.ExtraTortik.ricks_stuff.compat.curios.CuriosClientInternalHelper;
import org.ExtraTortik.ricks_stuff.compat.curios.CuriosCompat;

@OnlyIn(value=Dist.CLIENT)
public class CuriosClientCompat {
    public static void registerRenderers() {
        if (!CuriosCompat.isLoaded()) return;
        CuriosClientInternalHelper.registerRenderers();
    }
}

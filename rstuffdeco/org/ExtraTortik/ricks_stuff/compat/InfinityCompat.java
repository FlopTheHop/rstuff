/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.world.level.Level
 *  net.neoforged.fml.ModList
 *  org.ExtraTortik.ricks_stuff.portal.BlenderDimensionHelper
 *  org.ExtraTortik.ricks_stuff.portal.FishDimensionHelper
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package org.ExtraTortik.ricks_stuff.compat;

import java.lang.reflect.Method;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import org.ExtraTortik.ricks_stuff.portal.BlenderDimensionHelper;
import org.ExtraTortik.ricks_stuff.portal.FishDimensionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InfinityCompat {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "infinity";
    private static Class<?> portalCreatorClass;
    private static Class<?> infinityMethodsClass;

    private static Class<?> getPortalCreatorClass() throws ClassNotFoundException {
        if (portalCreatorClass != null) {
            return portalCreatorClass;
        }
        try {
            portalCreatorClass = Class.forName("net.codexarchonic.infinity.util.teleport.PortalCreator");
        }
        catch (ClassNotFoundException e) {
            portalCreatorClass = Class.forName("net.lerariemann.infinity.util.teleport.PortalCreator");
        }
        return portalCreatorClass;
    }

    private static Class<?> getInfinityMethodsClass() throws ClassNotFoundException {
        if (infinityMethodsClass != null) {
            return infinityMethodsClass;
        }
        try {
            infinityMethodsClass = Class.forName("net.codexarchonic.infinity.util.InfinityMethods");
        }
        catch (ClassNotFoundException e) {
            infinityMethodsClass = Class.forName("net.lerariemann.infinity.util.InfinityMethods");
        }
        return infinityMethodsClass;
    }

    public static boolean isLoaded() {
        return ModList.get() != null && ModList.get().isLoaded(MOD_ID);
    }

    public static ResourceKey<Level> resolveOrGenerateDimension(MinecraftServer server, String inputText) {
        if (!InfinityCompat.isLoaded()) return null;
        if (server == null) return null;
        if (inputText == null) return null;
        if (inputText.trim().isEmpty()) {
            return null;
        }
        String trimmed = inputText.trim();
        if (trimmed.equalsIgnoreCase("minecraft:overworld")) return null;
        if (trimmed.equalsIgnoreCase("overworld")) return null;
        if (trimmed.equalsIgnoreCase("minecraft:the_nether")) return null;
        if (trimmed.equalsIgnoreCase("the_nether")) return null;
        if (trimmed.equalsIgnoreCase("nether")) return null;
        if (trimmed.equalsIgnoreCase("minecraft:the_end")) return null;
        if (trimmed.equalsIgnoreCase("the_end")) return null;
        if (trimmed.equalsIgnoreCase("end")) {
            return null;
        }
        if (BlenderDimensionHelper.isBlenderDimension((String)trimmed)) {
            return null;
        }
        if (FishDimensionHelper.isFishDimension((String)trimmed)) {
            return null;
        }
        try {
            ResourceLocation dimId;
            if (trimmed.startsWith("infinity:") && ResourceLocation.tryParse((String)trimmed) != null) {
                dimId = ResourceLocation.tryParse((String)trimmed);
            } else if (trimmed.contains(":") && !trimmed.startsWith("infinity:") && !trimmed.startsWith("minecraft:")) {
                ResourceKey key;
                ResourceLocation customLoc = ResourceLocation.tryParse((String)trimmed);
                if (customLoc != null && server.getLevel(key = ResourceKey.create((ResourceKey)Registries.DIMENSION, (ResourceLocation)customLoc)) != null) {
                    return key;
                }
                dimId = InfinityCompat.convertTextToInfinityId(server, trimmed);
            } else {
                String textToConvert = trimmed.startsWith("minecraft:") ? trimmed.substring("minecraft:".length()) : trimmed;
                dimId = InfinityCompat.convertTextToInfinityId(server, textToConvert);
            }
            if (dimId == null) {
                return null;
            }
            ResourceKey dimKey = ResourceKey.create((ResourceKey)Registries.DIMENSION, (ResourceLocation)dimId);
            if (server.getLevel(dimKey) != null) return dimKey;
            Class<?> portalCreator = InfinityCompat.getPortalCreatorClass();
            try {
                Method m = portalCreator.getMethod("tryAddInfinityDimension", MinecraftServer.class, ResourceLocation.class);
                m.invoke(null, server, dimId);
            }
            catch (NoSuchMethodException e) {
                Method m = portalCreator.getMethod("addDimension", MinecraftServer.class, ResourceLocation.class);
                m.invoke(null, server, dimId);
            }
            LOGGER.info("Portal Gun triggered Infinity dimension generation for '{}'", (Object)dimId);
            return dimKey;
        }
        catch (Throwable t) {
            LOGGER.error("Portal Gun failed to generate Infinity dimension for text '{}'", (Object)inputText, (Object)t);
            return null;
        }
    }

    private static ResourceLocation convertTextToInfinityId(MinecraftServer server, String text) {
        try {
            Class<?> methodsClass = InfinityCompat.getInfinityMethodsClass();
            Method preprocessMethod = methodsClass.getMethod("dimTextPreprocess", String.class);
            Method toIdMethod = methodsClass.getMethod("dimTextToId", String.class);
            Object preprocessed = preprocessMethod.invoke(null, text);
            Object idObj = toIdMethod.invoke(null, preprocessed);
            if (idObj instanceof ResourceLocation) {
                ResourceLocation rl = (ResourceLocation)idObj;
                return rl;
            }
            if (idObj == null) return null;
            return ResourceLocation.tryParse((String)idObj.toString());
        }
        catch (Throwable t) {
            LOGGER.error("Portal Gun failed to convert text '{}' to Infinity dimension ID", (Object)text, (Object)t);
            return null;
        }
    }
}

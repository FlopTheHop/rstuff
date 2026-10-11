/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.suggestion.SuggestionsBuilder
 *  net.minecraft.commands.CommandBuildContext
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.commands.Commands
 *  net.minecraft.commands.SharedSuggestionProvider
 *  net.minecraft.commands.arguments.EntityArgument
 *  net.minecraft.commands.arguments.ResourceLocationArgument
 *  net.minecraft.commands.arguments.item.ItemArgument
 *  net.minecraft.commands.arguments.item.ItemInput
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.item.Item
 *  org.ExtraTortik.ricks_stuff.entity.omega.MultiverseErasureSavedData
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceManager
 */
package org.ExtraTortik.ricks_stuff.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import org.ExtraTortik.ricks_stuff.entity.omega.MultiverseErasureSavedData;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceManager;

public class OmegaCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal((String)"omega").requires(source -> source.hasPermission(2))).then(Commands.literal((String)"clear").executes(context -> OmegaCommand.clearAll((CommandSourceStack)context.getSource())))).then(Commands.literal((String)"list").executes(context -> OmegaCommand.listErased((CommandSourceStack)context.getSource())))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal((String)"unerase").then(Commands.literal((String)"all").executes(context -> OmegaCommand.clearAll((CommandSourceStack)context.getSource())))).then(Commands.literal((String)"player").then(Commands.argument((String)"targets", (ArgumentType)EntityArgument.players()).executes(context -> OmegaCommand.unerasePlayers((CommandSourceStack)context.getSource(), EntityArgument.getPlayers((CommandContext)context, (String)"targets")))))).then(Commands.literal((String)"entity").then(Commands.argument((String)"entity_type", (ArgumentType)ResourceLocationArgument.id()).suggests((context, builder) -> {
            MinecraftServer server = ((CommandSourceStack)context.getSource()).getServer();
            MultiverseErasureSavedData data = MultiverseErasureSavedData.get((MinecraftServer)server);
            return SharedSuggestionProvider.suggest((Iterable)data.getErasedEntityTypes(), (SuggestionsBuilder)builder);
        }).executes(context -> OmegaCommand.uneraseEntity((CommandSourceStack)context.getSource(), ResourceLocationArgument.getId((CommandContext)context, (String)"entity_type")))))).then(Commands.literal((String)"item").then(Commands.argument((String)"item", (ArgumentType)ItemArgument.item((CommandBuildContext)buildContext)).executes(context -> OmegaCommand.uneraseItem((CommandSourceStack)context.getSource(), ItemArgument.getItem((CommandContext)context, (String)"item")))))));
    }

    private static int clearAll(CommandSourceStack source) {
        MinecraftServer server = source.getServer();
        OmegaDeviceManager.clearAllErasures((MinecraftServer)server);
        source.sendSuccess(() -> Component.literal((String)"Cleared all Multiverse Erasure records. All beings and matter restored to the timeline."), true);
        return 1;
    }

    private static int listErased(CommandSourceStack source) {
        MinecraftServer server = source.getServer();
        MultiverseErasureSavedData data = MultiverseErasureSavedData.get((MinecraftServer)server);
        Set uuids = data.getErasedUUIDs();
        Set entities = data.getErasedEntityTypes();
        Set items = data.getErasedItemTypes();
        if (uuids.isEmpty() && entities.isEmpty() && items.isEmpty()) {
            source.sendSuccess(() -> Component.literal((String)"The Multiverse timeline is intact. No active erasures."), false);
            return 0;
        }
        source.sendSuccess(() -> Component.literal((String)"=== ACTIVE MULTIVERSE ERASURES ==="), false);
        if (!uuids.isEmpty()) {
            source.sendSuccess(() -> Component.literal((String)("Erased Individual UUIDs (" + uuids.size() + "):")), false);
            for (UUID uuid : uuids) {
                source.sendSuccess(() -> Component.literal((String)("  - " + uuid.toString())), false);
            }
        }
        if (!entities.isEmpty()) {
            source.sendSuccess(() -> Component.literal((String)("Erased Entity Types (" + entities.size() + "):")), false);
            for (String entityType : entities) {
                source.sendSuccess(() -> Component.literal((String)("  - " + entityType)), false);
            }
        }
        if (items.isEmpty()) return uuids.size() + entities.size() + items.size();
        source.sendSuccess(() -> Component.literal((String)("Erased Item Types (" + items.size() + "):")), false);
        Iterator iterator = items.iterator();
        while (iterator.hasNext()) {
            String itemType = (String)iterator.next();
            source.sendSuccess(() -> Component.literal((String)("  - " + itemType)), false);
        }
        return uuids.size() + entities.size() + items.size();
    }

    private static int unerasePlayers(CommandSourceStack source, Collection<ServerPlayer> players) {
        MinecraftServer server = source.getServer();
        int count = 0;
        Iterator<ServerPlayer> iterator = players.iterator();
        while (iterator.hasNext()) {
            String name;
            ServerPlayer player = iterator.next();
            if (OmegaDeviceManager.unerasePlayer((MinecraftServer)server, (UUID)player.getUUID())) {
                ++count;
                name = player.getName().getString();
                source.sendSuccess(() -> Component.literal((String)("Restored player " + name + " to the timeline.")), true);
                continue;
            }
            name = player.getName().getString();
            source.sendFailure((Component)Component.literal((String)("Player " + name + " was not erased.")));
        }
        return count;
    }

    private static int uneraseEntity(CommandSourceStack source, ResourceLocation entityTypeId) {
        String idStr;
        MinecraftServer server = source.getServer();
        if (OmegaDeviceManager.uneraseEntityType((MinecraftServer)server, (String)(idStr = entityTypeId.toString()))) {
            source.sendSuccess(() -> Component.literal((String)("Restored entity type " + idStr + " across the Multiverse.")), true);
            return 1;
        }
        source.sendFailure((Component)Component.literal((String)("Entity type " + idStr + " is not erased.")));
        return 0;
    }

    private static int uneraseItem(CommandSourceStack source, ItemInput itemInput) {
        MinecraftServer server = source.getServer();
        Item item = itemInput.getItem();
        ResourceLocation itemKey = BuiltInRegistries.ITEM.getKey((Object)item);
        if (itemKey == null) {
            source.sendFailure((Component)Component.literal((String)"Invalid item."));
            return 0;
        }
        String idStr = itemKey.toString();
        if (OmegaDeviceManager.uneraseItem((MinecraftServer)server, (String)idStr)) {
            source.sendSuccess(() -> Component.literal((String)("Restored item " + idStr + " across the Multiverse.")), true);
            return 1;
        }
        source.sendFailure((Component)Component.literal((String)("Item " + idStr + " is not erased.")));
        return 0;
    }
}

package owmii.losttrinkets.command;

import com.mojang.brigadier.CommandDispatcher;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import owmii.losttrinkets.LostTrinkets;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.handler.UnlockManager;

import java.util.Collection;

public class MainCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal(LostTrinkets.MOD_ID)
                .then(CommandManager.literal("unlock").requires(source -> {
                    return source.hasPermissionLevel(2);
                }).then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal("all").executes(context -> {
                    Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "targets");
                    players.forEach(player -> {
                        UnlockManager.getTrinkets().forEach(trinket -> {
                            UnlockManager.unlock(player, trinket, false, false);
                        });
                    });
                    context.getSource().sendFeedback(new TranslatableText("chat.losttrinkets.unlocked.all"), true);
                    return players.size();
                })).then(CommandManager.literal("random").executes(context -> {
                    Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "targets");
                    players.forEach(LostTrinketsAPI.get()::unlock);
                    return players.size();
                }))))
                .then(CommandManager.literal("clear").requires(source -> {
                    return source.hasPermissionLevel(2);
                }).then(CommandManager.argument("targets", EntityArgumentType.players()).executes(context -> {
                    Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "targets");
                    players.forEach(player -> {
                        LostTrinketsAPI.getTrinkets(player).clear();
                    });
                    return players.size();
                }))));
    }

    public static void register() {
        CommandRegistrationEvent.EVENT.register((dispatcher, selection) -> {
            register(dispatcher);
        });
    }
}

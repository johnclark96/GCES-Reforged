package com.lypaka.gces.gottacatchemsmall.Commands;

import com.lypaka.gces.gottacatchemsmall.Config.ConfigGetters;
import com.lypaka.gces.gottacatchemsmall.Utils.AccountHandler;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class PlayerCommands {

    public static CommandSpec getCheckCommand() {

        return CommandSpec.builder()
                .permission("gces.command.check")
                .arguments(
                        GenericArguments.optional(GenericArguments.player(Text.of("player")))
                )
                .executor((sender, context) -> {

                    Player player;
                    if (context.getOne("player").isPresent() && sender.hasPermission("gces.command.check.others")) {

                        player = (Player) context.getOne("player").get();

                    } else {

                        player = (Player) sender;

                    }
                    int index = ConfigGetters.getIndexFromString(ConfigGetters.getPlayerDifficulty(player));
                    int catchLvl = 0;
                    try {
                        catchLvl = AccountHandler.getCatchTier(player, index);
                    } catch (ObjectMappingException e) {
                        e.printStackTrace();
                    }
                    int levelLvl = 0;
                    try {
                        levelLvl = AccountHandler.getLevelTier(player, index);
                    } catch (ObjectMappingException e) {
                        e.printStackTrace();
                    }
                    sender.sendMessage(Text.of(TextColors.YELLOW, "Catch level = " + catchLvl + ". Level level = " + levelLvl));

                    return CommandResult.success();
                })
                .build();

    }

}

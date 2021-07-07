package com.lypaka.gces.gottacatchemsmall.Commands;

import com.lypaka.gces.gottacatchemsmall.Config.ConfigGetters;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigManager;
import com.lypaka.gces.gottacatchemsmall.GCES;
import com.lypaka.gces.gottacatchemsmall.Listeners.JoinListener;
import com.lypaka.gces.gottacatchemsmall.Utils.AccountHandler;
import com.lypaka.gces.gottacatchemsmall.Utils.TierHandler;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.io.IOException;

public class AdminCommands {

    public static CommandSpec getReloadCommand() {

        return CommandSpec.builder()
                .permission("gces.command.admin")
                .executor((sender, context) -> {

                    ConfigManager.load();
                    sender.sendMessage(Text.of(TextColors.GREEN, "Successfully reloaded GCES config!"));
                    if (!ConfigManager.getBaseNode(0, "Restriction-Optional").getBoolean()) {

                        GCES.instance.logger.info("Detecting that restrictions are not optional, quickly checking each player online for these settings...");
                        for (Player player : Sponge.getServer().getOnlinePlayers()) {

                            try {

                                JoinListener.generateAccount(player);

                            } catch (ObjectMappingException | IOException e) {

                                e.printStackTrace();

                            }

                        }

                    }

                    return CommandResult.success();

                })
                .build();

    }

    public static CommandSpec getLevelUpCommand() {

        return CommandSpec.builder()
                .arguments(
                        GenericArguments.string(Text.of("category")),
                        GenericArguments.player(Text.of("player"))
                )
                .permission("gces.command.admin")
                .executor((sender, context) -> {

                    Player player = (Player) context.getOne("player").get();
                    int index = ConfigGetters.getIndexFromString(ConfigGetters.getPlayerDifficulty(player));
                    String category = context.getOne("category").get().toString();

                    switch (category.toLowerCase()) {

                        case "catching":

                            try {

                                AccountHandler.levelUpCatchingTier(player, index);

                            } catch (ObjectMappingException e) {

                                e.printStackTrace();

                            }
                            break;

                        case "catchingEvo":

                            try {

                                AccountHandler.levelUpCatchingEvoStage(player, index);

                            } catch (ObjectMappingException | IOException e) {

                                e.printStackTrace();

                            }
                            break;

                        case "leveling":

                            try {

                                AccountHandler.levelUpLevelingTier(player, index);

                            } catch (ObjectMappingException e) {

                                e.printStackTrace();

                            }
                            break;

                        case "evolving":

                            try {

                                AccountHandler.levelUpEvolvingTier(player, index);

                            } catch (ObjectMappingException | IOException e) {

                                e.printStackTrace();

                            }
                            break;

                    }

                    player.sendMessage(Text.of(TextColors.GREEN, "You leveled up your " + category.replace("CatchingEvo", "catching evolution") + " tier!"));
                    sender.sendMessage(Text.of("Successfully leveled up " + player.getName()));
                    ConfigManager.savePlayer(player.getUniqueId());

                    return CommandResult.success();

                })
                .build();

    }

    public static CommandSpec getSetLevelCommand() {

        return CommandSpec.builder()
                .arguments(
                        GenericArguments.string(Text.of("category")),
                        GenericArguments.player(Text.of("player")),
                        GenericArguments.integer(Text.of("tier"))
                )
                .permission("gces.command.admin")
                .executor((sender, context) -> {

                    Player player = (Player) context.getOne("player").get();
                    String category = context.getOne("category").get().toString();
                    int level = (int) context.getOne("tier").get();
                    int index = ConfigGetters.getIndexFromString(ConfigGetters.getPlayerDifficulty(player));

                    try {

                        if (level <= TierHandler.getMaxTierLevel(index, category)) {

                            switch (category) {

                                case "Catching":
                                    AccountHandler.setCatchTier(player, level);
                                    break;
                                case "Leveling":
                                    AccountHandler.setLevelTier(player, level);
                                    break;

                            }

                            ConfigManager.savePlayer(player.getUniqueId());

                        } else {

                            sender.sendMessage(Text.of(TextColors.RED, "Cannot level up player higher than max tier level!"));

                        }

                    } catch (ObjectMappingException e) {

                        e.printStackTrace();

                    }

                    player.sendMessage(Text.of(TextColors.GREEN, "You leveled up your " + category.replace("CatchingEvo", "catching evolution") + " tier!"));
                    sender.sendMessage(Text.of("Successfully leveled up " + player.getName()));

                    return CommandResult.success();

                })
                .build();

    }

    public static CommandSpec getGetLevelCommand() {

        return CommandSpec.builder()
                .arguments(
                        GenericArguments.player(Text.of("player")),
                        GenericArguments.string(Text.of("tier"))
                )
                .permission("gces.command.admin")
                .executor((sender, context) -> {

                    Player player = (Player) context.getOne("player").get();
                    String tier = context.getOne("tier").get().toString();
                    if (tier.equalsIgnoreCase("catching")) {

                        tier = "Catching";

                    } else {

                        tier = "Leveling";

                    }

                    int lvl = ConfigManager.getPlayerConfigNode(player.getUniqueId(), "Account", "Levels", tier + "-Tier").getInt();

                    sender.sendMessage(Text.of(player.getName() + "'s " + tier + " Tier Level is: " + lvl));

                    return CommandResult.success();

                })
                .build();

    }

    public static CommandSpec getPermissionCommand() {

        return CommandSpec.builder()
                .permission("gces.command.admin")
                .arguments(
                        GenericArguments.string(Text.of("add|set|remove|unset")),
                        GenericArguments.player(Text.of("player")),
                        GenericArguments.string(Text.of("permission"))
                )
                .executor((sender, context) -> {

                    Player player = (Player) context.getOne("player").get();
                    String permission = context.getOne("permission").get().toString();
                    String mode = context.getOne("add|set|remove|unset").get().toString();
                    if (mode.equalsIgnoreCase("add") || mode.equalsIgnoreCase("set")) {

                        mode = "add";

                    } else {

                        mode = "remove";

                    }

                    int index = ConfigGetters.getIndexFromString(ConfigGetters.getPlayerDifficulty(player));

                    if (mode.equalsIgnoreCase("add")) {

                        try {

                            if (!AccountHandler.hasPermission(player, permission, index)) {

                                AccountHandler.addPermission(player, permission, index);
                                sender.sendMessage(Text.of(TextColors.GREEN, "Successfully added permission " + permission + " to " + player.getName()));

                            }

                        } catch (ObjectMappingException e) {

                            e.printStackTrace();

                        }

                    } else {

                        try {

                            if (AccountHandler.hasPermission(player, permission, index)) {

                                AccountHandler.removePermission(player, permission, index);
                                sender.sendMessage(Text.of(TextColors.GREEN, "Successfully removed permission " + permission + " from " + player.getName()));

                            }

                        } catch (ObjectMappingException | IOException e) {

                            e.printStackTrace();

                        }

                    }

                    return CommandResult.success();

                })
                .build();

    }

    public static CommandSpec getDifficultyCommands() {

        return CommandSpec.builder()
                .permission("gces.command.admin")
                .arguments(
                        GenericArguments.string(Text.of("check|set")),
                        GenericArguments.player(Text.of("player")),
                        GenericArguments.optional(GenericArguments.string(Text.of("difficulty")))
                )
                .executor((sender, context) -> {

                    Player player = (Player) context.getOne("player").get();
                    String mode = context.getOne("check|set").get().toString();

                    if (mode.equalsIgnoreCase("set") && context.getOne("difficulty").isPresent()) {

                        String difficulty = context.getOne("difficulty").get().toString();
                        if (difficulty.equalsIgnoreCase("Default")) {

                            difficulty = "Default";

                        } else if (difficulty.equalsIgnoreCase("easy")) {

                            difficulty = "Easy";

                        } else if (difficulty.equalsIgnoreCase("medium")) {

                            difficulty = "Medium";

                        } else if (difficulty.equalsIgnoreCase("hard")) {

                            difficulty = "Hard";

                        } else {

                            sender.sendMessage(Text.of(TextColors.RED, "Invalid difficulty setting! Use default, easy, medium, hard or default!"));
                            return CommandResult.success();

                        }

                        try {

                            if (!ConfigGetters.isDifficultyEnabled(difficulty)) {

                                difficulty = "Default";
                                GCES.getLogger().error("Trying to use a difficulty not enabled as the default difficulty, using the Default difficulty instead!");

                            }

                            JoinListener.generateAccount(player, difficulty);

                        } catch (ObjectMappingException | IOException e) {

                            e.printStackTrace();

                        }
                        sender.sendMessage(Text.of(TextColors.GREEN, "Successfully set " + player.getName() + "'s difficulty to " + difficulty + "!"));

                    } else if (mode.equalsIgnoreCase("check")) {

                        sender.sendMessage(Text.of(TextColors.GREEN, player.getName() + "'s current difficulty setting is: " + AccountHandler.getDifficulty(player)));

                    } else {

                        sender.sendMessage(Text.of(TextColors.RED, "Invalid usage! Use \"/gces difficulty set <player> <difficulty>\" or \"/gces difficulty check <player>\"!"));

                    }

                    return CommandResult.success();

                })
                .build();

    }

}

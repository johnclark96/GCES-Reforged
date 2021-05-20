package com.lypaka.gces.gottacatchemsmall.Commands;

import com.lypaka.gces.gottacatchemsmall.Config.ConfigManager;
import com.lypaka.gces.gottacatchemsmall.GCES;
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
import org.spongepowered.api.text.format.TextStyles;

import java.io.IOException;

public class GCESAdminCommand {

    public static void registerAdminCommands() {

        CommandSpec reload = CommandSpec.builder()
                .permission("gces.command.admin")
                .executor((sender, context) -> {

                    ConfigManager.load();
                    sender.sendMessage(Text.of(TextColors.GREEN, "Successfully reloaded GCES config!"));

                    return CommandResult.success();

                })
                .build();

        CommandSpec levelUp = CommandSpec.builder()
                .arguments(
                        GenericArguments.string(Text.of("category")),
                        GenericArguments.player(Text.of("player"))
                )
                .permission("gces.command.admin")
                .executor((sender, context) -> {

                    Player player = (Player) context.getOne("player").get();
                    String category = context.getOne("category").get().toString();

                    switch (category) {
                        case "Catching":
                            try {
                                AccountHandler.levelUpCatchingTier(player);
                            } catch (ObjectMappingException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "CatchingEvo":
                            try {
                                AccountHandler.levelUpCatchingEvoStage(player);
                            } catch (ObjectMappingException | IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "Leveling":
                            try {
                                AccountHandler.levelUpLevelingTier(player);
                            } catch (ObjectMappingException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "Evolving":
                            try {
                                AccountHandler.levelUpEvolvingTier(player);
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

        CommandSpec setlvl = CommandSpec.builder()
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

                    try {
                        if (level <= TierHandler.getMaxTierLevel(category)) {

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

        CommandSpec getLevel = CommandSpec.builder()
                .arguments(
                        GenericArguments.player(Text.of("player")),
                        GenericArguments.string(Text.of("tier"))
                )
                .permission("gces.command.admin")
                .executor((sender, context) -> {

                    Player player = (Player) context.getOne("player").get();
                    String tier = context.getOne("tier").get().toString();

                    int lvl = ConfigManager.getPlayerConfigNode(player.getUniqueId(), "Account", "Levels", tier + "-Tier").getInt();

                    sender.sendMessage(Text.of(player.getName() + "'s " + tier + " Tier Level is: " + lvl));

                    return CommandResult.success();

                })
                .build();

        CommandSpec addPerm = CommandSpec.builder()
                .arguments(
                        GenericArguments.player(Text.of("player")),
                        GenericArguments.string(Text.of("permission"))
                )
                .permission("gces.command.admin")
                .executor((sender, context) -> {

                    Player player = (Player) context.getOne("player").get();
                    String perm = context.getOne("permission").get().toString();


                    try {

                        if (!AccountHandler.hasPermission(player, perm)) {

                            AccountHandler.addPermission(player, perm);
                            sender.sendMessage(Text.of("Successfully added permission: " + perm + " to " + player.getName() + "!"));
                            ConfigManager.savePlayer(player.getUniqueId());

                        } else {

                            sender.sendMessage(Text.of(player.getName() + " already has this permission!"));

                        }

                    } catch (ObjectMappingException e) {
                        e.printStackTrace();
                    }

                    return CommandResult.success();

                })
                .build();

        CommandSpec removePerm = CommandSpec.builder()
                .arguments(
                        GenericArguments.player(Text.of("player")),
                        GenericArguments.string(Text.of("permission"))
                )
                .permission("gces.command.admin")
                .executor((sender, context) -> {

                    Player player = (Player) context.getOne("player").get();
                    String perm = context.getOne("permission").get().toString();

                    try {

                        if (AccountHandler.hasPermission(player, perm)) {

                            AccountHandler.removePermission(player, perm);
                            sender.sendMessage(Text.of("Successfully removed permission: " + perm + " from " + player.getName() + "!"));
                            ConfigManager.savePlayer(player.getUniqueId());

                        } else {

                            sender.sendMessage(Text.of(player.getName() + " does not have that permission to remove!"));

                        }

                    } catch (ObjectMappingException | IOException e) {
                        e.printStackTrace();
                    }

                    return CommandResult.success();

                })
                .build();


        CommandSpec check = CommandSpec.builder()
                .permission("gces.command.list")
                .executor((sender, context) -> {

                    Player player = (Player) sender;
                    int catchLvl = 0;
                    int levelLvl = 0;
                    try {
                        catchLvl = AccountHandler.getCatchTier(player);
                        levelLvl = AccountHandler.getLevelTier(player);
                    } catch (ObjectMappingException e) {
                        e.printStackTrace();
                    }
                    player.sendMessage(Text.of(TextColors.YELLOW, "Catch level = " + catchLvl + ". Level level = " + levelLvl));

                    return CommandResult.success();
                })
                .build();

        CommandSpec main = CommandSpec.builder()
                .child(levelUp, "levelup", "lvlup")
                .child(setlvl, "setlvl", "setlevel")
                .child(getLevel, "getlvl", "getlevel")
                .child(check, "check")
                .child(addPerm, "addperm", "add")
                .child(removePerm, "remove", "removeperm")
                .child(reload, "reload")
                .executor((sender, context) -> {

                    return CommandResult.success();

                })
                .build();

        Sponge.getCommandManager().register(GCES.instance, main, "gces");

    }

}

package com.lypaka.gces.gottacatchemsmall.Utils;

import com.google.common.reflect.TypeToken;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigManager;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AccountHandler {


    /**--------------------------------Getters--------------------------------**/

    public static int getCatchTier (Player player, int index) throws ObjectMappingException {

        String mode = ConfigManager.getConfigNode(index, 6, "Catching-Levels-Mode").getString();
        if (mode.equalsIgnoreCase("levels")) {

            return ConfigManager.getPlayerConfigNode(player.getUniqueId(), "Levels", "Catching-Tier").getInt();

        } else {

            Map<String, Map<String, String>> map = ConfigManager.getConfigNode(index, 0, "Level", "Tiers").getValue(new TypeToken<Map<String, Map<String, String>>>() {});
            Map<String, String> permissionMap = ConfigManager.getConfigNode(index, 6, "Catching-Levels-Permissions").getValue(new TypeToken<Map<String, String>>() {});
            int size = map.size();
            for (int i = 1; i <= size; i++) {

                if (player.hasPermission(permissionMap.get("Tier-" + i))) {

                    return i;

                }

            }

        }

        return 0;

    }

    public static int getLevelTier (Player player, int index) throws ObjectMappingException {

        String mode = ConfigManager.getConfigNode(index, 6, "Leveling-Levels-Mode").getString();
        if (mode.equalsIgnoreCase("levels")) {

            return ConfigManager.getPlayerConfigNode(player.getUniqueId(),"Levels", "Leveling-Tier").getInt();

        } else {

            Map<String, Map<String, String>> map = ConfigManager.getConfigNode(index, 2, "Leveling", "Tiers").getValue(new TypeToken<Map<String, Map<String, String>>>() {});
            Map<String, String> permissionMap = ConfigManager.getConfigNode(index, 6, "Leveling-Levels-Permissions").getValue(new TypeToken<Map<String, String>>() {});
            int size = map.size();
            for (int i = 1; i <= size; i++) {

                if (player.hasPermission(permissionMap.get("Tier-" + i))) {

                    return i;

                }

            }

        }

        return 0;

    }

    public static List<String> getPermissions (Player player) throws ObjectMappingException {

        return ConfigManager.getPlayerConfigNode(player.getUniqueId(),"Unlocked-Permissions").getList(TypeToken.of(String.class));

    }

    public static int getTierLevel (Player player, String skill) {

        return ConfigManager.getPlayerConfigNode(player.getUniqueId(), "Levels", "Skill-Tiers", skill).getInt();

    }

    /**----------------------------------Setters------------------------------**/


    public static void setCatchTier (Player player, int tier) {

        ConfigManager.getPlayerConfigNode(player.getUniqueId(),"Levels", "Catching-Tier").setValue(tier);

    }

    public static void setLevelTier (Player player, int number) {

        ConfigManager.getPlayerConfigNode(player.getUniqueId(),"Levels", "Leveling-Tier").setValue(number);

    }

    public static void addPermission (Player player, String permission, int index) throws ObjectMappingException {

        String mode = ConfigManager.getConfigNode(index, 6, "Permission-Mode").getString();

        if (!AccountHandler.hasPermission(player, permission, index)) {

            if (mode.equalsIgnoreCase("gces")) {

                ArrayList<String> list = new ArrayList<String>(ConfigManager.getPlayerConfigNode(player.getUniqueId(),"Unlocked-Permissions").getList(TypeToken.of(String.class)));
                list.add(permission);
                ConfigManager.getPlayerConfigNode(player.getUniqueId(),"Unlocked-Permissions").setValue(list);

            } else {

                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "lp user " + player.getName() + " permission set " + permission + " true");

            }

        }

    }

    public static void removePermission (Player player, String permission, int index) throws ObjectMappingException, IOException {

        String mode = ConfigManager.getConfigNode(index, 6, "Permission-Mode").getString();

        if (AccountHandler.hasPermission(player, permission, index)) {

            if (mode.equalsIgnoreCase("gces")) {

                ArrayList<String> list = new ArrayList<String>(ConfigManager.getPlayerConfigNode(player.getUniqueId(),"Unlocked-Permissions").getList(TypeToken.of(String.class)));
                list.remove(permission);
                ConfigManager.getPlayerConfigNode(player.getUniqueId(),"Unlocked-Permissions").setValue(list);

            } else {

                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "lp user " + player.getName() + " permission unset " + permission);

            }

        }

    }

    /**----------------------------------Misc--------------------------------**/

    public static boolean hasPermission (Player player, String permission, int index) throws ObjectMappingException {

        String mode = ConfigManager.getConfigNode(index, 6, "Permission-Mode").getString();

        if (mode.equalsIgnoreCase("gces")) {

            return ConfigManager.getPlayerConfigNode(player.getUniqueId(),"Unlocked-Permissions").getList(TypeToken.of(String.class)).contains(permission);

        } else {

            return player.hasPermission(permission);

        }

    }

    public static void levelUpCatchingTier (Player player, int index) throws ObjectMappingException {


        String mode = ConfigManager.getConfigNode(index, 6, "Permission-Mode").getString();
        int level = ConfigManager.getPlayerConfigNode(player.getUniqueId(),"Levels", "Catching-Tier").getInt();
        if (level < TierHandler.getMaxTierLevel(index, "Catching")) {

            if (mode.equalsIgnoreCase("gces")) {

                ConfigManager.getPlayerConfigNode(player.getUniqueId(),"Levels", "Catching-Tier").setValue(level + 1);
                ConfigManager.savePlayer(player.getUniqueId());

            } else {

                Map<String, String> cTiers = ConfigManager.getConfigNode(index, 6, "Catching-Levels-Permissions").getValue(new TypeToken<Map<String, String>>() {});
                int totalTiers = cTiers.size();
                String currentPermission = cTiers.get("Tier-" + level);
                int nextLevel = level + 1;
                if (totalTiers <= nextLevel) {

                    Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "lp user " + player.getName() + " permission unset " + currentPermission);
                    String nextPermission = cTiers.get("Tier-" + nextLevel);
                    Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "lp user " + player.getName() + " permission set " + nextPermission + " true");

                } else {

                    player.sendMessage(Text.of(TextColors.RED, "Your level cannot go any higher!"));

                }

            }

        }

    }

    public static void levelUpLevelingTier (Player player, int index) throws ObjectMappingException {

        int level = ConfigManager.getPlayerConfigNode(player.getUniqueId(),"Levels", "Leveling-Tier").getInt();
        String mode = ConfigManager.getConfigNode(index, 6, "Permission-Mode").getString();

        if (level < TierHandler.getMaxTierLevel(index, "Leveling")) {

            if (mode.equalsIgnoreCase("gces")) {

                ConfigManager.getPlayerConfigNode(player.getUniqueId(), "Levels", "Leveling-Tier").setValue(level + 1);
                ConfigManager.savePlayer(player.getUniqueId());

            } else {

                Map<String, String> lTiers = ConfigManager.getConfigNode(index, 6, "Leveling-Levels-Permissions").getValue(new TypeToken<Map<String, String>>() {
                });
                int totalTiers = lTiers.size();
                String currentPermission = lTiers.get("Tier-" + level);
                int nextLevel = level + 1;
                if (totalTiers <= nextLevel) {

                    Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "lp user " + player.getName() + " permission unset " + currentPermission);
                    String nextPermission = lTiers.get("Tier-" + nextLevel);
                    Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "lp user " + player.getName() + " permission set " + nextPermission + " true");

                } else {

                    player.sendMessage(Text.of(TextColors.RED, "Your level cannot go any higher!"));

                }

            }

        }


    }

    public static void levelUpEvolvingTier (Player player, int index) throws ObjectMappingException, IOException {

        String mode = ConfigManager.getConfigNode(index, 6, "Permission-Mode").getString();
        if (mode.equalsIgnoreCase("gces")) {

            if (getPermissions(player).contains("gces.evolving.middle")) {

                removePermission(player, "gces.evolving.middle", index);
                addPermission(player, "gces.evolving.final", index);

            }

        } else {

            if (player.hasPermission("gces.evolving.middle")) {

                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "lp user " + player.getName() + " permission unset gces.evolving.middle");
                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "lp user " + player.getName() + " permission set gces.evolving.final true");

            }

        }


    }

    public static void levelUpCatchingEvoStage (Player player, int index) throws ObjectMappingException, IOException {

        String mode = ConfigManager.getConfigNode(index, 6, "Permission-Mode").getString();
        if (mode.equalsIgnoreCase("gces")) {

            if (getPermissions(player).contains("gces.catching.firststage")) {

                removePermission(player, "gces.catching.firststage", index);
                addPermission(player, "gces.catching.middlestage", index);

            } else if (getPermissions(player).contains("gces.catching.middlestage")) {

                removePermission(player, "gces.catching.middlestage", index);
                addPermission(player, "gces.catching.finalstage", index);

            }

        } else {

            if (player.hasPermission("gces.catching.firststage")) {

                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "lp user " + player.getName() + " permission unset gces.catching.firststage");
                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "lp user " + player.getName() + " permission set gces.catching.middlestage true");

            } else if (player.hasPermission("gces.catching.middlestage")) {

                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "lp user " + player.getName() + " permission unset gces.catching.middlestage");
                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "lp user " + player.getName() + " permission set gces.catching.finalstage true");

            }

        }

    }

    public static void setDifficulty (Player player, String diff) {

        ConfigManager.getPlayerConfigNode(player.getUniqueId(), "Difficulty").setValue(diff);
        ConfigManager.savePlayer(player.getUniqueId());

    }

    public static String getDifficulty (Player player) {

        return ConfigManager.getPlayerConfigNode(player.getUniqueId(), "Difficulty").getString();

    }

}

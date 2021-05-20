package com.lypaka.gces.gottacatchemsmall.Utils;

import com.google.common.reflect.TypeToken;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigManager;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.List;
import java.util.Map;

public class TierHandler {

    public static boolean areShiniesRestricted() {

        return ConfigManager.getConfigNode(0, "Shiny-Pokemon", "Restrict-Shinies-By-Level").getBoolean();

    }

    public static int getMaxCatchLevel (int tierNum) {

        if (tierNum == 0) {

            return 0;

        } else {

            return ConfigManager.getConfigNode(0, "Level", "Tiers", "Tier-" + tierNum, "Max-Level").getInt();

        }

    }

    public static String getCatchLevelMessage (int tierNum) {

        if (tierNum == 0) {

            return ConfigManager.getConfigNode(0, "Level", "Restriction-Default-Message").getString();

        } else {

            return ConfigManager.getConfigNode(0, "Level", "Tiers", "Tier-" + tierNum, "Message").getString();

        }

    }

    public static String getCatchLegendaryMessage() {

        return ConfigManager.getConfigNode(0, "Legendary-Pokemon", "Restriction-Default-Message").getString();

    }

    public static String getCatchEvoStageMessage() {

        return ConfigManager.getConfigNode(0, "Evolution-Stage", "Restriction-Message").getString();

    }

    public static String getCatchPermission() {

        return ConfigManager.getConfigNode(0, "Level", "Permission").getString();

    }

    public static String getLegendaryPermission() {

        return ConfigManager.getConfigNode(0, "Legendary-Pokemon", "Unlock-Legendary-Pokemon").getString();

    }

    public static boolean areLegendariesRestricted() {

        return ConfigManager.getConfigNode(0, "Legendary-Pokemon", "Restrict-Catching-Legendary-Pokemon").getBoolean();

    }

    public static boolean areEvoStagesRestricted() {

        return ConfigManager.getConfigNode(0, "Evolution-Stage", "Restrict-By-Evolution-Stage").getBoolean();

    }

    public static boolean isCatchLevelAccessRestricted() {

        return !ConfigManager.getConfigNode(0, "Level", "Permission").getString().equalsIgnoreCase("none");

    }

    public static boolean isCatchLevelRestricted() {

        return ConfigManager.getConfigNode(0, "Level", "Restrict-Levels-By-Tier").getBoolean();

    }



    /**------------------------------------Leveling Tiers----------------------------------------------------**/

    public static int getMaxLvlLevel (int tierNum) {

        if (tierNum == 0) {

            return 0;

        } else {

            return ConfigManager.getConfigNode(2, "Leveling", "Tiers", "Tier-" + tierNum, "Max-Level").getInt();

        }

    }

    public static String getLvlMessage (int tierNum) {

        if (tierNum == 0) {

            return ConfigManager.getConfigNode(2, "Leveling", "Restriction-Default-Message").getString();

        } else {

            return ConfigManager.getConfigNode(2, "Leveling", "Tiers", "Tier-" + tierNum, "Message").getString();

        }

    }

    public static String getLevelPermission() {

        return ConfigManager.getConfigNode(2, "Leveling", "Permission").getString();

    }

    public static boolean isLevelingSystemEnabled() {

        return ConfigManager.getConfigNode(2, "Leveling", "Enable-Progression-Based-Leveling-System").getBoolean();

    }

    public static boolean restrictBattles() {

        return ConfigManager.getConfigNode(2, "Battles", "Restrict-Battles").getBoolean();

    }

    public static String getBattleMessage() {

        return ConfigManager.getConfigNode(2, "Battles", "Restrict-Battles-Message").getString();

    }

    /**--------------------------------------Trading Tiers-------------------------------------**/

    public static String getTierBase() {

        return ConfigManager.getConfigNode(3, "Trading", "Lock-Trade-Tiers-By").getString();

    }

    public static String getTradePerm() {

        return ConfigManager.getConfigNode(3, "Trading", "Permission").getString();

    }

    public static boolean areTradesModified() {

        return ConfigManager.getConfigNode(3, "Trading", "Enable-Progression-Based-Trade-System").getBoolean();

    }

    public static String getTradeMessage() {

        return ConfigManager.getConfigNode(3, "Trading", "Restriction-Default-Message").getString();

    }

    public static boolean restrictLegendaries() {

        return ConfigManager.getConfigNode(3, "Trading", "Legendaries", "Enabled").getBoolean();

    }

    public static String getLegendaryMessage() {

        return ConfigManager.getConfigNode(3, "Trading", "Legendaries", "Message").getString();

    }

    /**---------------------------------------Evolution Tiers-----------------------------------**/

    public static boolean areEvolutionsRestricted() {

        return ConfigManager.getConfigNode(1, "Evolving", "Restrict-Evolutions").getBoolean();

    }

    public static String getEvoStage (EntityPixelmon pokemon) {

        // Pokemon has no pre-evolutions and can evolve, Pokemon is baby-stage
        if (pokemon.getBaseStats().preEvolutions.length == 0 && pokemon.getBaseStats().evolutions.size() != 0) {
            return "First";
        }

        // Pokemon has pre-evolutions and can evolve, Pokemon is middle-stage
        if (pokemon.getBaseStats().preEvolutions.length != 0 && pokemon.getBaseStats().evolutions.size() != 0) {
            return "Middle";
        }

        // Pokemon has pre-evolutions and can not evolve, Pokemon is final-stage
        if (pokemon.getBaseStats().preEvolutions.length != 0 && pokemon.getBaseStats().evolutions.size() == 0) {
            return "Final";
        }

        // Pokemon has no pre-evolutions and can not evolve, Pokemon is single-stage
        if (pokemon.getBaseStats().preEvolutions.length == 0 && pokemon.getBaseStats().evolutions.size() == 0) {
            return "Single";
        }
        return "None";
    }

    public static String getEvoPermission() {

        return ConfigManager.getConfigNode(1, "Evolving", "Permission").getString();

    }

    public static String getEvoRestrictionMessage() {

        return ConfigManager.getConfigNode(1, "Evolving", "Restriction-Default-Message").getString();

    }

    public static String getMegaPerm() {

        return ConfigManager.getConfigNode(1, "Mega-Evolving", "Unlock-Mega-Evolving").getString();

    }

    public static String getMegaMessage() {

        return ConfigManager.getConfigNode(1, "Mega-Evolving", "Restriction-Message").getString();

    }

    public static boolean areMegasRestricted() {

        return ConfigManager.getConfigNode(1, "Mega-Evolving", "Restrict-Mega-Evolutions").getBoolean();

    }

    /**--------------------------------------Z-Moves------------------------------------**/

    public static boolean restrictDynamaxing() {

        return ConfigManager.getConfigNode(4, "Dynamaxing", "Restrict-Dynamaxing").getBoolean();

    }

    public static String getDynamaxingMessage() {

        return ConfigManager.getConfigNode(4, "Dynamaxing", "Restriction-Message").getString();

    }

    public static String getDynamaxingPermission() {

        return ConfigManager.getConfigNode(4, "Dynamaxing", "Unlock-Z-Moves").getString();

    }

    public static boolean unlockDynamaxingOnJoin() {

        return ConfigManager.getConfigNode(4, "Dynamaxing", "Unlock-Permission-Given-On-Join").getBoolean();

    }

    /**-------------------------------------General--------------------------------------**/

    public static String getStringFromConfig (ConfigurationNode node) {
        return node.getString();
    }

    public static int getMaxTierLevel (String tier) throws ObjectMappingException {

        if (tier.equals("Catching")) {

            Map<String, Map<String, String>> map = ConfigManager.getConfigNode(0, "Level", "Tiers").getValue(new TypeToken<Map<String, Map<String, String>>>() {});
            return map.size();

        } else if (tier.equals("Leveling")) {

            Map<String, Map<String, String>> map = ConfigManager.getConfigNode(2, "Leveling", "Tiers").getValue(new TypeToken<Map<String, Map<String, String>>>() {});
            return map.size();

        }

        return 0;

    }

    public static boolean doIndividualLegendaries() {

        return ConfigManager.getConfigNode(5, "Individual-Unlocking", "Enabled").getBoolean();

    }

    public static List<String> getGroups() throws ObjectMappingException {

        return ConfigManager.getConfigNode(5, "Individual-Unlocking", "Permissions", "Permission-Groups").getList(TypeToken.of(String.class));

    }

    public static List<String> getLegendaries (String group) throws ObjectMappingException {

        return ConfigManager.getConfigNode(5, "Individual-Unlocking", "Permissions", "Unlock", group).getList(TypeToken.of(String.class));

    }

}

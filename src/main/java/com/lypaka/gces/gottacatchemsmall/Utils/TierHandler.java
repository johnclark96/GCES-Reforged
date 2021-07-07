package com.lypaka.gces.gottacatchemsmall.Utils;

import com.google.common.reflect.TypeToken;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigManager;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.List;
import java.util.Map;

public class TierHandler {

    public static boolean areShiniesRestricted (int index) {

        return ConfigManager.getConfigNode(index, 0, "Shiny-Pokemon", "Restrict-Shinies-By-Level").getBoolean();

    }

    public static int getMaxCatchLevel (int index, int tierNum) {

        if (tierNum == 0) {

            return 0;

        } else {

            return ConfigManager.getConfigNode(index, 0, "Level", "Tiers", "Tier-" + tierNum, "Max-Level").getInt();

        }

    }

    public static String getCatchLevelMessage (int index, int tierNum) {

        if (tierNum == 0) {

            return ConfigManager.getConfigNode(index, 0, "Level", "Restriction-Default-Message").getString();

        } else {

            return ConfigManager.getConfigNode(index, 0, "Level", "Tiers", "Tier-" + tierNum, "Message").getString();

        }

    }

    public static String getCatchLegendaryMessage (int index) {

        return ConfigManager.getConfigNode(index, 0, "Legendary-Pokemon", "Restriction-Default-Message").getString();

    }

    public static String getCatchEvoStageMessage (int index) {

        return ConfigManager.getConfigNode(index, 0, "Evolution-Stage", "Restriction-Message").getString();

    }

    public static String getCatchPermission (int index) {

        return ConfigManager.getConfigNode(index, 0, "Level", "Permission").getString();

    }

    public static String getLegendaryPermission (int index) {

        return ConfigManager.getConfigNode(index, 0, "Legendary-Pokemon", "Unlock-Legendary-Pokemon").getString();

    }

    public static boolean areLegendariesRestricted (int index) {

        return ConfigManager.getConfigNode(index, 0, "Legendary-Pokemon", "Restrict-Catching-Legendary-Pokemon").getBoolean();

    }

    public static boolean areEvoStagesRestricted (int index) {

        return ConfigManager.getConfigNode(index, 0, "Evolution-Stage", "Restrict-By-Evolution-Stage").getBoolean();

    }

    public static boolean isCatchLevelAccessRestricted (int index) {

        return !ConfigManager.getConfigNode(index, 0, "Level", "Permission").getString().equalsIgnoreCase("none");

    }

    public static boolean isCatchLevelRestricted (int index) {

        return ConfigManager.getConfigNode(index, 0, "Level", "Restrict-Levels-By-Tier").getBoolean();

    }



    /**------------------------------------Leveling Tiers----------------------------------------------------**/

    public static int getMaxLvlLevel (int index, int tierNum) {

        if (tierNum == 0) {

            return 0;

        } else {

            return ConfigManager.getConfigNode(index, 2, "Leveling", "Tiers", "Tier-" + tierNum, "Max-Level").getInt();

        }

    }

    public static String getLvlMessage (int index, int tierNum) {

        if (tierNum == 0) {

            return ConfigManager.getConfigNode(index, 2, "Leveling", "Restriction-Default-Message").getString();

        } else {

            return ConfigManager.getConfigNode(index, 2, "Leveling", "Tiers", "Tier-" + tierNum, "Message").getString();

        }

    }

    public static String getLevelPermission (int index) {

        return ConfigManager.getConfigNode(index, 2, "Leveling", "Permission").getString();

    }

    public static boolean isLevelingSystemEnabled (int index) {

        return ConfigManager.getConfigNode(index, 2, "Leveling", "Enable-Progression-Based-Leveling-System").getBoolean();

    }

    public static boolean restrictBattles (int index) {

        return ConfigManager.getConfigNode(index, 2, "Battles", "Restrict-Battles").getBoolean();

    }

    public static String getBattleMessage (int index) {

        return ConfigManager.getConfigNode(index, 2, "Battles", "Restrict-Battles-Message").getString();

    }

    /**--------------------------------------Trading Tiers-------------------------------------**/

    public static String getTierBase (int index) {

        return ConfigManager.getConfigNode(index, 3, "Trading", "Lock-Trade-Tiers-By").getString();

    }

    public static String getTradePerm (int index) {

        return ConfigManager.getConfigNode(index, 3, "Trading", "Permission").getString();

    }

    public static boolean areTradesModified (int index) {

        return ConfigManager.getConfigNode(index, 3, "Trading", "Enable-Progression-Based-Trade-System").getBoolean();

    }

    public static String getTradeMessage (int index) {

        return ConfigManager.getConfigNode(index, 3, "Trading", "Restriction-Default-Message").getString();

    }

    public static boolean restrictLegendaries (int index) {

        return ConfigManager.getConfigNode(index, 3, "Trading", "Legendaries", "Enabled").getBoolean();

    }

    public static String getLegendaryMessage (int index) {

        return ConfigManager.getConfigNode(index, 3, "Trading", "Legendaries", "Message").getString();

    }

    /**---------------------------------------Evolution Tiers-----------------------------------**/

    public static boolean areEvolutionsRestricted (int index) {

        return ConfigManager.getConfigNode(index, 1, "Evolving", "Restrict-Evolutions").getBoolean();

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

    public static String getEvoPermission (int index) {

        return ConfigManager.getConfigNode(index, 1, "Evolving", "Permission").getString();

    }

    public static String getEvoRestrictionMessage (int index) {

        return ConfigManager.getConfigNode(index, 1, "Evolving", "Restriction-Default-Message").getString();

    }

    public static String getMegaPerm (int index) {

        return ConfigManager.getConfigNode(index, 1, "Mega-Evolving", "Unlock-Mega-Evolving").getString();

    }

    public static String getMegaMessage (int index) {

        return ConfigManager.getConfigNode(index, 1, "Mega-Evolving", "Restriction-Message").getString();

    }

    public static boolean areMegasRestricted (int index) {

        return ConfigManager.getConfigNode(index, 1, "Mega-Evolving", "Restrict-Mega-Evolutions").getBoolean();

    }

    /**--------------------------------------Z-Moves------------------------------------**/

    public static boolean restrictZMoves (int index) {

        return ConfigManager.getConfigNode(index, 4, "Z-Moves", "Restrict-Z-Moves").getBoolean();

    }

    public static String getZMovesMessage (int index) {

        return ConfigManager.getConfigNode(index, 4, "Z-Moves", "Restriction-Message").getString();

    }

    public static String getZMovesPermission (int index) {

        return ConfigManager.getConfigNode(index, 4, "Z-Moves", "Unlock-Z-Moves").getString();

    }

    public static boolean unlockZMovesOnJoin (int index) {

        return ConfigManager.getConfigNode(index, 4, "Z-Moves", "Unlock-Permission-Given-On-Join").getBoolean();

    }

    /** ------------------------------------Dynamaxing-----------------------------------**/

    public static boolean restrictDynamaxing (int index) {

        return ConfigManager.getConfigNode(index, 8, "Dynamaxing", "Restrict-Dynamaxing").getBoolean();

    }

    public static String getDynamaxingMessage (int index) {

        return ConfigManager.getConfigNode(index, 8, "Dynamaxing", "Restriction-Message").getString();

    }

    public static String getDynamaxingPermission (int index) {

        return ConfigManager.getConfigNode(index, 8, "Dynamaxing", "Unlock-Dynamaxing").getString();

    }

    public static boolean unlockDynamaxingOnJoin (int index) {

        return ConfigManager.getConfigNode(index, 8, "Dynamaxing", "Unlock-Permission-Given-On-Join").getBoolean();

    }

    /**-------------------------------------General--------------------------------------**/

    public static int getMaxTierLevel (int index, String tier) throws ObjectMappingException {

        if (tier.equalsIgnoreCase("Catching")) {

            Map<String, Map<String, String>> map = ConfigManager.getConfigNode(index, 0, "Level", "Tiers").getValue(new TypeToken<Map<String, Map<String, String>>>() {});
            return map.size();

        } else if (tier.equalsIgnoreCase("Leveling")) {

            Map<String, Map<String, String>> map = ConfigManager.getConfigNode(index, 2, "Leveling", "Tiers").getValue(new TypeToken<Map<String, Map<String, String>>>() {});
            return map.size();

        }

        return 0;

    }

    public static boolean doIndividualLegendaries (int index) {

        return ConfigManager.getConfigNode(index, 5, "Individual-Unlocking", "Enabled").getBoolean();

    }

    public static List<String> getGroups (int index) throws ObjectMappingException {

        return ConfigManager.getConfigNode(index, 5, "Individual-Unlocking", "Permissions", "Permission-Groups").getList(TypeToken.of(String.class));

    }

    public static List<String> getLegendaries (int index, String group) throws ObjectMappingException {

        return ConfigManager.getConfigNode(index, 5, "Individual-Unlocking", "Permissions", "Unlock", group).getList(TypeToken.of(String.class));

    }

}

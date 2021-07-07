package com.lypaka.gces.gottacatchemsmall.Config;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigGetters {

    public static boolean difficultiesEnabled() throws ObjectMappingException {

        Map<String, Boolean> dMap = ConfigManager.getBaseNode(0, "Difficulties").getValue(new TypeToken<Map<String, Boolean>>() {});
        boolean enabled = false;
        for (Map.Entry<String, Boolean> entry : dMap.entrySet()) {

            if (entry.getValue()) {

                enabled = true;
                break;

            }

        }

        return enabled;

    }

    public static List<String> getEnabledDifficulties() throws ObjectMappingException {

        Map<String, Boolean> dMap = ConfigManager.getBaseNode(0, "Difficulties").getValue(new TypeToken<Map<String, Boolean>>() {});
        List<String> difficulties = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : dMap.entrySet()) {

            if (entry.getValue()) {

                difficulties.add(entry.getKey());

            }

        }

        return difficulties;

    }

    public static boolean isDifficultyEnabled (String diff) throws ObjectMappingException {

        if (diff.equalsIgnoreCase("Default")) return true;

        Map<String, Boolean> dMap = ConfigManager.getBaseNode(0, "Difficulties-Enabled").getValue(new TypeToken<Map<String, Boolean>>() {});
        for (Map.Entry<String, Boolean> entry : dMap.entrySet()) {

            if (diff.equalsIgnoreCase(entry.getKey())) {

                return entry.getValue();

            }

        }

        return false;

    }

    public static int getIndexFromString (String diff) {

        switch (diff.toLowerCase()) {

            case "easy":
                return 0;

            case "medium":
                return 1;

            case "hard":
                return 2;

            default:
                return 3;

        }

    }

    public static String getStringFromIndex (int index) {

        switch (index) {

            case 0:
                return "Easy";

            case 1:
                return "Medium";

            case 2:
                return "Hard";

            default:
                return "Default";

        }

    }

    public static String getPlayerDifficulty (Player player) {

        return ConfigManager.getPlayerConfigNode(player.getUniqueId(), "Difficulty").getString();

    }

}

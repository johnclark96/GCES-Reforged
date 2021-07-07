package com.lypaka.gces.gottacatchemsmall.Listeners;

import com.lypaka.gces.gottacatchemsmall.Config.ConfigGetters;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigManager;
import com.lypaka.gces.gottacatchemsmall.GCES;
import com.lypaka.gces.gottacatchemsmall.Utils.AccountHandler;
import com.lypaka.gces.gottacatchemsmall.Utils.TierHandler;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import java.io.IOException;

public class JoinListener {

    @Listener
    public void onFirstJoin (ClientConnectionEvent.Join event, @Root Player player) {

        ConfigManager.loadPlayer(player.getUniqueId());
        if (ConfigManager.getPlayerConfigNode(player.getUniqueId(), "Difficulty").isVirtual()) {

            ConfigManager.getPlayerConfigNode(player.getUniqueId(), "Difficulty").setValue("none");
            ConfigManager.savePlayer(player.getUniqueId());

        }

    }

    public static void generateAccount (Player player) throws ObjectMappingException, IOException {

        if (!ConfigManager.getBaseNode(0, "Restriction-Optional").getBoolean()) {

            String defaultDiff = ConfigManager.getBaseNode(0, "Force-Difficulty").getString().toLowerCase();
            ConfigManager.getPlayerConfigNode(player.getUniqueId(), "Difficulty").setValue(defaultDiff);
            int index = ConfigGetters.getIndexFromString(defaultDiff);
            if (!ConfigGetters.isDifficultyEnabled(defaultDiff)) {

                index = ConfigGetters.getIndexFromString("default");
                GCES.getLogger().error("Trying to use a difficulty not enabled as the default difficulty, using the Default difficulty instead!");

            }

            if (ConfigManager.getConfigNode(index, 0, "Level", "Auto-Set-To-First-Level").getBoolean()) {

                AccountHandler.setCatchTier(player, 1);

            } else {

                AccountHandler.setCatchTier(player, 0);

            }

            if (ConfigManager.getConfigNode(index, 0, "Level", "Give-Unlock-Permission-On-First-Join").getBoolean()) {

                AccountHandler.addPermission(player, ConfigManager.getConfigNode(index, 0, "Level", "Permission").getString(), index);

            }

            if (ConfigManager.getConfigNode(index, 1, "Evolving", "Give-Unlock-Permission-On-First-Join").getBoolean()) {

                AccountHandler.addPermission(player, ConfigManager.getConfigNode(index, 1, "Evolving", "Permission").getString(), index);

            }

            if (ConfigManager.getConfigNode(index, 1, "Evolving", "Auto-Unlock-First-Stage-Evolutions").getBoolean()) {

                AccountHandler.addPermission(player, "gces.evolving.firststage", index);

            }


            if (ConfigManager.getConfigNode(index, 2, "Leveling", "Auto-Set-To-First-Level").getBoolean()) {

                AccountHandler.setLevelTier(player, 1);

            } else {

                AccountHandler.setLevelTier(player, 0);

            }

            if (ConfigManager.getConfigNode(index, 2, "Leveling", "Give-Unlock-Permission-On-First-Join").getBoolean()) {

                AccountHandler.addPermission(player, ConfigManager.getConfigNode(index, 2, "Leveling", "Permission").getString(), index);

            }

            if (ConfigManager.getConfigNode(index, 3, "Trading", "Give-Unlock-Permission-On-First-Join").getBoolean()) {

                AccountHandler.addPermission(player, ConfigManager.getConfigNode(index, 3, "Trading", "Permission").getString(), index);

            }

            if (ConfigManager.getConfigNode(index, 1, "Mega-Evolving", "Unlock-Permission-Given-On-Join").getBoolean()) {

                AccountHandler.addPermission(player, ConfigManager.getConfigNode(index, 1, "Mega-Evolving", "Unlock-Mega-Evolving").getString(), index);

            }

            if (TierHandler.unlockZMovesOnJoin(index)) {

                AccountHandler.addPermission(player, TierHandler.getZMovesPermission(index), index);

            }

            if (TierHandler.unlockDynamaxingOnJoin(index)) {

                AccountHandler.addPermission(player, TierHandler.getDynamaxingPermission(index), index);

            }

            ConfigManager.savePlayer(player.getUniqueId());

        }

    }

    public static void generateAccount (Player player, String diff) throws ObjectMappingException, IOException {

        int index = ConfigGetters.getIndexFromString(diff);
        if (!ConfigGetters.isDifficultyEnabled(diff)) {

            index = ConfigGetters.getIndexFromString("default");
            diff = "Default";
            GCES.getLogger().error("Trying to use a difficulty not enabled as the default difficulty, using the Default difficulty instead!");

        }

        ConfigManager.getPlayerConfigNode(player.getUniqueId(), "Difficulty").setValue(diff);

        if (ConfigManager.getConfigNode(index, 0, "Level", "Auto-Set-To-First-Level").getBoolean()) {

            AccountHandler.setCatchTier(player, 1);

        } else {

            AccountHandler.setCatchTier(player, 0);

        }

        if (ConfigManager.getConfigNode(index, 0, "Level", "Give-Unlock-Permission-On-First-Join").getBoolean()) {

            AccountHandler.addPermission(player, ConfigManager.getConfigNode(index, 0, "Level", "Permission").getString(), index);

        }

        if (ConfigManager.getConfigNode(index, 1, "Evolving", "Give-Unlock-Permission-On-First-Join").getBoolean()) {

            AccountHandler.addPermission(player, ConfigManager.getConfigNode(index, 1, "Evolving", "Permission").getString(), index);

        }

        if (ConfigManager.getConfigNode(index, 1, "Evolving", "Auto-Unlock-First-Stage-Evolutions").getBoolean()) {

            AccountHandler.addPermission(player, "gces.evolving.firststage", index);

        }


        if (ConfigManager.getConfigNode(index, 2, "Leveling", "Auto-Set-To-First-Level").getBoolean()) {

            AccountHandler.setLevelTier(player, 1);

        } else {

            AccountHandler.setLevelTier(player, 0);

        }

        if (ConfigManager.getConfigNode(index, 2, "Leveling", "Give-Unlock-Permission-On-First-Join").getBoolean()) {

            AccountHandler.addPermission(player, ConfigManager.getConfigNode(index, 2, "Leveling", "Permission").getString(), index);

        }

        if (ConfigManager.getConfigNode(index, 3, "Trading", "Give-Unlock-Permission-On-First-Join").getBoolean()) {

            AccountHandler.addPermission(player, ConfigManager.getConfigNode(index, 3, "Trading", "Permission").getString(), index);

        }

        if (ConfigManager.getConfigNode(index, 1, "Mega-Evolving", "Unlock-Permission-Given-On-Join").getBoolean()) {

            AccountHandler.addPermission(player, ConfigManager.getConfigNode(index, 1, "Mega-Evolving", "Unlock-Mega-Evolving").getString(), index);

        }

        if (TierHandler.unlockZMovesOnJoin(index)) {

            AccountHandler.addPermission(player, TierHandler.getZMovesPermission(index), index);

        }

        if (TierHandler.unlockDynamaxingOnJoin(index)) {

            AccountHandler.addPermission(player, TierHandler.getDynamaxingPermission(index), index);

        }

        ConfigManager.savePlayer(player.getUniqueId());

    }

}

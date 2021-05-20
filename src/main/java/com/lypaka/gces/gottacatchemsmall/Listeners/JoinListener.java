package com.lypaka.gces.gottacatchemsmall.Listeners;

import com.lypaka.gces.gottacatchemsmall.Config.ConfigManager;
import com.lypaka.gces.gottacatchemsmall.GCES;
import com.lypaka.gces.gottacatchemsmall.Utils.AccountHandler;
import com.lypaka.gces.gottacatchemsmall.Utils.TierHandler;
import com.lypaka.pixelskills.PixelSkills;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JoinListener {

    @Listener
    public void onFirstJoin (ClientConnectionEvent.Join event, @Root Player player) {

        ConfigManager.loadPlayer(player.getUniqueId());
        if (GCES.isPixelSkillsLoaded) {

            if (ConfigManager.getPlayerConfigNode(player.getUniqueId(), "Levels", "Skill-Tiers", "Archaeologist").isVirtual()) {

                Map<String, Integer> map = new HashMap<>();
                for (String skill : PixelSkills.skills) {

                    map.put(skill, 1);

                }

                ConfigManager.getPlayerConfigNode(player.getUniqueId(), "Levels", "Skill-Tiers").setValue(map);
                ConfigManager.savePlayer(player.getUniqueId());

            }

        }

    }

    public static void generateAccount (Player player) throws ObjectMappingException, IOException {

        if (ConfigManager.getConfigNode(0, "Level", "Auto-Set-To-First-Level").getBoolean()) {

            AccountHandler.setCatchTier(player, 1);

        } else {

            AccountHandler.setCatchTier(player, 0);

        }

        if (ConfigManager.getConfigNode(0, "Level", "Give-Unlock-Permission-On-First-Join").getBoolean()) {

            AccountHandler.addPermission(player, ConfigManager.getConfigNode(0, "Level", "Permission").getString());

        }

        if (ConfigManager.getConfigNode(1, "Evolving", "Give-Unlock-Permission-On-First-Join").getBoolean()) {

            AccountHandler.addPermission(player, ConfigManager.getConfigNode(1, "Evolving", "Permission").getString());

        }

        if (ConfigManager.getConfigNode(1, "Evolving", "Auto-Unlock-First-Stage-Evolutions").getBoolean()) {

            AccountHandler.addPermission(player, "gces.evolving.firststage");

        }


        if (ConfigManager.getConfigNode(2, "Leveling", "Auto-Set-To-First-Level").getBoolean()) {

            AccountHandler.setLevelTier(player, 1);

        } else {

            AccountHandler.setLevelTier(player, 0);

        }

        if (ConfigManager.getConfigNode(2, "Leveling", "Give-Unlock-Permission-On-First-Join").getBoolean()) {

            AccountHandler.addPermission(player, ConfigManager.getConfigNode(2, "Leveling", "Permission").getString());

        }

        if (ConfigManager.getConfigNode(3, "Trading", "Give-Unlock-Permission-On-First-Join").getBoolean()) {

            AccountHandler.addPermission(player, ConfigManager.getConfigNode(3, "Trading", "Permission").getString());

        }

        if (ConfigManager.getConfigNode(1, "Mega-Evolving", "Unlock-Permission-Given-On-Join").getBoolean()) {

            AccountHandler.addPermission(player, ConfigManager.getConfigNode(3, "Mega-Evolving", "Unlock-Mega-Evolving").getString());

        }

        if (TierHandler.unlockDynamaxingOnJoin()) {

            AccountHandler.addPermission(player, TierHandler.getDynamaxingPermission());

        }

        ConfigManager.savePlayer(player.getUniqueId());

    }

}

package com.lypaka.gces.gottacatchemsmall.Listeners;

import com.google.common.reflect.TypeToken;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigGetters;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigManager;
import com.lypaka.gces.gottacatchemsmall.Utils.AccountHandler;
import com.lypaka.gces.gottacatchemsmall.Utils.FancyText;
import com.lypaka.pixelskills.Utils.AccountsHandler;
import com.lypaka.pixelskills.Utils.CustomEvents.SkillExperienceEvent;
import com.lypaka.pixelskills.Utils.CustomEvents.SkillLevelUpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.List;

public class SkillsListener {

    // 7

    @SubscribeEvent
    public void onSkillEXP (SkillExperienceEvent event) throws ObjectMappingException {

        String skill = event.getSkill();
        Player player = event.getPlayer();
        if (ConfigGetters.getPlayerDifficulty(player).equalsIgnoreCase("none")) return;

        int index = ConfigGetters.getIndexFromString(ConfigGetters.getPlayerDifficulty(player));
        if (!ConfigManager.getConfigNode(index, 6, "World-Blacklist").isEmpty()) {

            List<String> worlds = ConfigManager.getConfigNode(index, 6, "World-Blacklist").getList(TypeToken.of(String.class));
            World world = player.getWorld();
            if (worlds.contains(world.getName())) return;

        }
        if (ConfigManager.getConfigNode(index, 7, "Skills", "Enable-Skill-Restriction").getBoolean()) {

            List<String> blackList = ConfigManager.getConfigNode(index, 7, "Skills", "Blacklist").getList(TypeToken.of(String.class));
            if (blackList.contains(skill)) {

                return;

            }

            if (!ConfigManager.getConfigNode(index, 7, "Skills", "Tiers", skill).isVirtual()) {

                int tierLevel = AccountHandler.getTierLevel(player, skill);
                int capLevel = ConfigManager.getConfigNode(index, 7, "Skills", "Tiers", skill, "Tier-" + tierLevel, "Cap-Level").getInt();
                int skillLevel = AccountsHandler.getLevel(skill, player);
                String message = ConfigManager.getConfigNode(index, 7, "Skills", "Tiers", skill, "Tier-" + tierLevel, "Message").getString();

                if (skillLevel >= capLevel) {

                    event.setCanceled(true);
                    player.sendMessage(FancyText.getFancyText(message));

                }

            }

        }

    }

    @SubscribeEvent
    public void onSkillLvlUp (SkillLevelUpEvent event) throws ObjectMappingException {

        String skill = event.getSkill();
        Player player = event.getPlayer();
        if (ConfigGetters.getPlayerDifficulty(player).equalsIgnoreCase("none")) return;

        int index = ConfigGetters.getIndexFromString(ConfigGetters.getPlayerDifficulty(player));
        if (!ConfigManager.getConfigNode(index, 7, "World-Blacklist").isEmpty()) {

            List<String> worlds = ConfigManager.getConfigNode(index, 7, "World-Blacklist").getList(TypeToken.of(String.class));
            World world = player.getWorld();
            if (worlds.contains(world.getName())) return;

        }

        if (ConfigManager.getConfigNode(index, 7, "Skills", "Enable-Skill-Restriction").getBoolean()) {

            List<String> blackList = ConfigManager.getConfigNode(index, 7, "Skills", "Blacklist").getList(TypeToken.of(String.class));
            if (blackList.contains(skill)) {

                return;

            }

            if (!ConfigManager.getConfigNode(index, 7, "Skills", "Tiers", skill).isVirtual()) {

                int tierLevel = AccountHandler.getTierLevel(player, skill);
                int capLevel = ConfigManager.getConfigNode(index, 7, "Skills", "Tiers", skill, "Tier-" + tierLevel, "Cap-Level").getInt();
                int skillLevel = AccountsHandler.getLevel(skill, player);
                String message = ConfigManager.getConfigNode(index, 7, "Skills", "Tiers", skill, "Tier-" + tierLevel, "Message").getString();

                if (skillLevel >= capLevel) {

                    event.setCanceled(true);
                    player.sendMessage(FancyText.getFancyText(message));

                }

            }

        }

    }

}

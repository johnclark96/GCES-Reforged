package com.lypaka.gces.gottacatchemsmall.Listeners;

import com.google.common.reflect.TypeToken;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigGetters;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigManager;
import com.lypaka.gces.gottacatchemsmall.Utils.AccountHandler;
import com.lypaka.gces.gottacatchemsmall.Utils.FancyText;
import com.lypaka.gces.gottacatchemsmall.Utils.TierHandler;
import com.pixelmonmod.pixelmon.api.events.ExperienceGainEvent;
import com.pixelmonmod.pixelmon.api.events.LevelUpEvent;
import com.pixelmonmod.pixelmon.api.events.RareCandyEvent;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.List;

public class LevelingListener {

    @SubscribeEvent
    public void onLevelUp (LevelUpEvent event) throws ObjectMappingException {

        Player player = (Player) event.player;
        if (ConfigGetters.getPlayerDifficulty(player).equalsIgnoreCase("none")) return;

        int index = ConfigGetters.getIndexFromString(ConfigGetters.getPlayerDifficulty(player));
        if (!ConfigManager.getConfigNode(index, 6, "World-Blacklist").isEmpty()) {

            List<String> worlds = ConfigManager.getConfigNode(index, 6, "World-Blacklist").getList(TypeToken.of(String.class));
            World world = player.getWorld();
            if (worlds.contains(world.getName())) return;

        }
        int pokeLevel = event.pokemon.getLevel();
        int level = AccountHandler.getLevelTier(player, index);

        if (pokeLevel < PixelmonConfig.maxLevel) {

            if (!AccountHandler.hasPermission(player, TierHandler.getLevelPermission(index), index)) {

                if (!TierHandler.getLevelPermission(index).equalsIgnoreCase("none")) {

                    event.setCanceled(true);
                    player.sendMessage(FancyText.getFancyText(TierHandler.getLvlMessage(index, 0)));

                }

            } else {

                if (TierHandler.isLevelingSystemEnabled(index)) {

                    if (pokeLevel >= TierHandler.getMaxLvlLevel(index, level)) {

                        event.setCanceled(true);
                        player.sendMessage(FancyText.getFancyText(TierHandler.getLvlMessage(index, level)));

                    }

                }

            }

        }


    }


    @SubscribeEvent
    public void onRareCandy (RareCandyEvent event) throws ObjectMappingException {

        Player player = (Player) event.player;
        if (ConfigGetters.getPlayerDifficulty(player).equalsIgnoreCase("none")) return;

        int index = ConfigGetters.getIndexFromString(ConfigGetters.getPlayerDifficulty(player));
        if (!ConfigManager.getConfigNode(index, 6, "World-Blacklist").isEmpty()) {

            List<String> worlds = ConfigManager.getConfigNode(index, 6, "World-Blacklist").getList(TypeToken.of(String.class));
            World world = player.getWorld();
            if (worlds.contains(world.getName())) return;

        }
        //int pokeLevel = event.getPokemon.level.getLevel();
        int pokeLevel = event.pixelmon.getPokemonData().getLevel();
        int level = AccountHandler.getLevelTier(player, index);

        if (pokeLevel < PixelmonConfig.maxLevel) {

            if (!AccountHandler.hasPermission(player, TierHandler.getLevelPermission(index), index)) {

                if (!TierHandler.getLevelPermission(index).equalsIgnoreCase("none")) {

                    event.setCanceled(true);
                    player.sendMessage(FancyText.getFancyText(TierHandler.getLvlMessage(index, 0)));

                }

            } else {

                if (TierHandler.isLevelingSystemEnabled(index)) {

                    if (pokeLevel >= TierHandler.getMaxLvlLevel(index, level)) {

                        event.setCanceled(true);
                        player.sendMessage(FancyText.getFancyText(TierHandler.getLvlMessage(index, level)));

                    }

                }

            }

        }

    }

    @SubscribeEvent
    public void onEXPGain (ExperienceGainEvent event) throws ObjectMappingException {

        Player player = (Player) event.pokemon.getPlayerOwner();
        if (ConfigGetters.getPlayerDifficulty(player).equalsIgnoreCase("none")) return;

        int index = ConfigGetters.getIndexFromString(ConfigGetters.getPlayerDifficulty(player));
        if (!ConfigManager.getConfigNode(index, 6, "World-Blacklist").isEmpty()) {

            List<String> worlds = ConfigManager.getConfigNode(index, 6, "World-Blacklist").getList(TypeToken.of(String.class));
            World world = player.getWorld();
            if (worlds.contains(world.getName())) return;

        }
        int level = AccountHandler.getLevelTier(player, index);
        int pokeLevel = event.pokemon.getLevel();

        if (pokeLevel < PixelmonConfig.maxLevel) {

            if (TierHandler.isLevelingSystemEnabled(index)) {

                if (pokeLevel >= TierHandler.getMaxLvlLevel(index, level)) {

                    event.setExperience(0);
                    player.sendMessage(FancyText.getFancyText(TierHandler.getLvlMessage(index, level).replace("Pokemon", event.pokemon.getBaseStats().pixelmonName)));

                }

            }

        }

    }

}

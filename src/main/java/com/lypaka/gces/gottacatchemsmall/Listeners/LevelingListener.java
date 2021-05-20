package com.lypaka.gces.gottacatchemsmall.Listeners;

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

public class LevelingListener {

    @SubscribeEvent
    public void onLevelUp (LevelUpEvent event) throws ObjectMappingException {

        Player player = (Player) event.player;
        int pokeLevel = event.pokemon.getPokemon().getLevel();
        int level = AccountHandler.getLevelTier(player);

        if (pokeLevel < PixelmonConfig.maxLevel) {

            if (!AccountHandler.hasPermission(player, TierHandler.getLevelPermission())) {

                if (!TierHandler.getLevelPermission().equalsIgnoreCase("none")) {

                    event.setCanceled(true);
                    player.sendMessage(FancyText.getFancyText(TierHandler.getLvlMessage(0)));

                }

            } else {

                if (TierHandler.isLevelingSystemEnabled()) {

                    if (pokeLevel >= TierHandler.getMaxLvlLevel(level)) {

                        event.setCanceled(true);
                        player.sendMessage(FancyText.getFancyText(TierHandler.getLvlMessage(level)));

                    }

                }

            }

        }


    }


    @SubscribeEvent
    public void onRareCandy (RareCandyEvent event) throws ObjectMappingException {

        Player player = (Player) event.player;
        int pokeLevel = event.pixelmon.getLvl().getLevel();
        int level = AccountHandler.getLevelTier(player);

        if (pokeLevel < PixelmonConfig.maxLevel) {

            if (!AccountHandler.hasPermission(player, TierHandler.getLevelPermission())) {

                if (!TierHandler.getLevelPermission().equalsIgnoreCase("none")) {

                    event.setCanceled(true);
                    player.sendMessage(FancyText.getFancyText(TierHandler.getLvlMessage(0)));

                }

            } else {

                if (TierHandler.isLevelingSystemEnabled()) {

                    if (pokeLevel >= TierHandler.getMaxLvlLevel(level)) {

                        event.setCanceled(true);
                        player.sendMessage(FancyText.getFancyText(TierHandler.getLvlMessage(level)));

                    }

                }

            }

        }

    }

    @SubscribeEvent
    public void onEXPGain (ExperienceGainEvent event) throws ObjectMappingException {

        Player player = (Player) event.pokemon.getPlayerOwner();
        int level = AccountHandler.getLevelTier(player);
        int pokeLevel = event.pokemon.getLevel();

        if (pokeLevel < PixelmonConfig.maxLevel) {

            if (TierHandler.isLevelingSystemEnabled()) {

                if (pokeLevel >= TierHandler.getMaxLvlLevel(level)) {

                    event.setExperience(0);
                    player.sendMessage(FancyText.getFancyText(TierHandler.getLvlMessage(level).replace("Pokemon", event.pokemon.getBaseStats().pixelmonName)));

                }

            }

        }

    }

}

package com.lypaka.gces.gottacatchemsmall.Listeners;

import com.google.common.reflect.TypeToken;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigManager;
import com.lypaka.gces.gottacatchemsmall.Utils.AccountHandler;
import com.lypaka.gces.gottacatchemsmall.Utils.FancyText;
import com.lypaka.gces.gottacatchemsmall.Utils.TierHandler;
import com.pixelmonmod.pixelmon.api.events.EvolveEvent;
import com.pixelmonmod.pixelmon.api.events.MegaEvolutionEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.util.List;

public class EvolutionListener {

    @SubscribeEvent
    public void onEvolution (EvolveEvent.PreEvolve event) throws ObjectMappingException {

        Player player = (Player) event.player;
        if (!ConfigManager.getConfigNode(7, "World-Blacklist").isEmpty()) {

            List<String> worlds = ConfigManager.getConfigNode(7, "World-Blacklist").getList(TypeToken.of(String.class));
            org.spongepowered.api.world.World world = player.getWorld();
            if (worlds.contains(world.getName())) return;

        }

        if (TierHandler.areEvolutionsRestricted()) {

            if (!TierHandler.getEvoPermission().equalsIgnoreCase("none")) {

                if (!AccountHandler.hasPermission(player, TierHandler.getEvoPermission())) {

                    event.setCanceled(true);
                    player.sendMessage(FancyText.getFancyText(TierHandler.getEvoRestrictionMessage()));

                }

            } else {

                if (!AccountHandler.hasPermission(player, "gces.evolving." + TierHandler.getEvoStage(event.postEvo.create((World) player.getWorld())) + "stage")) {

                    event.setCanceled(true);
                    player.sendMessage(FancyText.getFancyText(TierHandler.getEvoRestrictionMessage()));

                }

            }

        }

    }

    @SubscribeEvent
    public void onMega (MegaEvolutionEvent.BattleEvolve event) throws ObjectMappingException {

        Player player = (Player) event.pw.getPlayerOwner();
        if (!ConfigManager.getConfigNode(7, "World-Blacklist").isEmpty()) {

            List<String> worlds = ConfigManager.getConfigNode(7, "World-Blacklist").getList(TypeToken.of(String.class));
            org.spongepowered.api.world.World world = player.getWorld();
            if (worlds.contains(world.getName())) return;

        }

        if (TierHandler.areMegasRestricted()) {

            if (!AccountHandler.hasPermission(player, TierHandler.getMegaPerm())) {

                if (!TierHandler.getMegaPerm().equalsIgnoreCase("none")) {

                    event.setCanceled(true);
                    player.sendMessage(FancyText.getFancyText(TierHandler.getMegaMessage()));

                }

            }

        }

    }

}

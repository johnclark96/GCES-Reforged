package com.lypaka.gces.gottacatchemsmall.Listeners;

import com.google.common.reflect.TypeToken;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigManager;
import com.lypaka.gces.gottacatchemsmall.Utils.FancyText;
import com.lypaka.gces.gottacatchemsmall.Utils.TierHandler;
import com.pixelmonmod.pixelmon.api.events.DynamaxEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.List;

public class DynamaxListener {

    @SubscribeEvent
    public void onDynamax (DynamaxEvent.BattleEvolve event) throws ObjectMappingException {

        Player player = (Player) event.pw.getPlayerOwner();
        if (!ConfigManager.getConfigNode(7, "World-Blacklist").isEmpty()) {

            List<String> worlds = ConfigManager.getConfigNode(7, "World-Blacklist").getList(TypeToken.of(String.class));
            World world = player.getWorld();
            if (worlds.contains(world.getName())) return;

        }

        if (TierHandler.restrictDynamaxing()) {

            if (!player.hasPermission(TierHandler.getDynamaxingPermission())) {

                event.setCanceled(true);
                player.sendMessage(FancyText.getFancyText(TierHandler.getDynamaxingMessage()));

            }

        }

    }

}

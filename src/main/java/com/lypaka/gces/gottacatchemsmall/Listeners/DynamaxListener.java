package com.lypaka.gces.gottacatchemsmall.Listeners;

import com.google.common.reflect.TypeToken;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigGetters;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigManager;
import com.lypaka.gces.gottacatchemsmall.Utils.AccountHandler;
import com.lypaka.gces.gottacatchemsmall.Utils.FancyText;
import com.lypaka.gces.gottacatchemsmall.Utils.TierHandler;
import com.pixelmonmod.pixelmon.api.events.DynamaxEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import java.util.List;

public class DynamaxListener {

    @SubscribeEvent
    public void onDynamax (DynamaxEvent.BattleEvolve event) throws ObjectMappingException {

        if (event.pw.getPlayerOwner() != null) {

            Player player = (Player) event.pw.getPlayerOwner();

            if (ConfigGetters.getPlayerDifficulty(player).equalsIgnoreCase("none")) return;

            int index = ConfigGetters.getIndexFromString(ConfigGetters.getPlayerDifficulty(player));

            if (TierHandler.restrictDynamaxing(index)) {

                String permission = ConfigManager.getConfigNode(index, 8, "Dynamaxing", "Unlock-Dynamaxing").getString();
                if (permission.equalsIgnoreCase("none")) return;


                if (!ConfigManager.getConfigNode(index, 6, "World-Blacklist").isEmpty()) {

                    List<String> worlds = ConfigManager.getConfigNode(index, 6, "World-Blacklist").getList(TypeToken.of(String.class));
                    World world = player.getWorld();
                    if (worlds.contains(world.getName())) return;

                }
                if (!AccountHandler.hasPermission(player, permission, index)) {

                    event.setCanceled(true);
                    player.sendMessage(FancyText.getFancyText(TierHandler.getDynamaxingMessage(index)));

                }

            }

        }

    }

}

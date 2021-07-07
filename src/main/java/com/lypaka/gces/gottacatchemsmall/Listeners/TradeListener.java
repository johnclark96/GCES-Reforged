package com.lypaka.gces.gottacatchemsmall.Listeners;

import com.google.common.reflect.TypeToken;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigGetters;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigManager;
import com.lypaka.gces.gottacatchemsmall.Utils.AccountHandler;
import com.lypaka.gces.gottacatchemsmall.Utils.FancyText;
import com.lypaka.gces.gottacatchemsmall.Utils.TierHandler;
import com.pixelmonmod.pixelmon.api.enums.ReceiveType;
import com.pixelmonmod.pixelmon.api.events.PixelmonReceivedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.world.World;

import java.util.List;

public class TradeListener {

    @Listener
    public void onInteract (InteractBlockEvent.Secondary.MainHand event, @Root Player player) throws ObjectMappingException {

        if (ConfigGetters.getPlayerDifficulty(player).equalsIgnoreCase("none")) return;

        int index = ConfigGetters.getIndexFromString(ConfigGetters.getPlayerDifficulty(player));

        if (!ConfigManager.getConfigNode(index, 6, "World-Blacklist").isEmpty()) {

            List<String> worlds = ConfigManager.getConfigNode(index, 6, "World-Blacklist").getList(TypeToken.of(String.class));
            World world = player.getWorld();
            if (worlds.contains(world.getName())) return;

        }
        if (event.getTargetBlock().getState().getType().getName().contains("trade_machine")) {

            if (!AccountHandler.hasPermission(player, TierHandler.getTradePerm(index), index)) {

                if (!TierHandler.getTradePerm(index).equalsIgnoreCase("none")) {

                    event.setCancelled(true);
                    player.sendMessage(FancyText.getFancyText(TierHandler.getTradeMessage(index)));

                }

            }

        }

    }

    // Could *technically* do this on the trade event but I figured it would be easier to just do it on the received event so don't have to get and apply both players at once
    // Plus you would have to code in more conditions for NPC trades and player trades and all that, so just easier to do it this way in my opinion

    @SubscribeEvent
    public void onTrade (PixelmonReceivedEvent event) throws ObjectMappingException {

        Player player = (Player) event.player;
        if (ConfigGetters.getPlayerDifficulty(player).equalsIgnoreCase("none")) return;

        int index = ConfigGetters.getIndexFromString(ConfigGetters.getPlayerDifficulty(player));

        if (TierHandler.areTradesModified(index)) {

            if (event.receiveType.equals(ReceiveType.Trade)) {

                if (!ConfigManager.getConfigNode(index, 7, "World-Blacklist").isEmpty()) {

                    List<String> worlds = ConfigManager.getConfigNode(index, 7, "World-Blacklist").getList(TypeToken.of(String.class));
                    World world = player.getWorld();
                    if (worlds.contains(world.getName())) return;

                }
                int lvl = event.pokemon.getLevel();
                Pokemon pokemon = event.pokemon;

                int playerLevel;
                if (TierHandler.getTierBase(index).equals("Catching")) {

                    playerLevel = AccountHandler.getCatchTier(player, index);

                } else {

                    playerLevel = AccountHandler.getLevelTier(player, index);

                }

                int lvlMax = getMaxLvl(index, TierHandler.getTierBase(index), playerLevel);
                if (lvl > lvlMax) {

                    pokemon.setLevel(lvlMax);
                    player.sendMessage(FancyText.getFancyText(TierHandler.getTradeMessage(index)));

                }

            }

        }

    }

    private static int getMaxLvl (int index, String value, int num) {

        if (value.equals("Catching")) {

            return TierHandler.getMaxCatchLevel(index, num);

        } else {

            return TierHandler.getMaxLvlLevel(index, num);

        }

    }

}

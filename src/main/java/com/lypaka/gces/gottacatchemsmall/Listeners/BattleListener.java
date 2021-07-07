package com.lypaka.gces.gottacatchemsmall.Listeners;

import com.google.common.reflect.TypeToken;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigGetters;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigManager;
import com.lypaka.gces.gottacatchemsmall.Utils.AccountHandler;
import com.lypaka.gces.gottacatchemsmall.Utils.FancyText;
import com.lypaka.gces.gottacatchemsmall.Utils.TierHandler;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.List;

public class BattleListener {

    @SubscribeEvent
    public void onBattleStart (BattleStartedEvent event) throws ObjectMappingException {

        BattleControllerBase bcb = event.bc;

        if (bcb.participants.get(0) instanceof PlayerParticipant) {

            EntityPlayerMP fPlayer = (EntityPlayerMP) bcb.participants.get(0).getEntity();
            Player player = (Player) fPlayer;

            if (ConfigGetters.getPlayerDifficulty(player).equalsIgnoreCase("none")) return;

            int index = ConfigGetters.getIndexFromString(ConfigGetters.getPlayerDifficulty(player));

            if (TierHandler.restrictBattles(index)) {

                if (!ConfigManager.getConfigNode(index, 6, "World-Blacklist").isEmpty()) {

                    List<String> worlds = ConfigManager.getConfigNode(index, 6, "World-Blacklist").getList(TypeToken.of(String.class));
                    World world = player.getWorld();
                    if (worlds.contains(world.getName())) return;

                }
                int level = AccountHandler.getLevelTier(player, index);
                int pokeLevel = TierHandler.getMaxLvlLevel(index, level);
                //PlayerStorage party = PixelmonStorage.pokeBallManager.getPlayerStorage(fPlayer).get();
                PlayerPartyStorage party = Pixelmon.storageManager.getParty(fPlayer);

                for (int i = 0; i < party.countPokemon(); i++) {

                    //EntityPixelmon pokemon = party.getPokemon(party.getIDFromPosition(i), fPlayer.getEntityWorld());
                    Pokemon pokemon = party.get(i);

                    if (pokemon != null) {

                        int pokemonLevel = pokemon.getLevel();
                        if (pokemonLevel > pokeLevel) {

                            event.setCanceled(true);
                            player.sendMessage(FancyText.getFancyText(TierHandler.getBattleMessage(index)));
                            break;

                        }

                    }

                }

            }

        }
        if (bcb.participants.get(1) instanceof PlayerParticipant) {

            EntityPlayerMP fPlayer = (EntityPlayerMP) bcb.participants.get(1).getEntity();
            Player player = (Player) fPlayer;

            if (ConfigGetters.getPlayerDifficulty(player).equalsIgnoreCase("none")) return;

            int index = ConfigGetters.getIndexFromString(ConfigGetters.getPlayerDifficulty(player));

            if (TierHandler.restrictBattles(index)) {

                if (!ConfigManager.getConfigNode(index, 6, "World-Blacklist").isEmpty()) {

                    List<String> worlds = ConfigManager.getConfigNode(index, 6, "World-Blacklist").getList(TypeToken.of(String.class));
                    World world = player.getWorld();
                    if (worlds.contains(world.getName())) return;

                }
                int level = AccountHandler.getLevelTier(player, index);
                int pokeLevel = TierHandler.getMaxLvlLevel(index, level);

                //PlayerStorage party = PixelmonStorage.pokeBallManager.getPlayerStorage(fPlayer).get();
                PlayerPartyStorage party = Pixelmon.storageManager.getParty(fPlayer);

                for (int i = 0; i < party.countPokemon(); i++) {

                    //EntityPixelmon pokemon = party.getPokemon(party.getIDFromPosition(i), fPlayer.getEntityWorld());
                    Pokemon pokemon = party.get(i);

                    if (pokemon != null) {

                        int pokemonLevel = pokemon.getLevel();
                        if (pokemonLevel > pokeLevel) {

                            event.setCanceled(true);
                            player.sendMessage(FancyText.getFancyText(TierHandler.getBattleMessage(index)));
                            break;

                        }

                    }

                }

            }

        }

    }

}

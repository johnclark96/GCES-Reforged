package com.lypaka.gces.gottacatchemsmall.Listeners;

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

public class BattleListener {

    @SubscribeEvent
    public void onBattleStart (BattleStartedEvent event) throws ObjectMappingException {

        BattleControllerBase bcb = event.bc;

        if (bcb.participants.get(0) instanceof PlayerParticipant) {

            if (TierHandler.restrictBattles()) {

                EntityPlayerMP fPlayer = (EntityPlayerMP) bcb.participants.get(0).getEntity();
                Player player = (Player) fPlayer;
                int level = AccountHandler.getLevelTier(player);
                int pokeLevel = TierHandler.getMaxLvlLevel(level);

                PlayerPartyStorage party = Pixelmon.storageManager.getParty(fPlayer);

                for (int i = 0; i < party.countAblePokemon(); i++) {

                    Pokemon pokemon = party.get(i);

                    if (pokemon != null) {

                        int pokemonLevel = pokemon.getLevel();

                        if (pokemonLevel > pokeLevel) {

                            event.setCanceled(true);
                            player.sendMessage(FancyText.getFancyText(TierHandler.getBattleMessage()));
                            break;

                        }

                    }

                }

            }

        }
        if (bcb.participants.get(1) instanceof PlayerParticipant) {

            if (TierHandler.restrictBattles()) {

                EntityPlayerMP fPlayer = (EntityPlayerMP) bcb.participants.get(0).getEntity();
                Player player = (Player) fPlayer;
                int level = AccountHandler.getLevelTier(player);
                int pokeLevel = TierHandler.getMaxLvlLevel(level);

                PlayerPartyStorage party = Pixelmon.storageManager.getParty(fPlayer);

                for (int i = 0; i < party.countAblePokemon(); i++) {

                    Pokemon pokemon = party.get(i);

                    if (pokemon != null) {

                        int pokemonLevel = pokemon.getLevel();

                        if (pokemonLevel > pokeLevel) {

                            event.setCanceled(true);
                            player.sendMessage(FancyText.getFancyText(TierHandler.getBattleMessage()));
                            break;

                        }

                    }

                }

            }

        }

    }

}

package com.lypaka.gces.gottacatchemsmall.Listeners;

import com.lypaka.gces.gottacatchemsmall.Utils.FancyText;
import com.lypaka.gces.gottacatchemsmall.Utils.TierHandler;
import com.pixelmonmod.pixelmon.api.events.DynamaxEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.api.entity.living.player.Player;

public class DynamaxListener {

    @SubscribeEvent
    public void onDynamax (DynamaxEvent.BattleEvolve event) {

        Player player = (Player) event.pw.getPlayerOwner();

        if (TierHandler.restrictDynamaxing()) {

            if (!player.hasPermission(TierHandler.getDynamaxingPermission())) {

                event.setCanceled(true);
                player.sendMessage(FancyText.getFancyText(TierHandler.getDynamaxingMessage()));

            }

        }

    }

}

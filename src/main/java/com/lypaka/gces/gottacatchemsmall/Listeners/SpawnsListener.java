package com.lypaka.gces.gottacatchemsmall.Listeners;

import com.lypaka.gces.gottacatchemsmall.Config.ConfigGetters;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigManager;
import com.lypaka.gces.gottacatchemsmall.GCES;
import com.lypaka.gces.gottacatchemsmall.Utils.AccountHandler;
import com.lypaka.gces.gottacatchemsmall.Utils.TierHandler;
import com.pixelmonmod.pixelmon.api.events.spawning.SpawnEvent;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.spawning.PlayerTrackingSpawner;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Random;

public class SpawnsListener {
    
    public static int spawnLevel = 1;

    @SubscribeEvent
    public void onSpawn (SpawnEvent event) throws ObjectMappingException {

        if (ConfigManager.getBaseNode(0, "Scale-Spawns").getBoolean()) {

            if (event.action.getOrCreateEntity() instanceof EntityPixelmon) {

                if (event.spawner instanceof PlayerTrackingSpawner) {

                    Player player = (Player) ((PlayerTrackingSpawner) event.spawner).getTrackedPlayer();
                    EntityPixelmon pokemon = (EntityPixelmon) event.action.getOrCreateEntity();
                    
                    if (!ConfigGetters.getPlayerDifficulty(player).equalsIgnoreCase("none")) {

                        // If SpawnValidation is loaded, it needs to be the only thing modifying the natural spawn, so we do nothing here.
                        if (!GCES.isSpawnValidationLoaded) {

                            setSpawnLevel(player);
                            pokemon.getLvl().setLevel(spawnLevel);
                            pokemon.updateStats();

                        }

                    }

                }

            }

        }

    }
    
    public static void setSpawnLevel (Player player) throws ObjectMappingException {

        int index = ConfigGetters.getIndexFromString(ConfigGetters.getPlayerDifficulty(player));
        int playerLevel = AccountHandler.getCatchTier(player, index);
        int maxLevel = TierHandler.getMaxCatchLevel(index, playerLevel);
        
        int configMax = PixelmonConfig.maxLevel;
        if (maxLevel != 0) {

            String[] modifier = ConfigManager.getBaseNode(0, "Spawn-Settings").getString().split(" ");
            String operand = modifier[0];
            String amt = modifier[1];
            int amount;
            int newLvl;
            Random random = new Random();

            switch (operand) {

                case "+-":
                case "-+":
                    if (amt.contains("r")) {

                        amount = Integer.parseInt(amt.replace("r", ""));
                        newLvl = random.nextInt(amount - 1) + 1;
                        if (random.nextInt(100) <= 50) {

                            spawnLevel = (Math.max(1, maxLevel - newLvl));

                        } else {

                            spawnLevel = (Math.min(configMax, maxLevel + newLvl));

                        }

                    } else {

                        amount = Integer.parseInt(amt);
                        if (random.nextInt(100) <= 50) {

                            spawnLevel = (Math.max(1, maxLevel - amount));

                        } else {

                            spawnLevel = (Math.min(configMax, maxLevel + amount));

                        }

                    }
                    break;

                case "+":
                    if (amt.contains("r")) {

                        amount = Integer.parseInt(amt.replace("r", ""));
                        newLvl = random.nextInt(amount - 1) + 1;
                        spawnLevel = (Math.max(configMax, maxLevel + newLvl));

                    } else {

                        amount = Integer.parseInt(amt);
                        spawnLevel = (Math.min(configMax, maxLevel + amount));

                    }
                    break;

                case "-":
                    if (amt.contains("r")) {

                        amount = Integer.parseInt(amt.replace("r", ""));
                        newLvl = random.nextInt(amount - 1) + 1;
                        spawnLevel = (Math.max(1, maxLevel - newLvl));

                    } else {

                        amount = Integer.parseInt(amt);
                        spawnLevel = (Math.max(1, maxLevel - amount));

                    }
                    break;

            }

        }
        
    }

}

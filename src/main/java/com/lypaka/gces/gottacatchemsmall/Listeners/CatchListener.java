package com.lypaka.gces.gottacatchemsmall.Listeners;

import com.google.common.reflect.TypeToken;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigGetters;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigManager;
import com.lypaka.gces.gottacatchemsmall.Utils.AccountHandler;
import com.lypaka.gces.gottacatchemsmall.Utils.FancyText;
import com.lypaka.gces.gottacatchemsmall.Utils.TierHandler;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleEndCause;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.List;

public class CatchListener {

    @SubscribeEvent
    public void onCapture (CaptureEvent.StartCapture event) throws ObjectMappingException {

        Player player = (Player) event.player;
        if (ConfigGetters.getPlayerDifficulty(player).equalsIgnoreCase("none")) return;

        int index = ConfigGetters.getIndexFromString(ConfigGetters.getPlayerDifficulty(player));
        if (!ConfigManager.getConfigNode(index, 6, "World-Blacklist").isEmpty()) {

            List<String> worlds = ConfigManager.getConfigNode(index, 6, "World-Blacklist").getList(TypeToken.of(String.class));
            World world = player.getWorld();
            if (worlds.contains(world.getName())) return;

        }
        int pokeLevel = event.getPokemon().getPokemonData().getLevel();

        if (event.getPokemon().getPokemonData().isShiny() && !TierHandler.areShiniesRestricted(index)) return;

        if (isLegendary(event.getPokemon().getPokemonName())) {

            if (TierHandler.areLegendariesRestricted(index)) {

                if (!AccountHandler.hasPermission(player, TierHandler.getLegendaryPermission(index), index)) {

                    event.setCanceled(true);

                    if (BattleRegistry.getBattle(event.player) != null) {

                        BattleRegistry.getBattle(event.player).endBattle(EnumBattleEndCause.FORCE);

                    }

                    String ballType = event.pokeball.getType().getFilenamePrefix();
                    ItemStack fStack = new ItemStack(Item.getByNameOrId("pixelmon:" + ballType));
                    org.spongepowered.api.item.inventory.ItemStack stack = (org.spongepowered.api.item.inventory.ItemStack) (Object) fStack;
                    stack.setQuantity(1);
                    player.getInventory().offer(stack);
                    player.sendMessage(FancyText.getFancyText(TierHandler.getCatchLegendaryMessage(index)));

                } else {

                    if (TierHandler.doIndividualLegendaries(index)) {

                        String name = event.getPokemon().getPokemonName();
                        List<String> groups = TierHandler.getGroups(index);

                        for (String group : groups) {

                            List<String> pokemonList = TierHandler.getLegendaries(index, group);
                            if (pokemonList.contains(name)) {

                                if (!AccountHandler.hasPermission(player, group, index)) {

                                    event.setCanceled(true);
                                    String message = ConfigManager.getConfigNode(index, 5, "Individual-Unlocking", "Message").getString();
                                    player.sendMessage(FancyText.getFancyText(message));
                                    break;

                                }

                            }

                        }

                    }

                }

            }

        } else {

            if (TierHandler.areEvoStagesRestricted(index)) {

                if (AccountHandler.hasPermission(player, "gces.catching." + TierHandler.getEvoStage(event.getPokemon()).toLowerCase() + "stage", index)) {

                    if (TierHandler.isCatchLevelAccessRestricted(index) && !AccountHandler.hasPermission(player, TierHandler.getCatchPermission(index), index)) {

                        event.setCanceled(true);
                        if (BattleRegistry.getBattle(event.player) != null) {

                            BattleRegistry.getBattle(event.player).endBattle(EnumBattleEndCause.FORCE);

                        }

                        String ballType = event.pokeball.getType().getFilenamePrefix();
                        ItemStack fStack = new ItemStack(Item.getByNameOrId("pixelmon:" + ballType));
                        org.spongepowered.api.item.inventory.ItemStack stack = (org.spongepowered.api.item.inventory.ItemStack) (Object) fStack;
                        stack.setQuantity(1);
                        player.getInventory().offer(stack);
                        player.sendMessage(FancyText.getFancyText(TierHandler.getCatchLevelMessage(index, 0)));

                    } else {

                        if (TierHandler.isCatchLevelRestricted(index)) {

                            int level = AccountHandler.getCatchTier(player, index);

                            if (TierHandler.getMaxCatchLevel(index, level) != 0) {

                                if (pokeLevel > TierHandler.getMaxCatchLevel(index, level)) {

                                    event.setCanceled(true);
                                    player.sendMessage(FancyText.getFancyText(TierHandler.getCatchLevelMessage(index, level)));
                                    if (BattleRegistry.getBattle(event.player) != null) {

                                        BattleRegistry.getBattle(event.player).endBattle(EnumBattleEndCause.FORCE);

                                    }

                                    String ballType = event.pokeball.getType().getFilenamePrefix();
                                    ItemStack fStack = new ItemStack(Item.getByNameOrId("pixelmon:" + ballType));
                                    org.spongepowered.api.item.inventory.ItemStack stack = (org.spongepowered.api.item.inventory.ItemStack) (Object) fStack;
                                    stack.setQuantity(1);
                                    player.getInventory().offer(stack);

                                }

                            } else {

                                event.setCanceled(true);
                                player.sendMessage(FancyText.getFancyText(TierHandler.getCatchLevelMessage(index, level)));
                                if (BattleRegistry.getBattle(event.player) != null) {

                                    BattleRegistry.getBattle(event.player).endBattle(EnumBattleEndCause.FORCE);

                                }

                                String ballType = event.pokeball.getType().getFilenamePrefix();
                                ItemStack fStack = new ItemStack(Item.getByNameOrId("pixelmon:" + ballType));
                                org.spongepowered.api.item.inventory.ItemStack stack = (org.spongepowered.api.item.inventory.ItemStack) (Object) fStack;
                                stack.setQuantity(1);
                                player.getInventory().offer(stack);

                            }

                        }

                    }

                } else {

                    event.setCanceled(true);
                    player.sendMessage(FancyText.getFancyText(TierHandler.getCatchEvoStageMessage(index)));

                }

            } else {

                if (TierHandler.isCatchLevelAccessRestricted(index) && !AccountHandler.hasPermission(player, TierHandler.getCatchPermission(index), index)) {

                    event.setCanceled(true);
                    if (BattleRegistry.getBattle(event.player) != null) {

                        BattleRegistry.getBattle(event.player).endBattle(EnumBattleEndCause.FORCE);

                    }

                    String ballType = event.pokeball.getType().getFilenamePrefix();
                    ItemStack fStack = new ItemStack(Item.getByNameOrId("pixelmon:" + ballType));
                    org.spongepowered.api.item.inventory.ItemStack stack = (org.spongepowered.api.item.inventory.ItemStack) (Object) fStack;
                    stack.setQuantity(1);
                    player.getInventory().offer(stack);
                    player.sendMessage(FancyText.getFancyText(TierHandler.getCatchLevelMessage(index, 0)));

                } else {

                    if (TierHandler.isCatchLevelRestricted(index)) {

                        int playerLevel = AccountHandler.getCatchTier(player, index);

                        if (TierHandler.getMaxCatchLevel(index, playerLevel) != 0) {

                            if (pokeLevel > TierHandler.getMaxCatchLevel(index, playerLevel)) {

                                event.setCanceled(true);
                                player.sendMessage(FancyText.getFancyText(TierHandler.getCatchLevelMessage(index, playerLevel)));
                                if (BattleRegistry.getBattle(event.player) != null) {

                                    BattleRegistry.getBattle(event.player).endBattle(EnumBattleEndCause.FORCE);

                                }

                                String ballType = event.pokeball.getType().getFilenamePrefix();
                                ItemStack fStack = new ItemStack(Item.getByNameOrId("pixelmon:" + ballType));
                                org.spongepowered.api.item.inventory.ItemStack stack = (org.spongepowered.api.item.inventory.ItemStack) (Object) fStack;
                                stack.setQuantity(1);
                                player.getInventory().offer(stack);

                            }

                        } else {

                            event.setCanceled(true);
                            if (BattleRegistry.getBattle(event.player) != null) {

                                BattleRegistry.getBattle(event.player).endBattle(EnumBattleEndCause.FORCE);

                            }

                            String ballType = event.pokeball.getType().getFilenamePrefix();
                            ItemStack fStack = new ItemStack(Item.getByNameOrId("pixelmon:" + ballType));
                            org.spongepowered.api.item.inventory.ItemStack stack = (org.spongepowered.api.item.inventory.ItemStack) (Object) fStack;
                            stack.setQuantity(1);
                            player.getInventory().offer(stack);
                            player.sendMessage(FancyText.getFancyText(TierHandler.getCatchLevelMessage(index, 0)));

                        }

                    }

                }

            }

        }

    }


    public static boolean isLegendary (String pokemon) {
        return pokemon.contains("Mewtwo") || pokemon.contains("Mew") || pokemon.contains("HoOh") || pokemon.contains("Ho-Oh") || pokemon.contains("Lugia") ||
                pokemon.contains("Entei") || pokemon.contains("Suicune") || pokemon.contains("Raikou") || pokemon.contains("Celebi") || pokemon.contains("Latios") ||
                pokemon.contains("Latias") || pokemon.contains("Groudon") || pokemon.contains("Kyogre") || pokemon.contains("Rayquaza") ||
                pokemon.contains("Jirachi") || pokemon.contains("Deoxys") || pokemon.contains("Heatran") || pokemon.contains("Moltres") || pokemon.contains("Zapdos") ||
                pokemon.contains("Articuno") || pokemon.contains("Regirock") || pokemon.contains("Regice") || pokemon.contains("Registeel") || pokemon.contains("Azelf") ||
                pokemon.contains("Uxie") || pokemon.contains("Mesprit") || pokemon.contains("Dialga") || pokemon.contains("Palkia") || pokemon.contains("Giratina") ||
                pokemon.contains("Cresselia") || pokemon.contains("Regigigas") || pokemon.contains("Darkrai") || pokemon.contains("Shaymin") || pokemon.contains("Arceus") ||
                pokemon.contains("Manaphy") || pokemon.contains("Victini") || pokemon.contains("Keldeo") || pokemon.contains("Terrakion") || pokemon.contains("Virizion") ||
                pokemon.contains("Cobalion") || pokemon.contains("Thundurus") || pokemon.contains("Tornadus") || pokemon.contains("Landorus") || pokemon.contains("Reshiram") ||
                pokemon.contains("Kyurem") || pokemon.contains("Zekrom") || pokemon.contains("Xerneas") || pokemon.contains("Yveltal") || pokemon.contains("Zygarde") ||
                pokemon.contains("TypeNull") || pokemon.contains("Type:Null") || pokemon.contains("Type Null") || pokemon.contains("Silvally") || pokemon.contains("TapuKoko") || pokemon.contains("Tapu Koko") ||
                pokemon.contains("TapuFini") || pokemon.contains("Tapu Fini") || pokemon.contains("TapuLele") || pokemon.contains("Tapu Lele") || pokemon.contains("TapuBulu") ||
                pokemon.contains("Tapu Bulu") || pokemon.contains("Cosmog") || pokemon.contains("Cosmoem") || pokemon.contains("Solgaleo") || pokemon.contains("Lunala") ||
                pokemon.contains("Necrozma") || pokemon.contains("Meloetta") || pokemon.contains("Genesect") || pokemon.contains("Diancie") || pokemon.contains("Hoopa") ||
                pokemon.contains("Volcanion") || pokemon.contains("Magearna") || pokemon.contains("Marshadow") || pokemon.contains("Zeraora") || pokemon.contains("Zacian") ||
                pokemon.contains("Zamazenta") || pokemon.contains("Eternatus") || pokemon.contains("Meltan") || pokemon.contains("Melmetal") || pokemon.contains("Zarude") || pokemon.contains("Calyrex") ||
                pokemon.contains("Glastrier") || pokemon.contains("Spectrier") || pokemon.contains("Regieleki") || pokemon.contains("Regidrago") || pokemon.contains("Kubfu") || pokemon.contains("Urshifu");
    }
}

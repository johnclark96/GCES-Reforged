package com.lypaka.gces.gottacatchemsmall.Listeners;

import com.google.common.reflect.TypeToken;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigManager;
import com.lypaka.gces.gottacatchemsmall.Utils.AccountHandler;
import com.lypaka.gces.gottacatchemsmall.Utils.FancyText;
import com.lypaka.gces.gottacatchemsmall.Utils.TierHandler;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleEndCause;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.List;

public class CatchListener {

    private int catchRate;

    @SubscribeEvent
    public void onCapture (CaptureEvent.StartCapture event) throws ObjectMappingException {

        Player player = (Player) event.player;
        if (!ConfigManager.getConfigNode(7, "World-Blacklist").isEmpty()) {

            List<String> worlds = ConfigManager.getConfigNode(7, "World-Blacklist").getList(TypeToken.of(String.class));
            World world = player.getWorld();
            if (worlds.contains(world.getName())) return;

        }
        int pokeLevel = event.getPokemon().getPokemonData().getLevel();

        if (event.getPokemon().getPokemonData().isShiny() && !TierHandler.areShiniesRestricted()) return;

        if (isLegendary(event.getPokemon().getPokemonName())) {

            if (TierHandler.areLegendariesRestricted()) {

                if (!AccountHandler.hasPermission(player, TierHandler.getLegendaryPermission())) {

                    event.setCanceled(true);

                    if (BattleRegistry.getBattle((EntityPlayer) player) != null) {

                        BattleRegistry.getBattle((EntityPlayer) player).endBattle(EnumBattleEndCause.FORCE);

                    }

                    String ballType = event.pokeball.getType().getFilenamePrefix();
                    ItemStack fStack = new ItemStack(Item.getByNameOrId("pixelmon:" + ballType));
                    org.spongepowered.api.item.inventory.ItemStack stack = (org.spongepowered.api.item.inventory.ItemStack) (Object) fStack;
                    stack.setQuantity(1);
                    player.getInventory().offer(stack);
                    player.sendMessage(FancyText.getFancyText(TierHandler.getCatchLegendaryMessage()));

                } else {

                    if (TierHandler.doIndividualLegendaries()) {

                        String name = event.getPokemon().getPokemonName();
                        List<String> groups = TierHandler.getGroups();

                        for (String group : groups) {

                            List<String> pokemonList = TierHandler.getLegendaries(group);
                            if (pokemonList.contains(name)) {

                                if (!AccountHandler.hasPermission(player, group)) {

                                    event.setCanceled(true);
                                    String message = ConfigManager.getConfigNode(5, "Individual-Unlocking", "Message").getString();
                                    player.sendMessage(FancyText.getFancyText(message));
                                    break;

                                }

                            }

                        }

                    }

                }

            }

        } else {

            if (TierHandler.areEvoStagesRestricted()) {

                if (AccountHandler.hasPermission(player, "gces.catching." + TierHandler.getEvoStage(event.getPokemon()).toLowerCase() + "stage")) {

                    if (TierHandler.isCatchLevelAccessRestricted() && !AccountHandler.hasPermission(player, TierHandler.getCatchPermission())) {

                        event.setCanceled(true);
                        if (BattleRegistry.getBattle((EntityPlayer) player) != null) {

                            BattleRegistry.getBattle((EntityPlayer) player).endBattle(EnumBattleEndCause.FORCE);

                        }

                        String ballType = event.pokeball.getType().getFilenamePrefix();
                        ItemStack fStack = new ItemStack(Item.getByNameOrId("pixelmon:" + ballType));
                        org.spongepowered.api.item.inventory.ItemStack stack = (org.spongepowered.api.item.inventory.ItemStack) (Object) fStack;
                        stack.setQuantity(1);
                        player.getInventory().offer(stack);
                        player.sendMessage(FancyText.getFancyText(TierHandler.getCatchLevelMessage(0)));

                    } else {

                        if (TierHandler.isCatchLevelRestricted()) {

                            int level = AccountHandler.getCatchTier(player);

                            if (TierHandler.getMaxCatchLevel(level) != 0) {

                                if (pokeLevel > TierHandler.getMaxCatchLevel(level)) {

                                    event.setCanceled(true);
                                    player.sendMessage(FancyText.getFancyText(TierHandler.getCatchLevelMessage(level)));
                                    if (BattleRegistry.getBattle((EntityPlayer) player) != null) {

                                        BattleRegistry.getBattle((EntityPlayer) player).endBattle(EnumBattleEndCause.FORCE);

                                    }

                                    String ballType = event.pokeball.getType().getFilenamePrefix();
                                    ItemStack fStack = new ItemStack(Item.getByNameOrId("pixelmon:" + ballType));
                                    org.spongepowered.api.item.inventory.ItemStack stack = (org.spongepowered.api.item.inventory.ItemStack) (Object) fStack;
                                    stack.setQuantity(1);
                                    player.getInventory().offer(stack);

                                }

                            } else {

                                event.setCanceled(true);
                                player.sendMessage(FancyText.getFancyText(TierHandler.getCatchLevelMessage(level)));
                                if (BattleRegistry.getBattle((EntityPlayer) player) != null) {

                                    BattleRegistry.getBattle((EntityPlayer) player).endBattle(EnumBattleEndCause.FORCE);

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
                    player.sendMessage(FancyText.getFancyText(TierHandler.getCatchEvoStageMessage()));

                }

            } else {

                if (TierHandler.isCatchLevelAccessRestricted() && !AccountHandler.hasPermission(player, TierHandler.getCatchPermission())) {

                    event.setCanceled(true);
                    if (BattleRegistry.getBattle((EntityPlayer) player) != null) {

                        BattleRegistry.getBattle((EntityPlayer) player).endBattle(EnumBattleEndCause.FORCE);

                    }

                    String ballType = event.pokeball.getType().getFilenamePrefix();
                    ItemStack fStack = new ItemStack(Item.getByNameOrId("pixelmon:" + ballType));
                    org.spongepowered.api.item.inventory.ItemStack stack = (org.spongepowered.api.item.inventory.ItemStack) (Object) fStack;
                    stack.setQuantity(1);
                    player.getInventory().offer(stack);
                    player.sendMessage(FancyText.getFancyText(TierHandler.getCatchLevelMessage(0)));

                } else {

                    if (TierHandler.isCatchLevelRestricted()) {

                        int playerLevel = AccountHandler.getCatchTier(player);

                        if (TierHandler.getMaxCatchLevel(playerLevel) != 0) {

                            if (pokeLevel > TierHandler.getMaxCatchLevel(playerLevel)) {

                                event.setCanceled(true);
                                player.sendMessage(FancyText.getFancyText(TierHandler.getCatchLevelMessage(playerLevel)));
                                if (BattleRegistry.getBattle((EntityPlayer) player) != null) {

                                    BattleRegistry.getBattle((EntityPlayer) player).endBattle(EnumBattleEndCause.FORCE);

                                }

                                String ballType = event.pokeball.getType().getFilenamePrefix();
                                ItemStack fStack = new ItemStack(Item.getByNameOrId("pixelmon:" + ballType));
                                org.spongepowered.api.item.inventory.ItemStack stack = (org.spongepowered.api.item.inventory.ItemStack) (Object) fStack;
                                stack.setQuantity(1);
                                player.getInventory().offer(stack);

                            }

                        } else {

                            event.setCanceled(true);
                            if (BattleRegistry.getBattle((EntityPlayer) player) != null) {

                                BattleRegistry.getBattle((EntityPlayer) player).endBattle(EnumBattleEndCause.FORCE);

                            }

                            String ballType = event.pokeball.getType().getFilenamePrefix();
                            ItemStack fStack = new ItemStack(Item.getByNameOrId("pixelmon:" + ballType));
                            org.spongepowered.api.item.inventory.ItemStack stack = (org.spongepowered.api.item.inventory.ItemStack) (Object) fStack;
                            stack.setQuantity(1);
                            player.getInventory().offer(stack);
                            player.sendMessage(FancyText.getFancyText(TierHandler.getCatchLevelMessage(0)));

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

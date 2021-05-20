package com.lypaka.gces.gottacatchemsmall.Config;

import com.lypaka.gces.gottacatchemsmall.GCES;
import com.lypaka.gces.gottacatchemsmall.Listeners.JoinListener;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Loads and stores all the configuration settings.
 * It loads from file on server start up. or when a player reloads the plugin.
 *
 * @uathor landonjw
 * @since 9/25/2019 - Version 1.0.0
 */
public class ConfigManager {

    /** Name of the file to grab configuration settings from. */
    private static final String[] FILE_NAMES = {"catching.conf", "evolving.conf", "leveling.conf", "trading.conf", "dynamaxing.conf", "legendaries.conf", "misc.conf", "skills.conf"};
    /** Paths needed to locate the configuration file. */
    private static Path dir;
    private static Path playerDir;
    private static Path mainPlayerDir;
    private static Path[] config = new Path[FILE_NAMES.length];
    private static Map<UUID, Path> playerConfig = new HashMap<UUID, Path>();
    /** Loader for the configuration file. */
    private static ArrayList<ConfigurationLoader<CommentedConfigurationNode>> configLoad = new ArrayList<ConfigurationLoader<CommentedConfigurationNode>>(FILE_NAMES.length);
    private static Map<UUID, ConfigurationLoader<CommentedConfigurationNode>> playerConfigLoad = new HashMap<UUID, ConfigurationLoader<CommentedConfigurationNode>>();
    /** Storage for all the configuration settings. */
    private static CommentedConfigurationNode[] configNode = new CommentedConfigurationNode[FILE_NAMES.length];
    private static Map<UUID, CommentedConfigurationNode> playerConfigNode = new HashMap<UUID, CommentedConfigurationNode>();

    /**
     * Locates the configuration file and loads it.
     * @param folder Folder where the configuration file is located.
     */
    public static void setup(Path folder){
        dir = folder;
        mainPlayerDir = Paths.get(folder.toString(), "player-accounts");

        for (int i = 0; i < FILE_NAMES.length; i++) {
            config[i] = dir.resolve(FILE_NAMES[i]);
        }
        load();
    }

    /**
     * Loads the configuration settings into storage.
     */
    public static void load(){
        //Create directory if it doesn't exist.
        try{
            if (!Files.exists(dir)) {
                Files.createDirectory(dir);
            }

            for (int i = 0; i < FILE_NAMES.length; i++) {
                //Create or locate file and load configuration file into storage.
                GCES.getContainer().getAsset(FILE_NAMES[i]).get().copyToFile(config[i], false, true);

                ConfigurationLoader<CommentedConfigurationNode> tempConfigLoad = HoconConfigurationLoader.builder().setPath(config[i]).build();

                configLoad.add(i, tempConfigLoad);
                configNode[i] = tempConfigLoad.load();
            }

            if (!Files.exists(mainPlayerDir)) {
                Files.createDirectory(mainPlayerDir);
            }
        } catch (IOException e){
            GCES.getLogger().error("GCES configuration could not load.");
            e.printStackTrace();
        }
    }

    /**
     * Saves the configuration settings to configuration file.
     */
    public static void save(){
        Task.builder().execute(() -> {
            for (int i = 0; i < FILE_NAMES.length; i++) {
                try{
                    configLoad.get(i).save(configNode[i]);
                }
                catch(IOException e){
                    GCES.getLogger().error("GCES could not save configuration.");
                    e.printStackTrace();
                }
            }
        }).async().submit(GCES.instance);
    }

    public static void loadPlayer(UUID uuid) {
        playerDir = Paths.get(mainPlayerDir.toString(), uuid.toString());
        if (playerConfig.get(uuid) == null) {
            playerConfig.put(uuid, playerDir.resolve("account.conf"));
        }

        try {
            if (!Files.exists(playerDir)) {
                Files.createDirectory(playerDir);
            }
            boolean firstLoad = !Files.exists(playerConfig.get(uuid));
            GCES.getContainer().getAsset("account.conf").get().copyToFile(playerConfig.get(uuid), false, true);

            ConfigurationLoader<CommentedConfigurationNode> tempConfigLoad = HoconConfigurationLoader.builder().setPath(playerConfig.get(uuid)).build();

            playerConfigLoad.put(uuid, tempConfigLoad);
            playerConfigNode.put(uuid, tempConfigLoad.load());

            if (firstLoad) {
                JoinListener.generateAccount(Sponge.getServer().getPlayer(uuid).get());
            }


        } catch (IOException | ObjectMappingException e) {
            GCES.getLogger().error("GCES player configuration could not load.");
            e.printStackTrace();
        }
    }

    public static void savePlayer(UUID uuid) {

        try {
            playerConfigLoad.get(uuid).save(playerConfigNode.get(uuid));
        } catch(IOException e){
            GCES.getLogger().error("GCES could not save player configuration.");
            e.printStackTrace();
        }

    }


    /**
     * Gets the configuration loader ArrayList.
     * @return The configuration loader ArrayList.
     */
    public static ArrayList<ConfigurationLoader<CommentedConfigurationNode>> getConfigLoad(){
        return configLoad;
    }

    /**
     * Gets the configuration loader at specific index.
     * @param index Index of the configuration loader.
     * @return The configuration loader.
     */
    public static ConfigurationLoader<CommentedConfigurationNode> getConfigLoad(int index){
        return configLoad.get(index);
    }

    public static ConfigurationLoader<CommentedConfigurationNode> getPlayerConfigLoad(UUID uuid) {
        return playerConfigLoad.get(uuid);
    }

    /**
     * Gets a node from the configuration node, where all configuration settings are stored.
     * @param index Index of the config.
     * @param node A node within the configuration node.
     * @return A node within the configuration node.
     */
    public static CommentedConfigurationNode getConfigNode(int index, Object... node){
        return configNode[index].getNode(node);
    }

    public static CommentedConfigurationNode getPlayerConfigNode(UUID uuid, Object... node){
        return playerConfigNode.get(uuid).getNode(node);
    }

    public static CommentedConfigurationNode getConfigFile (int index) {
        return configNode[index];
    }

    public static CommentedConfigurationNode getPlayerConfigFile (UUID uuid) {
        return playerConfigNode.get(uuid);
    }



}
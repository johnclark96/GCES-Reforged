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
import java.util.concurrent.CompletableFuture;

/**
 * Loads and stores all the configuration settings.
 * It loads from file on server start up. or when a player reloads the plugin.
 *
 * @uathor landonjw
 * @since 9/25/2019 - Version 1.0.0
 */
public class ConfigManager {

    /** Name of the file to grab configuration settings from. */
    private static final String[] BASE_FILES = {"general.conf"};
    private static final String[] FILE_NAMES = {"catching.conf", "evolving.conf", "leveling.conf", "trading.conf", "zmoves.conf", "legendaries.conf", "misc.conf", "skills.conf", "dynamaxing.conf"};
    //                                               0                  1               2               3               4               5                   6           7               8
    private static final String[] FOLDER_NAMES = {"Easy", "Medium", "Hard", "Default"};
    //                                              0         1        2        3
    /** Paths needed to locate the configuration file. */
    private static Path dir;
    private static Path playerDir;
    private static Path mainPlayerDir;
    private static Path[][] difficultiesDir = new Path[FOLDER_NAMES.length][FILE_NAMES.length];
    private static Path mainDifficultiesDir;
    private static Path[] baseConfig = new Path[BASE_FILES.length];
    private static Path[] config = new Path[FOLDER_NAMES.length];
    private static Path[] config2 = new Path[FILE_NAMES.length];
    private static Map<UUID, Path> playerConfig = new HashMap<UUID, Path>();
    /** Loader for the configuration file. */
    private static ArrayList<ConfigurationLoader<CommentedConfigurationNode>> folderLoad = new ArrayList<>(FOLDER_NAMES.length);
    private static ArrayList<ConfigurationLoader<CommentedConfigurationNode>> fileLoad = new ArrayList<>(FILE_NAMES.length);
    private static ArrayList<ConfigurationLoader<CommentedConfigurationNode>> baseLoad = new ArrayList<>(BASE_FILES.length);

    private static ArrayList<ArrayList<ConfigurationLoader<CommentedConfigurationNode>>> configLoad = new ArrayList<ArrayList<ConfigurationLoader<CommentedConfigurationNode>>>(28);
    private static Map<UUID, ConfigurationLoader<CommentedConfigurationNode>> playerConfigLoad = new HashMap<UUID, ConfigurationLoader<CommentedConfigurationNode>>();
    /** Storage for all the configuration settings. */
    private static CommentedConfigurationNode[][] configNode = new CommentedConfigurationNode[FOLDER_NAMES.length][FILE_NAMES.length];
    private static Map<UUID, CommentedConfigurationNode> playerConfigNode = new HashMap<UUID, CommentedConfigurationNode>();
    private static CommentedConfigurationNode[] baseNode = new CommentedConfigurationNode[BASE_FILES.length];

    private static boolean isSaving = false;


    public static void setup(Path path) throws IOException {
        dir = path;
        mainPlayerDir = checkDir(dir.resolve("player-accounts"));
        mainDifficultiesDir = checkDir(dir.resolve("difficulties"));

        for (int i = 0; i < FOLDER_NAMES.length; i++) {

            Path tempDiffDir = checkDir(mainDifficultiesDir.resolve(FOLDER_NAMES[i]));
            for (int j = 0; j < FILE_NAMES.length; j++) {
                difficultiesDir[i][j] = tempDiffDir.resolve(FILE_NAMES[j]);
                GCES.getContainer().getAsset(FILE_NAMES[j]).get().copyToFile(difficultiesDir[i][j], false, true);

                ConfigurationLoader<CommentedConfigurationNode> tempConfigLoad = HoconConfigurationLoader.builder().setPath(difficultiesDir[i][j]).build();
                folderLoad.add(j, tempConfigLoad);

                configLoad.add(i, folderLoad);
                configNode[i][j] = tempConfigLoad.load();
            }

        }

        for (int i = 0; i < BASE_FILES.length; i++) {

            baseConfig[i] = dir.resolve(BASE_FILES[i]);

        }


        //save();
        load();
    }

    public static Path checkDir(Path dir) {
        if (!Files.exists(dir)) {
            try {
                Files.createDirectories(dir);
            } catch (IOException e) {
                throw new RuntimeException("Error creating dir! " + dir, e);
            }
        }
        return dir;
    }

    /**
     * Loads the configuration settings into storage.
     */
    public static void load(){
        //Create directory if it doesn't exist.
        try{
            for (int i = 0; i < FOLDER_NAMES.length; i++) {
                Path tempDiffDir = checkDir(mainDifficultiesDir.resolve(FOLDER_NAMES[i]));
                for (int j = 0; j < FILE_NAMES.length; j++) {
                    difficultiesDir[i][j] = tempDiffDir.resolve(FILE_NAMES[j]);
                    GCES.getContainer().getAsset(FILE_NAMES[j]).get().copyToFile(difficultiesDir[i][j], false, true);

                    ConfigurationLoader<CommentedConfigurationNode> tempConfigLoad = HoconConfigurationLoader.builder().setPath(difficultiesDir[i][j]).build();
                    folderLoad.add(j, tempConfigLoad);

                    configLoad.add(i, folderLoad);
                    configNode[i][j] = tempConfigLoad.load();
                }
            }

            for (int i = 0; i < BASE_FILES.length; i++) {

                GCES.getContainer().getAsset(BASE_FILES[i]).get().copyToFile(baseConfig[i], false, true);
                ConfigurationLoader<CommentedConfigurationNode> tempConfigLoad = HoconConfigurationLoader.builder().setPath(baseConfig[i]).build();

                baseLoad.add(i, tempConfigLoad);
                baseNode[i] = tempConfigLoad.load();

            }

        } catch (IOException e){
            GCES.getLogger().error("PixelSkills configuration could not load.");
            e.printStackTrace();
        }
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


    /**
     * Saves the configuration settings to configuration file.
     */
    public static void save(){
        Task.builder().execute(() -> {
            for (int i = 0; i < FOLDER_NAMES.length; i++) {

                for (int j = 0; j < FILE_NAMES.length; j++) {
                    try{
                        configLoad.get(i).get(j).save(configNode[i][j]);
                    }
                    catch(IOException e){
                        GCES.getLogger().error("GCES could not save configuration.");
                        e.printStackTrace();
                    }
                }

            }

            for (int i = 0; i < BASE_FILES.length; i++) {

                try {

                    baseLoad.get(i).save(baseNode[i]);

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }

        }).async().submit(GCES.instance);
    }

    public static void saveDifficulty (int folder, int file) {

        try {
            configLoad.get(folder).get(file).save(configNode[folder][file]);
        } catch (IOException e) {
            GCES.instance.logger.error("Error saving difficulty file!");
        }

    }

    public static CompletableFuture<Void> savePlayer(UUID uuid) {
        return CompletableFuture.runAsync(() -> {
            try {

                playerConfigLoad.get(uuid).save(playerConfigNode.get(uuid));

            } catch(IOException e) {

                GCES.getLogger().error("GCES could not save player configuration.");
                e.printStackTrace();

            }

        }, Sponge.getScheduler().createAsyncExecutor(GCES.instance));
    }

    public static CommentedConfigurationNode getConfigNode (int folder, int file, Object... node) {

        return configNode[folder][file].getNode(node);

    }

    public static CommentedConfigurationNode getPlayerConfigNode (UUID uuid, Object... node) {

        return playerConfigNode.get(uuid).getNode(node);

    }

    public static CommentedConfigurationNode getBaseNode (int index, Object... node) {

        return baseNode[index].getNode(node);

    }

}
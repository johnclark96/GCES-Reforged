package com.lypaka.gces.gottacatchemsmall;

import com.google.inject.Inject;
import com.lypaka.gces.gottacatchemsmall.Commands.GCESAdminCommand;
import com.lypaka.gces.gottacatchemsmall.Config.ConfigManager;
import com.lypaka.gces.gottacatchemsmall.Listeners.*;
import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraftforge.common.MinecraftForge;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Plugin(
        id = "gottacatchemsmall",
        name = "GCS",
        version = "6.0.0-Reforged"
)
public class GCES {

    @Inject
    @ConfigDir(sharedRoot = false)
    public Path configDir;

    @Inject
    private PluginContainer container;

    @Inject
    public Logger logger;

    public static GCES instance;

    public static boolean isPixelSkillsLoaded = false;

    @Listener
    public void onPreInit (GamePreInitializationEvent event) {
        logger.info("GCES is starting up! Let's hope this bitch doesn't set your server on fire.");
        instance = this;
        container = Sponge.getPluginManager().getPlugin("gottacatchemsmall").get();
        ConfigManager.setup(configDir);
        Pixelmon.EVENT_BUS.register(new CatchListener());
        Pixelmon.EVENT_BUS.register(new LevelingListener());
        Pixelmon.EVENT_BUS.register(new TradeListener());
        Pixelmon.EVENT_BUS.register(new EvolutionListener());
        Pixelmon.EVENT_BUS.register(new DynamaxListener());
        Pixelmon.EVENT_BUS.register(new BattleListener());
        Sponge.getEventManager().registerListeners(this, new TradeListener());
        Sponge.getEventManager().registerListeners(this, new JoinListener());
        GCESAdminCommand.registerAdminCommands();
        if (ConfigManager.getConfigNode(7, "World-Blacklist").isVirtual()) {

            List<String> emptyList = new ArrayList<>();
            ConfigManager.getConfigNode(7, "World-Blacklist").setValue(emptyList);
            ConfigManager.save();

        }

        if (Sponge.getPluginManager().getPlugin("pixelskills").isPresent()) {

            isPixelSkillsLoaded = true;
            logger.info("Detected PixelSkills! Activating Skill tier restrictions!");

            // If we load this with the other events, will probably throw error when PixelSkills is not present
            MinecraftForge.EVENT_BUS.register(new SkillsListener());

        }
    }

    @Listener
    public void onReload (GameReloadEvent event) {
        ConfigManager.load();
        logger.info("GCES has reloaded!");
    }

    public static PluginContainer getContainer() {
        return instance.container;
    }

    public static Logger getLogger() {
        return instance.logger;
    }

}

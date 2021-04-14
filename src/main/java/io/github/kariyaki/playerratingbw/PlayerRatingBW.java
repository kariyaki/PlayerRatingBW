package io.github.kariyaki.playerratingbw;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class PlayerRatingBW extends JavaPlugin {

    private final Logger logger = getLogger();
    private final FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        logger.info("onEnable is called!");
        super.onEnable();
        saveDefaultConfig();
        DatabaseConnection.setupConnection();
        setupCommand();
    }

    private void setupCommand() {
        var command = this.getCommand("rating");
        if(command != null) {
            command.setExecutor(new CommandRating());
        }
    }

    @Override
    public void onDisable() {
        logger.info("onDisable is called");
        super.onDisable();
        DatabaseConnection.closeConnection();
    }

    public static PlayerRatingBW getPlugin() {
        return getPlugin(PlayerRatingBW.class);
    }

    public static FileConfiguration config() {
        return getPlugin().config;
    }


}

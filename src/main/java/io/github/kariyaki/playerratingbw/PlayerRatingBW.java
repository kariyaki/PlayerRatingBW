package io.github.kariyaki.playerratingbw;

import github.scarsz.discordsrv.DiscordSRV;
import io.github.kariyaki.playerratingbw.command.discord.DiscordSRVListener;
import io.github.kariyaki.playerratingbw.command.minecraft.CommandRating;
import io.github.kariyaki.playerratingbw.placeholder.PlayerRatingExpansion;
import io.github.kariyaki.playerratingbw.utils.DatabaseConnection;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class PlayerRatingBW extends JavaPlugin {

    private final Logger logger = getLogger();
    private final FileConfiguration config = getConfig();
    private final DiscordSRVListener discordsrvListener = new DiscordSRVListener(this);

    @Override
    public void onEnable() {
        logger.info("onEnable is called!");
        super.onEnable();
        saveDefaultConfig();
        DatabaseConnection.setupConnection();
        setupCommand();
        DiscordSRV.api.subscribe(discordsrvListener);
    }

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
        DiscordSRV.api.unsubscribe(discordsrvListener);
    }

    public static PlayerRatingBW getPlugin() {
        return getPlugin(PlayerRatingBW.class);
    }

    public static FileConfiguration config() {
        return getPlugin().config;
    }


}

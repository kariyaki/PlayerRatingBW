package io.github.kariyaki.playerratingbw.command.discord;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.ListenerPriority;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessageReceivedEvent;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Message;
import github.scarsz.discordsrv.objects.MessageFormat;
import github.scarsz.discordsrv.util.*;
import io.github.kariyaki.playerratingbw.models.PlayerStats;
import io.github.kariyaki.playerratingbw.rating.RatingService;
import org.bukkit.plugin.Plugin;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.BiFunction;

public class DiscordSRVListener {

    private final Plugin plugin;
    private final RatingService ratingService;
    private final ArrayList<String> availableCommands = new ArrayList<>() {{
        add("/prbw list - List all available commands");
        add("/prbw top - List the top three ratings");
        add("/prbw <playername> - List the specific player ranking");
    }};

    public DiscordSRVListener(Plugin plugin) {
        this.plugin = plugin;
        this.ratingService = new RatingService();
    }

    @Subscribe(priority = ListenerPriority.MONITOR)
    public void discordMessageReceived(DiscordGuildMessageReceivedEvent event) {
        plugin.getLogger().info("Received a chat message on Discord: " + event.getMessage().getContentRaw());
        if(event.getMessage().getContentRaw().startsWith("/prbw")) {
            var variables = event.getMessage().getContentRaw().split(" ");

            if(variables.length < 2) {
                sendErrorMessage(event, "Missing arguments. Use `/prbw list` for available commands");
            }
            else if(variables[1].equals("list")) {
                sendMessage(event, buildAvailableCommandsMessage(), "Available commands:");
            }
            else if(variables[1].equals("top")) {
                var topThreeMessage = buildTopThreeMessage(ratingService.getTopThreeRating());
                sendMessage(event, topThreeMessage, "Bedwars - Top three ratings:");
            }
            else {
                var playerName = variables[1];
                var playerStats = ratingService.getPlayerRating(playerName);
                if (playerStats == null) sendErrorMessage(event, "Unknown player \"" + playerName + "\".");
                else sendMessage(event, buildPlayerStatsMessage(playerStats), "Bedwars - " + playerName + " rating:");
            }
        }
    }

    private void sendMessage(DiscordGuildMessageReceivedEvent event, String topThreeMessage, String title) {
        MessageFormat messageFormat = new MessageFormat();
        messageFormat.setColor(new Color(0,204, 0));
        messageFormat.setTitle(title);
        messageFormat.setDescription(topThreeMessage);
        messageFormat.setFooterText("PlayerRatingBW");
        BiFunction<String, Boolean, String> translator = (content, needsEscape) -> content;
        Message message = DiscordSRV.translateMessage(messageFormat, translator);
        DiscordUtil.queueMessage(event.getChannel(), message);
    }

    private void sendErrorMessage(DiscordGuildMessageReceivedEvent event, String errorMessage) {
        MessageFormat messageFormat = new MessageFormat();
        messageFormat.setColor(Color.red);
        messageFormat.setDescription(errorMessage);
        messageFormat.setFooterText("PlayerRatingBW");
        BiFunction<String, Boolean, String> translator = (content, needsEscape) -> content;
        Message message = DiscordSRV.translateMessage(messageFormat, translator);
        DiscordUtil.queueMessage(event.getChannel(), message);
    }

    private String buildTopThreeMessage(ArrayList<PlayerStats> topThreeRating) {
        StringBuilder message = new StringBuilder();
        for(var i = 0; i < topThreeRating.size(); i++) {
            var playerStats = topThreeRating.get(i);
            message.append("\n").append(i + 1).append(". ").append(playerStats.getNick())
                    .append(" (").append(playerStats.getRating()).append(")");
        }
        return message.toString();
    }


    private String buildPlayerStatsMessage(PlayerStats playerStats) {
        return "Rating: " + playerStats.getRating() + "\n" +
                "Wins: " + playerStats.getWins();
    }

    private String buildAvailableCommandsMessage() {
        StringBuilder message = new StringBuilder();
        for (Object command : availableCommands) {
            message.append(command).append("\n");
        }
        return message.toString();
    }

}
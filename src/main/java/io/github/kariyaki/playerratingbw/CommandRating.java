package io.github.kariyaki.playerratingbw;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandRating implements CommandExecutor {
    private final RatingService ratingService = new RatingService();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 0) {
            printTopThreeRatings(commandSender);
        } else {
            printPlayerRating(commandSender, strings);
        }
        return true;
    }

    private void printPlayerRating(CommandSender commandSender, String[] strings) {
        if (strings.length == 0) commandSender.sendMessage(ChatColor.AQUA + "[PlayerRatingBW] " + ChatColor.RED + "Must specify a player name (/rating <name>)");
        var playerRating = ratingService.getPlayerRating(strings[0]);
        if (playerRating == null) {
            commandSender.sendMessage(ChatColor.AQUA + "[PlayerRatingBW] " + ChatColor.RED + "No rating available for " + strings[0]);
        } else {
            commandSender.sendMessage(ChatColor.AQUA + "[PlayerRatingBW] " + ChatColor.GREEN + "=== " + playerRating.getNick() + " ===");
            commandSender.sendMessage(ChatColor.AQUA + "[PlayerRatingBW] " + ChatColor.WHITE + "Rating: " + ChatColor.BLUE + playerRating.getRating());
            commandSender.sendMessage(ChatColor.AQUA + "[PlayerRatingBW] " + ChatColor.WHITE + "Wins: " + ChatColor.BLUE +  playerRating.getWins());
        }
    }

    private void printTopThreeRatings(CommandSender commandSender) {
        var ratings = ratingService.getTopThreeRating();
        commandSender.sendMessage(ChatColor.AQUA + "[PlayerRatingBW] " + ChatColor.GREEN + "Top three ratings:");
        for (var i = 0; i < ratings.size(); i++) {
            var playerStats = ratings.get(i);
            commandSender.sendMessage(ChatColor.AQUA + "[PlayerRatingBW] " + ChatColor.YELLOW + "" + (i + 1) + ". " + ChatColor.WHITE + playerStats.getNick() + " (" + ChatColor.BLUE + playerStats.getRating() + ChatColor.WHITE + ")");
        }
    }
}

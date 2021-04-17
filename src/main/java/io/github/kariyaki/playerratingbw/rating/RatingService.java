package io.github.kariyaki.playerratingbw.rating;

import io.github.kariyaki.playerratingbw.PlayerRatingBW;
import io.github.kariyaki.playerratingbw.models.PlayerStats;
import io.github.kariyaki.playerratingbw.utils.DatabaseConnection;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RatingService {

    private final Map<String, PlayerStats> allPlayerStats = new HashMap<>();

    public RatingService() { }

    private void getAndCalculateRatings() {
        try {
            ResultSet results = DatabaseConnection.getPlayerData();
            createOrUpdateRatingForAllPlayers(results);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createOrUpdateRatingForAllPlayers(ResultSet results) throws SQLException {
        while (results.next()) {
            var playerUuid = results.getString("player_uuid");
            var key = results.getString("key");
            var value = results.getInt("value");

            var stats = getOrCreateStats(playerUuid);

            switch (key) {
                case "bedwars:beds_destroyed" -> stats.setBedsDestroyed(value);
                case "bedwars:deaths" -> stats.setDeaths(value);
                case "bedwars:kills" -> stats.setKills(value);
                case "bedwars:loses" -> stats.setLoses(value);
                case "bedwars:wins" -> stats.setWins(value);
            }
            var rating = calculateRating(stats);
            stats.setRating(rating);

            allPlayerStats.put(playerUuid, stats);
        }
    }

    private double calculateRating(PlayerStats playerStats) {
        var bedsDestroyed = PlayerRatingBW.config().getDouble("bedsDestroyed");
        var deaths = PlayerRatingBW.config().getDouble("deaths");
        var finalKills = PlayerRatingBW.config().getDouble("finalKills");
        var kills = PlayerRatingBW.config().getDouble("kills");
        var wins = PlayerRatingBW.config().getDouble("wins");
        var loses = PlayerRatingBW.config().getDouble("loses");

        return (playerStats.getBedsDestroyed() * bedsDestroyed)
                + (playerStats.getFinalKills() * finalKills)
                + (playerStats.getKills() * kills)
                + (playerStats.getWins() * wins)
                - (playerStats.getLoses() * loses)
                - (playerStats.getDeaths() * deaths);
    }

    private PlayerStats getOrCreateStats(String playerUuid) {
        if (allPlayerStats.containsKey(playerUuid)) {
            return allPlayerStats.get(playerUuid);
        } else {
            PlayerStats playerStats = new PlayerStats();
            var name = tryToGetPlayerName(UUID.fromString(playerUuid));
            playerStats.setNick(name);
            allPlayerStats.put(playerUuid, playerStats);
            return playerStats;
        }
    }

    private String tryToGetPlayerName(UUID playerUuid) {
        var player = Bukkit.getPlayer(playerUuid);
        String name;
        if (player != null) {
            name = player.getName();
        } else {
            name = Bukkit.getOfflinePlayer(playerUuid).getName();
        }
        return name;
    }

    private UUID tryToGetPlayerUUID(String playerName) {
        var player = Bukkit.getPlayer(playerName);
        UUID playerUUID = null;
        if (player != null) {
            playerUUID = player.getUniqueId();
        } else {
            var optionalResult = Arrays.stream(Bukkit.getOfflinePlayers())
                    .filter(x -> playerName.equals(x.getName()))
                    .findFirst();
            if (optionalResult.isPresent()) {
                playerUUID = optionalResult.get().getUniqueId();
            }
        }
        return playerUUID;
    }

    public ArrayList<PlayerStats> getTopThreeRating() {
        getAndCalculateRatings();
        var topThree = new ArrayList<PlayerStats>();
        var values = allPlayerStats.values().toArray();
        for (int i = 0; i < allPlayerStats.values().size(); i++) {
            topThree.add((PlayerStats) values[i]);
        }
        topThree.sort((p1, p2) -> {
            if (p1.getRating() > p2.getRating())
                return -1;
            else if (p1.getRating() < p2.getRating())
                return 1;
            return 0;
        });
        return topThree;
    }

    public PlayerStats getPlayerRating(String playerName) {
        getAndCalculateRatings();
        var playerUUID = tryToGetPlayerUUID(playerName);
        if (playerUUID == null) return null;

        return allPlayerStats.get(playerUUID.toString());
    }
}

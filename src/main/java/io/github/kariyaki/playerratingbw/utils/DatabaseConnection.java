package io.github.kariyaki.playerratingbw.utils;

import io.github.kariyaki.playerratingbw.PlayerRatingBW;

import java.sql.*;

public class DatabaseConnection {

    static Connection connection;

    public static void setupConnection() {
        var url = PlayerRatingBW.config().getString("url");
        var user = PlayerRatingBW.config().getString("user");
        var password = PlayerRatingBW.config().getString("password");

        if (url == null) {
            throw new IllegalArgumentException("The config file is missing the mysql url. The url is mandatory.");
        }
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException throwables) {
            throw new IllegalArgumentException("Could not connect to database. Maybe the config is wrong?");
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not close the database connection.");
        }
    }

    public static ResultSet getPlayerData() throws SQLException {
        String sql = "SELECT * FROM mbedwars_player_stats;";
        PreparedStatement stmt = connection.prepareStatement(sql);
        return stmt.executeQuery();
    }
}

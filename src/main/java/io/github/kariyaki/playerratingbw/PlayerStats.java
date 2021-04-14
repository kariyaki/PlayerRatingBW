package io.github.kariyaki.playerratingbw;

public class PlayerStats {
    private String playerUuid;
    private String nick;
    private int bedsDestroyed;
    private int deaths;
    private int finalKills;
    private int kills;
    private int wins;
    private int loses;
    private double rating;

    public PlayerStats() {
    }

    public String getPlayerUuid() {
        return playerUuid;
    }

    public void setPlayerUuid(String playerUuid) {
        this.playerUuid = playerUuid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getBedsDestroyed() {
        return bedsDestroyed;
    }

    public void setBedsDestroyed(int bedsDestroyed) {
        this.bedsDestroyed = bedsDestroyed;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getFinalKills() {
        return finalKills;
    }

    public void setFinalKills(int finalKills) {
        this.finalKills = finalKills;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLoses() {
        return loses;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "PlayerStats{" +
                "playerUuid='" + playerUuid + '\'' +
                ", nick='" + nick + '\'' +
                ", bedsDestroyed=" + bedsDestroyed +
                ", deaths=" + deaths +
                ", finalKills=" + finalKills +
                ", kills=" + kills +
                ", wins=" + wins +
                ", loses=" + loses +
                ", rating='" + rating + '\'' +
                '}';
    }
}

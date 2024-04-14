package dev.vedcodee.it.placeholder;

import dev.vedcodee.it.Main;
import dev.vedcodee.it.database.DuelPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class PlaceholderSupport extends PlaceholderExpansion {


    public String getIdentifier() {
        return "bsDuel";
    }

    public String getAuthor() {
        return "VedCodee";
    }

    public String getVersion() {
        return "";
    }

    public String onPlaceholderRequest(Player player, String identifier) {
        // %bsduel_leaderboard_{top}_death%
        // %bsduel_leaderboard_{top}_win%
        // %bsduel_leaderboard_{top}_duels%

        if (identifier.contains("leaderboard")) {
            int number = Integer.parseInt(identifier.split("_")[1]);
            String type = identifier.split("_")[2];
            switch (type) {
                case "win":
                    return getTopPlayerWin(number) != null ? String.valueOf(getTopPlayerWin(number).getWin()) : Main.getInstance().getConfiguration().getString("placeholders.no_team_leaderboard_stats");
                case "deaths":
                    return getTopPlayerDeaths(number) != null ? String.valueOf(getTopPlayerDeaths(number).getDeaths()) : Main.getInstance().getConfiguration().getString("placeholders.no_team_leaderboard_stats");
                case "duels":
                    return getTopPlayerDuels(number) != null ? String.valueOf(getTopPlayerDuels(number).getDuels()) : Main.getInstance().getConfiguration().getString("placeholders.no_team_leaderboard_stats");
                case "win.name":
                    return getTopPlayerWin(number) != null ? String.valueOf(getTopPlayerWin(number).getPlayer()) : Main.getInstance().getConfiguration().getString("placeholders.no_team_leaderboard_stats_name");
                case "deaths.name":
                    return getTopPlayerDeaths(number) != null ? String.valueOf(getTopPlayerDeaths(number).getPlayer()) : Main.getInstance().getConfiguration().getString("placeholders.no_team_leaderboard_stats_name");
                case "duels.name":
                    return getTopPlayerDuels(number) != null ? String.valueOf(getTopPlayerDuels(number).getPlayer()) : Main.getInstance().getConfiguration().getString("placeholders.no_team_leaderboard_stats_name");
                default:
                    return "";
            }
        }
        if (player == null) return "";
        int win = Main.getInstance().getDatabase().getWins(player.getName());
        int deaths = Main.getInstance().getDatabase().getDeath(player.getName());
        int winFormatted = win == -1 ? 0 : win;
        int deathsFormatted = deaths == -1 ? 0 : deaths;
        switch (identifier) {
            case "duels":
                return String.valueOf(deathsFormatted + winFormatted);
            case "deaths":
                return String.valueOf(deaths);
            case "win":
                return String.valueOf(winFormatted);
            default:
                return "";
        }
    }

    private DuelPlayer getTopPlayerWin(int teamNumber) {
        List<DuelPlayer> playersCopy = new ArrayList<>(Main.getInstance().getDatabase().getPlayers());
        playersCopy.sort(Comparator.comparingInt(DuelPlayer::getWin).reversed());

        if (teamNumber <= 0 || teamNumber > playersCopy.size()) return null;

        return playersCopy.get(teamNumber - 1);
    }

    private DuelPlayer getTopPlayerDeaths(int teamNumber) {
        List<DuelPlayer> playersCopy = new ArrayList<>(Main.getInstance().getDatabase().getPlayers());
        playersCopy.sort(Comparator.comparingInt(DuelPlayer::getDeaths).reversed());

        if (teamNumber <= 0 || teamNumber > playersCopy.size()) return null;

        return playersCopy.get(teamNumber - 1);
    }

    private DuelPlayer getTopPlayerDuels(int teamNumber) {
        List<DuelPlayer> playersCopy = new ArrayList<>(Main.getInstance().getDatabase().getPlayers());
        playersCopy.sort(Comparator.comparingInt(DuelPlayer::getDuels).reversed());

        if (teamNumber <= 0 || teamNumber > playersCopy.size()) return null;

        return playersCopy.get(teamNumber - 1);
    }


}

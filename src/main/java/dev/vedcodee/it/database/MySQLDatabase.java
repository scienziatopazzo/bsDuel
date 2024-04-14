package dev.vedcodee.it.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.vedcodee.it.Main;
import dev.vedcodee.it.arena.Arena;
import dev.vedcodee.it.utils.LocationUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MySQLDatabase {

    private HikariDataSource source;
    private Connection connection;

    public MySQLDatabase() throws SQLException {

        HikariConfig config = new HikariConfig();
        ConfigurationSection configuration = Main.getInstance().getConfiguration().getConfigurationSection("database");
        String host = configuration.getString("host");
        String port = configuration.getString("port");
        String databaseName = configuration.getString("database");
        String username = configuration.getString("username");
        String password = configuration.getString("password");

        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + databaseName);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10);

        this.source = new HikariDataSource(config);

        this.connection = source.getConnection();

        createTable();
    }


    private void createTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS arenas (" +
                    "name VARCHAR(255) NOT NULL," +
                    "spawn_1 VARCHAR(255) NOT NULL," +
                    "spawn_2 VARCHAR(255) NOT NULL" +
                    ")");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_stats (" +
                    "player VARCHAR(255) NOT NULL," +
                    "win INT NOT NULL," +
                    "death INT NOT NULL" +
                    ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // ARENAS

    public void loadArenas() {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            List<Arena> arenas = new ArrayList<>();
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM arenas");
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String spawn1 = resultSet.getString("spawn_1");
                    String spawn2 = resultSet.getString("spawn_2");
                    Arena arena = new Arena(name);
                    arena.setSpawn(1, LocationUtils.getLocation(spawn1));
                    arena.setSpawn(2, LocationUtils.getLocation(spawn2));
                    arenas.add(arena);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Arena.setArenas(arenas);
        });
    }

    public void refreshArenas() {
        if(Arena.getArenas() == null) return;
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            resetArenas();
            for (Arena arena : Arena.getArenas()) {
                if(!arena.areSpawnSet()) continue;
                addArena(
                        arena.getName(),
                        LocationUtils.getString(arena.getSpawns().getLoc1()),
                        LocationUtils.getString(arena.getSpawns().getLoc2())
                );
            }
        });
    }



    private void addArena(String name, String spawn_1, String spawn_2) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO arenas (name, spawn_1, spawn_2) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, spawn_1);
            preparedStatement.setString(3, spawn_2);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetArenas() {
        String query = "DELETE FROM arenas";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // STATS

    public void addStats(String player, int win, int death) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            try {
                boolean playerExists = getWins(player) != -1 && getDeath(player) != -1;

                String sql = playerExists ?
                        "UPDATE player_stats SET win = win + ?, death = death + ? WHERE player = ?" :
                        "INSERT INTO player_stats (player, win, death) VALUES (?, ?, ?)";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setInt(playerExists ? 1 : 2, win);
                    preparedStatement.setInt(playerExists ? 2 : 3, death);
                    preparedStatement.setString(playerExists ? 3 : 1, player);
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }


    public int getWins(String player) {
        int win = -1;
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT win FROM player_stats WHERE player = ?")) {
            preparedStatement.setString(1, player);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                win = resultSet.getInt("win");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return win;
    }

    public int getDeath(String player) {
        int deaths = -1;
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT death FROM player_stats WHERE player = ?")) {
            preparedStatement.setString(1, player);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                deaths = resultSet.getInt("death");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deaths;
    }

    public List<DuelPlayer> getPlayers() {
        List<DuelPlayer> players = new ArrayList<>();

        String query = "SELECT player, win, death FROM player_stats";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String player = resultSet.getString("player");
                int win = resultSet.getInt("win");
                int deaths = resultSet.getInt("death");
                players.add(new DuelPlayer(player, win, deaths, win + deaths));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return players;
    }



}

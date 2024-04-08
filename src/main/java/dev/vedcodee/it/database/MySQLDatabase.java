package dev.vedcodee.it.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.vedcodee.it.Main;
import dev.vedcodee.it.arena.Arena;
import dev.vedcodee.it.utils.LocationUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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


    private void createTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS arenas (" +
                    "name VARCHAR(255) NOT NULL," +
                    "spawn_1 VARCHAR(255) NOT NULL," +
                    "spawn_2 VARCHAR(255) NOT NULL" +
                    ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
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


}

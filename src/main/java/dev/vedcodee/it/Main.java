package dev.vedcodee.it;

import co.aikar.commands.BukkitCommandManager;
import dev.vedcodee.it.arena.component.events.PlayerDeathEventImp;
import dev.vedcodee.it.arena.component.events.PlayerQuitEventImp;
import dev.vedcodee.it.commands.ArenaCommand;
import dev.vedcodee.it.commands.LeaveCommand;
import dev.vedcodee.it.database.MySQLDatabase;
import dev.vedcodee.it.kit.DuelKit;
import dev.vedcodee.it.placeholder.PlaceholderSupport;
import dev.vedcodee.it.utils.GameFile;
import dev.vedcodee.it.utils.gui.GUIEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    private GameFile configFile;
    private GameFile messageFile;
    private FileConfiguration configuration;
    private FileConfiguration messageConfiguration;
    private MySQLDatabase database;

    @Override
    public void onEnable() {

        instance = this;

        loadConfig();
        loadDatabase();


        BukkitCommandManager manager = new BukkitCommandManager(this);
        manager.registerCommand(new ArenaCommand());
        manager.registerCommand(new LeaveCommand());


        DuelKit.loadKits();

        Bukkit.getPluginManager().registerEvents(new GUIEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathEventImp(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitEventImp(), this);

        if(getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"))
            new PlaceholderSupport().register();
    }

    @Override
    public void onDisable() {
        database.refreshArenas();
        if(!database.getSource().isClosed())
            database.getSource().close();
    }

    private void loadConfig() {
        configFile = new GameFile("config.yml");
        messageFile = new GameFile("messages.yml");
        configuration = configFile.getFileConfiguration();
        messageConfiguration = messageFile.getFileConfiguration();
    }

    private void loadDatabase() {
        try {
            database = new MySQLDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        database.loadArenas();
    }

}

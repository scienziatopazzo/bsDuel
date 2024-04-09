package dev.vedcodee.it.utils;


import dev.vedcodee.it.Main;
import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

@Getter
public class GameFile {


    private FileConfiguration fileConfiguration;
    private final File file;


    public GameFile(String name) {

        JavaPlugin main = Main.getInstance();

        File configFile = new File(main.getDataFolder(), name);
        this.file = configFile;


        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            try {
                main.saveResource(name, false);
            }catch (Exception e) {
                try {
                    configFile.createNewFile();
                }catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }



        if(name.contains(".yml")) {
            FileConfiguration configuration = new YamlConfiguration();

            try {
                configuration.load(configFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }

            fileConfiguration = configuration;
        }

    }

    public void reload() {
        try {
            fileConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
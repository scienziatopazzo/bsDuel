package dev.vedcodee.it.utils;


import dev.vedcodee.it.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ChatUtils {

    // String color(String)
    // List<String> color (List<String>)
    // List<String> color (String...)

    public static String prefix() { return Main.getInstance().getMessageConfiguration().getString("prefix"); }

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }


    public static List<String> color(List<String> list) {
        return list.stream()
                .map(s -> ChatColor.translateAlternateColorCodes('&', s))
                .collect(Collectors.toList());
    }

    public static List<String> color(String... list) {
        return Arrays.asList(list).stream()
                .map(s -> ChatColor.translateAlternateColorCodes('&', s))
                .collect(Collectors.toList());
    }

    private static void sendMessage(Player player, String patch, String... args) {
        FileConfiguration config = Main.getInstance().getMessageConfiguration();
        String message = config.getString(patch);
        for (int i = 0; i < args.length; i++) {
            message = message.replace("{" + i + "}", args[i]);
        }
        player.sendMessage(color(message));
    }

    public static void sendMessage(Player player, FileConfiguration config, String patch, HashMap<String, String> placeholders) {
        String newMessage = config.getString(patch);
        player.sendMessage(color(replace(newMessage, placeholders)));
    }

    public static List<String> replace(List<String> message, HashMap<String, String> placeholders) {
        List<String> newMessage = new ArrayList<>(message);
        placeholders.forEach((placeholder, replace) -> {
            for (int i = 0; i < newMessage.size(); i++) {
                newMessage.set(i, newMessage.get(i).replace("{" + placeholder + "}", replace));
            }
        });
        return color(newMessage);
    }

    public static String replace(String message, HashMap<String, String> placeholders) {
        String[] newMessage = {message};
        placeholders.put("prefix", prefix());
        placeholders.forEach((placeholder, replace) -> {
            newMessage[0] = newMessage[0].replace("{" + placeholder + "}", replace);
        });
        return color(newMessage[0]);
    }


    public static void sendMessage(Player player, String message, HashMap<String, String> placeholders) {
        player.sendMessage(color(replace(Main.getInstance().getMessageConfiguration().getString(message), placeholders)));
    }

    private static void sendTitle(Player player, FileConfiguration config, String patch, String... args) {
        String message = config.getString(patch);

        if (message.contains("[title]") && message.contains("[subtitle]")) {
            String[] parts = message.split("\\[subtitle\\]");
            String title = parts[0].replace("[title]", "");
            String subtitle = parts[1].replace("[subtitle]", "");

            for (int i = 0; i < args.length; i++) {
                title = title.replace("{" + i + "}", args[i]);
                subtitle = subtitle.replace("{" + i + "}", args[i]);
            }

            player.sendTitle(color(title), color(subtitle));
        } else {
            for (int i = 0; i < args.length; i++) {
                message = message.replace("{" + i + "}", args[i]);
            }

            player.sendTitle(color(message), "");
        }
    }

    public static void sendTitle(Player player, FileConfiguration config, String patch, HashMap<String, String> placeholders) {
        String message = config.getString(patch);

        if (message.contains("[title]") && message.contains("[subtitle]")) {
            String[] parts = message.split("\\[subtitle\\]");
            String title = parts[0].replace("[title]", "");
            String subtitle = parts[1].replace("[subtitle]", "");

            title = replace(title, placeholders);
            subtitle = replace(subtitle, placeholders);

            player.sendTitle(color(title), color(subtitle));
        } else {
            message = replace(message, placeholders);
            player.sendTitle(color(message), "");
        }
    }


    public static String makeLowercaseExceptFirst(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        return String.valueOf(input.charAt(0)).toUpperCase() + input.substring(1).toLowerCase();
    }





}

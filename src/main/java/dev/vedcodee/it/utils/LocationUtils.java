package dev.vedcodee.it.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtils {

    public static String getString(Location location) {
        return location.getWorld().getName() + ";;" + (int) location.getX() + ";;" + (int) location.getY() + ";;" + (int) location.getZ() + ";;" + (int) location.getYaw() + ";;" + (int) location.getPitch();
    }

    public static Location getLocation(String location) {
        return new Location(
                Bukkit.getWorld(location.split(";;")[0]),
                Integer.parseInt(location.split(";;")[1]),
                Integer.parseInt(location.split(";;")[2]),
                Integer.parseInt(location.split(";;")[3]),
                Float.parseFloat(location.split(";;")[4]),
                Float.parseFloat(location.split(";;")[5])
        );
    }

}

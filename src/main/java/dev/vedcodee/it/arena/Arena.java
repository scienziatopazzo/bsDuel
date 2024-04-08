package dev.vedcodee.it.arena;

import dev.vedcodee.it.utils.pair.LocationPair;
import dev.vedcodee.it.utils.pair.PlayerPair;
import javafx.util.Pair;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Getter
public class Arena {

    @Getter
    @Setter
    private static List<Arena> arenas = new ArrayList<>();

    private final String name;


    private final LocationPair spawns;
    private final PlayerPair players;

    public Arena(String name) {
        this.name = name;
        this.spawns = new LocationPair();
        this.players = new PlayerPair();
        arenas.add(this);
    }


    public boolean areSpawnSet() {
        return spawns.getLoc1() != null && spawns.getLoc2() != null;
    }

    public boolean thereIsMaxPlayers() {
        return players.getPlayer1() != null && players.getPlayer2() != null;
    }

    public void setSpawn(int spawn, Location location) {
        if(spawn == 1) {
            spawns.setLoc1(location);
        } else if(spawn == 2) {
            spawns.setLoc2(location);
        }
    }

    public static Arena getArenaByName(String name) {
        return arenas.stream().filter(arena -> arena.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }



}

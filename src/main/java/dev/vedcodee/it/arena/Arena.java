package dev.vedcodee.it.arena;

import dev.vedcodee.it.Main;
import dev.vedcodee.it.arena.component.Status;
import dev.vedcodee.it.arena.component.gui.ArenaSelectKitGUI;
import dev.vedcodee.it.arena.component.runnable.StartingRunnable;
import dev.vedcodee.it.kit.DuelKit;
import dev.vedcodee.it.utils.ChatUtils;
import dev.vedcodee.it.utils.LocationUtils;
import dev.vedcodee.it.utils.pair.LocationPair;
import dev.vedcodee.it.utils.pair.PlayerPair;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class Arena {

    @Getter
    @Setter
    private static List<Arena> arenas = new ArrayList<>();

    private final String name;


    private final LocationPair spawns;
    private final PlayerPair players;
    private final HashMap<Location, Material> removedBlocks;
    private final List<Location> placedBlocks;
    @Setter
    private Player victory;
    @Setter
    private Status status;

    public Arena(String name) {
        this.name = name;
        this.spawns = new LocationPair();
        this.players = new PlayerPair();
        this.status = Status.EMPTY;
        this.removedBlocks = new HashMap<>();
        this.placedBlocks = new ArrayList<>();
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



    public void starting() {
        if(!thereIsMaxPlayers()) return;
        ArenaSelectKitGUI gui_1 = new ArenaSelectKitGUI(players.getPlayer1());
        ArenaSelectKitGUI gui_2 = new ArenaSelectKitGUI(players.getPlayer2());
        players.setPlayer1_selection(gui_1);
        players.setPlayer2_selection(gui_2);
        players.setPlayer1_content(players.getPlayer1().getInventory().getContents());
        players.setPlayer2_content(players.getPlayer2().getInventory().getContents());
        players.getPlayer1().teleport(spawns.getLoc1());
        players.getPlayer2().teleport(spawns.getLoc2());
        new StartingRunnable(this).runTaskTimer(Main.getInstance(), 0L, 20L);
    }

    public void start() {
        players.getPlayer1().getOpenInventory().close();
        players.getPlayer2().getOpenInventory().close();
        players.getPlayer1().getInventory().setContents(players.getPlayer1_selection().getKitSelected() == null ? DuelKit.getKitByName(Main.getInstance().getConfiguration().getString("kit.default")).getContent() : players.getPlayer1_selection().getKitSelected().getContent());
        players.getPlayer2().getInventory().setContents(players.getPlayer2_selection().getKitSelected() == null ? DuelKit.getKitByName(Main.getInstance().getConfiguration().getString("kit.default")).getContent() : players.getPlayer2_selection().getKitSelected().getContent());
    }


    public void stop() {
        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put("player", victory.getName());

        Main.getInstance().getDatabase().addStats(victory.getName(), 1, 0);
        ChatUtils.sendTitle(victory, Main.getInstance().getMessageConfiguration(), "win_title");

        Player def = players.getPlayer1() == victory ? players.getPlayer2() : players.getPlayer1();
        for (String command : Main.getInstance().getConfiguration().getStringList("rewards.win"))
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ChatUtils.color(command).replace("{player}", victory.getName()));
        if(def != null) {
            Main.getInstance().getDatabase().addStats(def.getName(), 0, 1);
            ChatUtils.sendTitle(def, Main.getInstance().getMessageConfiguration(), "lost_title");
            for (String command : Main.getInstance().getConfiguration().getStringList("rewards.lost"))
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ChatUtils.color(command).replace("{player}", def.getName()));
        }
        sendMessage(ChatUtils.replace(Main.getInstance().getMessageConfiguration().getString("win"), placeholders));

        for (Location location : placedBlocks) location.getBlock().setType(Material.AIR);
        removedBlocks.forEach((location, material) -> location.getBlock().setType(material));
        placedBlocks.clear();
        removedBlocks.clear();
        Location lobby = LocationUtils.getLocation(Main.getInstance().getConfiguration().getString("lobby"));
        players.getPlayer1().teleport(lobby);
        players.getPlayer2().teleport(lobby);

        players.getPlayer1().getInventory().setContents(players.getPlayer1_content());
        players.getPlayer2().getInventory().setContents(players.getPlayer2_content());

        players.setPlayer1(null);
        players.setPlayer2(null);
        players.getPlayer1_selection().delete();
        players.getPlayer2_selection().delete();
        players.setPlayer1_selection(null);
        players.setPlayer2_selection(null);
        players.setPlayer1_content(null);
        players.setPlayer2_content(null);

        setStatus(Status.EMPTY);
    }



    public void sendMessage(String message) {
        if(players.getPlayer1() != null) players.getPlayer1().sendMessage(message);
        if(players.getPlayer2() != null) players.getPlayer2().sendMessage(message);
    }


    public static Arena getArenaByName(String name) {
        return arenas.stream().filter(arena -> arena.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static boolean isInArena(Player player) {
        return arenas.stream().anyMatch(arena -> arena.getPlayers().getPlayer1() == player || arena.getPlayers().getPlayer2() == player);
    }

    public static Arena getArenaByPlayer(Player player) {
        return arenas.stream().filter(arena -> arena.getPlayers().getPlayer1() == player || arena.getPlayers().getPlayer2() == player).findFirst().orElse(null);
    }

}

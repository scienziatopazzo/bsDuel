package dev.vedcodee.it.arena.component.events;

import dev.vedcodee.it.Main;
import dev.vedcodee.it.arena.Arena;
import dev.vedcodee.it.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class PlayerDeathEventImp implements Listener {


    @EventHandler
    public void onQuit(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if(!Arena.isInArena(player)) return;
        Arena arena = Arena.getArenaByPlayer(player);
        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put("player", player.getName());
        arena.sendMessage(ChatUtils.replace(Main.getInstance().getMessageConfiguration().getString("death"), placeholders));
        arena.setVictory(arena.getPlayers().getPlayer1() == player ? arena.getPlayers().getPlayer2() : arena.getPlayers().getPlayer1());
        arena.stop();
        event.setDeathMessage(null);
        event.setKeepInventory(true);
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> player.spigot().respawn(), 1L);
    }

}

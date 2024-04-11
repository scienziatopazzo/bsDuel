package dev.vedcodee.it.arena.component.events;

import dev.vedcodee.it.Main;
import dev.vedcodee.it.arena.Arena;
import dev.vedcodee.it.utils.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class PlayerQuitEventImp implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(!Arena.isInArena(player)) return;
        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put("player", player.getName());
        Arena arena = Arena.getArenaByPlayer(player);
        arena.sendMessage(ChatUtils.replace(Main.getInstance().getMessageConfiguration().getString("leaved"), placeholders));
        arena.setVictory(arena.getPlayers().getPlayer1() == player ? arena.getPlayers().getPlayer2() : arena.getPlayers().getPlayer1());
        arena.stop();
    }

}

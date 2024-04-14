package dev.vedcodee.it.arena.component.events.blocks;

import dev.vedcodee.it.arena.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PlayerBreakImp implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if(!Arena.isInArena(player)) return;

        Arena arena = Arena.getArenaByPlayer(player);

        arena.getRemovedBlocks().put(event.getBlock().getLocation().clone(), event.getBlock().getType());
    }

}

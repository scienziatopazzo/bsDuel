package dev.vedcodee.it.arena.component.events.blocks;

import dev.vedcodee.it.arena.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlayerPlaceImp implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if(!Arena.isInArena(player)) return;

        Arena arena = Arena.getArenaByPlayer(player);

        arena.getPlacedBlocks().add(event.getBlockPlaced().getLocation());
    }

}

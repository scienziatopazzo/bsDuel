package dev.vedcodee.it.arena.component.events.blocks;

import dev.vedcodee.it.arena.Arena;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ExplosionEventImp implements Listener {

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        for (Player player : event.getEntity().getWorld().getPlayers()) {
            if (event.getEntity().getLocation().distance(player.getLocation()) < 10) {
                if(!Arena.isInArena(player)) return;
                Arena arena = Arena.getArenaByPlayer(player);
                for (Block block : event.blockList())
                    arena.getRemovedBlocks().put(block.getLocation().clone(), block.getType());
                return;
            }
        }
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent event) {
        for (Player player : event.getBlock().getLocation().getWorld().getPlayers()) {
            if (event.getBlock().getLocation().distance(player.getLocation()) < 10) {
                if(!Arena.isInArena(player)) return;
                Arena arena = Arena.getArenaByPlayer(player);
                for (Block block : event.blockList())
                    arena.getRemovedBlocks().put(block.getLocation().clone(), block.getType());
                return;
            }
        }
    }



}

package dev.vedcodee.it.arena.component.runnable;

import dev.vedcodee.it.arena.Arena;
import lombok.RequiredArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class SelectGUIRunnable extends BukkitRunnable {

    private final Arena arena;

    @Override
    public void run() {
        if(arena.getPlayers().getPlayer1_selection() != null && !arena.getPlayers().getPlayer1().getOpenInventory().getTitle().contains("Select Kit"))
            arena.getPlayers().getPlayer1_selection().open(arena.getPlayers().getPlayer1());

        if(arena.getPlayers().getPlayer2_selection() != null && !arena.getPlayers().getPlayer2().getOpenInventory().getTitle().contains("Select Kit"))
            arena.getPlayers().getPlayer2_selection().open(arena.getPlayers().getPlayer2());

    }
}

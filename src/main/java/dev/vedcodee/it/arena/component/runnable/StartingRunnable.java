package dev.vedcodee.it.arena.component.runnable;

import dev.vedcodee.it.Main;
import dev.vedcodee.it.arena.Arena;
import dev.vedcodee.it.arena.component.Status;
import dev.vedcodee.it.utils.ChatUtils;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

@Getter
public class StartingRunnable extends BukkitRunnable {

    private final Arena arena;
    private final int defSeconds;
    private final SelectGUIRunnable guiRunnable;
    private int seconds;

    public StartingRunnable(Arena arena) {
        this.arena = arena;
        this.defSeconds = Main.getInstance().getConfiguration().getInt("startingSeconds");
        this.seconds = defSeconds;
        this.guiRunnable = new SelectGUIRunnable(arena);
        guiRunnable.runTaskTimer(Main.getInstance(), 0L, 5L);
    }

    @Override
    public void run() {

        arena.setStatus(Status.STARTING);
        if (arena.getPlayers().getPlayer1() == null || arena.getPlayers().getPlayer2() == null) {
            arena.setStatus(Status.EMPTY);
            arena.stop();
            arena.sendMessage(ChatUtils.replace(Main.getInstance().getMessageConfiguration().getString("starting_cancelled"), new HashMap<>()));
            cancel();
            return;
        }

        if(seconds == 0) {
            arena.setStatus(Status.STATED);
            arena.sendMessage(ChatUtils.replace(Main.getInstance().getMessageConfiguration().getString("started"), new HashMap<>()));
            guiRunnable.cancel();
            arena.start();
            cancel();
            return;
        }

        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put("seconds", String.valueOf(seconds));
        arena.sendMessage(ChatUtils.replace(Main.getInstance().getMessageConfiguration().getString("starting"), placeholders));
        seconds--;

    }

}


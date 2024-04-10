package dev.vedcodee.it.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import dev.vedcodee.it.arena.Arena;
import dev.vedcodee.it.utils.ChatUtils;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;

import java.util.HashMap;

@CommandAlias("leave")
public class LeaveCommand extends BaseCommand {

    @Default
    public void onLeave(Player player) {

        if(!Arena.isInArena(player)) {
            ChatUtils.sendMessage(player, "not_in_arena", new HashMap<>());
            return;
        }

        Arena arena = Arena.getArenaByPlayer(player);

        arena.setVictory(arena.getPlayers().getPlayer1() == player ? arena.getPlayers().getPlayer2() : arena.getPlayers().getPlayer1());
        arena.stop();
    }

}

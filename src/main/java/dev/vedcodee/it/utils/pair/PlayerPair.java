package dev.vedcodee.it.utils.pair;

import dev.vedcodee.it.arena.component.gui.ArenaSelectKitGUI;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
public class PlayerPair {

    private Player player1;
    private ArenaSelectKitGUI player1_selection;
    private Player player2;
    private ArenaSelectKitGUI player2_selection;


    public PlayerPair(Player loc1, Player loc2) {
        this.player1 = loc1;
        this.player2 = loc2;
    }

    public PlayerPair() { }

}

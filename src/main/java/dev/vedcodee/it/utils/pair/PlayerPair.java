package dev.vedcodee.it.utils.pair;

import dev.vedcodee.it.arena.component.gui.ArenaSelectKitGUI;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class PlayerPair {

    private Player player1;
    private ArenaSelectKitGUI player1_selection;
    private ItemStack[] player1_content;
    private Player player2;
    private ArenaSelectKitGUI player2_selection;
    private ItemStack[] player2_content;


    public PlayerPair(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public PlayerPair() { }

}

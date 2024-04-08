package dev.vedcodee.it.utils.pair;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
public class PlayerPair {

    private Player player1;
    private Player player2;


    public PlayerPair(Player loc1, Player loc2) {
        this.player1 = loc1;
        this.player2 = loc2;
    }

    public PlayerPair() { }

}

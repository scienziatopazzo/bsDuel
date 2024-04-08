package dev.vedcodee.it.utils.pair;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter
@Setter
public class LocationPair {

    private Location loc1;
    private Location loc2;


    public LocationPair(Location loc1, Location loc2) {
        this.loc1 = loc1;
        this.loc2 = loc2;
    }

    public LocationPair() { }


}

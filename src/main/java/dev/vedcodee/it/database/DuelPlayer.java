package dev.vedcodee.it.database;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DuelPlayer {

    private final String player;
    private final int win;
    private final int deaths;
    private final int duels;

}

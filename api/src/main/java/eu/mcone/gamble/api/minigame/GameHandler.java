/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl, Louis Bockel and the MC ONE Minecraftnetwork. All rights reserved
 *  You are not allowed to decompile the code
 */

package eu.mcone.gamble.api.minigame;

import eu.mcone.gamble.api.Gamble;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class GameHandler {

    @Setter
    private GamePhase gamePhase;

    public void finishGame(EndReason reason, GameResult... results) {
        Gamble.getInstance().getMiniGamesHandler().finishCurrentGame(reason, results);
    }

    public abstract void gamePhaseSwitched(GamePhase phase);
}

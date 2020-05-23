/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.api.listener;

import eu.mcone.gamble.api.Gamble;
import eu.mcone.gamble.api.minigame.GambleGame;
import org.bukkit.event.Listener;

public abstract class GambleListener implements Listener {

    private Gamble gamble;
    private GambleGame gambleGame;

    public GambleListener(Gamble gamble, GambleGame gambleGame) {
        this.gamble = gamble;
        this.gambleGame = gambleGame;
    }
}

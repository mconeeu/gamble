/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.testgame;

import eu.mcone.gamble.api.minigame.GambleGame;
import eu.mcone.gamble.testgame.handler.GameHandler;
import org.bukkit.ChatColor;

public class TestGame extends GambleGame {

    public TestGame() {
        super("TEST-GAME", ChatColor.BLACK, "test");
        setGameHandler(new GameHandler());
    }

    @Override
    public void initiate() {
        sendConsoleMessage("Initialising the testgame...");
    }

    @Override
    public void abandon() {

    }
}

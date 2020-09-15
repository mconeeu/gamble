/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl, Louis Bockel and the MC ONE Minecraftnetwork. All rights reserved
 *  You are not allowed to decompile the code
 */

package eu.mcone.gamble.plugin.state;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.gamble.plugin.Gamble;
import eu.mcone.gamble.plugin.scoreboard.LobbyObjective;
import eu.mcone.gameapi.api.gamestate.common.EndGameState;

public class EndState extends EndGameState {

    public EndState() {
        super();
        setObjective(LobbyObjective.class);
    }

    @Override
    public void onCountdownSecond(CorePlugin plugin, int second) {
        super.onCountdownSecond(plugin, second);

        if (second == 10) {
            Gamble.getInstance().getMiniGamesHandler().getLoader().unloadGames();
        }
    }
}

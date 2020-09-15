/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl, Louis Bockel and the MC ONE Minecraftnetwork. All rights reserved
 *  You are not allowed to decompile the code
 */

package eu.mcone.gamble.plugin.state;

import eu.mcone.gamble.plugin.scoreboard.LobbyObjective;
import eu.mcone.gameapi.api.gamestate.common.LobbyGameState;

public class LobbyState extends LobbyGameState {

    public LobbyState() {
        super();
        setObjective(LobbyObjective.class);
    }
}

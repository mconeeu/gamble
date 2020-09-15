/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl, Louis Bockel and the MC ONE Minecraftnetwork. All rights reserved
 *  You are not allowed to decompile the code
 */

package eu.mcone.gamble.plugin.state;

import eu.mcone.gamble.plugin.Gamble;
import eu.mcone.gameapi.api.event.gamestate.GameStateCountdownEndEvent;
import eu.mcone.gameapi.api.event.gamestate.GameStateCountdownStartEvent;
import eu.mcone.gameapi.api.event.gamestate.GameStateStartEvent;
import eu.mcone.gameapi.api.gamestate.GameState;

public class DiceState extends GameState {

    public DiceState() {
        super("DICE");
    }

    @Override
    public void onStart(GameStateStartEvent event) {
        super.onStart(event);
        Gamble.getInstance().getDiceHandler().requestDice();
    }

    @Override
    public void onCountdownStart(GameStateCountdownStartEvent event) {
        super.onCountdownStart(event);
    }

    @Override
    public void onCountdownEnd(GameStateCountdownEndEvent event) {
        super.onCountdownEnd(event);
    }
}

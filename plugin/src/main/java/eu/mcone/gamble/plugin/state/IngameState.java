/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl, Louis Bockel and the MC ONE Minecraftnetwork. All rights reserved
 *  You are not allowed to decompile the code
 */

package eu.mcone.gamble.plugin.state;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gamble.plugin.Gamble;
import eu.mcone.gamble.plugin.scoreboard.MapObjective;
import eu.mcone.gameapi.api.event.gamestate.GameStateStartEvent;
import eu.mcone.gameapi.api.gamestate.common.InGameState;
import eu.mcone.gameapi.api.player.GamePlayerState;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

public class IngameState extends InGameState {

    public IngameState() {
        setObjective(MapObjective.class);
    }

    @Override
    public void onStart(GameStateStartEvent event) {
        for (Player p : Gamble.getInstance().getPlayerManager().getPlayers(GamePlayerState.PLAYING)) {
            Gamble.getInstance().getGameWorld().teleportSilently(p, "spawn");

            CorePlayer cp = Gamble.getInstance().getGamblePlayer(p.getUniqueId()).getCorePlayer();
            cp.getScoreboard().setNewObjective(new MapObjective());
            cp.getScoreboard().getObjective(DisplaySlot.SIDEBAR).reload();
        }

        Gamble.getInstance().getGameHandler().recalculateRanking();
        Gamble.getInstance().getDiceHandler().requestDice();
        super.onStart(event);
    }
}

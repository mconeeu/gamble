package eu.mcone.gamble.plugin.state;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gamble.api.Gamble;
import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gamble.plugin.GamblePlugin;
import eu.mcone.gamble.plugin.scoreboard.MapObjective;
import eu.mcone.gamble.plugin.util.RangCalculation;
import eu.mcone.gameapi.api.event.gamestate.GameStateStartEvent;
import eu.mcone.gameapi.api.gamestate.common.InGameState;
import eu.mcone.gameapi.api.player.GamePlayerState;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

/**
 * Datei erstellt von: Felix Schmid in Projekt: mcone-gamble
 */
public class IngameState extends InGameState {

    public IngameState() {}

    @Override
    public void onStart(GameStateStartEvent event) {
        for (Player p : GamblePlugin.getInstance().getPlayerManager().getPlayers(GamePlayerState.PLAYING)) {
            GamblePlugin.getInstance().getGameWorld().teleport(p, "spawn");

            CorePlayer cp = GamblePlugin.getInstance().getGamblePlayer(p.getUniqueId()).getCorePlayer();
            cp.getScoreboard().setNewObjective(new MapObjective());
            cp.getScoreboard().getObjective(DisplaySlot.SIDEBAR).reload();
        }

        GamblePlugin.getInstance().setPlayerRanking(RangCalculation.recalculate());
        GamblePlugin.getInstance().requestDice();

        super.onStart(event);
    }
}

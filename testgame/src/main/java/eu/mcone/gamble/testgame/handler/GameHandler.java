package eu.mcone.gamble.testgame.handler;

import eu.mcone.gamble.api.minigame.EndReason;
import eu.mcone.gamble.api.Gamble;
import eu.mcone.gamble.api.minigame.GamePhase;
import eu.mcone.gamble.api.minigame.GameResult;
import eu.mcone.gamble.testgame.TestGame;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GameHandler extends eu.mcone.gamble.api.minigame.GameHandler {

    @Override
    public void gamePhaseSwitched(GamePhase phase) {
        if (phase.equals(GamePhase.INGAME)) {
            TestGame.getInstance().getMessenger().broadcast("Ingame...");
            final Player player = (Player) Bukkit.getOnlinePlayers().toArray()[0];
            TestGame.getInstance().getMessenger().broadcast("Alter mittem im game simma");
            finishGame(EndReason.ENDED, new GameResult(Gamble.getInstance().getGamblePlayer(player.getUniqueId()), 1, 8));
        } else if (phase.equals(GamePhase.END)) {
            TestGame.getInstance().getMessenger().broadcast("Stopping minigame...");
        }
    }
}

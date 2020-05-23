package eu.mcone.gamble.testgame;

import eu.mcone.gamble.api.EndReason;
import eu.mcone.gamble.api.Gamble;
import eu.mcone.gamble.api.helper.DefaultLobbyCallback;
import eu.mcone.gamble.api.minigame.GambleGame;
import eu.mcone.gamble.api.minigame.GambleGamePhase;
import eu.mcone.gamble.api.minigame.GambleGameResult;
import eu.mcone.gamble.api.minigame.GambleGameType;
import eu.mcone.gameapi.api.GamePlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Datei erstellt von: Felix Schmid in Projekt: mcone-gamble
 */
public class TestGame extends GambleGame {

    private Gamble gamble;
    private GamePlugin gamePlugin;

    public TestGame() {

    }

    @Override
    public void initiate(Gamble gamble) {
        this.gamble = gamble;
        this.gamePlugin = (GamePlugin) gamble;
        gamePlugin.sendConsoleMessage("Initialising the testgame...");

        gamble.getMinigameHelper().setLobbyCountdown(10);
        gamble.getMinigameHelper().setLobbyCountdownCallback(new DefaultLobbyCallback(new ArrayList<Player>(Bukkit.getOnlinePlayers())));
    }

    @Override
    public void phaseSwitched(GambleGamePhase gamePhase) {
        if (gamePhase == GambleGamePhase.INGAME) {
            gamePlugin.getMessenger().broadcast("Ingame...");
            GambleGameResult[] results = new GambleGameResult[1];
            Player player = (Player) Bukkit.getOnlinePlayers().toArray()[0];
            Iterator<? extends Player> it = Bukkit.getOnlinePlayers().iterator();

            while (it.hasNext()) {
                Player p = it.next();
                if (p.getName().equalsIgnoreCase("schmiddinger")) {
                    player = p;
                }
            }


            gamePlugin.getMessenger().broadcast("Alter mittem im game simma");

            results[0] = new GambleGameResult(gamble.getGamblePlayer(player.getUniqueId()), 1, 8);
            Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                @Override
                public void run() {
                    gamble.finishGambleGame(GambleGameType.TEST, EndReason.ENDED, results);
                }
            }, 5 * 20);
        } else if (gamePhase == GambleGamePhase.END) {
            gamePlugin.getMessenger().broadcast("Stopping minigame...");
        }
    }
}

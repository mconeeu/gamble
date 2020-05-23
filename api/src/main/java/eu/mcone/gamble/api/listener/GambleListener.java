package eu.mcone.gamble.api.listener;

import eu.mcone.gamble.api.Gamble;
import eu.mcone.gamble.api.minigame.GambleGame;
import org.bukkit.event.Listener;

/**
 * Datei erstellt von: Felix Schmid in Projekt: mcone-gamble
 */
public abstract class GambleListener implements Listener {

    private Gamble gamble;
    private GambleGame gambleGame;

    public GambleListener(Gamble gamble, GambleGame gambleGame) {
        this.gamble = gamble;
        this.gambleGame = gambleGame;
    }
}

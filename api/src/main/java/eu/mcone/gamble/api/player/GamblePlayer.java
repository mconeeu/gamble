package eu.mcone.gamble.api.player;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gamble.api.Gamble;
import org.bukkit.entity.Player;

/**
 * Datei erstellt von: Felix Schmid in Projekt: mcone-gamble
 */
public interface GamblePlayer extends Comparable<GamblePlayer> {

    Player getPlayer();

    int getCurrentPosition();

    void changePosition(Gamble gamble, int position);

    int getCurrentPlacing();

    default CorePlayer getCorePlayer() {
        return CoreSystem.getInstance().getCorePlayer(getPlayer());
    }

}

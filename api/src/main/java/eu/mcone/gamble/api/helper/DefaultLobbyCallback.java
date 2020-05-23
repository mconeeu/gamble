package eu.mcone.gamble.api.helper;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.util.CoreTitle;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Datei erstellt von: Felix Schmid in Projekt: mcone-gamble
 */
public class DefaultLobbyCallback implements CountdownCallback {

    private List<Player> players;

    public DefaultLobbyCallback(List<Player> players) {
        this.players = players;
    }

    @Override
    public void tick(int second) {
        CoreTitle title = CoreSystem.getInstance().createTitle()
                .title("")
                .subTitle(color(second))
                .stay(1)
                .fadeIn(1)
                .fadeOut(1);
        players.forEach(title::send);
    }

    private String color(int second) {
        String prefix;

        switch (second) {
            case 3:
                prefix = "§c";
                break;
            case 2:
                prefix = "§e";
                break;
            case 1:
                prefix = "§a";
                break;
            default:
                prefix = "§7";
                break;
        }

        return prefix + second;
    }

}

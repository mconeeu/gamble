package eu.mcone.gamble.plugin.player;

import eu.mcone.gamble.api.Gamble;
import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gamble.plugin.GamblePlugin;
import eu.mcone.gamble.plugin.listener.InventoryTriggerListener;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;

/**
 * Datei erstellt von: Felix Schmid in Projekt: mcone-gamble
 */
public class BaseGamblePlayer implements GamblePlayer {

    @Getter
    private Player player;
    @Getter
    @Setter
    private int currentPosition;

    public BaseGamblePlayer(Player player, int currentPosition) {
        this.player = player;
        this.currentPosition = currentPosition;
    }

    @Override
    public void changePosition(Gamble gamble, int position) {
        int tmpPosition = position;
        if (tmpPosition > GamblePlugin.getInstance().getWorldFields()) {
            tmpPosition = GamblePlugin.getInstance().getWorldFields();
        }

        Location loc = GamblePlugin.getInstance().getGameWorld().getLocation("field_" + tmpPosition);

        if (currentPosition == position) {
            player.teleport(loc.clone().add(0, 2, 0));
        } else {
            currentPosition = position;
            //player.setVelocity(new Vector(0, 5, 0));
            Bukkit.getScheduler().scheduleSyncDelayedTask(GamblePlugin.getInstance(), new Runnable() {

                @Override
                public void run() {
                    player.playSound(player.getLocation().add(0, 2, 0), Sound.EXPLODE, 1f, 1f);
                    player.setVelocity(new Vector(0, 60, 0));
                }

            }, 15);
            Bukkit.getScheduler().scheduleSyncDelayedTask(GamblePlugin.getInstance(), new Runnable() {

                @Override
                public void run() {
                    if (currentPosition != position) {
                        if (position > GamblePlugin.getInstance().getWorldFields()) {
                            GamblePlugin.getInstance().getMessenger().send(player, "§7Du bist jetzt §6" + (position - GamblePlugin.getInstance().getWorldFields()) + " §7über dem Ziel!");
                        } else {
                            GamblePlugin.getInstance().getMessenger().send(player, "§7Du bist jetzt auf Feld §6" + position + "§7!");
                        }
                    }
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
                    currentPosition = position;
                    player.teleport(loc.clone().add(0, 2, 0));
                }

            }, 45);
        }
    }

    @Override
    public int getCurrentPlacing() {

        for (Map.Entry<Integer, List<GamblePlayer>> entry : GamblePlugin.getInstance().getPlayerRanking().entrySet()) {
            if (entry.getValue().contains(this)) {
                return entry.getKey();
            }
        }

        return 0;
    }

    @Override
    public int compareTo(GamblePlayer o) {
        return Integer.compare(o.getCurrentPosition(), currentPosition);
    }

    public void giveMapItems() {
        player.getInventory().setItem(8, InventoryTriggerListener.OVERVIEW);
    }

}

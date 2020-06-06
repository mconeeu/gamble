/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.plugin.player;

import eu.mcone.gamble.plugin.Gamble;
import eu.mcone.gamble.plugin.inventory.OverviewInventory;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;

public class GamblePlayer implements eu.mcone.gamble.api.player.GamblePlayer {

    @Getter
    private final Player player;
    @Getter
    @Setter
    private int currentPosition;

    public GamblePlayer(Player player, int currentPosition) {
        this.player = player;
        this.currentPosition = currentPosition;
    }

    public void changePosition(int position) {
        int tmpPosition = position;
        if (tmpPosition > Gamble.getInstance().getGameFields()) {
            tmpPosition = Gamble.getInstance().getGameFields();
        }

        Location loc = Gamble.getInstance().getGameWorld().getLocation("field_" + tmpPosition);

        if (currentPosition == position) {
            player.teleport(loc.clone().add(0, 2, 0));
        } else {
            currentPosition = position;
            //player.setVelocity(new Vector(0, 5, 0));
            Bukkit.getScheduler().scheduleSyncDelayedTask(Gamble.getInstance(), () -> {
                player.playSound(player.getLocation().add(0, 2, 0), Sound.EXPLODE, 1f, 1f);
                player.setVelocity(new Vector(0, 60, 0));
            }, 15);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Gamble.getInstance(), () -> {
                if (currentPosition != position) {
                    if (position > Gamble.getInstance().getGameFields()) {
                        Gamble.getInstance().getMessenger().send(player, "§7Du bist jetzt §6" + (position - Gamble.getInstance().getGameFields()) + " §7über dem Ziel!");
                    } else {
                        Gamble.getInstance().getMessenger().send(player, "§7Du bist jetzt auf Feld §6" + position + "§7!");
                    }
                }
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
                currentPosition = position;
                player.teleport(loc.clone().add(0, 2, 0));
            }, 45);
        }
    }

    @Override
    public int getCurrentPlacing() {
        for (Map.Entry<Integer, List<eu.mcone.gamble.api.player.GamblePlayer>> entry : Gamble.getInstance().getGameHandler().getPlayerRanking().entrySet()) {
            if (entry.getValue().contains(this)) {
                return entry.getKey();
            }
        }

        return 0;
    }

    @Override
    public int compareTo(eu.mcone.gamble.api.player.GamblePlayer o) {
        return Integer.compare(o.getCurrentPosition(), currentPosition);
    }

    public void giveMapItems() {
        player.getInventory().setItem(8, OverviewInventory.OVERVIEW);
    }

}

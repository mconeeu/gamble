/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.api.player;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gamble.api.Gamble;
import org.bukkit.entity.Player;

public interface GamblePlayer extends Comparable<GamblePlayer> {

    Player getPlayer();

    void changePosition(int position);

    int getCurrentPosition();

    int getCurrentPlacing();

    void giveMapItems();

    default CorePlayer getCorePlayer() {
        return CoreSystem.getInstance().getCorePlayer(getPlayer());
    }

}

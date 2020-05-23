/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.plugin.scoreboard;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreObjective;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class LobbyObjective extends eu.mcone.gameapi.api.scoreboard.LobbyObjective {

    @Override
    protected void onRegister(CorePlayer corePlayer) {
        super.onRegister(corePlayer);
        setDisplayName("    §6§lGamble");

        setScore(3, "");
        setScore(2, "§8» §7Wartende Spieler:");
        onReload(corePlayer);
    }

    @Override
    protected void onReload(CorePlayer corePlayer) {
        super.onReload(corePlayer);
        setScore(1, "§f  " + Bukkit.getOnlinePlayers().size());
    }

}

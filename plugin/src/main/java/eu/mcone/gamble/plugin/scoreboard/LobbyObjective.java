/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl, Louis Bockel and the MC ONE Minecraftnetwork. All rights reserved
 *  You are not allowed to decompile the code
 */

package eu.mcone.gamble.plugin.scoreboard;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreSidebarObjectiveEntry;
import org.bukkit.Bukkit;

public class LobbyObjective extends eu.mcone.gameapi.api.scoreboard.LobbyObjective {

    @Override
    protected void onLobbyRegister(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry) {
        super.onRegister(corePlayer, entry);
        setDisplayName("    §6§lGamble");

        entry.setScore(3, "");
        entry.setScore(2, "§8» §7Wartende Spieler:");
        onReload(corePlayer, entry);
    }

    @Override
    protected void onLobbyReload(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry) {
        super.onReload(corePlayer, entry);
        entry.setScore(1, "§f  " + Bukkit.getOnlinePlayers().size());
    }
}

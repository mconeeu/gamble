/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl, Louis Bockel and the MC ONE Minecraftnetwork. All rights reserved
 *  You are not allowed to decompile the code
 */

package eu.mcone.gamble.plugin.scoreboard;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreSidebarObjectiveEntry;
import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gamble.plugin.Gamble;
import eu.mcone.gameapi.api.scoreboard.InGameObjective;

public class MapObjective extends InGameObjective {
    public MapObjective() {
    }

    @Override
    protected void onInGameRegister(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry) {
        setDisplayName("    §6§lGamble");
        entry.setScore(9, "");
        entry.setScore(8, "§8» §7Dein Feld:");
        entry.setScore(6, "");
        entry.setScore(5, "§8» §7Aktueller Platz:");
        entry.setScore(3, "");
        entry.setScore(2, "§8» §7Felder bis zum Ziel:");
        onReload(corePlayer, entry);
    }

    @Override
    protected void onInGameReload(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry) {
        GamblePlayer gp = Gamble.getInstance().getGamblePlayer(corePlayer.getUuid());
        entry.setScore(7, "§f  " + gp.getCurrentPosition() + "/" + Gamble.getInstance().getGameFields());
        entry.setScore(4, "§f  " + gp.getCurrentPlacing());
        entry.setScore(1, "§f  " + (Gamble.getInstance().getGameFields() - gp.getCurrentPosition()));
    }
}

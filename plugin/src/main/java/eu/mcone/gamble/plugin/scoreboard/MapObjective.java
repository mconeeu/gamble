/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.plugin.scoreboard;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreSidebarObjective;
import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gamble.plugin.GamblePlugin;

public class MapObjective extends CoreSidebarObjective {
    public MapObjective() {
        super("gamble_inmap");
    }

    @Override
    protected void onRegister(CorePlayer corePlayer) {
        setDisplayName("    §6§lGamble");

        setScore(9, "");
        setScore(8, "§8» §7Dein Feld:");
        setScore(6, "");
        setScore(5, "§8» §7Aktueller Platz:");
        setScore(3, "");
        setScore(2, "§8» §7Felder bis zum Ziel:");
        onReload(corePlayer);
    }

    @Override
    protected void onReload(CorePlayer corePlayer) {
        GamblePlayer gp = GamblePlugin.getInstance().getGamblePlayer(corePlayer.getUuid());
        setScore(7, "§f  " + gp.getCurrentPosition() + "/" + GamblePlugin.getInstance().getWorldFields());
        setScore(4, "§f  " + gp.getCurrentPlacing());
        setScore(1, "§f  " + (GamblePlugin.getInstance().getWorldFields() - gp.getCurrentPosition()));
    }
}

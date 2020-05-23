/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.plugin.listener;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gamble.api.Gamble;
import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gamble.api.utils.GamblePlayerUtils;
import eu.mcone.gamble.plugin.GamblePlugin;
import eu.mcone.gamble.plugin.player.BaseGamblePlayer;
import eu.mcone.gamble.plugin.scoreboard.LobbyObjective;
import eu.mcone.gameapi.api.event.player.GamePlayerLoadedEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(GamePlayerLoadedEvent event) {
        GamblePlayer gp = new BaseGamblePlayer(event.getBukkitPlayer(), 0);
        GamblePlugin.getInstance().getIngame().add(gp);
        GamblePlayerUtils.resetPlayer(gp);

        CorePlayer cp = event.getCorePlayer();
        cp.getScoreboard().setNewObjective(new LobbyObjective());

        cp.getScoreboard().setNewObjective(new LobbyObjective());

        for (CorePlayer all : CoreSystem.getInstance().getOnlineCorePlayers()) {
            if (all.getScoreboard() != null) {
                if (all.getScoreboard().getObjective(DisplaySlot.SIDEBAR) != null) {
                    all.getScoreboard().getObjective(DisplaySlot.SIDEBAR).reload();
                }
            }
        }

        CoreSystem.getInstance().getWorldManager().getWorld(GamblePlugin.getInstance().getGameConfig().parseConfig().getLobby()).teleport(event.getBukkitPlayer(), "spawn");
    }

}

/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl, Louis Bockel and the MC ONE Minecraftnetwork. All rights reserved
 *  You are not allowed to decompile the code
 */

package eu.mcone.gamble.plugin.listener;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gamble.api.utils.PlayerUtils;
import eu.mcone.gamble.plugin.Gamble;
import eu.mcone.gamble.plugin.scoreboard.LobbyObjective;
import eu.mcone.gamble.plugin.state.PlayingState;
import eu.mcone.gameapi.api.event.player.GamePlayerLoadedEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.scoreboard.DisplaySlot;

public class GeneralPlayerListener implements Listener {

    @EventHandler
    public void onLoad(GamePlayerLoadedEvent e) {
        Gamble.getInstance().registerGamblePlayer(e.getBukkitPlayer());
        PlayerUtils.resetPlayer(e.getBukkitPlayer());

        CorePlayer cp = e.getCorePlayer();
        cp.getScoreboard().setNewObjective(new LobbyObjective());

        for (CorePlayer all : CoreSystem.getInstance().getOnlineCorePlayers()) {
            if (all.getScoreboard() != null) {
                if (all.getScoreboard().getObjective(DisplaySlot.SIDEBAR) != null) {
                    all.getScoreboard().getObjective(DisplaySlot.SIDEBAR).reload();
                }
            }
        }

        CoreSystem.getInstance().getWorldManager().getWorld(Gamble.getInstance().getGameConfig().parseConfig().getLobby()).teleportSilently(e.getBukkitPlayer(), "spawn");
    }

    @EventHandler
    public void onUnload(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        Gamble.getInstance().unregisterGamblePlayer(p.getUniqueId());

        for (CorePlayer cp : CoreSystem.getInstance().getOnlineCorePlayers()) {
            if (cp.getUuid().equals(p.getUniqueId())) {
                cp.getScoreboard().getObjective(DisplaySlot.SIDEBAR).reload();
            }
        }

        Gamble.getInstance().getGameHandler().checkForWin();
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        if (!(Gamble.getInstance().getGameStateManager().getRunning() instanceof PlayingState)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event) {
        event.getDrops().clear();
        event.setDroppedExp(0);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (!(Gamble.getInstance().getGameStateManager().getRunning() instanceof PlayingState)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(Gamble.getInstance().getGameStateManager().getRunning() instanceof PlayingState)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (!(Gamble.getInstance().getGameStateManager().getRunning() instanceof PlayingState)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (!(Gamble.getInstance().getGameStateManager().getRunning() instanceof PlayingState)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (!(Gamble.getInstance().getGameStateManager().getRunning() instanceof PlayingState)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(PlayerInteractEvent e) {
        if (!(Gamble.getInstance().getGameStateManager().getRunning() instanceof PlayingState)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onThunderChange(final ThunderChangeEvent e) {
        if (!(Gamble.getInstance().getGameStateManager().getRunning() instanceof PlayingState)) {
            if (e.toThunderState()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onLightningStrike(LightningStrikeEvent e) {
        if (!(Gamble.getInstance().getGameStateManager().getRunning() instanceof PlayingState)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onHangingBreakEvent(HangingBreakEvent e) {
        if (!(Gamble.getInstance().getGameStateManager().getRunning() instanceof PlayingState)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        if (!(Gamble.getInstance().getGameStateManager().getRunning() instanceof PlayingState)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerAchievementAward(PlayerAchievementAwardedEvent event) {
        event.setCancelled(true);
    }


    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent e) {
        if (!(Gamble.getInstance().getGameStateManager().getRunning() instanceof PlayingState)) {
            e.setCancelled(true);
        }
    }
}

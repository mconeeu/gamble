/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.plugin.listener;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gamble.api.GamblePhase;
import eu.mcone.gamble.plugin.GamblePlugin;
import eu.mcone.gamble.plugin.inventory.OverviewInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryTriggerListener implements Listener {

    public static final ItemStack OVERVIEW;

    static {
        OVERVIEW = new ItemBuilder(Material.COMPASS, 1).displayName("§fSpielübersicht").create();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = e.getItem();
            if(item != null) {
                if(GamblePlugin.getInstance().getGamblePhase() == GamblePhase.DICE || GamblePlugin.getInstance().getGamblePhase() == GamblePhase.MAP) {
                    if(item.getType() == Material.COMPASS) {
                        new OverviewInventory(p);
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

}

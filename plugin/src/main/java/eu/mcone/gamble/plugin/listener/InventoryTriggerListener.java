/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.plugin.listener;

import eu.mcone.gamble.plugin.Gamble;
import eu.mcone.gamble.plugin.inventory.OverviewInventory;
import eu.mcone.gamble.plugin.state.DiceState;
import eu.mcone.gamble.plugin.state.IngameState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryTriggerListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = e.getItem();
            if (item != null) {
                if (Gamble.getInstance().getGameStateManager().getRunning() instanceof DiceState || Gamble.getInstance().getGameStateManager().getRunning() instanceof IngameState) {
                    if (item.getType() == Material.COMPASS) {
                        new OverviewInventory(p);
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

}

/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.plugin.inventory;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.item.Skull;
import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gamble.plugin.Gamble;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class OverviewInventory extends CoreInventory {

    public static final ItemStack OVERVIEW = new ItemBuilder(Material.COMPASS, 1).displayName("§fSpielübersicht").create();

    public OverviewInventory(Player player) {
        super("§6Spielübersicht", player, InventorySlot.ROW_3, InventoryOption.FILL_EMPTY_SLOTS);

        GamblePlayer gp = Gamble.getInstance().getGamblePlayer(player.getUniqueId());

        int slot = 0;

        for (Map.Entry<Integer, List<GamblePlayer>> entry : Gamble.getInstance().getGameHandler().getPlayerRanking().entrySet()) {
            for (GamblePlayer gp1 : entry.getValue()) {
                Skull s = new Skull(gp1.getPlayer().getName(), entry.getKey());
                if (gp1.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                    s.setDisplayName("§6§l" + gp1.getPlayer().getName());
                } else {
                    s.setDisplayName(gp1.getPlayer().getName());
                }
                s.setPlayer(gp1.getPlayer());
                s.lore(
                        "§7Feld: §e" + gp1.getCurrentPosition() + "/" + Gamble.getInstance().getGameFields(),
                        "§7Platz: §e" + gp1.getCurrentPlacing(),
                        "§7Abstand zum Ziel: §e" + (Gamble.getInstance().getGameFields() - gp1.getCurrentPosition()),
                        "§7Abstand zu dir: §e" + Math.abs(gp.getCurrentPosition() - gp1.getCurrentPosition())
                );
                setItem(slot, s.getItemStack());
                slot++;
            }
        }

        openInventory();

    }

}

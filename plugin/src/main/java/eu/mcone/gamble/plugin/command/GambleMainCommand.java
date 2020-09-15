/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl, Louis Bockel and the MC ONE Minecraftnetwork. All rights reserved
 *  You are not allowed to decompile the code
 */

package eu.mcone.gamble.plugin.command;

import eu.mcone.coresystem.api.bukkit.command.CorePlayerCommand;
import eu.mcone.coresystem.api.bukkit.world.CoreLocation;
import eu.mcone.gamble.plugin.Gamble;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;

public class GambleMainCommand extends CorePlayerCommand {

    public GambleMainCommand() {
        super("gamble", "gamble.info");
    }

    @Override
    public boolean onPlayerCommand(Player player, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("test-locations")) {
                player.sendMessage("Testing all locations.");
                int x = 0;
                for (Map.Entry<String, CoreLocation> entry : Gamble.getInstance().getGameWorld().getLocations().entrySet()) {
                    final int fX = x;
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Gamble.getInstance(), () -> {
                        player.sendMessage("Position: " + fX + "(§l" + entry.getKey() + ")");
                        player.teleport(entry.getValue().bukkit());
                    }, 20 * x);
                    x++;
                }
                return true;
            }
        }

        return true;
    }
}

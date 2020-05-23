/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.plugin.command;

import eu.mcone.coresystem.api.bukkit.command.CorePlayerCommand;
import eu.mcone.coresystem.api.bukkit.world.CoreLocation;
import eu.mcone.gamble.api.minigame.GambleGameType;
import eu.mcone.gamble.plugin.GamblePlugin;
import eu.mcone.gameapi.api.GamePlugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Arrays;
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
                for (Map.Entry<String, CoreLocation> entry : GamblePlugin.getInstance().getGameWorld().getLocations().entrySet()) {
                    final int fX = x;
                    Bukkit.getScheduler().scheduleSyncDelayedTask(GamblePlugin.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            player.sendMessage("Position: " + fX + "(§l" + entry.getKey() + ")");
                            player.teleport(entry.getValue().bukkit());
                        }
                    }, 20 * x);
                    x++;
                }
                return true;
            } else if (args[0].equalsIgnoreCase("gamemode")) {
                if (player.getGameMode() == GameMode.SURVIVAL) {
                    player.setGameMode(GameMode.CREATIVE);
                } else {
                    player.setGameMode(GameMode.SURVIVAL);
                }
                return true;
            } else if (args[0].equalsIgnoreCase("lock")) {
                GamblePlugin.getInstance().toggleListeners(true);
                player.sendMessage("Locked map");
                return true;
            } else if (args[0].equalsIgnoreCase("unlock")) {
                GamblePlugin.getInstance().toggleListeners(false);
                player.sendMessage("Unlocked map");
                return true;
            } else if(args[0].equalsIgnoreCase("interrupt")) {
                GamePlugin.getGamePlugin().getGameStateManager().cancelCountdown();
                player.sendMessage("Countdown cancelled!");
                return true;
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("nextgame")) {
                GamblePlugin.getInstance().setNextGameType(GambleGameType.valueOf(args[1]));
                player.sendMessage("Next Game Type = " + GamblePlugin.getInstance().getNextGameType());
                return true;
            }
        }

        GamblePlugin.getInstance().getMessenger().send(player, "§7Gamble §av" + GamblePlugin.getInstance().getDescription().getVersion() + " §7by §6Schmiddinger");
        player.sendMessage("§7Version: §e" + GamblePlugin.getInstance().getDescription().getVersion());
        player.sendMessage("§7Author: §eSchmiddinger");
        player.sendMessage("§7CoreWorld: §e" + GamblePlugin.getInstance().getGameWorld().getName());
        player.sendMessage("§7Current round: §e" + GamblePlugin.getInstance().getCurrentRound());
        player.sendMessage("§7World fields: §e" + GamblePlugin.getInstance().getWorldFields());
        try {
            player.sendMessage("§7Current game: §e" + GamblePlugin.getInstance().getCurrentGame().getName());
            player.sendMessage("§7Last game: §e" + GamblePlugin.getInstance().getLastGambleGame().getDisplayName());
            player.sendMessage("     - §7File: §e" + GamblePlugin.getInstance().getLastGambleGame().getJarFile());
            player.sendMessage("     - §7Author: §e" + GamblePlugin.getInstance().getLastGambleGame().getAuthor());
            player.sendMessage("§7Next game: §e" + GamblePlugin.getInstance().getNextGameType().getDisplayName());
            player.sendMessage("     - §7File: §e" + GamblePlugin.getInstance().getNextGameType().getJarFile());
            player.sendMessage("     - §7Author: §e" + GamblePlugin.getInstance().getNextGameType().getAuthor());
        } catch (Exception ignored) {}
        player.sendMessage("§7Ingame: §e" + GamblePlugin.getInstance().getIngame().toString());
        player.sendMessage("§7Last round result: §e" + Arrays.toString(GamblePlugin.getInstance().getLastGameResult()));
        player.sendMessage("§7Gamble results: §e" + GamblePlugin.getInstance().getGambleResults().toString());


        return true;
    }
}

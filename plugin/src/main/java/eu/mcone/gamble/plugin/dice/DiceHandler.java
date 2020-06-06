/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.plugin.dice;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.util.CoreTitle;
import eu.mcone.gamble.api.minigame.GameResult;
import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gamble.plugin.Gamble;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class DiceHandler implements Listener {

    private final Map<UUID, Integer> results, currentValue;
    private final Random random;
    private BukkitTask cubeTask;

    private final CoreTitle coreTitle;

    public DiceHandler() {
        results = new HashMap<>();
        currentValue = new HashMap<>();
        random = new Random();

        Bukkit.getPluginManager().registerEvents(this, Gamble.getInstance());

        coreTitle = CoreSystem.getInstance().createTitle();
        coreTitle.fadeIn(0);
        coreTitle.fadeOut(0);
        coreTitle.stay(3);
    }


    public void requestDice() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Gamble.getInstance(), () -> {
            Gamble.getInstance().getMessenger().broadcast("§7Jetzt wird gewürfelt!");
            Bukkit.getScheduler().scheduleSyncDelayedTask(Gamble.getInstance(), this::request, 40);
        }, 40);
    }

    public void request() {
        results.clear();
        currentValue.clear();

        AtomicInteger seconds = new AtomicInteger(10);

        BukkitTask countdownTask = Bukkit.getScheduler().runTaskTimer(Gamble.getInstance(), () -> {
            if (seconds.get() > 0) {
                for (eu.mcone.gamble.api.player.GamblePlayer gp : Gamble.getInstance().getGamblePlayers()) {
                    gp.getPlayer().setLevel(seconds.get());
                }

                seconds.getAndDecrement();
            }
        }, 0, 20);


        cubeTask = Bukkit.getScheduler().runTaskTimer(Gamble.getInstance(), () -> {
            if (seconds.get() > 0) {
                for (eu.mcone.gamble.api.player.GamblePlayer gp : Gamble.getInstance().getGamblePlayers()) {
                    if (!results.containsKey(gp.getPlayer().getUniqueId())) {
                        int cube = Math.max(random.nextInt(6 + Gamble.getInstance().getMiniGamesHandler().getExtraCubesForPlayer(gp)), 1);
                        currentValue.put(gp.getPlayer().getUniqueId(), cube);
                        coreTitle.title((cube <= 6 ? "" + cube : "§6" + cube)).send(gp.getPlayer());
                        gp.getPlayer().playSound(gp.getPlayer().getLocation(), Sound.CLICK, 1f, 1f);
                    }
                }
            } else {
                countdownTask.cancel();

                for (Map.Entry<UUID, Integer> entry : currentValue.entrySet()) {
                    GamblePlayer player = Gamble.getInstance().getGamblePlayer(entry.getKey());

                    if (!results.containsKey(entry.getKey())) {
                        results.put(entry.getKey(), entry.getValue());
                        Gamble.getInstance().getMessenger().send(player.getPlayer(), "§7Du hast eine §f" + entry.getValue() + " §7gewürfelt.");
                    }

                    player.changePosition(player.getCurrentPosition() + entry.getValue());
                    player.getCorePlayer().getScoreboard().getObjective(DisplaySlot.SIDEBAR).reload();
                    player.giveMapItems();
                }


                Bukkit.getScheduler().scheduleSyncDelayedTask(Gamble.getInstance(), () -> {
                    if (!Gamble.getInstance().getGameHandler().checkForWin()) {
                        Gamble.getInstance().getMessenger().broadcast("Das nächste Spiel wird gezogen!");

                        Bukkit.getScheduler().scheduleSyncDelayedTask(Gamble.getInstance(), () -> {
                            try {
                                Gamble.getInstance().getMiniGamesHandler().startRandomGame();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }, 40);
                    }
                }, 50);

                cubeTask.cancel();
            }
        }, 0, 2);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (!results.containsKey(e.getPlayer().getUniqueId()) && Gamble.getInstance().getGamePlayer(e.getPlayer()) != null) {
            if (currentValue.containsKey(e.getPlayer().getUniqueId())) {
                int current = currentValue.get(e.getPlayer().getUniqueId());
                results.put(e.getPlayer().getUniqueId(), current);
                coreTitle.title((current <= 6 ? "" + current : "§6" + current)).send(e.getPlayer());
                Gamble.getInstance().getMessenger().send(e.getPlayer(), "§7Du hast eine §f" + current + " §7gewürfelt.");
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.LEVEL_UP, 1f, 1f);
            }
        }
    }

}

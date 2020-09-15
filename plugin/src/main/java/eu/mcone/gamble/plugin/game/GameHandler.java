/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl, Louis Bockel and the MC ONE Minecraftnetwork. All rights reserved
 *  You are not allowed to decompile the code
 */

package eu.mcone.gamble.plugin.game;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.util.CoreTitle;
import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gamble.api.utils.OrderUtils;
import eu.mcone.gamble.api.utils.PlayerUtils;
import eu.mcone.gamble.plugin.Gamble;
import eu.mcone.gamble.plugin.state.EndState;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;

public class GameHandler {

    @Getter
    private final Map<Integer, List<GamblePlayer>> playerRanking;

    public GameHandler() {
        playerRanking = new HashMap<>();
    }

    public boolean checkForWin() {
        if (isPlayerInGoal()) {
            Bukkit.getScheduler().cancelAllTasks();

            Map<Integer, List<GamblePlayer>> ordered = OrderUtils.sortByKeyDescending(playerRanking);

            int placedIndex = 0;
            Gamble.getInstance().getMessenger().broadcast("§f§lDas Spiel ist vorbei. So ist es ausgegangen:");
            for (Map.Entry<Integer, List<GamblePlayer>> entry : ordered.entrySet()) {
                StringJoiner names = new StringJoiner(", ");
                final int[] rf = {0};
                entry.getValue().forEach(x -> rf[0] = x.getCurrentPosition());

                entry.getValue().forEach(x -> names.add(x.getPlayer().getName()));
                String field = (entry.getKey() < Gamble.getInstance().getGameFields())
                        ? ("Feld §f" + rf[0])
                        : ("§f" + (rf[0] - Gamble.getInstance().getGameFields()) + "§7 Felder über dem Ziel");

                if (placedIndex == 0) {
                    CoreTitle wonTitle = CoreSystem.getInstance().createTitle()
                            .title("§6§l" + names.toString())
                            .subTitle("§f§rHat das Spiel gewonnen!")
                            .stay(3)
                            .fadeIn(2)
                            .fadeOut(2);
                    Bukkit.getOnlinePlayers().forEach(wonTitle::send);
                    Gamble.getInstance().getMessenger().broadcast("  §61§7. §6§l" + names.toString() + " §7(" + field + ")");
                } else if ((placedIndex == 1 || placedIndex == 2) && entry.getValue().size() != 0) {
                    Gamble.getInstance().getMessenger().broadcast("  §6" + (placedIndex + 1) + "§7. §6" + names.toString() + " §7(" + field + ")");
                } else {
                    final int pl = placedIndex;
                    entry.getValue().forEach(x -> {
                        Gamble.getInstance().getMessenger().send(x.getPlayer(), "§7Du wurdest §6" + (pl + 1) + "§7.");
                    });
                }
                placedIndex++;
            }

            for (Player p : Bukkit.getOnlinePlayers()) {
                PlayerUtils.resetPlayer(p);
                Gamble.getInstance().getGameWorld().teleportSilently(p, "finish");
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 1f, 1f);
            }

            Gamble.getInstance().getGameStateManager().setGameState(new EndState(), true);
            return true;
        } else if (Gamble.getInstance().getGamblePlayers().size() == 1) {
            Bukkit.getScheduler().cancelAllTasks();

            for (Player p : Bukkit.getOnlinePlayers()) {
                PlayerUtils.resetPlayer(p);

                Gamble.getInstance().getGameWorld().teleportSilently(p, "finish");
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 1f, 1f);
            }

            Gamble.getInstance().getMessenger().broadcast("§7Du hast das Spiel §6gewonnen!");
            Gamble.getInstance().getGameStateManager().setGameState(new EndState(), true);
        }

        return false;
    }

    public boolean isPlayerInGoal() {
        for (GamblePlayer gp : Gamble.getInstance().getGamblePlayers()) {
            if (gp.getCurrentPosition() >= Gamble.getInstance().getGameFields()) {
                return true;
            }
        }
        return false;
    }

    public void recalculateRanking() {
        Map<Integer, List<GamblePlayer>> m = new HashMap<>();

        for (GamblePlayer gp : Gamble.getInstance().getGamblePlayers()) {
            if (m.containsKey(gp.getCurrentPosition())) {
                m.get(gp.getCurrentPosition()).add(gp);
            } else {
                m.put(gp.getCurrentPosition(), new ArrayList<GamblePlayer>() {{
                    add(gp);
                }});
            }
        }

        List<Integer> sorted = new ArrayList<>(m.keySet());
        Collections.sort(sorted);

        int x = 1;
        for (int i = sorted.size() - 1; i >= 0; i--) {
            playerRanking.put(x, m.get(sorted.get(i)));
            x++;
        }
    }
}

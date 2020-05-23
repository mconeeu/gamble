/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.plugin.helper;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.util.CoreTitle;
import eu.mcone.gamble.api.Gamble;
import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gamble.api.utils.OrderUtils;
import eu.mcone.gamble.plugin.GamblePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class FinishGambleHelper {

    public boolean request() {
        if (playerIsInGoal()) {
            finish();
            return true;
        }
        return false;
    }

    public void finish() {
        Map<Integer, List<GamblePlayer>> ordered = OrderUtils.sortByKeyDescending(GamblePlugin.getInstance().getPlayerRanking());
        if (Gamble.DEBUG) System.out.println("Gamble: checking game finish, Ordered list: " + ordered.toString());

        int placedIndex = 0;
        GamblePlugin.getInstance().getMessenger().broadcast("§f§lDas Spiel ist vorbei. So ist es ausgegangen:");
        for (Map.Entry<Integer, List<GamblePlayer>> entry : ordered.entrySet()) {
            StringJoiner names = new StringJoiner(", ");
            final int[] rf = {0};
            entry.getValue().forEach(x -> rf[0] = x.getCurrentPosition());

            entry.getValue().forEach(x -> names.add(x.getPlayer().getName()));
            String field = (entry.getKey() < GamblePlugin.getInstance().getWorldFields())
                    ? ("Feld §f" + rf[0])
                    : ("§f" + (rf[0] - GamblePlugin.getInstance().getWorldFields()) + "§7 Felder über dem Ziel");

            if (placedIndex == 0) {
                CoreTitle wonTitle = CoreSystem.getInstance().createTitle()
                        .title("§6§l" + names.toString())
                        .subTitle("§f§rHat das Spiel gewonnen!")
                        .stay(3)
                        .fadeIn(2)
                        .fadeOut(2);
                Bukkit.getOnlinePlayers().forEach(wonTitle::send);
                GamblePlugin.getInstance().getMessenger().broadcast("  §61§7. §6§l" + names.toString() + " §7(" + field + ")");
            } else if ((placedIndex == 1 || placedIndex == 2) && entry.getValue().size() != 0) {
                GamblePlugin.getInstance().getMessenger().broadcast("  §6" + (placedIndex + 1) + "§7. §6" + names.toString() + " §7(" + field + ")");
            } else {
                final int pl = placedIndex;
                entry.getValue().forEach(x -> {
                    GamblePlugin.getInstance().getMessenger().send(x.getPlayer(), "§7Du wurdest §6" + (pl + 1) + "§7.");
                });
            }
            placedIndex++;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            GamblePlugin.getInstance().getGameWorld().teleport(p, "finish");
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 1f, 1f);
        }

        GamblePlugin.getInstance().getGameStateManager().setNextGameState(true);
    }

    public boolean playerIsInGoal() {
        for (GamblePlayer gp : GamblePlugin.getInstance().getIngame()) {
            if (gp.getCurrentPosition() >= GamblePlugin.getInstance().getWorldFields()) {
                return true;
            }
        }
        return false;
    }


}

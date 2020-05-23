/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.plugin.util;

import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gamble.plugin.GamblePlugin;

import java.util.*;

public class RangCalculation {

    public static Map<Integer, List<GamblePlayer>> recalculate() {
        Map<Integer, List<GamblePlayer>> m = new HashMap<>();

        for (GamblePlayer gp : GamblePlugin.getInstance().getIngame()) {
            if (m.get(gp.getCurrentPosition()) != null) {
                m.get(gp.getCurrentPosition()).add(gp);
            } else {
                m.put(gp.getCurrentPosition(), new ArrayList<>(Collections.singleton(gp)));
            }
        }

        Map<Integer, List<GamblePlayer>> positions = new HashMap<>();
        List<Integer> sorted = new ArrayList<>(m.keySet());
        Collections.sort(sorted);

        int x = 1;
        for (int i = sorted.size() - 1; i >= 0; i--) {
            positions.put(x, m.get(sorted.get(i)));
            x++;
        }

        return positions;
    }

}

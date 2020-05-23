/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.api.minigame;

import eu.mcone.gamble.api.player.GamblePlayer;
import lombok.Getter;

@Getter
public class GambleGameResult {

    private GamblePlayer player;
    private int placement, extraCubeSize;

    public GambleGameResult(GamblePlayer player, int placement, int extraCubeSize) {
        this.player = player;
        this.placement = placement;
        this.extraCubeSize = extraCubeSize;
    }
}

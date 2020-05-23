package eu.mcone.gamble.api.minigame;

import eu.mcone.gamble.api.player.GamblePlayer;
import lombok.Getter;

/**
 * Datei erstellt von: Felix Schmid in Projekt: mcone-gamble
 */

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

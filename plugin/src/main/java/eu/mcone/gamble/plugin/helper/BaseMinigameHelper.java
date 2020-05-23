/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.plugin.helper;

import eu.mcone.gamble.api.helper.CountdownCallback;
import eu.mcone.gamble.api.helper.MinigameHelper;
import lombok.Getter;

@Getter
public class BaseMinigameHelper implements MinigameHelper {

    @Getter
    private CountdownCallback callback;
    private Integer countdownTime = 10;

    @Override
    public void setLobbyCountdown(int time) {
        countdownTime = time;
        System.out.println("CountdownTime " + time);
    }

    @Override
    public void setLobbyCountdownCallback(CountdownCallback callback) {
        this.callback = callback;
        System.out.println("Callback set!");
    }

}

/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.api.helper;

public interface MinigameHelper {

    /**
     * Countdown which will run before minigame get started
     * @param time Seconds
     */
    void setLobbyCountdown(int time);

    /**
     * Sets the callback which get run every second. For more
     * see CountdownCallback.java class
     * @param callback Callback object
     */
    void setLobbyCountdownCallback(CountdownCallback callback);

}

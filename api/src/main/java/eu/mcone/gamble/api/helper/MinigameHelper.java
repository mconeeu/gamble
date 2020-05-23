package eu.mcone.gamble.api.helper;

/**
 * Datei erstellt von: Felix Schmid in Projekt: mcone-gamble
 */
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

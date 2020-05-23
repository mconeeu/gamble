package eu.mcone.gamble.plugin.helper;

import eu.mcone.gamble.api.helper.CountdownCallback;
import eu.mcone.gamble.api.helper.MinigameHelper;
import lombok.Getter;

/**
 * Datei erstellt von: Felix Schmid in Projekt: mcone-gamble
 */
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

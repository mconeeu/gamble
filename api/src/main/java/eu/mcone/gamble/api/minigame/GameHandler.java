package eu.mcone.gamble.api.minigame;

import eu.mcone.gamble.api.Gamble;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class GameHandler {

    @Setter
    private GamePhase gamePhase;

    public void finishGame(EndReason reason, GameResult... results) {
        Gamble.getInstance().getMiniGamesHandler().finishCurrentGame(reason, results);
    }

    public abstract void gamePhaseSwitched(GamePhase phase);
}

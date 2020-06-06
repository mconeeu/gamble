package eu.mcone.gamble.api.minigame;

public interface MiniGamesHandler {

    void finishCurrentGame(EndReason reason, GameResult... results);
}

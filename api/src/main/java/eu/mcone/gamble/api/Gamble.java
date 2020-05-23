/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.api;

import eu.mcone.gamble.api.helper.MinigameHelper;
import eu.mcone.gamble.api.listener.GambleListener;
import eu.mcone.gamble.api.minigame.GambleGame;
import eu.mcone.gamble.api.minigame.GambleGamePhase;
import eu.mcone.gamble.api.minigame.GambleGameResult;
import eu.mcone.gamble.api.minigame.GambleGameType;
import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gameapi.api.GameAPI;
import org.bukkit.Bukkit;

import java.util.UUID;

public interface Gamble {

    public static final boolean DEBUG = true;

    /*Gamble getInstance();*/

    @Deprecated
    GameAPI getGameAPI();

    @Deprecated
    GambleGamePhase getGamePhase();

    GambleGameType getRandomGambleGameType();

    void finishGambleGame(GambleGameType type, EndReason reason, GambleGameResult... results);

    GambleGameResult[] getLastGameResult();

    GambleGameResult[] getGameResult(int round);

    int getCurrentRound();

    GamblePlayer getGamblePlayer(UUID uuid);

    MinigameHelper getMinigameHelper();

    default void registerListener(GambleGame game, GambleListener listener) {
        Bukkit.getPluginManager().registerEvents(listener, game);
        if (DEBUG) {
            getGameAPI().getMessenger().broadcast("§b[DEBUG] §7" + game.getName() + " registered a new listener!");
            getGameAPI().sendConsoleMessage("§b[DEBUG] §7" + game.getName() + " registered a new listener!");
        }
    }

}

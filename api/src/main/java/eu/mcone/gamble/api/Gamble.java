/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl, Louis Bockel and the MC ONE Minecraftnetwork. All rights reserved
 *  You are not allowed to decompile the code
 */

package eu.mcone.gamble.api;

import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.gamble.api.minigame.MiniGamesHandler;
import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gameapi.api.GamePlugin;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;

import java.util.Collection;
import java.util.UUID;

@Getter
public abstract class Gamble extends GamePlugin {

    @Getter
    private static Gamble instance;
    @Setter
    private MiniGamesHandler miniGamesHandler;

    public Gamble() {
        super("Gamble", ChatColor.GOLD, "gamble.prefix");
        instance = this;
    }

    public abstract MiniGamesHandler getMiniGamesHandler();

    public abstract GamblePlayer getGamblePlayer(UUID uuid);

    public abstract Collection<GamblePlayer> getGamblePlayers();

    public abstract CoreWorld getMinigameWorld();
}

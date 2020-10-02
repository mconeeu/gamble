/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl, Louis Bockel and the MC ONE Minecraftnetwork. All rights reserved
 *  You are not allowed to decompile the code
 */

package eu.mcone.gamble.plugin.game;

import eu.mcone.gamble.api.minigame.GambleGame;
import eu.mcone.gamble.plugin.Gamble;
import eu.mcone.gamble.plugin.util.MinigameUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.*;

public class MiniGamesLoader {

    private final Map<String, GambleGame> games;
    public static final File GAME_DIR = new File(Gamble.getInstance().getDataFolder(), "games");

    public MiniGamesLoader() {
        games = new HashMap<>();

        if (!GAME_DIR.exists()) {
            GAME_DIR.mkdirs();
        }

        Gamble.getInstance().sendConsoleMessage("§aLoading minigames...");
        for (File file : Objects.requireNonNull(GAME_DIR.listFiles())) {
            if (file.getName().contains(".jar")) {
                try {
                    Plugin plugin = Bukkit.getPluginManager().loadPlugin(file);

                    if (plugin instanceof GambleGame) {
                        GambleGame gamePlugin = (GambleGame) plugin;

                        if (!games.containsKey(gamePlugin.getName())) {
                            if (gamePlugin.getGameHandler() != null) {
                                games.put(gamePlugin.getName(), gamePlugin);
                                Gamble.getInstance().sendConsoleMessage("§aMinigame " + gamePlugin.getName() + " loaded!");
                            } else {
                                throw new IllegalArgumentException("No GameHandler set!");
                            }
                        }
                    }
                } catch (IllegalArgumentException | InvalidPluginException | InvalidDescriptionException e) {
                    Gamble.getInstance().sendConsoleMessage("§cCould not load plugin!");
                    e.printStackTrace();
                }
            }
        }
    }

    public List<GambleGame> getGames() {
        return new ArrayList<>(games.values());
    }

    public void unloadGames() {
        for (GambleGame plugin : games.values()) {
            unload(plugin);
        }
    }

    public void unloadGame(String name) {
        if (games.containsKey(name)) {
            unload(games.get(name));
        }
    }

    public void disableGame(String name) {
        if (games.containsKey(name)) {
            GambleGame game = games.get(name);

            game.abandon();
            for (Listener listener : game.getListeners()) {
                HandlerList.unregisterAll(listener);
            }

            Bukkit.getPluginManager().disablePlugin(game);
        }
    }

    public void enableGame(String name) {
        if (games.containsKey(name)) {
            Plugin plugin = games.get(name);
            if (!Bukkit.getPluginManager().isPluginEnabled(plugin)) {
                Bukkit.getPluginManager().enablePlugin(plugin);
            } else {
                Gamble.getInstance().sendConsoleMessage("§aGame " + name + " is already enabled!");
            }
        }
    }

    private void unload(GambleGame game) {
        Gamble.getInstance().sendConsoleMessage("§cUnloading Minigame " + game.getName());
        game.abandon();
        game.onDisable();
        Bukkit.getPluginManager().disablePlugin(game);
        MinigameUtils.unloadPlugin(game);
    }
}

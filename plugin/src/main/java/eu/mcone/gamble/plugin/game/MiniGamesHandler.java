/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl, Louis Bockel and the MC ONE Minecraftnetwork. All rights reserved
 *  You are not allowed to decompile the code
 */

package eu.mcone.gamble.plugin.game;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.util.CoreTitle;
import eu.mcone.gamble.api.minigame.EndReason;
import eu.mcone.gamble.api.minigame.GambleGame;
import eu.mcone.gamble.api.minigame.GamePhase;
import eu.mcone.gamble.api.minigame.GameResult;
import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gamble.api.utils.PlayerUtils;
import eu.mcone.gamble.plugin.Gamble;
import eu.mcone.gamble.plugin.scoreboard.MapObjective;
import eu.mcone.gamble.plugin.state.DiceState;
import eu.mcone.gamble.plugin.state.PlayingState;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.*;

public class MiniGamesHandler implements eu.mcone.gamble.api.minigame.MiniGamesHandler {

    @Getter
    private final MiniGamesLoader loader;
    private final List<String> played;
    private final Map<String, GameResult[]> gameResults;

    @Getter
    private GambleGame currentGame;
    @Getter
    private GambleGame lastGame;
    @Getter
    private int roundCount;

    public MiniGamesHandler() {
        loader = new MiniGamesLoader();
        played = new ArrayList<>();
        gameResults = new HashMap<>();
    }

    public void startRandomGame() {
        if (currentGame == null) {
            GambleGame gambleGame = getRandomNotPlayedGame();

            if (Bukkit.getPluginManager().isPluginEnabled(gambleGame.getName())) {
                loader.disableGame(gambleGame.getName());
            }

            Gamble.getInstance().getGameStateManager().setGameState(new PlayingState(), true);

            loader.enableGame(gambleGame.getName());
            gambleGame.initiate();
            currentGame = gambleGame;
            played.add(currentGame.getName());
            Gamble.getInstance().getMessenger().broadcast("§7Jetzt wird §f" + gambleGame.getPluginColor() + gambleGame.getName() + " §7gespielt!");

            final BukkitTask[] task = new BukkitTask[1];
            final int[] countdownTime = {10};

            currentGame.getGameHandler().gamePhaseSwitched(GamePhase.SELECTED);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Gamble.getInstance(), () -> {
                currentGame.getGameHandler().gamePhaseSwitched(GamePhase.LOBBY);

                for (GamblePlayer player : Gamble.getInstance().getGamblePlayers()) {
                    PlayerUtils.resetPlayer(player.getPlayer());
                }

                task[0] = Bukkit.getScheduler().runTaskTimer(Gamble.getInstance(), () -> {
                    if (countdownTime[0] > 0) {
                        sendCountDown(countdownTime[0]);
                        countdownTime[0]--;
                    } else {
                        currentGame.getGameHandler().gamePhaseSwitched(GamePhase.INGAME);
                        task[0].cancel();
                    }
                }, 0, 20);
            }, 60);

            roundCount++;
        }
    }

    @Override
    public void finishCurrentGame(EndReason reason, GameResult... results) {
        lastGame = currentGame;
        gameResults.put(currentGame.getName(), results);

        if (reason == EndReason.EXCEPTION) {

        } else {
            resetMaps();

            gameResults.put(currentGame.getName(), results != null ? results : new GameResult[]{});

            Gamble.getInstance().getMessenger().broadcast("Das Spiel ist vorbei - so ist es ausgegangen:");
            for (GameResult gameResult : getLastGameResult()) {
                if (gameResult != null && gameResult.getPlayer() != null) {
                    Gamble.getInstance().getMessenger().broadcast("        §6" + gameResult.getPlacement() + ". §f" + gameResult.getPlayer().getPlayer().getName());
                }
            }

            for (GamblePlayer gamblePlayer : Gamble.getInstance().getGamblePlayers()) {
                PlayerUtils.resetPlayer(gamblePlayer.getPlayer());
                gamblePlayer.changePosition(gamblePlayer.getCurrentPosition());
                gamblePlayer.getCorePlayer().getScoreboard().setNewObjective(new MapObjective());
                gamblePlayer.getCorePlayer().getScoreboard().getObjective(DisplaySlot.SIDEBAR).reload();
                gamblePlayer.giveMapItems();
            }

            Gamble.getInstance().getGameStateManager().setGameState(new DiceState(), true);
        }

        loader.disableGame(currentGame.getName());
        currentGame = null;
    }

    public int getExtraCubesForPlayer(GamblePlayer player) {
        for (GameResult result : getLastGameResult()) {
            if (result.getPlayer() == player) {
                return result.getExtraCubeSize();
            }
        }

        return 0;
    }

    public GameResult[] getLastGameResult() {
        if (lastGame != null) {
            return gameResults.getOrDefault(lastGame.getName(), new GameResult[0]);
        }

        return new GameResult[0];
    }

    private void resetMaps() {
        for (World world : Bukkit.getWorlds()) {
            world.setThunderDuration(0);
            world.setThundering(false);
            world.setStorm(false);
            Gamble.getInstance().getGameWorld().purgeAnimals();
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("doMobSpawning", "false");
        }
    }

    public GambleGame getRandomGame() {
        return loader.getGames().get(Math.max(new Random(loader.getGames().size()).nextInt(loader.getGames().size()), 0));
    }

    public GambleGame getRandomNotPlayedGame() {
        Random random = new Random(loader.getGames().size());
        GambleGame game = null;

        int i = 0;
        while (game == null) {
            if (i <= loader.getGames().size()) {
                GambleGame randomGame = loader.getGames().get(Math.max(random.nextInt(loader.getGames().size()), 0));
                if (!played.contains(randomGame.getName())) {
                    game = randomGame;
                }

                i++;
            } else {
                played.clear();
                i = 0;
            }
        }

        return game;
    }

    private void sendCountDown(int second) {
        String prefix;

        switch (second) {
            case 3:
                prefix = "§c";
                Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1));
                break;
            case 2:
                prefix = "§e";
                Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1));
                break;
            case 1:
                prefix = "§a";
                Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1));
                break;
            default:
                prefix = "§7";
                break;
        }

        CoreTitle title = CoreSystem.getInstance().createTitle()
                .title("")
                .subTitle(prefix + second)
                .stay(1)
                .fadeIn(1)
                .fadeOut(1);
        Bukkit.getOnlinePlayers().forEach(title::send);
    }
}

/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.plugin;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.gamble.api.EndReason;
import eu.mcone.gamble.api.Gamble;
import eu.mcone.gamble.api.GamblePhase;
import eu.mcone.gamble.api.helper.MinigameHelper;
import eu.mcone.gamble.api.listener.GambleListener;
import eu.mcone.gamble.api.listener.LockListener;
import eu.mcone.gamble.api.minigame.GambleGame;
import eu.mcone.gamble.api.minigame.GambleGamePhase;
import eu.mcone.gamble.api.minigame.GambleGameResult;
import eu.mcone.gamble.api.minigame.GambleGameType;
import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gamble.api.utils.GamblePlayerUtils;
import eu.mcone.gamble.api.utils.MinigameUtils;
import eu.mcone.gamble.plugin.command.GambleMainCommand;
import eu.mcone.gamble.plugin.dice.Dice;
import eu.mcone.gamble.plugin.helper.BaseMinigameHelper;
import eu.mcone.gamble.plugin.helper.FinishGambleHelper;
import eu.mcone.gamble.plugin.listener.InventoryTriggerListener;
import eu.mcone.gamble.plugin.listener.PlayerJoinListener;
import eu.mcone.gamble.plugin.player.BaseGamblePlayer;
import eu.mcone.gamble.plugin.scoreboard.MapObjective;
import eu.mcone.gamble.plugin.state.EndState;
import eu.mcone.gamble.plugin.state.IngameState;
import eu.mcone.gamble.plugin.state.LobbyState;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.*;

@Getter
public class GamblePlugin extends GamePlugin implements Gamble {

    @Getter
    private static GamblePlugin instance;
    private CoreWorld gameWorld;
    private GambleGameType lastGambleGame;
    private int currentRound, worldFields;
    private Random random;
    private GambleGame currentGame;
    @Setter
    private GambleGameType nextGameType;
    private List<GamblePlayer> ingame;
    private Map<Integer, GambleGameResult[]> gambleResults;
    private Dice dice;
    private LockListener lockListener;
    private List<GambleListener> minigameListeners;
    @Setter
    private Map<Integer, List<GamblePlayer>> playerRanking;
    @Setter
    private GamblePhase gamblePhase;
    private MinigameHelper minigameHelper;
    private FinishGambleHelper finishGambleHelper;

    public GamblePlugin() {
        super("Gamble", ChatColor.GOLD, "gamble.prefix");
    }

    @Override
    public void onGameEnable() {
        instance = this;
        gameWorld = CoreSystem.getInstance().getWorldManager().getWorld(GamblePlugin.getInstance().getGameConfig().parseConfig().getGameWorld());
        dice = new Dice();
        playerRanking = new HashMap<>();

        getPlayerManager();
        getDamageLogger();
        getGameStateManager().addGameState(new LobbyState())
                .addGameState(new IngameState())
                .addGameState(new EndState())
                .startGame();

        CoreSystem.getInstance().getTranslationManager().loadAdditionalCategories("gamble");

        registerEvents(
                new PlayerJoinListener(),
                new InventoryTriggerListener()
        );
        registerCommands(
                new GambleMainCommand()
        );

        ingame = new ArrayList<>();
        gambleResults = new HashMap<>();
        currentRound = 0;
        random = new Random();
        minigameListeners = new ArrayList<>();
        currentGame = null;
        lastGambleGame = null;
        countFields();
        resetMaps();
        lockListener = new LockListener(this, null);
        toggleListeners(true);
        gamblePhase = GamblePhase.LOBBY;
        minigameHelper = new BaseMinigameHelper();
        finishGambleHelper = new FinishGambleHelper();

        sendConsoleMessage("§aVersion §f" + this.getDescription().getVersion() + "§a enabled...");
    }

    private void countFields() {
        gameWorld.getLocations().forEach((k, v) -> {
            if (k.startsWith("field_")) {
                worldFields++;
            }
        });
    }

    public void toggleListeners(boolean enable) {
        if (enable) {
            registerEvents(
                    lockListener
            );
        } else {
            HandlerList.unregisterAll(lockListener);
        }
    }

    public void initiateNextGameRound() throws Exception {
        currentRound++;
        for (GamblePlayer gp : ingame) {
            //TODO: Disable scoreboard
            /*gp.getCorePlayer().getScoreboard().getObjective(DisplaySlot.SIDEBAR);*/
        }
        initiateGame(getRandomGambleGameType(), 1);
    }

    private void initiateGame(GambleGameType type, int x) throws Exception {
        if (currentGame == null) {
            Plugin plugin = null;
            try {
                if (x == 1) {
                    getMessenger().broadcast("§7Jetzt wird §f" + type.getDisplayName() + " §7gespielt!");
                }
                plugin = Bukkit.getPluginManager().loadPlugin(type.getJarFile());
            } catch (Exception e) {
                for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
                    if (p.getName().startsWith("gamble-")) {
                        sendConsoleMessage("Unloading " + p.getName() + "!");
                        MinigameUtils.unloadPlugin(p);
                    }
                }
                plugin = Bukkit.getPluginManager().loadPlugin(type.getJarFile());
            }

            if (plugin instanceof GambleGame) {
                currentGame = (GambleGame) plugin;
                Bukkit.getPluginManager().enablePlugin(plugin);
                currentGame.initiate(this);

                startGame(plugin);
            } else {
                Bukkit.getLogger().warning("Can't load " + type + "!");
                if (plugin != null) {
                    Bukkit.getPluginManager().disablePlugin(plugin);
                    MinigameUtils.unloadPlugin(plugin);
                }
                if (x < 10) {
                    sendConsoleMessage("Trying to load next game");
                    initiateGame(getRandomGambleGameType(), x + 1);
                } else {
                    //TODO: End gamble because of too many errors
                    executeErrorShutdown();
                }
            }

        }
    }

    private void startGame(Plugin plugin) {
        BaseMinigameHelper baseMinigameHelper = (BaseMinigameHelper) minigameHelper;
        final int[] countdownId = {1};
        final int[] countdownTime = {baseMinigameHelper.getCountdownTime()};

        currentGame.phaseSwitched(GambleGamePhase.SELECTED);
        if (DEBUG) sendConsoleMessage("Minigame phase switched to SELECTED");
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                currentGame.phaseSwitched(GambleGamePhase.LOBBY);
                gamblePhase = GamblePhase.INGAME;
                getIngame().forEach(GamblePlayerUtils::resetPlayer);
                if (DEBUG) sendConsoleMessage("Minigame phase switched to LOBBY");
                countdownId[0] = Bukkit.getScheduler().scheduleSyncRepeatingTask(getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        if (countdownTime[0] > 0) {
                            baseMinigameHelper.getCallback().tick(countdownTime[0]);
                            countdownTime[0]--;
                        } else {
                            Bukkit.getScheduler().cancelTask(countdownId[0]);
                            if (DEBUG) sendConsoleMessage("Minigame phase switched to INGAME");
                            toggleListeners(false);
                            currentGame.phaseSwitched(GambleGamePhase.INGAME);
                        }
                    }
                }, 0, 20);
            }
        }, 60);
    }

    public void executeErrorShutdown() {
        sendConsoleMessage("Gamble wird aufgrund von Fehlern beendet!");
        getMessenger().broadcast("§cGamble wird wegen internen Fehlern leider abgebrochen. Der Server startet neu.");
        Bukkit.getScheduler().runTaskLater(((Plugin) getInstance()), new Runnable() {
            @Override
            public void run() {
                Bukkit.shutdown();
            }
        }, 60);
    }

    @Override
    public void onGameDisable() {
        for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
            if (p.getName().startsWith("gamble-")) {
                sendConsoleMessage("Unloading " + p.getName() + "!");
                MinigameUtils.unloadPlugin(p);
            }
        }
    }

    public void requestDice() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                getMessenger().broadcast("§7Jetzt wird gewürfelt!");
                Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        gamblePhase = GamblePhase.DICE;
                        dice.request();
                    }
                }, 40);
            }
        }, 40);
    }

    @Override
    public GameAPI getGameAPI() {
        return null;
    }

    @Override
    public GambleGamePhase getGamePhase() {
        return null;
    }

    @Override
    public GambleGameType getRandomGambleGameType() {
        GambleGameType type = nextGameType;
        if (type == null) {
            type = GambleGameType.values()[random.nextInt(GambleGameType.values().length)];
        }
        return type;
    }

    @Override
    public void finishGambleGame(GambleGameType type, EndReason reason, GambleGameResult... results) {
        if (reason == EndReason.EXCEPTION) {
            getMessenger().broadcast("§cDie Runde wurde aufgrund eines internen Fehlers beendet!");
            //TODO: End round with exception
        } else {
            gamblePhase = GamblePhase.MAP;
            currentGame.phaseSwitched(GambleGamePhase.END);
            minigameListeners.forEach(HandlerList::unregisterAll);
            for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                if (plugin.getName().startsWith("gamble-")) {
                    sendConsoleMessage("Unloading minigame: " + plugin.getName());
                    MinigameUtils.unloadPlugin(plugin);
                }
            }
            currentGame = null;
            lastGambleGame = type;
            resetMaps();
            toggleListeners(true);
            if (results != null) {
                gambleResults.put(currentRound, results);
            } else {
                gambleResults.put(currentRound, new GambleGameResult[]{});
            }
            getMessenger().broadcast("Das Spiel ist vorbei - so ist es ausgegangen:");
            for (GambleGameResult gameResult : getLastGameResult()) {
                if (gameResult != null && gameResult.getPlayer() != null) {
                    getMessenger().broadcast("        §6" + gameResult.getPlacement() + ". §f" + gameResult.getPlayer().getPlayer().getName());
                }
            }
            for (GamblePlayer gamblePlayer : ingame) {
                GamblePlayerUtils.resetPlayer(gamblePlayer);
                gamblePlayer.changePosition(this, gamblePlayer.getCurrentPosition());
                gamblePlayer.getCorePlayer().getScoreboard().setNewObjective(new MapObjective());
                gamblePlayer.getCorePlayer().getScoreboard().getObjective(DisplaySlot.SIDEBAR).reload();
                ((BaseGamblePlayer) gamblePlayer).giveMapItems();
            }
            requestDice();
        }
    }

    private void resetMaps() {
        for (World world : Bukkit.getWorlds()) {
            world.setThunderDuration(0);
            world.setThundering(false);
            world.setStorm(false);
            gameWorld.purgeAnimals();
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("doMobSpawning", "false");
        }
    }

    @Override
    public GambleGameResult[] getLastGameResult() {
        return getGameResult(currentRound);
    }

    @Override
    public GambleGameResult[] getGameResult(int round) {
        if (gambleResults.containsKey(round)) {
            return gambleResults.get(round);
        }
        return new GambleGameResult[]{};
    }

    @Override
    public GamblePlayer getGamblePlayer(UUID uuid) {
        for (GamblePlayer playing : this.getIngame()) {
            if (playing.getPlayer().getUniqueId().equals(uuid)) {
                return playing;
            }
        }
        return null;
    }

    @Override
    public void registerListener(GambleGame game, GambleListener listener) {
        minigameListeners.add(listener);
        Bukkit.getPluginManager().registerEvents(listener, game);
        if (DEBUG) {
            getMessenger().broadcast("§b[DEBUG] §7" + game.getName() + " registered a new listener (" + listener.getClass().getName() + ")!");
            sendConsoleMessage("§b[DEBUG] §7" + game.getName() + " registered a new listener (" + listener.getClass().getName() + ")!");
        }
    }
}

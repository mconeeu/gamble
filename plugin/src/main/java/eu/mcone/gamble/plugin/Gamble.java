/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl, Louis Bockel and the MC ONE Minecraftnetwork. All rights reserved
 *  You are not allowed to decompile the code
 */

package eu.mcone.gamble.plugin;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gamble.plugin.command.GambleMainCommand;
import eu.mcone.gamble.plugin.dice.DiceHandler;
import eu.mcone.gamble.plugin.game.GameHandler;
import eu.mcone.gamble.plugin.game.MiniGamesHandler;
import eu.mcone.gamble.plugin.listener.GeneralPlayerListener;
import eu.mcone.gamble.plugin.listener.InventoryTriggerListener;
import eu.mcone.gamble.plugin.state.EndState;
import eu.mcone.gamble.plugin.state.IngameState;
import eu.mcone.gamble.plugin.state.LobbyState;
import eu.mcone.gamble.plugin.state.PlayingState;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Gamble extends eu.mcone.gamble.api.Gamble {

    @Getter
    public static Gamble instance;

    private final Map<UUID, GamblePlayer> gamblePlayers;

    @Getter
    private CoreWorld gameWorld;
    @Getter
    private int gameFields;

    private eu.mcone.gamble.plugin.game.MiniGamesHandler miniGamesHandler;
    @Getter
    private GameHandler gameHandler;
    @Getter
    private DiceHandler diceHandler;
    @Getter
    private CoreWorld minigameWorld;

    public Gamble() {
        super();
        instance = this;
        gamblePlayers = new HashMap<>();
    }

    @Override
    public void onGameEnable() {
        gameWorld = CoreSystem.getInstance().getWorldManager().getWorld(Gamble.getInstance().getGameConfig().parseConfig().getGameWorld());

        getPlayerManager();
        getDamageLogger();
        getGameStateManager().addGameStateFirst(new LobbyState())
                .addGameState(new IngameState())
                .addGameState(new PlayingState())
                .addGameState(new EndState())
                .startGame();

        miniGamesHandler = new MiniGamesHandler();
        gameHandler = new GameHandler();
        diceHandler = new DiceHandler();
        minigameWorld = CoreSystem.getInstance().getWorldManager().getWorld("minigames");

        //GAME WORLD
        CoreSystem.getInstance().getTranslationManager().loadAdditionalCategories("gamble");

        //Count all available game fields
        gameWorld.getLocations().forEach((k, v) -> {
            if (k.startsWith("field_")) {
                gameFields++;
            }
        });

        registerEvents(
                new InventoryTriggerListener(),
                new GeneralPlayerListener()
        );

        registerCommands(
                new GambleMainCommand()
        );

        sendConsoleMessage("§aVersion §f" + this.getDescription().getVersion() + "§a enabled...");
    }

    @Override
    public void onGameDisable() {
        miniGamesHandler.getLoader().unloadGames();
    }

    @Override
    public GamblePlayer getGamblePlayer(UUID uuid) {
        return gamblePlayers.get(uuid);
    }

    public void registerGamblePlayer(Player player) {
        gamblePlayers.put(player.getUniqueId(), new eu.mcone.gamble.plugin.player.GamblePlayer(player, 0));
    }

    public void unregisterGamblePlayer(UUID uuid) {
        gamblePlayers.remove(uuid);
    }

    public Collection<GamblePlayer> getGamblePlayers() {
        return gamblePlayers.values();
    }

    @Override
    public MiniGamesHandler getMiniGamesHandler() {
        return miniGamesHandler;
    }
}

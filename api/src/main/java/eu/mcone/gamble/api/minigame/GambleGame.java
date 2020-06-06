/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.api.minigame;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class GambleGame extends CorePlugin {

    @Getter
    private static GambleGame instance;
    private final List<Listener> listeners;
    private GameHandler gameHandler;

    protected GambleGame(String pluginName, ChatColor pluginColor, String prefixTranslation) {
        super(pluginName, pluginColor, prefixTranslation);
        instance = this;
        listeners = new ArrayList<>();
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public void registerListener(Listener listener) {
        listeners.add(listener);
        registerListener(listener);
    }

    public abstract void initiate();

    public abstract void abandon();

    public void setGameHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }
}

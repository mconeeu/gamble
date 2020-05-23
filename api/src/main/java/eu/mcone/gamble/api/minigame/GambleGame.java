/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.api.minigame;

import eu.mcone.gamble.api.Gamble;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class GambleGame extends JavaPlugin  {

    @Override
    public final void onEnable() {}

    @Override
    public final void onDisable() {}

    @Override
    public final void onLoad() {}

    public abstract void initiate(Gamble gamble);

    public abstract void phaseSwitched(GambleGamePhase gamePhase);

}

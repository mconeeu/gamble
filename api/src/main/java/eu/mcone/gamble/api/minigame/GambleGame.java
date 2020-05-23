package eu.mcone.gamble.api.minigame;

import eu.mcone.gamble.api.Gamble;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Datei erstellt von: Felix Schmid in Projekt: mcone-gamble
 */
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

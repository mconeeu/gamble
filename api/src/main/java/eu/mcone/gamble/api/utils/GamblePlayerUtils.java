package eu.mcone.gamble.api.utils;

import eu.mcone.gamble.api.player.GamblePlayer;
import org.bukkit.GameMode;
import org.bukkit.potion.PotionEffect;

/**
 * Datei erstellt von: Felix Schmid in Projekt: mcone-gamble
 */
public class GamblePlayerUtils {

    public static void resetPlayer(GamblePlayer gamePlayer) {
        gamePlayer.getPlayer().setGameMode(GameMode.SURVIVAL);
        gamePlayer.getPlayer().getInventory().setHelmet(null);
        gamePlayer.getPlayer().getInventory().setChestplate(null);
        gamePlayer.getPlayer().getInventory().setLeggings(null);
        gamePlayer.getPlayer().getInventory().setBoots(null);
        gamePlayer.getPlayer().setLevel(0);
        gamePlayer.getPlayer().setExp(0);
        gamePlayer.getPlayer().setMaxHealth(20);
        gamePlayer.getPlayer().setHealth(20);
        gamePlayer.getPlayer().setFoodLevel(20);
        gamePlayer.getPlayer().getInventory().clear();
        for(PotionEffect effect : gamePlayer.getPlayer().getActivePotionEffects()) {
            gamePlayer.getPlayer().removePotionEffect(effect.getType());
        }
    }

}

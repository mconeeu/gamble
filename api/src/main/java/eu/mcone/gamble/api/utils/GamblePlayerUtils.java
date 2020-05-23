/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.api.utils;

import eu.mcone.gamble.api.player.GamblePlayer;
import org.bukkit.GameMode;
import org.bukkit.potion.PotionEffect;

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

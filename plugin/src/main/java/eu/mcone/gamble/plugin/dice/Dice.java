package eu.mcone.gamble.plugin.dice;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.util.CoreTitle;
import eu.mcone.gamble.api.minigame.GambleGameResult;
import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gamble.plugin.GamblePlugin;
import eu.mcone.gamble.plugin.player.BaseGamblePlayer;
import eu.mcone.gamble.plugin.util.RangCalculation;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Datei erstellt von: Felix Schmid in Projekt: mcone-gamble
 */

@Getter
public class Dice implements Listener {

    private Map<Integer, CoreTitle> titles;
    private Map<UUID, Integer> results, currentValue;
    private Random random;

    public Dice() {
        random = new Random();
        titles = new HashMap<Integer, CoreTitle>();
        results = new HashMap<UUID, Integer>();
        currentValue = new HashMap<UUID, Integer>();

        Bukkit.getPluginManager().registerEvents(this, GamblePlugin.getInstance());

        for (int i = 0; i < 33; i++) {
            CoreTitle title = CoreSystem.getInstance().createTitle();
            title.title("" + i);
            title.fadeIn(0);
            title.fadeOut(0);
            title.stay(3);
            titles.put(i, title);
        }
    }

    /**
     * Initiates dice phase for all players and automatically
     * stops dicing after x seconds. After a timeout of x seconds, it will
     * notify all users that a new game is selected soon and x seconds after that
     * it will start a new game round.
     */
    public void request() {
        request(new Runnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, Integer> results : getResults().entrySet()) {
                    GamblePlayer gamblePlayer = GamblePlugin.getInstance().getGamblePlayer(results.getKey());
                    GamblePlugin.getInstance().getMessenger().send(gamblePlayer.getPlayer(), "§7Du hast eine §f" + results.getValue() + " §7gewürfelt.");
                    gamblePlayer.changePosition(GamblePlugin.getInstance(), gamblePlayer.getCurrentPosition() + results.getValue());
                }

                GamblePlugin.getInstance().setPlayerRanking(RangCalculation.recalculate());

                for (GamblePlayer gp : GamblePlugin.getInstance().getIngame()) {
                    gp.getCorePlayer().getScoreboard().getObjective(DisplaySlot.SIDEBAR).reload();
                    ((BaseGamblePlayer) gp).giveMapItems();
                }

                Bukkit.getScheduler().scheduleSyncDelayedTask(GamblePlugin.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        if(!GamblePlugin.getInstance().getFinishGambleHelper().request()) {
                            GamblePlugin.getInstance().getMessenger().broadcast("Das nächste Spiel wird gezogen!");

                            Bukkit.getScheduler().scheduleSyncDelayedTask(GamblePlugin.getInstance(), new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        GamblePlugin.getInstance().initiateNextGameRound();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, 40);
                        }
                    }
                }, 50);
            }
        });
    }

    public void request(Runnable runnable) {
        results.clear();
        currentValue.clear();


        Bukkit.getScheduler().scheduleSyncDelayedTask(GamblePlugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (GamblePlayer gamblePlayer : GamblePlugin.getInstance().getIngame()) {
                    if (!results.containsKey(gamblePlayer.getPlayer().getUniqueId())) {
                        gamblePlayer.getPlayer().playSound(gamblePlayer.getPlayer().getLocation(), Sound.LEVEL_UP, 1f, 1f);
                        results.put(gamblePlayer.getPlayer().getUniqueId(), currentValue.get(gamblePlayer.getPlayer().getUniqueId()));
                        titles.get(currentValue.get(gamblePlayer.getPlayer().getUniqueId())).send(gamblePlayer.getPlayer());
                    }
                }

                runnable.run();
            }
        }, 20 * 10);

        for (GamblePlayer gamblePlayer : GamblePlugin.getInstance().getIngame()) {
            request(gamblePlayer);
        }
    }

    public void request(GamblePlayer gamblePlayer) {
        if (!results.containsKey(gamblePlayer.getPlayer().getUniqueId())) {
            int extraCS = 0;
            for (GambleGameResult gameResult : GamblePlugin.getInstance().getLastGameResult()) {
                if (gameResult.getPlayer() != null) {
                    if (gameResult.getPlayer().getPlayer().getUniqueId().equals(gamblePlayer.getPlayer().getUniqueId())) {
                        extraCS = gameResult.getExtraCubeSize();
                    }
                }
            }

            int i = 0;
            final int finalExtraCS = extraCS;
            for (int r = 0; r < 6 + extraCS; r++) {
                i++;
                Bukkit.getScheduler().scheduleAsyncDelayedTask(GamblePlugin.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        if (!results.containsKey(gamblePlayer.getPlayer().getUniqueId())) {
                            if (currentValue.containsKey(gamblePlayer.getPlayer().getUniqueId())) {
                                currentValue.remove(gamblePlayer.getPlayer().getUniqueId());
                            }
                            currentValue.put(gamblePlayer.getPlayer().getUniqueId(), random.nextInt(6 + finalExtraCS) + 1);
                            titles.get(random.nextInt(6 + finalExtraCS) + 1).send(gamblePlayer.getPlayer());
                            gamblePlayer.getPlayer().playSound(gamblePlayer.getPlayer().getLocation(), Sound.CLICK, 1f, 1f);
                        }
                    }
                }, 7 * i);
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask(GamblePlugin.getInstance(), new Runnable() {
                @Override
                public void run() {
                    request(gamblePlayer);
                }
            }, i + 3);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!results.containsKey(event.getPlayer().getUniqueId()) && GamblePlugin.getInstance().getGamePlayer(event.getPlayer()) != null) {
            if (currentValue.containsKey(event.getPlayer().getUniqueId())) {
                results.put(event.getPlayer().getUniqueId(), currentValue.get(event.getPlayer().getUniqueId()));
                titles.get(currentValue.get(event.getPlayer().getUniqueId())).send(event.getPlayer());
                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.LEVEL_UP, 1f, 1f);
            }
        }
    }

}

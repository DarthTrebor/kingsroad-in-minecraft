package org.example.Listeners.BukkitListeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;
import org.example.ConnectedPlayer;
import org.example.Leaderboard;
import org.example.Potions.ActiveEffect;
import org.example.utils.ActionBarUtils;
import org.example.utils.UserExperienceUtils;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class OnEntityDeath implements Listener
{
    private final Plugin plugin;
    private final Random randomizer;
    private final Leaderboard leaderboard;
    ConcurrentHashMap<String, ConnectedPlayer> connectedPlayers;

    public OnEntityDeath(Leaderboard leaderboard, Plugin plugin, ConcurrentHashMap<String, ConnectedPlayer> connectedPlayers)
    {
        this.plugin = plugin;
        this.leaderboard = leaderboard;
        this.connectedPlayers = connectedPlayers;
        this.randomizer = new Random();
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event)
    {
        event.getDrops().clear();
        event.setDroppedExp(0);

        if (event.getEntity().getKiller() != null)
        {
            Player entityKiller = event.getEntity().getKiller();
            int xpToAdd = randomizer.nextInt(50) + 1;

            UserExperienceUtils.addExperienceToPlayer(plugin, entityKiller, xpToAdd);

            if (connectedPlayers.get(entityKiller.getUniqueId().toString()) != null)
            {
                //: TODO to send gold only to the players in party also check if they are online
                Bukkit.getOnlinePlayers().forEach(player -> {
                    AtomicInteger coinValue = new AtomicInteger(randomizer.nextInt(100));
                    List<ActiveEffect> effects = connectedPlayers.get(event.getEntity().getKiller().getUniqueId().toString()).effects();
                    effects.forEach(effect -> {
                        if (Objects.equals(effect.getPotionType(), "gold_luck")) {
                            coinValue.updateAndGet(value -> value + (int) (value * 0.45));
                        }
                    });
                    ActionBarUtils.sendAndClearActionBar(plugin, player, ChatColor.GREEN + "You have picked up " + ChatColor.GOLD + coinValue.intValue() + " gold!", 40);
                    leaderboard.addCoins(player, coinValue.intValue());
                });
            }
        }
    }
}

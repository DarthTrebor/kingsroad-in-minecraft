package org.example.Listeners.BukkitListeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.utils.EntityUtils;
import java.util.Objects;

public class OnCreatureSpawn implements Listener
{
    private final Plugin plugin;

    public OnCreatureSpawn(Plugin plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event)
    {
        if (event.getEntity() instanceof Horse horse)
        {
            if ((ChatColor.GOLD + "Decoy").equals(horse.getCustomName())) {

                horse.setCustomName(ChatColor.RED + "Decoy");
                horse.setCustomNameVisible(true);
                Objects.requireNonNull(horse.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(100.0);
                horse.setHealth(100.0);
                horse.setAdult();
                horse.setPersistent(true);
                horse.setAI(false);

                manageTargets(horse);

                Bukkit.broadcastMessage(ChatColor.GREEN + "Decoy casted!");
            }
        }
        else
        {
            event.getEntity();
            EntityUtils.updateEntityName(event.getEntity(), 0.0);
        }
    }

    private void manageTargets(Horse horse)
    {
        new BukkitRunnable()
        {
            int ticksElapsed = 0;
            final int totalDuration = 200;

            @Override
            public void run()
            {
                if (horse.isDead() || !horse.isValid())
                {
                    this.cancel();
                    return;
                }

                int secondsRemaining = (totalDuration - ticksElapsed) / 20;

                if (ticksElapsed % 20 == 0)
                {
                    horse.setCustomName(ChatColor.RED + "Decoy " + ChatColor.GREEN + secondsRemaining + "s");
                    horse.setCustomNameVisible(true);
                }

                if (ticksElapsed >= totalDuration)
                {
                    horse.remove();
                    Bukkit.broadcastMessage(ChatColor.RED + "Your decoy has disappeared!");
                    this.cancel();
                    return;
                }

                ticksElapsed += 10;

                for (Entity entity : horse.getWorld().getNearbyEntities(horse.getLocation(), 10, 10, 10))
                {
                    if (entity instanceof Monster monster)
                    {
                        Player nearestPlayer = getNearestPlayer(monster);

                        if (nearestPlayer != null)
                        {
                            double distanceToHorse = monster.getLocation().distance(horse.getLocation());
                            double distanceToPlayer = monster.getLocation().distance(nearestPlayer.getLocation());

                            if (distanceToHorse < distanceToPlayer)
                            {
                                monster.setTarget(horse);
                            }
                            else
                            {
                                monster.setTarget(nearestPlayer);
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 10L); // Runs every 10 ticks (0.5 seconds)
    }

    private Player getNearestPlayer(LivingEntity entity)
    {
        Player nearestPlayer = null;
        double nearestDistance = Double.MAX_VALUE;

        for (Player player : entity.getWorld().getPlayers()) {
            double distance = entity.getLocation().distanceSquared(player.getLocation());
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestPlayer = player;
            }
        }

        return nearestPlayer;
    }
}

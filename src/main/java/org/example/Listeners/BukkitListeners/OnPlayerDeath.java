package org.example.Listeners.BukkitListeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class OnPlayerDeath implements Listener
{
    private final Plugin plugin;

    public OnPlayerDeath(Plugin plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        {
            // don't lose any xp
            event.setKeepLevel(true);
            event.setDroppedExp(0);
        }

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                // respawn user without clicking respawn button
                event.getEntity().spigot().respawn();
            }
        }.runTaskLater(plugin, 1L); // Delay of 1 tick
    }
}

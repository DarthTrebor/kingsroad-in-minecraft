package org.example.Listeners.BukkitListeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class OnEntityTarget implements Listener
{
    private final Plugin plugin;

    public OnEntityTarget(Plugin plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityTarget(org.bukkit.event.entity.EntityTargetEvent event)
    {
        if (event.getTarget() instanceof Horse horse)
        {
            if ((ChatColor.GOLD + "Decoy Horse Spawn Egg").equals(horse.getCustomName()))
            {
                event.setTarget(horse);
            }
        }
    }
}

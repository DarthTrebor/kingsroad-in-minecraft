package org.example.Potions;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ActiveEffect
{
    String potionType;
    String potionSize;
    Integer time;
    Player player;
    BukkitRunnable runnableEffect;

    public ActiveEffect(Plugin plugin, Player player, String potionType, String potionSize, Integer time)
    {
        this.player = player;
        this.potionSize = potionSize;
        this.potionType = potionType;
        this.time = time;
        this.runnableEffect = new BukkitRunnable() {
            @Override
            public void run() {
                if (getTime() > 0)
                {
                    setTime(getTime() - 1);
                }
                else
                {
                    this.cancel();
                }
            }
        };
        this.runnableEffect.runTaskTimer(plugin, 0L, 20L);
    }

    public void setTime(Integer time)
    {
        this.time = time;
    }

    public Integer getTime()
    {
        return time;
    }

    public String getPotionSize()
    {
        return potionSize;
    }

    public String getPotionType()
    {
        return potionType;
    }
}

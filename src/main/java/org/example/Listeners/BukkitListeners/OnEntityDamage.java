package org.example.Listeners.BukkitListeners;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.utils.EntityUtils;
import java.util.Random;

public class OnEntityDamage implements Listener
{
    private final Plugin plugin;
    private final Random randomizer = new Random();

    public OnEntityDamage(Plugin plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onEntityDamage(EntityDamageByEntityEvent event)
    {
        event.getDamager().sendMessage(ChatColor.GOLD + "Decoy Damage");
        if (event.getEntity().getCustomName() != null)
        {
            if (event.getEntity().getCustomName().contains(ChatColor.RED + "Decoy"))
            {
                if (event.getDamager() instanceof Player) {
                    event.setCancelled(true);
                }
                else
                {
                    if (event.getDamager() instanceof Arrow arrow)
                    {
                        PersistentDataContainer dataContainer = arrow.getPersistentDataContainer();

                        NamespacedKey key = new NamespacedKey(plugin, "hit_by_player");
                        if (dataContainer.has(key, PersistentDataType.STRING))
                        {
                            arrow.remove();
                            event.setCancelled(true);
                        }
                    }
                    event.setDamage(0.01);
                }
            }
            else
            {
                if (event.getEntity() instanceof LivingEntity livingEntity)
                {
                    double damage = event.getFinalDamage();

                    displayDamageDealt(livingEntity, damage);

                    EntityUtils.updateEntityName(livingEntity, damage);
                }
            }
        }
    }

    void displayDamageDealt(LivingEntity entity, double damage)
    {
        double offsetX = (randomizer.nextInt(21) - 10) / 100.0;
        double offsetY = (randomizer.nextInt(21) - 10) / 100.0;
        double offsetZ = (randomizer.nextInt(21) - 10) / 100.0;

        ArmorStand damageIndicator = entity.getWorld().spawn(entity.getEyeLocation().add(offsetX, offsetY, offsetZ), ArmorStand.class);
        damageIndicator.setInvisible(true);
        damageIndicator.setCustomName(ChatColor.RED + "⚔ " + ChatColor.WHITE + String.format("%.1f", damage));
        damageIndicator.setCustomNameVisible(true);
        damageIndicator.setGravity(false);
        damageIndicator.setMarker(true);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                damageIndicator.remove();
            }
        }.runTaskLater(plugin, 40); // disappears after 2 seconds
    }
}

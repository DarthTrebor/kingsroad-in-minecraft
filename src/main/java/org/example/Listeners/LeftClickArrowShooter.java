package org.example.Listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.example.Skills.Piercing;

/**
 * Shoots an arrow with left click based on the attack speed of the player.
 */
public class LeftClickArrowShooter implements Listener
{
    private final Plugin plugin;

    public LeftClickArrowShooter(Plugin plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLeftClick(PlayerInteractEvent event)
    {
        if (event.getAction().toString().contains("LEFT_CLICK") && event.getHand() == EquipmentSlot.HAND)
        {
            Player player = event.getPlayer();

            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() == Material.BOW)
            {
                shootArrow(player);
            }
        }
    }

    private void shootArrow(Player player)
    {
        Location location = player.getEyeLocation();
        Vector direction = location.getDirection();

        Arrow arrow = player.getWorld().spawn(location, Arrow.class);
        arrow.setVelocity(direction.multiply(2));
        arrow.setShooter(player);

        double maxDamage = 14.5;
        arrow.setDamage(maxDamage);
        arrow.setCritical(true);

        NamespacedKey key = new NamespacedKey(plugin, "hit_by_player");
        arrow.getPersistentDataContainer().set(key, PersistentDataType.STRING, "true");

        new Piercing().arrowPierce(arrow, player);
    }
}

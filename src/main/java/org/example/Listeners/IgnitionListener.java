package org.example.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.example.CustomEnchants.IgnitionEnchantment;

import java.util.Objects;
import java.util.Random;

public class IgnitionListener implements Listener
{

    private final IgnitionEnchantment ignitionEnchant = new IgnitionEnchantment(100);
    private final Random random = new Random();

    private boolean hasFullNetheriteArmor(Player player)
    {
        ItemStack[] armor = player.getInventory().getArmorContents();
        for (ItemStack piece : armor) {
            if (piece == null || piece.getType() == Material.AIR || !piece.getType().name().contains("NETHERITE")) {
                return false;
            }
        }
        return true;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
    {
        if (event.getDamager() instanceof Player player)
        {
            if (hasFullNetheriteArmor(player))
            {
                if (random.nextInt(ignitionEnchant.getActivationChance()) == 0)
                {
                    Entity victim = event.getEntity();
                    if (victim instanceof LivingEntity livingVictim && !Objects.requireNonNull(livingVictim.getCustomName()).contains(ChatColor.RED + "Decoy"))
                    {
                        livingVictim.setFireTicks(ignitionEnchant.getDuration());
                        player.sendMessage(ChatColor.RED + "Ignition activated! Your attack set the enemy on fire!");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event)
    {
        if (event.getEntity() instanceof Player player)
        {
            ItemStack bow = event.getBow();
            if (bow != null && hasFullNetheriteArmor(player))
            {
                if (random.nextInt(10) == 0) {
                    if (event.getProjectile() instanceof Arrow arrow)
                    {
                        arrow.setFireTicks(100); // Ignite arrow for 5 seconds
                        player.sendMessage(ChatColor.RED + "Ignition activated! Your arrow will ignite the target!");
                    }
                }
            }
        }
    }
}

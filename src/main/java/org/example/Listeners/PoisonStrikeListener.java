package org.example.Listeners;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.CustomEnchants.PoisonStrikeEnchantment;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PoisonStrikeListener implements Listener
{

    private final Enchantment poisonStrikeEnchant = new PoisonStrikeEnchantment();
    private final Plugin plugin;

    public PoisonStrikeListener(Plugin plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            if (item.getType().name().endsWith("_SWORD") || item.getType() == Material.BOW)
            {
                ItemMeta meta = item.getItemMeta();
                if (meta != null)
                {
                    meta.addEnchant(poisonStrikeEnchant, 1, true);

                    List<String> lore = new ArrayList<>();
                    lore.add(ChatColor.GREEN + "Poison Strike: Applies poison to enemies.");
                    meta.setLore(lore);

                    item.setItemMeta(meta);

                    player.sendMessage(ChatColor.GREEN + "Your sword has been enchanted with Poison Strike!");
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
    {
        if (event.getDamager() instanceof Player player)
        {
            if (isWearingFullLeatherArmor(player))
            {
                Entity victim = event.getEntity();

                if (victim instanceof LivingEntity livingVictim)
                {
                    applyCustomPoison(livingVictim, 100, 2.0);
                }
            }
        }
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event)
    {
        if (event.getEntity() instanceof Arrow arrow)
        {
            if (arrow.getShooter() instanceof Player player)
            {
                if (isWearingFullLeatherArmor(player))
                {
                    Entity hitEntity = event.getHitEntity();
                    if (hitEntity != null)
                    {
                        if (hitEntity instanceof LivingEntity livingVictim && !Objects.requireNonNull(livingVictim.getCustomName()).contains(ChatColor.RED + "Decoy"))
                        {
                            applyCustomPoison(livingVictim, 100, 2.0);
                        }
                    }
                }
            }
        }
    }

    public void applyCustomPoison(LivingEntity entity, int durationTicks, double damagePercentPerTick)
    {
        double damage = entity.getHealth() * (damagePercentPerTick / 100.0);

        new BukkitRunnable() {
            int ticksElapsed = 0;

            @Override
            public void run()
            {
                if (entity.isDead() || ticksElapsed >= durationTicks)
                {
                    this.cancel();
                    return;
                }

                entity.damage(damage);
                ticksElapsed += 20;
            }
        }.runTaskTimer(this.plugin, 0, 20);
    }

    private boolean isWearingFullLeatherArmor(Player player)
    {
        ItemStack[] armorContents = player.getInventory().getArmorContents();

        for (ItemStack armorPiece : armorContents)
        {
            if (armorPiece == null || !armorPiece.getType().name().contains("LEATHER"))
            {
                return false;
            }
        }
        return true;
    }
}

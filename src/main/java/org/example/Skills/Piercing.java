package org.example.Skills;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.example.Constants;

import java.util.HashSet;
import java.util.Objects;

/**
 * The projectile pierces through multiple entities on a distance of {@code 10} blocks.
 * If the projectile hits a block in the process, the piercing {@code stops}.
 */
public class Piercing implements HeroSkill
{
    private final Integer pierceDistance = Constants.Numbers.TEN; // no more than 10 blocks
    private final Integer radius = Constants.Numbers.ZERO; // only hit the current angle

    public Piercing()
    {
        // empty constructor
    }

    @Override
    public String getName()
    {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getHeroClass()
    {
        return "Archer";
    }

    public void arrowPierce(Arrow arrow, Player player)
    {
        HashSet<Entity> hitEntities = new HashSet<>();
        Vector direction = arrow.getVelocity().normalize();
        Location startLocation = arrow.getLocation();

        for (int i = 1; i <= pierceDistance; i++) {
            Location currentPoint = startLocation.clone().add(direction.clone().multiply(i));

            Material blockType = currentPoint.getBlock().getType();

            if (blockType != Material.AIR && blockType.isSolid())
            {
                double arrowY = currentPoint.getY();
                double blockY = currentPoint.getBlock().getY();

                if (blockType.name().contains("SLAB"))
                {
                    if (arrowY <= blockY + 0.5)
                    {
                        break; // if hit a slab stop piercing
                    }
                }
                else
                {
                    break; // if hit a block stop piercing
                }
            }

            for (Entity entity : Objects.requireNonNull(currentPoint.getWorld()).getNearbyEntities(currentPoint, radius, radius, radius))
            {
                if (entity instanceof LivingEntity livingEntity && !hitEntities.contains(entity) && entity != player)
                {
                    livingEntity.damage(arrow.getDamage(), player);
                    hitEntities.add(entity);
                }
            }
        }


    }
}

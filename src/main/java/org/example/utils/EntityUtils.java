package org.example.utils;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import java.util.Objects;

public class EntityUtils
{
    public static void updateEntityName(LivingEntity entity, Double damage)
    {
        String entityType = entity.getType().name().toLowerCase().replace("_", " ");
        double maxHealth = Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();
        double currentHealth = entity.getHealth();
        entity.setCustomName(ChatColor.GRAY + capitalize(entityType) + ChatColor.GREEN + " (" + (int) (currentHealth - damage < 0? 0 : currentHealth - damage) + "/" + (int) maxHealth + ")");
        entity.setCustomNameVisible(true);
    }

    private static String capitalize(String text)
    {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}

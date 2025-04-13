package org.example;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Collections;

public class EventToken {

    private static final String entityName = "Event Token";
    private static final ChatColor entityTitleColor = ChatColor.BLUE;
    private static final ChatColor entityDescriptionColor = ChatColor.GRAY;

    public static String buildEntityName()
    {
        return entityTitleColor + entityName;
    }

    public static String buildEntityDescription()
    {
        return entityDescriptionColor + "This item is used to participate in the events.";
    }

    public static ItemStack buildToken()
    {
        ItemStack token = new ItemStack(Material.PAPER, 1);

        ItemMeta meta = token.getItemMeta();
        if (meta != null)
        {
            meta.setDisplayName(buildEntityName());
            meta.setLore(Collections.singletonList(buildEntityDescription()));
            token.setItemMeta(meta);
        }

        return token;
    }
}
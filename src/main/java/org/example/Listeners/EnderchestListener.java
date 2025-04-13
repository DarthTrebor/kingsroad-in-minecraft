package org.example.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.EventToken;

public class EnderchestListener implements Listener
{
    @EventHandler
    public void onEnderChestClick(PlayerInteractEvent event)
    {
        if (event.getClickedBlock() == null || event.getClickedBlock().getType() != Material.ENDER_CHEST)
        {
            return;
        }

        Inventory inventory = event.getPlayer().getInventory();
        ItemStack[] items = inventory.getContents();

        boolean hasToken = false;

        for (int i = 0; i < items.length; i++)
        {
            ItemStack item = items[i];

            if (item != null && item.getType() == Material.PAPER && item.hasItemMeta())
            {
                ItemMeta meta = item.getItemMeta();

                if (meta != null && meta.getDisplayName().equals(EventToken.buildEntityName()))
                {
                    hasToken = true;

                    if (item.getAmount() > 1) {
                        item.setAmount(item.getAmount() - 1);
                    } else {
                        inventory.setItem(i, null); // Remove the item if it's the last one
                    }

                    event.getPlayer().sendMessage(ChatColor.GREEN + "You have used an Event Token!");
                    break;
                }
            }
        }

        if (hasToken)
        {
            event.setCancelled(true);
        } else
        {
            event.getPlayer().sendMessage(ChatColor.RED + "You need an Event Token to use this!");
            event.setCancelled(true);
        }
    }
}

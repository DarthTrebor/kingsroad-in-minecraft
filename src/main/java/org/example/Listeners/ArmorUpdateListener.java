package org.example.Listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import java.util.Objects;

public class ArmorUpdateListener implements Listener
{

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        int tempestCount = 0;

        // Count Netherite armor pieces
        ItemStack[] armorContents = player.getInventory().getArmorContents();
        for (ItemStack piece : armorContents)
        {
            if (isTempestbringerItem(piece))
            {
                tempestCount++;
            }
        }

        // Count Tempestbringer Shield in off-hand
        ItemStack offHandItem = player.getInventory().getItem(EquipmentSlot.OFF_HAND);
        if (isTempestbringerItem(offHandItem) && offHandItem.getType() == Material.SHIELD)
        {
            tempestCount++;
        }

        // Count Tempestbringer Sword in main-hand or inventory
        boolean swordFound = false;
        for (ItemStack item : player.getInventory().getContents())
        {
            if (isTempestbringerItem(item) && item.getType() == Material.NETHERITE_SWORD)
            {
                tempestCount++;
                swordFound = true;
                break; // Only count one sword
            }
        }

        // Check if dual-wielding a Tempestbringer Sword
        if (swordFound && isTempestbringerItem(offHandItem) && offHandItem.getType() == Material.NETHERITE_SWORD)
        {
            tempestCount++;
        }

        // Update lore for all Tempestbringer items
        for (ItemStack item : armorContents)
        {
            if (isTempestbringerItem(item))
            {
                KRDCommandListener.updateItemDescription(player, item, tempestCount);
            }
        }

        // Update lore for shield and swords
        if (isTempestbringerItem(offHandItem))
        {
            KRDCommandListener.updateItemDescription(player, offHandItem, tempestCount);
        }
        for (ItemStack item : player.getInventory().getContents())
        {
            if (isTempestbringerItem(item) && (item.getType() == Material.NETHERITE_SWORD || item.getType() == Material.SHIELD))
            {
                KRDCommandListener.updateItemDescription(player, item, tempestCount);
            }
        }
    }

    private boolean isTempestbringerItem(ItemStack item)
    {
        if (item == null || !item.hasItemMeta()) return false;
        String displayName = Objects.requireNonNull(item.getItemMeta()).getDisplayName();
        return displayName.contains("Tempestbringer");
    }
}

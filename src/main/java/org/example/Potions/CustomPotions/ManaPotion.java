package org.example.Potions.CustomPotions;

import static org.example.Constants.fromBase64;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.example.Constants;
import org.example.Potions.KRDPotion;
import org.example.Potions.KRDPotionType;
import java.util.List;

public class ManaPotion implements KRDPotion
{
    private final KRDPotionType type;
    private final Plugin plugin;

    public ManaPotion(KRDPotionType type, Plugin plugin)
    {
        this.type = type;
        this.plugin = plugin;
    }

    public ItemStack createPotion()
    {
        ItemStack potion = fromBase64(Constants.CLEAN_TAGS_POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(getNameWithColor());

            meta.setColor(Color.fromRGB(5, 184, 255));

            String durationText = getDurationStr();
            meta.setLore(List.of(
                    ChatColor.GRAY + "Mana Regen " + ChatColor.BLUE + "(" + durationText + ")",
                    ChatColor.GRAY + "Regenerates " + ChatColor.BLUE + getBoostPercent() + "%" + ChatColor.GRAY + " of mana per second."
            ));

            NamespacedKey potionTypeKey = new NamespacedKey(plugin, "potionType");
            meta.getPersistentDataContainer().set(potionTypeKey, PersistentDataType.STRING, "mana_potion");

            NamespacedKey potionSizeKey = new NamespacedKey(plugin, "potionSize");
            meta.getPersistentDataContainer().set(potionSizeKey, PersistentDataType.STRING, type.toString());

            potion.setItemMeta(meta);
        }

        return potion;
    }

    @Override
    public String getIdentifier()
    {
        return type + "_mana_potion";
    }

    @Override
    public int getBoostPercent()
    {
        return type == KRDPotionType.NORMAL ? 30 : 45;
    }

    @Override
    public String getDurationStr()
    {
        return type == KRDPotionType.NORMAL ? "15:00" : "30:00";
    }

    @Override
    public Integer gerDurationInt()
    {
        return type == KRDPotionType.NORMAL ? 15 : 30;
    }

    @Override
    public ChatColor getColorBukkit()
    {
        return ChatColor.BLUE;
    }

    @Override
    public String getColorPrefix()
    {
        return "&d";
    }

    @Override
    public String getName()
    {
        return (type == KRDPotionType.NORMAL ? "" : "Grand ") + "Potion of Mana";
    }

    @Override
    public String getNameWithColor()
    {
        return getColorBukkit() + getName();
    }
}

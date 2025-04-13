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

public class LuckPotion implements KRDPotion
{

    private final KRDPotionType type;
    private final Plugin plugin;

    public LuckPotion(KRDPotionType type, Plugin plugin)
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


            meta.setColor(Color.fromRGB(0, 255, 0));

            String durationText = (type == KRDPotionType.NORMAL) ? "15:00" : "30:00";
            meta.setLore(List.of(
                    ChatColor.GRAY + "Gold Luck " + ChatColor.BLUE + "(" + durationText + ")",
                    ChatColor.GRAY + "Increases gold find by " + ChatColor.BLUE + getBoostPercent() + "%" + ChatColor.GRAY + " when kills mobs."
            ));

            NamespacedKey potionTypeKey = new NamespacedKey(plugin, "potionType");
            meta.getPersistentDataContainer().set(potionTypeKey, PersistentDataType.STRING, "gold_luck_potion");

            NamespacedKey potionSizeKey = new NamespacedKey(plugin, "potionSize");
            meta.getPersistentDataContainer().set(potionSizeKey, PersistentDataType.STRING, type.toString());

            potion.setItemMeta(meta);
        }

        return potion;
    }

    @Override
    public String getIdentifier()
    {
        return type + "_gold_luck_potion";
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
        return ChatColor.GREEN;
    }

    @Override
    public String getColorPrefix()
    {
        return "&a";
    }

    @Override
    public String getName()
    {
        return (type == KRDPotionType.NORMAL? "": "Grand ") + "Potion of Gold Find";
    }

    @Override
    public String getNameWithColor()
    {
        return getColorBukkit() + getName();
    }
}

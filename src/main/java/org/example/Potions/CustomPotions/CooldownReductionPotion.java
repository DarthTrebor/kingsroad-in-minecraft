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

public class CooldownReductionPotion implements KRDPotion
{
    private final KRDPotionType type;
    private final Plugin plugin;

    public CooldownReductionPotion(KRDPotionType type, Plugin plugin)
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
            meta.setColor(Color.fromRGB(4, 29, 89));

            String durationText = getDurationStr();
            meta.setLore(List.of(
                    ChatColor.GRAY + "Cooldown Reduction " + ChatColor.BLUE + "(" + durationText + ")",
                    ChatColor.GRAY + "Reduces abilities cooldown by " + ChatColor.BLUE + getBoostPercent() + "%" + ChatColor.GRAY + '.'
            ));

            NamespacedKey potionTypeKey = new NamespacedKey(plugin, "potionType");
            meta.getPersistentDataContainer().set(potionTypeKey, PersistentDataType.STRING, "cooldown_reduction_potion");

            NamespacedKey potionSizeKey = new NamespacedKey(plugin, "potionSize");
            meta.getPersistentDataContainer().set(potionSizeKey, PersistentDataType.STRING, type.toString());
            potion.setItemMeta(meta);
        }

        return potion;
    }

    @Override
    public String getIdentifier()
    {
        return type + "_cooldown_reduction_potion";
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
        return ChatColor.DARK_BLUE;
    }

    @Override
    public String getColorPrefix()
    {
        return "#1";
    }

    @Override
    public String getName()
    {
        return (type == KRDPotionType.NORMAL? "" : "Grand ") + "Potion of Cooldown Reduction";
    }

    @Override
    public String getNameWithColor()
    {
        return getColorBukkit() + getName();
    }
}

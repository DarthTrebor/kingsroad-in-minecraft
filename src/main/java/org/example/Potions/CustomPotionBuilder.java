package org.example.Potions;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class CustomPotionBuilder
{
    private final ItemStack potion;
    private final PotionMeta meta;

    private PotionType basePotionType = PotionType.AWKWARD;
    private String displayName;
    private List<String> lore = new ArrayList<>();
    private int customModelData = -1;

    public CustomPotionBuilder()
    {
        this.potion = new ItemStack(Material.POTION);
        this.meta = (PotionMeta) potion.getItemMeta();
    }

    public CustomPotionBuilder setBasePotionType(PotionType type)
    {
        this.basePotionType = type;
        return this;
    }

    public CustomPotionBuilder setDisplayName(ChatColor color, String name)
    {
        this.displayName = color + name;
        return this;
    }

    public CustomPotionBuilder setLore(List<String> lore)
    {
        this.lore = lore;
        return this;
    }

    public CustomPotionBuilder setCustomModelData(int modelData)
    {
        this.customModelData = modelData;
        return this;
    }

    public ItemStack build()
    {
        meta.setBasePotionData(new PotionData(basePotionType));
        meta.setDisplayName(displayName);
        meta.setLore(lore);

        if (customModelData != -1)
        {
            meta.setCustomModelData(customModelData);
        }

        potion.setItemMeta(meta);

        return potion;
    }
}

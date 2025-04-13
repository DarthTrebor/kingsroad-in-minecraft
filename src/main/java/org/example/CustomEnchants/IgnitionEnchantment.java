package org.example.CustomEnchants;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class IgnitionEnchantment extends Enchantment
{
    private final Integer duration;
    private final Integer activationChance = 5;

    public IgnitionEnchantment(Integer duration)
    {
        super(NamespacedKey.minecraft("ignition"));
        this.duration = duration;
    }

    @NotNull
    @Override
    public String getName()
    {
        return "Ignition";
    }

    @Override
    public int getMaxLevel()
    {
        return 1;
    }

    @Override
    public int getStartLevel()
    {
        return 1;
    }

    @Override
    public @NotNull EnchantmentTarget getItemTarget()
    {
        return EnchantmentTarget.WEAPON;
    }

    @Override
    public boolean isTreasure()
    {
        return false;
    }

    @Override
    public boolean isCursed()
    {
        return false;
    }

    @Override
    public boolean conflictsWith(@NotNull Enchantment other)
    {
        return false; // No conflicts with other enchantments
    }

    @Override
    public boolean canEnchantItem(@NotNull ItemStack item)
    {
        String typeName = item.getType().name();
        return typeName.endsWith("_SWORD") || typeName.endsWith("_BOW");
    }

    public Integer getDuration()
    {
        return duration;
    }

    public Integer getActivationChance()
    {
        return activationChance;
    }
}

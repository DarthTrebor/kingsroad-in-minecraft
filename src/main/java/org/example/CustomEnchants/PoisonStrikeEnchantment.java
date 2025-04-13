package org.example.CustomEnchants;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PoisonStrikeEnchantment extends Enchantment {

    public PoisonStrikeEnchantment() {
        super(NamespacedKey.minecraft("poison_strike"));
    }

    @NotNull
    @Override
    public String getName() {
        return "Poison Strike";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public @NotNull EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.WEAPON;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(@NotNull Enchantment other) {
        return false; // No conflicts with other enchantments
    }

    @Override
    public boolean canEnchantItem(@NotNull ItemStack item) {
        return item.getType().name().endsWith("_SWORD");
    }
}

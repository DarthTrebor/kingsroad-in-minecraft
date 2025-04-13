package org.example.Potions;

import org.bukkit.ChatColor;

public interface KRDPotion
{
    String getIdentifier();

    int getBoostPercent();

    String getDurationStr();

    Integer gerDurationInt();

    ChatColor getColorBukkit();

    String getColorPrefix();

    String getName();

    String getNameWithColor();
}

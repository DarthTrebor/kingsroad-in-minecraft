package org.example.CustomPlaceholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.example.ConnectedPlayer;
import org.example.Potions.ActiveEffect;
import org.example.Potions.KRDPotion;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ActiveEffects extends PlaceholderExpansion
{
    private final ConcurrentHashMap<String, ConnectedPlayer> connectedPlayers;
    private final HashMap<String, KRDPotion> existingPotions;

    public ActiveEffects(ConcurrentHashMap<String, ConnectedPlayer> connectedPlayers, HashMap<String, KRDPotion> existingPotions)
    {
        this.connectedPlayers = connectedPlayers;
        this.existingPotions = existingPotions;
    }

    @Override
    public @NotNull String getIdentifier()
    {
        return "activeeffects";
    }

    @Override
    public @NotNull String getAuthor()
    {
        return "DanielKRD";
    }

    @Override
    public @NotNull String getVersion()
    {
        return "1.0.0";
    }


    @Override
    public boolean canRegister()
    {
        return true;
    }

    @Override
    public boolean persist()
    {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params)
    {
        if (player == null)
        {
            return "";
        }
        return getActiveEffects(player);
    }

    private String getActiveEffects(Player player)
    {
        StringBuilder effects = new StringBuilder("Your active potions:");

        List<ActiveEffect> potionEffects = connectedPlayers.get(player.getUniqueId().toString()).effects();
        potionEffects.forEach(effect -> {
            String potionName = existingPotions.get(effect.getPotionSize() + "_" + effect.getPotionType()).getNameWithColor();
            int timeInSeconds = effect.getTime();
            String formattedTime = formatTime(timeInSeconds);
            effects.append(String.format("\n&f•  %s (%s)", potionName, formattedTime));
        });

        return effects.toString();
    }

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }
}

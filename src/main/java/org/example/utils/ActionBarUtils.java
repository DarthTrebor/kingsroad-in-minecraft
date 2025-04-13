package org.example.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ActionBarUtils
{
    public static void sendActionBar(Player player, String message)
    {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    public static void sendAndClearActionBar(Plugin plugin, Player player, String message, int durationTicks)
    {
        sendActionBar(player, message);

        new BukkitRunnable() {
            @Override
            public void run() {
                sendActionBar(player, "");
            }
        }.runTaskLater(plugin, durationTicks);
    }
}

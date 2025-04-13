package org.example.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.example.ConnectedPlayer;
import org.example.Potions.ActiveEffect;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PotionDrinkListener implements Listener
{

    private final ConcurrentHashMap<String, ConnectedPlayer> connectedPlayers;
    private final Plugin plugin;

    public PotionDrinkListener(Plugin plugin, ConcurrentHashMap<String, ConnectedPlayer> connectedPlayers)
    {
        this.connectedPlayers = connectedPlayers;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPotionDrink(PlayerItemConsumeEvent event)
    {
        ItemStack consumedItem = event.getItem();

        if (consumedItem.getType() == Material.POTION || consumedItem.getType() == Material.SPLASH_POTION || consumedItem.getType() == Material.LINGERING_POTION)
        {
            if (consumedItem.getItemMeta() instanceof PotionMeta potionMeta)
            {
                Map<String, String> potionMessages = new HashMap<>();
                potionMessages.put("gold_luck_potion", ChatColor.GREEN + "Grand Potion of Gold Luck");
                potionMessages.put("avoidance_potion", ChatColor.DARK_GRAY + "Grand Potion of Avoidance");
                potionMessages.put("strength_potion", ChatColor.RED + "Grand Potion of Strength");
                potionMessages.put("experience_potion", ChatColor.YELLOW + "Grand Potion of Experience");
                potionMessages.put("mana_potion", ChatColor.BLUE + "Grand Potion of Mana");
                potionMessages.put("cooldown_reduction_potion", ChatColor.DARK_BLUE + "Grand Potion of Cooldown Reduction");

                if (potionMeta.getPersistentDataContainer().has(new NamespacedKey(plugin, "potionType"), PersistentDataType.STRING))
                {
                    Player player = event.getPlayer();
                    String userId = player.getUniqueId().toString();
                    String potionType = potionMeta.getPersistentDataContainer().get(new NamespacedKey(plugin, "potionType"), PersistentDataType.STRING);
                    String potionSize = potionMeta.getPersistentDataContainer().get(new NamespacedKey(plugin, "potionSize"), PersistentDataType.STRING);

                    if (potionMessages.containsKey(potionType))
                    {
                        int timeInSeconds = 30 * 60; // Example: 30 minutes in seconds
                        String randomId = UUID.randomUUID().toString();

                        try (Connection connection = DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/krd_database", "root", "passwd!"
                        )) {
                            String query = "SELECT COUNT(*) FROM users_custom_potions WHERE user_id = ? AND potion_type = ?";
                            try (PreparedStatement checkStmt = connection.prepareStatement(query))
                            {
                                checkStmt.setString(1, userId);
                                checkStmt.setString(2, potionType);

                                ResultSet rs = checkStmt.executeQuery();
                                if (rs.next() && rs.getInt(1) > 0)
                                {
                                    player.sendMessage(ChatColor.RED + "You already have a " + consumedItem.getItemMeta().getDisplayName().replace("Grand ", "") + ChatColor.RED + " active.");
                                    event.setCancelled(true);
                                    return;
                                }
                            }

                            String insertQuery = "INSERT INTO users_custom_potions (id, user_id, potion_type, potion_size, time) VALUES (?, ?, ?, ?, ?)";
                            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery))
                            {
                                insertStmt.setString(1, randomId);
                                insertStmt.setString(2, userId);
                                insertStmt.setString(3, potionType);
                                insertStmt.setString(4, potionSize);
                                insertStmt.setInt(5, timeInSeconds);

                                insertStmt.executeUpdate();
                            }

                            player.sendMessage(ChatColor.GREEN + "You have consumed a " + potionMessages.get(potionType) + ChatColor.GREEN + "! Press" + ChatColor.GRAY + " TAB " + ChatColor.GREEN + "to view all your active effects.");
                            connectedPlayers.get(player.getUniqueId().toString()).effects().add(new ActiveEffect(plugin, player, potionType, potionSize, timeInSeconds));
                        }
                        catch (SQLException e)
                        {
                            e.printStackTrace();
                            player.sendMessage(ChatColor.RED + "An error occurred while processing your potion. Please try again later.");
                        }
                    }
                }
            }
        }
    }
}

package org.example.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.example.ConnectedPlayer;
import org.example.EventToken;
import org.example.Leaderboard;
import org.example.Potions.ActiveEffect;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerJoinListener implements Listener
{
    private final ConcurrentHashMap<String, ConnectedPlayer> connectedPlayers;
    private final ConcurrentHashMap<String, Scoreboard> playerScoreboards;
    private final Plugin plugin;
    private final Leaderboard leaderboard;

    public PlayerJoinListener(Leaderboard leaderboard, Plugin plugin, ConcurrentHashMap<String, Scoreboard> playerScoreboard, ConcurrentHashMap<String, ConnectedPlayer> connectedPlayers)
    {
        this.leaderboard = leaderboard;
        this.plugin = plugin;
        this.playerScoreboards = playerScoreboard;
        this.connectedPlayers = connectedPlayers;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        ItemStack token = EventToken.buildToken();
        token.setAmount(3);

        String uuid = event.getPlayer().getUniqueId().toString();
        ConnectedPlayer connectedPlayer = new ConnectedPlayer(event.getPlayer(), new ArrayList<>());

        event.getPlayer().getInventory().addItem(token);

        fetchAndApplyPotions(uuid, connectedPlayer, event.getPlayer());

        connectedPlayers.put(uuid, connectedPlayer);

        event.getPlayer().sendMessage("Welcome! You have received 3 Tokens.");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTask(plugin, () -> leaderboard.updateScoreboard(player));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        connectedPlayers.remove(event.getPlayer().getUniqueId().toString());
        playerScoreboards.remove(event.getPlayer().getUniqueId().toString());
    }

    private void fetchAndApplyPotions(String userId, ConnectedPlayer connectedPlayer, Player player)
    {
        String url = "jdbc:mysql://localhost:3306/krd_database";
        String username = "root";
        String password = "passwd!";

        String query = "SELECT potion_type, time, potion_size FROM users_custom_potions WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(query))
        {

            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next())
            {
                String potionName = rs.getString("potion_type");
                String potionType = rs.getString("potion_size");
                int time = rs.getInt("time");
                player.sendMessage(potionType + "_" + potionName);
                ActiveEffect effect = new ActiveEffect(plugin, player, potionName, potionType, time);
                connectedPlayer.addEffect(effect);
            }

        }
        catch (SQLException ignored)
        {
        }
    }
}
package org.example;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class Leaderboard
{
    private static final String DB_URL = "jdbc:mysql://localhost:3306/krd_database";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "passwd!";

    private final ConcurrentHashMap<String, Scoreboard> playerScoreboards;

    public Leaderboard(ConcurrentHashMap<String, Scoreboard> playerScoreboards)
    {
        this.playerScoreboards = playerScoreboards;
    }

    private static Connection getConnection() throws Exception
    {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private static void createUser(String playerId)
    {
        String query = "INSERT INTO users_balance (user_id, balance_coins, balance_gems) VALUES (?, 0, 0)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {

            statement.setString(1, playerId);
            statement.executeUpdate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void addCoins(Player player, double coins)
    {
        String query = "UPDATE users_balance SET balance_coins = balance_coins + ? WHERE user_id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query))
        {

            statement.setDouble(1, coins);  // The amount to add
            statement.setString(2, player.getUniqueId().toString()); // The player's ID
            statement.executeUpdate();
            updateScoreboard(player);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void addGems(Player player, Integer gems)
    {
        String query = "UPDATE users_balance SET balance_gems = balance_gems + ? WHERE user_id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query))
        {

            statement.setDouble(1, gems);  // The amount to add
            statement.setString(2, player.getUniqueId().toString()); // The player's ID
            statement.executeUpdate();
            updateScoreboard(player);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static int getCoins(String playerId)
    {
        String query = "SELECT balance_coins FROM users_balance WHERE user_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {

            statement.setString(1, playerId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("balance_coins");
            } else {
                createUser(playerId); // Create the user if not found
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0; // Default value if not found
    }

    // Fetch gems from the database
    public static int getGems(String playerId)
    {
        String query = "SELECT balance_gems FROM users_balance WHERE user_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {

            statement.setString(1, playerId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("balance_gems");
            } else {
                createUser(playerId); // Create the user if not found
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; // Default value if not found
    }

    public void updateScoreboard(Player player)
    {
        String playerId = player.getUniqueId().toString();

        int coins = getCoins(playerId);
        int gems = getGems(playerId);

        Scoreboard board = playerScoreboards.get(playerId);
        if (board == null)
        {
            board = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
            playerScoreboards.put(playerId, board);
        }

        Objective objective = board.getObjective("leaderboard");
        if (objective == null)
        {
            objective = board.registerNewObjective("leaderboard", "dummy", ChatColor.GOLD + "  Crownhaven  ");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        for (String entry : board.getEntries())
        {
            board.resetScores(entry);
        }

        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        objective.getScore(ChatColor.GRAY + "     " + formattedDate).setScore(8); // Centered date
        objective.getScore(" ").setScore(7);
        objective.getScore(ChatColor.WHITE + "⚲ Town").setScore(6); // Example with Compass
        objective.getScore("  ").setScore(5);
        objective.getScore(ChatColor.WHITE + "Coins: " + ChatColor.GOLD + coins).setScore(4); // Coins
        objective.getScore(ChatColor.WHITE + "Gems: " + ChatColor.AQUA + gems).setScore(3); // Gems
        objective.getScore("   ").setScore(2);
        objective.getScore(ChatColor.GRAY + "eu.crownhaven.net").setScore(1);

        player.setScoreboard(board);
    }
}

package org.example.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.example.Main;

import java.sql.*;

public class UserExperienceUtils {

    public static void addExperienceToPlayer(Plugin plugin, Player player, int experience)
    {
        try
        {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/krd_database", "root", "passwd!"
            );
            String playerUUID = player.getUniqueId().toString();

            PreparedStatement checkStatement = connection.prepareStatement(
                    "SELECT xp FROM player_xp WHERE player_uuid = ?"
            );
            checkStatement.setString(1, playerUUID);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next())
            {
                int currentXP = resultSet.getInt("xp");
                int newXP = currentXP + experience;

                PreparedStatement updateStatement = connection.prepareStatement(
                        "UPDATE player_xp SET xp = ? WHERE player_uuid = ?"
                );
                updateStatement.setInt(1, newXP);
                updateStatement.setString(2, playerUUID);
                updateStatement.executeUpdate();
            }
            else
            {
                PreparedStatement insertStatement = connection.prepareStatement(
                        "INSERT INTO player_xp (player_uuid, xp) VALUES (?, ?)"
                );
                insertStatement.setString(1, playerUUID);
                insertStatement.setInt(2, experience);
                insertStatement.executeUpdate();
            }
            ActionBarUtils.sendAndClearActionBar(plugin, player, ChatColor.GREEN + "You have collected " + ChatColor.YELLOW + experience + ChatColor.GREEN + " XP", 80);
        }
        catch (SQLException e)
        {
            plugin.getLogger().severe("Error updating player XP!");
        }
    }
}

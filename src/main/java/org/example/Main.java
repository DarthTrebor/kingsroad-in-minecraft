package org.example;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.example.CustomPlaceholders.ActiveEffects;
import org.example.Listeners.KRDCommandListener;
import org.example.Listeners.PlayerJoinListener;
import org.example.Listeners.EnderchestListener;
import org.example.Listeners.IgnitionListener;
import org.example.Listeners.ArmorUpdateListener;
import org.example.Listeners.PoisonStrikeListener;
import org.example.Listeners.PotionDrinkListener;
import org.example.Listeners.LeftClickArrowShooter;
import org.example.Listeners.BukkitListeners.OnEntityTarget;
import org.example.Listeners.BukkitListeners.OnPlayerDeath;
import org.example.Listeners.BukkitListeners.OnEntityDeath;
import org.example.Listeners.BukkitListeners.OnEntityDamage;
import org.example.Listeners.BukkitListeners.OnCreatureSpawn;
import org.example.Potions.CustomPotions.ExperiencePotion;
import org.example.Potions.CustomPotions.AvoidancePotion;
import org.example.Potions.CustomPotions.StrengthPotion;
import org.example.Potions.CustomPotions.CooldownReductionPotion;
import org.example.Potions.CustomPotions.LuckPotion;
import org.example.Potions.CustomPotions.ManaPotion;
import org.example.Potions.KRDPotion;
import org.example.Potions.KRDPotionType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class Main extends JavaPlugin {

    String banner = """
             _  ______  ____                                    \s
            | |/ /  _ \\|  _ \\ _   _ _ __   __ _  ___  ___  _ __ \s
            | ' /| |_) | | | | | | | '_ \\ / _` |/ _ \\/ _ \\| '_ \\\s
            | . \\|  _ <| |_| | |_| | | | | (_| |  __/ (_) | | | |
            |_|\\_\\_| \\_\\____/ \\__,_|_| |_|\\__, |\\___|\\___/|_| |_|
                                          |___/                 \s""";


    ConcurrentHashMap<String, ConnectedPlayer> connectedPlayers = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Scoreboard> playerScoreboards = new ConcurrentHashMap<>();
    HashMap<String, KRDPotion> existingPotions;

    @Override
    public void onEnable() {
        getLogger().info("KingsRoad plugin has been enabled!");
        getLogger().info(banner);

        loadExistingPotions();

        Leaderboard leaderboard = new Leaderboard(playerScoreboards);

        Objects.requireNonNull(getCommand("krd")).setExecutor(new KRDCommandListener(leaderboard,this));

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(leaderboard ,this, playerScoreboards, connectedPlayers), this);
        Bukkit.getPluginManager().registerEvents(new EnderchestListener(), this);
        Bukkit.getPluginManager().registerEvents(new IgnitionListener(), this);
        Bukkit.getPluginManager().registerEvents(new PotionDrinkListener(this, connectedPlayers), this);
        //Bukkit.getPluginManager().registerEvents(new DecoyListener(this, connectedPlayers), this);

        // Bukkit listeners
        Bukkit.getPluginManager().registerEvents(new OnPlayerDeath(this), this);
        Bukkit.getPluginManager().registerEvents(new OnEntityDeath(leaderboard, this, connectedPlayers), this);
        Bukkit.getPluginManager().registerEvents(new OnCreatureSpawn(this), this);
        Bukkit.getPluginManager().registerEvents(new OnEntityTarget(this), this);
        Bukkit.getPluginManager().registerEvents(new OnEntityDamage(this), this);

        getServer().getPluginManager().registerEvents(new ArmorUpdateListener(), this);

        Bukkit.getPluginManager().registerEvents(new PoisonStrikeListener(this), this);
        getServer().getPluginManager().registerEvents(new LeftClickArrowShooter(this), this);

        if (new ActiveEffects(connectedPlayers, existingPotions).register())
        {
            getLogger().info("ActiveEffects placeholder registered successfully.");
        } else
        {
            getLogger().severe("Failed to register ActiveEffects placeholder.");
        }
    }

    @Override
    public void onDisable()
    {
        getLogger().info("KingsRoad plugin has been disabled!");
    }

    private void loadExistingPotions()
    {
        existingPotions = new HashMap<>();
        Arrays.stream(KRDPotionType.values()).toList().forEach(potionType -> {
            AvoidancePotion avoidancePotion = new AvoidancePotion(potionType, this);
            CooldownReductionPotion cooldownReductionPotion = new CooldownReductionPotion(potionType, this);
            ExperiencePotion experiencePotion = new ExperiencePotion(potionType, this);
            LuckPotion luckPotion = new LuckPotion(potionType, this);
            ManaPotion manaPotion = new ManaPotion(potionType, this);
            StrengthPotion strengthPotion = new StrengthPotion(potionType, this);
            existingPotions.put(avoidancePotion.getIdentifier(), avoidancePotion);
            existingPotions.put(cooldownReductionPotion.getIdentifier(), cooldownReductionPotion);
            existingPotions.put(experiencePotion.getIdentifier(), experiencePotion);
            existingPotions.put(luckPotion.getIdentifier(), luckPotion);
            existingPotions.put(manaPotion.getIdentifier(), manaPotion);
            existingPotions.put(strengthPotion.getIdentifier(), strengthPotion);
        });
    }
}
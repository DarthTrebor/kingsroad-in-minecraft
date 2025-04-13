package org.example.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.Leaderboard;
import org.example.Potions.CustomPotions.*;
import org.example.Potions.KRDPotionType;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Base64;

public class KRDCommandListener implements CommandExecutor
{

    private final JavaPlugin plugin;
    private final Leaderboard leaderboard;

    public KRDCommandListener(Leaderboard leaderboard, JavaPlugin plugin)
    {
        this.leaderboard = leaderboard;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args)
    {
        if (!(sender instanceof Player player))
        {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        if (args.length < 1)
        {
            player.sendMessage(ChatColor.RED + "Usage: /krd <subcommand>");
            return true;
        }

        String subcommand = args[0].toLowerCase();

        if (subcommand.equals("storept"))
        {
            YamlConfiguration config = new YamlConfiguration();
            ItemStack mainHandItem = player.getInventory().getItemInMainHand();
            config.set("item", mainHandItem);
            sender.sendMessage(Base64.getEncoder().encodeToString(config.saveToString().getBytes()));
        }

        if (subcommand.equals("give_decoy"))
        {

            ItemStack spawnEgg = new ItemStack(Material.HORSE_SPAWN_EGG);
            ItemMeta meta = spawnEgg.getItemMeta();

            if (meta instanceof SpawnEggMeta)
            {
                meta.setDisplayName(ChatColor.GOLD + "Decoy");
                meta.setLore(java.util.Arrays.asList(
                        ChatColor.GRAY + "Spawns a Decoy Horse",
                        ChatColor.GRAY + "Use this to attract nearby entities."
                ));
                meta.getPersistentDataContainer().set(new org.bukkit.NamespacedKey(plugin, "decoy_horse"),
                        org.bukkit.persistence.PersistentDataType.STRING, "true");
                spawnEgg.setItemMeta(meta);
            }

            player.getInventory().addItem(spawnEgg);
            player.sendMessage(ChatColor.GREEN + "You received a Decoy Horse Spawn Egg!");
            return true;
        }

        if (subcommand.equals("give_coins"))
        {
            try
            {
                String username = args[1];
                int coinAmount = Integer.parseInt(args[2]);

                if (coinAmount <= 0)
                {
                    player.sendMessage(ChatColor.RED + "The number of coins cannot be negative!");
                    return true;
                }

                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(username);
                if (offlinePlayer.hasPlayedBefore())
                {
                    leaderboard.addCoins(offlinePlayer.getPlayer(), coinAmount);
                }
                else
                {
                    player.sendMessage(ChatColor.RED + "This player was not found!");
                }
            }
            catch (Exception e)
            {
                player.sendMessage(ChatColor.RED + "An error appeared while processing this command!");
                e.printStackTrace();
            }
            return true;
        }

        if (subcommand.equals("give_gems"))
        {
            try
            {
                String username = args[1];
                int gemsAmount = Integer.parseInt(args[2]);

                if (gemsAmount <= 0)
                {
                    player.sendMessage(ChatColor.RED + "The number of coins cannot be negative!");
                    return true;
                }

                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(username);
                if (offlinePlayer.hasPlayedBefore())
                {
                    leaderboard.addGems(offlinePlayer.getPlayer(), gemsAmount);
                }
                else
                {
                    player.sendMessage(ChatColor.RED + "This player was not found!");
                }
            }
            catch (Exception e)
            {
                player.sendMessage(ChatColor.RED + "An error appeared while processing this command!");
                e.printStackTrace();
            }
            return true;
        }

        if (subcommand.equals("give_potion"))
        {
            if (args.length < 2)
            {
                player.sendMessage(ChatColor.RED + "Usage: /krd give_potion <all/experience/goldluck/cooldown/avoidance/mana> <normal/grand>");
                return true;
            }

            String potionType = args[1].toLowerCase();
            String potionSize = args[2].toLowerCase();

            switch (potionType)
            {
                case "experience" -> {
                    ExperiencePotion experiencePotion = new ExperiencePotion(potionSize.equals("normal") ? KRDPotionType.NORMAL : KRDPotionType.GRAND, plugin);
                    ItemStack potion = experiencePotion.createPotion();
                    player.getInventory().addItem(potion);

                    player.sendMessage(ChatColor.GREEN + "You have received an " + ChatColor.YELLOW + "Experience" + ChatColor.GREEN + " Potion!");
                    return true;
                }
                case "strength" -> {
                    StrengthPotion strengthPotion = new StrengthPotion(potionSize.equals("normal") ? KRDPotionType.NORMAL : KRDPotionType.GRAND, plugin);
                    ItemStack potion = strengthPotion.createPotion();
                    player.getInventory().addItem(potion);

                    player.sendMessage(ChatColor.GREEN + "You have received an " + ChatColor.RED + "Strength" + ChatColor.GREEN + " Potion!");
                    return true;
                }
                case "goldluck" -> {
                    LuckPotion luckPotion = new LuckPotion(potionSize.equals("normal") ? KRDPotionType.NORMAL : KRDPotionType.GRAND, plugin);
                    ItemStack potion = luckPotion.createPotion();
                    player.getInventory().addItem(potion);

                    player.sendMessage(ChatColor.GREEN + "You have received a " + ChatColor.GOLD + "Gold Luck" + ChatColor.GREEN + " Potion!");
                    return true;
                }
                case "mana" -> {
                    ManaPotion manaPotion = new ManaPotion(potionSize.equals("normal") ? KRDPotionType.NORMAL : KRDPotionType.GRAND, plugin);
                    ItemStack potion = manaPotion.createPotion();
                    player.getInventory().addItem(potion);

                    player.sendMessage(ChatColor.GREEN + "You have received a " + ChatColor.BLUE + "Mana" + ChatColor.GREEN + " Potion!");
                    return true;
                }
                case "avoidance" -> {
                    AvoidancePotion avoidancePotion = new AvoidancePotion(potionSize.equals("normal") ? KRDPotionType.NORMAL : KRDPotionType.GRAND, plugin);
                    ItemStack potion = avoidancePotion.createPotion();
                    player.getInventory().addItem(potion);

                    player.sendMessage(ChatColor.GREEN + "You have received a " + ChatColor.DARK_GRAY + "Avoidance" + ChatColor.GREEN + " Potion!");
                    return true;
                }
                case "cooldown" -> {
                    CooldownReductionPotion cooldownReductionPotion = new CooldownReductionPotion(potionSize.equals("normal") ? KRDPotionType.NORMAL : KRDPotionType.GRAND, plugin);
                    ItemStack potion = cooldownReductionPotion.createPotion();
                    player.getInventory().addItem(potion);

                    player.sendMessage(ChatColor.GREEN + "You have received a " + ChatColor.DARK_BLUE + "Cooldown Reduction" + ChatColor.GREEN + " Potion!");
                    return true;
                }
                case "all" -> {
                    ItemStack[] potions = new ItemStack[]{
                            new ExperiencePotion(potionSize.equals("normal") ? KRDPotionType.NORMAL : KRDPotionType.GRAND, plugin).createPotion(),
                            new StrengthPotion(potionSize.equals("normal") ? KRDPotionType.NORMAL : KRDPotionType.GRAND, plugin).createPotion(),
                            new LuckPotion(potionSize.equals("normal") ? KRDPotionType.NORMAL : KRDPotionType.GRAND, plugin).createPotion(),
                            new ManaPotion(potionSize.equals("normal") ? KRDPotionType.NORMAL : KRDPotionType.GRAND, plugin).createPotion(),
                            new AvoidancePotion(potionSize.equals("normal") ? KRDPotionType.NORMAL : KRDPotionType.GRAND, plugin).createPotion(),
                            new CooldownReductionPotion(potionSize.equals("normal") ? KRDPotionType.NORMAL : KRDPotionType.GRAND, plugin).createPotion()
                    };

                    if (canAddItemsToInventory(player, potions.length))
                    {
                        player.sendMessage(ChatColor.RED + "Not enough inventory space. Make room for " + potions.length + " potions!");
                        return true;
                    }

                    for (ItemStack potion : potions)
                    {
                        player.getInventory().addItem(potion);
                    }

                    player.sendMessage(ChatColor.GREEN + "You have received " + potions.length + " potions!");
                    return true;
                }
            }

            player.sendMessage(ChatColor.RED + "Invalid potion type. Use /krd give_potion <experience/gold_luck>.");
            return true;
        }

        // Handle the "create_item" subcommand
        if (subcommand.equals("create_item"))
        {
            if (args.length < 2)
            {
                player.sendMessage(ChatColor.RED + "Usage: /krd create_item <netherite_piece> (helmet/chestplate/leggings/boots/shield/sword)");
                return true;
            }

            String pieceType = args[1].toLowerCase();
            Material material = switch (pieceType) {
                case "helmet" -> Material.NETHERITE_HELMET;
                case "chestplate" -> Material.NETHERITE_CHESTPLATE;
                case "leggings" -> Material.NETHERITE_LEGGINGS;
                case "boots" -> Material.NETHERITE_BOOTS;
                case "shield" -> Material.SHIELD;
                case "sword" -> Material.NETHERITE_SWORD;
                default -> null;
            };

            if (material == null)
            {
                player.sendMessage(ChatColor.RED + "Invalid piece type. Please specify helmet, chestplate, leggings, boots, shield, or sword.");
                return true;
            }

            // Create and give the item
            ItemStack item = new ItemStack(material);
            updateItemDescription(player, item, 0); // Assume 1 piece initially when created
            player.getInventory().addItem(item);

            player.sendMessage(ChatColor.GREEN + "You have received a special Tempestbringer " + pieceType + "!");
            return true;
        }

        player.sendMessage(ChatColor.RED + "Unknown subcommand. Use /krd create_item or /krd give_potion.");
        return true;
    }

    /**
     * Updates the item's description dynamically based on the number of equipped pieces.
     *
     * @param player The player to check for equipped pieces.
     * @param item   The item to update.
     * @param pieceCount Initial piece count if creating the item.
     */
    public static void updateItemDescription(Player player, ItemStack item, int pieceCount)
    {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        // Generate random stats
        Random random = new Random();
        int armorValue = random.nextInt(100) + 1; // 1 to 100
        int healthValue = random.nextInt(100) + 1; // 1 to 100
        int dodgeChance = random.nextInt(100) + 1; // 1 to 100
        int critChance = random.nextInt(100) + 1; // 1 to 100

        // Set the custom name
        String minecraftName = switch (item.getType())
        {
            case NETHERITE_HELMET -> "Helmet";
            case NETHERITE_CHESTPLATE -> "Chestplate";
            case NETHERITE_LEGGINGS -> "Leggings";
            case NETHERITE_BOOTS -> "Boots";
            case SHIELD -> "Shield";
            case NETHERITE_SWORD -> "Sword";
            default -> null;
        };

        if (minecraftName != null)
        {
            meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Tempestbringer " + minecraftName);
        }

        // Update lore
        List<String> lore = new ArrayList<>();
        // Add random stats to the lore
        lore.add(ChatColor.WHITE + "+" + armorValue + " Armor");
        lore.add(ChatColor.WHITE + "+" + healthValue + " Health");
        lore.add(ChatColor.WHITE + "+" + dodgeChance + "% Dodge Chance");
        lore.add(ChatColor.WHITE + "+" + critChance + "% Crit Chance");
        lore.add("");

        healthValue = random.nextInt(100) + 1; // Re-roll health value for set bonuses

        // Add set bonuses
        lore.add(ChatColor.GOLD + "~ Tempest Armaments ~");
        lore.add(ChatColor.WHITE + "Set bonuses:");
        lore.add(ChatColor.GRAY + "(2) " + ChatColor.GREEN + "+5% Crit Chance");
        lore.add(ChatColor.GRAY + "(3) " + ChatColor.GREEN + healthValue + " Health");
        lore.add(ChatColor.GRAY + "(4) " + ChatColor.RED + "Ignition. " + ChatColor.GRAY + "You have a chance to set your enemy on fire for 5 seconds.");
        lore.add(""); // Blank line for spacing

        lore.add(ChatColor.RED + "Required Class: Knight");

        lore.add(ChatColor.GOLD + "(" + pieceCount + "/6) pieces equipped."); // Adjusted for 6-piece set

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    private boolean canAddItemsToInventory(Player player, int itemCount)
    {
        int emptySlots = 0;
        for (ItemStack item : player.getInventory().getContents())
        {
            if (item == null)
            {
                emptySlots++;
            }
        }
        return emptySlots < itemCount;
    }
}

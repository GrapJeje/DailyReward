package eu.camonetwork.dailyreward.infrastructure;

import eu.camonetwork.dailyreward.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RewardItems {
    public ItemStack giveRewards(Player player, String reward) {
        reward = reward.toLowerCase();

        if (reward.startsWith("candy")) {
            String[] parts = reward.split("_");
            int amount;

            try {
                amount = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(Text.colorize("&cDe daily rewards hebben momenteel een error, contacteer een admin!"));
                Bukkit.getServer().getPluginManager().disablePlugin(Main.instance);
                return null;
            }

            return this.CandyItem(amount);
        } else if (reward.startsWith("money")) {
            String[] parts = reward.split("_");
            int amount;

            try {
                amount = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(Text.colorize("&cDe daily rewards hebben momenteel een error, contacteer een admin!"));
                Bukkit.getServer().getPluginManager().disablePlugin(Main.instance);
                return null;
            }

            return this.MoneyItem(amount);
        } else if (reward.startsWith("material")) {
            String[] parts = reward.split("\\.");
            Material material;
            int amount;

            try {
                material = Material.valueOf(parts[1].toUpperCase());
            } catch (Exception e) {
                player.sendMessage(Text.colorize("&cDe daily rewards hebben momenteel een error, contacteer een admin!"));
                Bukkit.getServer().getPluginManager().disablePlugin(Main.instance);
                return null;
            }

            try {
                amount = Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                player.sendMessage(Text.colorize("&cDe daily rewards hebben momenteel een error, contacteer een admin!"));
                Bukkit.getServer().getPluginManager().disablePlugin(Main.instance);
                return null;
            }

            if (amount == 0) amount = 1;

            return this.MaterialItem(material, amount);

        } else {
            player.sendMessage(Text.colorize("&cDe daily rewards hebben momenteel een error, contacteer een admin!"));
            Bukkit.getServer().getPluginManager().disablePlugin(Main.instance);
            return null;
         }

    }

    private ItemStack CandyItem(int amount) {
        return ItemBuilder.create()
                .setType(Material.PAPER)
                .setDisplayName("&d" + amount + " Candy")
                .addLore("&7Rechter-muisknop om in te wisselen")
                .build();
    }

    // Amount in 5000.
    private ItemStack MoneyItem(int amount) {
        return ItemBuilder.create()
                .setType(Material.GHAST_TEAR)
                .setDisplayName("&f€5000")
                .setAmount(amount)
                .build();
    }

    private ItemStack MaterialItem(Material material, int amount) {
        return ItemBuilder.create()
                .setType(material)
                .setAmount(amount)
                .build();
    }
}
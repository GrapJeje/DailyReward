package eu.camonetwork.dailyreward.infrastructure;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RewardItems {
    public ItemStack giveRewards(Player player, String reward) {
        if (reward.startsWith("candy")) {
            String[] parts = reward.split("_");
            int amount;

            try {
                amount = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(Text.colorize("&cDe daily rewards hebben momenteel een error, contacteer een admin!"));
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
                return null;
            }

            return this.MoneyItem(amount);
        } else {
            player.sendMessage(Text.colorize("&cDe daily rewards hebben momenteel een error, contacteer een admin!"));
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
}
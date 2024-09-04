package eu.camonetwork.dailyreward.infrastructure;

import eu.camonetwork.dailyreward.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;

public class RewardItems {

    public ItemStack giveRewards(Player player, String reward) {
        switch (reward) {
            case "candy_15":
                return this.CandyItem(15);
            case "money_4":
                return this.MoneyItem(4);
            default:
                player.sendMessage(Text.colorize("&cDe daily rewards hebben momenteel een error, contacteer een admin!"));
                break;
        }

        return null;
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

    public void keyHelper(Player player, String keyName, int amount) {

        if (keyName != null) {
            if (Main.cp.getConfigHandler().getCrates().get(keyName.toLowerCase()) == null) {
                Bukkit.getLogger().warning("Crate not found: " + keyName);
                return;
            }

            Main.cp.getCrateHandler().giveCrateKey(player, keyName, amount);

        }
    }
}

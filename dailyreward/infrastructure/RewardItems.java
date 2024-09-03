package eu.camonetwork.dailyreward.infrastructure;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RewardItems {

    public ItemStack giveRewards(Player player, String reward) {
        switch (reward) {
            case "candy_15":
                return this.CandyItem(15);
            default:
                player.sendMessage(Text.colorize("&cDe daily rewards hebben momenteel een error, contacteer een admin!"));
                break;
        }

        return null;
    }

    private ItemStack CandyItem(int amount) {
        return ItemBuilder.create()
                .setType(Material.PAPER)
                .setDisplayName("&d"+ amount + " Candy")
                .addLore("&7Rechter-muisknop om in te wisselen")
                .build();
    }


}

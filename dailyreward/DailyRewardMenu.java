package eu.camonetwork.dailyreward;

import eu.camonetwork.dailyreward.infrastructure.InventoryMenu;
import eu.camonetwork.dailyreward.infrastructure.ItemBuilder;
import eu.camonetwork.dailyreward.infrastructure.Text;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DailyRewardMenu extends InventoryMenu {

    private final int price = 20;

    public DailyRewardMenu() {
        this.setName("Daily Rewards");
        this.lines = 3;
        this.readOnly = true;
    }

    @Override
    protected void update() {
        drawLine(1, this.fillerItem());
        drawLine(2, this.fillerItem());
        drawLine(3, this.fillerItem());

        this.clickableItem(10, this.dayItem(Material.PAPER, 1, "&d15 Candy!"), player, () -> {
            this.nextStage(player, "candy_15");
        });

        this.clickableItem(11, this.dayItem(Material.PAPER, 2, "&d15 Candy!"), player, () -> {
            player.closeInventory();
        });

        this.clickableItem(12, this.dayItem(Material.PAPER, 3, "&d15 Candy!"), player, () -> {
            player.closeInventory();
        });

        this.clickableItem(13, this.dayItem(Material.PAPER, 4, "&d15 Candy!"), player, () -> {
            player.closeInventory();
        });

        this.clickableItem(14, this.dayItem(Material.PAPER, 5, "&d15 Candy!"), player, () -> {
            player.closeInventory();
        });

        this.clickableItem(15, this.dayItem(Material.PAPER, 6, "&d15 Candy!"), player, () -> {
            player.closeInventory();
        });

        this.clickableItem(16, this.dayItem(Material.PAPER, 7, "&d15 Candy!"), player, () -> {
            player.closeInventory();
        });

        inventory.setItem(21, this.StreakItem(player));
        this.clickableItem(22, this.closeItem(), player, player::closeInventory);
        inventory.setItem(23, this.longestStreakItem(player));
    }

    private void nextStage(Player player, String reward) {
        ItemStack itemStack = Main.rewardItems.giveRewards(player, reward);
        int streak = Main.defaultConfig.getPlayerStreak(player);
        int longestStreak = Main.defaultConfig.getPlayerLongestStreak(player);
        int allTimeStreak = Main.defaultConfig.getAllTimeLongestStreakCount();

        if (Main.defaultConfig.getPlayerClaimed(player)) {
            player.closeInventory();
            player.sendMessage(Text.colorize("&3Je hebt dit reward al geclaimed, kom morgen terug!"));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
            return;
        }

        if (this.isInventoryFull(player)) {
            player.closeInventory();
            player.getInventory().addItem(itemStack);

            if (longestStreak + 1 >= allTimeStreak) {
                Main.defaultConfig.setAllTimeLongestStreak(player, streak + 1);
            }

            if (streak + 1 >= longestStreak) {
                Main.defaultConfig.setPlayerLongestStreak(player, streak + 1);
            }

            if (Main.defaultConfig.getPlayerClaimed(player)) {
                if (!Main.defaultConfig.getPlayerBought(player)) {
                    Main.defaultConfig.setPlayerBought(player, true);
                }
            }

            Main.defaultConfig.setPlayerClaimed(player, true);
            Main.defaultConfig.setPlayerSteak(player, streak + 1);
        } else {
            player.closeInventory();
            player.sendMessage(Text.colorize("&cMaak je inventory leeg om je reward te kunnen claimen"));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
        }
    }

    private ItemStack dayItem(Material material, int day, String reward) {
        ItemBuilder itemBuilder = ItemBuilder.create();
        itemBuilder.setType(material)
                .setDisplayName("&cDag " + day)
                .setAmount(day)
                .addLore(reward)
                .addLore(" ");

        if (Main.defaultConfig.getPlayerDay(player) == day + 1) {
            itemBuilder.addLore("&aKom morgen terug om dit te claimen!");
        } else if (Main.defaultConfig.getPlayerDay(player) != day) {
            itemBuilder.addLore(" ");
        } else if (!Main.defaultConfig.getPlayerClaimed(player)) {
            itemBuilder.addLore("&aClick to claim!");
        } else if (Main.defaultConfig.getPlayerClaimed(player) && !Main.defaultConfig.getPlayerBought(player)) {
            itemBuilder.addLore("&aClick om opnieuw te claimen voor &d" + price + " candy&a.");
            itemBuilder.addGlowItem();
        } else {
            itemBuilder.addLore("&aAlready claimed!");
            itemBuilder.addGlowItem();
        }

        return itemBuilder.build();
    }

    private ItemStack longestStreakItem(Player player) {
        return ItemBuilder.create()
                .setType(Material.PAPER)
                .setDisplayName("&6Langste streak record:")
                .addLore("&e" + Main.defaultConfig.getAllTimeLongestStreakPlayer() + " - " + Main.defaultConfig.getAllTimeLongestStreakCount())
                .addLore("&eJouw langste streak: " + Main.defaultConfig.getPlayerLongestStreak(player))
                .build();
    }

    private ItemStack StreakItem(Player player) {
        return ItemBuilder.create()
                .setType(Material.PAPER)
                .setDisplayName("&6Streak:")
                .addLore("&eJouw streak is: " + Main.defaultConfig.getPlayerStreak(player))
                .build();
    }

    private ItemStack closeItem() {
        return ItemBuilder.create()
                .setType(Material.BARRIER)
                .setDisplayName("&4Close")
                .addLore("&7Click to close")
                .build();
    }

    private boolean isInventoryFull(Player player) {
        Inventory inv = player.getInventory();
        int emptySlotCount = 0;

        for (int i = 0; i < 36; i++) {
            ItemStack item = inv.getItem(i);
            if (item == null || item.getType() == Material.AIR) {
                emptySlotCount++;
            }
        }

        return emptySlotCount >= 1;
    }
}

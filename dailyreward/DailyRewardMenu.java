package eu.camonetwork.dailyreward;

import eu.camonetwork.dailyreward.infrastructure.InventoryMenu;
import eu.camonetwork.dailyreward.infrastructure.ItemBuilder;
import eu.camonetwork.dailyreward.infrastructure.Text;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class DailyRewardMenu extends InventoryMenu {

    private final int price = 20;

    public DailyRewardMenu() {
        this.setName("Daily Rewards");
        this.lines = 3;
        this.readOnly = true;
    }

    @Override
    protected void update() {
        // Fill the background with filler item.
        this.fillBackground();

        // Add all the day items.
        this.DailyRewardsItem(player);

        // Add all the info items.
        inventory.setItem(21, this.StreakItem(player));
        this.clickableItem(22, this.closeItem(), player, player::closeInventory);
        inventory.setItem(23, this.longestStreakItem(player));
    }

    private void DailyRewardsItem(Player player) {
        DefaultConfig config = new DefaultConfig();
        int currentDay = config.getPlayerDay(player);
        int baseDay = ((currentDay - 1) / 7) * 7 + 1;

        this.clickableItem(10, this.dayItem(Material.GHAST_TEAR, baseDay, "&a20k!"), player, () -> {
            this.nextStage(player, "money_4", baseDay);
        });

        this.clickableItem(11, this.dayItem(Material.PAPER, baseDay + 1, "&d15 Candy!"), player, () -> {
            this.nextStage(player, "candy_15", baseDay + 1);
        });

        this.clickableItem(12, this.dayItem(Material.PAPER, baseDay + 2, "&d15 Candy!"), player, () -> {
            this.nextStage(player, "candy_15", baseDay + 2);
        });

        this.clickableItem(13, this.dayItem(Material.PAPER, baseDay + 3, "&d15 Candy!"), player, () -> {
            this.nextStage(player, "candy_15", baseDay + 3);
        });

        this.clickableItem(14, this.dayItem(Material.PAPER, baseDay + 4, "&d15 Candy!"), player, () -> {
            this.nextStage(player, "candy_15", baseDay + 4);
        });

        this.clickableItem(15, this.dayItem(Material.PAPER, baseDay + 5, "&d15 Candy!"), player, () -> {
            this.nextStage(player, "candy_15", baseDay + 5);
        });

        this.clickableItem(16, this.dayItem(Material.TRIPWIRE_HOOK, baseDay + 6, "&4&l&ka &6&lLegendary key! &4&l&ka"), player, () -> {
            this.nextStage(player, baseDay + 6, "legendary");
        });
    }

    // Give a custom item.
    private void nextStage(Player player, String reward, int day) {
        ItemStack itemStack = Main.rewardItems.giveRewards(player, reward);
        boolean claimed = Main.defaultConfig.getPlayerClaimed(player);
        int streak = Main.defaultConfig.getPlayerStreak(player);
        int longestStreak = Main.defaultConfig.getPlayerLongestStreak(player);
        int allTimeStreak = Main.defaultConfig.getAllTimeLongestStreakCount();
        int playerDay = Main.defaultConfig.getPlayerDay(player) + 1;
        if (claimed) playerDay = Main.defaultConfig.getPlayerDay(player);

        int serverDay = Main.defaultConfig.getServerDay(player);
        boolean pClaimed = Main.defaultConfig.getPlayerClaimed(player) && playerDay >= day;
        boolean pBought = Main.defaultConfig.getPlayerBought(player) && playerDay >= day;

        if (day > serverDay || day < serverDay) return;
        player.closeInventory();

        if (pClaimed && pBought) {
            player.sendMessage(Text.colorize("&3Je hebt dit reward al geclaimed en opnieuw gekocht, kom morgen terug!"));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
            return;
        }

        if (pClaimed && !pBought) {
            if (Main.pp.getAPI().look(player.getUniqueId()) >= price) {
                if (!this.isInventoryFull(player)) {

                    player.sendMessage(Text.colorize("&cMaak je inventory leeg om dit te kunnen kopen."));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                    return;
                }

                Main.pp.getAPI().take(player.getUniqueId(), price);
                player.getInventory().addItem(itemStack);

                player.sendMessage(Text.colorize("&3Je hebt zojuist &a" + price + " &dCandy &3betaald."));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
                Main.defaultConfig.setPlayerBought(player, true);
            } else {

                player.sendMessage(Text.colorize("&3Je hebt niet genoeg &dCandy &3om dit te kopen."));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                return;
            }
        }

        if (!Main.defaultConfig.getPlayerClaimed(player)) {
            if (!this.isInventoryFull(player)) {

                player.sendMessage(Text.colorize("&cMaak je inventory leeg om je reward te kunnen claimen"));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                return;
            }

            player.getInventory().addItem(itemStack);
            Main.defaultConfig.setPlayerClaimed(player, true);
            Main.defaultConfig.setPlayerSteak(player, streak + 1);

            if (streak + 1 > longestStreak) {
                Main.defaultConfig.setPlayerLongestStreak(player, streak + 1);
            }

            if (streak + 1 >= allTimeStreak) {
                Main.defaultConfig.setAllTimeLongestStreak(player, streak + 1);
            }
        }
    }

    // Give a key item.
    private void nextStage(Player player, int day, String keyName) {
        boolean claimed = Main.defaultConfig.getPlayerClaimed(player);
        int streak = Main.defaultConfig.getPlayerStreak(player);
        int longestStreak = Main.defaultConfig.getPlayerLongestStreak(player);
        int allTimeStreak = Main.defaultConfig.getAllTimeLongestStreakCount();
        int playerDay = Main.defaultConfig.getPlayerDay(player) + 1;
        if (claimed) playerDay = Main.defaultConfig.getPlayerDay(player);

        int serverDay = Main.defaultConfig.getServerDay(player);
        boolean pClaimed = Main.defaultConfig.getPlayerClaimed(player) && playerDay >= day;
        boolean pBought = Main.defaultConfig.getPlayerBought(player) && playerDay >= day;

        if (day > serverDay || day < serverDay) return;
        player.closeInventory();

        if (pClaimed && pBought) {
            player.sendMessage(Text.colorize("&3Je hebt dit reward al geclaimed en opnieuw gekocht, kom morgen terug!"));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
            return;
        }

        if (pClaimed && !pBought) {
            if (Main.pp.getAPI().look(player.getUniqueId()) >= price) {
                if (!this.isInventoryFull(player)) {

                    player.sendMessage(Text.colorize("&cMaak je inventory leeg om dit te kunnen kopen."));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                    return;
                }

                Main.pp.getAPI().take(player.getUniqueId(), price);
                Main.cp.getCrateHandler().giveCrateKey(player, keyName);

                player.sendMessage(Text.colorize("&3Je hebt zojuist &a" + price + " &dCandy &3betaald."));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
                Main.defaultConfig.setPlayerBought(player, true);
            } else {

                player.sendMessage(Text.colorize("&3Je hebt niet genoeg &dCandy &3om dit te kopen."));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                return;
            }
        }

        if (!Main.defaultConfig.getPlayerClaimed(player)) {
            if (!this.isInventoryFull(player)) {

                player.sendMessage(Text.colorize("&cMaak je inventory leeg om je reward te kunnen claimen"));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                return;
            }

            Main.cp.getCrateHandler().giveCrateKey(player, keyName);
            Main.defaultConfig.setPlayerClaimed(player, true);
            Main.defaultConfig.setPlayerSteak(player, streak + 1);

            if (streak + 1 > longestStreak) {
                Main.defaultConfig.setPlayerLongestStreak(player, streak + 1);
            }

            if (streak + 1 >= allTimeStreak) {
                Main.defaultConfig.setAllTimeLongestStreak(player, streak + 1);
            }
        }
    }

    private ItemStack dayItem(Material material, int day, String reward) {
        boolean claimed = Main.defaultConfig.getPlayerClaimed(player);
        ItemBuilder itemBuilder = ItemBuilder.create();
        itemBuilder.setType(material)
                .setDisplayName("&cDag " + day)
                .addLore(reward)
                .addLore(" ");

        if (day > 64) {
            itemBuilder.setAmount(1);
        } else {
            itemBuilder.setAmount(day);
        }

        int playerDay = Main.defaultConfig.getPlayerDay(player) + 1;
        if (claimed) playerDay = Main.defaultConfig.getPlayerDay(player);

        int serverDay = Main.defaultConfig.getServerDay(player);
        boolean correctDay = playerDay == day && day == serverDay;
        boolean pClaimed = Main.defaultConfig.getPlayerClaimed(player) && playerDay >= day;
        boolean pBought = Main.defaultConfig.getPlayerBought(player) && playerDay >= day;

        if (pClaimed && !pBought && !(day < serverDay)) {
            itemBuilder.addLore("&aClick om opnieuw te claimen voor &d" + price + " candy&a.");
            itemBuilder.addGlowItem();
        } else if (pClaimed && pBought || day < serverDay) {
            itemBuilder.addLore("&aAlready claimed!");
            itemBuilder.addGlowItem();
        } else if (correctDay) {
            itemBuilder.addLore("&aClick to claim!");
        } else if (day == serverDay + 1) {
            itemBuilder.addLore("&aKom morgen terug om dit te claimen!");
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

package eu.camonetwork.dailyreward;

import eu.camonetwork.dailyreward.infrastructure.InventoryMenu;
import eu.camonetwork.dailyreward.infrastructure.ItemBuilder;
import eu.camonetwork.dailyreward.infrastructure.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DailyRewardMenu extends InventoryMenu {

    private final int price = 20;
    private boolean run = false;
    private final Map<Integer, BukkitTask> animationTasks = new HashMap<>();

    public DailyRewardMenu() {
        this.setName("Daily Rewards");
        this.lines = 3;
        this.readOnly = true;
    }

    @Override
    protected void update() {
        if (player == null) return;

        // Fill the background with filler item.
        this.fillBackground();

        // Add all the day items.
        this.DailyRewardsItem(player);

        run = true;

        // Add all the info items.
        inventory.setItem(21, this.StreakItem(player));
        this.clickableItem(22, this.closeItem(), player, player::closeInventory);
        inventory.setItem(23, this.longestStreakItem(player));
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        run = false;
        for (BukkitTask task : animationTasks.values()) {
            if (task != null) {
                task.cancel();
            }
        }
        animationTasks.clear();
    }

    private void DailyRewardsItem(Player player) {
        int currentDay = Main.defaultConfig.getPlayerDay(player);
        int maxDay = Main.itemConfig.getMaxDay();
        int baseDay = ((currentDay - 1) / 7) * 7 + 1;

        for (int i = 0; i < 7; i++) {
            int slot = 10 + i;
            int day = baseDay + i;
            int countDay = baseDay + i;

            if (day > maxDay) {
                day = (day - 1) % maxDay + 1;
            }

            final int finalDay = day;

            List<String> rewards = Main.itemConfig.getRewardsForDay(day);
            List<String> lores = Main.itemConfig.getLoresForDay(day);

            if (rewards.isEmpty()) continue;

            if (rewards.size() > 1) {
                this.showAnimation(rewards, player, slot, day, countDay);
            } else {
                String reward = rewards.get(0);
                String lore = !lores.isEmpty() ? lores.get(0) : "";
                this.clickableItem(slot, this.dayItem(this.getMaterial(reward), countDay, lore), player, () -> {
                    if (Main.itemConfig.getIfKey(finalDay)) {
                        this.nextStage(player, finalDay, reward);
                    } else {
                        this.nextStage(player, reward, finalDay);
                    }
                });
            }
        }
    }

    private void showAnimation(List<String> rewards, Player player, int slot, int day, int countDay) {
        if (animationTasks.containsKey(slot)) {
            animationTasks.get(slot).cancel();
            animationTasks.remove(slot);
        }

        Random random = new Random();

        int length = rewards.size();
        List<String> lores = Main.itemConfig.getLoresForDay(day);

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
            int index = 0;

            @Override
            public void run() {
                if (!run) {
                    if (animationTasks.containsKey(slot)) {
                        animationTasks.get(slot).cancel();
                        animationTasks.remove(slot);
                    }
                    return;
                }

                if (length == 0) return;

                String reward = rewards.get(index);
                Material material = getMaterial(reward);
                String lore = !lores.isEmpty() ? lores.get(index % lores.size()) : "";

                clickableItem(slot, dayItem(material, countDay, lore), player, () -> {
                    String randomReward = rewards.get(random.nextInt(length));
                    if (Main.itemConfig.getIfKey(day)) {
                        nextStage(player, day, randomReward);
                    } else {
                        nextStage(player, randomReward, day);
                    }
                });

                index++;

                if (index >= length) {
                    index = 0;
                }
            }
        }, 0L, 20L);

        animationTasks.put(slot, task);
    }

    private Material getMaterial(String reward) {
        reward = reward.toLowerCase();
        if (reward.startsWith("money")) {
            return Material.GHAST_TEAR;
        } else if (reward.startsWith("candy")) {
            return Material.PAPER;
        } else if (reward.startsWith("key")) {
            return Material.TRIPWIRE_HOOK;
        } else if (reward.startsWith("material")) {
            String[] parts = reward.split("\\.");
            Material material;

            try {
                material = Material.valueOf(parts[1].toUpperCase());
            } catch (Exception e) {
                player.sendMessage(Text.colorize("&cDe daily rewards hebben momenteel een error, contacteer een admin!"));
                return null;
            }

            return material;
        } else {
            return Material.BARRIER;
        }
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

        String[] parts = keyName.split("_");
        keyName = parts[1];

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
        if (player == null) return new ItemStack(Material.BARRIER);

        ItemBuilder itemBuilder = ItemBuilder.create();
        itemBuilder.setType(material)
                .setDisplayName("&cDag " + day)
                .addLore(reward)
                .addLore(" ");

        itemBuilder.setAmount(Math.min(day, 64));

        int playerDay = Main.defaultConfig.getPlayerDay(player) + 1;
        if (Main.defaultConfig.getPlayerClaimed(player)) playerDay = Main.defaultConfig.getPlayerDay(player);

        int serverDay = Main.defaultConfig.getServerDay(player);
        boolean correctDay = playerDay == day && day == serverDay;
        boolean pClaimed = Main.defaultConfig.getPlayerClaimed(player) && playerDay >= day;
        boolean pBought = Main.defaultConfig.getPlayerBought(player) && playerDay >= day;

        if (pClaimed && !pBought && !(day < serverDay)) {
            itemBuilder.addLore("&aClick om opnieuw te claimen voor &d" + price + " candy&a.");
            itemBuilder.addGlowItem();
        } else if (pClaimed && pBought || day < serverDay) {
            itemBuilder.addLore("&aAl geclaimed!");
            itemBuilder.addGlowItem();
        } else if (correctDay) {
            itemBuilder.addLore("&aKlik om te claimen!");
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
package eu.camonetwork.dailyreward;

import eu.camonetwork.dailyreward.infrastructure.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class DefaultConfig extends ConfigManager {
    public DefaultConfig() {
        this.setFileName("config.yml");
    }

    public boolean getIfEnabled() {
        return this.getConfig().getBoolean("enabled");
    }

    public List<OfflinePlayer> getAllPlayers() {
        ConfigurationSection playerSection = this.getConfig().getConfigurationSection("players");

        if (playerSection == null) return Collections.emptyList();

        List<OfflinePlayer> offlinePlayer = new ArrayList<>();

        for (String key : playerSection.getKeys(false)) {
            UUID playerUUID = UUID.fromString(key);

            offlinePlayer.add(Bukkit.getOfflinePlayer(playerUUID));
        }

        return offlinePlayer;
    }

    public void setServerDay(Player player, int day) {
        this.getConfig().set("players." + player.getUniqueId() + ".serverday", day);
        this.save();
    }

    public int getServerDay(Player player) {
        return this.getConfig().getInt("players." + player.getUniqueId() + ".serverday", 1);
    }

    public void setPlayerDay(Player player, int day) {
        this.getConfig().set("players." + player.getUniqueId() + ".dag", day);
        this.save();
    }

    public int getPlayerDay(Player player) {
        return this.getConfig().getInt("players." + player.getUniqueId() + ".dag", 0);
    }

    public void setPlayerClaimed(Player player, boolean claimed) {
        this.getConfig().set("players." + player.getUniqueId() + ".claimed", claimed);
        this.save();
    }

    public boolean getPlayerClaimed(Player player) {
        return this.getConfig().getBoolean("players." + player.getUniqueId() + ".claimed");
    }

    public void setPlayerBought(Player player, boolean bought) {
        this.getConfig().set("players." + player.getUniqueId() + ".bought", bought);
        this.save();
    }

    public boolean getPlayerBought(Player player) {
        return this.getConfig().getBoolean("players." + player.getUniqueId() + ".bought");
    }

    public void setPlayerSteak(Player player, int day) {
        this.getConfig().set("players." + player.getUniqueId() + ".dag", day);
        this.save();
    }

    public int getPlayerStreak(Player player) {
        return this.getConfig().getInt("players." + player.getUniqueId() + ".dag", 0);
    }

    public void setPlayerLongestStreak(Player player, int day) {
        this.getConfig().set("players." + player.getUniqueId() + ".dag", day);
        this.save();
    }

    public int getPlayerLongestStreak(Player player) {
        return this.getConfig().getInt("players." + player.getUniqueId() + ".dag");
    }

    public void setAllTimeLongestStreak(Player player, int day) {
        this.getConfig().set("streak", null);

        if (player != null) {
            this.getConfig().set("streak." + player.getUniqueId(), day);
        }

        this.save();
    }

    public int getAllTimeLongestStreakCount() {
        int longestStreak = 0;

        for (String key : this.getConfig().getConfigurationSection("streak").getKeys(false)) {
            longestStreak = this.getConfig().getInt("streak." + key);
        }

        return longestStreak;
    }

    public String getAllTimeLongestStreakPlayer() {
        String longestStreakUUID = "37819856-6e9a-4866-9a73-922c8c684b21";
        int longestStreak = 0;

        for (String key : this.getConfig().getConfigurationSection("streak").getKeys(false)) {
            int currentStreak = this.getConfig().getInt("streak." + key);
            if (currentStreak > longestStreak) {
                longestStreak = currentStreak;
                longestStreakUUID = key;
            }
        }

        return this.getPlayerNameFromUUID(longestStreakUUID);
    }

    public String getPlayerNameFromUUID(String uuid) {
        UUID playerUUID = UUID.fromString(uuid);
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);

        return offlinePlayer.getName() != null ? offlinePlayer.getName() : "Unknown";
    }


}

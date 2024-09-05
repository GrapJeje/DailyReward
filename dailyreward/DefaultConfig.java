package eu.camonetwork.dailyreward;

import eu.camonetwork.dailyreward.infrastructure.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class DefaultConfig extends ConfigManager {

    public DefaultConfig() {
        this.setFileName("config.yml");
    }

    /**
     * Checks if the daily rewards system is enabled.
     *
     * @return true if enabled, false otherwise
     */
    public boolean getIfEnabled() {
        return this.getConfig().getBoolean("enabled");
    }

    /**
     * Stel de reset-tijd in vanuit de config in de Calendar
     *
     * @param timeZone De gewenste tijdzone
     * @return een Calendar object met de ingestelde tijd
     */
    public Calendar getResetCalendar(TimeZone timeZone) {
        String resetTimeString = this.getConfig().getString("resettime", "00:00:00:000");
        assert resetTimeString != null;

        String[] timeParts = resetTimeString.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);
        int second = Integer.parseInt(timeParts[2]);
        int millisecond = Integer.parseInt(timeParts[3]);

        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, millisecond);

        return calendar;
    }

    /**
     * Retrieves a list of all players (online and offline) from the config.
     *
     * @return a list of OfflinePlayer objects representing all players
     */
    public List<OfflinePlayer> getAllPlayers() {
        ConfigurationSection playerSection = this.getConfig().getConfigurationSection("players");

        if (playerSection == null) return Collections.emptyList();

        List<OfflinePlayer> offlinePlayers = new ArrayList<>();

        for (String key : playerSection.getKeys(false)) {
            UUID playerUUID = UUID.fromString(key);
            offlinePlayers.add(Bukkit.getOfflinePlayer(playerUUID));
        }

        return offlinePlayers;
    }

    // --- Methods for handling online players ---

    public void setServerDay(Player player, int day) {
        this.setServerDay((OfflinePlayer) player, day);
    }

    public int getServerDay(Player player) {
        return this.getServerDay((OfflinePlayer) player);
    }

    public void setPlayerDay(Player player, int day) {
        this.setPlayerDay((OfflinePlayer) player, day);
    }

    public int getPlayerDay(Player player) {
        return this.getPlayerDay((OfflinePlayer) player);
    }

    public void setPlayerClaimed(Player player, boolean claimed) {
        this.setPlayerClaimed((OfflinePlayer) player, claimed);
    }

    public boolean getPlayerClaimed(Player player) {
        return this.getPlayerClaimed((OfflinePlayer) player);
    }

    public void setPlayerBought(Player player, boolean bought) {
        this.setPlayerBought((OfflinePlayer) player, bought);
    }

    public boolean getPlayerBought(Player player) {
        return this.getPlayerBought((OfflinePlayer) player);
    }

    public void setPlayerSteak(Player player, int streak) {
        this.setPlayerSteak((OfflinePlayer) player, streak);
    }

    public int getPlayerStreak(Player player) {
        return this.getPlayerStreak((OfflinePlayer) player);
    }

    public void setPlayerLongestStreak(Player player, int streak) {
        this.setPlayerLongestStreak((OfflinePlayer) player, streak);
    }

    public int getPlayerLongestStreak(Player player) {
        return this.getPlayerLongestStreak((OfflinePlayer) player);
    }

    public void setAllTimeLongestStreak(Player player, int day) {
        this.setAllTimeLongestStreak((OfflinePlayer) player, day);
    }

    // --- New overloaded methods for handling offline players ---

    /**
     * Sets the server day for an offline player.
     *
     * @param player the OfflinePlayer whose server day is being set
     * @param day    the day to set
     */
    public void setServerDay(OfflinePlayer player, int day) {
        this.getConfig().set("players." + player.getUniqueId() + ".serverday", day);
        this.save();
    }

    /**
     * Gets the server day for an offline player.
     *
     * @param player the OfflinePlayer whose server day is being retrieved
     * @return the server day
     */
    public int getServerDay(OfflinePlayer player) {
        return this.getConfig().getInt("players." + player.getUniqueId() + ".serverday", 1);
    }

    /**
     * Sets the player's daily reward day for an offline player.
     *
     * @param player the OfflinePlayer whose day is being set
     * @param day    the day to set
     */
    public void setPlayerDay(OfflinePlayer player, int day) {
        this.getConfig().set("players." + player.getUniqueId() + ".dag", day);
        this.save();
    }

    /**
     * Gets the player's daily reward day for an offline player.
     *
     * @param player the OfflinePlayer whose day is being retrieved
     * @return the player's daily reward day
     */
    public int getPlayerDay(OfflinePlayer player) {
        return this.getConfig().getInt("players." + player.getUniqueId() + ".dag", 0);
    }

    /**
     * Sets whether the player has claimed the reward for an offline player.
     *
     * @param player  the OfflinePlayer whose claimed status is being set
     * @param claimed whether the player has claimed the reward
     */
    public void setPlayerClaimed(OfflinePlayer player, boolean claimed) {
        this.getConfig().set("players." + player.getUniqueId() + ".claimed", claimed);
        this.save();
    }

    /**
     * Gets whether the player has claimed the reward for an offline player.
     *
     * @param player the OfflinePlayer whose claimed status is being retrieved
     * @return true if the player has claimed, false otherwise
     */
    public boolean getPlayerClaimed(OfflinePlayer player) {
        return this.getConfig().getBoolean("players." + player.getUniqueId() + ".claimed");
    }

    /**
     * Sets whether the player has bought the reward for an offline player.
     *
     * @param player the OfflinePlayer whose bought status is being set
     * @param bought whether the player has bought the reward
     */
    public void setPlayerBought(OfflinePlayer player, boolean bought) {
        this.getConfig().set("players." + player.getUniqueId() + ".bought", bought);
        this.save();
    }

    /**
     * Gets whether the player has bought the reward for an offline player.
     *
     * @param player the OfflinePlayer whose bought status is being retrieved
     * @return true if the player has bought, false otherwise
     */
    public boolean getPlayerBought(OfflinePlayer player) {
        return this.getConfig().getBoolean("players." + player.getUniqueId() + ".bought");
    }

    /**
     * Sets the player's streak for an offline player.
     *
     * @param player the OfflinePlayer whose streak is being set
     * @param day    the streak to set
     */
    public void setPlayerSteak(OfflinePlayer player, int day) {
        this.getConfig().set("players." + player.getUniqueId() + ".dag", day);
        this.save();
    }

    /**
     * Gets the player's streak for an offline player.
     *
     * @param player the OfflinePlayer whose streak is being retrieved
     * @return the player's streak
     */
    public int getPlayerStreak(OfflinePlayer player) {
        return this.getConfig().getInt("players." + player.getUniqueId() + ".dag", 0);
    }

    /**
     * Sets the player's longest streak for an offline player.
     *
     * @param player the OfflinePlayer whose longest streak is being set
     * @param day    the longest streak to set
     */
    public void setPlayerLongestStreak(OfflinePlayer player, int day) {
        this.getConfig().set("players." + player.getUniqueId() + ".streak", day);
        this.save();
    }

    /**
     * Gets the player's longest streak for an offline player.
     *
     * @param player the OfflinePlayer whose longest streak is being retrieved
     * @return the player's longest streak
     */
    public int getPlayerLongestStreak(OfflinePlayer player) {
        return this.getConfig().getInt("players." + player.getUniqueId() + ".streak");
    }

    /**
     * Sets the all-time longest streak for an offline player.
     *
     * @param player the OfflinePlayer whose all-time longest streak is being set
     * @param day    the all-time longest streak to set
     */
    public void setAllTimeLongestStreak(OfflinePlayer player, int day) {
        this.getConfig().set("streak", null);

        if (player != null) {
            this.getConfig().set("streak." + player.getUniqueId(), day);
        }

        this.save();
    }

    // --- Other utility methods ---

    /**
     * Gets the count of the longest all-time streak.
     *
     * @return the count of the longest streak
     */
    public int getAllTimeLongestStreakCount() {
        int longestStreak = 0;

        ConfigurationSection streakSection = this.getConfig().getConfigurationSection("streak");
        if (streakSection == null) {
            return longestStreak; // return 0 if the streak section is missing
        }

        for (String key : streakSection.getKeys(false)) {
            int currentStreak = this.getConfig().getInt("streak." + key);
            if (currentStreak > longestStreak) {
                longestStreak = currentStreak;
            }
        }

        return longestStreak;
    }

    /**
     * Gets the name of the player with the longest all-time streak.
     *
     * @return the name of the player
     */
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

    /**
     * Converts a player's UUID to their name.
     *
     * @param uuid the UUID of the player
     * @return the player's name
     */
    public String getPlayerNameFromUUID(String uuid) {
        UUID playerUUID = UUID.fromString(uuid);
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);

        return offlinePlayer.getName() != null ? offlinePlayer.getName() : "Unknown";
    }

}

package eu.camonetwork.dailyreward;

import eu.camonetwork.dailyreward.infrastructure.ConfigManager;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ItemConfig extends ConfigManager {

    public ItemConfig() {
        this.setFileName("items.yml");
    }

    /**
     * Retrieves the list of rewards configured for a specific day.
     *
     * @param day The day for which to retrieve the rewards.
     * @return A list of strings representing the rewards for the specified day.
     *         Returns an empty list if no rewards are found for the given day.
     */
    public List<String> getRewardsForDay(int day) {
        if (this.getConfig() == null || !this.getConfig().contains("items.day" + day + ".rewards")) {
            Bukkit.getLogger().warning("No reward configuration found for day " + day);
            return new ArrayList<>();
        }

        return (List<String>) this.getConfig().get("items.day" + day + ".rewards");
    }

    /**
     * Retrieves the list of lores configured for a specific day.
     *
     * @param day The day for which to retrieve the lores.
     * @return A list of strings representing the lores for the specified day.
     *         Returns an empty list if no lores are found for the given day.
     */
    public List<String> getLoresForDay(int day) {
        if (this.getConfig() == null || !this.getConfig().contains("items.day" + day + ".lores")) {
            Bukkit.getLogger().warning("No lores configuration found for day " + day);
            return new ArrayList<>();
        }

        return (List<String>) this.getConfig().get("items.day" + day + ".lores");
    }

    /**
     * Determines the maximum day number configured in the configuration file.
     *
     * @return The highest day number found in the configuration. Returns 0 if no days are configured.
     */
    public int getMaxDay() {
        if (this.getConfig() == null || !this.getConfig().contains("items")) {
            Bukkit.getLogger().warning("No configuration found for items.");
            return 0;
        }

        Set<String> keys = this.getConfig().getConfigurationSection("items").getKeys(false);
        int maxDay = 0;

        for (String key : keys) {
            if (key.startsWith("day")) {
                try {
                    int dayNumber = Integer.parseInt(key.substring(3));
                    if (dayNumber > maxDay) {
                        maxDay = dayNumber;
                    }
                } catch (NumberFormatException e) {
                    Bukkit.getLogger().warning("Invalid day format in configuration: " + key);
                }
            }
        }

        return maxDay;
    }

    /**
     * Checks if the "key" setting is present and true for a specific day.
     *
     * @param day The day for which to check the "key" setting.
     * @return {@code true} if the "key" setting is present and true for the specified day;
     *         {@code false} otherwise.
     */
    public boolean getIfKey(int day) {
        if (this.getConfig() == null || !this.getConfig().contains("items")) {
            Bukkit.getLogger().warning("No configuration found for items.");
            return false;
        }

        return this.getConfig().getBoolean("items.day" + day + ".key");
    }

}

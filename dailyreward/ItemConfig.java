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
     * Retrieves the rewards for a specific day.
     * @param day The day for which the rewards should be retrieved.
     * @return A list of strings containing the rewards, or an empty list if no rewards are found.
     */
    public List<String> getRewardsForDay(int day) {
        if (this.getConfig() == null || !this.getConfig().contains("items.day" + day + ".rewards")) {
            Bukkit.getLogger().warning("No reward configuration found for day " + day);
            return new ArrayList<>();
        }

        return (List<String>) this.getConfig().get("items.day" + day + ".rewards");
    }

    /**
     * Retrieves all lores for a specific day.
     * @param day The day for which the lores should be retrieved.
     * @return A list of strings containing the lores, or an empty list if no lores are found.
     */
    public List<String> getLoresForDay(int day) {
        if (this.getConfig() == null || !this.getConfig().contains("items.day" + day + ".lores")) {
            Bukkit.getLogger().warning("No lores configuration found for day " + day);
            return new ArrayList<>();
        }

        return (List<String>) this.getConfig().get("items.day" + day + ".lores");
    }

    /**
     * Determines the maximum day number available in the configuration.
     * @return The maximum day number, or 0 if no days are configured.
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

    public boolean getIfKey(int day) {
        if (this.getConfig() == null || !this.getConfig().contains("items")) {
            Bukkit.getLogger().warning("No configuration found for items.");
            return false;
        }

        return this.getConfig().getBoolean("items.day" + day + ".key");
    }

}

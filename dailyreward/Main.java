package eu.camonetwork.dailyreward;

import eu.camonetwork.dailyreward.infrastructure.ConfigManager;
import eu.camonetwork.dailyreward.infrastructure.RewardItems;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Main extends JavaPlugin {
    public static Main instance;
    public static List<ConfigManager> configList = new ArrayList<>();
    public static DefaultConfig defaultConfig = new DefaultConfig();
    public static RewardItems rewardItems = new RewardItems();
    private void registerCommand(String cmdName, CommandExecutor executor, TabCompleter tabCompleter) {
        PluginCommand command = getServer().getPluginCommand(cmdName);
        if (command != null) {
            command.setExecutor(executor);
            if (tabCompleter != null) {
                command.setTabCompleter(tabCompleter);
            }
        }
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        configList.add(defaultConfig);

        configList.forEach(ConfigManager::reload);

        // Commands
        registerCommand("dailyrewards", new DailyRewardCommand(), null);

        getLogger().info("---DailyRewards loaded!---");
    }

    @Override
    public void onDisable() {
        getLogger().info("---DailyRewards unloaded---");
    }
}

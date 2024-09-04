package eu.camonetwork.dailyreward;

import eu.camonetwork.dailyreward.infrastructure.ConfigManager;
import eu.camonetwork.dailyreward.infrastructure.RewardItems;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import plus.crates.CratesPlus;

import java.util.ArrayList;
import java.util.List;

public final class Main extends JavaPlugin {
    public static Main instance;
    public static List<ConfigManager> configList = new ArrayList<>();
    public static DefaultConfig defaultConfig = new DefaultConfig();
    public static ItemConfig itemConfig = new ItemConfig();
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
    public void registerEventListener(Listener eventListener) {
        Main.instance.getServer().getPluginManager().registerEvents(eventListener, Main.instance);
    }
    public static PlayerPoints pp = (PlayerPoints) Bukkit.getServer().getPluginManager().getPlugin("PlayerPoints");
    public static CratesPlus cp = (CratesPlus) Bukkit.getServer().getPluginManager().getPlugin("CratesPlus");

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        configList.add(defaultConfig);
        configList.add(itemConfig);

        configList.forEach(ConfigManager::reload);

        // Commands
        registerCommand("dailyrewards", new DailyRewardCommand(), null);

        // Listeners
        registerEventListener(new DailyRewardListener());

        getLogger().info("---DailyRewards loaded!---");
    }

    @Override
    public void onDisable() {
        getLogger().info("---DailyRewards unloaded---");
    }
}

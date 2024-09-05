package eu.camonetwork.dailyreward;

import eu.camonetwork.dailyreward.infrastructure.ConfigManager;
import eu.camonetwork.dailyreward.infrastructure.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class DailyRewardCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (args.length == 1 && Objects.equals(args[0], "reload")) {
            this.reloadConfig(player);
            return true;
        }

        if (args.length == 1 && Objects.equals(args[0], "info")) {
            this.infoCommand(player);
            return true;
        }

        if (!player.hasPermission("camo.dailyrewards.open")) return false;

        if (!Main.defaultConfig.getIfEnabled()) {
            player.sendMessage(Text.colorize("&cDe daily rewards zijn momenteel uitgeschakeld. Contacteer een admin als je denkt dat dit een fout is."));
            return false;
        }

        DailyRewardMenu menu = new DailyRewardMenu();
        menu.open(player);

        return true;
    }

    private void infoCommand(Player player) {
        player.sendMessage("");
        player.sendMessage(Text.colorize("&b&lDailyRewards &8> &7Versie " + Main.instance.getDescription().getVersion()));
        player.sendMessage(Text.colorize("&b&lAuthor &8> &7GrapJeje"));
        player.sendMessage("");
        player.sendMessage(Text.colorize("&6This plugin is custom made for &3CamoNetwork"));
        player.sendMessage("");
        player.sendMessage(Text.colorize("&3/&cDaily &3- Open the Daily Reward menu."));
        player.sendMessage(Text.colorize("&3/&CDaily info &3- Show this info menu."));
        if (player.hasPermission("camo.admin.reload")) player.sendMessage(Text.colorize("&3/&cDaily reload &3- Reload the Daily Reward Config."));
        if (player.hasPermission("camo.admin.reload")) player.sendMessage(Text.colorize("&cGithub info &3- https://github.com/GrapJeje/DailyReward"));
        player.sendMessage("");
    }

    private void reloadConfig(Player player) {
        if (!player.hasPermission("camo.admin.reload")) return;

        Main.configList.forEach(ConfigManager::reload);
        Main.configList.stream().map(ConfigManager::getFileName)
                .forEach(x ->
                        Bukkit.getLogger().info(String.format("Reloaded %s", x))
                );

        player.sendMessage(Text.colorize("&cConfig's successfully reloaded"));
    }
}

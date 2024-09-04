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

        if (!Main.defaultConfig.getIfEnabled()) {
            player.sendMessage(Text.colorize("&cDe daily rewards zijn momenteel uitgeschakeld. Contacteer een admin als je denkt dat dit een fout is."));
            return false;
        }

        if (args.length == 1 && Objects.equals(args[0], "reload")) {
            this.reloadConfig(player);
            return false;
        }

        DailyRewardMenu menu = new DailyRewardMenu();
        menu.open(player);

        return true;
    }

    private void reloadConfig(Player player) {
        if (!player.hasPermission("camo.admin.reload")) {
            return;
        }

        Main.configList.forEach(ConfigManager::reload);
        Main.configList.stream().map(ConfigManager::getFileName)
                .forEach(x ->
                        Bukkit.getLogger().info(String.format("Reloaded %s", x))
                );

        player.sendMessage(Text.colorize("&cConfig's successfully reloaded"));
    }
}

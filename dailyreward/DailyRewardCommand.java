package eu.camonetwork.dailyreward;

import eu.camonetwork.dailyreward.infrastructure.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DailyRewardCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (!Main.defaultConfig.getIfEnabled()) {
            player.sendMessage(Text.colorize("&cDe daily rewards zijn momenteel uitgeschakeld. Contacteer een admin als je denkt dat dit een fout is."));
            return false;
        }

        DailyRewardMenu menu = new DailyRewardMenu();
        menu.open(player);

        return true;
    }
}

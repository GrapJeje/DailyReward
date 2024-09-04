package eu.camonetwork.dailyreward;

import eu.camonetwork.dailyreward.infrastructure.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class DailyRewardListener implements Listener {

    public DailyRewardListener() {
        this.scheduleDailyMidnightTask();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        int dayCount = Main.defaultConfig.getPlayerDay(player);
        int serverDay = Main.defaultConfig.getServerDay(player);

        if (dayCount != serverDay) {
            player.sendMessage(Text.colorize("&3Je hebt een daily reward voor je klaarliggen! &c/daily&3."));
        }
    }


    private void scheduleDailyMidnightTask() {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Amsterdam");

        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date midnight = calendar.getTime();
        if (midnight.before(new Date())) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            midnight = calendar.getTime();
        }

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Bukkit.getScheduler().runTask(Main.instance, () -> {

                    for (OfflinePlayer player : Main.defaultConfig.getAllPlayers()) {
                        Player onlinePlayer = (Player) player;

                        int dayCount = Main.defaultConfig.getPlayerDay(onlinePlayer);
                        int serverDay = Main.defaultConfig.getServerDay(onlinePlayer);

                        if (dayCount == serverDay) {
                            Main.defaultConfig.setServerDay(onlinePlayer, serverDay + 1);
                        } else {
                            Main.defaultConfig.setPlayerSteak(onlinePlayer, 0);
                            Main.defaultConfig.setPlayerDay(onlinePlayer, 0);
                            Main.defaultConfig.setServerDay(onlinePlayer, 1);
                        }

                        Main.defaultConfig.setPlayerClaimed(onlinePlayer, false);
                        Main.defaultConfig.setPlayerBought(onlinePlayer, false);
                    }

                    for (Player onlineplayer : Bukkit.getOnlinePlayers()) {
                        onlineplayer.sendMessage(Text.colorize("&3De daily rewards zijn gereset!"));
                        onlineplayer.playSound(onlineplayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                    }
                });
            }
        }, midnight, 24 * 60 * 60 * 1000);
    }
}

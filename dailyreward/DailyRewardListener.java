package eu.camonetwork.dailyreward;

import eu.camonetwork.dailyreward.infrastructure.Text;
import org.bukkit.Bukkit;
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

        Calendar calendar = Main.defaultConfig.getResetCalendar(timeZone);
        Bukkit.getLogger().info(Text.colorize("&aDaily Rewards reset scheduled for: " + calendar.getTime()));

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
                        int dayCount = Main.defaultConfig.getPlayerDay(player);
                        int serverDay = Main.defaultConfig.getServerDay(player);

                        if (dayCount == serverDay) {
                            Main.defaultConfig.setServerDay(player, serverDay + 1);
                        } else {
                            Main.defaultConfig.setPlayerSteak(player, 0);
                            Main.defaultConfig.setPlayerDay(player, 0);
                            Main.defaultConfig.setServerDay(player, 1);
                        }

                        Main.defaultConfig.setPlayerClaimed(player, false);
                        Main.defaultConfig.setPlayerBought(player, false);
                    }

                    Bukkit.getLogger().info(Text.colorize("&aDailyRewards updaten...."));

                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        onlinePlayer.sendMessage(Text.colorize("&3De daily rewards zijn gereset!"));
                        onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                    }
                });
            }
        }, midnight, 24 * 60 * 60 * 1000);
    }
}

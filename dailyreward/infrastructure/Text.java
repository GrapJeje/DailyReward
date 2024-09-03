package eu.camonetwork.dailyreward.infrastructure;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Text {
    public static @NotNull String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> splitLines(String description) {
        StringBuilder result = new StringBuilder();
        String[] lines = description.split("\n");
        for (String line : lines) {
            int i = 0;
            while (i < line.length()) {
                int endIndex = Math.min(i + 30, line.length());
                result.append(line, i, endIndex).append("\n");
                i += 30;
            }
        }
        return Arrays.stream(result.toString().split("\n")).map(x -> "&7" + x).collect(Collectors.toList());
    }

    public static int colorizeToRGB(String color) {
        if (color.startsWith("#")) {
            return Integer.parseInt(color.substring(1), 16);
        } else {
            throw new IllegalArgumentException("Invalid color format. Expected hex code.");
        }
    }

    public static String strip(String text) {
        return ChatColor.stripColor(text);
    }
}

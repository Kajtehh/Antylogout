package pl.kajteh.antylogout.util;

import org.bukkit.ChatColor;

public class BukkitColorUtil {

    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text); // TODO ADD HEX COLOR SUPPORT THERE
    }
}

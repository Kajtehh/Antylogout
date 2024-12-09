package pl.kajteh.antylogout.message;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Message {

    private final MessageType type;
    private final String content;

    public Message(MessageType type, String content) {
        this.type = type;
        this.content = ChatColor.translateAlternateColorCodes('&', content); // TODO: ADD HEX COLOR HERE TOO
    }

    public void send(Player player) {
        switch (this.type) {
            case CHAT:
                player.sendMessage(this.content);
                break;
            case ACTION_BAR:
                player.sendActionBar(this.content);
                break;
        }
    }
}

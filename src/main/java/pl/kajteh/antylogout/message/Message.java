package pl.kajteh.antylogout.message;

import com.destroystokyo.paper.Title;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Map;

public class Message {

    private final MessageType type;
    private final String content;

    public Message(MessageType type, String content) {
        this.type = type;
        this.content = content;
    }

    public Message(Map<String, Object> map) {
        this(MessageType.valueOf((String) map.get("type")), (String) map.get("content"));
    }

    private String replacePlaceholder(String content, String key, Object value) {
        return content.replace(String.format("{%s}", key), value.toString());
    }

    private String[] processMessageContent(String content, Map<String, Object> replacements) {
        if (replacements != null && !replacements.isEmpty()) {
            for (Map.Entry<String, Object> entry : replacements.entrySet()) {
                content = this.replacePlaceholder(content, entry.getKey(), entry.getValue());
            }
        }

        return content.split("\n");
    }

    public void send(Player player, Map<String, Object> replacements) {
        final String[] messageContent = this.processMessageContent(ChatColor.translateAlternateColorCodes('&', this.content), replacements);

        switch (this.type) {
            case CHAT:
                player.sendMessage(messageContent);
                break;
            case ACTION_BAR:
                player.sendActionBar(messageContent[0]);
                break;
            case TITLE:
                final String title = messageContent.length > 0 ? messageContent[0] : "";
                final String subtitle = messageContent.length > 1 ? messageContent[1] : "";

                player.sendTitle(new Title(title, subtitle));
                break;
            default:
                throw new UnsupportedOperationException("Unsupported message type: " + this.type);
        }
    }

    public void send(Player player, String placeholderKey, Object placeholderValue) {
        this.send(player, Collections.singletonMap(placeholderKey, placeholderValue));
    }

    public void send(Player player) {
        this.send(player, null);
    }
}
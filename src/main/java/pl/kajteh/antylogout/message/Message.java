package pl.kajteh.antylogout.message;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kajteh.antylogout.util.BukkitColorUtil;
import pl.kajteh.antylogout.util.PlaceholderUtil;

import java.util.Collections;
import java.util.Map;

public class Message {

    private final MessageType type;
    private final String content;

    public Message(String content) {
        this.type = MessageType.CHAT;
        this.content = content;
    }

    public Message(MessageType type, String content) {
        this.type = type;
        this.content = content;
    }

    public Message(Map<String, Object> map) {
        this(MessageType.valueOf((String) map.get("type")), (String) map.get("content"));
    }

    private String[] processMessageContent(String content, Map<String, Object> replacements) {
        if (replacements != null && !replacements.isEmpty()) {
            content = PlaceholderUtil.replacePlaceholder(content, replacements);
        }

        return content.split("\n");
    }

    public void send(CommandSender sender, Map<String, Object> replacements) {
        if (this.type == null || this.content == null) {
            new Message("&4l&lANTYLOGOUT &8&l| &cAn error occurred while sending the message. Please check the console for details.");
            return;
        }

        final String[] messageContent = this.processMessageContent(BukkitColorUtil.colorize(this.content), replacements);

        switch (this.type) {
            case CHAT:
                sender.sendMessage(messageContent);
                break;
            case ACTION_BAR:
                if(!(sender instanceof Player)) {
                    return;
                }

                ((Player) sender).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(messageContent[0]));
                break;
            case TITLE:
                if(!(sender instanceof Player)) {
                    return;
                }
                final String title = messageContent.length > 0 ? messageContent[0].trim() : "";
                final String subtitle = messageContent.length > 1 ? messageContent[1].trim() : "";

                ((Player) sender).sendTitle(title, subtitle, 10, 70, 20);
                break;
            case NONE:
                break;
            default:
                throw new UnsupportedOperationException("Unsupported message type: " + this.type);
        }
    }

    public void send(CommandSender sender, String placeholderKey, Object placeholderValue) {
        this.send(sender, Collections.singletonMap(placeholderKey, placeholderValue));
    }

    public void send(CommandSender sender) {
        this.send(sender, null);
    }

    public void send(Player player, String placeholderKey, Object placeholderValue) {
        this.send(player, Collections.singletonMap(placeholderKey, placeholderValue));
    }

    public void send(Player player) {
        this.send(player, null);
    }
}

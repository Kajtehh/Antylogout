package pl.kajteh.antylogout.config;

import org.bukkit.configuration.file.FileConfiguration;
import pl.kajteh.antylogout.CombatPlugin;
import pl.kajteh.antylogout.message.Message;

import java.util.*;

public class CombatConfigImpl implements CombatConfig{

    private final FileConfiguration configuration;

    public CombatConfigImpl(CombatPlugin plugin) {
        plugin.saveDefaultConfig();

        this.configuration = plugin.getConfig();
    }

    @Override
    public String getCombatBypassPermission() {
        return this.configuration.getString("combat-bypass-permission");
    }

    @Override
    public long getCombatDuration() {
        return this.configuration.getLong("combat-duration");
    }

    @Override
    public Message getCombatMessage() {
        return new Message(this.configuration.getConfigurationSection("combat-message").getValues(false));
    }

    @Override
    public Set<Message> getCombatEndMessages() {
        final Set<Message> messages = new HashSet<>();

        @SuppressWarnings("unchecked")
        final List<Map<String, Object>> rawMessages = (List<Map<String, Object>>) this.configuration.getList("combat-end-messages");

        rawMessages.forEach(rawMessage -> messages.add(new Message(rawMessage)));

        return messages;
    }


    @Override
    public boolean isCombatFromMobs() {
        return this.configuration.getBoolean("combat-from-mobs");
    }

    @Override
    public boolean isCombatFromProjectiles() {
        return this.configuration.getBoolean("combat-from-projectiles");
    }

    @Override
    public Set<String> getCombatBlockedRegions() {
        return new HashSet<>(this.configuration.getStringList("combat-blocked-regions"));
    }
}

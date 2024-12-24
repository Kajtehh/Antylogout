package pl.kajteh.antylogout.config;

import org.bukkit.configuration.file.FileConfiguration;
import pl.kajteh.antylogout.CombatPlugin;
import pl.kajteh.antylogout.message.Message;

import java.util.*;
import java.util.stream.Collectors;

public class CombatConfigImpl implements CombatConfig{

    private final CombatPlugin plugin;
    private FileConfiguration configuration;

    public CombatConfigImpl(CombatPlugin plugin) {
        this.plugin = plugin;
        
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
    public boolean isCombatStartNotificationsEnabled() {
        return this.configuration.getBoolean("combat-start-notifications-enabled");
    }

    @Override
    public Message getCombatStartMessageAttacker() {
        return this.getMessage("combat-start-message-attacker");
    }

    @Override
    public Message getCombatStartMessageVictim() {
        return this.getMessage("combat-start-message-victim");
    }

    @Override
    public Message getCombatMessage() {
        return this.getMessage("combat-message");
    }

    @Override
    public Set<Message> getCombatEndMessages() {
        return this.getMessages("combat-end-message");
    }

    /*
    @Override
    public boolean isCombatBossBarEnabled() {
        return this.configuration.getBoolean("combat-boss-bar-enabled");
    }

    @Override
    public CombatBossBar getCombatBossBar() {
        return this.getBossBar("combat-boss-bar");
    }*/

    @Override
    public boolean isRemoveCombatOnOpponentDeath() {
        return this.configuration.getBoolean("remove-combat-on-opponent-death");
    }

    @Override
    public Set<Message> getRemoveCombatMessage() {
        return this.getMessages("remove-combat-message");
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
    public boolean isCommandsBlockedDuringCombat() {
        return this.configuration.getBoolean("commands-blocked-during-combat");
    }

    @Override
    public Set<String> getCombatCommandWhitelist() {
        return new HashSet<>(this.configuration.getStringList("combat-command-whitelist"));
    }

    @Override
    public Set<Message> getCombatCommandBlockedMessage() {
        return this.getMessages("combat-command-blocked-message");
    }

    @Override
    public Set<String> getCombatBlockedRegions() {
        return new HashSet<>(this.configuration.getStringList("combat-blocked-regions"));
    }

    @Override
    public double getCombatBlockedRegionKnockbackMultiplier() {
        return this.configuration.getDouble("combat-blocked-region-knockback-multiplier");
    }

    @Override
    public Set<Message> getCombatBlockedRegionEnterMessage() {
        return this.getMessages("combat-blocked-region-enter-message");
    }

    @Override
    public void save() {
        this.plugin.saveConfig();
    }

    @Override
    public void reload() {
        this.plugin.reloadConfig();
        this.configuration = this.plugin.getConfig();
    }

    private Message getMessage(String path) {
        return new Message(Objects.requireNonNull(this.configuration.getConfigurationSection(path)).getValues(false));
    }

    private Set<Message> getMessages(String path) {
        @SuppressWarnings("unchecked")
        final List<Map<String, Object>> rawMessages = (List<Map<String, Object>>) this.configuration.getList(path);

        assert rawMessages != null;
        return rawMessages.stream()
                .map(Message::new)
                .collect(Collectors.toSet());
    }

}

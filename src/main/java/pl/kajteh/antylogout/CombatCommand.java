package pl.kajteh.antylogout;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import pl.kajteh.antylogout.config.CombatConfig;
import pl.kajteh.antylogout.message.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombatCommand implements CommandExecutor, TabCompleter {

    private final CombatConfig combatConfig;
    private final CombatCache combatCache;

    private static final String ADMIN_PERMISSION = "antylogout.admin";

    private static final Message NO_PERMISSION_MESSAGE = new Message("&4&lANTYLOGOUT &8&l| &7By &f&nKajteh");
    private static final Message USAGE_MESSAGE = new Message("&7Usage: &f/antylogout {usage}");
    private static final Message RELOAD_MESSAGE = new Message("&aReloaded Antylogout's configuration file!");
    private static final Message ARGS_NOT_FOUND_MESSAGE = new Message("&cCommand not found. &cInvalid args &4{args}&c!");
    private static final Message TARGET_NOT_FOUND_MESSAGE = new Message("&cTarget &4{target}&c was not found.");
    private static final Message COMBAT_ADD_MESSAGE = new Message("&cCombat added to &4{player}&c!");
    private static final Message COMBAT_REMOVE_MESSAGE = new Message("&cCombat added to &4{player}&c!");
    private static final Message TARGET_IS_NOT_IN_COMBAT = new Message("&cTarget isn't in combat.");

    public CombatCommand(CombatConfig combatConfig, CombatCache combatCache) {
        this.combatConfig = combatConfig;
        this.combatCache = combatCache;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission(ADMIN_PERMISSION)) {
            NO_PERMISSION_MESSAGE.send(sender);
            return false;
        }

        if(args.length < 1) {
            USAGE_MESSAGE.send(sender, "usage", "reload/add/remove");
            return true;
        }

        if(args[0].equalsIgnoreCase("reload")) {
            this.combatConfig.reload();

            RELOAD_MESSAGE.send(sender);
            return true;
        }

        if(args[0].equalsIgnoreCase("add")) {
            if(args.length < 2) {
                USAGE_MESSAGE.send(sender, "usage", "add {player}");
                return true;
            }

            final Player target = Bukkit.getPlayerExact(args[1]);

            if(target == null || !target.isOnline()) {
                TARGET_NOT_FOUND_MESSAGE.send(sender);
                return true;
            }

            this.combatCache.addCombat(target.getUniqueId(), this.combatConfig.getCombatDuration());

            COMBAT_ADD_MESSAGE.send(sender, "player", target.getName());
            return true;
        }

        if(args[0].equalsIgnoreCase("remove")) {
            if(args.length < 2) {
                USAGE_MESSAGE.send(sender, "usage", "remove {player}");
                return true;
            }

            final Player target = Bukkit.getPlayerExact(args[1]);

            if(target == null || !target.isOnline()) {
                TARGET_NOT_FOUND_MESSAGE.send(sender);
                return true;
            }

            if(!this.combatCache.getCombat(target.getUniqueId()).isPresent()) {
                TARGET_IS_NOT_IN_COMBAT.send(sender);
                return true;
            }

            this.combatCache.removeCombat(target.getUniqueId());

            COMBAT_REMOVE_MESSAGE.send(sender, "player", target.getName());
            return true;
        }

        ARGS_NOT_FOUND_MESSAGE.send(sender, "args", Arrays.toString(args));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission(ADMIN_PERMISSION)) {
            return new ArrayList<>();
        }

        final List<String> completions = new ArrayList<>();

        if(args.length == 1) {
            completions.add("reload");
            completions.add("add");
            completions.add("remove");
        }

        if(args.length == 2 && args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")) {
            Bukkit.getOnlinePlayers().forEach(player -> completions.add(player.getName()));
        }

        return completions;
    }
}

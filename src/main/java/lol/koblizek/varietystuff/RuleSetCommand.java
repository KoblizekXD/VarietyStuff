package lol.koblizek.varietystuff;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class RuleSetCommand implements CommandExecutor {

    public static final Map<String, Boolean> RULES = new HashMap<>() {{
        put("spectatorRespawn", false);
        put("badlandsSpectatorRespawn", false);
    }};

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Rules: " + RULES.keySet().toString());
        } else if (args.length == 1) {
            String rule = args[0];
            // Get the rule value
            if (RULES.containsKey(rule)) {
                sender.sendMessage(rule + " is: " + RULES.get(rule));
            } else {
                sender.sendMessage("Rule not found");
            }
        } else if (args.length == 2) {
            String rule = args[0];
            String value = args[1];
            // Set the rule value
            if (RULES.containsKey(rule)) {
                boolean boolValue = Boolean.parseBoolean(value);
                RULES.put(rule, boolValue);
                sender.sendMessage("Set " + rule + " to " + boolValue);
            } else {
                sender.sendMessage("Rule not found");
            }
        } else {
            sender.sendMessage("Invalid number of arguments");
        }
        return true;
    }
}

package dev.allenalt.authyz.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoginCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        // Check if the player is already logged in
        // if (AuthManager.isLoggedIn(player)) { ... }

        // Command usage: /login <password>
        if (args.length < 1) {
            player.sendMessage("§cUsage: /login <password>");
            return false;
        }

        String password = args[0];

        // Authenticate the player
        // boolean success = AuthManager.loginPlayer(player, password);
        boolean success = true; // Placeholder for your logic

        if (success) {
            player.sendMessage("§aYou have successfully logged in!");
            // Teleport player out of limbo, remove blindness, etc.
        } else {
            // Handle incorrect password, update attempts, and kick if necessary
            player.sendMessage("§cWrong password!");
        }

        return true;
    }
}

package dev.allenalt.authyz.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegisterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if the command is being run by a player
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        // Here you would check if the player is already logged in or registered
        // For example: if (AuthManager.isRegistered(player)) { ... }

        // Command usage: /register <password> <repeat password>
        if (args.length < 2) {
            // Send usage message from messages.yml
            player.sendMessage("§cUsage: /register <password> <repeat password>");
            return false;
        }

        String password = args[0];
        String repeatPassword = args[1];

        // Check if passwords match
        if (!password.equals(repeatPassword)) {
            // Send "incorrect-password" message from messages.yml
            player.sendMessage("§cPasswords do not match!");
            return true;
        }

        // Add logic for password length and strength from config.yml
        // For example: if (password.length() < config.getInt("main.min-password-length")) { ... }

        // If everything is correct, register the player
        // This is where you would hash the password and save it to the database
        // AuthManager.registerPlayer(player, password);

        player.sendMessage("§aYou have been successfully registered!");
        // You can also send the titles from messages.yml here

        return true;
    }
}

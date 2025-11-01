package dev.allenalt.authyz;

import dev.allenalt.authyz.commands.LoginCommand;
import dev.allenalt.authyz.commands.RegisterCommand;
import dev.allenalt.authyz.listeners.PlayerJoinListener;
import org.bukkit.plugin.java.JavaPlugin;

public class AuthyZ extends JavaPlugin {

    private static AuthyZ instance;

    @Override
    public void onEnable() {
        instance = this;

        // Load configurations
        saveDefaultConfig();
        // You would also create a file manager for messages.yml here

        getLogger().info("AuthyZ has been enabled!");

        // Register Commands
        registerCommands();

        // Register Event Listeners
        registerListeners();
    }

    @Override
    public void onDisable() {
        getLogger().info("AuthyZ has been disabled.");
    }

    private void registerCommands() {
        // Registering each command to its respective class
        getCommand("login").setExecutor(new LoginCommand());
        getCommand("register").setExecutor(new RegisterCommand());
        // You would add changepassword and unregister here as well
    }

    private void registerListeners() {
        // Registering the listener that handles player join events
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    public static AuthyZ getInstance() {
        return instance;
    }
}

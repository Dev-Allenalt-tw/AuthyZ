package dev.allenalt.authyz.listeners;

import dev.allenalt.authyz.AuthyZ;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoinListener implements Listener {

    private final AuthyZ plugin;

    // A manager to handle player authentication status (hypothetical, you'd need to create this)
    // private final AuthManager authManager;

    public PlayerJoinListener() {
        this.plugin = AuthyZ.getInstance();
        // this.authManager = plugin.getAuthManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // --- Premium & Bedrock Auto-Login Logic (Placeholder) ---
        // In a real scenario, you'd check Mojang sessions or Floodgate API here.
        // For example: if (isPremium(player) && !config.getBoolean("main.online-mode-need-auth")) {
        //     player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.premium-login")));
        //     return; // Player is authenticated, so we stop here.
        // }

        // For now, we assume everyone needs to authenticate to demonstrate the flow.

        // Check if the player is registered (this would check your database)
        boolean isRegistered = false; // Placeholder: Replace with `authManager.isRegistered(player.getUniqueId())`

        if (isRegistered) {
            // Player is registered, so they need to log in.
            preparePlayerForAuth(player, "login");
        } else {
            // Player is not registered, they need to register.
            preparePlayerForAuth(player, "register");
        }
    }

    /**
     * Puts a player into the "limbo" state, awaiting authentication.
     * @param player The player to prepare.
     * @param authType "login" or "register".
     */
    private void preparePlayerForAuth(Player player, String authType) {
        // --- 1. Handle Limbo Teleportation ---
        if (plugin.getConfig().getBoolean("limbo.enable-limbo")) {
            // Save player's original location to teleport them back after login
            // You would store this in a Map, e.g., Map<UUID, Location> originalLocations;
            // originalLocations.put(player.getUniqueId(), player.getLocation());

            final String worldName = plugin.getConfig().getString("limbo.limbo-world", "world");
            if (Bukkit.getWorld(worldName) != null) {
                Location limboLoc = new Location(
                        Bukkit.getWorld(worldName),
                        plugin.getConfig().getDouble("limbo.spawn-location.x"),
                        plugin.getConfig().getDouble("limbo.spawn-location.y"),
                        plugin.getConfig().getDouble("limbo.spawn-location.z")
                );
                player.teleport(limboLoc);
            } else {
                plugin.getLogger().warning("Limbo world '" + worldName + "' not found! Cannot teleport player.");
            }
        }

        // --- 2. Apply Limbo Effects ---
        player.setGameMode(GameMode.ADVENTURE); // Prevents breaking blocks
        if (plugin.getConfig().getBoolean("limbo.apply-blindness")) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 255, false, false));
        }

        // --- 3. Send Messages and Titles ---
        String messageKey = authType + "-message";
        String titleKey = authType + "-title";
        String subtitleKey = authType + "-subtitle";

        // You'd have a message manager to handle this cleanly
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages." + messageKey, "Please authenticate!")));
        player.sendTitle(
            ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages." + titleKey, "")),
            ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages." + subtitleKey, "")),
            10, 70, 20 // fade-in, stay, fade-out
        );

        // --- 4. Display Boss Bar ---
        if (plugin.getConfig().getBoolean("main.enable-bossbar")) {
            createAuthBossBar(player, authType);
        }

        // --- 5. Start Authentication Timeout ---
        long authTime = plugin.getConfig().getLong("main.auth-time", 60000);
        new BukkitRunnable() {
            @Override
            public void run() {
                // You would need to check if the player is still online and not yet authenticated
                // For example: if (player.isOnline() && !authManager.isLoggedIn(player.getUniqueId())) {
                if (player.isOnline()) { // Simplified check
                    player.kickPlayer(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.login-timeout", "You took too long to authenticate.")));
                }
            }
        }.runTaskLater(plugin, (authTime / 1000) * 20); // Convert milliseconds to server ticks
    }

    private void createAuthBossBar(Player player, String authType) {
        String title = (authType.equals("login")) ? "Please login using /login" : "Please register using /register";
        
        BarColor color;
        try {
            color = BarColor.valueOf(plugin.getConfig().getString("main.bossbar-color", "RED"));
        } catch (IllegalArgumentException e) {
            color = BarColor.RED;
        }

        BarStyle style;
        try {
            style = BarStyle.valueOf(plugin.getConfig().getString("main.bossbar-overlay", "PROGRESS"));
        } catch (IllegalArgumentException e) {
            style = BarStyle.PROGRESS;
        }

        BossBar bossBar = Bukkit.createBossBar(ChatColor.RED + title, color, style);
        bossBar.addPlayer(player);
        
        // You would store this boss bar in a Map to remove it upon successful login/logout
        // For example: Map<UUID, BossBar> playerBossBars;
        // playerBossBars.put(player.getUniqueId(), bossBar);
    }
}

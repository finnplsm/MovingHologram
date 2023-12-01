package de.finnp.movinghologram.v1_8_R2.listener;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import de.finnp.movinghologram.v1_8_R2.player.HologramPlayerHandler;

import java.util.UUID;

public class PlayerJoinListener implements Listener {

    public PlayerJoinListener(final Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(@NonNull final PlayerJoinEvent event) {
        @NonNull final UUID uuid = event.getPlayer().getUniqueId();
        @NonNull final HologramPlayerHandler hologramPlayerHandler = HologramPlayerHandler.getInstance();
        hologramPlayerHandler.createHologramPlayer(uuid);
    }
}
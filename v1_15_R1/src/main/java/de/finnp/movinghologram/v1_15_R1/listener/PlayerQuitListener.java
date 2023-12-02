package de.finnp.movinghologram.v1_15_R1.listener;

import de.finnp.movinghologram.v1_15_R1.player.HologramPlayerHandler;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class PlayerQuitListener implements Listener {

    public PlayerQuitListener(@NonNull final Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerQuit(@NonNull final PlayerQuitEvent event) {
        @NonNull final UUID uuid = event.getPlayer().getUniqueId();
        @NonNull final HologramPlayerHandler hologramPlayerHandler = HologramPlayerHandler.getInstance();
        hologramPlayerHandler.deleteHologramPlayer(uuid);
    }
}

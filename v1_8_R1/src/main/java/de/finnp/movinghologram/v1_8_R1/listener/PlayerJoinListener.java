package de.finnp.movinghologram.v1_8_R1.listener;

import de.finnp.movinghologram.v1_8_R1.MovingHologramPlugin;
import de.finnp.movinghologram.v1_8_R1.api.HologramApi;
import de.finnp.movinghologram.v1_8_R1.api.HologramBuilder;
import de.finnp.movinghologram.v1_8_R1.player.HologramPlayer;
import de.finnp.movinghologram.v1_8_R1.player.HologramPlayerHandler;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerJoinListener implements Listener {

    public PlayerJoinListener(final Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(@NonNull final PlayerJoinEvent event) {
        @NonNull final UUID uuid = event.getPlayer().getUniqueId();
        @NonNull final HologramPlayerHandler hologramPlayerHandler = HologramPlayerHandler.getInstance();
        @NonNull final HologramPlayer hologramPlayer = hologramPlayerHandler.createHologramPlayer(uuid);

        @NonNull final Player player = event.getPlayer();

        new HologramApi(player).sendHologram(new HologramBuilder()
                .addItemStack(new ItemStack(Material.BEACON))
                .addText("&c&lWillkommen")
                .addText("&b&lauf unserem")
                .addText("&c&lServer")
                .build(),30);
    }
}

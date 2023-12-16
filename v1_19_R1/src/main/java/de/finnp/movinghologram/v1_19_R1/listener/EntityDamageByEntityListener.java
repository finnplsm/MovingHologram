package de.finnp.movinghologram.v1_19_R1.listener;

import de.finnp.movinghologram.v1_19_R1.player.HologramPlayer;
import de.finnp.movinghologram.v1_19_R1.player.HologramPlayerHandler;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

public class EntityDamageByEntityListener implements Listener {

    public EntityDamageByEntityListener(@NonNull final Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityDamageByEntity(@NonNull final EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;
        @NonNull final HologramPlayerHandler hologramPlayerHandler = HologramPlayerHandler.getInstance();
        @NonNull final HologramPlayer damagerHologramPlayer = hologramPlayerHandler.getHologramPlayerFromUUID(event.getDamager().getUniqueId());
        @NonNull final HologramPlayer entityHologramPlayer = hologramPlayerHandler.getHologramPlayerFromUUID(event.getEntity().getUniqueId());
        damagerHologramPlayer.updateCombatTag();
        entityHologramPlayer.updateCombatTag();
    }
}

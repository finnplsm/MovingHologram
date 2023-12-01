package de.finnp.movinghologram.v1_8_R3;

import de.finnp.movinghologram.v1_8_R3.listener.EntityDamageByEntityListener;
import de.finnp.movinghologram.v1_8_R3.listener.PlayerJoinListener;
import de.finnp.movinghologram.v1_8_R3.listener.PlayerQuitListener;
import de.finnp.movinghologram.v1_8_R3.player.HologramPlayerHandler;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MovingHologramPlugin extends JavaPlugin {

    private static MovingHologramPlugin INSTANCE;

    @Override
    public void onEnable() {
        setInstance(this);
        @NonNull final HologramPlayerHandler hologramPlayerHandler = HologramPlayerHandler.getInstance();
        new EntityDamageByEntityListener(this);
        new PlayerJoinListener(this);
        new PlayerQuitListener(this);

        Bukkit.getOnlinePlayers().forEach((player) -> hologramPlayerHandler.createHologramPlayer(player.getUniqueId()));
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @NonNull
    public static MovingHologramPlugin getInstance() {
        return INSTANCE;
    }

    private static void setInstance(@NonNull final MovingHologramPlugin instance) {
        MovingHologramPlugin.INSTANCE = instance;
    }
}
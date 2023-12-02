package de.finnp.movinghologram.v1_13_R1.api;

import de.finnp.movinghologram.v1_13_R1.player.HologramPlayer;
import de.finnp.movinghologram.v1_13_R1.player.HologramPlayerHandler;
import lombok.NonNull;
import org.bukkit.entity.Player;

public class HologramApi {
    @NonNull
    private final Player player;
    @NonNull
    private final HologramPlayerHandler hologramPlayerHandler;
    @NonNull
    private final HologramPlayer hologramPlayer;


    public HologramApi(@NonNull final Player player) {
        this.player = player;
        this.hologramPlayerHandler = HologramPlayerHandler.getInstance();
        this.hologramPlayer = hologramPlayerHandler.createHologramPlayer(player.getUniqueId());
    }

    public @NonNull Player getPlayer() {
        return player;
    }

    private @NonNull HologramPlayerHandler getHologramPlayerHandler() {
        return hologramPlayerHandler;
    }

    private @NonNull HologramPlayer getHologramPlayer() {
        return hologramPlayer;
    }

    public void sendHologram(@NonNull final Object[] objects, final long seconds) {
        hologramPlayer.showHUD(objects, (20*seconds));
    }

    public void updateHologram(@NonNull final Object[] objects, final long seconds) {
        hologramPlayer.destroy();
        hologramPlayer.showHUD(objects, (20*seconds));
    }

    public void deleteHologram() {
        hologramPlayer.destroy();
    }


}

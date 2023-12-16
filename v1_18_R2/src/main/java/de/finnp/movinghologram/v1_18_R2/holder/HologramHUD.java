package de.finnp.movinghologram.v1_18_R2.holder;

import de.finnp.movinghologram.v1_18_R2.player.HologramPlayer;
import de.finnp.movinghologram.v1_18_R2.player.HologramPlayerHandler;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HologramHUD extends HologramHolder {
    private long duration = 0L;
    private final long lifetime;

    public HologramHUD(@NonNull final Location location, @NonNull final Player player, @NonNull final Object[] objects, final long lifetime) {
        super("hud-" + player.getUniqueId(), location, player, objects);
        this.lifetime = lifetime;
    }

    public long getLifetime() {
        return this.lifetime;
    }

    @Override
    public void onUpdate() {
        ++this.duration;
        if (this.duration > this.lifetime) {
            @NonNull final HologramPlayerHandler hologramPlayerHandler = HologramPlayerHandler.getInstance();
            @NonNull final HologramPlayer hologramPlayer = hologramPlayerHandler.getHologramPlayerFromUUID(this.viewer.getUniqueId());
            hologramPlayer.deleteHUD();
        } else {
            this.setLocation(this.viewer.getLocation().add(this.viewer.getEyeLocation().getDirection().normalize().multiply(3)).add(0.0D, (this.getHeight() - this.heightAverage) / 2.0D, 0.0D));
            Location location = this.viewer.getLocation().add(this.viewer.getEyeLocation().getDirection().normalize().multiply(3));

            location.add(0.0D, (this.getHeight() - this.heightAverage) / 2.0D, 0.0D);
            this.setLocation(location);
        }
    }
}

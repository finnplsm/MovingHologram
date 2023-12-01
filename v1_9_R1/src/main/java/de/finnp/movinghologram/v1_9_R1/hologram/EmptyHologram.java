package de.finnp.movinghologram.v1_9_R1.hologram;

import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class EmptyHologram extends Hologram {

    public EmptyHologram(@NonNull final Location location, @NonNull final Player player) {
        super(location, player, 0.23D);
    }

    @Override
    public void create() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void update() {
    }

    @Override
    public void move(@NonNull Location location) {
    }
}

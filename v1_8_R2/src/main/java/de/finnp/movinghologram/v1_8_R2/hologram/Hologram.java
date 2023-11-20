package de.finnp.movinghologram.v1_8_R2.hologram;

import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class Hologram {
    @NonNull
    protected Location location;
    @NonNull
    protected final Player viewer;
    protected final double height;

    public Hologram(@NonNull final Location location, @NonNull final Player player, final double height) {
        this.location = location;
        this.viewer = player;
        this.height = height;
    }

    @NonNull
    public Location getLocation() {
        return this.location;
    }

    @NonNull
    public Player getViewer() {
        return this.viewer;
    }

    public double getHeight() {
        return this.height;
    }

    public void setLocation(@NonNull final Location location) {
        this.location = location;
        this.move(location);
    }

    public abstract void create();

    public abstract void destroy();

    public abstract void update();

    public abstract void move(@NonNull final Location location);
}

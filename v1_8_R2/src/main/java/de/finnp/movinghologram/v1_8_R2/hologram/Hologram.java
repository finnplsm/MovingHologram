package de.finnp.movinghologram.v1_8_R2.hologram;

import lombok.NonNull;
import net.minecraft.server.v1_8_R2.PlayerConnection;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public abstract class Hologram {
    @NonNull
    private Location location;
    @NonNull
    private final Player viewer;
    @NonNull
    private final PlayerConnection connection;
    private final double height;


    protected Hologram(@NonNull final Location location, @NonNull final Player player, final double height) {
        this.location = location;
        this.viewer = player;
        this.connection = ((CraftPlayer) getViewer()).getHandle().playerConnection;
        this.height = height;
    }

    @NonNull
    protected Location getLocation() {
        return this.location;
    }

    @NonNull
    protected Player getViewer() {
        return this.viewer;
    }

    @NonNull
    protected PlayerConnection getConnection() {
        return connection;
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

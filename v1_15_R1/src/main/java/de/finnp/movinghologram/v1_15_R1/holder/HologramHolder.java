package de.finnp.movinghologram.v1_15_R1.holder;

import de.finnp.movinghologram.v1_15_R1.hologram.Hologram;
import de.finnp.movinghologram.v1_15_R1.hologram.ItemHologram;
import de.finnp.movinghologram.v1_15_R1.hologram.TextHologram;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class HologramHolder {
    @NonNull
    protected List<Object> rawLines = new ArrayList<>();
    @NonNull
    protected List<Hologram> holograms = new ArrayList<>();
    @NonNull
    protected final String name;
    @NonNull
    protected Location location;
    @NonNull
    protected final Player viewer;
    private final double height;
    protected final double heightAverage;

    public HologramHolder(@NonNull final String name, @NonNull final Location location, @NonNull final Player player, @NonNull final Object[] objects) {
        this.name = name;
        this.location = location;
        this.viewer = player;
        double height = 0.0D;

        @NonNull final Location subtract = this.location.clone().subtract(0.0D, height, 0.0D);

        for (@NonNull final Object object : objects) {
            if (object instanceof ItemStack) {
                @NonNull final ItemStack itemStack = (ItemStack) object;
                this.rawLines.add(((ItemStack) object).getItemMeta().getDisplayName());
                this.holograms.add(new ItemHologram(subtract.subtract(0.0D, 0.275D, 0.0D), this.viewer, itemStack));

            } else if (object instanceof String) {
                this.rawLines.add(object);
                this.holograms.add(new TextHologram(subtract.subtract(0.0D, 0.115D, 0.0D), this.viewer, ((String) object)));
            }
        }
        @NonNull Hologram hologram;
        for (@NonNull final Iterator<Hologram> iterator = this.holograms.iterator(); iterator.hasNext(); height += hologram.getHeight()) {
            hologram = iterator.next();
        }

        this.height = height;
        this.heightAverage = this.height / (double) this.holograms.size();
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    @NonNull
    public Location getLocation() {
        return this.location;
    }

    @NonNull
    public List<Hologram> getHolograms() {
        return this.holograms;
    }

    @NonNull
    public Player getViewer() {
        return this.viewer;
    }

    public double getHeight() {
        return this.height;
    }

    public void setLocation(@NonNull final Location location) {
        this.location = location.add(0.0D, this.heightAverage / 2.0D, 0.0D);
        double v = 0.0D;

        @NonNull Hologram hologram;
        for (@NonNull final Iterator<Hologram> iterator = this.holograms.iterator(); iterator.hasNext(); v += hologram.getHeight()) {
            hologram = iterator.next();
            hologram.setLocation(location.clone().subtract(0.0D, v + hologram.getHeight() / 2.0D, 0.0D));
        }

    }

    public void create() {
        for (@NonNull final Hologram hologram : this.holograms) {
            hologram.create();
        }
    }

    public void remove() {
        for (@NonNull final Hologram hologram : this.holograms) {
            hologram.destroy();
        }
    }

    public void update() {
        this.onUpdate();
        for (@NonNull final Hologram hologram : this.holograms) {
            hologram.update();
        }
    }

    public abstract void onUpdate();
}

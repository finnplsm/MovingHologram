package de.finnp.movinghologram.v1_14_R1.hologram;

import de.finnp.movinghologram.v1_14_R1.MovingHologramPlugin;
import lombok.NonNull;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.entity.Player;

public class TextHologram extends Hologram {

    @NonNull
    private final String rawLine;
    @NonNull
    private final EntityArmorStand armorStand;
    private final int id;
    @NonNull
    private final PacketPlayOutSpawnEntityLiving packetPlayOutSpawnEntityLiving;
    @NonNull
    private final PacketPlayOutEntityDestroy packetPlayOutEntityDestroy;
    private final static boolean MARKER = false;

    public TextHologram(@NonNull final Location location, @NonNull final Player player, @NonNull final String rawLine) {
        super(location, player, 0.23D);
        this.rawLine = rawLine;

        @NonNull final CraftWorld craftWorld = (CraftWorld) getLocation().getWorld();
        this.armorStand = new EntityArmorStand(craftWorld.getHandle(), getLocation().getX(), getLocation().getY(), getLocation().getZ());
        this.armorStand.setInvisible(true);
        this.armorStand.setCustomNameVisible(true);
        this.armorStand.setCustomName(IChatBaseComponent.ChatSerializer.a(" "));

        this.id = this.armorStand.getId();
        this.packetPlayOutSpawnEntityLiving = new PacketPlayOutSpawnEntityLiving(this.armorStand);
        this.packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy(this.id);
    }

    @NonNull
    public String getRawLine() {
        return this.rawLine;
    }

    private void setText(@NonNull final String text) {
        this.armorStand.setCustomName(IChatBaseComponent.ChatSerializer.a(text));
        Bukkit.getScheduler().runTask(MovingHologramPlugin.getInstance(), this::updateMetadata);
    }

    private void updateMetadata() {
        @NonNull final PacketPlayOutEntityMetadata packetPlayOutEntityMetadata = new PacketPlayOutEntityMetadata(this.id, this.armorStand.getDataWatcher(), true);
        getConnection().sendPacket(packetPlayOutEntityMetadata);
    }

    private void updateArmorStandLocation() {
        this.armorStand.setLocation(getLocation().getX(), MARKER ? getLocation().getY() + 1.25D : getLocation().getY() - 0.75D, getLocation().getZ(), getLocation().getYaw(), getLocation().getPitch());
        @NonNull final PacketPlayOutEntityTeleport packetPlayOutEntityTeleport = new PacketPlayOutEntityTeleport(this.armorStand);
        getConnection().sendPacket(packetPlayOutEntityTeleport);
    }

    @Override
    public void create() {
        getConnection().sendPacket(packetPlayOutSpawnEntityLiving);
        Bukkit.getScheduler().runTask(MovingHologramPlugin.getInstance(), this::updateMetadata);
    }

    @Override
    public void destroy() {
        getConnection().sendPacket(this.packetPlayOutEntityDestroy);
    }

    @Override
    public void update() {
        String rawLine = this.rawLine;

        if (rawLine.length() < 1) {
            rawLine = " ";
        }
        this.setText(rawLine);
    }

    @Override
    public void move(@NonNull final Location location) {
        this.updateArmorStandLocation();
    }
}

package de.finnp.movinghologram.v1_8_R1.hologram;

import lombok.NonNull;
import net.minecraft.server.v1_8_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class TextHologram extends Hologram {

    private final String rawLine;
    private final EntityArmorStand armorStand;
    private final int id;
    private final PacketPlayOutSpawnEntityLiving packetPlayOutSpawnEntityLiving;
    private final PacketPlayOutEntityDestroy packetPlayOutEntityDestroy;
    private final static boolean MARKER = false;

    public TextHologram(@NonNull final Location location, @NonNull final Player player, @NonNull final String rawLine) {
        super(location, player, 0.23D);
        this.rawLine = rawLine;

        @NonNull final CraftWorld craftWorld = (CraftWorld) this.location.getWorld();
        this.armorStand = new EntityArmorStand(craftWorld.getHandle(), this.location.getX(), this.location.getY(), this.location.getZ());
        this.armorStand.setInvisible(true);
        this.armorStand.setCustomNameVisible(true);
        this.armorStand.setCustomName("");

        this.id = this.armorStand.getId();
        this.packetPlayOutSpawnEntityLiving = new PacketPlayOutSpawnEntityLiving(this.armorStand);
        this.packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy(this.id);
    }

    @NonNull
    public String getRawLine() {
        return this.rawLine;
    }

    private void setText(@NonNull final String text) {
        this.armorStand.setCustomName(text);
        this.updateMetadata();

    }

    private void updateMetadata() {
        @NonNull final PacketPlayOutEntityMetadata packetPlayOutEntityMetadata = new PacketPlayOutEntityMetadata(this.id, this.armorStand.getDataWatcher(), true);
        @NonNull final CraftPlayer craftPlayer = (CraftPlayer) this.viewer;
        craftPlayer.getHandle().playerConnection.sendPacket(packetPlayOutEntityMetadata);
    }

    private void updateArmorStandLocation() {
        this.armorStand.setLocation(this.location.getX(), MARKER ? this.location.getY() + 1.25D : this.location.getY() - 0.75D, this.location.getZ(), this.location.getYaw(), this.location.getPitch());
        @NonNull final PacketPlayOutEntityTeleport packetPlayOutEntityTeleport = new PacketPlayOutEntityTeleport(this.armorStand);
        @NonNull final CraftPlayer craftPlayer = (CraftPlayer) this.viewer;
        craftPlayer.getHandle().playerConnection.sendPacket(packetPlayOutEntityTeleport);
    }

    @Override
    public void create() {
        @NonNull final CraftPlayer craftPlayer = (CraftPlayer) this.viewer;
        craftPlayer.getHandle().playerConnection.sendPacket(packetPlayOutSpawnEntityLiving);
        this.updateMetadata();
    }

    @Override
    public void destroy() {
        @NonNull final CraftPlayer craftPlayer = (CraftPlayer) this.viewer;
        craftPlayer.getHandle().playerConnection.sendPacket(this.packetPlayOutEntityDestroy);
    }

    @Override
    public void update() {
        String rawLine = this.rawLine;

        if (rawLine == null || rawLine.length() < 1) {
            rawLine = " ";
        }
        this.setText(rawLine);
    }

    @Override
    public void move(@NonNull final Location location) {
        this.updateArmorStandLocation();
    }
}

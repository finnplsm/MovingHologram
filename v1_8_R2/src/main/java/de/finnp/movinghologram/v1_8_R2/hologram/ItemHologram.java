package de.finnp.movinghologram.v1_8_R2.hologram;

import de.finnp.movinghologram.v1_8_R2.MovingHologramPlugin;
import lombok.NonNull;
import net.minecraft.server.v1_8_R2.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;
import java.util.logging.Logger;

public class ItemHologram extends Hologram {

    @NonNull
    private final static Logger LOGGER = MovingHologramPlugin.getInstance().getLogger();
    @NonNull
    private final net.minecraft.server.v1_8_R2.ItemStack itemStack;
    @NonNull
    private final EntityArmorStand armorStand;
    private final int id;
    @NonNull
    private final PacketPlayOutSpawnEntityLiving packetPlayOutSpawnEntityLiving;
    @NonNull
    private final PacketPlayOutEntityDestroy packetPlayOutEntityDestroy;
    private final boolean isBlock;
    private float rotation;

    public ItemHologram(@NonNull final Location location, @NonNull final Player player, @NonNull final ItemStack itemStack) {
        super(location, player, 0.55D);

        this.rotation = (new Random()).nextFloat() * 100.0F;
        this.isBlock = itemStack.getType().isBlock();

        @NonNull final CraftWorld craftWorld = (CraftWorld) this.location.getWorld();

        this.armorStand = new EntityArmorStand(craftWorld.getHandle(), this.location.getX(), this.location.getY(), this.location.getZ());
        this.armorStand.setInvisible(true);
        this.armorStand.setSmall(true);

        this.id = this.armorStand.getId();
        this.itemStack = CraftItemStack.asNMSCopy(itemStack);
        this.packetPlayOutSpawnEntityLiving = new PacketPlayOutSpawnEntityLiving(this.armorStand);
        this.packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy(this.id);
    }

    private void sendItem(@NonNull final net.minecraft.server.v1_8_R2.ItemStack itemStack) {
        @NonNull final PacketPlayOutEntityEquipment packetPlayOutEntityEquipment = new PacketPlayOutEntityEquipment(this.id, 4, itemStack);
        @NonNull final CraftPlayer craftPlayer = (CraftPlayer) this.viewer;
        craftPlayer.getHandle().playerConnection.sendPacket(packetPlayOutEntityEquipment);
        this.updateMetadata();
    }

    private void updateMetadata() {
        @NonNull final PacketPlayOutEntityMetadata packetPlayOutEntityMetadata = new PacketPlayOutEntityMetadata(this.id, this.armorStand.getDataWatcher(), true);
        @NonNull final CraftPlayer craftPlayer = (CraftPlayer) this.viewer;
        craftPlayer.getHandle().playerConnection.sendPacket(packetPlayOutEntityMetadata);
    }

    private void updateArmorStandLocation() {
        this.armorStand.setLocation(this.location.getX(), this.isBlock ? this.location.getY() + 0.75D : this.location.getY() + 0.34D, this.location.getZ(), this.rotation, this.location.getPitch());
        @NonNull final PacketPlayOutEntityTeleport packetPlayOutEntityTeleport = new PacketPlayOutEntityTeleport(this.armorStand);
        @NonNull final CraftPlayer craftPlayer = (CraftPlayer) this.viewer;
        craftPlayer.getHandle().playerConnection.sendPacket(packetPlayOutEntityTeleport);

    }

    @Override
    public void create() {
        @NonNull final CraftPlayer craftPlayer = (CraftPlayer) this.viewer;
        craftPlayer.getHandle().playerConnection.sendPacket(packetPlayOutSpawnEntityLiving);
        this.sendItem(this.itemStack);

    }

    @Override
    public void destroy() {
        @NonNull final CraftPlayer craftPlayer = (CraftPlayer) this.viewer;
        craftPlayer.getHandle().playerConnection.sendPacket(this.packetPlayOutEntityDestroy);
    }

    @Override
    public void update() {
        this.rotation = (float) ((double) this.rotation + 1.8D);
        if (this.rotation >= 180.0F) {
            this.rotation = -180.0F;
        }
    }

    @Override
    public void move(@NonNull Location location) {
        this.updateArmorStandLocation();
    }
}

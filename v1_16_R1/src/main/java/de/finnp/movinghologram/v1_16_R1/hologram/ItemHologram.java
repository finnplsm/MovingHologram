package de.finnp.movinghologram.v1_16_R1.hologram;

import com.mojang.datafixers.util.Pair;
import de.finnp.movinghologram.v1_16_R1.MovingHologramPlugin;
import lombok.NonNull;
import net.minecraft.server.v1_16_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class ItemHologram extends Hologram {

    @NonNull
    private final static Logger LOGGER = MovingHologramPlugin.getInstance().getLogger();
    @NonNull
    private final net.minecraft.server.v1_16_R1.ItemStack itemStack;
    @NonNull
    private final static net.minecraft.server.v1_16_R1.ItemStack ITEM_STACK_AIR = CraftItemStack.asNMSCopy(new ItemStack(Material.AIR));
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

        @NonNull final CraftWorld craftWorld = (CraftWorld) getLocation().getWorld();

        this.armorStand = new EntityArmorStand(craftWorld.getHandle(), getLocation().getX(), getLocation().getY(), getLocation().getZ());
        this.armorStand.setInvisible(true);
        this.armorStand.setSmall(true);

        this.id = this.armorStand.getId();
        this.itemStack = CraftItemStack.asNMSCopy(itemStack);

        this.packetPlayOutSpawnEntityLiving = new PacketPlayOutSpawnEntityLiving(this.armorStand);
        this.packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy(this.id);
    }

    private void sendItem(@NonNull final net.minecraft.server.v1_16_R1.ItemStack itemStack) {
        @NonNull final List<Pair<EnumItemSlot, net.minecraft.server.v1_16_R1.ItemStack>> list = Arrays.asList(
                Pair.of(EnumItemSlot.MAINHAND, ITEM_STACK_AIR),
                Pair.of(EnumItemSlot.OFFHAND, ITEM_STACK_AIR),
                Pair.of(EnumItemSlot.FEET, ITEM_STACK_AIR),
                Pair.of(EnumItemSlot.LEGS, ITEM_STACK_AIR),
                Pair.of(EnumItemSlot.CHEST, ITEM_STACK_AIR),
                Pair.of(EnumItemSlot.HEAD, itemStack));
        @NonNull final PacketPlayOutEntityEquipment packetPlayOutEntityEquipment = new PacketPlayOutEntityEquipment(this.id, list);
        getConnection().sendPacket(packetPlayOutEntityEquipment);
        Bukkit.getScheduler().runTask(MovingHologramPlugin.getInstance(), this::updateMetadata);
    }

    private void updateMetadata() {
        @NonNull final PacketPlayOutEntityMetadata packetPlayOutEntityMetadata = new PacketPlayOutEntityMetadata(this.id, this.armorStand.getDataWatcher(), true);
        getConnection().sendPacket(packetPlayOutEntityMetadata);
    }

    private void updateArmorStandLocation() {
        this.armorStand.setLocation(getLocation().getX(), this.isBlock ? getLocation().getY() + 0.75D : getLocation().getY() + 0.34D, getLocation().getZ(), this.rotation, getLocation().getPitch());
        @NonNull final PacketPlayOutEntityTeleport packetPlayOutEntityTeleport = new PacketPlayOutEntityTeleport(this.armorStand);
        getConnection().sendPacket(packetPlayOutEntityTeleport);

    }

    @Override
    public void create() {
        getConnection().sendPacket(packetPlayOutSpawnEntityLiving);
        this.sendItem(this.itemStack);
    }

    @Override
    public void destroy() {
        getConnection().sendPacket(this.packetPlayOutEntityDestroy);
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

package de.finnp.movinghologram.v1_19_R1.hologram;

import com.mojang.datafixers.util.Pair;
import de.finnp.movinghologram.v1_19_R1.MovingHologramPlugin;
import lombok.NonNull;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
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
    private final net.minecraft.world.item.ItemStack itemStack;
    @NonNull
    private static final net.minecraft.world.item.ItemStack ITEM_STACK_AIR = CraftItemStack.asNMSCopy(new ItemStack(Material.AIR));
    @NonNull
    private final EntityArmorStand armorStand;
    private final int id;
    @NonNull
    private final PacketPlayOutSpawnEntity packetPlayOutSpawnEntity;
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
        this.armorStand.j(true);
        this.armorStand.t(true);

        this.id = this.armorStand.ae();
        this.itemStack = CraftItemStack.asNMSCopy(itemStack);

        this.packetPlayOutSpawnEntity = new PacketPlayOutSpawnEntity(this.armorStand, 78);
        this.packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy(this.id);
    }

    private void sendItem(@NonNull final net.minecraft.world.item.ItemStack itemStack) {
        @NonNull final List<Pair<EnumItemSlot, net.minecraft.world.item.ItemStack>> list = Arrays.asList(
                Pair.of(EnumItemSlot.a, ITEM_STACK_AIR),
                Pair.of(EnumItemSlot.b, ITEM_STACK_AIR),
                Pair.of(EnumItemSlot.c, ITEM_STACK_AIR),
                Pair.of(EnumItemSlot.d, ITEM_STACK_AIR),
                Pair.of(EnumItemSlot.e, ITEM_STACK_AIR),
                Pair.of(EnumItemSlot.f, itemStack));
        @NonNull final PacketPlayOutEntityEquipment packetPlayOutEntityEquipment = new PacketPlayOutEntityEquipment(this.id, list);
        getConnection().a(packetPlayOutEntityEquipment);
        Bukkit.getScheduler().runTask(MovingHologramPlugin.getInstance(), this::updateMetadata);
    }

    private void updateMetadata() {
        @NonNull final DataWatcher dataWatcher = this.armorStand.ai();
        @NonNull final PacketPlayOutEntityMetadata packetPlayOutEntityMetadata = new PacketPlayOutEntityMetadata(this.id, dataWatcher, true);
        getConnection().a(packetPlayOutEntityMetadata);
    }

    private void updateArmorStandLocation() {
        this.armorStand.a(getLocation().getX(), this.isBlock ? getLocation().getY() + 0.75D : getLocation().getY() + 0.34D, getLocation().getZ(), this.rotation, getLocation().getPitch());
        @NonNull final PacketPlayOutEntityTeleport packetPlayOutEntityTeleport = new PacketPlayOutEntityTeleport(this.armorStand);
        getConnection().a(packetPlayOutEntityTeleport);

    }

    @Override
    public void create() {
        getConnection().a(packetPlayOutSpawnEntity);
        this.sendItem(this.itemStack);
    }

    @Override
    public void destroy() {
        getConnection().a(this.packetPlayOutEntityDestroy);
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

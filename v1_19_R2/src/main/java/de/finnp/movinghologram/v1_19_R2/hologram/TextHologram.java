package de.finnp.movinghologram.v1_19_R2.hologram;

import de.finnp.movinghologram.v1_19_R2.MovingHologramPlugin;
import lombok.NonNull;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;
import org.bukkit.entity.Player;

public class TextHologram extends Hologram {

    @NonNull
    private final String rawLine;
    @NonNull
    private final EntityArmorStand armorStand;
    private final int id;
    @NonNull
    private final PacketPlayOutSpawnEntity packetPlayOutSpawnEntity;
    @NonNull
    private final PacketPlayOutEntityDestroy packetPlayOutEntityDestroy;
    private final static boolean MARKER = false;

    public TextHologram(@NonNull final Location location, @NonNull final Player player, @NonNull final String rawLine) {
        super(location, player, 0.23D);
        this.rawLine = rawLine;

        @NonNull final CraftWorld craftWorld = (CraftWorld) getLocation().getWorld();
        this.armorStand = new EntityArmorStand(craftWorld.getHandle(), getLocation().getX(), getLocation().getY(), getLocation().getZ());

        this.armorStand.j(true);
        this.armorStand.n(true);
        this.armorStand.t(true);
        this.armorStand.b(IChatBaseComponent.ChatSerializer.a(" "));

        this.id = this.armorStand.ah();
        this.packetPlayOutSpawnEntity = new PacketPlayOutSpawnEntity(this.armorStand, 78);
        this.packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy(this.id);
    }

    @NonNull
    public String getRawLine() {
        return this.rawLine;
    }

    private void setText(@NonNull final String text) {
        this.armorStand.b(IChatBaseComponent.ChatSerializer.a(text));
        Bukkit.getScheduler().runTask(MovingHologramPlugin.getInstance(), this::updateMetadata);
    }

    private void updateMetadata() {
        @NonNull final DataWatcher dataWatcher = this.armorStand.al();
        @NonNull final PacketPlayOutEntityMetadata packetPlayOutEntityMetadata = new PacketPlayOutEntityMetadata(this.id, dataWatcher.b());
        getConnection().a(packetPlayOutEntityMetadata);
    }

    private void updateArmorStandLocation() {
        this.armorStand.a(getLocation().getX(), MARKER ? getLocation().getY() + 1.25D : getLocation().getY() - 0.75D, getLocation().getZ(), getLocation().getYaw(), getLocation().getPitch());
        @NonNull final PacketPlayOutEntityTeleport packetPlayOutEntityTeleport = new PacketPlayOutEntityTeleport(this.armorStand);
        getConnection().a(packetPlayOutEntityTeleport);
    }

    @Override
    public void create() {
        getConnection().a(packetPlayOutSpawnEntity);
        Bukkit.getScheduler().runTask(MovingHologramPlugin.getInstance(), this::updateMetadata);
    }

    @Override
    public void destroy() {
        getConnection().a(this.packetPlayOutEntityDestroy);
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

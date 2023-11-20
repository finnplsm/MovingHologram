package de.finnp.movinghologram.v1_8_R2.player;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import de.finnp.movinghologram.v1_8_R2.MovingHologramPlugin;
import de.finnp.movinghologram.v1_8_R2.holder.HologramHUD;

import java.util.UUID;

public class HologramPlayer {
    @NonNull
    private final UUID uuid;
    private HologramHUD hologramHUD;
    @NonNull
    private final BukkitTask hologramUpdateTask;
    private long lastTag = System.currentTimeMillis() - (long) (10 * 1000) - 1L;

    public HologramPlayer(@NonNull final UUID uuid) {
        this.uuid = uuid;

        this.hologramUpdateTask = (new BukkitRunnable() {
            public void run() {
                if (HologramPlayer.this.hologramHUD != null) {
                    HologramPlayer.this.hologramHUD.update();
                }
            }
        }).runTaskTimerAsynchronously(MovingHologramPlugin.getInstance(), 0L, 1L);
    }

    private long getLastTag() {
        return lastTag;
    }

    private void setLastTag(final long lastTag) {
        this.lastTag = lastTag;
    }

    public void showHUD(@NonNull final Object[] objects, long duration) {
        this.deleteHUD();
        if (duration == -1L) {
            duration = Long.MAX_VALUE;
        }
        @NonNull final Player player = Bukkit.getPlayer(this.uuid);

        assert player != null;

        this.hologramHUD = new HologramHUD(player.getLocation(), player, objects, duration);
        Bukkit.getScheduler().runTaskAsynchronously(MovingHologramPlugin.getInstance(), () -> this.hologramHUD.create());
    }

    public void deleteHUD() {
        if (this.hasHUD()) {

            @NonNull final Player player = Bukkit.getPlayer(this.uuid);

            assert player != null;

            this.hologramHUD.remove();
            this.hologramHUD = null;
        }

    }

    public boolean hasHUD() {
        return this.hologramHUD != null;
    }

    public void clearHolograms() {
        this.deleteHUD();
    }

    public void destroy() {
        this.clearHolograms();
        this.hologramUpdateTask.cancel();
    }

    public void updateCombatTag() {
        setLastTag(System.currentTimeMillis());
    }
}

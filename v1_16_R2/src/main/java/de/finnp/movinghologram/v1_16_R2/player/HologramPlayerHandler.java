package de.finnp.movinghologram.v1_16_R2.player;

import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HologramPlayerHandler {
    private static HologramPlayerHandler INSTANCE;
    @NonNull
    private final Map<UUID, HologramPlayer> hologramPlayerMap = new HashMap<UUID, HologramPlayer>();

    public static HologramPlayerHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HologramPlayerHandler();
        }
        return INSTANCE;
    }

    @NonNull
    public HologramPlayer createHologramPlayer(@NonNull final UUID uuid) {
        if (!this.hologramPlayerMap.containsKey(uuid)) {
            @NonNull
            final HologramPlayer hologramPlayer = new HologramPlayer(uuid);
            this.hologramPlayerMap.put(uuid, hologramPlayer);
            return hologramPlayer;
        } else {
            return this.hologramPlayerMap.get(uuid);
        }
    }

    public void deleteHologramPlayer(@NonNull final UUID uuid) {
        if (this.existsHologramPlayer(uuid)) {
            @NonNull
            final HologramPlayer hologramPlayer = this.getHologramPlayerFromUUID(uuid);
            hologramPlayer.destroy();
        }
        this.hologramPlayerMap.remove(uuid);
    }

    @NonNull
    public HologramPlayer getHologramPlayerFromUUID(@NonNull final UUID uuid) {
        return this.hologramPlayerMap.get(uuid);
    }

    public boolean existsHologramPlayer(@NonNull final UUID uuid) {
        return this.hologramPlayerMap.containsKey(uuid);
    }
}

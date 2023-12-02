package de.finnp.movinghologram.v1_19_R2.api;

import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class HologramBuilder {
    @NonNull private final ArrayList<Object> objects;

    public HologramBuilder() {
        this.objects = new ArrayList<>();
    }

    public HologramBuilder addText(@NonNull final String text){
        objects.add(ChatColor.translateAlternateColorCodes('&', text));
        return this;
    }

    public HologramBuilder addItemStack(@NonNull final ItemStack itemStack){
        objects.add(itemStack);
        return this;
    }

    public @NonNull Object[] build(){
        return objects.toArray();
    }
}

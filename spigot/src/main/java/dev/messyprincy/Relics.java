package dev.messyprincy;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class Relics extends JavaPlugin {
    public static Relics INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        getLogger().info("Loaded Meßy's Relics");
    }

    public static NamespacedKey key(String path) {
        return new NamespacedKey(INSTANCE, path);
    }
}
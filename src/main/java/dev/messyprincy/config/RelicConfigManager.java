package dev.messyprincy.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class RelicConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("relics.json");

    private static RelicConfig config;

    public static RelicConfig get() {
        if (config == null) {
            load();
        }
        return config;
    }

    public static void load() {
        if (!Files.exists(CONFIG_PATH)) {
            config = new RelicConfig();
            save();
            return;
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
            config = GSON.fromJson(reader, RelicConfig.class);
        } catch (IOException e) {
            config = new RelicConfig();
        }
    }

    public static void save() {
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

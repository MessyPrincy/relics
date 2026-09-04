package dev.messyprincy.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.messyprincy.Relics;
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
            RelicConfig loaded = GSON.fromJson(reader, RelicConfig.class);
            if (isPercentValid(loaded)) {
                config = loaded;
            } else {
                Relics.LOGGER.error("Invalid relics.json. Tiers do not sum up to 100%. Falling back to defaults");
                config = new RelicConfig();
            }
        } catch (IOException e) {
            config = new RelicConfig();
        }
    }

    public static void save() {
        if (!isPercentValid(config)) {
            Relics.LOGGER.error("Invalid relics.json. Tiers do not sum up to 100%. Refusing to save.");
            return;
        }

        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isPercentValid(RelicConfig testedConfig) {
        if (testedConfig.tiers == null) {
            return false;
        }

        int needed_percent = 100;

        int config_percent = testedConfig.tiers.values().stream()
                .mapToInt(Integer::intValue)
                .sum();

        return needed_percent == config_percent;
    }
}

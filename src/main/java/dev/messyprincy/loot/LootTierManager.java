package dev.messyprincy.loot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class LootTierManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static Path pathFor (String tier) {
        return FabricLoader.getInstance()
                .getConfigDir()
                .resolve("relics")
                .resolve(tier + ".json");
    }

    public static LootTierData load(String tier) {
        Path path = pathFor(tier);

        if (!Files.exists(path)) {
            LootTierData fresh = new LootTierData();
            save(tier, fresh);
            return fresh;
        }

        try (Reader reader = Files.newBufferedReader(path)) {
            return GSON.fromJson(reader, LootTierData.class);
        } catch (IOException e) {
            return new LootTierData();
        }
    }

    public static void save(String tier, LootTierData data) {
        Path path = pathFor(tier);
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(data, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

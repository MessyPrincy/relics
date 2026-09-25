package dev.messyprincy.spawn;

import dev.messyprincy.Relics;
import dev.messyprincy.config.RelicConfig;
import dev.messyprincy.config.RelicConfigManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;


public class RelicSpawner {
    private static int ticksUntilSpawn;
    private static List<ResourceKey<Level>> validDimensions;

    public static void initialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            validateDimensions(server, RelicConfigManager.get());
            resetTimer(server);
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (!(ticksUntilSpawn <= 0)) {
                ticksUntilSpawn--;
                return;
            }

            resetTimer(server);
            trySpawn(server);
        });
    }

    public static void resetTimer(MinecraftServer server) {
        RelicConfig config = RelicConfigManager.get();

        ticksUntilSpawn = (config.spawnIntervalSeconds * 20) / Math.max(1, getValidPlayers(server).size());
    }

    private static List<ServerPlayer> getValidPlayers (MinecraftServer server) {
        return PlayerLookup.all(server)
                .stream()
                .filter(serverPlayer -> {
                    ResourceKey<Level> playerLevel = serverPlayer.level().dimension();

                    return validDimensions.contains(playerLevel);
                })
                .filter(serverPlayer -> !serverPlayer.isSpectator())
                .toList();
    }

    private static void trySpawn(MinecraftServer server) {
        if (validDimensions.isEmpty()) {
            Relics.LOGGER.error("No valid dimensions, skipping relic spawn");
            return;
        }

        List<ServerPlayer> validPlayers = getValidPlayers(server);
        if (validPlayers.isEmpty()) {
            Relics.LOGGER.error("No valid players, skipping relic spawn");
            return;
        }

        Relics.LOGGER.info("Spawn relic");
    }

    private static boolean validateDimension(MinecraftServer server, ResourceKey<Level> key) {
        if (server.getLevel(key) == null) {
            Relics.LOGGER.warn("Dimension {} does not exist in the server", key.toString());
            return false;
        }

        return true;
    }

    public static void validateDimensions(MinecraftServer server, RelicConfig config) {
        validDimensions = new ArrayList<>();
        for (String dimension : config.allowedDimensions) {
            ResourceLocation id = ResourceLocation.tryParse(dimension);

            ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, id);
            if (validateDimension(server, key)) {
                validDimensions.add(key);
            }
        }
    }
}

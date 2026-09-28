package dev.messyprincy.spawn;

import dev.messyprincy.Relics;
import dev.messyprincy.block.ModBlocks;
import dev.messyprincy.config.RelicConfig;
import dev.messyprincy.config.RelicConfigManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;


public class RelicSpawner {
    private static final Random RANDOM = new Random();
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

        RelicConfig config = RelicConfigManager.get();
        ServerPlayer validPlayer = validPlayers.get(RANDOM.nextInt(validPlayers.size()));
        ServerLevel level = validPlayer.serverLevel();

        Optional<BlockPos> relicPos = SpawnPositionFinder.find(level, validPlayer.blockPosition(), config.spawnRadiusMin, config.spawnRadiusMax);
        if (relicPos.isEmpty()) {
            // Maybe remove after testing is done
            Relics.LOGGER.error("No suitable location found near {}, skipping spawn", validPlayer.getDisplayName());
            return;
        }

        level.setBlock(relicPos.get(), ModBlocks.RELIC.defaultBlockState(), 3);
        // Remove after testing is done
        Relics.LOGGER.info("Relic spawned at {}", relicPos.get());
        level.scheduleTick(relicPos.get(), ModBlocks.RELIC, config.relicLifeTimeSeconds * 20);
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

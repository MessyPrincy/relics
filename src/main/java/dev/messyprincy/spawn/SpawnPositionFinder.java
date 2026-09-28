package dev.messyprincy.spawn;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Optional;
import java.util.Random;

public class SpawnPositionFinder {
    private static final Random RANDOM = new Random();
    private static final int MAX_ATTEMPT = 10;

    public static Optional<BlockPos> find(ServerLevel level, BlockPos center, int minRadius, int maxRadius) {
        for (int i = 0; i < MAX_ATTEMPT ; i++) {
            BlockPos candidate = randomPointNear(center, minRadius, maxRadius);

            if (!level.hasChunk(candidate.getX() >> 4, candidate.getZ() >> 4)) {
                continue;
            }

            BlockPos surfacePos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, candidate);

            if (isValid(level, surfacePos)) {
                return Optional.of(surfacePos);
            }
        }

        return Optional.empty();
    }

    private static BlockPos randomPointNear(BlockPos center, int minRadius, int maxRadius) {
        double angle = RANDOM.nextDouble() * 2 * Math.PI;
        double distance = minRadius + RANDOM.nextDouble() * (maxRadius - minRadius);

        double x = center.getX() + distance * Math.cos(angle);
        double z = center.getZ() + distance * Math.sin(angle);

        return BlockPos.containing(x, center.getY(), z);
    }

    private static boolean isValid(ServerLevel level, BlockPos pos) {
        if (!level.getBlockState(pos).isAir()) {
            return false;
        }

        if (!level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP)) {
            return false;
        }

        if (!level.getWorldBorder().isWithinBounds(pos)) {
            return false;
        }

        return true;
    }
}

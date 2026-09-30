package dev.messyprincy.loot;

import com.google.gson.JsonElement;
import dev.messyprincy.Relics;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.stream.IntStream;

public class LootRoller {
    private static final RandomSource RANDOM = RandomSource.createNewThreadLocalInstance();

    private static LootTierData rollBestTier(Map<String, Integer> tiers, int charge) {
        if (charge < 0) {
            throw new IllegalArgumentException("Charge cannot be lower than 0");
        }

        String tier = IntStream.range(0, (charge + 1))
                .mapToObj(i -> rollTier(tiers))
                .min(Comparator.comparingInt(tiers::get))
                .orElseThrow();

        return LootTierManager.load(tier);
    }

    private static String rollTier(Map<String, Integer> tiers) {
        int roll = RANDOM.nextInt(100);
        int cumulative = 0;

        for (var entry : tiers.entrySet()) {
            cumulative += entry.getValue();
            if (roll < cumulative) {
                return entry.getKey();
            }
        }

        throw new IllegalStateException("Tiers do not add up to 100%");
    }

    public static ItemStack rollItem(RegistryOps<JsonElement> registryOps, Map<String, Integer> tiers, int charge) {
        LootTierData tierData = rollBestTier(tiers, charge);

        if (tierData == null || tierData.items == null || tierData.items.isEmpty()) {
            return ItemStack.EMPTY;
        }

        JsonElement chosen = tierData.items.get(RANDOM.nextInt(tierData.items.size()));

        return ItemStack.CODEC.parse(registryOps, chosen)
                .resultOrPartial(err -> Relics.LOGGER.error("Failed to decode loot item: {}", err))
                .orElse(ItemStack.EMPTY);
    }
}


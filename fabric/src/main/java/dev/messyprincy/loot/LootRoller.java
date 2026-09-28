package dev.messyprincy.loot;

import com.google.gson.JsonElement;
import dev.messyprincy.Relics;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.stream.IntStream;

public class LootRoller {
    private static final Random RANDOM = new Random();

    private static LootTierData rollBestTier(Map<String, Integer> tiers, int grade) {
        if (grade < 0) {
            throw new IllegalArgumentException("Grade cannot be lower than 0");
        }

        String tier = IntStream.range(0, grade++)
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

    public static ItemStack rollItem(RegistryOps<JsonElement> registryOps, Map<String, Integer> tiers, int grade) {
        List<JsonElement> items = rollBestTier(tiers, grade).items;

        if (items.isEmpty()) {
            return ItemStack.EMPTY;
        }

        JsonElement chosen = items.get(RANDOM.nextInt(items.size()));

        return ItemStack.CODEC.parse(registryOps, chosen)
                .resultOrPartial(err -> Relics.LOGGER.error("Failed to decode loot item: {}", err))
                .orElse(ItemStack.EMPTY);
    }
}


package dev.messyprincy.loot;

import dev.messyprincy.config.RelicConfig;
import dev.messyprincy.config.RelicConfigManager;
import dev.messyprincy.item.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.HashSet;
import java.util.Set;

public class LootInjector {
    private static Set<ResourceKey<LootTable>> hostileMobTables = null;
    private static final Set<ResourceLocation> OMINOUS_VAULT_TABLES = Set.of(
            ResourceLocation.parse("minecraft:chests/trial_chambers/reward_ominous_common"),
            ResourceLocation.parse("minecraft:chests/trial_chambers/reward_ominous_rare"),
            ResourceLocation.parse("minecraft:chests/trial_chambers/reward_ominous_unique")
    );

    public static void initialize() {
        buildHostileMobTableSet();
        LootTableEvents.MODIFY.register((key, builder, source, registries) -> {
            if (isHostileMobTable(key)) {
                injectVoidTrace(builder);
            }
            if (isOminousVaultTable(key)) {
                injectRelicKey(builder);
            }
        });
    }

    private static void buildHostileMobTableSet() {
        hostileMobTables = new HashSet<>();
        for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
            if (type.getCategory() == MobCategory.MONSTER) {
                hostileMobTables.add(type.getDefaultLootTable());
            }
        }
    }

    private static boolean isHostileMobTable(ResourceKey<LootTable> key) {
        if (hostileMobTables == null) {
            buildHostileMobTableSet();
        }
        return hostileMobTables.contains(key);
    }

    private static boolean isOminousVaultTable(ResourceKey<LootTable> key) {
        return OMINOUS_VAULT_TABLES.contains(key.location());
    }

    private static void injectRelicKey(LootTable.Builder builder) {
        RelicConfig config = RelicConfigManager.get();
        if (!config.enableVaultLoot) {
            return;
        }
        float chance = (float) Math.clamp(config.relicKeyChance, 0.0, 1.0);

        LootPool.Builder pool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0f))
                .when(LootItemRandomChanceCondition.randomChance(chance))
                .add(LootItem.lootTableItem(ModItems.RELIC_KEY));

        builder.withPool(pool);
    }

    private static void injectVoidTrace(LootTable.Builder builder) {
        RelicConfig config = RelicConfigManager.get();
        if (!config.enableMobLoot) {
            return;
        }
        float chance = (float) Math.clamp(config.voidTraceChance, 0.0, 1.0);

        LootPool.Builder pool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0f))
                .when(LootItemKilledByPlayerCondition.killedByPlayer())
                .when(LootItemRandomChanceCondition.randomChance(chance))
                .add(LootItem.lootTableItem(ModItems.VOID_TRACE));

        builder.withPool(pool);
    }
}

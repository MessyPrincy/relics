package dev.messyprincy.block;

import dev.messyprincy.registry.ModCreativeTabs;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.List;

import static dev.messyprincy.Relics.MOD_ID;

public class ModBlocks {
    public static final List<Block> ALL_BLOCKS = new ArrayList<>();

    public static Block register(Block block, String name, boolean shouldRegisterItem) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(MOD_ID, name);

        if (shouldRegisterItem) {
            BlockItem relicItem = new BlockItem(block, new Item.Properties());
            Registry.register(BuiltInRegistries.ITEM, id, relicItem);
        }

        ALL_BLOCKS.add(block);
        return Registry.register(BuiltInRegistries.BLOCK, id, block);
    }

    public static final Block RELIC = register(
            new RelicBlock(BlockBehaviour.Properties.of().sound(SoundType.AMETHYST)),
            "relic",
            true
    );

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(ModCreativeTabs.RELIC_GROUP_KEY)
                .register((itemGroup) -> {
                    for (Block block : ALL_BLOCKS) {
                        itemGroup.accept(block.asItem());
                    }
                });
    }
}

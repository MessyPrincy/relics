package dev.messyprincy.registry;

import dev.messyprincy.block.ModBlocks;
import dev.messyprincy.item.ModItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import static dev.messyprincy.Relics.MOD_ID;

public class ModCreativeTabs {
    public static final ResourceKey<CreativeModeTab> RELIC_GROUP_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "relics_tab")
    );
    public static final CreativeModeTab RELICS_TAB = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "relics_tab"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ModBlocks.RELIC.asItem()))
                    .title(Component.translatable("creativetab.relics"))
                    .build()
    );

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(RELIC_GROUP_KEY)
                .register((itemGroup) -> {
                    for (var block : ModBlocks.ALL_BLOCKS) {
                        itemGroup.accept(block.asItem());
                    }
                    for (var item : ModItems.ALL_ITEMS) {
                        itemGroup.accept(item);
                    }
                });
    }
}

package dev.messyprincy.items;

import dev.messyprincy.ModCreativeTabs;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;


import java.util.ArrayList;
import java.util.List;

import static dev.messyprincy.Relics.MOD_ID;

public class ModItems {
    public static final List<Item> ALL_ITEMS = new ArrayList<>();

    public static Item register(Item item, String id) {
        ResourceLocation itemID = ResourceLocation.fromNamespaceAndPath(MOD_ID, id);

        Item registeredItem = Registry.register(BuiltInRegistries.ITEM, itemID, item);

        ALL_ITEMS.add(registeredItem);
        return registeredItem;
    }

    public static final Item RELIC_KEY = register(
            new Item(new Item.Properties()),
            "relic_key"
    );

    public static final Item VOID_TRACE = register(
            new Item(new Item.Properties()),
            "void_trace"
    );

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(ModCreativeTabs.RELIC_GROUP_KEY)
                .register((itemGroup) -> {
                    for (Item item : ALL_ITEMS) {
                        itemGroup.accept(item);
                    }
                });
    }
}

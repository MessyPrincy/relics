package dev.messyprincy;

import dev.messyprincy.block.ModBlocks;
import dev.messyprincy.command.RelicCommands;
import dev.messyprincy.config.RelicConfigManager;
import dev.messyprincy.item.ModItems;
import dev.messyprincy.registry.ModCreativeTabs;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.resources.ResourceLocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Relics implements ModInitializer {
	public static final String MOD_ID = "relics";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		RelicConfigManager.load();
		ModBlocks.initialize();
		ModItems.initialize();
		ModCreativeTabs.initialize();

		CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
			RelicCommands.register(dispatcher);
		}));

		LOGGER.info("Hello Fabric world!");
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}

package dev.messyprincy.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.serialization.JsonOps;
import dev.messyprincy.config.RelicConfigManager;
import dev.messyprincy.loot.LootTierData;
import dev.messyprincy.loot.LootTierManager;
import net.minecraft.resources.RegistryOps;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class RelicCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("relic")
                        .then(Commands.literal("reload")
                                .requires(source -> source.hasPermission(2))
                                .executes(ctx -> {
                                    RelicConfigManager.load();
                                    ctx.getSource().sendSuccess(
                                            () -> Component.literal("Relic config reloaded."),
                                            true
                                    );
                                    return 1;
                                })
                        )
                        .then(Commands.literal("add")
                                .requires(source -> source.hasPermission(2))
                                .then(Commands.argument("tier", StringArgumentType.word())
                                        .executes(ctx -> {
                                            String tier = StringArgumentType.getString(ctx, "tier").toLowerCase();
                                            var player = ctx.getSource().getPlayerOrException();
                                            ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);

                                            if (heldItem.isEmpty()) {
                                                ctx.getSource().sendFailure(Component.literal("You must hold an item to add it."));
                                                return 0;
                                            }

                                            if (tier.equals("bronze") || tier.equals("silver") || tier.equals("gold")) {
                                                LootTierData data = LootTierManager.load(tier);

                                                var registryOps = RegistryOps.create(
                                                        JsonOps.INSTANCE,
                                                        ctx.getSource().registryAccess()
                                                );

                                                var result = ItemStack.CODEC.encodeStart(registryOps, heldItem);

                                                var encoded = result.resultOrPartial(err ->
                                                        ctx.getSource().sendFailure(Component.literal("Failed to encode item: " + err))
                                                );

                                                if (encoded.isEmpty()) {
                                                    return 0;
                                                }

                                                data.items.add(encoded.get());
                                                LootTierManager.save(tier, data);
                                                ctx.getSource().sendSuccess(() -> Component.literal("Added item to " + tier + " loot."), true);
                                                return 1;
                                            } else {
                                                ctx.getSource().sendFailure(Component.literal("Invalid tier. Use bronze, silver, or gold."));
                                                return 0;
                                            }
                                        })
                                )
                        )
        );
    }
}
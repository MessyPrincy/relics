package dev.messyprincy.blocks;

import dev.messyprincy.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

public class RelicBlock extends Block {
    public static final IntegerProperty CHARGE = IntegerProperty.create("charge", 0, 4);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CHARGE);
    }

    public RelicBlock(Properties settings) {
        super(settings);

        registerDefaultState(defaultBlockState().setValue(CHARGE, 0));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack item, BlockState state, Level level, BlockPos blockPos,
                                          Player player, InteractionHand hand, BlockHitResult hitResult) {
        int charge = state.getValue(CHARGE);

        if (item.is(ModItems.VOID_TRACE)) {
            if (charge >= 4) {
                return ItemInteractionResult.FAIL;
            }

            if (!level.isClientSide) {
                level.setBlock(blockPos, state.setValue(CHARGE, charge + 1), 3);
                level.playSound(null, blockPos, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, 1.0F, 1.0F);

                if (!player.getAbilities().instabuild) {
                    item.shrink(1);
                }
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (item.is(ModItems.RELIC_KEY)) {
            if (level instanceof ServerLevel serverLevel) {
                dropLoot(serverLevel, blockPos, charge, player);
                level.removeBlock(blockPos, false);

                if (!player.getAbilities().instabuild) {
                    item.shrink(1);
                }

            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.useItemOn(item, state, level, blockPos, player, hand, hitResult);
    }

    private void dropLoot(ServerLevel level, BlockPos blockPos, int charge, Player player) {

        Block.popResource(level, blockPos, new ItemStack(net.minecraft.world.item.Items.DIRT));
    }
}

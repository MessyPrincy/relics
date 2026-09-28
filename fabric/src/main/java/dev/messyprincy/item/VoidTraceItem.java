package dev.messyprincy.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class VoidTraceItem extends Item {
    public VoidTraceItem(Properties settings) {
        super(settings);
    }

    @Override
    public void appendHoverText(ItemStack item, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        tooltip.add(Component.translatable("itemTooltip.relics.void_trace").withStyle(ChatFormatting.DARK_PURPLE));
    }
}
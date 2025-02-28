package com.petrolpark.destroy.content.processing.discstamping;

import java.util.List;

import javax.annotation.Nullable;

import com.petrolpark.destroy.DestroyItems;
import com.petrolpark.destroy.core.item.WithSecondaryItem;
import com.tterrag.registrate.util.nullness.NonnullType;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class DiscStamperItem extends WithSecondaryItem {

    public DiscStamperItem(@NonnullType Properties properties) {
        super(properties, DiscStamperItem::getDisc);
    };

    public static ItemStack getDisc(ItemStack stamper) {
        if (stamper.getOrCreateTag().contains("Disc", Tag.TAG_COMPOUND)) {
            return ItemStack.of(stamper.getOrCreateTag().getCompound("Disc"));
        };
        return ItemStack.EMPTY;
    };

    public static ItemStack of(ItemStack discStack) {
        ItemStack stack = DestroyItems.DISC_STAMPER.asStack();
        stack.getOrCreateTag().put("Disc", discStack.save(new CompoundTag()));
        return stack;
    };

    @Nullable
    public static RecordItem getDiscItem(ItemStack stamper) {
        ItemStack stack = getDisc(stamper);
        if (stack.getItem() instanceof RecordItem record) return record;
        return null;
    };

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        RecordItem item = getDiscItem(stack);
        if (item != null) {
            tooltip.add(Component.translatable(item.getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
        };
    };

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return itemStack;
    };
    
};

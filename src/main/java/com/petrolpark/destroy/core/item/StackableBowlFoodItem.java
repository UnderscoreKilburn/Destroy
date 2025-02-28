package com.petrolpark.destroy.core.item;

import java.util.stream.Stream;

import com.petrolpark.destroy.MoveToPetrolparkLibrary;
import com.petrolpark.util.ItemHelper;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

@MoveToPetrolparkLibrary
public class StackableBowlFoodItem extends Item {

    public StackableBowlFoodItem(Properties properties) {
        super(properties);
    };

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        ItemHelper.give(livingEntity, Stream.of(new ItemStack(Items.BOWL)));
        return super.finishUsingItem(stack, level, livingEntity);
    };
    
};

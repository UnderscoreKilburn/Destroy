package com.petrolpark.destroy.content.product;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ChewingGumItem extends Item {

    public ChewingGumItem(Properties properties) {
        super(properties);
    };

    @Override
    public int getUseDuration(ItemStack stack) {
        return 36000; // Half an hour
    };
    
};

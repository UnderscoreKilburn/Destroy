package com.petrolpark.destroy.content.product.periodictable;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;

public class TankPeriodicTableBlockItemColor implements ItemColor {

    public static final TankPeriodicTableBlockItemColor INSTANCE = new TankPeriodicTableBlockItemColor();

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        if (tintIndex != 0) return -1;
        if (!(stack.getItem() instanceof TankPeriodicTableBlockItem item)) return -1;
        return item.getColor();
    };
    
};

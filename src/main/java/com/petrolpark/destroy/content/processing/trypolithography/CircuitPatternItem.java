package com.petrolpark.destroy.content.processing.trypolithography;

import com.petrolpark.compat.create.item.directional.IDirectionalOnBelt;
import com.petrolpark.util.NBTHelper;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CircuitPatternItem extends Item implements IDirectionalOnBelt {

    public CircuitPatternItem(Properties properties) {
        super(properties);
    };

    public static int getPattern(ItemStack stack) {
        if (!(stack.getItem() instanceof CircuitPatternItem || stack.getItem() instanceof SequencedAssemblyItem)) return 0;
        return NBTHelper.readBinaryMatrix4x4(stack.getOrCreateTag(), "Pattern");
    };

    public static void putPattern(ItemStack stack, int pattern) {
        if (stack.getItem() instanceof CircuitPatternItem || stack.getItem() instanceof SequencedAssemblyItem) NBTHelper.writeBinaryMatrix4x4(stack.getOrCreateTag(), "Pattern", pattern);
    };
    
};

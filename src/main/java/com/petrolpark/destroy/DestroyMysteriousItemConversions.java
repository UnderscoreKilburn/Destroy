package com.petrolpark.destroy;

import com.petrolpark.destroy.config.DestroySubstancesConfigs;
import com.simibubi.create.AllItems;
import com.simibubi.create.compat.jei.ConversionRecipe;
import com.simibubi.create.compat.jei.category.MysteriousItemConversionCategory;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class DestroyMysteriousItemConversions {

    public static final void addAll() {
        MysteriousItemConversionCategory.RECIPES.add(ConversionRecipe.create(AllItems.EMPTY_BLAZE_BURNER.asStack(), DestroyBlocks.COOLER.asStack()));
        if (DestroySubstancesConfigs.iodineDragonsBreath()) MysteriousItemConversionCategory.RECIPES.add(ConversionRecipe.create(DestroyItems.IODINE.asStack(), new ItemStack(Items.DRAGON_BREATH)));
        MysteriousItemConversionCategory.RECIPES.add(ConversionRecipe.create(DestroyItems.BUCKET_AND_SPADE.asStack(), DestroyItems.TEAR_BOTTLE.asStack()));
        MysteriousItemConversionCategory.RECIPES.add(ConversionRecipe.create(DestroyItems.MOLTEN_STAINLESS_STEEL_BUCKET.asStack(), DestroyBlocks.STAINLESS_STEEL_BLOCK.asStack()));
    };
};

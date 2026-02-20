package com.petrolpark.destroy.datagen.recipe;

import com.petrolpark.destroy.DestroyItems;
import com.petrolpark.destroy.DestroyRecipeTypes;
import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public class DestroyChargingRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe
    REDSTONE = convert(AllItems.CINDER_FLOUR, Items.REDSTONE),
    REDSTONE_TORCH = convert(Items.TORCH, Items.REDSTONE_TORCH),
    VOLTAIC_PILE = convert(DestroyItems.DISCHARGED_VOLTAIC_PILE, DestroyItems.VOLTAIC_PILE);

    public GeneratedRecipe convert(ItemLike from, ItemLike to) {
        return create(() -> from, b -> b.output(to));
    }

    public DestroyChargingRecipeGen(PackOutput output, String defaultNamespace) {
        super(output, defaultNamespace);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return DestroyRecipeTypes.CHARGING;
    }
}

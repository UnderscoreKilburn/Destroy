package com.petrolpark.destroy.datagen.recipe;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyItems;
import com.simibubi.create.api.data.recipe.WashingRecipeGen;
import net.minecraft.data.PackOutput;

public class DestroyWashingRecipeGen extends WashingRecipeGen {

    GeneratedRecipe
    RUINED_CIRCUIT_BOARD = create(DestroyItems.RUINED_CIRCUIT_BOARD::get, b -> b.output(DestroyItems.RUINED_CIRCUIT_MASK).output(DestroyItems.COPPER_POWDER)),
    SODIUM_WASHING = create(DestroyItems.SODIUM_INGOT::get, b -> b.output(DestroyItems.OXIDIZED_SODIUM_INGOT));

    public DestroyWashingRecipeGen(PackOutput output) {
        super(output, Destroy.MOD_ID);
    }
}

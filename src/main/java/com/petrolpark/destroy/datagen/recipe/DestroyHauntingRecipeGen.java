package com.petrolpark.destroy.datagen.recipe;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyItems;
import com.simibubi.create.api.data.recipe.HauntingRecipeGen;
import net.minecraft.data.PackOutput;

public class DestroyHauntingRecipeGen extends HauntingRecipeGen {

    GeneratedRecipe
    MAGIC_REDUCTANT = convert(DestroyItems.MAGIC_OXIDANT, DestroyItems.MAGIC_REDUCTANT),
    SPECTRUM = convert(DestroyItems.BLANK_MUSIC_DISC, DestroyItems.MUSIC_DISC_SPECTRUM)
    ;

    public DestroyHauntingRecipeGen(PackOutput output) {
        super(output, Destroy.MOD_ID);
    }
}

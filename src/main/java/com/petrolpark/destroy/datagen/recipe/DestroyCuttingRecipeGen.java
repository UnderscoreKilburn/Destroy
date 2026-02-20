package com.petrolpark.destroy.datagen.recipe;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyItems;
import com.simibubi.create.api.data.recipe.CuttingRecipeGen;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;

public class DestroyCuttingRecipeGen extends CuttingRecipeGen {

    GeneratedRecipe
    SODIUM_INGOT = create(DestroyItems.OXIDIZED_SODIUM_INGOT::get, b -> b.duration(50).output(0.1f, DestroyItems.SODIUM_INGOT)),
    CONFETTI = create(() -> Items.PAPER, b -> b.duration(10).output(DestroyItems.WHITE_CONFETTI, 8))
    ;

    public DestroyCuttingRecipeGen(PackOutput output) {
        super(output, Destroy.MOD_ID);
    }
}

package com.petrolpark.destroy.datagen.recipe;

import com.petrolpark.destroy.DestroyFluids;
import com.petrolpark.destroy.DestroyRecipeTypes;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.Tags;

public class DestroyCentrifugationRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe
    MILK = create("milk", b -> b
        .require(Tags.Fluids.MILK, 1000)
        .output(DestroyFluids.CREAM.get(), 500)
        .output(DestroyFluids.SKIMMED_MILK.get(), 500)
        .duration(500));

    public DestroyCentrifugationRecipeGen(PackOutput output, String defaultNamespace) {
        super(output, defaultNamespace);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return DestroyRecipeTypes.CENTRIFUGATION;
    }
}

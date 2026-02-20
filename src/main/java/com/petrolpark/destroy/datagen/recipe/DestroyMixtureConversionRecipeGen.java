package com.petrolpark.destroy.datagen.recipe;

import com.petrolpark.destroy.DestroyRecipeTypes;
import com.petrolpark.destroy.chemistry.legacy.LegacyMixture;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.chemistry.minecraft.MixtureFluid;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.material.Fluids;

public class DestroyMixtureConversionRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe
    WATER = create("water", b -> b
        .require(Fluids.WATER, 1)
        .output(MixtureFluid.of(1, LegacyMixture.pure(DestroyMolecules.WATER))))
    ;

    public DestroyMixtureConversionRecipeGen(PackOutput output, String defaultNamespace) {
        super(output, defaultNamespace);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return DestroyRecipeTypes.MIXTURE_CONVERSION;
    }
}

package com.petrolpark.destroy.datagen.recipe;

import com.petrolpark.destroy.DestroyRecipeTypes;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.core.recipe.ingredient.fluid.MoleculeTagFluidIngredient;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;

public class DestroyFlameRetardantApplicationRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe MIXTURE = create("mixture", b -> b.require(MoleculeTagFluidIngredient.of(DestroyMolecules.Tags.FLAME_RETARDANT, 1.f, 3.f, 1)));

    public DestroyFlameRetardantApplicationRecipeGen(PackOutput output, String defaultNamespace) {
        super(output, defaultNamespace);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return DestroyRecipeTypes.FLAME_RETARDANT_APPLICATION;
    }
}

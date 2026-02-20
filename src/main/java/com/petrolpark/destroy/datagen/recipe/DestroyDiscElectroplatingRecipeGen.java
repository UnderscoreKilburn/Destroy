package com.petrolpark.destroy.datagen.recipe;

import com.petrolpark.destroy.DestroyRecipeTypes;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.core.recipe.ingredient.fluid.IonFluidIngredient;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;

public class DestroyDiscElectroplatingRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe DISC_ELECTROPLATING = create("disc_electroplating", b -> b.require(IonFluidIngredient.of(DestroyMolecules.NICKEL_ION, 1.f, 10)));

    public DestroyDiscElectroplatingRecipeGen(PackOutput output, String defaultNamespace) {
        super(output, defaultNamespace);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return DestroyRecipeTypes.DISC_ELECTROPLATING;
    }
}

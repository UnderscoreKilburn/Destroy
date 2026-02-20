package com.petrolpark.destroy.datagen.recipe;

import com.petrolpark.destroy.DestroyItems;
import com.petrolpark.destroy.DestroyRecipeTypes;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.core.chemistry.NamedMixtures;
import com.petrolpark.destroy.core.recipe.ingredient.fluid.IonFluidIngredient;
import com.petrolpark.destroy.core.recipe.ingredient.fluid.MoleculeFluidIngredient;
import com.petrolpark.destroy.core.recipe.ingredient.fluid.SaltFluidIngredient;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;

public class DestroyElectrolysisRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe
    CASTNER_PROCESS = create("castner_process", b -> b
        .require(MoleculeFluidIngredient.of(DestroyMolecules.MERCURY, 250))
        .require(SaltFluidIngredient.of(DestroyMolecules.SODIUM_ION, DestroyMolecules.CHLORIDE, 1.0f, 250))
        // todo: just merge the two outputs into one? having two separate outputs makes things unnecessarily complicated
        .output(NamedMixtures.SODIUM_AMALGAM.get(255))
        .output(NamedMixtures.CHLORINE_SOLUTION.get(245))
    ),
    CHROMIUM = create("chromium_electrolysis", b -> b
        .require(IonFluidIngredient.of(DestroyMolecules.CHROMIUM_III, 0.4f, 25))
        .output(DestroyItems.CHROMIUM_POWDER)
    ),
    COPPER = create("copper_electrolysis", b -> b
        .require(IonFluidIngredient.of(DestroyMolecules.COPPER_II, 0.4f, 25))
        .output(DestroyItems.COPPER_POWDER)
    ),
    IRON = create("iron_electrolysis", b -> b
        .require(IonFluidIngredient.of(DestroyMolecules.IRON_III, 0.4f, 25))
        .output(DestroyItems.IRON_POWDER)
    ),
    LEAD = create("lead_electrolysis", b -> b
        .require(IonFluidIngredient.of(DestroyMolecules.LEAD_II, 0.4f, 25))
        .output(DestroyItems.LEAD_POWDER)
    ),
    NICKEL = create("nickel_electrolysis", b -> b
        .require(IonFluidIngredient.of(DestroyMolecules.NICKEL_ION, 0.4f, 25))
        .output(DestroyItems.NICKEL_POWDER)
    ),
    ZINC = create("zinc_electrolysis", b -> b
        .require(IonFluidIngredient.of(DestroyMolecules.ZINC_ION, 0.4f, 25))
        .output(DestroyItems.ZINC_POWDER)
    );

    public DestroyElectrolysisRecipeGen(PackOutput output, String defaultNamespace) {
        super(output, defaultNamespace);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return DestroyRecipeTypes.ELECTROLYSIS;
    }
}

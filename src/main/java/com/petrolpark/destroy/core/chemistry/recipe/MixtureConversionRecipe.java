package com.petrolpark.destroy.core.chemistry.recipe;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyFluids;
import com.petrolpark.destroy.DestroyRecipeTypes;
import com.petrolpark.destroy.core.recipe.SingleFluidRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;

import net.minecraftforge.fluids.FluidStack;

public class MixtureConversionRecipe extends SingleFluidRecipe {

    public MixtureConversionRecipe(ProcessingRecipeParams params) {
        super(DestroyRecipeTypes.MIXTURE_CONVERSION, params);
        if (
            getFluidIngredients().size() != 1
            || getFluidResults().size() != 1
            || getFluidIngredients().get(0).getRequiredAmount() != 1
            || getFluidResults().get(0).getAmount() != 1
            || !DestroyFluids.isMixture(getFluidResults().get(0))
        )
        Destroy.LOGGER.warn("Mixture conversion recipes must define a single input and (Mixture) output which each have an amount of 1");
    };

    /**
     * The check to see whether the Fluid Stack which is being given actually applies to the recipe should be done beforehand.
     * @param nonMixtureStack
     * @return A Mixture Fluid Stack
     */
    public FluidStack apply(FluidStack nonMixtureStack) {
        FluidStack result = getFluidResults().get(0).copy();
        result.setAmount(nonMixtureStack.getAmount());
        return result;
    };

    @Override
    protected int getMaxFluidOutputCount() {
        return 1;
    };

    @Override
    public String getRecipeTypeName() {
        return "Mixture Conversion";
    };
    
};

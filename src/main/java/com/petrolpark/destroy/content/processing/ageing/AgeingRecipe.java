package com.petrolpark.destroy.content.processing.ageing;

import com.petrolpark.destroy.DestroyRecipeTypes;
import com.petrolpark.destroy.core.recipe.SingleFluidRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;

import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class AgeingRecipe extends SingleFluidRecipe {

    public AgeingRecipe(ProcessingRecipeParams params) {
        super(DestroyRecipeTypes.AGING, params);
    };

    @Override
    protected int getMaxInputCount() {
        return 2;
    };

    @Override
    protected int getMaxOutputCount() {
        return 0;
    };

    @Override
    protected int getMaxFluidOutputCount() {
        return 1;
    };

    @Override
    public boolean matches(RecipeWrapper pContainer, Level level) {
        return false;
    };

    @Override
    public String getRecipeTypeName() {
        return "aging";
    };
    
}

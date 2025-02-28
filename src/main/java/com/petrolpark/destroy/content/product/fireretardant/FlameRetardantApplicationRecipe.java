package com.petrolpark.destroy.content.product.fireretardant;

import com.petrolpark.destroy.DestroyRecipeTypes;
import com.petrolpark.destroy.core.recipe.SingleFluidRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;

import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class FlameRetardantApplicationRecipe extends SingleFluidRecipe {

    public FlameRetardantApplicationRecipe(ProcessingRecipeParams params) {
        super(DestroyRecipeTypes.FLAME_RETARDANT_APPLICATION, params);
    };

    @Override
    public boolean matches(RecipeWrapper container, Level level) {
        return true;
    };

    @Override
    protected int getMaxInputCount() {
        return 0;
    };

    @Override
    protected int getMaxOutputCount() {
        return 0;
    };

    @Override
    protected int getMaxFluidOutputCount() {
        return 1;
    }

    @Override
    public String getRecipeTypeName() {
        return "flame retardant application";
    };


    
};

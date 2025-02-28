package com.petrolpark.destroy.content.processing.dynamo;

import com.petrolpark.destroy.DestroyRecipeTypes;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;

public class ElectrolysisRecipe extends BasinRecipe {

    public ElectrolysisRecipe(ProcessingRecipeParams params) {
        super(DestroyRecipeTypes.ELECTROLYSIS, params);
        if (processingDuration == 0) processingDuration = 200;
    };
    
};

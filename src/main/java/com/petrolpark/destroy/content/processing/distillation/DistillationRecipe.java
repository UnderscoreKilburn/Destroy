package com.petrolpark.destroy.content.processing.distillation;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.petrolpark.destroy.DestroyRecipeTypes;
import com.petrolpark.destroy.core.recipe.SingleFluidRecipe;
import com.petrolpark.recipe.advancedprocessing.IBiomeSpecificProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;

public class DistillationRecipe extends SingleFluidRecipe implements IBiomeSpecificProcessingRecipe {

    private Set<BiomeValue> biomes = new HashSet<>();

    public DistillationRecipe(ProcessingRecipeParams params) {
        super(DestroyRecipeTypes.DISTILLATION, params);
    };

    /**
     * The number of fractions this Recipe produces
     */
    public int getFractions() {
        return getFluidResults().size();
    };

    @Override
    protected int getMaxFluidOutputCount() {
        return 7;
    };

    @Override
    protected boolean canRequireHeat() {
        return true;
    };

    @Override
    protected boolean canSpecifyDuration() {
        return false;
    };

    @Override
    public String getRecipeTypeName() {
        return "distillation";
    }

    @Override
    public void setAllowedBiomes(Set<BiomeValue> biomes) {
        this.biomes = ImmutableSet.copyOf(biomes);
    };

    @Override
    public Set<BiomeValue> getAllowedBiomes() {
        return biomes;
    };
    
}

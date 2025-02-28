package com.petrolpark.destroy.content.processing.centrifuge;

import java.util.Set;
import java.util.HashSet;

import com.ibm.icu.impl.locale.XCldrStub.ImmutableSet;
import com.petrolpark.destroy.DestroyRecipeTypes;
import com.petrolpark.destroy.core.recipe.SingleFluidRecipe;
import com.petrolpark.recipe.advancedprocessing.IBiomeSpecificProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;

import net.minecraftforge.fluids.FluidStack;

public class CentrifugationRecipe extends SingleFluidRecipe implements IBiomeSpecificProcessingRecipe {
    
    private Set<BiomeValue> biomes = new HashSet<>();

    public CentrifugationRecipe(ProcessingRecipeParams params) {
        super(DestroyRecipeTypes.CENTRIFUGATION, params);
    };

    public FluidStack getDenseOutputFluid() {
        checkForValidOutputs();
        return fluidResults.get(0);
    };

    public FluidStack getLightOutputFluid() {
        checkForValidOutputs();
        return fluidResults.get(1);
    };

    private void checkForValidOutputs() {
        if (fluidResults.isEmpty() || fluidResults.size() != 2) throw new IllegalStateException("Centrifugation Recipe " + id + " contains the wrong number of output fluids.");
    };

    @Override
    public String getRecipeTypeName() {
        return "Centrifugation";
    }

    @Override
    public void setAllowedBiomes(Set<BiomeValue> biomes) {
        biomes = ImmutableSet.copyOf(biomes);
    };

    @Override
    public Set<BiomeValue> getAllowedBiomes() {
        return biomes;
    };
    
}

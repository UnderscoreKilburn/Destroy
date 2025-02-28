package com.petrolpark.destroy.content.processing.sieve;

import com.petrolpark.destroy.DestroyRecipeTypes;
import com.petrolpark.recipe.advancedprocessing.firsttimelucky.IFirstTimeLuckyRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;

import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class SievingRecipe extends ProcessingRecipe<RecipeWrapper> implements IFirstTimeLuckyRecipe<SievingRecipe> {

    protected boolean firstTimeLucky;
    
    public SievingRecipe(ProcessingRecipeParams params) {
        super(DestroyRecipeTypes.SIEVING, params);
    };

    @Override
    public boolean matches(RecipeWrapper inv, Level level) {
        if (inv.isEmpty()) return false;
		return ingredients.get(0).test(inv.getItem(0));
    };

    @Override
    protected int getMaxInputCount() {
        return 1;
    };

    @Override
    protected int getMaxOutputCount() {
        return 16;
    };

    @Override
    protected boolean canSpecifyDuration() {
        return true;
    };

    @Override
    public SievingRecipe getAsRecipe() {
        return this;
    };

    @Override
    public boolean shouldBeLuckyFirstTime() {
        return firstTimeLucky;
    };

    @Override
    public void setLuckyFirstTime(boolean lucky) {
        firstTimeLucky = lucky;
    };
    
};

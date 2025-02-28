package com.petrolpark.destroy.content.product.periodictable;

import com.google.gson.JsonSyntaxException;
import com.petrolpark.destroy.DestroyRecipeTypes;
import com.petrolpark.destroy.core.recipe.SingleFluidRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class ElementTankFillingRecipe extends SingleFluidRecipe {

    public final Block blockResult;

    public ElementTankFillingRecipe(ProcessingRecipeParams params) {
        super(DestroyRecipeTypes.ELEMENT_TANK_FILLING, params);
        ItemStack result = getRollableResultsAsItemStacks().get(0);
        if (result.getItem() instanceof BlockItem blockItem) {
            blockResult = blockItem.getBlock();
        } else {
            throw new JsonSyntaxException("Element Tank Filling recipes must give a block");
        };
    };

    @Override
    public boolean matches(RecipeWrapper pContainer, Level pLevel) {
        return true;
    };

    @Override
    protected int getMaxInputCount() {
        return 0;
    };

    @Override
    protected int getMaxOutputCount() {
        return 1;
    };

    @Override
    protected int getMaxFluidOutputCount() {
        return 0;
    };

    @Override
    protected boolean canSpecifyDuration() {
        return false;
    }

    @Override
    public String getRecipeTypeName() {
        return "element tank filling";
    };
    
};

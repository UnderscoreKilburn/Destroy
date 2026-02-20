package com.petrolpark.destroy.content.processing.treetap;

import com.google.gson.JsonObject;
import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyRecipeTypes;
import com.petrolpark.destroy.chemistry.legacy.LegacyMixture;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.chemistry.minecraft.MixtureFluid;
import com.petrolpark.destroy.core.chemistry.vat.material.VatMaterial;
import com.petrolpark.destroy.core.recipe.ingredient.BlockIngredient;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TappingRecipe extends ProcessingRecipe<RecipeWrapper> {

    protected BlockIngredient blockIngredient;

    public TappingRecipe(ProcessingRecipeParams params) {
        super(DestroyRecipeTypes.TAPPING, params);
    };

    public BlockIngredient getBlock() {return blockIngredient;}
    public void setBlock(BlockIngredient block) {blockIngredient = block;}

    @Override
    public boolean matches(RecipeWrapper pContainer, Level pLevel) {
        return false;
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
    };

    @Override
    public void readAdditional(JsonObject json) {
        blockIngredient = BlockIngredient.fromJson(GsonHelper.getNonNull(json, "blocks"));
    }

    @Override
    public void readAdditional(FriendlyByteBuf buffer) {
        blockIngredient = BlockIngredient.fromNetwork(buffer);
    }

    @Override
    public void writeAdditional(JsonObject json) {
        json.add("blocks", blockIngredient.toJson());
    }

    @Override
    public void writeAdditional(FriendlyByteBuf buffer) {
        blockIngredient.toNetwork(buffer);
    }
};

package com.petrolpark.destroy.content.processing.treetap;

import com.petrolpark.destroy.DestroyRecipeTypes;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.data.PackOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fluids.FluidStack;


public class DestroyTappingRecipeGen extends ProcessingRecipeGen {


    GeneratedRecipe
    JUNGLE_LOG = tap(FluidHelper.copyStackWithAmount(TappingRecipe.LATEX_FLUID, 10), Blocks.STRIPPED_JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_WOOD);

    public GeneratedRecipe tap(FluidStack fluid, Block... blocks) {
        ProcessingRecipeSerializer<TappingRecipe> serializer = getSerializer();
        GeneratedRecipe generatedRecipe = c -> {
            TappingRecipe.Builder builder = new TappingRecipe.Builder(
                serializer.getFactory(),
                ResourceLocation.fromNamespaceAndPath(modid, CatnipServices.REGISTRIES.getKeyOrThrow(blocks[0]).getPath())
            );
            builder.block(blocks).output(fluid).build(c);
        };

        all.add(generatedRecipe);
        return generatedRecipe;
    }

    public DestroyTappingRecipeGen(PackOutput output, String defaultNamespace) {
        super(output, defaultNamespace);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return DestroyRecipeTypes.TAPPING;
    }

}

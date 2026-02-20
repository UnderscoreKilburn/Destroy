package com.petrolpark.destroy.datagen.recipe;

import com.petrolpark.destroy.DestroyItems;
import com.petrolpark.destroy.DestroyRecipeTypes;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

public class DestroyObliterationRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe
    NANODIAMONDS = convert(Blocks.COAL_BLOCK, DestroyItems.NANODIAMONDS, 0.1f);

    public GeneratedRecipe convert(ItemLike from, ItemLike to, float chance) {
        return create(() -> from, b -> b.output(chance, to));
    }

    public DestroyObliterationRecipeGen(PackOutput output, String defaultNamespace) {
        super(output, defaultNamespace);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return DestroyRecipeTypes.OBLITERATION;
    }
}

package com.petrolpark.destroy.datagen.recipe;

import com.petrolpark.destroy.DestroyFluids;
import com.petrolpark.destroy.DestroyItems;
import com.petrolpark.destroy.DestroyRecipeTypes;
import com.petrolpark.destroy.datagen.recipe.DestroyRecipeProvider.I;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;

public class DestroyArcFurnaceRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe
    CALCIUM_CARBIDE = create("calcium_carbide", b -> b
        .require(Items.CHARCOAL)
        .require(I.lime())
        .output(DestroyItems.CALCIUM_CARBIDE)),
    STAINLESS_STEEL = create("stainless_steel", b -> b
        .require(Items.CHARCOAL)
        .require(I.ironDust())
        .require(I.ironDust())
        .require(I.ironDust())
        .require(I.ironDust())
        .require(I.ironDust())
        .require(I.nickelDust())
        .require(I.nickelDust())
        .require(I.chromiumDust())
        .output(DestroyFluids.MOLTEN_STAINLESS_STEEL.get(), 1000)
        .duration(102400)),
    STAINLESS_STEEL_FLUXED = create("stainless_steel_fluxed", b -> b
            .require(Items.CHARCOAL)
            .require(I.ironDust())
            .require(I.ironDust())
            .require(I.ironDust())
            .require(I.ironDust())
            .require(I.ironDust())
            .require(I.nickelDust())
            .require(I.nickelDust())
            .require(I.chromiumDust())
            .require(I.fluxes())
            .output(DestroyFluids.MOLTEN_STAINLESS_STEEL.get(), 1000)
            .output(DestroyItems.SLAG)
            .duration(25600));

    public DestroyArcFurnaceRecipeGen(PackOutput output, String defaultNamespace) {
        super(output, defaultNamespace);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return DestroyRecipeTypes.ARC_FURNACE;
    }
}
